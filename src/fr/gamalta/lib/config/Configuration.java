package fr.gamalta.lib.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {
	
	FileConfiguration config;
	private File file;
	private String fileName;
	String parentFileName;
	private JavaPlugin plugin;
	
	public Configuration(JavaPlugin plugin, String parentFileName, String fileName) {
		
		this(plugin, parentFileName, fileName, fileName);
		
	}
	
	public Configuration(JavaPlugin plugin, String parentFileName, String fileName, String resource) {
		
		this.plugin = plugin;
		this.fileName = fileName.endsWith(".yml") ? fileName : fileName + ".yml";
		this.parentFileName = parentFileName.equals("") ? parentFileName : parentFileName.endsWith("/") ? parentFileName : parentFileName + File.separator;
		file = new File("plugins/RedBlock/" + this.parentFileName + this.fileName);
		createConfig(this.parentFileName + (resource.endsWith(".yml") ? resource : resource + ".yml"));
		load();
		
	}
	
	public boolean contains(String path) {
		
		Object obj;
		
		try {
			
			obj = config.get(path);
			
		} catch (Exception e) {
			return false;
		}
		
		return obj != null;
	}
	
	private void createConfig(String resource) {
		
		if (!file.exists()) {
			
			if (!file.getParentFile().exists()) {
				
				try {
					
					file.getParentFile().mkdirs();
					file.createNewFile();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
			
			InputStream in = plugin.getResource(resource);
			
			if (in != null) {
				try {
					
					OutputStream out = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
					in.close();
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
	}
	
	public void deleteConfig() {
		
		file.delete();
		
	}
	
	public Object get(String path) {
		
		return config.get(path);
	}
	
	public Object get(String path, Object def) {
		
		return config.get(path, def);
	}
	
	public boolean getBoolean(String path) {
		
		return config.getBoolean(path);
	}
	
	public boolean getBoolean(String path, Boolean def) {
		
		return config.getBoolean(path, def);
	}
	
	public List<Boolean> getBooleanList(String path) {
		
		return config.getBooleanList(path);
	}
	
	public List<Byte> getByteList(String path) {
		
		return config.getByteList(path);
	}
	
	public FileConfiguration getConfig() {
		
		return config;
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		
		return config.getConfigurationSection(path);
	}
	
	public double getDouble(String path) {
		
		return config.getDouble(path);
	}
	
	public List<Double> getDoubleList(String path) {
		
		return config.getDoubleList(path);
	}
	
	public File getFile() {
		
		return file;
		
	}
	
	public List<Float> getFloatList(String path) {
		
		return config.getFloatList(path);
	}
	
	public Integer getInt(String path) {
		
		return config.getInt(path);
	}
	
	public Long getLong(String path) {
		
		return config.getLong(path);
	}
	
	public List<Long> getLongList(String path) {
		
		return config.getLongList(path);
	}
	
	public List<Short> getShortList(String path) {
		
		return config.getShortList(path);
	}
	
	public String getString(String path) {
		
		return config.getString(path);
	}
	
	public String getString(String path, String def) {
		
		return config.getString(path, def);
	}
	
	public List<String> getStringList(String path) {
		
		return config.getStringList(path);
	}
	
	public boolean isConfigurationSection(String string) {
		
		return config.isConfigurationSection(string);
	}
	
	public void load() {
		
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void saveConfig() {
		
		try {
			
			config.save(file);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void set(String path, Object obj) {
		
		config.set(path, obj);
		saveConfig();
	}
	
	@Override
	public String toString() {
		
		return fileName;
	}
}