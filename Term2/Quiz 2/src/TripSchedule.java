public class TripSchedule<Path> {
    public Trip[] trips; // bu bir referans

    public TripSchedule(String filePath) {
        String[] lines = FileHelper.readAllLines(filePath);

        trips = new Trip[100]; // referansa değerini verdik
        for (int i = 0; i < lines.length; i++) {
            trips[i] = new Trip(filePath);
        }
    }
}