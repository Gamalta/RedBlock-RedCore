package fr.gamalta.redblock.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import fr.gamalta.redblock.core.RedCore;

public class onBungeeCordMessagingEvent implements PluginMessageListener {

	public onBungeeCordMessagingEvent(RedCore main) {
	}

	@Override
	public void onPluginMessageReceived(String channel, Player arg, byte[] message) {

		if (channel.equals("BungeeCord")) {

			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			String subchannel = in.readUTF();

			if (subchannel.equals("sound")) {

				String target = in.readUTF();
				String soundId = in.readUTF();
				Float volume = in.readFloat();
				Float pitch = in.readFloat();

				if (target != null && soundId != null && volume != null && pitch != null) {

					if (target.equalsIgnoreCase("all")) {

						for (Player player : Bukkit.getOnlinePlayers()) {

							player.playSound(player.getLocation(), soundId, SoundCategory.PLAYERS, volume, pitch);
						}

					} else {

						Player player = Bukkit.getPlayer(target);

						if (player != null) {

							player.playSound(player.getLocation(), soundId, volume, pitch);
						}
					}
				}
			}
		}
	}
}
