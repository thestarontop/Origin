package net.java.main.modules.client.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.UpdateEvent;
import net.java.main.modules.client.hud.hud.Border;
import net.java.main.modules.client.hud.hud.Element;
import net.java.main.modules.client.hud.noti.Noti;
import net.java.main.modules.client.hud.noti.NotiType;
import net.java.main.utils.RenderUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Notification extends Element {

    public static List<Noti> notificationList = new ArrayList<>();

    public Notification() {
        super("Notification");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (notificationList.isEmpty()) return;
        for (Noti noti : notificationList) {
            noti.currDuration --;
            if (noti.currDuration <= 0) {
                removeNotification(noti);
            }
        }
    }


    public static void addNotification(Noti noti) {
        notificationList.add(noti);
    }
    public static void removeNotification(Noti noti) {
        notificationList.remove(noti);
    }

    @Override
    public Border draw(PoseStack poseStack) {
        if (notificationList.isEmpty()) return new Border(0,0,0,0);
        for (int i = 0; i < notificationList.size(); i++) {

            Noti noti = notificationList.get(i);
            if (noti != null) {

                int x = mc.getWindow().getGuiScaledWidth() - mc.font.width(noti.getTitle() + " " + noti.getSubtitle()) - 30;
                int y = mc.getWindow().getGuiScaledHeight() - 40 - (i * (mc.font.lineHeight + 10));

                noti.animX = (float) noti.xAnimUtils.animate(x,noti.animX,0.2f);
                noti.animY = (float) noti.yAnimUtils.animate(y,noti.animY,0.2f);

                int color;
                if (noti.getType() == NotiType.INFO) {
                    color = new Color(100, 100, 100, 200).getRGB();
                } else if (noti.getType() == NotiType.ERROR) {
                    color = new Color(255, 100, 100,200).getRGB();
                } else if (noti.getType() == NotiType.SUCCESS) {
                    color = new Color(100, 255, 100,200).getRGB();
                } else {
                    color = new Color(255, 255, 255,200).getRGB();
                }
                RenderUtils.drawRect(poseStack,noti.animX,noti.animY,noti.animX + mc.font.width(noti.getTitle() + " " + noti.getSubtitle()) + 10,noti.animY + mc.font.lineHeight + 10 ,color);

                mc.font.drawShadow(poseStack, noti.getTitle() + " " + noti.getSubtitle(), noti.animX + 5, noti.animY + 5, new Color(255, 255, 255).getRGB());

            }
        }
        return new Border(mc.getWindow().getGuiScaledWidth() - mc.font.width("This is a display Notification") - 30,mc.getWindow().getGuiScaledHeight() - 40,mc.getWindow().getGuiScaledWidth() - 20,mc.getWindow().getGuiScaledHeight() - 20);
    }

    @Override
    public void update() {

    }
}
