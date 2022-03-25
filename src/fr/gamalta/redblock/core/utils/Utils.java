package fr.gamalta.redblock.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.lib.item.RedItem;
import fr.gamalta.lib.message.Advancements;
import fr.gamalta.redblock.core.RedCore;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class Utils {
	
	RedCore main;
	
	public Utils(RedCore main) {
		
		this.main = main;
	}
	
	public void createConfig(RedCore core, File file) {
		
		if (!file.exists()) {
			
			try {
				
				if (!file.getParentFile().exists()) {
					
					file.getParentFile().mkdirs();
					
				}
				
				file.createNewFile();
				
			} catch (IOException ignored) {
			}
			
			InputStream in = core.getResource(file.getName());
			
			try {
				
				OutputStream out = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.close();
				in.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
	}
	
	public void createDataFile(Player player) {
		
		String uuid = player.getUniqueId().toString();
		
		if (!main.configurations.containsKey(uuid)) {
			
			Configuration configuration = new Configuration(main, main.dataFileName + "/Players", uuid, "Default");
			main.configurations.put(uuid, configuration);
			
		}
	}
	
	public ItemStack getKillONoob() {
		
		ItemStack item = new RedItem(main, main.settingsCFG, "KillONoob.Item").create();
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "osgarod.item"), PersistentDataType.STRING, "killonoob");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public ItemStack getLaunchpad(int veloX, int veloY) {
		
		ItemStack item = new RedItem(main, main.settingsCFG, "Launchpad.Item").replace("%VelocityX%", veloX + "").replace("%VelocityY%", veloY + "").create();
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "osgarod.launchpad.velocity.x"), PersistentDataType.INTEGER, veloX);
		itemMeta.getPersistentDataContainer().set(new NamespacedKey(main, "osgarod.launchpad.velocity.y"), PersistentDataType.INTEGER, veloX);
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public int getLaunchpadCount() {
		
		int count = 0;
		
		if (main.launchpadsCFG.isConfigurationSection("Launchpads")) {
			
			count = main.launchpadsCFG.getConfigurationSection("Launchpads").getKeys(false).size();
		}
		
		return count;
	}
	
	public String getLaunchpadID(Location location) {
		
		if (main.launchpadsCFG.isConfigurationSection("Launchpads")) {
			
			for (String section : main.launchpadsCFG.getConfigurationSection("Launchpads").getKeys(false)) {
				
				World world = Bukkit.getWorld(main.launchpadsCFG.getString("Launchpads." + section + ".World"));
				int x = main.launchpadsCFG.getInt("Launchpads." + section + ".X");
				int y = main.launchpadsCFG.getInt("Launchpads." + section + ".Y");
				int z = main.launchpadsCFG.getInt("Launchpads." + section + ".Z");
				
				if (location.getWorld() == world && location.getBlockX() == x && location.getBlockY() == y && location.getBlockZ() == z) {
					
					return section;
				}
			}
		}
		return "ERROR";
	}
	
	public boolean hasAvailableSlot(Player player) {
		
		for (int i = 0; i < 36; i++) {
			
			if (player.getInventory().getItem(i) == null) {
				
				return true;
			}
		}
		return false;
	}
	
	public boolean isInteger(String string) {
		
		try {
			
			Integer.parseInt(string);
			return true;
			
		} catch (Exception e) {
			
			return false;
		}
	}
	
	public boolean isLaunchpad(Location location) {
		
		return getLaunchpadID(location).equals("ERROR") ? false : true;
		
	}
	
	public boolean launchpadIsPlate() {
		
		ItemStack item = getLaunchpad(0, 0);
		Material material = item.getType();
		
		if (material == Material.ACACIA_PRESSURE_PLATE || material == Material.BIRCH_PRESSURE_PLATE || material == Material.DARK_OAK_PRESSURE_PLATE || material == Material.OAK_PRESSURE_PLATE || material == Material.SPRUCE_PRESSURE_PLATE || material == Material.JUNGLE_PRESSURE_PLATE || material == Material.STONE_PRESSURE_PLATE || material == Material.HEAVY_WEIGHTED_PRESSURE_PLATE || material == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
			
			return true;
			
		}
		return false;
	}
	
	public void sendJoinAdvancement(Player player) {
		
		if (!player.isValid()) {
			
			Bukkit.getScheduler().runTaskLater(main, () -> sendJoinAdvancement(player), 20L);
			
		} else {
			
			ItemStack icon = new ItemStack(Material.PLAYER_HEAD);
			SkullMeta skullMeta = (SkullMeta) icon.getItemMeta();
			skullMeta.setOwningPlayer(player);
			icon.setItemMeta(skullMeta);
			
			Advancements advancements = new Advancements(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, main.messageCFG.getString("JoinAdvancement").replace("%player%", player.getName()))), icon);
			advancements.displayToast(Bukkit.getOnlinePlayers());
		}
	}
	
	public static String setGradient(String text, int[] colors) {

		String coloredText = "";
		
		double distanceRed = (double) (colors[0] - colors[3]) / text.length();
		double distanceGreen = (double) (colors[1] - colors[4]) / text.length();
		double distanceBlue = (double) (colors[2] - colors[5]) / text.length();
		int z = 0;

		for (int i = 0; i < text.length(); i++) {

			int newRed = (int) (colors[0] - distanceRed * z);
			int newGreen = (int) (colors[1] - distanceGreen * z);
			int newBlue = (int) (colors[2] - distanceBlue * z);

			coloredText = coloredText + ChatColor.of(String.format("#%02x%02x%02x", newRed, newGreen, newBlue)) + text.charAt(z);
			z++;
		}
		return coloredText;
	}
}