package org.dustin.kotlin.hello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;
import static com.lightbend.lagom.javadsl.api.Service.topic;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;

/**
 * If implement Service in Kotling get this runtime error:
 *
 * Caused by: java.lang.IllegalArgumentException: Service.descriptor must be implemented as a default method
 *
 * The code where the exception is thrown first checks to see if the descriptor method has a Java 8 interface default method, and if not, checks if it has been created with Scala (Lagom is written in Scala and on top of the Akka framework). Unfortunately, the Java 8 interop coming next to Kotlin is not yet perfect and it doesn't handle default interface methods. There is no way to tell the compiler the descriptor method in the earlier code should be compiled as a Java 8 default method. So, for the time being, we have to rely on Java to define the service interface. Hence, if you uncomment the Java code for HelloService and remove the Kotlin implementation, you will be able to get the environment up and running again.
 */
public interface HelloService extends Service {

    /**
     * Example: curl http://localhost:9000/api/hello/Alice
     */
    ServiceCall<NotUsed, String> hello(String id);


    /**
     * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
     * "Hi"}' http://localhost:9000/api/hello/Alice
     */
    ServiceCall<GreetingMessage, Done> useGreeting(String id);


    /**
     * This gets published to Kafka.
     */
    Topic<HelloEvent> helloEvents();

    @Override
    default Descriptor descriptor() {
        // @formatter:off
    return named("hello").withCalls(
        pathCall("/api/hello/:id",  this::hello),
        pathCall("/api/hello/:id", this::useGreeting)
      ).withTopics(
          topic("hello-events", this::helloEvents)
          // Kafka partitions messages, messages within the same partition will
          // be delivered in order, to ensure that all messages for the same user
          // go to the same partition (and hence are delivered in order with respect
          // to that user), we configure a partition key strategy that extracts the
          // name as the partition key.
          .withProperty(KafkaProperties.partitionKeyStrategy(), HelloEvent::getEventName)
        ).withAutoAcl(true);
    // @formatter:on
    }
}