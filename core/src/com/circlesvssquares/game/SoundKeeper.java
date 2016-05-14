package com.circlesvssquares.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.circlesvssquares.game.game_objects.units.SmallUnitParams;
import com.circlesvssquares.game.game_objects.units.UnitBase;

import java.sql.Time;
import java.util.Random;

/**
 * Created by maximka on 12.5.16.
 */
public class SoundKeeper implements Disposable {
    private static final long SHOOTING_DELTA = 800000000l;   // 0.8 sec
    private static final long UNIT_SOUNDS_DELTA = 1000000000l;   // 1 sec
    public static SoundKeeper instance = new SoundKeeper();

    private Array<Sound> smallCircleReset = new Array<Sound>();
    private Array<Sound> bigCircleReset = new Array<Sound>();
    private Array<Sound> smallCircleAttack = new Array<Sound>();
    private Array<Sound> bigCircleAttack = new Array<Sound>();
    private Array<Sound> smallCircleCapture = new Array<Sound>();
    private Array<Sound> smallCircleTakingDamage = new Array<Sound>();
    private Array<Sound> smallCircleDestroyed = new Array<Sound>();

    private Music moreGold;
    private Music chainsaw;
    private Sound smallShooting;
    private Sound bigShooting;
    private long lastShootingTime = TimeUtils.nanoTime();
    private long lastUnitSoundTime = TimeUtils.nanoTime();

    private Random generator = new Random();

    private SoundKeeper() {
        for (int i = 1; i <= 3; ++i) {
            if (i <= 2) {
                smallCircleReset.add(Gdx.audio.newSound(
                    Gdx.files.internal("sounds/space_marine_move" + i + ".mp3"))
                );
                smallCircleDestroyed.add(Gdx.audio.newSound(
                    Gdx.files.internal("sounds/space_marine_destroyed" + i + ".mp3"))
                );
            }
            smallCircleAttack.add(Gdx.audio.newSound(
                Gdx.files.internal("sounds/space_marine_attack" + i + ".mp3"))
            );
            smallCircleCapture.add(Gdx.audio.newSound(
                Gdx.files.internal("sounds/space_marine_captured" + i + ".mp3"))
            );
            smallCircleTakingDamage.add(Gdx.audio.newSound(
                Gdx.files.internal("sounds/space_marine_under-fire" + i + ".mp3"))
            );
            bigCircleReset.add(Gdx.audio.newSound(
                Gdx.files.internal("sounds/d_move" + i + ".mp3"))
            );
            bigCircleAttack.add(Gdx.audio.newSound(
                Gdx.files.internal("sounds/d_attack" + i + ".mp3"))
            );
        }

        smallShooting = Gdx.audio.newSound(
            Gdx.files.internal("sounds/small_shooting.wav"));
        bigShooting = Gdx.audio.newSound(
            Gdx.files.internal("sounds/big_shooting.wav"));
        moreGold = Gdx.audio.newMusic(
            Gdx.files.internal("sounds/need_more_gold.mp3")
        );
        chainsaw = Gdx.audio.newMusic(
            Gdx.files.internal("sounds/chainsaw.mp3")
        );
    }

    public void playShooting(UnitBase unit) {
        if (TimeUtils.timeSinceNanos(lastShootingTime) > SHOOTING_DELTA) {
            lastShootingTime = TimeUtils.nanoTime();
            if (unit instanceof SmallUnitParams) {
                smallShooting.play();
            } else {
                bigShooting.play();
            }
        }
    }

    public void playSmallCircleReset() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        smallCircleReset.get(generator.nextInt(smallCircleReset.size)).play();
    }

    public void playBigCircleReset() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        bigCircleReset.get(generator.nextInt(bigCircleReset.size)).play();
    }

    public void playBigCircleAttack() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        bigCircleAttack.get(generator.nextInt(bigCircleAttack.size)).play();
    }

    public void playSmallCircleAttack() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        smallCircleAttack.get(generator.nextInt(smallCircleAttack.size)).play();
    }

    public void playSmallCircleTakingDamage() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        smallCircleTakingDamage.get(generator.nextInt(smallCircleTakingDamage.size)).play();
    }

    public void playSmallCircleCapture() {
        smallCircleCapture.get(generator.nextInt(smallCircleCapture.size)).play();
    }

    public void playSmallCircleDestroyed() {
        if (TimeUtils.timeSinceNanos(lastUnitSoundTime) <= UNIT_SOUNDS_DELTA) {
            return;
        }
        lastUnitSoundTime = TimeUtils.nanoTime();

        smallCircleDestroyed.get(generator.nextInt(smallCircleDestroyed.size)).play();
    }

    public void playMoreGold() {
        if (!moreGold.isPlaying()) {
            moreGold.play();
        }
    }

    public void playChainsaw() {
        if (!chainsaw.isPlaying()) {
            chainsaw.play();
        }
    }

    @Override
    public void dispose() {
        for (Sound s: smallCircleAttack) {
            s.dispose();
        }
        for (Sound s: smallCircleReset) {
            s.dispose();
        }
        for (Sound s: smallCircleCapture) {
            s.dispose();
        }
        for (Sound s: smallCircleTakingDamage) {
            s.dispose();
        }
        for (Sound s: bigCircleAttack) {
            s.dispose();
        }
        for (Sound s: bigCircleReset) {
            s.dispose();
        }
        for (Sound s: smallCircleDestroyed) {
            s.dispose();
        }

        smallShooting.dispose();
        bigShooting.dispose();
        moreGold.dispose();
        chainsaw.dispose();
    }
}
