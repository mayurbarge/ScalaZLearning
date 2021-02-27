package typeclasses

class NormalTypeClass {
  // Define a very simple JSON AST
  sealed trait Json
  final case class JsObject(get: Map[String, Json]) extends Json
  final case class JsString(get: String) extends Json
  final case class JsNumber(get: Double) extends Json

  //1) Type class: type class is represented by a trait with at least one type parameter.

  // The "serialize to JSON" behavior is encoded in this trait
  trait JsonWriter[A] {
    def write(value: A): Json
  }

  //2) Type Class Instance: The instances of a type class provide implementa􏰀ons for the types we care about, including types from the Scala standard library and types from our domain model.

  case class Person(name: String, email: String)
  object JsonWriterInstances {
    implicit val stringJsonWriter = new JsonWriter[String] {
      def write(value: String): Json =
        JsString(value)
    }
    implicit val personJsonWriter = new JsonWriter[Person] {
      def write(value: Person): Json =
        JsObject(Map("name" -> JsString(value.name),
          "email" -> JsString(value.email)
        ))
    }
  }

  //3) Interfaces:
   // 3.1] Interface Ob-jects
  object Json {
    def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
      w.write(value)
  }
  import JsonWriterInstances._
  Json.toJson(Person("Dave", "dave@example.com"))

  //3.2] Interface Syntax
  object JsonSyntax {
    implicit class JsonWriterOps[A](value: A) {
      def toJson(implicit w: JsonWriter[A]): Json =
        w.write(value)
    } }
  import JsonWriterInstances._
  import JsonSyntax._
  Person("Dave", "dave@example.com").toJson


}
