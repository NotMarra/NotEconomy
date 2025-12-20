package com.notmarra.noteconomy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.notlib.extensions.NotListener;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.NotScheduler;

public class PlayerJoin extends NotListener {

    public PlayerJoin(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "PlayerJoin";
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        NotScheduler scheduler = new NotScheduler(plugin);
        scheduler.runTaskAsync(() -> {
            if (!NotEconomy.getDB().playerExists(p)) {
                NotEconomy.getDB().setupPlayer(p);
            }
        });
    }
}
