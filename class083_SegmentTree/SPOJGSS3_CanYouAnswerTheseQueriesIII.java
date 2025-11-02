import java.util.*;

/**
 * SPOJ GSS3 - Can you answer these queries III
 * 题目：支持单点修改和查询区间最大子段和
 * 来源：SPOJ
 * 网址：https://www.spoj.com/problems/GSS3/
 * 
 * 线段树维护四个信息：
 * 1. 区间和 sum
 * 2. 区间最大前缀和 lmax
 * 3. 区间最大后缀和 rmax
 * 4. 区间最大子段和 max
 * 
 * 时间复杂度：
 *   - 建树：O(n)
 *   - 单点修改：O(log n)
 *   - 区间查询：O(log n)
 * 空间复杂度：O(n)
 */

class Node {
    int sum;     // 区间和
    int lmax;    // 最大前缀和
    int rmax;    // 最大后缀和
    int max;     // 最大子段和
    
    Node(int sum, int lmax, int rmax, int max) {
        this.sum = sum;
        this.lmax = lmax;
        this.rmax = rmax;
        this.max = max;
    }
}

public class SPOJGSS3_CanYouAnswerTheseQueriesIII {
    private Node[] tree;
    private int n;
    
    public SPOJGSS3_CanYouAnswerTheseQueriesIII(int[] nums) {
        n = nums.length;
        tree = new Node[4 * n];
        build(0, 0, n - 1, nums);
    }
    
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        int sum = left.sum + right.sum;
        int lmax = Math.max(left.lmax, left.sum + right.lmax);
        int rmax = Math.max(right.rmax, right.sum + left.rmax);
        int max = Math.max(Math.max(left.max, right.max), left.rmax + right.lmax);
        
        return new Node(sum, lmax, rmax, max);
    }
    
    private void build(int idx, int l, int r, int[] nums) {
        if (l == r) {
            int val = nums[l];
            tree[idx] = new Node(val, val, val, val);
            return;
        }
        
        int mid = (l + r) / 2;
        build(2 * idx + 1, l, mid, nums);
        build(2 * idx + 2, mid + 1, r, nums);
        tree[idx] = merge(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    public void update(int pos, int val) {
        update(0, 0, n - 1, pos, val);
    }
    
    private void update(int idx, int l, int r, int pos, int val) {
        if (l == r) {
            tree[idx] = new Node(val, val, val, val);
            return;
        }
        
        int mid = (l + r) / 2;
        if (pos <= mid) {
            update(2 * idx + 1, l, mid, pos, val);
        } else {
            update(2 * idx + 2, mid + 1, r, pos, val);
        }
        tree[idx] = merge(tree[2 * idx + 1], tree[2 * idx + 2]);
    }
    
    public Node query(int ql, int qr) {
        return query(0, 0, n - 1, ql, qr);
    }
    
    private Node query(int idx, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[idx];
        }
        
        int mid = (l + r) / 2;
        Node left = null, right = null;
        
        if (ql <= mid) {
            left = query(2 * idx + 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            right = query(2 * idx + 2, mid + 1, r, ql, qr);
        }
        
        if (left == null) return right;
        if (right == null) return left;
        return merge(left, right);
    }
    
    public static void main(String[] args) {
        // 测试样例
        int[] nums = {-1, 2, 3, -4, 5, -6};
        SPOJGSS3_CanYouAnswerTheseQueriesIII st = new SPOJGSS3_CanYouAnswerTheseQueriesIII(nums);
        
        // 查询区间最大子段和
        Node result = st.query(0, 5);
        System.out.println("区间[0,5]最大子段和: " + result.max); // 2+3-4+5=6
        
        // 单点修改
        st.update(0, 10);
        result = st.query(0, 5);
        System.out.println("修改后区间[0,5]最大子段和: " + result.max); // 10+2+3-4+5=16
        
        // 查询子区间
        result = st.query(1, 4);
        System.out.println("区间[1,4]最大子段和: " + result.max); // 2+3-4+5=6
    }
}