package class184;

import java.util.*;

/**
 * 支配树(Dominator Tree)实现 - Java版本
 * 
 * 支配树是图论中的一个重要概念，主要用于程序分析和编译器优化等领域。
 * 在有向图中，对于指定的源点s，如果从s到达节点w的所有路径都必须经过节点u，
 * 则称节点u支配节点w。
 * 
 * 本实现基于Lengauer-Tarjan算法，时间复杂度为O((V+E)log(V+E))
 * 
 * 应用场景：
 * 1. 编译器优化：控制流图分析，死代码消除，循环优化
 * 2. 程序分析：数据流分析，可达性分析
 * 3. 图论问题：关键节点识别，路径分析
 */
public class DominatorTree {
    private int n;                  // 节点数量
    private int root;               // 根节点
    private List<List<Integer>> graph;     // 原图邻接表
    private List<List<Integer>> reverseGraph; // 反向图邻接表
    private int[] dfn;              // DFS序
    private int[] id;               // DFS序到节点的映射
    private int[] fa;               // DFS树中的父节点
    private int[] semi;             // 半支配点
    private int[] idom;             // 立即支配点
    private int[] best;             // 并查集优化用
    private int dfsClock;           // DFS时钟
    private List<List<Integer>> bucket;    // bucket[v]存储semi[v]相同的节点
    private List<List<Integer>> tree;      // 支配树
    
    /**
     * 构造函数
     * @param n 节点数量
     * @param root 根节点
     */
    public DominatorTree(int n, int root) {
        this.n = n;
        this.root = root;
        this.graph = new ArrayList<>();
        this.reverseGraph = new ArrayList<>();
        this.bucket = new ArrayList<>();
        this.tree = new ArrayList<>();
        
        // 初始化邻接表
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
            reverseGraph.add(new ArrayList<>());
            bucket.add(new ArrayList<>());
            tree.add(new ArrayList<>());
        }
        
        // 初始化数组
        this.dfn = new int[n];
        this.id = new int[n];
        this.fa = new int[n];
        this.semi = new int[n];
        this.idom = new int[n];
        this.best = new int[n];
        
        // 初始化数组值
        Arrays.fill(dfn, -1);
        Arrays.fill(semi, -1);
        Arrays.fill(idom, -1);
        for (int i = 0; i < n; i++) {
            best[i] = i;
        }
        
        this.dfsClock = 0;
    }
    
    /**
     * 添加有向边
     * @param u 起点
     * @param v 终点
     */
    public void addEdge(int u, int v) {
        graph.get(u).add(v);
        reverseGraph.get(v).add(u);
    }
    
    /**
     * DFS遍历，构建DFS树
     * @param u 当前节点
     */
    private void dfs(int u) {
        dfn[u] = dfsClock;
        id[dfsClock] = u;
        dfsClock++;
        
        for (int v : graph.get(u)) {
            if (dfn[v] == -1) {
                fa[v] = u;
                dfs(v);
            }
        }
    }
    
    /**
     * 并查集查找操作（带路径压缩）
     * @param x 节点
     * @return 根节点
     */
    private int find(int x) {
        if (x == fa[x]) {
            return x;
        }
        
        int root = find(fa[x]);
        
        // 路径压缩优化
        if (semi[best[fa[x]]] < semi[best[x]]) {
            best[x] = best[fa[x]];
        }
        
        return fa[x] = root;
    }
    
    /**
     * 构建支配树
     */
    public void build() {
        // 1. DFS遍历，构建DFS树
        dfs(root);
        
        // 2. 从后向前处理每个节点
        for (int i = dfsClock - 1; i >= 0; i--) {
            int u = id[i];
            
            // 计算半支配点
            for (int v : reverseGraph.get(u)) {
                if (dfn[v] == -1) continue; // 节点v不在DFS树中
                
                if (dfn[v] < dfn[u]) {
                    // v是u的祖先
                    semi[u] = (semi[u] == -1) ? dfn[v] : Math.min(semi[u], dfn[v]);
                } else {
                    // v是u的后代，通过并查集找到v的祖先
                    find(v);
                    semi[u] = (semi[u] == -1) ? semi[best[v]] : Math.min(semi[u], semi[best[v]]);
                }
            }
            
            if (i > 0 && semi[u] != -1) {
                bucket.get(id[semi[u]]).add(u);
                
                // 处理bucket中的节点
                int w = fa[u];
                for (int v : bucket.get(w)) {
                    find(v);
                    if (semi[best[v]] == semi[v]) {
                        idom[v] = w;
                    } else {
                        idom[v] = best[v];
                    }
                }
                
                bucket.get(w).clear();
            }
        }
        
        // 3. 确定立即支配点
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            if (semi[u] != -1 && idom[u] != id[semi[u]]) {
                idom[u] = idom[idom[u]];
            }
        }
        
        // 4. 构建支配树
        for (int i = 1; i < dfsClock; i++) {
            int u = id[i];
            if (idom[u] != -1) {
                tree.get(idom[u]).add(u);
            }
        }
    }
    
    /**
     * 获取节点u的支配节点
     * @param u 节点
     * @return 支配节点列表
     */
    public List<Integer> getDominatedNodes(int u) {
        List<Integer> result = new ArrayList<>();
        if (dfn[u] == -1) return result; // 节点不存在
        
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(u);
        
        while (!queue.isEmpty()) {
            int v = queue.poll();
            result.add(v);
            
            for (int w : tree.get(v)) {
                queue.offer(w);
            }
        }
        
        return result;
    }
    
    /**
     * 检查节点u是否支配节点v
     * @param u 可能的支配节点
     * @param v 被支配节点
     * @return 是否支配
     */
    public boolean dominates(int u, int v) {
        if (dfn[u] == -1 || dfn[v] == -1) return false;
        
        // 从v向上追溯到根节点，检查是否经过u
        int current = v;
        while (current != root && current != -1) {
            if (current == u) return true;
            current = idom[current];
        }
        
        return current == u;
    }
    
    /**
     * 获取立即支配点
     * @param u 节点
     * @return 立即支配点，如果不存在返回-1
     */
    public int getImmediateDominator(int u) {
        if (dfn[u] == -1 || u == root) return -1;
        return idom[u];
    }
    
    /**
     * 获取支配树
     * @return 支配树的邻接表表示
     */
    public List<List<Integer>> getDominatorTree() {
        return tree;
    }
    
    /**
     * 打印支配树结构
     */
    public void printDominatorTree() {
        System.out.println("支配树结构:");
        for (int i = 0; i < n; i++) {
            if (!tree.get(i).isEmpty()) {
                System.out.print("节点 " + i + " 支配: ");
                for (int child : tree.get(i)) {
                    System.out.print(child + " ");
                }
                System.out.println();
            }
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 支配树测试 ===");
        
        // 创建测试图
        // 0 -> 1 -> 2 -> 4
        //  \-> 3 --^
        DominatorTree dt = new DominatorTree(5, 0);
        dt.addEdge(0, 1);
        dt.addEdge(0, 3);
        dt.addEdge(1, 2);
        dt.addEdge(3, 2);
        dt.addEdge(2, 4);
        
        // 构建支配树
        dt.build();
        
        // 打印结果
        dt.printDominatorTree();
        
        // 测试支配关系
        System.out.println("\n支配关系测试:");
        System.out.println("节点0是否支配节点4: " + dt.dominates(0, 4));
        System.out.println("节点1是否支配节点4: " + dt.dominates(1, 4));
        System.out.println("节点2是否支配节点4: " + dt.dominates(2, 4));
        
        // 测试立即支配点
        System.out.println("\n立即支配点:");
        for (int i = 1; i < 5; i++) {
            int idom = dt.getImmediateDominator(i);
            System.out.println("节点" + i + "的立即支配点: " + (idom == -1 ? "无" : idom));
        }
        
        // 测试被支配节点
        System.out.println("\n被支配节点:");
        for (int i = 0; i < 5; i++) {
            List<Integer> dominated = dt.getDominatedNodes(i);
            System.out.println("节点" + i + "支配的节点: " + dominated);
        }
    }
}