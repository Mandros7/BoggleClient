# BoggleClient

A [Boggle](http://en.wikipedia.org/wiki/Boggle) game Client written in Java using JSON libraries. 


Source code
-----------
The source code was written as an assignment during a subject in the [University of Oviedo](http://www.uniovi.es) (Computer architecture). 
It's written in English, but commented in Spanish due to the fact that subject was imparted in Spanish as well. Several server versions
were pulled from [this repository](https://github.com/jcgranda/3comp_2013-2014).

Client uses 3 kinds of messages parsed using JSON:
- Commands: START, JOIN, LEAVE...
- Synchronous responses: SLEAVE, SJOIN... 
- Asynchronous responses: ALEAVE, AJOIN, ASTART ...

It also takes advantage of multi-threading by separating the network data receaving and sending from the proccesing and 
data presetantion layer. This layer was initially a command line based interface (tagged as "hito1" and "hito2") and then 
a GUI developed using SWING and AWT resources.

Authors
-----------
Hector Rodriguez Campo
Marco Martinez Avila
