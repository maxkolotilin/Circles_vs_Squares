package com.circlesvssquares.game.game_objects.buildings;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.circlesvssquares.game.TextureKeeper;
import com.circlesvssquares.game.game_objects.GameObject;

/**
 * Created by maximka on 24.4.16.
 */

public class SupportTower extends Tower {
    private static final float DEFAULT_HP = 10;
    private static final float SUPPORT_RATIO = 2f;
    private static final float SUPPORT_RADIUS = 150f;

    private Circle supportArea = new Circle();

    public SupportTower() {
        super(DEFAULT_HP);
        sprite = new Sprite(TextureKeeper.getInstance().getBuilding(4),
            TEXTURE_SIZE, TEXTURE_SIZE);
        supportArea.radius = SUPPORT_RADIUS * buildingLevel;
    }

    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        supportArea.setPosition(getCenter());
    }

    @Override
    public void drawShapes(ShapeRenderer shapeRenderer) {
        super.drawShapes(shapeRenderer);

        if (shapeRenderer.getCurrentType() == ShapeRenderer.ShapeType.Line) {
            // draw support area
            shapeRenderer.setColor(partyColor[party.ordinal()]);
            shapeRenderer.circle(supportArea.x, supportArea.y, supportArea.radius, 1000);
        }
    }

    @Override
    public void setBuildingLevel(int buildingLevel) {
        super.setBuildingLevel(buildingLevel);
        supportArea.radius = SUPPORT_RADIUS * buildingLevel;
    }

    public boolean isInSupportArea(GameObject other) {
        Shape2D shape = other.getShape();

        if (shape instanceof Rectangle) {
            return Intersector.overlaps(supportArea, (Rectangle) shape);
        } else if (shape instanceof Circle) {
            return supportArea.overlaps((Circle) shape);
        } else {
            throw new NotImplementedException("Unknown shape");
        }
    }

    public float getSupportRatio() {
        return SUPPORT_RATIO;
    }
}
