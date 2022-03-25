package fr.gamalta.lib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gamalta.lib.config.Configuration;
import fr.gamalta.redblock.core.RedCore;

public class SqlConnection {
	
	Connection connection;
	JavaPlugin main;
	String urlbase, host, database, user, pass, pluginName;
	int timeout;
	
	public SqlConnection(Configuration configuration, String path, JavaPlugin main, String pluginName) {
		
		urlbase = configuration.getString(path + ".Protocol");
		host = configuration.getString(path + ".Host");
		database = configuration.getString(path + ".Database");
		user = configuration.getString(path + ".Username");
		pass = configuration.getString(path + ".Password");
		
		if (configuration.contains(path + "TimeOut")) {
			
			timeout = configuration.getInt(path + "TimeOut");
			
		} else {
			
			timeout = 10;
		}
		
		this.main = main;
		this.pluginName = pluginName;
		connection();
	}
	
	public SqlConnection(String urlbase, String host, String database, String user, String pass, JavaPlugin main, String pluginName) {
		
		this(urlbase, host, database, user, pass, main, pluginName, 10);
	}
	
	public SqlConnection(String urlbase, String host, String database, String user, String pass, JavaPlugin main, String pluginName, int timeout) {
		
		this.urlbase = urlbase;
		this.host = host;
		this.database = database;
		this.user = user;
		this.pass = pass;
		this.main = main;
		this.pluginName = pluginName;
		this.timeout = timeout;
		connection();
	}
	
	public void connection() {
		
		if (!isConnected()) {
			
			try {
				
				DriverManager.setLoginTimeout(timeout);
				connection = DriverManager.getConnection(urlbase + host + "/" + database + "?autoReconnect=true", user, pass);
				Bukkit.getConsoleSender().sendMessage("[" + RedCore.redblock + "§r]-[" + pluginName + "]§2 Connexion MySql réussie");
				
			} catch (SQLException e) {
				
				Bukkit.getConsoleSender().sendMessage("[" + RedCore.redblock + "§r]-[" + pluginName + "]§4 Connection MySql échouée !");
				Bukkit.getPluginManager().disablePlugin(main);
			}
		}
	}
	
	public void disconnect() {
		
		if (isConnected()) {
			
			try {
				
				connection.close();
				Bukkit.getConsoleSender().sendMessage("[" + RedCore.redblock + "§r]-[" + pluginName + "]§2 Déonnexion MySql réussie");
				
			} catch (SQLException e) {
				
				Bukkit.getConsoleSender().sendMessage("[" + RedCore.redblock + "§r]-[" + pluginName + "]§4 Déconnexion MySql échouée !");
			}
		}
	}
	
	public Connection getConnection() {
		
		return connection;
	}
	
	public boolean isConnected() {
		
		return connection != null;
	}
}
