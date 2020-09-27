package Models;

import Main.database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentDatabase {
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> weekAppointments = FXCollections.observableArrayList();

        public static ObservableList<Appointment> getAllAppointments () {
            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        LocalDate begin = LocalDate.now();
        try {
            Statement statement = database.getConn().createStatement();
            String query = "SELECT * FROM appointment where start >= '" + begin +"'order by start asc;";
            ResultSet results = statement.executeQuery(query);
            while(results.next()) {
                DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime starttime = results.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String start = starttime.format(formatTime);
                LocalDateTime endTime = results.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                String end = endTime.format(formatTime);

                Appointment appointment = new Appointment(
                        results.getInt("appointmentId"),
                        results.getInt("customerId"),
                        start,
                        end,
                        results.getString("title"),
                        results.getString("description"),
                        results.getString("location"),
                        results.getString("contact"));

                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

        public static ObservableList<Appointment> getMonthlyAppointments () {
           monthAppointments.clear();
           LocalDate begin = LocalDate.now();
           LocalDate end = LocalDate.now().plusDays(30);
            try {
                Statement statement = database.getConn().createStatement();
                String query = "SELECT * FROM appointment WHERE start >= '" + begin + "' AND start <= '" + end + "'order by start asc";
                ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime starttime = results.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    String start = starttime.format(formatTime);
                    LocalDateTime endTime = results.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    String endMonthTime = endTime.format(formatTime);
                    Appointment appointment = new Appointment(
                            results.getInt("appointmentId"),
                            results.getInt("customerId"),
                            start,
                            endMonthTime,
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"));
                    monthAppointments.add(appointment);
                }
                statement.close();
                return monthAppointments;
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                return null;
            }
        }

        public static ObservableList<Appointment> getWeeklyAppointments() {
            weekAppointments.clear();
            LocalDate begin = LocalDate.now();
            LocalDate end = LocalDate.now().plusDays(6);
            try {
                Statement statement = database.getConn().createStatement();
                String query = "SELECT * FROM appointment WHERE start >= '" + begin + "' AND start <= '" + end + "'order by start asc";
                ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    DateTimeFormatter formatTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime starttime = results.getTimestamp("start").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    String start = starttime.format(formatTime);
                    LocalDateTime endTime = results.getTimestamp("end").toLocalDateTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
                    String endWeekTime = endTime.format(formatTime);

                    Appointment wAppointment = new Appointment(
                            results.getInt("appointmentId"),
                            results.getInt("customerId"),
                            start,
                            endWeekTime,
                            results.getString("title"),
                            results.getString("description"),
                            results.getString("location"),
                            results.getString("contact"));
                    weekAppointments.add(wAppointment);
                }
                statement.close();
                return weekAppointments;
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
                return null;
            }
        }

        public static Appointment appointmentIn15Min() {
            Appointment appointment;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime now15 = now.plusMinutes(15);
            String user = UserDatabase.getCurrentUser().getUsername();
            try {
                Statement statement = database.getConn().createStatement();
                String query = "select * from appointment where start > now() and start <= date_add(now(),interval 15 minute) and userID = 1";
                ResultSet results = statement.executeQuery(query);
                System.out.println(query);
                if(results.next()) {
                    appointment = new Appointment(results.getInt("appointmentId"), results.getInt("customerId"), results.getString("start"),
                            results.getString("end"), results.getString("title"), results.getString("description"),
                            results.getString("location"), results.getString("contact"));
                    System.out.println(appointment);
                    return appointment;
                }
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
            return null;
        }

        public static boolean addAppointment(int id, String type, String contact, String location, String date, String time) {

            String tsStart = createTimeStamp(date, time, location, true);
            String tsEnd = createTimeStamp(date, time, location, false);
            try {
                Statement statement = database.getConn().createStatement();
                String query = "INSERT INTO appointment SET customerId='" + id + "', title='" + type + "', description='" + type + "', type='" + "" + "', contact='" + contact + "', userId='" + 1 + "', location='" + location + "', start='" + tsStart + "', end='" +
                        tsEnd + "', url='', createDate=NOW(), createdBy='', lastUpdate=NOW(), lastUpdateBy=''";
                int update = statement.executeUpdate(query);
                if(update == 1) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
            return false;
        }

        public static boolean updateAppointment(int id, String type, String contact, String location, String date, String time) {

            String tsStart = createTimeStamp(date, time, location, true);
            String tsEnd = createTimeStamp(date, time, location, false);
            try {
                Statement statement = database.getConn().createStatement();
                String query = "UPDATE appointment SET title='" + type + "', description='" + type + "', contact='" +
                        contact + "', location='" + location + "', start='" + tsStart + "', end='" + tsEnd + "' WHERE " +
                        "appointmentId='" + id + "'";
                int update = statement.executeUpdate(query);
                System.out.println(query);
                if(update == 1) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
            return false;
        }

        public static boolean overlappingAppointment(int id, String location, String date, String time) {
            String start = createTimeStamp(date, time, location, true);
            try {
                Statement statement = database.getConn().createStatement();
                String query = "SELECT * FROM appointment WHERE start = '" + start + "' AND location = '" + location + "'";
                ResultSet results = statement.executeQuery(query);
                if(results.next()) {
                    if(results.getInt("appointmentId") == id) {
                        statement.close();
                        return false;
                    }
                    statement.close();
                    return true;
                } else {
                    statement.close();
                    return false;
                }
            } catch (SQLException e) {
                System.out.println("SQLExcpection: " + e.getMessage());
                return true;
            }
        }

        public static boolean deleteAppointment(int id) {
            try {
                Statement statement = database.getConn().createStatement();
                String query = "DELETE FROM appointment WHERE appointmentId = " + id;
                int update = statement.executeUpdate(query);
                if(update == 1) {
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("SQLException: " + e.getMessage());
            }
            return false;
        }

        public static String createTimeStamp(String date, String time, String location, boolean startMode) {
        String h = time.split(":")[0];
        int rawHours = Integer.parseInt(h);
        if(rawHours < 9) {
            rawHours += 12;
        }
        if(!startMode) {
            rawHours += 1;
        }
        String rawD = String.format("%s %02d:%s", date, rawHours, "00");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm");
        LocalDateTime locDatTim = LocalDateTime.parse(rawD, df);
        ZoneId zid;
        if(location.equals("New York")) {
            zid = ZoneId.of("America/New_York");
        } else if(location.equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
        } else {
            zid = ZoneId.of("Europe/London");
        }
        ZonedDateTime zdt = locDatTim.atZone(zid);
        ZonedDateTime utcDate = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        locDatTim = utcDate.toLocalDateTime();
        Timestamp ts = Timestamp.valueOf(locDatTim);
        return ts.toString();
    }

    }
