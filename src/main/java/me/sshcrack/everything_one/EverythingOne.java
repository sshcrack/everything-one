package me.sshcrack.everything_one;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EverythingOne implements ModInitializer {
	public static final String MOD_ID = "everything-one";
	public static int MAX_STUFF = 2;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			EntityAttributeInstance maxHealth = handler.player.getAttributes().getCustomInstance(EntityAttributes.MAX_HEALTH);
			assert maxHealth != null;

			maxHealth.setBaseValue(MAX_STUFF);
		});
	}
}