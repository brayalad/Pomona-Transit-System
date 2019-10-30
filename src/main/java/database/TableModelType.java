package database;

import models.*;

import java.util.*;

public enum TableModelType {

    ACTUAL_TRIP_STOP_INFO(
            ActualTripStopInfo.class,
            "ActualTripStopInfo",
            Columns.TRIP_NUMBER_COLUMN_LABEL,
            Columns.DATE_COLUMN_LABEL,
            Columns.SCHEDULED_START_TIME_COLUMN_LABEL,
            Columns.STOP_NUMBER_COLUMN_LABEL,
            Columns.SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL,
            Columns.ACTUAL_START_TIME_COLUMN_LABEL,
            Columns.ACTUAL_ARRIVAL_TIME_COLUMN_LABEL,
            Columns.NUMBER_OF_PASSENGERS_IN_COLUMN_LABEL,
            Columns.NUMBER_OF_PASSENGERS_OUT_COLUMN_LABEL
    ),
    BUS(
            Bus.class,
            "Bus",
            Columns.BUS_ID_COLUMN_LABEL,
            Columns.MODEL_COLUMN_LABEL,
            Columns.YEAR_COLUMN_LABEL
    ),
    DRIVER(
            Driver.class,
            "Driver",
            Columns.DRIVER_NAME_COLUMN_LABEL,
            Columns.DRIVER_TELEPHONE_NUMBER_COLUMN_LABEL
    ),
    STOP(
            Stop.class,
            "Stop",
         Columns.STOP_NUMBER_COLUMN_LABEL,
         Columns.STOP_ADDRESS_COLUMN_LABEL
    ),
    TRIP(
            Trip.class,
            "Trip",
          Columns.TRIP_NUMBER_COLUMN_LABEL,
          Columns.START_LOCATION_NAME_COLUMN_LABEL,
          Columns.DESTINATION_NAME_COLUMN_LABEL
    ),
    TRIP_OFFERING(
            TripOffering.class,
            "TripOffering",
         Columns.TRIP_NUMBER_COLUMN_LABEL,
         Columns.DATE_COLUMN_LABEL,
         Columns.SCHEDULED_START_TIME_COLUMN_LABEL,
         Columns.SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL,
         Columns.DRIVER_NAME_COLUMN_LABEL,
         Columns.BUS_ID_COLUMN_LABEL
    ),
    TRIP_STOP_INFO(
            TripStopInfo.class,
            "TripStop",
            Columns.TRIP_NUMBER_COLUMN_LABEL,
            Columns.STOP_NUMBER_COLUMN_LABEL,
            Columns.SEQUENCE_NUMBER_COLUMN_LABEL,
            Columns.DRIVING_TIME_COLUMN_LABEL
    );

    private final Class clazz;
    private final String name;
    private final Set<String> columns;

    TableModelType(final Class clazz, final String name, final String ...columns){
        this.clazz = clazz;
        this.name = name;
        this.columns = new HashSet<>(Arrays.asList(columns));
    }

    public Class getClazz(){ return clazz; }

    public String getName(){ return name; }

    public Set<String> getColumns() {
        return columns;
    }

    public static Set<TableModelType> getDataTypeSet(){
        return new HashSet<>(Arrays.asList(TableModelType.values()));
    }

    public static Map<Class, TableModelType> getTypeToClassMap(){
        final Map<Class, TableModelType> map = new HashMap<>();
        for(final TableModelType type : TableModelType.values()){
            map.put(type.getClazz(), type);
        }
        return map;
    }

    public static class Columns {
        public static final String TRIP_NUMBER_COLUMN_LABEL = "TripNumber";
        public static final String DATE_COLUMN_LABEL = "Date";
        public static final String SCHEDULED_START_TIME_COLUMN_LABEL = "ScheduledStartTime";
        public static final String STOP_NUMBER_COLUMN_LABEL = "StopNumber";
        public static final String SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL = "ScheduledArrivalTime";
        public static final String ACTUAL_START_TIME_COLUMN_LABEL = "ActualStartTime";
        public static final String ACTUAL_ARRIVAL_TIME_COLUMN_LABEL = "ActualArrivalTime";
        public static final String NUMBER_OF_PASSENGERS_IN_COLUMN_LABEL = "NumberOfPassengerIn";
        public static final String NUMBER_OF_PASSENGERS_OUT_COLUMN_LABEL = "NumberOfPassengerOut";
        public static final String BUS_ID_COLUMN_LABEL = "BusID";
        public static final String MODEL_COLUMN_LABEL = "Model";
        public static final String YEAR_COLUMN_LABEL = "Year";
        public static final String DRIVER_NAME_COLUMN_LABEL = "DriverName";
        public static final String DRIVER_TELEPHONE_NUMBER_COLUMN_LABEL = "DriverTelephoneNumber";
        public static final String STOP_ADDRESS_COLUMN_LABEL = "StopAddress";
        public static final String START_LOCATION_NAME_COLUMN_LABEL = "StartLocationName";
        public static final String DESTINATION_NAME_COLUMN_LABEL = "DestinationName";
        public static final String SEQUENCE_NUMBER_COLUMN_LABEL = "SequenceNumber";
        public static final String DRIVING_TIME_COLUMN_LABEL = "DrivingTime";
    }

}
