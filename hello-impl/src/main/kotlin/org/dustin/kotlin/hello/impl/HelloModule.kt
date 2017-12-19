package org.dustin.kotlin.hello.impl

import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport
import org.dustin.kotlin.hello.api.HelloService


class HelloModule : AbstractModule(), ServiceGuiceSupport {
    override fun configure() {
        bindService(HelloService::class.java, HelloServiceImpl::class.java)
    }
}
