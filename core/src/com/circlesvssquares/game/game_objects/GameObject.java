package com.circlesvssquares.game.game_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by maximka on 18.4.16.
 */

public abstract class GameObject implements Json.Serializable {
    protected static final Color partyColor[] = {
        new Color(0 ,0, 0, 1),
        new Color(.5f, .5f, .5f, 1),
        new Color(1, 0, 0, 1),
        new Color(0, 0, 1, 1)
    };

    protected final float DEFAULT_HP;
    protected float healthPoints;
    protected Party party;
    protected Sprite sprite;
    private Vector2 center = new Vector2();

    public GameObject(float defaultHP) {
        DEFAULT_HP = defaultHP;
    }

    public boolean isAlive()
    {
        return healthPoints > 0;
    }

    public void receiveDamage(float value)
    {
        healthPoints -= value;
    }

    public Party getParty() {
        return party;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void translate(float deltaX, float deltaY) {
        sprite.translate(deltaX, deltaY);
    }

    public Vector2 getCenter() {
        return sprite.getBoundingRectangle().getCenter(center);
    }

    public void setPosition(float x, float y) {
        sprite.setPosition(x, y);
    }

    public Shape2D getShape() {
        return sprite.getBoundingRectangle();
    }

    public boolean overlaps(GameObject other) throws NotImplementedException {
        Shape2D shape_1 = getShape();
        Shape2D shape_2 = other.getShape();

        if (shape_1 instanceof Rectangle && shape_2 instanceof Rectangle) {
            return ((Rectangle) shape_1).overlaps((Rectangle) shape_2);
        } else if (shape_1 instanceof Circle && shape_2 instanceof Circle) {
            return ((Circle) shape_1).overlaps((Circle) shape_2);
        } else if (shape_1 instanceof Circle && shape_2 instanceof Rectangle) {
            return Intersector.overlaps((Circle) shape_1, (Rectangle) shape_2);
        } else if (shape_1 instanceof Rectangle && shape_2 instanceof Circle) {
            return Intersector.overlaps((Circle) shape_2, (Rectangle) shape_1);
        } else {
            throw new NotImplementedException();
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("hp", healthPoints);
        json.writeValue("party", party);
        json.writeValue("posx", sprite.getX());
        json.writeValue("posy", sprite.getY());
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        healthPoints = jsonData.getFloat("hp");
        party = Party.valueOf(jsonData.getString("party"));
        float x = jsonData.getFloat("posx");
        float y = jsonData.getFloat("posy");
        setPosition(x, y);
    }

    public abstract void drawShapes(ShapeRenderer shapeRenderer);
}
