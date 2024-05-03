import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Quiz3 {
    public static class Vertex {
        public int x;
        public int y;

        public Vertex(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class Edge {
        public Vertex start;
        public Vertex end;
        public float weight;

        public Edge(Vertex start, Vertex end) {
            this.start = start;
            this.end = end;
            this.weight = getDistance(start, end);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));

        int numTestCase = Integer.parseInt(reader.readLine());
        for (int i = 0; i < numTestCase; i++) {
            String[] input = reader.readLine().split(" ");
            int numDroneStations = Integer.parseInt(input[0]);
            int numTotalStations = Integer.parseInt(input[1]);
            int numGroundPumpStations = numTotalStations - numDroneStations;

            Vertex[] stations = new Vertex[numTotalStations];
            for (int j = 0; j < numTotalStations; j++) {
                String[] station = reader.readLine().split(" ");
                stations[j] = new Vertex(Integer.parseInt(station[0]), Integer.parseInt(station[1]));
            }

            List<Edge> mst = constructMST(stations);
            Collections.sort(mst, (a, b) -> Float.compare(a.weight, b.weight));
            float result = mst.get(mst.size() - numDroneStations).weight;
            System.out.println(String.format("%.2f", result).replace(",", "."));
        }

        reader.close();
    }

    private static float getDistance(Vertex start, Vertex end) {
        int x = start.x - end.x;
        int y = start.y - end.y;
        return (float) Math.sqrt(x * x + y * y);
    }

    private static List<Edge> constructMST(Vertex[] stations) {
        List<Edge> mst = new ArrayList<>();

        int[] parent = new int[stations.length];
        float[] weight = new float[stations.length];
        boolean[] inMST = new boolean[stations.length];

        final int START = 0;
        inMST[START] = true;
        for (int i = 0; i < stations.length; i++) {
            if (i == START)
                continue;
            float w = getDistance(stations[START], stations[i]);
            weight[i] = w;
            parent[i] = START;
        }

        while (mst.size() < stations.length - 1) {
            int minIndex = -1;
            float minWeight = Float.MAX_VALUE;
            for (int i = 0; i < stations.length; i++) {
                if (!inMST[i] && weight[i] < minWeight) {
                    minIndex = i;
                    minWeight = weight[i];
                }
            }

            inMST[minIndex] = true;
            mst.add(new Edge(stations[parent[minIndex]], stations[minIndex]));

            for (int i = 0; i < stations.length; i++) {
                if (!inMST[i]) {
                    float w = getDistance(stations[minIndex], stations[i]);
                    if (w < weight[i]) {
                        weight[i] = w;
                        parent[i] = minIndex;
                    }
                }
            }
        }

        return mst;
    }
}