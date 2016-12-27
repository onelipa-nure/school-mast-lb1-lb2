package com.onelipa;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

/**
 * Created by Oleksandr on 22.12.2016.
 */
public class SpeleoAgentBehaviour extends CyclicBehaviour {

    private interface IState{
        void handle();
    }

    private class AskMessageState implements IState{

        @Override
        public void handle() {
            ArrayList<WampusEnvironment.NeighbourInfo> neighbourInfos = getMyAgent().getEnvironment().getNeighbours(getMyAgent());
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setLanguage("robo-english");
            msg.setContent(translateNeighbourInfosToMessage(neighbourInfos));
            msg.addReceiver(new AID(SpeleoNavigatorAgent.NAME, false));
            myAgent.send(msg);
            System.out.println(SpeleoAgent.NAME + ": " + msg.getContent());
            state = new WaitForHelpState();
        }

        private String translateNeighbourInfosToMessage(ArrayList<WampusEnvironment.NeighbourInfo> neighbourInfos){
            StringBuilder sb = new StringBuilder();

            for (WampusEnvironment.NeighbourInfo info : neighbourInfos)
            {
                sb.append(info.name);
                sb.append(" on the ");
                sb.append(info.direction.toString());
                sb.append(", ");
            }

            return sb.toString();
        }
    }

    private class WaitForHelpState implements IState{

        //private final MessageTemplate HelpMessageTemplate = MessageTemplate.and(MessageTemplate.MatchLanguage("robo-english"),
                //MessageTemplate.MatchInReplyTo(SpeleoAgent.NAME));
        private final MessageTemplate HelpMessageTemplate = MessageTemplate.MatchLanguage("robo-english");

        @Override
        public void handle() {
            System.out.println("WaitForHelpState");
            ACLMessage msg = myAgent.blockingReceive(HelpMessageTemplate);

            if (!msg.getContent().contains("go"))
            {
                //  something gone wrong
                state = new AskMessageState();
                return;
            }

            Direction instruction = extractDirection(msg.getContent());

            SpeleoAgent agent = getMyAgent();
            WampusEnvironment env = agent.getEnvironment();

            Vector2<Integer> currentPosition = env.getPosition(agent);
            Vector2<Integer> targetPosition = new Vector2<>(currentPosition.getX() + instruction.toVector().getX(),
                    currentPosition.getY() + instruction.toVector().getY());
            env.setPosition(agent, targetPosition);
            System.out.println(SpeleoAgent.NAME + ": moving " + instruction.toString());
            state = new AskMessageState();
        }

        private Direction extractDirection(String msg){
            if (msg.contains(Direction.LEFT.toString()))
                return Direction.LEFT;
            if (msg.contains(Direction.RIGHT.toString()))
                return Direction.RIGHT;
            if (msg.contains(Direction.TOP.toString()))
                return Direction.TOP;

            return Direction.BOTTOM;
        }
    }

    private IState state = new AskMessageState();

    @Override
    public void action() {
        state.handle();
    }

    private SpeleoAgent getMyAgent(){
        return (SpeleoAgent) myAgent;
    }
}
