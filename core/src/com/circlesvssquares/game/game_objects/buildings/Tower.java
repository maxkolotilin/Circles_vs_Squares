package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by maximka on 24.4.16.
 */

public abstract class Tower extends BuildingBase {
    protected static final int TEXTURE_SIZE = 48;
    private Circle towerShape = new Circle();

    public Tower(float defaultHP) {
        super(defaultHP);

        levelLabel.width = 15f;
        levelLabel.height = 12f;

        partyLabel.width = 15f;
        partyLabel.height = 15f;
    }

    @Override
    public Shape2D getShape() {
        towerShape.radius = sprite.getWidth() / 2;
        towerShape.setPosition(getCenter());

        return towerShape;
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);

        Vector2 drawOrigin = getCenter();
        healthBar.height = 6 * buildingLevel;
        healthBar.y = drawOrigin.y - 28f - 6 * buildingLevel;
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        Vector2 drawOrigin = getCenter();

        healthBar.x = drawOrigin.x - 25f;
        healthBar.y = drawOrigin.y - 28f - 6 * buildingLevel;

        levelLabel.x = drawOrigin.x + 5f;
        levelLabel.y = drawOrigin.y + 2f;

        partyLabel.x = drawOrigin.x - 14f;
        partyLabel.y = drawOrigin.y - 14f;
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        healthBar.width = 50f;
        if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Filled) {
            healthBar.width *= getHealthRatio();
        }

        super.drawShapes(shapeRenderer);
    }
}
