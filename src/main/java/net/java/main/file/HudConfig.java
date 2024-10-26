package net.java.main.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;
import net.java.main.modules.client.ClickGui;
import net.java.main.modules.client.HUD;
import net.java.main.modules.client.hud.hud.Element;

import java.io.*;
import java.util.Map;


public class HudConfig extends FileConfig {
    public HudConfig(File file) {
        super(file);
    }

    @Override
    public void loadConfig() throws IOException {
        final JsonElement jsonElement = new JsonParser().parse(new BufferedReader(new FileReader(getFile())));

        if(jsonElement instanceof JsonNull)
            return;

        for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
            final Module module = madebystarontopandfml.getInstance().getModuleManager().getModule(entry.getKey());

            if (module != null) {
                final JsonObject jsonModule = (JsonObject) entry.getValue();

                Element e = (Element) module;

                e.renderX = jsonModule.get("renderX").getAsInt();
                e.renderY = jsonModule.get("renderY").getAsInt();
                e.scale = jsonModule.get("scale").getAsFloat();
            }
        }
    }

    @Override
    public void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();

        for (final Module module : ModuleManager.modules) {
            if (module.getCategory() != Module.Category.CLIENT) {
                break;
            } else {
                if(module instanceof ClickGui || module instanceof HUD) break;
            }

            Element e = (Element) module;

            final JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("renderX", e.renderX);
            jsonMod.addProperty("renderY", e.renderY);
            jsonMod.addProperty("scale", e.getScale());
            jsonObject.add(e.getName(), jsonMod);
        }

        final PrintWriter printWriter = new PrintWriter(new FileWriter(getFile()));
        printWriter.println(FileManager.PRETTY_GSON.toJson(jsonObject));
        printWriter.close();
    }

    @Override
    public File getConfigFile() {
        return file;
    }
}
