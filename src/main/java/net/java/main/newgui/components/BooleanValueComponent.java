package net.java.main.newgui.components;

import com.mojang.blaze3d.vertex.PoseStack;

import net.java.main.newgui.Component;
import net.java.main.newgui.ModuleRenderer;
import net.java.main.value.BooleanValue;
import net.minecraft.client.Minecraft;

import static net.java.main.utils.MinecraftInstance.mc;

public class BooleanValueComponent extends Component {
    private final BooleanValue booleanValue;

    public BooleanValueComponent(BooleanValue value, ModuleRenderer parent, int offset) {
        super(value, parent, offset);
        this.booleanValue = value;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta, int x, int y, int width, int height) {
        super.render(stack, mouseX, mouseY, delta, x, y, width, height);
        String text = this.booleanValue.getName() + ": " + this.booleanValue.getValue();
        mc.font.drawShadow(stack, text, (float) (x + 5), (float) (y + (height / 2 - 9 / 2)), this.booleanValue.getValue() ? 5635925 : 16733525);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (this.isHovered(
                mouseX, mouseY, this.parent.parent.x ,this.parent.parent.y + this.parent.offset + this.offset, this.parent.parent.width, this.parent.parent.height
        )
                && mouseButton == 0) {
            this.booleanValue.setValue(!this.booleanValue.getValue());
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }
}
