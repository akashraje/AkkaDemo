package com.akash.AkkaDemo.SupervisionDemo;

import akka.actor.AbstractActor;
import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class FaultyChildActor extends AbstractLoggingActor {

    public static class Command { }

    private long count = 0L;


    public static Props props() {
        return Props.create(FaultyChildActor.class);
    }

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
                .match(Command.class, this::onCommand)
                .build();
    }

    private void onCommand(Command command) {
        count++;
        if (count % 4 == 0) {
            throw new RuntimeException("Why 4 !!!!");
        } else  {
            log().info("Got Command. count : " + count);
        }
    }
}
