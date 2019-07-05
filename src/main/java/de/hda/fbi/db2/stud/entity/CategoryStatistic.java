package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Category Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */

@Entity
@Table(name = "CategoryStatistic", schema = "public")
@NamedQueries({
    @NamedQuery(
        name = "CategoryStatistic.deleteAll",
        query = "delete from CategoryStatistic c where c.categoryStatID <= :id"
    ),
    @NamedQuery(
        name = "CategoryStatistic.getStatistic",
        query = "select statistic FROM CategoryStatistic as statistic "
            + "WHERE statistic.categoryStatID = (select  max(statistic.categoryStatID)"
            + " from CategoryStatistic  as statistic)"
)
})
@SequenceGenerator(name = "seq", initialValue = 1, allocationSize = 1)
public class CategoryStatistic implements Serializable{

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
  private int categoryStatID;

  @ElementCollection
  private Map<String, Integer> statistic = new HashMap<>();

  //CategoryStatistic -> Category
  @OneToMany(mappedBy = "categoryStatisticPlay", cascade = CascadeType.ALL)
  private List<Category> categoryList = new ArrayList<>();

  public CategoryStatistic() {

  }
  public void setCategoryStatFirst(String categoryString) {
    statistic.put(categoryString, 0);
  }

  public void setStatistic(String categoryName) {
    int counter  = statistic.get(categoryName);
    counter++;
    statistic.replace(categoryName, counter);
  }

  public void setStatisticNormal(String categoryName) {
    int counter  = statistic.get(categoryName);
    statistic.replace(categoryName, counter);
  }

  public void setCategoryList(Category categoryTemp) {
    categoryList.add(categoryTemp);
  }

  public Map<String, Integer> getStatistic() {
    return statistic;
  }
  public void setCategoryStaticMap (String categoryNameTemp, int counterTemp) {
    statistic.put(categoryNameTemp, counterTemp);
  }
  public int getCategoryStatID() {
    return categoryStatID;
  }
  public void setCategoryMap(Map<String, Integer> temp) {
    statistic = temp;
  }

  public void clearCategoryList() {
    categoryList.clear();
  }

}
