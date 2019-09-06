package com.akash.AkkaDemo.SupervisionDemo;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import scala.concurrent.duration.Duration;

public class SupervisorActor extends AbstractLoggingActor {
    final ActorRef child;
    {
        child = getContext().actorOf(FaultyChildActor.props(), "child");
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .matchAny(any -> child.forward(any, getContext()))
                .build();
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(
                10,
                Duration.create("10 seconds"),
                DeciderBuilder.match(RuntimeException.class, ex -> SupervisorStrategy.escalate()).build()
        );
    }

    public static Props props() {
        return Props.create(SupervisorActor.class);
    }
}
