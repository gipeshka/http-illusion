package com.gipeshka.utils

import javax.inject.{Inject, Singleton}

import com.couchbase.client.java
import com.couchbase.client.java.{Bucket, CouchbaseCluster}
import com.google.inject.ImplementedBy
import pureconfig.loadConfig

@ImplementedBy(classOf[SimpleCouchbaseStorageCluster])
trait CouchbaseStorageCluster
{
  def storageName: String

  def storageBucket: Bucket
}

@Singleton
class SimpleCouchbaseStorageCluster @Inject()(
  config: CouchbaseConfig
) extends CouchbaseStorageCluster
{
  private val cluster: java.CouchbaseCluster = CouchbaseCluster.create(config.host:_*)

  val storageName = config.storageBucketName
  val storageBucket: Bucket = cluster.openBucket(storageName, config.storageBucketPassword)
}

case class CouchbaseConfig(
  host: List[String],
  storageBucketName: String,
  storageBucketPassword: String
)

object CouchbaseConfig
{
  def load: CouchbaseConfig = {
    loadConfig[CouchbaseConfig]("couchbase") match {
      case Right(config) => config
      case Left(error) =>
        throw new RuntimeException("Cannot read couchbase config file, errors:\n" + error.toList.mkString("\n"))
    }
  }
}
