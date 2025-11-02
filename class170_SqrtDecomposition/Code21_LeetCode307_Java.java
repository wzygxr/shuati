package class172;

// LeetCode 307. Range Sum Query - Mutable - Java实现
// 题目来源：LeetCode 307. Range Sum Query - Mutable
// 题目链接：https://leetcode.com/problems/range-sum-query-mutable/
// 题目大意：
// 设计一个数据结构，支持以下两种操作：
// 1. update(i, val)：将下标为i的元素更新为val
// 2. sumRange(i, j)：返回下标从i到j的元素和
// 要求两种操作的时间复杂度尽可能低

// 解题思路：
// 使用分块算法解决此问题
// 1. 将数组分成sqrt(n)大小的块
// 2. 每个块维护块内元素的和
// 3. 对于update操作，更新原数组和对应块的和
// 4. 对于sumRange操作，不完整块直接累加，完整块使用预计算的块和

// 时间复杂度分析：
// 1. 预处理：O(n)，构建分块结构
// 2. update操作：O(1)，更新单个元素和对应块的和
// 3. sumRange操作：O(√n)，遍历不完整块 + 累加完整块的和
// 空间复杂度：O(n)，存储原数组、块信息和块和数组

// 工程化考量：
// 1. 异常处理：检查输入边界，防止数组越界
// 2. 性能优化：使用块和减少重复计算
// 3. 可读性：清晰的变量命名和注释
// 4. 测试用例：包含边界测试和性能测试

import java.util.*;

public class Code21_LeetCode307_Java {
    
    // 原数组
    private int[] nums;
    
    // 块大小和块数量
    private int blockSize;
    private int blockNum;
    
    // 每个块的和
    private int[] blockSum;
    
    // 每个元素所属的块编号
    private int[] belong;
    
    /**
     * 构造函数：初始化数据结构
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * @param nums 初始数组
     */
    public Code21_LeetCode307_Java(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be null or empty");
        }
        
        this.nums = nums.clone(); // 深拷贝，避免修改原数组
        int n = nums.length;
        
        // 计算块大小，通常取sqrt(n)
        this.blockSize = (int) Math.sqrt(n);
        // 计算块数量，向上取整
        this.blockNum = (n + blockSize - 1) / blockSize;
        
        // 初始化块和数组
        this.blockSum = new int[blockNum];
        this.belong = new int[n];
        
        // 构建分块结构
        build();
    }
    
    /**
     * 构建分块结构
     * 时间复杂度：O(n)
     */
    private void build() {
        int n = nums.length;
        
        // 确定每个元素属于哪个块，并计算块和
        for (int i = 0; i < n; i++) {
            belong[i] = i / blockSize;
            blockSum[belong[i]] += nums[i];
        }
    }
    
    /**
     * 更新操作：将下标为i的元素更新为val
     * 时间复杂度：O(1)
     * 
     * @param i 要更新的下标
     * @param val 新的值
     */
    public void update(int i, int val) {
        // 检查输入边界
        if (i < 0 || i >= nums.length) {
            throw new IllegalArgumentException("Index out of bounds: " + i);
        }
        
        // 计算差值，更新块和
        int diff = val - nums[i];
        nums[i] = val;
        blockSum[belong[i]] += diff;
    }
    
    /**
     * 区间求和操作：返回下标从i到j的元素和
     * 时间复杂度：O(√n)
     * 
     * @param i 区间左端点
     * @param j 区间右端点
     * @return 区间和
     */
    public int sumRange(int i, int j) {
        // 检查输入边界
        if (i < 0 || j >= nums.length || i > j) {
            throw new IllegalArgumentException("Invalid range: [" + i + ", " + j + "]");
        }
        
        int sum = 0;
        int leftBlock = belong[i];
        int rightBlock = belong[j];
        
        if (leftBlock == rightBlock) {
            // 情况1：区间在同一个块内，直接遍历累加
            for (int k = i; k <= j; k++) {
                sum += nums[k];
            }
        } else {
            // 情况2：区间跨越多个块
            
            // 处理左边不完整的块
            for (int k = i; k < (leftBlock + 1) * blockSize && k < nums.length; k++) {
                sum += nums[k];
            }
            
            // 处理中间完整的块（使用预计算的块和）
            for (int k = leftBlock + 1; k < rightBlock; k++) {
                sum += blockSum[k];
            }
            
            // 处理右边不完整的块
            for (int k = rightBlock * blockSize; k <= j; k++) {
                sum += nums[k];
            }
        }
        
        return sum;
    }
    
    /**
     * 获取数组长度
     * 时间复杂度：O(1)
     * 
     * @return 数组长度
     */
    public int size() {
        return nums.length;
    }
    
    /**
     * 获取指定位置的元素值
     * 时间复杂度：O(1)
     * 
     * @param i 下标
     * @return 元素值
     */
    public int get(int i) {
        if (i < 0 || i >= nums.length) {
            throw new IllegalArgumentException("Index out of bounds: " + i);
        }
        return nums[i];
    }
    
    /**
     * 主函数：演示使用方法和测试
     */
    public static void main(String[] args) {
        // 测试用例1：基础功能测试
        System.out.println("=== 测试用例1：基础功能测试 ===");
        int[] testNums = {1, 3, 5, 7, 9, 11};
        Code21_LeetCode307_Java numArray = new Code21_LeetCode307_Java(testNums);
        
        // 测试初始状态
        System.out.println("初始数组: " + Arrays.toString(testNums));
        System.out.println("sumRange(0, 2) = " + numArray.sumRange(0, 2) + " (期望: 9)");
        System.out.println("sumRange(1, 4) = " + numArray.sumRange(1, 4) + " (期望: 24)");
        
        // 测试更新操作
        numArray.update(1, 10);
        System.out.println("更新后数组: [" + numArray.get(0) + ", " + numArray.get(1) + ", " + numArray.get(2) + ", ...]");
        System.out.println("更新后 sumRange(0, 2) = " + numArray.sumRange(0, 2) + " (期望: 16)");
        
        // 测试用例2：边界测试
        System.out.println("\n=== 测试用例2：边界测试 ===");
        try {
            numArray.update(-1, 100);
            System.out.println("ERROR: 应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过: " + e.getMessage());
        }
        
        try {
            numArray.sumRange(2, 1);
            System.out.println("ERROR: 应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过: " + e.getMessage());
        }
        
        // 测试用例3：性能测试
        System.out.println("\n=== 测试用例3：性能测试 ===");
        performanceTest();
        
        System.out.println("\n=== 所有测试完成 ===");
    }
    
    /**
     * 性能测试方法
     * 测试大规模数据下的性能表现
     */
    public static void performanceTest() {
        int n = 100000; // 10万数据量
        int[] largeNums = new int[n];
        for (int i = 0; i < n; i++) {
            largeNums[i] = i + 1;
        }
        
        long startTime = System.currentTimeMillis();
        
        Code21_LeetCode307_Java numArray = new Code21_LeetCode307_Java(largeNums);
        
        // 执行大量操作
        int operations = 100000;
        for (int i = 0; i < operations; i++) {
            if (i % 3 == 0) {
                // 更新操作
                int index = (int) (Math.random() * n);
                int value = (int) (Math.random() * 1000);
                numArray.update(index, value);
            } else {
                // 查询操作
                int left = (int) (Math.random() * n);
                int right = left + (int) (Math.random() * 100);
                if (right >= n) right = n - 1;
                numArray.sumRange(left, right);
            }
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试完成，耗时：" + (endTime - startTime) + "ms");
        System.out.println("操作数量：" + operations + "，数据规模：" + n);
        System.out.println("块大小：" + numArray.blockSize + "，块数量：" + numArray.blockNum);
    }
    
    /**
     * 单元测试方法
     * 包含多个测试场景
     */
    public static void test() {
        System.out.println("=== 开始单元测试 ===");
        
        // 测试场景1：空数组
        try {
            new Code21_LeetCode307_Java(new int[0]);
            System.out.println("ERROR: 应该抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("空数组测试通过");
        }
        
        // 测试场景2：单元素数组
        Code21_LeetCode307_Java singleArray = new Code21_LeetCode307_Java(new int[]{42});
        assert singleArray.sumRange(0, 0) == 42 : "单元素测试失败";
        singleArray.update(0, 100);
        assert singleArray.sumRange(0, 0) == 100 : "单元素更新测试失败";
        System.out.println("单元素数组测试通过");
        
        // 测试场景3：常规数组
        int[] regularNums = {2, 4, 6, 8, 10};
        Code21_LeetCode307_Java regularArray = new Code21_LeetCode307_Java(regularNums);
        
        // 验证初始状态
        assert regularArray.sumRange(0, 4) == 30 : "初始和测试失败";
        assert regularArray.sumRange(1, 3) == 18 : "子区间和测试失败";
        
        // 验证更新操作
        regularArray.update(2, 10);
        assert regularArray.sumRange(0, 4) == 34 : "更新后和测试失败";
        assert regularArray.get(2) == 10 : "获取更新值测试失败";
        
        System.out.println("常规数组测试通过");
        
        // 测试场景4：边界值
        int[] edgeNums = {1, 2, 3, 4, 5};
        Code21_LeetCode307_Java edgeArray = new Code21_LeetCode307_Java(edgeNums);
        
        // 边界查询
        assert edgeArray.sumRange(0, 0) == 1 : "左边界查询失败";
        assert edgeArray.sumRange(4, 4) == 5 : "右边界查询失败";
        assert edgeArray.sumRange(0, 4) == 15 : "全范围查询失败";
        
        System.out.println("边界值测试通过");
        
        System.out.println("=== 所有单元测试通过 ===");
    }
}

// 复杂度分析总结：
// 时间复杂度：
// - 构造函数：O(n) 需要遍历整个数组构建分块结构
// - update操作：O(1) 只需要更新单个元素和对应块的和
// - sumRange操作：O(√n) 最坏情况下需要遍历两个不完整块
//
// 空间复杂度：O(n) 用于存储原数组、块信息和块和数组
//
// 算法优势：
// 1. update操作非常高效，只需要O(1)时间
// 2. 实现相对简单，代码易于理解和维护
// 3. 对于update操作频繁的场景表现优秀
//
// 算法局限性：
// 1. sumRange操作的时间复杂度为O(√n)，不如线段树的O(log n)
// 2. 对于查询操作非常频繁的场景，效率可能不如其他数据结构
//
// 适用场景：
// 1. update操作比sumRange操作更频繁的场景
// 2. 数据规模中等，不需要极致查询性能的场景
// 3. 需要快速实现和调试的场景
//
// 对比其他解法：
// 1. 线段树：查询O(log n)，更新O(log n)，实现复杂
// 2. 树状数组：查询O(log n)，更新O(log n)，代码简洁
// 3. 前缀和：查询O(1)，更新O(n)，适用于更新少的场景
//
// 工程化建议：
// 1. 根据实际数据分布调整块大小
// 2. 对于小规模数据，可以直接使用暴力解法
// 3. 考虑添加缓存机制优化频繁查询