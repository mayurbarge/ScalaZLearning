import scalaz._, Scalaz._
1 === 1
//1 === "w" compilation fails
1 == "Foo"
1.some =/= 2.some
//1 assert_=== 2

1 > 2.0
1 lt 2
// 1 gt 2.0
1.0 ?|? 2.0
1.0 max 2.0

3.show
3.shows
"hello".println


'a' to 'e'
'a' |-> 'e'
3 |=> 5
3.succ

90 |-> 100
100 |-> 90
90.pred
95.succ

90 -+- 1
90 --- 1
1 |--> (10,100)
1 |==> (10,100)
4.from

implicitly[Enum[Char]].min
implicitly[Enum[Char]].max
implicitly[Enum[Int]].min
implicitly[Enum[Int]].min

object YesNoTruthy {
  trait CanTruthy[A] { self =>
    def truthys(a: A): Boolean
  }
  object CanTruthy {
    def apply[A](implicit ev: CanTruthy[A]) = ev
    def truthys[A](f: A => Boolean) = new CanTruthy[A] {
      override def truthys(a: A): Boolean = f(a)
    }

    implicit val intCanTruthly: CanTruthy[Int] =  CanTruthy.truthys({
      case 0 => false
      case _ => true
    })
    implicit def listCanTruthy[A]: CanTruthy[List[A]] = CanTruthy.truthys({
      case Nil => false
      case _ => true
    })
    implicit def nilCanTruthy: CanTruthy[scala.collection.immutable.Nil.type] =
      CanTruthy.truthys(_ => false)
    implicit def booleanCanTruthy: CanTruthy[Boolean] = CanTruthy.truthys(identity)
  }

  trait CanTruthyOps[A] {
    def self: A
    implicit def F: CanTruthy[A]
    def truthys: Boolean = F.truthys(self)
  }
  object ToCanIsTruthyOps {
    implicit def toCanIsTruthyOps[A](v: A)(implicit ev: CanTruthy[A]) = new CanTruthyOps[A] {
      def self = v
      implicit def F = ev
    }
  }
  def truthyIf[A: CanTruthy, B, C](cond: A)(ifyes: B)(ifno: C) = {
    val F = CanTruthy[A]
    if(F.truthys(cond)) ifyes else ifno
    // or
    // if(cond.truthy) ifyes else ifno
  }
}
import YesNoTruthy.ToCanIsTruthyOps._
import YesNoTruthy._
10.truthys
//ToCanIsTruthyOps.toCanIsTruthyOps(10).truthys
List("10").truthys
//nil.truthys
truthyIf(Nil)("Yeah!")("No!")
truthyIf(2 :: 3 :: 4 :: Nil)("Yeah!")("No!")
truthyIf(true)("Yeah!")("No!")
