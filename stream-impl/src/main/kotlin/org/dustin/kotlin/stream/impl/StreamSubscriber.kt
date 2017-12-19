package org.dustin.kotlin.stream.impl

import akka.Done
import akka.stream.javadsl.Flow
import org.dustin.kotlin.hello.api.HelloEvent
import org.dustin.kotlin.hello.api.HelloService
import java.util.concurrent.CompletableFuture
import javax.inject.Inject


class StreamSubscriber @Inject constructor(val helloService: HelloService, val repository: StreamRepository) {
    init {
        // Create a subscriber
        helloService.helloEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each message it processes
                        Flow.create<HelloEvent>().mapAsync<Done>(1) { event ->

                            if (event is HelloEvent.GreetingMessageChanged) {
                                // Update the message
                                repository.updateMessage(event.name, event.message)

                            } else {
                                // Ignore all other events
                                CompletableFuture.completedFuture(Done.getInstance())
                            }
                        }
                )
    }
}