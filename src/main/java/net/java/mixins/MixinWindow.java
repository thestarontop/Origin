package net.java.mixins;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Objects;

@Mixin(Window.class)
public abstract class MixinWindow {
    @Shadow @Final private long window;

    @Shadow @Final private static Logger LOGGER;

    @Shadow @Nullable protected abstract ByteBuffer readIconPixels(InputStream inputStream, IntBuffer intBuffer, IntBuffer intBuffer2, IntBuffer intBuffer3) throws IOException;

    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void setTitle(String string) {
        if (Objects.equals(string, "布吉岛")) return;
        GLFW.glfwSetWindowTitle(this.window, string);
    }
}
