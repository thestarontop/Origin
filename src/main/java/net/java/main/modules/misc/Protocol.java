package net.java.main.modules.misc;


import io.netty.buffer.Unpooled;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.*;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.protocol.IProtocol;
import net.java.main.protocol.heypixel.Heypixel;
import net.java.main.protocol.heypixel.check.HeypixelCheckPacket;
import net.java.main.protocol.heypixel.check.HeypixelSessionManager;
import net.java.main.utils.network.NetPayload;
import net.java.main.value.ListValue;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Protocol extends Module {
    public Protocol(){
        super("Protocol","bzd", Category.MISC);
        addValues(mode,requestMode);
    }
    public static ListValue requestMode = new ListValue("requestMode", new String[]{"Port", "UUID","Merge"},"Merge");

    public static ListValue mode = new ListValue("type", new String[]{ "Myth","WNF"},"WNF");
    public static ListValue getRequestMode() {
        return requestMode;
    }

    public static NeteaseLauncher getNEL() {
        return NeteaseLauncher.valueOf(mode.getValue());
    }
    ResourceLocation channel;
    protected HeypixelSessionManager manager;


   @Override
    public void onEnable() {
       super.onEnable();
    }

        @EventTarget
    private void onPacket(PacketEvent e) {
       var c = Protocols.HeyPixel.protocol.onPacket(e.getPacket());
            var hex = "d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a23303623ff06d13481d20001249cd20001249cd3000001937f1098b896b7636f6d2e73756e2e6a6d782e72656d6f74652e7574696cb9636f6d2e73756e2e6f72672e6170616368652e786572636573bb73756e2e746578742e7265736f75726365732e636c64722e657874bb636f6d2e73756e2e786d6c2e696e7465726e616c2e73747265616dd9246f72672e6f70656e6a646b2e6e6173686f726e2e696e7465726e616c2e73637269707473d92073756e2e7574696c2e7265736f75726365732e636c64722e70726f7669646572" +
                    "06f601d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a233036230300007676d3000001937f1098bc94d921433a5c5c57494e444f57535c5c73797374656d33325c5c75726c6d6f6e2e646c6cd923433a5c5c57494e444f57535c5c53595354454d33325c5c6e65747574696c732e646c6cd923433a5c5c57494e444f57535c5c53595354454d33325c5c696572747574696c2e646c6cd921433a5c5c57494e444f57535c5c53595354454d33325c5c737276636c692e646c6c" +
                    "06e701d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a23303623010200d200009983d200009983d3000001937f1098bc82d9326e65742e6d696e6563726166742e636c69656e742e6775692e636861742e4e61727261746f72436861744c697374656e6572ab5452414e53464f524d4552d92b6e65742e6d696e6563726166742e636c69656e742e6d756c7469706c617965722e506c61796572496e666fab5452414e53464f524d4552";

            //  var bytes = new byte[] {-39, 36, 100, 102, 57, 101, 99, 55, 98, 57, 45, 102, 48, 99, 98, 45, 52, 52, 97, 54, 45, 98, 98, 54, 50, 45, 99, 99, 98, 54, 54, 101, 49, 50, 101, 57, 98, 49, -39, 36, 54, 52, 48, 55, 97, 50, 100, 56, 45, 52, 53, 99, 98, 45, 52, 102, 101, 50, 45, 97, 48, 48, 54, 45, 49, 101, 50, 99, 100, 49, 52, 53, 53, 97, 99, 102, 3, 4, 0, 118, 118, -49, 0, 0, 1, -109, 52, -88, 101, 80, -108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 115, 121, 115, 116, 101, 109, 51, 50, 92, 117, 114, 108, 109, 111, 110, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 110, 101, 116, 117, 116, 105, 108, 115, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 105, 101, 114, 116, 117, 116, 105, 108, 46, 100, 108, 108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 115, 114, 118, 99, 108, 105, 46, 100, 108, 108};
           mc.getConnection().send(new ServerboundCustomPayloadPacket(channel, new FriendlyByteBuf(Unpooled.buffer().writeBytes(hex.getBytes()))));

            if (!c) return;
       e.cancelEvent();
        }
    @EventTarget
    private void onTick(TickEvent e) {
        Protocols.HeyPixel.protocol.tick();
    }
    @EventTarget
    private void onEntityJoin(EntityJoinWorldEvent e) {
        Protocols.HeyPixel.protocol.onEntityJoinWorld(e);
    }
    @EventTarget
    private void onWorldChange(WorldChangeEvent e) {
        Protocols.HeyPixel.protocol.onWorldChanged(e);
    }
    @Override
    public void onActivate() {
        Protocols.HeyPixel.protocol.reload();
    }


    public enum NeteaseLauncher {
        Myth,
        WNF,
        }
    public enum Protocols {
        HeyPixel(Heypixel.get()),
        NULL(new IProtocol() {

            @Override
            public void init() {

            }

            @Override
            public void reload() {

            }

            @Override
            public String name() {
                return "";
            }

            @Override
            public boolean onPacket(Packet packet) {
                return false;
            }


            @Override
            public void onWorldChanged(WorldChangeEvent e) {

            }
            @Override
            public void onMessage(Text text) {

            }

            @Override
            public void onEntityJoinWorld(EntityJoinWorldEvent e) {

            }

            @Override
            public void tick() {

            }
        });

        public final IProtocol protocol;

        Protocols(IProtocol protocol) {
            this.protocol = protocol;
        }
    }






}
