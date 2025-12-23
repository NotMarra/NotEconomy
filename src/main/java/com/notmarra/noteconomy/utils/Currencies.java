package com.notmarra.noteconomy.utils;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.notmarra.notlib.extensions.NotPlugin;

public class Currencies {

    public static Set<String> getCurrencies(NotPlugin plugin) {
        FileConfiguration currencyConfig = plugin.getSubConfig("currencies.yml");
        if (currencyConfig == null)
            throw new ExceptionInInitializerError("No currencies.yml found!");
        return currencyConfig.getKeys(false);
    }
}
