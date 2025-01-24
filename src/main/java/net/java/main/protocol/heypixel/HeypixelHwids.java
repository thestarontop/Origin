package net.java.main.protocol.heypixel;

import com.google.gson.annotations.SerializedName;



import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class HeypixelHwids {
    @SerializedName("hwids")
    public List<HeypixelHwid> hwids = new ArrayList<>();

    public boolean has(String s) {
        return hwids.stream().anyMatch(h -> h.userId.equals(s));
    }

    public HeypixelHwid get(String s) {
        return hwids.stream().filter(h -> h.userId.equals(s)).findFirst().orElse(null);
    }

    public void add(HeypixelHwid hwid) {
        if (!has(hwid.userId)) {
            hwids.add(hwid);
        }
    }
}
