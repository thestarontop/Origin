package net.java.main.protocol.nel.all;

import com.mojang.authlib.GameProfile;
import net.java.main.protocol.nel.GameSessionProvider;
import net.java.main.protocol.nel.myth.MythSessionsProvider;
import net.java.main.protocol.nel.wnf.WNFSessionsProvider;


import java.util.ArrayList;
import java.util.List;

public class AllNELSessionsProvider implements GameSessionProvider {
    private static AllNELSessionsProvider sInstance;
    private final List<GameSessionProvider> providers = new ArrayList<>();

    public static AllNELSessionsProvider get() {
        if (sInstance == null) {
            sInstance = new AllNELSessionsProvider();
        }
        return sInstance;
    }

    @Override
    public void refresh() {
        var myth = MythSessionsProvider.get();
        var wnf = WNFSessionsProvider.get();

        providers.clear();
        providers.add(myth);
        providers.add(wnf);

        for (GameSessionProvider provider : providers) {
            try {
                provider.refresh();
            } catch (Exception e) {
          //      Genshin.LOG.warn("Failed to refresh provider: {}", provider.getClass().getName());
//                e.printStackTrace();
            }
        }
    }

    private GameSessionProvider find(int port, GameProfile profile) {
        return providers.stream().filter(p -> p.has(port, profile)).findFirst().orElse(null);
    }

    @Override
    public String getPlayerUUID(int port) {
        var provider = find(port, null);
        return provider == null ? "" : provider.getPlayerUUID(port);
    }

    @Override
    public String getUserId(int port) {
        var provider = find(port, null);
        return provider == null ? "" : provider.getUserId(port);
    }

    @Override
    public String getToken(int port) {
        var provider = find(port, null);
        return provider == null ? "" : provider.getToken(port);
    }

    @Override
    public String getPlayerUUID(GameProfile profile) {
        var provider = find(-1, profile);
        return provider == null ? "" : provider.getPlayerUUID(profile);
    }

    @Override
    public String getUserId(GameProfile profile) {
        var provider = find(-1, profile);
        return provider == null ? "" : provider.getUserId(profile);
    }

    @Override
    public String getToken(GameProfile profile) {
        var provider = find(-1, profile);
        return provider == null ? "" : provider.getToken(profile);
    }

    @Override
    public boolean has(int port) {
        return find(port, null) != null;
    }

    @Override
    public boolean has(GameProfile profile) {
        return find(-1, profile) != null;
    }
}
