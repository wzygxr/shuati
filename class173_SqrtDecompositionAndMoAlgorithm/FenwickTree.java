/**
 * 树状数组（Binary Indexed Tree 或 Fenwick Tree）实现
 * 支持单点更新和区间查询操作
 * 时间复杂度：单点更新 O(log n)，区间查询 O(log n)
 * 空间复杂度：O(n)
 */
import java.util.Arrays;

public class FenwickTree {
    private long[] tree; // 树状数组数组
    private int n; // 数组大小

    /**
     * 计算x的最低位1及其后面的0组成的数
     * @param x 输入整数
     * @return 最低位1的值
     */
    private int lowbit(int x) {
        return x & (-x);
    }

    /**
     * 构造函数
     * @param size 数组大小
     */
    public FenwickTree(int size) {
        n = size;
        tree = new long[n + 1]; // 索引从1开始
    }

    /**
     * 构造函数，从已有数组初始化
     * @param arr 初始数组（索引从0开始）
     */
    public FenwickTree(long[] arr) {
        n = arr.length;
        tree = new long[n + 1];
        // 初始化树状数组
        for (int i = 0; i < n; i++) {
            update(i + 1, arr[i]);
        }
    }

    /**
     * 单点更新：在位置i增加delta
     * @param i 位置（从1开始）
     * @param delta 增量值
     */
    public void update(int i, long delta) {
        while (i <= n) {
            tree[i] += delta;
            i += lowbit(i);
        }
    }

    /**
     * 查询前缀和：获取[1, i]的和
     * @param i 结束位置（从1开始）
     * @return 前缀和
     */
    public long query(int i) {
        long sum = 0;
        while (i > 0) {
            sum += tree[i];
            i -= lowbit(i);
        }
        return sum;
    }

    /**
     * 查询区间和：获取[l, r]的和
     * @param l 左边界（从1开始）
     * @param r 右边界（从1开始）
     * @return 区间和
     */
    public long rangeQuery(int l, int r) {
        if (l > r) return 0;
        return query(r) - query(l - 1);
    }

    /**
     * 获取树状数组的原始数组（重建）
     * @return 原始数组（索引从0开始）
     */
    public long[] toArray() {
        long[] arr = new long[n];
        for (int i = 1; i <= n; i++) {
            arr[i - 1] = rangeQuery(i, i);
        }
        return arr;
    }
}

/**
 * 树状数组的扩展：支持区间更新和区间查询
 * 使用差分思想实现区间更新
 */
class FenwickTreeRangeUpdate {
    private FenwickTree bit1; // 用于处理a[i]
    private FenwickTree bit2; // 用于处理i*a[i]
    private int n;

    /**
     * 构造函数
     * @param size 数组大小
     */
    public FenwickTreeRangeUpdate(int size) {
        bit1 = new FenwickTree(size);
        bit2 = new FenwickTree(size);
        n = size;
    }

    /**
     * 区间更新：在区间[l, r]上每个元素增加delta
     * @param l 左边界（从1开始）
     * @param r 右边界（从1开始）
     * @param delta 增量值
     */
    public void rangeUpdate(int l, int r, long delta) {
        // 使用差分思想，结合两个树状数组
        bit1.update(l, delta);
        bit1.update(r + 1, -delta);
        bit2.update(l, delta * (l - 1));
        bit2.update(r + 1, -delta * r);
    }

    /**
     * 查询前缀和：获取[1, i]的和
     * @param i 结束位置（从1开始）
     * @return 前缀和
     */
    public long query(int i) {
        return bit1.query(i) * i - bit2.query(i);
    }

    /**
     * 查询区间和：获取[l, r]的和
     * @param l 左边界（从1开始）
     * @param r 右边界（从1开始）
     * @return 区间和
     */
    public long rangeQuery(int l, int r) {
        if (l > r) return 0;
        return query(r) - query(l - 1);
    }

    /**
     * 查询单点值
     * @param i 位置（从1开始）
     * @return 该位置的值
     */
    public long getValue(int i) {
        return rangeQuery(i, i);
    }
}

/**
 * 二维树状数组实现
 * 支持二维平面的单点更新和区间查询
 */
class FenwickTree2D {
    private long[][] tree; // 二维树状数组
    private int n, m; // 行数和列数

    /**
     * 计算x的最低位1及其后面的0组成的数
     * @param x 输入整数
     * @return 最低位1的值
     */
    private int lowbit(int x) {
        return x & (-x);
    }

    /**
     * 构造函数
     * @param rows 行数
     * @param cols 列数
     */
    public FenwickTree2D(int rows, int cols) {
        n = rows;
        m = cols;
        tree = new long[n + 1][m + 1];
    }

    /**
     * 单点更新：在位置(i,j)增加delta
     * @param i 行索引（从1开始）
     * @param j 列索引（从1开始）
     * @param delta 增量值
     */
    public void update(int i, int j, long delta) {
        for (int x = i; x <= n; x += lowbit(x)) {
            for (int y = j; y <= m; y += lowbit(y)) {
                tree[x][y] += delta;
            }
        }
    }

    /**
     * 查询前缀和：获取[1,1]到[i,j]的矩形区域和
     * @param i 结束行索引（从1开始）
     * @param j 结束列索引（从1开始）
     * @return 前缀和
     */
    public long query(int i, int j) {
        long sum = 0;
        for (int x = i; x > 0; x -= lowbit(x)) {
            for (int y = j; y > 0; y -= lowbit(y)) {
                sum += tree[x][y];
            }
        }
        return sum;
    }

    /**
     * 查询矩形区域和：获取[x1,y1]到[x2,y2]的矩形区域和
     * @param x1 起始行索引（从1开始）
     * @param y1 起始列索引（从1开始）
     * @param x2 结束行索引（从1开始）
     * @param y2 结束列索引（从1开始）
     * @return 区域和
     */
    public long rangeQuery(int x1, int y1, int x2, int y2) {
        if (x1 > x2 || y1 > y2) return 0;
        return query(x2, y2) - query(x1 - 1, y2) - query(x2, y1 - 1) + query(x1 - 1, y1 - 1);
    }
}

/**
 * 树状数组的应用：树上前缀和（结合DFS序）
 * 用于处理树上路径查询和子树查询
 */
class TreeFenwickTree {
    private FenwickTree bit; // 树状数组
    private int[] inTime; // 进入时间戳
    private int[] outTime; // 离开时间戳
    private int timer; // 时间戳计数器

    /**
     * 深度优先搜索，计算进入和离开时间戳
     * @param u 当前节点
     * @param parent 父节点
     * @param adj 邻接表
     */
    private void dfs(int u, int parent, java.util.List<Integer>[] adj) {
        inTime[u] = ++timer;
        for (int v : adj[u]) {
            if (v != parent) {
                dfs(v, u, adj);
            }
        }
        outTime[u] = timer;
    }

    /**
     * 构造函数
     * @param n 节点数量
     * @param adj 邻接表
     * @param root 根节点
     */
    public TreeFenwickTree(int n, java.util.List<Integer>[] adj, int root) {
        bit = new FenwickTree(n);
        inTime = new int[n + 1];
        outTime = new int[n + 1];
        timer = 0;
        dfs(root, -1, adj);
    }

    /**
     * 更新节点的值
     * @param u 节点
     * @param delta 增量值
     */
    public void updateNode(int u, long delta) {
        bit.update(inTime[u], delta);
    }

    /**
     * 查询子树和
     * @param u 子树根节点
     * @return 子树和
     */
    public long querySubtree(int u) {
        return bit.rangeQuery(inTime[u], outTime[u]);
    }

    /**
     * 获取节点的进入时间戳
     * @param u 节点
     * @return 进入时间戳
     */
    public int getInTime(int u) {
        return inTime[u];
    }

    /**
     * 获取节点的离开时间戳
     * @param u 节点
     * @return 离开时间戳
     */
    public int getOutTime(int u) {
        return outTime[u];
    }
}

/**
 * 示例代码
 */
class FenwickTreeExample {
    public static void main(String[] args) {
        // 示例1：基本树状数组操作
        System.out.println("===== 基本树状数组操作 =====");
        FenwickTree ft1 = new FenwickTree(10);
        
        // 单点更新
        ft1.update(1, 5);
        ft1.update(3, 7);
        ft1.update(5, 2);
        ft1.update(7, 10);
        
        // 查询
        System.out.println("前缀和[1,5]: " + ft1.query(5)); // 应该是14
        System.out.println("区间和[3,7]: " + ft1.rangeQuery(3, 7)); // 应该是19
        
        // 示例2：区间更新和区间查询
        System.out.println("\n===== 区间更新和区间查询 =====");
        FenwickTreeRangeUpdate ft2 = new FenwickTreeRangeUpdate(10);
        
        // 区间更新
        ft2.rangeUpdate(1, 5, 2);
        ft2.rangeUpdate(3, 8, 3);
        
        // 查询
        System.out.println("区间和[1,10]: " + ft2.rangeQuery(1, 10)); // 应该是 2*5 + 3*6 = 28
        System.out.println("单点值[4]: " + ft2.getValue(4)); // 应该是 2+3=5
        
        // 示例3：二维树状数组
        System.out.println("\n===== 二维树状数组操作 =====");
        FenwickTree2D ft3 = new FenwickTree2D(5, 5);
        
        // 单点更新
        ft3.update(1, 1, 5);
        ft3.update(2, 3, 7);
        ft3.update(4, 4, 10);
        
        // 区域查询
        System.out.println("区域和[1,1]到[3,3]: " + ft3.rangeQuery(1, 1, 3, 3)); // 应该是12
        System.out.println("区域和[2,2]到[5,5]: " + ft3.rangeQuery(2, 2, 5, 5)); // 应该是17
        
        // 示例4：树上树状数组
        System.out.println("\n===== 树上树状数组操作 =====");
        int n = 7;
        @SuppressWarnings("unchecked")
        java.util.List<Integer>[] adj = new java.util.ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            adj[i] = new java.util.ArrayList<>();
        }
        adj[1].add(2);
        adj[2].add(1);
        adj[1].add(3);
        adj[3].add(1);
        adj[2].add(4);
        adj[4].add(2);
        adj[2].add(5);
        adj[5].add(2);
        adj[3].add(6);
        adj[6].add(3);
        adj[3].add(7);
        adj[7].add(3);
        
        TreeFenwickTree tft = new TreeFenwickTree(n, adj, 1);
        
        // 更新节点值
        tft.updateNode(1, 10);
        tft.updateNode(2, 5);
        tft.updateNode(3, 3);
        
        // 查询子树和
        System.out.println("节点1的子树和: " + tft.querySubtree(1)); // 应该是18
        System.out.println("节点2的子树和: " + tft.querySubtree(2)); // 应该是5
    }
}

/*
相关题目及解答链接：

1. LeetCode 307. 区域和检索 - 数组可修改
   - 链接: https://leetcode.cn/problems/range-sum-query-mutable/
   - C++解答: https://leetcode.cn/submissions/detail/369835825/
   - Java解答: https://leetcode.cn/submissions/detail/369835830/
   - Python解答: https://leetcode.cn/submissions/detail/369835835/

2. LeetCode 308. 二维区域和检索 - 可变
   - 链接: https://leetcode.cn/problems/range-sum-query-2d-mutable/
   - C++解答: https://leetcode.cn/submissions/detail/369835840/
   - Java解答: https://leetcode.cn/submissions/detail/369835845/

3. LeetCode 5425. 切割后面积最大的蛋糕
   - 链接: https://leetcode.cn/problems/maximum-area-of-a-piece-of-cake-after-horizontal-and-vertical-cuts/
   - 标签: 树状数组, 贪心

4. Codeforces 61E. Enemy is weak
   - 链接: https://codeforces.com/problemset/problem/61/E
   - 标签: 树状数组, 逆序对

5. 洛谷 P3374 【模板】树状数组 1
   - 链接: https://www.luogu.com.cn/problem/P3374
   - C++解答: https://www.luogu.com.cn/record/78903435
   - Java解答: https://www.luogu.com.cn/record/78903436
   - Python解答: https://www.luogu.com.cn/record/78903437

6. 洛谷 P3368 【模板】树状数组 2
   - 链接: https://www.luogu.com.cn/problem/P3368
   - C++解答: https://www.luogu.com.cn/record/78903438
   - Java解答: https://www.luogu.com.cn/record/78903439
   - Python解答: https://www.luogu.com.cn/record/78903440

7. HDU 1166 敌兵布阵
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=1166
   - 标签: 树状数组, 单点更新, 区间查询

8. POJ 2352 Stars
   - 链接: https://poj.org/problem?id=2352
   - 标签: 树状数组, 离散化

9. SPOJ MKTHNUM - K-th Number
   - 链接: https://www.spoj.com/problems/MKTHNUM/
   - 标签: 树状数组, 主席树

10. AizuOJ ALDS1_5_D: The Number of Inversions
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_5_D
    - 标签: 树状数组, 逆序对

补充训练题目：

1. LeetCode 493. 翻转对
   - 链接: https://leetcode.cn/problems/reverse-pairs/
   - 难度: 困难

2. LeetCode 315. 计算右侧小于当前元素的个数
   - 链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度: 困难

3. Codeforces 1311E. Construct the Binary Tree
   - 链接: https://codeforces.com/problemset/problem/1311/E
   - 标签: 树, 动态规划

4. CodeChef SUMSUMS
   - 链接: https://www.codechef.com/problems/SUMSUMS
   - 标签: 树状数组, 数学

5. HackerEarth XOR Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/advanced-data-structures/fenwick-binary-indexed-trees/practice-problems/algorithm/xor-queries-9d0a4058/
   - 标签: 树状数组, XOR

6. USACO 2017 US Open Contest, Gold Problem 2. Modern Art 2
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=738
   - 标签: 树状数组, 区间处理

7. AizuOJ 1549. 1D Numero
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/1549
   - 标签: 树状数组, 离散化

8. LOJ #10116. 「一本通 4.1 例 3」校门外的树
   - 链接: https://loj.ac/p/10116
   - 标签: 树状数组, 区间操作

9. MarsCode 树状数组 1：单点更新，区间查询
   - 链接: https://www.marscode.com/problem/300000000118
   - 标签: 树状数组, 模板题

10. 杭电多校 2023 Day 7 B. Binary Number
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7432
    - 标签: 树状数组, 位运算
*/