package com.notmarra.noteconomy.economy;

import org.bukkit.entity.Player;
import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase;

public class Economy {
    private final IEconomyDatabase db = NotEconomy.getDB();
    private final EconomyCache cache = NotEconomy.getEC();

    public void setupPlayer(Player p) {
        db.setupPlayer(p);

        for (String id : Currencies.getCurrencies()) {
            double balance = db.getBalance(p, id);

            cache.store(new PlayerBalance(p.getUniqueId(), id, balance));
        }
    }

    public double getBalance(Player p, String currencyId) {
        String hash = p.getUniqueId().toString() + "_" + currencyId;
        PlayerBalance pb = cache.get(hash);

        return (pb != null) ? pb.getBalance() : 0.0;
    }

    public void setBalance(Player p, String currencyId, double balance) {
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
}