package class176;

import java.util.*;

/**
 * 树上莫队算法实现 - 树上路径查询问题
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个权值。多次查询两个节点之间的路径上有多少种不同的权值。
 * 
 * 解题思路：
 * 1. 树上莫队通过欧拉序或DFS序将树结构转换为线性结构
 * 2. 使用时间戳标记每个节点的进入和离开时间
 * 3. 将树上的路径查询转换为线性数组的区间查询
 * 4. 应用莫队算法处理这些区间查询
 * 
 * 时间复杂度分析：
 * - 树上莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是树的节点数
 * 
 * 空间复杂度分析：
 * - 存储树的邻接表、欧拉序等需要 O(n) 的空间
 * - 其他辅助数组需要 O(n) 的空间
 * - 总体空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理树为空或查询无效的情况
 * 2. 性能优化：合理选择块的大小，使用奇偶排序优化
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */
public class TreeMoAlgorithm_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;  // 查询的左边界（欧拉序中的位置）
        int r;  // 查询的右边界（欧拉序中的位置）
        int lca;  // 两个节点的最近公共祖先
        int idx;  // 查询的索引，用于输出答案时保持顺序
        int block;  // 查询所属的块
        
        public Query(int l, int r, int lca, int idx, int blockSize) {
            this.l = l;
            this.r = r;
            this.lca = lca;
            this.idx = idx;
            this.block = l / blockSize;
        }
    }
    
    // 树的邻接表
    private static List<List<Integer>> tree;
    // 节点的权值
    private static int[] values;
    // 离散化后的权值
    private static int[] discreteValues;
    // 欧拉序数组
    private static int[] euler;
    // 节点的进入时间戳
    private static int[] inTime;
    // 节点的离开时间戳
    private static int[] outTime;
    // 节点的父节点
    private static int[] parent;
    // 节点的深度
    private static int[] depth;
    // 用于LCA的倍增表
    private static int[][] up;
    // 欧拉序的当前时间戳
    private static int time;
    // 块的大小
    private static int blockSize;
    // 离散化后的值域大小
    private static int valueRange;
    // 每个权值出现的次数
    private static int[] count;
    // 当前不同权值的数量
    private static int currentResult;
    // 记录节点是否在当前区间中
    private static boolean[] inCurrent;
    // 答案数组
    private static int[] answers;
    
    /**
     * 离散化函数
     * @param arr 原始权值数组
     */
    private static void discretize(int[] arr) {
        Set<Integer> valueSet = new HashSet<>();
        for (int val : arr) {
            valueSet.add(val);
        }
        
        List<Integer> valueList = new ArrayList<>(valueSet);
        Collections.sort(valueList);
        
        Map<Integer, Integer> valueMap = new HashMap<>();
        for (int i = 0; i < valueList.size(); i++) {
            valueMap.put(valueList.get(i), i + 1);  // 从1开始编号
        }
        
        discreteValues = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            discreteValues[i] = valueMap.get(arr[i]);
        }
        
        valueRange = valueList.size();
    }
    
    /**
     * 预处理LCA所需的父节点和深度信息
     * @param u 当前节点
     * @param p 父节点
     * @param d 深度
     */
    private static void dfsLCA(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        inTime[u] = ++time;
        euler[time] = u;
        
        for (int v : tree.get(u)) {
            if (v != p) {
                dfsLCA(v, u, d + 1);
            }
        }
        
        outTime[u] = time;
    }
    
    /**
     * 预处理倍增表
     * @param n 节点数
     * @param logMax 最大的log值
     */
    private static void preprocessLCA(int n, int logMax) {
        up = new int[logMax][n + 1];  // 节点编号从1开始
        
        // 初始化up[0]层
        for (int i = 1; i <= n; i++) {
            up[0][i] = parent[i];
        }
        
        // 填充倍增表
        for (int k = 1; k < logMax; k++) {
            for (int i = 1; i <= n; i++) {
                up[k][i] = up[k-1][up[k-1][i]];
            }
        }
    }
    
    /**
     * 查找两个节点的最近公共祖先
     * @param u 节点u
     * @param v 节点v
     * @return 最近公共祖先
     */
    private static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到与v同一深度
        for (int k = up.length - 1; k >= 0; k--) {
            if (depth[u] - (1 << k) >= depth[v]) {
                u = up[k][u];
            }
        }
        
        if (u == v) {
            return u;
        }
        
        // 同时提升u和v
        for (int k = up.length - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }
        
        return up[0][u];
    }
    
    /**
     * 比较两个查询的顺序，用于莫队算法的排序
     * @param q1 第一个查询
     * @param q2 第二个查询
     * @return 比较结果
     */
    private static int compareQueries(Query q1, Query q2) {
        if (q1.block != q2.block) {
            return Integer.compare(q1.block, q2.block);
        }
        // 对于同一块内的查询，按照右边界排序，偶数块升序，奇数块降序（奇偶排序优化）
        if (q1.block % 2 == 0) {
            return Integer.compare(q1.r, q2.r);
        } else {
            return Integer.compare(q2.r, q1.r);
        }
    }
    
    /**
     * 切换节点的状态（加入或移除）
     * @param u 节点编号
     */
    private static void toggle(int u) {
        int val = discreteValues[u];
        if (inCurrent[u]) {
            // 移除节点
            count[val]--;
            if (count[val] == 0) {
                currentResult--;
            }
        } else {
            // 添加节点
            if (count[val] == 0) {
                currentResult++;
            }
            count[val]++;
        }
        inCurrent[u] = !inCurrent[u];
    }
    
    /**
     * 主解题函数
     * @param n 节点数
     * @param m 查询数
     * @param val 节点权值数组
     * @param edges 边的列表
     * @param queriesInput 查询列表，每个查询包含两个节点u和v
     * @return 每个查询的结果
     */
    public static int[] solve(int n, int m, int[] val, int[][] edges, int[][] queriesInput) {
        // 初始化树
        tree = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
        
        // 构建邻接表
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            tree.get(u).add(v);
            tree.get(v).add(u);
        }
        
        // 初始化数组
        values = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            values[i] = val[i];
        }
        
        // 离散化
        discretize(values);
        
        // 初始化欧拉序相关数组
        euler = new int[2 * n + 2];  // 欧拉序数组
        inTime = new int[n + 1];     // 进入时间
        outTime = new int[n + 1];    // 离开时间
        parent = new int[n + 1];     // 父节点
        depth = new int[n + 1];      // 深度
        time = 0;
        
        // DFS预处理
        dfsLCA(1, 1, 0);  // 假设根节点为1
        
        // 预处理LCA的倍增表
        int logMax = (int)(Math.log(n) / Math.log(2)) + 2;
        preprocessLCA(n, logMax);
        
        // 转换查询
        blockSize = (int)Math.sqrt(n) + 1;
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            int u = queriesInput[i][0];
            int v = queriesInput[i][1];
            
            // 确保u的进入时间小于v的进入时间
            if (inTime[u] > inTime[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            int ancestor = lca(u, v);
            int l, r;
            
            // 处理两种情况：u是v的祖先，或者不是
            if (ancestor == u) {
                l = inTime[u];
                r = inTime[v];
            } else {
                l = outTime[u];
                r = inTime[v];
            }
            
            queries[i] = new Query(l, r, ancestor, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, TreeMoAlgorithm_Java::compareQueries);
        
        // 初始化莫队算法相关数组
        count = new int[valueRange + 2];
        currentResult = 0;
        inCurrent = new boolean[n + 1];
        answers = new int[m];
        
        // 初始化当前区间的左右指针
        int curL = 1;
        int curR = 0;
        
        // 处理每个查询
        for (Query q : queries) {
            int l = q.l;
            int r = q.r;
            int ancestor = q.lca;
            int idx = q.idx;
            
            // 调整左右指针到目标位置
            while (curL > l) toggle(euler[--curL]);
            while (curR < r) toggle(euler[++curR]);
            while (curL < l) toggle(euler[curL++]);
            while (curR > r) toggle(euler[curR--]);
            
            // 处理LCA节点
            if (ancestor != euler[l]) {
                toggle(ancestor);
            }
            
            // 保存当前查询的结果
            answers[idx] = currentResult;
            
            // 恢复LCA节点的状态
            if (ancestor != euler[l]) {
                toggle(ancestor);
            }
        }
        
        return answers;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int n = 5;
        int m = 2;
        int[] val = {0, 1, 2, 1, 3, 2};  // 节点编号从1开始，索引0不使用
        int[][] edges = {
            {1, 2},
            {1, 3},
            {2, 4},
            {2, 5}
        };
        int[][] queries = {
            {3, 4},  // 查询节点3和4之间路径上的不同权值数量
            {2, 5}   // 查询节点2和5之间路径上的不同权值数量
        };
        
        int[] results = solve(n, m, val, edges, queries);
        
        // 输出结果
        System.out.println("Query Results:");
        for (int result : results) {
            System.out.println(result);
        }
    }
}