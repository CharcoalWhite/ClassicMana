package org.charcoalwhite.classicmana.mixin;

import net.minecraft.scoreboard.ScoreAccess;
import net.minecraft.scoreboard.ScoreHolder;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_INTACT;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_BROKEN;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_STAR_HOLLOW;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_REGEN_BASE;
import static org.charcoalwhite.classicmana.ClassicMana.MANA_SCALE;
import static org.charcoalwhite.classicmana.ClassicMana.MAX_MANABAR_LIFE;
import static org.charcoalwhite.classicmana.ClassicMana.scoreboard;
import static org.charcoalwhite.classicmana.ClassicMana.manaObjective;
import static org.charcoalwhite.classicmana.ClassicMana.manaRegenObjective;
import static org.charcoalwhite.classicmana.ClassicMana.maxManaObjective;
import static org.charcoalwhite.classicmana.ClassicMana.manabarLifeObjective;

import org.charcoalwhite.classicmana.api.RegenManaCallback;
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
		this.incrementManabarLife();
		int mana = this.getMana();
		int maxMana = this.getMaxMana();
		if (mana < maxMana && mana >= 0) {
			this.regenMana();
		} else if (mana != maxMana) {
			mana = maxMana;
			this.setMana(mana);
			this.updateMana();
		}
		
		int manabarLife = this.getManabarLife();
		if (manabarLife == MAX_MANABAR_LIFE) {
			this.updateMana();
		}
    };

	@Override
    public void regenMana() {
		this.resetManaRegen();
		RegenManaCallback.EVENT.invoker().interact((ServerPlayerEntity)(Object)this);
		int manaRegenFinal = this.getManaRegen() + MANA_REGEN_BASE;

		int mana = this.getMana();
		int maxMana = this.getMaxMana();
		mana += manaRegenFinal;
		if (mana > maxMana || mana < 0) {
			mana = maxMana;
		}

		this.setMana(mana);
		this.updateMana();
    }

	@Override
    public void updateMana() {
		this.resetManabarLife();
		
		int mana = this.getMana();
		int maxMana = this.getMaxMana();
		mana /= MANA_SCALE;
		maxMana /= MANA_SCALE;
		String manabar = "";
		for (int i = mana / 2; i > 0; -- i) {
			manabar += MANA_STAR_INTACT;
		}

		if (mana % 2 == 1) {
			manabar += MANA_STAR_BROKEN;
		}

		for (int i = (maxMana - mana) / 2; i > 0; -- i) {
			manabar += MANA_STAR_HOLLOW;
		}

		this.sendMessage(Text.literal(manabar).formatted(Formatting.AQUA), true);
    }

	@Override
	public int getMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaScore = scoreboard.getOrCreateScore(scoreHolder, manaObjective);
		return manaScore.getScore();
	}

	@Override
    public void setMana(int mana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaScore = scoreboard.getOrCreateScore(scoreHolder, manaObjective);
		manaScore.setScore(mana);
    }

	@Override
	public int incrementMana(int mana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaScore = scoreboard.getOrCreateScore(scoreHolder, manaObjective);
		return manaScore.incrementScore(mana);
	}

	@Override
	public int incrementMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaScore = scoreboard.getOrCreateScore(scoreHolder, manaObjective);
		return manaScore.incrementScore();
	}

	@Override
    public void resetMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaScore = scoreboard.getOrCreateScore(scoreHolder, manaObjective);
		manaScore.resetScore();
    }

	@Override
	public int getManaRegen() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaRegenScore = scoreboard.getOrCreateScore(scoreHolder, manaRegenObjective);
		return manaRegenScore.getScore();
	}

	@Override
    public void setManaRegen(int mana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaRegenScore = scoreboard.getOrCreateScore(scoreHolder, manaRegenObjective);
		manaRegenScore.setScore(mana);
    }

	@Override
	public int incrementManaRegen(int mana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaRegenScore = scoreboard.getOrCreateScore(scoreHolder, manaRegenObjective);
		return manaRegenScore.incrementScore(mana);
	}

	@Override
	public int incrementManaRegen() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaRegenScore = scoreboard.getOrCreateScore(scoreHolder, manaRegenObjective);
		return manaRegenScore.incrementScore();
	}

	@Override
    public void resetManaRegen() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manaRegenScore = scoreboard.getOrCreateScore(scoreHolder, manaRegenObjective);
		manaRegenScore.resetScore();
    }

	@Override
	public int getMaxMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess maxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxManaObjective);
		return maxManaScore.getScore();
	}
	
	@Override
    public void setMaxMana(int maxMana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess maxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxManaObjective);
		maxManaScore.setScore(maxMana);
    }

	@Override
	public int incrementMaxMana(int maxMana) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess maxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxManaObjective);
		return maxManaScore.incrementScore(maxMana);
	}

	@Override
	public int incrementMaxMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess maxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxManaObjective);
		return maxManaScore.incrementScore();
	}

	@Override
    public void resetMaxMana() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess maxManaScore = scoreboard.getOrCreateScore(scoreHolder, maxManaObjective);
		maxManaScore.resetScore();
    }

	@Override
	public int getManabarLife() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLifeObjective);
		return manabarLifeScore.getScore();
	}

	@Override
    public void setManabarLife(int manabarLife) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLifeObjective);
		manabarLifeScore.setScore(manabarLife);
    }
	
	@Override
	public int incrementManabarLife(int manabarLife) {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLifeObjective);
		return manabarLifeScore.incrementScore(manabarLife);
	}

	@Override
	public int incrementManabarLife() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLifeObjective);
		return manabarLifeScore.incrementScore();
	}

	@Override
    public void resetManabarLife() {
		ScoreHolder scoreHolder = this.getScoreHolder();
        ScoreAccess manabarLifeScore = scoreboard.getOrCreateScore(scoreHolder, manabarLifeObjective);
		manabarLifeScore.resetScore();
    }

	@Shadow
	public abstract void sendMessage(Text message, boolean overlay);

	@Shadow
	public abstract ScoreHolder getScoreHolder();
}