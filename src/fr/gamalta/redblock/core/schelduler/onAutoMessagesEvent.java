package fr.gamalta.redblock.core.schelduler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.gamalta.lib.RedLib;
import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;

public class onAutoMessagesEvent {

	RedLib lib;
	int messageID = 1;

	public onAutoMessagesEvent(RedCore main) {

		lib = new RedLib();
		long refresh = (long) (main.settingsCFG.getDouble("AutomaticMessage.Time") * 1200L);

		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {

			if (!main.settingsCFG.contains("AutomaticMessage.Message." + messageID)) {

				messageID = 1;
			}

			for (Player player : Bukkit.getOnlinePlayers()) {

				player.spigot().sendMessage(new Message(main, main.settingsCFG, "AutomaticMessage.Message." + messageID + ".Message").create());
			}

			if (main.settingsCFG.contains("AutomaticMessage.Message." + messageID + ".Sound")) {

				lib.playSound(Bukkit.getOnlinePlayers(), main.settingsCFG, "AutomaticMessage.Message." + messageID + ".Sound");
			}

			messageID++;
		}, refresh, refresh);
	}
}