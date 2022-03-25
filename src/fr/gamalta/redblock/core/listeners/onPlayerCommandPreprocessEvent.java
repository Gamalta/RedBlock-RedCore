package fr.gamalta.redblock.core.listeners;

import java.time.LocalTime;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.gamalta.lib.RedLib;
import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onPlayerCommandPreprocessEvent implements Listener {

	RedCore main;
	Utils utils;
	RedLib lib;

	public onPlayerCommandPreprocessEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);
		lib = new RedLib();
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

		Player player = event.getPlayer();
		String command = event.getMessage().substring(1);
		String[] commandArgs = command.split(" ");

		if (main.settingsCFG.contains("Worlds." + player.getWorld().getName() + ".Disable")) {

			ConfigurationSection section = main.settingsCFG.getConfigurationSection("Worlds." + player.getWorld().getName() + ".Disable");

			section.getKeys(false).forEach(string -> {

				String sectionCommand = section.getString(string + ".Command").toLowerCase();

				if (command.equalsIgnoreCase(sectionCommand) || command.toLowerCase().startsWith(sectionCommand)) {

					if (!player.hasPermission(section.getString(string + ".Permission"))) {

						event.setCancelled(true);
						player.spigot().sendMessage(new Message(main, main.messageCFG, "CommandDisabled").create());
						return;

					}
				}
			});
		}

		if (main.ninja.contains(player) || main.ninjaDeactivation.containsKey(player)) {

		}

		if (main.settingsCFG.contains("NinjaCommandDisable." + command)) {

			event.setCancelled(true);
			player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Command").create());
		}

		if (commandArgs[0].equalsIgnoreCase("vanish") || commandArgs[0].equalsIgnoreCase("v") || commandArgs[0].equalsIgnoreCase("essentials:v") || commandArgs[0].equalsIgnoreCase("essentials:vanish")) {

			event.setCancelled(true);

			if (player.hasPermission(main.permissionsCFG.getString("Vanish.Use"))) {

				if (!main.ninja.contains(player) && !main.ninjaDeactivation.containsKey(player)) {

					LocalTime time = LocalTime.now();

					if (main.vanishDeactivation.containsKey(player)) {

						main.vanishDeactivation.remove(player);
						main.vanish.add(player);
						player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.CancelDisable").create());

					} else if (main.vanish.contains(player)) {

						main.vanish.remove(player);
						main.vanishDeactivation.put(player, time);
						player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.Deactivation").replace("%cooldown%", main.settingsCFG.getLong("Vanish.Cooldown") + "").create());

						Bukkit.getScheduler().runTaskLater(main, (Runnable) () -> {

							if (main.vanishDeactivation.containsKey(player)) {

								player.setGlowing(false);
								Bukkit.getOnlinePlayers().stream().forEach(pl -> pl.showPlayer(main, player));
								main.vanish.remove(player);
								player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.Disable").create());

								main.vanishDeactivation.remove(player);
							}
						}, main.settingsCFG.getLong("Vanish.Cooldown") * 20);

					} else {

						main.vanish.add(player);
						Bukkit.getOnlinePlayers().stream().filter(pl -> !pl.hasPermission(main.permissionsCFG.getString("Vanish.View"))).forEach(pl -> pl.hidePlayer(main, player));
						player.setGlowing(true);
						player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.Enable").create());

					}
				} else {

					player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.Ninja").create());

				}
			} else {

				player.spigot().sendMessage(new Message(main, main.messageCFG, "Vanish.NoPermission").create());

			}
		}
	}
}