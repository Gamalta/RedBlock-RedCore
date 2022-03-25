package fr.gamalta.lib.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementDisplay;
import net.minecraft.advancements.AdvancementFrameType;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionInstance;
import net.minecraft.advancements.critereon.LootSerializationContext;
import net.minecraft.network.chat.IChatBaseComponent.ChatSerializer;
import net.minecraft.network.protocol.game.PacketPlayOutAdvancements;
import net.minecraft.resources.MinecraftKey;

public class Advancements {

	private net.minecraft.world.item.ItemStack icon;
	private String title;
	
	public Advancements(String title, ItemStack icon) {
		this.title = title;
		this.icon = CraftItemStack.asNMSCopy(icon);
	}
	
	public void displayToast(Collection<? extends Player> collection) {

		MinecraftKey notName = new MinecraftKey("fr.redblock", "notification");

		AdvancementRewards advRewards = new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null);
		MinecraftKey backgroundTexture = new MinecraftKey("minecraft:textures/gui/advancements/backgrounds/adventure.png");

		Map<String, Criterion> advCriteria = new HashMap<>();
		String[][] advRequirements = new String[0][];
		advCriteria.put("for_free", new Criterion(new CriterionInstance(){
			@Override
			public MinecraftKey a() {
				return new MinecraftKey("minecraft", "impossible");
			}

			@Override
			public JsonObject a(LootSerializationContext arg0) {
				return null;
			}
		}));
		ArrayList<String[]> fixedRequirements = new ArrayList<>();
		fixedRequirements.add(new String[] { "for_free" });
		advRequirements = Arrays.<Object>stream(fixedRequirements.toArray()).toArray(paramInt -> new String[paramInt][]);
		AdvancementDisplay saveDisplay = new AdvancementDisplay(icon, ChatSerializer.a(ComponentSerializer.toString(new TextComponent(title))), ChatSerializer.a(ComponentSerializer.toString(new TextComponent("description"))), backgroundTexture, AdvancementFrameType.a, true, false, true);
		Advancement saveAdv = new Advancement(notName, null, saveDisplay, advRewards, advCriteria, advRequirements);
		HashMap<MinecraftKey, AdvancementProgress> prg = new HashMap<>();
		AdvancementProgress advPrg = new AdvancementProgress();
		advPrg.a(advCriteria, advRequirements);
		advPrg.getCriterionProgress("for_free").b();
		prg.put(notName, advPrg);
		PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false, Arrays.asList(saveAdv), new HashSet<>(), prg);
		
		for (Player player : collection) {
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		}

		HashSet<MinecraftKey> rm = new HashSet<>();
		rm.add(notName);
		prg.clear();
		packet = new PacketPlayOutAdvancements(false, new ArrayList<>(), rm, prg);
		
		for (Player player : collection) {
			((CraftPlayer) player).getHandle().b.sendPacket(packet);
		}
	}
}
