package org.dustin.kotlin.hello.impl

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.lightbend.lagom.javadsl.persistence.AggregateEvent
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger
import com.lightbend.lagom.serialization.Jsonable
import javax.annotation.concurrent.Immutable

interface KHelloEvent : Jsonable, AggregateEvent<KHelloEvent> {

    @Immutable
    @JsonDeserialize
    data class KGreetingMessageChanged(val name: String, val message: String) : KHelloEvent {
        override fun aggregateTag(): AggregateEventTagger<KHelloEvent> {
            return SHelloEvent.TAG
        }
    }
}