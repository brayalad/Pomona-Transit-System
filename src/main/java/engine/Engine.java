package engine;

import database.DatabaseManager;
import ui.TextUserInterface;

public class Engine {
    private final TextUserInterface ui;
    private final DatabaseManager db;

    public Engine(final TextUserInterface ui, final DatabaseManager db) {
        this.ui = ui;
        this.db = db;
    }
}
