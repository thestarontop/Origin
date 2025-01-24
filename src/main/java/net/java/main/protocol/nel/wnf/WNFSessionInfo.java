package net.java.main.protocol.nel.wnf;

import com.google.gson.annotations.SerializedName;

public class WNFSessionInfo {
    @SerializedName("IsProxyGame")
    public boolean isProxy;
    @SerializedName("ServerPort")
    public int port;
    @SerializedName("EntityId")
    public String entityId;
    @SerializedName("RoleId")
    public String username;
    @SerializedName("Token")
    public String token;
    @SerializedName("PlayerUuid")
    public String playerUuid;
    @SerializedName("PlayerGameUuid")
    public String playerGameUuid = "";
    @SerializedName("GameId")
    public String serverEntityId;
}
