import java.util.ArrayList;

// Need to implement operations - decreaseKey, minHeapInsert, etc.
public class BinaryHeap {
    private ArrayList<ArrayList<Integer>> queue;
    private int heapSize;

    public BinaryHeap(ArrayList< ArrayList<Integer> > queue) {
        this.queue = queue;
        this.heapSize = 0;
    }

    public ArrayList<Integer> extractMin() {
        ArrayList<Integer> min = this.queue.remove(0);
        try {
            this.queue.add(0, this.queue.remove(this.queue.size() - 1));
        }

        catch(NullPointerException | IndexOutOfBoundsException ex) { }

        this.heapSize--;
        this.minHeapify(0);

        this.sort();
        return min;
    }

    public void replace(int pos1, int pos2) {
        ArrayList<Integer> temp1 = this.queue.get(pos1);
        ArrayList<Integer> temp2 = this.queue.get(pos2);

        this.queue.remove(pos1);
        this.queue.add(pos1, temp2);

        this.queue.remove(pos2);
        this.queue.add(pos2, temp1);
    }

    public void sort() {
        int i = this.queue.size()-1;
        while(i > 0) {
            if (this.heapSize > 1 && this.queue.get(i-1).get(2) > this.queue.get(i).get(2))
                this.replace(i-1, i);

            else if (this.heapSize > 1 && this.queue.get(i-1).get(2) == this.queue.get(i).get(2) &&
                    this.queue.get(i-1).get(3) > this.queue.get(i).get(3))
                this.replace(i-1, i);
            i--;
        }
    }

    public void add(ArrayList<Integer> path) {
        this.heapSize++;
        this.queue.add(path);

        int i = this.queue.indexOf(path);
        while(i > 0 && this.queue.get(parent(i)).get(2) > this.queue.get(i).get(2)) {
            this.replace(parent(i), i);
            i = parent(i);
        }

        if(i > 0 && this.queue.get(parent(i)).get(2) == this.queue.get(i).get(2) && this.queue.get(parent(i)).get(3) > this.queue.get(i).get(3))
            this.replace(parent(i), i);

        this.sort();
    }

    public void minHeapify(int i) {
        int l = left(i), r = right(i), smallest;

        if(l < this.heapSize && this.queue.get(l).get(2) < this.queue.get(i).get(2))
            smallest = l;
        else if(l < this.heapSize && this.queue.get(l).get(2) == this.queue.get(i).get(2) && this.queue.get(l).get(3) < this.queue.get(i).get(3))
            smallest = l;

        else smallest = i;

        if(r < this.heapSize && this.queue.get(r).get(2) < this.queue.get(smallest).get(2))
            smallest = r;
        else if(r < this.heapSize && this.queue.get(r).get(2) == this.queue.get(smallest).get(2) &&
                this.queue.get(r).get(3) < this.queue.get(smallest).get(3))
            smallest = r;

        if(smallest != i) {
            this.replace(i, smallest);
            minHeapify(smallest);
        }
    }

    public int left(int i) {
        return 2*i + 1;
    }

    public int right(int i) {
        return 2*i + 2;
    }

    public int parent(int i) {
        return (int) (Math.ceil((double)i / (double)2) - 1);
    }

    public ArrayList<ArrayList<Integer>> getQueue() {
        return queue;
    }

    public int getHeapSize() {
        return heapSize;
    }
}
