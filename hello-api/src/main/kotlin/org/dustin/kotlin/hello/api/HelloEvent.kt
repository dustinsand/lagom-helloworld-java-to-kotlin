package org.dustin.kotlin.hello.api

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import lombok.Value


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(JsonSubTypes.Type(value = HelloEvent.GreetingMessageChanged::class, name = "greeting-message-changed"))
interface HelloEvent {
    fun getEventName(): String

    @Value
    data class GreetingMessageChanged(val name : String, val message : String) : HelloEvent {
        override fun getEventName(): String {
            return this.name
        }
    }

}