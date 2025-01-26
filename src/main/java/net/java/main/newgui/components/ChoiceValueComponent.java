package net.java.main.newgui.components;

import com.mojang.blaze3d.vertex.PoseStack;

import net.java.main.newgui.Component;
import net.java.main.newgui.ModuleRenderer;
import net.java.main.value.ListValue;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.List;

import static net.java.main.utils.MinecraftInstance.mc;

public class ChoiceValueComponent extends Component {
    private final ListValue choiceValue;

    public ChoiceValueComponent(ListValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        this.choiceValue = value;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = this.choiceValue.getName() + ": " + this.choiceValue.getValue();
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(mouseX, mouseY, this.parent.parent.x ,this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height) && mouseButton == 0) {
            List<String> modes = Arrays.asList(this.choiceValue.getValues());
            int currentIndex = modes.indexOf(this.choiceValue.getValue());
            int nextIndex = (currentIndex + 1) % modes.size();
            this.choiceValue.setValue(modes.get(nextIndex));
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
