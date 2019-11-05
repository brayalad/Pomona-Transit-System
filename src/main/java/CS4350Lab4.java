import java.sql.*;

import java.sql.Date;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class CS4350Lab4 {
    private static final String CON_STR_FORMAT = "jdbc:%s://%s:%s/%s?user=%s&password=%s";

    private static final String NO_RESULT_FORMAT = "No result for input: \n %s";

    private static final String MENU =  "Menu Options:\n" +
                                        " 1)\tDisplay a Schedule\n" +
                                        " 2)\tDelete a Trip Offering\n" +
                                        " 3)\tAdd a Trip Offering\n" +
                                        " 4)\tChange a Driver\n" +
                                        " 5)\tChange a Bus\n" +
                                        " 6)\tDisplay Trip Stops\n" +
                                        " 7)\tDisplay Weekly Schedule for Driver\n" +
                                        " 8)\tAdd a Driver\n" +
                                        " 9)\tAdd a Bus\n" +
                                        "10)\tDelete a Bus\n" +
                                        "11)\tInsert Actual Trip Info\n" +
                                        "12)\tDisplay all commands\n" +
                                        "13)\tDisplay a table\n" +
                                        "14)\tExecute custom statement\n" +
                                        " Q)\tExit program\n";

    private static final String DISPLAY_TABLE_FORMAT = "SELECT * FROM %s";

    private static final List<String> TABLES = List.of("ActualTripStopInfo", "Bus", "Driver", "Stop", "Trip", "TripOffering", "TripStopInfo");

    private static final String DISPLAY_SCHEDULE_QUERY_FORMAT = "SELECT TRO.ScheduledStartTime, TRO.ScheduledArrivalTime, TRO.DriverName, TRO.BusID " +
                                                                "FROM TripOffering AS TRO, Trip  AS T " +
                                                                "WHERE T.StartLocationName LIKE ? AND " +
                                                                "T.DestinationName = ? AND " +
                                                                "TRO.Date = ? AND " +
                                                                "T.TripNumber = TRO.TripNumber " +
                                                                "ORDER BY ScheduledStartTime ";

    private static final String DELETE_TRIP_OFFERING_QUERY_FORMAT = "DELETE TripOffering " +
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

    private static final String INSERT_TRIP_DATA_QUERY_FORMAT = "INSERT INTO ActualTripInfo VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            run();
        } catch (RuntimeException e) {
            printError(e);
            println("Going back to Main Menu.");
            run();
        }
    }

    private static void run(){
        Map<String, PreparedStatement> statements = null;
        try {
            statements = getPreparedStatements(connectToDatabase());
        } catch (SQLException e) {
            printError("Connection to database not made.");
        } finally {
            if(statements != null) {
                print(MENU);
                run(statements);
            } else {
                println("Make sure database settings are correct.");
            }
        }
    }

    private static void run(Map<String, PreparedStatement> statements) {
        try {
            String input;
             do {
                 input = getInput("Option to Run Command: ");
                 switch (input.toUpperCase()) {
                    case "1":
                        displaySchedule(statements.get("DISPLAY_SCHEDULE"));
                        break;

                    case "2":
                        deleteTripOffering(statements.get("DELETE_TRIP_OFFERING"));
                        break;

                    case "3":
                        addTripOffering(statements.get("ADD_TRIP_OFFERING"));
                        break;

                    case "4":
                        changeDriver(statements.get("CHANGE_DRIVER"));
                        break;

                    case "5":
                        changeBus(statements.get("CHANGE_BUS"));
                        break;

                    case "6":
                        displayTripStops(statements.get("DISPLAY_TRIP_STOPS"));
                        break;

                    case "7":
                        displayWeekly(statements.get("DISPLAY_WEEKLY"));
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
                         displayTable(statements.values().iterator().next().getConnection().createStatement());
                        break;
                     case "14":
                         executeCustomStatement(statements.values().iterator().next().getConnection().createStatement());
                         break;

                    case "Q":
                        println("Ending Program.");
                        break;

                    default:
                        println("Invalid input.");
                        break;
                }
                clearStatementParameters(statements);
                 if(!input.equalsIgnoreCase("Q")) print(MENU);
            } while (!input.equalsIgnoreCase("Q"));
        } catch (SQLException e) {
            printError(e);
            run(statements);
        }

        System.exit(0);
    }

    private static void displaySchedule(PreparedStatement statement) {
        Map<String, String> input = getInput(START_LOCATION_NAME, DESTINATION_NAME, DATE);
        try{
            statement.setString(1, input.get(START_LOCATION_NAME));
            statement.setString(2, input.get(DESTINATION_NAME));
            statement.setDate(3, Date.valueOf(input.get(DATE)));

            ResultSet resultSet = executeQuery(statement);
            displayResults(resultSet);
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void deleteTripOffering(PreparedStatement statement) {
        Map<String, String> input = getInput(TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        try {
            statement.setLong(1, Long.parseLong(input.get(TRIP_NUMBER)));
            statement.setDate(2, Date.valueOf(input.get(DATE)));
            statement.setTime(3, Time.valueOf(input.get(SCHEDULED_START_TIME)));

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Successfully deleted Trip Offering");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void addTripOffering(PreparedStatement statement) {
        try {
            String input;
            do {
                Map<String, String> inputs = getInput(TRIP_NUMBER, DATE, SCHEDULED_START_TIME, SCHEDULED_ARRIVAL_TIME, DRIVER_NAME, BUS_ID);

                statement.setLong(1, Long.parseLong(inputs.get(TRIP_NUMBER)));
                statement.setDate(2, Date.valueOf(inputs.get(DATE)));
                statement.setTime(3, Time.valueOf(inputs.get(SCHEDULED_START_TIME)));
                statement.setTime(4, Time.valueOf(inputs.get(SCHEDULED_ARRIVAL_TIME)));
                statement.setString(5, inputs.get(DRIVER_NAME));
                statement.setLong(6, Long.parseLong(inputs.get(BUS_ID)));

                if (executeUpdate(statement) == 0) printError("Trip offer was not added.");

                input = getInput("'YES' to add another trip ('NO' to continue): ");
            } while (input.equalsIgnoreCase("YES"));

            print("Successfully added all Trip Offerings");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void changeDriver(PreparedStatement statement) {
        Map<String, String> input = getInput(DRIVER_NAME, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

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


    private static void changeBus(PreparedStatement statement){
        Map<String, String> input = getInput(BUS_ID, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        try {
            statement.setLong(1, Long.parseLong(input.get(BUS_ID)));
            statement.setLong(2, Long.parseLong(input.get(TRIP_NUMBER)));
            statement.setString(3, input.get(DATE));
            statement.setString(4, input.get(SCHEDULED_START_TIME));

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Successfully updated Bus");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void displayTripStops(PreparedStatement statement) {
        String tripNumber = getInput(TRIP_NUMBER);

        try{
            statement.setLong(1, Long.parseLong(tripNumber));
            displayResults(executeQuery(statement));
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void displayWeekly(PreparedStatement statement) {
        Map<String, String> input = getInput(DRIVER_NAME, DATE);

        LocalDate startDate = LocalDate.parse(input.get(DATE));

        boolean columnsDisplayed = false;
        for(int i = 0; i < 7; ++i) {
            LocalDate currentDate = startDate.plusDays(i);
            try {
                statement.setString(1, input.get(DRIVER_NAME));
                statement.setString(2, currentDate.toString());

                ResultSet rs = executeQuery(statement);

                if (!columnsDisplayed) displayResultColumns(rs);
                columnsDisplayed = !columnsDisplayed ? true : true;

                displayResultSet(rs, currentDate.toString());
            } catch (SQLException e) {
                printError(e);
            }
        }
    }

    private static void addDriver(PreparedStatement statement){
        Map<String, String> input = getInput(DRIVER_NAME, DRIVER_PHONE_NUMBER);

        try {
            statement.setString(1, input.get(DRIVER_NAME));
            statement.setInt(2, Integer.parseInt(input.get(DRIVER_PHONE_NUMBER)));

            if(executeUpdate(statement) == 0) throw new SQLException("Driver not added.");

            println("Successfully added a new Driver");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void addBus(PreparedStatement statement) {
        Map<String, String> input = getInput(BUS_ID, BUS_MODEL, BUS_YEAR);

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

    private static void deleteBus(PreparedStatement statement){
        String busID = getInput("Bus ID: ");

        try {
            statement.setString(1, busID);

            if (executeUpdate(statement) == 0) throw new SQLException(compileNoResultsMessage(busID));

            println("Successfully deleted");
        } catch (SQLException e){
            printError(e);
        }
    }

    private static void insertTripData(PreparedStatement statement) {
        Map<String, String> input = getInput(
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

            println("Successfully recorded data");
        } catch (SQLException e) {
            printError(e);
        }
    }

    private static void executeCustomStatement(Statement statement){
        String query = getInput("Statement: ");

        try {
            if(query.toUpperCase().startsWith("SELECT")){
                displayResults(statement.executeQuery(query));
            } else if (query.toUpperCase().startsWith("INSERT") || query.startsWith("UPDATE") || query.startsWith("DELETE")){
                if(statement.executeUpdate(query) == 0) throw new SQLException(compileNoResultsMessage(query));
            } else {
                throw new SQLException("Invalid Statement.");
            }
        } catch (SQLException e){
            printError(e);
        }
    }

    private static Connection connectToDatabase(){
        if(System.getenv() == null) throw new RuntimeException("DB setting not set properly");

        try {
            Class.forName(System.getenv("DB_DRIVER"));
            String driver = System.getenv("DB_TYPE");
            String dbName = System.getenv("DB_NAME");
            String hostname = System.getenv("DB_HOST");
            String port = System.getenv("DB_PORT");
            String user = System.getenv("DB_USERNAME");
            String password = System.getenv("DB_PASSWORD");

            String conStr = String.format(CON_STR_FORMAT, driver, hostname, port, dbName, user, password);

            return DriverManager.getConnection(conStr);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Connecting to Database Failed.");
        }
    }

    private static Map<String, PreparedStatement> getPreparedStatements(Connection dbc) throws SQLException {
        Map<String, PreparedStatement> preparedStatements = new HashMap<>();

        for(Map.Entry<String, String> statement : getStatementsFormats().entrySet())
            preparedStatements.put(statement.getKey(), dbc.prepareStatement(statement.getValue()));

        return Map.copyOf(preparedStatements);
    }

    private static Map<String, String> getStatementsFormats() {
        Map<String, String> statements = new HashMap<>(11);

        statements.put("DISPLAY_SCHEDULE", DISPLAY_SCHEDULE_QUERY_FORMAT);
        statements.put("DELETE_TRIP_OFFERING", DELETE_TRIP_OFFERING_QUERY_FORMAT);
        statements.put("ADD_TRIP_OFFERING", ADD_TRIP_OFFERING_QUERY_FORMAT);
        statements.put("CHANGE_DRIVER", CHANGE_DRIVER_QUERY_FORMAT);
        statements.put("CHANGE_BUS", CHANGE_BUS_QUERY_FORMAT);
        statements.put("DISPLAY_TRIP_STOPS", DISPLAY_TRIP_STOPS_QUERY_FORMAT);
        statements.put("DISPLAY_WEEKLY", DISPLAY_WEEKLY_QUERY_FORMAT);
        statements.put("ADD_DRIVER", ADD_DRIVER_QUERY_FORMAT);
        statements.put("ADD_BUS", ADD_BUS_QUERY_FORMAT);
        statements.put("DELETE_BUS", DELETE_BUS_QUERY_FORMAT);
        statements.put("INSERT_TRIP_DATA", INSERT_TRIP_DATA_QUERY_FORMAT);

        return Map.copyOf(statements);
    }

    private static void displayTable(Statement statement) throws SQLException {
        int n = TABLES.size();
        String f = "%d)\t%s";

        int input;
        do{
            println("Tables:");
            for(int i = 0; i < n; ++i)
                println(String.format(f,(i+1), TABLES.get(i)));
            println(String.format(f, (n+1), "Display All"));

            input = Integer.parseInt(getInput("Table: "));
        } while(!(input <= (n + 1) && input > 0));

        for (String s : (input == (n + 1) ? TABLES : List.of((TABLES.get(input - 1)))))
            displayResults(statement.executeQuery(String.format(DISPLAY_TABLE_FORMAT, s)));
    }

    private static void displayResults(ResultSet rs) throws SQLException {
        displayResults(rs,null);
    }

    private static void displayResults(ResultSet rs, String title) throws SQLException {
        displayResultColumns(rs, title);
        displayResultSet(rs, title);
    }

    private static void displayResultSet(ResultSet rs) throws SQLException {
        displayResultSet(rs, null);
    }

    private static void displayResultSet(ResultSet rs, String title) throws SQLException {
        int colCount = rs.getMetaData().getColumnCount();

        if(title != null)
            print(String.format("\n-----------------------%s-------------------------------\n", title));

        while(rs.next()){
            for(int i = 1; i <= colCount; i++)
                print(rs.getString(i) + "\t\t");
            println("");
        }
    }

    private static void displayResultColumns(ResultSet rs) throws SQLException {
        displayResultColumns(rs, null);
    }

    private static void displayResultColumns(ResultSet rs, String title) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        if(title != null)
            print(String.format("\n-----------------------%s-------------------------------\n", title));

        for(int i = 1; i <= colCount; i++){
            print(rsmd.getColumnName(i) + "\t\t");
        }
        println("\n------------------------------------------------------\n");

    }

    private static ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    private static long executeUpdate(PreparedStatement statement) throws SQLException {
        return statement.executeLargeUpdate();
    }

    private static Map<String, String> getInput(String ...args){
       return Arrays.stream(args).collect(Collectors.toMap(arg -> arg, CS4350Lab4::getInput));
    }

    private static String getInput(){
        return input.nextLine().trim();
    }

    private static String getInput(String s){
        print(String.format("Enter %s", s));
        return getInput();
    }

    private static void print(String s){
        System.out.print(s);
    }

    private static void println(String s){
        System.out.println(s);
    }

    private static void printError(Exception e){
        printError(e.getLocalizedMessage());
    }

    private static void printError(String s){
        System.err.printf("Error: %s \n", s);
    }

    private static String compileNoResultsMessage(Map<String, String> input){
        return compileNoResultsMessage(input.toString());
    }

    private static String compileNoResultsMessage(String input){
        return String.format(NO_RESULT_FORMAT, input);
    }

    private static void clearStatementParameters(Map<String, PreparedStatement> statements) throws SQLException{
        for (PreparedStatement statement : statements.values()) statement.clearParameters();
    }
}