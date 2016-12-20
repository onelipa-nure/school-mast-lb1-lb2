package com.onelipa;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Hashtable;

/**
 * Created by Oleksandr on 20.12.2016.
 */
public class BookSellerAgent extends Agent {

    private Hashtable<String, Book> booksCatalogue;

    @Override
    protected void setup() {
        booksCatalogue = extractBookCatalogueFromArguments();

        addBehaviour(new OfferRequestServerBehaviour());
        addBehaviour(new PurchaseOrdersServerBehaviour());
    }

    private Hashtable<String, Book> extractBookCatalogueFromArguments()
    {
        Hashtable<String, Book> result = new Hashtable<>();

        Object[] args = getArguments();

        if (args == null || args.length == 0 || (args.length % 2) != 0)
            return result;

        for (int i = 0; i < args.length; i += 2)
        {
            String bookName = (String) args[i];

            if (bookName == null)
                continue;

            double bookPrice;

            try {
                bookPrice = Double.parseDouble((String) args[i + 1]);
            }
            catch (Exception e)
            {
                continue;
            }

            Book book = new Book();
            book.setName(bookName);
            book.setPrice(bookPrice);

            result.put(book.getName(), book);
        }

        return result;
    }

    public class OfferRequestServerBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(template);

            if (msg == null)
            {
                block();
                return;
            }

            String requestingBookName = msg.getContent();

            ACLMessage reply = msg.createReply();

            Book book = booksCatalogue.get(requestingBookName);

            if (book == null)
            {
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("not-available");
            }
            else
            {
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(String.valueOf(book.getPrice()));
            }

            myAgent.send(reply);
        }
    }

    public class PurchaseOrdersServerBehaviour extends CyclicBehaviour {
        @Override
        public void action() {

        }
    }
}
