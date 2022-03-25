package fr.gamalta.redblock.core.listeners;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitScheduler;

import fr.gamalta.lib.RedLib;
import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerInteractAtEntityEvent implements Listener {

	RedCore main;
	Utils utils;
	RedLib lib;

	public onPlayerInteractAtEntityEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
		lib = new RedLib();
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {

		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();

		if (item != null && item.hasItemMeta() && event.getHand() == EquipmentSlot.HAND) {

			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer container = itemMeta.getPersistentDataContainer();
			NamespacedKey killonoobKey = new NamespacedKey(main, "osgarod.item");
			PersistentDataType<String, String> persistentDataString = PersistentDataType.STRING;

			if (container.has(killonoobKey, persistentDataString) && container.get(killonoobKey, persistentDataString).equals("killonoob")) {

				Entity entity = event.getRightClicked();
				Location loc = entity.getLocation();

				if (entity instanceof Player) {

					Player target = (Player) entity;

					if (player.hasPermission(main.permissionsCFG.getString("KillONoob.Use"))) {

						LocalTime time = LocalTime.now();

						if (main.killONoobCooldown.containsKey(player)) {

							Duration duration = Duration.between(main.killONoobCooldown.get(player), time);

							int cooldown = (int) (main.settingsCFG.getLong("KillONoob.Cooldown") - duration.getSeconds());

							player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.Spam").replace("%Cooldown%", cooldown + "").create());

						} else {

							event.setCancelled(true);
							target.spawnParticle(Particle.MOB_APPEARANCE, loc, 10);
							target.playSound(loc, Sound.ENTITY_GHAST_SCREAM, 1.0f, 1.0f);
							BukkitScheduler scheduler = Bukkit.getScheduler();

							scheduler.runTaskLater(main, () -> {

								target.setHealth(0.0D);
								player.getWorld().strikeLightningEffect(loc);

								Collection<? extends Player> players = Bukkit.getOnlinePlayers();

								lib.playSound(players, main.settingsCFG, "KillONoob.Sound");

								for (Player p : players) {

									p.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.Broadcast").replace("%player%", target.getName()).create());

								}
							}, 70L);

							scheduler.runTaskLater(main, () -> {

								main.killONoobCooldown.remove(player);

							}, main.settingsCFG.getLong("KillONoob.Cooldown") * 20);
						}
					} else {

						player.getInventory().remove(item);
						player.updateInventory();
						player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.NoPermission.Use").create());
					}

				}
			}
		}
	}
}