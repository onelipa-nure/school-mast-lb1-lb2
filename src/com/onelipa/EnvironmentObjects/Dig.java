package com.onelipa.EnvironmentObjects;

import com.onelipa.IWampusEnvironmentObject;
import com.onelipa.WampusEnvironment;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class Dig implements IWampusEnvironmentObject {
    @Override
    public String getObjectName() {
        return "Dig";
    }

    @Override
    public void onEnvironmentUpdated(WampusEnvironment env) {
        if (checkIfGotAgent())
            System.out.println("LOSE (fell to the dig");
    }

    private boolean checkIfGotAgent(){
        return false;
    }

    @Override
    public void die() {

    }
}
