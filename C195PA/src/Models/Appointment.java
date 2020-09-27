package Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Appointment {

    private final SimpleIntegerProperty appointmentID = new SimpleIntegerProperty();
    private final SimpleIntegerProperty appointmentCustomerID = new SimpleIntegerProperty();
    private final SimpleStringProperty appointmentStart = new SimpleStringProperty();
    private final SimpleStringProperty appointmentEnd = new SimpleStringProperty();
    private final SimpleStringProperty appointmentTitle = new SimpleStringProperty();
    private final SimpleStringProperty appointmentDescription = new SimpleStringProperty();
    private final SimpleStringProperty appointmentLocation = new SimpleStringProperty();
    private final SimpleStringProperty appointmentContact = new SimpleStringProperty();

        public Appointment(int id, int custId, String start, String end, String title, String description, String location, String contact) {
            setAppointmentID(id);
            setAppointmentCustomerID(custId);
            setAppointmentStart(start);
            setAppointmentEnd(end);
            setAppointmentTitle(title);
            setAppointmentDescription(description);
            setAppointmentLocation(location);
            setAppointmentContact(contact);
        }


    public int getAppointmentID() {
            return appointmentID.get();
        }

        public SimpleIntegerProperty appointmentIDProperty() {
            return appointmentID;
        }

        public void setAppointmentID(int appointmentID) {
            this.appointmentID.set(appointmentID);
        }

        public int getAppointmentCustomerID() {
            return appointmentCustomerID.get();
        }

        public SimpleIntegerProperty appointmentCustomerIDProperty() {
            return appointmentCustomerID;
        }

        public void setAppointmentCustomerID(int appointmentCustomerID) {
            this.appointmentCustomerID.set(appointmentCustomerID);
        }

        public String getAppointmentStart() {
            return appointmentStart.get();
        }

        public SimpleStringProperty appointmentStartProperty() {
            return appointmentStart;
        }

        public void setAppointmentStart(String appointmentStart) {
            this.appointmentStart.set(appointmentStart);
        }

        public String getAppointmentEnd() {
            return appointmentEnd.get();
        }

        public SimpleStringProperty appointmentEndProperty() {
            return appointmentEnd;
        }

        public void setAppointmentEnd(String appointmentEnd) {
            this.appointmentEnd.set(appointmentEnd);
        }

        public String getAppointmentTitle() {
            return appointmentTitle.get();
        }

        public SimpleStringProperty appointmentTitleProperty() {
            return appointmentTitle;
        }

        public void setAppointmentTitle(String appointmentTitle) {
            this.appointmentTitle.set(appointmentTitle);
        }

        public String getAppointmentDescription() {
            return appointmentDescription.get();
        }

        public SimpleStringProperty appointmentDescriptionProperty() {
            return appointmentDescription;
        }

        public void setAppointmentDescription(String appointmentDescription) {
            this.appointmentDescription.set(appointmentDescription);
        }

        public String getAppointmentLocation() {
            return appointmentLocation.get();
        }

        public SimpleStringProperty appointmentLocationProperty() {
            return appointmentLocation;
        }

        public void setAppointmentLocation(String appointmentLocation) {
            this.appointmentLocation.set(appointmentLocation);
        }

        public String getAppointmentContact() {
            return appointmentContact.get();
        }

        public SimpleStringProperty appointmentContactProperty() {
            return appointmentContact;
        }

        public void setAppointmentContact(String appointmentContact) {
            this.appointmentContact.set(appointmentContact);
        }

    public StringProperty getAppointmentStartProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime ldt = LocalDateTime.parse(this.appointmentStart.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid);
        StringProperty date = new SimpleStringProperty(utcDate.toLocalDateTime().toString());
        return date;
    }

    public StringProperty getAppointmentEndProperty() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime ldt = LocalDateTime.parse(this.appointmentEnd.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid);
        StringProperty date = new SimpleStringProperty(utcDate.toLocalDateTime().toString());
        return date;
    }

    public LocalDate getDateOnly() {
        Timestamp ts = Timestamp.valueOf(this.appointmentStart.get());
        ZonedDateTime zdt;
        ZoneId zid;
        LocalDate ld;
        if(this.appointmentLocation.get().equals("New York")) {
            zid = ZoneId.of("America/New_York");
        } else if(this.appointmentLocation.get().equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
        } else {
            zid = ZoneId.of("Europe/London");
        }
        zdt = ts.toLocalDateTime().atZone(zid);
        ld = zdt.toLocalDate();
        return ld;
    }

    public String getTimeOnly() {
        Timestamp ts = Timestamp.valueOf(this.appointmentStart.get());
        ZonedDateTime zdt;
        ZoneId zid;
        LocalTime lt;
        if(this.appointmentLocation.get().equals("New York")) {
            zid = ZoneId.of("America/New_York");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(4);
        } else if(this.appointmentLocation.get().equals("Phoenix")) {
            zid = ZoneId.of("America/Phoenix");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().minusHours(7);
        } else {
            zid = ZoneId.of("Europe/London");
            zdt = ts.toLocalDateTime().atZone(zid);
            lt = zdt.toLocalTime().plusHours(1);
        }
        int rawH = Integer.parseInt(lt.toString().split(":")[0]);
        if(rawH > 12) {
            rawH -= 12;
        }
        String ampm;
        if(rawH < 9 || rawH == 12) {
            ampm = "PM";
        } else {
            ampm = "AM";
        }
        String time = rawH + ":00 " + ampm;
        return time;
    }

    public String get15Time() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime ldt = LocalDateTime.parse(this.appointmentStart.getValue(), df);
        ZonedDateTime zdt = ldt.atZone(ZoneId.of("UTC"));
        ZoneId zid = ZoneId.systemDefault();
        ZonedDateTime utcDate = zdt.withZoneSameInstant(zid);
        DateTimeFormatter tFormatter = DateTimeFormatter.ofPattern("kk:mm");
        LocalTime localTime = LocalTime.parse(utcDate.toString().substring(11,16), tFormatter);
        return localTime.toString();
    }


}