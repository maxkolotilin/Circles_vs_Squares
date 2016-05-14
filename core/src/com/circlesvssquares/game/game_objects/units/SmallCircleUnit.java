package com.circlesvssquares.game.game_objects.units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.circlesvssquares.game.SoundKeeper;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.interactions.Interactable;
import com.circlesvssquares.game.interactions.UnitAttack;
import com.circlesvssquares.game.interactions.UnitCapture;

/**
 * Created by maximka on 18.4.16.
 */

public class SmallCircleUnit extends UnitCircle implements SmallUnitParams {
    public SmallCircleUnit() {
        super(HP, DAMAGE, VIEW_RADIUS);
        sprite = new Sprite(TextureKeeper.instance.getSmallCircle(0), TEXTURE_SIZE,
            TEXTURE_SIZE);
    }

    @Override
    public void resetUnit(float x, float y) {
        super.resetUnit(x, y);

        SoundKeeper.instance.playSmallCircleReset();
    }

    @Override
    public void setInteraction(Interactable interaction) {
        if (interaction instanceof UnitAttack &&
            this.interaction == null) {
            if (interactionTarget instanceof UnitBase) {
                if (((UnitBase) interactionTarget).getInteractionTarget() == this) {
                    SoundKeeper.instance.playSmallCircleTakingDamage();
                }
            } else {
                SoundKeeper.instance.playSmallCircleAttack();
            }
        } else if (interactionTarget != null && interactionTarget.getParty() == party) {
            SoundKeeper.instance.playSmallCircleCapture();
        }

        super.setInteraction(interaction);
    }

    @Override
    public boolean isAlive() {
        boolean isAlive = super.isAlive();
        if (!isAlive) {
            SoundKeeper.instance.playSmallCircleDestroyed();
        }

        return isAlive;
    }

    @Override
    public void setTexture(int index) {
        sprite.setTexture(TextureKeeper.instance.getSmallCircle(index));
    }
}
