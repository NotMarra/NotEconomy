package com.notmarra.noteconomy.economy;

import java.util.List;

import org.bukkit.entity.Player;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.notlib.extensions.NotCommandGroup;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.ChatF;
import com.notmarra.notlib.utils.command.NotCommand;
import com.notmarra.notlib.utils.command.arguments.NotLiteralArg;
import com.notmarra.notlib.utils.command.arguments.NotPlayerArg;

public class EconomyCommandGroup extends NotCommandGroup {
    EconomyCache cache = NotEconomy.getEC();
    Economy economy = new Economy();

    public EconomyCommandGroup(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "CurrenciesCommandGroup";
    }

    @Override
    public List<NotCommand> notCommands() {
        List<NotCommand> commands = new java.util.ArrayList<>();
        for (String id : Currencies.getCurrencies(plugin)) {
            commands.add(createCommand(id));
        }
        return commands;
    }

    private NotCommand createCommand(String currency) {
        NotCommand command = NotCommand.of(currency, cmd -> {
            double bal = economy.getBalance(cmd.getPlayer(), currency);
            String formatted = economy.format(bal, currency);
            ChatF.of(tm("prefix") + tm("economy.balance").replace("%amount%", formatted)).sendTo(cmd.getPlayer());
        });

        // 1. arguments
        NotLiteralArg addBalanceArg = command.literalArg("add", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-player")).sendTo(arg.getPlayer());
        });
        addBalanceArg.setPermission("noteconomy.add");

        NotLiteralArg removeBalanceArg = command.literalArg("remove", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-player")).sendTo(arg.getPlayer());
        });
        removeBalanceArg.setPermission("noteconomy.remove");

        NotLiteralArg setBalanceArg = command.literalArg("set", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-player")).sendTo(arg.getPlayer());
        });
        setBalanceArg.setPermission("noteconomy.set");

        // 2. argument
        NotPlayerArg addPlayerArg = addBalanceArg.playerArg("player", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-amount")).sendTo(arg.getPlayer());
        });

        NotPlayerArg removePlayerArg = removeBalanceArg.playerArg("player", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-amount")).sendTo(arg.getPlayer());
        });

        NotPlayerArg setPlayerArg = setBalanceArg.playerArg("player", arg -> {
            ChatF.of(tm("prefix") + tm("economy.prompt-amount")).sendTo(arg.getPlayer());
        });

        // 3. argument
        addPlayerArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            Player target = addPlayerArg.getPlayer();
            String formatted = economy.format(amount, currency);
            ChatF.of(tm("prefix") + tm("economy.add.sender")
                    .replace("%amount%", formatted)
                    .replace("%player%", target.getName()))
                    .sendTo(arg.getPlayer());

            ChatF.of(tm("prefix") + tm("economy.add.receiver").replace("%amount%", formatted))
                    .sendTo(target);
            economy.addBalance(target, currency, amount);
        });

        removePlayerArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            Player target = removePlayerArg.getPlayer();
            String formatted = economy.format(amount, currency);

            ChatF.of(tm("prefix") + tm("economy.remove.sender")
                    .replace("%amount%", formatted)
                    .replace("%player%", target.getName()))
                    .sendTo(arg.getPlayer());

            ChatF.of(tm("prefix") + tm("economy.remove.receiver").replace("%amount%", formatted))
                    .sendTo(target);
            economy.withdrawBalance(target, currency, amount);
        });

        setPlayerArg.doubleArg("amount", arg -> {
            double amount = arg.get();
            Player target = setPlayerArg.getPlayer();
            String formatted = economy.format(amount, currency);

            ChatF.of(tm("prefix") + tm("economy.set.sender")
                    .replace("%amount%", formatted)
                    .replace("%player%", target.getName()))
                    .sendTo(arg.getPlayer());

            ChatF.of(tm("prefix") + tm("economy.set.receiver").replace("%amount%", formatted))
                    .sendTo(target);
            economy.setBalance(target, currency, amount);
        });

        return command;
    }
}
