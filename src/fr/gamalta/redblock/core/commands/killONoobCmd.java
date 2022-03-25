package fr.gamalta.redblock.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class killONoobCmd implements CommandExecutor {

	RedCore main;
	Utils utils;

	public killONoobCmd(RedCore main) {

		this.main = main;
		utils = new Utils(main);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {

			Player player = (Player) sender;

			if (player.hasPermission(main.permissionsCFG.getString("KillONoob.Give"))) {

				if (utils.hasAvailableSlot(player)) {

					player.getInventory().addItem(utils.getKillONoob());
					player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.Give").create());

				} else {

					player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.InventoryFull").create());

				}
			} else {

				player.spigot().sendMessage(new Message(main, main.messageCFG, "KillONoob.NoPermission.Give").create());
			}
		}
		return false;
	}
}