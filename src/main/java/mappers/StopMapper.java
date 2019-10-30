package mappers;

import database.TableModelType.Columns;
import models.Stop;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StopMapper implements TableDataModelMapper<Stop> {


    @Override
    public Stop mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return Stop.builder()
                .setStopNumber(resultSet.getLong(Columns.STOP_NUMBER_COLUMN_LABEL))
                .setStopAddress(resultSet.getString(Columns.STOP_ADDRESS_COLUMN_LABEL))
                .build();
    }

    @Override
    public Stop mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
