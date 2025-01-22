package net.java.main.protocol.nel.myth;

import com.google.gson.annotations.SerializedName;



public class MythSessionInfo {
    @SerializedName("port")
    public int port;
    @SerializedName("entityId")
    public String entityId;
    @SerializedName("roleId")
    public String username;
    @SerializedName("dToken")
    public String token;
    @SerializedName("playerUuid")
    public String playerUuid;
    @SerializedName("playerGameUuid")
    public String playerGameUuid = "";
    @SerializedName("serverEntityId")
    public String serverEntityId;
}
