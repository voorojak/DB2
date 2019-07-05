package de.hda.fbi.db2.stud.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
 * Catalog Class.
 * @version 0.1.1
 * @since 20.04.2019
 * @author Aria Gholami
 * @author Youssef Ebrahimzadeh
 */

@Entity
@Table(name = "Catalog", schema = "public")
@NamedQueries({
    @NamedQuery(
        name = "Catalog.getByCatalogID",
        query = "select cat from Catalog as cat where cat.catalogID in :categories"
    )
})
public class Catalog implements Serializable {

    //Variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int catalogID;

    @Column(name = "catalogName")
    private String catalogName;

    //catalog -> categorie
    @OneToMany(mappedBy = "catalogClass")
    private List<Category> catalogList = new ArrayList<>();

    public Catalog() {

    }

    public void setName(String nameTemp){
        catalogName = nameTemp;
    }

    public String getName() {
        return catalogName;
    }

    public List<Category> getCategoryList() {
        return catalogList;
    }

    public void setCategorieList(Category categoryTemp) {
        catalogList.add(categoryTemp);
    }
}
