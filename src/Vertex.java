import java.util.ArrayList;

public class Vertex {
    private int name;
    private int cost;
    private int time;
    private Vertex pred;
    private ArrayList<ArrayList<Integer>> pathList;

    public Vertex(int name, int cost, int time) {
        this.name = name;
        this.cost = cost;
        this.time = time;
        this.pred = null;
        pathList = new ArrayList<>();
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

    public Vertex getPred() {
        return pred;
    }

    public ArrayList<Integer> getPathList() {
        if(this.pathList.size() > 0)
            return this.pathList.get(pathList.size()-1);
        else return null;
    }

    public void setName(int name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setPred(Vertex pred) {
        this.pred = pred;
    }

    public void addPathToList(int cost, int time) {
        ArrayList<Integer> newPath = new ArrayList<>();
        newPath.add(cost); newPath.add(time);
        this.pathList.add(newPath);
    }
}
