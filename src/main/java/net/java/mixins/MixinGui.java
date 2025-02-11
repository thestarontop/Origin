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

    @Inject(method = "displayScoreboardSidebar",at=@At("HEAD"))
    protected void displayScoreboardSidebar(PoseStack arg, Objective arg2, CallbackInfo ci) {
        String s = arg2.getDisplayName().getString();
        if(madebystarontopandfml.getInstance().getModuleManager().getModule("NameProtect").isEnabled() && s.contains(NameProtect.name)){
            s = StringUtils.replace(s,NameProtect.name,ColorUtils.makeColour("Hidden"));
           arg2.setDisplayName(new TextComponent(s).setStyle(arg2.getDisplayName().getStyle()));
        }
    }
}
