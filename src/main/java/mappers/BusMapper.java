package mappers;

import database.TableModelType.Columns;
import models.Bus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;

public class BusMapper implements TableDataModelMapper<Bus> {

    @Override
    public Bus mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Bus bus = new Bus();

        bus.setBuildID(resultSet.getString(Columns.BUS_ID_COLUMN_LABEL));
        bus.setModel(resultSet.getString(Columns.MODEL_COLUMN_LABEL));
        bus.setYear(Year.parse(resultSet.getString(Columns.YEAR_COLUMN_LABEL)));

        return bus;
    }

    @Override
    public Bus mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
