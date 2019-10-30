package mappers;

import database.TableModelType.Columns;
import models.TripOffering;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripOfferingMapper implements TableDataModelMapper<TripOffering> {

    @Override
    public TripOffering mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return TripOffering.builder()
                .setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL))
                .setDate(resultSet.getDate(Columns.DATE_COLUMN_LABEL))
                .setScheduledStartTime(resultSet.getTime(Columns.SCHEDULED_START_TIME_COLUMN_LABEL))
                .setScheduledArrivalTime(resultSet.getTime(Columns.SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL))
                .setDriverName(resultSet.getString(Columns.DRIVER_NAME_COLUMN_LABEL))
                .setBusID(resultSet.getString(Columns.BUS_ID_COLUMN_LABEL))
                .build();
    }

    @Override
    public TripOffering mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
