package com.onelipa.EnvironmentObjects;

import com.onelipa.IWampusEnvironmentObject;
import com.onelipa.SpeleoAgent;
import com.onelipa.WampusEnvironment;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class Wampus implements IWampusEnvironmentObject {
    @Override
    public String getObjectName() {
        return "Wampus";
    }

    @Override
    public void onEnvironmentUpdated(WampusEnvironment env) {
        ArrayList<IWampusEnvironmentObject> objectsOnThisSlot = env.getObjectsByPosition(env.getPosition(this));

        for (IWampusEnvironmentObject object : objectsOnThisSlot)
        {
            if (Objects.equals(object.getObjectName(), new SpeleoAgent().getObjectName())){
                System.out.println("Wampus got " + object.getObjectName());
                env.kill(object);
            }
        }
    }

    @Override
    public void die() {

    }
}
