package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;
import java.sql.Time;

public class TripOffering implements TableDataModel {
    private long tripNumber;
    private Date date;
    private Time scheduledStartTime;
    private Time scheduledArrivalTime;
    private String driverName;
    private String busID;

    public TripOffering(){}

    public TripOffering(
            final long tripNumber,
            final Date date,
            final Time scheduledStartTime,
            final Time scheduledArrivalTime,
            final String driverName,
            final String busID
    ) {
        this.tripNumber = tripNumber;
        this.date = date;
        this.scheduledStartTime = scheduledStartTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.driverName = driverName;
        this.busID = busID;
    }

    public void setTripNumber(final long tripNumber) {
        this.tripNumber = tripNumber;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public void setDate(final Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setScheduledStartTime(final Time scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public Time getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledArrivalTime(final Time scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public Time getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setDriverName(final String driverName){
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setBusID(final String busID){
        this.busID = busID;
    }

    public String getBusID() {
        return busID;
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
