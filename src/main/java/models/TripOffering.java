package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.Date;
import java.sql.Time;

public class TripOffering extends AbstractTableDataModel implements TableDataModel {
    private final long tripNumber;
    private final Date date;
    private final Time scheduledStartTime;
    private final Time scheduledArrivalTime;
    private final String driverName;
    private final String busID;

    private TripOffering(
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

    public long getTripNumber() {
        return tripNumber;
    }

    public Date getDate() {
        return date;
    }

    public Time getScheduledStartTime() {
        return scheduledStartTime;
    }

    public Time getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getBusID() {
        return busID;
    }

    public static Builder builder(){ return new Builder(); }

    @Override
    public Object getPrimaryKey() {
        return tripNumber;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }

    public static class Builder {
        private long tripNumber;
        private Date date;
        private Time scheduledStartTime;
        private Time scheduledArrivalTime;
        private String driverName;
        private String busID;

        public Builder setTripNumber(final long tripNumber) {
            this.tripNumber = tripNumber;
            return this;
        }

        public Builder setDate(final Date date) {
            this.date = date;
            return this;
        }

        public Builder setScheduledStartTime(final Time scheduledStartTime) {
            this.scheduledStartTime = scheduledStartTime;
            return this;
        }

        public Builder setScheduledArrivalTime(final Time scheduledArrivalTime) {
            this.scheduledArrivalTime = scheduledArrivalTime;
            return this;
        }

        public Builder setDriverName(final String driverName){
            this.driverName = driverName;
            return this;
        }

        public Builder setBusID(final String busID){
            this.busID = busID;
            return this;
        }

        public TripOffering build(){
            return new TripOffering(tripNumber, date, scheduledStartTime, scheduledArrivalTime, driverName, busID);
        }
    }
}
