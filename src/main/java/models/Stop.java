package models;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Stop implements TableDataModel {
    private long stopNumber;
    private String stopAddress;

    public Stop(){}

    public Stop(final long stopNumber, final String stopAddress) {
        this.stopNumber = stopNumber;
        this.stopAddress = stopAddress;
    }

    public void setStopNumber(final long stopNumber){
        this.stopNumber = stopNumber;
    }

    public long getStopNumber() {
        return stopNumber;
    }

    public void setStopAddress(final String stopAddress){
        this.stopAddress = stopAddress;
    }

    public String getStopAddress() {
        return stopAddress;
    }

    @Override
    public Object getPrimaryKey() {
        return stopNumber;
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
