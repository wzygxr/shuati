package class046;

/**
 * 一维数组的动态和 (Running Sum of 1d Array)
 * 
 * 题目描述:
 * 给你一个数组 nums 。数组「动态和」的计算公式为：runningSum[i] = sum(nums[0]…nums[i]) 。
 * 请返回 nums 的动态和。
 * 
 * 示例:
 * 输入：nums = [1,2,3,4]
 * 输出：[1,3,6,10]
 * 解释：动态和计算过程为 [1, 1+2, 1+2+3, 1+2+3+4] 。
 * 
 * 输入：nums = [1,1,1,1,1]
 * 输出：[1,2,3,4,5]
 * 解释：动态和计算过程为 [1, 1+1, 1+1+1, 1+1+1+1, 1+1+1+1+1] 。
 * 
 * 输入：nums = [3,1,2,10,1]
 * 输出：[3,4,6,16,17]
 * 
 * 提示:
 * 1 <= nums.length <= 1000
 * -10^6 <= nums[i] <= 10^6
 * 
 * 题目链接: https://leetcode.com/problems/running-sum-of-1d-array/
 * 
 * 解题思路:
 * 使用前缀和的思想，从前向后累加即可。
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 原地修改：节省空间，避免创建新数组
 * 3. 整数溢出：虽然题目保证在32位整数范围内，但实际工程中需要考虑
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能计算前缀和，时间复杂度O(n)无法优化。
 * 空间复杂度O(1)也是最优的（不考虑输出数组）。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和
 * 2. 边界测试：测试空数组、单元素数组等特殊情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java中数组是对象，可以直接修改原数组，但要注意并发安全问题。
 * 与C++相比，Java有自动内存管理，无需手动释放数组内存。
 * 与Python相比，Java是静态类型语言，需要显式声明数组类型。
 */
public class Code01_RunningSumOf1DArray {

    /**
     * 计算数组的动态和
     * 
     * @param nums 输入数组
     * @return 动态和数组
     * 
     * 异常场景处理:
     * - 空数组：直接返回原数组
     * - 单元素数组：直接返回原数组
     * - 大数组：使用原地修改避免内存浪费
     * 
     * 边界条件:
     * - 数组长度为0或1
     * - 数组元素包含负数
     * - 数组元素包含大数（可能溢出）
     */
    public static int[] runningSum(int[] nums) {
        // 边界情况处理：空数组或单元素数组直接返回
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        
        // 直接在原数组上进行修改，节省空间
        // 从第二个元素开始，每个位置的值等于前一个位置的前缀和加上当前位置的原始值
        for (int i = 1; i < nums.length; i++) {
            // 调试打印：显示中间过程
            // System.out.println("位置 " + i + ": 前一个前缀和 = " + nums[i-1] + ", 当前值 = " + nums[i]);
            nums[i] += nums[i - 1];
        }
        
        return nums;
    }

    /**
     * 单元测试方法
     */
    public static void testRunningSum() {
        System.out.println("=== 一维数组的动态和单元测试 ===");
        
        // 测试用例1：正常情况
        int[] nums1 = {1, 2, 3, 4};
        int[] result1 = runningSum(nums1.clone());
        System.out.print("测试用例1 [1,2,3,4]: ");
        for (int num : result1) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 1 3 6 10)");
        
        // 测试用例2：全1数组
        int[] nums2 = {1, 1, 1, 1, 1};
        int[] result2 = runningSum(nums2.clone());
        System.out.print("测试用例2 [1,1,1,1,1]: ");
        for (int num : result2) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 1 2 3 4 5)");
        
        // 测试用例3：混合数值
        int[] nums3 = {3, 1, 2, 10, 1};
        int[] result3 = runningSum(nums3.clone());
        System.out.print("测试用例3 [3,1,2,10,1]: ");
        for (int num : result3) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 3 4 6 16 17)");
        
        // 测试用例4：空数组
        int[] nums4 = {};
        int[] result4 = runningSum(nums4);
        System.out.print("测试用例4 []: ");
        for (int num : result4) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 空数组)");
        
        // 测试用例5：单元素数组
        int[] nums5 = {5};
        int[] result5 = runningSum(nums5);
        System.out.print("测试用例5 [5]: ");
        for (int num : result5) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 5)");
        
        // 测试用例6：包含负数
        int[] nums6 = {-1, 2, -3, 4};
        int[] result6 = runningSum(nums6.clone());
        System.out.print("测试用例6 [-1,2,-3,4]: ");
        for (int num : result6) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: -1 1 -2 2)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        int size = 1000000; // 100万元素
        int[] largeArray = new int[size];
        
        // 初始化大数组
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 100; // 避免溢出
        }
        
        long startTime = System.currentTimeMillis();
        runningSum(largeArray);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 " + size + " 个元素耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testRunningSum();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("\n=== 测试完成 ===");
    }
}