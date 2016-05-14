package com.circlesvssquares.game.game_objects.units;

import com.circlesvssquares.game.game_objects.Party;

/**
 * Created by maximka on 1.5.16.
 */

public abstract class UnitSquare extends UnitBase {
    public UnitSquare(float defaultHP, float defaultDamage, float viewRadius) {
        super(defaultHP, defaultDamage, viewRadius);
        party = Party.SQUARES;
    }

    @Override
    public void translate(float deltaX, float deltaY) {
        super.translate(-deltaX, deltaY);
    }
}
