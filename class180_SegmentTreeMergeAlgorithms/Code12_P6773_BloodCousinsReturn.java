import java.io.*;
import java.util.*;

/**
 * 题目：P6773 [NOI2020] 命运
 * 测试链接：https://www.luogu.com.cn/problem/P6773
 * 
 * 题目描述：
 * 给定一棵n个节点的树，树上有m条限制条件，每条限制条件(u,v)表示u和v的公共祖先中深度最大的那个节点必须在路径u-v上。
 * 求有多少种给树边染色的方案（每条边染成黑色或白色），使得所有限制条件都满足。
 * 
 * 解题思路：
 * 1. 树形DP + 线段树合并
 * 2. 设dp[u][d]表示以u为根的子树中，从根到u的路径上深度为d的边必须被染成黑色的方案数
 * 3. 使用线段树合并优化DP状态转移
 * 4. 时间复杂度：O(n log n)
 * 
 * 核心思想：
 * - 对于每个限制条件(u,v)，设w=lca(u,v)，那么从w到u和w到v的路径上至少有一条边是黑色
 * - 转化为：对于每个节点u，记录其子树中需要满足的最深的限制条件
 * - 使用线段树维护DP状态，通过合并操作实现高效状态转移
 */
public class Code12_P6773_BloodCousinsReturn {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static PrintWriter out = new PrintWriter(System.out);
    
    static int n, m, idx;
    static int[] h, e, ne;
    static int[][] fa;
    static int[] depth;
    static List<Integer>[] constraints;
    static final int MOD = 998244353;
    static final int LOG = 20;
    
    // 线段树节点
    static class Node {
        int l, r;
        long sum, mul;
        Node() {
            mul = 1;
        }
    }
    
    static Node[] tr;
    static int[] root;
    static int cnt;
    
    public static void main(String[] args) throws IOException {
        String[] s = br.readLine().split(" ");
        n = Integer.parseInt(s[0]);
        
        // 初始化邻接表
        h = new int[n + 1];
        e = new int[2 * n + 5];
        ne = new int[2 * n + 5];
        Arrays.fill(h, -1);
        idx = 0;
        
        // 建树
        for (int i = 1; i < n; i++) {
            s = br.readLine().split(" ");
            int u = Integer.parseInt(s[0]);
            int v = Integer.parseInt(s[1]);
            add(u, v);
            add(v, u);
        }
        
        // 初始化倍增数组和深度
        fa = new int[n + 1][LOG];
        depth = new int[n + 1];
        constraints = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            constraints[i] = new ArrayList<>();
        }
        
        // DFS预处理深度和父节点
        dfs(1, 0);
        
        // 处理限制条件
        m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            s = br.readLine().split(" ");
            int u = Integer.parseInt(s[0]);
            int v = Integer.parseInt(s[1]);
            int w = lca(u, v);
            constraints[u].add(depth[w]);
            constraints[v].add(depth[w]);
        }
        
        // 初始化线段树
        tr = new Node[40 * n]; // 足够大的空间
        root = new int[n + 1];
        cnt = 0;
        
        // 树形DP
        dfs_dp(1, 0);
        
        // 输出结果
        long ans = query(root[1], 0, n, 0, depth[1]);
        out.println(ans);
        out.flush();
    }
    
    static void add(int a, int b) {
        e[idx] = b;
        ne[idx] = h[a];
        h[a] = idx++;
    }
    
    static void dfs(int u, int father) {
        depth[u] = depth[father] + 1;
        fa[u][0] = father;
        for (int i = 1; i < LOG; i++) {
            fa[u][i] = fa[fa[u][i - 1]][i - 1];
        }
        for (int i = h[u]; i != -1; i = ne[i]) {
            int j = e[i];
            if (j == father) continue;
            dfs(j, u);
        }
    }
    
    static int lca(int a, int b) {
        if (depth[a] < depth[b]) {
            int temp = a;
            a = b;
            b = temp;
        }
        for (int i = LOG - 1; i >= 0; i--) {
            if (depth[fa[a][i]] >= depth[b]) {
                a = fa[a][i];
            }
        }
        if (a == b) return a;
        for (int i = LOG - 1; i >= 0; i--) {
            if (fa[a][i] != fa[b][i]) {
                a = fa[a][i];
                b = fa[b][i];
            }
        }
        return fa[a][0];
    }
    
    static void pushup(int u) {
        tr[u].sum = (tr[tr[u].l].sum + tr[tr[u].r].sum) % MOD;
    }
    
    static void pushdown(int u, int l, int r) {
        if (tr[u].mul != 1) {
            int mid = (l + r) >> 1;
            if (tr[u].l != 0) {
                tr[tr[u].l].sum = tr[tr[u].l].sum * tr[u].mul % MOD;
                tr[tr[u].l].mul = tr[tr[u].l].mul * tr[u].mul % MOD;
            }
            if (tr[u].r != 0) {
                tr[tr[u].r].sum = tr[tr[u].r].sum * tr[u].mul % MOD;
                tr[tr[u].r].mul = tr[tr[u].r].mul * tr[u].mul % MOD;
            }
            tr[u].mul = 1;
        }
    }
    
    static void update(int u, int l, int r, int pos, long val) {
        if (l == r) {
            tr[u].sum = (tr[u].sum + val) % MOD;
            return;
        }
        pushdown(u, l, r);
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tr[u].l == 0) tr[u].l = ++cnt;
            update(tr[u].l, l, mid, pos, val);
        } else {
            if (tr[u].r == 0) tr[u].r = ++cnt;
            update(tr[u].r, mid + 1, r, pos, val);
        }
        pushup(u);
    }
    
    static long query(int u, int l, int r, int ql, int qr) {
        if (u == 0 || ql > r || qr < l) return 0;
        if (ql <= l && r <= qr) return tr[u].sum;
        pushdown(u, l, r);
        int mid = (l + r) >> 1;
        long res = 0;
        if (ql <= mid) res = (res + query(tr[u].l, l, mid, ql, qr)) % MOD;
        if (qr > mid) res = (res + query(tr[u].r, mid + 1, r, ql, qr)) % MOD;
        return res;
    }
    
    static int merge(int u, int v, int l, int r, long mulu, long mulv) {
        if (u == 0 && v == 0) return 0;
        if (u == 0) {
            tr[v].sum = tr[v].sum * mulv % MOD;
            tr[v].mul = tr[v].mul * mulv % MOD;
            return v;
        }
        if (v == 0) {
            tr[u].sum = tr[u].sum * mulu % MOD;
            tr[u].mul = tr[u].mul * mulu % MOD;
            return u;
        }
        if (l == r) {
            tr[u].sum = (tr[u].sum * mulu + tr[v].sum * mulv) % MOD;
            return u;
        }
        pushdown(u, l, r);
        pushdown(v, l, r);
        int mid = (l + r) >> 1;
        tr[u].l = merge(tr[u].l, tr[v].l, l, mid, mulu, mulv);
        tr[u].r = merge(tr[u].r, tr[v].r, mid + 1, r, mulu, mulv);
        pushup(u);
        return u;
    }
    
    static void dfs_dp(int u, int father) {
        // 初始化当前节点的线段树
        root[u] = ++cnt;
        update(root[u], 0, n, 0, 1); // 初始状态
        
        // 处理所有限制条件，找到最深的要求
        int maxd = 0;
        for (int d : constraints[u]) {
            maxd = Math.max(maxd, d);
        }
        
        for (int i = h[u]; i != -1; i = ne[i]) {
            int j = e[i];
            if (j == father) continue;
            
            dfs_dp(j, u);
            
            // 计算子树的贡献
            long sum = query(root[j], 0, n, 0, depth[u]);
            
            // 合并线段树
            root[u] = merge(root[u], root[j], 0, n, sum, (MOD + 1 - sum) % MOD);
        }
        
        // 处理当前节点的限制条件
        if (maxd > 0) {
            // 清除不满足限制条件的状态
            update(root[u], 0, n, maxd, 0);
        }
    }
}

/**
 * 类似题目推荐：
 * 1. P5494 【模板】线段树合并 - 线段树合并模板题
 * 2. CF911G Mass Change Queries - 区间赋值问题
 * 3. P4556 [Vani有约会]雨天的尾巴 - 树上差分+线段树合并
 * 4. P3224 [HNOI2012]永无乡 - 并查集+线段树合并
 * 5. P5298 [PKUWC2018]Minimax - 概率DP+线段树合并
 * 
 * 解题技巧总结：
 * 1. 线段树合并常用于优化树形DP，特别是需要维护子树信息的情况
 * 2. 注意线段树合并的时间复杂度是O(n log n)，但需要合理分配内存
 * 3. 对于限制条件，通常转化为对深度的要求
 * 4. 使用懒标记优化区间乘操作
 */