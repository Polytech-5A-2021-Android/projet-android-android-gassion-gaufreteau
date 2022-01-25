# projet-android-android-gassion-gaufreteau


GASSION Axel - GAUFRETEAU Simon -- 5A INFO

## Concept

L'application créée a pour but d'intéragir avec un objet connecté.
Notre objet comporte 3 LED RGB, une matrice de LED RGB et un capteur de son.

### Fonctionnalités IOT

L'application propose la possibilité de changer la couleur des LED, en choisissant la couleur de chaque LED via un ColorPicker.
Il est possible d'appliquer les modifications ou de créer un préréglage qui sera stocké dans la base de données sur le serveur et pourra être réutilisé plus tard.

Lorsqu'un préréglage est crée, il n'est pas appliqué.

On peut aussi effectuer deux actions sur la matrice de LED.
On peut afficher un message textuel que l'on envoie depuis l'application.
On peut aussi activer le capteur de son depuis l'application : la matrice affichera une sorte de flux correspondant au son capté.

Enfin, nous avons implémenté une sorte de double authentification pour ajouter un objet connecté.
Lorsque l'utilisateur se connecte, il a accès à la liste des objets qui lui sont associés dans la base de données.
Soit il en séléctionne un et peut faire des actions dessus, soit il peut en synchroniser un nouveau.
Il va devoir entrer l'ID en base de données de l'objet. Si l'objet existe bien, la matrice LED va afficher un code de confirmation qui pourra être entré sur l'application.
Si tout se passe bien, le lien entre l'utilisateur et l'objet est effectué, et l'utilisateur pourra avoir accès aux commandes de cet objet.

NB : Le bouton pour afficher des effets n'est pas mappé, cela était un objectif dans le cas où nous aurions plus de temps à consacrer au projet.

### Fonctionnalités Application

On peut se connecter, créer un compte, mais aussi modifier le mot de passe de l'utilisateur dans les paramètres.


## Spécificités

Il est a noter que l'application a été créée dans le but de pouvoir intéragir avec l'API.
Cependant, nous avons essayé de faire en sorte qu'il soit possible de se déplacer dans l'application en utilisant la base de données de l'application (en dur) pour que l'application soit consultable sans API.

Ainsi, il existe un utilisateur lmabda : "Axel" - "Axel", qui permet de se connecter sans API.
Il existe deux faux objets connectés.

NB : L'utilisation de l'application sans API peut être un peu longue, car l'exécution des parties utilisant la DB en dur se fait après une dizaine de secondes d'attentes dans le cas où la requête API n'a rien renvoyé.


## Execution

Voici une vidéo youtube montrant l'utilisation de l'application avec API : https://youtu.be/t3mpLG7kAsE
