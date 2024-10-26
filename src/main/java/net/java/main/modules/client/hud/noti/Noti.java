package net.java.main.modules.client.hud.noti;


import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.utils.AnimationUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

public class Noti {
    final String title;
    final String subtitle;
    final int duration;
    final NotiType type;
    public int currDuration;

    protected static Minecraft mc = Minecraft.getInstance();

    public Noti(String title, String subtitle, int duration, NotiType type) {
        this.title = title;
        this.subtitle = subtitle;
        this.duration = duration;
        currDuration = duration;
        this.type = type;
        animX = mc.getWindow().getGuiScaledWidth();
        animY = mc.getWindow().getGuiScaledHeight();
    }

    public String getTitle() {
        return title;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public NotiType getType() {
        return type;
    }


    public float animX = mc.getWindow().getGuiScaledWidth(),animY = mc.getWindow().getGuiScaledHeight();
    public AnimationUtils xAnimUtils = new AnimationUtils();
    public AnimationUtils yAnimUtils = new AnimationUtils();

}