package com.dontstopshooting.dontstopshooting.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dontstopshooting.dontstopshooting.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticleHandler {

    private final Map<String, ParticleEffectPool> pools = new HashMap<>();
    private final List<ParticleEffectPool.PooledEffect> effects = new ArrayList<>();

    public ParticleHandler() {
        createPool("explosion");
        createPool("gun");
        createPool("bullet");
        createPool("wood");
    }

    public void createPool(String name) {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(name), GameScreen.atlas);
        effect.setEmittersCleanUpBlendFunction(false);
        ParticleEffectPool pool = new ParticleEffectPool(effect, 1, 100);
        pools.put(name, pool);
    }

    public void createExplosion(float x, float y) {
        ParticleEffectPool.PooledEffect effect = pools.get("explosion").obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void createGunExplosion(float x, float y) {
        ParticleEffectPool.PooledEffect effect = pools.get("gun").obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void createBullet(float x, float y) {
        ParticleEffectPool.PooledEffect effect = pools.get("bullet").obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void createWood(float x, float y) {
        ParticleEffectPool.PooledEffect effect = pools.get("wood").obtain();
        effect.setPosition(x, y);
        effects.add(effect);
    }

    public void draw(SpriteBatch batch, float delta) {
        for (int i = effects.size() - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(batch, delta);
            if (effect.isComplete()) {
                effect.free();
                effects.remove(i);
            }
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
