# Esecuzione del progetto

### Dalla source, con maven compile + exec:

```
mvn compile exec:java -Dexec.args=""
```
Inserendo gli argomenti desiderati come opzione al plugin exec di maven.

### Dallo shaded jar:

```
java -jar target/progetto-1.0-jar-with-dependencies.jar
```
sostituendo il percorso del jar.

### Packaging in shaded jar:
```
mvn package
```

# Logging

È possibile abilitare i log dettagliati alla console utilizzando l'opzione -v dalla linea di comando.

L'app producerà logs nella cartella logs/ (creata automaticamente).