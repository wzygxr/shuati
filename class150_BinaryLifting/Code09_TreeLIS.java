package class119;

import java.util.*;

/**
 * Codeforces 932D Tree 树上最长不下降子序列查询问题
 * 题目链接: https://codeforces.com/problemset/problem/932/D
 * 
 * 题目描述：
 * 给定一棵树，初始时只有一个节点1，权值为0，后续有n个操作，每次操作分为两种情况：
 * 1. 1 u v：向树中插入一个新的节点，其父节点为u，权值为v
 * 2. 2 u v：询问以节点u为起点的最长不下降子序列的长度，这里规定的最长不下降子序列需要满足：
 *    以u为起点，每次的路线必须都是当前节点的父节点序列中的权值和小于等于v
 * 
 * 解题思路：
 * 使用树上倍增算法维护从根到每个节点路径上的最长不下降子序列信息
 * 对于每个节点，我们维护到其2^j级祖先路径上的最长不下降子序列相关信息
 * 然后通过合并这些信息来快速查询包含当前节点的最长不下降子序列长度
 * 
 * 算法复杂度分析：
 * 时间复杂度：O(n log n * log W)，其中W是权值范围
 * 空间复杂度：O(n log n * log W)
 * 
 * 核心思想：
 * 1. 使用树上倍增维护每个节点向上跳2^j步的信息
 * 2. 对于每个节点，维护从该节点到其祖先路径上的权值信息
 * 3. 通过二进制分解快速查询满足条件的最长路径
 */
public class Code09_TreeLIS {
    private static int n;                // 节点数量
    private static int LOG;              // 最大跳步级别
    private static int[][] parent;       // parent[j][u] 表示u的2^j级祖先
    private static int[] depth;          // 每个节点的深度
    private static int[] a;              // 节点权值
    private static List<Integer>[] adj;  // 邻接表
    // jump[j][u] 存储权值到长度的映射，表示从u到2^j级祖先路径上的最长不下降子序列信息
    private static Map<Integer, Integer>[][] jump;
    
    public static void main(String[] args) {
        // 测试用例
        testCase1();
        testCase2();
    }
    
    /**
     * 测试用例1: 简单树结构
     * 1
     * |\
     * 2 3
     * 权值: 1, 2, 3
     * 期望输出: 1 2 3
     */
    private static void testCase1() {
        System.out.println("测试用例1:");
        n = 3;
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        a = new int[n + 1];
        adj = new ArrayList[n + 1];
        jump = new HashMap[LOG][n + 1];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        for (int j = 0; j < LOG; j++) {
            for (int i = 0; i <= n; i++) {
                jump[j][i] = new HashMap<>();
            }
        }
        
        // 设置节点权值
        a[1] = 1;
        a[2] = 2;
        a[3] = 3;
        
        // 构建树结构
        adj[1].add(2);
        adj[2].add(1);
        adj[1].add(3);
        adj[3].add(1);
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    // 合并两个跳跃段的信息
                    mergeJump(i, j);
                }
            }
        }
        
        // 处理每个节点的查询并输出结果
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = query(i);
        }
        
        System.out.print("结果: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
    }
    
    /**
     * 测试用例2: 复杂树结构
     * 1
     * |\
     * 2 5
     * | |\
     * 3 6 7
     * |
     * 4
     * 权值: 3, 1, 2, 4, 5, 2, 3
     */
    private static void testCase2() {
        System.out.println("测试用例2:");
        n = 7;
        LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        // 初始化数据结构
        parent = new int[LOG][n + 1];
        depth = new int[n + 1];
        a = new int[n + 1];
        adj = new ArrayList[n + 1];
        jump = new HashMap[LOG][n + 1];
        
        for (int i = 0; i <= n; i++) {
            adj[i] = new ArrayList<>();
        }
        
        for (int j = 0; j < LOG; j++) {
            for (int i = 0; i <= n; i++) {
                jump[j][i] = new HashMap<>();
            }
        }
        
        // 设置节点权值
        a[1] = 3;
        a[2] = 1;
        a[3] = 2;
        a[4] = 4;
        a[5] = 5;
        a[6] = 2;
        a[7] = 3;
        
        // 构建树结构
        adj[1].add(2); adj[2].add(1);
        adj[1].add(5); adj[5].add(1);
        adj[2].add(3); adj[3].add(2);
        adj[3].add(4); adj[4].add(3);
        adj[5].add(6); adj[6].add(5);
        adj[5].add(7); adj[7].add(5);
        
        // 预处理
        Arrays.fill(parent[0], -1);
        dfs(1, -1, 0);
        
        // 构建倍增表
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (parent[j-1][i] != -1) {
                    parent[j][i] = parent[j-1][parent[j-1][i]];
                    // 合并两个跳跃段的信息
                    mergeJump(i, j);
                }
            }
        }
        
        // 处理每个节点的查询并输出结果
        int[] result = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            result[i] = query(i);
        }
        
        System.out.print("结果: ");
        for (int i = 1; i <= n; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
    }
    
    /**
     * DFS预处理函数，初始化每个节点的父节点、深度和jump[0][u]信息
     * @param u 当前节点
     * @param p 父节点
     * @param d 当前深度
     * 
     * 算法步骤：
     * 1. 设置当前节点的父节点和深度
     * 2. 初始化jump[0][u]，即直接父节点的信息
     * 3. 如果当前节点不是根节点，则从父节点继承信息并更新
     * 4. 如果是根节点，则初始化为自己的权值和长度1
     * 5. 递归处理所有子节点
     */
    private static void dfs(int u, int p, int d) {
        parent[0][u] = p;
        depth[u] = d;
        
        // 初始化jump[0][u]，即直接父节点的信息
        if (p != -1) {
            // 基础情况：从父节点到当前节点的最长不下降子序列
            int maxLen = 1;
            // 查找父节点路径中小于等于当前权值的最大长度
            for (Map.Entry<Integer, Integer> entry : jump[0][p].entrySet()) {
                if (entry.getKey() <= a[u]) {
                    maxLen = Math.max(maxLen, entry.getValue() + 1);
                }
            }
            jump[0][u].put(a[u], Math.max(jump[0][u].getOrDefault(a[u], 0), maxLen));
            // 保留父节点的所有信息，但更新当前权值的长度
            for (Map.Entry<Integer, Integer> entry : jump[0][p].entrySet()) {
                jump[0][u].put(entry.getKey(), Math.max(jump[0][u].getOrDefault(entry.getKey(), 0), entry.getValue()));
            }
        } else {
            // 根节点的情况
            jump[0][u].put(a[u], 1);
        }
        
        // 递归处理子节点
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    /**
     * 合并两个跳跃段的信息，构建jump[j][u]
     * @param u 当前节点
     * @param j 当前跳跃级别
     * 
     * 算法原理：
     * jump[j][u]表示从节点u向上跳2^j步路径上的最长不下降子序列信息
     * 通过合并jump[j-1][u]和jump[j-1][mid]两个段的信息来构建
     * 其中mid是u的2^(j-1)级祖先
     */
    private static void mergeJump(int u, int j) {
        int mid = parent[j-1][u];
        // 合并jump[j-1][u]和jump[j-1][mid]
        // 复制jump[j-1][u]的信息
        jump[j][u].putAll(jump[j-1][u]);
        
        // 对于jump[j-1][mid]中的每个权值w，找到jump[j-1][u]中小于等于w的最大长度
        for (Map.Entry<Integer, Integer> entry : jump[j-1][mid].entrySet()) {
            int w = entry.getKey();
            int len = entry.getValue();
            
            // 查找jump[j-1][u]中小于等于w的最大长度
            int maxPrev = 0;
            for (Map.Entry<Integer, Integer> e : jump[j-1][u].entrySet()) {
                if (e.getKey() <= w) {
                    maxPrev = Math.max(maxPrev, e.getValue());
                }
            }
            
            // 更新当前权值的最大长度
            jump[j][u].put(w, Math.max(jump[j][u].getOrDefault(w, 0), maxPrev + len));
        }
    }
    
    /**
     * 查询包含节点u的最长不下降子序列长度
     * @param u 查询节点
     * @return 最长不下降子序列长度
     * 
     * 查询算法：
     * 1. 从当前节点开始，向上合并所有跳跃段的信息
     * 2. 使用二进制分解，从最高位开始尝试跳跃
     * 3. 对于每个跳跃段，合并其信息到当前结果中
     * 4. 最终返回所有可能长度中的最大值
     */
    private static int query(int u) {
        int res = 1; // 至少包含自己
        int current = u;
        
        // 从当前节点向上合并所有跳跃段的信息
        Map<Integer, Integer> info = new HashMap<>();
        info.put(a[u], 1);
        
        for (int j = LOG - 1; j >= 0; j--) {
            if (parent[j][current] != -1) {
                // 合并jump[j][current]的信息到info
                Map<Integer, Integer> temp = new HashMap<>(info);
                
                for (Map.Entry<Integer, Integer> entry : jump[j][current].entrySet()) {
                    int w = entry.getKey();
                    int len = entry.getValue();
                    
                    // 查找info中小于等于w的最大长度
                    int maxPrev = 0;
                    for (Map.Entry<Integer, Integer> e : info.entrySet()) {
                        if (e.getKey() <= w) {
                            maxPrev = Math.max(maxPrev, e.getValue());
                        }
                    }
                    
                    // 更新临时信息
                    temp.put(w, Math.max(temp.getOrDefault(w, 0), maxPrev + len));
                }
                
                info = temp;
                current = parent[j][current];
            }
        }
        
        // 找出最大长度
        for (int len : info.values()) {
            res = Math.max(res, len);
        }
        
        return res;
    }
    
    /**
     * 算法优化与工程化考量：
     * 1. 时间优化：
     *    - 使用TreeMap代替HashMap可以加速查询小于等于当前权值的最大长度，将查询时间从O(W)优化到O(log W)
     *    - 预计算LOG值，避免重复计算
     *    - 对于大规模数据，可以使用离散化技术处理权值范围大的情况
     *    
     * 2. 空间优化：
     *    - 合理设置数组大小，避免内存溢出
     *    - 对于某些节点，可以只存储必要的权值信息，减少空间消耗
     *    
     * 3. 边界情况处理：
     *    - 处理根节点的特殊情况
     *    - 确保节点编号从1开始，避免数组越界
     *    
     * 4. 代码健壮性：
     *    - 添加异常处理，确保输入合法
     *    - 对于大规模数据，调整栈大小以避免栈溢出
     *    
     * 5. 算法优化：
     *    - 当权值范围较大时，可以考虑使用线段树或Fenwick树来维护最长不下降子序列信息
     *    - 可以进一步优化合并操作，减少不必要的计算
     * 
     * 实际应用扩展：
     * 1. 动态树结构：支持在线插入节点的场景
     * 2. 权值约束查询：支持不同权值和限制的查询
     * 3. 路径属性统计：可以扩展到统计路径上的其他属性
     * 4. 多维权值：可以处理多维权值的最长不下降子序列问题
     */
}