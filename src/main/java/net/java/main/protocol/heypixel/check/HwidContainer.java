package net.java.main.protocol.heypixel.check;



import net.java.main.protocol.heypixel.HardwareList;
import net.java.main.protocol.heypixel.HeypixelHwid;
import net.java.main.protocol.heypixel.msgpack.value.Value;
import net.java.main.protocol.heypixel.msgpack.value.ValueFactory;
import net.java.main.protocol.heypixel.msgpack.value.Variable;
import net.java.main.protocol.heypixel.utils.StringUtils;
import net.java.main.protocol.heypixel.utils.VariableUtils;
import net.java.main.utils.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HwidContainer {
    private final String RANDOM_DISK_;
    private final String BASEBOARD_SERIAL_NUM;
    private final HeypixelHwid obj = new HeypixelHwid();
    public Variable diskSerials;
    public Variable cpu;
    public Variable disks;
    public Variable networkInterfaces;
    public Variable baseboard;
    public Variable baseboardSerial;
    public Variable networkHardware;
    public Variable system;

    public long userId;
    public String userTokenHash;

    public Variable user;
    public Variable neteaseUsersHash;

    public HwidContainer() {
        RANDOM_DISK_ = "SCRW1" + RandomUtils.randomNumber(7) + "F" + RandomUtils.randomNumber(4);
        BASEBOARD_SERIAL_NUM = RandomUtils.randomNumber(16);
        setRandom();
    }

    public HwidContainer setRandom() {
        this.cpu = randomCpuInfo();
        this.baseboardSerial = randomBaseboardSerial();
        this.diskSerials = randomDiskSerials();
        this.baseboard = randomBaseboardInfo();
        this.disks = randomDiskInfo();
        this.networkInterfaces = randomNetworkInterfaces();
        this.networkHardware = randomNetworkHardware();
        this.system = randomSystemHwid();

        this.neteaseUsersHash = randomNeteaseUsers();
        return this;
    }

    public void loadFromObj(HeypixelHwid obj) {
        this.networkHardware = VariableUtils.stringsToVariable(obj.network_hwids);
        this.cpu = new Variable().setStringValue(obj.cpu);
        this.baseboardSerial = new Variable().setStringValue(obj.baseboard_serial);
        this.diskSerials = VariableUtils.stringsToVariable(obj.disk_serials);
        this.baseboard = VariableUtils.stringMapToVariable(obj.baseboards);
        this.disks = VariableUtils.stringsMapToVariable(obj.disks);
        this.networkInterfaces = VariableUtils.stringsMapToVariable(obj.network_interfaces);
        this.system = new Variable().setStringValue(obj.system);
    }

    public HwidContainer setUserInfo(long userId, String hash) {
        this.userId = userId;
        this.userTokenHash = hash;

        HashMap<Value, Value> hashMap = new HashMap<>();
        hashMap.put(ValueFactory.newString("TokenHash"), ValueFactory.newString(hash));
        hashMap.put(ValueFactory.newString("UserId"), ValueFactory.newInteger((userId & 2147483648L) == 0 ? userId | 2147483648L : userId));
        user = new Variable().setMapValue(hashMap);

        this.obj.user = String.valueOf(userId);
        return this;
    }

    public HeypixelHwid toObj() {
        return obj;
    }

    public Variable randomNeteaseUsers() {
        ArrayList<Value> arrayList = new ArrayList<>();
        var users = new ArrayList<String>();

        for (int i = 0; i < RandomUtils.nextInt(1, 4); i++) {
            var email = RandomUtils.randomString(10) + "@163.com";
            users.add(email);
        }

        users.forEach((file) -> arrayList.add(ValueFactory.newString(StringUtils.hashString(file))));
        return new Variable().setArrayValue(arrayList);
    }

    public Variable randomCpuInfo() {
        var cpu = RandomUtils.random(6, "BEF0") + RandomUtils.randomStringHex(7) + "|" + HardwareList.randomCpu();

        this.obj.cpu = cpu;
        return new Variable().setStringValue(cpu);
    }

    public Variable randomNetworkHardware() {
        List<String> networks = new ArrayList<>();
        for (int i = 0; i < this.obj.network_interfaces.size(); i++) {
            var temp = "%A%A-%B%B-%C%C-%D%D-%E%E-%F%F";
            while (temp.contains("%A")) {
                temp = temp.replaceFirst("%A", RandomUtils.randomStringHex(1));
            }
            while (temp.contains("%B")) {
                temp = temp.replaceFirst("%B", RandomUtils.randomStringHex(1));
            }
            while (temp.contains("%C")) {
                temp = temp.replaceFirst("%C", RandomUtils.randomStringHex(1));
            }
            while (temp.contains("%D")) {
                temp = temp.replaceFirst("%D", RandomUtils.randomStringHex(1));
            }
            while (temp.contains("%E")) {
                temp = temp.replaceFirst("%E", RandomUtils.randomStringHex(1));
            }
            while (temp.contains("%F")) {
                temp = temp.replaceFirst("%F", RandomUtils.randomStringHex(1));
            }

            networks.add(temp);
        }

        this.obj.network_hwids = networks;

        return VariableUtils.stringsToVariable(networks);
    }


    public Variable randomBaseboardSerial() {
        this.obj.baseboard_serial = BASEBOARD_SERIAL_NUM;
        return new Variable().setStringValue(BASEBOARD_SERIAL_NUM);
    }


    public Variable randomDiskSerials() {
        ArrayList<String> disk = new ArrayList<>();
        disk.add("");
        disk.add(RANDOM_DISK_);
        disk.add("");
        disk.add("");
        disk.add("");

        this.obj.disk_serials = disk;
        return VariableUtils.stringsToVariable(disk);
    }

    public Variable randomNetworkInterfaces() {
        List<Map<String, String>> networks = new ArrayList<>();

        List<String> networkHs = HardwareList.randomNetwork();
        for (String networkH : networkHs) {
            var hashMap = new HashMap<String, String>();
            hashMap.put("a", networkH);
            var mac = RandomUtils.randomStringHexLower(2) + // 1
                ":" + RandomUtils.randomStringHexLower(2) + // 2
                ":" + RandomUtils.randomStringHexLower(2) + // 3
                ":" + RandomUtils.randomStringHexLower(2) + // 4
                ":" + RandomUtils.randomStringHexLower(2) + // 5
                ":" + RandomUtils.randomStringHexLower(2); // 6
            hashMap.put("b", mac);
            networks.add(hashMap);
        }

        this.obj.network_interfaces = networks;
        return VariableUtils.stringsMapToVariable(networks);
    }

    public Variable randomDiskInfo() {
        ArrayList<Map<String, String>> disks = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("a", RANDOM_DISK_);
        hashMap.put("b", "\\\\\\\\.\\\\PHYSICALDRIVE0");
        hashMap.put("c", HardwareList.randomDisk() + " (标准磁盘驱动器)");
        disks.add(hashMap);

        this.obj.disks = disks;
        return VariableUtils.stringsMapToVariable(disks);
    }

    public Variable randomBaseboardInfo() {
        String manufacturer = RandomUtils.randomString(7);

        StringBuilder str = new StringBuilder();
        for (char c : manufacturer.toCharArray()) {
            str.append(RandomUtils.randomStringHex(2));
            str.append(c);
        }

        String model = "unknown";

        String version = "1.0";
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("a", str.toString());
        hashMap.put("b", model);
        hashMap.put("c", BASEBOARD_SERIAL_NUM);
        hashMap.put("d", version);

        this.obj.baseboards = hashMap;
        return VariableUtils.stringMapToVariable(hashMap);
    }

    public Variable randomSystemHwid() {
        var uuid = "%A%A%A%A%A%A%A%A-%B%B%B%B-%C%C%C%C-%D%D%D%D-%E%E%E%E%E%E%E%E%E%E%E%E";

        while (uuid.contains("%A")) {
            uuid = uuid.replaceFirst("%A", RandomUtils.randomStringHex(1));
        }

        while (uuid.contains("%B")) {
            uuid = uuid.replaceFirst("%B", RandomUtils.randomStringHex(1));
        }

        while (uuid.contains("%C")) {
            uuid = uuid.replaceFirst("%C", RandomUtils.randomStringHex(1));
        }

        while (uuid.contains("%D")) {
            uuid = uuid.replaceFirst("%D", RandomUtils.randomStringHex(1));
        }

        while (uuid.contains("%E")) {
            uuid = uuid.replaceFirst("%E", RandomUtils.randomStringHex(1));
        }

        this.obj.system = uuid;
        return new Variable().setStringValue(uuid);
    }
}
