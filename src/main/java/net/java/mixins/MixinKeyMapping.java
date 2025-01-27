package net.java.mixins;

import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyMapping.class)
public abstract class MixinKeyMapping {
    @Shadow private boolean isDown;

     @Shadow public abstract IKeyConflictContext getKeyConflictContext();

    @Shadow public abstract KeyModifier getKeyModifier();

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public boolean isDown() {
        Module InvMove = madebystarontopandfml.getInstance().getModuleManager().getModule("invmove");
        boolean shouldInvMove = InvMove.isEnabled() ? this.isDown : (this.isDown && this.getKeyConflictContext().isActive() && this.getKeyModifier().isActive(this.getKeyConflictContext()));
        return shouldInvMove;
    }
}
