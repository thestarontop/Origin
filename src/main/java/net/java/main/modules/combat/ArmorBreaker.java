package net.java.main.modules.combat;

import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.PacketEvent;
import net.java.main.modules.Module;
import net.java.main.utils.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;

public class ArmorBreaker extends Module {
    public ArmorBreaker(){super("ArmorBreaker","bzd",Category.COMBAT);}
    @EventTarget
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof ServerboundInteractPacket){
            var a = mc.player.getInventory().selected;
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
            PacketUtils.sendPacketNoEvent(event.getPacket());
            PacketUtils.sendPacketNoEvent(new ServerboundSwingPacket(InteractionHand.OFF_HAND));
            mc.getConnection().send(new ServerboundSetCarriedItemPacket(a));
        }
        if (event.getPacket() instanceof ServerboundSwingPacket packet && packet.getHand() == InteractionHand.MAIN_HAND){
            event.cancelEvent();
        }

    }
    public static double getDamage(ItemStack itemStack) {
       return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS,itemStack)*0.00000001;
    }
}
