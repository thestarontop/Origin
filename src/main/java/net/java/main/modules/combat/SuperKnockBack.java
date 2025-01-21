package net.java.main.modules.combat;

import net.java.main.event.events.AttackEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.utils.PacketUtils;
import net.java.main.value.ListValue;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;


public class SuperKnockBack extends Module {
    public SuperKnockBack() {
        super("SuperKnockBack","bzd", Module.Category.COMBAT);
        this.addValues(mode);
    }
    public ListValue mode = new ListValue("mode",new String[]{"normal","test"},"normal");
    boolean pass = false;
    @EventTarget
    public void onPacket(PacketEvent event){
        if (mode.getValue() == "normal") {
            if (event.getPacket() instanceof ServerboundInteractPacket) {
                boolean sprinting = mc.player.isSprinting();
                if (sprinting) {
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                    mc.player.setSprinting(true);
                } else {
                    if (pass) {
                        pass = false;
                        return;
                    }
                    pass = true;
                    event.cancelEvent();
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                    mc.getConnection().send(event.getPacket());
                    mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.STOP_SPRINTING));
                    mc.player.setSprinting(false);
                }
            }
        }else{
            if (event.getPacket() instanceof ServerboundInteractPacket){
                event.cancelEvent();
                var a = mc.player.getInventory().selected;
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                PacketUtils.sendPacketNoEvent(event.getPacket());
                PacketUtils.sendPacketNoEvent(new ServerboundSwingPacket(InteractionHand.OFF_HAND));
                var maxdamage = 0.0;
                var prevslot = a;
                for (var i = 36;i <= 45; i++){
                    ItemStack item = mc.player.inventoryMenu.getSlot(i).getItem();
                    if (getDamage(item) > maxdamage){
                        prevslot = i-36;
                        maxdamage = getDamage(item);
                    }
                }
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(prevslot));
                mc.getConnection().send(new ServerboundPlayerCommandPacket(mc.player, ServerboundPlayerCommandPacket.Action.START_SPRINTING));
                PacketUtils.sendPacketNoEvent(event.getPacket());
                PacketUtils.sendPacketNoEvent(new ServerboundSwingPacket(InteractionHand.OFF_HAND));
                mc.getConnection().send(new ServerboundSetCarriedItemPacket(a));
            }
            if (event.getPacket() instanceof ServerboundSwingPacket packet && packet.getHand() == InteractionHand.MAIN_HAND){
                event.cancelEvent();
            }
        }
    }
    public static double getDamage(ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS,itemStack)*0.001 + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.KNOCKBACK,itemStack)*0.0005;
    }
}

