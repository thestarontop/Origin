package net.java.main.protocol.nel.zone;

import com.google.gson.annotations.SerializedName;


public class ZoneSessionInfo {
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
