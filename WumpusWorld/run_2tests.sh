#!/bin/bash
cd bin
date "+%c"

# hier als standard definiert: 
# Ausgangspopulation 50, Mutationswahrscheinlichkeit 5%, Rekombinationsstelle 3 und die besten 30% pflanzen sich fort
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent "P50I" "M5I" "K3I" "F30I" 1>/dev/null 2>> "../Error.txt"

# Abweichend: Ausgangspopulation 100 
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent "P100I" "M5I" "K3I" "F30I" 1>/dev/null 2>> "../Error.txt"

# Abweichend: Mutationswahrscheinlichkeit 30%
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent "P50I" "M30I" "K3I" "F30I" 1>/dev/null 2>> "../Error.txt"

# Abweichend: Kreuzungsstelle zufaellig
time java -cp .:./* wumpusworld.solutions.ws1112.JTSBMMSSNR.TestRegelAgent "P50I" "M5I" "K-1I" "F30I" 1>/dev/null 2>> "../Error.txt"

date "+%c"