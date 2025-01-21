package net.java.mixins;

import net.java.main.event.events.TextEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class MixinFont {
    @ModifyVariable(
            method = "drawInternal(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZIIZ)I",
            at = @At("HEAD"),
            ordinal = 0
    )
    private String onDrawInternal(String text) {
        if (text == null || madebystarontopandfml.eventManager == null) {
            return text;
        }

        TextEvent event = new TextEvent(text);
        madebystarontopandfml.eventManager.call(event);
        return event.getText();
    }

    @ModifyVariable(
            method = "width(Ljava/lang/String;)I",
            at = @At("HEAD"),
            ordinal = 0
    )
    private String onWidth(String text) {
        if (text == null || madebystarontopandfml.eventManager == null) {
            return text;
        }

        TextEvent event = new TextEvent(text);
        madebystarontopandfml.eventManager.call(event);
        return event.getText();
    }
}
