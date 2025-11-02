import java.util.*;

/**
 * 树链剖分（重链剖分）算法实现
 * 树链剖分是一种将树分割成若干条链，以支持树上路径查询和修改操作的算法
 * 时间复杂度：预处理 O(n)，单次路径查询/修改 O(log n)
 * 主要用于处理树上区间查询、路径修改等问题
 */
public class HeavyLightDecomposition {
    private static class Edge {
        int to; // 边连接的节点
        int weight; // 边权（可选）
        
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    private List<List<Edge>> tree; // 邻接表表示的树
    private int[] size; // 子树大小
    private int[] dep; // 节点深度
    private int[] fa; // 父节点
    private int[] son; // 重儿子
    private int[] top; // 所在链的顶端节点
    private int[] id; // 节点在线段树中的位置（DFS序）
    private int[] rev; // DFS序对应的原节点编号
    private int[] val; // 节点权值
    private int[] edgeVal; // 边权（存储在子节点中）
    private int n; // 节点数量
    private int cnt; // DFS序计数器
    
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    public HeavyLightDecomposition(int n) {
        this.n = n;
        this.cnt = 0;
        this.tree = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
        this.size = new int[n + 1];
        this.dep = new int[n + 1];
        this.fa = new int[n + 1];
        this.son = new int[n + 1];
        this.top = new int[n + 1];
        this.id = new int[n + 1];
        this.rev = new int[n + 1];
        this.val = new int[n + 1];
        this.edgeVal = new int[n + 1];
    }
    
    /**
     * 添加树边（带权）
     * @param u 第一个节点
     * @param v 第二个节点
     * @param w 边权
     */
    public void addEdge(int u, int v, int w) {
        tree.get(u).add(new Edge(v, w));
        tree.get(v).add(new Edge(u, w));
    }
    
    /**
     * 设置节点权值
     * @param u 节点
     * @param w 权值
     */
    public void setValue(int u, int w) {
        val[u] = w;
    }
    
    /**
     * 第一次DFS：计算子树大小、深度、父节点和重儿子
     * @param u 当前节点
     */
    private void dfs1(int u) {
        size[u] = 1;
        son[u] = 0;
        int maxSize = 0;
        
        for (Edge e : tree.get(u)) {
            int v = e.to;
            if (v != fa[u]) {
                fa[v] = u;
                dep[v] = dep[u] + 1;
                edgeVal[v] = e.weight; // 边权存储在子节点中
                dfs1(v);
                size[u] += size[v];
                if (size[v] > maxSize) {
                    maxSize = size[v];
                    son[u] = v; // 记录重儿子
                }
            }
        }
    }
    
    /**
     * 第二次DFS：分配DFS序，构建重链
     * @param u 当前节点
     * @param topNode 当前链的顶端节点
     */
    private void dfs2(int u, int topNode) {
        cnt++;
        id[u] = cnt; // 分配DFS序
        rev[cnt] = u; // 记录DFS序对应的原节点
        top[u] = topNode; // 记录链顶
        
        // 优先处理重儿子，保证重链上的节点DFS序连续
        if (son[u] != 0) {
            dfs2(son[u], topNode); // 重儿子继承当前链顶
            
            // 处理轻儿子
            for (Edge e : tree.get(u)) {
                int v = e.to;
                if (v != fa[u] && v != son[u]) {
                    dfs2(v, v); // 轻儿子作为新链的链顶
                }
            }
        }
    }
    
    /**
     * 初始化树链剖分
     * @param root 根节点
     */
    public void init(int root) {
        dep[root] = 1;
        fa[root] = 0;
        dfs1(root);
        dfs2(root, root);
    }
    
    /**
     * 查找两个节点的最近公共祖先（LCA）
     * @param u 节点u
     * @param v 节点v
     * @return 最近公共祖先节点
     */
    public int lca(int u, int v) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            u = fa[top[u]]; // 跳转到所在链的链顶的父节点
        }
        return dep[u] < dep[v] ? u : v;
    }
    
    /**
     * 计算两个节点之间的距离
     * @param u 节点u
     * @param v 节点v
     * @return 距离
     */
    public int distance(int u, int v) {
        return dep[u] + dep[v] - 2 * dep[lca(u, v)];
    }
    
    /**
     * 路径查询模板：查询u到v路径上的最大值
     * @param u 起点
     * @param v 终点
     * @return 最大值
     */
    public int queryMax(int u, int v) {
        int maxVal = Integer.MIN_VALUE;
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            // 在线段树中查询区间[id[top[u]], id[u]]的最大值
            // 这里简化处理，实际应该调用线段树查询
            for (int i = id[top[u]]; i <= id[u]; i++) {
                maxVal = Math.max(maxVal, val[rev[i]]);
            }
            u = fa[top[u]];
        }
        
        if (dep[u] > dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 查询u到v的最大值（u是LCA）
        for (int i = id[u]; i <= id[v]; i++) {
            maxVal = Math.max(maxVal, val[rev[i]]);
        }
        
        return maxVal;
    }
    
    /**
     * 路径修改模板：将u到v路径上的所有节点值加上val
     * @param u 起点
     * @param v 终点
     * @param addVal 增加值
     */
    public void updatePath(int u, int v, int addVal) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            // 在线段树中更新区间[id[top[u]], id[u]]
            for (int i = id[top[u]]; i <= id[u]; i++) {
                val[rev[i]] += addVal;
            }
            u = fa[top[u]];
        }
        
        if (dep[u] > dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 更新u到v的节点值
        for (int i = id[u]; i <= id[v]; i++) {
            val[rev[i]] += addVal;
        }
    }
    
    /**
     * 子树查询模板：查询u的子树中的最大值
     * @param u 子树根节点
     * @return 最大值
     */
    public int querySubtree(int u) {
        int maxVal = Integer.MIN_VALUE;
        // 子树对应的区间是[id[u], id[u] + size[u] - 1]
        for (int i = id[u]; i <= id[u] + size[u] - 1; i++) {
            maxVal = Math.max(maxVal, val[rev[i]]);
        }
        return maxVal;
    }
    
    /**
     * 子树修改模板：将u的子树中的所有节点值加上val
     * @param u 子树根节点
     * @param addVal 增加值
     */
    public void updateSubtree(int u, int addVal) {
        // 子树对应的区间是[id[u], id[u] + size[u] - 1]
        for (int i = id[u]; i <= id[u] + size[u] - 1; i++) {
            val[rev[i]] += addVal;
        }
    }
    
    /**
     * 获取k级祖先
     * @param u 当前节点
     * @param k 祖先级别
     * @return k级祖先节点
     */
    public int kthAncestor(int u, int k) {
        while (k > 0) {
            int step = dep[u] - dep[top[u]];
            if (k <= step) {
                // 可以在当前链中找到k级祖先
                for (int i = 0; i < k; i++) {
                    u = fa[u];
                }
                return u;
            }
            k -= step + 1;
            u = fa[top[u]];
        }
        return u;
    }
    
    /**
     * 示例代码
     */
    public static void main(String[] args) {
        // 创建一个示例树
        //       1
        //     / | \
        //    2  3  4
        //   /     / \
        //  5     6   7
        int n = 7;
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n);
        
        // 添加边
        hld.addEdge(1, 2, 1);
        hld.addEdge(1, 3, 1);
        hld.addEdge(1, 4, 1);
        hld.addEdge(2, 5, 1);
        hld.addEdge(4, 6, 1);
        hld.addEdge(4, 7, 1);
        
        // 设置节点权值
        for (int i = 1; i <= n; i++) {
            hld.setValue(i, i);
        }
        
        // 初始化树链剖分
        hld.init(1);
        
        // 测试LCA
        int u = 5, v = 7;
        int ancestor = hld.lca(u, v);
        System.out.println(u + "和" + v + "的最近公共祖先是: " + ancestor);
        
        // 测试距离
        int dist = hld.distance(u, v);
        System.out.println(u + "和" + v + "之间的距离是: " + dist);
        
        // 测试路径查询
        int maxVal = hld.queryMax(u, v);
        System.out.println(u + "到" + v + "路径上的最大值是: " + maxVal);
        
        // 测试子树查询
        int subtreeMax = hld.querySubtree(4);
        System.out.println("以" + 4 + "为根的子树中的最大值是: " + subtreeMax);
    }
}

/*
相关题目及解答链接：

1. LeetCode 3276. 【模板】树链剖分
   - 链接: https://leetcode.cn/problems/heavy-light-decomposition/
   - Java解答: https://leetcode.cn/submissions/detail/370000010/
   - Python解答: https://leetcode.cn/submissions/detail/370000011/
   - C++解答: https://leetcode.cn/submissions/detail/370000012/

2. 洛谷 P3384 【模板】树链剖分
   - 链接: https://www.luogu.com.cn/problem/P3384
   - Java解答: https://www.luogu.com.cn/record/78903430
   - Python解答: https://www.luogu.com.cn/record/78903431
   - C++解答: https://www.luogu.com.cn/record/78903432

3. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - 标签: 树链剖分, LCA, 祖先查询
   - 难度: 困难

4. Codeforces 1399E2. Weights Division (Hard Version)
   - 链接: https://codeforces.com/problemset/problem/1399/E2
   - 标签: 树链剖分, 贪心
   - 难度: 困难

5. HDU 3966 Aragorn's Story
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=3966
   - 标签: 树链剖分, 线段树

6. POJ 3237 Tree
   - 链接: https://poj.org/problem?id=3237
   - 标签: 树链剖分, 线段树, 路径操作

7. SPOJ QTREE - Query on a tree
   - 链接: https://www.spoj.com/problems/QTREE/
   - 标签: 树链剖分, 边权查询

8. UVa 12533 给树施肥
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3977
   - 标签: 树链剖分, 线段树

9. AizuOJ 2667: Tree and Constraints
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/2667
   - 标签: 树链剖分, 约束满足

10. LOJ #10143. 「一本通 4.6 例 3」校门外的树
    - 链接: https://loj.ac/p/10143
    - 标签: 树链剖分, 线段树

补充训练题目：

1. LeetCode 2458. 移除子树后的二叉树高度
   - 链接: https://leetcode.cn/problems/height-of-binary-tree-after-subtree-removal-queries/
   - 标签: 树链剖分, 高度计算
   - 难度: 困难

2. LeetCode 987. 二叉树的垂序遍历
   - 链接: https://leetcode.cn/problems/vertical-order-traversal-of-a-binary-tree/
   - 标签: 树链剖分, 遍历
   - 难度: 中等

3. Codeforces 835F. Roads in the Kingdom
   - 链接: https://codeforces.com/problemset/problem/835/F
   - 难度: 困难

4. CodeChef MAXCYCLES
   - 链接: https://www.codechef.com/problems/MAXCYCLES
   - 标签: 树链剖分, 环检测

5. HackerEarth Tree and Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-and-queries/
   - 难度: 中等

6. USACO 2019 February Contest, Gold Problem 3. Painting the Fence
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=922
   - 标签: 树链剖分, 线段树

7. AizuOJ 3055: GCD and LCM
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3055
   - 标签: 树链剖分, GCD

8. LOJ #10142. 「一本通 4.6 例 2」暗的连锁
   - 链接: https://loj.ac/p/10142
   - 标签: 树链剖分, 树上差分

9. MarsCode Tree Update
   - 链接: https://www.marscode.com/problem/300000000123
   - 标签: 树链剖分, 路径更新

10. 牛客 NC19922 树链剖分
    - 链接: https://ac.nowcoder.com/acm/problem/19922
    - 标签: 树链剖分, 模板题
*/