package com.notmarra.noteconomy.utils.Database;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.notlib.database.NotDatabase;
import com.notmarra.notlib.database.source.NotSQLite;
import com.notmarra.notlib.database.structure.NotColumn;
import com.notmarra.notlib.database.structure.NotTable;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.NotDebugger;

public class SQLite extends NotSQLite implements IEconomyDatabase {
    public final Set<String> TABLES = Currencies.getCurrencies(plugin);
    public final String UUID = "uuid";
    public final String PLAYER = "player";
    public final String BALANCE = "balance";

    public SQLite(NotPlugin plugin, String defaultConfig) {
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
    public boolean setupPlayer(Player player) {
        String uuidStr = player.getUniqueId().toString();

        for (String id : TABLES) {
            try {
                NotTable table = getTable(id);
                if (table == null)
                    continue;

                boolean playerExists = table.exists(builder -> builder.whereEquals(UUID, uuidStr));

                if (!playerExists) {
                    table.insertRow(List.of(
                            uuidStr,
                            player.getName(),
                            0.0));
                }
            } catch (Exception e) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Chyba při setupu měny " + id + ": " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public double getBalance(Player player, String currency) {
        try {
            NotTable table = getTable(currency);
            if (table == null) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Table " + table + " is null!");
                return 0;
            }
            return table
                    .selectOne(b -> b.whereEquals(UUID, player.getUniqueId().toString()))
                    .getDouble(BALANCE);
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error checking player's balance: " + e);
            return 0;
        }
    }

    @Override
    public void setBalance(Player player, String currency, double balance) {
        try {
            NotTable table = getTable(currency);
            if (table == null) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Table " + table + " is null!");
                return;
            }
            table
                    .update(b -> {
                        b.whereEquals(UUID, player.getUniqueId().toString());
                        b.set(BALANCE, balance);
                    });
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting player's balance: " + e);
            return;
        }
    }

    @Override
    public void updateBalance(UUID player, String currency, double balance) {
        try {
            NotTable table = getTable(currency);
            if (table == null) {
                NotEconomy.dbg().log(NotDebugger.C_ERROR, "Table " + table + " is null!");
                return;
            }
            table
                    .update(b -> {
                        b.whereEquals(UUID, player.toString());
                        b.set(BALANCE, balance);
                    });
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting player's balance: " + e);
            return;
        }
    }
}
