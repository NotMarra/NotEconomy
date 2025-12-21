package com.notmarra.noteconomy.economy;

import java.util.Collection;
import java.util.UUID;

import com.notmarra.notlib.cache.BaseNotCache;
import com.notmarra.notlib.extensions.NotPlugin;

public class EconomyCache extends BaseNotCache<PlayerBalance> {

    public EconomyCache(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String hash(PlayerBalance source) {
        return source.getPlayerUuid().toString() + "_" + source.getCurrencyId();
    }

    public PlayerBalance getBalance(UUID uuid, String currencyId) {
        String key = uuid.toString() + "_" + currencyId;
        return get(key);
    }

    public Collection<PlayerBalance> getAllCachedData() {
        return getStorage().values();
    }
}
