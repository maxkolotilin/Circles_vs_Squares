package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.circlesvssquares.game.GameScreen;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.players.ComputerPlayer;
import com.circlesvssquares.game.players.Player;
import com.circlesvssquares.game.players.RealPlayer;

/**
 * Created by maximka on 24.4.16.
 */

public class Mine extends BuildingBase {
    private static final float DEFAULT_HP = 10f;
    private static final int TEXTURE_SIZE = 80;
    private static final float MINE_SPEED = 3f;     // per second
    private Player player = null;

    public Mine() {
        super(DEFAULT_HP);
        sprite = new Sprite(TextureKeeper.instance.getBuilding(0),
            TEXTURE_SIZE, TEXTURE_SIZE);

        levelLabel.width = 15f;
        levelLabel.height = 20f;

        partyLabel.width = 25f;
        partyLabel.height = 25f;
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);

        Vector2 drawOrigin = getCenter();
        healthBar.y = drawOrigin.y - 18f + 2 * (3 - buildingLevel);
        healthBar.height = 6 * buildingLevel;
    }

    @Override
    public void setParty(Party party) {
        super.setParty(party);
        if (party == Party.CIRCLES) {
            // player = RealPlayer.getInstance();
            player = GameScreen.getCirclesPlayer();
        } else if (party == Party.SQUARES) {
            // player = ComputerPlayer.getInstance();
            player = GameScreen.getSquaresPlayer();
        } else {
            player = null;
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        Vector2 drawOrigin = getCenter();

        healthBar.x = drawOrigin.x - 35f;
        healthBar.y = drawOrigin.y - 18f + 2 * (3 - buildingLevel);

        levelLabel.x = drawOrigin.x + 15f;
        levelLabel.y = drawOrigin.y + 8f;

        partyLabel.x = drawOrigin.x - 25f;
        partyLabel.y = drawOrigin.y + 8f;
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        healthBar.width = 70f;
        if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
            healthBar.width *= getHealthRatio();
        }

        super.drawShapes(shapeRenderer);
    }

    @Override
    public void interact(float timeDelta) {
        super.interact(timeDelta);

        if (player != null) {
            player.addMoney(buildingLevel * MINE_SPEED * timeDelta);
        }
    }
}
