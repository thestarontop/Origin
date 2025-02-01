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
    /**
     * @author starontop
     * @reason bzd
     */
    @Overwrite
    public void setIcon(InputStream inputStream, InputStream inputStream2) {
        RenderSystem.assertInInitPhase();
        try {
            inputStream = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("madebystarontopandfml", "textures/icon/icon_16x16.png")).getInputStream();
            inputStream2 = Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("madebystarontopandfml", "textures/icon/icon_32x32.png")).getInputStream();
            MemoryStack memorystack = MemoryStack.stackPush();

            try {

                IntBuffer intbuffer = memorystack.mallocInt(1);
                IntBuffer intbuffer1 = memorystack.mallocInt(1);
                IntBuffer intbuffer2 = memorystack.mallocInt(1);
                GLFWImage.Buffer buffer = GLFWImage.mallocStack(2, memorystack);
                ByteBuffer bytebuffer = this.readIconPixels(inputStream, intbuffer, intbuffer1, intbuffer2);
                if (bytebuffer == null) {
                    throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
                }

                buffer.position(0);
                buffer.width(intbuffer.get(0));
                buffer.height(intbuffer1.get(0));
                buffer.pixels(bytebuffer);
                ByteBuffer bytebuffer1 = this.readIconPixels(inputStream2, intbuffer, intbuffer1, intbuffer2);
                if (bytebuffer1 == null) {
                    throw new IllegalStateException("Could not load icon: " + STBImage.stbi_failure_reason());
                }

                buffer.position(1);
                buffer.width(intbuffer.get(0));
                buffer.height(intbuffer1.get(0));
                buffer.pixels(bytebuffer1);
                buffer.position(0);
                GLFW.glfwSetWindowIcon(this.window, buffer);
                STBImage.stbi_image_free(bytebuffer);
                STBImage.stbi_image_free(bytebuffer1);
            } catch (Throwable var11) {
                if (memorystack != null) {
                    try {
                        memorystack.close();
                    } catch (Throwable var10) {
                        var11.addSuppressed(var10);
                    }
                }

                throw var11;
            }

            if (memorystack != null) {
                memorystack.close();
            }
        } catch (IOException var12) {
            LOGGER.error("Couldn't set icon", var12);
        }

    }
}
