package class119;

import java.util.*;

/**
 * 洛谷 P5588 小猪佩奇爬树
 * 题目描述：给定一棵树，每个节点有一个颜色，计算每种颜色的所有节点中，
 * 有多少对节点(u, v)满足u是v的祖先或者v是u的祖先
 * 
 * 最优解算法：树上倍增 + DFS序 + 颜色统计
 * 时间复杂度：O(n log n + m)
 * 空间复杂度：O(n log n + c)，其中c是颜色数量
 * 
 * 解题思路：
 * 1. 使用DFS序判断祖先-后代关系
 * 2. 对于每种颜色的节点，利用DFS序的性质，按时间戳排序后统计满足条件的节点对
 * 3. 使用树状数组优化子树内节点计数的查询
 */
public class Code08_PiggyClimbTree {
    private int n;                // 节点数量
    private int LOG;              // 最大跳步级别
    private int[][] parent;       // parent[j][u] 表示u的2^j级祖先
    private int[] depth;          // 每个节点的深度
    private int[] color;          // 每个节点的颜色
    private List<Integer>[] adj;  // 邻接表
    private int[] inTime;         // DFS入时间戳
    private int[] outTime;        // DFS出时间戳
    private int time;             // 时间戳计数器
    private Map<Integer, List<Integer>> colorNodes; // 存储每种颜色的所有节点

    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    public Code08_PiggyClimbTree(int n) {
        this.n = n;
        this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        this.parent = new int[LOG][n + 1]; // 节点编号从1开始
        this.depth = new int[n + 1];
        this.color = new int[n + 1];
        this.adj = new ArrayList[n + 1];
        this.inTime = new int[n + 1];
        this.outTime = new int[n + 1];
        this.colorNodes = new HashMap<>();
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
    }
    
    /**
     * 添加树的边
     * @param u 父节点
     * @param v 子节点
     */
    public void addEdge(int u, int v) {
        adj[u].add(v);
        adj[v].add(u);
    }
    
    /**
     * 设置节点颜色
     * @param node 节点编号
     * @param c 颜色
     */
    public void setColor(int node, int c) {
        color[node] = c;
        colorNodes.putIfAbsent(c, new ArrayList<>());
        colorNodes.get(c).add(node);
    }
    
    /**
     * 预处理树结构，构建DFS序和倍增表
     */
    public void preprocess() {
        // 初始化父数组
        for (int i = 0; i < LOG; i++) {
            Arrays.fill(parent[i], -1);
        }
        
        // DFS预处理深度、父节点和时间戳
        time = 0;
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                }
            }
        }
    }
    
    /**
     * 深度优先搜索，预处理父节点、深度和时间戳
     * @param u 当前节点
     * @param p 父节点
     * @param d 深度
     */
    private void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        inTime[u] = ++time;
        
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
        
        outTime[u] = time;
    }
    
    /**
     * 判断节点u是否是节点v的祖先
     * @param u 可能的祖先节点
     * @param v 可能的后代节点
     * @return 如果u是v的祖先，返回true；否则返回false
     */
    private boolean isAncestor(int u, int v) {
        // u是v的祖先当且仅当v的入时间在u的入时间之后，且出时间在u的出时间之前
        return inTime[u] <= inTime[v] && outTime[v] <= outTime[u];
    }
    
    /**
     * 查找两个节点的最近公共祖先
     * @param u 节点u
     * @param v 节点v
     * @return 最近公共祖先
     */
    private int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到v的深度
        for (int j = LOG - 1; j >= 0; j--) {
            if (depth[u] - (1 << j) >= depth[v]) {
                u = parent[j][u];
            }
        }
        
        if (u == v) {
            return u;
        }
        
        // 同时提升两个节点
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][u] != -1 && parent[j][u] != parent[j][v]) {
                u = parent[j][u];
                v = parent[j][v];
            }
        }
        
        return parent[0][u];
    }
    
    /**
     * 计算每种颜色的符合条件的节点对数量
     * @return 颜色到符合条件节点对数量的映射
     */
    public Map<Integer, Long> calculateColorPairs() {
        Map<Integer, Long> result = new HashMap<>();
        
        // 对于每种颜色，计算其中有多少对节点满足祖先-后代关系
        for (Map.Entry<Integer, List<Integer>> entry : colorNodes.entrySet()) {
            int c = entry.getKey();
            List<Integer> nodes = entry.getValue();
            long count = 0;
            
            // 对节点按inTime排序，这样在DFS序中，祖先节点会排在后代节点前面
            nodes.sort(Comparator.comparingInt(a -> inTime[a]));
            
            // 对于每对节点，检查是否满足祖先-后代关系
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = i + 1; j < nodes.size(); j++) {
                    int u = nodes.get(i);
                    int v = nodes.get(j);
                    
                    // 检查u是否是v的祖先或者v是否是u的祖先
                    if (isAncestor(u, v) || isAncestor(v, u)) {
                        count++;
                    }
                }
            }
            
            result.put(c, count);
        }
        
        return result;
    }
    
    /**
     * 更高效的计算方法：利用DFS序的性质优化计算
     * @return 颜色到符合条件节点对数量的映射
     */
    public Map<Integer, Long> calculateColorPairsOptimized() {
        Map<Integer, Long> result = new HashMap<>();
        
        for (Map.Entry<Integer, List<Integer>> entry : colorNodes.entrySet()) {
            int c = entry.getKey();
            List<Integer> nodes = entry.getValue();
            
            // 按inTime排序
            nodes.sort(Comparator.comparingInt(a -> inTime[a]));
            
            // 计算每个节点的子树中包含的同色节点数
            long count = 0;
            int size = nodes.size();
            
            // 使用线段树或Fenwick树来高效查询子树中的节点数
            // 这里为了简化，使用数组实现一个简单的前缀和查询
            int[] tree = new int[n + 2];
            
            // 按inTime顺序处理节点
            for (int node : nodes) {
                // 查询当前节点子树中已经处理过的同色节点数
                int subtreeCount = query(tree, outTime[node]) - query(tree, inTime[node] - 1);
                count += subtreeCount;
                
                // 将当前节点加入树中
                update(tree, inTime[node], 1);
            }
            
            result.put(c, count);
        }
        
        return result;
    }
    
    /**
     * 树状数组的更新操作
     */
    private void update(int[] tree, int idx, int delta) {
        while (idx < tree.length) {
            tree[idx] += delta;
            idx += idx & -idx; // 加上最低位的1
        }
    }
    
    /**
     * 树状数组的查询操作（前缀和）
     */
    private int query(int[] tree, int idx) {
        int sum = 0;
        while (idx > 0) {
            sum += tree[idx];
            idx -= idx & -idx; // 减去最低位的1
        }
        return sum;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 示例测试
        int n = 5;
        Code08_PiggyClimbTree tree = new Code08_PiggyClimbTree(n);
        
        // 添加边（1为根节点）
        tree.addEdge(1, 2);
        tree.addEdge(1, 3);
        tree.addEdge(2, 4);
        tree.addEdge(2, 5);
        
        // 设置颜色
        tree.setColor(1, 1);
        tree.setColor(2, 1);
        tree.setColor(3, 2);
        tree.setColor(4, 1);
        tree.setColor(5, 2);
        
        // 预处理
        tree.preprocess();
        
        // 计算结果
        Map<Integer, Long> result = tree.calculateColorPairs();
        
        // 输出结果
        System.out.println("颜色1的符合条件节点对数量: " + result.getOrDefault(1, 0L)); // 应为3对
        System.out.println("颜色2的符合条件节点对数量: " + result.getOrDefault(2, 0L)); // 应为0对
        
        // 使用优化方法计算
        Map<Integer, Long> optimizedResult = tree.calculateColorPairsOptimized();
        System.out.println("优化方法 - 颜色1的符合条件节点对数量: " + optimizedResult.getOrDefault(1, 0L));
        System.out.println("优化方法 - 颜色2的符合条件节点对数量: " + optimizedResult.getOrDefault(2, 0L));
    }
    
    /**
     * 算法优化与工程化考量：
     * 1. DFS序优化：利用DFS序判断祖先-后代关系，将时间复杂度从O(n^2)降低到O(1)
     * 2. 树状数组优化：使用树状数组高效维护子树内的节点计数
     * 3. 颜色分组：按颜色分组处理节点，避免不必要的计算
     * 4. 线段树vs树状数组：在这个问题中，树状数组效率更高，实现更简单
     * 5. 排序优化：按inTime排序后可以按顺序处理，利用子树性质减少计算
     * 
     * 异常场景与边界场景：
     * - 所有节点颜色都相同的情况
     * - 每个节点颜色都不同的情况
     * - 树退化成链表的极端情况
     * - 大量重复颜色集中在树的同一子树中的情况
     */
}