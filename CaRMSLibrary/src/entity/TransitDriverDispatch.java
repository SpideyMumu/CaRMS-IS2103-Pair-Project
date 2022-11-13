/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author muhdm
 */
@Entity
public class TransitDriverDispatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitId;
    
    @ManyToOne(optional = false)
    @JoinColumn (name = "originOutletId", nullable = false)
    private Outlet originOutlet;
    
    @ManyToOne(optional = false)
    @JoinColumn (name = "returnnOutletId", nullable = false)
    private Outlet returnOutlet;
    
    @ManyToOne(optional = false)
    @JoinColumn (name = "driverEmployeeId", nullable = false)
    private Employee driver;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date transitStartDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date transitEndDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car transitCar;

    public TransitDriverDispatch() {
    }
    
    
    public Outlet getOriginOutlet() {
        return originOutlet;
    }

    public void setOriginOutlet(Outlet originOutlet) {
        this.originOutlet = originOutlet;
    }

    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }
    
    public Car getTransitCar() {
        return transitCar;
    }

    public void setTransitCar(Car transitCar) {
        this.transitCar = transitCar;
    }

    public Date getTransitEndDate() {
        return transitEndDate;
    }

    public void setTransitEndDate(Date transitEndDate) {
        this.transitEndDate = transitEndDate;
    }

    public Date getTransitStartDate() {
        return transitStartDate;
    }

    public void setTransitStartDate(Date transitStartDate) {
        this.transitStartDate = transitStartDate;
    }    
    
    public Long getTransitId() {
        return transitId;
    }

    public void setTransitId(Long transitId) {
        this.transitId = transitId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitId != null ? transitId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitId fields are not set
        if (!(object instanceof TransitDriverDispatch)) {
            return false;
        }
        TransitDriverDispatch other = (TransitDriverDispatch) object;
        if ((this.transitId == null && other.transitId != null) || (this.transitId != null && !this.transitId.equals(other.transitId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDriverDispatch[ id=" + transitId + " ]";
    }
    
}
