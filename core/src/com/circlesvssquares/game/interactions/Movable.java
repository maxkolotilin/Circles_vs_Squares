package com.circlesvssquares.game.interactions;

import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 18.4.16.
 */
public interface Movable {
    void move(UnitBase unit, float timeDelta);
}
