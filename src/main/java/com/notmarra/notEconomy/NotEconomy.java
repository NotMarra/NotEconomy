package com.notmarra.noteconomy;

import com.notmarra.noteconomy.economy.EconomyCache;
import com.notmarra.noteconomy.listeners.PlayerJoin;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;
import com.notmarra.noteconomy.utils.Database.MySQL;
import com.notmarra.noteconomy.utils.Database.SQLite;
import com.notmarra.notlib.cache.NotCache;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.ChatF;
import com.notmarra.notlib.utils.NotDebugger;

public final class NotEconomy extends NotPlugin {
    private static NotEconomy instance;
    private NotDebugger debugger;
    private IEconomyDatabase dbInstance;
    private EconomyCache economyCache;

    @Override
    public void initNotPlugin() {
        saveDefaultConfig("currencies.yml");
        reloadConfig("currencies.yml");
        String type = getConfig().getString("data.type", "SQLite");

        if (type.equalsIgnoreCase("MySQL")) {
            dbInstance = new MySQL(this, "config.yml");
        } else {
            dbInstance = new SQLite(this, "config.yml");
        }

        db().registerDatabase(dbInstance.asNotDatabase());

        addListener(new PlayerJoin(instance));
        NotCache.getInstance().registerCache("balances", economyCache);
    }

    @Override
    public void onNotPluginEnable() {
        instance = this;
        NotCache.getInstance().unregisterCache("balances");

        log().info(ChatF.of("NotEconomy started succefully!").build());
    }

    @Override
    public void onNotPluginDisable() {
        db().close();
        log().info(ChatF.of("NotEconomy shut down succefully!").build());
    }

    public NotDebugger getDebugger() {
        return debugger;
    }

    public static NotEconomy getInstance() {
        return instance;
    }

    public static NotDebugger dbg() {
        return getInstance().getDebugger();
    }

    public IEconomyDatabase DB() {
        return dbInstance;
    }

    public static IEconomyDatabase getDB() {
        return getInstance().DB();
    }

    public EconomyCache getEconomyCache() {
        return economyCache;
    }

    public static EconomyCache getEC() {
        return getInstance().getEconomyCache();
    }
}
