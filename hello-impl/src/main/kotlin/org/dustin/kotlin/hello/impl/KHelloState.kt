package org.dustin.kotlin.hello.impl

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.lightbend.lagom.serialization.CompressedJsonable
import javax.annotation.concurrent.Immutable


@Immutable
@JsonDeserialize
data class KHelloState(val message: String, val timestamp: String) : CompressedJsonable