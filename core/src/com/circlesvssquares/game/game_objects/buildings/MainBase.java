package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.circlesvssquares.game.GameScreen;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.game_objects.Party;
import com.circlesvssquares.game.players.ComputerPlayer;
import com.circlesvssquares.game.players.Player;
import com.circlesvssquares.game.players.RealPlayer;

/**
 * Created by maximka on 24.4.16.
 */
public class MainBase extends BuildingBase {
    private static final float DEFAULT_HP = 10;
    private static final float MINE_SPEED = 3f;
    private static final int WIDTH = 80;
    private static final int HEIGHT = 100;
    private Player player = null;
    private Party nativeParty;

    @Override
    public void write(Json json) {
        super.write(json);
        json.writeValue("np", nativeParty);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        super.read(json, jsonData);
        setNativeParty(Party.valueOf(jsonData.getString("np")));
    }

    public MainBase() {
        super(DEFAULT_HP);
        sprite = new Sprite(TextureKeeper.instance.getBuilding(1),
            WIDTH, HEIGHT);

        levelLabel.width = 15f;
        levelLabel.height = 20f;

        partyLabel.width = 18f;
        partyLabel.height = 18f;
    }

    @Override
    public void setParty(Party party) {
        super.setParty(party);
        if (nativeParty != party) {
            GameScreen.setWinner(party);
        }
    }

    public void setNativeParty(Party party) {
        nativeParty = party;
        this.party = party;
        if (party == Party.CIRCLES) {
            // player = RealPlayer.getInstance();
            player = GameScreen.getCirclesPlayer();
        } else if (party == Party.SQUARES) {
            // player = ComputerPlayer.getInstance();
            player = GameScreen.getSquaresPlayer();
        } else {
            throw new IllegalArgumentException("Wrong native party");
        }
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);

        Vector2 drawOrigin = getCenter();
        healthBar.y = drawOrigin.y - 35f + 2 * (3 - buildingLevel);
        healthBar.height = 3.5f * buildingLevel;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        Vector2 drawOrigin = getCenter();

        healthBar.x = drawOrigin.x - 30f;
        healthBar.y = drawOrigin.y - 35f + 2 * (3 - buildingLevel);

        levelLabel.x = drawOrigin.x;
        levelLabel.y = drawOrigin.y - 15f;

        partyLabel.x = drawOrigin.x - 12f;
        partyLabel.y = drawOrigin.y + 25f;
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        healthBar.width = 60f;
        if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
            healthBar.width *= getHealthRatio();
        }

        super.drawShapes(shapeRenderer);
    }

    @Override
    public void interact(float timeDelta) {
        super.interact(timeDelta);

        if (player != null && party == nativeParty) {
            player.addMoney(buildingLevel * MINE_SPEED * timeDelta);
        }
    }
}
