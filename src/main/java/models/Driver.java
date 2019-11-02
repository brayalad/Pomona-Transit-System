package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class Driver implements TableDataModel {
    private String driverName;
    private String driverTelephoneNumber;

    public Driver(){}

    public Driver(final String driverName, final String driverTelephoneNumber) {
        this.driverName = driverName;
        this.driverTelephoneNumber = driverTelephoneNumber;
    }

    public void setDriversName(final String driverName) {
        this.driverName = driverName;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverTelephoneNumber(final String driverTelephoneNumber) {
        this.driverTelephoneNumber = driverTelephoneNumber;
    }

    public String getDriverTelephoneNumber() {
        return driverTelephoneNumber;
    }

    @Override
    public Object getPrimaryKey() {
        return driverName;
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
