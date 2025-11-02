package class169.supplementary_solutions;

import java.io.*;
import java.util.*;

/**
 * 洛谷P3242 [HNOI2015]接水果 - Java实现
 * 题目来源：https://www.luogu.com.cn/problem/P3242
 * 题目描述：树上路径包含关系与扫描线结合的整体二分问题
 * 
 * 问题描述：
 * 给定一棵树，每个节点有一个权值。有两种操作：
 * 1. 类型A：在节点u和v之间连接一条边，边权为w
 * 2. 类型B：查询所有满足路径u-v被路径a-b包含的边的边权的第k小
 * 
 * 解题思路：
 * 1. 首先使用DFS序将树上路径转换为平面矩形区域
 * 2. 将边和查询转换为矩形区域的覆盖和查询问题
 * 3. 使用扫描线和树状数组结合整体二分求解
 * 
 * 时间复杂度：O((P+Q) * log(P) * log(max_weight))
 * 空间复杂度：O(P+Q)
 */
public class HNOI2015接水果 {
    static final int MAXN = 40005;
    static final int MAXM = 80005;
    static final int MAXQ = 100005;
    
    // 树的结构
    static int[] head = new int[MAXN];
    static int[] next = new int[MAXM];
    static int[] to = new int[MAXM];
    static int cnt;
    
    // DFS序
    static int[] in = new int[MAXN];
    static int[] out = new int[MAXN];
    static int timeStamp;
    
    // 父节点和深度（用于LCA）
    static int[] dep = new int[MAXN];
    static int[][] f = new int[MAXN][20];
    
    // 边的信息
    static int[] u = new int[MAXM];
    static int[] v = new int[MAXM];
    static int[] w = new int[MAXM];
    static int m, q;
    
    // 扫描线事件
    static class Event {
        int x, y1, y2, type, id;
        Event(int x, int y1, int y2, int type, int id) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
            this.type = type;
            this.id = id;
        }
    }
    
    static List<Event> events = new ArrayList<>();
    
    // 查询信息
    static class Query {
        int x1, x2, y1, y2, k, id;
        Query(int x1, int x2, int y1, int y2, int k, int id) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.k = k;
            this.id = id;
        }
    }
    
    static Query[] queries = new Query[MAXQ];
    static int[] ans = new int[MAXQ];
    
    // 整体二分相关
    static int[] ql = new int[MAXQ];
    static int[] qr = new int[MAXQ];
    static int[] qmid = new int[MAXQ];
    static int[] qans = new int[MAXQ];
    
    // 树状数组
    static class FenwickTree {
        int[] tree = new int[MAXN];
        
        void update(int x, int val) {
            for (; x < MAXN; x += x & -x) {
                tree[x] += val;
            }
        }
        
        int query(int x) {
            int res = 0;
            for (; x > 0; x -= x & -x) {
                res += tree[x];
            }
            return res;
        }
        
        int query(int l, int r) {
            return query(r) - query(l - 1);
        }
    }
    
    static FenwickTree ft = new FenwickTree();
    
    // 初始化树的邻接表
    static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        head[u] = cnt;
        to[cnt] = v;
    }
    
    // DFS计算入时间戳和出时间戳
    static void dfs(int u, int fa) {
        in[u] = ++timeStamp;
        dep[u] = dep[fa] + 1;
        f[u][0] = fa;
        for (int i = 1; i < 20; i++) {
            f[u][i] = f[f[u][i-1]][i-1];
        }
        for (int i = head[u]; i > 0; i = next[i]) {
            int v = to[i];
            if (v != fa) {
                dfs(v, u);
            }
        }
        out[u] = timeStamp;
    }
    
    // 求LCA
    static int lca(int u, int v) {
        if (dep[u] < dep[v]) {
            int tmp = u;
            u = v;
            v = tmp;
        }
        
        for (int i = 19; i >= 0; i--) {
            if (dep[f[u][i]] >= dep[v]) {
                u = f[u][i];
            }
        }
        
        if (u == v) return u;
        
        for (int i = 19; i >= 0; i--) {
            if (f[u][i] != f[v][i]) {
                u = f[u][i];
                v = f[v][i];
            }
        }
        
        return f[u][0];
    }
    
    // 处理边，将其转换为扫描线事件
    static void processEdge(int u, int v, int w, int id) {
        if (dep[u] < dep[v]) {
            int tmp = u;
            u = v;
            v = tmp;
        }
        
        // 将边转换为矩形区域
        events.add(new Event(1, in[u], out[u], 1, id));
        events.add(new Event(in[v], in[u], out[u], -1, id));
        events.add(new Event(out[v] + 1, in[u], out[u], 1, id));
    }
    
    // 处理查询，将其转换为矩形区域查询
    static void processQuery(int a, int b, int k, int id) {
        int l = lca(a, b);
        if (l == a) {
            // 路径a-b是链状的，且a是LCA
            queries[id] = new Query(in[b], out[b], in[l] + 1, in[a], k, id);
        } else if (l == b) {
            // 路径a-b是链状的，且b是LCA
            queries[id] = new Query(in[a], out[a], in[l] + 1, in[b], k, id);
        } else {
            // 路径a-b经过LCA，分成两段
            if (in[a] > in[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            queries[id] = new Query(in[a], out[a], in[b], out[b], k, id);
        }
    }
    
    // 整体二分核心函数
    static void solve(int ql, int qr, int l, int r) {
        if (ql > qr || l > r) return;
        
        if (l == r) {
            // 所有查询的答案都是l
            for (int i = ql; i <= qr; i++) {
                ans[queries[i].id] = l;
            }
            return;
        }
        
        int mid = (l + r) >> 1;
        
        // 收集扫描线事件
        List<Event> tmpEvents = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            if (w[i] <= mid) {
                processEdge(u[i], v[i], w[i], i);
            }
        }
        
        // 将查询也加入事件列表
        for (int i = ql; i <= qr; i++) {
            tmpEvents.add(new Event(queries[i].x1, queries[i].y1, queries[i].y2, -2, i));
            tmpEvents.add(new Event(queries[i].x2 + 1, queries[i].y1, queries[i].y2, -3, i));
        }
        
        // 按x坐标排序事件
        tmpEvents.sort((a, b) -> a.x - b.x);
        
        // 初始化答案计数
        int[] cnt = new int[qr - ql + 1];
        
        // 处理扫描线
        int eventPtr = 0;
        for (int x = 1; x <= timeStamp; x++) {
            // 处理所有x坐标等于当前x的事件
            while (eventPtr < tmpEvents.size() && tmpEvents.get(eventPtr).x == x) {
                Event e = tmpEvents.get(eventPtr++);
                if (e.type == 1 || e.type == -1) {
                    // 矩形覆盖事件
                    ft.update(e.y1, e.type);
                    ft.update(e.y2 + 1, -e.type);
                } else if (e.type == -2) {
                    // 查询开始事件
                    int idx = e.id - ql;
                    cnt[idx] -= ft.query(e.y1, e.y2);
                } else if (e.type == -3) {
                    // 查询结束事件
                    int idx = e.id - ql;
                    cnt[idx] += ft.query(e.y1, e.y2);
                }
            }
        }
        
        // 清理树状数组
        for (Event e : tmpEvents) {
            if (e.type == 1 || e.type == -1) {
                ft.update(e.y1, -e.type);
                ft.update(e.y2 + 1, e.type);
            }
        }
        
        // 分类查询
        int left = ql, right = qr;
        int[] leftQueries = new int[qr - ql + 1];
        int[] rightQueries = new int[qr - ql + 1];
        
        for (int i = ql; i <= qr; i++) {
            int idx = i - ql;
            if (cnt[idx] >= queries[i].k) {
                // 答案在左半部分
                leftQueries[left - ql] = i;
                left++;
            } else {
                // 答案在右半部分，调整k值
                queries[i].k -= cnt[idx];
                rightQueries[right - qr] = i;
                right--;
            }
        }
        
        // 合并查询顺序
        for (int i = ql; i < left; i++) {
            queries[i] = queries[leftQueries[i - ql]];
        }
        for (int i = qr; i > right; i--) {
            queries[i] = queries[rightQueries[i - qr]];
        }
        
        // 递归处理左右两部分
        solve(ql, left - 1, l, mid);
        solve(right + 1, qr, mid + 1, r);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        q = Integer.parseInt(parts[2]);
        
        // 读取树的边
        for (int i = 1; i < n; i++) {
            parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 计算DFS序和LCA所需信息
        dfs(1, 0);
        
        // 读取水果（边）的信息
        int[] weights = new int[m + 1];
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            u[i] = Integer.parseInt(parts[0]);
            v[i] = Integer.parseInt(parts[1]);
            w[i] = Integer.parseInt(parts[2]);
            weights[i] = w[i];
        }
        
        // 离散化边权
        Arrays.sort(weights, 1, m + 1);
        int uniqueWeights = 1;
        for (int i = 2; i <= m; i++) {
            if (weights[i] != weights[uniqueWeights]) {
                weights[++uniqueWeights] = weights[i];
            }
        }
        
        for (int i = 1; i <= m; i++) {
            w[i] = Arrays.binarySearch(weights, 1, uniqueWeights + 1, w[i]) - weights[0] + 1;
        }
        
        // 读取查询
        for (int i = 1; i <= q; i++) {
            parts = br.readLine().split(" ");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            int k = Integer.parseInt(parts[2]);
            processQuery(a, b, k, i);
        }
        
        // 整体二分求解
        solve(1, q, 1, uniqueWeights);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(weights[ans[i]]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}