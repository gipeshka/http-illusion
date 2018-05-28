package com.gipeshka.handler

import java.time.Clock
import javax.inject.{Inject, Named, Singleton}

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

import akka.Done
import akka.event.LoggingAdapter
import com.couchbase.client.java.document.JsonDocument
import com.couchbase.client.java.document.json.JsonObject
import com.couchbase.client.java.query.N1qlQuery
import com.gipeshka.request._
import com.gipeshka.utils.CouchbaseStorageCluster
import spray.json._

@Singleton
class CouchbaseReactionManagerDecorator @Inject()(
  @Named("Default") reactionManager: MockReactionManager,
  couchbaseCluster: CouchbaseStorageCluster,
  logger: LoggingAdapter
)(implicit
  clock: Clock,
  val executionContext: ExecutionContext
) extends MockReactionManager with AddReactionRequestFormat with DeleteReactionRequestFormat
{
  def add(addReactionRequest: AddReactionRequest): Future[Done] = {
    reactionManager.add(addReactionRequest).flatMap { _=>
      val key = getKey(addReactionRequest.request)

      couchbaseCluster.storageBucket.upsert(
        JsonDocument.create(
          key,
          JsonObject.create()
            .put("_id", s"mock_configuration_$key")
            .put("_type", "mock_configuration")
            .put("creation_date", clock.millis)
            // I HATE MY CARMA
            .put("request", JsonObject.fromJson(addReactionRequest.request.toJson.toString))
            .put("response", JsonObject.fromJson(addReactionRequest.response.toJson.toString))
        )
      )

      Future.successful(Done)
    }
  }

  def delete(deleteReactionRequest: DeleteReactionRequest): Future[Done] = {
    reactionManager.delete(deleteReactionRequest).flatMap { _=>
      val key = getKey(deleteReactionRequest.request)

      couchbaseCluster.storageBucket.remove(key)

      Future.successful(Done)
    }
  }

  def search(searchReactionRequest: SearchReactionRequest): Future[Map[ReactionRequestPart, Reaction]] = {
    reactionManager.search(searchReactionRequest)
  }

  def state: Future[Map[ReactionRequestPart, Reaction]] = {
    reactionManager.state
  }

  private def getKey(request: ReactionRequestPart): String = request.hashCode.toHexString

  private def setup(): Future[Done] = {
    val rows = couchbaseCluster.storageBucket.query(
      N1qlQuery.simple(
        s"""SELECT * FROM ${couchbaseCluster.storageName} WHERE _type = "mock_configuration""""
      )
    )

    Future.sequence(
      rows.asScala.toList.map { row =>
        // I HATE MY CARMA
        val addReactionRequest =
          row.value
            .getObject(couchbaseCluster.storageName)
            .toString
            .parseJson
            .convertTo[AddReactionRequest]

        reactionManager.add(addReactionRequest)
      }
    ).map { _ =>
      Done
    }.recover {
      case e: NullPointerException => logger.error(s"Couchbase mock configuration is borken. " +
        s"Please check mock_configurations in ${couchbaseCluster.storageName} bucket"
      )
        Done
    }
  }

  setup()
}
