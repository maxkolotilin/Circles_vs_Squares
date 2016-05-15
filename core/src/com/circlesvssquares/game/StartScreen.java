package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by maximka on 12.5.16.
 */

public class StartScreen implements Screen {
    private static final int DELAY = 2500;
    private Circles_vs_Squares game;
    private boolean switcher = false;
    private BitmapFont font;
    private Texture pegi;
    private Texture logoLibGDX;
    private SpriteBatch batch;

    public StartScreen(Circles_vs_Squares game) {
        this.game = game;
        font = new BitmapFont(Gdx.files.internal("arial.fnt"));
        font.setColor(1, 1, 1, 1);
        batch = new SpriteBatch();
        logoLibGDX = new Texture("libGDX.png");
        pegi = new Texture("pegi_18.png");
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
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TextureKeeper.getInstance();
            SoundKeeper.getInstance();
            game.createScreens();
        } else {
            switcher = true;
            batch.begin();
            font.draw(batch, "MaximKa gamedev", 420, 450);
            batch.draw(logoLibGDX, 340, 200);
            batch.draw(pegi, 780, 200);
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
        pegi.dispose();
        logoLibGDX.dispose();
    }
}
