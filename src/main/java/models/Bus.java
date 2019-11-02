package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Year;

public class Bus implements TableDataModel {
    private String busID;
    private String model;
    private Year year;

    public Bus(){}

    public Bus(final String busID, final String model, final Year year) {
        this.busID = busID;
        this.model = model;
        this.year = year;
    }

    public void setBuildID(final String buildID) {
        this.busID = buildID;
    }

    public String getBusID() {
        return busID;
    }

    public void setModel(final String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public void setYear(final Year year) {
        this.year = year;
    }

    public Year getYear() {
        return year;
    }

    @Override
    public Object getPrimaryKey() {
        return busID;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
