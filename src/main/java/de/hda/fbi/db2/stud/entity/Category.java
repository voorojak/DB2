package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Category Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */

@Entity
@Table(name = "Category", schema = "public")
@NamedQueries({
    @NamedQuery(
        name = "Category.getCategoryByID",
        query = "select c from Category as c where c.categoryID = :categoryID"
    ),
    @NamedQuery(
        name = "Category.updateID",
        query = "UPDATE Category public set public.categoryStatisticPlay = :statisticPlay"
    ),
    @NamedQuery(
        name = "Category.deleteClass",
        query = "delete from Category c where c.categoryStatisticPlay.categoryStatID = :id"
    ),
    @NamedQuery(
        name = "Category.countAllCategories",
        query = "select count(c) from Category as c"
    )
})

public class Category implements Serializable {

    //Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryID;

    //Category -> Question
    @OneToMany(mappedBy = "categorieClass")
    private List<Question> questions = new ArrayList<>();

    //Catalog -> categorie
    @ManyToOne
    private Catalog catalogClass;

    //CategoryStat -> Category
    @ManyToOne(cascade = CascadeType.ALL)
    private CategoryStatistic categoryStatisticPlay;

    //Game -> Category
    @ManyToOne
    private Game gameObject;

    @Column(unique = true, name = "categoryname")
    private String categoryName;


    public Category() {

    }

    public String getCategorieName() {
        return categoryName;
    }

    public List<Question> getQuestion() {
        return questions;
    }

    public void setCategoryName(String categorieNameTmp) {
        categoryName = categorieNameTmp;
    }

    public void setCatalogClass(Catalog catalogClass) {
        this.catalogClass = catalogClass;
    }

    public void setGame(Game gameTemp) {
        gameObject = gameTemp;
    }

    public void setQuestion(Question questionTmp) {
        questions.add(questionTmp);
    }

    public void setCategoryStatisticPlay(CategoryStatistic categoryStatisticTemp) {
        categoryStatisticPlay = categoryStatisticTemp;
    }

}
