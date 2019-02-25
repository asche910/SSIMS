import java.util.*;

public class Vertex {
    private String label;
    private String desc; // 描述
    private List<Edge> edgeList;
    private Vertex preVertex;
    private boolean isVisited;


    public Vertex(String label) {
        this.label = label;
        edgeList = new ArrayList<>();
    }

    public Vertex(String label, String desc) {
        this.label = label;
        this.desc = desc;
        edgeList = new ArrayList<>();
    }

    public class Edge{
        private int weight;
        private String parentLabel;
        private Vertex toVertex; // 终点

        public Edge(int weight, Vertex toVertex) {
            this.weight = weight;
            this.toVertex = toVertex;
            parentLabel = label;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getParentLabel() {
            return parentLabel;
        }

        public void setParentLabel(String parentLabel) {
            this.parentLabel = parentLabel;
        }

        public Vertex getToVertex() {
            return toVertex;
        }

        public void setToVertex(Vertex toVertex) {
            this.toVertex = toVertex;
        }

        @Override
        public String toString() {
            return "Edge: " + weight +  " --> " + toVertex.toString();
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Edge> getEdgeList() {
        return edgeList;
    }

    public void setEdgeList(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    public Vertex getPreVertex() {
        return preVertex;
    }

    public void setPreVertex(Vertex preVertex) {
        this.preVertex = preVertex;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean visited) {
        isVisited = visited;
    }

    /**
     * 新建一条指定边
     * @param target
     * @param weight
     * @return 连接成功返回true, 否则false
     */
    public boolean connect(Vertex target, int weight){
        if(!this.equals(target)){
            Iterator<Vertex> vertexIterator = getNeiVertexItr();
            while (vertexIterator.hasNext()){
                Vertex vertex = vertexIterator.next();
                // 已存在
                if (vertex.equals(target)){
                    return false;
                }
            }
            edgeList.add(new Edge(weight, target));
            // 无向图，互相连通
            target.edgeList.add(new Edge(weight, this));
        }
        return true;
    }

    /**
     * 获得与指定Vertex相连的Edge
     * @param target
     * @return
     */
    public Edge getEdge(String target){
        for (Edge edge: edgeList){
            if (edge.getToVertex().getLabel().equals(target)){
                return edge;
            }
        }
        return null;
    }

    /**
     * 获得与指定Vertex相连的Edge
     * @param target
     * @return
     */
    public Edge getEdge(Vertex target){
        for (Edge edge: edgeList){
            if (edge.getToVertex().equals(target)){
                return edge;
            }
        }
        return null;
    }

    public void removeEdge(Vertex vertex){
        int index = -1;
        for(int i = 0; i < edgeList.size(); i++){
            if (edgeList.get(i).getToVertex().equals(vertex))
                index = i;
        }
        if (index == -1)
            return;
        // vertex.edgeList.remove(edgeList.get(index));
        edgeList.remove(index);
    }

    /**
     * 提供周围Vertex的迭代器
     * 由edge的迭代器间接得到目标
     */
    public class NeighborVertexItr implements Iterator <Vertex>{
        private Iterator<Edge> vertexIterator;

        public NeighborVertexItr() {
            vertexIterator = edgeList.iterator();
        }

        @Override
        public boolean hasNext() {
            return vertexIterator.hasNext();
        }

        @Override
        public Vertex next() {
            Vertex vertex;
            if (vertexIterator.hasNext()){
                Edge edge = vertexIterator.next();
                vertex = edge.getToVertex();
            } else
                throw new NoSuchElementException();
            return vertex;
        }
    }

    public class NeighborWeightItr implements Iterator<Integer>{

        private Iterator<Edge> vertexIterator;

        public NeighborWeightItr() {
            vertexIterator = edgeList.iterator();
        }

        @Override
        public boolean hasNext() {
            return vertexIterator.hasNext();
        }

        @Override
        public Integer next() {
            if (vertexIterator.hasNext()){
                Edge edge = vertexIterator.next();
                return edge.getWeight();
            }
            throw new NoSuchElementException();
        }
    }

    public NeighborVertexItr getNeiVertexItr(){
        return new NeighborVertexItr();
    }

    public NeighborWeightItr getNeiWeightItr(){
        return new NeighborWeightItr();
    }

    @Override
    public String toString() {
        return  "Vert: " + label + "; edge num: " + getEdgeList().size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex){
            return ((Vertex) obj).getLabel().equals(label);
        }
        return false;
    }
}
