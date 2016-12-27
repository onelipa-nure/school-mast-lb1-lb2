package com.onelipa;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public final class WampusEnvironmentObjectAndPosition {
    public IWampusEnvironmentObject object;
    public Vector2<Integer> position;

    public WampusEnvironmentObjectAndPosition(IWampusEnvironmentObject object, Vector2<Integer> position)
    {
        setObject(object);
        setPosition(position);
    }

    public IWampusEnvironmentObject getObject() {
        return object;
    }

    public void setObject(IWampusEnvironmentObject object) {
        this.object = object;
    }

    public Vector2<Integer> getPosition() {
        return position;
    }

    public void setPosition(Vector2<Integer> position) {
        this.position = position;
    }
}
