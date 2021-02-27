package listops.ex1

import java.time.DayOfWeek

import scalaz._
import Scalaz._
import scalaz.Zipper._

object sc extends App {
  case class Travel(day: DayOfWeek)

  val travels = List(Travel(DayOfWeek.MONDAY), Travel(DayOfWeek.TUESDAY),Travel(DayOfWeek.WEDNESDAY),Travel(DayOfWeek.FRIDAY),
                         Travel(DayOfWeek.WEDNESDAY),Travel(DayOfWeek.THURSDAY))


  val x: Option[List[Travel]] = travels.takeWhileM(travelTime => Option(travelTime.day == DayOfWeek.MONDAY))
  val y = travels.toZipper


  val  z = travels.adjacentPairs

  val zz = travels.toZipper

 /* zz.map(zipper => {
    println(zipper.copoint.day)
  })*/

  val c =
  zz.foldRight(List(List.empty[Travel]))( (zipper, acc) => {

      val focus = zipper.focus
      val left = zipper.lefts.toList
    println(focus)
    println(left)
      if(left.headOption.getOrElse(Travel(DayOfWeek.MONDAY)).day.getValue > focus.day.getValue) {
        //zipper.next
        List(focus) :: acc
      }
      else {
        //zipper.next
        (focus :: acc.head) :: acc.tail
      }
    }
  )

  c.map(l => {
    println("--------")
    l.map(println(_))
  })

  /*
  Here's a quick example off the top of my head. Suppose that we've got a sequence of numbers and we want to do a simple form of smoothing with an exponential moving average, where the new value for each position in the list is an average of the current value and all the other values, but with more distant neighbors contributing less.
   */
  val weights = Stream.from(1).map(1.0 / math.pow(2, _))

  def sumNeighborWeights(neighbors: Stream[Double]) =
    neighbors.fzipWith(weights)(_ * _).sum

  def smooth(data: NonEmptyList[Double]) = data.toZipper

  val result = smooth(NonEmptyList[Double](0, 0, 0, 1, 0, 0, 0)).toList


}
