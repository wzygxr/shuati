package class183;

import java.util.*;

/**
 * 圆方树算法实现
 * 
 * 圆方树是一种用于处理仙人掌图（Cactus Graph）的数据结构。
 * 仙人掌图是一种特殊的无向图，其中任意两条简单环最多只有一个公共顶点。
 * 圆方树将仙人掌图转化为一棵树结构，使得可以使用树型DP等树算法来解决仙人掌图上的问题。
 * 
 * 时间复杂度：O(n + m)，其中n是节点数，m是边数
 * 空间复杂度：O(n + m)，用于存储图和圆方树
 */
public class CircleSquareTree {
    private int n;  // 原图顶点数
    private int m;  // 圆方树顶点数
    private List<List<Integer>> graph;  // 原图
    private List<List<Integer>> squareGraph;  // 圆方树
    private int[] dfn;  // 深度优先搜索的时间戳
    private int[] low;  // 能够回溯到的最早的时间戳
    private Deque<Integer> stack;  // 栈，用于保存双连通分量
    private int cnt;  // 时间戳计数器
    private int id;  // 圆方树顶点编号
    
    /**
     * 构造函数
     * @param n 原图顶点数
     */
    public CircleSquareTree(int n) {
        this.n = n;
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        squareGraph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            squareGraph.add(new ArrayList<>());
        }
        dfn = new int[n + 1];
        low = new int[n + 1];
        stack = new ArrayDeque<>();
        cnt = 0;
        id = n;  // 方点编号从n+1开始
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        graph.get(v).add(u);
    }
    
    /**
     * Tarjan算法寻找双连通分量并构建圆方树
     * @param u 当前节点
     * @param parent 父节点
     */
    private void tarjan(int u, int parent) {
        cnt++;
        dfn[u] = low[u] = cnt;
        stack.push(u);
        
        for (int v : graph.get(u)) {
            if (v == parent) continue;
            
            if (dfn[v] == 0) {
                tarjan(v, u);
                low[u] = Math.min(low[u], low[v]);
                
                // 发现一个双连通分量
                if (low[v] >= dfn[u]) {
                    id++;
                    squareGraph.add(new ArrayList<>());
                    
                    int w = -1;
                    while (w != v) {
                        w = stack.pop();
                        squareGraph.get(w).add(id);
                        squareGraph.get(id).add(w);
                    }
                    
                    squareGraph.get(u).add(id);
                    squareGraph.get(id).add(u);
                }
            } else {
                // 回边，更新low值
                low[u] = Math.min(low[u], dfn[v]);
            }
        }
    }
    
    /**
     * 构建圆方树
     * @return 圆方树的邻接表表示
     */
    public List<List<Integer>> build() {
        for (int i = 1; i <= n; i++) {
            if (dfn[i] == 0) {
                tarjan(i, 0);
            }
        }
        
        m = id;
        return squareGraph;
    }
    
    /**
     * 获取圆方树的顶点数
     * @return 顶点数
     */
    public int getSize() {
        return m;
    }
    
    /**
     * 判断是否为方点
     * @param u 节点编号
     * @return 是否为方点
     */
    public boolean isSquare(int u) {
        return u > n;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例：创建一个简单的仙人掌图
        CircleSquareTree cst = new CircleSquareTree(4);
        cst.addEdge(1, 2);
        cst.addEdge(2, 3);
        cst.addEdge(3, 1);  // 形成一个三角形环
        cst.addEdge(3, 4);
        
        List<List<Integer>> squareGraph = cst.build();
        System.out.println("圆方树构建完成，顶点数: " + cst.getSize());
        
        // 输出圆方树的邻接表
        for (int i = 1; i <= cst.getSize(); i++) {
            System.out.println("节点 " + i + " 的邻居: " + squareGraph.get(i));
        }
    }
}