package org.dustin.kotlin.hello.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import lombok.Value


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(JsonSubTypes.Type(value = KHelloEvent.KGreetingMessageChanged::class, name = "greeting-message-changed"))
interface KHelloEvent {
    fun getEventName(): String

    @Value
    data class KGreetingMessageChanged(val name : String, val message : String) : KHelloEvent {
        override fun getEventName(): String {
            return this.name
        }
    }

}