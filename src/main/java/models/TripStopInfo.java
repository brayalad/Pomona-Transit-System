package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class TripStopInfo extends AbstractTableDataModel implements TableDataModel {
    private final long tripNumber;
    private final long stopNumber;
    private final long sequenceNumber;
    private final long drivingTime;

    private TripStopInfo(final long tripNumber, final long stopNumber, final long sequenceNumber, final long drivingTime) {
        this.tripNumber = tripNumber;
        this.stopNumber = stopNumber;
        this.sequenceNumber = sequenceNumber;
        this.drivingTime = drivingTime;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public long getDrivingTime() {
        return drivingTime;
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
        private long stopNumber;
        private long sequenceNumber;
        private long drivingTime;

        public Builder setTripNumber(final long tripNumber){
            this.tripNumber = tripNumber;
            return this;
        }

        public Builder setStopNumber(final long stopNumber){
            this.stopNumber = stopNumber;
            return this;
        }

        public Builder setSequenceNumber(final long sequenceNumber){
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public Builder setDrivingTime(final long drivingTime){
            this.drivingTime = drivingTime;
            return this;
        }

        public TripStopInfo build(){
            return new TripStopInfo(tripNumber, stopNumber, sequenceNumber, drivingTime);
        }
    }
}
