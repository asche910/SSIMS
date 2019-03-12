
/**
 * 测试类
 */
public class Test {
    public static void main(String[] args){
        System.out.println("Test Start: ");

        Graph graph = new Graph();

        Vertex vertex1 = new Vertex("A");
        Vertex vertex2 = new Vertex("B");
        Vertex vertex3 = new Vertex("C");
        Vertex vertex4 = new Vertex("D");
        Vertex vertex5 = new Vertex("E");
        Vertex vertex6 = new Vertex("F");
        Vertex vertex7 = new Vertex("G");

        graph.addVertex(vertex1);
        graph.addVertex(vertex2);
        graph.addVertex(vertex3);
        graph.addVertex(vertex4);
        graph.addVertex(vertex5);
        graph.addVertex(vertex6);
        graph.addVertex(vertex7);

        graph.addEdge("A", "C", 700);
        graph.addEdge("A", "E", 1000);
        graph.addEdge("A", "F", 600);
        graph.addEdge("B", "C", 1000);
        graph.addEdge("B", "G", 1000);
        graph.addEdge("C", "D", 400);
        graph.addEdge("D", "E", 300);
        graph.addEdge("D", "G", 400);
        graph.addEdge("E", "F", 600);
        graph.addEdge("F", "G", 500);
//        graph.readFromFile();

        System.out.print(graph.toString());

//        graph.removeVertex(0);

        System.out.print(graph.toString());

        graph.queryVertex(0);

        graph.searchShortestPath(0, 4);

        graph.navigateAllVertex(0);

        graph.getMinSpanTreeByPrim();


         graph.saveToFile();
    }
}
