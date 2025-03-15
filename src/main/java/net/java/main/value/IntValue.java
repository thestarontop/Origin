package net.java.main.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.java.main.utils.AnimationUtils;

public class IntValue extends Value<Integer> {
    public boolean dragging = false;
    public float barAnim = 0;
    public float xAnim = 0;
    public AnimationUtils barAnimUtils = new AnimationUtils();
    public AnimationUtils xAnimUtils = new AnimationUtils();
    public IntValue(String name, int value, int minimum, int maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    private int minimum;

    private int maximum;
    public int getMinimum() {
        return minimum;
    }
    public int getMaximum() {
        return maximum;
    }


    public void set(Number newValue) {
        setValue(newValue.intValue());
    }

    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            setValue(element.getAsInt());
        }
    }
}