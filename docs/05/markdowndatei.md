# Bericht Praktikum 5
## Was wurde gemacht?
Wir haben in diesem Praktikum Indexe gesetzt, um die Zeit für die Abfrage der Queries zu beschleunigen.
 
## Alle Queries:
### 1. Frage: Ausgabe aller Spieler (Spielername), die in einem bestimmten Zeitraum gespielt hatten.

    SELECT gamer.gamername FROM gamer, (SELECT * from Game as g WHERE g.gameBegin > '03.06.2019' AND g.gameEnd < '14.06.2019' ) as games WHERE gamer.gamerid = games.gamerplay_gamerid

Index: gamebegin, gameend
Dauer ohne Index: 315 ms
Dauer mit Index: 270 ms

### 2. Frage: Ausgabe zu einem bestimmten Spieler: Alle Spiele (Id, Datum), sowie die Anzahl der korrekten Antworten pro Spiel mit Angabe der Gesamtanzahl der Fragen pro Spiel bzw. alternativ den Prozentsatz der korrekt beantwortetenFragen.
  

   

         SELECT
    (SELECT count(*) as RightAnswers from game_rightanswes where game_rightanswes.game_gameid = games.gameid),
    (SELECT count(*) as TotalQuestions
    from game_question
    where game_question.game_gameid = games.gameid),
    games.gameid, games.gamebegin, games.gameend
    FROM gamer, (select * from game where game.gamerplay_gamerid = 1 ) as games where gamer.gamername = 'Gamer1'

Index: game_question.game_gameid, game_rightanswes.game_gameid, game.gamerplay_gamerid, gamer.gamername
Dauer ohne Index: 1.9s 
Dauer mit Index: 250 ms

### 3.Frage: Ausgabe aller Spieler mit Anzahl der gespielten Spiele, nach Anzahl absteigend geordnet.

   

    SELECT gamer.gamername , count(*) as Games FROM public.Game natural join gamer WHERE (GAMERPLAY_GAMERID = gamer.gamerid) GROUP BY  gamername ORDER BY Games DESC

Index:GAMERPLAY_GAMERID,  gamer.gamerid
Dauer ohne Index: 520 ms
Dauer mit Index: 300 ms

### 4. Frage: Ausgabe der am meisten gefragten Kategorie, oder alternativ, die Beliebtheit der Kategorien nach Anzahl derAuswahl absteigend sortiert.

      select * FROM categorystatistic_statistic where categorystatistic_categorystatid = (SELECT MAX(categorystatistic_statistic.categorystatistic_categorystatid) from categorystatistic_statistic ) ORDER BY statistic DESC 
    
   Index: categorystatistic_statistic.categorystatistic_categorystatid,
   Dauer ohne Index: 2 s
   Dauer mit Index: 150 ms
   
### Fazit:
Durch unsere Indexe hat sich die Schnelligkeit stark geändert. Obwohl davor Indexe vorhanden waren, wurde der Zugriff durch unsere Queries durch mehr Indexe verschnellert.


 

