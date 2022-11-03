/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
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
public class CarRentalCustomer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carRentalCustomerId;
    @Column(nullable = false, unique = true, length = 8)
    private String mobileNumber;
    @Column(nullable = false, length = 10)
    private String password;
    
    @OneToMany
    private List<Reservation> reservations;
    
    
    public Long getCarRentalCustomerId() {
        return carRentalCustomerId;
    }

    public void setCarRentalCustomerId(Long carRentalCustomerId) {
        this.carRentalCustomerId = carRentalCustomerId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carRentalCustomerId != null ? carRentalCustomerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carRentalCustomerId fields are not set
        if (!(object instanceof CarRentalCustomer)) {
            return false;
        }
        CarRentalCustomer other = (CarRentalCustomer) object;
        if ((this.carRentalCustomerId == null && other.carRentalCustomerId != null) || (this.carRentalCustomerId != null && !this.carRentalCustomerId.equals(other.carRentalCustomerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + carRentalCustomerId + " ]";
    }
    
}
