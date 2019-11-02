package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TripStopInfo implements TableDataModel {
    private long tripNumber;
    private long stopNumber;
    private long sequenceNumber;
    private long drivingTime;

    public TripStopInfo(){}

    public TripStopInfo(final long tripNumber, final long stopNumber, final long sequenceNumber, final long drivingTime) {
        this.tripNumber = tripNumber;
        this.stopNumber = stopNumber;
        this.sequenceNumber = sequenceNumber;
        this.drivingTime = drivingTime;
    }

    public void setTripNumber(final long tripNumber){
        this.tripNumber = tripNumber;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public void setStopNumber(final long stopNumber){
        this.stopNumber = stopNumber;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public void setSequenceNumber(final long sequenceNumber){
        this.sequenceNumber = sequenceNumber;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setDrivingTime(final long drivingTime){
        this.drivingTime = drivingTime;
    }

    public long getDrivingTime() {
        return drivingTime;
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
