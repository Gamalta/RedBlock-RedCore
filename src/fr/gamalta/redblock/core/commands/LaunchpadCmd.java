package fr.gamalta.redblock.core.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class LaunchpadCmd implements CommandExecutor, TabCompleter {

	RedCore main;
	Utils utils;

	public LaunchpadCmd(RedCore main) {

		this.main = main;
		utils = new Utils(main);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (player.hasPermission(main.permissionsCFG.getString("Launchpads.Give"))) {

				if (args.length > 0) {

					if (args[0].equalsIgnoreCase("create")) {

						if (args.length == 3 && utils.isInteger(args[1]) && utils.isInteger(args[2])) {

							if (utils.hasAvailableSlot(player)) {

								player.getInventory().addItem(utils.getLaunchpad(Integer.valueOf(args[1]), Integer.valueOf(args[2])));
								player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Give").create());

							} else {

								player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.InventoryFull").create());

							}
						} else {

							player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Usage.Create").create());

						}
					} else if (args[0].equalsIgnoreCase("list")) {

						if (utils.getLaunchpadCount() > 0) {

							player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.List.Title").create());

							for (String section : main.launchpadsCFG.getConfigurationSection("Launchpads").getKeys(false)) {

								String world = main.launchpadsCFG.getString("Launchpads." + section + ".World");
								int x = main.launchpadsCFG.getInt("Launchpads." + section + ".X");
								int y = main.launchpadsCFG.getInt("Launchpads." + section + ".Y");
								int z = main.launchpadsCFG.getInt("Launchpads." + section + ".Z");
								int veloX = main.launchpadsCFG.getInt("Launchpads." + section + ".Velocity.X");
								int veloY = main.launchpadsCFG.getInt("Launchpads." + section + ".Velocity.Y");

								player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.List.Item").replace("%ID%", section).replace("%World%", world).replace("%X%", x + "").replace("%Y%", y + "").replace("%Z%", z + "").replace("%VelocityX%", veloX + "").replace("%VelocityY%", veloY + "").create());
							}

						} else {

							player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.List.Empty").create());

						}
					} else {

						player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Usage.Default").create());
					}
				} else {

					player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Usage.Default").create());
				}
			} else {

				player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.NoPermission.Command").create());
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {

		ArrayList<String> tabComplete = new ArrayList<>();

		if (args.length == 1) {

			if (!args[0].equals("")) {

				if ("create".startsWith(args[0].toLowerCase())) {

					tabComplete.add("create");
				}

				if ("list".startsWith(args[0].toLowerCase())) {

					tabComplete.add("list");
				}
			} else {

				tabComplete.add("create");
				tabComplete.add("list");
			}

		} else if (args[0].equalsIgnoreCase("create")) {

			if (args.length == 2) {

				if (args[1].equals("")) {

					for (int i = 0; i < 10; i++) {

						tabComplete.add(i + "");

					}
				} else if (!args[1].equals("0")) {

					for (int i = 0; i < 10; i++) {

						tabComplete.add(args[1] + i);

					}
				}

			} else if (args.length == 3) {

				if (args[2].equals("")) {

					for (int i = 0; i < 10; i++) {

						tabComplete.add(i + "");

					}
				} else if (!args[2].equals("0")) {

					for (int i = 0; i < 10; i++) {

						tabComplete.add(args[2] + i);

					}
				}
			}
		}

		Collections.sort(tabComplete);

		return tabComplete;
	}
}