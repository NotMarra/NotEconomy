package com.notmarra.noteconomy.economy;

import org.bukkit.entity.Player;

import com.notmarra.noteconomy.utils.ChatMessage;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.command.NotCommand;
import com.notmarra.notlib.utils.command.arguments.NotArgument;
import com.notmarra.notlib.utils.command.arguments.NotLiteralArg;
import com.notmarra.notlib.utils.command.arguments.NotPlayersArg;

public class EconomyCommandArguments {
    public static void createEconomyArguments(NotArgument<?> command, NotPlugin plugin, Economy economy,
            String currency) {
        // 1. arguments
        NotLiteralArg addBalanceArg = command.literalArg("add", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        addBalanceArg.setPermission("noteconomy.add");

        NotLiteralArg removeBalanceArg = command.literalArg("remove", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        removeBalanceArg.setPermission("noteconomy.remove");

        NotLiteralArg setBalanceArg = command.literalArg("set", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        setBalanceArg.setPermission("noteconomy.set");

        // 2. argument
        NotPlayersArg addPlayersArg = addBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        NotPlayersArg removePlayersArg = removeBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        NotPlayersArg setPlayersArg = setBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        // 3. argument
        addPlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);
                ChatMessage.send(plugin.tm("economy.add.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.add.receiver").replace("%amount%", formatted), target);

                economy.addBalance(target, currency, amount);
            }
        });

        removePlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);

                ChatMessage.send(plugin.tm("economy.remove.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.remove.receiver").replace("%amount%", formatted), target);
                economy.withdrawBalance(target, currency, amount);
            }
        });

        setPlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);

                ChatMessage.send(plugin.tm("economy.set.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.set.receiver").replace("%amount%", formatted), target);
                economy.setBalance(target, currency, amount);
            }
        });
    }

    public static void createEconomyArguments(NotCommand command, NotPlugin plugin, Economy economy,
            String currency) {
        // 1. arguments
        NotLiteralArg addBalanceArg = command.literalArg("add", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        addBalanceArg.setPermission("noteconomy.add");

        NotLiteralArg removeBalanceArg = command.literalArg("remove", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        removeBalanceArg.setPermission("noteconomy.remove");

        NotLiteralArg setBalanceArg = command.literalArg("set", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-player"), arg.getPlayer());
        });
        setBalanceArg.setPermission("noteconomy.set");

        // 2. argument
        NotPlayersArg addPlayersArg = addBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        NotPlayersArg removePlayersArg = removeBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        NotPlayersArg setPlayersArg = setBalanceArg.playersArg("players", arg -> {
            ChatMessage.send(plugin.tm("economy.prompt-amount"), arg.getPlayer());
        });

        // 3. argument
        addPlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);
                ChatMessage.send(plugin.tm("economy.add.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.add.receiver").replace("%amount%", formatted), target);

                economy.addBalance(target, currency, amount);
            }
        });

        removePlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);

                ChatMessage.send(plugin.tm("economy.remove.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.remove.receiver").replace("%amount%", formatted), target);
                economy.withdrawBalance(target, currency, amount);
            }
        });

        setPlayersArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            for (Player target : addPlayersArg.get()) {
                String formatted = economy.format(amount, currency);

                ChatMessage.send(plugin.tm("economy.set.sender")
                        .replace("%amount%", formatted)
                        .replace("%player%", target.getName()), arg.getPlayer());

                ChatMessage.send(plugin.tm("economy.set.receiver").replace("%amount%", formatted), target);
                economy.setBalance(target, currency, amount);
            }
        });
    }
}
