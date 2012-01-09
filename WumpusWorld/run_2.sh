#!/bin/bash
cd bin
date "+%c"
echo "Wie gross soll die Ausgangspopulation sein?"
read POPULATION
echo "An welcher Stelle soll die Rekombination erfolgen (-1==Zufall, sonst 0-)?"
read REKOMBINATION
echo "Die wie viel besten Prozent der Individuen duerfen sich fortpflanzen (1-100)?"
read FORTPFLANZUNG
echo "Wie gross soll die Wahrscheinlichkeit in Prozent fuer eine Mutation sein (0-100)?"
read MUTATION
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent "S2L" "P${POPULATION}I" "M${MUTATION}I" "K${REKOMBINATION}I" "F${FORTPFLANZUNG}I" 1>/dev/null 2>> "../Error.txt"
echo "fertig"
date "+%c"