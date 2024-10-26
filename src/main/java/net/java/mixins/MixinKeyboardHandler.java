package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.event.events.KeyPressEvent;
import net.minecraft.client.*;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class MixinKeyboardHandler {


    @Inject(method = "keyPress",at=@At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int mods, CallbackInfo ci) {
        KeyPressEvent event = new KeyPressEvent(key);
        if(action == GLFW.GLFW_PRESS && key != GLFW.GLFW_KEY_UNKNOWN) {
            madebystarontopandfml.getInstance().getEventManager().call(event);
        }
    }
}
