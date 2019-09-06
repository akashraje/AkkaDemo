package com.akash.AkkaDemo;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.dsl.Creators;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class App2 {

    static class Alarm extends AbstractLoggingActor {

        //Protocol
        static class Activity { }
        static class Disable {
            private final String password;

            Disable(String password) {
                this.password = password;
            }
        }

        static class Enable {
            private final String password;

            Enable(String password) {
                this.password = password;
            }
        }

        private final String password;
        private final Receive enabled;
        private final Receive disabled;

        Alarm(String password) {
            this.password = password;


            enabled = ReceiveBuilder.create()
                    .match(Activity.class, this::onActivity)
                    .match(Disable.class, this::onDisable)
                    .build();

            disabled = ReceiveBuilder.create()
                    .match(Enable.class, this::onEnable)
                    .build();
        }

        private void onEnable(Enable enable) {
            if (password.equals(enable.password)) {
                log().info("Alarm Enabled");
                getContext().become(enabled);
            } else {
                log().info("Incorrect password while Enabling alarm");
            }
        }

        private void onDisable(Disable disable) {
            if (password.equals(disable.password)) {
                log().info("Alarm Disabled");
                getContext().become(disabled);
            } else  {
                log().info("Incorrect password while disabling alarm");
            }
        }

        private void onActivity(Activity activity) {
            log().info("Alarm! Alarm! Alarm!");
        }

        public static Props props(String password) {
            return Props.create(Alarm.class, password);
        }

        @Override
        public Receive createReceive() {
            return disabled;
        }
    }

    public static void main(String[] args) {

        ActorSystem system = ActorSystem.create("default");

        String password = "pass";
        ActorRef alarm = system.actorOf(Alarm.props(password), "alarm");

        alarm.tell(new Alarm.Activity(), ActorRef.noSender());

        alarm.tell(new Alarm.Enable("sdfsdf"), ActorRef.noSender());
        alarm.tell(new Alarm.Enable(password), ActorRef.noSender());

        alarm.tell(new Alarm.Activity(), ActorRef.noSender());

        alarm.tell(new Alarm.Disable("asdsd"), ActorRef.noSender());
        alarm.tell(new Alarm.Disable(password), ActorRef.noSender());

        alarm.tell(new Alarm.Activity(), ActorRef.noSender());


    }
}
