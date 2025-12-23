package com.notmarra.noteconomy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.economy.EconomyCache;
import com.notmarra.noteconomy.economy.PlayerBalance;
import com.notmarra.notlib.extensions.NotListener;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.NotScheduler;
import com.notmarra.noteconomy.utils.Currencies;

public class PlayerQuit extends NotListener {

    public PlayerQuit(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "PlayerQuit";
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        EconomyCache cache = NotEconomy.getEC();

        NotScheduler scheduler = new NotScheduler(plugin);
        scheduler.runTaskAsync(() -> {
            for (String currencyId : Currencies.getCurrencies(plugin)) {
                PlayerBalance pb = cache.getBalance(p.getUniqueId(), currencyId);

                if (pb != null) {
                    NotEconomy.getDB().updateBalance(pb.getPlayerUuid(), pb.getCurrencyId(), pb.getBalance());

                    cache.remove(p.getUniqueId().toString() + "_" + currencyId);
                }
            }
        });
    }
}