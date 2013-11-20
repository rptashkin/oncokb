package org.mskcc.cbio.oncogkb.model;
// Generated Oct 31, 2013 4:28:42 PM by Hibernate Tools 3.2.1.GA

/**
 * TumorType generated by hbm2java
 */
public class TumorType  implements java.io.Serializable {


     private String tumorTypeId;
     private String name;
     private String shortName;
     private String color;

    public TumorType() {
    }

	
    public TumorType(String tumorTypeId, String name, String shortName) {
        this.tumorTypeId = tumorTypeId;
        this.name = name;
        this.shortName = shortName;
    }
    public TumorType(String tumorTypeId, String name, String shortName, String color) {
       this.tumorTypeId = tumorTypeId;
       this.name = name;
       this.shortName = shortName;
       this.color = color;
    }
   
    public String getTumorTypeId() {
        return this.tumorTypeId;
    }
    
    public void setTumorTypeId(String tumorTypeId) {
        this.tumorTypeId = tumorTypeId;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getShortName() {
        return this.shortName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
    public String getColor() {
        return this.color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }


}


