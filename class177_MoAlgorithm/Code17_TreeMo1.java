// Count on a tree II - 树上莫队算法实现 (Java版本)
// 题目来源: SPOJ COT2 - Count on a tree II
// 题目链接: https://www.luogu.com.cn/problem/SP10707
// 题目大意: 给定一棵树，每个节点有权值，多次询问两点间路径上不同权值的个数
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 2. 洛谷 P3379 【模板】最近公共祖先（LCA） - https://www.luogu.com.cn/problem/P3379
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P3379_TreeMo3.py
//
// 3. 洛谷 P4689 [Ynoi2016]这是我自己的发明 - https://www.luogu.com.cn/problem/P4689
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4689_TreeMo3.py
//
// 4. 洛谷 P4074 [WC2013]糖果公园 - https://www.luogu.com.cn/problem/P4074
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_P4074_TreeMo3.py
//
// 5. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 8. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 9. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 10. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py

package class179;

import java.io.*;
import java.util.*;

public class Code17_TreeMo1 {
    public static int MAXN = 40010;
    public static int MAXM = 100010;
    
    // 链式前向星存储树
    public static int[] head = new int[MAXN];
    public static int[] to = new int[MAXN * 2];
    public static int[] next = new int[MAXN * 2];
    public static int tot = 0;
    
    // 树的相关信息
    public static int[] val = new int[MAXN];      // 节点权值
    public static int[] dep = new int[MAXN];      // 节点深度
    public static int[] fa = new int[MAXN];       // 节点父亲
    public static int[][] f = new int[MAXN][20];  // 倍增数组
    
    // 括号序相关
    public static int[] id = new int[MAXN * 2];   // 括号序中第i个位置对应的节点
    public static int[] fff = new int[MAXN];      // 节点第一次出现的位置
    public static int[] ggg = new int[MAXN];      // 节点第二次出现的位置
    public static int indexx = 0;                 // 括号序长度
    
    // 莫队相关
    public static int[] pos = new int[MAXN * 2];  // 每个位置所属的块
    public static int sz;                         // 块大小
    
    // 查询相关
    public static class Query {
        int l, r, lca, id;
        
        public Query(int l, int r, int lca, int id) {
            this.l = l;
            this.r = r;
            this.lca = lca;
            this.id = id;
        }
        
        public Query() {}
    }
    
    public static Query[] q = new Query[MAXM];
    
    // 计数和答案相关
    public static int[] cnt = new int[MAXN * 2];  // 权值计数
    public static int[] ans = new int[MAXM];      // 答案数组
    public static int[] vis = new int[MAXN];      // 节点是否在当前路径中
    public static int curAns = 0;                 // 当前答案
    
    // 添加边
    public static void addEdge(int u, int v) {
        to[++tot] = v;
        next[tot] = head[u];
        head[u] = tot;
    }
    
    // DFS生成括号序
    public static void dfs(int u, int father) {
        fff[u] = ++indexx;
        id[indexx] = u;
        fa[u] = father;
        
        // 遍历子节点
        for (int i = head[u]; i > 0; i = next[i]) {
            int v = to[i];
            if (v != father) {
                dep[v] = dep[u] + 1;
                dfs(v, u);
            }
        }
        
        ggg[u] = ++indexx;
        id[indexx] = u;
    }
    
    // 预处理倍增数组
    public static void preProcess(int n) {
        // 初始化f数组
        for (int i = 1; i <= n; i++) {
            f[i][0] = fa[i];
        }
        
        // 倍增处理
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i <= n; i++) {
                if (f[i][j-1] != -1) {
                    f[i][j] = f[f[i][j-1]][j-1];
                }
            }
        }
    }
    
    // 计算LCA
    public static int lca(int u, int v) {
        if (dep[u] < dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u调整到和v同一深度
        int diff = dep[u] - dep[v];
        for (int i = 0; i < 20; i++) {
            if ((diff & (1 << i)) != 0) {
                u = f[u][i];
            }
        }
        
        if (u == v) return u;
        
        // 同时向上跳
        for (int i = 19; i >= 0; i--) {
            if (f[u][i] != f[v][i]) {
                u = f[u][i];
                v = f[v][i];
            }
        }
        
        return f[u][0];
    }
    
    // 添加或删除节点（根据vis状态）
    public static void toggle(int x) {
        if (vis[x] == 1) {
            // 删除节点
            cnt[val[x]]--;
            if (cnt[val[x]] == 0) {
                curAns--;
            }
            vis[x] = 0;
        } else {
            // 添加节点
            if (cnt[val[x]] == 0) {
                curAns++;
            }
            cnt[val[x]]++;
            vis[x] = 1;
        }
    }
    
    // 查询排序比较器
    public static class QueryComparator implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            if (pos[a.l] != pos[b.l]) {
                return pos[a.l] - pos[b.l];
            }
            return pos[a.r] - pos[b.r];
        }
    }
    
    public static void main(String[] args) throws Exception {
        FastReader in = new FastReader();
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = in.nextInt();
        int m = in.nextInt();
        
        // 初始化查询数组
        for (int i = 0; i < MAXM; i++) {
            q[i] = new Query();
        }
        
        // 读取节点权值
        Map<Integer, Integer> mp = new HashMap<>();
        int cntt = 0;
        for (int i = 1; i <= n; i++) {
            int x = in.nextInt();
            if (!mp.containsKey(x)) {
                mp.put(x, ++cntt);
            }
            val[i] = mp.get(x);
        }
        
        // 读取边信息并建图
        Arrays.fill(head, -1);
        for (int i = 1; i < n; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // DFS生成括号序
        dfs(1, 0);
        
        // 预处理倍增数组
        preProcess(n);
        
        // 分块处理
        sz = (int) Math.sqrt(indexx);
        for (int i = 1; i <= indexx; i++) {
            pos[i] = (i - 1) / sz + 1;
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            int u = in.nextInt();
            int v = in.nextInt();
            int l = lca(u, v);
            
            // 确保fff[u] <= fff[v]
            if (fff[u] > fff[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 根据LCA是否为端点设置查询区间
            if (l == u) {
                q[i] = new Query(fff[u], fff[v], 0, i);
            } else {
                q[i] = new Query(ggg[u], fff[v], l, i);
            }
        }
        
        // 排序查询
        Arrays.sort(q, 1, m + 1, new QueryComparator());
        
        // 莫队处理
        int l = 1, r = 0;
        for (int i = 1; i <= m; i++) {
            int ql = q[i].l;
            int qr = q[i].r;
            int qlca = q[i].lca;
            
            // 移动左右指针
            while (r < qr) {
                r++;
                toggle(id[r]);
            }
            while (r > qr) {
                toggle(id[r]);
                r--;
            }
            while (l < ql) {
                toggle(id[l]);
                l++;
            }
            while (l > ql) {
                l--;
                toggle(id[l]);
            }
            
            // 处理LCA
            if (qlca != 0) {
                toggle(qlca);
                ans[q[i].id] = curAns;
                toggle(qlca);
            } else {
                ans[q[i].id] = curAns;
            }
        }
        
        // 输出答案
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
    }

    // 读写工具类
    static class FastReader {
        final private int BUFFER_SIZE = 1 << 16;
        private final InputStream in;
        private final byte[] buffer;
        private int ptr, len;

        public FastReader() {
            in = System.in;
            buffer = new byte[BUFFER_SIZE];
            ptr = len = 0;
        }

        private boolean hasNextByte() throws IOException {
            if (ptr < len)
                return true;
            ptr = 0;
            len = in.read(buffer);
            return len > 0;
        }

        private byte readByte() throws IOException {
            if (!hasNextByte())
                return -1;
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