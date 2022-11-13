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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author kathleen
 */
@Entity
public class CarRentalCustomer extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;    
    
    @Column(nullable = false, length = 10)
    private String password;
    
    @OneToMany(mappedBy="carRentalCustomer")
    private List<Reservation> reservations;

    public CarRentalCustomer() {
        super();
        this.reservations = new ArrayList<Reservation>();
    }

    public CarRentalCustomer(String password, List<Reservation> reservations) {
        this();
        this.password = password;
        this.reservations = reservations;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carRentalCustomerId fields are not set
        if (!(object instanceof CarRentalCustomer)) {
            return false;
        }
        CarRentalCustomer other = (CarRentalCustomer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + customerId + " ]";
    }
    
}
