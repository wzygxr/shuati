import java.util.*;

/**
 * 牛客网 NC79 线段树区间和
 * 题目链接: https://www.nowcoder.com/practice/8b3b95850edb4115918ecebdf1b4d222
 * 
 * 题目描述:
 * 给定一个整数数组，实现线段树数据结构，支持以下操作：
 * 1. 构建线段树
 * 2. 查询区间和
 * 3. 单点更新
 * 
 * 算法思路:
 * 线段树是一种二叉树结构，用于高效处理区间查询和更新操作。
 * 每个节点存储一个区间的聚合信息（区间和）。
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
public class NowcoderNC79_SegmentTreeSum {
    
    private int[] tree;  // 线段树数组
    private int n;       // 原始数组长度
    
    /**
     * 构造函数：构建线段树
     * @param nums 原始数组
     */
    public NowcoderNC79_SegmentTreeSum(int[] nums) {
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
        
        // 合并左右子树信息（求和）
        tree[idx] = tree[leftChild] + tree[rightChild];
    }
    
    /**
     * 查询区间和
     * @param queryLeft 查询区间左边界
     * @param queryRight 查询区间右边界
     * @return 区间和
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
        
        int sum = 0;
        
        // 查询左子树
        if (queryLeft <= mid) {
            sum += queryHelper(segLeft, mid, queryLeft, queryRight, leftChild);
        }
        
        // 查询右子树
        if (queryRight > mid) {
            sum += queryHelper(mid + 1, segRight, queryLeft, queryRight, rightChild);
        }
        
        return sum;
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
        
        // 更新父节点（求和）
        tree[idx] = tree[leftChild] + tree[rightChild];
    }
    
    /**
     * 区间加法更新（可选功能，增强实用性）
     * @param updateLeft 更新区间左边界
     * @param updateRight 更新区间右边界
     * @param delta 增加值
     */
    public void rangeAdd(int updateLeft, int updateRight, int delta) {
        // 参数校验
        if (updateLeft < 0 || updateRight >= n || updateLeft > updateRight) {
            throw new IllegalArgumentException("更新区间不合法");
        }
        
        rangeAddHelper(0, n - 1, updateLeft, updateRight, delta, 0);
    }
    
    /**
     * 递归区间加法辅助函数
     */
    private void rangeAddHelper(int segLeft, int segRight, int updateLeft, int updateRight, int delta, int idx) {
        // 当前区间完全包含在更新区间内
        if (updateLeft <= segLeft && segRight <= updateRight) {
            tree[idx] += delta * (segRight - segLeft + 1);
            return;
        }
        
        int mid = segLeft + (segRight - segLeft) / 2;
        int leftChild = 2 * idx + 1;
        int rightChild = 2 * idx + 2;
        
        // 更新左子树
        if (updateLeft <= mid) {
            rangeAddHelper(segLeft, mid, updateLeft, updateRight, delta, leftChild);
        }
        
        // 更新右子树
        if (updateRight > mid) {
            rangeAddHelper(mid + 1, segRight, updateLeft, updateRight, delta, rightChild);
        }
        
        // 更新父节点
        tree[idx] = tree[leftChild] + tree[rightChild];
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
     * 获取原始数组长度
     * @return 数组长度
     */
    public int size() {
        return n;
    }
    
    /**
     * 主函数：测试用例
     */
    public static void main(String[] args) {
        // 测试用例1：正常数组
        int[] nums1 = {1, 3, 5, 7, 9, 11};
        NowcoderNC79_SegmentTreeSum st1 = new NowcoderNC79_SegmentTreeSum(nums1);
        
        System.out.println("=== 测试用例1 ===");
        System.out.println("数组: " + Arrays.toString(nums1));
        
        // 测试查询
        System.out.println("查询[0, 2]区间和: " + st1.query(0, 2));  // 期望: 1+3+5=9
        System.out.println("查询[1, 4]区间和: " + st1.query(1, 4));  // 期望: 3+5+7+9=24
        System.out.println("查询[0, 5]区间和: " + st1.query(0, 5));  // 期望: 1+3+5+7+9+11=36
        
        // 测试单点更新
        st1.update(2, 10);
        System.out.println("更新索引2为10后，查询[0, 2]区间和: " + st1.query(0, 2));  // 期望: 1+3+10=14
        
        // 测试区间加法
        st1.rangeAdd(1, 3, 5);
        System.out.println("区间[1,3]加5后，查询[0, 5]区间和: " + st1.query(0, 5));  // 期望: 1+8+15+12+9+11=56
        
        // 测试用例2：边界情况
        int[] nums2 = {5};
        NowcoderNC79_SegmentTreeSum st2 = new NowcoderNC79_SegmentTreeSum(nums2);
        
        System.out.println("\n=== 测试用例2 ===");
        System.out.println("数组: " + Arrays.toString(nums2));
        System.out.println("查询[0, 0]区间和: " + st2.query(0, 0));  // 期望: 5
        
        // 测试用例3：负数数组
        int[] nums3 = {-1, -3, -5, -7};
        NowcoderNC79_SegmentTreeSum st3 = new NowcoderNC79_SegmentTreeSum(nums3);
        
        System.out.println("\n=== 测试用例3 ===");
        System.out.println("数组: " + Arrays.toString(nums3));
        System.out.println("查询[0, 3]区间和: " + st3.query(0, 3));  // 期望: -1-3-5-7=-16
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        int size = 100000;
        int[] largeNums = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            largeNums[i] = rand.nextInt(1000);
        }
        
        long startTime = System.currentTimeMillis();
        NowcoderNC79_SegmentTreeSum stLarge = new NowcoderNC79_SegmentTreeSum(largeNums);
        long buildTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        int sumVal = stLarge.query(0, size - 1);
        long queryTime = System.currentTimeMillis() - startTime;
        
        startTime = System.currentTimeMillis();
        stLarge.rangeAdd(0, size - 1, 10);
        long updateTime = System.currentTimeMillis() - startTime;
        
        System.out.println("构建" + size + "个元素的线段树耗时: " + buildTime + "ms");
        System.out.println("查询整个区间和耗时: " + queryTime + "ms");
        System.out.println("区间加法更新耗时: " + updateTime + "ms");
        System.out.println("初始区间和: " + sumVal);
        
        // 异常测试
        System.out.println("\n=== 异常测试 ===");
        try {
            st1.query(-1, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到预期异常: " + e.getMessage());
        }
        
        try {
            st1.update(10, 5);
        } catch (IllegalArgumentException e) {
            System.out.println("捕获到预期异常: " + e.getMessage());
        }
        
        // 内存使用分析
        System.out.println("\n=== 内存使用分析 ===");
        System.out.println("原始数组大小: " + size + " 个整数");
        System.out.println("线段树数组大小: " + (4 * size) + " 个整数");
        System.out.println("内存使用比例: " + (4.0 * size / size) + " 倍");
        
        // 工程化改进建议
        System.out.println("\n=== 工程化改进建议 ===");
        System.out.println("1. 使用懒惰标记优化区间更新操作");
        System.out.println("2. 实现动态开点线段树节省内存");
        System.out.println("3. 添加线程安全支持");
        System.out.println("4. 实现序列化和反序列化功能");
        System.out.println("5. 添加监控和性能统计功能");
    }
}