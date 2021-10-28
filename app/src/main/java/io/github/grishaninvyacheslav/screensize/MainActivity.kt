package io.github.grishaninvyacheslav.screensize

import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private fun horizontalPxTodp(px: Int) = px * 160 / getResources().getDisplayMetrics().xdpi
    private fun verticalPxTodp(px: Int) = px * 160 / getResources().getDisplayMetrics().ydpi

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getNavigationBarHeight(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(metrics)
            val usableHeight = metrics.heightPixels
            windowManager.defaultDisplay.getRealMetrics(metrics)
            val realHeight = metrics.heightPixels
            return if (realHeight > usableHeight) realHeight - usableHeight else 0
        }
        return 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val fillView = findViewById<View>(R.id.fill_view)
        val screenSize = resources.configuration.screenLayout and
                Configuration.SCREENLAYOUT_SIZE_MASK
        val toastMsg: String
        toastMsg = when (screenSize) {
            Configuration.SCREENLAYOUT_SIZE_LARGE -> "Large screen"
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> "Normal screen"
            Configuration.SCREENLAYOUT_SIZE_SMALL -> "Small screen"
            else -> "Screen size is neither large, normal or small"
        }
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        findViewById<TextView>(R.id.text).text =
            "windowManager: ${horizontalPxTodp(width)}dp x ${verticalPxTodp(height)}dp\nДоступно: ${horizontalPxTodp(fillView.width)}dp x ${verticalPxTodp(fillView.height)}dp\nВысота status bar: ${verticalPxTodp(getStatusBarHeight())}dp\nВысота navigation bar: ${verticalPxTodp(getNavigationBarHeight())}dp\nКатегория: $toastMsg"
    }
}