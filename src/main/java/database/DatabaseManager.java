package database;

import mappers.TableDataMapper;
import mappers.TableDataModelMapper;

import java.sql.Connection;

public class DatabaseManager {
    private final Connection db;
    private final TableDataMapper mapper;


    public DatabaseManager(final Connection db, final TableDataMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }
}
