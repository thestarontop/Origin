package net.java.main.modules.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.event.annotations.EventTarget;
import net.java.main.event.events.Render2DEvent;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.ModuleManager;
import net.java.main.utils.RenderUtils;
import net.java.main.value.ListValue;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.FormattedText;
import org.lwjgl.glfw.GLFW;
import net.java.main.modules.Module;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HUD extends Module {
    public static int realMouseX = 0, realMouseY = 0;
    static int xOffset = -1;
    static int yOffset = -1;
    SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");
    public static ListValue mode = new ListValue("type", new String[]{"Left", "Right"},"Right");

    public HUD() {
        super("HUD","HUD", Category.CLIENT, GLFW.GLFW_KEY_UNKNOWN);
        setEnable(true);
        addValues(mode);
    }

    @EventTarget
    public void onRender(Render2DEvent event) {
        mc.font.drawShadow(new PoseStack(), madebystarontopandfml.NAME+"-"+ madebystarontopandfml.VERSION, 4, 1, RenderUtils.getRainbowOpaque(0.9f));
        mc.font.drawShadow(new PoseStack(), "("+HOUR_FORMAT.format(new Date(System.currentTimeMillis()))+")",92 , 1.3f, -1);
        if (mode.getValue() == "Right") {

            int screenWidth = mc.getWindow().getGuiScaledWidth();
            float y = 0;
            List<Module> modenable = madebystarontopandfml.getInstance().getModuleManager().getEnableMods();
            modenable.sort(Comparator.<Module>comparingDouble(m -> {
                String name = m.getName();
                ;
                return mc.font.width(name);
            }).reversed());
            for (Module module : modenable) {
                mc.font.drawShadow(new PoseStack(), module.getName(), screenWidth - mc.font.width(module.getName()), y, RenderUtils.getRainbowOpaque(0.5f));
                y += 10;
            }
        }else {
            List<String> enabled = new ArrayList<>();
            for (Module module : ModuleManager.modules) {
                if (module.isEnabled()) {
                    enabled.add(module.getName());
                }
            }
            enabled.sort((s1, s2) -> Integer.compare(mc.font.width(s2), mc.font.width(s1)));
            float y = 0;
            for (String str : enabled) {
                mc.font.drawShadow(new PoseStack(), str, 4, 14 + y, RenderUtils.getRainbowOpaque(0.5f));
                y += 10;
            }
        }


    }

}
