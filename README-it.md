# promethium-batch

In una elaborazione batch abbiamo una (o più) orgine dati (tipicamente un file, ma non solo),  una (o più) destinazione dati (tipicamente un file, ma non solo) e un processo elaborativo che prende i dati dalla origine, li trasforma in modo da poterli scrivere nella destinazione.

Nel caso che ci siano più origini dati, è il processo elaborativo che deve decidere da quale origine leggere, e non è possibile parallelizzare il processo elaborativo.

Nel caso di una singola origine dati il processo elaborativo può ricevere in input i dati letti dalla sorgente dati, non richiede una logica di lettura. La lettura dalla origine dati può essere fatta dalla infrastruttura batch, e il processo elaborativo può essere parallelizzato. In questo caso, nelle destinazioni dati può essere mantenuta o meno la sequenzalità della origine dati (dipende dalla implementazione della parallellizzazione).

Per gerstire in modo generico le origini e destinazione dati vengono usate le classi
* `SourceResource` origine dati
* `SinkResource` destinazione dati

Nel caso che sia presente una sola origine dati la parte elaborativa può produrre una tupla e sarà la infrastruttura a inviare i dati alle destinazioni, in questo caso il formato elaborativo è:

~~~java
Loop.from(src).into(snk1,snk2).forEach(it -> { ... });
~~~

in alternativa alla parte elaborativa oltre al valore di input vengono forniti i `Consumer` per scrivere direttamente in dati sulle corrispondenti destinazioni, il questo caso il formato elaborativo è:

~~~java
Loop.from(src).into(snk1,snk2).forEach((it,wr1,wr2) -> { ... });
~~~

entrambi i formati possono essere parallelizzati, ma solo il primo permette una implementazione che mantiene l'ordine dei dati originale.

Nel caso che siano presenti più di una origine dati, alla parte elaborativa vongono forniti i `Supplier` per leggere i dati dalle origini, e i `Consumer` per scrivere i dati, in questo caso il formato elaborativo è:

~~~java
Loop.from(src1, src2).into(snk1, snk2)
        .proceed((rd1, rd2, wr1, wr2) -> { ... });
~~~

Nel caso di elaborazione sequenziale la separazione della elaborazione in una parte dedicata alla lettura dei dati, una dedicata alla scrittura dei dati e una specifica per la elaborazione dei dati, non offre particolati vantaggi. Questa separazione è propedeutica alla elaborazione parallela. 

Una elaborazione sequenziale nella forma

~~~java
Loop.from(src).into(...).forEach(...);
~~~

può essere trasformata in parallela sostituendo il `forEach` in `forEachParallel`:

~~~java
Loop.from(src).into(...).forEachParallel(numTask, ...);
~~~

senza alcuna modifica della parte di elaborazione dati.