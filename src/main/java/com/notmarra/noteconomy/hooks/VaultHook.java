package com.notmarra.noteconomy.hooks;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.util.List;

public class VaultHook implements net.milkbowl.vault.economy.Economy {

    private final Economy internalEconomy = new Economy();

    private String getCurrencyId() {
        return NotEconomy.getInstance().getConfig().getString("vault.currency_id", "money");
    }

    @Override
    public boolean isEnabled() {
        return NotEconomy.getInstance().getConfig().getBoolean("vault.enabled", false);
    }

    @Override
    public String getName() {
        return "NotEconomy";
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return internalEconomy.getBalance(player.getUniqueId(), getCurrencyId());
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        internalEconomy.addBalance(player.getUniqueId(), getCurrencyId(), amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (!internalEconomy.hasBalance(player.getUniqueId(), getCurrencyId(), amount)) {
            return new EconomyResponse(0, getBalance(player), EconomyResponse.ResponseType.FAILURE,
                    "Insufficient funds");
        }
        internalEconomy.withdrawBalance(player.getUniqueId(), getCurrencyId(), amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public String format(double amount) {
        return internalEconomy.format(amount, getCurrencyId());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public EconomyResponse bankBalance(String arg0) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String arg0) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
        return false;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        return null;
    }

    @Override
    public double getBalance(String arg0) {
        return this.getBalance((OfflinePlayer) Bukkit.getPlayerExact(arg0));
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        return this.getBalance((OfflinePlayer) Bukkit.getPlayerExact(arg0));
    }

    @Override
    public double getBalance(OfflinePlayer arg0, String arg1) {
        return this.getBalance(arg0);
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean has(String arg0, double arg1) {
        return this.getBalance(arg0) >= (double) ((long) arg1);
    }

    @Override
    public boolean has(OfflinePlayer arg0, double arg1) {
        return this.getBalance(arg0) >= (double) ((long) arg1);
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {
        return this.has(arg0, arg2);
    }

    @Override
    public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
        return this.has(arg0, arg2);
    }

    @Override
    public boolean hasAccount(String arg0) {
        return false;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer arg0, String arg1) {
        return false;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
        return null;
    }
}