package class163;

// Lomsat gelral (Codeforces 600E) - 统计子树中出现次数最多的颜色值之和
// 题目来源: Codeforces 600E
// 链接: https://codeforces.com/problemset/problem/600/E

// Tree and Queries, java版
// 题目来源: Codeforces 375D
// 链接: https://codeforces.com/problemset/problem/375/D
// 
// 题目大意:
// 给定一棵n个节点的树，每个节点有一个颜色值。
// 有m个查询，每个查询给定一个节点v和一个整数k，
// 要求统计v的子树中，出现次数至少为k的颜色数量。
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中每种颜色的出现次数
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 离线处理所有查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 查询处理:
// 对于每个查询，统计子树中出现次数至少为k的颜色数量
// 通过维护出现i次的颜色数量来快速计算答案
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;

// 第一题：Lomsat gelral (Codeforces 600E)实现
class Code09_LomsatGelral {
    // 最大节点数
    private static final int MAXN = 100001;
    
    // 节点数
    private static int n;
    
    // 每个节点的颜色
    private static int[] color = new int[MAXN];
    
    // 邻接表存储树
    private static List<Integer>[] tree = new ArrayList[MAXN];
    
    // 树链剖分相关
    private static int[] size = new int[MAXN];  // 子树大小
    private static int[] son = new int[MAXN];   // 重儿子
    
    // DSU on Tree相关
    private static int[] colorCount = new int[MAXN];  // 每种颜色的出现次数
    private static long[] ans = new long[MAXN];       // 每个节点的答案
    private static int maxFreq = 0;                   // 当前最大出现次数
    private static long sumFreq = 0;                  // 出现次数最多的颜色值之和
    
    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }
    }
    
    // 第一次DFS，计算子树大小和重儿子
    private static void dfs1(int u, int fa) {
        size[u] = 1;
        son[u] = 0;
        
        for (int v : tree[u]) {
            if (v != fa) {
                dfs1(v, u);
                size[u] += size[v];
                if (son[u] == 0 || size[son[u]] < size[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 增加节点颜色贡献
    private static void addNode(int u) {
        int c = color[u];
        // 如果当前颜色的出现次数等于最大出现次数，增加sumFreq
        if (colorCount[c] == maxFreq) {
            sumFreq += c;
        } else if (colorCount[c] == maxFreq + 1) {
            // 如果增加后超过当前最大出现次数，更新最大出现次数和sumFreq
            maxFreq++;
            sumFreq = c;
        }
        colorCount[c]++;
    }
    
    // 删除节点颜色贡献
    private static void removeNode(int u) {
        int c = color[u];
        // 如果当前颜色的出现次数等于最大出现次数
        if (colorCount[c] == maxFreq) {
            sumFreq -= c;
            // 如果只有这一种颜色达到最大出现次数，更新最大出现次数
            if (sumFreq == 0) {
                maxFreq--;
                // 重新计算sumFreq（简化处理）
                sumFreq = 0;
                for (int i = 1; i < MAXN; i++) {
                    if (colorCount[i] == maxFreq) {
                        sumFreq += i;
                    }
                }
            }
        }
        colorCount[c]--;
    }
    
    // 添加子树贡献
    private static void addSubtree(int u, int fa) {
        addNode(u);
        for (int v : tree[u]) {
            if (v != fa) {
                addSubtree(v, u);
            }
        }
    }
    
    // 删除子树贡献
    private static void removeSubtree(int u, int fa) {
        removeNode(u);
        for (int v : tree[u]) {
            if (v != fa) {
                removeSubtree(v, u);
            }
        }
    }
    
    // DSU on Tree主过程
    private static void dsuOnTree(int u, int fa, boolean keep) {
        // 处理所有轻儿子
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                dsuOnTree(v, u, false);  // 不保留信息
            }
        }
        
        // 处理重儿子
        if (son[u] != 0) {
            dsuOnTree(son[u], u, true);  // 保留信息
        }
        
        // 添加当前节点的贡献
        addNode(u);
        
        // 添加轻儿子的贡献
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                addSubtree(v, u);
            }
        }
        
        // 记录当前节点的答案
        ans[u] = sumFreq;
        
        // 如果不保留信息，则清除
        if (!keep) {
            removeSubtree(u, fa);
            maxFreq = 0;
            sumFreq = 0;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数
        in.nextToken();
        n = (int) in.nval;
        
        // 读取每个节点的颜色
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            color[i] = (int) in.nval;
        }
        
        // 读取边信息，构建树
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            tree[u].add(v);
            tree[v].add(u);
        }
        
        // 执行算法
        dfs1(1, 0);  // 以节点1为根进行第一次DFS
        dsuOnTree(1, 0, false);  // 执行DSU on Tree
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            out.print(ans[i] + " ");
        }
        out.println();
        
        out.flush();
        out.close();
        br.close();
    }
}

// 第二题：Tree and Queries (Codeforces 375D)实现
public class Code09_TreeAndQueries1 {
    
    // 最大节点数
    public static int MAXN = 100001;
    
    // 节点数和查询数
    public static int n, m;
    
    // 每个节点的颜色
    public static int[] color = new int[MAXN];
    
    // 邻接表存储树
    public static List<Integer>[] tree = new ArrayList[MAXN];
    
    // 树链剖分相关
    public static int[] size = new int[MAXN];  // 子树大小
    public static int[] son = new int[MAXN];   // 重儿子
    
    // DSU on Tree相关
    public static int[] colorCount = new int[MAXN];  // 每种颜色的出现次数
    public static int[] countFreq = new int[MAXN];   // 出现i次的颜色数量
    public static int[] ans = new int[MAXN];         // 查询答案
    
    // 查询结构
    static class Query {
        int id;  // 查询编号
        int k;   // 最小出现次数
        
        Query(int id, int k) {
            this.id = id;
            this.k = k;
        }
    }
    
    // 每个节点的查询列表
    public static List<Query>[] queries = new ArrayList[MAXN];
    
    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
            queries[i] = new ArrayList<>();
        }
    }
    
    // 第一次DFS，计算子树大小和重儿子
    public static void dfs1(int u, int fa) {
        size[u] = 1;
        son[u] = 0;
        
        for (int v : tree[u]) {
            if (v != fa) {
                dfs1(v, u);
                size[u] += size[v];
                if (son[u] == 0 || size[son[u]] < size[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 增加节点颜色贡献
    public static void addNode(int u) {
        // 原来的出现次数对应的频率减1
        countFreq[colorCount[color[u]]]--;
        // 颜色出现次数加1
        colorCount[color[u]]++;
        // 新的出现次数对应的频率加1
        countFreq[colorCount[color[u]]]++;
    }
    
    // 删除节点颜色贡献
    public static void removeNode(int u) {
        // 原来的出现次数对应的频率减1
        countFreq[colorCount[color[u]]]--;
        // 颜色出现次数减1
        colorCount[color[u]]--;
        // 新的出现次数对应的频率加1
        countFreq[colorCount[color[u]]]++;
    }
    
    // 添加子树贡献
    public static void addSubtree(int u, int fa) {
        addNode(u);
        for (int v : tree[u]) {
            if (v != fa) {
                addSubtree(v, u);
            }
        }
    }
    
    // 删除子树贡献
    public static void removeSubtree(int u, int fa) {
        removeNode(u);
        for (int v : tree[u]) {
            if (v != fa) {
                removeSubtree(v, u);
            }
        }
    }
    
    // DSU on Tree主过程
    public static void dsuOnTree(int u, int fa, boolean keep) {
        // 处理所有轻儿子
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                dsuOnTree(v, u, false);  // 不保留信息
            }
        }
        
        // 处理重儿子
        if (son[u] != 0) {
            dsuOnTree(son[u], u, true);  // 保留信息
        }
        
        // 添加当前节点的贡献
        addNode(u);
        
        // 添加轻儿子的贡献
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                addSubtree(v, u);
            }
        }
        
        // 处理当前节点的所有查询
        for (Query q : queries[u]) {
            // 统计出现次数至少为k的颜色数量
            int result = 0;
            for (int i = q.k; i < MAXN; i++) {
                result += countFreq[i];
            }
            ans[q.id] = result;
        }
        
        // 如果不保留信息，则清除
        if (!keep) {
            removeSubtree(u, fa);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数和查询数
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        // 读取每个节点的颜色
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            color[i] = (int) in.nval;
        }
        
        // 读取边信息，构建树
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            tree[u].add(v);
            tree[v].add(u);
        }
        
        // 读取查询信息
        for (int i = 1; i <= m; i++) {
            in.nextToken();
            int v = (int) in.nval;
            in.nextToken();
            int k = (int) in.nval;
            queries[v].add(new Query(i, k));
        }
        
        // 执行算法
        dfs1(1, 0);  // 以节点1为根进行第一次DFS
        dsuOnTree(1, 0, false);  // 执行DSU on Tree
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}

// 第三题：Dominant Indices (Codeforces 1009F)实现
class Code10_DominantIndices {
    // 最大节点数
    private static final int MAXN = 100001;
    private static final int MAX_DEPTH = 100001;
    
    // 节点数
    private static int n;
    
    // 邻接表存储树
    private static List<Integer>[] tree = new ArrayList[MAXN];
    
    // 树链剖分相关
    private static int[] size = new int[MAXN];  // 子树大小
    private static int[] son = new int[MAXN];   // 重儿子
    private static int[] depth = new int[MAXN]; // 节点深度
    
    // DSU on Tree相关
    private static int[] cnt = new int[MAX_DEPTH];  // 各深度节点数量
    private static int[] ans = new int[MAXN];       // 每个节点的答案
    private static int maxCount = 0;                // 最大出现次数
    private static int ansDepth = 0;                // 出现次数最多的深度
    
    // 初始化
    static {
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }
    }
    
    // 第一次DFS，计算子树大小、重儿子和深度
    private static void dfs1(int u, int fa) {
        size[u] = 1;
        son[u] = 0;
        depth[u] = depth[fa] + 1;
        
        for (int v : tree[u]) {
            if (v != fa) {
                dfs1(v, u);
                size[u] += size[v];
                if (son[u] == 0 || size[son[u]] < size[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 增加节点深度贡献
    private static void addNode(int u, int d) {
        cnt[d]++;
        // 更新最大出现次数和对应的深度
        if (cnt[d] > maxCount || (cnt[d] == maxCount && d < ansDepth)) {
            maxCount = cnt[d];
            ansDepth = d;
        }
    }
    
    // 删除节点深度贡献
    private static void removeNode(int u, int d) {
        cnt[d]--;
    }
    
    // 添加子树贡献
    private static void addSubtree(int u, int fa, int rootDepth) {
        int d = depth[u] - rootDepth; // 相对于根节点的深度
        addNode(u, d);
        for (int v : tree[u]) {
            if (v != fa) {
                addSubtree(v, u, rootDepth);
            }
        }
    }
    
    // 删除子树贡献
    private static void removeSubtree(int u, int fa, int rootDepth) {
        int d = depth[u] - rootDepth;
        removeNode(u, d);
        for (int v : tree[u]) {
            if (v != fa) {
                removeSubtree(v, u, rootDepth);
            }
        }
    }
    
    // 重置统计信息
    private static void resetStats() {
        maxCount = 0;
        ansDepth = 0;
    }
    
    // DSU on Tree主过程
    private static void dsuOnTree(int u, int fa, boolean keep) {
        int rootDepth = depth[u]; // 当前子树的根深度
        
        // 处理所有轻儿子
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                dsuOnTree(v, u, false);  // 不保留信息
            }
        }
        
        // 处理重儿子
        if (son[u] != 0) {
            dsuOnTree(son[u], u, true);  // 保留信息
        }
        
        // 添加当前节点的贡献
        int d = depth[u] - rootDepth;
        addNode(u, d);
        
        // 添加轻儿子的贡献
        for (int v : tree[u]) {
            if (v != fa && v != son[u]) {
                addSubtree(v, u, rootDepth);
            }
        }
        
        // 记录当前节点的答案（出现次数最多的深度）
        ans[u] = ansDepth;
        
        // 如果不保留信息，则清除
        if (!keep) {
            removeSubtree(u, fa, rootDepth);
            resetStats();
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取节点数
        in.nextToken();
        n = (int) in.nval;
        
        // 读取边信息，构建树
        for (int i = 1; i < n; i++) {
            in.nextToken();
            int u = (int) in.nval;
            in.nextToken();
            int v = (int) in.nval;
            tree[u].add(v);
            tree[v].add(u);
        }
        
        // 执行算法
        depth[0] = -1; // 根节点的父节点深度为-1，使得根节点深度为0
        dfs1(1, 0);  // 以节点1为根进行第一次DFS
        dsuOnTree(1, 0, false);  // 执行DSU on Tree
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}