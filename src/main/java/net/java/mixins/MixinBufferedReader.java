package net.java.mixins;

import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.At;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.Inject;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.Redirect;
import dev.architectury.patchedmixin.staticmixin.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@Mixin(BufferedReader.class)
public abstract class MixinBufferedReader extends Reader {
    @Redirect(method = "readLine", at = @At(value = "INVOKE", target = "Ljava/io/BufferedReader;readLine()Ljava/lang/String;"))
    private String readLine(CallbackInfo ci) throws IOException {
        return "";
    }

}
