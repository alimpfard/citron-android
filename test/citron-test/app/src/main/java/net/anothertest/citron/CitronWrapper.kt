package net.anothertest.citron

import net.anothertest.Either
import net.anothertest.NativeCitron

import java.util.Optional

class CitronWrapper(path: String) {
    private var nativeCitron: NativeCitron = NativeCitron()

    init {
        if (!nativeCitron.initialize(path))
            throw RuntimeException("Could not initialize citron")
        _evaluate("Reflect addGlobalVariable: 'eval_context'")
        _evaluate("eval_context is Map new")
        _evaluate("import Library/Utils/Highlight")
    }

    fun destroy() {
        nativeCitron.destroy()
    }

    private fun _evaluate(expression: String): Either<String, String> {
        val ret = nativeCitron.evaluate(expression)
        return if (nativeCitron.is_error)
            Either(ret, Either.EitherType.EitherRIGHT)
        else
            Either(ret, Either.EitherType.EitherLEFT)
    }

    fun evaluate(expression: String): Either<String, String> {
        val expr = "Reflect run: { ^\n$expression\n. } inContextAsMain: eval_context arguments: []"
        return _evaluate(expr)
    }
    fun evaluateOrErrorString(expression: String): String {
        return evaluate(expression)
                .mapRight { t -> "Error: $t" }
                .getUnified()
    }

    fun highlight(code: String): String {
        return _evaluate("Highlight highlight: ([${code.toCharArray().map { c: Char -> c.toByte() }.joinToString()}] foldl: {\\:acc:x acc appendByte: x.} accumulator: '') html: (False)").getUnified()
    }
}
