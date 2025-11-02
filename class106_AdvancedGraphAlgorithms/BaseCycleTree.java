package class183;

import java.util.*;

/**
 * 基环树（内向基环树）算法实现
 * 
 * 基环树是一种特殊的图结构，它由一个环和连接在环上的若干棵树组成。
 * 每个基环树都有且仅有一个环，删除环中的任意一条边后，图变为一棵树。
 * 
 * 时间复杂度：O(n)，其中n是节点数
 * 空间复杂度：O(n)，用于存储图和辅助数组
 */
public class BaseCycleTree {
    private int n;
    private List<List<Integer>> graph;
    private boolean[] visited;
    private boolean[] inCycle;
    private List<Integer> cycle;
    private int[] parent;
    private int loopStart, loopEnd;
    
    /**
     * 构造函数
     * @param n 节点数
     */
    public BaseCycleTree(int n) {
        this.n = n;
        graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        visited = new boolean[n + 1];
        inCycle = new boolean[n + 1];
        parent = new int[n + 1];
        cycle = new ArrayList<>();
        loopStart = loopEnd = -1;
    }
    
    /**
     * 添加边
     * @param u 起点
     * @param v 终点
     */
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
    }
    
    /**
     * 使用DFS寻找环
     * @param u 当前节点
     * @return 是否找到环
     */
    private boolean dfs(int u) {
        visited[u] = true;
        for (int v : graph.get(u)) {
            if (!visited[v]) {
                parent[v] = u;
                if (dfs(v)) {
                    return true;
                }
            } else if (v != parent[u]) {  // 发现回边，说明找到了环
                loopStart = v;
                loopEnd = u;
                return true;
            }
        }
        return false;
    }
    
    /**
     * 寻找基环树中的环
     * @return 环中的节点列表
     */
    public List<Integer> findCycle() {
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                if (dfs(i)) {
                    // 从loopEnd回溯到loopStart，构建环
                    int u = loopEnd;
                    while (u != loopStart) {
                        cycle.add(u);
                        inCycle[u] = true;
                        u = parent[u];
                    }
                    cycle.add(loopStart);
                    inCycle[loopStart] = true;
                    Collections.reverse(cycle);
                    return cycle;
                }
            }
        }
        return cycle;
    }
    
    /**
     * 获取在环中的节点标记数组
     * @return 布尔数组，表示每个节点是否在环中
     */
    public boolean[] getInCycle() {
        return inCycle;
    }
    
    /**
     * 处理环上的子树，计算每个子树的信息
     * @return 每个节点子树的大小
     */
    public int[] processSubtrees() {
        int[] subtreeInfo = new int[n + 1];
        
        // 计算子树大小的DFS函数
        class SubtreeDFS {
            int dfsSubtree(int u, int parentNode) {
                int res = 1;  // 节点自身
                for (int v : graph.get(u)) {
                    if (v != parentNode && !inCycle[v]) {
                        res += dfsSubtree(v, u);
                    }
                }
                subtreeInfo[u] = res;
                return res;
            }
        }
        
        SubtreeDFS subtreeDFS = new SubtreeDFS();
        
        // 对环上的每个节点处理其子树
        for (int node : cycle) {
            subtreeDFS.dfsSubtree(node, -1);
        }
        
        return subtreeInfo;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 示例：创建一个基环树
        BaseCycleTree bct = new BaseCycleTree(5);
        bct.addEdge(1, 2);
        bct.addEdge(2, 3);
        bct.addEdge(3, 4);
        bct.addEdge(4, 5);
        bct.addEdge(5, 2);  // 形成环：2->3->4->5->2
        
        List<Integer> cycle = bct.findCycle();
        System.out.println("找到的环: " + cycle);
        
        int[] subtreeInfo = bct.processSubtrees();
        System.out.println("子树信息: " + Arrays.toString(subtreeInfo));
    }
}