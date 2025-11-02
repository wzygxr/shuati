package class181;

// Journey - CF1336F
// 测试链接 : https://codeforces.com/problemset/problem/1336/F
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code08_Journey {

    public static int MAXN = 150001;
    public static int MAXT = MAXN * 50; // 线段树节点数

    public static int n, k;
    
    // 邻接表存储树
    public static int[] head = new int[MAXN];
    public static int[] nxt = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cntg;
    
    // 树上信息
    public static int[] dep = new int[MAXN];
    public static int[] dfn = new int[MAXN];
    public static int[] rev = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[][] anc = new int[MAXN][21];
    public static int id;
    
    // 线段树相关
    public static int[] ls = new int[MAXT];
    public static int[] rs = new int[MAXT];
    public static int[] sum = new int[MAXT];
    public static int cntt;
    
    // 回收站
    public static int[] st = new int[MAXT];
    public static int top;
    
    // 链信息
    public static List<int[]>[] chains = new List[MAXN];
    
    // 答案
    public static long ans;
    
    // 树状数组
    public static int[] bit = new int[MAXN];
    
    // 添加边
    public static void addEdge(int u, int v) {
        nxt[++cntg] = head[u];
        to[cntg] = v;
        head[u] = cntg;
    }
    
    // 初始化线段树节点
    public static int newNode() {
        if (top > 0) {
            return st[top--];
        }
        return ++cntt;
    }
    
    // 更新节点信息
    public static void pushUp(int rt) {
        sum[rt] = sum[ls[rt]] + sum[rs[rt]];
    }
    
    // 更新线段树
    public static void update(int rt, int l, int r, int pos, int val) {
        if (rt == 0) {
            rt = newNode();
        }
        sum[rt] += val;
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (ls[rt] == 0) {
                ls[rt] = newNode();
            }
            update(ls[rt], l, mid, pos, val);
        } else {
            if (rs[rt] == 0) {
                rs[rt] = newNode();
            }
            update(rs[rt], mid + 1, r, pos, val);
        }
        pushUp(rt);
    }
    
    // 查询线段树
    public static int query(int rt, int l, int r, int L, int R) {
        if (rt == 0 || L > r || R < l) {
            return 0;
        }
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        int res = 0;
        if (L <= mid) {
            res += query(ls[rt], l, mid, L, R);
        }
        if (R > mid) {
            res += query(rs[rt], mid + 1, r, L, R);
        }
        return res;
    }
    
    // 合并线段树
    public static int merge(int a, int b) {
        if (a == 0 || b == 0) {
            return a + b;
        }
        sum[a] += sum[b];
        ls[a] = merge(ls[a], ls[b]);
        rs[a] = merge(rs[a], rs[b]);
        st[++top] = b;
        return a;
    }
    
    // 删除线段树节点
    public static void del(int rt) {
        if (rt == 0) {
            return;
        }
        del(ls[rt]);
        del(rs[rt]);
        st[++top] = rt;
        ls[rt] = rs[rt] = sum[rt] = 0;
    }
    
    // 树状数组操作
    public static int lowbit(int x) {
        return x & (-x);
    }
    
    public static void addBit(int x, int val) {
        x++;
        for (int i = x; i < MAXN; i += lowbit(i)) {
            bit[i] += val;
        }
    }
    
    public static int sumBit(int x) {
        x++;
        int res = 0;
        for (int i = x; i > 0; i -= lowbit(i)) {
            res += bit[i];
        }
        return res;
    }
    
    // DFS预处理
    public static void dfs1(int u, int fa) {
        dep[u] = dep[fa] + 1;
        anc[u][0] = fa;
        siz[u] = 1;
        dfn[u] = ++id;
        rev[id] = u;
        
        // 预处理祖先
        for (int i = 1; i <= 20; i++) {
            anc[u][i] = anc[anc[u][i - 1]][i - 1];
        }
        
        // 遍历子节点
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != fa) {
                dfs1(v, u);
                siz[u] += siz[v];
            }
        }
    }
    
    // 跳到指定深度
    public static int jump(int x, int d) {
        for (int i = 20; i >= 0; i--) {
            if (((d >> i) & 1) != 0) {
                x = anc[x][i];
            }
        }
        return x;
    }
    
    // 求LCA
    public static int lca(int x, int y) {
        if (dep[x] < dep[y]) {
            int temp = x;
            x = y;
            y = temp;
        }
        for (int i = 20; i >= 0; i--) {
            if (dep[anc[x][i]] >= dep[y]) {
                x = anc[x][i];
            }
        }
        if (x == y) {
            return x;
        }
        for (int i = 20; i >= 0; i--) {
            if (anc[x][i] != anc[y][i]) {
                x = anc[x][i];
                y = anc[y][i];
            }
        }
        return anc[x][0];
    }
    
    // 处理不同LCA的情况
    public static void dfs2(int u, int fa) {
        // 先递归处理子节点
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != fa) {
                dfs2(v, u);
            }
        }
        
        // 统计贡献
        for (int[] chain : chains[u]) {
            ans += sumBit(dfn[chain[0]]) + sumBit(dfn[chain[1]]);
        }
        
        // 更新树状数组
        for (int[] chain : chains[u]) {
            if (dep[chain[0]] - dep[u] >= k) {
                int node = jump(chain[0], dep[chain[0]] - dep[u] - k);
                addBit(dfn[node], 1);
                addBit(dfn[node] + siz[node], -1);
            }
            if (dep[chain[1]] - dep[u] >= k) {
                int node = jump(chain[1], dep[chain[1]] - dep[u] - k);
                addBit(dfn[node], 1);
                addBit(dfn[node] + siz[node], -1);
            }
        }
    }
    
    // 清空树状数组
    public static void clearBit() {
        Arrays.fill(bit, 0);
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader(System.in);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = in.nextInt();
        int m = in.nextInt();
        k = in.nextInt();
        
        // 初始化链信息
        for (int i = 1; i <= n; i++) {
            chains[i] = new ArrayList<>();
        }
        
        // 建树
        for (int i = 1; i < n; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 预处理
        dfs1(1, 0);
        
        // 读入链信息
        for (int i = 1; i <= m; i++) {
            int x = in.nextInt();
            int y = in.nextInt();
            if (dfn[x] > dfn[y]) {
                int temp = x;
                x = y;
                y = temp;
            }
            int l = lca(x, y);
            chains[l].add(new int[]{x, y});
        }
        
        // 处理不同LCA的情况
        dfs2(1, 0);
        
        out.println(ans);
        out.flush();
        out.close();
    }
    
    // 读写工具类
    static class FastReader {
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        private final InputStream in;

        FastReader(InputStream in) {
            this.in = in;
        }

        private int readByte() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0)
                    return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            do {
                c = readByte();
            } while (c <= ' ' && c != -1);
            boolean neg = false;
            if (c == '-') {
                neg = true;
                c = readByte();
            }
            int val = 0;
            while (c > ' ' && c != -1) {
                val = val * 10 + (c - '0');
                c = readByte();
            }
            return neg ? -val : val;
        }
    }
}