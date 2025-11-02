/**
 * Codeforces 52C Circular RMQ
 * 题目链接: https://codeforces.com/problemset/problem/52/C
 * 
 * 题目描述:
 * 给定一个环形数组，支持两种操作：
 * 1. 区间加法：将区间[l, r]内的所有元素加上一个值
 * 2. 区间最小值查询：查询区间[l, r]内的最小值
 * 
 * 解题思路:
 * 使用线段树+懒惰标记来处理环形数组的区间操作。
 * 对于环形区间，需要特殊处理跨越数组边界的情况。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(n)
 */

public class Codeforces52C_CircularRMQ {
    private int n;
    private long[] tree;
    private long[] lazy;
    
    public Codeforces52C_CircularRMQ(long[] arr) {
        this.n = arr.length;
        tree = new long[4 * n];
        lazy = new long[4 * n];
        build(arr, 1, 0, n - 1);
    }
    
    private void build(long[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2 * node, start, mid);
            build(arr, 2 * node + 1, mid + 1, end);
            tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
        }
    }
    
    private void pushDown(int node) {
        if (lazy[node] != 0) {
            lazy[2 * node] += lazy[node];
            lazy[2 * node + 1] += lazy[node];
            tree[2 * node] += lazy[node];
            tree[2 * node + 1] += lazy[node];
            lazy[node] = 0;
        }
    }
    
    public void update(int l, int r, long val) {
        update(1, 0, n - 1, l, r, val);
    }
    
    private void update(int node, int start, int end, int l, int r, long val) {
        if (l > r) return;
        if (l <= start && end <= r) {
            tree[node] += val;
            lazy[node] += val;
            return;
        }
        pushDown(node);
        int mid = (start + end) / 2;
        if (l <= mid) update(2 * node, start, mid, l, Math.min(r, mid), val);
        if (r > mid) update(2 * node + 1, mid + 1, end, Math.max(l, mid + 1), r, val);
        tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
    }
    
    public long query(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }
    
    private long query(int node, int start, int end, int l, int r) {
        if (l > r) return Long.MAX_VALUE;
        if (l <= start && end <= r) {
            return tree[node];
        }
        pushDown(node);
        int mid = (start + end) / 2;
        long leftMin = Long.MAX_VALUE, rightMin = Long.MAX_VALUE;
        if (l <= mid) leftMin = query(2 * node, start, mid, l, Math.min(r, mid));
        if (r > mid) rightMin = query(2 * node + 1, mid + 1, end, Math.max(l, mid + 1), r);
        return Math.min(leftMin, rightMin);
    }
    
    // 处理环形区间查询
    public long circularQuery(int l, int r) {
        if (l <= r) {
            return query(l, r);
        } else {
            return Math.min(query(l, n - 1), query(0, r));
        }
    }
    
    // 处理环形区间更新
    public void circularUpdate(int l, int r, long val) {
        if (l <= r) {
            update(l, r, val);
        } else {
            update(l, n - 1, val);
            update(0, r, val);
        }
    }
    
    public static void main(String[] args) {
        // 测试用例
        long[] arr = {1, 2, 3, 4, 5};
        Codeforces52C_CircularRMQ segTree = new Codeforces52C_CircularRMQ(arr);
        
        // 测试正常区间
        System.out.println("正常区间查询 [1,3]: " + segTree.query(1, 3)); // 期望: 2
        segTree.update(1, 3, 2);
        System.out.println("更新后查询 [1,3]: " + segTree.query(1, 3)); // 期望: 4
        
        // 测试环形区间
        System.out.println("环形区间查询 [3,1]: " + segTree.circularQuery(3, 1)); // 期望: 1
        segTree.circularUpdate(3, 1, 1);
        System.out.println("环形更新后查询 [3,1]: " + segTree.circularQuery(3, 1)); // 期望: 2
    }
}