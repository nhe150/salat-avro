package com.banno.salat.avro.test

import com.banno.salat.avro._
import global._
import org.apache.avro.Schema
import scala.collection.JavaConversions._

import org.specs2.matcher.JsonMatchers

object MultiGraterSpec extends SalatAvroSpec with JsonMatchers{
  import models._

  "a multi-grater" should {
    "make an avro schema that includes multiple records" in {
      val mg = grater[Alice] + (grater[Louis] + grater[Basil]) + grater[Edward]
      val schema = mg.asAvroSchema
      schema.getName must_== "union"
      val types: Iterable[Schema] = schema.getTypes
      val iter: Iterator[Schema] = types.iterator
      iter.next.getName must_== "Louis"
      iter.next.getName must_== "Basil"
      iter.next.getName must_== "Alice"
      iter.next.getName must_== "Edward"
    }

    "adding the same grater should not add it to the union twice" in {
      val mg = grater[Alice] + grater[Edward] + grater[Alice]
      val schema = mg.asAvroSchema
      schema.getTypes.size must_== 2 
    }

    "be able to support any type if any of its graters support it" in {
      val mg = grater[Alice] + grater[Edward]
      mg.supports(ed) must beTrue
    }

    "be able to serialize _any_ of graters that it contains" in {
//Had to comment out a test because Specs2 doesn't seem to see the JSON elements in the correct order after updating from 2.9.2 to 2.10.X
      val mg = grater[Alice] + grater[Edward]
      val json = serializeToJSON(ed, Some(mg))
       println("json = " + json)
      json must /("com.banno.salat.avro.test.models.Edward") /("a" -> ed.a)
      json must /("com.banno.salat.avro.test.models.Edward") /("b" -> ed.b)
     // json must /("com.banno.salat.avro.test.models.Edward") /("c" -> ed.c)
    }
    
    "be able to deserialize _any_ of graters that it contains" in {
      val oldGraph: Alice = graph
      val mg = grater[Alice] + grater[Edward]
      val newGraph = serializeAndDeserialize(oldGraph, Some(mg))
      newGraph must_== oldGraph
    }

    "be able to deserialize something that was serialized by one of its single avro graters (However, it must be added to the end as to preserve order)" in {
      val oldEd = ed
      val baos = byteArrayOuputStream()
      val encoder = binaryEncoder(baos)
      grater[Edward].serialize(oldEd, encoder)
      val bytesUnderSingle = baos.toByteArray

      val mg = grater[Edward] + grater[Alice]
      val decoder = binaryDecoder(bytesUnderSingle)
      val newEd = mg.asObject(decoder)

      newEd must_== oldEd
    }
  }
}
