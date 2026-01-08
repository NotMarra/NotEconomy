package com.notmarra.noteconomy;

import org.bukkit.plugin.ServicePriority;

import com.notmarra.noteconomy.economy.Economy;
import com.notmarra.noteconomy.economy.EconomyCache;
import com.notmarra.noteconomy.economy.EconomyCommandGroup;
import com.notmarra.noteconomy.economy.PlayerBalance;
import com.notmarra.noteconomy.hooks.EconomyPlaceholder;
import com.notmarra.noteconomy.hooks.VaultHook;
import com.notmarra.noteconomy.listeners.PlayerJoin;
import com.notmarra.noteconomy.listeners.PlayerQuit;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.noteconomy.utils.UniversalCommandGroup;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;
import com.notmarra.noteconomy.utils.Database.MySQL;
import com.notmarra.noteconomy.utils.Database.SQLite;
import com.notmarra.notlib.cache.NotCache;
import com.notmarra.notlib.extensions.NotLangId;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.ChatF;
import com.notmarra.notlib.utils.NotDebugger;
import com.notmarra.notlib.utils.NotScheduler;

public final class NotEconomy extends NotPlugin {
    private static NotEconomy instance;
    private NotDebugger debugger;
    private IEconomyDatabase dbInstance;
    private EconomyCache economyCache;

    @Override
    public void initNotPlugin() {
        instance = this;
        saveDefaultConfig("currencies.yml");
        saveDefaultConfig("lang/en.yml");
        reloadConfig("currencies.yml");
        String type = getConfig().getString("data.type", "SQLite");
        tm().setDefaultLangFolder("lang");
        tm().registerLangs(NotLangId.EN);

        if (type.equalsIgnoreCase("MySQL")) {
            dbInstance = new MySQL(this, "config.yml");
        } else {
            dbInstance = new SQLite(this, "config.yml");
        }

        db().registerDatabase(dbInstance.asNotDatabase());

        addListener(new PlayerJoin(instance));
        addListener(new PlayerQuit(this));

        this.economyCache = new EconomyCache(instance);
        NotCache.getInstance().registerCache("balances", economyCache);
        updateTopCache();

        NotScheduler scheduler = new NotScheduler(instance);
        scheduler.runTaskTimerAsync(() -> {
            saveCacheToDatabase();
            updateTopCache();
        }, 6000L, 6000L);

        addCommandGroup(new EconomyCommandGroup(instance));
        addCommandGroup(new UniversalCommandGroup(instance));
    }

    @Override
    public void onNotPluginEnable() {
        instance = this;
        debugger = new NotDebugger(instance);

        if (getConfig().getBoolean("vault.enabled", false)
                && getServer().getPluginManager().getPlugin("Vault") != null) {
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultHook(), this,
                    ServicePriority.Normal);
            log().info("NotEconomy successfully hooked into Vault!");
        }

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new EconomyPlaceholder(this).register();
            log().info("PlaceholderAPI expansion registered successfully!");
        }

        log().info(ChatF.of("NotEconomy started succefully!").build());
    }

    @Override
    public void onNotPluginDisable() {
        if (economyCache != null) {
            saveCacheToDatabase();
            NotCache.getInstance().unregisterCache("balances");
        }
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

    private void saveCacheToDatabase() {
        EconomyCache cache = NotEconomy.getEC();
        var allBalances = cache.getAllCachedData();

        if (allBalances.isEmpty())
            return;

        for (PlayerBalance pb : allBalances) {
            getDB().updateBalance(pb.getPlayerUuid(), pb.getCurrencyId(), pb.getBalance());
        }
    }

    private void updateTopCache() {
        for (String currency : Currencies.getCurrencies(instance)) {
            new Economy().updateTopCache(currency);
        }
    }

    private void reloadC() {
        NotScheduler scheduler = new NotScheduler(instance);
        scheduler.runTaskAsync(() -> {
            if (economyCache != null) {
                saveCacheToDatabase();
                NotCache.getInstance().unregisterCache("balances");
                economyCache = null;
            }
        });
        scheduler.runTaskAsync(() -> {
            this.economyCache = new EconomyCache(instance);
            NotCache.getInstance().registerCache("balances", economyCache);
            updateTopCache();
        });
    }

    public static void reloadCache() {
        getInstance().reloadC();
    }
}
