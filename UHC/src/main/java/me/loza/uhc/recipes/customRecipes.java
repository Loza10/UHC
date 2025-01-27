package me.loza.uhc.recipes;

import me.loza.uhc.UHC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class customRecipes {
    ShapedRecipe goldenHeadRecipe;
    ShapedRecipe tridentRecipe;

    private final UHC plugin;
    public customRecipes(UHC plugin) {
        this.plugin = plugin;
    }

    public void registerTridentRecipe() {
        ItemStack trident = new ItemStack(Material.TRIDENT);
        ItemMeta itemMeta = trident.getItemMeta();

        if (itemMeta != null) {
            itemMeta.addEnchant(Enchantment.LOYALTY, 1, true);
            trident.setItemMeta(itemMeta);
        }

        NamespacedKey key = new NamespacedKey(plugin, "custom_trident");

        tridentRecipe = new ShapedRecipe(key, trident);
        tridentRecipe.shape("D D", " D ", " S ");

        tridentRecipe.setIngredient('D', Material.DIAMOND);
        tridentRecipe.setIngredient('S', Material.STICK);

        Bukkit.addRecipe(tridentRecipe);
    }

    public void registerGoldenHeadRecipe() {
        ItemStack goldenHead = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta itemMeta = goldenHead.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName("§6Golden Head");
            goldenHead.setItemMeta(itemMeta);
        }

        NamespacedKey key = new NamespacedKey(plugin, "custom_head");

        goldenHeadRecipe = new ShapedRecipe(key, goldenHead);
        goldenHeadRecipe.shape("GGG", "GPG", "GGG");

        goldenHeadRecipe.setIngredient('G', Material.GOLD_INGOT);
        goldenHeadRecipe.setIngredient('P', Material.PLAYER_HEAD);

        Bukkit.addRecipe(goldenHeadRecipe);
    }

    public void removeGoldenHeadRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "custom_head");
        Bukkit.removeRecipe(key);
    }
    public void removeTridentRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, "custom_trident");
        Bukkit.removeRecipe(key);
    }
}
