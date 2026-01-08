package com.notmarra.noteconomy.economy;

import java.util.List;

import com.notmarra.noteconomy.utils.ChatMessage;
import com.notmarra.noteconomy.utils.Currencies;
import com.notmarra.notlib.extensions.NotCommandGroup;
import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.command.NotCommand;

public class EconomyCommandGroup extends NotCommandGroup {
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
            ChatMessage.send(tm("economy.balance").replace("%amount%", formatted), cmd.getPlayer());
        });

        EconomyCommandArguments.createEconomyArguments(command, plugin, economy, currency);

        return command;
    }
}
