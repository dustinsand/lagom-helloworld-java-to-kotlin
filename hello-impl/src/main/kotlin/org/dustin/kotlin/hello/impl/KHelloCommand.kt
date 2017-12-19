package org.dustin.kotlin.hello.impl

import akka.Done
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.lightbend.lagom.javadsl.persistence.PersistentEntity
import com.lightbend.lagom.serialization.CompressedJsonable
import com.lightbend.lagom.serialization.Jsonable
import javax.annotation.concurrent.Immutable


interface KHelloCommand : Jsonable {
    /**
     * A command to switch the greeting message.
     *
     *
     * It has a reply type of [akka.Done], which is sent back to the caller
     * when all the events emitted by this command are successfully persisted.
     */
    @Immutable
    @JsonDeserialize
    data class UseGreetingMessage(val message: String) : KHelloCommand, CompressedJsonable, PersistentEntity.ReplyType<Done>


    /**
     * A command to say hello to someone using the current greeting message.
     *
     *
     * The reply type is String, and will contain the message to say to that
     * person.
     */
    @Immutable
    @JsonDeserialize
    data class Hello(val name: String) : KHelloCommand, PersistentEntity.ReplyType<String>
}