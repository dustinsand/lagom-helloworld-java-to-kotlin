package org.dustin.kotlin.hello.impl

import akka.Done
import akka.actor.ActorSystem
import akka.testkit.javadsl.TestKit
import com.lightbend.lagom.javadsl.testkit.PersistentEntityTestDriver
import org.junit.AfterClass
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test


class HelloEntityTest {
    companion object {
        var system: ActorSystem? = null

        @BeforeClass
        @JvmStatic
        fun setup() {
            system = ActorSystem.create("HelloEntityTest")
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            TestKit.shutdownActorSystem(system)
            system = null
        }
    }
    
    @Test
    fun testHelloWorld() {
        val driver = PersistentEntityTestDriver(system,
                HelloEntity(), "world-1")

        val outcome1 = driver.run(HelloCommand.Hello("Alice"))
        assertEquals("Hello, Alice!", outcome1.replies[0])
        assertEquals(emptyList<Any>(), outcome1.issues())

        val outcome2 = driver.run(HelloCommand.UseGreetingMessage("Hi"),
                HelloCommand.Hello("Bob"))
        assertEquals(1, outcome2.events().size.toLong())
        assertEquals(PHelloEvent.KGreetingMessageChanged("world-1", "Hi"), outcome2.events()[0])
        assertEquals("Hi", outcome2.state().message)
        assertEquals(Done.getInstance(), outcome2.replies[0])
        assertEquals("Hi, Bob!", outcome2.replies[1])
        assertEquals(2, outcome2.replies.size.toLong())
        assertEquals(emptyList<Any>(), outcome2.issues())
    }
}