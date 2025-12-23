package com.notmarra.noteconomy.utils.Database;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.notmarra.notlib.database.NotDatabase;

public interface IEconomyDatabase {
    boolean setupPlayer(Player player);

    double getBalance(Player player, String currency);

    void setBalance(Player player, String currency, double balance);

    void updateBalance(UUID uuid, String currency, double balance);

    NotDatabase asNotDatabase();
}