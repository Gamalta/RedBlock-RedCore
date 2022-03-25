package fr.gamalta.redblock.core.listeners;

import java.time.Duration;
import java.time.LocalTime;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerInteractEvent implements Listener {

	RedCore main;
	Utils utils;

	public onPlayerInteractEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);

	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack item = event.getItem();

		if (item != null && event.getHand() == EquipmentSlot.HAND) {

			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer container = itemMeta.getPersistentDataContainer();
			NamespacedKey killonoobKey = new NamespacedKey(main, "osgarod.item");
			PersistentDataType<String, String> persistentDataString = PersistentDataType.STRING;

			if (container.has(killonoobKey, persistentDataString) && container.get(killonoobKey, persistentDataString).equals("killonoob")) {

				if (player.hasPermission(main.permissionsCFG.getString("KillONoob.Use"))) {

					if (action.equals(Action.LEFT_CLICK_BLOCK) || action.equals(Action.RIGHT_CLICK_BLOCK)) {

						LocalTime time = LocalTime.now();

						if (main.killONoobCooldown.containsKey(player)) {

							Duration duration = Duration.between(main.killONoobCooldown.get(player), time);

							int cooldown = (int) (main.settingsCFG.getLong("KillONoob.Cooldown") - duration.getSeconds());

							player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.Spam").replace("%Cooldown%", cooldown + "").create());

						} else {

							event.setCancelled(true);

							player.getWorld().strikeLightning(event.getClickedBlock().getLocation());
							player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.Block").create());
							main.killONoobCooldown.put(player, time);

							Bukkit.getScheduler().runTaskLater(main, (Runnable) () -> {

								main.killONoobCooldown.remove(player);

							}, main.settingsCFG.getLong("KillONoob.Cooldown") * 20);
						}
					}
				} else {

					player.getInventory().remove(item);
					player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.NoPermission.Use").create());

				}
			}
		}
	}
}