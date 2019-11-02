package mappers;

import database.TableModelType.Columns;
import models.TripOffering;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripOfferingMapper implements TableDataModelMapper<TripOffering> {

    @Override
    public TripOffering mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final TripOffering tripOffering = new TripOffering();

        tripOffering.setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL));
        tripOffering.setDate(resultSet.getDate(Columns.DATE_COLUMN_LABEL));
        tripOffering.setScheduledStartTime(resultSet.getTime(Columns.SCHEDULED_START_TIME_COLUMN_LABEL));
        tripOffering.setScheduledArrivalTime(resultSet.getTime(Columns.SCHEDULED_ARRIVAL_TIME_COLUMN_LABEL));
        tripOffering.setDriverName(resultSet.getString(Columns.DRIVER_NAME_COLUMN_LABEL));
        tripOffering.setBusID(resultSet.getString(Columns.BUS_ID_COLUMN_LABEL));

        return tripOffering;
    }

    @Override
    public TripOffering mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
