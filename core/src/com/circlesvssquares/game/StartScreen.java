package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by maximka on 12.5.16.
 */
public class StartScreen implements Screen {
    private Circles_vs_Squares game;
    private boolean switcher = false;
    private BitmapFont font;
    private SpriteBatch batch;

    public StartScreen(Circles_vs_Squares game) {
        this.game = game;
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(1, 1, 1, 1);
        batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (switcher) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game.createScreens();
        } else {
            switcher = true;
            batch.begin();
            font.draw(batch, "MaximKa gamedev", 420, 400);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}
