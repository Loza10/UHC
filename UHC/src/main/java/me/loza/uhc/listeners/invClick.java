package me.loza.uhc.listeners;

import me.loza.uhc.UHC;
import me.loza.uhc.handlers.prefixHandler;
import me.loza.uhc.recipes.customRecipes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class invClick implements Listener {
    private final UHC plugin;
    prefixHandler pref = new prefixHandler();
    public invClick(UHC plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if ((event.getInventory().getType() == InventoryType.BREWING) && !(plugin.getConfig().getBoolean("features.potions"))) {
            event.setCancelled(true);
            if (event.getPlayer() instanceof Player) {
                event.getPlayer().sendMessage(pref.getErrorPrefix() + "Potions are currently disabled!");
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        customRecipes cr = new customRecipes(plugin);
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            String invName = event.getView().getTitle();
            if (invName.equalsIgnoreCase(ChatColor.GOLD + "UHC Config")) {
                event.setCancelled(true);
                if (player.isOp() && event.getCurrentItem() != null) {
                    String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    player.closeInventory();
                    openConfig(player, itemName);
                }
            } else if (invName.equalsIgnoreCase(ChatColor.GOLD + "Healing") || invName.equalsIgnoreCase(ChatColor.GOLD + "Nether Config") || invName.equalsIgnoreCase(ChatColor.GOLD + "Enchanting") || invName.equalsIgnoreCase(ChatColor.GOLD + "Misc") || invName.equalsIgnoreCase(ChatColor.GOLD + "Combat")) {
                event.setCancelled(true);
                if (player.isOp() && event.getCurrentItem() != null) {
                    String itemName = toSnakeCase(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                    String path = "features." + itemName;
                    if (!(itemName.equalsIgnoreCase("apple_rates")) && !(itemName.equalsIgnoreCase("flint_rates"))) {
                        if (plugin.getConfig().getBoolean(path)) {
                            plugin.getConfig().set(path, false);
                            plugin.saveConfig();
                            if (itemName.equalsIgnoreCase("golden_heads")) {
                                cr.removeGoldenHeadRecipe();
                            }
                            if (itemName.equalsIgnoreCase("trident_craft")) {
                                cr.removeTridentRecipe();
                            }
                            if (itemName.equalsIgnoreCase("original_melee")) {
                                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                    onlinePlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
                                }
                            }
                        } else {
                            plugin.getConfig().set(path, true);
                            plugin.saveConfig();
                            if (itemName.equalsIgnoreCase("golden_heads")) {
                                cr.registerGoldenHeadRecipe();
                            }
                            if (itemName.equalsIgnoreCase("trident_craft")) {
                                cr.registerTridentRecipe();
                            }
                            if (itemName.equalsIgnoreCase("original_melee")) {
                                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                                    onlinePlayer.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100);
                                }
                            }
                        }
                        player.closeInventory();
                        openConfig(player, invName);
                    } else {
                        openAFConfig(player, itemName);
                    }
                }
            } else if (invName.equalsIgnoreCase(ChatColor.GOLD + "Apple Rates") || invName.equalsIgnoreCase(ChatColor.GOLD + "Flint Rates")) {
                event.setCancelled(true);
                String itemName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                if (!(itemName.equalsIgnoreCase(invName))) {
                    Integer percent = Integer.parseInt(itemName);
                    Integer original = plugin.getConfig().getInt("features." + toSnakeCase(ChatColor.stripColor(invName)));
                    if (event.getCurrentItem().getType() == Material.LIME_CONCRETE) {
                        Integer newPercent = percent + original;
                        if (newPercent <= 100) {
                            plugin.getConfig().set("features." + toSnakeCase(ChatColor.stripColor(invName)), newPercent);
                            plugin.saveConfig();
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "You can not go above 100!");
                        }
                    } else if (event.getCurrentItem().getType() == Material.RED_CONCRETE) {
                        Integer newPercent = original - percent;
                        if (newPercent >= 0) {
                            plugin.getConfig().set("features." + toSnakeCase(ChatColor.stripColor(invName)), newPercent);
                            plugin.saveConfig();
                        } else {
                            player.sendMessage(pref.getErrorPrefix() + "You can not go below 0!");
                        }
                    }
                    player.closeInventory();
                    openAFConfig(player, toSnakeCase(invName));
                }
            }
        }
    }
    public void openAFConfig(Player player, String config) {
        if (config.toLowerCase().contains("apple_rates".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Apple Rates");
            inventory.setItem(11, createItem(Material.RED_CONCRETE, ChatColor.RED + "1",
                    ChatColor.DARK_GRAY + "» " + ChatColor.RED + "-1.0%", ChatColor.GRAY + "» Click to subtract!"
            ));
            inventory.setItem(12, createItem(Material.RED_CONCRETE, ChatColor.RED + "5",
                    ChatColor.DARK_GRAY + "» " + ChatColor.RED + "-5.0%", ChatColor.GRAY + "» Click to subtract!"
            ));
            inventory.setItem(13, createItem(Material.APPLE, ChatColor.GOLD + "Apple Rates",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + plugin.getConfig().getInt("features.apple_rates"), ChatColor.GRAY + "» Current apple rates!"
            ));
            inventory.setItem(14, createItem(Material.LIME_CONCRETE, ChatColor.GREEN + "5",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "+5.0%", ChatColor.GRAY + "» Click to add!"
            ));
            inventory.setItem(15, createItem(Material.LIME_CONCRETE, ChatColor.GREEN + "1",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "+1.0%", ChatColor.GRAY + "» Click to add!"
            ));
            player.openInventory(inventory);
        } else {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Flint Rates");
            inventory.setItem(11, createItem(Material.RED_CONCRETE, ChatColor.RED + "5",
                    ChatColor.DARK_GRAY + "» " + ChatColor.RED + "-5.0%", ChatColor.GRAY + "» Click to subtract!"
            ));
            inventory.setItem(12, createItem(Material.RED_CONCRETE, ChatColor.RED + "10",
                    ChatColor.DARK_GRAY + "» " + ChatColor.RED + "-10%", ChatColor.GRAY + "» Click to subtract!"
            ));
            inventory.setItem(13, createItem(Material.FLINT, ChatColor.GOLD + "Flint Rates",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GRAY + plugin.getConfig().getInt("features.flint_rates"), ChatColor.GRAY + "» Current flint rates!"
            ));
            inventory.setItem(14, createItem(Material.LIME_CONCRETE, ChatColor.GREEN + "10",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "+10%", ChatColor.GRAY + "» Click to add!"
            ));
            inventory.setItem(15, createItem(Material.LIME_CONCRETE, ChatColor.GREEN + "5",
                    ChatColor.DARK_GRAY + "» " + ChatColor.GREEN + "+5.0%", ChatColor.GRAY + "» Click to add!"
            ));
            player.openInventory(inventory);
        }
    }
    public void openConfig(Player player, String config) {
        if (config.toLowerCase().contains("Healing".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Healing");
            inventory.setItem(12, createItem(Material.PLAYER_HEAD, ChatColor.GOLD + "Golden Heads",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Golden Heads")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(13, createItem(Material.GOLDEN_APPLE, ChatColor.GOLD + "Absorption",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Absorption")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(14, createItem(Material.ENCHANTED_GOLDEN_APPLE, ChatColor.GOLD + "God Apples",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("God Apples")), ChatColor.GRAY + "» Click to toggle!"
            ));
            player.openInventory(inventory);
        } else if (config.toLowerCase().contains("Nether Config".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Nether Config");
            inventory.setItem(11, createItem(Material.OBSIDIAN, ChatColor.GOLD + "Nether",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Nether")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(12, createItem(Material.BREWING_STAND, ChatColor.GOLD + "Potions",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Potions")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(13, createItem(Material.BLAZE_POWDER, ChatColor.GOLD + "Strength Potions",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Strength Potions")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(14, createItem(Material.GUNPOWDER, ChatColor.GOLD + "Splash Potions",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Splash Potions")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(15, createItem(Material.GLOWSTONE, ChatColor.GOLD + "Tier II",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Tier II")), ChatColor.GRAY + "» Click to toggle!"
            ));
            player.openInventory(inventory);
        } else if (config.toLowerCase().contains("Enchanting".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Enchanting");
            inventory.setItem(12, createItem(Material.EMERALD, ChatColor.GOLD + "Villager Trading",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Villager Trading")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(13, createItem(Material.ENCHANTED_BOOK, ChatColor.GOLD + "Fire Aspect",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Fire Aspect")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(14, createItem(Material.BOOKSHELF, ChatColor.GOLD + "Bookshelves",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Bookshelves")), ChatColor.GRAY + "» Click to toggle!"
            ));
            player.openInventory(inventory);
        } else if (config.toLowerCase().contains("Misc".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Misc");
            inventory.setItem(11, createItem(Material.APPLE, ChatColor.GOLD + "Apple Rates",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Apple Rates")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(12, createItem(Material.FLINT, ChatColor.GOLD + "Flint Rates",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Flint Rates")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(13, createItem(Material.ENDER_PEARL, ChatColor.GOLD + "Pearl Damage",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Pearl Damage")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(14, createItem(Material.SADDLE, ChatColor.GOLD + "Horses",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Horses")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(15, createItem(Material.SHEARS, ChatColor.GOLD + "Shear String",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Shear String")), ChatColor.GRAY + "» Click to toggle!"
            ));
            player.openInventory(inventory);
        } else if (config.toLowerCase().contains("Combat".toLowerCase())) {
            Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.GOLD + "Combat");
            inventory.setItem(11, createItem(Material.BOW, ChatColor.GOLD + "Bow Nerf",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Bow Nerf")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(12, createItem(Material.IRON_AXE, ChatColor.GOLD + "Axe Nerf",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Axe Nerf")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(13, createItem(Material.SHIELD, ChatColor.GOLD + "Shields",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Shields")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(14, createItem(Material.IRON_SWORD, ChatColor.GOLD + "Original Melee",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Original Melee")), ChatColor.GRAY + "» Click to toggle!"
            ));
            inventory.setItem(15, createItem(Material.TRIDENT, ChatColor.GOLD + "Trident Craft",
                    ChatColor.DARK_GRAY + "» " + formatStatus(toSnakeCase("Trident Craft")), ChatColor.GRAY + "» Click to toggle!"
            ));
            player.openInventory(inventory);
        }
    }
    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(Arrays.asList(lore));
            } else {
                meta.setLore(Collections.emptyList());
            }
            item.setItemMeta(meta);
        }
        return item;
    }
    public static String toSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        String trimmedInput = input.trim();
        return trimmedInput.replaceAll(" +", "_").toLowerCase();
    }
    public String formatStatus(String path) {
        if (path.equalsIgnoreCase("apple_rates") || path.equalsIgnoreCase("flint_rates")) {
            return ChatColor.GREEN + String.valueOf(plugin.getConfig().getInt("features." + path)) + "%";
        }
        if (plugin.getConfig().getBoolean("features." + path)) {
            return ChatColor.GREEN + "Enabled";
        } else {
            return ChatColor.RED + "Disabled";
        }
    }
}
