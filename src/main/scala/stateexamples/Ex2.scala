package stateexamples
import scalaz._, Scalaz._

object Ex2 extends App {
  val m = for {
    _    <- modify { s: String => s * s.size }
    size <- gets { s: String => s.size }
  } yield size

  val r = m.run("hello")
  println(r)
//  ====>    ("hellohellohellohellohello", 25)

}
