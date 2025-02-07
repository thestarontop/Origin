package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.modules.misc.NameProtect;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.StringUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(PlayerTabOverlay.class)
public abstract class MixinPlayerTabOverlay {
    @Shadow public abstract Component getNameForDisplay(PlayerInfo arg);

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;", ordinal=0))
    public List<FormattedCharSequence> k(Font font, FormattedText formattedText, int n) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nameprotect").isEnabled()) {
            return font.split(new TextComponent(StringUtils.replace(formattedText.getString(),NameProtect.name,ColorUtils.makeColour("Hidden"))), n);
        }
        return font.split(formattedText,n);
    }
    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/Font;split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;", ordinal=1))
    public List<FormattedCharSequence> w(Font font, FormattedText formattedText, int n) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nameprotect").isEnabled()) {
            return font.split(new TextComponent(StringUtils.replace(formattedText.getString(),NameProtect.name,ColorUtils.makeColour("Hidden"))), n);
        }
        return font.split(formattedText,n);
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/components/PlayerTabOverlay;getNameForDisplay(Lnet/minecraft/client/multiplayer/PlayerInfo;)Lnet/minecraft/network/chat/Component;"))
    public Component o(PlayerTabOverlay playerTabOverlay, PlayerInfo playerInfo) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("nameprotect").isEnabled() && getNameForDisplay(playerInfo).getString().contains(NameProtect.name)) {
            return new TextComponent(StringUtils.replace(getNameForDisplay(playerInfo).getString(),NameProtect.name,ColorUtils.makeColour("Hidden")));
        }
        return getNameForDisplay(playerInfo);
    }
}
