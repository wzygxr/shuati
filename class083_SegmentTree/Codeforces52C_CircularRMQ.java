import java.util.*;

/**
 * Codeforces 52C - Circular RMQ
 * 题目：环形数组的区间最小值查询和区间加法
 * 来源：Codeforces
 * 网址：https://codeforces.com/problemset/problem/52/C
 * 
 * 支持环形数组的区间最小值查询和区间加法
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 区间修改：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

public class Codeforces52C_CircularRMQ {
    private long[] tree;     // 线段树数组
    private long[] lazy;     // 懒标记数组
    private int n;           // 数组长度
    
    public Codeforces52C_CircularRMQ(int[] nums) {
        n = nums.length;
        tree = new long[4 * n];
        lazy = new long[4 * n];
        build(0, 0, n - 1, nums);
    }
    
    private void build(int idx, int l, int r, int[] nums) {
        if (l == r) {
            tree[idx] = nums[l];
            return;
        }
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid, nums);
        build(2 * idx + 2, mid + 1, r, nums);
        tree[idx] = Math.min(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    private void pushDown(int idx) {
        if (lazy[idx] != 0) {
            tree[2 * idx + 1] += lazy[idx];
            tree[2 * idx + 2] += lazy[idx];
            lazy[2 * idx + 1] += lazy[idx];
            lazy[2 * idx + 2] += lazy[idx];
            lazy[idx] = 0;
        }
    }
    
    private void updateRange(int idx, int l, int r, int ql, int qr, long val) {
        if (ql <= l && r <= qr) {
            tree[idx] += val;
            lazy[idx] += val;
            return;
        }
        pushDown(idx);
        int mid = (l + r) / 2;
        if (ql <= mid) {
            updateRange(2 * idx + 1, l, mid, ql, qr, val);
        }
        if (qr > mid) {
            updateRange(2 * idx + 2, mid + 1, r, ql, qr, val);
        }
        tree[idx] = Math.min(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    private long queryRange(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        pushDown(idx);
        int mid = (l + r) / 2;
        long minVal = Long.MAX_VALUE;
        if (ql <= mid) {
            minVal = Math.min(minVal, queryRange(2 * idx + 1, l, mid, ql, qr));
        }
        if (qr > mid) {
            minVal = Math.min(minVal, queryRange(2 * idx + 2, mid + 1, r, ql, qr));
        }
        return minVal;
    }
    
    /**
     * 处理环形区间更新
     * @param l 起始位置
     * @param r 结束位置
     * @param val 要增加的值
     */
    public void circularUpdate(int l, int r, long val) {
        if (l <= r) {
            // 正常区间
            updateRange(0, 0, n - 1, l, r, val);
        } else {
            // 环形区间：从l到末尾，从开头到r
            updateRange(0, 0, n - 1, l, n - 1, val);
            updateRange(0, 0, n - 1, 0, r, val);
        }
    }
    
    /**
     * 处理环形区间查询
     * @param l 起始位置
     * @param r 结束位置
     * @return 区间最小值
     */
    public long circularQuery(int l, int r) {
        if (l <= r) {
            // 正常区间
            return queryRange(0, 0, n - 1, l, r);
        } else {
            // 环形区间：从l到末尾，从开头到r
            long min1 = queryRange(0, 0, n - 1, l, n - 1);
            long min2 = queryRange(0, 0, n - 1, 0, r);
            return Math.min(min1, min2);
        }
    }
    
    public static void main(String[] args) {
        // 测试样例
        int[] nums = {1, 2, 3, 4, 5};
        Codeforces52C_CircularRMQ st = new Codeforces52C_CircularRMQ(nums);
        
        // 正常区间查询
        System.out.println("正常区间[0,2]最小值: " + st.circularQuery(0, 2)); // 1
        
        // 环形区间查询：从4到1 (4->末尾->开头->1)
        System.out.println("环形区间[4,1]最小值: " + st.circularQuery(4, 1)); // 1
        
        // 环形区间更新：从4到1加2
        st.circularUpdate(4, 1, 2);
        System.out.println("更新后环形区间[4,1]最小值: " + st.circularQuery(4, 1)); // 3
        
        // 验证更新结果
        System.out.println("位置0的值: " + st.circularQuery(0, 0)); // 3
        System.out.println("位置4的值: " + st.circularQuery(4, 4)); // 7
    }
}