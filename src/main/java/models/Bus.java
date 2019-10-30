package models;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.Year;

public class Bus extends AbstractTableDataModel implements TableDataModel {
    private final String busID;
    private final String model;
    private final Year year;

    private Bus(final String busID, final String model, final Year year) {
        this.busID = busID;
        this.model = model;
        this.year = year;
    }

    public String getBusID() {
        return busID;
    }

    public String getModel() {
        return model;
    }

    public Year getYear() {
        return year;
    }

    public static Builder builder(){ return new Builder(); }

    @Override
    public Object getPrimaryKey() {
        return busID;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, TO_STRING_STYLE);
    }

    public static class Builder {
        private String busID;
        private String model;
        private Year year;

        public Builder setBuildID(final String buildID) {
            this.busID = buildID;
            return this;
        }

        public Builder setModel(final String model) {
            this.model = model;
            return this;
        }

        public Builder setYear(final Year year) {
            this.year = year;
            return this;
        }

        public Bus build(){
            return new Bus(busID, model, year);
        }
    }

}
