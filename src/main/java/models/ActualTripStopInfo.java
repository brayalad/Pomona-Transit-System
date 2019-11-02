package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Date;
import java.sql.Time;

public class ActualTripStopInfo implements TableDataModel {
    private long tripNumber;
    private Date date;
    private Time scheduledStartTime;
    private long stopNumber;
    private Time scheduledArrivalTime;
    private Time actualStartTime;
    private Time actualArrivalTime;
    private long numberOfPassengerIn;
    private long numberOfPassengerOut;

    public ActualTripStopInfo(){}

    public ActualTripStopInfo(
            final long tripNumber,
            final Date date,
            final Time scheduledStartTime,
            final long stopNumber,
            final Time scheduledArrivalTime,
            final Time actualStartTime,
            final Time actualArrivalTime,
            final long numberOfPassengerIn,
            final long numberOfPassengerOut
    ) {
        this.tripNumber = tripNumber;
        this.date = date;
        this.scheduledStartTime = scheduledStartTime;
        this.stopNumber = stopNumber;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.actualStartTime = actualStartTime;
        this.actualArrivalTime = actualArrivalTime;
        this.numberOfPassengerIn = numberOfPassengerIn;
        this.numberOfPassengerOut = numberOfPassengerOut;
    }

    public void setTripNumber(final long tripNumber){
        this.tripNumber = tripNumber;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public void setDate(final Date date){
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setScheduledStartTime(final Time scheduledStartTime){
        this.scheduledStartTime = scheduledStartTime;
    }

    public Time getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setStopNumber(final long stopNumber){
        this.stopNumber = stopNumber;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public void setScheduledArrivalTime(final Time scheduledArrivalTime){
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public Time getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setActualStartTime(final Time actualStartTime){
        this.actualStartTime = actualStartTime;
    }

    public Time getActualStartTime() {
        return actualStartTime;
    }

    public void setActualArrivalTime(final Time actualArrivalTime){
        this.actualArrivalTime = actualArrivalTime;
    }

    public Time getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setNumberOfPassengerIn(final long numberOfPassengerIn){
        this.numberOfPassengerIn = numberOfPassengerIn;
    }

    public long getNumberOfPassengerIn() {
        return numberOfPassengerIn;
    }

    public void setNumberOfPassengerOut(final long numberOfPassengerOut){
        this.numberOfPassengerOut = numberOfPassengerOut;
    }

    public long getNumberOfPassengerOut() {
        return numberOfPassengerOut;
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
