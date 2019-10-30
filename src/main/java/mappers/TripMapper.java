package mappers;

import database.TableModelType.Columns;
import models.Trip;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripMapper implements TableDataModelMapper<Trip> {

    @Override
    public Trip mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return Trip.builder()
                .setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL))
                .setStartLocationName(resultSet.getString(Columns.START_LOCATION_NAME_COLUMN_LABEL))
                .setDestinationName(resultSet.getString(Columns.DESTINATION_NAME_COLUMN_LABEL))
                .build();
    }

    @Override
    public Trip mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
