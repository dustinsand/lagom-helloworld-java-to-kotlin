package org.dustin.kotlin.hello.impl

import akka.Done
import akka.NotUsed
import akka.japi.Pair
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.broker.Topic
import com.lightbend.lagom.javadsl.broker.TopicProducer
import com.lightbend.lagom.javadsl.persistence.Offset
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry
import org.dustin.kotlin.hello.api.GreetingMessage
import org.dustin.kotlin.hello.api.HelloEvent
import org.dustin.kotlin.hello.api.HelloService
import javax.inject.Inject


class KHelloServiceImpl @Inject constructor(private val persistentEntityRegistry: PersistentEntityRegistry) : HelloService {

    init {
        persistentEntityRegistry.register(KHelloEntity::class.java)
    }

    override fun hello(id: String): ServiceCall<NotUsed, String> {
        return ServiceCall {
            // Look up the hello world entity for the given ID.
            val ref = persistentEntityRegistry.refFor(KHelloEntity::class.java, id)
            // Ask the entity the Hello command.
            ref.ask<String, KHelloCommand.Hello>(KHelloCommand.Hello(id))
        }
    }

    override fun useGreeting(id: String): ServiceCall<GreetingMessage, Done> {
        return ServiceCall { request ->
            // Look up the hello world entity for the given ID.
            val ref = persistentEntityRegistry.refFor(KHelloEntity::class.java, id)
            // Tell the entity to use the greeting message specified.
            ref.ask<Done, KHelloCommand.UseGreetingMessage>(KHelloCommand.UseGreetingMessage(request.message))
        }

    }

    override fun helloEvents(): Topic<HelloEvent> {
        // We want to publish all the shards of the hello event
        return TopicProducer.taggedStreamWithOffset(SHelloEvent.TAG.allTags()
        ) { tag, offset ->

            // Load the event stream for the passed in shard tag
            persistentEntityRegistry.eventStream(tag, offset).map { eventAndOffset ->

                // Now we want to convert from the persisted event to the published event.
                // Although these two events are currently identical, in future they may
                // change and need to evolve separately, by separating them now we save
                // a lot of potential trouble in future.
                val eventToPublish: org.dustin.kotlin.hello.api.HelloEvent

                if (eventAndOffset.first() is KHelloEvent.KGreetingMessageChanged) {
                    val messageChanged = eventAndOffset.first() as KHelloEvent.KGreetingMessageChanged
                    eventToPublish = org.dustin.kotlin.hello.api.HelloEvent.GreetingMessageChanged(
                            messageChanged.name, messageChanged.message)
                } else {
                    throw IllegalArgumentException("Unknown event: " + eventAndOffset.first())
                }

                // We return a pair of the translated event, and its offset, so that
                // Lagom can track which offsets have been published.
                Pair.create<HelloEvent, Offset>(eventToPublish, eventAndOffset.second())
            }
        }
    }
}