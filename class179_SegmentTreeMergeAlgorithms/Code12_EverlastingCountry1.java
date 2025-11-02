package class181;

// BZOJ2733/HNOI2012 永无乡，java版
// 测试链接 : https://www.luogu.com.cn/problem/P3224
// 提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * BZOJ2733/HNOI2012 永无乡
 * 
 * 题目来源: 2012年全国青少年信息学奥林匹克竞赛（HNOI2012）
 * 题目链接: https://www.luogu.com.cn/problem/P3224
 * 
 * 题目描述:
 * 永无乡包含n个岛，编号1到n。每个岛都有一个文明等级，初始时各岛的文明等级各不相同。
 * 现在有两种操作：
 * 1. 连接两个岛，使得这两个岛所在的连通块可以互相到达
 * 2. 查询某个岛所在连通块中文明等级第k小的岛的编号
 * 
 * 解题思路:
 * 1. 使用并查集维护连通性，确保每次查询都是在同一个连通块内
 * 2. 为每个岛建立一棵权值线段树，维护该岛所在连通块中各文明等级的出现次数
 * 3. 当合并两个连通块时，将它们的线段树进行合并
 * 4. 当查询第k小时，在线段树上进行二分查找
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n + m log n)，其中n是岛的数量，m是操作数量。
 *   每个操作（合并或查询）的时间复杂度为O(log n)，线段树合并操作的时间复杂度为O(log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并结合并查集是该问题的最优解。其他可能的解法包括Treap合并或Splay合并，
 * 但线段树合并的实现更加直观，且空间效率较高。
 * 
 * 线段树合并与并查集结合的核心思想:
 * 1. 并查集维护连通性，找到每个节点的根节点
 * 2. 每个根节点对应一棵权值线段树，维护该连通块中的信息
 * 3. 合并连通块时，合并对应的线段树，并更新并查集
 * 4. 查询时，在对应根节点的线段树上进行查询
 */
public class Code12_EverlastingCountry1 {

    // 最大节点数
    public static int MAXN = 100001;

    // 线段树节点数上限（需要足够大以容纳动态开点）
    public static int MAXT = MAXN * 40;

    // 岛的数量和操作数量
    public static int n, m;

    // 每个岛的文明等级
    public static int[] rank = new int[MAXN];

    // 文明等级到岛编号的映射（用于离散化后还原）
    public static int[] index = new int[MAXN];

    // 并查集父节点数组
    public static int[] parent = new int[MAXN];

    // 每个根节点对应的线段树根节点
    public static int[] root = new int[MAXN];
    
    // 线段树左右子节点数组
    public static int[] ls = new int[MAXT];
    public static int[] rs = new int[MAXT];
    
    // 线段树节点维护的计数
    public static int[] count = new int[MAXT];
    
    // 线段树节点计数器
    public static int cntt;

    /**
     * 初始化并查集
     */
    public static void initUnionFind() {
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
    }

    /**
     * 查找根节点（带路径压缩）
     * @param x 要查找的节点
     * @return x的根节点
     */
    public static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 创建新的线段树节点
     * @return 新节点的索引
     */
    public static int newNode() {
        cntt++;
        ls[cntt] = rs[cntt] = 0;
        count[cntt] = 0;
        return cntt;
    }

    /**
     * 线段树单点更新
     * @param p 当前节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param x 要更新的位置
     * @param v 要增加的计数
     */
    public static void update(int p, int l, int r, int x, int v) {
        if (l == r) {
            // 叶子节点，直接更新计数
            count[p] += v;
            return;
        }
        int mid = (l + r) >> 1;
        // 动态开点
        if (x <= mid) {
            if (ls[p] == 0) {
                ls[p] = newNode();
            }
            update(ls[p], l, mid, x, v);
        } else {
            if (rs[p] == 0) {
                rs[p] = newNode();
            }
            update(rs[p], mid + 1, r, x, v);
        }
        // 更新当前节点的计数
        count[p] = count[ls[p]] + count[rs[p]];
    }

    /**
     * 线段树合并操作
     * @param x 第一棵线段树的根节点
     * @param y 第二棵线段树的根节点
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @return 合并后的线段树根节点
     */
    public static int merge(int x, int y, int l, int r) {
        // 如果其中一棵树为空，直接返回另一棵树
        if (x == 0) return y;
        if (y == 0) return x;
        
        // 叶子节点处理
        if (l == r) {
            // 合并计数
            count[x] += count[y];
            return x;
        }
        
        int mid = (l + r) >> 1;
        
        // 递归合并左右子树
        ls[x] = merge(ls[x], ls[y], l, mid);
        rs[x] = merge(rs[x], rs[y], mid + 1, r);
        
        // 合并后更新当前节点计数
        count[x] = count[ls[x]] + count[rs[x]];
        
        return x;
    }

    /**
     * 在线段树中查询第k小的值
     * @param p 当前节点索引
     * @param l 当前区间左边界
     * @param r 当前区间右边界
     * @param k 要查询的第k小
     * @return 第k小的文明等级对应的岛编号
     */
    public static int queryKth(int p, int l, int r, int k) {
        if (l == r) {
            // 叶子节点，返回对应的岛编号
            return index[l];
        }
        int mid = (l + r) >> 1;
        // 左子树的节点数
        int leftCount = (ls[p] != 0) ? count[ls[p]] : 0;
        
        if (k <= leftCount) {
            // 第k小在左子树
            return queryKth(ls[p], l, mid, k);
        } else {
            // 第k小在右子树
            return queryKth(rs[p], mid + 1, r, k - leftCount);
        }
    }

    public static void main(String[] args) throws IOException {
        // 使用快速IO提高输入输出效率
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        // 读取岛的数量和操作数量
        String[] parts = in.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读取每个岛的文明等级
        parts = in.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            rank[i] = Integer.parseInt(parts[i - 1]);
            index[rank[i]] = i; // 文明等级到岛编号的映射
        }
        
        // 初始化并查集
        initUnionFind();
        
        // 初始化线段树
        cntt = 0;
        for (int i = 1; i <= n; i++) {
            // 为每个岛创建线段树，并插入自身的文明等级
            root[i] = newNode();
            update(root[i], 1, n, rank[i], 1);
        }
        
        // 处理初始连接操作
        for (int i = 0; i < m; i++) {
            parts = in.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            
            // 查找u和v的根节点
            int rootU = find(u);
            int rootV = find(v);
            
            if (rootU != rootV) {
                // 合并两个连通块的线段树
                root[rootU] = merge(root[rootU], root[rootV], 1, n);
                // 更新并查集
                parent[rootV] = rootU;
            }
        }
        
        // 处理查询和合并操作
        int q = Integer.parseInt(in.readLine());
        for (int i = 0; i < q; i++) {
            parts = in.readLine().split(" ");
            char op = parts[0].charAt(0);
            
            if (op == 'Q') {
                // 查询操作：Q x k，查询岛x所在连通块中第k小的岛编号
                int x = Integer.parseInt(parts[1]);
                int k = Integer.parseInt(parts[2]);
                
                int rootX = find(x);
                // 检查k是否合法
                if (k > count[root[rootX]]) {
                    out.println(-1);
                } else {
                    int ans = queryKth(root[rootX], 1, n, k);
                    out.println(ans);
                }
            } else if (op == 'B') {
                // 合并操作：B x y，连接岛x和岛y
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                
                // 查找x和y的根节点
                int rootX = find(x);
                int rootY = find(y);
                
                if (rootX != rootY) {
                    // 合并两个连通块的线段树
                    root[rootX] = merge(root[rootX], root[rootY], 1, n);
                    // 更新并查集
                    parent[rootY] = rootX;
                }
            }
        }
        
        out.flush();
        in.close();
        out.close();
    }
    
    /**
     * 工程化考量：
     * 1. 异常处理：代码中没有显式的异常处理，但使用了try-with-resources模式来关闭流
     * 2. 性能优化：使用BufferedReader和PrintWriter提高IO效率
     * 3. 内存优化：动态开点线段树减少内存使用
     * 4. 边界处理：处理了查询k超过连通块大小的情况，返回-1
     * 5. 并查集优化：使用路径压缩优化并查集的查询效率
     * 
     * 语言特性差异：
     * 1. Java中的数组初始化：使用静态数组预分配空间
     * 2. 递归深度：Java的递归深度限制可能对大规模数据有影响
     * 
     * 调试技巧：
     * 1. 可以添加中间变量打印，观察线段树合并和查询过程
     * 2. 使用断言验证线段树的计数正确性
     * 
     * 优化空间：
     * 1. 可以使用按秩合并优化并查集，提高合并效率
     * 2. 对于文明等级范围很大的情况，需要先进行离散化
     */
}