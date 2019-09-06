package com.akash.AkkaDemo;


import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class App {

    static class Counter extends AbstractLoggingActor {

        int counter = 0;
        static class Message { }

        @Override
        public Receive createReceive() {
            return ReceiveBuilder.create().match(Message.class, this::onMessage).build();
        }

        private void onMessage(Message message) {
            counter++;
            log().info("Counter increased " + counter);
        }

        public static Props props() {
            return Props.create(Counter.class);
        }
    }


    public static void main(String[] args) {
//        System.out.println("Hello World !");

        ActorSystem system = ActorSystem.create("default");
        ActorRef counter = system.actorOf(Counter.props(), "counter");

        counter.tell(new Counter.Message(), ActorRef.noSender());

        for (int i = 0; i < 5; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        counter.tell(new Counter.Message(), ActorRef.noSender());
                    }
                }
            }).start();
        }
    }
}
