package com.dontstopshooting.dontstopshooting.utils;

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

    public Vector2 getRealLocation() {
        return loc.cpy().add(offset);
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
