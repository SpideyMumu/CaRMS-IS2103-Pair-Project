/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @Column(nullable = false, length = 22)
    private String rentalRateName;
    
    @Enumerated (EnumType.STRING)
    private RentalRateType rentalRateType;
    
    @Column(nullable = false, precision = 11, scale = 2)
    private BigDecimal ratePerDay;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date endDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public RentalRate() {
    }

    public RentalRate(String rentalRateName, RentalRateType rentalRateType, BigDecimal ratePerDay, Date startDate, Date endDate, CarCategory carCategory) {
        this.rentalRateName = rentalRateName;
        this.rentalRateType = rentalRateType;
        this.ratePerDay = ratePerDay;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carCategory = carCategory;
    }
    
    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    public RentalRateType getRentalRateType() {
        return rentalRateType;
    }

    public void setRentalRateType(RentalRateType rentalRateType) {
        this.rentalRateType = rentalRateType;
    }

    public CarCategory getCarCategory() {
        return carCategory;
    }

    public void setCarCategory(CarCategory carCategory) {
        if(this.carCategory != null)
        {
            this.carCategory.getRentalRates().remove(this);
        }
        
        this.carCategory = carCategory;
        
        if(this.carCategory != null)
        {
            if(!this.carCategory.getRentalRates().contains(this))
            {
                this.carCategory.getRentalRates().add(this);
            }
        }
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

    public String getRentalRateName() {
        return rentalRateName;
    }

    public void setRentalRateName(String rentalRateName) {
        this.rentalRateName = rentalRateName;
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
