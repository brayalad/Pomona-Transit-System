package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Stop extends AbstractTableDataModel implements TableDataModel {
    private final long stopNumber;
    private final String stopAddress;

    private Stop(final long stopNumber, final String stopAddress) {
        this.stopNumber = stopNumber;
        this.stopAddress = stopAddress;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    public static Builder builder(){ return new Builder(); }

    @Override
    public Object getPrimaryKey() {
        return stopNumber;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }

    public static class Builder {
        private long stopNumber;
        private String stopAddress;

        public Builder setStopNumber(final long stopNumber){
            this.stopNumber = stopNumber;
            return this;
        }

        public Builder setStopAddress(final String stopAddress){
            this.stopAddress = stopAddress;
            return this;
        }

        public Stop build(){
            return new Stop(stopNumber, stopAddress);
        }
    }
}
