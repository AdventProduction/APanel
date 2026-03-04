package kz.ifihtich.apanel;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("apanel.reload")){
            commandSender.sendMessage(Utils.color(getConfig().getString("messages.noPerm")));
            return true;
        }
        APanel.getInstance().reloadConfig();
        commandSender.sendMessage(Utils.color(getConfig().getString("messages.reload")));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Arrays.asList();
    }

    private static FileConfiguration getConfig(){
        return APanel.getInstance().getConfig();
    }
}
