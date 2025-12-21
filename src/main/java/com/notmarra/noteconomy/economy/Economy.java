package com.notmarra.noteconomy.economy;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;
import java.util.Set;

public class Economy {
    private final NotEconomy instance = NotEconomy.getInstance();
    private final IEconomyDatabase db = NotEconomy.getDB();
    private final EconomyCache cache = NotEconomy.getEC();

    public void setupPlayer(Player p) {
        if (!db.playerExists(p)) {
            db.setupPlayer(p);
        }

        FileConfiguration currencyConfig = instance.getSubConfig("currencies.yml");
        if (currencyConfig == null)
            return;

        Set<String> currencyIds = currencyConfig.getKeys(false);
        for (String id : currencyIds) {
            double balance = db.getBalance(p, id);

            cache.store(new PlayerBalance(p.getUniqueId(), id, balance));
        }
    }

    public double getBalance(Player p, String currencyId) {
        String hash = p.getUniqueId().toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);

        return (pb != null) ? pb.getBalance() : 0.0;
    }
}