package org.dustin.kotlin.hello.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.concurrent.Immutable

@JsonDeserialize
data class KGreetingMessage(val message: String)