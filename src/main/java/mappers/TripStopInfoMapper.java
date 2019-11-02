package mappers;

import database.TableModelType.Columns;
import models.TripStopInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripStopInfoMapper implements TableDataModelMapper<TripStopInfo> {

    @Override
    public TripStopInfo mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final TripStopInfo tripStopInfo = new TripStopInfo();

        tripStopInfo.setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL));
        tripStopInfo.setStopNumber(resultSet.getLong(Columns.STOP_NUMBER_COLUMN_LABEL));
        tripStopInfo.setSequenceNumber(resultSet.getLong(Columns.SEQUENCE_NUMBER_COLUMN_LABEL));
        tripStopInfo.setDrivingTime(resultSet.getLong(Columns.DRIVING_TIME_COLUMN_LABEL));

        return tripStopInfo;
    }

    @Override
    public TripStopInfo mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
