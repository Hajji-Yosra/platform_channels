package tn.isetbizerte.tp3_channels

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class Torche(context: Context) : MethodChannel.MethodCallHandler {

    private val cameraManager = context.getSystemService(CameraManager::class.java)
    private var cameraIdAvecFlash: String? = null

    init {
        // Cherche la première caméra qui a un flash
        for (id in cameraManager.cameraIdList) {
            val caracs = cameraManager.getCameraCharacteristics(id)
            val aFlash = caracs.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            if (aFlash) {
                cameraIdAvecFlash = id
                break
            }
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "allumer" -> {
                val etat = call.argument<Boolean>("etat") ?: false
                val id = cameraIdAvecFlash
                if (id == null) {
                    result.error("PAS_DE_FLASH", "Cet appareil n'a pas de flash", null)
                    return
                }
                try {
                    cameraManager.setTorchMode(id, etat)
                    result.success(null)
                } catch (e: Exception) {
                    result.error("ERREUR_TORCH", e.message, null)
                }
            }
            else -> result.notImplemented()
        }
    }
}