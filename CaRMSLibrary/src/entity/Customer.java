/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author muhdm
 */
@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long mobileNum;

    public Long getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(Long mobileNum) {
        this.mobileNum = mobileNum;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mobileNum != null ? mobileNum.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the mobileNum fields are not set
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.mobileNum == null && other.mobileNum != null) || (this.mobileNum != null && !this.mobileNum.equals(other.mobileNum))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Customer[ id=" + mobileNum + " ]";
    }
    
}
