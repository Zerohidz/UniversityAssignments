public class Main {
    public static String inputPath;
    public static String outputPath;
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bad arguments!");
            System.exit(1);
        }
        inputPath = args[0];
        outputPath = args[1];

        TripSchedule schedule = new TripSchedule(inputPath);
        TripController controller = new TripController();
        controller.trip_schedule = schedule;
        controller.DepartureSchedule(schedule);
        System.out.println();
        controller.ArrivalSchedule(schedule);
    }
}