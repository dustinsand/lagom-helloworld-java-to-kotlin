package org.dustin.kotlin.hello.impl

import com.lightbend.lagom.javadsl.persistence.AggregateEventTag

object SHelloEvent {
    /**
     * Tags are used for getting and publishing streams of events. Each event
     * will have this tag, and in this case, we are partitioning the tags into
     * 4 shards, which means we can have 4 concurrent processors/publishers of
     * events.
     */
    val TAG = AggregateEventTag.sharded(KHelloEvent::class.java, 4)

}