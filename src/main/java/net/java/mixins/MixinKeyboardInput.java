package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.event.events.MovementInputEvent;
import net.minecraft.client.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyboardInput.class)
public class MixinKeyboardInput extends Input {
    @Shadow @Final private Options options;

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void tick(boolean bl) {
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = this.up == this.down ? 0.0F : (this.up ? 1.0F : -1.0F);
        this.leftImpulse = this.left == this.right ? 0.0f : (this.left ? 1.0f : -1.0f);
        MovementInputEvent event = new MovementInputEvent(forwardImpulse,leftImpulse);
        madebystarontopandfml.getInstance().getEventManager().call(event);
        forwardImpulse = event.getForward();
        leftImpulse = event.getStrafe();
        this.jumping = this.options.keyJump.isDown();
        this.shiftKeyDown = this.options.keyShift.isDown();
        if (bl) {
            this.leftImpulse = (float)((double)this.leftImpulse * 0.3);
            this.forwardImpulse = (float)((double)this.forwardImpulse * 0.3);
        }
    }

}
