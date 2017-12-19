package org.dustin.kotlin.stream.impl

import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport
import org.dustin.kotlin.hello.api.HelloService
import org.dustin.kotlin.stream.api.StreamService


class StreamModule : AbstractModule(), ServiceGuiceSupport {
    override fun configure() {
        // Bind the StreamService service
        bindService(StreamService::class.java, StreamServiceImpl::class.java)
        // Bind the HelloService client
        bindClient(HelloService::class.java)
        // Bind the subscriber eagerly to ensure it starts up
        bind(StreamSubscriber::class.java).asEagerSingleton()
    }
}
