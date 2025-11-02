/**
 * 树链剖分（Heavy-Light Decomposition）算法实现
 * 
 * 算法核心思想：
 * 1. 将树分解为多条重链（heavy chains）和轻链（light chains）
 * 2. 重链上的节点在DFS序中是连续的
 * 3. 使用线段树等数据结构维护每条链上的信息
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 路径查询/更新：O(log²n) 或 O(log n)（取决于底层数据结构）
 * 
 * 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 树上路径查询（最大值、最小值、和等）
 * 2. 树上路径更新
 * 3. 子树查询和更新
 * 4. LCA（最近公共祖先）查询
 * 
 * 题目来源：
 * - Codeforces 343D - Water Tree
 * - SPOJ QTREE - Query on a tree
 * - HDU 3966 - Aragorn's Story
 * - POJ 3237 - Tree
 * - 洛谷 P3384 【模板】轻重链剖分
 */

import java.util.*;

public class HeavyLightDecomposition {
    
    // 树的基本信息
    private int n;                          // 节点数量
    private List<Integer>[] tree;            // 树的邻接表表示
    private int[] parent;                    // 父节点数组
    private int[] depth;                     // 深度数组
    private int[] size;                      // 子树大小
    private int[] heavy;                     // 重儿子节点
    private int[] head;                      // 链头节点
    private int[] pos;                       // DFS序位置
    private int curPos;                      // 当前DFS序位置
    
    // 线段树相关（用于维护链上信息）
    private int[] segTree;                   // 线段树数组
    private int[] lazy;                      // 懒标记数组
    private int[] arr;                       // 原始数组值
    
    /**
     * 构造函数
     * @param n 节点数量
     */
    public HeavyLightDecomposition(int n) {
        this.n = n;
        this.tree = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }
        this.parent = new int[n + 1];
        this.depth = new int[n + 1];
        this.size = new int[n + 1];
        this.heavy = new int[n + 1];
        this.head = new int[n + 1];
        this.pos = new int[n + 1];
        this.curPos = 0;
        
        // 线段树相关初始化
        this.segTree = new int[4 * n];
        this.lazy = new int[4 * n];
        this.arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
    }
    
    /**
     * 添加边
     * @param u 节点u
     * @param v 节点v
     */
    public void addEdge(int u, int v) {
        tree[u].add(v);
        tree[v].add(u);
    }
    
    /**
     * 第一次DFS：计算子树大小和重儿子
     * @param u 当前节点
     * @param p 父节点
     */
    private void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        
        int maxSize = 0;
        for (int v : tree[u]) {
            if (v == p) continue;
            dfs1(v, u);
            size[u] += size[v];
            if (size[v] > maxSize) {
                maxSize = size[v];
                heavy[u] = v;
            }
        }
    }
    
    /**
     * 第二次DFS：构建重链
     * @param u 当前节点
     * @param h 链头节点
     */
    private void dfs2(int u, int h) {
        head[u] = h;
        pos[u] = curPos++;
        
        if (heavy[u] != -1) {
            dfs2(heavy[u], h);
        }
        
        for (int v : tree[u]) {
            if (v == parent[u] || v == heavy[u]) continue;
            dfs2(v, v);
        }
    }
    
    /**
     * 初始化树链剖分
     * @param root 根节点
     */
    public void build(int root) {
        dfs1(root, 0);
        dfs2(root, root);
        buildSegTree(1, 0, n - 1);
    }
    
    /**
     * 构建线段树
     * @param idx 线段树节点索引
     * @param l 区间左边界
     * @param r 区间右边界
     */
    private void buildSegTree(int idx, int l, int r) {
        if (l == r) {
            segTree[idx] = arr[l];
            return;
        }
        int mid = (l + r) / 2;
        buildSegTree(2 * idx, l, mid);
        buildSegTree(2 * idx + 1, mid + 1, r);
        segTree[idx] = segTree[2 * idx] + segTree[2 * idx + 1];
    }
    
    /**
     * 路径查询：查询u到v路径上的和
     * @param u 节点u
     * @param v 节点v
     * @return 路径和
     */
    public int queryPath(int u, int v) {
        int res = 0;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            res += querySegTree(1, 0, n - 1, pos[head[u]], pos[u]);
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        res += querySegTree(1, 0, n - 1, pos[u], pos[v]);
        return res;
    }
    
    /**
     * 路径更新：将u到v路径上的值增加val
     * @param u 节点u
     * @param v 节点v
     * @param val 增加值
     */
    public void updatePath(int u, int v, int val) {
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            updateSegTree(1, 0, n - 1, pos[head[u]], pos[u], val);
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        updateSegTree(1, 0, n - 1, pos[u], pos[v], val);
    }
    
    /**
     * 子树查询：查询以u为根的子树的和
     * @param u 根节点
     * @return 子树和
     */
    public int querySubtree(int u) {
        return querySegTree(1, 0, n - 1, pos[u], pos[u] + size[u] - 1);
    }
    
    /**
     * 子树更新：将以u为根的子树的值增加val
     * @param u 根节点
     * @param val 增加值
     */
    public void updateSubtree(int u, int val) {
        updateSegTree(1, 0, n - 1, pos[u], pos[u] + size[u] - 1, val);
    }
    
    /**
     * 线段树区间查询
     */
    private int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (l > segR || r < segL) return 0;
        if (lazy[idx] != 0) {
            segTree[idx] += (segR - segL + 1) * lazy[idx];
            if (segL != segR) {
                lazy[2 * idx] += lazy[idx];
                lazy[2 * idx + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
        if (l <= segL && segR <= r) {
            return segTree[idx];
        }
        int mid = (segL + segR) / 2;
        int leftRes = querySegTree(2 * idx, segL, mid, l, r);
        int rightRes = querySegTree(2 * idx + 1, mid + 1, segR, l, r);
        return leftRes + rightRes;
    }
    
    /**
     * 线段树区间更新
     */
    private void updateSegTree(int idx, int segL, int segR, int l, int r, int val) {
        if (lazy[idx] != 0) {
            segTree[idx] += (segR - segL + 1) * lazy[idx];
            if (segL != segR) {
                lazy[2 * idx] += lazy[idx];
                lazy[2 * idx + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            segTree[idx] += (segR - segL + 1) * val;
            if (segL != segR) {
                lazy[2 * idx] += val;
                lazy[2 * idx + 1] += val;
            }
            return;
        }
        int mid = (segL + segR) / 2;
        updateSegTree(2 * idx, segL, mid, l, r, val);
        updateSegTree(2 * idx + 1, mid + 1, segR, l, r, val);
        segTree[idx] = segTree[2 * idx] + segTree[2 * idx + 1];
    }
    
    /**
     * 测试用例：验证树链剖分功能
     */
    public static void main(String[] args) {
        // 创建一个有5个节点的树
        int n = 5;
        HeavyLightDecomposition hld = new HeavyLightDecomposition(n);
        
        // 构建树结构
        hld.addEdge(1, 2);
        hld.addEdge(1, 3);
        hld.addEdge(2, 4);
        hld.addEdge(2, 5);
        
        // 初始化节点值
        for (int i = 1; i <= n; i++) {
            hld.arr[hld.pos[i]] = i; // 简单地将节点值设为节点编号
        }
        
        // 构建树链剖分
        hld.build(1);
        
        // 测试路径查询
        System.out.println("路径1-4的和: " + hld.queryPath(1, 4)); // 应该输出1+2+4=7
        System.out.println("路径3-5的和: " + hld.queryPath(3, 5)); // 应该输出3+1+2+5=11
        
        // 测试路径更新
        hld.updatePath(1, 4, 10);
        System.out.println("更新后路径1-4的和: " + hld.queryPath(1, 4)); // 应该输出7+30=37
        
        // 测试子树查询
        System.out.println("以2为根的子树和: " + hld.querySubtree(2)); // 应该输出2+4+5=11
        
        System.out.println("树链剖分测试完成！");
    }
}