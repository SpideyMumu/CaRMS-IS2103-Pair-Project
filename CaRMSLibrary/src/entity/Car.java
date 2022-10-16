/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author muhdm
 */
@Entity
public class Car implements Serializable {

    //private static final long serialVersionUID = 1L;
    @Id
    @Column (nullable = false)
    private String licensePlateNum;
    
    @ManyToOne (optional = false)
    @JoinColumn (nullable = false)
    private Model model;
    
    @Column (nullable = false)
    private String color;
    private static String DEFAULT_COLOR = "Black";
    
    @Column (nullable = false)
    private String status;  
    private static String DEFAULT_STATUS = "Available";
    //    private Outlet outlet;
    //    private CarCategory carCategory;

    public Car() {
    }

    
    public Car(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
        this.status = DEFAULT_STATUS;
        this.color = DEFAULT_COLOR;
    }
    
    
    
    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (licensePlateNum != null ? licensePlateNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the licensePlateNum fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.licensePlateNum == null && other.licensePlateNum != null) || (this.licensePlateNum != null && !this.licensePlateNum.equals(other.licensePlateNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "enitity.Car[ id=" + licensePlateNum + " ]";
    }
    
}
