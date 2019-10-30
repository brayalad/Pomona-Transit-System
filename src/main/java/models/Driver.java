package models;

import org.apache.commons.lang3.builder.ToStringBuilder;


public class Driver extends AbstractTableDataModel implements TableDataModel {
    private final String driverName;
    private final String driverTelephoneNumber;

    private Driver(final String driverName, final String driverTelephoneNumber) {
        this.driverName = driverName;
        this.driverTelephoneNumber = driverTelephoneNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverTelephoneNumber() {
        return driverTelephoneNumber;
    }

    public static Builder builder(){ return new Builder(); }

    @Override
    public Object getPrimaryKey() {
        return driverName;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }

    public static class Builder {
        private String driversName;
        private String driverTelephoneNumber;

        public Builder setDriversName(final String driversName) {
            this.driversName = driversName;
            return this;
        }

        public Builder setDriverTelephoneNumber(final String driverTelephoneNumber) {
            this.driverTelephoneNumber = driverTelephoneNumber;
            return this;
        }

        public Driver build(){
            return new Driver(driversName, driverTelephoneNumber);
        }
    }
}
