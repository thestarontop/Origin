package net.java.mixins;

import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import net.java.main.event.events.TextEvent;
import net.java.main.madebystarontopandfml;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Font.class)
public abstract class MixinFont {
    /*@ModifyArg(
            method = "drawInternal(Ljava/lang/String;FFILcom/mojang/math/Matrix4f;ZZ)I", // 目标方法的名称
            at = @At(value = "HEAD"), // 在方法开头
            index = 0 // 指定要修改的参数的索引，这里是第一个参数 `string`
    )
    private String modifyStringArgument(String string) {
        TextEvent event = new TextEvent(string);
        madebystarontopandfml.getInstance().getEventManager().call(event);
        string = event.getText();
        return string;
    }
    @ModifyArg(
            method = "drawInternal(Ljava/lang/String;FFIZLcom/mojang/math/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;ZIIZ)I", // 目标方法的名称
            at = @At(value = "HEAD"), // 在方法开头
            index = 0 // 指定要修改的参数的索引，这里是第一个参数 `string`
    )
    private String modifyStringArgument2(String string) {
        TextEvent event = new TextEvent(string);
        madebystarontopandfml.getInstance().getEventManager().call(event);
        string = event.getText();
        return string;
    }*/
}
