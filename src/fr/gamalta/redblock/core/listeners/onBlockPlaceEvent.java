package fr.gamalta.redblock.core.listeners;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.RedCore;
import fr.gamalta.redblock.core.utils.Utils;

public class onBlockPlaceEvent implements Listener {

	RedCore main;
	Utils utils;

	public onBlockPlaceEvent(RedCore main) {

		this.main = main;
		utils = new Utils(main);

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {

		Player player = event.getPlayer();
		ItemStack item = event.getItemInHand();

		if (item != null) {

			ItemMeta itemMeta = item.getItemMeta();
			PersistentDataContainer container = itemMeta.getPersistentDataContainer();
			NamespacedKey veloXKey = new NamespacedKey(main, "osgarod.launchpad.velocity.x");
			NamespacedKey veloYKey = new NamespacedKey(main, "osgarod.launchpad.velocity.y");
			PersistentDataType<Integer, Integer> persistentDataString = PersistentDataType.INTEGER;

			if (container.has(veloXKey, persistentDataString) && container.has(veloYKey, persistentDataString)) {

				int veloX = container.get(veloXKey, persistentDataString);
				int veloY = container.get(veloYKey, persistentDataString);

				if (player.hasPermission(main.permissionsCFG.getString("Launchpads.Place"))) {

					int id = utils.getLaunchpadCount() + 1;

					main.launchpadsCFG.set("Launchpads." + id + ".World", event.getBlockPlaced().getWorld().getName());
					main.launchpadsCFG.set("Launchpads." + id + ".X", Integer.valueOf(event.getBlockPlaced().getX()));
					main.launchpadsCFG.set("Launchpads." + id + ".Y", Integer.valueOf(event.getBlockPlaced().getY()));
					main.launchpadsCFG.set("Launchpads." + id + ".Z", Integer.valueOf(event.getBlockPlaced().getZ()));
					main.launchpadsCFG.set("Launchpads." + id + ".Velocity.X", veloX);
					main.launchpadsCFG.set("Launchpads." + id + ".Velocity.Y", veloY);

					player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.Created").create());
					item.setAmount(item.getAmount() - 1);

					if (event.getHand() == EquipmentSlot.HAND) {

						player.getInventory().setItemInMainHand(item);

					} else if (event.getHand() == EquipmentSlot.OFF_HAND) {

						player.getInventory().setItemInOffHand(item);
					}

				} else {

					player.spigot().sendMessage(new Message(main, main.messageCFG, "Launchpads.NoPermission.Place").create());
					player.getInventory().remove(item);
					event.setCancelled(true);
				}
			}
		}
	}
}