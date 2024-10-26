package net.java.main.gui;


import com.mojang.blaze3d.vertex.PoseStack;
import net.java.main.command.commands.Bind.BindManager;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;
import net.java.main.utils.ColorUtils;
import net.java.main.utils.Pair;
import net.java.main.utils.RenderUtils;
import net.java.main.value.BooleanValue;
import net.java.main.value.FloatValue;
import net.java.main.value.ListValue;
import net.java.main.value.Value;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;

import java.awt.*;
import java.util.*;
import java.util.List;
import net.java.main.utils.AnimationUtils;

public class ClickGUi extends Screen {
    public ClickGUi() {
        super(new TextComponent("game.clickgui"));
    }

    int x, y;
    int width = 400, height = 300;
    float animWidth = 0, animHeight = 0;
    AnimationUtils animWidthUtil = new AnimationUtils();
    AnimationUtils animHeightUtil = new AnimationUtils();
    float barAnim1 = 0, barAnim2 = 0;
    AnimationUtils barAnim1Util = new AnimationUtils();
    AnimationUtils barAnim2Util = new AnimationUtils();
    int mouseX, mouseY;
    Module.Category currCategory;
    Module currModule;
    int categoryBaseX = 20, categoryBaseY = 20;
    int moduleBaseX = 95, moduleBaseY = 20;
    int settingsBaseX = 170, settingsBaseY = 10;
    int categoryInterval = 4;
    int moduleInterval = 3;
    boolean dragging = false;
    int startX,startY;

    protected static Minecraft mc = Minecraft.getInstance();


    @Override
    public void init() {
        for (Module m : ModuleManager.modules) {
            for (Value<?> v : m.getValues()) {
                if (v instanceof FloatValue) {
                    ((FloatValue) v).dragging = false;
                    ((FloatValue) v).barAnim = 0;
                    ((FloatValue) v).xAnim = 0;
                }
            }
        }
        this.x = mc.screen.width / 2 - 400 / 2;
        this.y = mc.screen.height / 2 - 300 / 2;
        barAnim1 = y;
        barAnim2 = y;

        ArrayList<Module.Category> categories = new ArrayList<>(Arrays.asList(Module.Category.values()));
        categories.sort(Comparator.comparingInt((Module.Category s) -> mc.font.width(s.name())).reversed());

        moduleBaseX = mc.font.width(categories.get(0).name()) + categoryBaseX + 40;

        if (currCategory != null) {
            ArrayList<Module> currModules = new ArrayList<>();
            for (Module m : ModuleManager.modules) {
                if (m.getCategory() == currCategory) {
                    currModules.add(m);
                }
            }
            currModules.sort(Comparator.comparingInt((Module s) -> mc.font.width(s.getName())).reversed());

            settingsBaseX = mc.font.width(currModules.get(0).getName()) + moduleBaseX + 40;

        }
        if (currCategory == null) {
            width = moduleBaseX - 20;
        } else if (currModule == null) {
            width = settingsBaseX - 20;
        } else {
            width = settingsBaseX + 200;
        }

    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float p_96565_) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        int h1 = categoryBaseY * 2 + Module.Category.values().length * mc.font.lineHeight + Module.Category.values().length * categoryInterval;
        if (currCategory == null) {
            width = moduleBaseX - 20;
            currModule = null;
            height = categoryBaseY * 2 + Module.Category.values().length * mc.font.lineHeight + Module.Category.values().length * categoryInterval;
            barAnim1 = y;
            barAnim2 = y;
        } else if (currModule == null) {
            width = settingsBaseX - 20;
            ArrayList<Module> currModules = new ArrayList<>();
            for (Module m : ModuleManager.modules) {
                if (m.getCategory() == currCategory) {
                    currModules.add(m);
                }
            }
            int h2 = moduleBaseY * 2 + currModules.size() * mc.font.lineHeight + currModules.size() * moduleInterval;
            height = Math.max(h2, h1);
            barAnim2 = y;
        } else {
            if (currModule.getValues().isEmpty()) {
                width = settingsBaseX + 200;
            } else {
                width = settingsBaseX + 200;

                ArrayList<Module> currModules = new ArrayList<>();
                for (Module m : ModuleManager.modules) {
                    if (m.getCategory() == currCategory) {
                        currModules.add(m);
                    }
                }
                int h2 = moduleBaseY * 2 + currModules.size() * mc.font.lineHeight + currModules.size() * moduleInterval;
                int vY = 0;
                for (Value<?> v : currModule.getValues()) {
                    if (v instanceof BooleanValue) {
                        vY += mc.font.lineHeight + 2;
                    } else if (v instanceof FloatValue) {
                        vY += mc.font.lineHeight + 13;
                    } else if (v instanceof ListValue) {
                        ListValue l = (ListValue) v;
                        vY += mc.font.lineHeight + 2;
                        if (l.listOpen) {
                            for (String s : l.getValues()) {
                                vY += mc.font.lineHeight + 2;
                            }
                        }
                    }
                }

                height = Math.max(Math.max(vY + settingsBaseY * 2, h1), h2);
            }
        }


        if (currModule != null) {
            for (Value<?> v : currModule.getValues()) {
                if (v instanceof FloatValue) {
                    if (((FloatValue) v).dragging) {
                        FloatValue f = (FloatValue) v;
                        float k = ((float) (mouseX - this.x - settingsBaseX) / (width - 10 - settingsBaseX));
                        if (k < 0) k = 0;
                        if (k > 1) k = 1;
                        f.setValue(f.getMaximum() * k);
                    }
                }
            }
        }

        animWidth = (float) animWidthUtil.animate(width, animWidth, 0.4f);
        animHeight = (float) animHeightUtil.animate(height, animHeight, 0.4f);
        // bg
        RenderUtils.drawRect(poseStack,x,y,x + animWidth,y + animHeight,new Color(0,0,0,100).getRGB());

        // bar
        if (currCategory != null) {
            barAnim1 = (float) barAnim1Util.animate(y + height, barAnim1, 0.2f);
            if (barAnim1 > animHeight + y) barAnim1 = animHeight + y;
            RenderUtils.drawRect(poseStack,x + moduleBaseX - 20,y,x + moduleBaseX - 18,barAnim1,new Color(255,255,255,100).getRGB());
        }
        if (currModule != null) {
            barAnim2 = (float) barAnim2Util.animate(y + height, barAnim2, 0.1f);
            if (barAnim2 > animHeight + y) barAnim2 = animHeight + y;
            RenderUtils.drawRect(poseStack,x + settingsBaseX - 20,y,x + settingsBaseX - 18,barAnim2,new Color(255,255,255,100).getRGB());
        }

        PoseStack poseStack1 = new PoseStack();
        poseStack1.pushPose();
        poseStack1.translate(x + 16,y + 7,0);
        poseStack1.scale(0.8f,0.8f,0.8f);
        // client name
        mc.font.draw(poseStack1, "Origin", 0, 0, ColorUtils.skyRainbow(1,0.9f,0.9f).getRGB());
        poseStack1.popPose();

        drawCategory(poseStack, List.of(Module.Category.values()), x + categoryBaseX, y + categoryBaseY);
        if (currCategory != null) {
            ArrayList<Module> currModules = new ArrayList<>();
            for (Module m : ModuleManager.modules) {
                if (m.getCategory() == currCategory) {
                    currModules.add(m);
                }
            }
            drawModule(poseStack, currModules, x + moduleBaseX, y + moduleBaseY);
        }
        if (currModule != null) {
            drawModuleSettings(poseStack, currModule, x + settingsBaseX, y + settingsBaseY);
        }

    }

    @Override
    public boolean keyPressed(int key, int p_96553_, int p_96554_) {
        for (Module m : ModuleManager.modules) {
            if (m.isKeyListening) {
                m.isKeyListening = false;
                m.setKey(key);
                return false;
            }
        }
        return super.keyPressed(key, p_96553_, p_96554_);
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (mouseX > x + categoryBaseX && mouseX < x + moduleBaseX) {
            Pair<Boolean, Module.Category> categoryMap = clickCategory(List.of(Module.Category.values()), (int) mouseX, (int) mouseY);
            if (categoryMap.getKey() && currCategory != categoryMap.getValue()) {
                currCategory = categoryMap.getValue();
                currModule = null;

                ArrayList<Module> currModules = new ArrayList<>();
                for (Module m : ModuleManager.modules) {
                    if (m.getCategory() == currCategory) {
                        currModules.add(m);
                    }
                }
                currModules.sort(Comparator.comparingInt((Module s) -> mc.font.width(s.getName())).reversed());

                settingsBaseX = mc.font.width(currModules.get(0).getName()) + moduleBaseX + 40;
            }
        }
        if (currCategory != null) {
            if (mouseX > x + moduleBaseX && mouseX < x + settingsBaseX ) {
                ArrayList<Module> currModules = new ArrayList<>();
                for (Module m : ModuleManager.modules) {
                    if (m.getCategory() == currCategory) {
                        currModules.add(m);
                    }
                }

                Pair<Boolean, Module> moduleMap = clickModule(currModules, (int) mouseX, (int) mouseY);
                if (moduleMap.getKey()) {
                    if (mouseButton == 0) {
                        moduleMap.getValue().toggle();
                    } else if (mouseButton == 1) {
                        currModule = moduleMap.getValue();
                    }
                }
            }
        }
        if (currModule != null) {
            if (mouseX > x + settingsBaseX && mouseX < x + width) {
                clickSettings(currModule, (int) mouseX, (int) mouseY, mouseButton);
            }
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    public boolean mouseReleased(double p_97812_, double p_97813_, int p_97814_) {
        if (currModule !=null) {
            for (Value<?> v : currModule.getValues()) {
                if (v instanceof FloatValue) {
                    ((FloatValue) v).dragging = false;
                }
            }
        }

        if (dragging) {
            dragging = false;
            startX = 114514;
            startY = 114514;
        }


        return super.mouseReleased(p_97812_, p_97813_, p_97814_);
    }
    private void drawModule(PoseStack poseStack, List<Module> modules, int x, int y) {
        final int[] mY = {0};
        modules.forEach(module -> {
            if (module == currModule) {
                RenderUtils.drawRect(poseStack,x - 2,y  + mY[0] - 2,x + 2 + mc.font.width(module.getName()),y + mY[0] + mc.font.lineHeight + 2,new Color(255,255,255,100).getRGB());
            }
            int color;
            if (module.isEnabled()) {
                color = ColorUtils.skyRainbow(5,1f,1f).getRGB();
            } else {
                color = Color.white.getRGB();
            }
            mc.font.draw(poseStack, module.getName(), x, y + mY[0], color);
            mY[0] += mc.font.lineHeight + moduleInterval;
        });
    }
    private Pair<Boolean, Module> clickModule(List<Module> modules, int mouseX, int mouseY) {
        final int[] mY = {0};
        for (Module module : modules) {
            if (mouseX >= x + moduleBaseX && mouseX < x + moduleBaseX + mc.font.width(module.getName()) && mouseY >= y + moduleBaseY + mY[0] && mouseY <= y + moduleBaseY + mY[0] + mc.font.lineHeight) {

                Pair<Boolean, Module> map = new Pair<>(true, module);
                return map;
            }
            mY[0] += mc.font.lineHeight + moduleInterval;
        }

        Pair<Boolean, Module> map = new Pair<>(false, null);
        return map;
    }

    private void drawCategory(PoseStack poseStack, List<Module.Category> categories, int x, int y) {
        final int[] cY = {0};
        categories.forEach(category -> {
            int color;
            if (category == currCategory) {
                color = ColorUtils.skyRainbow(3,1f,1f).getRGB();
            } else {
                color = Color.white.getRGB();
            }
            mc.font.draw(poseStack, category.name(), x, y + cY[0], color);
            cY[0] += mc.font.lineHeight + categoryInterval;
        });

    }
    private void drawModuleSettings(PoseStack poseStack, Module module, int x, int y) {
        int vY = 0;

        // keyBind
        if (!module.isKeyListening) {
            mc.font.draw(poseStack, "KeyBind: " + ChatFormatting.AQUA + BindManager.getKeyName(module.getKey()), x, y + vY, Color.white.getRGB());
        } else {
            mc.font.draw(poseStack, "KeyBind: " + ChatFormatting.RED + "Listening ...", x, y + vY, Color.white.getRGB());
        }
        vY += mc.font.lineHeight + 4;

        for (Value<?> v : module.getValues()) {
            if (v instanceof BooleanValue) {
                BooleanValue b = (BooleanValue) v;
                mc.font.draw(poseStack, b.getName(), x, y + vY, Color.white.getRGB());
                RenderUtils.drawRect(poseStack,this.x + width - 30,y + vY,this.x + width - 10,y + vY + 10,new Color(255,255,255,100).getRGB());

                float x1;
                int color1;
                if (b.getValue()) {
                    x1 = 10;
                    color1 = Color.green.getRGB();
                } else {
                    x1 = 0;
                    color1 = Color.red.getRGB();
                }

                b.boolValueAnimX = (float) b.boolValueAnimationUtils.animate(x1,b.boolValueAnimX,0.4);

                RenderUtils.drawRect(poseStack,this.x + width - 29 + b.boolValueAnimX,y + vY + 1,this.x + width - 21 + b.boolValueAnimX,y + vY + 9,color1);
                vY += mc.font.lineHeight + 2;
            } else if (v instanceof FloatValue) {
                FloatValue f = (FloatValue) v;
                // name
                mc.font.draw(poseStack, f.name + ": ", x, y + vY, Color.white.getRGB());

                float x2 = (this.x + width - 10 - x) * (f.getValue() / f.getMaximum());

                f.xAnim = (float) f.xAnimUtils.animate(x2,f.xAnim,0.4);

                float x1 = x + f.xAnim;

                if (x1 <= (mc.font.width(f.name + ": ") + x)) {
                    x1 = mc.font.width(f.name + ": ") + x;
                } else if (x1 > (this.x + this.width - 10 - mc.font.width(f.getValue().toString()))) {
                    x1 = this.x + this.width - 10 - mc.font.width(f.getValue().toString());
                }
                int a = (int) (55 * (f.getValue() / f.getMaximum()));
                // value string
                mc.font.draw(poseStack,f.getValue().toString(),x1,y + vY,new Color(255,255,255,a + 200).getRGB());

                boolean hasButton;
                if (this.mouseX >= x && this.mouseX <= this.x + width - 10 && this.mouseY  >= y + vY + 10 && this.mouseY <= y + vY + 19) {
                    hasButton = true;
                    f.barAnim = (float) f.barAnimUtils.animate(2,f.barAnim,0.4);
                } else {
                    hasButton = false;
                    f.barAnim = (float) f.barAnimUtils.animate(0,f.barAnim,0.4);
                }
                if (f.dragging) hasButton = true;

                // base bar
                RenderUtils.drawRect(poseStack,x,y + vY + 13 - f.barAnim,this.x + width - 10,y + vY + 15 + f.barAnim,new Color(255,255,255, 100).getRGB());

                // value bar
                RenderUtils.drawRect(poseStack,x,y + vY + 13 - f.barAnim,f.xAnim + x,y + vY + 15 + f.barAnim,new Color(50,50,255,255).getRGB());

                if (hasButton) {
                    RenderUtils.drawRect(poseStack,f.xAnim + x - 1.5f - f.barAnim,y + vY + 13 - f.barAnim,f.xAnim + x + 1.5f + f.barAnim,y + vY + 15 + f.barAnim,new Color(255,255,255,255).getRGB());
                }

                vY += mc.font.lineHeight + 13;
            } else if (v instanceof ListValue) {
                ListValue l = (ListValue) v;

                mc.font.draw(poseStack,l.name + ": " + ChatFormatting.BLUE + l.getValue(),x,y + vY,new Color(255,255,255,255).getRGB());
                String a;
                if (l.listOpen) {
                    a = "▼";
                } else {
                    a = "◁";
                }
                mc.font.draw(poseStack,a,this.x + width - 10 - mc.font.width(a),y + vY,new Color(255,255,255,255).getRGB());

                vY += mc.font.lineHeight + 2;

                if (l.listOpen) {
                    for (String s : l.getValues()) {
                        int color = new Color(255,255,255,255).getRGB();
                        if (Objects.equals(s, l.getValue())) {
                            color = new Color(50,50,255,255).getRGB();
                        }


                        mc.font.draw(poseStack," " + s,x,y + vY, color);
                        vY += mc.font.lineHeight + 2;

                    }
                }
            }
        }
    }
    private void clickSettings(Module module,int mouseX, int mouseY,int mouseButton) {
        int vY = 0;

        if (mouseX > this.x + settingsBaseX && mouseX < this.x + width - 10 && mouseY > y + settingsBaseY + vY && mouseY < y + settingsBaseY + vY + 10) {
            if (!module.isKeyListening) {
                module.isKeyListening = true;
            } else {
                if (mouseButton == 1) {
                    module.isKeyListening = false;
                    module.setKey(-1);
                }
            }
        }

        vY += mc.font.lineHeight + 4;

        for (Value<?> v : module.getValues()) {
            if (v instanceof BooleanValue) {
                BooleanValue b = (BooleanValue) v;
                if (mouseX >= this.x + width - 30 && mouseX <= this.x + width - 10 && mouseY >= y + settingsBaseY + vY && mouseY <= y + settingsBaseY + vY + 10) {
                    b.setValue(!b.getValue());
                }
                vY += mc.font.lineHeight + 2;
            } else if (v instanceof FloatValue) {
                FloatValue f = (FloatValue) v;
                if (mouseX >= x + settingsBaseX && mouseX <= this.x + width - 10 && mouseY >= y + settingsBaseY + vY + 11 && mouseY <= y + settingsBaseY + vY + 15) {
                    f.dragging = true;
                    f.startX = mouseX;
                    f.startY = mouseY;
                    float k = ((float) (mouseX - this.x - settingsBaseX) / (width - 10 - settingsBaseX));
                    f.setValue(f.getMaximum() * k);
                }

                vY += mc.font.lineHeight + 13;
            } else if (v instanceof ListValue) {
                ListValue l = (ListValue) v;

                if (mouseX >= x + settingsBaseX && mouseX <= x + width - 10 && mouseY >= y + settingsBaseY + vY && mouseY <= y + settingsBaseY + vY + mc.font.lineHeight) {
                    if (l.listOpen) {
                        l.listOpen = false;
                    } else {
                        l.listOpen = true;
                    }
                }

                vY += mc.font.lineHeight + 2;
                if (l.listOpen) {
                    for (String s : l.getValues()) {
                        if (mouseX >= x + settingsBaseX && mouseX <= x + width - 10 && mouseY >= y + settingsBaseY + vY && mouseY <= y + settingsBaseY + vY + mc.font.lineHeight) {
                            l.setValue(s);
                        }

                        vY += mc.font.lineHeight + 2;
                    }
                }
            }
        }
    }
    private Pair<Boolean, Module.Category> clickCategory(List<Module.Category> categories, int mouseX, int mouseY) {
        final int[] cY = {0};

        for (Module.Category category : categories) {
            if (mouseX >= x + categoryBaseX && mouseX < x + categoryBaseX + mc.font.width(category.name()) && mouseY >= y + categoryBaseY + cY[0] && mouseY <= y + categoryBaseY + cY[0] + mc.font.lineHeight) {

                Pair<Boolean, Module.Category> map = new Pair<>(true, category);
                return map;
            }
            cY[0] += mc.font.lineHeight + categoryInterval;
        }

        Pair<Boolean, Module.Category> map = new Pair<>(false, null);
        return map;
    }
}
