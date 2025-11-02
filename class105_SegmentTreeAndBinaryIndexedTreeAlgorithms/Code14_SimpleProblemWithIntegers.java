// POJ 3468 A Simple Problem with Integers
// 题目描述：给定一个长度为N的整数序列，执行以下操作：
// 1. C a b c: 将区间 [a,b] 中的每个数都加上c
// 2. Q a b: 查询区间 [a,b] 中所有数的和
// 题目链接：http://poj.org/problem?id=3468
// 解题思路：使用线段树 + 懒惰标记实现区间加法和区间求和查询

import java.io.*;
import java.util.*;

/**
 * 线段树实现区间加法和区间求和查询
 * 时间复杂度：
 * - 构建线段树：O(n)
 * - 区间更新：O(log n)
 * - 区间查询：O(log n)
 * 空间复杂度：O(n) - 线段树数组大小为4n
 */
public class Code14_SimpleProblemWithIntegers {
    
    /**
     * 线段树节点类
     */
    static class Node {
        long sum;    // 区间和
        long add;    // 懒惰标记，表示区间每个元素需要增加的值
    }
    
    private Node[] tree;  // 线段树数组
    private int[] arr;    // 原始数组
    private int n;        // 数组长度
    
    /**
     * 构造函数
     * @param nums 输入数组
     */
    public Code14_SimpleProblemWithIntegers(int[] nums) {
        this.n = nums.length;
        this.arr = Arrays.copyOf(nums, n);
        // 线段树数组大小为4n，保证足够空间
        this.tree = new Node[4 * n];
        for (int i = 0; i < 4 * n; i++) {
            tree[i] = new Node();
        }
        // 构建线段树
        build(0, 0, n - 1);
    }
    
    /**
     * 构建线段树
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    private void build(int node, int start, int end) {
        if (start == end) {
            // 叶子节点，直接赋值
            tree[node].sum = arr[start];
            tree[node].add = 0;
            return;
        }
        
        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;
        
        // 递归构建左右子树
        build(leftNode, start, mid);
        build(rightNode, mid + 1, end);
        
        // 合并左右子树信息
        tree[node].sum = tree[leftNode].sum + tree[rightNode].sum;
        tree[node].add = 0;  // 非叶子节点初始懒惰标记为0
    }
    
    /**
     * 下传懒惰标记
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    private void pushDown(int node, int start, int end) {
        if (tree[node].add != 0) {
            // 只有当懒惰标记不为0时需要下传
            int leftNode = 2 * node + 1;
            int rightNode = 2 * node + 2;
            int mid = start + (end - start) / 2;
            
            // 更新左子节点的区间和和懒惰标记
            tree[leftNode].sum += tree[node].add * (mid - start + 1);
            tree[leftNode].add += tree[node].add;
            
            // 更新右子节点的区间和和懒惰标记
            tree[rightNode].sum += tree[node].add * (end - mid);
            tree[rightNode].add += tree[node].add;
            
            // 清除当前节点的懒惰标记
            tree[node].add = 0;
        }
    }
    
    /**
     * 区间更新（加法）
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 需要更新的区间左边界
     * @param r 需要更新的区间右边界
     * @param val 要增加的值
     */
    private void updateRange(int node, int start, int end, int l, int r, long val) {
        // 当前区间与目标区间无交集
        if (start > r || end < l) {
            return;
        }
        
        // 当前区间完全包含在目标区间内
        if (start >= l && end <= r) {
            // 更新区间和
            tree[node].sum += val * (end - start + 1);
            // 更新懒惰标记
            tree[node].add += val;
            return;
        }
        
        // 下传懒惰标记到子节点
        pushDown(node, start, end);
        
        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;
        
        // 递归更新左右子树
        updateRange(leftNode, start, mid, l, r, val);
        updateRange(rightNode, mid + 1, end, l, r, val);
        
        // 更新当前节点的区间和
        tree[node].sum = tree[leftNode].sum + tree[rightNode].sum;
    }
    
    /**
     * 区间查询
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 查询的区间左边界
     * @param r 查询的区间右边界
     * @return 查询区间的和
     */
    private long queryRange(int node, int start, int end, int l, int r) {
        // 当前区间与查询区间无交集
        if (start > r || end < l) {
            return 0;
        }
        
        // 当前区间完全包含在查询区间内
        if (start >= l && end <= r) {
            return tree[node].sum;
        }
        
        // 下传懒惰标记到子节点
        pushDown(node, start, end);
        
        int mid = start + (end - start) / 2;
        int leftNode = 2 * node + 1;
        int rightNode = 2 * node + 2;
        
        // 递归查询左右子树
        long leftSum = queryRange(leftNode, start, mid, l, r);
        long rightSum = queryRange(rightNode, mid + 1, end, l, r);
        
        // 返回左右子树查询结果的和
        return leftSum + rightSum;
    }
    
    /**
     * 公共接口：区间更新
     * @param l 区间左边界（注意：这里是1-based索引）
     * @param r 区间右边界（注意：这里是1-based索引）
     * @param val 要增加的值
     */
    public void updateRange(int l, int r, long val) {
        // 转换为0-based索引
        updateRange(0, 0, n - 1, l - 1, r - 1, val);
    }
    
    /**
     * 公共接口：区间查询
     * @param l 区间左边界（注意：这里是1-based索引）
     * @param r 区间右边界（注意：这里是1-based索引）
     * @return 查询区间的和
     */
    public long queryRange(int l, int r) {
        // 转换为0-based索引
        return queryRange(0, 0, n - 1, l - 1, r - 1);
    }
    
    /**
     * 主方法，用于处理输入输出
     */
    public static void main(String[] args) throws IOException {
        // 使用快速IO
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        StringTokenizer st = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(st.nextToken());  // 数组长度
        int q = Integer.parseInt(st.nextToken());  // 查询次数
        
        // 读取数组元素
        st = new StringTokenizer(br.readLine());
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        // 创建线段树
        Code14_SimpleProblemWithIntegers solution = new Code14_SimpleProblemWithIntegers(arr);
        
        // 处理每个查询
        while (q-- > 0) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            
            if (op.equals("Q")) {
                // 查询操作
                long sum = solution.queryRange(a, b);
                out.println(sum);
            } else if (op.equals("C")) {
                // 更新操作
                long c = Long.parseLong(st.nextToken());
                solution.updateRange(a, b, c);
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        // 测试用例1：基本操作测试
        int[] nums1 = {1, 2, 3, 4, 5};
        Code14_SimpleProblemWithIntegers solution1 = new Code14_SimpleProblemWithIntegers(nums1);
        System.out.println("测试用例1:");
        System.out.println("初始数组: [1, 2, 3, 4, 5]");
        System.out.println("查询区间[1, 5]的和: " + solution1.queryRange(1, 5));  // 应为15
        solution1.updateRange(2, 4, 2);
        System.out.println("更新区间[2, 4]每个元素加2后，数组变为: [1, 4, 5, 6, 5]");
        System.out.println("查询区间[1, 5]的和: " + solution1.queryRange(1, 5));  // 应为21
        System.out.println("查询区间[2, 4]的和: " + solution1.queryRange(2, 4));  // 应为15
        
        // 测试用例2：边界情况测试
        int[] nums2 = {10};
        Code14_SimpleProblemWithIntegers solution2 = new Code14_SimpleProblemWithIntegers(nums2);
        System.out.println("\n测试用例2:");
        System.out.println("初始数组: [10]");
        System.out.println("查询区间[1, 1]的和: " + solution2.queryRange(1, 1));  // 应为10
        solution2.updateRange(1, 1, -5);
        System.out.println("更新区间[1, 1]每个元素加-5后，数组变为: [5]");
        System.out.println("查询区间[1, 1]的和: " + solution2.queryRange(1, 1));  // 应为5
        
        // 测试用例3：多次更新和查询测试
        int[] nums3 = {0, 0, 0, 0, 0};
        Code14_SimpleProblemWithIntegers solution3 = new Code14_SimpleProblemWithIntegers(nums3);
        System.out.println("\n测试用例3:");
        System.out.println("初始数组: [0, 0, 0, 0, 0]");
        solution3.updateRange(1, 5, 1);  // 所有元素加1
        solution3.updateRange(2, 4, 2);  // 中间三个元素再加2
        solution3.updateRange(3, 3, 3);  // 中间元素再加3
        System.out.println("多次更新后，数组变为: [1, 3, 6, 3, 1]");
        System.out.println("查询区间[1, 5]的和: " + solution3.queryRange(1, 5));  // 应为14
        System.out.println("查询区间[1, 3]的和: " + solution3.queryRange(1, 3));  // 应为10
        System.out.println("查询区间[3, 5]的和: " + solution3.queryRange(3, 5));  // 应为10
    }
}