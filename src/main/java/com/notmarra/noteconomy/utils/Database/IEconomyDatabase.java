package com.notmarra.noteconomy.utils.Database;

import org.bukkit.entity.Player;

import com.notmarra.notlib.database.NotDatabase;

public interface IEconomyDatabase {
    boolean setupPlayer(Player player);

    double getBalance(Player player, String currency);

    void setBalance(Player player, String currency, double balance);

    void updateBalance(Player player, String currency, double balance);

    NotDatabase asNotDatabase();
}