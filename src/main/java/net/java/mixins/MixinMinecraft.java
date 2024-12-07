package net.java.mixins;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.java.main.event.events.ClientStartEvent;
import net.java.main.event.events.ScreenEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.event.events.TickEvent;
import net.java.main.event.events.WorldEvent;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.VirtualScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.client.resources.PaintingTextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.ModCheck;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow private int rightClickDelay;


    @Shadow @Final private static Logger LOGGER;

    @Shadow private Thread gameThread;

    @Shadow @Nullable public ClientLevel level;

    @Shadow @Nullable public LocalPlayer player;

    @Shadow @Nullable public Screen screen;

    @Shadow @Final public MouseHandler mouseHandler;

    @Shadow @Final private Window window;

    @Shadow public boolean noRender;

    @Shadow @Final private SoundManager soundManager;

    @Shadow public abstract void updateTitle();

    @Shadow @Final private ModelManager modelManager;

    @Shadow @Final private FontManager fontManager;

    @Shadow @Final public GameRenderer gameRenderer;

    @Shadow @Final public LevelRenderer levelRenderer;

    @Shadow @Final private PackRepository resourcePackRepository;

    @Shadow @Final public ParticleEngine particleEngine;

    @Shadow @Final private MobEffectTextureManager mobEffectTextures;

    @Shadow @Final private PaintingTextureManager paintingTextures;

    @Shadow @Final public TextureManager textureManager;

    @Shadow @Final private ReloadableResourceManager resourceManager;

    @Shadow @Final private VirtualScreen virtualScreen;



    @Shadow @Nullable public abstract ClientPacketListener getConnection();

    @Shadow @Nullable private IntegratedServer singleplayerServer;

    @Shadow public abstract boolean isConnectedToRealms();

    @Shadow @Nullable private ServerData currentServer;

    @Shadow
    public static ModCheck checkModStatus() {
        return null;
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;profiler:Lnet/minecraft/util/profiling/ProfilerFiller;",ordinal = 5,shift = At.Shift.BEFORE))
    private void runtick(CallbackInfo ci) {
        TickEvent event = new TickEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }

    @Inject(method = "loadLevel", at = @At("HEAD"))
    public void loadLevel(String string, CallbackInfo ci) {
        WorldEvent event = new WorldEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }

    @Inject(method = "startUseItem", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;rightClickDelay:I", shift = At.Shift.AFTER))
    private void useItem(CallbackInfo ci) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("FastPlace").isEnabled())
            this.rightClickDelay = 0;
    }

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void setScreen(@Nullable Screen arg) {
        if (SharedConstants.IS_RUNNING_IN_IDE && Thread.currentThread() != this.gameThread) {
            LOGGER.error("setScreen called from non-game thread");
        }

        if (arg == null && this.level == null) {
            arg = new TitleScreen();
        } else if (arg == null && this.player.isDeadOrDying()) {
            if (this.player.shouldShowDeathScreen()) {
                arg = new DeathScreen((Component)null, this.level.getLevelData().isHardcore());
            } else {
                this.player.respawn();
            }
        }

        ForgeHooksClient.clearGuiLayers((Minecraft)(Object) this);
        Screen old = this.screen;
        ScreenOpenEvent event = new ScreenOpenEvent((Screen)arg);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            Screen arg1 = event.getScreen();
            if (old != null && arg1 != old) {
                old.removed();
            }
            madebystarontopandfml.getInstance().getEventManager().call(new ScreenEvent(arg1));
            this.screen = arg1;
            BufferUploader.reset();
            if (arg1 != null) {
                Minecraft.getInstance().options.keyUse.setDown(false);
                this.mouseHandler.releaseMouse();
                arg1.init((Minecraft)(Object)this, this.window.getGuiScaledWidth(), this.window.getGuiScaledHeight());
                this.noRender = false;
            } else {
                this.soundManager.resume();
                this.mouseHandler.grabMouse();
            }
            this.updateTitle();
        }
    }
    @Inject(method="<init>",at=@At("TAIL"))
    public void onInit(CallbackInfo ci){
        madebystarontopandfml.getInstance().getEventManager().call(new ClientStartEvent());
    }
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    private String createTitle() {
        StringBuilder stringbuilder = new StringBuilder(madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION + "---");
        if (checkModStatus().shouldReportAsModified()) {
            stringbuilder.append("*");
        }

        stringbuilder.append(" ");
        stringbuilder.append(SharedConstants.getCurrentVersion().getName());
        ClientPacketListener clientpacketlistener = this.getConnection();
        if (clientpacketlistener != null && clientpacketlistener.getConnection().isConnected()) {
            stringbuilder.append(" - ");
            if (this.singleplayerServer != null && !this.singleplayerServer.isPublished()) {
                stringbuilder.append(I18n.get("title.singleplayer"));
            } else if (this.isConnectedToRealms()) {
                stringbuilder.append(I18n.get("title.multiplayer.realms"));
            } else if (this.singleplayerServer == null && (this.currentServer == null || !this.currentServer.isLan())) {
                stringbuilder.append(I18n.get("title.multiplayer.other"));
            } else {
                stringbuilder.append(I18n.get("title.multiplayer.lan"));
            }
        }

        return stringbuilder.toString();
    }

}
