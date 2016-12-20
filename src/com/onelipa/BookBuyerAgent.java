package com.onelipa;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by Oleksandr on 20.12.2016.
 */
public class BookBuyerAgent extends Agent {

    private static final long RequestComedownMilliseconds = 60000;

    private static AID[] Sellers = {
        new AID("seller1", AID.ISLOCALNAME),
        new AID("seller2", AID.ISLOCALNAME),
        new AID("seller3", AID.ISLOCALNAME)
    };

    private String targetBookName;

    @Override
    protected void setup() {
        System.out.println("Starting BookBuyerAgent " + getAID().getName() + ".");

        targetBookName = extractTargetBookNameFromArguments();

        if (targetBookName == null)
        {
            doDelete();
            return;
        }

        addBehaviour(new TickerBehaviour(this, RequestComedownMilliseconds) {
            @Override
            protected void onTick() {
                myAgent.addBehaviour(new RequestPerformer());
            }
        });
    }

    private String extractTargetBookNameFromArguments() {

        Object[] args = getArguments();

        if (args == null || args.length == 0)
            return null;

        String bookTitle = (String) args[0];

        return bookTitle;
    }

    public class RequestPerformer extends Behaviour {

        private MessageTemplate receiveReplyTemplate;
        private AID bestSeller;
        private double bestPrice;
        private int responsesCount;
        private IActionState state;

        public RequestPerformer()
        {
            state = new SendCFPToSellers();
        }

        @Override
        public void action() {
            state.handle();
        }

        @Override
        public boolean done() {
            return state != null && state.isFinished();
        }

        public class SendCFPToSellers implements IActionState {

            @Override
            public void handle() {
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

                for (AID seller : Sellers)
                    cfp.addReceiver(seller);

                cfp.setContent(targetBookName);
                cfp.setConversationId("book-trade");
                cfp.setReplyWith("cfp"+System.currentTimeMillis());
                myAgent.send(cfp);

                receiveReplyTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));

                state = new ReceiveMsgFromSellers();
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        }

        public class ReceiveMsgFromSellers implements IActionState
        {

            @Override
            public void handle() {

                ACLMessage reply = myAgent.receive(receiveReplyTemplate);

                if (reply == null)
                    return;

                if (reply.getPerformative() != ACLMessage.PROPOSE)
                    return;

                double price = Double.parseDouble(reply.getContent());

                if (bestSeller == null || price < bestPrice){
                    bestPrice = price;
                    bestSeller = reply.getSender();
                }

                responsesCount++;

                if (responsesCount >= Sellers.length)
                    state = new SendPurchaseOrderToBestSeller();
                else
                    block();
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        }

        public class SendPurchaseOrderToBestSeller implements IActionState
        {

            @Override
            public void handle() {
                ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                order.addReceiver(bestSeller);
                order.setContent(targetBookName);
                order.setConversationId("book-trade");
                order.setReplyWith("order"+System.currentTimeMillis());
                myAgent.send(order);

                receiveReplyTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"),
                        MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                state = new RecievePurchaseOrderReply();
            }

            @Override
            public boolean isFinished() {
                return bestSeller == null;
            }
        }

        public class RecievePurchaseOrderReply implements IActionState{

            @Override
            public void handle() {
                ACLMessage reply = myAgent.receive(receiveReplyTemplate);

                if (reply == null)
                {
                    block();
                    return;
                }

                if (reply.getPerformative() == ACLMessage.INFORM){
                    System.out.println(targetBookName + "successfully purchased.");
                    System.out.println("Price = " + bestPrice);
                    myAgent.doDelete();
                }

                state = new DoneState();
            }

            @Override
            public boolean isFinished() {
                return false;
            }
        }

        public class DoneState implements IActionState
        {
            @Override
            public void handle() {

            }

            @Override
            public boolean isFinished() {
                return true;
            }
        }
    }

    public interface IActionState
    {
        void handle();
        boolean isFinished();
    }
}
