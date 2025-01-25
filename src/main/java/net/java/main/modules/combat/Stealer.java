package net.java.main.modules.combat;


import com.google.common.collect.Lists;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.MotionEvent;
import net.java.main.event.events.PacketEvent;
import net.java.main.event.events.TickEvent;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.Module;
import net.java.main.utils.MSTimer;
import net.java.main.value.FloatValue;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Stealer extends Module {
    public Stealer() {
        super("Stealer", "bzd", Module.Category.COMBAT);
        addValues(delay);
    }

    private int tickCounter = 0;
    Int2ObjectMap<ItemStack> int2objectmap = new Int2ObjectOpenHashMap();
    public FloatValue delay = new FloatValue("Delay", 3f, 0f, 6f);


    @EventTarget
    public void onTick(TickEvent event) {
        if (mc.screen instanceof ContainerScreen screen) {
            tickCounter++;
            var hasItem = false;
            for (int i = 0; i < screen.getMenu().getContainer().getContainerSize(); i++) {
                ItemStack stack = screen.getMenu().getSlot(i).getItem();
                if (stack.isEmpty()) {
                    continue;
                }

                hasItem = true;

               // if (!ItemUtil.useful(stack)) continue;
                if (tickCounter >=delay.getValue()) {
                    mc.getConnection().send(new ServerboundContainerClickPacket(screen.getMenu().containerId, i, i, 1, ClickType.QUICK_MOVE,screen.getMenu().getContainer().getItem(i), int2objectmap));
                    tickCounter = 0;
                }
            }

            if (!hasItem) {
                screen.onClose();
            }
        }
        if (mc.level != null && mc.player != null ) {
            if (!mc.player.isAlive()) {
                toggle();
                return;
            }
        }
    }
}



