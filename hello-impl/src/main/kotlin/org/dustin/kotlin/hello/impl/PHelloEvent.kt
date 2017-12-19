package org.dustin.kotlin.hello.impl

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.lightbend.lagom.javadsl.persistence.AggregateEvent
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger
import com.lightbend.lagom.serialization.Jsonable
import javax.annotation.concurrent.Immutable

interface PHelloEvent : Jsonable, AggregateEvent<PHelloEvent> {

    @Immutable
    @JsonDeserialize
    data class KGreetingMessageChanged(val name: String, val message: String) : PHelloEvent {
        override fun aggregateTag(): AggregateEventTagger<PHelloEvent> {
            return HelloEventObject.TAG
        }
    }
}

object HelloEventObject {
    /**
     * Tags are used for getting and publishing streams of events. Each event
     * will have this tag, and in this case, we are partitioning the tags into
     * 4 shards, which means we can have 4 concurrent processors/publishers of
     * events.
     */
    val TAG = AggregateEventTag.sharded(PHelloEvent::class.java, 4)

}