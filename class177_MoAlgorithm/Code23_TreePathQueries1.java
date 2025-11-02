// Tree Path Queries - 树上莫队算法实现 (Java版本)
// 题目来源: 模板题 - 树上路径不同元素查询
// 题目链接: https://www.luogu.com.cn/problem/P4396
// 题目大意: 给定一棵树，每个节点有一个权值，每次查询路径u-v上有多少不同的权值
// 时间复杂度: O(n*sqrt(n))，空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P4396 [AHOI2013] 作业 - https://www.luogu.com.cn/problem/P4396
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code23_TreePathQueries1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code23_TreePathQueries2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code23_TreePathQueries3.py
//
// 2. Codeforces 375D Tree and Queries - https://codeforces.com/problemset/problem/375/D
//
// 3. HDU 6604 Blow up the city - https://acm.hdu.edu.cn/showproblem.php?pid=6604

package class179;

import java.io.*;
import java.util.*;

public class Code23_TreePathQueries1 {
    public static int MAXN = 100010;
    public static int MAXV = 100010;
    public static int n, m, idx;
    public static int[] arr = new int[MAXN];
    public static int[] bi = new int[MAXN];
    public static int[] cnt = new int[MAXV]; // 记录每种权值的出现次数
    public static int[] ans = new int[MAXN]; // 存储答案
    public static int diff = 0; // 当前路径不同元素的数量
    
    // 树的邻接表
    public static List<Integer>[] tree = new List[MAXN];
    
    // 欧拉序相关
    public static int[] in = new int[MAXN]; // 进入时间戳
    public static int[] out = new int[MAXN]; // 离开时间戳
    public static int[] seq = new int[MAXN * 2]; // 欧拉序序列
    public static int[] fa = new int[MAXN]; // 父节点
    public static int[] dep = new int[MAXN]; // 深度
    public static int[][] up = new int[MAXN][20]; // 倍增数组，用于LCA查询
    
    // 查询结构
    public static class Query {
        int l, r, lca, id; // l,r:欧拉序中的区间，lca:最近公共祖先，id:查询编号
        
        public Query(int l, int r, int lca, int id) {
            this.l = l;
            this.r = r;
            this.lca = lca;
            this.id = id;
        }
    }
    
    public static Query[] queries = new Query[MAXN];

    // 查询排序比较器
    public static class QueryCmp implements Comparator<Query> {
        @Override
        public int compare(Query a, Query b) {
            if (bi[a.l] != bi[b.l]) {
                return bi[a.l] - bi[b.l];
            }
            if ((bi[a.l] & 1) == 1) {
                return a.r - b.r;
            } else {
                return b.r - a.r;
            }
        }
    }

    // 添加/删除节点到路径
    public static void toggle(int node) {
        int value = arr[node];
        if (cnt[value] > 0) {
            cnt[value]--;
            if (cnt[value] == 0) {
                diff--;
            }
        } else {
            cnt[value]++;
            diff++;
        }
    }

    // 预处理LCA的倍增数组
    public static void dfs(int u, int parent) {
        in[u] = ++idx;
        seq[idx] = u;
        fa[u] = parent;
        dep[u] = dep[parent] + 1;
        up[u][0] = parent;
        for (int i = 1; i < 20; i++) {
            up[u][i] = up[up[u][i-1]][i-1];
        }
        for (int v : tree[u]) {
            if (v != parent) {
                dfs(v, u);
            }
        }
        out[u] = ++idx;
        seq[idx] = u;
    }

    // 查询LCA
    public static int lca(int u, int v) {
        if (dep[u] < dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        // 提升u到v的深度
        for (int i = 19; i >= 0; i--) {
            if (dep[up[u][i]] >= dep[v]) {
                u = up[u][i];
            }
        }
        if (u == v) return u;
        // 同时提升u和v
        for (int i = 19; i >= 0; i--) {
            if (up[u][i] != up[v][i]) {
                u = up[u][i];
                v = up[v][i];
            }
        }
        return up[u][0];
    }

    // 将树上路径转换为欧拉序区间
    public static void buildQuery(int u, int v, int id) {
        int ancestor = lca(u, v);
        if (ancestor == u) {
            // 路径u-v在同一条链上，u是祖先
            queries[id] = new Query(in[u], in[v], 0, id);
        } else if (ancestor == v) {
            // 路径u-v在同一条链上，v是祖先
            queries[id] = new Query(in[v], in[u], 0, id);
        } else {
            // 路径u-v需要经过LCA
            if (in[u] > in[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            queries[id] = new Query(out[u], in[v], ancestor, id);
        }
    }

    public static void main(String[] args) throws IOException {
        // 快速输入输出
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        // 初始化树
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }

        // 读取节点数和查询次数
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        // 读取节点权值
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // 读取树的边
        for (int i = 0; i < n - 1; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            tree[u].add(v);
            tree[v].add(u);
        }

        // 预处理欧拉序和LCA
        idx = 0;
        dfs(1, 0);

        // 分块 - 块大小为sqrt(2n)
        int blockSize = (int) Math.sqrt(2 * n) + 1;
        for (int i = 1; i <= 2 * n; i++) {
            bi[i] = (i - 1) / blockSize;
        }

        // 读取查询并构建欧拉序查询
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            buildQuery(u, v, i);
        }

        // 排序查询
        Arrays.sort(queries, 0, m, new QueryCmp());

        // 初始化莫队指针
        int winL = 1, winR = 0;
        Arrays.fill(cnt, 0);
        diff = 0;

        // 处理每个查询
        for (int i = 0; i < m; i++) {
            Query q = queries[i];
            int l = q.l;
            int r = q.r;
            int ancestor = q.lca;
            int id = q.id;

            // 移动指针
            while (winR < r) toggle(seq[++winR]);
            while (winL > l) toggle(seq[--winL]);
            while (winR > r) toggle(seq[winR--]);
            while (winL < l) toggle(seq[winL++]);

            // 如果有LCA，需要额外处理
            if (ancestor != 0) {
                toggle(ancestor); // 临时加入LCA
                ans[id] = diff;
                toggle(ancestor); // 记得撤销
            } else {
                ans[id] = diff;
            }
        }

        // 输出答案
        for (int i = 0; i < m; i++) {
            bw.write(ans[i] + "\n");
        }

        bw.flush();
        bw.close();
        br.close();
    }
}