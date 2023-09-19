package me.phantam.customdropmobs;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class CustomDropMobs extends JavaPlugin implements Listener {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
        Bukkit.getPluginManager().registerEvents(this, this);
    }
    @Override
    public void onDisable() {
        saveConfig();
        saveDefaultConfig();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    new BukkitRunnable {
        @EventHandler
        public void onEntityDeath(EntityDeathEvent event) {
            LivingEntity entity = event.getEntity();
            String entityType = entity.getType().name();

            ConfigurationSection mobConfig = config.getConfigurationSection("customdropmobs." + entityType);

            if (mobConfig != null) {
                for (String itemName : mobConfig.getKeys(false)) {
                    if (itemName.equalsIgnoreCase("COMMAND")) {
                        String commandString = mobConfig.getString(itemName);
                        assert commandString != null;
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandString);
                    } else {
                        Material material = Material.getMaterial(itemName);
                        if (material != null) {
                            int quantity = mobConfig.getInt(itemName);
                            ItemStack itemStack = new ItemStack(material, quantity);
                            event.getDrops().add(itemStack);
                        }
                    }
                }
            }
        }
    }
}
