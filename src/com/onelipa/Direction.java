package com.onelipa;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public enum Direction {
    TOP ("top")
            {
                @Override
                public Vector2<Integer> toVector() {
                    return new Vector2<>(0, 1);
                }
            },
    LEFT ("left")
            {
                @Override
                public Vector2<Integer> toVector() {
                    return new Vector2<>(-1, 0);
                }
            },
    RIGHT ("right")
            {
                @Override
                public Vector2<Integer> toVector() {
                    return new Vector2<>(1, 0);
                }
            },
    BOTTOM ("bottom")
            {
                @Override
                public Vector2<Integer> toVector() {
                    return new Vector2<>(0, -1);
                }
            };

    private final String name;

    Direction(String s) {
        name = s;
    }

    public String toString() {
        return this.name;
    }

    public abstract Vector2<Integer> toVector();
}
