package org.dustin.kotlin.hello.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.concurrent.Immutable

@Immutable
@JsonDeserialize
data class GreetingMessage(val message: String)