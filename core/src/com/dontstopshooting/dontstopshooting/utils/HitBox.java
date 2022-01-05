package com.dontstopshooting.dontstopshooting.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class HitBox {
    public Vector2 loc, offset;
    public float width, height;

    public HitBox(Vector2 loc, Vector2 offset, float width, float height) {
        this.loc = loc;
        this.offset = offset;
        this.width = width;
        this.height = height;
    }

    public void render(SpriteBatch batch) {
        batch.end();
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setColor(Color.RED);
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect((int)getRealLocation().x, (int)getRealLocation().y, (int)width, (int)height);
        renderer.end();
        batch.begin();
    }

    public Vector2 getRealLocation() {
        return loc.cpy().add(offset);
    }

    public Vector2 getCenter() {
        return getRealLocation().add(width/2f, height/2f);
    }

    public boolean intersect(HitBox other) {
        return ((      getRealLocation().x <= other.getRealLocation().x && other.getRealLocation().x <=       getRealLocation().x + width) ||
                (other.getRealLocation().x <=       getRealLocation().x &&       getRealLocation().x <= other.getRealLocation().x + width)) &&
               ((      getRealLocation().y <= other.getRealLocation().y && other.getRealLocation().y <=       getRealLocation().y + height) ||
                (other.getRealLocation().y <=       getRealLocation().y &&       getRealLocation().y <= other.getRealLocation().y + height));
    }

    public static boolean intersect(HitBox a, HitBox b) {
        return a.intersect(b);
    }
}
