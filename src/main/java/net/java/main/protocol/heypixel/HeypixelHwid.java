package net.java.main.protocol.heypixel;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HeypixelHwid {
    @SerializedName("user")
    public String user;
    @SerializedName("network_hwids")
    public List<String> network_hwids;
    @SerializedName("cpu")
    public String cpu;
    @SerializedName("baseboard_serial")
    public String baseboard_serial;
    @SerializedName("disk_serials")
    public List<String> disk_serials;
    @SerializedName("baseboards")
    public Map<String, String> baseboards;
    @SerializedName("disks")
    public List<Map<String, String>> disks;
    @SerializedName("network_interfaces")
    public List<Map<String, String>> network_interfaces;
    @SerializedName("system")
    public String system;
}
