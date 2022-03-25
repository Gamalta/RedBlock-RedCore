package fr.gamalta.redblock.core.listeners;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import fr.gamalta.lib.RedLib;
import fr.gamalta.redblock.core.RedCore;
import me.clip.deluxechat.events.DeluxeChatEvent;

public class onDeluxeChatEvent implements Listener {

	private RedCore main;
	private RedLib lib;

	public onDeluxeChatEvent(RedCore main) {

		this.main = main;
		lib = new RedLib();
	}

	@EventHandler
	public void onDeluxeChat(DeluxeChatEvent event) {

		String message = event.getChatMessage();
		Player player = event.getPlayer();

		if (message.contains("@")) {

			String format = "";

			for (Player p : Bukkit.getOnlinePlayers()) {

				if (player != p && message.contains("@" + p.getName())) {

					format += p.getName() + ", ";
					lib.playSound(p, main.settingsCFG, "Mention.Target.Sound");
					lib.sendActionBar(p, main.settingsCFG, "Mention.Target.ActionBar", Collections.singletonMap("%sender%", player.getName()));
					event.setChatMessage(message.replace("@" + p.getName(), main.settingsCFG.getString("Mention.Color." + event.getDeluxeFormat().getIdentifier()).replace("%player%", p.getName())));

				}
			}

			lib.playSound(player, main.settingsCFG, "Mention.Sender.Sound");
			lib.sendActionBar(player, main.settingsCFG, "Mention.Sender.ActionBar", Collections.singletonMap("%target%", format));
		}
	}
}