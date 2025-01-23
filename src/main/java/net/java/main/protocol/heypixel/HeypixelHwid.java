package net.java.main.protocol.heypixel;

import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HeypixelHwid {
    public String userId;
    public List<String> networkHardware;
    public String cpuInfo;
    public String baseboardSerial;
    public List<String> diskSerials;
    public Map<String,String> baseboardInfo;
    public List<Map<String,String>> diskStoreInfo;
    public List<Map<String,String>> networkInterfaces;
    public String systemHwid;

    public HeypixelHwid(String userId, ArrayList<String> networkHardwareInfo, String cpuInfo, String baseboardSerial, ArrayList<String> diskSerials, HashMap<String, String> baseboardInfo, List<Map<String, String>> diskStoreInfo, List<Map<String, String>> networkInterfaces, String systemInfo) {
    }
}
