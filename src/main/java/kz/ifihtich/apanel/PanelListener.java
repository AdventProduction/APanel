package kz.ifihtich.apanel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PanelListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        Inventory inventory = event.getInventory();

        if (!(inventory.getHolder() instanceof MenuHolder)) return;
        MenuHolder holder = (MenuHolder) inventory.getHolder();
        if (!holder.getName().equalsIgnoreCase("panel")) return;
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        int clickedSlot = event.getSlot();

        ConfigurationSection section = getConfig().getConfigurationSection("panel.items");
        if (section == null) return;

        for (String key : section.getKeys(false)){

            String path = "panel.items." + key;
            Object slotObject = getConfig().get(path + ".slot");
            if (slotObject == null) continue;

            boolean match = false;

            if (slotObject instanceof Integer) {
                match = (Integer) slotObject == clickedSlot;
            }

            else if (slotObject instanceof List<?>) {
                for (Object val : (List<?>) slotObject) {

                    if (val instanceof Integer &&
                            (Integer) val == clickedSlot) {
                        match = true;
                        break;
                    }

                    if (val instanceof String) {
                        String s = (String) val;
                        if (!s.contains("-")) continue;

                        String[] parts = s.split("-");
                        if (parts.length != 2) continue;

                        try {
                            int from = Integer.parseInt(parts[0].trim());
                            int to = Integer.parseInt(parts[1].trim());

                            if (clickedSlot >= from &&
                                    clickedSlot <= to) {
                                match = true;
                                break;
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
            }

            if (!match) continue;

            if (getConfig().contains(path + ".commands")){
                String command = getConfig().getString(path + ".command").replace("{player}", PanelCommand.targetPlayer.get(player.getUniqueId()));
                Bukkit.dispatchCommand(player, command);
                player.closeInventory();
                return;

            }

        }


    }

    private static FileConfiguration getConfig() {
        return APanel.getInstance().getConfig();
    }
}
