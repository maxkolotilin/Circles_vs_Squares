package com.circlesvssquares.game.interactions;

import com.circlesvssquares.game.game_objects.GameObject;
import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 18.4.16.
 */

public interface Interactable {
    void interact(UnitBase unit, GameObject other, float timeDelta);
}
