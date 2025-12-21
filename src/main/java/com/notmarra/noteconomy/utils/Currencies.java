package com.notmarra.noteconomy.utils;

import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;

import com.notmarra.noteconomy.NotEconomy;

public class Currencies {

    public static Set<String> getCurrencies() {
        FileConfiguration currencyConfig = NotEconomy.getInstance().getSubConfig("currencies.yml");
        if (currencyConfig == null)
            throw new ExceptionInInitializerError("No currencies.yml found!");
        return currencyConfig.getKeys(false);
    }
}
