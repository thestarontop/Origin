package net.java.main.protocol.nel;


import com.mojang.authlib.GameProfile;
import net.java.main.modules.misc.Protocol;
import net.java.main.protocol.nel.myth.MythSessionsProvider;
import net.java.main.protocol.nel.wnf.WNFSessionsProvider;

import static net.java.main.modules.misc.Protocol.NeteaseLauncher.Myth;

public interface GameSessionProvider {
    static GameSessionProvider get() {
        return switch (Protocol.getNEL()) {
            case Myth -> MythSessionsProvider.get();
            case WNF -> WNFSessionsProvider.get();

        };
    }

    void refresh();

    String getPlayerUUID(int port);

    String getUserId(int port);

    String getToken(int port);

    String getPlayerUUID(GameProfile profile);

    String getUserId(GameProfile profile);

    String getToken(GameProfile profile);

    boolean has(int port);

    boolean has(GameProfile profile);

    default boolean has(int port, GameProfile profile) {
        return switch (Protocol.getRequestMode().getValue()) {
            case "Port" -> has(port);
            case "UUID" -> has(profile);
            case "Merge" -> {
                if (profile != null) {
                    yield has(profile);
                } else yield has(port);
            }
            default -> throw new IllegalStateException("Unexpected value: " + Protocol.getRequestMode().getValue());
        };
    }

    default String getPlayerUUID(int port, GameProfile profile) {
        return switch (Protocol.getRequestMode().getValue()) {
            case "Port" -> getUserId(port);
            case "UUID" -> getUserId(profile);
            case "Merge" -> {
                if (profile != null) {
                    var ret = getUserId(profile);

                    if (ret != null && !ret.isEmpty()) yield getUserId(profile);
                    else yield getUserId(port);
                } else yield getUserId(port);
            }
            default -> throw new IllegalStateException("Unexpected value: " + Protocol.getRequestMode().getValue());
        };
    }

    default String getUserId(int port, GameProfile profile) {
        return switch (Protocol.getRequestMode().getValue()) {
            case "Port" -> getUserId(port);
            case "UUID" -> getUserId(profile);
            case "Merge" -> {
                if (profile != null) {
                    var ret = getUserId(profile);

                    if (ret != null && !ret.isEmpty()) yield getUserId(profile);
                    else yield getUserId(port);
                } else yield getUserId(port);
            }
            default -> throw new IllegalStateException("Unexpected value: " + Protocol.getRequestMode().getValue());
        };
    }

    default String getToken(int port, GameProfile profile) {
        return switch (Protocol.getRequestMode().getValue()) {
            case "Port" -> getUserId(port);
            case "UUID" -> getUserId(profile);
            case "Merge" -> {
                if (profile != null) {
                    var ret = getUserId(profile);

                    if (ret != null && !ret.isEmpty()) yield getUserId(profile);
                    else yield getUserId(port);
                } else yield getUserId(port);
            }
            default -> throw new IllegalStateException("Unexpected value: " + Protocol.getRequestMode().getValue());
        };
    }
}
