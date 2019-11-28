import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class cpath {
    private static ArrayList<Vertex> vertices = new ArrayList<>();
    private static int[][][] adjMatrix;
    private static BinaryHeap heap = new BinaryHeap(new ArrayList<>());
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        int numberOfVertices = 0, source, destination;
        ArrayList<Integer> path;

        // int numberOfVertices = 0, source, destination;
        String fileName = args[0];
        source = Integer.parseInt(args[1]); destination = Integer.parseInt(args[2]);
        int budget = Integer.parseInt(args[3]);
        insert(source, source, 0, 0);

        try {
            // Input from a text file
            FileReader file = new FileReader(fileName);
            BufferedReader br = new BufferedReader(file);
            String input;

            int vertex1, vertex2, line = 0;
            while((input = br.readLine()) != null) {
                input = input.stripLeading();
                String[] splitLine = input.split(" ");

                if(input.length() > 0) {
                    if (line == 0) {
                        numberOfVertices = Integer.parseInt(splitLine[0]);
                        adjMatrix = new int[numberOfVertices][numberOfVertices][2];

                        for(int i = 0; i < numberOfVertices; i++) {
                            // Initialize every vertex in the vertices array
                            vertices.add(new Vertex(i, Integer.MIN_VALUE, Integer.MAX_VALUE));
                        }

                        initAdjMatrix(numberOfVertices); // Adjacency matrix costs and times initialized to -1's
                        line++;
                    }
                    else {
                        vertex1 = Integer.parseInt(splitLine[0]);
                        vertex2 = Integer.parseInt(splitLine[1]);
                        adjMatrix[vertex1][vertex2][0] = Integer.parseInt(splitLine[2]);
                        adjMatrix[vertex1][vertex2][1] = Integer.parseInt(splitLine[3]);
                    }
                }
            }
        }

        catch(IOException io) { }

        /*for(int i = 0; i < numberOfVertices; i++) {
            // Initialize every vertex in the vertices array
            vertices.add(new Vertex(i, Integer.MIN_VALUE, Integer.MAX_VALUE));
        }

        initAdjMatrix(numberOfVertices);

        vertices.get(source).setCost(0); vertices.get(source).setTime(0); // Source to Source Traversal has cost 0 and time 0
        insert(source, source, 0, 0);

        int numberOfEdges = sc.nextInt();

        for(int i = 0; i < numberOfEdges; i++) {
            u = sc.nextInt(); v = sc.nextInt(); cost = sc.nextInt(); time = sc.nextInt();
            // For adjacency matrix
            adjMatrix[u][v][0] = cost; adjMatrix[u][v][1] = time;
        }*/

        while(heap.getQueue().size() > 0) {
            path = heap.extractMin();

            Vertex v1 = vertices.get(path.get(1));
            ArrayList<Integer> fastestPath = new ArrayList<>();

            try {
                fastestPath = v1.getFastestPath(); // Get the fastest path previously found
            }
            catch(NullPointerException ex) { }

            if(fastestPath == null || path.get(3) < fastestPath.get(1)) {
                // Only insert the new path to the list if it is non-dominated
                v1.addPathToList(path.get(2), path.get(3));

                // Update the cost and time required to reach the vertex using the new path
                v1.setCost(v1.getFastestPath().get(0));
                v1.setTime(v1.getFastestPath().get(1));
                v1.setPred(vertices.get(path.get(0))); // Set the predecessor of the vertex to be used to extract the path later

                for(int j = 0; j < numberOfVertices; j++) {
                    if(adjMatrix[v1.getName()][j][0] != -1)
                        relax(v1.getName(), j);
                }
            }
        }

        System.out.println("Source - " +source+ " Destination - " +destination);
        System.out.println("List of Paths: ");
        for(ArrayList<Integer> pathList : vertices.get(destination).getPathList()) {
            System.out.print(Arrays.toString(pathList.toArray()) + " ");
        }
        System.out.println();

        ArrayList<Integer> fastestPath = vertices.get(destination).getFastestPath();
        System.out.println("The fastest path requires a cost of " +fastestPath.get(0)+ " units and time of " +fastestPath.get(1)+ " units");
        System.out.println("Path :");
        printPath(vertices.get(destination));
    }

    private static void printPath(Vertex v) {
        if(v.getPred() == v) {
            System.out.print(v.getName());
            return;
        }
        printPath(v.getPred());
        System.out.print(" -> " + v.getName());
    }

    private static void initAdjMatrix(int n) {
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                adjMatrix[i][j][0] = -1;
                adjMatrix[i][j][1] = -1;
            }
        }
    }

    private static void relax(int u, int v) {
        Vertex v1 = vertices.get(u); Vertex v2 = vertices.get(v);
        int edgeCost = adjMatrix[u][v][0], edgeTime = adjMatrix[u][v][1];
        ArrayList<Integer> fastestPath = new ArrayList<>();

        try {
            fastestPath = v2.getFastestPath();
        }
        catch(NullPointerException ex) { }

        if(fastestPath == null || (edgeTime + v1.getTime()) < fastestPath.get(1)) {
            // Only insert into the queue if a non-dominated path is found
            insert(u, v, edgeCost + v1.getCost(), edgeTime + v1.getTime());
        }
    }

    private static void insert(int u, int v, int cost, int time) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(u); path.add(v); path.add(cost); path.add(time);
        heap.add(path);
    }
}
