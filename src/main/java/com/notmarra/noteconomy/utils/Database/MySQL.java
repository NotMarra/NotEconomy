package com.notmarra.noteconomy.utils.Database;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.notlib.database.NotDatabase;
import com.notmarra.notlib.database.source.NotMySQL;
import com.notmarra.notlib.database.structure.NotColumn;
import com.notmarra.notlib.database.structure.NotTable;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.NotDebugger;

public class MySQL extends NotMySQL implements IEconomyDatabase {
    public final Set<String> TABLES = Currencies.getCurrencies(plugin);
    public final String UUID = "uuid";
    public final String PLAYER = "player";
    public final String BALANCE = "balance";

    public MySQL(NotPlugin plugin, String defaultConfig) {
        super(plugin, defaultConfig);
    }

    @Override
    public List<NotTable> setupTables() {
        List<NotTable> list = new java.util.ArrayList<>();
        for (String id : TABLES) {
            list.add(
                    NotTable.createNew(id, List.of(
                            NotColumn.varchar(UUID, 36).primaryKey().notNull(),
                            NotColumn.varchar(PLAYER, 36).notNull(),
                            NotColumn.doubleType(BALANCE).notNull().defaultValue("0"))));

        }
        return list;
    }

    @Override
    public NotDatabase asNotDatabase() {
        return this;
    }

    @Override
    public boolean setupPlayer(UUID uuid) {
        for (String id : TABLES) {
            try {
                NotTable table = getTable(id);
                if (table == null)
                    continue;

                boolean playerExists = table.exists(builder -> builder.whereEquals(UUID, uuid));

                if (!playerExists) {
                    table.insertRow(List.of(
                            uuid,
                            Bukkit.getPlayer(uuid),
                            0.0));
                }
            } catch (Exception e) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Chyba při setupu měny " + id + ": " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public double getBalance(UUID uuid, String currency) {
        try {
            NotTable table = getTable(currency);
            if (table == null) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Table " + table + " is null!");
                return 0;
            }
            return table
                    .selectOne(b -> b.whereEquals(UUID, uuid.toString()))
                    .getDouble(BALANCE);
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error checking player's balance: " + e);
            return 0;
        }
    }

    @Override
    public void setBalance(UUID uuid, String currency, double balance) {
        try {
            NotTable table = getTable(currency);
            if (table == null) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Table " + table + " is null!");
                return;
            }
            table
                    .selectOne(b -> b.whereEquals(UUID, uuid.toString()))
                    .set(BALANCE, balance);
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting player's balance: " + e);
            return;
        }
    }

    @Override
    public void updateBalance(UUID uuid, String currency, double balance) {
        setBalance(uuid, currency, balance);
    }
}
