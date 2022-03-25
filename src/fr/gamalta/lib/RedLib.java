package fr.gamalta.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.redblock.core.RedCore;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class RedLib {

	public void sendActionBar(Player player, Configuration configuration, String path, @Nullable Map<String, String> replaces) {

		sendActionBar(Arrays.asList(player), configuration, path, replaces);

	}

	public void sendActionBar(Collection<? extends Player> players, Configuration configuration, String path, @Nullable Map<String, String> replaces) {

		sendActionBar(new ArrayList<Player>(players), configuration, path, replaces);

	}

	public void sendActionBar(List<Player> players, Configuration configuration, String path, @Nullable Map<String, String> replaces) {

		String string = configuration.getString(path);

		if (replaces != null) {

			for (Map.Entry<String, String> entry : replaces.entrySet()) {

				string = string.replace(entry.getKey(), entry.getValue());
			}
		}

		BaseComponent[] components = TextComponent.fromLegacyText(string);

		players.forEach(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components));

	}

	public void playSound(Player player, Configuration configuration, String path) {

		playSound(Arrays.asList(player), configuration, path);
	}

	public void playSound(Collection<? extends Player> players, Configuration configuration, String path) {

		playSound(new ArrayList<Player>(players), configuration, path);
	}

	public void playSound(List<Player> players, Configuration configuration, String path) {

		float volume = 1F;
		float pitch = 1F;
		Sound type = Sound.MUSIC_MENU;

		if (configuration.contains(path)) {

			Object object = configuration.get(path);

			if (object instanceof String) {

				type = Sound.valueOf((String) object);

			} else {

				if (configuration.contains(path + ".Type")) {

					type = Sound.valueOf(configuration.getString(path + ".Type"));
				}

				if (configuration.contains(path + ".Volume")) {

					volume = configuration.getInt(path + ".Volume") / 100;
				}

				if (configuration.contains(path + ".Pitch")) {

					volume = configuration.getInt(path + "Pitch") / 100;
				}
			}
		}

		for (Player player : players) {

			player.playSound(player.getLocation(), type, volume, pitch);
		}
	}

	public void sendTitle(Player player, Configuration configuration, String path) {

		sendTitle(Arrays.asList(player), configuration, path);

	}

	public void sendTitle(Collection<? extends Player> players, Configuration configuration, String path) {

		sendTitle(new ArrayList<Player>(players), configuration, path);
	}

	public void sendTitle(List<Player> players, Configuration config, String path) {

		path = path + ".";
		int fadeIn = 10;
		int stay = 10;
		int fadeOut = 10;
		String title = " ";
		String subTitle = " ";

		Object obj = config.get(path);

		if (obj instanceof String) {

			subTitle = (String) obj;

		} else {

			if (config.contains(path + "Title")) {

				title = config.getString(path + "Title");

			}

			if (config.contains(path + "SubTitle")) {

				subTitle = config.getString(path + "SubTitle");

			}

			if (config.contains(path + "FadeIn")) {

				fadeIn = config.getInt(path + "FadeIn");

			}

			if (config.contains(path + "Stay")) {

				stay = config.getInt(path + "Stay");

			}

			if (config.contains(path + "FadeOut")) {

				fadeOut = config.getInt(path + "FadeOut");

			}
		}

		for (Player player : players) {

			player.sendTitle(color(title), color(subTitle), fadeIn, stay, fadeOut);
		}
	}

	public String color(String string) {

		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public List<String> color(List<String> strings) {

		int i = 0;
		for (String string : strings) {
			strings.set(i, color(string));
			i++;
		}

		return strings;
	}

	public HashMap<String, Configuration> getPlayersDataFiles() {
		return RedCore.getInstance().configurations;
	}
}
