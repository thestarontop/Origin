package net.java.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.misc.NameProtect;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.StringUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {

    @Inject(method={"displayScoreboardSidebar"}, at={@At(value="RETURN")})
    public void f(PoseStack poseStack, Objective objective, CallbackInfo callbackInfo) {
        poseStack.last();
    }

    @Redirect(method={"displayScoreboardSidebar"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Font;draw(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/lang/String;FFI)I"))
    public int c(Font font, PoseStack poseStack, String string, float f, float f2, int n) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled()){
            string = StringUtils.replace(string, NameProtect.name, ColorUtils.makeColour("Hidden"));
        }
        return font.draw(poseStack, string, f, f2, n);
    }

    @Redirect(method={"displayScoreboardSidebar"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/scores/PlayerTeam;formatNameForTeam(Lnet/minecraft/world/scores/Team;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/network/chat/MutableComponent;"))
    public MutableComponent l(Team team, Component component) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled()){
            component = new TextComponent(StringUtils.replace(component.getString(), NameProtect.name, ColorUtils.makeColour("Hidden")));
        }
        return (MutableComponent)component;
    }

    @Redirect(method={"displayScoreboardSidebar"}, at=@At(value="INVOKE", target="Lnet/minecraft/world/scores/Objective;getDisplayName()Lnet/minecraft/network/chat/Component;"))
    public Component d(Objective objective) {
        Component component = objective.getDisplayName();
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled()){
            component = new TextComponent(StringUtils.replace(component.getString(), NameProtect.name, ColorUtils.makeColour("Hidden")));
        }
        return component;
    }
}
