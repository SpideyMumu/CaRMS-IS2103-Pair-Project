/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author muhdm
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, unique = true, length = 22)
    @Size(min = 1)
    @NotNull
    private String categoryName;

    @OneToMany(mappedBy = "carCategory")
    private List<Model> models;

    @OneToMany(mappedBy = "carCategory", fetch = FetchType.EAGER)
    private List<RentalRate> rentalRates;

    public CarCategory() {
        this.models = new ArrayList<Model>();
        this.rentalRates = new ArrayList<RentalRate>();
    }

    public CarCategory(String categoryName, List<Model> models, List<RentalRate> rentalRates) {
        this.categoryName = categoryName;
        this.models = models;
        this.rentalRates = rentalRates;
    }

    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
    }

    public CarCategory(String categoryName) {
        this.categoryName = categoryName;
        this.models = new ArrayList<Model>();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (categoryId != null ? categoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the categoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.categoryId == null && other.categoryId != null) || (this.categoryId != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + categoryId + " ]";
    }

}
