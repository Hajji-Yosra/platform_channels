package tn.isetbizerte.tp3_channels

import android.os.Build
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.EventChannel
import android.Manifest
import android.content.pm.PackageManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

class MainActivity : FlutterActivity() {
    private val scope = MainScope()                          // portée des coroutines de l'activité
    private var resultatPermission: MethodChannel.Result? = null   // réponse en attente


    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        val messager = flutterEngine.dartExecutor.binaryMessenger

        // Étape 1 : canal "appareil/infos"
        MethodChannel(messager, "appareil/infos").setMethodCallHandler { call, result ->
            when (call.method) {
                "lire" -> result.success(
                    mapOf(
                        "marque" to Build.MANUFACTURER,
                        "modele" to Build.MODEL,
                        "version" to Build.VERSION.RELEASE,
                        "sdk" to Build.VERSION.SDK_INT
                    )
                )
                else -> result.notImplemented()
            }
        }
        // Étape 2 : canal "appareil/vibrer"
        MethodChannel(messager, "appareil/vibrer").setMethodCallHandler(Vibreur(this))

        // Étape 3 : canal "capteur/lumiere" (flux continu)
        EventChannel(messager, "capteur/lumiere").setStreamHandler(CapteurLumiere(this))

        // Étape 4a : demander la permission du micro
        MethodChannel(messager, "audio/permission").setMethodCallHandler { call, result ->
            when (call.method) {
                "demander" -> {
                    if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                        result.success(true)                 // déjà accordée
                    } else {
                        resultatPermission = result          // « je vous rappelle »
                        requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQ_MICRO)
                    }
                }
                else -> result.notImplemented()
            }
        }

        // Étape 4b : flux du niveau sonore
        EventChannel(messager, "audio/niveau").setStreamHandler(NiveauSonore(scope))

        // Exercice 1 : capteur de proximité
        EventChannel(messager, "capteur/proximite").setStreamHandler(CapteurProximite(this))
    }

    // L'utilisateur a répondu à la boîte de dialogue
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_MICRO) {
            resultatPermission?.success(
                grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED
            )
            resultatPermission = null
        }
    }

    override fun onDestroy() {
        scope.cancel()          // annule les coroutines encore actives (concurrence structurée)
        super.onDestroy()
    }

    companion object {
        private const val REQ_MICRO = 42
    }

}


