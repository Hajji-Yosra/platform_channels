# tp3_channels

Exercice 3:
  En envoyant qu-une valeur toutes le 200ms, L'app reste fluide sans consommer beacoup plus de batterie
Questions de Comprehension:

1) MethodChannel: poser une question recevoir une réponse
     EventChannel: envoyer des valeurs en continue tant que flutter écoute
     
2)hot Reload ne recharge que le code Dart
  
 3)Si on oublie Dart reste bloqué
    Si on appelle deux fois l'app se plante avec une erreur
4)Dans Dispatchers.IO, lire le micro est opération bloquante. Si on le fait qur le thread principal, l'écran se fige mais avec Dispatchers.Main, flutter doit recevoir les valeurs sur ce thread principal.
  
5) onCancel est appelé lorsque flutter arrête d'écouter ou ça continue et vide la batterie
     
6) L'utilisateur répond plus tard et on y répond quand l'app appelle on RequestPermissionResult
     
7) Le plugin contient aussi du code kotlin écrit par son développeur


<img width="720" height="1600" alt="1" src="https://github.com/user-attachments/assets/284e92b5-865b-497d-91bc-ab6dffae7406" />
<img width="720" height="1600" alt="2" src="https://github.com/user-attachments/assets/970dbe67-752d-463e-8457-fd04ee7c44ee" />

