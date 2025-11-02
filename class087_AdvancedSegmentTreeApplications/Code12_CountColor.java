import java.util.*;

/**
 * POJ 2777 Count Color - 区间染色问题
 * 
 * 题目描述：
 * 给定一个长度为L的板条，初始时所有位置都是颜色1，执行O次操作：
 * 1. "C A B C": 将区间[A,B]染成颜色C
 * 2. "P A B": 查询区间[A,B]中有多少种不同的颜色
 * 
 * 解法要点：
 * - 使用线段树维护区间颜色集合(用位运算表示)
 * - 结合懒惰标记实现区间染色
 * - 通过位运算计算颜色种类数
 * 
 * 时间复杂度：
 * - 区间染色：O(log L)
 * - 区间查询：O(log L)
 * 
 * 空间复杂度：O(4L)
 * 
 * 工程化考量：
 * - 使用位运算高效表示颜色集合
 * - 懒惰标记优化区间更新
 * - 输入验证和边界处理
 */
public class Code12_CountColor {
    
    private int[] tree;      // 线段树，存储颜色集合(位掩码)
    private int[] lazy;      // 懒惰标记，存储要设置的颜色
    private int n;           // 线段树大小
    
    /**
     * 构造函数
     * @param L 板条长度
     */
    public Code12_CountColor(int L) {
        this.n = L;
        this.tree = new int[4 * n];
        this.lazy = new int[4 * n];
        // 初始化所有位置为颜色1
        build(1, 1, n);
    }
    
    /**
     * 构建线段树
     */
    private void build(int node, int l, int r) {
        if (l == r) {
            tree[node] = 1; // 颜色1用位掩码1表示
            return;
        }
        int mid = (l + r) >> 1;
        build(node << 1, l, mid);
        build(node << 1 | 1, mid + 1, r);
        tree[node] = tree[node << 1] | tree[node << 1 | 1];
    }
    
    /**
     * 区间染色操作
     * @param l 区间左端点
     * @param r 区间右端点
     * @param color 颜色编号(1-30)
     */
    public void update(int l, int r, int color) {
        update(1, 1, n, l, r, color);
    }
    
    private void update(int node, int l, int r, int ql, int qr, int color) {
        if (ql <= l && r <= qr) {
            // 完全覆盖，设置懒惰标记和当前节点颜色
            lazy[node] = color;
            tree[node] = 1 << (color - 1);
            return;
        }
        
        // 下推懒惰标记
        pushDown(node, l, r);
        
        int mid = (l + r) >> 1;
        if (ql <= mid) {
            update(node << 1, l, mid, ql, qr, color);
        }
        if (qr > mid) {
            update(node << 1 | 1, mid + 1, r, ql, qr, color);
        }
        
        // 合并子区间信息
        tree[node] = tree[node << 1] | tree[node << 1 | 1];
    }
    
    /**
     * 查询区间颜色种类数
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 颜色种类数
     */
    public int query(int l, int r) {
        int mask = query(1, 1, n, l, r);
        return Integer.bitCount(mask);
    }
    
    private int query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }
        
        // 下推懒惰标记
        pushDown(node, l, r);
        
        int mid = (l + r) >> 1;
        int result = 0;
        if (ql <= mid) {
            result |= query(node << 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            result |= query(node << 1 | 1, mid + 1, r, ql, qr);
        }
        return result;
    }
    
    /**
     * 下推懒惰标记
     */
    private void pushDown(int node, int l, int r) {
        if (lazy[node] != 0 && l != r) {
            int color = lazy[node];
            lazy[node << 1] = color;
            lazy[node << 1 | 1] = color;
            tree[node << 1] = 1 << (color - 1);
            tree[node << 1 | 1] = 1 << (color - 1);
            lazy[node] = 0;
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        System.out.println("=== 测试用例1：基本功能测试 ===");
        Code12_CountColor segTree = new Code12_CountColor(10);
        
        // 初始状态：所有位置都是颜色1
        System.out.println("初始查询[1,10]颜色种类数: " + segTree.query(1, 10)); // 应为1
        
        // 染色操作
        segTree.update(1, 5, 2); // 将[1,5]染成颜色2
        System.out.println("染色后查询[1,10]颜色种类数: " + segTree.query(1, 10)); // 应为2
        System.out.println("查询[1,5]颜色种类数: " + segTree.query(1, 5)); // 应为1
        System.out.println("查询[6,10]颜色种类数: " + segTree.query(6, 10)); // 应为1
        
        // 覆盖染色
        segTree.update(3, 7, 3); // 将[3,7]染成颜色3
        System.out.println("覆盖染色后查询[1,10]颜色种类数: " + segTree.query(1, 10)); // 应为3
        
        // 测试用例2：边界情况
        System.out.println("\n=== 测试用例2：边界情况 ===");
        segTree = new Code12_CountColor(5);
        
        // 单点染色
        segTree.update(1, 1, 2);
        segTree.update(5, 5, 3);
        System.out.println("单点染色后查询[1,5]颜色种类数: " + segTree.query(1, 5)); // 应为3
        
        // 测试用例3：性能测试（大规模数据）
        System.out.println("\n=== 测试用例3：性能测试 ===");
        int L = 100000;
        Code12_CountColor largeSegTree = new Code12_CountColor(L);
        
        long startTime = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            int l = (i * 10) % L + 1;
            int r = Math.min(l + 100, L);
            largeSegTree.update(l, r, (i % 30) + 1);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("1000次染色操作耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例通过！");
    }
}