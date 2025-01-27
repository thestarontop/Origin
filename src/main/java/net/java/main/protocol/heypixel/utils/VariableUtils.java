package net.java.main.protocol.heypixel.utils;



import net.java.main.protocol.heypixel.msgpack.value.Value;
import net.java.main.protocol.heypixel.msgpack.value.ValueFactory;
import net.java.main.protocol.heypixel.msgpack.value.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VariableUtils {
    public static Variable stringsToVariable(List<String> list) {
        var values = new ArrayList<Value>();
        for (String s : list) {
            values.add(ValueFactory.newString(s));
        }
        return new Variable().setArrayValue(values);
    }

    public static Variable stringMapToVariable(Map<String, String> map) {
        var valueMap = new HashMap<Value, Value>();
        for (var s : map.entrySet()) {
            valueMap.put(ValueFactory.newString(s.getKey()), ValueFactory.newString(s.getValue()));
        }
        return new Variable().setMapValue(valueMap);
    }

    public static Variable stringsMapToVariable(List<Map<String, String>> list) {
        var values = new ArrayList<Value>();
        for (var s : list) {
            values.add(stringMapToVariable(s));
        }
        return new Variable().setArrayValue(values);
    }
}
