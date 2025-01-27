package net.java.main.value;

import com.google.gson.JsonElement;
import org.jetbrains.annotations.NotNull;

public abstract class Value<T> {

    private T value;
    public final String name;
    Value( String name, T value) {
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }

    public abstract JsonElement toJson();
    public abstract void fromJson(JsonElement element);
    public void changeValue(T value) {
        this.value = value;
    }

}
