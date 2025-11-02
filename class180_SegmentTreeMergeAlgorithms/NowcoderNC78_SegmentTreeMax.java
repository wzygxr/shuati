import java.util.*;

/**
 * 牛客网 NC78 线段树区间最大值
 * 题目链接: https://www.nowcoder.com/practice/1a834e5e3e1a4b7ba251417554e07c00
 * 
 * 题目描述:
 * 给定一个整数数组，实现线段树数据结构，支持以下操作：
 * 1. 构建线段树
 * 2. 查询区间最大值
 * 3. 单点更新
 * 
 * 算法思路:
 * 线段树是一种二叉树结构，用于高效处理区间查询和更新操作。
 * 每个节点存储一个区间的聚合信息（最大值）。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 查询: O(log n)
 * - 更新: O(log n)
 * 
 * 空间复杂度分析:
 * - 线段树存储: O(4n) 或 O(2n)（优化后）
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、索引越界检查
 * 2. 性能优化：位运算、缓存友好设计
 * 3. 可测试性：边界测试、性能测试
 * 4. 可维护性：清晰的结构、详细注释
 * 
 * 面试要点:
 * 1. 线段树的基本原理和结构
 * 2. 建树、查询、更新的实现细节
 * 3. 时间空间复杂度分析
 * 4. 工程化改进建议
 */
public class NowcoderNC78_SegmentTreeMax {
    
    private int[] tree;  // 线段树数组
    private int n;       // 原始数组长度
    
    /**
     * 构造函数：构建线段树
     * @param nums 原始数组
     */
    public NowcoderNC78_SegmentTreeMax(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        this.n = nums.length;
        this.tree = new int[4 * n];  // 分配4倍空间
        buildTree(nums, 0, n - 1, 0);
    }
    
    /**
     * 递归构建线段树
     * @param nums 原始数组
     * @param left 当前区间左边界
     * @param right 当前区间右边界
     * @param idx 当前节点在线段树中的索引
     */
    private void buildTree(int[] nums, int left, int right, int idx) {
        // 叶子节点，存储单个元素
        if (left == right) {
            tree[idx] = nums[left];
            return;
        }
        
        // 计算中间点，分割区间
        int mid = left + (right - left) / 2;
        int leftChild = 2 * idx + 1;  // 左子节点索引
        int rightChild = 2 * idx + 2; // 右子节点索引
        
        // 递归构建左右子树
        buildTree(nums, left, mid, leftChild);
        buildTree(nums, mid + 1, right, rightChild);
        
        // 合并左右子树信息（取最大值）
        tree[idx] = Math.max(tree[leftChild], tree[rightChild]);
    }
    
    /**
     * 查询区间最大值
     * @param queryLeft 查询区间左边界
     * @param queryRight 查询区间右边界
     * @return 区间最大值
     */
    public int query(int queryLeft, int queryRight) {
        // 参数校验
        if (queryLeft < 0 || queryRight >= n || queryLeft > queryRight) {
            throw new IllegalArgumentException("查询区间不合法");
        }
        
        return queryHelper(0, n - 1, queryLeft, queryRight, 0);
    }
    
    /**
     * 递归查询辅助函数
     */
    private int queryHelper(int segLeft, int segRight, int queryLeft, int queryRight, int idx) {
        // 当前区间完全包含在查询区间内
        if (queryLeft <= segLeft && segRight <= queryRight) {
            return tree[idx];
        }
        
        int mid = segLeft + (segRight - segLeft) / 2;
        int leftChild = 2 * idx + 1;
        int rightChild = 2 * idx + 2;
        
        int maxVal = Integer.MIN_VALUE;
        
        // 查询左子树
        if (queryLeft <= mid) {
            maxVal = Math.max(maxVal, queryHelper(segLeft, mid, queryLeft, queryRight, leftChild));
        }
        
        // 查询右子树
        if (queryRight > mid) {
            maxVal = Math.max(maxVal, queryHelper(mid + 1, segRight, queryLeft, queryRight, rightChild));
        }
        
        return maxVal;
    }
    
    /**
     * 单点更新
     * @param index 要更新的索引
     * @param value 新的值
     */
    public void update(int index, int value) {
        // 参数校验
        if (index < 0 || index >= n) {
            throw new IllegalArgumentException("索引越界");
        }
        
        updateHelper(0, n - 1, index, value, 0);
    }
    
    /**
     * 递归更新辅助函数
     */
    private void updateHelper(int segLeft, int segRight, int index, int value, int idx) {
        // 找到目标叶子节点
        if (segLeft == segRight) {
            tree[idx] = value;
            return;
        }
        
        int mid = segLeft + (segRight - segLeft) / 2;
        int leftChild = 2 * idx + 1;
        int rightChild = 2 * idx + 2;
        
        // 根据索引位置决定更新哪棵子树
        if (index <= mid) {
            updateHelper(segLeft, mid, index, value, leftChild);
        } else {
            updateHelper(mid + 1, segRight, index, value, rightChild);
        }
        
        // 更新父节点（取左右子树最大值）
        tree[idx] = Math.max(tree[leftChild], tree[rightChild]);
    }
    
    /**
     * 打印线段树结构（用于调试）
     */
    public void printTree() {
        System.out.println("线段树结构:");
        printTreeHelper(0, n - 1, 0, 0);
    }
    
    private void printTreeHelper(int left, int right, int idx, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }
        
        System.out.println(indent + "区间[" + left + ", " + right + "]: " + tree[idx]);
        
        if (left != right) {
            int mid = left + (right - left) / 2;
            printTreeHelper(left, mid, 2 * idx + 1, depth + 1);
            printTreeHelper(mid + 1, right, 2 * idx + 2, depth + 1);
        }
    }
    
    /**
     * 主函数：测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：正常数组
        int[] nums1 = {1, 3, 5, 7, 9, 11};
        NowcoderNC78_SegmentTreeMax st1 = new NowcoderNC78_SegmentTreeMax(nums1);
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("数组: " + Arrays.toString(nums1));
        
        // 测试查询
        System.out.println("查询[0, 2]最大值: " + st1.query(0, 2));  // 期望: 5
        System.out.println("查询[1, 4]最大值: " + st1.query(1, 4));  // 期望: 9
        System.out.println("查询[0, 5]最大值: " + st1.query(0, 5));  // 期望: 11
        
        // 测试更新
        st1.update(2, 10);
        System.out.println("更新索引2为10后，查询[0, 2]最大值: " + st1.query(0, 2));  // 期望: 10
        
        // 测试用例2：边界情况
        int[] nums2 = {5};
        NowcoderNC78_SegmentTreeMax st2 = new NowcoderNC78_SegmentTreeMax(nums2);
        
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("查询[0, 0]最大值: " + st2.query(0, 0));  // 期望: 5
        
        // 测试用例3：负数数组
        int[] nums3 = {-1, -3, -5, -7};
        NowcoderNC78_SegmentTreeMax st3 = new NowcoderNC78_SegmentTreeMax(nums3);
        
        System.out.println("\n=== 测试用例3 ===");
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("查询[0, 3]最大值: " + st3.query(0, 3));  // 期望: -1
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int size = 100000;
        int[] largeNums = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            largeNums[i] = rand.nextInt(1000000);
        }
        
        long startTime = System.currentTimeMillis();
        NowcoderNC78_SegmentTreeMax stLarge = new NowcoderNC78_SegmentTreeMax(largeNums);
        long buildTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int maxVal = stLarge.query(0, size - 1);
        long queryTime = System.currentTimeMillis() - startTime;
        
        System.out.println("构建" + size + "个元素的线段树耗时: " + buildTime + "ms");
        System.out.println("查询整个区间最大值耗时: " + queryTime + "ms");
        System.out.println("最大值: " + maxVal);
        
        // 异常测试
        try {
            st1.query(-1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("\n=== 异常测试 ===");
            System.out.println("捕获到预期异常: " + e.getMessage());
        }
    }
}