import java.io.*;
import java.util.*;

/**
 * 由多个节点组成的图
 */
@SuppressWarnings("Duplicates")
public class Graph {
    private Map<String, Vertex> vertexMap;
    private Map<String, Integer> labelMap; // 景点名称转化为对应的index
    private int edgeCount;

    private String verFileName = "Vex.txt";
    private String edgeFileName = "Edge.txt";

    public Graph() {
        vertexMap = new LinkedHashMap<>();
        labelMap = new LinkedHashMap<>();
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    private int getVertexSize() {
        return vertexMap.size();
    }

    public void setEdgeCount(int edgeCount) {
        this.edgeCount = edgeCount;
    }

    public void addVertex(Vertex vertex) {
        vertexMap.put(vertex.getLabel(), vertex);
    }

    public boolean addEdge(String from, String to, int weight) {
        Vertex fromVertex = vertexMap.get(from);
        Vertex toVertex = vertexMap.get(to);
        return addEdge(fromVertex, toVertex, weight);
    }

    public boolean addEdge(Vertex from, Vertex to, int weight) {
        if (from != null && to != null) {
            from.connect(to, weight);
            edgeCount += 2;
            return true;
        }
        return false;
    }

    /**
     * 删除节点操作
     * @param index
     */
    public void removeVertex(int index){
        handleIndex(index);

        Vertex vertex = getVertexList().get(index);
        List<Vertex.Edge> edgeList = vertex.getEdgeList();
        // 首先删除与之相连的边
        for (Vertex.Edge edge: edgeList){
             // vertex.removeEdge(edge.getToVertex());
            edge.getToVertex().removeEdge(vertex);
        }
        // 然后删除此节点
        vertexMap.remove(vertex.getLabel());
    }

    /**
     * 删除边操作
     * @param from
     * @param to
     */
    public void removeEdge(int from, int to){
        handleIndex(from);
        handleIndex(to);

        List<Vertex> vertexList = getVertexList();
        Vertex fromVertex = vertexList.get(from);
        Vertex toVertex = vertexList.get(to);
        fromVertex.removeEdge(toVertex);
        toVertex.removeEdge(fromVertex);
    }

    /**
     * 修改景点信息（包括名称和介绍）
     * @param index
     * @param isName true --> name; false --> desc
     * @param content name or desc
     */
    public void editVertex(int index, boolean isName, String content){
        handleIndex(index);

        Vertex vertex = getVertexList().get(index);

        if (isName)
            vertex.setLabel(content);
        else
            vertex.setLabel(content);
    }

    /**
     * 修改道路信息（长度）
     * @param from
     * @param to
     */
    public void editEdge(int from, int to, int weight){
        handleIndex(from);
        handleIndex(to);

        List<Vertex> vertexList = getVertexList();
        Vertex fromVertex = vertexList.get(from);
        Vertex toVertex = vertexList.get(to);


        List<Vertex.Edge> edgeList = fromVertex.getEdgeList();
        for (Vertex.Edge edge: edgeList){
            if (edge.getToVertex().equals(toVertex)){
                edge.setWeight(weight);
                break;
            }
        }
        List<Vertex.Edge> edgeList_ = toVertex.getEdgeList();
        for (Vertex.Edge edge: edgeList_){
            if (edge.getToVertex().equals(fromVertex)){
                edge.setWeight(weight);
                break;
            }
        }
    }

    public Vertex getVertex(String target) {
        return vertexMap.get(target);
    }

    /**
     * 景点查询
     * @param index
     */
    public void queryVertex(int index){
        handleIndex(index);
        Vertex vertex = getVertexList().get(index);
        System.out.println("景点名称：" + vertex.getLabel());
        System.out.println("景点介绍：" + vertex.getDesc());
        System.out.print("相邻景点及距离：");
        List<Vertex.Edge> edgeList = vertex.getEdgeList();
        for (Vertex.Edge edge: edgeList) {
            System.out.printf("%s --> %dm; ", edge.getToVertex().getLabel(), edge.getWeight());
        }
        System.out.println();
    }

    public void searchShortestPath(int from, int to){
        List<Vertex> vertexList = getVertexList();
        String fromLabel = vertexList.get(from).getLabel();
        String toLabel = vertexList.get(to).getLabel();
        int minLength = getShortestPathLong(fromLabel, toLabel);
        System.out.println("最短路线：" + getShortestPath(toLabel));
        System.out.printf("路径总长度：%dm\n", minLength);
    }

    public void navigateAllVertex(int index){
        handleIndex(index);
        String label = getVertexList().get(index).getLabel();
        System.out.println("游览全图：" + getPathByDFS(label));
    }

    /**
     * 从文件读取节点及边的信息
     */
    public void readFromFile(){
        File verFile  = new File(verFileName);
        File edgeFile = new File(edgeFileName);

        try {
            FileInputStream verFIS = new FileInputStream(verFile);
            FileInputStream edgeFIS = new FileInputStream(edgeFile);
            BufferedReader verReader = new BufferedReader(new InputStreamReader(verFIS));
            BufferedReader edgeReader = new BufferedReader(new InputStreamReader(edgeFIS));

            int num = Integer.parseInt(verReader.readLine().trim());
            for (int i = 0; i < num; i++){
                int index = Integer.parseInt(verReader.readLine().trim());
                String label = verReader.readLine().trim();
                String desc = verReader.readLine().trim();
                Vertex vertex = new Vertex(label, desc);
                addVertex(vertex);
            }
            verReader.close();
            verFIS.close();
            // ------------------------------------>
            List<Vertex> vertexList = getVertexList();
            String line;
            while ((line = edgeReader.readLine()) != null){
                String[] strs = line.split("\\b{3,}");
                String[] nums = new String[3];
                int index = 0;
                for (int i = 0; i < strs.length; i++){
                    if (!strs[i].trim().equals(""))
                        nums[index++] = strs[i].trim();
                }
                // System.out.println(Arrays.toString(strs));
                int from = Integer.parseInt(nums[0]);
                int to = Integer.parseInt(nums[1]);
                int weight = Integer.parseInt(nums[2]);

                addEdge(vertexList.get(from), vertexList.get(to), weight);
            }
            edgeReader.close();
            edgeFIS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存节点及边的信息至文件
     */
    public void saveToFile(){
        File verFile  = new File(verFileName);
        File edgeFile = new File(edgeFileName);
        try {
            FileOutputStream verFOS = new FileOutputStream(verFile);
            FileOutputStream edgeFOS = new FileOutputStream(edgeFile);
            StringBuilder verBuilder = new StringBuilder();
            StringBuilder edgeBuilder = new StringBuilder();
            Set<Vertex.Edge> edgeSet = new HashSet<>();

            List<Vertex> vertexList = getVertexList();
            verBuilder.append(vertexList.size()).append('\n');
            for (int i = 0; i < vertexList.size(); i++){
                Vertex vertex = vertexList.get(i);
                verBuilder.append(i).append('\n');
                verBuilder.append(vertex.getLabel()).append('\n');
                verBuilder.append(vertex.getDesc()).append('\n');
                // ------------------------->
                List<Vertex.Edge> edgeList = vertex.getEdgeList();
                edgeSet.addAll(edgeList);
            }
            Map<String, Integer> map = getLabelMap();
            for (Vertex.Edge edge: edgeSet){
                int from = map.get(edge.getParentLabel());
                int to = map.get(edge.getToVertex().getLabel());
                if (from == to)
                    continue;
                edgeBuilder.append(String.format("%d    %d    %d\n", from, to, edge.getWeight()));
            }

            verFOS.write(verBuilder.toString().getBytes());
            verFOS.close();
            edgeFOS.write(edgeBuilder.toString().getBytes());
            edgeFOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * index检测
     * @param index
     */
    private void handleIndex(int index){
        if (index < 0 || index >= vertexMap.size())
            throw new IndexOutOfBoundsException();
    }

    /**
     * Dijkstra算法求最短路径
     *
     * @param from
     * @param to
     * @return
     */
    private int getShortestPathLong(String from, String to) {
        Vertex fromVertex = vertexMap.get(from);
        Vertex toVertex = vertexMap.get(to);
        int index = -1;
        if (fromVertex != null && toVertex != null) {
            int vertexSize = vertexMap.size();
            int[] dist = new int[vertexSize];
            List<Vertex> vertexList = getVertexList();

            // 初始化
            for (int i = 0; i < vertexSize; i++) {
                Vertex vertex = vertexList.get(i);
                if (vertex.equals(toVertex)) {
                    index = i;
                }
                if (vertex.equals(fromVertex)) {
                    dist[i] = 0;
                } else {
                    Vertex.Edge edge = fromVertex.getEdge(vertex);
                    if (edge == null) {
                        dist[i] = Integer.MAX_VALUE;
                    } else {
                        dist[i] = edge.getWeight();
                        vertex.setPreVertex(fromVertex);
                    }
                }
            }

            for (int i = 0; i < vertexSize; i++) {
                int min = Integer.MAX_VALUE, temp = 0;
                // 邻边中寻找权值最小的那一个
                for (int j = 0; j < vertexSize; j++) {
                    if (!vertexList.get(j).isVisited() && dist[j] < min) {
                        min = dist[j];
                        temp = j;
                    }
                }
                // 跳转至新的节点
                vertexList.get(temp).setVisited(true);
                // 更新相连dist[]数据
                for (int j = 0; j < vertexSize; j++) {
                    // TODO update dist[]...
                    Vertex vertex = vertexList.get(temp);
                    Vertex target = vertexList.get(j);
                    Vertex.Edge edge = vertex.getEdge(target);
                    if (edge == null)
                        continue;

                    if (edge.getWeight() + dist[temp] < dist[j]) {
                        dist[j] = edge.getWeight() + dist[temp];
                        target.setPreVertex(vertex);
                    }
                }
            }
            // System.out.println(Arrays.toString(dist));

            // 恢复Visited为默认值，以免影响下次操作
            for (Vertex ver : vertexList) {
                ver.setVisited(false);
            }

            if (index != -1)
                return dist[index];
        }
        return -1;
    }

    /**
     * 起点为@shortestPath函数中的起点
     *
     * @param target
     * @return
     */
    private String getShortestPath(String target) {
        Vertex vertex = vertexMap.get(target);
        if (vertex != null) {
            Vertex temp = vertex.getPreVertex();
            List<String> list = new ArrayList<>();
            list.add(target);
            while (temp != null) {
                list.add(temp.getLabel());
                temp = temp.getPreVertex();
            }
            return listToPath(list, true);
        }
        return null;
    }

    /**
     * 路径导航，无回路游览全图
     *
     * @param from 起点地址
     * @return
     */
    private String getPathByDFS(String from) {
        Vertex vertex = vertexMap.get(from);
        if (vertex == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        list.add(from);

        getPathByDFS(vertex, list);
        // 恢复Visited为默认值，以免影响下次操作
        for (Vertex ver : getVertexList()) {
            ver.setVisited(false);
        }
        return listToPath(list);
    }

    private void getPathByDFS(Vertex vertex, List<String> list) {
        Iterator<Vertex> iterator = vertex.getNeiVertexItr();
        vertex.setVisited(true);
        while (iterator.hasNext()) {
            Vertex ver = iterator.next();
            if (!ver.isVisited()) {
                list.add(ver.getLabel());
                getPathByDFS(ver, list);
            }
        }
    }

    /**
     * 使用普里姆(Prim)算法构造最小生成树
     */
    public void getMinSpanTreeByPrim() {
        List<Vertex> vertexList = getVertexList();
        int vertexSize = vertexMap.size();
        int [][] xy = new int[vertexSize][2]; // 存放二叉树坐标xy[][0`1]
        int min, i, j, k, index = 0;
        int[] adjvex = new int[vertexSize]; // 保存相关顶点下标以记录路径
        int[] lowcost = new int[vertexSize]; // 保存相关顶点间边的权值, 完成则清零
        lowcost[0] = 0;
        adjvex[0] = 0;
        Vertex vertex = vertexList.get(0);
        for (i = 1; i < vertexSize; i++) {
            Vertex target = vertexList.get(i);
            Vertex.Edge edge = vertex.getEdge(target);
            if (edge == null) {
                lowcost[i] = Integer.MAX_VALUE;
            } else {
                lowcost[i] = edge.getWeight();
            }
            adjvex[i] = 0;
        }
        for (i = 1; i < vertexSize; i++) {
            min = Integer.MAX_VALUE;

            j = 0;
            k = 0;
            while (j < vertexSize) {
                if (lowcost[j] != 0 && lowcost[j] < min) { // 如果权值不为零，且权值小于min
                    min = lowcost[j];//则让当前权值成为最小值
                    k = j; // 将当前最小值的下标存入k
                }
                j++;
            }
            xy[index][0] = adjvex[k];
            xy[index++][1] = k;
            // System.out.printf("(%d,%d) --> ", adjvex[k], k);
            // System.out.println(Arrays.toString(adjvex) + " --- " + Arrays.toString(lowcost) );

            lowcost[k] = 0; // 将当前顶点的权值设置为0，表示此顶点已经完成任务
            Vertex vertex1 = vertexList.get(k);
            for (j = 1; j < vertexSize; ++j) { // 循环所有顶点
                Vertex temp = vertexList.get(j);
                Vertex.Edge edge = vertex1.getEdge(temp);
                if (edge == null)
                    continue;
                int weight = edge.getWeight();
                if (lowcost[j] != 0 && weight < lowcost[j]) {
                    // 若k能到的点未完成任务并且此边小于对应的lowcost
                    lowcost[j] = weight; // 将较小权值存入lowcost
                    adjvex[j] = k; // 将下标为k的顶点存入adjvex
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        int sum = 0;
        // 输出相关信息
        for (index = 0; index < vertexSize - 1; index++){
            Vertex from = vertexList.get(xy[index][0]);
            Vertex to = vertexList.get(xy[index][1]);
            Vertex.Edge edge = from.getEdge(to);
            builder.append(new String(from.getLabel() + " --> " + to.getLabel() + " : " + edge.getWeight() + "m\n"));
            sum += edge.getWeight();
        }
        builder.append(String.format("总长度：%dm", sum));
        System.out.println("铺设道路及长度为：");
        System.out.println(builder.toString());
        // System.out.println(Arrays.toString(adjvex) + " --- " + Arrays.toString(lowcost));
    }

    /**
     * 将list的各个label连接成路径
     *
     * @param list
     * @param isReversed 默认false
     * @return
     */
    private String listToPath(List<String> list, boolean isReversed) {
        StringBuilder builder = new StringBuilder();
        if (isReversed) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (i == 0) {
                    builder.append(list.get(i));
                } else {
                    builder.append(list.get(i)).append(" --> ");
                }
            }
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (i == list.size() - 1) {
                    builder.append(list.get(i));
                } else {
                    builder.append(list.get(i)).append(" --> ");
                }
            }
        }
        return builder.toString();
    }

    /**
     * 将list的各个label连接成路径
     *
     * @param list
     * @return
     */
    private String listToPath(List<String> list) {
        return listToPath(list, false);
    }

    public Map<String, Integer> getLabelMap() {
        // 通过此函数更新labelMap
        getVertexList();
        return labelMap;
    }

    private List<Vertex> getVertexList() {
        List<Vertex> list = new ArrayList<>();
        labelMap.clear();
        Iterator<Map.Entry<String, Vertex>> iterator = vertexMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Vertex> entry = iterator.next();
            Vertex vertex = entry.getValue();
            list.add(vertex);
            labelMap.put(vertex.getLabel(), list.size() - 1);
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, Vertex>> iterator = vertexMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Vertex> entry = iterator.next();
            Vertex vertex = entry.getValue();
            builder.append(vertex.getLabel() + ": ");

            Vertex.NeighborWeightItr weightItr = vertex.getNeiWeightItr();
            while (weightItr.hasNext()) {
                int weight = weightItr.next();
                builder.append(" " + weight);
            }
            builder.append('\n');
        }
        return builder.toString();
    }
}
