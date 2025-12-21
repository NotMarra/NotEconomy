package com.notmarra.noteconomy.utils.Database;

import java.util.List;

import org.bukkit.entity.Player;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.notlib.database.NotDatabase;
import com.notmarra.notlib.database.source.NotMySQL;
import com.notmarra.notlib.database.structure.NotColumn;
import com.notmarra.notlib.database.structure.NotTable;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.NotDebugger;

public class MySQL extends NotMySQL implements IEconomyDatabase {
    public final String UUID = "uuid";
    public final String PLAYER = "player";
    public final String BALANCE = "balance";
    public final String TEST_TABLE = "test_economy";

    public MySQL(NotPlugin plugin, String defaultConfig) {
        super(plugin, defaultConfig);
    }

    @Override
    public List<NotTable> setupTables() {
        return List.of(
                NotTable.createNew(TEST_TABLE, List.of(
                        NotColumn.varchar(UUID, 36).primaryKey().notNull(),
                        NotColumn.varchar(PLAYER, 36).notNull(),
                        NotColumn.doubleType(BALANCE).notNull().defaultValue("0"))));
    }

    @Override
    public NotDatabase asNotDatabase() {
        return this;
    }

    @Override
    public boolean setupPlayer(Player player) {
        try {
            NotTable table = getTable(TEST_TABLE);
            if (table == null) {
                return false;
            }

            return table.insertRow(List.of(
                    player.getUniqueId().toString(),
                    player.getName(),
                    0));
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error setting up player " + player.getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean playerExists(Player player) {
        try {
            NotTable table = getTable(TEST_TABLE);
            if (table == null) {
                return false;
            }

            return table.exists(b -> b.whereEquals(UUID, player.getUniqueId().toString()));
        } catch (Exception e) {
            NotEconomy.dbg().log(NotDebugger.C_ERROR,
                    "Error checking if player exists " + player.getName() + ": " + e.getMessage());
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

}
