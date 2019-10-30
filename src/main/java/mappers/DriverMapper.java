package mappers;

import database.TableModelType.Columns;
import models.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverMapper implements TableDataModelMapper<Driver> {

    @Override
    public Driver mapRow(final ResultSet resultSet, final int i) throws SQLException {
        return Driver.builder()
                .setDriversName(resultSet.getString(Columns.DRIVER_NAME_COLUMN_LABEL))
                .setDriverTelephoneNumber(resultSet.getString(Columns.DRIVER_TELEPHONE_NUMBER_COLUMN_LABEL))
                .build();
    }

    @Override
    public Driver mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
