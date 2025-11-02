package class158;

// Problem: SPOJ COT - Count on a tree
// Link: https://www.spoj.com/problems/COT/
// Description: 给定一棵树，每个节点有一个权值，每次查询路径(u,v)上第k小的权值
// Solution: 使用树上主席树解决树上路径第k小问题
// Time Complexity: O(nlogn) for preprocessing, O(logn) for each query
// Space Complexity: O(nlogn)

import java.io.*;
import java.util.*;

public class SPOJ_COT_Java {
    static final int MAXN = 100005;
    static int[] a = new int[MAXN];           // 节点权值
    static int[] b = new int[MAXN];           // 离散化数组
    static int n, m;
    
    // 邻接表存储树
    static List<Integer>[] G = new List[MAXN];
    static int[] dep = new int[MAXN];         // 节点深度
    static int[] fa = new int[MAXN];          // 节点父亲
    static int[][] anc = new int[MAXN][20];   // 倍增祖先
    
    // 主席树节点
    static class Node {
        int l, r, sum;
        Node(int l, int r, int sum) {
            this.l = l;
            this.r = r;
            this.sum = sum;
        }
    }
    
    static Node[] T = new Node[40 * MAXN];    // 主席树节点数组
    static int[] root = new int[MAXN];        // 每个节点的主席树根
    static int cnt = 0;                       // 节点计数器
    
    // 创建新节点
    static int createNode(int l, int r, int sum) {
        T[++cnt] = new Node(l, r, sum);
        return cnt;
    }
    
    // 插入值到主席树
    static int insert(int pre, int l, int r, int val) {
        int now = createNode(0, 0, 0);
        T[now].sum = T[pre].sum + 1;
        
        if (l == r) return now;
        
        int mid = (l + r) >> 1;
        if (val <= mid) {
            T[now].l = insert(T[pre].l, l, mid, val);
            T[now].r = T[pre].r;
        } else {
            T[now].l = T[pre].l;
            T[now].r = insert(T[pre].r, mid + 1, r, val);
        }
        return now;
    }
    
    // 查询路径第k小
    static int query(int u, int v, int lca, int flca, int l, int r, int k) {
        if (l == r) return l;
        
        int mid = (l + r) >> 1;
        int x = T[T[u].l].sum + T[T[v].l].sum - T[T[lca].l].sum - T[T[flca].l].sum;
        
        if (k <= x) {
            return query(T[u].l, T[v].l, T[lca].l, T[flca].l, l, mid, k);
        } else {
            return query(T[u].r, T[v].r, T[lca].r, T[flca].r, mid + 1, r, k - x);
        }
    }
    
    // DFS构建主席树和预处理倍增
    static void dfs(int u, int f, int d) {
        dep[u] = d;
        fa[u] = f;
        anc[u][0] = f;
        
        // 倍增预处理
        for (int i = 1; (1 << i) <= d; i++) {
            anc[u][i] = anc[anc[u][i-1]][i-1];
        }
        
        // 在主席树中插入当前节点的权值
        root[u] = insert(root[f], 1, n, getId(a[u]));
        
        // 递归处理子节点
        for (int v : G[u]) {
            if (v != f) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    // 计算LCA
    static int lca(int u, int v) {
        if (dep[u] < dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到和v同一深度
        for (int i = 19; i >= 0; i--) {
            if (dep[u] - (1 << i) >= dep[v]) {
                u = anc[u][i];
            }
        }
        
        if (u == v) return u;
        
        // 同时提升u和v直到相遇
        for (int i = 19; i >= 0; i--) {
            if (anc[u][i] != anc[v][i]) {
                u = anc[u][i];
                v = anc[v][i];
            }
        }
        
        return anc[u][0];
    }
    
    // 离散化
    static int getId(int x) {
        return Arrays.binarySearch(b, 1, n + 1, x) + 1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(line[i - 1]);
            b[i] = a[i];
        }
        
        // 初始化邻接表
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
        }
        
        // 读取边
        for (int i = 1; i < n; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            G[u].add(v);
            G[v].add(u);
        }
        
        // 离散化
        Arrays.sort(b, 1, n + 1);
        int sz = 1;
        for (int i = 2; i <= n; i++) {
            if (b[i] != b[i - 1]) {
                b[++sz] = b[i];
            }
        }
        
        // 构建主席树和预处理
        root[0] = createNode(0, 0, 0);
        T[root[0]].l = T[root[0]].r = T[root[0]].sum = 0;
        dfs(1, 0, 0);
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            
            int lcaNode = lca(u, v);
            int id = query(root[u], root[v], root[lcaNode], root[fa[lcaNode]], 1, sz, k);
            out.println(b[id]);
        }
        
        out.close();
    }
}