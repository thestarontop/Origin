package net.java.main.utils.network;


import io.netty.buffer.ByteBuf;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.TickEvent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.NotNull;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.java.main.utils.MinecraftInstance.mc;


public class NetPayload {
    public static final Map<ResourceLocation, Consumer<ByteBuf>> REGISTERED_CHANNELS = new HashMap<>();

    public static void register(ResourceLocation channel, Consumer<ByteBuf> buf) {
        if (REGISTERED_CHANNELS.containsKey(channel)) return;

        REGISTERED_CHANNELS.put(channel, buf);
    }

    public static final List<Runnable> preTasks = new ArrayList<>();

    public static void pre(Runnable task) {
        preTasks.add(task);
    }

    public static void send(ResourceLocation channel, ByteBuf buf) {
        if (mc.getConnection() != null) {
            mc.getConnection().send(new ServerboundCustomPayloadPacket(new PayloadPacket(channel, buf)));
        }
    }

    @EventTarget
    private static void onTick(TickEvent event) {
        if (mc.getConnection() != null) {
            for (Runnable task : preTasks) {
                task.run();
            }
            preTasks.clear();
        }
    }

    public static class PayloadPacket extends FriendlyByteBuf {
        public ResourceLocation channel;
        public ByteBuf buf;

        public PayloadPacket(ResourceLocation channel, ByteBuf buf) {
            super(buf);
            this.channel = channel;
            this.buf = buf;
        }

        @Override
        public String toString() {
            return channel.toString() + " " + buf.toString();
        }

        @Override
        public <T extends IForgeRegistryEntry<T>> void writeRegistryIdUnsafe(@NotNull IForgeRegistry<T> registry, @NotNull T entry) {
            super.writeRegistryIdUnsafe(registry, entry);
        }
    }
}
