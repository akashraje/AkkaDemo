package com.akash.AkkaDemo.SupervisionDemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App3 {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("default");

        ActorRef supervisor = system.actorOf(SupervisorActor.props(), "supervisor");

        for (int i = 0; i < 10; i++) {
            supervisor.tell(new FaultyChildActor.Command(), ActorRef.noSender());
        }


    }
}
