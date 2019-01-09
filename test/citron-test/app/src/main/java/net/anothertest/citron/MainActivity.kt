package net.anothertest.citron

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Color
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.*
import net.anothertest.AbstractTerminalModel
import net.anothertest.VT100TerminalModel

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private var nativeCitron: CitronWrapper? = null
    private var citronPath: String = ""
    private val ROWS: Int = 1024

    private fun makeSpan(): SpannableString {
        val span = SpannableString(
                        "\n" +
                        "-".repeat(60) +
                        "\n")
        span.setSpan(ForegroundColorSpan(Color.MAGENTA),1, 61, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return span
    }

    private var metaProcessor: MetaProcessor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        citronPath = applicationContext.filesDir.toString() + "/Citron"

        val citronInstalled = getSharedPreferences("citron", Context.MODE_PRIVATE)
        if (!citronInstalled.getBoolean("installed", false)) {
            if (!copyAssetFolder(assets, "Citron", citronPath))
                return
            citronInstalled.edit().putBoolean("installed", true)
        }
        if (nativeCitron == null)
            nativeCitron = CitronWrapper(citronPath)
        val tv = findViewById<TextView>(R.id.console)
        tv.post {
            val bounds = Rect()
            tv.paint.getTextBounds("a", 0, 1, bounds)
            val charSize = listOf(bounds.width(), bounds.height())
            val consoleWidth: Int = tv.measuredWidth / charSize[0]
            val terminal = VT100TerminalModel(consoleWidth, ROWS)
            metaProcessor = MetaProcessor(terminal)

            val inputV = findViewById<EditText>(R.id.code_input)
            val progress = findViewById<ProgressBar>(R.id.progress_running)

            progress.isIndeterminate = true

            findViewById<Button>(R.id.button_eval).setOnClickListener {
                run {
                    val code = inputV.text.toString().trim()
                    if (code[0] == ':') {
                        // this is a metacommand
                        metaProcessor!!.process(code.drop(1))
                    } else {
                        val res = nativeCitron!!.evaluate(code)
                        terminal.line()

                        terminal.println(nativeCitron!!.highlight(code))
                        terminal.println(27.toChar() + "[0m")

                        if (res.isLeft) {
                            val span = SpannableString(res.left)
                            terminal.println(span.toString())
                        } else {
                            terminal.println("${27.toChar()}[32mError: ${27.toChar()}[0m${res.right}")
                        }
                    }
                    tv.text = terminal.asSpannable()
                }
            }
        }
    }

    override fun onDestroy() {
        nativeCitron!!.destroy()
        super.onDestroy()
    }



    private fun copyAssetFolder(assetManager: AssetManager,
                                fromAssetPath: String, toPath: String): Boolean {
        try {
            val files = assetManager.list(fromAssetPath)
            File(toPath).mkdirs()
            var res = true
            for (file in files!!)
                res = if (file.contains("."))
                    res and copyAsset(assetManager,
                            "$fromAssetPath/$file",
                            "$toPath/$file")
                else
                    res and copyAssetFolder(assetManager,
                            "$fromAssetPath/$file",
                            "$toPath/$file")
            return res
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    private fun copyAsset(assetManager: AssetManager,
                          fromAssetPath: String, toPath: String): Boolean {
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {
            `in` = assetManager.open(fromAssetPath)
            File(toPath).createNewFile()
            out = FileOutputStream(toPath)
            copyFile(`in`!!, out)
            `in`.close()
            `in` = null
            out.flush()
            out.close()
            out = null
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (true) {
            read = `in`.read(buffer)
            if (read == -1) break
            out.write(buffer, 0, read)
        }
    }
}

class MetaProcessor constructor(val terminalModel: AbstractTerminalModel) {

    fun process(code: String) {
        val csp = code.split(Regex("\\s+"))
        val cv = csp.component1()
        val args = csp.drop(1)

        if (cv == "clear")
            terminalModel.clear()
        else if (cv == "info") {
            terminalModel.clear()
            terminalModel.print("[Citron Compile-time Interpreter] <Shit goes here>")
        } else {
            terminalModel.print("Unknown metacommand ${csp}(${cv})")
            terminalModel.cursorColumn = 0
            terminalModel.moveCursorDown(1)
        }
    }
}
