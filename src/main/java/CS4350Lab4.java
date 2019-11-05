import java.sql.*;

import java.sql.Date;
import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.*;


public class Main {
    private static final String CON_STR_FORMAT = "jdbc:%s://%s:%s/%s?user=%s&password=%s";

    private static final String NO_RESULT_FORMAT = "No result for input: \n %s";

    private static final String MENU =  " 1)\tDisplay a Schedule\n" +
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
                                        " q)\tExit program\n";

    private static final String DISPLAY_SCHEDULE_QUERY_FORMAT = "SELECT TRO.ScheduledStartTime, TRO.ScheduledArrivalTime, TRO.DriverName, TRO.BusID " +
                                                                "FROM TripOffering AS TRO, Trip  AS T " +
                                                                "WHERE T.StartLocationName LIKE ? AND " +
                                                                "T.DestinationName LIKE ? AND " +
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
                                                              "WHERE DriverName LIKE ? " +
                                                              "AND Date = ? " +
                                                              "ORDER BY ScheduledStartTime ";

    private static final String ADD_DRIVER_QUERY_FORMAT = "INSERT INTO Driver VALUES (?, ?)";

    private static final String ADD_BUS_QUERY_FORMAT = "INSERT INTO Bus VALUES (?, ?, ?)";

    private static final String DELETE_BUS_QUERY_FORMAT = "DELETE Bus " +
                                                          "WHERE BusID = ?";

    private static final String INSERT_TRIP_DATA_QUERY_FORMAT = "INSERT INTO ActualTripInfo VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String START_LOCATION_NAME = "Start Location Name: ";

    private static final String DESTINATION_NAME = "Destination Name: ";

    private static final String DATE = "Date (yyyy-[m]m-[d]d): ";

    private static final String SCHEDULED_START_TIME = "Scheduled Start Time: ";

    private static final String TRIP_NUMBER = "Trip Number: ";

    private static final String SCHEDULED_ARRIVAL_TIME = "Scheduled Arrival Time: ";

    private static final String DRIVER_NAME = "Driver Name: ";

    private static final String BUS_ID = "Bus ID: ";

    private static final String DRIVER_PHONE_NUMBER = "Phone number: ";

    private static final String BUS_MODEL = "Bus model: ";

    private static final String BUS_YEAR = "Bus year: ";

    private static final String STOP_NUMBER = "Stop Number: ";

    private static final String ACTUAL_START_TIME = "Actual Start Time: ";

    private static final String ACTUAL_ARRIVAL_TIME = "Actual Arrival Time: ";

    private static final String NUMBER_OF_PASSENGERS_IN = "Number of Passengers in: ";

    private static final String NUMBER_OF_PASSENGERS_OUT = "Number of Passengers out: ";

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            connectToDatabase();
            print(MENU);
            run();
        } catch (RuntimeException e) {
            printError(e);
            println("Going back to Main Menu.");
        }
    }

    private static void run(){
        Map<String, PreparedStatement> statements = null;
        try {
            statements = getPreparedStatements(connectToDatabase());
        } catch (SQLException e) {
            printError("Connection to database not made.");
        } finally {
            if(statements != null)
                run(statements);
            else
                println("Make sure database settings are correct.");
        }
    }

    private static void run(Map<String, PreparedStatement> statements) {
        try {
            String input;
             do {
                 input = getInput("Command: ");
                 switch (input) {
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

                    case "q":
                        println("Ending Program.");
                        break;

                    default:
                        println("Invalid input.");
                        break;
                }
            } while (!input.equals("q"));
        } catch (SQLException | ParseException e) {
            printError(e);
        }

        System.exit(0);
    }

    private static void displaySchedule(PreparedStatement statement) throws SQLException{
        Map<String, String> input = getInput(START_LOCATION_NAME, DESTINATION_NAME, DATE);

        statement.setString(1, input.get(START_LOCATION_NAME));
        statement.setString(2, input.get(DESTINATION_NAME));
        statement.setDate(3, Date.valueOf(input.get(DATE)));

        try{
            ResultSet resultSet = executeQuery(statement);
            displayResults(resultSet);
        } catch (SQLException e) {
            throw new SQLException(compileNoResultsMessage(input));
        }
    }

//Delete a trip offering specified by Trip#, Date, and ScheduledStartTime

    private static void deleteTripOffering(PreparedStatement statement) throws SQLException{
        Map<String, String> input = getInput(TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        statement.setLong(1, Long.parseLong(input.get(TRIP_NUMBER)));
        statement.setDate(2, Date.valueOf(input.get(DATE)));
        statement.setTime(3, Time.valueOf(input.get(SCHEDULED_START_TIME)));

        long result = executeUpdate(statement);

        if(result == 0) throw new SQLException(compileNoResultsMessage(input));

        println("Successfully deleted Trip Offering");
    }

//Add a set of trip offerings assuming the values of all attributes are given

    private static void addTripOffering(PreparedStatement statement) throws SQLException {
        String input;
        do {
            Map<String, String> inputs = getInput(TRIP_NUMBER, DATE, SCHEDULED_START_TIME, SCHEDULED_ARRIVAL_TIME, DRIVER_NAME, BUS_ID);

            statement.setLong(1, Long.parseLong(inputs.get(TRIP_NUMBER)));
            statement.setDate(2, Date.valueOf(inputs.get(DATE)));
            statement.setTime(3, Time.valueOf(inputs.get(SCHEDULED_START_TIME)));
            statement.setTime(4, Time.valueOf(inputs.get(SCHEDULED_ARRIVAL_TIME)));
            statement.setString(5, inputs.get(DRIVER_NAME));
            statement.setLong(6, Long.parseLong(inputs.get(BUS_ID)));

            long result = executeUpdate(statement);

            input = getInput("'YES' to add another trip ('NO' to continue): ");
        } while(!input.equalsIgnoreCase("no"));



        print("Successfully added all Trip Offerings");
    }

//- Change the driver for a given Trip offering

    private static void changeDriver(PreparedStatement statement) throws SQLException {
        Map<String, String> input = getInput(DRIVER_NAME, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        statement.setString(1 , input.get(DRIVER_NAME));
        statement.setLong(2, Long.parseLong(input.get(TRIP_NUMBER)));
        statement.setDate(3, Date.valueOf(input.get(DATE)));
        statement.setTime(4, Time.valueOf(input.get(SCHEDULED_START_TIME)));

        long result = executeUpdate(statement);

        if(result == 0) throw new SQLException(compileNoResultsMessage(input));

        println("Successfully updated Driver");
    }

//Change the bus for a given Trip offering

    private static void changeBus(PreparedStatement statement) throws SQLException{
        Map<String, String> input = getInput(BUS_ID, TRIP_NUMBER, DATE, SCHEDULED_START_TIME);

        statement.setLong(1, Long.parseLong(input.get(BUS_ID)));
        statement.setLong(2, Long.parseLong(input.get(TRIP_NUMBER)));
        statement.setDate(3, Date.valueOf(input.get(DATE)));
        statement.setTime(4, Time.valueOf(input.get(SCHEDULED_START_TIME)));


            long result = executeUpdate(statement);

            if(result == 0) throw new SQLException(compileNoResultsMessage(input));

            println("Successfully updated Bus");
    }

    private static void displayTripStops(PreparedStatement statement) throws SQLException{
        String tripNumber = getInput(TRIP_NUMBER);

        statement.setLong(1, Long.parseLong(tripNumber));

//get table data

        try{
            ResultSet rs = executeQuery(statement);
            displayResults(rs);
        } catch (SQLException e) {
            throw new SQLException(compileNoResultsMessage(tripNumber));
        }
    }

    private static void displayWeekly(PreparedStatement statement) throws SQLException, ParseException {
        String driver = getInput("Driver name: ");
        String dateStr = getInput("Date: ");

        //convert date string to calendar object so it can be incremented for the week
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy");

        Calendar date = new GregorianCalendar();

        date.setTime(df.parse(dateStr));

        for(int i = 0; i < 7; i++){
            //convert back to a string
            dateStr = df.format(date.getTime());
            //query for selected driver and date
            //String query = String.format(DISPLAY_WEEKLY_QUERY_FORMAT, driver, dateStr);

            statement.setString(1, driver);
            statement.setDate(2, Date.valueOf(dateStr));

            ResultSet rs = executeQuery(statement);
            ResultSetMetaData rsmd = rs.getMetaData();

            int colCount = rsmd.getColumnCount();

            //if first then print out the column names
            if(i == 0){
                println("----------------------Day 1----------------------------");

                //get column names to print
                for(int j = 1; j <= colCount; j++){
                    if(j == 1 || j == 3)
                        print(rsmd.getColumnName(j) + "\t");
                    else
                        print(rsmd.getColumnName(j) + "\t\t");
                }
                println("");
            }

            //print out rows
            while(rs.next()){
                for(int j = 1; j <= colCount; j++)
                    print(rs.getString(j) + "\t\t");

                println("");
            }

            rs.close();


        //increment date by 1 at the end
        date.add(Calendar.DATE, 1);

        //add separator for each day of the week

        if(i < 6)
            println("----------------------Day " + (i+2) + "----------------------------");
    }

        println("------------------------------------------------------");
    }

    private static void addDriver(PreparedStatement statement) throws SQLException{
        Map<String, String> input = getInput(DRIVER_NAME, DRIVER_PHONE_NUMBER);

        statement.setString(1, input.get(DRIVER_NAME));
        statement.setInt(2, Integer.parseInt(input.get(DRIVER_PHONE_NUMBER)));

        executeUpdate(statement);

        println("Successfully added a new Driver");
    }

    private static void addBus(PreparedStatement statement) throws SQLException{
        Map<String, String> input = getInput(BUS_ID, BUS_MODEL, BUS_YEAR);

        statement.setLong(1, Long.parseLong(input.get(BUS_ID)));
        statement.setString(2, input.get(BUS_MODEL));
        statement.setString(3, input.get(BUS_YEAR));

        executeUpdate(statement);

        println("Successfully added a new Bus");
    }

    private static void deleteBus(PreparedStatement statement) throws SQLException{
        String busID = getInput("Bus ID: ");

        statement.setLong(1, Long.parseLong(busID));

        long result = executeUpdate(statement);

        if(result == 0) throw new SQLException(compileNoResultsMessage(busID));

        println("Successfully deleted");
    }

    private static void insertTripData(PreparedStatement statement) throws SQLException{
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

        statement.setLong(1, Long.parseLong(input.get(TRIP_NUMBER)));
        statement.setDate(2, Date.valueOf(input.get(DATE)));
        statement.setTime(3, Time.valueOf(input.get(SCHEDULED_START_TIME)));
        statement.setLong(4, Long.parseLong(input.get(STOP_NUMBER)));
        statement.setTime(5, Time.valueOf(input.get(SCHEDULED_ARRIVAL_TIME)));
        statement.setTime(6, Time.valueOf(input.get(ACTUAL_START_TIME)));
        statement.setTime(7, Time.valueOf(input.get(ACTUAL_ARRIVAL_TIME)));
        statement.setLong(8, Long.parseLong(input.get(NUMBER_OF_PASSENGERS_IN)));
        statement.setLong(9, Long.parseLong(input.get(NUMBER_OF_PASSENGERS_OUT)));

        executeUpdate(statement);

        println("Successfully recorded data");
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

    private static Map<String, String> getStatementsFormats() throws SQLException {
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

    private static void displayResults(ResultSet rs) throws SQLException {
        //get column names to print
        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();
        for(int i = 1; i <= colCount; i++){
            print(rsmd.getColumnName(i) + "\t");
        }
        println("");

        //print out rows
        while(rs.next()){
            for(int i = 1; i <= colCount; i++)
                print(rs.getString(i) + "\t\t");
            println("");
        }
        rs.close();

        println("------------------------------------------------------");
    }

    private static ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    private static Map<PreparedStatement, Long> executeUpdates(Collection<PreparedStatement> statements) throws SQLException {
        Map<PreparedStatement, Long> results = new HashMap<>();

        for(PreparedStatement statement : statements)
            results.put(statement, executeUpdate(statement));

        return results;
    }

    private static long executeUpdate(PreparedStatement statement) throws SQLException {
        return statement.executeLargeUpdate();
    }

    private static Map<String, String> getInput(String ...args){
        Map<String, String> inputs = new HashMap<>(args.length);
        for(String arg : args)
            inputs.put(arg, getInput(arg));

        return inputs;
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

    private static void clearStatementParameters(Map<String, PreparedStatement> statements){
        statements.values().forEach(s -> s.clearParameters());
    }

}
