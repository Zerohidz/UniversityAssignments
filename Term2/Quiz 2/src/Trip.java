import java.time.LocalDateTime;
import java.time.LocalTime;

public class Trip {
    // bunlar birer field
    public String tripName;
    public LocalDateTime departureTime;
    public LocalDateTime arrivalTime;
    public int duration;
    public String state;

    // parse'lıyoruz
    public Trip(String inputLine) {
        String[] inputs = inputLine.split("\t");

        tripName = inputs[0];

        departureTime = LocalDateTime.parse(inputs[1]);

        duration = Integer.parseInt(inputs[2]);

        state = "Idle";

        calculateArrival();
    }

    public int getDepartureTime() {
        return departureTime.getMinute();
    }
    
    public int getArrivalTime() {
        return arrivalTime.getMinute();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void calculateArrival() {
        arrivalTime = departureTime.plusMinutes(duration);
    }
}
