import java.util.Arrays;

/**
 * 测试类
 */
public class Test {
    public static void main(String[] args){
        System.out.println("Test Start: ");

        Graph graph = new Graph();

/*        Vertex vertex1 = new Vertex("A");
        Vertex vertex2 = new Vertex("B");
        Vertex vertex3 = new Vertex("C");
        Vertex vertex4 = new Vertex("D");
        Vertex vertex5 = new Vertex("E");
        Vertex vertex6 = new Vertex("F");

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addVertex(vertex4);
        graph.addVertex(vertex5);
        graph.addVertex(vertex6);

        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 5);
        graph.addEdge("B", "D", 5);
        graph.addEdge("B", "E", 2);
        graph.addEdge("C", "E", 4);
        graph.addEdge("C", "F", 7);
        graph.addEdge("D", "F", 4);
        graph.addEdge("E", "F", 3);*/
        graph.readFromFile();

        System.out.print(graph.toString());

        graph.removeVertex(0);

        System.out.print(graph.toString());

        graph.queryVertex(0);

        graph.searchShortestPath(0, 4);

        graph.navigateAllVertex(0);

        graph.getMinSpanTreeByPrim();


        // graph.saveToFile();
    }
}
