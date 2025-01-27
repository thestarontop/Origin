package net.java.main.protocol.heypixel;




import net.java.main.protocol.heypixel.utils.StringUtils;
import net.java.main.utils.RandomUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HardwareList {


    public static final String[] INTEL_VERSION = {"1st", "2nd", "3th", "4th", "5th", "6th", "7th", "8th", "9th", "10th", "11th", "12th", "13th", "14th"};
    public static final String[] INTEL_SUFFIX = {"K", "KF", "C"};
    public static final List<String> DISKS = List.of(
            "Samsung SSD 941 EVO",
            "Western Digital Blue SN650 NVMe",
            "Crucial P5 NVMe",
            "Seagate FireCuda 580 NVMe",
            "Kingston A2100 NVMe PCIe M.2",
            "Corsair MP550 PRO NVMe Gen4",
            "ADATA XPG Gammix D32 NVMe",
            "SanDisk Extreme PRO NVMe",
            "Plextor M9P Plus Plus NVMe PCIe M.2",
            "OWC Aura Pro X3 NVMe",
            "Gigabyte AORUS NVMe Gen4",
            "TeamGroup Delta Max NVMe PCIe M.2",
            "KLEVV Cras C980 NVMe PCIe M.2",
            "LENOVO"
    );
    public static final String[] DISK_MEM = {"256GB", "512GB", "1TB", "2TB", ""};
    public static final List<String> DLLS = List.of(
            "C:\\\\WINDOWS\\\\system32\\\\urlmon.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\netutils.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\iertutil.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\srvcli.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\clrhost.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\clusapi.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmcfg32.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmdext.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmdial32.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmgrcspps.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmifw.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmintegrator.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmlua.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmpbk32.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmstplua.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cmutil.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cngcredui.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cngprovider.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cnvfat.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\CodeIntegrityAggregator.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\cofiredm.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\colbact.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\colorui.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\capauthz.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\capiprovider.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\capisp.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bootstr.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bootsvc.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bootux.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bridgeres.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bcrypt.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bcryptprimitives.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\BdeHdCfgLib.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bderepair.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bdesvc.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\BdeSysprep.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bdeui.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bdmjpeg64.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bdmpegv64.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bi.dll",
            "C:\\\\WINDOWS\\\\SYSTEM32\\\\bidispl.dll"
    );

    public static void main(String[] args) throws IOException {
        Files.list(Path.of("C:\\\\WINDOWS\\\\SYSTEM32\\\\")).forEach(
                p -> {
                    var name = p.getFileName().toString();
                    if (!name.substring(0,name.length() - 4).contains(".") && name.endsWith(".dll")) {
                        System.out.println("\"C:\\\\WINDOWS\\\\SYSTEM32\\\\" + name+ "\",");
                    }
                }
        );
    }

    public static String randomCpu() {
        var suffix = RandomUtils.nextArray(INTEL_SUFFIX);
        var type = INTEL_TYPE.random();
        var ver = RandomUtils.nextArray(INTEL_VERSION);

        var id = ver.substring(0, ver.length() - 2);
        var num = Integer.parseInt(id);
        //      {ver}             {type.val}{id}{suffix}                  {model}    {step}
        var s1 = "{} Gen Intel(R) Core(TM) {}-{}{}|Intel64 Family 6 Model {} Stepping {}";

        String cpuId = (id.equals("1") ? "" : id) + type.suffix;

        if (RandomUtils.nextBoolean()) {
            s1 = "Intel(R) Core(TM) {}-{}{} CPU @ {}GHz|Intel64 Family 6 Model {} Stepping {}";
            return StringUtils.getReplaced(s1,
                    type.val,
                    cpuId,
                    suffix,
                    RandomUtils.nextInt(1, 3) + "." + RandomUtils.nextInt(0, 9) + "0",
                    RandomUtils.nextInt(7, 20) * num,
                    RandomUtils.nextInt(1, INTEL_VERSION.length)
            );
        }

        return StringUtils.getReplaced(s1,
                ver,
                type.val,
                cpuId,
                suffix,
                RandomUtils.nextInt(7, 20) * num,
                RandomUtils.nextInt(1, INTEL_VERSION.length)
        );
    }

    public static String randomDisk() {
        var mem = RandomUtils.nextArray(DISK_MEM);
        return RandomUtils.nextList(DISKS) + (mem.isEmpty() ? "" : " ") + RandomUtils.nextArray(DISK_MEM);
    }

    public static List<String> randomNetwork() {
        boolean hasNpcap = RandomUtils.nextBoolean();
        boolean hasvbox = RandomUtils.nextBoolean();
        boolean hasZeroTier = RandomUtils.nextBoolean();
        boolean hasHyperV = RandomUtils.nextBoolean();
        boolean hasVmware = RandomUtils.nextBoolean();

        var list = new ArrayList<String>();
        list.add("TAP-Windows Adapter V9-WFP Native MAC Layer LightWeight Filter-0000");
        if (hasNpcap) {
            list.add("TAP-Windows Adapter V9-Npcap Packet Driver (NPCAP)-0000");
        }
        if (hasNpcap && hasZeroTier) {
            list.add("ZeroTier Virtual Port-Npcap Packet Driver (NPCAP)-0000");
        }

        list.add("TAP-Windows Adapter V9-QoS Packet Scheduler-0000");

        if (hasHyperV) {
            list.add("Hyper-V Virtual Ethernet Adapter-WFP Native MAC Layer LightWeight Filter-0000");
        }

        list.add("TAP-Windows Adapter V9-WFP 802.3 MAC Layer LightWeight Filter-0000");

        if (hasZeroTier) {
            list.add("ZeroTier Virtual Port-QoS Packet Scheduler-0000");
            list.add("ZeroTier Virtual Port-WFP 802.3 MAC Layer LightWeight Filter-0000");
        }

        list.add("Realtek PCIe GbE Family Controller-WFP Native MAC Layer LightWeight Filter-0000");

        if (hasVmware && hasNpcap) {
            list.add("VMware Virtual Ethernet Adapter for VMnet1-Npcap Packet Driver (NPCAP)-0000");
        }

        if (hasNpcap) {
            list.add("Realtek PCIe GbE Family Controller-Npcap Packet Driver (NPCAP)-0000");
        }
        list.add("Realtek PCIe GbE Family Controller-QoS Packet Scheduler-0000");
        list.add("Realtek PCIe GbE Family Controller-WFP 802.3 MAC Layer LightWeight Filter-0000");
        list.add("Realtek PCIe GbE Family Controller");

        if (hasvbox && hasNpcap) {
            list.add("VirtualBox Host-Only Ethernet Adapter-Npcap Packet Driver (NPCAP)-0000");
        }

        if (hasvbox) {
            list.add("VirtualBox Host-Only Ethernet Adapter");
        }

        list.add("TAP-Windows Adapter V9");
        if (RandomUtils.nextBoolean()) {
            list.add("Famatech RadminVPN Ethernet Adapter");
        }
        if (hasVmware) {
            for (int i = 0; i < RandomUtils.nextInt(0, 5); i++) {
                var net = "VMware Virtual Ethernet Adapter for VMnet" + RandomUtils.nextInt(1, 8);
                if (!list.contains(net)) {
                    list.add(net);
                }
            }
        }
        if (hasZeroTier) {
            list.add("ZeroTier Virtual Port");
        }
        return list;
    }

    public enum INTEL_TYPE {
        I3("i3", "100"),
        I5("i5", "600"),
        I7("i7", "700"),
        I9("i9", "900");

        public final String val;
        public final String suffix;

        INTEL_TYPE(String val, String suffix) {
            this.val = val;
            this.suffix = suffix;
        }

        public static INTEL_TYPE random() {
            return RandomUtils.nextArray(values());
        }
    }
}
