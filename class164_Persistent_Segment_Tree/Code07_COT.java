package class157;

import java.io.*;
import java.util.*;

/**
 * SPOJ COT - Count on a tree
 * 
 * 题目描述:
 * 给定一棵N个节点的树，每个节点有一个权值。进行M次查询，每次查询两点间路径上第K小的点权。
 * 
 * 解题思路:
 * 使用树上主席树解决树上路径第K小问题。
 * 1. 对所有节点权值进行离散化处理
 * 2. 通过DFS序确定树的结构，计算每个节点的深度和父节点
 * 3. 预处理倍增数组用于计算LCA(最近公共祖先)
 * 4. 对每个节点建立主席树，表示从根到该节点路径上的信息
 * 5. 查询时利用前缀和思想和LCA，通过root[u]+root[v]-root[lca]-root[fa[lca]]得到路径信息
 * 6. 在线段树上二分查找第K小的数
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 */
public class Code07_COT {
    static final int MAXN = 100010;
    static final int LOG = 20;
    
    // 树的邻接表表示
    static List<Integer>[] graph = new List[MAXN];
    // 节点权值
    static int[] weight = new int[MAXN];
    // 离散化后的权值
    static int[] sorted = new int[MAXN];
    // 节点深度
    static int[] depth = new int[MAXN];
    // 节点父节点
    static int[] parent = new int[MAXN];
    // 倍增数组用于LCA
    static int[][] fa = new int[MAXN][LOG];
    
    // 每个节点的主席树根节点
    static int[] root = new int[MAXN];
    
    // 线段树节点信息
    static int[] left = new int[MAXN * 20];
    static int[] right = new int[MAXN * 20];
    static int[] sum = new int[MAXN * 20];
    
    // 线段树节点计数器
    static int cnt = 0;
    
    // DFS序相关
    static int timestamp = 0;
    static int[] dfn = new int[MAXN];
    static int[] rev = new int[MAXN];
    
    static {
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    /**
     * 构建空线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 根节点编号
     */
    static int build(int l, int r) {
        int rt = ++cnt;
        sum[rt] = 0;
        if (l < r) {
            int mid = (l + r) / 2;
            left[rt] = build(l, mid);
            right[rt] = build(mid + 1, r);
        }
        return rt;
    }
    
    /**
     * 在线段树中插入一个值
     * @param pos 要插入的值（离散化后的坐标）
     * @param l 区间左端点
     * @param r 区间右端点
     * @param pre 前一个版本的节点编号
     * @return 新节点编号
     */
    static int insert(int pos, int l, int r, int pre) {
        int rt = ++cnt;
        left[rt] = left[pre];
        right[rt] = right[pre];
        sum[rt] = sum[pre] + 1;
        
        if (l < r) {
            int mid = (l + r) / 2;
            if (pos <= mid) {
                left[rt] = insert(pos, l, mid, left[rt]);
            } else {
                right[rt] = insert(pos, mid + 1, r, right[rt]);
            }
        }
        return rt;
    }
    
    /**
     * 查询树上路径第k小的数
     * @param k 第k小
     * @param l 区间左端点
     * @param r 区间右端点
     * @param u 节点u的根
     * @param v 节点v的根
     * @param lca LCA节点的根
     * @param flca LCA父节点的根
     * @return 第k小的数在离散化数组中的位置
     */
    static int query(int k, int l, int r, int u, int v, int lca, int flca) {
        if (l >= r) return l;
        int mid = (l + r) / 2;
        // 计算左子树中数的个数
        int x = sum[left[u]] + sum[left[v]] - sum[left[lca]] - sum[left[flca]];
        if (x >= k) {
            // 第k小在左子树中
            return query(k, l, mid, left[u], left[v], left[lca], left[flca]);
        } else {
            // 第k小在右子树中
            return query(k - x, mid + 1, r, right[u], right[v], right[lca], right[flca]);
        }
    }
    
    /**
     * DFS遍历构建主席树
     * @param u 当前节点
     * @param fa 父节点
     * @param d 深度
     */
    static void dfs(int u, int fa, int d) {
        depth[u] = d;
        parent[u] = fa;
        dfn[u] = ++timestamp;
        rev[timestamp] = u;
        
        // 在主席树中插入当前节点的权值
        int pos = Arrays.binarySearch(sorted, 1, cnt + 1, weight[u]) + 1;
        root[u] = insert(pos, 1, cnt, root[fa]);
        
        // 递归处理子节点
        for (int v : graph[u]) {
            if (v != fa) {
                dfs(v, u, d + 1);
            }
        }
    }
    
    /**
     * 预处理LCA
     * @param n 节点数
     */
    static void preprocessLCA(int n) {
        // 初始化fa数组
        for (int i = 1; i <= n; i++) {
            fa[i][0] = parent[i];
        }
        
        // 倍增计算
        for (int j = 1; j < LOG; j++) {
            for (int i = 1; i <= n; i++) {
                if (fa[i][j - 1] != -1) {
                    fa[i][j] = fa[fa[i][j - 1]][j - 1];
                }
            }
        }
    }
    
    /**
     * 计算两个节点的LCA
     * @param u 节点u
     * @param v 节点v
     * @return LCA节点
     */
    static int lca(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 让u和v在同一深度
        for (int i = LOG - 1; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = fa[u][i];
            }
        }
        
        if (u == v) return u;
        
        // 同时向上跳
        for (int i = LOG - 1; i >= 0; i--) {
            if (fa[u][i] != -1 && fa[u][i] != fa[v][i]) {
                u = fa[u][i];
                v = fa[v][i];
            }
        }
        
        return parent[u];
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = reader.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int m = Integer.parseInt(line[1]);
        
        // 读取节点权值
        line = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            weight[i] = Integer.parseInt(line[i - 1]);
            sorted[i] = weight[i];
        }
        
        // 离散化处理
        Arrays.sort(sorted, 1, n + 1);
        cnt = 1;
        for (int i = 2; i <= n; i++) {
            if (sorted[i] != sorted[cnt]) {
                sorted[++cnt] = sorted[i];
            }
        }
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            line = reader.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 构建主席树
        root[0] = build(1, cnt);
        dfs(1, 0, 0);
        
        // 预处理LCA
        preprocessLCA(n);
        
        // 处理查询
        for (int i = 0; i < m; i++) {
            line = reader.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            int k = Integer.parseInt(line[2]);
            
            int lcaNode = lca(u, v);
            int pos = query(k, 1, cnt, root[u], root[v], root[lcaNode], root[parent[lcaNode]]);
            writer.println(sorted[pos]);
        }
        
        writer.flush();
        writer.close();
        reader.close();
    }
}