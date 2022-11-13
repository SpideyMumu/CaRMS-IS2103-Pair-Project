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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.CarStatus;

/**
 *
 * @author muhdm
 */
@Entity
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    
    @Column (unique = true, nullable = false, length = 22)
    @NotNull
    @Size (min = 8, max = 8)
    private String licensePlateNum;
    
    @Column (nullable = false, length = 22)
    @NotNull
    private String color;

    @ManyToOne (optional = false)
    @JoinColumn(name = "modelId" , nullable = false)
    @NotNull
    private Model model;

    @Enumerated(EnumType.STRING)
    @NotNull
    private CarStatus status;

    // Have to make all relationships mandatory
    @ManyToOne (optional = false)
    @JoinColumn(name = "outletId", nullable = false)
    @NotNull
    private Outlet outlet;    

    @OneToMany(mappedBy="car")
    private List<Reservation> reservations;
    
    
    public Car() {
        this.reservations = new ArrayList<>();
    }

    public Car(String licensePlateNum, Model model, CarStatus status, Outlet outlet) {
        this.licensePlateNum = licensePlateNum;
        this.model = model;
        this.status = status;
        this.outlet = outlet;
        this.color = "Black";
        this.reservations = new ArrayList<Reservation>();
    }
    
    public CarStatus getStatus() {
        return status;
    }

    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    
    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carId != null ? carId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carId fields are not set
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.carId == null && other.carId != null) || (this.carId != null && !this.carId.equals(other.carId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Car[ id=" + carId + " ]";
    }
    
}
