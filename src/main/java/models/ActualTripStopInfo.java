package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.sql.Date;
import java.sql.Time;

public class ActualTripStopInfo extends AbstractTableDataModel implements TableDataModel {
    private final long tripNumber;
    private final Date date;
    private final Time scheduledStartTime;
    private final long stopNumber;
    private final Time scheduledArrivalTime;
    private final Time actualStartTime;
    private final Time actualArrivalTime;
    private final long numberOfPassengerIn;
    private final long numberOfPassengerOut;

    private ActualTripStopInfo(
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

    public long getTripNumber() {
        return tripNumber;
    }

    public Date getDate() {
        return date;
    }

    public Time getScheduledStartTime() {
        return scheduledStartTime;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public Time getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public Time getActualStartTime() {
        return actualStartTime;
    }

    public Time getActualArrivalTime() {
        return actualArrivalTime;
    }

    public long getNumberOfPassengerIn() {
        return numberOfPassengerIn;
    }

    public long getNumberOfPassengerOut() {
        return numberOfPassengerOut;
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
        private long stopNumber;
        private Time scheduledArrivalTime;
        private Time actualStartTime;
        private Time actualArrivalTime;
        private long numberOfPassengerIn;
        private long numberOfPassengerOut;

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

        public Builder setStopNumber(final long stopNumber) {
            this.stopNumber = stopNumber;
            return this;
        }

        public Builder setScheduledArrivalTime(final Time scheduledArrivalTime) {
            this.scheduledArrivalTime = scheduledArrivalTime;
            return this;
        }

        public Builder setActualStartTime(final Time actualStartTime) {
            this.actualStartTime = actualStartTime;
            return this;
        }

        public Builder setActualArrivalTime(final Time actualArrivalTime) {
            this.actualArrivalTime = actualArrivalTime;
            return this;
        }

        public Builder setNumberOfPassengerIn(final long numberOfPassengerIn) {
            this.numberOfPassengerIn = numberOfPassengerIn;
            return this;
        }

        public Builder setNumberOfPassengerOut(final long numberOfPassengerOut) {
            this.numberOfPassengerOut = numberOfPassengerOut;
            return this;
        }

        public ActualTripStopInfo build(){
            return new ActualTripStopInfo(
                    tripNumber,
                    date,
                    scheduledStartTime,
                    stopNumber,
                    scheduledArrivalTime,
                    actualStartTime,
                    actualArrivalTime,
                    numberOfPassengerIn,
                    numberOfPassengerOut
            );
        }

    }

}
