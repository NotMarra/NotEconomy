package com.notmarra.noteconomy.economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;

public class Economy {
    private final IEconomyDatabase db = NotEconomy.getDB();

    public void setupPlayer(Player p) {
        db.setupPlayer(p);
        EconomyCache cache = NotEconomy.getEC();

        for (String id : Currencies.getCurrencies(NotEconomy.getInstance())) {
            double balance = db.getBalance(p, id);

            cache.store(new PlayerBalance(p.getUniqueId(), id, balance));
        }
    }

    public double getBalance(Player p, String currencyId) {
        EconomyCache cache = NotEconomy.getEC();
        String hash = p.getUniqueId().toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);

        return (pb != null) ? pb.getBalance() : 0.0;
    }

    public void setBalance(Player p, String currencyId, double balance) {
        EconomyCache cache = NotEconomy.getEC();
        String hash = p.getUniqueId().toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);
        if (pb != null) {
            pb.setBalance(balance);
        } else {
            pb = new PlayerBalance(p.getUniqueId(), currencyId, balance);
            cache.store(pb);
        }
    }

    public void addBalance(Player p, String currencyId, double amount) {
        double current = getBalance(p, currencyId);
        setBalance(p, currencyId, current + amount);
    }

    public boolean hasBalance(Player p, String currencyId, double amount) {
        return getBalance(p, currencyId) >= amount;
    }

    public boolean withdrawBalance(Player p, String currencyId, double amount) {
        if (!hasBalance(p, currencyId, amount))
            return false;
        addBalance(p, currencyId, -amount);
        return true;
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