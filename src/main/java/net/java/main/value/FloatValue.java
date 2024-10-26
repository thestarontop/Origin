package net.java.main.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.java.main.utils.AnimationUtils;

public class FloatValue extends Value<Float> {

    public AnimationUtils barAnimUtils = new AnimationUtils();
    public float barAnim = 0;
    public AnimationUtils xAnimUtils = new AnimationUtils();
    public float xAnim = 0;
    public boolean dragging = false;
    public int startX,startY;
    public FloatValue(String name, Float value, float minimum, float maximum) {
        super(name, value);
        this.minimum = minimum;
        this.maximum = maximum;
    }
    private float minimum;

    private float maximum;
    public float getMinimum() {
        return minimum;
    }
    public float getMaximum() {
        return maximum;
    }

    public void set(Number newValue) {
        setValue(newValue.floatValue());
    }


    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(getValue());
    }

    @Override
    public void fromJson(JsonElement element) {
        if (element.isJsonPrimitive()) {
            setValue(element.getAsFloat());
        }
    }
}
