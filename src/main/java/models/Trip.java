package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Trip extends AbstractTableDataModel implements TableDataModel {
    private final long tripNumber;
    private final String startLocationName;
    private final String destinationName;

    private Trip(final long tripNumber, final String startLocationName, final String destinationName){
        this.tripNumber = tripNumber;
        this.startLocationName = startLocationName;
        this.destinationName = destinationName;
    }

    public long getTripNumber() {
        return tripNumber;
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    public String getDestinationName() {
        return destinationName;
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
        private String startLocationName;
        private String destinationName;

        public Builder setTripNumber(final long tripNumber){
            this.tripNumber = tripNumber;
            return this;
        }

        public Builder setStartLocationName(final String startLocationName){
            this.startLocationName = startLocationName;
            return this;
        }

        public Builder setDestinationName(final String destinationName){
            this.destinationName = destinationName;
            return this;
        }

        public Trip build(){
            return new Trip(tripNumber, startLocationName, destinationName);
        }
    }
}
