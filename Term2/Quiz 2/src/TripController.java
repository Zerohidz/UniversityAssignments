import java.io.File;
import java.io.PrintStream;

public class TripController implements ArrivalController, DepartureController {
    protected TripSchedule trip_schedule;

    public TripController() {
        try {
            System.setOut(new PrintStream(new File(Main.outputPath)));
        }
        catch (Exception e) {

        }
    }

    public void DepartureSchedule(TripSchedule schedule) {
        // delayed yapmayı unutma
        bubbleSortDeparture(trip_schedule.trips);

        System.out.println("Departure order:");
        for (var trip : trip_schedule.trips) {
            System.out.println(trip.tripName + " depart at " + trip.departureTime + "   Trip State:" + trip.state);
        }
    }
    
    public void ArrivalSchedule(TripSchedule schedule) {
        // delayed yapmayı unutma
        bubbleSortArrival(trip_schedule.trips);
        
        System.out.println("Arrival order:");
        for (var trip : trip_schedule.trips) {
            System.out.println(trip.tripName + " arrive at " + trip.departureTime + "   Trip State:" + trip.state);
        }
    }

    void bubbleSortDeparture(Trip[] arr)
    {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr[j].getDepartureTime() > arr[j + 1].getDepartureTime()) {
                    // swap arr[j+1] and arr[j]
                    Trip temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
    }

    void bubbleSortArrival(Trip[] arr)
    {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr[j].getArrivalTime() > arr[j + 1].getArrivalTime()) {
                    // swap arr[j+1] and arr[j]
                    Trip temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
    }
}