#!/bin/bash
ANZAHL="1"
if [ X"$1" != X"" ] ; then
	ANZAHL=$1
else 
	echo "Falls du mehr als eine Simulation pro Suchverfahren machen moechtest:"
	echo "./run.sh <AnzahlSimulationen pro Suchverfahren>"
fi

cd bin

RNDSEED=`od -An -t d8  -N8 /dev/urandom` 
RNDSEED=`echo "R${RNDSEED}L" | tr -d ' '`

echo "Benutze Seed $RNDSEED, Anzahl der Simulationen $ANZAHL"
echo "Starte mit Breitensuche"
date "+%c"
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestGoldsammelAgent BS "H${ANZAHL}I" $RNDSEED 1>/dev/null 2>> "../Error.txt"
echo "Starte mit Uniforme Kostensuche"
date "+%c"
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestGoldsammelAgent UK "H${ANZAHL}I" $RNDSEED 1>/dev/null 2>> "../Error.txt"
echo "Starte mit A*"
date "+%c"
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestGoldsammelAgent AS "H${ANZAHL}I" $RNDSEED 1>/dev/null 2>> "../Error.txt"
echo "Starte mit A*-Spezial"
date "+%c"
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestGoldsammelAgent ASS "H${ANZAHL}I" $RNDSEED 1>/dev/null 2>> "../Error.txt"
echo "fertig"
date "+%c"