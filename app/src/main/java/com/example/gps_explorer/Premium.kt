import android.content.Context

object Premium {
    fun isPremium(context: Context): Boolean =
        context.getSharedPreferences("settings", Context.MODE_PRIVATE)
            .getBoolean("is_premium", false)
}