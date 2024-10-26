package net.java.main.file;

import java.io.File;
import java.io.IOException;

abstract class FileConfig {

    public File getFile() {
        return file;
    }

    public final File file;

    public FileConfig(File file) {
        this.file = file;
    }


    protected abstract void loadConfig() throws IOException;
    protected abstract void saveConfig() throws IOException;
    protected void createConfig() throws IOException {
        file.createNewFile();
    }

    public boolean hasConfig() {
        return file.exists();
    }
    public abstract File getConfigFile();
}