package mappers;

import database.TableModelType.Columns;
import models.ActualTripStopInfo;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ActualTripStopInfoMapper implements TableDataModelMapper<ActualTripStopInfo> {

    @Override
    public ActualTripStopInfo mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final ActualTripStopInfo actualTripStopInfo = new ActualTripStopInfo();

        actualTripStopInfo.setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL));
        actualTripStopInfo.setDate(resultSet.getDate(Columns.DATE_COLUMN_LABEL));
        actualTripStopInfo.setScheduledStartTime(resultSet.getTime(Columns.SCHEDULED_START_TIME_COLUMN_LABEL));
        actualTripStopInfo.setStopNumber(resultSet.getLong(Columns.STOP_NUMBER_COLUMN_LABEL));
        actualTripStopInfo.setScheduledArrivalTime(resultSet.getTime(Columns.SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL));
        actualTripStopInfo.setActualStartTime(resultSet.getTime(Columns.ACTUAL_START_TIME_COLUMN_LABEL));
        actualTripStopInfo.setActualArrivalTime(resultSet.getTime(Columns.ACTUAL_ARRIVAL_TIME_COLUMN_LABEL));
        actualTripStopInfo.setNumberOfPassengerIn(resultSet.getLong(Columns.NUMBER_OF_PASSENGERS_IN_COLUMN_LABEL));
        actualTripStopInfo.setNumberOfPassengerOut(resultSet.getLong(Columns.NUMBER_OF_PASSENGERS_OUT_COLUMN_LABEL));

        return actualTripStopInfo;
    }

    @Override
    public ActualTripStopInfo mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
