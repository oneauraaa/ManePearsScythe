package oneaura.ManePearsScythe;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;

public class ManePearsScythe extends JavaPlugin implements Listener, CommandExecutor {

    private static final String RESOURCE_PACK_URL = "https://download.mc-packs.net/pack/65cfcb5ada313e15e481e4bc9efc2123bd5a24ad.zip";
    private static final String RESOURCE_PACK_SHA1 = "65cfcb5ada313e15e481e4bc9efc2123bd5a24ad";
    private NamespacedKey recipeKey;
    private boolean scytheCrafted = false;
    private String scytheName;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("scythe").setExecutor(this);
        recipeKey = new NamespacedKey(this, "scythe_recipe");

        // Load configurable values
        scytheName = ChatColor.translateAlternateColorCodes('&', getConfig().getString("scythe-name", "&5Scythe"));

        // Load persisted crafted status
        scytheCrafted = getConfig().getBoolean("scythe-crafted", false);

        // Only register recipe if scythe hasn't been crafted yet (or one-time isn't
        // enabled)
        if (!getConfig().getBoolean("one-time-craftable-only", false) || !scytheCrafted) {
            registerRecipe();
        }

        printBanner();
    }

    private void printBanner() {
        String g = "\u00a76"; // gold (hair/dark)
        String y = "\u00a7e"; // yellow (skin)
        String o = "\u00a7x\u00a7F\u00a7F\u00a78\u00a7C\u00a70\u00a70"; // orange #FF8C00
        String w = "\u00a7f"; // white (highlights)
        String r = "\u00a7r"; // reset

        String[] banner = {
                g + "::::::::::::::::::::::::::::::::::::::::",
                g + "::::::::::::::::::::::::::::::::::::::::",
                g + "::::::::::::::::::::::::::::::::::::::::",
                g + "::::::::::::::::::::::::::::::::::::::::",
                g + "::::::::::::::::::::::::::::::::::::::::",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                o + "========================================",
                o + "========================================",
                o + "========================================",
                o + "========================================",
                o + "========================================",
                y + "---------------" + o + "==========" + y + "---------------",
                g + ":::::::::::::::" + w + "++++++++++" + g + ":::::::::::::::",
                g + ":::::::::::::::" + w + "++++++++++" + g + ":::::::::::::::",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                y + "-----" + g + "::::::::::::::::::::::::::::::" + y + "-----",
                "",
                g + "  ManePearsScythe " + r + "- Made by oneaura - guns.lol/oneaura",
                r + "- Scythe texture by Oasis - modrinth.com/user/OasisSucksAtMC"
        };

        for (String line : banner) {
            Bukkit.getConsoleSender().sendMessage(line + r);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.removeRecipe(recipeKey);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setResourcePack(RESOURCE_PACK_URL, hexToBytes(RESOURCE_PACK_SHA1));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (player.isOp()) {
                player.getInventory().addItem(createScythe());
                String prefix = getPrefix();
                player.sendMessage(prefix + " \u00a7aYou received the Scythe!");
            } else {
                player.sendMessage("\u00a7cYou do not have permission to use this command.");
            }
            return true;
        }
        return false;
    }

    private ItemStack createScythe() {
        ItemStack scythe = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta meta = scythe.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(scytheName);
            // meta.setLore(Arrays.asList(
            // "\u00a77A legendary weapon.",
            // " ",
            // "\u00a76\u00a7lAbilities:",
            // "\u00a7e\u26a1 Swift Cleave (Sword Sweep)",
            // "\u00a7e\ud83d\udee1 Shield Breaker",
            // "\u00a7e\u2601 Featherweight (No Fall Dmg)",
            // "\u00a7e\ud83d\udd28 Seismic Smash"));
            meta.setCustomModelData(1001);
            scythe.setItemMeta(meta);
        }
        return scythe;
    }

    // --- RECIPE ---

    private void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, createScythe());

        ConfigurationSection recipeSection = getConfig().getConfigurationSection("recipe");
        if (recipeSection == null) {
            getLogger().warning("No recipe section found in config.yml! Using default recipe.");
            recipe.shape("   ", " H ", "B N");
            recipe.setIngredient('H', Material.HEAVY_CORE);
            recipe.setIngredient('B', Material.BREEZE_ROD);
            recipe.setIngredient('N', Material.NETHERITE_INGOT);
            Bukkit.addRecipe(recipe);
            return;
        }

        String row1 = recipeSection.getString("row1", "   ");
        String row2 = recipeSection.getString("row2", " H ");
        String row3 = recipeSection.getString("row3", "B N");
        recipe.shape(row1, row2, row3);

        ConfigurationSection ingredients = recipeSection.getConfigurationSection("ingredients");
        if (ingredients != null) {
            for (Map.Entry<String, Object> entry : ingredients.getValues(false).entrySet()) {
                String key = entry.getKey();
                String materialName = entry.getValue().toString();
                if (key.length() == 1) {
                    Material mat = Material.matchMaterial(materialName);
                    if (mat != null) {
                        recipe.setIngredient(key.charAt(0), mat);
                    } else {
                        getLogger().warning("Invalid material '" + materialName + "' for ingredient '" + key + "'");
                    }
                }
            }
        }

        Bukkit.addRecipe(recipe);
    }

    // --- EVENTS ---

    // Block auto crafters and crafting tables when one-time-craftable is active
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        if (event.getRecipe() == null)
            return;
        if (!isScythe(event.getRecipe().getResult()))
            return;

        if (getConfig().getBoolean("one-time-craftable-only", false) && scytheCrafted) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!isScythe(event.getRecipe().getResult()))
            return;

        // Block if one-time-craftable is enabled and already crafted
        if (getConfig().getBoolean("one-time-craftable-only", false) && scytheCrafted) {
            event.setCancelled(true);
            if (event.getWhoClicked() instanceof Player player) {
                String prefix = getPrefix();
                player.sendMessage(prefix + " \u00a7cThe Scythe has already been crafted!");
            }
            return;
        }

        scytheCrafted = true;

        // Persist crafted status and remove recipe so auto crafters can't make more
        if (getConfig().getBoolean("one-time-craftable-only", false)) {
            getConfig().set("scythe-crafted", true);
            saveConfig();
            Bukkit.removeRecipe(recipeKey);
        }

        String prefix = getPrefix();
        String crafterName = event.getWhoClicked().getName();

        // Broadcast to all players
        Bukkit.broadcastMessage(prefix + " \u00a7e" + crafterName + " \u00a7dhas crafted the Scythe!");

        // Play sound for all players
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        }
    }

    @EventHandler
    public void onEnchant(PrepareItemEnchantEvent event) {
        if (isScythe(event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        ItemStack firstSlot = event.getInventory().getItem(0);
        ItemStack secondSlot = event.getInventory().getItem(1);
        if (isScythe(firstSlot) || isScythe(secondSlot)) {
            event.setResult(null);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player player) {
            if (isHoldingScythe(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) {
            return;
        }

        if (!isHoldingScythe(attacker))
            return;

        // --- SHIELD BREAK LOGIC (Players only) ---
        if (event.getEntity() instanceof Player victim) {
            boolean isBlocking = victim.isBlocking();
            boolean hasShield = (victim.getInventory().getItemInMainHand().getType() == Material.SHIELD
                    || victim.getInventory().getItemInOffHand().getType() == Material.SHIELD);
            boolean handRaised = victim.isHandRaised();
            boolean isCancelled = event.isCancelled();
            double finalDamage = event.getDamage();

            if (isBlocking || (hasShield && handRaised && (isCancelled || finalDamage == 0))) {
                disableShield(victim);
                victim.getWorld().playSound(victim.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                victim.getWorld().spawnParticle(Particle.CRIT, victim.getEyeLocation(), 15, 0.5, 0.5, 0.5, 0.1);
                return;
            }
        }

        if (event.isCancelled())
            return;
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            return;

        // --- BUFF LOGIC (Standard Hits) ---
        event.setDamage(event.getDamage() + 4.0);

        // --- SMASH ATTACK LOGIC (Overwrites Buff if Falling) ---
        float fallDistance = attacker.getFallDistance();

        if (fallDistance > 1.5 && !attacker.isOnGround() && !attacker.isClimbing() && !attacker.isInWater()) {

            // Scythe base damage (12) + 1.5 HP per block fallen
            double totalDamage = 12.0 + (fallDistance * 1.5);

            // Set base damage and bypass armor/enchantment reduction
            event.setDamage(EntityDamageEvent.DamageModifier.BASE, totalDamage);
            for (EntityDamageEvent.DamageModifier modifier : EntityDamageEvent.DamageModifier.values()) {
                if (modifier != EntityDamageEvent.DamageModifier.BASE && event.isApplicable(modifier)) {
                    event.setDamage(modifier, 0);
                }
            }

            attacker.getWorld().playSound(event.getEntity().getLocation(), Sound.ITEM_MACE_SMASH_GROUND, 1.0f, 1.0f);
            attacker.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, event.getEntity().getLocation(), 1);

            attacker.setFallDistance(0);
        }
    }

    // --- UTILS ---

    private String getPrefix() {
        String raw = getConfig().getString("prefix", "&b[Scythe]");
        return ChatColor.translateAlternateColorCodes('&', raw);
    }

    private void disableShield(Player player) {
        boolean offhand = player.getInventory().getItemInOffHand().getType() == Material.SHIELD;
        ItemStack shield;
        if (offhand) {
            shield = player.getInventory().getItemInOffHand().clone();
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
        } else {
            shield = player.getInventory().getItemInMainHand().clone();
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }

        player.updateInventory();
        player.setCooldown(Material.SHIELD, 100);

        getServer().getScheduler().runTaskLater(this, () -> {
            if (offhand) {
                player.getInventory().setItemInOffHand(shield);
            } else {
                player.getInventory().setItemInMainHand(shield);
            }
            player.updateInventory();
        }, 3L);
    }

    private boolean isHoldingScythe(Player player) {
        return isScythe(player.getInventory().getItemInMainHand());
    }

    private boolean isScythe(ItemStack item) {
        if (item == null || !item.hasItemMeta())
            return false;
        ItemMeta meta = item.getItemMeta();
        return meta.hasDisplayName() && meta.getDisplayName().equals(scytheName);
    }

    private static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }
}