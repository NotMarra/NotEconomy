package com.notmarra.noteconomy.hooks;

import com.notmarra.noteconomy.economy.Economy;
import com.notmarra.notlib.utils.NotPlaceholder;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.noteconomy.utils.Database.IEconomyDatabase.TopEntry;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EconomyPlaceholder extends NotPlaceholder {
    private final Economy economy = new Economy();

    public EconomyPlaceholder(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getIdentifier() {
        return "noteconomy";
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null)
            return "";

        // Split identifier: <currency>_<type>
        // Example: %noteconomy_money_formatted% -> parts = ["money", "formatted"]
        String[] parts = identifier.split("_");
        if (parts.length < 2)
            return null;

        String currencyId = parts[0];
        String type = parts[1];

        // Check if currency exists in configuration
        if (!Currencies.getCurrencies(plugin).contains(currencyId))
            return null;

        // 1. %noteconomy_<currency>_raw% (or just %noteconomy_<currency>%)
        if (type.equalsIgnoreCase("raw") || parts.length == 1) {
            double bal = economy.getBalance(player.getUniqueId(), currencyId);
            return String.valueOf((long) bal);
        }

        // 2. %noteconomy_<currency>_formatted%
        if (type.equalsIgnoreCase("formatted")) {
            double bal = economy.getBalance(player.getUniqueId(), currencyId);
            return economy.format(bal, currencyId);
        }

        // 3. %noteconomy_<currency>_top_#% and %noteconomy_<currency>_top_name_#%
        if (identifier.contains("_top_")) {
            try {
                int rank = Integer.parseInt(parts[parts.length - 1]);
                TopEntry entry = economy.getTopEntry(currencyId, rank);

                if (identifier.contains("_top_name_")) {
                    return entry.name();
                }

                if (identifier.contains("_top_")) {
                    return economy.format(entry.balance(), currencyId);
                }
            } catch (Exception e) {
                return "N/A";
            }
        }

        return null;
    }
}
