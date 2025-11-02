package class121;

// Codeforces 1499F Diameter Cuts
// 题目：给定一棵n个节点的树和一个整数k，计算有多少个连通子图的直径恰好为k。
// 树的直径是指树中任意两点之间最长的简单路径。
// 来源：Codeforces Educational Round 106 Problem F
// 链接：https://codeforces.com/contest/1499/problem/F

// 算法标签：树、动态规划、树形DP
// 难度：困难
// 时间复杂度：O(n^2)，其中n是树中节点的数量
// 空间复杂度：O(n^2)，用于存储DP状态

// 相关题目：
// - LeetCode 543. 二叉树的直径
// - LeetCode 1245. Tree Diameter (无向树的直径)
// - LeetCode 1522. Diameter of N-Ary Tree (N叉树的直径)
// - SPOJ PT07Z - Longest path in a tree (树中最长路径)
// - CSES 1131 - Tree Diameter (树的直径)
// - 51Nod 2602 - 树的直径
// - 洛谷 U81904 树的直径
// - AtCoder ABC221F - Diameter Set

// 解题思路：
// 使用树形DP，对每个节点计算子树中满足条件的连通子图数量。
// 状态定义：f[u][i] 表示以u为根的子树中，所有连通子图都合法且从u向下延伸的最长路径长度为i的方案数

import java.io.*;
import java.util.*;

public class Codeforces1499F_DiameterCuts {
    
    static final int MAXN = 5001;
    static final int MOD = 998244353;
    
    // 邻接表存储树
    static ArrayList<Integer>[] graph;
    static int n, k;  // 节点数和目标直径
    
    // DP状态
    // f[u][i] 表示以u为根的子树中，所有连通子图都合法且从u向下延伸的最长路径长度为i的方案数
    static long[][] f;
    static int[] size;  // 子树大小
    static long[] g;    // 临时数组用于DP转移
    
    /**
     * 树形DP求解
     * 
     * 算法思路：
     * 1. 对每个节点u，维护f[u][i]表示以u为根的子树中，所有连通子图都合法且从u向下延伸的最长路径长度为i的方案数
     * 2. 对于每个节点u的子节点v，合并u和v的子树信息
     * 3. 合并时考虑连接u和v需要增加1条边，所以路径长度为max(i, j+1)
     * 4. 如果合并后的路径长度超过k，则不合法
     * 
     * @param u 当前节点
     * @param parent 父节点
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    static void dfs(int u, int parent) {
        // 初始化当前节点的DP状态
        size[u] = 1;
        f[u][0] = 1;  // 只选择节点u本身
        
        // 遍历所有子节点
        for (int v : graph[u]) {
            if (v != parent) {
                dfs(v, u);
                
                // DP转移
                // 清空临时数组
                Arrays.fill(g, 0);
                
                // 合并u和v的子树信息
                for (int i = 0; i <= Math.min(k, size[u]); i++) {
                    for (int j = 0; j <= Math.min(size[v], k - i); j++) {
                        // 合并后的最长路径长度为max(i, j+1)
                        // j+1是因为连接u和v需要增加1条边
                        int newLength = Math.max(i, j + 1);
                        if (newLength <= k) {
                            g[newLength] = (g[newLength] + f[u][i] * f[v][j]) % MOD;
                        }
                    }
                }
                
                // 更新u的子树大小和DP状态
                size[u] += size[v];
                for (int i = 0; i <= Math.min(k, size[u]); i++) {
                    f[u][i] = g[i];
                }
            }
        }
        
        // 计算以u为根的子树中所有合法连通子图的总数
        long sum = 0;
        for (int i = 0; i <= Math.min(k, size[u]); i++) {
            sum = (sum + f[u][i]) % MOD;
        }
        
        // 如果不是根节点，需要调整DP状态
        if (u != 1) {
            // 将所有路径长度加1（因为要连接到父节点）
            for (int i = Math.min(k, size[u]); i > 0; i--) {
                f[u][i] = f[u][i - 1];
            }
            // 不连接到父节点的情况
            f[u][0] = sum;
        }
    }
    
    /**
     * 主方法
     * 
     * 输入格式：
     * - 第一行包含两个整数n和k，表示树中节点的数量和目标直径
     * - 接下来n-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
     * 
     * 输出格式：
     * - 输出一个整数，表示满足条件的连通子图数量，对998244353取模
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数和目标直径
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        k = Integer.parseInt(line[1]);
        
        // 初始化数据结构
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        f = new long[n + 1][n + 1];
        size = new int[n + 1];
        g = new long[n + 1];
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 执行树形DP
        dfs(1, 0);
        
        // 计算并输出结果
        long result = 0;
        for (int i = 0; i <= k; i++) {
            result = (result + f[1][i]) % MOD;
        }
        out.println(result);
        
        out.flush();
        out.close();
        br.close();
    }
}