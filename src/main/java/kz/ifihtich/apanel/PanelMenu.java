package kz.ifihtich.apanel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PanelMenu {

    public static Inventory inventory(String target, Player player){
        String invName = Utils.color(getConfig().getString("panel.name").replace("{player}", target), player);
        int size = getConfig().getInt("panel.size");
        Inventory inventory = Bukkit.createInventory(new MenuHolder("panel"), size, invName);
        ConfigurationSection section = getConfig().getConfigurationSection("panel.items");
        if (section != null){
            for (String key : section.getKeys(false)){
                String path = "panel.items." + key;

                String materialName = getConfig().getString(path + ".material", "STONE").toUpperCase();
                Material material = Material.getMaterial(materialName);
                if (material == null) continue;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                if (meta != null){
                    String name = Utils.color(getConfig().getString(path + ".name"), player);
                    meta.setDisplayName(name);

                    List<String> lore = getConfig().getStringList(path + ".lore");
                    List<String> formattedLore = new ArrayList<>();
                    for (String line : lore){
                        formattedLore.add(Utils.color(line, player));
                    }
                    meta.setLore(formattedLore);

                    if (getConfig().getBoolean(path + ".glow", false)){
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.addEnchant(Enchantment.MENDING, 1, false);
                    }

                    item.setItemMeta(meta);
                }

                Object slotObject = getConfig().get(path + ".slot");
                if (slotObject == null) continue;

                Set<Integer> slots = new HashSet<>();

                if (slotObject instanceof Integer) {
                    slots.add((Integer) slotObject);
                }

                else if (slotObject instanceof List<?>) {
                    for (Object val : (List<?>) slotObject) {

                        if (val instanceof Integer) {
                            slots.add((Integer) val);
                            continue;
                        }

                        if (val instanceof String) {
                            String s = (String) val;
                            if (!s.contains("-")) continue;

                            String[] parts = s.split("-");
                            if (parts.length != 2) continue;

                            try {
                                int from = Integer.parseInt(parts[0].trim());
                                int to = Integer.parseInt(parts[1].trim());

                                if (from > to) continue;

                                for (int i = from; i <= to; i++) {
                                    slots.add(i);
                                }
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }

                for (int slot : slots) {
                    if (slot >= 0 && slot < size) {
                        inventory.setItem(slot, item);
                    }
                }
            }
        }
        return inventory;
    }


    private static FileConfiguration getConfig(){
        return APanel.getInstance().getConfig();
    }
}
