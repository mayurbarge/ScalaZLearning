trait Plus1[A] {
  def plus1(a:A):A
}
def plus1[A <: Plus1[A]](a1:A,a2:A) = a1.plus1(a2)
case class Cat(food:String) extends Plus1[Cat] { self =>
  override def plus1(a: Cat) = Cat(self.food + " And " + a.food)
}
plus1(Cat("Cheese"), Cat("Mice")).food

object MonoidalSum {
  trait Monoid[A] {
    def empty:A
    def mappend(a1:A,a2:A):A
  }
  trait FoldLeft[F[_]] {
    def foldLeft[A,B](xs:F[A], z: B, f:(B,A)=>B):B
  }
  object FoldLeft {
    implicit val ListFoldLeft: FoldLeft[List] = new FoldLeft[List] {
      def foldLeft[A,B](xs:List[A], z:B, f:(B,A)=>B):B = {
        xs.foldLeft(z)(f)
      }
    }
  }

  object Monoid {
    implicit val intMonoid = new Monoid[Int] {
      override def empty = 0
      override def mappend(a1: Int, a2: Int) = a1 + a2
    }
    implicit val stringMonoid = new Monoid[String] {
      override def empty = ""
      override def mappend(a1: String, a2: String) = a1 + a2
    }
  }

  trait MonoidOp[A] {
    val F: Monoid[A]
    val value: A
    def |+|(a1: A): A = F.mappend(value, a1)
  }

  implicit def toMonoidOp[A: Monoid](a: A) = new MonoidOp[A] {
    override val F: Monoid[A] = implicitly[Monoid[A]]
    override val value = F.empty
  }
}
import MonoidalSum.FoldLeft._
import MonoidalSum._

def sum1[A:Monoid](list: List[A]): A = {
  val monoid = implicitly[Monoid[A]]
  list.foldLeft(monoid.empty)(monoid.mappend)
}
sum1(List("Reno","Bruno"))
sum1(List(1,2,3,4,5))

def sum[M[_]: FoldLeft, A: Monoid](xs: M[A]):A = {
  val monoid: Monoid[A] = implicitly[Monoid[A]]
  val F = implicitly[FoldLeft[M]]
  F.foldLeft(xs,monoid.empty,monoid.mappend)
}
sum(List(1,3,3))

def plus[A: Monoid](a1: A, a2: A):A = implicitly[Monoid[A]].mappend(a1,a2)
plus(3,4)
11 |+| 11

/*
A type parameter 𝐴 of a method or non-trait class may also have one or more context bounds
𝐴 : 𝑇. In this case the type parameter may be instantiated to any type 𝑆 for which
evidence exists at the instantiation point that 𝑆 satisfies the bound 𝑇.
Such evidence consists of an implicit value with type 𝑇[𝑆].
def foo[A: M](implicit b: B): C
// expands to:
// def foo[A](implicit evidence$1: M[A], b: B): C
 */
def foo[A](implicit ma: Monoid[A]) // is same as
def foo[A: Monoid] = {
  val ma = implicitly[Monoid[A]]
}
