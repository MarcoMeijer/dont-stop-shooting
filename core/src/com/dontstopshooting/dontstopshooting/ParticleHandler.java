package com.dontstopshooting.dontstopshooting;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public class ParticleHandler {

    private final ParticleEffectPool explosionPool;
    private final List<ParticleEffectPool.PooledEffect> effects = new ArrayList<>();

    public ParticleHandler() {
        ParticleEffect explosionEffect = new ParticleEffect();
        explosionEffect.load(Gdx.files.internal("explosion"), GameScreen.atlas);
        explosionEffect.setEmittersCleanUpBlendFunction(false);
        explosionPool = new ParticleEffectPool(explosionEffect, 1, 200);
    }

    public void createExplosion(float x, float y) {
        ParticleEffectPool.PooledEffect effect = explosionPool.obtain();
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
    }
}
