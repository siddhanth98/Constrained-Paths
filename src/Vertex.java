import java.util.ArrayList;
import java.util.HashMap;

public class Vertex {
    private int name;
    private int cost;
    private int time;
    private ArrayList<ArrayList<Integer>> pathList;
    private HashMap<Integer, Vertex> pathPred;

    public Vertex(int name, int cost, int time) {
        this.name = name;
        this.cost = cost;
        this.time = time;
        pathList = new ArrayList<>();
        pathPred = new HashMap<>();
    }

    public int getName() {
        return this.name;
    }

    public int getCost() {
        return cost;
    }

    public int getTime() {
        return time;
    }


    public ArrayList<Integer> getFastestPath() {
        if(this.pathList.size() > 0)
            return this.pathList.get(pathList.size()-1);
        else return null;
    }

    public ArrayList<ArrayList<Integer>> getPathList() {
        return pathList;
    }

    public HashMap<Integer, Vertex> getPathPred() {
        return pathPred;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addPathToList(int cost, int time) {
        ArrayList<Integer> newPath = new ArrayList<>();
        newPath.add(cost); newPath.add(time);
        this.pathList.add(newPath);
    }

    public void addPredecessorVertex(Vertex vertex, int pathCost) {
        this.pathPred.put(pathCost, vertex);
    }
}
