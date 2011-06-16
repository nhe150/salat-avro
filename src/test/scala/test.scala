package com.banno.salat.avro

import test.models._

package object test {
  def ed() = Edward(a = "hello", b = 1, c = 1.1, aa = Some("there"), bb = Some(2), cc = Some(2.2))
  def clara() = Clara(l = Seq("hello", "there"), m = List(1,2,3), n = List(Desmond(1)))
  def graph() = Alice("x", Some("y"),
                      Basil(Some(80), 81))
}
