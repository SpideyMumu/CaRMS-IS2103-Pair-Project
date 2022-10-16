/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 *
 * @author muhdm
 */
@Entity
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (length = 255, nullable = false, unique = true)
    private Long modelId;
    @Column (length = 255, nullable = false)
    private String makeName;
    @Column (length = 255, nullable = false, unique = true)
    private String modelName;
    
    @OneToMany
    @JoinColumn (nullable = false)
    private List<Car> listOfCars;

    public Model() {
        this.listOfCars = new ArrayList<Car>();
    }
    
    public List<Car> getListOfCars() {
        return listOfCars;
    }

    public void setListOfCars(List<Car> listOfCars) {
        this.listOfCars = listOfCars;
    }
    
    

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (modelId != null ? modelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the modelId fields are not set
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.modelId == null && other.modelId != null) || (this.modelId != null && !this.modelId.equals(other.modelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enitity.Model[ id=" + modelId + " ]";
    }
    
}
