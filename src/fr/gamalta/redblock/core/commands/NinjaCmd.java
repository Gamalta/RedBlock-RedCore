package fr.gamalta.redblock.core.commands;

import java.time.LocalTime;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class NinjaCmd implements CommandExecutor {

	private RedCore main;
	private Utils utils;

	public NinjaCmd(RedCore main) {

		this.main = main;
		utils = new Utils(main);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (player.hasPermission(main.permissionsCFG.getString("Ninja.Use"))) {

				if (main.vanish.contains(player) || main.vanishDeactivation.containsKey(player)) {
					main.vanish.remove(player);
					main.vanishDeactivation.remove(player);
					player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Vanish").create());
				}

				LocalTime time = LocalTime.now();

				if (main.ninjaDeactivation.containsKey(player)) {

					main.ninjaDeactivation.remove(player);
					main.ninja.add(player);
					player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.CancelDisable").create());

				} else if (main.ninja.contains(player)) {

					main.ninja.remove(player);
					main.ninjaDeactivation.put(player, time);
					player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Deactivation").replace("%cooldown%", main.settingsCFG.getLong("Ninja.Cooldown") + "").create());

					Bukkit.getScheduler().runTaskLater(main, (Runnable) () -> {

						if (main.ninjaDeactivation.containsKey(player)) {

							player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Disable").create());
							player.setGlowing(false);

							utils.sendJoinAdvancement(player);

							for (Player p : Bukkit.getOnlinePlayers()) {

								p.spigot().sendMessage(new Message(main, main.messageCFG, "Join").replace("%player%", player.getName()).create());
								p.showPlayer(main, player);
							}

							main.ninjaDeactivation.remove(player);
						}
					}, main.settingsCFG.getLong("Ninja.Cooldown") * 20);
				} else {

					main.ninja.add(player);
					player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.Enable").create());

					for (Player p : Bukkit.getOnlinePlayers()) {

						p.spigot().sendMessage(new Message(main, main.messageCFG, "Quit").replace("%player%", player.getName()).create());

						if (!p.hasPermission(main.permissionsCFG.getString("Ninja.View"))) {

							p.hidePlayer(main, player);

						}
					}

					player.setGlowing(true);
				}
			} else {

				player.spigot().sendMessage(new Message(main, main.messageCFG, "Ninja.NoPermission").create());

			}
		}
		return false;
	}
}