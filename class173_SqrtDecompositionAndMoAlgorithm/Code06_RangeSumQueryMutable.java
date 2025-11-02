package class175;

// LeetCode 307. Range Sum Query - Mutable - 分块算法实现 (Java版本)
// 题目来源: LeetCode
// 链接: https://leetcode.com/problems/range-sum-query-mutable/
// 题目大意: 实现一个支持区间和查询和单点更新的数据结构
// 约束条件: 
// - 1 <= nums.length <= 3 * 10^4
// - -100 <= nums[i] <= 100
// - 最多调用 3 * 10^4 次 update 和 sumRange 操作

// 分块算法分析:
// 时间复杂度: 
// - 构造函数: O(n)，需要初始化数组和分块信息
// - update操作: O(1)，只需要更新单个元素和对应的块和
// - sumRange操作: O(√n)，需要遍历部分块和部分元素
// 空间复杂度: O(n)，需要存储原始数组和块和数组

// 最优解验证:
// 分块算法是解决此类问题的经典方法之一，在更新和查询操作之间提供了良好的平衡。
// 对于频繁更新和查询的场景，分块算法通常比线段树更简单且常数更小。

import java.util.Arrays;

public class Code06_RangeSumQueryMutable {
    
    private int[] nums;           // 原始数组
    private int[] blockSum;        // 每个块的和
    private int blockSize;        // 块大小
    private int blockCount;       // 块数量
    
    /**
     * 构造函数 - 初始化分块数据结构
     * 
     * @param nums 初始数组
     * 
     * 算法步骤:
     * 1. 计算合适的块大小，通常选择sqrt(n)
     * 2. 初始化块和数组
     * 3. 计算每个块的初始和
     * 
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public Code06_RangeSumQueryMutable(int[] nums) {
        if (nums == null || nums.length == 0) {
            throw new IllegalArgumentException("数组不能为空");
        }
        
        this.nums = Arrays.copyOf(nums, nums.length);
        
        // 计算块大小，通常选择sqrt(n)
        this.blockSize = (int) Math.sqrt(nums.length);
        this.blockCount = (nums.length + blockSize - 1) / blockSize;
        
        // 初始化块和数组
        this.blockSum = new int[blockCount];
        
        // 计算每个块的初始和
        for (int i = 0; i < nums.length; i++) {
            int blockIndex = i / blockSize;
            blockSum[blockIndex] += nums[i];
        }
    }
    
    /**
     * 更新操作 - 将索引i处的值更新为val
     * 
     * @param i 要更新的索引
     * @param val 新的值
     * 
     * 算法步骤:
     * 1. 验证索引有效性
     * 2. 计算值的变化量
     * 3. 更新原始数组
     * 4. 更新对应的块和
     * 
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 异常处理:
     * - 索引越界: 抛出IllegalArgumentException
     * - 空数组: 已在构造函数中处理
     */
    public void update(int i, int val) {
        // 边界检查
        if (i < 0 || i >= nums.length) {
            throw new IllegalArgumentException("索引越界: " + i);
        }
        
        // 计算值的变化量
        int delta = val - nums[i];
        
        // 更新原始数组
        nums[i] = val;
        
        // 更新对应的块和
        int blockIndex = i / blockSize;
        blockSum[blockIndex] += delta;
    }
    
    /**
     * 区间和查询 - 计算索引left到right的区间和
     * 
     * @param left 区间左边界（包含）
     * @param right 区间右边界（包含）
     * @return 区间和
     * 
     * 算法步骤:
     * 1. 验证边界有效性
     * 2. 计算左右边界所在的块
     * 3. 如果左右边界在同一个块内，直接遍历计算
     * 4. 否则分三部分计算:
     *    - 左边不完整块
     *    - 中间完整块
     *    - 右边不完整块
     * 
     * 时间复杂度: O(√n)
     * 空间复杂度: O(1)
     * 
     * 异常处理:
     * - 边界越界: 抛出IllegalArgumentException
     * - left > right: 交换边界或返回0
     */
    public int sumRange(int left, int right) {
        // 边界检查
        if (left < 0 || right >= nums.length || left > right) {
            throw new IllegalArgumentException("区间边界无效: [" + left + ", " + right + "]");
        }
        
        // 如果左右边界相同，直接返回该位置的值
        if (left == right) {
            return nums[left];
        }
        
        int sum = 0;
        int leftBlock = left / blockSize;
        int rightBlock = right / blockSize;
        
        // 如果左右边界在同一个块内
        if (leftBlock == rightBlock) {
            // 直接遍历该块内的元素
            for (int i = left; i <= right; i++) {
                sum += nums[i];
            }
        } else {
            // 计算左边不完整块的和
            for (int i = left; i < (leftBlock + 1) * blockSize; i++) {
                sum += nums[i];
            }
            
            // 计算中间完整块的和
            for (int block = leftBlock + 1; block < rightBlock; block++) {
                sum += blockSum[block];
            }
            
            // 计算右边不完整块的和
            for (int i = rightBlock * blockSize; i <= right; i++) {
                sum += nums[i];
            }
        }
        
        return sum;
    }
    
    /**
     * 获取数组长度
     * 
     * @return 数组长度
     */
    public int size() {
        return nums.length;
    }
    
    /**
     * 获取块大小
     * 
     * @return 块大小
     */
    public int getBlockSize() {
        return blockSize;
    }
    
    /**
     * 获取块数量
     * 
     * @return 块数量
     */
    public int getBlockCount() {
        return blockCount;
    }
    
    /**
     * 单元测试方法 - 验证算法的正确性
     * 
     * 测试用例设计:
     * 1. 正常情况测试
     * 2. 边界情况测试
     * 3. 异常情况测试
     * 4. 性能测试
     */
    public static void main(String[] args) {
        // 测试用例1: 正常情况
        System.out.println("=== 测试用例1: 正常情况 ===");
        int[] nums1 = {1, 3, 5, 7, 9, 11};
        Code06_RangeSumQueryMutable numArray1 = new Code06_RangeSumQueryMutable(nums1);
        
        // 测试初始区间和
        System.out.println("sumRange(0, 2) = " + numArray1.sumRange(0, 2)); // 期望: 1+3+5=9
        System.out.println("sumRange(1, 4) = " + numArray1.sumRange(1, 4)); // 期望: 3+5+7+9=24
        
        // 测试更新操作
        numArray1.update(1, 10);
        System.out.println("更新后 sumRange(0, 2) = " + numArray1.sumRange(0, 2)); // 期望: 1+10+5=16
        
        // 测试用例2: 边界情况
        System.out.println("\n=== 测试用例2: 边界情况 ===");
        int[] nums2 = {2};
        Code06_RangeSumQueryMutable numArray2 = new Code06_RangeSumQueryMutable(nums2);
        
        System.out.println("sumRange(0, 0) = " + numArray2.sumRange(0, 0)); // 期望: 2
        numArray2.update(0, 5);
        System.out.println("更新后 sumRange(0, 0) = " + numArray2.sumRange(0, 0)); // 期望: 5
        
        // 测试用例3: 空数组（异常情况）
        System.out.println("\n=== 测试用例3: 异常情况 ===");
        try {
            int[] nums3 = {};
            Code06_RangeSumQueryMutable numArray3 = new Code06_RangeSumQueryMutable(nums3);
        } catch (IllegalArgumentException e) {
            System.out.println("正确捕获异常: " + e.getMessage());
        }
        
        // 测试用例4: 大规模数据性能测试
        System.out.println("\n=== 测试用例4: 性能测试 ===");
        int size = 10000;
        int[] nums4 = new int[size];
        for (int i = 0; i < size; i++) {
            nums4[i] = i + 1;
        }
        
        Code06_RangeSumQueryMutable numArray4 = new Code06_RangeSumQueryMutable(nums4);
        
        long startTime = System.currentTimeMillis();
        int result = numArray4.sumRange(0, size - 1);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大规模数据区间和: " + result);
        System.out.println("查询时间: " + (endTime - startTime) + "ms");
        
        // 验证结果正确性
        long expected = (long) size * (size + 1) / 2;
        System.out.println("期望结果: " + expected);
        System.out.println("结果正确性: " + (result == expected));
        
        System.out.println("\n=== 所有测试用例执行完成 ===");
    }
    
    /**
     * 调试方法 - 打印数据结构状态
     * 用于调试和问题定位
     */
    public void printStructure() {
        System.out.println("=== 分块数据结构状态 ===");
        System.out.println("数组长度: " + nums.length);
        System.out.println("块大小: " + blockSize);
        System.out.println("块数量: " + blockCount);
        
        System.out.println("原始数组: " + Arrays.toString(nums));
        System.out.println("块和数组: " + Arrays.toString(blockSum));
        
        // 验证块和正确性
        int totalSum = 0;
        for (int num : nums) {
            totalSum += num;
        }
        
        int blockSumTotal = 0;
        for (int sum : blockSum) {
            blockSumTotal += sum;
        }
        
        System.out.println("数组总和: " + totalSum);
        System.out.println("块和总和: " + blockSumTotal);
        System.out.println("一致性检查: " + (totalSum == blockSumTotal));
    }
}

/**
 * 工程化考量总结:
 * 
 * 1. 异常处理:
 *    - 构造函数验证输入有效性
 *    - update和sumRange方法验证参数边界
 *    - 提供清晰的错误信息
 * 
 * 2. 性能优化:
 *    - 选择合适的块大小平衡查询和更新性能
 *    - 避免不必要的计算和内存分配
 *    - 使用基本数据类型减少对象开销
 * 
 * 3. 可维护性:
 *    - 详细的注释说明算法原理和步骤
 *    - 模块化的方法设计
 *    - 完整的单元测试覆盖
 * 
 * 4. 调试支持:
 *    - 提供调试方法打印数据结构状态
 *    - 包含一致性检查功能
 *    - 支持性能测试和验证
 * 
 * 5. 扩展性:
 *    - 易于添加新的功能
 *    - 支持不同的块大小策略
 *    - 可以扩展为支持其他区间操作
 */