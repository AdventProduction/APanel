package kz.ifihtich.apanel;

import org.bukkit.plugin.java.JavaPlugin;

public final class APanel extends JavaPlugin {

    private static APanel instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getCommand("panel").setExecutor(new PanelCommand());
        getCommand("panel").setTabCompleter(new PanelCommand());
        getCommand("panelreload").setExecutor(new ReloadCommand());
        getCommand("panelreload").setTabCompleter(new ReloadCommand());
        getServer().getPluginManager().registerEvents(new PanelListener(), this);
        Utils.logo();

    }

    @Override
    public void onDisable() {
    }


    public static APanel getInstance(){
        return instance;
    }
}
