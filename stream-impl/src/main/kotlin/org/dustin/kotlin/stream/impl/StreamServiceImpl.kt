package org.dustin.kotlin.stream.impl

import akka.NotUsed
import akka.stream.javadsl.Source
import com.lightbend.lagom.javadsl.api.ServiceCall
import org.dustin.kotlin.hello.api.HelloService
import org.dustin.kotlin.stream.api.StreamService
import java.util.concurrent.CompletableFuture.completedFuture
import javax.inject.Inject


class StreamServiceImpl @Inject constructor(val helloService: HelloService, val repository: StreamRepository) : StreamService {
    override fun directStream(): ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> {
        return ServiceCall { hellos ->
            completedFuture<Source<String, NotUsed>>(
                    hellos.mapAsync(8, { name -> helloService.hello(name).invoke() }))
        }
    }

    override fun autonomousStream(): ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> {
        return ServiceCall { hellos ->
            completedFuture<Source<String, NotUsed>>(
                    hellos.mapAsync(8, { name -> repository.getMessage(name).thenApply { message -> String.format("%s, %s!", message.orElse("Hello"), name) } })
            )
        }
    }

}