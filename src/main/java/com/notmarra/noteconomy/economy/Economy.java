package com.notmarra.noteconomy.economy;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;

public class Economy {
    private final IEconomyDatabase db = NotEconomy.getDB();

    public void setupPlayer(UUID uuid) {
        db.setupPlayer(uuid);
        EconomyCache cache = NotEconomy.getEC();

        for (String id : Currencies.getCurrencies(NotEconomy.getInstance())) {
            double balance = db.getBalance(uuid, id);

            cache.store(new PlayerBalance(uuid, id, balance));
        }
    }

    public void setupPlayer(Player p) {
        setupPlayer(p.getUniqueId());
    }

    public double getBalance(UUID uuid, String currencyId) {
        EconomyCache cache = NotEconomy.getEC();
        String hash = uuid.toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);

        return (pb != null) ? pb.getBalance() : db.getBalance(uuid, currencyId);
    }

    public double getBalance(Player p, String currencyId) {
        return getBalance(p.getUniqueId(), currencyId);
    }

    public void setBalance(UUID uuid, String currencyId, double balance) {
        EconomyCache cache = NotEconomy.getEC();
        String hash = uuid.toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);
        if (pb != null) {
            pb.setBalance(balance);
        } else {
            db.setBalance(uuid, currencyId, balance);
        }
    }

    public void setBalance(Player p, String currencyId, double balance) {
        setBalance(p.getUniqueId(), currencyId, balance);
    }

    public void addBalance(UUID uuid, String currencyId, double amount) {
        double current = getBalance(uuid, currencyId);
        setBalance(uuid, currencyId, current + amount);
    }

    public void addBalance(Player p, String currencyId, double amount) {
        addBalance(p.getUniqueId(), currencyId, amount);
    }

    public boolean hasBalance(UUID uuid, String currencyId, double amount) {
        return getBalance(uuid, currencyId) >= amount;
    }

    public boolean hasBalance(Player p, String currencyId, double amount) {
        return hasBalance(p.getUniqueId(), currencyId, amount);
    }

    public boolean withdrawBalance(UUID uuid, String currencyId, double amount) {
        if (!hasBalance(uuid, currencyId, amount))
            return false;
        addBalance(uuid, currencyId, -amount);
        return true;
    }

    public boolean withdrawBalance(Player p, String currencyId, double amount) {
        return withdrawBalance(p.getUniqueId(), currencyId, amount);
    }

    public String format(double amount, String currencyId) {
        FileConfiguration config = NotEconomy.getInstance().getSubConfig("currencies.yml");
        if (config == null)
            return String.valueOf(amount);

        String symbol = config.getString(currencyId + ".symbol", "");
        boolean useDecimals = config.getBoolean(currencyId + ".decimal", true);
        String plural = config.getString(currencyId + ".plural", "");
        String singular = config.getString(currencyId + ".singular", "");
        String name = (amount == 1)
                ? config.getString(currencyId + ".singular", "")
                : config.getString(currencyId + ".plural", "");

        String formattedAmount;
        if (amount >= 1000) {
            formattedAmount = formatBigNumber(amount);
        } else {
            formattedAmount = useDecimals
                    ? String.format("%.2f", amount)
                    : String.valueOf((long) amount);
        }

        String pattern = NotEconomy.getInstance().tm("economy.format");
        return pattern
                .replace("%symbol%", symbol)
                .replace("%amount%", formattedAmount)
                .replace("%name%", name)
                .replace("%plural%", plural)
                .replace("%singular%", singular);
    }

    private String formatBigNumber(double amount) {
        if (amount >= 1_000_000_000)
            return String.format("%.1fB", amount / 1_000_000_000.0);
        if (amount >= 1_000_000)
            return String.format("%.1fM", amount / 1_000_000.0);
        if (amount >= 1_000)
            return String.format("%.1fk", amount / 1_000.0);
        return String.valueOf(amount);
    }
}