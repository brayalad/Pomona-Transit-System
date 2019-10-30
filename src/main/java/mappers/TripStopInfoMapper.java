package mappers;

import database.TableModelType.Columns;
import models.TripStopInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TripStopInfoMapper implements TableDataModelMapper<TripStopInfo> {

    @Override
    public TripStopInfo mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return TripStopInfo.builder()
                .setTripNumber(resultSet.getLong(Columns.TRIP_NUMBER_COLUMN_LABEL))
                .setStopNumber(resultSet.getLong(Columns.STOP_NUMBER_COLUMN_LABEL))
                .setSequenceNumber(resultSet.getLong(Columns.SEQUENCE_NUMBER_COLUMN_LABEL))
                .setDrivingTime(resultSet.getLong(Columns.DRIVING_TIME_COLUMN_LABEL))
                .build();
    }

    @Override
    public TripStopInfo mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
