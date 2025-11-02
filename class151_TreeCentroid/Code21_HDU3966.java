package class120;

// HDU 3966 Aragorn's Story
// 题目描述：给定一棵树，支持两种操作：1. 路径上的所有节点权值增加k；2. 查询某个节点的权值
// 算法思想：树链剖分 + 线段树，树链剖分的第一步就是找到树的重心来分割树
// 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=3966
// 时间复杂度：O(n log^2 n)
// 空间复杂度：O(n)

import java.util.*;

public class Code21_HDU3966 {
    
    static int MAXN = 50010;
    static int[] w = new int[MAXN]; // 节点权值
    static int[] tree = new int[MAXN * 4]; // 线段树
    static int[] lazy = new int[MAXN * 4]; // 延迟标记
    static List<Integer>[] graph = new ArrayList[MAXN]; // 邻接表
    static int[] fa = new int[MAXN]; // 父节点
    static int[] dep = new int[MAXN]; // 深度
    static int[] siz = new int[MAXN]; // 子树大小
    static int[] son = new int[MAXN]; // 重儿子
    static int[] top = new int[MAXN]; // 所在链的顶端
    static int[] dfn = new int[MAXN]; // 时间戳
    static int[] rnk = new int[MAXN]; // 时间戳对应的节点
    static int[] val = new int[MAXN]; // 时间戳对应的权值
    static int cnt = 0; // 时间戳计数器
    
    // 线段树更新操作
    static void pushDown(int rt, int l, int r) {
        if (lazy[rt] != 0) {
            int mid = (l + r) / 2;
            tree[rt * 2] += lazy[rt] * (mid - l + 1);
            tree[rt * 2 + 1] += lazy[rt] * (r - mid);
            lazy[rt * 2] += lazy[rt];
            lazy[rt * 2 + 1] += lazy[rt];
            lazy[rt] = 0;
        }
    }
    
    // 线段树区间更新
    static void update(int rt, int l, int r, int L, int R, int k) {
        if (L <= l && r <= R) {
            tree[rt] += k * (r - l + 1);
            lazy[rt] += k;
            return;
        }
        pushDown(rt, l, r);
        int mid = (l + r) / 2;
        if (L <= mid) update(rt * 2, l, mid, L, R, k);
        if (R > mid) update(rt * 2 + 1, mid + 1, r, L, R, k);
        tree[rt] = tree[rt * 2] + tree[rt * 2 + 1];
    }
    
    // 线段树单点查询
    static int query(int rt, int l, int r, int pos) {
        if (l == r) {
            return tree[rt];
        }
        pushDown(rt, l, r);
        int mid = (l + r) / 2;
        if (pos <= mid) return query(rt * 2, l, mid, pos);
        else return query(rt * 2 + 1, mid + 1, r, pos);
    }
    
    // 第一次DFS：计算父节点、深度、子树大小、重儿子
    static void dfs1(int u, int f) {
        fa[u] = f;
        dep[u] = dep[f] + 1;
        siz[u] = 1;
        son[u] = 0;
        int maxSize = 0;
        for (int v : graph[u]) {
            if (v != f) {
                dfs1(v, u);
                siz[u] += siz[v];
                if (siz[v] > maxSize) {
                    maxSize = siz[v];
                    son[u] = v;
                }
            }
        }
    }
    
    // 第二次DFS：分配时间戳，建立链
    static void dfs2(int u, int topf) {
        top[u] = topf;
        dfn[u] = ++cnt;
        rnk[cnt] = u;
        val[cnt] = w[u];
        if (son[u] != 0) {
            dfs2(son[u], topf); // 优先处理重儿子
            for (int v : graph[u]) {
                if (v != fa[u] && v != son[u]) {
                    dfs2(v, v); // 轻儿子单独成链
                }
            }
        }
    }
    
    // 树链剖分的路径更新
    static void updatePath(int u, int v, int k) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            update(1, 1, cnt, dfn[top[u]], dfn[u], k);
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        update(1, 1, cnt, dfn[u], dfn[v], k);
    }
    
    // 初始化线段树
    static void build(int rt, int l, int r) {
        if (l == r) {
            tree[rt] = val[l];
            return;
        }
        int mid = (l + r) / 2;
        build(rt * 2, l, mid);
        build(rt * 2 + 1, mid + 1, r);
        tree[rt] = tree[rt * 2] + tree[rt * 2 + 1];
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            int q = scanner.nextInt();
            
            // 初始化
            for (int i = 1; i <= n; i++) {
                w[i] = scanner.nextInt();
                graph[i] = new ArrayList<>();
            }
            
            // 读取边
            for (int i = 0; i < m; i++) {
                int u = scanner.nextInt();
                int v = scanner.nextInt();
                graph[u].add(v);
                graph[v].add(u);
            }
            
            // 树链剖分
            cnt = 0;
            dfs1(1, 0);
            dfs2(1, 1);
            
            // 建立线段树
            Arrays.fill(tree, 0);
            Arrays.fill(lazy, 0);
            build(1, 1, cnt);
            
            // 处理查询
            for (int i = 0; i < q; i++) {
                char op = scanner.next().charAt(0);
                if (op == 'Q') {
                    int u = scanner.nextInt();
                    System.out.println(query(1, 1, cnt, dfn[u]));
                } else {
                    int u = scanner.nextInt();
                    int v = scanner.nextInt();
                    int k = scanner.nextInt();
                    if (op == 'I') {
                        updatePath(u, v, k);
                    } else if (op == 'D') {
                        updatePath(u, v, -k);
                    }
                }
            }
        }
        scanner.close();
    }
}