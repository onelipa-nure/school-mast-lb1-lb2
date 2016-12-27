package com.onelipa;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.introspection.ACLMessage;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class SpeleoAgent extends Agent implements IWampusEnvironmentObject {

    public static final String NAME = "SpeleoAgent";
    private WampusEnvironment environment;

    public SpeleoAgent()
    {
        setEnvironment(Main.Environment);   //  dirty hack, sorry
        Main.Environment.setPosition(this, new Vector2<>(0, 3));
    }

    public void setEnvironment(WampusEnvironment _environment){
        environment = _environment;
    }

    @Override
    protected void setup() {
        System.out.println(NAME + " started.");
        addBehaviour(new SpeleoAgentBehaviour());
    }

    @Override
    public String getObjectName() {
        return NAME;
    }

    @Override
    public void onEnvironmentUpdated(WampusEnvironment env) {
        //  do not bother
    }

    @Override
    public void die() {
        System.out.println(NAME + " died.");
        doDelete();
    }

    public WampusEnvironment getEnvironment() {
        return environment;
    }
}
