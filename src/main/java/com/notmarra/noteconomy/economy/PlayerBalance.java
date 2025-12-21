package com.notmarra.noteconomy.economy;

import java.util.UUID;

public class PlayerBalance {
    private final UUID playerUuid;
    private final String currencyId;
    private double balance;

    public PlayerBalance(UUID playerUuid, String currencyId, double balance) {
        this.playerUuid = playerUuid;
        this.currencyId = currencyId;
        this.balance = balance;
    }

    /**
     * @return UUID
     */
    public UUID getPlayerUuid() {
        return playerUuid;
    }

    /**
     * @return String
     */
    public String getCurrencyId() {
        return currencyId;
    }

    /**
     * @return double
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Sets player's balance
     * 
     * @param balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
