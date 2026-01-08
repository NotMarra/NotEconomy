package com.notmarra.noteconomy.utils;

import java.util.List;

import com.notmarra.noteconomy.NotEconomy;
import com.notmarra.noteconomy.economy.Economy;
import com.notmarra.noteconomy.economy.EconomyCommandArguments;
import com.notmarra.notlib.extensions.NotCommandGroup;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.ChatF;
import com.notmarra.notlib.utils.NotDebugger;
import com.notmarra.notlib.utils.command.NotCommand;
import com.notmarra.notlib.utils.command.arguments.NotLiteralArg;

public class UniversalCommandGroup extends NotCommandGroup {
    Economy economy = new Economy();

    public UniversalCommandGroup(NotPlugin plugin) {
        super(plugin);
    }

    @Override
    public String getId() {
        return "NotEconomyCommandGroup";
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
        NotCommand command = NotCommand.of("noteconomy", cmd -> {
            List<String> help = tmList("economy.help");
            for (String string : help) {
                ChatF.of(string).sendTo(cmd.getPlayer());
            }
            return;
        });

        NotLiteralArg reload = command.literalArg("reload", arg -> {
            plugin.reloadConfig(plugin.CONFIG_YML);
            plugin.reloadConfig("currencies.yml");
            tm().reloadLangFiles();
            NotEconomy.dbg().log(NotDebugger.C_INFO, "NotEconomy has been reloaded!");
            ChatMessage.send(tm("universal.reloaded"), arg.getPlayer());
        });
        reload.setPermission("noteconomy.reload");

        NotLiteralArg currencies = command.literalArg("currencies", arg -> {
            StringBuilder curr = new StringBuilder();
            List<String> currencyList = new java.util.ArrayList<>(Currencies.getCurrencies(plugin));
            for (int i = 0; i < currencyList.size(); i++) {
                curr.append(currencyList.get(i));
                if (i < currencyList.size() - 1) {
                    curr.append(", ");
                }
            }
            ChatMessage.send(tm("economy.currency_list").replace("%list%", curr), arg.getPlayer());
        });
        currencies.setPermission("noteconomy.currencies");

        NotLiteralArg currencyCMD = command.literalArg(currency, arg -> {
            double bal = economy.getBalance(arg.getPlayer(), currency);
            String formatted = economy.format(bal, currency);
            ChatMessage.send(tm("economy.balance").replace("%amount%", formatted), arg.getPlayer());
        });

        EconomyCommandArguments.createEconomyArguments(currencyCMD, plugin, economy, currency);

        return command;
    }
}
