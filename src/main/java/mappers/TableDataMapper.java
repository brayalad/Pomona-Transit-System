package mappers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.TableModelType;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class TableDataMapper {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<TableModelType, TableDataModelMapper> mappers;

    public TableDataMapper(final Map<TableModelType, TableDataModelMapper> mappers) {
        this.mappers = mappers;
    }

    @SuppressWarnings("unchecked")
    public <T extends TableDataModel> T mapRow(final ResultSet resultSet, final Class<T> clazz) throws SQLException {
        return (T) mapRow(resultSet, TableModelType.getTypeToClassMap().get(clazz));
    }

    public TableDataModel mapRow(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (TableDataModel) mappers.get(type).mapRow(resultSet);
    }

    public ActualTripStopInfo mapRowToActualTripStopInfo(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (ActualTripStopInfo) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public Bus mapRowBus(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (Bus) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public Driver mapRowToDriver(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (Driver) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public Stop mapRowToStop(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (Stop) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public Trip mapRowToTrip(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (Trip) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public TripOffering mapRowToTripOffering(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (TripOffering) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

    public TripStopInfo mapRowToTripStopInfo(final ResultSet resultSet, final TableModelType type) throws SQLException {
        return (TripStopInfo) mapRow(resultSet, TableModelType.ACTUAL_TRIP_STOP_INFO);
    }

}
