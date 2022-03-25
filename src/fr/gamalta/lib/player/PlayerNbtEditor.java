package fr.gamalta.lib.player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class PlayerNbtEditor {

	public HashMap<Integer, ItemStack> getEnderChest(OfflinePlayer player) {

		HashMap<Integer, ItemStack> items = new HashMap<>();

		if (player.isOnline()) {

			int i = 0;

			for (ItemStack item : player.getPlayer().getEnderChest().getContents()) {

				items.put(i++, item);
			}

			return items;

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				NBTTagList list = compound.getList("EnderItems", 10);

				for (Object element : list) {

					NBTTagCompound itemCopmound = (NBTTagCompound) element;

					if (itemCopmound != null) {

						int slot = itemCopmound.getByte("Slot") & 0xFF;
						ItemStack item = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(itemCopmound));

						items.put(slot, item);

					}
				}

				fileInputStream.close();
				return items;

			} catch (IOException e) {

				e.printStackTrace();
				return items;
			}
		}
	}

	public String getFirstConnexion(OfflinePlayer player) {

		if (player.hasPlayedBefore()) {

			System.out.println(player.getFirstPlayed());

			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTimeInMillis(player.getFirstPlayed());
			int date = gregorianCalendar.get(5);
			int month = gregorianCalendar.get(2) + 1;
			int year = gregorianCalendar.get(1);
			gregorianCalendar.set(year, month, date);

			return gregorianCalendar.getTime().toString();
		}
		return "Never played on server.";

	}

	public long getFirstPlayed(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getFirstPlayed();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				long firstPlayed = compound.getLong("bukkit.firstPlayed");
				fileInputStream.close();

				return firstPlayed;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public float getFlySpeed(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getFlySpeed();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				float flySpeed = compound.getFloat("abilities.flySpeed");
				fileInputStream.close();

				return flySpeed;

			} catch (Exception e) {

				e.printStackTrace();
				return 0.0F;
			}
		}
	}

	public int getFoodLevel(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getFoodLevel();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				int foodLevel = compound.getInt("foodLevel");
				fileInputStream.close();

				return foodLevel;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public float getFoodSaturationLevel(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getSaturation();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				float foodSaturationLevel = compound.getFloat("foodLevel");
				fileInputStream.close();

				return foodSaturationLevel;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	@SuppressWarnings("deprecation")
	public GameMode getGamemode(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getGameMode();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				int gamemode = compound.getInt("playerGameType");
				fileInputStream.close();

				return GameMode.getByValue(gamemode);

			} catch (Exception e) {

				e.printStackTrace();
				return GameMode.SURVIVAL;
			}
		}
	}

	public float getHealth(OfflinePlayer player) {

		if (player.isOnline()) {

			return (float) player.getPlayer().getHealth();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				float health = compound.getFloat("Health");
				fileInputStream.close();

				return health;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public HashMap<Integer, ItemStack> getInventory(OfflinePlayer player) {

		HashMap<Integer, ItemStack> items = new HashMap<>();

		if (player.isOnline()) {

			int i = 0;

			PlayerInventory playerInv = player.getPlayer().getInventory();

			for (ItemStack item : playerInv.getContents()) {

				items.put(i++, item);
			}

			items.put(100, playerInv.getHelmet());
			items.put(101, playerInv.getChestplate());
			items.put(102, playerInv.getLeggings());
			items.put(103, playerInv.getBoots());
			items.put(150, playerInv.getItemInOffHand());

			return items;

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				NBTTagList list = compound.getList("Inventory", 10);

				for (Object element : list) {

					NBTTagCompound itemCopmound = (NBTTagCompound) element;

					if (itemCopmound != null) {

						int slot = itemCopmound.getByte("Slot") & 0xFF;

						ItemStack item = CraftItemStack.asBukkitCopy(net.minecraft.world.item.ItemStack.a(itemCopmound));

						items.put(slot, item);

					}
				}

				fileInputStream.close();
				return items;

			} catch (IOException e) {

				e.printStackTrace();
				return items;
			}
		}
	}

	public String getLastAdresseIp(OfflinePlayer target) {

		return "null";
	}

	public String getLastIpRegion(OfflinePlayer target) {

		return "null";
	}

	public long getLastPlayed(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getLastPlayed();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				long lastPlayed = compound.getLong("bukkit.lastPlayed");
				fileInputStream.close();

				return lastPlayed;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public Location getLocation(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getLocation();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				NBTTagList position = compound.getList("Pos", 6);
				NBTTagList rotation = compound.getList("Rotation", 5);

				UUID world = new UUID(compound.getLong("WorldUUIDMost"), compound.getLong("WorldUUIDLeast"));

				double x = position.d(0);
				double y = position.d(1);
				double z = position.d(2);
				float yaw = rotation.d(0);
				float pitch = rotation.d(1);

				fileInputStream.close();
				return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

			} catch (Exception e) {

				e.printStackTrace();
				return new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
			}
		}
	}

	public File getPlayerFile(OfflinePlayer player) {

		return new File(Bukkit.getWorlds().get(0).getName() + File.separator + "playerdata", player.getUniqueId().toString() + ".dat");

	}

	public String getTimePlay(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 20 / 60 / 60 + "";

		} else {

			return "null";

		}
	}

	public float getWalkSpeed(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getWalkSpeed();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				float walkSpeed = compound.getFloat("abilities.walkSpeed");
				fileInputStream.close();

				return walkSpeed;

			} catch (Exception e) {

				e.printStackTrace();
				return 0.0F;
			}
		}
	}

	public int getXpLevel(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getLevel();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				int xpLevel = compound.getInt("XpLevel");
				fileInputStream.close();

				return xpLevel;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public int getXpTotal(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().getTotalExperience();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				int xpTotal = compound.getInt("XpTotal");
				fileInputStream.close();

				return xpTotal;

			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}
	}

	public boolean isFlying(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().isFlying();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				boolean isFlying = compound.getByte("abilities.flying") != 0;
				fileInputStream.close();

				return isFlying;

			} catch (Exception e) {

				e.printStackTrace();
				return false;
			}
		}
	}

	public boolean isInvulnerable(OfflinePlayer player) {

		if (player.isOnline()) {

			return player.getPlayer().isInvulnerable();

		} else {

			try {

				FileInputStream fileInputStream = new FileInputStream(getPlayerFile(player));
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				boolean invulnerable = compound.getFloat("abilities.invulnerable") != 0;
				fileInputStream.close();

				return invulnerable;

			} catch (Exception e) {

				e.printStackTrace();
				return false;
			}
		}
	}

	public void setEnderChest(OfflinePlayer player, HashMap<Integer, ItemStack> items) {

		if (player.isOnline()) {

			Inventory playerEnderInv = player.getPlayer().getEnderChest();

			for (Entry<Integer, ItemStack> entry : items.entrySet()) {

				playerEnderInv.setItem(entry.getKey(), entry.getValue());
			}
		} else {

			NBTTagList list = new NBTTagList();

			for (Entry<Integer, ItemStack> entry : items.entrySet()) {

				ItemStack item = entry.getValue();

				if (item != null && item.getType() != Material.AIR) {

					NBTTagCompound compound = new NBTTagCompound();
					CraftItemStack.asNMSCopy(item).save(compound);

					compound.setByte("Slot", entry.getKey().byteValue());
					list.add(compound);

				}
			}

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.set("EnderItems", list);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {
			}
		}
	}

	public void setFlying(OfflinePlayer player, byte fly) {

		if (player.isOnline()) {

			player.getPlayer().setFlying(fly == 1);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setByte("abilities.mayfly", fly);
				compound.setByte("abilities.flying", fly);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setFlySpeed(OfflinePlayer player, float flySpeed) {

		if (player.isOnline()) {

			player.getPlayer().setFlySpeed(flySpeed);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setFloat("abilities.flySpeed", flySpeed);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setFoodLevel(OfflinePlayer player, int food) {

		if (player.isOnline()) {

			player.getPlayer().setFoodLevel(food);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setInt("foodLevel", food);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setFoodSaturationLevel(OfflinePlayer player, float foodStaturation) {

		if (player.isOnline()) {

			player.getPlayer().setSaturation(foodStaturation);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setFloat("foodSaturationLevel", foodStaturation);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void setGamemode(OfflinePlayer player, GameMode gameMode) {

		if (player.isOnline()) {

			player.getPlayer().setGameMode(gameMode);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setInt("playerGameType", gameMode.getValue());
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setHealth(OfflinePlayer player, float health) {

		if (player.isOnline()) {

			player.getPlayer().setHealth(health);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setFloat("Health", health);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setInventory(OfflinePlayer player, HashMap<Integer, ItemStack> items) {

		if (player.isOnline()) {

			PlayerInventory playerInv = player.getPlayer().getInventory();

			for (Entry<Integer, ItemStack> entry : items.entrySet()) {

				switch (entry.getKey()) {

					case 100:
						playerInv.setHelmet(entry.getValue());
						break;

					case 101:
						playerInv.setChestplate(entry.getValue());
						break;

					case 102:
						playerInv.setLeggings(entry.getValue());
						break;

					case 103:
						playerInv.setBoots(entry.getValue());
						break;

					case 150:
						playerInv.setItemInOffHand(entry.getValue());
						break;

					default:
						playerInv.setItem(entry.getKey(), entry.getValue());
						break;
				}
			}
		} else {

			NBTTagList list = new NBTTagList();

			for (Entry<Integer, ItemStack> entry : items.entrySet()) {

				ItemStack item = entry.getValue();

				if (item != null && item.getType() != Material.AIR) {

					NBTTagCompound compound = new NBTTagCompound();
					CraftItemStack.asNMSCopy(item).save(compound);

					compound.setByte("Slot", entry.getKey().byteValue());
					list.add(compound);

				}
			}

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.set("Inventory", list);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setInvulnerable(OfflinePlayer player, byte invulnerable) {

		if (player.isOnline()) {

			player.getPlayer().setInvulnerable(invulnerable == 1);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setByte("abilities.invulnerable", invulnerable);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setLocation(OfflinePlayer player, Location location) {

		if (player.isOnline()) {

			player.getPlayer().teleport(location);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setLong("WorldUUIDMost", location.getWorld().getUID().getMostSignificantBits());
				compound.setLong("WorldUUIDLeast", location.getWorld().getUID().getLeastSignificantBits());
				compound.setDouble("Pos.0", location.getX());
				compound.setDouble("Pos.1", location.getY());
				compound.setDouble("Pos.2", location.getZ());
				compound.setFloat("Rotation.0", location.getYaw());
				compound.setFloat("Rotation.1", location.getPitch());
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setWalkSpeed(OfflinePlayer player, float walkSpeed) {

		if (player.isOnline()) {

			player.getPlayer().setWalkSpeed(walkSpeed);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setFloat("abilities.walkSpeed", walkSpeed);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setXpLevel(OfflinePlayer player, int xpLevel) {

		if (player.isOnline()) {

			player.getPlayer().setLevel(xpLevel);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setInt("XpLevel", xpLevel);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void setXpTotal(OfflinePlayer player, int xpTotal) {

		if (player.isOnline()) {

			player.getPlayer().setTotalExperience(xpTotal);

		} else {

			try {

				File playerFile = getPlayerFile(player);
				FileInputStream fileInputStream = new FileInputStream(playerFile);
				NBTTagCompound compound = NBTCompressedStreamTools.a(fileInputStream);
				compound.setInt("XpTotal", xpTotal);
				FileOutputStream fileOutputStream = new FileOutputStream(playerFile);
				NBTCompressedStreamTools.a(compound, fileOutputStream);
				fileOutputStream.flush();
				fileOutputStream.close();
				fileInputStream.close();

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
}