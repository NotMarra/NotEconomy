package com.notmarra.noteconomy.utils.Database;

import org.bukkit.entity.Player;

import com.notmarra.notlib.database.NotDatabase;

public interface IEconomyDatabase {
    boolean playerExists(Player player);

    boolean setupPlayer(Player player);

    double getBalance(Player player, String currency);

    NotDatabase asNotDatabase();
}