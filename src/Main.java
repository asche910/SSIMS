import java.util.Scanner;

/**
 * Scenic spot information management system
 * 景区信息管理系统
 *
 * @author Asche
 */
@SuppressWarnings("Duplicates")
public class Main {
    private static String name = "Scenic Spot Information Management System";
    private static String logo = "      ___           ___                       ___           ___     \n" +
            "     /\\  \\         /\\  \\          ___        /\\__\\         /\\  \\    \n" +
            "    /::\\  \\       /::\\  \\        /\\  \\      /::|  |       /::\\  \\   \n" +
            "   /:/\\ \\  \\     /:/\\ \\  \\       \\:\\  \\    /:|:|  |      /:/\\ \\  \\  \n" +
            "  _\\:\\~\\ \\  \\   _\\:\\~\\ \\  \\      /::\\__\\  /:/|:|__|__   _\\:\\~\\ \\  \\ \n" +
            " /\\ \\:\\ \\ \\__\\ /\\ \\:\\ \\ \\__\\  __/:/\\/__/ /:/ |::::\\__\\ /\\ \\:\\ \\ \\__\\\n" +
            " \\:\\ \\:\\ \\/__/ \\:\\ \\:\\ \\/__/ /\\/:/  /    \\/__/~~/:/  / \\:\\ \\:\\ \\/__/\n" +
            "  \\:\\ \\:\\__\\    \\:\\ \\:\\__\\   \\::/__/           /:/  /   \\:\\ \\:\\__\\  \n" +
            "   \\:\\/:/  /     \\:\\/:/  /    \\:\\__\\          /:/  /     \\:\\/:/  /  \n" +
            "    \\::/  /       \\::/  /      \\/__/         /:/  /       \\::/  /   \n" +
            "     \\/__/         \\/__/                     \\/__/         \\/__/    ";

    private static Scanner scanner;
    private static Graph graph;

    public static void main(String[] args) {
        println(logo);
        printf(" :: %s ::\n", name);

        graph = new Graph();
        graph.readFromFile();

        scanner = new Scanner(System.in);
        while (true) {
            openMenu();
            println("请选择对应操作前的数字编号：");
            int n = scanner.nextInt();
            switch (n) {
                case 7:
                    println("系统即将关闭！");
                    System.exit(0);
                    break;
                case 6:
                    deleteVertex();
                    break;
                case 5:
                    editVertex();
                    break;
                case 4:
                    structCircuit();
                    break;
                case 3:
                    searchShortestPath();
                    break;
                case 2:
                    navgateAllVertex();
                    break;
                case 1:
                    queryVertex();
                    break;
                default:
                    println("请输入有效的操作索引！");
            }
        }
    }

    private static void openMenu() {
        println("\n------------ 景区信息管理系统 ------------");
        println("             1.查询景点信息");
        println("             2.旅游景点导航");
        println("             3.搜索最短路径");
        println("             4.铺设电路规划");
        println("             5.修改景区信息");
        println("             6.删除景区信息");
        println("             7.退出当前系统");
        println("-----------------------------------------\n");
    }

    private static void queryVertex() {
        println("请输入要查询的景点编号：");
        int index = scanner.nextInt();
        if (!isValid(index)) return;
        graph.queryVertex(index);
    }

    private static void navgateAllVertex() {
        println("请输入起点景点编号：");
        int index = scanner.nextInt();
        if (!isValid(index)) return;
        graph.navigateAllVertex(index);
    }

    private static void searchShortestPath(){
        println("请输入起点编号：");
        int from = scanner.nextInt();
        if (!isValid(from)) return;

        println("请输入终点编号：");
        int to = scanner.nextInt();
        if (!isValid(to)) return;

        graph.searchShortestPath(from, to);
    }

    private static void structCircuit(){
        graph.getMinSpanTreeByPrim();
    }

    private static void editVertex(){

        println("请输入对应的操作编号(默认为1) 1.景点 2.道路 3.回主菜单：");

        int ope = scanner.nextInt();
        if (ope == 2){
            println("请输入起点编号：");
            int from = scanner.nextInt();
            if (!isValid(from)) return;

            println("请输入终点编号：");
            int to = scanner.nextInt();
            if (!isValid(to)) return;

            while (true){
                println("请输入新的长度：");
                int length = scanner.nextInt();

                if (length <= 0){
                    println("请输入有效的长度！");
                    continue;
                }
                graph.editEdge(from, to, length);
                break;
            }
        }else if (ope == 3){
            return;
        }else {
            println("请输入要修改的景点编号：");
            int index = scanner.nextInt();
            if (!isValid(index)) return;

            println("请选择修该信息的编号(默认为1) 1.名称 2.介绍：");
            int n = scanner.nextInt();
            if (n == 2) {
                println("请输入新的景点介绍：");
                String content = scanner.next();
                graph.editVertex(index, false, content);
            } else {
                println("请输入新的景点名称：");
                String content = scanner.next();
                graph.editVertex(index, true, content);
            }
        }
        println("修改完成！");
        graph.saveToFile();
    }

    private static void deleteVertex(){
        println("请输入对应的操作编号(默认为1) 1.景点 2.道路 3.回主菜单：");
        int ope = scanner.nextInt();
        if (ope == 2){
            println("请输入将删除的道路起点编号：");
            int from = scanner.nextInt();
            if (!isValid(from)) return;

            println("请输入将删除的道路终点编号：");
            int to = scanner.nextInt();
            if (!isValid(to)) return;

            graph.removeEdge(from, to);
        }else if (ope == 3){
            return;
        }else {
            println("请输入将删除的景点编号：");
            int index = scanner.nextInt();
            if (!isValid(index)) return;
            graph.removeVertex(index);
        }
        println("删除完成！");
        graph.saveToFile();
    }

    /**
     * 检查输入是否有效
     * @param index
     * @return
     */
    private static boolean isValid(int index) {
        if (index < 0 || index >= graph.getEdgeCount()) {
            println("请输入有效的景点编号！");
            return false;
        }
        return true;
    }

    private static void println(String str) {
        System.out.println(str);
    }

    private static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }
}
