import java.io.PrintStream;
import java.sql.*;

import java.text.ParseException;

import java.text.SimpleDateFormat;

import java.util.*;


public class Main {
    private static final String CON_STR_FORMAT = "jdbc:%s://%s:%s/%s?user=%s&password=%s";

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
                                                                "WHERE T.StartLocationName LIKE '%s' AND " +
                                                                "T.DestinationName LIKE '%s' AND " +
                                                                "TRO.Date = '%s' AND " +
                                                                "T.TripNumber = TRO.TripNumber " +
                                                                "ORDER BY ScheduledStartTime ";

    private static final String DELETE_TRIP_OFFERING_QUERY_FORMAT = "DELETE TripOffering " +
                                                                    "WHERE TripNumber = '%s' AND " +
                                                                    "Date = '%s' AND " +
                                                                    "ScheduledStartTime = '%s'";

    private static final String ADD_TRIP_OFFERING_QUERY_FORMAT = "INSERT INTO TripOffering VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";

    private static final String CHANGE_DRIVER_QUERY_FORMAT = "UPDATE TripOffering " +
                                                             "SET DriverName = '%s' " +
                                                             "WHERE TripNumber = '%s' AND " +
                                                             "Date = '%s' AND " +
                                                             "ScheduledStartTime = '%s'";

    private static final String CHANGE_BUS_QUERY_FORMAT = "UPDATE TripOffering " +
                                                          "SET BusID = '%s' " +
                                                          "WHERE TripNumber = '%s' AND " +
                                                          "Date = '%s' AND " +
                                                          "ScheduledStartTime = '%s'";

    private static final String DISPLAY_TRIP_STOPS_QUERY_FORMAT = "SELECT * " +
                                                                  "FROM TripStopInfo " +
                                                                  "WHERE TripNumber = '%s' " +
                                                                  "ORDER BY SequenceNumber ";

    private static final String DISPLAY_WEEKLY_QUERY_FORMAT = "SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +
                                                              "FROM TripOffering " +
                                                              "WHERE DriverName LIKE '%s' " +
                                                              "AND Date = '%s' " +
                                                              "Order By ScheduledStartTime ";

    private static final String ADD_DRIVER_QUERY_FORMAT = "INSERT INTO Driver VALUES ('%s', '%s')";

    private static final String ADD_BUS_QUERY_FORMAT = "INSERT INTO Bus VALUES ('%s', '%s', '%s')";

    private static final String DELETE_BUS_QUERY_FORMAT = "DELETE Bus " +
                                                          "WHERE BusID = '%s'";

    private static final String INSERT_TRIP_DATA_QUERY_FORMAT = "INSERT INTO ActualTripInfo VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s') ";

    private static final Scanner input = new Scanner(System.in);

    private static final Map<String, PreparedStatement> pstmts = new HashMap<>();

    private static Statement statement;

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

    private static void run() {
        try {
            String input;
             do {
                 input = getInput("Command: ");
                 switch (input) {
                    case "1":
                        displaySchedule();
                        break;

                    case "2":
                        deleteTripOffering();
                        break;

                    case "3":
                        addTripOffering();
                        break;

                    case "4":
                        changeDriver();
                        break;

                    case "5":
                        changeBus();
                        break;

                    case "6":
                        displayTripStops();
                        break;

                    case "7":
                        displayWeekly();
                        break;

                    case "8":
                        addDriver();
                        break;

                    case "9":
                        addBus();
                        break;

                    case "10":
                        deleteBus();
                        break;

                    case "11":
                        insertTripData();
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

    private static void displaySchedule() throws SQLException{
        String startLocationName = getInput("Start Location Name: ");
        String destinationName = getInput("Destination Name: ");
        String date = getInput("Date: ");

        String query = String.format(DISPLAY_SCHEDULE_QUERY_FORMAT, startLocationName, destinationName, date);

        try{
            ResultSet resultSet = statement.executeQuery(query);

            displayResults(resultSet);
        } catch (SQLException e) {
            throw new SQLException(String.format("No schedule from %s to %s on %s", startLocationName, destinationName, date));
        }
    }

//Delete a trip offering specified by Trip#, Date, and ScheduledStartTime

    private static void deleteTripOffering() throws SQLException{
        String tripNumber = getInput("Start Trip Number: ");
        String date = getInput("Date: ");
        String scheduledStartTime = getInput("Scheduled Start Time: ");

        String query = String.format(DELETE_TRIP_OFFERING_QUERY_FORMAT, tripNumber, date, scheduledStartTime);

        int result = statement.executeUpdate(query);

        if(result == 0) throw new SQLException(String.format("No Trip Offering with Trip Number: %s on %s starting at %s", tripNumber, date, scheduledStartTime));

        println("Successfully deleted Trip Offering");
    }

//Add a set of trip offerings assuming the values of all attributes are given

    private static void addTripOffering() throws SQLException {
        Set<String> queries = new HashSet<>();

        String input;
        do {
            String tripNo = getInput("Trip Number: ");
            String date = getInput("Date: ");
            String startTime = getInput("Scheduled Start Time: ");
            String arrivalTime = getInput("Scheduled Arrival Time: ");
            String driver = getInput("Driver Name: ");
            String bus = getInput("Bus ID: ");

            String query = String.format(ADD_TRIP_OFFERING_QUERY_FORMAT, tripNo, date, startTime, arrivalTime, driver, bus);

            queries.add(query);

            input = getInput("'YES' to add another trip ('NO' to continue): ");
        } while(!input.equalsIgnoreCase("no"));

        for(String query : queries) {
            statement.execute(query);
        }

        print("Successfully added all Trip Offerings");
    }

//- Change the driver for a given Trip offering

    private static void changeDriver() throws SQLException {
        String driver = getInput("New Driver Name: ");
        String tripNo = getInput("Start Trip Number: ");
        String date = getInput("Date: ");
        String startTime = getInput("Scheduled Start Time: ");

        String query = String.format(CHANGE_DRIVER_QUERY_FORMAT, driver, tripNo, date, startTime);

        int result = statement.executeUpdate(query);

        if(result == 0) throw new SQLException(String.format("No Trip Offering with Trip Number: %s on %s starting at %s", tripNo, date, startTime));

        println("Successfully updated Driver");
    }

//Change the bus for a given Trip offering

    private static void changeBus() throws SQLException{
        String bus = getInput("New Bus Number: ");
        String tripNo = getInput("Start Trip Number: ");
        String date = getInput("Date: ");
        String startTime = getInput("Scheduled Start Time: ");

        String query = String.format(CHANGE_BUS_QUERY_FORMAT, bus, tripNo, date, startTime);

        try{
            int result = statement.executeUpdate(query);

            if(result == 0) throw new SQLException(String.format("No Trip Offering with Trip Number: %s on %s starting at %s", tripNo, date, startTime));

            println("Successfully updated Bus");
        } catch (SQLException e){
            throw new SQLException("No such Trip Offering or Bus Number in database");
        }
    }

    private static void displayTripStops() throws SQLException{
        String tripNo = getInput("Trip Number: ");

        String query = String.format(DISPLAY_TRIP_STOPS_QUERY_FORMAT, tripNo);

//get table data

        try{
            ResultSet rs = statement.executeQuery(query);
            displayResults(rs);
        } catch (SQLException e) {
            throw new SQLException(String.format("Trip Number %s does not exist.", tripNo));
        }
    }

    private static void displayWeekly() throws SQLException, ParseException {
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
            String query = String.format(DISPLAY_WEEKLY_QUERY_FORMAT, driver, dateStr);

            ResultSet rs = statement.executeQuery(query);
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

    private static void addDriver() throws SQLException{
        String driver = getInput("Driver name: ");
        String phone = getInput("Phone number: ");

        String query = String.format(ADD_DRIVER_QUERY_FORMAT, driver, phone);

        statement.execute(query);

        println("Successfully added a new Driver");
    }

    private static void addBus() throws SQLException{
        String bus = getInput("Bus ID: ");
        String model = getInput("Bus model: ");
        String year = getInput("Bus year: ");

        String query = String.format(ADD_BUS_QUERY_FORMAT, bus, model, year);

        statement.execute(query);

        println("Successfully added a new Bus");
    }

    private static void deleteBus() throws SQLException{
        String bus = getInput("Bus ID: ");

        String query = String.format(DELETE_BUS_QUERY_FORMAT, bus);

        int result = statement.executeUpdate(query);

        if(result == 0) throw new SQLException(String.format("No Bus with BusID %s", bus));

        println("Successfully deleted");
    }

    private static void insertTripData() throws SQLException{
        String tripNo = getInput("Trip Number: ");
        String date = getInput("Date: ");
        String startTime = getInput("Scheduled Start Time: ");
        String stop = getInput("Stop Number: ");
        String arrivalTime = getInput("Scheduled Arrival Time: ");
        String actualStart = getInput("Actual Start Time: ");
        String actualArrival = getInput("Actual Arrival Time: ");
        String passIn = getInput("Passengers in: ");
        String passOut = getInput("Passengers out: ");

        String query = String.format(INSERT_TRIP_DATA_QUERY_FORMAT, tripNo, date, startTime, stop, arrivalTime, actualStart, actualArrival, passIn, passOut);

        statement.execute(query);

        println("Successfully recorded data");
    }

    private static void connectToDatabase(){
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

            statement = DriverManager.getConnection(conStr).createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Connecting to Database Failed.");
        }
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
        System.err.printf("Error: %s", e.getLocalizedMessage());
    }
}

/*
        Class.forName("com.mysql.cj.jdbc.Driver");
        String driver = "mysql";
        String dbName = "pomona_transit_system";
        String hostname = "pomona-transit-system-mysql-database.cpxceyfhtxu5.us-west-1.rds.amazonaws.com";
        String port = "3306";
        String user = "admin";
        String password = "B1l9a9v8?";





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
                                                                "WHERE T.StartLocationName LIKE '%s' AND " +
                                                                "T.DestinationName LIKE '%s' AND " +
                                                                "TRO.Date = '%s' AND " +
                                                                "T.TripNumber = TRO.TripNumber " +
                                                                "ORDER BY ScheduledStartTime ";

    private static final String DELETE_TRIP_OFFERING_QUERY_FORMAT = "DELETE TripOffering " +
                                                                    "WHERE TripNumber = '%s' AND " +
                                                                    "Date = '%s' AND " +
                                                                    "ScheduledStartTime = '%s'";

    private static final String ADD_TRIP_OFFERING_QUERY_FORMAT = "INSERT INTO TripOffering VALUES ('%s', '%s', '%s', '%s', '%s', '%s')";

    private static final String CHANGE_DRIVER_QUERY_FORMAT = "UPDATE TripOffering " +
                                                             "SET DriverName = '%s' " +
                                                             "WHERE TripNumber = '%s' AND " +
                                                             "Date = '%s' AND " +
                                                             "ScheduledStartTime = '%s'";

    private static final String CHANGE_BUS_QUERY_FORMAT = "UPDATE TripOffering " +
                                                          "SET BusID = '%s' " +
                                                          "WHERE TripNumber = '%s' AND " +
                                                          "Date = '%s' AND " +
                                                          "ScheduledStartTime = '%s'";

    private static final String DISPLAY_TRIP_STOPS_QUERY_FORMAT = "SELECT * " +
                                                                  "FROM TripStopInfo " +
                                                                  "WHERE TripNumber = '%s' " +
                                                                  "ORDER BY SequenceNumber ";

    private static final String DISPLAY_WEEKLY_QUERY_FORMAT = "SELECT TripNumber, Date, ScheduledStartTime, ScheduledArrivalTime, BusID " +
                                                              "FROM TripOffering " +
                                                              "WHERE DriverName LIKE '%s' " +
                                                              "AND Date = '%s' " +
                                                              "Order By ScheduledStartTime ";

    private static final String ADD_DRIVER_QUERY_FORMAT = "INSERT INTO Driver VALUES ('%s', '%s')";

    private static final String ADD_BUS_QUERY_FORMAT = "INSERT INTO Bus VALUES ('%s', '%s', '%s')";

    private static final String DELETE_BUS_QUERY_FORMAT = "DELETE Bus " +
                                                          "WHERE BusID = '%s'";

    private static final String INSERT_TRIP_DATA_QUERY_FORMAT = "INSERT INTO ActualTripInfo VALUES('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s') ";


 */