package dev.hotwire.core.turbo.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.TypedValue
import android.webkit.WebResourceRequest
import androidx.annotation.AttrRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dev.hotwire.core.R
import dev.hotwire.core.turbo.visit.TurboVisitAction
import dev.hotwire.core.turbo.visit.TurboVisitActionAdapter
import java.io.File

fun Toolbar.displayBackButton() {
    navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_back)
}

fun Toolbar.displayBackButtonAsCloseIcon() {
    navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_close)
}


internal fun Context.runOnUiThread(func: () -> Unit) {
    when (mainLooper.isCurrentThread) {
        true -> func()
        else -> Handler(mainLooper).post { func() }
    }
}

internal fun Context.contentFromAsset(filePath: String): String {
    return assets.open(filePath).use {
        String(it.readBytes())
    }
}

internal fun Context.colorFromThemeAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    val attr = obtainStyledAttributes(typedValue.data, intArrayOf(attrColor))
    val attrValue = attr.getColor(0, -1)
    attr.recycle()

    return attrValue
}

internal fun String.extract(patternRegex: String): String? {
    val regex = Regex(patternRegex, RegexOption.IGNORE_CASE)
    return regex.find(this)?.groups?.get(1)?.value
}

internal fun String.truncateMiddle(maxChars: Int): String {
    if (maxChars <= 1 || length <= maxChars) { return this }

    return "${take(maxChars / 2)} [...] ${takeLast(maxChars / 2)}"
}

internal fun String.withoutNewLineChars(): String {
    return this.replace("\n", "")
}

internal fun String.withoutRepeatingWhitespace(): String {
    return this.replace(Regex("\\s+"), " ")
}

internal fun File.deleteAllFilesInDirectory() {
    if (!isDirectory) return

    listFiles()?.forEach {
        it.delete()
    }
}

internal val NavBackStackEntry?.location: String?
    get() = this?.arguments?.getString("location")

internal fun WebResourceRequest.isHttpGetRequest(): Boolean {
    return method.equals("GET", ignoreCase = true) &&
        url.scheme?.startsWith("HTTP", ignoreCase = true) == true
}

internal fun Any.toJson(): String {
    return gson.toJson(this)
}

internal fun <T> String.toObject(typeToken: TypeToken<T>): T {
    return gson.fromJson(this, typeToken.type)
}

internal fun Int.animateColorTo(toColor: Int, duration: Long = 150, onUpdate: (Int) -> Unit) {
    ValueAnimator.ofObject(ArgbEvaluator(), this, toColor).apply {
        this.duration = duration
        this.addUpdateListener {
            val color = it.animatedValue as Int?
            color?.let { onUpdate(color) }
        }
    }.start()
}

private val gson: Gson = GsonBuilder()
    .registerTypeAdapter(TurboVisitAction::class.java, TurboVisitActionAdapter())
    .create()
