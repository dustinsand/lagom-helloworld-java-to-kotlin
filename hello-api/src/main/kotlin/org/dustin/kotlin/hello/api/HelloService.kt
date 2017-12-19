/**
 * Can't use this until Java interoperability with default methods is supported in Kotlin.
 */

//package org.dustin.kotlin.hello.api
//
//import akka.Done
//import akka.NotUsed
//import com.lightbend.lagom.javadsl.api.Descriptor
//import com.lightbend.lagom.javadsl.api.Service
//import com.lightbend.lagom.javadsl.api.Service.*
//import com.lightbend.lagom.javadsl.api.ServiceCall
//import com.lightbend.lagom.javadsl.api.broker.Topic
//
//
//interface HelloService : Service {
//    /**
//     * Example: curl http://localhost:9000/api/hello/Alice
//     */
//    fun hello(id: String): ServiceCall<NotUsed, String>
//
//
//    /**
//     * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
//     * "Hi"}' http://localhost:9000/api/hello/Alice
//     */
//    fun useGreeting(id: String): ServiceCall<GreetingMessage, Done>
//
//
//    /**
//     * This gets published to Kafka.
//     */
//    fun helloEvents(): Topic<HelloEvent>
//
//    override fun descriptor(): Descriptor {
//        return named("hello").withCalls(
//                pathCall<NotUsed, String, String>("/api/hello/:id", this::hello),
//                pathCall<GreetingMessage, Done, String>("/api/hello/:id", this::useGreeting)
//        ).withTopics(
//                topic<HelloEvent>("hello-events", this::helloEvents)
//        ).withAutoAcl(true)
//    }
//}