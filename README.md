Un exemple test de serveur crud REST 

## Build
La version de maven utilisée est 3.8.1
Celle du JDK : OpenJDK 1.8.0_292

Pour les dépendances le fichier pom.xml est à la racine du projet.

NB : En abscence de maven, pas de soucis, la liste des bibliothèques utilisées se trouve tout simplement dans le répertoire de build jars/libs.

Le build se fait en retouchant le fichier ant (buildjars.xml) car les chemins des bibliothèques maven y sont en dur.

### Inititialisation du jeu de données

La base de données choisie est PostGreSQL. Le fichier src/main/resources/creationBaseSQL.sql recense les
opérations à effectuer sur la base. Sur une base existante, on peut modifier l'accès nom de database, login et password (oui, je sais, un password en clair dans un fichier texte) via le fichier de configuration db.conf (à la racine du projet si lancé du projet ou dans le répertoire des jars si lancé depuis les jars)

Si la base est bien accessible, le programme ImportCSv.jar charge les données dans la base.

```
$ java -jar ImportCSv.jar
```

### Déploiement

*Qu'est-ce que c'est ?  Ça se mange ?*

Le serveur ne se déploie pas mais se lance directement à partir du jar Bootstrap.jar :

```
$ java -jar Bootstrap.jar
```

### Utilisation

Les données d'entrée et sortie sont (implémentées un peu au dernier moment) en Json.

3 point d'entrée principaux : 

+ /aliment/
+ /groupe/
+ /sousgroupe/


