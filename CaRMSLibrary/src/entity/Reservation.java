/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kathleen
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDate;
    @Column(nullable = false, unique = true, length = 22)
    private String creditCardNumber;
    @Column(nullable = false, unique = true, length = 4)
    private String cvv;
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal totalAmountChargeable;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car car;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet pickUpLocation;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet returnLocation;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarRentalCustomer carRentalCustomer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private RentalRate rentalRate;
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
    public CarRentalCustomer getCustomer() {
        return carRentalCustomer;
    }

    public void setCustomer(CarRentalCustomer customer) {
        this.carRentalCustomer = customer;
    }

    public Outlet getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(Outlet returnLocation) {
        this.returnLocation = returnLocation;
    }

    public Outlet getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Outlet pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        if(this.car != null)
        {
            this.car.getReservations().remove(this);
        }
        
        this.car = car;
        
        if(this.car != null)
        {
            if(!this.car.getReservations().contains(this))
            {
                this.car.getReservations().add(this);
            }
        }
    }

    public BigDecimal getTotalAmountChargeable() {
        return totalAmountChargeable;
    }

    public void setTotalAmountChargeable(BigDecimal totalAmountChargeable) {
        this.totalAmountChargeable = totalAmountChargeable;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public RentalRate getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(RentalRate rentalRate) {
        this.rentalRate = rentalRate;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }
    
}
