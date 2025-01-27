package net.java.mixins;


import net.java.main.event.events.EntityJoinWorldEvent;
import net.java.main.event.events.WorldChangeEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.misc.Protocol;
import net.java.main.protocol.heypixel.Heypixel;
import net.minecraft.client.multiplayer.ClientLevel;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class MixinClientWorld {
    @Inject(method = "addEntity", at = @At("RETURN"))
    private void addEntity(int i, Entity arg, CallbackInfo info) {
        if (madebystarontopandfml.getInstance().getModuleManager().getModule("protocol").isEnabled()){
                Heypixel.get().onEntityJoinWorld(EntityJoinWorldEvent.get(arg, (ClientLevel) (Object) this));
            }
        EntityJoinWorldEvent event = new EntityJoinWorldEvent();
        madebystarontopandfml.getInstance().getEventManager().call(event);
    }
}
