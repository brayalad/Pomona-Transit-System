package mappers;

import database.TableModelType.Columns;
import models.Driver;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DriverMapper implements TableDataModelMapper<Driver> {

    @Override
    public Driver mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Driver driver = new Driver();

        driver.setDriversName(resultSet.getString(Columns.DRIVER_NAME_COLUMN_LABEL));
        driver.setDriverTelephoneNumber(resultSet.getString(Columns.DRIVER_TELEPHONE_NUMBER_COLUMN_LABEL));

        return driver;
    }

    @Override
    public Driver mapRow(final ResultSet resultSet) throws SQLException {
        return mapRow(resultSet, 0);
    }
}
