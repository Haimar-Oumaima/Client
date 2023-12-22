<!-- TOC -->
* [Architecture logiciel](#architecture-logiciel)
    * [Stack techniques:](#stack-techniques)
    * [Pour modifier le port de l'exposition du serveur:](#pour-modifier-le-port-de-lexposition-du-serveur)
    * [Pour generer le binary (.jar):](#pour-generer-le-binary-jar)
    * [Pour lancer le projet:](#pour-lancer-le-projet)
    * [Pour modifier le port de votre container:](#pour-modifier-le-port-de-votre-container)
<!-- TOC -->

# Architecture logiciel

## Stack techniques:
- Spring boot
- Docker

## Pour modifier le port de l'exposition du serveur:
- Ouvrer le fichier `application.properties` qui se situe dans `src/main/resources`
- Modifier le `server.port`
- Assurez de modifier le expose dans le fichier `Dockerfile`
- Regenerez le binary

## Pour generer le binary (.jar):
- Dans le racine du projet lancer la commande:
- `./gradlew build`

## Pour lancer le projet:
- Assurez vous que docker est installé sur votre machine, lancez:
- `docker-compose up --build`

## Pour modifier le port de votre container:
- Ouvrez le docker compose-yml
- Dans la section PORT: Choisissez le port de votre container : le port exposé du spring boot.
- Par default: le port de container c'est 8081, et le port du spring c'est 8081
