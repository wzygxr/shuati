import java.io.*;
import java.util.*;

/**
 * 洛谷 P4556 [Vani有约会]雨天的尾巴 (线段树分裂与合并综合应用)
 * 题目链接: https://www.luogu.com.cn/problem/P4556
 * 
 * 题目描述:
 * 给定一棵树，每个节点初始有一个救济粮类型。有m次操作，每次在路径(u, v)上发放类型为z的救济粮。
 * 最后查询每个节点最多的救济粮类型，如果有多个取编号最小的。
 * 
 * 核心算法: 线段树合并 + 树上差分 + 树链剖分
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 解题思路:
 * 1. 使用树链剖分求LCA，实现树上差分
 * 2. 对每个节点维护一个值域线段树，记录各种救济粮的数量
 * 3. 使用线段树合并技术将子节点的信息合并到父节点
 * 4. 通过树上差分实现路径加操作
 */
public class Code23_P4556_RainyTail_Advanced {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(System.out);
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static class SegmentTree {
        static class Node {
            int left, right;
            int maxCnt;  // 最大值
            int maxType; // 最大值对应的类型
            
            Node() {
                left = right = -1;
                maxCnt = 0;
                maxType = 0;
            }
        }
        
        Node[] tree;
        int cnt;
        
        public SegmentTree() {
            tree = new Node[20000000];
            for (int i = 0; i < tree.length; i++) {
                tree[i] = new Node();
            }
            cnt = 0;
        }
        
        int newNode() {
            if (cnt >= tree.length) {
                Node[] newTree = new Node[tree.length * 2];
                System.arraycopy(tree, 0, newTree, 0, tree.length);
                for (int i = tree.length; i < newTree.length; i++) {
                    newTree[i] = new Node();
                }
                tree = newTree;
            }
            tree[cnt].left = tree[cnt].right = -1;
            tree[cnt].maxCnt = 0;
            tree[cnt].maxType = 0;
            return cnt++;
        }
        
        void pushUp(int p) {
            if (p == -1) return;
            
            int leftCnt = (tree[p].left != -1) ? tree[tree[p].left].maxCnt : 0;
            int rightCnt = (tree[p].right != -1) ? tree[tree[p].right].maxCnt : 0;
            
            if (leftCnt > rightCnt) {
                tree[p].maxCnt = leftCnt;
                tree[p].maxType = (tree[p].left != -1) ? tree[tree[p].left].maxType : 0;
            } else if (rightCnt > leftCnt) {
                tree[p].maxCnt = rightCnt;
                tree[p].maxType = (tree[p].right != -1) ? tree[tree[p].right].maxType : 0;
            } else {
                tree[p].maxCnt = leftCnt;
                int leftType = (tree[p].left != -1) ? tree[tree[p].left].maxType : Integer.MAX_VALUE;
                int rightType = (tree[p].right != -1) ? tree[tree[p].right].maxType : Integer.MAX_VALUE;
                tree[p].maxType = Math.min(leftType, rightType);
            }
        }
        
        void update(int p, int l, int r, int type, int delta) {
            if (l == r) {
                tree[p].maxCnt += delta;
                tree[p].maxType = type;
                return;
            }
            
            int mid = (l + r) >> 1;
            if (type <= mid) {
                if (tree[p].left == -1) tree[p].left = newNode();
                update(tree[p].left, l, mid, type, delta);
            } else {
                if (tree[p].right == -1) tree[p].right = newNode();
                update(tree[p].right, mid + 1, r, type, delta);
            }
            pushUp(p);
        }
        
        // 线段树合并
        int merge(int p, int q, int l, int r) {
            if (p == -1) return q;
            if (q == -1) return p;
            
            if (l == r) {
                tree[p].maxCnt += tree[q].maxCnt;
                tree[p].maxType = l;
                return p;
            }
            
            int mid = (l + r) >> 1;
            tree[p].left = merge(tree[p].left, tree[q].left, l, mid);
            tree[p].right = merge(tree[p].right, tree[q].right, mid + 1, r);
            
            pushUp(p);
            return p;
        }
        
        // 线段树分裂
        int split(int p, int l, int r, int ql, int qr) {
            if (p == -1 || ql > r || qr < l) return -1;
            
            if (ql <= l && r <= qr) {
                int newP = newNode();
                tree[newP] = tree[p];
                tree[p].left = tree[p].right = -1;
                tree[p].maxCnt = 0;
                tree[p].maxType = 0;
                return newP;
            }
            
            int mid = (l + r) >> 1;
            int newP = newNode();
            
            if (ql <= mid && tree[p].left != -1) {
                tree[newP].left = split(tree[p].left, l, mid, ql, qr);
            }
            if (qr > mid && tree[p].right != -1) {
                tree[newP].right = split(tree[p].right, mid + 1, r, ql, qr);
            }
            
            pushUp(p);
            pushUp(newP);
            return newP;
        }
    }
    
    static int n, m;
    static List<Integer>[] graph;
    static int[] depth, parent, size, heavy, top, dfn, pos;
    static int timer;
    static SegmentTree segTree;
    static int[] root;
    static int[] ans;
    
    // 树链剖分 - 第一次DFS
    static void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        heavy[u] = -1;
        
        for (int v : graph[u]) {
            if (v == p) continue;
            dfs1(v, u);
            size[u] += size[v];
            if (heavy[u] == -1 || size[v] > size[heavy[u]]) {
                heavy[u] = v;
            }
        }
    }
    
    // 树链剖分 - 第二次DFS
    static void dfs2(int u, int t) {
        top[u] = t;
        dfn[u] = ++timer;
        pos[timer] = u;
        
        if (heavy[u] != -1) {
            dfs2(heavy[u], t);
        }
        
        for (int v : graph[u]) {
            if (v == parent[u] || v == heavy[u]) continue;
            dfs2(v, v);
        }
    }
    
    // 求LCA
    static int lca(int u, int v) {
        while (top[u] != top[v]) {
            if (depth[top[u]] < depth[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            u = parent[top[u]];
        }
        return depth[u] < depth[v] ? u : v;
    }
    
    // 树上差分更新
    static void updatePath(int u, int v, int type) {
        int l = lca(u, v);
        
        // 路径(u, v)上所有节点增加类型type的救济粮
        segTree.update(root[u], 1, 100000, type, 1);
        segTree.update(root[v], 1, 100000, type, 1);
        segTree.update(root[l], 1, 100000, type, -1);
        if (parent[l] != 0) {
            segTree.update(root[parent[l]], 1, 100000, type, -1);
        }
    }
    
    // 线段树合并DFS
    static void dfsMerge(int u) {
        for (int v : graph[u]) {
            if (v == parent[u]) continue;
            dfsMerge(v);
            root[u] = segTree.merge(root[u], root[v], 1, 100000);
        }
        
        // 记录答案
        if (segTree.tree[root[u]].maxCnt > 0) {
            ans[u] = segTree.tree[root[u]].maxType;
        } else {
            ans[u] = 0;
        }
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        n = io.nextInt();
        m = io.nextInt();
        
        // 初始化图
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        // 读入边
        for (int i = 1; i < n; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 初始化树链剖分数组
        depth = new int[n + 1];
        parent = new int[n + 1];
        size = new int[n + 1];
        heavy = new int[n + 1];
        top = new int[n + 1];
        dfn = new int[n + 1];
        pos = new int[n + 1];
        
        // 执行树链剖分
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 初始化线段树
        segTree = new SegmentTree();
        root = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            root[i] = segTree.newNode();
        }
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            int u = io.nextInt();
            int v = io.nextInt();
            int type = io.nextInt();
            updatePath(u, v, type);
        }
        
        // 合并线段树得到最终答案
        ans = new int[n + 1];
        dfsMerge(1);
        
        // 输出答案
        for (int i = 1; i <= n; i++) {
            io.println(ans[i]);
        }
        
        io.close();
    }
}

/**
 * 线段树合并与分裂在树上问题中的综合应用:
 * 
 * 1. 树上差分技术:
 *    - 通过LCA实现路径操作的差分标记
 *    - 将路径操作转化为节点操作
 *    - 支持高效的路径更新和查询
 * 
 * 2. 线段树合并的优势:
 *    - 高效合并子树信息: O(log n)时间复杂度
 *    - 支持复杂统计: 可以维护最大值、最小值、和等多种信息
 *    - 动态开点: 节省空间，只维护必要的节点
 * 
 * 3. 线段树分裂的应用:
 *    - 支持区间分离操作
 *    - 实现复杂的数据结构操作
 *    - 与线段树合并配合使用，形成完整的数据结构
 * 
 * 类似题目推荐:
 * 1. P3224 [HNOI2012]永无乡 - 线段树合并 + 并查集
 * 2. P5298 [PKUWC2018]Minimax - 概率DP + 线段树合并
 * 3. P6773 [NOI2020]命运 - 树形DP + 线段树合并
 * 4. CF600E Lomsat gelral - 子树统计 + 线段树合并
 * 
 * 算法复杂度分析:
 * - 树链剖分: O(n)预处理，O(log n)查询LCA
 * - 线段树合并: O(n log n)总体复杂度
 * - 空间复杂度: O(n log n)
 * 
 * 工程化优化:
 * 1. 内存池管理: 避免频繁的内存分配和回收
 * 2. 缓存友好: 优化内存访问模式
 * 3. 异常处理: 处理边界情况和非法输入
 * 4. 性能监控: 监控算法执行时间和内存使用
 */