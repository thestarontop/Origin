package net.java.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.modules.client.HUD;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class MixinChatScreen extends Screen {
    protected MixinChatScreen() {
        super(null);
    }
    @Inject(method = "mouseClicked", at = @At("HEAD"))
    public void mouseClicked(double p_94737_, double p_94738_, int p_94739_, CallbackInfoReturnable<Boolean> cir) {
        HUD.handleMouseClick(p_94737_, p_94738_, p_94739_);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(PoseStack p_95595_, int p_95596_, int p_95597_, float p_95598_, CallbackInfo ci) {
        HUD.realMouseX = p_95596_;
        HUD.realMouseY = p_95597_;
    }

    @Override
    public boolean mouseReleased(double p_94737_, double p_94738_, int p_94739_) {
        HUD.handleMouseReleased(p_94737_, p_94738_, p_94739_);
        return false;
    }
}
