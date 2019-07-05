package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Category Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */


@Entity
@Table(name = "Game", schema = "public")

@NamedQueries({
    @NamedQuery(//Frage 1
        name = "Game.playersInDateRange",
        query = "select distinct g.gamerPlay " +
            "from Game as g " +
            "where g.gameBegin > :beginDate " +
            "and g.gameEnd < :endDate"
    ),
    @NamedQuery(//Frage 2
        name = "Game.getGameByUser",
        query = "select g from Game as g where g.gamerPlay.gamerName = :userID"
    )

})
public class Game implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int gameID;

  //Game -> Gamer
  @OneToMany
  private List<Gamer> gamerMap = new ArrayList<>();

  //Game -> Category
  @ElementCollection
  private List<String> allCategories = new ArrayList<>();

  //Gamer -> Game
  @ManyToOne
  private Gamer gamerPlay;

  @Column(name = "gameBegin")
  private Date gameBegin;

  @Column(name = "gameEnd")
  private Date gameEnd;

  @OneToMany
  private Map <String, Category> chosenCategories = new HashMap<>();

  @OneToMany
  private Map<String, Question> chosenQuestions = new HashMap<>();

  @ElementCollection
  private Map<Question, Boolean> rightAnswes = new HashMap<>();

  public Game() {

  }

  public Gamer getGamerPlay() {
    return gamerPlay;
  }
  public void setGamerMap(Gamer gamer) {
    gamerMap.add(gamer);
  }
  public void putGamerMap(Gamer gameClass) {
    gamerMap.add(gameClass);
  }

  public void setGamer(Gamer gamerTemp) {
    gamerPlay = gamerTemp;
  }

  public List <String> getAllCategories() {
    return allCategories;
  }

  public Map <String, Category> getChosenCategories() {
    return chosenCategories;
  }

  public void addNewCategory(Category category) {
    chosenCategories.put(category.getCategorieName(), category);
  }

  public void setChosenCategories(Map<String, Category> categories) {
    chosenCategories = categories;
  }

  public void setChosenQuestions(int position, Question question) {
    int counter = 0;
    for (Map.Entry<String, Question> entry : chosenQuestions.entrySet()) {
      if (position == counter) {
        chosenQuestions.put(question.getQuestionString(), question);
      }
      counter++;
    }
  }

  public Map <String, Question> getChosenQuestions() {
    return chosenQuestions;
  }

  public void addQuestionToGame(Question question) {
    chosenQuestions.put(question.getQuestionString(), question);
  }

  public void setAllCategories(List<String> categoriesTemp) {
    allCategories = categoriesTemp;
  }

  public void addAllCategories(String categoryTemp) {
    allCategories.add(categoryTemp);
  }


  public void setBegin() {
    this.gameBegin = new Date();
  }

  public void setEnd() {
    this.gameEnd = new Date();
  }

  public Date getBegin() {
    Date d = gameBegin;
    return d;
  }

  public Date getEnd() {
    Date d = gameEnd;
    return d;
  }

  public int getGameID() {
    return gameID;
  }

  public void setTotalRightAnswer(String result, String questionString) {
      if (result.equals("Right Answer")) {
        rightAnswes.put(chosenQuestions.get(questionString), true);
      } else if (result.equals("False Answer")) {
        rightAnswes.put(chosenQuestions.get(questionString), false);
      }
  }

  public void addRightQuestion (Question question) {
    rightAnswes.put(question, true);
  }

  public int getSizeOfAllRightAnswers() {
    return rightAnswes.size();
  }

  public int getSizeOfQuestions() {
    return chosenQuestions.size();
  }
}
