package fr.gamalta.lib.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gamalta.lib.RedLib;
import fr.gamalta.lib.config.Configuration;

public class RedItem {

	private JavaPlugin main;
	private RedLib lib;
	private FileConfiguration config;
	private String path;
	private String name;
	private int amount = 1;
	private int durability = -1;
	private String owningPlayer;
	private Material material = Material.GRASS_BLOCK;
	private List<String> lores = new ArrayList<>();
	private List<ItemFlag> itemFlags = new ArrayList<>();
	private HashMap<Enchantment, Integer> enchantments = new HashMap<>();
	private Boolean isUnbreakable = false;
	private Boolean isStackable = true;

	public RedItem(JavaPlugin main, Configuration config, String path) {

		this(main, config.getConfig(), path);
	}

	public RedItem(JavaPlugin main, FileConfiguration config, String path) {

		this.main = main;
		this.config = config;
		this.path = path;
		lib = new RedLib();
		load();
	}

	public RedItem(JavaPlugin main, FileConfiguration config, String path, String name, int amount, int durability, String owningPlayer, Material material, List<String> lores, List<ItemFlag> itemFlags, HashMap<Enchantment, Integer> enchantments, Boolean isUnbreakable, Boolean isStackable) {

		this.main = main;
		this.config = config;
		this.path = path;
		lib = new RedLib();
		this.name = name;
		this.amount = amount;
		this.durability = durability;
		this.owningPlayer = owningPlayer;
		this.material = material;
		this.lores = lores;
		this.itemFlags = itemFlags;
		this.enchantments = enchantments;
		this.isUnbreakable = isUnbreakable;
		this.isStackable = isStackable;

	}

	private void load() {

		ConfigurationSection section = config.getConfigurationSection(path);

		if (section != null) {

			material = Material.getMaterial(section.getString("Type"));

			if (section.contains("Name")) {

				name = ChatColor.WHITE + section.getString("Name");

			}

			if (section.contains("Amount")) {

				amount = section.getInt("Amount");

			}

			if (section.contains("Durability")) {

				durability = section.getInt("Durability");
			}

			if (material == Material.PLAYER_HEAD) {

				if (section.contains("HeadOwner")) {

					owningPlayer = section.getString("HeadOwner");
				}
			}

			if (section.contains("Lores")) {

				lores = section.getStringList("Lores");

			}

			if (section.contains("ItemFlags")) {

				section.getStringList("ItemFlags").forEach(itemFlag -> itemFlags.add(ItemFlag.valueOf(itemFlag)));

			}

			if (section.contains("Enchantments")) {

				section.getConfigurationSection("Enchantments").getValues(true).forEach((key, value) -> enchantments.put(Enchantment.getByKey(NamespacedKey.minecraft(key)), (Integer) value));

			}

			if (section.contains("IsUnbreakable")) {

				isUnbreakable = section.getBoolean("IsUnbreakable");

			}

			if (section.contains("Stackable")) {

				isStackable = section.getBoolean("Stackable");

			}
		}
	}

	public RedItem replace(String oldString, String newString) {

		if (name != null) {

			name = name.replace(oldString, newString);
		}

		if (owningPlayer != null) {

			owningPlayer = owningPlayer.replace(oldString, newString);
		}

		int i = 0;

		for (String string : lores) {

			lores.set(i, string.replace(oldString, newString));
			i++;
		}

		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemStack create() {

		ItemStack item = new ItemStack(material, amount);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
		itemMeta.setLore(lib.color(lores));
		itemMeta.setUnbreakable(isUnbreakable);

		if (!isStackable) {

			itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "uuid"), PersistentDataType.STRING, UUID.randomUUID().toString());

		}

		if (name != null) {

			itemMeta.setDisplayName(lib.color(name));
		}

		if (itemMeta instanceof Damageable && durability != -1) {

			((Damageable) itemMeta).setDamage(material.getMaxDurability() - durability);

		}

		if (itemMeta instanceof SkullMeta && owningPlayer != null) {

			((SkullMeta) itemMeta).setOwner(owningPlayer);

		}

		enchantments.forEach((key, value) -> itemMeta.addEnchant(key, value, false));

		item.setItemMeta(itemMeta);
		return item;
	}

	public FileConfiguration getConfig() {

		return config;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getName() {

		return name;
	}

	public void setAmount(int amount) {

		this.amount = amount;
	}

	public int getAmount() {

		return amount;
	}

	public void setDurability(int durability) {

		this.durability = durability;
	}

	public int getDurability() {

		return durability;
	}

	public void setOwningPlayer(String owningPlayer) {

		this.owningPlayer = owningPlayer;
	}

	public String getOwningPlayer() {

		return owningPlayer;
	}

	public void setMaterial(Material material) {

		this.material = material;
	}

	public Material getMaterial() {

		return material;
	}

	public void setLores(List<String> lores) {

		this.lores = lores;
	}

	public void addLore(String lore) {

		lores.add(lore);
	}

	public void addLore(List<String> lores) {

		this.lores.addAll(lores);
	}

	public List<String> getLores() {
		return lores;
	}

	public void setItemFlags(List<ItemFlag> itemFlags) {

		this.itemFlags = itemFlags;
	}

	public void addItemFlag(ItemFlag itemFlag) {

		itemFlags.add(itemFlag);
	}

	public List<ItemFlag> getItemFlags() {

		return itemFlags;
	}

	public void setEnchantments(HashMap<Enchantment, Integer> enchantments) {

		this.enchantments = enchantments;
	}

	public void addEnchantment(Enchantment enchantment, Integer level) {

		enchantments.put(enchantment, level);
	}

	public Map<Enchantment, Integer> getEnchantments() {

		return enchantments;
	}

	public boolean isStackable() {

		return isStackable;
	}

	public void setStackable(boolean isStackable) {

		this.isStackable = isStackable;
	}

	public boolean isUnbreakable() {

		return isUnbreakable;
	}

	public void setUnbreakable(Boolean isUnbreakable) {

		this.isUnbreakable = isUnbreakable;
	}

	@Override
	public RedItem clone() {
		return new RedItem(main, config, path, name == null ? null : new String(name), new Integer(amount), new Integer(durability), owningPlayer == null ? null : new String(owningPlayer), Material.getMaterial(material.name()), new ArrayList<String>(lores), new ArrayList<ItemFlag>(itemFlags), new HashMap<Enchantment, Integer>(enchantments), new Boolean(isUnbreakable), new Boolean(isStackable));
	}
}