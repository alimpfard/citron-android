package net.anothertest

import android.arch.core.util.Function

class Either<L, R> constructor(private val inner: Any, private val type: EitherType) {

    val isLeft: Boolean
        get() = type == EitherType.EitherLEFT

    val left: L?
        get() = if (isLeft) inner as L else null

    val right: R?
        get() = if (isLeft) null else inner as R

    enum class EitherType {
        EitherLEFT,
        EitherRIGHT
    }

    fun <T> mapLeft(mapper: (L) -> T): Either<T, R> {
        return if (isLeft)
            fromLeft(mapper(inner as L))
        else
            this as Either<T, R>
    }

    fun <T> mapRight(mapper: (R) -> T): Either<L, T> {
        return if (!isLeft)
            fromRight(mapper(inner as R))
        else
            this as Either<L, T>
    }

    fun <T> getUnified(): T {
        return inner as T
    }

        fun <X, Y> fromLeft(left: X): Either<X, Y> {
            return Either(left as Any, EitherType.EitherLEFT)
        }

        fun <X, Y> fromRight(right: Y): Either<X, Y> {
            return Either(right as Any, EitherType.EitherRIGHT)
        }
}
