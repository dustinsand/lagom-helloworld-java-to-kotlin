package org.dustin.kotlin.hello.impl

import com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup
import com.lightbend.lagom.javadsl.testkit.ServiceTest.withServer
import org.dustin.kotlin.hello.api.HelloService
import org.dustin.kotlin.hello.api.KGreetingMessage
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit.SECONDS


class KHelloServiceTest {
    @Test
    @Throws(Exception::class)
    fun shouldStorePersonalizedGreeting() {
        withServer(defaultSetup().withCassandra(true)) { server ->
            val service = server.client(HelloService::class.java)

            val msg1 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS)
            assertEquals("Hello, Alice!", msg1) // default greeting

            service.useGreeting("Alice").invoke(KGreetingMessage(message = "Hi")).toCompletableFuture().get(5, SECONDS)
            val msg2 = service.hello("Alice").invoke().toCompletableFuture().get(5, SECONDS)
            assertEquals("Hi, Alice!", msg2)

            val msg3 = service.hello("Bob").invoke().toCompletableFuture().get(5, SECONDS)
            assertEquals("Hello, Bob!", msg3) // default greeting
        }
    }

}