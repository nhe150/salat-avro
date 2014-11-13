package com.banno.salat.avro.test

import com.banno.salat.avro._
import global._
import org.apache.avro.Schema
import scala.collection.mutable.{ArrayBuffer, Queue}

object SetQueueArraySupportSpec extends SalatAvroSpec {
  import models._

  "a grater for a case class with an set field" should {
    "generate an avro schema" in {
      val schema = grater[HasASet].asAvroSchema
      val recordSchema = schema.getTypes().get(0)
      println(recordSchema)
      recordSchema must containField("s", Schema.Type.ARRAY)
      recordSchema.getField("s").schema.getElementType.getType must_== Schema.Type.INT
    }

    "serialize and deserialize Queue" in {
      val old = HasAQueue(Queue[Int](1,2,3))

      println(serializeToJSON(old))
      val newO = serializeAndDeserialize(old)
      newO must_== old
    }

    "serialize and deserialize ArrayBuffer" in {
      val old = HasAArray(ArrayBuffer(1,2,3))

      println(serializeToJSON(old))
      val newO = serializeAndDeserialize(old)
      newO must_== old
    }


    //due to deserialization stage type [Ljava.lang.Object instead [D issue
    "serialize and deserialize array" in {
      val old = HasArray(Array[Double](1.0, 2.3))

      println(serializeToJSON(old))
      val newO = serializeAndDeserialize(old)

      newO must_== old
    }
  }

}
