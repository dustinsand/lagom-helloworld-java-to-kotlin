package org.dustin.kotlin.it

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.Materializer
import akka.stream.javadsl.Sink
import akka.stream.javadsl.Source
import com.lightbend.lagom.javadsl.client.integration.LagomClientFactory
import org.dustin.kotlin.hello.api.GreetingMessage
import org.dustin.kotlin.hello.api.HelloService
import org.dustin.kotlin.stream.api.StreamService
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URI
import java.util.Arrays
import java.util.concurrent.CompletionStage
import java.util.concurrent.TimeUnit


class StreamIT {
    companion object {
        private val SERVICE_LOCATOR_URI = "http://localhost:9008"

        private val clientFactory: LagomClientFactory = LagomClientFactory.create("integration-test", StreamIT::class.java.classLoader)
        // One of the clients can use the service locator, the other can use the service gateway, to test them both.
        private val helloService: HelloService = clientFactory.createDevClient(HelloService::class.java, URI.create(SERVICE_LOCATOR_URI))
        private val streamService: StreamService = clientFactory.createDevClient(StreamService::class.java, URI.create(SERVICE_LOCATOR_URI))
        private val system: ActorSystem = ActorSystem.create()
        private val mat: Materializer = ActorMaterializer.create(system)

        @AfterClass
        @JvmStatic
        fun tearDown() {
            clientFactory.close()
            system.terminate()
        }

    }

    @Test
    @Throws(Exception::class)
    fun helloWorld() {
        val answer = await(helloService.hello("foo").invoke())
        assertEquals("Hello, foo!", answer)
        await(helloService.useGreeting("bar").invoke(GreetingMessage("Hi")))
        val answer2 = await(helloService.hello("bar").invoke())
        assertEquals("Hi, bar!", answer2)
    }

    @Test
    @Throws(Exception::class)
    fun helloStream() {
        // Important to concat our source with a maybe, this ensures the connection doesn't get closed once we've
        // finished feeding our elements in, and then also to take 3 from the response stream, this ensures our
        // connection does get closed once we've received the 3 elements.
        val response = await<Source<String, NotUsed>>(streamService.directStream().invoke(
                Source.from(Arrays.asList("a", "b", "c")).concat(Source.maybe())))
        val messages = await(response.take(3).runWith(Sink.seq(), mat))
        assertEquals(Arrays.asList("Hello, a!", "Hello, b!", "Hello, c!"), messages)
    }

    @Throws(Exception::class)
    private fun <T> await(future: CompletionStage<T>): T {
        return future.toCompletableFuture().get(10, TimeUnit.SECONDS)
    }
}