package class168;

// P4175 [CTSC2008]网络管理 - Java实现
// 题目来源：https://www.luogu.com.cn/problem/P4175
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)

import java.util.*;
import java.io.*;

public class P4175_网络管理 {
    public static int MAXN = 80001;
    public static int MAXQ = 80001;
    public static int MAXM = 17; // log2(80000) ≈ 16.29
    
    public static int n, q;
    
    // 树结构
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 0;
    
    // 树链剖分
    public static int[] fa = new int[MAXN];
    public static int[] depth = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[] son = new int[MAXN];
    public static int[] top = new int[MAXN];
    public static int[] dfn = new int[MAXN];
    public static int[] rnk = new int[MAXN];
    public static int dfc = 0;
    
    // 节点权值
    public static int[] val = new int[MAXN];
    
    // 树状数组
    public static int[] tree = new int[MAXN];
    
    // 操作信息
    public static int[] op = new int[MAXQ];  // 0:修改 1:查询
    public static int[] x = new int[MAXQ];
    public static int[] y = new int[MAXQ];
    public static int[] k = new int[MAXQ];
    public static int[] qid = new int[MAXQ];
    
    // 整体二分
    public static int[] lset = new int[MAXQ];
    public static int[] rset = new int[MAXQ];
    public static int[] ans = new int[MAXQ];
    
    // 离散化
    public static int[] sorted = new int[MAXN + MAXQ];
    public static int cntv = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            val[i] = Integer.parseInt(line[i - 1]);
            sorted[++cntv] = val[i];
        }
        
        // 建树
        for (int i = 1; i < n; i++) {
            line = br.readLine().split(" ");
            int u = Integer.parseInt(line[0]);
            int v = Integer.parseInt(line[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 处理操作
        for (int i = 1; i <= q; i++) {
            line = br.readLine().split(" ");
            op[i] = Integer.parseInt(line[0]);
            x[i] = Integer.parseInt(line[1]);
            y[i] = Integer.parseInt(line[2]);
            
            if (op[i] == 0) {
                // 修改操作
                sorted[++cntv] = y[i];
            } else {
                // 查询操作
                k[i] = op[i];
                op[i] = 1;
                qid[i] = i;
            }
        }
        
        // 离散化
        Arrays.sort(sorted, 1, cntv + 1);
        cntv = unique(sorted, cntv);
        
        // 树链剖分预处理
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 整体二分求解
        compute(1, q, 1, cntv);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            if (ans[i] != 0) {
                out.println(sorted[ans[i]]);
            }
        }
        out.flush();
    }
    
    // 去重函数
    public static int unique(int[] arr, int len) {
        if (len <= 1) return len;
        int i = 1, j = 2;
        while (j <= len) {
            if (arr[j] != arr[i]) {
                arr[++i] = arr[j];
            }
            j++;
        }
        return i;
    }
    
    // 添加边
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次DFS：计算深度、父节点、子树大小、重儿子
    public static void dfs1(int u, int f) {
        fa[u] = f;
        depth[u] = depth[f] + 1;
        siz[u] = 1;
        
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v == f) continue;
            dfs1(v, u);
            siz[u] += siz[v];
            if (siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
    
    // 第二次DFS：计算dfn序、重链顶点
    public static void dfs2(int u, int t) {
        top[u] = t;
        dfn[u] = ++dfc;
        rnk[dfc] = u;
        
        if (son[u] != 0) {
            dfs2(son[u], t);
        }
        
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v == fa[u] || v == son[u]) continue;
            dfs2(v, v);
        }
    }
    
    // 树状数组操作
    public static int lowbit(int i) {
        return i & -i;
    }
    
    public static void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    public static int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    // 树链剖分查询路径上点的个数
    public static int queryPath(int u, int v) {
        int ret = 0;
        while (top[u] != top[v]) {
            if (depth[top[u]] < depth[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            ret += sum(dfn[u]) - sum(dfn[top[u]] - 1);
            u = fa[top[u]];
        }
        
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        ret += sum(dfn[v]) - sum(dfn[u] - 1);
        return ret;
    }
    
    // 树链剖分修改路径上的点
    public static void addPath(int u, int v, int val) {
        while (top[u] != top[v]) {
            if (depth[top[u]] < depth[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            add(dfn[top[u]], val);
            add(dfn[u] + 1, -val);
            u = fa[top[u]];
        }
        
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        add(dfn[u], val);
        add(dfn[v] + 1, -val);
    }
    
    // 整体二分核心函数
    public static void compute(int ql, int qr, int vl, int vr) {
        // 递归边界
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                if (op[qid[i]] == 1) {
                    ans[qid[i]] = vl;
                }
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 将值域小于等于mid的数加入树状数组
        for (int i = 1; i <= n; i++) {
            if (val[i] <= sorted[mid]) {
                addPath(i, i, 1);
            }
        }
        
        for (int i = 1; i <= q; i++) {
            if (op[i] == 0 && y[i] <= sorted[mid]) {
                addPath(x[i], x[i], 1);
            }
        }
        
        // 检查每个查询，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            if (op[id] == 1) {
                // 查询操作
                int satisfy = queryPath(x[id], y[id]);
                if (satisfy >= k[id]) {
                    // 说明第k大的数在左半部分
                    lset[++lsiz] = id;
                } else {
                    // 说明第k大的数在右半部分，需要在右半部分找第(k-satisfy)大的数
                    k[id] -= satisfy;
                    rset[++rsiz] = id;
                }
            } else {
                // 修改操作
                if (y[id] <= sorted[mid]) {
                    lset[++lsiz] = id;
                } else {
                    rset[++rsiz] = id;
                }
            }
        }
        
        // 将操作分组
        for (int i = 1; i <= lsiz; i++) {
            qid[ql + i - 1] = lset[i];
        }
        for (int i = 1; i <= rsiz; i++) {
            qid[ql + lsiz + i - 1] = rset[i];
        }
        
        // 清空树状数组
        for (int i = 1; i <= n; i++) {
            if (val[i] <= sorted[mid]) {
                addPath(i, i, -1);
            }
        }
        
        for (int i = 1; i <= q; i++) {
            if (op[i] == 0 && y[i] <= sorted[mid]) {
                addPath(x[i], x[i], -1);
            }
        }
        
        // 递归处理左右区间
        compute(ql, ql + lsiz - 1, vl, mid);
        compute(ql + lsiz, qr, mid + 1, vr);
    }
}