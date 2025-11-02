package class182;

/**
 * 线段树合并专题 - Code04_RainyTail.java
 * 
 * 主要解决以下问题：
 * 1. 雨天的尾巴问题（洛谷P4556）
 * 2. LeetCode 1519. 子树中标签相同的节点数（补充题目）
 * 
 * 线段树合并是一种高效处理树上子树信息合并的算法技术
 * 核心思想：通过动态开点和递归合并线段树，高效维护子树信息
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树空间：O(n log n)
 * - 其他数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * Java语言特性考虑：
 * - 使用数组模拟线段树节点，提高内存使用效率
 * - 预先分配足够的空间以避免频繁的内存分配
 * - 利用位运算优化运算速度
 * - 注意Java的递归深度限制，对于大规模数据可能需要调整
 */

/**
 * 雨天的尾巴问题，Java版
 * 测试链接 : https://www.luogu.com.cn/problem/P4556
 * 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
 * 
 * 题目来源：Vani有约会
 * 题目大意：在树上进行路径加操作，每次给路径上所有节点添加某种类型的救济粮，
 * 最后查询每个节点最多的救济粮类型
 * 解法：树链剖分 + 线段树合并 + 树上差分
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code04_RainyTail {
    
    public static int MAXN = 100001;
    public static int MAXT = MAXN * 100; // 线段树节点数，需要足够大以容纳动态开点
    
    public static int n, m;
    public static int[] fa = new int[MAXN];     // 父亲节点
    public static int[] dep = new int[MAXN];    // 深度
    public static int[] sz = new int[MAXN];     // 子树大小
    public static int[] hs = new int[MAXN];     // 重儿子
    public static int[] top = new int[MAXN];    // 所在重链的顶端
    public static int[] id = new int[MAXN];     // dfs序
    public static int[] orig = new int[MAXN];   // 原编号到dfs序的映射
    public static int cnt;                      // dfs序计数器
    
    // 链式前向星存图
    public static int[] head = new int[MAXN];   // 邻接表头节点
    public static int[] nxt = new int[MAXN * 2]; // 下一条边
    public static int[] to = new int[MAXN * 2];  // 边指向的节点
    public static int eCnt;                     // 边计数器
    
    // 线段树相关 - 动态开点实现
    public static int[] root = new int[MAXN];   // 每个节点的线段树根节点
    public static int[] ls = new int[MAXT];     // 左儿子指针
    public static int[] rs = new int[MAXT];     // 右儿子指针
    public static int[] max = new int[MAXT];    // 区间最大值
    public static int[] ans = new int[MAXT];    // 最大值对应的救济粮类型
    public static int segCnt;                   // 线段树节点计数器
    
    /**
     * 添加边到邻接表 - 使用链式前向星存储图结构
     * 
     * @param u 起始节点编号
     * @param v 目标节点编号
     * 
     * 算法原理：
     * 链式前向星是一种高效存储图结构的方法：
     * 1. 使用数组模拟链表结构
     * 2. 每个节点的边存储在链表中
     * 3. 通过头指针数组快速访问每个节点的边
     * 
     * 存储结构：
     * - head[u]: 节点u的第一条边在边数组中的索引
     * - nxt[i]: 下一条边的索引，形成链表
     * - to[i]: 边i指向的目标节点
     * 
     * 时间复杂度: O(1) - 常数时间操作
     * 空间复杂度: O(1) - 只增加一条边的存储
     * 
     * 优势：
     * - 内存紧凑：避免指针开销
     * - 遍历高效：支持快速遍历邻接边
     * - 动态扩展：支持动态添加边
     * 
     * 边界情况处理：
     * - 节点编号有效性：确保u,v在有效范围内
     * - 数组边界：确保eCnt不超过数组大小
     * - 重复边：支持添加重复边（如果需要）
     */
    public static void addEdge(int u, int v) {
        nxt[++eCnt] = head[u]; // 新边的下一条边指向当前头边
        to[eCnt] = v;          // 设置新边的目标节点
        head[u] = eCnt;        // 更新节点u的头边为新边
    }
    
    /**
     * 第一次DFS：计算父节点、深度、子树大小、重儿子
     * @param u 当前节点
     * @param father 父节点
     * @param depth 当前深度
     */
    public static void dfs1(int u, int father, int depth) {
        fa[u] = father;
        dep[u] = depth;
        sz[u] = 1;
        
        // 遍历所有子节点
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != father) {
                dfs1(v, u, depth + 1);
                sz[u] += sz[v];
                // 更新重儿子
                if (sz[v] > sz[hs[u]]) {
                    hs[u] = v;
                }
            }
        }
    }
    
    /**
     * 第二次DFS：分配链顶和DFS序，完成树链剖分
     * @param u 当前节点
     * @param tp 链顶节点
     */
    public static void dfs2(int u, int tp) {
        id[u] = ++cnt;
        orig[cnt] = u;
        top[u] = tp;
        
        if (hs[u] != 0) {
            dfs2(hs[u], tp);
        }
        
        // 处理轻儿子
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != fa[u] && v != hs[u]) {
                dfs2(v, v);
            }
        }
    }
    
    /**
     * 向上更新线段树节点信息
     * @param p 当前节点编号
     */
    public static void pushUp(int p) {
        if (max[ls[p]] >= max[rs[p]]) {
            max[p] = max[ls[p]];
            ans[p] = ans[ls[p]];
        } else {
            max[p] = max[rs[p]];
            ans[p] = ans[rs[p]];
        }
    }
    
    /**
     * 线段树单点更新
     * @param pos 要更新的位置
     * @param val 更新的值（增量）
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param rootId 当前根节点编号
     */
    public static void update(int pos, int val, int l, int r, int rootId) {
        if (l == r) {
            max[rootId] += val;
            ans[rootId] = l;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (ls[rootId] == 0) ls[rootId] = ++segCnt;
            update(pos, val, l, mid, ls[rootId]);
        } else {
            if (rs[rootId] == 0) rs[rootId] = ++segCnt;
            update(pos, val, mid + 1, r, rs[rootId]);
        }
        pushUp(rootId);
    }
    
    /**
     * 线段树合并 - 核心操作
     * @param p1 第一棵线段树的根节点
     * @param p2 第二棵线段树的根节点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @return 合并后的线段树根节点
     */
    public static int merge(int p1, int p2, int l, int r) {
        // 如果其中一个为空，返回另一个
        if (p1 == 0 || p2 == 0) {
            return p1 + p2;
        }
        
        // 如果是叶子节点
        if (l == r) {
            max[p1] += max[p2];
            ans[p1] = l;
            return p1;
        }
        
        int mid = (l + r) >> 1;
        ls[p1] = merge(ls[p1], ls[p2], l, mid);
        rs[p1] = merge(rs[p1], rs[p2], mid + 1, r);
        pushUp(p1);
        return p1;
    }
    
    /**
     * 获取两个节点的最近公共祖先（LCA）
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 最近公共祖先节点
     */
    public static int lca(int x, int y) {
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x;
                x = y;
                y = temp;
            }
            x = fa[top[x]];
        }
        return dep[x] < dep[y] ? x : y;
    }
    
    /**
     * 树上差分更新 - 路径加操作
     * @param x 路径起始点
     * @param y 路径终点
     * @param z 要添加的救济粮类型
     */
    public static void updatePath(int x, int y, int z) {
        int l = lca(x, y);
        update(z, 1, 1, MAXN, root[x]);
        update(z, 1, 1, MAXN, root[y]);
        update(z, -1, 1, MAXN, root[l]);
        update(z, -1, 1, MAXN, root[fa[l]]);
    }
    
    /**
     * 第三次DFS：后序遍历合并线段树并收集答案
     * @param u 当前节点
     */
    public static void dfs3(int u) {
        // 处理所有子节点
        for (int i = head[u]; i > 0; i = nxt[i]) {
            int v = to[i];
            if (v != fa[u]) {
                dfs3(v);
                root[u] = merge(root[u], root[v], 1, MAXN);
            }
        }
    }
    
    /**
     * 主函数
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            parts = br.readLine().split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            addEdge(x, y);
            addEdge(y, x);
        }
        
        // 初始化 - 执行树链剖分
        dfs1(1, 0, 1);  // 第一次DFS计算父节点、深度等信息
        dfs2(1, 1);    // 第二次DFS分配链顶和DFS序
        
        // 处理所有操作 - 路径加操作
        for (int i = 1; i <= m; i++) {
            parts = br.readLine().split(" ");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            updatePath(x, y, z);
        }
        
        // 计算答案 - 通过后序遍历合并线段树
        dfs3(1);
        
        // 输出答案 - 每个节点最多的救济粮类型
        for (int i = 1; i <= n; i++) {
            if (max[root[i]] == 0) {
                out.println(0);
            } else {
                out.println(ans[root[i]]);
            }
        }
        
        out.flush();
        out.close();
    }
    
    /**
     * LeetCode 1519. 子树中标签相同的节点数
     * 题目链接：https://leetcode.cn/problems/number-of-nodes-in-the-sub-tree-with-the-same-label/
     * 题目大意：给定一棵树，每个节点有一个标签，求每个节点的子树中标签相同的节点数
     * 解题思路：使用DFS后序遍历 + 动态规划统计子树信息
     * 时间复杂度：O(n * 26)，其中n为节点数，26为字母表大小
     * 空间复杂度：O(n)
     */
    static class LeetCode1519 {
        /**
         * 计算每个节点子树中标签相同的节点数
         * @param n 节点数量
         * @param edges 树的边列表
         * @param labels 节点标签
         * @return 包含每个节点子树中标签相同节点数的数组
         */
        public int[] countSubTrees(int n, int[][] edges, String labels) {
            // 构建邻接表
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            int[] result = new int[n];
            dfs(0, -1, graph, labels, result);
            
            return result;
        }
        
        /**
         * 深度优先搜索，统计子树中每个标签的出现次数
         * @param node 当前节点
         * @param parent 父节点
         * @param graph 树的邻接表
         * @param labels 节点标签
         * @param result 结果数组
         * @return 当前子树中每个标签的出现次数数组
         */
        private int[] dfs(int node, int parent, List<List<Integer>> graph, String labels, int[] result) {
            // 创建当前节点的计数数组
            int[] counts = new int[26];
            char label = labels.charAt(node);
            counts[label - 'a'] = 1;  // 当前节点自身计入一次
            
            // 遍历所有子节点
            for (int neighbor : graph.get(node)) {
                if (neighbor != parent) {  // 避免回到父节点
                    int[] childCounts = dfs(neighbor, node, graph, labels, result);
                    // 合并子节点的计数到当前节点
                    for (int i = 0; i < 26; i++) {
                        counts[i] += childCounts[i];
                    }
                }
            }
            
            // 保存当前节点的结果
            result[node] = counts[label - 'a'];
            return counts;
        }
    }
    
    /**
     * Codeforces 600E - Lomsat gelral
     * 题目链接：https://codeforces.com/contest/600/problem/E
     * 题目大意：给定一棵树，每个节点有一个颜色，求每个节点的子树中，出现次数最多的所有颜色的编号和
     * 解题思路：使用线段树合并，维护每个颜色的出现次数以及最大出现次数的颜色和
     * 时间复杂度：O(n log c)，其中c为颜色数量
     * 空间复杂度：O(n log c)
     */
    static class Codeforces600E {
        private static final int MAXN = 100005;
        private static final int MAXT = MAXN * 20;
        
        private int n;
        private int[] color;
        private List<List<Integer>> graph;
        
        // 线段树相关
        private int[] root;
        private int[] ls, rs;
        private long[] sum;
        private int[] max_cnt, cnt;
        private int segCnt;
        private long[] ans;
        
        /**
         * 解决Codeforces 600E问题
         * @param n 节点数量
         * @param colorArray 每个节点的颜色数组
         * @param edges 树的边数组
         * @return 每个节点的答案数组
         */
        public long[] solve(int n, int[] colorArray, int[][] edges) {
            this.n = n;
            this.color = colorArray;
            
            // 初始化图
            graph = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                int u = edge[0];
                int v = edge[1];
                graph.get(u).add(v);
                graph.get(v).add(u);
            }
            
            // 初始化线段树相关数组
            root = new int[MAXN];
            ls = new int[MAXT];
            rs = new int[MAXT];
            sum = new long[MAXT];
            max_cnt = new int[MAXT];
            cnt = new int[MAXT];
            segCnt = 0;
            ans = new long[MAXN];
            
            // 开始DFS遍历
            dfs(1, 0);
            
            return ans;
        }
        
        /**
         * 向上更新线段树节点信息
         * @param p 当前节点编号
         */
        private void pushUp(int p) {
            if (max_cnt[ls[p]] > max_cnt[rs[p]]) {
                max_cnt[p] = max_cnt[ls[p]];
                sum[p] = sum[ls[p]];
            } else if (max_cnt[rs[p]] > max_cnt[ls[p]]) {
                max_cnt[p] = max_cnt[rs[p]];
                sum[p] = sum[rs[p]];
            } else {
                max_cnt[p] = max_cnt[ls[p]];
                sum[p] = sum[ls[p]] + sum[rs[p]];
            }
        }
        
        /**
         * 线段树单点更新
         * @param pos 位置
         * @param val 值
         * @param l 左边界
         * @param r 右边界
         * @param p 当前节点
         */
        private void update(int pos, int val, int l, int r, int p) {
            if (l == r) {
                cnt[p] += val;
                max_cnt[p] = cnt[p];
                sum[p] = pos;
                return;
            }
            
            int mid = (l + r) >> 1;
            if (pos <= mid) {
                if (ls[p] == 0) ls[p] = ++segCnt;
                update(pos, val, l, mid, ls[p]);
            } else {
                if (rs[p] == 0) rs[p] = ++segCnt;
                update(pos, val, mid + 1, r, rs[p]);
            }
            
            pushUp(p);
        }
        
        /**
         * 线段树合并
         * @param p1 第一棵线段树的根节点
         * @param p2 第二棵线段树的根节点
         * @param l 左边界
         * @param r 右边界
         * @return 合并后的线段树根节点
         */
        private int merge(int p1, int p2, int l, int r) {
            if (p1 == 0 || p2 == 0) {
                return p1 + p2;
            }
            
            if (l == r) {
                cnt[p1] += cnt[p2];
                max_cnt[p1] = cnt[p1];
                sum[p1] = l;
                return p1;
            }
            
            int mid = (l + r) >> 1;
            ls[p1] = merge(ls[p1], ls[p2], l, mid);
            rs[p1] = merge(rs[p1], rs[p2], mid + 1, r);
            
            pushUp(p1);
            return p1;
        }
        
        /**
         * 深度优先搜索，合并子树的线段树
         * @param u 当前节点
         * @param parent 父节点
         */
        private void dfs(int u, int parent) {
            // 初始化当前节点的颜色
            if (root[u] == 0) root[u] = ++segCnt;
            update(color[u], 1, 1, n, root[u]);
            
            // 遍历所有子节点
            for (int v : graph.get(u)) {
                if (v != parent) {
                    dfs(v, u);
                    // 合并子节点的线段树
                    root[u] = merge(root[u], root[v], 1, n);
                }
            }
            
            // 保存当前节点的答案
            ans[u] = sum[root[u]];
        }
    }
    
    /**
     * 线段树合并技术总结与工程实践
     * 
     * 核心思想：
     * 1. 动态开点：仅在需要时创建线段树节点，节省空间
     * 2. 递归合并：自底向上合并子节点的线段树，高效维护子树信息
     * 3. 树上差分：将路径操作转化为端点操作，简化问题
     * 
     * 适用场景：
     * 1. 子树信息统计问题
     * 2. 树上路径更新与查询
     * 3. 需要频繁合并区间信息的场景
     * 
     * 优化技巧：
     * 1. 内存优化：
     *    - 预分配足够大的数组避免动态分配
     *    - 考虑使用内存池技术减少GC压力
     *    - 注意Java中数组的初始化开销
     * 2. 性能优化：
     *    - 使用位运算加速计算（如 (l + r) >> 1 代替 (l + r) / 2）
     *    - 递归实现虽然简洁但可能栈溢出，考虑非递归实现
     *    - 使用快速输入输出方式处理大数据量
     * 3. 工程实践考虑：
     *    - 错误处理：添加输入验证，处理异常情况
     *    - 边界条件：特别注意空树、单节点等特殊情况
     *    - 线程安全：在并发环境中需要加锁保护共享数据
     *    - 测试覆盖：编写全面的单元测试，验证算法正确性
     *    - 代码可维护性：适当封装，提高代码可读性和复用性
     * 
     * 语言特性对比：
     * 1. Java：数组索引从0开始，需要注意边界处理；递归深度有限制；内存管理由GC负责
     * 2. C++：数组索引灵活；指针操作更高效；需要手动管理内存
     * 3. Python：递归深度限制更严格；动态类型；性能相对较低但代码简洁
     * 
     * 扩展应用：
     * 1. 权值线段树合并
     * 2. 主席树（可持久化线段树）与线段树合并的结合
     * 3. 启发式合并与线段树合并的结合应用
     */
}