package com.notmarra.noteconomy.utils.Database;

import java.util.UUID;

import com.notmarra.notlib.database.NotDatabase;

public interface IEconomyDatabase {
    boolean setupPlayer(UUID uuid);

    double getBalance(UUID uuid, String currency);

    void setBalance(UUID uuid, String currency, double balance);

    void updateBalance(UUID uuid, String currency, double balance);

    NotDatabase asNotDatabase();
}