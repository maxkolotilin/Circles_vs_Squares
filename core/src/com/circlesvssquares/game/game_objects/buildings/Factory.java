package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.game_objects.Party;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by maximka on 24.4.16.
 */
public class Factory extends BuildingBase {
    private static final float DEFAULT_HP = 10f;
    private static final int TEXTURE_SIZE = 80;

    public Factory() {
        super(DEFAULT_HP);
        sprite = new Sprite(TextureKeeper.instance.getBuilding(2),
            TEXTURE_SIZE, TEXTURE_SIZE);

        levelLabel.width = 15f;
        levelLabel.height = 20f;

        partyLabel.width = 25f;
        partyLabel.height = 25f;
    }

    public float getStartX() {
        if (party == Party.CIRCLES) {
            return sprite.getX() + TEXTURE_SIZE;
        } else if (party == Party.SQUARES) {
            return sprite.getX();
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    public void interact(float timeDelta) {
        super.interact(timeDelta);
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);

        Vector2 drawOrigin = getCenter();
        healthBar.y = drawOrigin.y - 18f + 2 * (3 - buildingLevel);
        healthBar.height = 6 * buildingLevel;
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
}
