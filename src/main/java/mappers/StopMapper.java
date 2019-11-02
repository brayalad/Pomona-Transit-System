package mappers;

import database.TableModelType.Columns;
import models.Stop;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StopMapper implements TableDataModelMapper<Stop> {


    @Override
    public Stop mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Stop stop = new Stop();

        stop.setStopNumber(resultSet.getLong(Columns.STOP_NUMBER_COLUMN_LABEL));
        stop.setStopAddress(resultSet.getString(Columns.STOP_ADDRESS_COLUMN_LABEL));

        return stop;
    }

    @Override
    public Stop mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
