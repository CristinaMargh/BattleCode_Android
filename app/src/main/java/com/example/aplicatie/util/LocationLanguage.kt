package com.example.aplicatie.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import java.util.Locale

object LocationLanguage {

    const val PREF_LANG = "lang"              // "ro", "en", "de"
    const val PREFS = "app_prefs"
    const val PREF_LANG_OVERRIDE = "lang_override" // optional – demo/test
    fun getCurrentLang(ctx: Context): String {
        val p = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val override = p.getString(PREF_LANG_OVERRIDE, "") ?: ""
        if (override.isNotEmpty()) return override
        return p.getString(PREF_LANG, "en") ?: "en"
    }
    fun mapCountryToLang(countryCode: String?): String =
        when (countryCode?.uppercase()) {
            "RO" -> "ro"
            "MD" -> "ro"
            "DE" -> "de"
            "AT" -> "de"
            "CH" -> "de"
            else -> "en"
        }

    /** Apelează o singură dată la pornire (ex: în Welcome/Main). */
    suspend fun detectAndSaveLanguage(activity: Activity) {
        val prefs = activity.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        // Respectă override de test dacă există
        prefs.getString(PREF_LANG_OVERRIDE, null)?.let { forced ->
            prefs.edit().putString(PREF_LANG, forced).apply()
            return
        }

        // 1) încearcă prin locație (dacă avem permisiuni)
        val countryFromLoc = tryGetCountryFromLocation(activity)

        // 2) fallback pe SIM / Network
        val country = countryFromLoc
            ?: tryGetCountryFromSim(activity)
            ?: Locale.getDefault().country  // 3) fallback: locale telefon

        val lang = countryToLang(country)
        prefs.edit().putString(PREF_LANG, lang).apply()
    }

    /** Cere runtime permission pentru locație (opțional). */
    fun requestLocationPermission(activity: Activity, requestCode: Int = 42) {
        val needFine = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val needCoarse = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

        if (needFine || needCoarse) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                requestCode
            )
        }
    }

    private fun countryToLang(countryIso: String?): String {
        val c = (countryIso ?: "").uppercase(Locale.US)
        return when {
            c in listOf("RO", "MD") -> "ro"
            c in listOf("DE", "AT", "CH") -> "de"
            else -> "en"
        }
    }

    private suspend fun tryGetCountryFromLocation(activity: Activity): String? {
        val hasFine = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasFine && !hasCoarse) return null

        val fused = LocationServices.getFusedLocationProviderClient(activity)
        val loc = try { fused.lastLocation.await() } catch (_: Exception) { null } ?: return null

        // Folosim varianta sincronă (depreciată) – merge pe toate API-urile
        @Suppress("DEPRECATION")
        val list = try {
            Geocoder(activity, Locale.getDefault()).getFromLocation(loc.latitude, loc.longitude, 1)
        } catch (_: Exception) { null }

        return list?.firstOrNull()?.countryCode
    }

    private fun tryGetCountryFromSim(context: Context): String? = try {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        (tm.networkCountryIso ?: tm.simCountryIso)?.uppercase(Locale.US)
    } catch (_: Exception) { null }
}
