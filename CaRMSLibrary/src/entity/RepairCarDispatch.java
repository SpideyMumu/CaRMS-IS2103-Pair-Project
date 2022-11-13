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
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

/**
 *
 * @author kathleen
 */
@Entity
public class RepairCarDispatch implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long repairCarDispatchId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    @Future
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    @Future
    private Date endDate;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    @NotNull
    private Car repairCar;

    public Long getRepairCarDispatchId() {
        return repairCarDispatchId;
    }

    public void setRepairCarDispatchId(Long repairCarDispatchId) {
        this.repairCarDispatchId = repairCarDispatchId;
    }

    public Car getRepairCar() {
        return repairCar;
    }

    public void setRepairCar(Car repairCar) {
        this.repairCar = repairCar;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (repairCarDispatchId != null ? repairCarDispatchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the repairCarDispatchId fields are not set
        if (!(object instanceof RepairCarDispatch)) {
            return false;
        }
        RepairCarDispatch other = (RepairCarDispatch) object;
        if ((this.repairCarDispatchId == null && other.repairCarDispatchId != null) || (this.repairCarDispatchId != null && !this.repairCarDispatchId.equals(other.repairCarDispatchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RepairCarDispatch[ id=" + repairCarDispatchId + " ]";
    }

}
