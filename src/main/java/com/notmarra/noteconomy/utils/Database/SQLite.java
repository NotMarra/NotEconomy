package com.notmarra.noteconomy.utils.Database;

import java.util.List;
import java.util.Set;

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
    public final Set<String> TABLES = Currencies.getCurrencies();
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

    public boolean setupPlayer(Player player) {
        try {
            for (String id : TABLES) {
                NotTable table = getTable(id);
                if (table == null) {
                    return false;
                }

                if (!table.exists(b -> b.whereEquals(UUID, player.getUniqueId().toString()))) {
                    table.insertRow(List.of(
                            player.getUniqueId().toString(),
                            player.getName(),
                            0));
                }

            }
            return true;
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting up player " + player.getName() + ": " + e.getMessage());
            return false;
        }
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
                    .selectOne(b -> b.whereEquals(UUID, player.getUniqueId().toString()))
                    .set(BALANCE, balance);
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting player's balance: " + e);
            return;
        }
    }

    @Override
    public void updateBalance(Player player, String currency, double balance) {
        setBalance(player, currency, balance);
    }
}
