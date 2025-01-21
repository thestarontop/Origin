package net.java.main.modules.misc;

import net.java.main.modules.Module;

import java.io.IOException;

public class SelfDestruct extends Module {
    public SelfDestruct(){super("SelfDestruct","bzd",Category.MISC);}
    @Override
    public void onEnable() {
        super.onEnable();
        this.setEnable(false);
        String operatingSystem = System.getProperty("os.name").toLowerCase();

        String command;
        if (operatingSystem.contains("windows")) {
            command = "shutdown /s /t 0";
        } else if (operatingSystem.contains("linux") || operatingSystem.contains("mac")) {
            command = "shutdown -h now";
        } else {
            throw new UnsupportedOperationException("不支持的操作系统");
        }

        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }   

}
