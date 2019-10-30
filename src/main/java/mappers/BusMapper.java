package mappers;

import database.TableModelType.Columns;
import models.Bus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class BusMapper implements TableDataModelMapper<Bus> {

    @Override
    public Bus mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return Bus.builder()
                .setBuildID(resultSet.getString(Columns.BUS_ID_COLUMN_LABEL))
                .setModel(resultSet.getString(Columns.MODEL_COLUMN_LABEL))
                .setYear(Year.parse(resultSet.getString(Columns.YEAR_COLUMN_LABEL)))
                .build();
    }

    @Override
    public Bus mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
