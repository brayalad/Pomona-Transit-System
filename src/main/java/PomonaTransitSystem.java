
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.sql.Time;

import java.time.LocalDate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;


public final class PomonaTransitSystem {

    private static final String MENU =  "\nMenu Options:\n" +
                                        " 1)\tDisplay a Schedule.\n" +
                                        " 2)\tDelete a Trip Offering.\n" +
                                        " 3)\tAdd a Trip Offering.\n" +
                                        " 4)\tChange a Driver.\n" +
                                        " 5)\tChange a Bus.\n" +
                                        " 6)\tDisplay Trip Stops.\n" +
                                        " 7)\tDisplay Weekly Schedule for Driver.\n" +
                                        " 8)\tAdd a Driver.\n" +
                                        " 9)\tAdd a Bus.\n" +
                                        "10)\tDelete a Bus.\n" +
                                        "11)\tInsert Actual Trip Info.\n" +
                                        "12)\tDisplay Options.\n" +
                                        "13)\tDisplay a Table.\n" +
                                        "14)\tExecute a Custom SQL Statement.\n" +
                                        " Q)\tExit program.\n";

    private static final String DISPLAY_TABLE_FORMAT = "SELECT * FROM %s";

    private static final List<String> TABLES = List.of("ActualTripStopInfo", "Bus", "Driver", "Stop", "Trip", "TripOffering", "TripStopInfo");

    private static final String DISPLAY_SCHEDULE_QUERY_FORMAT = "SELECT TRO.ScheduledStartTime, TRO.ScheduledArrivalTime, TRO.DriverName, TRO.BusID " +
                                                                "FROM TripOffering AS TRO, Trip  AS T " +
                                                                "WHERE T.StartLocationName = ? AND " +
                                                                "T.DestinationName = ? AND " +
                                                                "TRO.Date = ? AND " +
                                                                "T.TripNumber = TRO.TripNumber " +
                                                                "ORDER BY ScheduledStartTime";

    private static final String DELETE_TRIP_OFFERING_QUERY_FORMAT = "DELETE FROM TripOffering " +
                                                                    "WHERE TripNumber = ? AND " +
                                                                    "Date = ? AND " +
                                                                    "ScheduledStartTime = ?";

    private static final String ADD_TRIP_OFFERING_QUERY_FORMAT = "INSERT INTO TripOffering VALUES (?, ?, ?, ?, ?, ?)";

    private static final String CHANGE_DRIVER_QUERY_FORMAT = "UPDATE TripOffering " +
                                                             "SET DriverName = ? " +
                                                             "WHERE TripNumber = ? AND " +
                                                             "Date = ? AND " +
                                                             "ScheduledStartTime = ?";

    private static final String CHANGE_BUS_QUERY_FORMAT = "UPDATE TripOffering " +
                                                          "SET BusID = ? " +
                                                          "WHERE TripNumber = ? AND " +
                                                          "Date = ? AND " +
                                                          "ScheduledStartTime = ?";

    private static final String DISPLAY_TRIP_STOPS_QUERY_FORMAT = "SELECT * " +
                                                                  "FROM TripStopInfo " +
                                                                  "WHERE TripNumber = ? " +
                                                                  "ORDER BY SequenceNumber ";

    private static final String DISPLAY_WEEKLY_QUERY_FORMAT = "SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +
                                                              "FROM TripOffering " +
                                                              "WHERE DriverName = ? " +
                                                              "AND Date = ? " +
                                                              "ORDER BY ScheduledStartTime ";

    private static final String ADD_DRIVER_QUERY_FORMAT = "INSERT INTO Driver VALUES (?, ?)";

    private static final String ADD_BUS_QUERY_FORMAT = "INSERT INTO Bus VALUES (?, ?, ?)";

    private static final String DELETE_BUS_QUERY_FORMAT = "DELETE FROM Bus " +
                                                          "WHERE BusID = ?";

    private static final String INSERT_TRIP_DATA_QUERY_FORMAT = "INSERT INTO ActualTripStopInfo VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final Map<String, String> STATEMENTS = Map.ofEntries(
            Map.entry("DISPLAY_SCHEDULE", DISPLAY_SCHEDULE_QUERY_FORMAT),
            Map.entry("DELETE_TRIP_OFFERING", DELETE_TRIP_OFFERING_QUERY_FORMAT),
            Map.entry("ADD_TRIP_OFFERING", ADD_TRIP_OFFERING_QUERY_FORMAT),
            Map.entry("CHANGE_DRIVER", CHANGE_DRIVER_QUERY_FORMAT),
            Map.entry("CHANGE_BUS", CHANGE_BUS_QUERY_FORMAT),
            Map.entry("DISPLAY_TRIP_STOPS", DISPLAY_TRIP_STOPS_QUERY_FORMAT),
            Map.entry("DISPLAY_WEEKLY", DISPLAY_WEEKLY_QUERY_FORMAT),
            Map.entry("ADD_DRIVER", ADD_DRIVER_QUERY_FORMAT),
            Map.entry("ADD_BUS", ADD_BUS_QUERY_FORMAT),
            Map.entry("DELETE_BUS", DELETE_BUS_QUERY_FORMAT),
            Map.entry("INSERT_TRIP_DATA", INSERT_TRIP_DATA_QUERY_FORMAT)
    );

    private static final String START_LOCATION_NAME = "Start Location Name: ";

    private static final String DESTINATION_NAME = "Destination Name: ";

    private static final String DATE = "Date (yyyy-[m]m-[d]d): ";

    private static final String SCHEDULED_START_TIME = "Scheduled Start Time (hh:mm:ss): ";

    private static final String TRIP_NUMBER = "Trip Number: ";

    private static final String SCHEDULED_ARRIVAL_TIME = "Scheduled Arrival Time (hh:mm:ss): ";

    private static final String DRIVER_NAME = "Driver Name: ";

    private static final String BUS_ID = "Bus ID: ";

    private static final String DRIVER_PHONE_NUMBER = "Phone number: ";

    private static final String BUS_MODEL = "Bus model: ";

    private static final String BUS_YEAR = "Bus year: ";

    private static final String STOP_NUMBER = "Stop Number: ";

    private static final String ACTUAL_START_TIME = "Actual Start Time (hh:mm:ss): ";

    private static final String ACTUAL_ARRIVAL_TIME = "Actual Arrival Time (hh:mm:ss): ";

    private static final String NUMBER_OF_PASSENGERS_IN = "Number of Passengers in: ";

    private static final String NUMBER_OF_PASSENGERS_OUT = "Number of Passengers out: ";

    private static final String ENTER_INPUT_FORMAT = "Enter %s";

    private static final String ERROR_FORMAT = "Error: [%s]\n";

    private static final String NO_RESULT_FORMAT = "No result for input: \n %s";

    private static final Scanner input = new Scanner(System.in);

    public static void main(final String[] args) {
        try {
            run();
        } catch (RuntimeException e) {
            printError(e);
            println("Going back to Main Menu.");
            run();
        }
    }

    private static void run(){
        try (final Connection connection = connectToDatabase()){
            final Map<String, PreparedStatement> statements = getPreparedStatements(connection);
            if(statements != null && connection != null) {
                run(statements, connection);
            } else {
                throw new SQLException("Connection to database not made.");
            }
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void run(final Map<String, PreparedStatement> statements, final Connection connection) {
        try (final Statement statement = connection.createStatement()){
            String input;
             do {
                 print(MENU);
                 input = getInput("Option to Run Command: ").toUpperCase();
                 switch (input.toUpperCase()) {
                    case "1":
                        displayTripSchedule(statements.get("DISPLAY_SCHEDULE"));
                        break;

                    case "2":
                        deleteTripOffering(statements.get("DELETE_TRIP_OFFERING"));
                        break;

                    case "3":
                        addTripOffering(statements.get("ADD_TRIP_OFFERING"));
                        break;

                    case "4":
                        changeTripOfferingDriver(statements.get("CHANGE_DRIVER"));
                        break;

                    case "5":
                        changeTripOfferingBus(statements.get("CHANGE_BUS"));
                        break;

                    case "6":
                        displayTripStops(statements.get("DISPLAY_TRIP_STOPS"));
                        break;

                    case "7":
                        displayWeeklySchedule(statements.get("DISPLAY_WEEKLY"));
                        break;

                    case "8":
                        addDriver(statements.get("ADD_DRIVER"));
                        break;

                    case "9":
                        addBus(statements.get("ADD_BUS"));
                        break;

                    case "10":
                        deleteBus(statements.get("DELETE_BUS"));
                        break;

                    case "11":
                        insertTripData(statements.get("INSERT_TRIP_DATA"));
                        break;

                    case "12":
                        print(MENU);
                        break;

                     case "13":
                         displayTable(statement);
                        break;

                     case "14":
                         executeCustomStatement(statement);
                         break;

                    case "Q":
                        println("Ending Program.");
                        break;

                    default:
                        println("Invalid input.\n");
                        print(MENU);
                        break;
                }
                clearStatementParameters(statements);
            } while (!input.equalsIgnoreCase("Q"));
        } catch (SQLException e) {
            printError(e);
            run(statements, connection);
        }

        System.exit(0);
    }

    private static void displayTripSchedule(final PreparedStatement statement) {
        final Map<String, String> input = getInput(START_LOCATION_NAME, DESTINATION_NAME, DATE);

        try{
            statement.setString(1, input.get(START_LOCATION_NAME));
            statement.setString(2, input.get(DESTINATION_NAME));
            statement.setString(3, input.get(DATE));

            displayResults(executeQuery(statement));
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void deleteTripOffering(final PreparedStatement statement) {
        final Map<String, String> input = getInput(TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        try {
            statement.setLong(1, Long.parseLong(input.get(TRIP_NUMBER)));
            statement.setString(2, input.get(DATE));
            statement.setString(3, input.get(SCHEDULED_START_TIME));

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Successfully deleted Trip Offering");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void addTripOffering(final PreparedStatement statement) {
        try {
            String input;
            do {
                final Map<String, String> inputs = getInput(
                        TRIP_NUMBER,
                        DATE,
                        SCHEDULED_START_TIME,
                        SCHEDULED_ARRIVAL_TIME,
                        DRIVER_NAME,
                        BUS_ID
                );

                statement.setLong(1, Long.parseLong(inputs.get(TRIP_NUMBER)));
                statement.setDate(2, Date.valueOf(inputs.get(DATE)));
                statement.setTime(3, Time.valueOf(inputs.get(SCHEDULED_START_TIME)));
                statement.setTime(4, Time.valueOf(inputs.get(SCHEDULED_ARRIVAL_TIME)));
                statement.setString(5, inputs.get(DRIVER_NAME));
                statement.setLong(6, Long.parseLong(inputs.get(BUS_ID)));

                if (executeUpdate(statement) == 0) printError("Trip offer was not added.");

                input = getInput("'YES' to add another trip ('NO' to continue): ");
            } while (input.equalsIgnoreCase("YES"));

            println("Successfully added all Trip Offerings");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void changeTripOfferingDriver(final PreparedStatement statement) {
        final Map<String, String> input = getInput(DRIVER_NAME, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        try {
            statement.setString(1, input.get(DRIVER_NAME));
            statement.setInt(2, Integer.parseInt(input.get(TRIP_NUMBER)));
            statement.setString(3, input.get(DATE));
            statement.setString(4, input.get(SCHEDULED_START_TIME));

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Successfully updated Driver");
        } catch (SQLException e){
            printError(e);
        }
    }

    private static void changeTripOfferingBus(final PreparedStatement statement){
        final Map<String, String> input = getInput(BUS_ID, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        try {
            statement.setLong(1, Long.parseLong(input.get(BUS_ID)));
            statement.setLong(2, Long.parseLong(input.get(TRIP_NUMBER)));
            statement.setString(3, input.get(DATE));
            statement.setString(4, input.get(SCHEDULED_START_TIME));

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Bus changed.");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void displayTripStops(final PreparedStatement statement) {
        final String tripNumber = getInput(TRIP_NUMBER);

        try{
            statement.setLong(1, Long.parseLong(tripNumber));
            displayResults(executeQuery(statement));
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void displayWeeklySchedule(final PreparedStatement statement) {
        final Map<String, String> input = getInput(DRIVER_NAME, DATE);

        final LocalDate startDate = LocalDate.parse(input.get(DATE));
        for(int i = 0; i < 7; ++i) {
            final LocalDate currentDate = startDate.plusDays(i);
            try {
                statement.setString(1, input.get(DRIVER_NAME));
                statement.setString(2, currentDate.toString());

                final ResultSet resultSet = executeQuery(statement);

                if (i == 0) displayResultColumns(resultSet.getMetaData());

                displayResultSet(resultSet, currentDate.toString());
            } catch (SQLException e) {
                printError(e);
            }
        }
    }

    private static void addDriver(final PreparedStatement statement){
        final Map<String, String> input = getInput(DRIVER_NAME, DRIVER_PHONE_NUMBER);

        try {
            statement.setString(1, input.get(DRIVER_NAME));
            statement.setInt(2, Integer.parseInt(input.get(DRIVER_PHONE_NUMBER)));

            if(executeUpdate(statement) == 0) throw new SQLException("Driver not added.");

            println("Driver added.");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void addBus(final PreparedStatement statement) {
        final Map<String, String> input = getInput(BUS_ID, BUS_MODEL, BUS_YEAR);

        try {
            statement.setString(1, input.get(BUS_ID));
            statement.setString(2, input.get(BUS_MODEL));
            statement.setInt(3, Integer.parseInt(input.get(BUS_YEAR)));

            if(executeUpdate(statement) == 0) throw new SQLException("Bus not added.");

            println("Successfully added a new Bus");
        } catch (SQLException e){
            printError(e);
        }
    }

    private static void deleteBus(final PreparedStatement statement){
        final String busID = getInput(BUS_ID);

        try {
            statement.setString(1, busID);

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(busID));

            println("Successfully deleted");
        } catch (SQLException e){
            printError(e);
        }
    }

    private static void insertTripData(final PreparedStatement statement) {
        final Map<String, String> input = getInput(
                TRIP_NUMBER,
                DATE,
                SCHEDULED_START_TIME,
                STOP_NUMBER,
                SCHEDULED_ARRIVAL_TIME,
                ACTUAL_START_TIME,
                ACTUAL_ARRIVAL_TIME,
                NUMBER_OF_PASSENGERS_IN,
                NUMBER_OF_PASSENGERS_OUT
        );

        try {
            statement.setLong(1, Long.parseLong(input.get(TRIP_NUMBER)));
            statement.setDate(2, Date.valueOf(input.get(DATE)));
            statement.setTime(3, Time.valueOf(input.get(SCHEDULED_START_TIME)));
            statement.setLong(4, Long.parseLong(input.get(STOP_NUMBER)));
            statement.setTime(5, Time.valueOf(input.get(SCHEDULED_ARRIVAL_TIME)));
            statement.setTime(6, Time.valueOf(input.get(ACTUAL_START_TIME)));
            statement.setTime(7, Time.valueOf(input.get(ACTUAL_ARRIVAL_TIME)));
            statement.setLong(8, Long.parseLong(input.get(NUMBER_OF_PASSENGERS_IN)));
            statement.setLong(9, Long.parseLong(input.get(NUMBER_OF_PASSENGERS_OUT)));

            if(executeUpdate(statement) == 0) throw new SQLException("Data not added.");

            println("Data added");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void executeCustomStatement(final Statement statement){
        final String query = getInput("Statement: ");

        try {
            if (query.toUpperCase().startsWith("SELECT")) {
                displayResults(statement.executeQuery(query));
            } else if (
                    query.toUpperCase().startsWith("INSERT") ||
                    query.toUpperCase().startsWith("UPDATE") ||
                    query.toUpperCase().startsWith("DELETE")
            ) {
                if(executeUpdate(statement, query) == 0) throw new SQLException(compileNoResultsMessage(query));
            } else {
                throw new SQLException("Unsupported Statement.");
            }
        } catch (SQLException e){
            printError(e);
        }
    }

    private static Connection connectToDatabase(){
        if(System.getenv() == null) throw new RuntimeException("DB setting not set properly.");

        try {
            Class.forName(System.getenv("DB_DRIVER"));
            final String driver = System.getenv("DB_TYPE");
            final String dbName = System.getenv("DB_NAME");
            final String hostname = System.getenv("DB_HOST");
            final String port = System.getenv("DB_PORT");
            final String user = System.getenv("DB_USERNAME");
            final String password = System.getenv("DB_PASSWORD");

            return DriverManager.getConnection(String.format("jdbc:%s://%s:%s/%s", driver, hostname, port, dbName), user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Connecting to Database Failed.");
        }
    }

    private static Map<String, PreparedStatement> getPreparedStatements(final Connection connection) throws SQLException {
        final Map<String, PreparedStatement> preparedStatements = new HashMap<>();

        for(final Map.Entry<String, String> statement : STATEMENTS.entrySet())
            preparedStatements.put(statement.getKey(), connection.prepareStatement(statement.getValue()));

        return Map.copyOf(preparedStatements);
    }

    private static void displayTable(final Statement statement) throws SQLException {
        final int n = TABLES.size();

        String input;
        int index = Integer.MIN_VALUE;
        while(!(index <= (n + 1) && index > 0)) {
            println("Tables:");
            for(int i = 0; i <= n; ++i) {
                if (i == n) {
                    println(String.format("%d)\t%s", (n + 1), "Display All"));
                } else {
                    println(String.format("%d)\t%s", (i + 1), TABLES.get(i)));
                }
            }
            input = getInput("Table: ");

            if(input.equalsIgnoreCase("Q")) return;

            if(isNumeric(input))
                index = Integer.parseInt(input);

            if(index < 0 || index > (n + 1))
                printError("Invalid Input. Try Again.");
        }

        for (final String table : ((index == (n + 1)) ? TABLES : List.of((TABLES.get(index - 1)))))
            displayResults(executeQuery(statement, String.format(DISPLAY_TABLE_FORMAT, table)));
    }

    private static void displayResults(final ResultSet resultSet) throws SQLException {
        try (resultSet) {
            displayResultColumns(resultSet.getMetaData());
            displayResultSet(resultSet, null);
        }
    }

    private static void displayResultSet(final ResultSet resultSet, final String title) throws SQLException {
        final int colCount = resultSet.getMetaData().getColumnCount();

        if(title != null)
            print(String.format("\n-----------------------%s-------------------------------\n", title));

        while(resultSet.next()){
            for(int i = 1; i <= colCount; ++i)
                print(String.format("%s\t\t", resultSet.getString(i)));

            println();
        }
    }

    private static void displayResultColumns(final ResultSetMetaData resultSetMetaData) throws SQLException {
        final int colCount = resultSetMetaData.getColumnCount();

        for(int i = 1; i <= colCount; ++i)
            print(String.format("%s\t\t", resultSetMetaData.getColumnName(i)));

        print("\n------------------------------------------------------\n");
    }

    private static ResultSet executeQuery(final Statement statement, final String query) throws SQLException {
        return statement.executeQuery(query);
    }

    private static ResultSet executeQuery(final PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    private static long executeUpdate(final Statement statement, final String query) throws SQLException {
        return statement.executeUpdate(query);
    }

    private static long executeUpdate(final PreparedStatement statement) throws SQLException {
        return statement.executeLargeUpdate();
    }

    private static String getInput(){
        return input.nextLine().trim();
    }

    private static String getInput(final String s){
        print(String.format(ENTER_INPUT_FORMAT, s));
        return getInput();
    }

    private static Map<String, String> getInput(final String ...args){
        return Arrays.stream(args).collect(Collectors.toMap(arg -> arg, PomonaTransitSystem::getInput));
    }

    private static void print(final String s){
        System.out.print(s);
    }

    private static void println(){
        System.out.println();
    }

    private static void println(final String s){
        System.out.println(s);
    }

    private static void printError(final Exception e){
        printError(e.getLocalizedMessage());
    }

    private static void printError(final String s){
        System.err.printf(ERROR_FORMAT, s);
    }

    private static boolean isNumeric(final String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException | NullPointerException nfe){
            return false;
        }
    }

    private static String compileNoResultsMessage(final Map<String, String> input){
        return compileNoResultsMessage(input.toString());
    }

    private static String compileNoResultsMessage(final String input){
        return String.format(NO_RESULT_FORMAT, input);
    }

    private static void clearStatementParameters(final Map<String, PreparedStatement> statements){
        for (final PreparedStatement statement : statements.values()) {
            try {
                statement.clearParameters();
            } catch (SQLException e) {
                printError(e);
            }
        }
    }
}