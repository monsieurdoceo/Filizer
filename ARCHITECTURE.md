# Filizer - Architecture et points a corriger

## Etat actuel

Le projet est maintenant structure de facon plus propre et plus maintenable:

- `FilizerPlugin` est le point d'entree Bukkit.
- `PluginBootstrap` centralise le wiring.
- `FileManager` joue le role de facade.
- `CustomFile` reste l'objet metier principal.
- `FileRegistry` n'est plus un singleton utilise dans le flux normal.
- La registry est indexee par chemin absolu normalise, ce qui evite les collisions de noms.

La compilation passe actuellement.

## Ce qu'il faut encore corriger

### 1. Finaliser `WatchServiceSynchronizationStrategy`

Actuellement, la strategie est encore un stub.

A faire:
- creer un `WatchService` unique
- enregistrer les dossiers parents des fichiers suivis
- lancer un thread dedie a l'ecoute des evenements
- recharger le bon `CustomFile` lors des modifications
- fermer proprement le watcher a l'arret du plugin

### 2. Ajouter une vraie couche de tests

Le projet n'a pas encore de tests automatises.

Tests a ajouter en priorite:
- creation d'un fichier via `FileManager`
- ajout unique dans le registry
- recherche par nom et par chemin
- suppression du disque et du registry
- rechargement apres modification externe

### 3. Durer `PluginBootstrap`

`start()` est propre, mais `stop()` est encore minimal.

A prevoir:
- arret des threads ou watchers futurs
- nettoyage des ressources eventuelles
- remise a zero des references si besoin

### 4. Mieux gerer les recherches par nom

La recherche par nom reste pratique, mais elle peut devenir ambigue si plusieurs fichiers ont le meme nom.

Recommandation:
- privilegier les recherches par chemin dans les nouveaux usages
- garder `findFile(String)` pour compatibilite
- documenter clairement son comportement en cas de collision

### 5. Nettoyer les conventions de nommage

Le projet est encore un peu mixe entre anciens et nouveaux noms.

A harmoniser:
- `file/infrastructure` pour tout ce qui touche au disque et au YAML
- `file/domain` pour les objets metier
- `file/api` pour la facade publique
- `shared/util` pour les utilitaires transverses

## Ce qu'il faut ajouter ensuite

### Fonctionnel

- une vraie gestion des erreurs plus fine
- une methode de chargement de fichiers existants plus explicite
- une API plus claire pour les cas ou le nom n'est pas unique
- un vrai cycle de vie pour les services de synchro

### Qualite

- tests unitaires
- tests d'integration legers
- validation des chemins et des noms plus stricte
- documentation des comportements limites

## Priorites recommandees

### Priorite haute

1. Finaliser la synchro `WatchService`
2. Ajouter des tests sur `FileManager` et `FileRegistry`
3. Documenter les cas ambigus sur la recherche par nom

### Priorite moyenne

4. Nettoyer le lifecycle du bootstrap
5. Ajouter une meilleure gestion des erreurs metier
6. Harmoniser les conventions de nommage restantes

### Priorite basse

7. Raffiner la documentation interne
8. Ajouter des exemples d'utilisation de l'API
9. Preparer une couche plus formelle pour les futures features

## Resume

La structure actuelle est deja saine.

Les points restants ne sont pas des urgences de refactor massif, mais plutot des finitions qui feront passer le projet d'une bonne base a une base vraiment robuste.
