import scalaz._
import Scalaz._

object T {
  case class TravelZones(from: String, to: String)
  type CardState = Map[TravelZones,  BigDecimal]

}

val map1 = Map(T.TravelZones("1","2") -> BigDecimal(3))
val map2 = Map(T.TravelZones("1","2") -> BigDecimal(3))

val default = map1 |+| map2
// Map(foo → 4, bar → 2): Map[String, Int]

val min = map1.mapValues(Tags.MinVal(_)) |+| map2.mapValues(Tags.MinVal(_))

