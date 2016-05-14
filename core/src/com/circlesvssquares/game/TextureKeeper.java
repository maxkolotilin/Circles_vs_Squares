package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.circlesvssquares.game.game_objects.units.BigUnitParams;
import com.circlesvssquares.game.game_objects.units.SmallUnitParams;
import com.circlesvssquares.game.game_objects.units.UnitBase;

/**
 * Created by maximka on 19.4.16.
 */

public class TextureKeeper implements Disposable {
    public static final TextureKeeper instance = new TextureKeeper();

    private Array<Texture> smallCircles = new Array<Texture>();
    private Array<Texture> bigCircles = new Array<Texture>();
    private Array<Texture> smallSquares = new Array<Texture>();
    private Array<Texture> bigSquares = new Array<Texture>();
    private Array<Texture> buildings = new Array<Texture>();

    private Texture smallBlood;
    private Texture bigBlood;
    private Texture deadParts;

    private TextureKeeper() {
        smallCircles.add(new Texture("circle_100.png"));
        smallCircles.add(new Texture(Gdx.files.internal("circle_75.png")));
        smallCircles.add(new Texture(Gdx.files.internal("circle_50.png")));
        smallCircles.add(new Texture(Gdx.files.internal("circle_25.png")));

        smallSquares.add(new Texture(Gdx.files.internal("square_100.png")));
        smallSquares.add(new Texture(Gdx.files.internal("square_75.png")));
        smallSquares.add(new Texture(Gdx.files.internal("square_50.png")));
        smallSquares.add(new Texture(Gdx.files.internal("square_25.png")));

        bigCircles.add(new Texture(Gdx.files.internal("circle_big_100.png")));
        bigCircles.add(new Texture(Gdx.files.internal("circle_big_75.png")));
        bigCircles.add(new Texture(Gdx.files.internal("circle_big_50.png")));
        bigCircles.add(new Texture(Gdx.files.internal("circle_big_25.png")));

        bigSquares.add(new Texture(Gdx.files.internal("square_big_100.png")));
        bigSquares.add(new Texture(Gdx.files.internal("square_big_75.png")));
        bigSquares.add(new Texture(Gdx.files.internal("square_big_50.png")));
        bigSquares.add(new Texture(Gdx.files.internal("square_big_25.png")));

        buildings.add(new Texture("mine.png"));
        buildings.add(new Texture("base.png"));
        buildings.add(new Texture("factory.png"));
        buildings.add(new Texture("def_tower.png"));
        buildings.add(new Texture("support_tower.png"));

        smallBlood = new Texture("blood_small.png");
        bigBlood = new Texture("blood_big.png");
        deadParts = new Texture("parts.png");
    }

    public Texture getDeadParts() {
        return deadParts;
    }

    public Texture getBloodTexture(UnitBase unit) {
        if (unit instanceof SmallUnitParams) {
            return smallBlood;
        }
        if (unit instanceof BigUnitParams) {
            return bigBlood;
        }

        return null;
    }

    public Texture getSmallCircle(int index) {
        return smallCircles.get(index);
    }

    public Texture getSmallSquare(int index) {
        return smallSquares.get(index);
    }

    public Texture getBigCircle(int index) {
        return bigCircles.get(index);
    }

    public Texture getBigSquare(int index) {
        return bigSquares.get(index);
    }

    public Texture getBuilding(int index) {
        return buildings.get(index);
    }

    @Override
    public void dispose() {
        for (Texture t: smallCircles) {
            t.dispose();
        }
        for (Texture t: smallSquares) {
            t.dispose();
        }
        for (Texture t: bigCircles) {
            t.dispose();
        }
        for (Texture t: bigSquares) {
            t.dispose();
        }
        for (Texture t: buildings) {
            t.dispose();
        }

        smallBlood.dispose();
        bigBlood.dispose();
        deadParts.dispose();
    }
}
