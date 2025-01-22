package net.java.main.protocol.nel;


import net.java.main.modules.misc.Protocol;
import net.java.main.protocol.nel.myth.MythSessionsProvider;
import net.java.main.protocol.nel.zone.ZoneSessionsProvider;

import static net.java.main.modules.misc.Protocol.NeteaseLauncher.Myth;
import static net.java.main.modules.misc.Protocol.NeteaseLauncher.Zone;

public interface GameSessionProvider {
    void refresh();

    String getPlayerUUID(int port);
    String getUserId(int port);
    String getToken(int port);

    static GameSessionProvider get() {
        return switch (Protocol.getNEL()) {
            case Zone -> ZoneSessionsProvider.get();
            case Myth -> MythSessionsProvider.get();
        };
    }
}
