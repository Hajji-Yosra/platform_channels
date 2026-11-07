import 'package:flutter/services.dart';

/// Point d'accès unique au code natif Kotlin.
class CanauxNatifs {
  static const _infos = MethodChannel('appareil/infos');
  static const _vibreur = MethodChannel('appareil/vibrer');
  static const _lumiere = EventChannel('capteur/lumiere');
  static const _permission = MethodChannel('audio/permission');
  static const _audio = EventChannel('audio/niveau');
  static const _proximite = EventChannel('capteur/proximite');

  static Stream<bool> proximite() =>
     _proximite.receiveBroadcastStream().map((v) => v as bool);

  static Future<Map<String, dynamic>> lireInfos() async {
    final infos = await _infos.invokeMapMethod<String, dynamic>('lire');
    return infos ?? {};
  }

  static Future<void> vibrer(String motif) =>
      _vibreur.invokeMethod('vibrer', {'motif': motif});

  static Stream<double> lumiere() =>
      _lumiere.receiveBroadcastStream().map((v) => (v as num).toDouble());

  static Future<bool> demanderMicro() async =>
      await _permission.invokeMethod<bool>('demander') ?? false;

  static Stream<double> niveauSonore() =>
      _audio.receiveBroadcastStream().map((v) => (v as num).toDouble());
}
