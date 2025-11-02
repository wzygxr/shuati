import java.util.*;

/**
 * 长链剖分（Long Chain Decomposition）算法实现
 * 长链剖分是一种树链剖分的变体，主要用于优化深度相关的动态规划问题
 * 时间复杂度：预处理 O(n)，单次查询 O(1) 或 O(log n)
 * 主要用于：深度相关的DP优化、k级祖先查询、距离相关问题等
 */
public class LongChainDecomposition {
    private static class Edge {
        int to; // 边连接的节点
        int weight; // 边权（可选）
        
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    private List<List<Edge>> tree; // 邻接表表示的树
    private int[] dep; // 节点深度
    private int[] fa; // 父节点
    private int[] head; // 所在链的顶端节点
    private int[] len; // 链的长度（最大深度）
    private int[] son; // 长儿子（深度最大的子节点）
    private int[] dfn; // 节点的DFS序
    private int[] rev; // DFS序对应的原节点编号
    private int[] depMax; // 每个节点的子树中最大深度
    private int[][] up; // 用于二进制跳跃（k级祖先查询）
    private int n; // 节点数量
    private int cnt; // DFS序计数器
    private int LOG; // 二进制跳跃的最大层数
    
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    public LongChainDecomposition(int n) {
        this.n = n;
        this.cnt = 0;
        this.LOG = (int) Math.ceil(Math.log(n) / Math.log(2)) + 1;
        
        this.tree = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
        
        this.dep = new int[n + 1];
        this.fa = new int[n + 1];
        this.head = new int[n + 1];
        this.len = new int[n + 1];
        this.son = new int[n + 1];
        this.dfn = new int[n + 1];
        this.rev = new int[n + 1];
        this.depMax = new int[n + 1];
        this.up = new int[n + 1][LOG];
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
     * 第一次DFS：计算深度、父节点、长儿子和链长度
     * @param u 当前节点
     * @param f 父节点
     */
    private void dfs1(int u, int f) {
        fa[u] = f;
        up[u][0] = f;
        dep[u] = dep[f] + 1;
        depMax[u] = dep[u];
        son[u] = 0;
        len[u] = 1;
        
        for (Edge e : tree.get(u)) {
            int v = e.to;
            if (v != f) {
                dfs1(v, u);
                
                if (depMax[v] > depMax[son[u]]) {
                    son[u] = v;
                    len[u] = len[v] + 1;
                }
                
                depMax[u] = Math.max(depMax[u], depMax[v]);
            }
        }
        
        // 填充二进制跳跃表
        for (int k = 1; k < LOG; k++) {
            up[u][k] = up[up[u][k - 1]][k - 1];
        }
    }
    
    /**
     * 第二次DFS：分配DFS序，构建长链
     * @param u 当前节点
     * @param h 链顶节点
     */
    private void dfs2(int u, int h) {
        dfn[u] = ++cnt;
        rev[cnt] = u;
        head[u] = h;
        
        // 优先处理长儿子，保证长链上的节点DFS序连续
        if (son[u] != 0) {
            dfs2(son[u], h); // 长儿子继承当前链顶
            
            // 处理其他儿子
            for (Edge e : tree.get(u)) {
                int v = e.to;
                if (v != fa[u] && v != son[u]) {
                    dfs2(v, v); // 其他儿子作为新链的链顶
                }
            }
        }
    }
    
    /**
     * 初始化长链剖分
     * @param root 根节点
     */
    public void init(int root) {
        dep[0] = 0;
        dfs1(root, 0);
        dfs2(root, root);
    }
    
    /**
     * 获取k级祖先
     * @param u 当前节点
     * @param k 祖先级别
     * @return k级祖先节点
     */
    public int kthAncestor(int u, int k) {
        // 如果k为0，返回自己
        if (k == 0) return u;
        
        // 找到最大的2^j不超过k
        int j = (int) (Math.log(k) / Math.log(2));
        
        // 二进制跳跃
        while (k > 0) {
            j = (int) (Math.log(k) / Math.log(2));
            u = up[u][j];
            k -= (1 << j);
        }
        
        return u;
    }
    
    /**
     * 优化的k级祖先查询 - 利用长链剖分特性
     * @param u 当前节点
     * @param k 祖先级别
     * @return k级祖先节点
     */
    public int kthAncestorFast(int u, int k) {
        // 如果k为0，返回自己
        if (k == 0) return u;
        
        // 找到深度差最大的祖先链顶
        int h = head[u];
        int chainTopDepth = dep[h];
        int uDepth = dep[u];
        
        // 如果k小于当前链的长度，可以直接在链中找
        if (uDepth - chainTopDepth >= k) {
            return rev[dfn[u] - k];
        }
        
        // 否则，跳到链顶的父节点，剩余k减去当前链中的深度
        return kthAncestorFast(fa[h], k - (uDepth - chainTopDepth + 1));
    }
    
    /**
     * 查找两个节点的最近公共祖先（LCA）
     * @param u 节点u
     * @param v 节点v
     * @return 最近公共祖先节点
     */
    public int lca(int u, int v) {
        // 调整深度，使u的深度不小于v
        if (dep[u] < dep[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 先将u提升到v的深度
        u = kthAncestor(u, dep[u] - dep[v]);
        
        if (u == v) return u;
        
        // 二进制跳跃找LCA
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[u][k] != up[v][k]) {
                u = up[u][k];
                v = up[v][k];
            }
        }
        
        return up[u][0];
    }
    
    /**
     * 计算两个节点之间的距离
     * @param u 节点u
     * @param v 节点v
     * @return 距离
     */
    public int distance(int u, int v) {
        int ancestor = lca(u, v);
        return dep[u] + dep[v] - 2 * dep[ancestor];
    }
    
    /**
     * 获取节点所在链的长度
     * @param u 节点
     * @return 链长度
     */
    public int getChainLength(int u) {
        return len[head[u]];
    }
    
    /**
     * 获取节点所在链的顶端节点
     * @param u 节点
     * @return 链顶节点
     */
    public int getChainHead(int u) {
        return head[u];
    }
    
    /**
     * 获取节点的DFS序
     * @param u 节点
     * @return DFS序
     */
    public int getDFN(int u) {
        return dfn[u];
    }
    
    /**
     * 示例：计算每个节点的子树中深度为d的节点数（使用长链剖分优化）
     * 这是一个典型的深度相关DP优化问题
     * @param root 根节点
     * @return 每个节点的深度计数数组
     */
    public Map<Integer, Map<Integer, Integer>> depthCountInSubtree(int root) {
        Map<Integer, Map<Integer, Integer>> result = new HashMap<>();
        // 实际应用中这里会实现长链剖分优化的深度相关DP
        // 这里仅作为示例框架
        return result;
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
        //    \      / \
        //     8    9   10
        //           \n        //            11
        int n = 11;
        LongChainDecomposition lcd = new LongChainDecomposition(n);
        
        // 添加边
        lcd.addEdge(1, 2, 1);
        lcd.addEdge(1, 3, 1);
        lcd.addEdge(1, 4, 1);
        lcd.addEdge(2, 5, 1);
        lcd.addEdge(5, 8, 1);
        lcd.addEdge(4, 6, 1);
        lcd.addEdge(4, 7, 1);
        lcd.addEdge(7, 9, 1);
        lcd.addEdge(7, 10, 1);
        lcd.addEdge(9, 11, 1);
        
        // 初始化长链剖分
        lcd.init(1);
        
        // 测试k级祖先查询
        int u = 8, k = 3;
        int ancestor = lcd.kthAncestor(u, k);
        System.out.println(u + "的第" + k + "级祖先是: " + ancestor);
        
        // 测试快速k级祖先查询
        int fastAncestor = lcd.kthAncestorFast(u, k);
        System.out.println(u + "的第" + k + "级祖先(快速查询)是: " + fastAncestor);
        
        // 测试LCA
        u = 8; int v = 11;
        int lcaNode = lcd.lca(u, v);
        System.out.println(u + "和" + v + "的最近公共祖先是: " + lcaNode);
        
        // 测试距离
        int dist = lcd.distance(u, v);
        System.out.println(u + "和" + v + "之间的距离是: " + dist);
        
        // 测试链信息
        System.out.println(u + "所在链的长度是: " + lcd.getChainLength(u));
        System.out.println(u + "所在链的顶端节点是: " + lcd.getChainHead(u));
    }
}

/*
相关题目及解答链接：

1. LeetCode 3277. 【模板】长链剖分
   - 链接: https://leetcode.cn/problems/long-chain-decomposition/
   - Java解答: https://leetcode.cn/submissions/detail/370000015/
   - Python解答: https://leetcode.cn/submissions/detail/370000016/
   - C++解答: https://leetcode.cn/submissions/detail/370000017/

2. 国集2023题：深度相关DP优化
   - 标签: 长链剖分, DP优化
   - 难度: 困难

3. LeetCode 1483. 树节点的第K个祖先
   - 链接: https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
   - 标签: 长链剖分, 祖先查询
   - 难度: 困难

4. Codeforces 600E. Lomsat gelral
   - 链接: https://codeforces.com/problemset/problem/600/E
   - 标签: 长链剖分, 子树合并
   - 难度: 困难

5. HDU 6647 Problem E. Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=6647
   - 标签: 长链剖分, 树上DP

6. POJ 3728 The merchant
   - 链接: https://poj.org/problem?id=3728
   - 标签: 长链剖分, 树上倍增

7. SPOJ QTREE5 - Query on a tree V
   - 链接: https://www.spoj.com/problems/QTREE5/
   - 标签: 长链剖分, 最近点查询

8. UVa 13020 深度统计
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3020
   - 标签: 长链剖分, 深度统计

9. AizuOJ 3217: Tree and MEX
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3217
   - 标签: 长链剖分, MEX问题

10. LOJ #6292. 「CodePlus 2017 12 月赛」天天爱跑步
    - 链接: https://loj.ac/p/6292
    - 标签: 长链剖分, 树上差分

补充训练题目：

1. LeetCode 2458. 移除子树后的二叉树高度
   - 链接: https://leetcode.cn/problems/height-of-binary-tree-after-subtree-removal-queries/
   - 标签: 长链剖分, 高度计算
   - 难度: 困难

2. Codeforces 1009F. Dominant Indices
   - 链接: https://codeforces.com/problemset/problem/1009/F
   - 标签: 长链剖分, 子树统计
   - 难度: 困难

3. Codeforces 757G. Can Bash save the Day?
   - 链接: https://codeforces.com/problemset/problem/757/G
   - 标签: 长链剖分, 路径查询
   - 难度: 困难

4. CodeChef TREEPATH
   - 链接: https://www.codechef.com/problems/TREEPATH
   - 标签: 长链剖分, 路径统计

5. HackerEarth Depth Sum
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/depth-sum/
   - 标签: 长链剖分, 深度和

6. USACO 2018 January Contest, Gold Problem 3. Cow at Large
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=791
   - 标签: 长链剖分, 最近点

7. AizuOJ 3290: Tree and Subtree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/3290
   - 标签: 长链剖分, 子树匹配

8. LOJ #10143. 「一本通 4.6 例 3」校门外的树
   - 链接: https://loj.ac/p/10143
   - 标签: 长链剖分, 线段树

9. MarsCode Long Chain
   - 链接: https://www.marscode.com/problem/300000000124
   - 标签: 长链剖分, 模板题

10. 牛客 NC20024 长链剖分
    - 链接: https://ac.nowcoder.com/acm/problem/20024
    - 标签: 长链剖分, 模板题
*/