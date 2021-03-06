package org.dustin.kotlin.hello.impl

import akka.Done
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import java.time.LocalDateTime
import java.util.*

/**
 * This is an event sourced entity. It has a state, {@link HelloState}, which
 * stores what the greeting should be (eg, "Hello").
 * <p>
 * Event sourced entities are interacted with by sending them commands. This
 * entity supports two commands, a {@link UserGreetingMessage} command, which is
 * used to change the greeting, and a {@link Hello} command, which is a read
 * only command which returns a greeting to the name specified by the command.
 * <p>
 * Commands get translated to events, and it's the events that get persisted by
 * the entity. Each event will have an event handler registered for it, and an
 * event handler simply applies an event to the current state. This will be done
 * when the event is first created, and it will also be done when the entity is
 * loaded from the database - each event will be replayed to recreate the state
 * of the entity.
 * <p>
 * This entity defines one event, the {@link GreetingMessageChanged} event,
 * which is emitted when a {@link UseGreetingMessage} command is received.
 */
class HelloEntity : PersistentEntity<HelloCommand, PHelloEvent, HelloState>() {
    /*
     * Behaviour is defined using a behaviour builder. The behaviour builder
     * starts with a state, if this entity supports snapshotting (an
     * optimisation that allows the state itself to be persisted to combine many
     * events into one), then the passed in snapshotState may have a value that
     * can be used.
     *
     * Otherwise, the default state is to use the Hello greeting.
     */
    override fun initialBehavior(snapshotState: Optional<HelloState>): Behavior {
        val b = newBehaviorBuilder(snapshotState.orElse(HelloState("Hello", LocalDateTime.now().toString())))

        /*
         * Command handler for the UseGreetingMessage command.
         */
        b.setCommandHandler(HelloCommand.UseGreetingMessage::class.java) { cmd, ctx ->
            ctx.thenPersist(PHelloEvent.KGreetingMessageChanged(name = entityId(), message = cmd.message)) { _ ->
                ctx.reply(Done.getInstance())
            }
        }

        /*
         * Event handler for the GreetingMessageChanged event.
         */
        b.setEventHandler(PHelloEvent.KGreetingMessageChanged::class.java) { evt ->
            HelloState(evt.message, LocalDateTime.now().toString())
        }

        /*
         * Command handler for the Hello command.
         */
        b.setReadOnlyCommandHandler(HelloCommand.Hello::class.java) { cmd, ctx ->
            ctx.reply("${state().message}, ${cmd.name}!")
        }

        /*
         * We've defined all our behaviour, so build and return it.
         */
        return b.build()
    }
}