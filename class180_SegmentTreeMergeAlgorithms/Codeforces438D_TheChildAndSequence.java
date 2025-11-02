/**
 * Codeforces 438D The Child and Sequence
 * 题目链接: https://codeforces.com/problemset/problem/438/D
 * 
 * 题目描述:
 * 给定一个数组，支持三种操作：
 * 1. 区间求和：查询区间[l, r]的和
 * 2. 区间取模：将区间[l, r]内的每个元素对x取模
 * 3. 单点赋值：将位置k的值修改为x
 * 
 * 解题思路:
 * 使用吉司机线段树（Segment Tree Beats）来维护区间取模操作。
 * 关键观察：一个数对x取模后，值至少减半，因此每个数最多被取模log(max_value)次。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间求和: O(log n)
 * - 区间取模: O(log n * log max_value)
 * - 单点赋值: O(log n)
 * 
 * 空间复杂度: O(n)
 */

public class Codeforces438D_TheChildAndSequence {
    private int n;
    private long[] sum;
    private long[] max;
    
    public Codeforces438D_TheChildAndSequence(long[] arr) {
        this.n = arr.length;
        sum = new long[4 * n];
        max = new long[4 * n];
        build(arr, 1, 0, n - 1);
    }
    
    private void build(long[] arr, int node, int start, int end) {
        if (start == end) {
            sum[node] = arr[start];
            max[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2 * node, start, mid);
            build(arr, 2 * node + 1, mid + 1, end);
            sum[node] = sum[2 * node] + sum[2 * node + 1];
            max[node] = Math.max(max[2 * node], max[2 * node + 1]);
        }
    }
    
    public void update(int index, long value) {
        update(1, 0, n - 1, index, value);
    }
    
    private void update(int node, int start, int end, int index, long value) {
        if (start == end) {
            sum[node] = value;
            max[node] = value;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                update(2 * node, start, mid, index, value);
            } else {
                update(2 * node + 1, mid + 1, end, index, value);
            }
            sum[node] = sum[2 * node] + sum[2 * node + 1];
            max[node] = Math.max(max[2 * node], max[2 * node + 1]);
        }
    }
    
    public void modulo(int l, int r, long mod) {
        modulo(1, 0, n - 1, l, r, mod);
    }
    
    private void modulo(int node, int start, int end, int l, int r, long mod) {
        if (l > r || max[node] < mod) {
            return;
        }
        
        if (start == end) {
            sum[node] %= mod;
            max[node] = sum[node];
            return;
        }
        
        int mid = (start + end) / 2;
        if (l <= mid) {
            modulo(2 * node, start, mid, l, Math.min(r, mid), mod);
        }
        if (r > mid) {
            modulo(2 * node + 1, mid + 1, end, Math.max(l, mid + 1), r, mod);
        }
        
        sum[node] = sum[2 * node] + sum[2 * node + 1];
        max[node] = Math.max(max[2 * node], max[2 * node + 1]);
    }
    
    public long query(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }
    
    private long query(int node, int start, int end, int l, int r) {
        if (l > r) return 0;
        if (l <= start && end <= r) {
            return sum[node];
        }
        
        int mid = (start + end) / 2;
        long result = 0;
        if (l <= mid) {
            result += query(2 * node, start, mid, l, Math.min(r, mid));
        }
        if (r > mid) {
            result += query(2 * node + 1, mid + 1, end, Math.max(l, mid + 1), r);
        }
        return result;
    }
    
    public static void main(String[] args) {
        // 测试用例
        long[] arr = {10, 15, 20, 25, 30};
        Codeforces438D_TheChildAndSequence segTree = new Codeforces438D_TheChildAndSequence(arr);
        
        // 测试区间求和
        System.out.println("区间求和 [0,4]: " + segTree.query(0, 4)); // 期望: 100
        
        // 测试区间取模
        segTree.modulo(0, 4, 12);
        System.out.println("取模后区间求和 [0,4]: " + segTree.query(0, 4)); // 期望: 10%12 + 15%12 + 20%12 + 25%12 + 30%12
        
        // 测试单点赋值
        segTree.update(2, 50);
        System.out.println("单点赋值后区间求和 [0,4]: " + segTree.query(0, 4));
        
        // 测试边界情况
        System.out.println("空区间求和 [5,5]: " + segTree.query(5, 5)); // 期望: 0
    }
}