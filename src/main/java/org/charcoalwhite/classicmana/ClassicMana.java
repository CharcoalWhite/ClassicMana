package org.charcoalwhite.classicmana;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassicMana implements ModInitializer {
	public static final String MOD_ID = "classicmana";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final String MANA = "classic_mana.mana";
	public static final String MANA_REGEN = "classic_mana.mana_regen";
	public static final String MAX_MANA = "classic_mana.max_mana";
	public static final String MANABAR_LIFE = "classic_mana.manabar_life";

	// It is 2^24.
	// The max mana a player can achieve is 127.
	public static final int MANA_SCALE = 16777216;

	// It is 2^20.
	// Regen 1 mana per 16 ticks.
	// "I don't like seeing my manabar does not regen more than 1 sec."
	public static final int MANA_REGEN_BASE = 1048576;
	public static final int MAX_MANABAR_LIFE = 40;
	public static final char MANA_STAR_INTACT = '\u2605';
	public static final char MANA_STAR_BROKEN = '\u2bea';
	public static final char MANA_STAR_HOLLOW = '\u2606';
	public static Scoreboard scoreboard;
	public static ScoreboardObjective manaObjective;
	public static ScoreboardObjective manaRegenObjective;
	public static ScoreboardObjective maxManaObjective;
	public static ScoreboardObjective manabarLifeObjective;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("[ClassicMana] Loaded!");

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			getObjective(server);
		});
	}

	public static void getObjective(MinecraftServer server) {
		scoreboard = server.getScoreboard();
		manaObjective = scoreboard.getNullableObjective(MANA);
		if (manaObjective == null) {
			scoreboard.addObjective(MANA, ScoreboardCriterion.DUMMY, Text.of("Mana"), ScoreboardCriterion.RenderType.INTEGER, true, null);
			manaObjective = scoreboard.getNullableObjective(MANA);
		}

		manaRegenObjective = scoreboard.getNullableObjective(MANA_REGEN);
		if (manaRegenObjective == null) {
			scoreboard.addObjective(MANA_REGEN, ScoreboardCriterion.DUMMY, Text.of("Mana Regen Addend"), ScoreboardCriterion.RenderType.INTEGER, true, null);
			manaRegenObjective = scoreboard.getNullableObjective(MANA_REGEN);
		}

		maxManaObjective = scoreboard.getNullableObjective(MAX_MANA);
		if (maxManaObjective == null) {
			scoreboard.addObjective(MAX_MANA, ScoreboardCriterion.DUMMY, Text.of("Max Mana"), ScoreboardCriterion.RenderType.INTEGER, true, null);
			maxManaObjective = scoreboard.getNullableObjective(MAX_MANA);
		}

		manabarLifeObjective = scoreboard.getNullableObjective(MANABAR_LIFE);
		if (manabarLifeObjective == null) {
			scoreboard.addObjective(MANABAR_LIFE, ScoreboardCriterion.DUMMY, Text.of("Manabar Life"), ScoreboardCriterion.RenderType.INTEGER, true, null);
			manabarLifeObjective = scoreboard.getNullableObjective(MANABAR_LIFE);
		}
	}
}
