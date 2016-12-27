package com.onelipa;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class Vector2<T> {
    private T x;
    private T y;

    public Vector2(T x, T y)
    {
        setX(x);
        setY(y);
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }
}
