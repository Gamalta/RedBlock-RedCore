package fr.gamalta.redblock.core;

import java.io.File;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import fr.gamalta.lib.config.Configuration;
import fr.gamalta.lib.message.Message;
import fr.gamalta.redblock.core.commands.LaunchpadCmd;
import fr.gamalta.redblock.core.commands.NinjaCmd;
import fr.gamalta.redblock.core.commands.killONoobCmd;
import fr.gamalta.redblock.core.listeners.onAsyncPlayerChatEvent;
import fr.gamalta.redblock.core.listeners.onBlockBreakEvent;
import fr.gamalta.redblock.core.listeners.onBlockPlaceEvent;
import fr.gamalta.redblock.core.listeners.onBungeeCordMessagingEvent;
import fr.gamalta.redblock.core.listeners.onDeluxeChatEvent;
import fr.gamalta.redblock.core.listeners.onPlayerChangedWorldEvent;
import fr.gamalta.redblock.core.listeners.onPlayerCommandPreprocessEvent;
import fr.gamalta.redblock.core.listeners.onPlayerCommandSendEvent;
import fr.gamalta.redblock.core.listeners.onPlayerInteractAtEntityEvent;
import fr.gamalta.redblock.core.listeners.onPlayerInteractEvent;
import fr.gamalta.redblock.core.listeners.onPlayerJoinEvent;
import fr.gamalta.redblock.core.listeners.onPlayerMoveEvent;
import fr.gamalta.redblock.core.listeners.onPlayerQuitEvent;
import fr.gamalta.redblock.core.schelduler.onAutoMessagesEvent;
import fr.gamalta.redblock.core.utils.Utils;

public class RedCore extends JavaPlugin {

	private static RedCore INSTANCE;
	private Utils utils = new Utils(this);
	public static String redblock = Utils.setGradient("RedBlock", new int[] { 255, 0, 0, 252, 132, 3 });
	private String parentFileName = "Core";
	public String dataFileName = "Data";
	public Configuration settingsCFG = new Configuration(this, parentFileName, "Settings");
	public Configuration messageCFG = new Configuration(this, parentFileName, "Messages");
	public Configuration permissionsCFG = new Configuration(this, parentFileName, "Permissions");
	public Configuration launchpadsCFG = new Configuration(this, dataFileName, "Launchpads");
	public HashMap<String, Configuration> configurations = new HashMap<>();
	public ArrayList<Player> launchpadCooldown = new ArrayList<>();
	public HashMap<Player, LocalTime> killONoobCooldown = new HashMap<>();
	public HashMap<Player, LocalTime> ninjaDeactivation = new HashMap<>();
	public HashMap<Player, LocalTime> vanishDeactivation = new HashMap<>();
	public ArrayList<Player> ninja = new ArrayList<>();
	public ArrayList<Player> vanish = new ArrayList<>();

	@Override
	public void onEnable() {
		
		initListener();
		initCommands();
		initTabCompleter();
		initClock();
		initOther();
		
	}
	
	@Override
	public void onDisable() {
		
		for (Player player : ninja) {
			
			player.setGlowing(false);
			player.spigot().sendMessage(new Message(this, messageCFG, "Ninja.Reload").create());
			utils.sendJoinAdvancement(player);
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				
				p.spigot().sendMessage(new Message(this, messageCFG, "Join").replace("%player%", player.getName()).create());
				p.showPlayer(this, player);
			}
		}
		
		for (Player player : vanish) {
			
			player.setGlowing(false);
			player.spigot().sendMessage(new Message(this, messageCFG, "Vanish.Reload").create());
			
			for (Player p : Bukkit.getOnlinePlayers()) {
				p.showPlayer(this, player);
			}
		}
	}

	private void initCommands() {
		
		getCommand("Launchpad").setExecutor(new LaunchpadCmd(this));
		getCommand("killonoob").setExecutor(new killONoobCmd(this));
		getCommand("ninja").setExecutor(new NinjaCmd(this));
	}

	private void initTabCompleter() {
		
		getCommand("Launchpad").setTabCompleter(new LaunchpadCmd(this));
	}

	private void initListener() {
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new onAsyncPlayerChatEvent(this), this);
		pm.registerEvents(new onBlockPlaceEvent(this), this);
		pm.registerEvents(new onPlayerMoveEvent(this), this);
		pm.registerEvents(new onBlockBreakEvent(this), this);
		pm.registerEvents(new onPlayerJoinEvent(this), this);
		pm.registerEvents(new onPlayerQuitEvent(this), this);
		pm.registerEvents(new onPlayerInteractAtEntityEvent(this), this);
		pm.registerEvents(new onPlayerInteractEvent(this), this);
		pm.registerEvents(new onPlayerCommandPreprocessEvent(this), this);
		pm.registerEvents(new onPlayerChangedWorldEvent(this), this);
		pm.registerEvents(new onPlayerCommandSendEvent(this), this);
		
	}

	private void initClock() {
		
		new onAutoMessagesEvent(this);
	}
	
	private void initDeluxeChatEvent() {
		
		PluginManager pm = Bukkit.getPluginManager();
		
		if (pm.isPluginEnabled("DeluxeChat")) {
			
			pm.registerEvents(new onDeluxeChatEvent(this), this);
		}
	}
	
	private void initMVdWPlaceholderAPI() {
		
		if (Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")) {
			
			PlaceholderAPI.registerPlaceholder(this, "osgarod_suffix", event -> {
				
				if (event.isOnline()) {
					
					if (ninja.contains(event.getPlayer())) {
						
						return settingsCFG.getString("Ninja.Suffix");
					}
					
					if (vanish.contains(event.getPlayer())) {
						
						return settingsCFG.getString("Vanish.Suffix");
					}
				}
				return "";
			});
		}
	}
	
	private void initOther() {
		
		INSTANCE = this;
		
		initMVdWPlaceholderAPI();
		initDeluxeChatEvent();
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new onBungeeCordMessagingEvent(this));
		
		utils.createConfig(this, new File("plugins/RedBlock/Example.yml"));
	}

	public static RedCore getInstance() {
		return INSTANCE;
	}
}