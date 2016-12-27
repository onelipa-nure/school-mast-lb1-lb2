package com.onelipa;

import com.onelipa.EnvironmentObjects.Dig;
import com.onelipa.EnvironmentObjects.Glitter;
import com.onelipa.EnvironmentObjects.Wampus;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

public class Main {

    public static final WampusEnvironment Environment = new WampusEnvironment(new Vector2<>(4, 4));

    public static void main(String[] args) {
        startWampusScenario(args);
    }

    private static void startWampusScenario(String[] args)
    {
        String host = "localhost";
        int port = -1;
        String platform = null;		//default name
        boolean main = true;

        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(host, port, platform, main);
        AgentContainer container = runtime.createMainContainer(profile);

        AgentController navigatorAgentController;
        AgentController speleoAgentController;

        try {
            speleoAgentController = container.createNewAgent(
                    SpeleoAgent.NAME,
                    SpeleoAgent.class.getName(),
                    args);
        } catch(StaleProxyException e) {
            throw new RuntimeException(e);
        }

        try {
            navigatorAgentController = container.createNewAgent(
                    SpeleoNavigatorAgent.NAME,
                    SpeleoNavigatorAgent.class.getName(),
                    args);
        } catch(StaleProxyException e) {
            throw new RuntimeException(e);
        }

        Environment.setPosition(new Glitter(), new Vector2<Integer>(3, 0));
        Environment.setPosition(new Wampus(), new Vector2<Integer>(2, 2));
        Environment.setPosition(new Dig(), new Vector2<Integer>(0, 2));
        Environment.setPosition(new Dig(), new Vector2<Integer>(1, 0));
        Environment.setPosition(new Dig(), new Vector2<Integer>(3, 2));

        try {
            speleoAgentController.start();
            navigatorAgentController.start();
        } catch(StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (ControllerException e) {
            e.printStackTrace();
        }
    }
}
