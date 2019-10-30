package mappers;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface TableDataModelMapper<T> extends RowMapper<T> {

    T mapRow(ResultSet resultSet) throws SQLException;

}
