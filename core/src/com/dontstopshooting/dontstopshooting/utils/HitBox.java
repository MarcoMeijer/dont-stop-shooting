package com.dontstopshooting.dontstopshooting.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class HitBox {
    private static final ShapeRenderer renderer = new ShapeRenderer() {{
        setColor(Color.RED);
        begin(ShapeRenderer.ShapeType.Line);
    }};

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
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.rect((int)getRealLocation().x, (int)getRealLocation().y, (int)width, (int)height);
        batch.begin();
    }

    public Vector2 getRealLocation() {
        return loc.cpy().add(offset);
    }

    public Vector2 getCenter() {
        return getRealLocation().add(width/2f, height/2f);
    }

    public boolean intersect(HitBox other) {
        return ((      getRealLocation().x <= other.getRealLocation().x && other.getRealLocation().x <=       getRealLocation().x +       width) ||
                (other.getRealLocation().x <=       getRealLocation().x &&       getRealLocation().x <= other.getRealLocation().x + other.width)) &&
               ((      getRealLocation().y <= other.getRealLocation().y && other.getRealLocation().y <=       getRealLocation().y +       height) ||
                (other.getRealLocation().y <=       getRealLocation().y &&       getRealLocation().y <= other.getRealLocation().y + other.height));
    }

    public static boolean intersect(HitBox a, HitBox b) {
        return a.intersect(b);
    }
}
