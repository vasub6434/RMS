package com.bonrix.dggenraterset.Model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "fencedata")
public class FenceData {

    @Id
    @GeneratedValue
    private Long fenceid;
    
    private String fencetype;
    
    private String fencevalue;
    
    private Long managerid;
    
    private String fencename;
    
    private Boolean status;
    
    // Getters and Setters
    public Long getFenceid() {
        return fenceid;
    }

    public void setFenceid(Long fenceid) {
        this.fenceid = fenceid;
    }

    public String getFencetype() {
        return fencetype;
    }

    public void setFencetype(String fencetype) {
        this.fencetype = fencetype;
    }

    public String getFencevalue() {
        return fencevalue;
    }

    public void setFencevalue(String fencevalue) {
        this.fencevalue = fencevalue;
    }

    public Long getManagerid() {
        return managerid;
    }

    public void setManagerid(Long managerid) {
        this.managerid = managerid;
    }

    public String getFencename() {
        return fencename;
    }

    public void setFencename(String fencename) {
        this.fencename = fencename;
    }

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

    

    
   
  
}