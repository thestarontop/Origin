package net.java.main.protocol.heypixel;


import io.netty.buffer.Unpooled;
import net.java.main.protocol.heypixel.msgpack.core.MessagePack;
import net.java.main.protocol.heypixel.utils.BufferHelper;
import net.java.main.protocol.heypixel.utils.HeypixelVarUtils;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;

public class Dumper {
    public static void main1(String[] args) throws IOException, DecoderException {
        var hex = Hex.decodeHex("d92463373465643630352d663737632d346661362d613038322d353632393933656630376562d92438353733343630312d363134342d346461392d386332312d396536383438666266643461d948426c6f636b7b6d696e6563726166743a706c617965725f686561647d5b666163696e673d6e6f7274682c747970653d73696e676c652c77617465726c6f676765643d66616c73655d");
        try (var unpacker = MessagePack.newDefaultUnpacker(hex)) {
            var item = unpacker.unpackValue().asStringValue().asString();
            var a = unpacker.unpackValue().asStringValue().asString();
            var c = unpacker.unpackValue().asStringValue().asString();

            System.out.println(item);
            System.out.println(a);
            System.out.println(c);
        }
    }

    public static void main0(String[] args) throws DecoderException {
        var hex = "d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a23303623ff06d13481d20001249cd20001249cd3000001937f1098b896b7636f6d2e73756e2e6a6d782e72656d6f74652e7574696cb9636f6d2e73756e2e6f72672e6170616368652e786572636573bb73756e2e746578742e7265736f75726365732e636c64722e657874bb636f6d2e73756e2e786d6c2e696e7465726e616c2e73747265616dd9246f72672e6f70656e6a646b2e6e6173686f726e2e696e7465726e616c2e73637269707473d92073756e2e7574696c2e7265736f75726365732e636c64722e70726f7669646572" +
                "06f601d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a233036230300007676d3000001937f1098bc94d921433a5c5c57494e444f57535c5c73797374656d33325c5c75726c6d6f6e2e646c6cd923433a5c5c57494e444f57535c5c53595354454d33325c5c6e65747574696c732e646c6cd923433a5c5c57494e444f57535c5c53595354454d33325c5c696572747574696c2e646c6cd921433a5c5c57494e444f57535c5c53595354454d33325c5c737276636c692e646c6c" +
                "06e701d92934353463613961362d343164352d346664612d613963642d3932636238633135366565352a23303623d92961613731306530332d656530662d346130642d623432312d6339316665353832353636332a23303623010200d200009983d200009983d3000001937f1098bc82d9326e65742e6d696e6563726166742e636c69656e742e6775692e636861742e4e61727261746f72436861744c697374656e6572ab5452414e53464f524d4552d92b6e65742e6d696e6563726166742e636c69656e742e6d756c7469706c617965722e506c61796572496e666fab5452414e53464f524d4552";

        var bytes = new byte[]{
                -39, 36, 100, 102, 57, 101, 99, 55, 98, 57, 45, 102, 48, 99, 98, 45, 52, 52, 97, 54, 45, 98, 98, 54, 50, 45, 99, 99, 98, 54, 54, 101, 49, 50, 101, 57, 98, 49, -39, 36, 54, 52, 48, 55, 97, 50, 100, 56, 45, 52, 53, 99, 98, 45, 52, 102, 101, 50, 45, 97, 48, 48, 54, 45, 49, 101, 50, 99, 100, 49, 52, 53, 53, 97, 99, 102, 3, 4, 0, 118, 118, -49, 0, 0, 1, -109, 52, -88, 101, 80, -108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 115, 121, 115, 116, 101, 109, 51, 50, 92, 117, 114, 108, 109, 111, 110, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 110, 101, 116, 117, 116, 105, 108, 115, 46, 100, 108, 108, -39, 32, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 105, 101, 114, 116, 117, 116, 105, 108, 46, 100, 108, 108, -66, 67, 58, 92, 87, 73, 78, 68, 79, 87, 83, 92, 83, 89, 83, 84, 69, 77, 51, 50, 92, 115, 114, 118, 99, 108, 105, 46, 100, 108, 108
        };

        try (var unpacker = MessagePack.newDefaultUnpacker(Hex.decodeHex(hex))) {

            // client id
            var clientId = unpacker.unpackString();
            // uuid
            var uuid = unpacker.unpackString();
            // --> main
            var id = unpacker.unpackValue().asIntegerValue().asLong();
            var size = unpacker.unpackValue().asIntegerValue().asLong();
            var size1 = unpacker.unpackValue().asIntegerValue().asLong();
            var size2 = unpacker.unpackValue().asIntegerValue().asLong();
            var size3 = unpacker.unpackValue().asIntegerValue().asLong();
            var time = unpacker.unpackValue().asIntegerValue().asLong();
            var list = unpacker.unpackValue().toJson();

            System.out.println("cid: " + clientId);
            System.out.println("uuid: " + uuid);
            System.out.println("id: " + id);
            System.out.println("size: " + size);
            System.out.println("size1: " + size1);
            System.out.println("size2: " + size2);
            System.out.println("size3: " + size3);
            System.out.println("time: " + time);
            System.out.println("list: " + list);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws DecoderException {
        var hex = "01a20dd92961636539303036392d653434392d343838322d613333352d6332616635653564306164392a23303623d92930353635646132662d383430632d343230632d626234332d3063313635613432316136322a23303623d30000019381c23eeec2d9d45b6d696e65636166742c20656e7469747963756c6c696e672c2061726d6f75726572735f776f726b73686f702c206e6574656173655f6f6666696369616c2c20696d6d6564696174656c79666173742c2063756c6c6c6573736c65617665732c20686579706978656c2c206e6f636861746c6167666f7267652c206d656d6f72796c65616b6669782c207265657365735f736f6469756d5f6f7074696f6e732c20666f7267652c20727562696469756d2c20656d6265646469756d706c75732c20696365626572672c206765636b6f6c6962335dbe493a5c4d434c446f776e6c6f61645c47616d655c2e6d696e656372616674d927493a5c4d434c446f776e6c6f61645c6578745c6a72652d7636342d3232303432305c6a646b3137d95b424242464642424533363834313832317c313374682047656e20496e74656c28522920436f726528544d292069352d31333630304b7c496e74656c36342046616d696c792036204d6f64656c20313833205374657070696e672031b03735323333383639383139343631373495a0b15343525731323236313837394637393537a0a0a09cb142392d43382d37422d41372d43352d4442b130442d32362d36322d35412d36312d3039b135342d41342d43352d41422d30342d4233b142372d38342d43342d37352d36312d3435b131392d35432d37392d32362d42362d4136b137382d44372d31372d39352d33342d3639b134352d34432d45392d44322d32302d3535b139412d37352d38372d41312d41332d4430b141452d30352d30452d43312d43322d3744b142312d31422d38332d33352d35312d3335b145342d44372d32432d39322d43382d4343b138332d33452d45382d41412d44422d343682a6557365724964d300000000ae1d34d5a9546f6b656e48617368d9403265356163653432623332373432383861303430613963386530356566383734636437356139373432373839343434336262653939343064613932353839343984a161d923353139304b4445423252374432444a3238423455343933315132394435723345393059a162a7756e6b6e6f776ea163b037353233333836393831393436313734a164a3312e309183a161b15343525731323236313837394637393537a162b55c5c5c5c2e5c5c504859534943414c445249564530a163d9384b696e6773746f6e204132303030204e564d652050434965204d2e32203154422028e6a087e58786e7a381e79b98e9a9b1e58aa8e599a8299582a161d95f54502d4c696e6b20417263686572205434552041433133303020576972656c657373204e6574776f726b20416461707465722d574650204e6174697665204d4143204c61796572204c696768745765696768742046696c7465722d30303035a162b139343a41453a43323a39333a36373a364482a161d93141535553205553422d4e3133204e616e6f204475616c2d42616e64203830322e31316e204e6574776f726b2057692d4669a162b145313a43303a31393a33453a35383a424182a161d9294c696e6b737973205755534236333030204143313230302057692d466920416461707465722d574650a162b142353a42423a42323a42353a44323a413982a161d95b496e74656c2043656e7472696e6f20416476616e6365642d4e2036323330204e6574776f726b20416461707465722d574650204e6174697665204d4143204c61796572204c696768745765696768742046696c7465722d30303032a162b145343a33413a34423a33323a45363a373982a161d956442d4c696e6b204457412d3138322055534220332e30204e3630302057692d466920416461707465722d574650204e6174697665204d4143204c61796572204c696768745765696768742046696c7465722d30303036a162b134313a43423a36433a36453a42443a4230d944433138303439353733323134453243322d31363344413932422d35444336413642302d42414441444432372d45383939453636393537313242343233333236373337333992d94036626430363132646663343265623735333539646237333336373039383461356633363633613065613738356131653432373538653630386165373462343062d94062353236333462643864343332623438356632316339306562646230636430623739643865623237653036353362376666306463636235633963656632383237";

        var buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(Hex.decodeHex(hex)));
        var bufferHelper = new BufferHelper();

        var id = HeypixelVarUtils.readVarInt(buf);
        var bytes = bufferHelper.readByteArrayHeypixelClient(buf);

        try (var unpacker = MessagePack.newDefaultUnpacker(bytes)) {

            // client id
            var clientId = unpacker.unpackString();
            // uuid
            var uuid = unpacker.unpackString();
            // --> main
            var runtime = unpacker.unpackValue();
            var bool = unpacker.unpackBoolean();
            var mods = unpacker.unpackValue();
            var run_dir = unpacker.unpackValue();
            var java_home = unpacker.unpackValue();

            var cpuInfo = unpacker.unpackValue();
            var baseboardSerial = unpacker.unpackValue();
            var diskSerials = unpacker.unpackValue();
            var networkHardwareInfo = unpacker.unpackValue();
            var userIdInfo = unpacker.unpackValue();
            var baseboardInfo = unpacker.unpackValue();
            var diskStoreInfo = unpacker.unpackValue();
            var networkInterfaces = unpacker.unpackValue();
            var systemInfo = unpacker.unpackValue();
            var neteaseUserListHash = unpacker.unpackValue();

            System.out.println("cid: " + clientId);
            System.out.println("uuid: " + uuid);
            System.out.println("id: " + id);
            System.out.println("runTime: " + runtime.toJson());
            System.out.println("bool: " + bool);
            System.out.println("mods: " + mods.toJson());
            System.out.println("run_dir: " + run_dir.toJson());
            System.out.println("java_home: " + java_home.toJson());

            System.out.println("cpuInfo: " + cpuInfo.toJson());
            System.out.println("baseboardSerial: " + baseboardSerial.toJson());
            System.out.println("diskSerials: " + diskSerials.toJson());
            System.out.println("networkHardwareInfo: " + networkHardwareInfo.toJson());
            System.out.println("userIdInfo: " + userIdInfo.toJson());
            System.out.println("baseboardInfo: " + baseboardInfo.toJson());
            System.out.println("diskStoreInfo: " + diskStoreInfo.toJson());
            System.out.println("networkInterfaces: " + networkInterfaces.toJson());
            System.out.println("systemInfo: " + systemInfo.toJson());
            System.out.println("neteaseUserListHash: " + neteaseUserListHash.toJson());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
