package com.onelipa;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public final class WampusEnvironment {

    public class NeighbourInfo
    {
        public Direction direction;
        public String name;
    }

    private final ArrayList<WampusEnvironmentObjectAndPosition> objects;
    private final Vector2<Integer> Size;

    public WampusEnvironment(Vector2<Integer> size)
    {
        objects = new ArrayList<>();
        Size = size;
    }

    public ArrayList<IWampusEnvironmentObject> getObjectsByPosition(Vector2<Integer> position){
        ArrayList<WampusEnvironmentObjectAndPosition> objectsWithPos = findObjectsByPosition(position);

        if (objectsWithPos == null)
            return null;

        ArrayList<IWampusEnvironmentObject> objects = new ArrayList<>(objectsWithPos.size());
        for (WampusEnvironmentObjectAndPosition objAndPos : objectsWithPos){
            objects.add(objAndPos.getObject());
        }
        return objects;
    }

    public void kill(IWampusEnvironmentObject object){
        WampusEnvironmentObjectAndPosition objectAndPos = findObject(object);

        if (objectAndPos == null)
            return;

        objects.remove(objectAndPos);
        objectAndPos.getObject().die();
    }

    public Vector2<Integer> getPosition(IWampusEnvironmentObject object){
        WampusEnvironmentObjectAndPosition objAndPos = findObject(object);

        if (objAndPos == null)
            return null;

        return new Vector2<>(objAndPos.getPosition().getX(), objAndPos.getPosition().getY());
    }

    public void setPosition(IWampusEnvironmentObject object, Vector2<Integer> position)
    {
        WampusEnvironmentObjectAndPosition objAndPos = findObject(object);

        if (objAndPos == null)
        {
            RegisterObject(object, position);
            return;
        }

        if (isValidPosition(position))
            objAndPos.setPosition(position);
        else
            objAndPos.setPosition(new Vector2<>(0, 0));
    }

    public ArrayList<NeighbourInfo> getNeighbours(IWampusEnvironmentObject referenceObject)
    {
        ArrayList<NeighbourInfo> result = new ArrayList<>();

        WampusEnvironmentObjectAndPosition objAndPos = findObject(referenceObject);

        if (objAndPos == null)
            return null;

        tryAddNeighbourInfo(Direction.LEFT, objAndPos.getPosition(), result);
        tryAddNeighbourInfo(Direction.TOP, objAndPos.getPosition(), result);
        tryAddNeighbourInfo(Direction.RIGHT, objAndPos.getPosition(), result);
        tryAddNeighbourInfo(Direction.BOTTOM, objAndPos.getPosition(), result);

        return result;
    }

    private void tryAddNeighbourInfo(Direction direction, Vector2<Integer> referencePosition, ArrayList<NeighbourInfo> dest){
        Vector2<Integer> position = new Vector2<>(
                referencePosition.getX() + direction.toVector().getX(),
                referencePosition.getY() + direction.toVector().getY());

        if (!isValidPosition(position))
        {
            NeighbourInfo voidInfo = new NeighbourInfo();
            voidInfo.direction = direction;
            voidInfo.name = "Void";
            dest.add(voidInfo);
            return;
        }

        WampusEnvironmentObjectAndPosition object = findObjectByPosition(position);

        if (object != null)
        {
            NeighbourInfo info = new NeighbourInfo();
            info.direction = direction;
            info.name = object.getObject().getObjectName();
            dest.add(info);
        }
    }

    private void RegisterObject(IWampusEnvironmentObject object, Vector2<Integer> position)
    {
        Vector2<Integer> validatedPosition;

        if (isValidPosition(position))
            validatedPosition = position;
        else
            validatedPosition = new Vector2<>(0, 0);

        WampusEnvironmentObjectAndPosition objAndPos = new WampusEnvironmentObjectAndPosition(object, validatedPosition);
        objects.add(objAndPos);
    }

    private WampusEnvironmentObjectAndPosition findObjectByPosition(Vector2<Integer> position)
    {
        if (!isValidPosition(position))
            return null;

        for (WampusEnvironmentObjectAndPosition objAndPos : objects)
        {
            if (objAndPos.getPosition().equals(position))
                return objAndPos;
        }

        return null;
    }

    private ArrayList<WampusEnvironmentObjectAndPosition> findObjectsByPosition(Vector2<Integer> position)
    {
        if (!isValidPosition(position))
            return null;

        ArrayList<WampusEnvironmentObjectAndPosition> result = new ArrayList<>();

        for (WampusEnvironmentObjectAndPosition objAndPos : objects)
        {
            if (objAndPos.getPosition().equals(position))
                result.add(objAndPos);
        }

        return result;
    }

    private WampusEnvironmentObjectAndPosition findObject(IWampusEnvironmentObject object)
    {
        for (WampusEnvironmentObjectAndPosition objAndPos : objects)
        {
            if (objAndPos.getObject() == object)
                return objAndPos;
        }

        return null;
    }

    private boolean isValidPosition(Vector2<Integer> pos)
    {
        if (pos.getX() >= Size.getX())
            return false;

        if (pos.getX() < 0)
            return false;

        if (pos.getY() >= Size.getY())
            return false;

        if (pos.getY() < 0)
            return false;

        return true;
    }
}
