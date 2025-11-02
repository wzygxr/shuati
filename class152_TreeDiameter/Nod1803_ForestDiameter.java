import java.util.*;

/**
 * 51Nod-1803 - 森林的直径
 * 题目描述：有一个由n个节点组成的森林，每个节点属于一棵树。
 * 支持两种操作：
 * 1. 连接两棵树：输入格式为"1 u v"，表示将节点u所在的树与节点v所在的树合并
 * 2. 查询操作：输入格式为"2 u"，表示询问节点u所在的树的直径
 * 
 * 解题思路：
 * 1. 使用并查集来管理各个树的合并
 * 2. 对于每个树，维护其直径的两个端点
 * 3. 当合并两棵树时，新树的直径只可能是原两棵树的直径之一，或者通过连接边形成的新路径（即u树的两个端点和v树的两个端点之间的最长路径）
 * 
 * 时间复杂度分析：
 * - 并查集操作的均摊时间复杂度接近O(1)
 * - 合并操作需要计算4种可能的路径长度，需要额外的BFS/DFS操作，单次时间复杂度为O(n)，但实际应用中树的大小通常不大
 * 
 * 空间复杂度：O(n)
 */
public class Nod1803_ForestDiameter {
    private static int n, m; // n是节点数，m是操作数
    private static int[] parent; // 并查集父节点数组
    private static int[][] treeDiameter; // 存储每个树的直径的两个端点，treeDiameter[root][0]和treeDiameter[root][1]
    private static List<List<Integer>> graph; // 邻接表存储森林结构
    
    /**
     * 并查集的查找操作，带路径压缩
     * @param x 要查找的节点
     * @return 节点x所在树的根节点
     */
    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * BFS函数，从指定节点开始，找到距离最远的节点和最远距离
     * @param start 起始节点
     * @param visited 标记数组，用于在合并过程中避免越界
     * @return 包含最远节点和最远距离的数组，格式为[最远节点, 最远距离]
     */
    private static int[] bfs(int start, boolean[] visited) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> distance = new HashMap<>();
        
        queue.offer(start);
        distance.put(start, 0);
        visited[start] = true;
        
        int maxDistance = 0;
        int farthestNode = start;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    int newDistance = distance.get(current) + 1;
                    distance.put(neighbor, newDistance);
                    queue.offer(neighbor);
                    
                    if (newDistance > maxDistance) {
                        maxDistance = newDistance;
                        farthestNode = neighbor;
                    }
                }
            }
        }
        
        return new int[]{farthestNode, maxDistance};
    }
    
    /**
     * 计算两个节点之间的距离
     * @param u 起始节点
     * @param v 目标节点
     * @return u和v之间的距离
     */
    private static int getDistance(int u, int v) {
        boolean[] visited = new boolean[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> distance = new HashMap<>();
        
        queue.offer(u);
        distance.put(u, 0);
        visited[u] = true;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            if (current == v) {
                return distance.get(current);
            }
            
            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distance.put(neighbor, distance.get(current) + 1);
                    queue.offer(neighbor);
                }
            }
        }
        
        return -1; // 应该不会到达这里，因为u和v在同一棵树中
    }
    
    /**
     * 计算一棵树的直径
     * @param root 树的根节点
     * @return 树的直径的两个端点，格式为[端点1, 端点2]
     */
    private static int[] computeDiameter(int root) {
        boolean[] visited = new boolean[n + 1];
        // 第一次BFS找到距离root最远的节点u
        int[] result1 = bfs(root, visited);
        int u = result1[0];
        
        // 重置visited数组
        Arrays.fill(visited, false);
        // 第二次BFS找到距离u最远的节点v
        int[] result2 = bfs(u, visited);
        int v = result2[0];
        
        return new int[]{u, v};
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        n = scanner.nextInt();
        m = scanner.nextInt();
        
        // 初始化并查集
        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        // 初始化邻接表
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 初始化每个树的直径（初始时每个节点自身就是一棵树）
        treeDiameter = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            treeDiameter[i][0] = i;
            treeDiameter[i][1] = i;
        }
        
        // 处理每个操作
        for (int i = 0; i < m; i++) {
            int op = scanner.nextInt();
            if (op == 1) {
                // 连接操作
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                
                // 添加边
                graph.get(u).add(v);
                graph.get(v).add(u);
                
                // 合并两个集合
                int rootU = find(u);
                int rootV = find(v);
                if (rootU != rootV) {
                    parent[rootV] = rootU;
                    
                    // 计算新树的直径
                    int a1 = treeDiameter[rootU][0];
                    int a2 = treeDiameter[rootU][1];
                    int b1 = treeDiameter[rootV][0];
                    int b2 = treeDiameter[rootV][1];
                    
                    // 可能的四种路径
                    int d1 = getDistance(a1, a2); // 原u树的直径
                    int d2 = getDistance(b1, b2); // 原v树的直径
                    int d3 = getDistance(a1, b1); // a1到b1
                    int d4 = getDistance(a1, b2); // a1到b2
                    int d5 = getDistance(a2, b1); // a2到b1
                    int d6 = getDistance(a2, b2); // a2到b2
                    
                    // 找出最长的路径
                    int maxDistance = d1;
                    int[] newDiameter = {a1, a2};
                    
                    if (d2 > maxDistance) {
                        maxDistance = d2;
                        newDiameter = new int[]{b1, b2};
                    }
                    if (d3 > maxDistance) {
                        maxDistance = d3;
                        newDiameter = new int[]{a1, b1};
                    }
                    if (d4 > maxDistance) {
                        maxDistance = d4;
                        newDiameter = new int[]{a1, b2};
                    }
                    if (d5 > maxDistance) {
                        maxDistance = d5;
                        newDiameter = new int[]{a2, b1};
                    }
                    if (d6 > maxDistance) {
                        maxDistance = d6;
                        newDiameter = new int[]{a2, b2};
                    }
                    
                    // 更新新树的直径
                    treeDiameter[rootU] = newDiameter;
                }
            } else if (op == 2) {
                // 查询操作
                int u = scanner.nextInt();
                int root = find(u);
                int a = treeDiameter[root][0];
                int b = treeDiameter[root][1];
                // 计算直径长度并输出
                System.out.println(getDistance(a, b));
            }
        }
        scanner.close();
    }
}