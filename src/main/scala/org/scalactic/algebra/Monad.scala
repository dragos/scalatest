/*
 * Copyright 2001-2014 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic.algebra

import scala.language.higherKinds
import scala.language.implicitConversions

/**
 * Typeclass trait for algebraic structure containing <em>insertion</em> and <em>flat-mapping</em> methods that obey
 * laws of <em>identity</em> and <em>associativity</em>.
 *
 * <p>
 * A <code>Monad/code> instance wraps an object that in some way behaves as a <code>Monad</code>.
 * </p>
 */
trait Monad[Context[_]] extends Applicative[Context] {
  /**
   * Applies the given function to the value contained in this context, returning the result 
   * of the function, which is a value wrapped in another context.
   */
  def flatMap[A, B](ca: Context[A])(f: A => Context[B]): Context[B]
}

/**
 * Companion object for <code>Monad</code> typeclass.
 */
object Monad {

  def apply[Context[_]](implicit ev: Applicative[Context]): Applicative[Context] = ev

  class Adapter[Context[_], A](val underlying: Context[A])(implicit val monad: Monad[Context]) {
    def map[B](f: A => B) = monad.map(underlying)(f)
    def flatMap[B](f: A => Context[B]) = monad.flatMap(underlying)(f)
  }

  implicit def adapters[Context[_], A](ca: Context[A])(implicit ev: Monad[Context]): Adapter[Context, A] =
    new Adapter(ca)(ev)
}