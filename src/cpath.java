import java.util.*;

public class cpath {
    private static ArrayList<Vertex> vertices = new ArrayList<>();
    private static HashMap<Vertex, ArrayList <HashMap <Vertex, ArrayList<Integer> > > > neighbours = new HashMap<>();
    private static int[][][] adjMatrix;
    private static HashMap<Vertex, Vertex> pred = new HashMap<>();
    private static ArrayList< ArrayList<Integer> > heap = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);


    public static void main(String[] args) {
        int numberOfVertices = sc.nextInt(), source = sc.nextInt(), destination = sc.nextInt(), cost, time, u, v;
        ArrayList<Integer> path;
        adjMatrix = new int[numberOfVertices][numberOfVertices][2];

        for(int i = 0; i < numberOfVertices; i++) {
            // Initialize every vertex in the vertices array
            vertices.add(new Vertex(i, Integer.MIN_VALUE, Integer.MAX_VALUE));
        }

        initAdjMatrix(numberOfVertices); // Adjacency matrix initialized to -1's
        vertices.get(source).setCost(0); vertices.get(source).setTime(0); // Source to Source Traversal has cost 0 and time 0
        insert(source, source, 0, 0);

        int numberOfEdges = sc.nextInt();
        ArrayList< HashMap<Vertex, ArrayList<Integer>>> vs;
        HashMap<Vertex, ArrayList<Integer> > edge;
        ArrayList<Integer> weights;


        for(int i = 0; i < numberOfEdges; i++) {
            u = sc.nextInt(); v = sc.nextInt(); cost = sc.nextInt(); time = sc.nextInt();
            // For adjacency matrix
            adjMatrix[u][v][0] = cost; adjMatrix[u][v][1] = time;

            // For adjacency list
            weights = new ArrayList<>();
            weights.add(cost); weights.add(time);

            edge = new HashMap<>();
            edge.put(vertices.get(v), weights);

            if(neighbours.containsKey(vertices.get(u))) {
                vs = neighbours.get(vertices.get(u));
                vs.add(edge);
                neighbours.put(vertices.get(u), vs);
            }
            else {
                vs = new ArrayList<>();
                vs.add(edge);
                neighbours.put(vertices.get(u), vs);
            }
        }

        while(heap.size() > 0) {
            path = heap.remove(0);
            Vertex v1 = vertices.get(path.get(1));
            ArrayList<Integer> fastestPath = new ArrayList<>();

            try {
                fastestPath = v1.getFastestPath(); // Get the previous fastest path found
            }
            catch(NullPointerException ex) { }

            if(fastestPath == null || path.get(3) < fastestPath.get(1)) {
                // Only insert the new path to the list if it is faster than the previous fastest path found
                v1.addPathToList(path.get(2), path.get(3));
                v1.setCost(v1.getFastestPath().get(0));
                v1.setTime(v1.getFastestPath().get(1));
                v1.setPred(vertices.get(path.get(0)));

                for(int j = 0; j < numberOfVertices; j++) {
                    if(adjMatrix[v1.getName()][j][0] != -1)
                        relax(v1.getName(), j, adjMatrix[v1.getName()][j][0], adjMatrix[v1.getName()][j][1]);
                }
                mergeSort(0, heap.size()-1, heap);
            }
        }

        System.out.println("Source - " +source+ " Destination - " +destination);
        System.out.println("List of Paths: ");
        for(ArrayList<Integer> pathList : vertices.get(destination).getPathList()) {
            System.out.print(Arrays.toString(pathList.toArray()) + " ");
        }
        System.out.println();

        ArrayList<Integer> fastestPath = vertices.get(destination).getFastestPath();
        System.out.println("Fastest Path Cost = " +fastestPath.get(0)+ " units and Time = " +fastestPath.get(1)+ " units");
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
            pred.put(vertices.get(i), null);
            for(int j = 0; j < n; j++) {
                adjMatrix[i][j][0] = -1;
                adjMatrix[i][j][1] = -1;
            }
        }
    }

    private static void relax(int u, int v, int cost, int time) {
        Vertex v1 = vertices.get(u); Vertex v2 = vertices.get(v);
        int edgeCost = adjMatrix[u][v][0], edgeTime = adjMatrix[u][v][1];
        ArrayList<Integer> fastestPath = new ArrayList<>();

        try {
            fastestPath = v2.getFastestPath();
        }

        catch(NullPointerException ex) { }

        if(fastestPath == null || (edgeTime + v1.getTime()) < fastestPath.get(1)) {
            // Only compute new costs and times if new path found is faster than the previous fastest path found to vertex v

            /*if(fastestPath != null) {
                v2.setCost(fastestPath.get(0));
                v2.setTime(fastestPath.get(1));
            }*/

            insert(u, v, edgeCost + v1.getCost(), edgeTime + v1.getTime()); // Insert the new path in the queue
        }
    }

    private static void insert(int u, int v, int cost, int time) {
        ArrayList<Integer> path = new ArrayList<>();
        path.add(u); path.add(v); path.add(cost); path.add(time);
        heap.add(path);
    }


    private static void mergeSort(int beg, int end, ArrayList<ArrayList<Integer>> arr) {
        if(beg < end) {
            int mid = (beg + end) / 2;
            mergeSort(beg, mid, arr);
            mergeSort(mid + 1, end, arr);
            merge(beg, end, mid, arr);
        }
    }

    private static void merge(int beg, int end, int mid, ArrayList<ArrayList<Integer>> arr) {
        int i = beg, j = mid+1, index = 0;
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();

        while(i <= mid && j <= end) {
            if(arr.get(i).get(2) < arr.get(j).get(2))
                temp.add(arr.get(i++));

            else if(arr.get(i).get(2) == arr.get(j).get(2) && arr.get(i).get(3) <= arr.get(j).get(3))
                temp.add(arr.get(i++));

            else temp.add(arr.get(j++));
        }

        if(i > mid) {
            while(j <= end)
                temp.add(arr.get(j++));
        }
        else {
            while(i <= mid)
                temp.add(arr.get(i++));
        }

        int k = beg;
        for(i = 0; i < (end - beg + 1); i++) {
            arr.remove(k); arr.add(k++, temp.get(i));
        }
    }
}
