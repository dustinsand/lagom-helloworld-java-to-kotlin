package org.dustin.kotlin.stream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import org.dustin.kotlin.hello.api.KHelloEvent;
import org.dustin.kotlin.hello.api.HelloService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the HelloService event stream.
 */
public class StreamSubscriber {

  @Inject
  public StreamSubscriber(HelloService helloService, StreamRepository repository) {
    // Create a subscriber
    helloService.helloEvents().subscribe()
      // And subscribe to it with at least once processing semantics.
      .atLeastOnce(
        // Create a flow that emits a Done for each message it processes
        Flow.<KHelloEvent>create().mapAsync(1, event -> {

          if (event instanceof KHelloEvent.KGreetingMessageChanged) {
            KHelloEvent.KGreetingMessageChanged messageChanged = (KHelloEvent.KGreetingMessageChanged) event;
            // Update the message
            return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());

          } else {
            // Ignore all other events
            return CompletableFuture.completedFuture(Done.getInstance());
          }
        })
      );

  }
}
