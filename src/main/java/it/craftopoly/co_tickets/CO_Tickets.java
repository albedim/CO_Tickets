package it.craftopoly.co_tickets;

import it.craftopoly.co_tickets.executor.TicketExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class CO_Tickets extends JavaPlugin {

    private static CO_Tickets plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        saveDefaultConfig();
        getCommand("ticket").setExecutor(new TicketExecutor());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CO_Tickets getInstance()
    {
        return plugin;
    }
}
