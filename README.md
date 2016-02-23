# Projet2Reseau
##  Gestionnaire de tâche à l'aide des sockets TCP
### Edouard CATTEZ - Julien LELEU

----------------

## Protocole

| Action| Ressource  | Data | Succés | Echec |
|:------|:-----------|:-----|:-------|-------|
|GET|/taches/{id}|-|OK **createur**={createur}:**description**={description}:**etat**={etat}|NOT_FOUND {id}|
|POST|/taches/|**createur**={createur}:**description**={description}[:**executant**={executant}]|CREATED {id}||
|PUT|/taches/{id}|**etat**={etat}:**executant**={executant}|MODIFIED {id}|NOT_FOUND {id}|
|DELETE|/taches/{id}||REMOVED {id}|NOT_FOUND {id}|
|POST|/users/|login|OK login||

Les datas peuvent être envoyées dans n'importe quel ordre en respectant la nomenclature `clef=valeur`
