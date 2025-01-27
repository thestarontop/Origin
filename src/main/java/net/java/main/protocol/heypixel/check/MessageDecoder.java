package net.java.main.protocol.heypixel.check;

import java.nio.charset.StandardCharsets;

public class MessageDecoder {
    private int[] keys;

    public MessageDecoder(int[] decodeKeys) {
        this.keys = decodeKeys;
    }

    public void update(int[] keys) {
        this.keys = keys;
    }

    public String decode(byte[] bArr) {
        return decode(new String(bArr, StandardCharsets.UTF_8));
    }

    public String decode(String str) {
        return decodeMessage(str);
    }

    private int decodeInt(int i) {
        int i2 = i >> 2;
        return (i2 | 1) - ((1 | ((-i2) - 1)) - ((-i2) - 1));
    }

    private char decodeChar(int i) {
        int i2 = i >> 2;
        return (char) ((i2 | 1) - ((1 | ((-i2) - 1)) - ((-i2) - 1)));
    }

    private String decodeMessage(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i += 4) {
            String sub = str.substring(i, i + 4);
            int val = Integer.parseInt(sub);
            int parseInt1 = Integer.parseInt(sub.substring(0, 1));
            if (parseInt1 == 1) {
                val = subInteger(val);
            }
            int result;
            if (parseInt1 == 1) {
                val = keys[val];
                result = decodeChar(val);
                sb.append((char) result);
            } else {
                result = decodeInt(val);
                sb.append(result);
            }
        }

        return sb.toString();
    }

    public int subInteger(int i) {
        return Integer.parseInt(Integer.toString(i).substring(1));
    }
}
