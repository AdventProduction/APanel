package kz.ifihtich.apanel;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PanelCommand implements TabExecutor {

    public static Map<UUID, String> targetPlayer = new HashMap<>();
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;

        if (!player.hasPermission("apanel.use")) {
            player.sendMessage(Utils.color(getConfig().getString("messages.noPerm"), player));
            return true;
        }

        if (strings.length == 0) {
            player.sendMessage(Utils.color(getConfig().getString("messages.empty"), player));
            return true;
        }

        String target = strings[0];

        Inventory panel = PanelMenu.inventory(target, player);
        player.openInventory(panel);
        targetPlayer.put(player.getUniqueId(), target);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission("apanel.use")) return Arrays.asList();

        if (strings.length == 1) {
            List<String> result = new ArrayList<>();
            String entered = strings[0].toLowerCase();

            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(entered)) {
                    result.add(p.getName());
                }
            }
            return result;
        }
        return Arrays.asList();
    }


    private static FileConfiguration getConfig(){
        return APanel.getInstance().getConfig();
    }
}
