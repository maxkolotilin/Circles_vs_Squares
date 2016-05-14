package com.circlesvssquares.game.players;

import com.badlogic.gdx.math.Vector3;
import com.circlesvssquares.game.GameScreen;
import com.circlesvssquares.game.Level;
import com.circlesvssquares.game.game_objects.Party;

/**
 * Created by maximka on 1.5.16.
 */
public class RealPlayer extends Player {
    public RealPlayer(Party party, Level level) {
        super(party, level);
    }

    @Override
    public void processInput(Vector3 touchPos) {
        processInput(touchPos, GameScreen.LEFT_BOARD);
    }
}
