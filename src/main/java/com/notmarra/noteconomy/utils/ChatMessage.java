package com.notmarra.noteconomy.utils;

import org.bukkit.entity.Player;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.notlib.utils.ChatF;

public class ChatMessage {
    public static void send(String message, Player p) {
        ChatF.of(message.replace("%prefix%", NotEconomy.getInstance().tm("prefix")))
                .sendTo(p);
    }
}
