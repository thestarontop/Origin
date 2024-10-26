package net.java.main.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.java.main.utils.AnimationUtils;

public class BooleanValue extends Value<Boolean> {

    public float boolValueAnimX;
    public AnimationUtils boolValueAnimationUtils = new AnimationUtils();
    public BooleanValue(String name,boolean value) {
        super(name,value);
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            setValue(element.getAsBoolean() || element.getAsString().equals("true"));
        }
    }
}
