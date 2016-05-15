package com.circlesvssquares.game.game_objects.units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.circlesvssquares.game.TextureKeeper;

/**
 * Created by maximka on 18.4.16.
 */

public class BigSquareUnit extends UnitSquare implements BigUnitParams {
    public BigSquareUnit() {
        super(HP, DAMAGE, CAPTURE_SPEED, VIEW_RADIUS);
        sprite = new Sprite(TextureKeeper.getInstance().getBigSquare(0),
            TEXTURE_SIZE, TEXTURE_SIZE);
    }

    @Override
    public void setTexture(int index) {
        sprite.setTexture(TextureKeeper.getInstance().getBigSquare(index));
    }
}
