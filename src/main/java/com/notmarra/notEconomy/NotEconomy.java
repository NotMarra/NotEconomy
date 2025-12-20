package com.notmarra.noteconomy;

import com.notmarra.notlib.extensions.NotPlugin;
import com.notmarra.notlib.utils.ChatF;

public final class NotEconomy extends NotPlugin {
    private static NotEconomy instance;

    @Override
    public void initNotPlugin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'initNotPlugin'");
    }

    @Override
    public void onNotPluginDisable() {
        instance = this;

        log().info(ChatF.of("NotEconomy started succefully!").build());
    }

    @Override
    public void onNotPluginEnable() {
        log().info(ChatF.of("NotEconomy shut down succefully!").build());
    }

    public static NotEconomy getInstance() {
        return instance;
    }
}
