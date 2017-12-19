package org.dustin.kotlin.stream.impl

import akka.Done
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class StreamRepository @Inject constructor(private val uninitialisedSession: CassandraSession) {
    var initialisedSession: CompletableFuture<CassandraSession>? = null

    init {
        // Eagerly create the session
        session()
    }

    private fun session(): CompletionStage<CassandraSession>? {
        // If there's no initialised session, or if the initialised session future completed
        // with an exception, then reinitialise the session and attempt to create the tables
        if (initialisedSession == null || initialisedSession!!.isCompletedExceptionally) {
            initialisedSession = uninitialisedSession.executeCreateTable(
                    "CREATE TABLE IF NOT EXISTS greeting_message (name text PRIMARY KEY, message text)"
            ).thenApply { _ -> uninitialisedSession }.toCompletableFuture()
        }
        return initialisedSession
    }

    fun updateMessage(name: String, message: String): CompletionStage<Done> {
        return session()!!.thenCompose { session ->
            session.executeWrite("INSERT INTO greeting_message (name, message) VALUES (?, ?)",
                    name, message)
        }
    }

    fun getMessage(name: String): CompletionStage<Optional<String>> {
        return session()!!.thenCompose { session -> session.selectOne("SELECT message FROM greeting_message WHERE name = ?", name) }.thenApply { maybeRow -> maybeRow.map({ row -> row.getString("message") }) }
    }
}