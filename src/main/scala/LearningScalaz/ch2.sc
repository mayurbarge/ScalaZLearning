import scalaz._
import scalaz.syntax.Ops
import std.option._
import std.list._
import std.map._
import std.anyVal._
import std.string._
import std.tuple._
import syntax.equal._
import syntax.functor._

object FMap {
  trait Functor[F[_]] {
    def fmap[A, B](ma: F[A])(f: A => B): F[B]
  }
  object Functor {
    implicit val listFunctor: Functor[List] = new Functor[List] {
      override def fmap[A, B](ma: List[A])(f: A => B) = ma.map(f)
    }
  }
  trait FunctorOps[F[_], A] extends Ops[F[A]] {
    implicit def F: Functor[F]
    final def fmap[B](f: A => B): F[B] = F.fmap(self)(f)
  }
  implicit def ToFunctorOps[F[_],A](v: F[A])(implicit F0: Functor[F]) =
    new FunctorOps[F,A]{
      override implicit def F: Functor[F] = F0
      override def self = v
    }
  import FMap.Functor._
  List(1,2,3).fmap(_.toString + "_____")

}

//(1,2).map({_+1})
import std.tuple
import scalaz.syntax.functor._ // the syntax comes from here
import scalaz.syntax.functor._

Functor[({type λ[α] = (String, Int, α)})#λ]
//Function types are functors over their return type:
//Functor[({type λ[α] = String => α})#λ]
//Functor[({type λ[α] = (String, Int) => α})#λ]
//Functor[({type λ[α] = (String, Int, Boolean) => α})#λ]

//Tuple types are functors over their rightmost parameter:
Functor[({type λ[α] = (String, α)})#λ]
Functor[({type λ[α] = (String, Int, α)})#λ]
Functor[({type λ[α] = (String, Int, Boolean, α)})#λ]
Functor[List].lift{(_:Int)*2.0}
List(1,2,4) >| "x"
List(1,2,4).fpair(8)
List(1,2,4).strengthL("x")
List(1,2,4).strengthR("x")

import Scalaz._
1.point[List]
1.point[Option] map {_ + 2}
9.some <*> {(_:Int) + 3}.some