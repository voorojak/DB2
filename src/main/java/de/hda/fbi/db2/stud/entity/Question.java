package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Question Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */

@Entity
@Table(name = "Question", schema = "public")
@NamedQueries({
    @NamedQuery(
        name = "Question.QuestionID",
        query = "select q from Question as q where q.categorieClass.categoryID in :categories"

    )
})

public class Question implements Serializable {

    //Variables
    @Id
    @Column(nullable = false, name = "questionID")
    private int questionID;


    @ElementCollection
    private Map<String, Boolean> answers = new HashMap<>();

    //categorie -> Question
    @ManyToOne
    private Category categorieClass;

    @Column(nullable = false, name = "questionString")
    private String questionString;


    public Question() {

    }

    public String getQuestionString() {
        return questionString;
    }
    public int getQuestionId() {
        return questionID;
    }

    public void setQuestionString(String questionTmp){
        questionString = questionTmp;
    }
    public void setQuestionId(int questionIdTmp) {
        questionID = questionIdTmp;
    }

    public void setCategory(Category c) {
        categorieClass = c;
    }

    public void setAnswer(int rightAnswerTemp,
                               String answer1,
                               String answer2,
                               String answer3,
                               String answer4) {
        switch (rightAnswerTemp) {
            case 1:
                answers.put(answer1, true);
                answers.put(answer2, false);
                answers.put(answer3, false);
                answers.put(answer4, false);
                break;
            case 2:
                answers.put(answer1, false);
                answers.put(answer2, true);
                answers.put(answer3, false);
                answers.put(answer4, false);
                break;
            case 3:
                answers.put(answer1, false);
                answers.put(answer2, false);
                answers.put(answer3, true);
                answers.put(answer4, false);
                break;
            case 4:
                answers.put(answer1, false);
                answers.put(answer2, false);
                answers.put(answer3, false);
                answers.put(answer4, true);
                break;
            default:
                break;
        }
    }

    public Map<String, Boolean> getAnswer() {
        return answers;
    }
}
