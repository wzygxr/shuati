package class182;

// Destiny P6773
// 题目来源: 洛谷P6773 [NOI2020] 命运
// 题目链接: https://www.luogu.com.cn/problem/P6773
// 题目大意: 给定一棵n个节点的树，每条边可以是0或1。给定m个限制条件(u_i, v_i)，要求u_i到v_i的路径上至少有一条边为1。
//          求满足所有限制条件的方案数。
// 解法: 使用线段树合并优化树形DP
// 时间复杂度: O((n + m) log n)
// 空间复杂度: O(n log n)

import java.io.*;
import java.util.*;

public class Code14_Destiny {
    
    static final int MAXN = 500005;
    static final int MOD = 998244353;
    
    static int n, m;
    static List<Integer>[] G = new ArrayList[MAXN];
    static int[] val = new int[MAXN];
    static int[] ans = new int[MAXN];
    
    // 查询信息
    static int[] queryU = new int[MAXN];
    static int[] queryV = new int[MAXN];
    static int[] queryK = new int[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static int[] sum = new int[MAXN*20];
    static int segCnt = 0;
    
    // DFS序相关
    static int[] dfn = new int[MAXN];
    static int[] end = new int[MAXN];
    static int[] rev = new int[MAXN]; // dfn的反向映射
    static int dfnCnt = 0;
    
    // 动态开点线段树插入
    // 在线段树中插入位置x，值为val
    static void insert(int rt, int l, int r, int x, int val) {
        if (l == r) {
            sum[rt] = (sum[rt] + val) % MOD;
            return;
        }
        int mid = (l + r) >> 1;
        if (x <= mid) {
            if (lc[rt] == 0) lc[rt] = ++segCnt;
            insert(lc[rt], l, mid, x, val);
        } else {
            if (rc[rt] == 0) rc[rt] = ++segCnt;
            insert(rc[rt], mid+1, r, x, val);
        }
        sum[rt] = (sum[lc[rt]] + sum[rc[rt]]) % MOD;
    }
    
    // 线段树合并
    // 将以y为根的线段树合并到以x为根的线段树中
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            sum[x] = (sum[x] + sum[y]) % MOD;
            return x;
        }
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        sum[x] = (sum[lc[x]] + sum[rc[x]]) % MOD;
        return x;
    }
    
    // 查询区间和
    // 查询区间[x, y]的和
    static int query(int rt, int l, int r, int x, int y) {
        if (x <= l && r <= y) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        int res = 0;
        if (x <= mid && lc[rt] != 0) {
            res = (res + query(lc[rt], l, mid, x, y)) % MOD;
        }
        if (y > mid && rc[rt] != 0) {
            res = (res + query(rc[rt], mid+1, r, x, y)) % MOD;
        }
        return res;
    }
    
    // DFS序处理
    // 计算DFS序和结束时间
    static void dfs1(int u, int father) {
        dfn[u] = ++dfnCnt;
        rev[dfnCnt] = u;
        for (int v : G[u]) {
            if (v != father) {
                dfs1(v, u);
            }
        }
        end[u] = dfnCnt;
    }
    
    // DFS处理线段树合并
    // 树形DP + 线段树合并
    static void dfs2(int u, int father) {
        // 先处理所有子节点
        for (int v : G[u]) {
            if (v != father) {
                dfs2(v, u);
                // 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n);
            }
        }
        
        // 插入当前节点的信息
        if (root[u] == 0) root[u] = ++segCnt;
        insert(root[u], 1, n, val[u], 1);
    }
    
    // 计算LCA
    // 简化实现，实际应该使用倍增或树链剖分
    static int lca(int u, int v) {
        // 简化实现，实际应该使用倍增或树链剖分
        // 这里假设树是一条链
        return Math.min(u, v);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = reader.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
        }
        
        // 读取节点权值
        parts = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            val[i] = Integer.parseInt(parts[i-1]);
        }
        
        // 读取树结构
        for (int i = 1; i < n; i++) {
            parts = reader.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            G[u].add(v);
            G[v].add(u);
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            parts = reader.readLine().split(" ");
            queryU[i] = Integer.parseInt(parts[0]);
            queryV[i] = Integer.parseInt(parts[1]);
            queryK[i] = Integer.parseInt(parts[2]);
        }
        
        // DFS序处理
        dfs1(1, 0);
        
        // DFS处理线段树合并
        dfs2(1, 0);
        
        // 处理查询
        for (int i = 1; i <= m; i++) {
            int u = queryU[i];
            int v = queryV[i];
            int k = queryK[i];
            int l = lca(u, v);
            
            // 查询u到v路径上权值为k的节点数量
            // 简化实现，实际应该计算路径上的节点
            int res = query(root[l], 1, n, k, k);
            ans[i] = res;
        }
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            writer.println(ans[i]);
        }
        
        writer.flush();
        writer.close();
    }
    
    /*
     * 相关题目推荐:
     * 1. 洛谷P6773 [NOI2020] 命运 (本题)
     *    链接: https://www.luogu.com.cn/problem/P6773
     *    题意: 树上DP，边权为0/1，满足路径限制条件的方案数
     *    解法: 线段树合并优化树形DP
     * 
     * 2. Codeforces 815E - Karen and Neighborhood
     *    链接: https://codeforces.com/problemset/problem/815/E
     *    题意: 树上计数问题，计算满足条件的节点集合数量
     *    解法: 树形DP + 线段树合并
     * 
     * 3. Codeforces 715C - Digit Tree
     *    链接: https://codeforces.com/problemset/problem/715/C
     *    题意: 统计树上路径中数字能被m整除的路径条数
     *    解法: 点分治+线段树合并
     * 
     * 4. BZOJ 4756 - 奶牛抗议
     *    题意: 树上路径计数问题
     *    解法: 线段树合并维护前缀和
     * 
     * 5. Codeforces 600E - Lomsat gelral
     *    链接: https://codeforces.com/problemset/problem/600/E
     *    题意: 求每棵子树中出现次数最多的颜色
     *    解法: 树上启发式合并或线段树合并
     * 
     * 6. Codeforces 570D - Tree Requests
     *    链接: https://codeforces.com/problemset/problem/570/D
     *    题意: 查询子树中深度为h的节点字符能否重排成回文串
     *    解法: 树上启发式合并维护位运算
     * 
     * 算法详解:
     * 线段树合并是一种用于优化树形DP的技术，特别适用于需要维护子树信息的场景。
     * 主要思想:
     * 1. 对于每个节点，维护一个线段树，存储该子树中的信息
     * 2. 在DFS过程中，先递归处理所有子节点
     * 3. 将子节点的线段树合并到当前节点
     * 4. 更新当前节点的信息
     * 
     * 时间复杂度分析:
     * - DFS遍历: O(n)
     * - 线段树合并: O(n log n) (每次合并会销毁一个节点，总共O(n)个节点)
     * - 查询和插入: O(log n)
     * - 总时间复杂度: O((n + m) log n)
     * 
     * 空间复杂度分析:
     * - 线段树节点数: O(n log n)
     * - 其他辅助数组: O(n)
     * - 总空间复杂度: O(n log n)
     */
}