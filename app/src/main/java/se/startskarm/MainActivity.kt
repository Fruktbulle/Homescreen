package se.startskarm

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val match = ViewGroup.LayoutParams.MATCH_PARENT
    private val wrap = ViewGroup.LayoutParams.WRAP_CONTENT

    // Lägg till dina egna appar här: paketnamn till visningsnamn.
    // Appar som inte är installerade hoppas över automatiskt.
    private val apps = listOf(
        "com.netflix.ninja" to "Netflix",
        "com.google.android.youtube.tv" to "YouTube",
        "com.spotify.tv.android" to "Spotify"
    )

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var clock: TextView
    private lateinit var date: TextView
    private val tick = object : Runnable {
        override fun run() {
            val now = Date()
            clock.text = SimpleDateFormat("HH:mm", Locale("sv", "SE")).format(now)
            date.text = SimpleDateFormat("EEEE d MMMM", Locale("sv", "SE")).format(now)
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = FrameLayout(this)
        root.setBackgroundColor(Color.BLACK)

        // Bakgrundsbild (background.jpg i res/drawable-nodpi)
        root.addView(
            ImageView(this).apply {
                setImageResource(R.drawable.background)
                scaleType = ImageView.ScaleType.CENTER_CROP
            },
            FrameLayout.LayoutParams(match, match)
        )

        // Mörkning så att text och knappar blir läsbara
        root.addView(
            View(this).apply { setBackgroundColor(Color.parseColor("#77000000")) },
            FrameLayout.LayoutParams(match, match)
        )

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(96, 72, 96, 72)
        }

        // Klocka och datum uppe till vänster
        clock = TextView(this).apply {
            textSize = 80f
            setTextColor(Color.WHITE)
        }
        date = TextView(this).apply {
            textSize = 22f
            setTextColor(Color.parseColor("#D0D6DC"))
        }
        content.addView(clock)
        content.addView(date)

        // Tom yta som tar upp utrymmet mellan topp och nederdel
        content.addView(View(this), LinearLayout.LayoutParams(0, 0, 1f))

        // Appar i mitten nederst
        val appRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }
        for ((pkg, name) in apps) {
            val intent = packageManager.getLaunchIntentForPackage(pkg) ?: continue
            appRow.addView(
                button(name) { startActivity(intent) },
                LinearLayout.LayoutParams(wrap, wrap).apply { setMargins(16, 0, 16, 0) }
            )
        }
        content.addView(appRow)

        // Systemknappar högerjusterade längst ner
        val systemRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.END
            setPadding(0, 48, 0, 0)
        }
        systemRow.addView(
            button("Ingångar (HDMI)") { openInputs() },
            LinearLayout.LayoutParams(wrap, wrap).apply { setMargins(16, 0, 16, 0) }
        )
        systemRow.addView(
            button("Inställningar") { startActivity(Intent(Settings.ACTION_SETTINGS)) },
            LinearLayout.LayoutParams(wrap, wrap).apply { setMargins(16, 0, 0, 0) }
        )
        content.addView(systemRow)

        root.addView(content, FrameLayout.LayoutParams(match, match))
        setContentView(root)

        // Sätt fokus på första appknappen, eller på Inställningar om inga appar finns
        (appRow.getChildAt(0) ?: systemRow.getChildAt(1)).requestFocus()
    }

    private fun button(label: String, onClick: () -> Unit): Button =
        Button(this).apply {
            text = label
            textSize = 22f
            setOnClickListener { onClick() }
        }

    // Försöker öppna TV:ns ingångsväljare. Vilken åtgärd som fungerar beror på
    // Sony-modellen och Android-versionen, så vi testar fler och visar en
    // tydlig fallback om ingen av dem finns.
    private fun openInputs() {
        val candidates = listOf(
            Intent("com.android.tv.action.VIEW_INPUT_SETTINGS"),
            Intent("android.settings.HDMI_CEC_SETTINGS"),
            Intent(Settings.ACTION_SETTINGS)
        )
        for (intent in candidates) {
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
                return
            }
        }
        Toast.makeText(
            this,
            "Tryck på Ingång-knappen på fjärrkontrollen för att välja HDMI.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onResume() {
        super.onResume()
        handler.post(tick)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(tick)
    }
}
