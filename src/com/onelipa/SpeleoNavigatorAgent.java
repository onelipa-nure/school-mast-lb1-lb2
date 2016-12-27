package com.onelipa;

import com.onelipa.EnvironmentObjects.Dig;
import com.onelipa.EnvironmentObjects.Glitter;
import com.onelipa.EnvironmentObjects.Wampus;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class SpeleoNavigatorAgent extends Agent {

    private class DirectionAndPriority {
        public Direction direction;
        public int priority;

        public DirectionAndPriority(Direction _direction, int _priority){
            direction = _direction;
            priority = _priority;
        }
    }

    public static final String NAME = "SpeleoNavigatorAgent";
    private static final MessageTemplate listeningMessageTemplate = MessageTemplate.and(
            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
            MessageTemplate.MatchLanguage("robo-english"));

    @Override
    protected void setup() {
        System.out.println(NAME + " started.");
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = blockingReceive(listeningMessageTemplate);
                ACLMessage reply = msg.createReply();

                String[] statetments = msg.getContent().split(", ");

                DirectionAndPriority[] directions = new DirectionAndPriority[4];

                directions[0] = new DirectionAndPriority(Direction.LEFT, handleDirection(Direction.LEFT, statetments));
                directions[1] = new DirectionAndPriority(Direction.TOP, handleDirection(Direction.TOP, statetments));
                directions[2] = new DirectionAndPriority(Direction.RIGHT, handleDirection(Direction.RIGHT, statetments));
                directions[3] = new DirectionAndPriority(Direction.BOTTOM, handleDirection(Direction.BOTTOM, statetments));

                int bestPriority = Integer.MIN_VALUE;
                ArrayList<Direction> bestDirections = new ArrayList<Direction>();

                for (DirectionAndPriority insturction : directions){
                    if (insturction.priority > bestPriority)
                    {
                        bestPriority = insturction.priority;
                        bestDirections.clear();
                        bestDirections.add(insturction.direction);
                    }
                    else if (insturction.priority == bestPriority)
                    {
                        bestDirections.add(insturction.direction);
                    }
                }

                Direction chosenDirection = bestDirections.get(Math.abs(new Random().nextInt()) % bestDirections.size());

                reply.setContent("go " + chosenDirection.toString());
                myAgent.send(reply);
                System.out.println(NAME + ": " + reply.getContent());
            }

            private int handleDirection(Direction direction, String[] statetments){
                ArrayList<String> filteredStatements = filterByDirection(direction, statetments);

                for (String statement : filteredStatements)
                {
                    if (statement.contains("Void"))
                        return -100;

                    if (statement.contains(new Wampus().getObjectName()))
                        return -10;

                    if (statement.contains(new Dig().getObjectName()))
                        return -5;

                    if (statement.contains(new Glitter().getObjectName()))
                        return 10;
                }

                return 0;
            }

            private ArrayList<String> filterByDirection(Direction direction, String[] statements){

                ArrayList<String> filteredStatements = new ArrayList<>();

                for (String statement : statements)
                {
                    if (statement.contains(direction.toString()))
                        filteredStatements.add(statement);
                }

                return filteredStatements;
            }
        });
    }
}
