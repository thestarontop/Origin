package net.java.main.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class ListValue extends Value<String> {

    public String[] getValues() {
        return values;
    }

    private String[] values;
    public boolean listOpen = false;

    public ListValue(String name, String[] values, String value) {
        super(name, value);
        this.values = values;
        this.setValue(value);
    }

    public boolean equal(String other) {
        return this.getValue().equalsIgnoreCase(other);
    }

    public boolean contains(String string) {
        return Arrays.stream(values).anyMatch(s -> s.equalsIgnoreCase(string));
    }

    @Override
    public void changeValue(String value) {
        for (String element : values) {
            if (element.equalsIgnoreCase(value)) {
                this.setValue(element);
                break;
            }
        }
    }

    @Override
    public JsonPrimitive toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            changeValue(element.getAsString());
        }
    }
}
