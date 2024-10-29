package org.charcoalwhite.classicmana.mixin;

import java.util.Objects;
import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_0;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_1;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_2;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_REGEN_BASE;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_SCALE;
import static org.charcoalwhite.classicmana.ClassicMana.MAX_MANABAR_LIFE;
import static org.charcoalwhite.classicmana.ClassicMana.scoreboard;
import static org.charcoalwhite.classicmana.ClassicMana.mana;
import static org.charcoalwhite.classicmana.ClassicMana.maxMana;
import static org.charcoalwhite.classicmana.ClassicMana.manabarLife;
import org.charcoalwhite.classicmana.api.ServerPlayerEntityApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityApi {
	@Inject(method = "tick()V", at = @At("RETURN"))
	private void tickMana(CallbackInfo info) {
		this.tickMana();
	}

	@Override
	public void tickMana() {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
		ScoreAccess playerManaScore = scoreboard.getOrCreateScore(scoreHolder, mana);
		ScoreAccess playerMaxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxMana);
		ScoreAccess playerManabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLife);
		playerManabarLifeScore.incrementScore();
		Integer playerMana = playerManaScore.getScore();
		Integer playerMaxMana = playerMaxManaScore.getScore();
		Integer playerManabarLife = playerManabarLifeScore.getScore();
		if (playerMana < playerMaxMana && playerMana >= 0) {
			this.regenMana();
		} else if (!(Objects.equals(playerMana, playerMaxMana))) {
			playerMana = playerMaxMana;
			this.setMana(playerMana);
			this.updateMana();
		}

		if (Objects.equals(playerManabarLife, MAX_MANABAR_LIFE)) {
			this.updateMana();
		}
    };

	@Override
    public void regenMana() {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
		ScoreAccess playerManaScore = scoreboard.getOrCreateScore(scoreHolder, mana);
		ScoreAccess playerMaxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxMana);
		Integer playerMana = playerManaScore.getScore();
		Integer playerMaxMana = playerMaxManaScore.getScore();
		playerMana += MANA_REGEN_BASE;
		if (playerMana > playerMaxMana || playerMana < 0) {
			playerMana = playerMaxMana;
		}

		this.setMana(playerMana);
		this.updateMana();
    }

	@Override
    public void updateMana() {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
		ScoreAccess playerManaScore = scoreboard.getOrCreateScore(scoreHolder, mana);
		ScoreAccess playerMaxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxMana);
		ScoreAccess playerManabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLife);
		Integer playerMana = playerManaScore.getScore();
		Integer playerMaxMana = playerMaxManaScore.getScore();
		String manabar = "";
		playerMana /= MANA_SCALE;
		playerMaxMana /= MANA_SCALE;
		for (Integer i = playerMana / 2; i > 0; -- i) {
			manabar += MANA_STAR_2;
		}

		if (playerMana % 2 == 1) {
			manabar += MANA_STAR_1;
		}

		for (Integer i = (playerMaxMana - playerMana) / 2; i > 0; -- i) {
			manabar += MANA_STAR_0;
		}

		this.sendMessage(Text.literal(manabar).formatted(Formatting.AQUA), true);
		playerManabarLifeScore.setScore(0);
    }

	@Override
    public void setMana(Integer playerMana) {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
        ScoreAccess playerManaScore = scoreboard.getOrCreateScore(scoreHolder, mana);
		playerManaScore.setScore(playerMana);
    }
	
	@Override
    public void setMaxMana(Integer playerMaxMana) {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
        ScoreAccess playerMaxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxMana);
		playerMaxManaScore.setScore(playerMaxMana);
    }

	@Override
    public void setManabarLife(Integer playerManabarLife) {
		ScoreHolder scoreHolder = ScoreHolder.fromProfile(((ServerPlayerEntity)(Object)this).getGameProfile());
        ScoreAccess playerManabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLife);
		playerManabarLifeScore.setScore(playerManabarLife);
    }

	@Shadow
	public abstract void sendMessage(Text message, boolean overlay);
}