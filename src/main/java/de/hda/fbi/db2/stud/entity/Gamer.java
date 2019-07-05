package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**  <property name="eclipselink.jdbc.batch-writing" value="JDBC"/>
 <property name="eclipselink.jdbc.batch-writing.size" value="1000"/>
 * Category Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */

@Entity
@Table (name = "Gamer", schema = "public")
@NamedQueries ({
    @NamedQuery (
        name = "Gamer.findByUsername",
        query = "select g from Gamer as g where g.gamerName = :gamerName"
    ),
    @NamedQuery (  //Frage 3
        name = "Gamer.sortedByGames",
        query = "SELECT" +
            " gamer," +
            " SIZE (gamer.gameList) AS numberOfPlayedGames" +
            " FROM Gamer as gamer" +
            " ORDER BY numberOfPlayedGames DESC"
    ),
    @NamedQuery (
        name = "Gamer.findGamers",
        query = "select g from Gamer as g"
    )
})
public class Gamer implements Serializable{


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int gamerID;

  //Game -> Gamer
  @ManyToOne
  private Game gamePlay;

  @Column(name = "gamerName")
  private String gamerName;

  //Gamer -> Game
  @OneToMany(mappedBy = "gamerPlay", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List <Game> gameList = new ArrayList<>();

  public Gamer() {

  }


  public void setNewGamer(String gamerName) {
    this.gamerName = gamerName;
  }

  public void setGame(Game gameTemp) {
    gamePlay = gameTemp;
  }

  public String getGamerName() {
    return gamerName;
  }

  public void addGameToList(Game gameTemp){
    gameList.add(gameTemp);
  }
  public List<Game> getGameList() {
    return gameList;
  };

  public Game getGamePlay() {
    return gamePlay;
  }

  public int getGamerID() {
    return gamerID;
  }
  public Integer getNumberOfGames() {
    return gameList.size();
  }

}
