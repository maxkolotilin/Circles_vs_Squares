package com.circlesvssquares.game.game_objects.units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.circlesvssquares.game.SoundKeeper;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.interactions.Interactable;
import com.circlesvssquares.game.interactions.UnitAttack;

/**
 * Created by maximka on 18.4.16.
 */

public class BigCircleUnit extends UnitCircle implements BigUnitParams {

    public BigCircleUnit() {
        super(HP, DAMAGE, VIEW_RADIUS);
        sprite = new Sprite(TextureKeeper.instance.getBigCircle(0), TEXTURE_SIZE,
            TEXTURE_SIZE);
    }

    @Override
    public void resetUnit(float x, float y) {
        super.resetUnit(x, y);

        SoundKeeper.instance.playBigCircleReset();
    }

    @Override
    public void setInteraction(Interactable interaction) {
        if (interaction instanceof UnitAttack &&
                this.interaction == null) {
            SoundKeeper.instance.playBigCircleAttack();
        }

        super.setInteraction(interaction);
    }

    @Override
    public void setTexture(int index) {
        sprite.setTexture(TextureKeeper.instance.getBigCircle(index));
    }
}
