package com.notmarra.noteconomy.utils.Database;

import java.util.List;
import java.util.UUID;

import com.notmarra.notlib.database.NotDatabase;

public interface IEconomyDatabase {
    public record TopEntry(String name, double balance) {
    }

    boolean setupPlayer(UUID uuid);

    double getBalance(UUID uuid, String currency);

    void setBalance(UUID uuid, String currency, double balance);

    void updateBalance(UUID uuid, String currency, double balance);

    List<TopEntry> getTopBalances(String currency, int limit);

    NotDatabase asNotDatabase();
}