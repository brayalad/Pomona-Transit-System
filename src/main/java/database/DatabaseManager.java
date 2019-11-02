package database;

import java.sql.Connection;

public class DatabaseManager {
    private final Connection db;
    private final DatabaseMapper mapper;


    public DatabaseManager(final Connection db, final DatabaseMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }
}
