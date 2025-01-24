package net.java.main.protocol.nel.wnf;

import com.google.gson.annotations.SerializedName;
import com.mojang.authlib.GameProfile;
import net.java.main.protocol.nel.GameSessionProvider;
import net.java.main.utils.network.Http;


import java.util.ArrayList;
import java.util.List;

public class WNFSessionsProvider implements GameSessionProvider {
    private static WNFSessionsProvider sInstance;
    public final List<WNFSessionInfo> sessions = new ArrayList<>();

    public static WNFSessionsProvider get() {
        if (sInstance == null) {
            sInstance = new WNFSessionsProvider();
        }
        return sInstance;
    }

    @Override
    public void refresh() {
        try {
            WNFResponse response = Http.get("http://127.0.0.1:23550/getGames")
                .sendJson(WNFResponse.class);

            sessions.clear();
            sessions.addAll(response.data.stream().filter(s -> s.isProxy).toList());
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    @Override
    public String getPlayerUUID(int port) {
        return sessions.stream().filter(s -> s.port == port).findFirst().orElse(new WNFSessionInfo()).playerGameUuid;
    }

    @Override
    public String getUserId(int port) {
        return sessions.stream().filter(s -> s.port == port).findFirst().orElse(new WNFSessionInfo()).entityId;
    }

    @Override
    public String getToken(int port) {
        return sessions.stream().filter(s -> s.port == port).findFirst().orElse(new WNFSessionInfo()).token;
    }

    @Override
    public String getPlayerUUID(GameProfile profile) {
        return sessions.stream().filter(s -> s.playerGameUuid.equals(profile.getId().toString())).findFirst().orElse(new WNFSessionInfo()).playerGameUuid;
    }

    @Override
    public String getUserId(GameProfile profile) {
        return sessions.stream().filter(s -> s.playerGameUuid.equals(profile.getId().toString())).findFirst().orElse(new WNFSessionInfo()).entityId;
    }

    @Override
    public String getToken(GameProfile profile) {
        return sessions.stream().filter(s -> s.playerGameUuid.equals(profile.getId().toString())).findFirst().orElse(new WNFSessionInfo()).token;
    }

    @Override
    public boolean has(int port) {
        return sessions.stream().anyMatch(s -> s.port == port);
    }

    @Override
    public boolean has(GameProfile profile) {
        return sessions.stream().anyMatch(s -> s.playerGameUuid.equals(profile.getId().toString()));
    }

    public static class WNFResponse {
        @SerializedName("code")
        public int code;
        @SerializedName("message")
        public String msg;
        @SerializedName("count")
        public int count;
        @SerializedName("entities")
        public List<WNFSessionInfo> data;
    }
}
