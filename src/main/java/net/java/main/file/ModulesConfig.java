package net.java.main.file;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import net.java.main.madebystarontopandfml;
import net.java.main.modules.Module;
import net.java.main.modules.ModuleManager;
import net.java.main.value.Value;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class ModulesConfig extends FileConfig {

    public ModulesConfig(final File file) {
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
                if (module.isEnabled() != jsonModule.get("State").getAsBoolean()) {
                    module.setEnable(jsonModule.get("State").getAsBoolean());
                }
                if (module.getKey() != jsonModule.get("KeyBind").getAsInt()) {
                    module.setKey(jsonModule.get("KeyBind").getAsInt());
                }
                for (final Value moduleValue : module.getValues()) {
                    final JsonElement element = jsonModule.get(moduleValue.getName());

                    if (element != null) moduleValue.fromJson(element);
                }
            }
        }
    }

    @Override
    public void saveConfig() throws IOException {
        final JsonObject jsonObject = new JsonObject();

        for (final Module module : ModuleManager.modules) {
            final JsonObject jsonMod = new JsonObject();
            jsonMod.addProperty("State", module.isEnabled());
            jsonMod.addProperty("KeyBind", module.getKey());
            module.getValues().forEach(value -> jsonMod.add(value.getName(), value.toJson()));
            jsonObject.add(module.getName(), jsonMod);
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
