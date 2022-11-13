/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.RentalRateType;

/**
 *
 * @author kathleen
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    
    @Column(nullable = false, length = 125, unique = true)
    @NotNull
    @Size(min = 1)
    private String name;
    
    @Enumerated (EnumType.STRING)
    @NotNull
    private RentalRateType type;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal ratePerDay;
    
    @Column(nullable = false)
    @NotNull
    private boolean enabled;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @Future
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    @Future
    private Date endDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    private CarCategory carCategory;
    
    @OneToMany (mappedBy = "rentalRate")
    private List<Reservation> reservations;
    
    public RentalRate() {
        this.enabled = true;
        this.reservations = new ArrayList<>();
    }

    public RentalRate(String rentalRateName, RentalRateType rentalRateType, CarCategory carCategory, BigDecimal ratePerDay, Date startDate, Date endDate) {
        this.name = rentalRateName;
        this.type = rentalRateType;
        this.ratePerDay = ratePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carCategory = carCategory;
        this.enabled = true;
        this.reservations = new ArrayList<>();
    }
    
    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public RentalRateType getType() {
        return type;
    }

    public void setType(RentalRateType type) {
        this.type = type;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
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

    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + rentalRateId + " ]";
    }
    
}
