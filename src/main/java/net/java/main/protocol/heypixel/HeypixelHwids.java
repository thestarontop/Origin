package net.java.main.protocol.heypixel;

import com.google.gson.annotations.SerializedName;



import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class HeypixelHwids {
    @SerializedName("hwids")
    public List<HeypixelHwid> hwids = new ArrayList<>();

    public boolean has(String s) {
        return hwids.stream().anyMatch(h -> s.equals(h.user));
    }

    public HeypixelHwid get(String s) {
        return hwids.stream().filter(h -> h.user.equals(s)).findFirst().orElse(null);
    }

    public void add(HeypixelHwid hwid) {
        if (!has(hwid.user)) {
            hwids.add(hwid);
        }
    }
}
