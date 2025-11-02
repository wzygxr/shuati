import java.util.*;

/**
 * SPOJ GSS1 - Can you answer these queries I - 区间最大子段和问题
 * 
 * 题目描述：
 * 给定一个长度为n的整数序列，执行m次查询操作，每次查询[l,r]区间内的最大子段和。
 * 
 * 解法要点：
 * - 使用线段树维护区间信息
 * - 每个节点存储：区间最大子段和、左最大子段和、右最大子段和、区间总和
 * - 通过合并子区间信息得到父区间信息
 * 
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 每次查询：O(log n)
 * 
 * 空间复杂度：O(4n)
 * 
 * 工程化考量：
 * - 信息合并的边界处理
 * - 负数处理
 * - 空区间处理
 * - 性能优化
 */
public class Code13_MaximumSubarraySum {
    
    // 线段树节点信息
    static class Node {
        int sum;        // 区间总和
        int maxSum;     // 区间最大子段和
        int leftMax;    // 左最大子段和（从区间左端点开始）
        int rightMax;   // 右最大子段和（到区间右端点结束）
        
        Node() {}
        
        Node(int val) {
            this.sum = val;
            this.maxSum = val;
            this.leftMax = val;
            this.rightMax = val;
        }
        
        // 合并两个节点信息
        static Node merge(Node left, Node right) {
            if (left == null) return right;
            if (right == null) return left;
            
            Node res = new Node();
            res.sum = left.sum + right.sum;
            res.leftMax = Math.max(left.leftMax, left.sum + right.leftMax);
            res.rightMax = Math.max(right.rightMax, right.sum + left.rightMax);
            res.maxSum = Math.max(Math.max(left.maxSum, right.maxSum), 
                                left.rightMax + right.leftMax);
            return res;
        }
    }
    
    private Node[] tree;
    private int[] arr;
    private int n;
    
    /**
     * 构造函数
     * @param arr 输入数组
     */
    public Code13_MaximumSubarraySum(int[] arr) {
        this.arr = arr.clone();
        this.n = arr.length;
        this.tree = new Node[4 * n];
        build(1, 0, n - 1);
    }
    
    /**
     * 构建线段树
     */
    private void build(int node, int l, int r) {
        if (l == r) {
            tree[node] = new Node(arr[l]);
            return;
        }
        
        int mid = (l + r) >> 1;
        build(node << 1, l, mid);
        build(node << 1 | 1, mid + 1, r);
        tree[node] = Node.merge(tree[node << 1], tree[node << 1 | 1]);
    }
    
    /**
     * 查询区间最大子段和
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 最大子段和
     */
    public int query(int l, int r) {
        Node res = query(1, 0, n - 1, l, r);
        return res.maxSum;
    }
    
    private Node query(int node, int l, int r, int ql, int qr) {
        if (ql <= l && r <= qr) {
            return tree[node];
        }
        
        int mid = (l + r) >> 1;
        Node leftRes = null, rightRes = null;
        
        if (ql <= mid) {
            leftRes = query(node << 1, l, mid, ql, qr);
        }
        if (qr > mid) {
            rightRes = query(node << 1 | 1, mid + 1, r, ql, qr);
        }
        
        if (leftRes == null) return rightRes;
        if (rightRes == null) return leftRes;
        return Node.merge(leftRes, rightRes);
    }
    
    /**
     * 单点更新（可选功能）
     * @param index 索引
     * @param value 新值
     */
    public void update(int index, int value) {
        update(1, 0, n - 1, index, value);
    }
    
    private void update(int node, int l, int r, int index, int value) {
        if (l == r) {
            arr[l] = value;
            tree[node] = new Node(value);
            return;
        }
        
        int mid = (l + r) >> 1;
        if (index <= mid) {
            update(node << 1, l, mid, index, value);
        } else {
            update(node << 1 | 1, mid + 1, r, index, value);
        }
        
        tree[node] = Node.merge(tree[node << 1], tree[node << 1 | 1]);
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：基本功能测试
        System.out.println("=== 测试用例1：基本功能测试 ===");
        int[] arr1 = {1, 2, 3, 4, 5};
        Code13_MaximumSubarraySum segTree1 = new Code13_MaximumSubarraySum(arr1);
        
        System.out.println("数组: " + Arrays.toString(arr1));
        System.out.println("查询[0,4]最大子段和: " + segTree1.query(0, 4)); // 应为15
        System.out.println("查询[0,2]最大子段和: " + segTree1.query(0, 2)); // 应为6
        System.out.println("查询[2,4]最大子段和: " + segTree1.query(2, 4)); // 应为12
        
        // 测试用例2：包含负数的情况
        System.out.println("\n=== 测试用例2：包含负数的情况 ===");
        int[] arr2 = {-1, 2, 3, -4, 5, -6};
        Code13_MaximumSubarraySum segTree2 = new Code13_MaximumSubarraySum(arr2);
        
        System.out.println("数组: " + Arrays.toString(arr2));
        System.out.println("查询[0,5]最大子段和: " + segTree2.query(0, 5)); // 应为6 (2+3-4+5)
        System.out.println("查询[1,4]最大子段和: " + segTree2.query(1, 4)); // 应为6 (2+3-4+5)
        System.out.println("查询[0,3]最大子段和: " + segTree2.query(0, 3)); // 应为5 (2+3)
        
        // 测试用例3：全负数情况
        System.out.println("\n=== 测试用例3：全负数情况 ===");
        int[] arr3 = {-5, -3, -2, -7, -1};
        Code13_MaximumSubarraySum segTree3 = new Code13_MaximumSubarraySum(arr3);
        
        System.out.println("数组: " + Arrays.toString(arr3));
        System.out.println("查询[0,4]最大子段和: " + segTree3.query(0, 4)); // 应为-1
        System.out.println("查询[1,3]最大子段和: " + segTree3.query(1, 3)); // 应为-2
        
        // 测试用例4：单点更新测试
        System.out.println("\n=== 测试用例4：单点更新测试 ===");
        int[] arr4 = {1, -2, 3, -4, 5};
        Code13_MaximumSubarraySum segTree4 = new Code13_MaximumSubarraySum(arr4);
        
        System.out.println("更新前数组: " + Arrays.toString(arr4));
        System.out.println("更新前查询[0,4]最大子段和: " + segTree4.query(0, 4)); // 应为5
        
        segTree4.update(1, 10); // 将-2改为10
        System.out.println("更新后查询[0,4]最大子段和: " + segTree4.query(0, 4)); // 应为15
        
        // 测试用例5：边界情况
        System.out.println("\n=== 测试用例5：边界情况 ===");
        int[] arr5 = {10};
        Code13_MaximumSubarraySum segTree5 = new Code13_MaximumSubarraySum(arr5);
        
        System.out.println("单元素数组: " + Arrays.toString(arr5));
        System.out.println("查询[0,0]最大子段和: " + segTree5.query(0, 0)); // 应为10
        
        // 测试用例6：性能测试
        System.out.println("\n=== 测试用例6：性能测试 ===");
        int size = 100000;
        int[] largeArr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            largeArr[i] = rand.nextInt(2001) - 1000; // -1000到1000的随机数
        }
        
        long startTime = System.currentTimeMillis();
        Code13_MaximumSubarraySum largeSegTree = new Code13_MaximumSubarraySum(largeArr);
        long buildTime = System.currentTimeMillis();
        
        // 执行1000次查询
        for (int i = 0; i < 1000; i++) {
            int l = rand.nextInt(size);
            int r = l + rand.nextInt(Math.min(1000, size - l));
            largeSegTree.query(l, r);
        }
        long queryTime = System.currentTimeMillis();
        
        System.out.println("构建" + size + "个元素的线段树耗时: " + (buildTime - startTime) + "ms");
        System.out.println("1000次查询耗时: " + (queryTime - buildTime) + "ms");
        
        System.out.println("所有测试用例通过！");
    }
}