package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Trip implements TableDataModel {
    private long tripNumber;
    private String startLocationName;
    private String destinationName;

    public Trip(){}

    public Trip(final long tripNumber, final String startLocationName, final String destinationName){
        this.tripNumber = tripNumber;
        this.startLocationName = startLocationName;
        this.destinationName = destinationName;
    }

    public void setTripNumber(final long tripNumber){
        this.tripNumber = tripNumber;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public void setStartLocationName(final String startLocationName){
        this.startLocationName = startLocationName;
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    public void setDestinationName(final String destinationName){
        this.destinationName = destinationName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    @Override
    public Object getPrimaryKey() {
        return tripNumber;
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
