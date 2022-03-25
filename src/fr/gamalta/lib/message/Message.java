package fr.gamalta.lib.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.gamalta.lib.config.Configuration;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Message {

	private Player player;
	private String text = "";
	private HashMap<String, TextComponent> variants = new HashMap<>();

	@SuppressWarnings("deprecation")
	public Message(Configuration configuration, String path) {

		Object obj = configuration.get(path);

		if (obj instanceof String) {

			text = (String) obj;

		} else if (obj instanceof ConfigurationSection) {

			if (configuration.contains(path + ".Variants")) {

				configuration.getConfigurationSection(path + ".Variants").getKeys(false).forEach(variant -> {

					TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', configuration.getString(path + ".Variants." + variant + ".Text")));
					
					if (configuration.contains(path + ".Variants." + variant + ".Action") && configuration.contains(path + ".Variants." + variant + ".Result")) {

						component.setClickEvent(new ClickEvent(ClickEvent.Action.valueOf(configuration.getString(path + ".Variants." + variant + ".Action")), configuration.getString(path + ".Variants." + variant + ".Result")));

					}

					if (configuration.contains(path + ".Variants." + variant + ".Hover")) {

						component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(configuration.getString(path + ".Variants." + variant + ".Hover")).create()));
					}

					variants.put(variant, component);
				});
			}

			if (configuration.contains(path + ".Text")) {

				Object obj1 = configuration.get(path + ".Text");

				if (obj1 instanceof String) {

					text = (String) obj1;

				} else if (obj1 instanceof List<?> && !((List<?>) obj1).isEmpty() && ((List<?>) obj1).get(0) instanceof String) {

					text = String.join("\n", configuration.getStringList(path + ".Text"));

				}
			}

			variants.keySet().stream().filter(string -> text.contains(string)).forEach(string -> text = text.replace(string, "&r" + string + "&r"));

		} else if (obj instanceof List<?> && !((List<?>) obj).isEmpty() && ((List<?>) obj).get(0) instanceof String) {

			text = String.join("\n", configuration.getStringList(path + ".Text"));

		}
	}

	public Message(JavaPlugin main, Configuration configuration, String path) {

		this(configuration, path);
	}

	public void addVariants(String path, TextComponent component) {

		variants.put(path, component);
	}

	public BaseComponent[] create() {
		
		List<BaseComponent> components = new ArrayList<>();
		String legacyText = ChatColor.translateAlternateColorCodes('&', text);
		Pattern gradientRegex = Pattern.compile("<gradient\\((((2[0-5][0-5])|([0-1]?[0-9]?[0-9]));){2}((2[0-5][0-5])|([0-1]?[0-9]?[0-9]))/(((2[0-5][0-5])|([0-1]?[0-9]?[0-9]));){2}((2[0-5][0-5])|([0-1]?[0-9]?[0-9]))\\)(.*?)gradient>");
		Pattern rgbRegex = Pattern.compile("<rgb\\((((2[0-5][0-5])|([0-1]?[0-9]?[0-9]));){2}((2[0-5][0-5])|([0-1]?[0-9]?[0-9]))\\)>");
		Matcher rgbMatcher = rgbRegex.matcher(legacyText);
		Matcher gradientmatcher = gradientRegex.matcher(legacyText);
		
		while (gradientmatcher.find()) {

			int textStart = gradientmatcher.group().indexOf(")");
			String rgb = gradientmatcher.group().substring(10, textStart);
			String[] colors = rgb.replace("/", ";").split(";");
			legacyText = legacyText.replace(gradientmatcher.group(), setGradient(gradientmatcher.group().substring(textStart + 1).replace("gradient>", ""), Stream.of(colors).mapToInt(Integer::parseInt).toArray()));
		}
		
		while (rgbMatcher.find()) {
			
			String rgb = rgbMatcher.group().replace("<rgb(", "").replace(")>", "");
			String[] colors = rgb.split(";");
			legacyText = legacyText.replace(rgbMatcher.group(), ChatColor.of(String.format("#%02x%02x%02x", Integer.valueOf(colors[0]), Integer.valueOf(colors[1]), Integer.valueOf(colors[2]))) + "");
		}

		if (player != null) {
			legacyText = PlaceholderAPI.setPlaceholders(player, legacyText);
		}
		
		for (BaseComponent component : TextComponent.fromLegacyText(legacyText)) {
			
			if (variants.containsKey(component.toPlainText())) {
				
				components.add(variants.get(component.toPlainText()));
				
			} else {
				
				components.add(component);
			}
		}
		
		return components.toArray(new BaseComponent[components.size()]);
	}

	public Player getPlaceHolderPlayer() {

		return player;
	}

	public String getText() {
		// return text without formatting
		return text;
	}

	public HashMap<String, TextComponent> getVariants() {

		return variants;
	}

	public Message replace(String oldString, String newString) {

		text = text.replace(oldString, newString);

		return this;
	}

	private String setGradient(String text, int[] colors) {
		
		String coloredText = "";
		
		double distanceRed = (double) (colors[0] - colors[3]) / text.length();
		double distanceGreen = (double) (colors[1] - colors[4]) / text.length();
		double distanceBlue = (double) (colors[2] - colors[5]) / text.length();
		int z = 0;
		
		for (int i = 0; i < text.length(); i++) {
			
			int newRed = (int) (colors[0] - distanceRed * z);
			int newGreen = (int) (colors[1] - distanceGreen * z);
			int newBlue = (int) (colors[2] - distanceBlue * z);
			
			coloredText = coloredText + ChatColor.of(String.format("#%02x%02x%02x", newRed, newGreen, newBlue)) + text.charAt(z);
			z++;
		}
		return coloredText;
	}

	public Message setPlaceHolderPlayer(Player player) {

		this.player = player;
		return this;
	}
	
	public void setText(String text) {

		this.text = text;
	}
	
	public void setVariants(HashMap<String, TextComponent> variants) {
		
		this.variants = variants;
	}
}