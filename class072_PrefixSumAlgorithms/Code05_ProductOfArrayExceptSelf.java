package class046;

/**
 * 除自身以外数组的乘积 (Product of Array Except Self)
 * 
 * 题目描述:
 * 给你一个长度为 n 的整数数组 nums，其中 n > 1，返回输出数组 output，
 * 其中 output[i] 等于 nums 中除 nums[i] 之外其余各元素的乘积。
 * 
 * 示例:
 * 输入: [1,2,3,4]
 * 输出: [24,12,8,6]
 * 
 * 提示:
 * 题目数据保证数组之中任意元素的全部前缀元素和后缀（甚至是整个数组）的乘积都在 32 位整数范围内。
 * 
 * 说明:
 * 请不要使用除法，且在 O(n) 时间复杂度内完成此题。
 * 
 * 进阶:
 * 你可以在常数空间复杂度内完成这个题目吗？（出于对空间复杂度分析的目的，输出数组不被视为额外空间。）
 * 
 * 题目链接: https://leetcode.com/problems/product-of-array-except-self/
 * 
 * 解题思路:
 * 1. 使用两个数组分别存储左侧所有元素的乘积和右侧所有元素的乘积
 * 2. 对于位置i，结果为左侧乘积乘以右侧乘积
 * 3. 进阶：使用输出数组存储左侧乘积，然后从右向左遍历计算右侧乘积并直接更新结果
 * 
 * 时间复杂度: O(n) - 需要遍历数组两次
 * 空间复杂度: O(1) - 不考虑输出数组，只使用常数额外空间
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 空间优化：使用输出数组存储中间结果，避免额外空间
 * 3. 整数溢出：虽然题目保证在32位整数范围内，但实际工程中需要考虑
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能计算乘积。
 * 空间复杂度O(1)也是最优的（不考虑输出数组）。
 * 
 * 算法核心:
 * 对于每个位置i，结果 = 左侧所有元素的乘积 × 右侧所有元素的乘积
 * 通过左右两次遍历，分别计算左侧乘积和右侧乘积。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的左侧乘积和右侧乘积
 * 2. 边界测试：测试包含0、负数、大数等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java中数组是对象，可以直接修改输出数组。
 * 与C++相比，Java有自动内存管理，无需手动释放数组内存。
 * 与Python相比，Java是静态类型语言，需要显式声明数组类型。
 */
public class Code05_ProductOfArrayExceptSelf {

    /**
     * 计算除自身以外数组的乘积
     * 
     * @param nums 输入数组
     * @return 除自身以外数组的乘积
     * 
     * 异常场景处理:
     * - 空数组：直接返回原数组
     * - 单元素数组：直接返回原数组
     * - 包含0的数组：需要特殊处理（但算法本身支持）
     * - 包含负数的数组：算法本身支持
     * 
     * 边界条件:
     * - 数组长度为0或1
     * - 数组元素包含0
     * - 数组元素包含负数
     * - 数组元素包含大数（可能溢出）
     */
    public static int[] productExceptSelf(int[] nums) {
        // 边界情况处理：空数组或单元素数组直接返回
        if (nums == null || nums.length <= 1) {
            return nums;
        }
        
        int n = nums.length;
        int[] result = new int[n];
        
        // 第一遍遍历：从左到右，计算每个位置左侧所有元素的乘积
        // result[i] = nums[0] * nums[1] * ... * nums[i-1]
        result[0] = 1; // 第一个元素左侧没有元素，乘积为1
        for (int i = 1; i < n; i++) {
            result[i] = result[i - 1] * nums[i - 1];
            // 调试打印：显示左侧乘积
            // System.out.println("位置 " + i + " 左侧乘积: " + result[i]);
        }
        
        // 第二遍遍历：从右到左，计算每个位置右侧所有元素的乘积，并与左侧乘积相乘
        // 同时计算右侧乘积：rightProduct = nums[i+1] * nums[i+2] * ... * nums[n-1]
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            // 当前位置的结果 = 左侧乘积 × 右侧乘积
            result[i] *= rightProduct;
            // 更新右侧乘积，为下一个位置（左边）准备
            rightProduct *= nums[i];
            // 调试打印：显示最终结果和右侧乘积
            // System.out.println("位置 " + i + " 结果: " + result[i] + ", 右侧乘积: " + rightProduct);
        }
        
        return result;
    }

    /**
     * 单元测试方法
     */
    public static void testProductExceptSelf() {
        System.out.println("=== 除自身以外数组的乘积单元测试 ===");
        
        // 测试用例1：正常情况
        int[] nums1 = {1, 2, 3, 4};
        int[] result1 = productExceptSelf(nums1);
        System.out.print("测试用例1 [1,2,3,4]: ");
        for (int num : result1) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 24 12 8 6)");
        
        // 测试用例2：包含负数
        int[] nums2 = {2, 3, 4, 5};
        int[] result2 = productExceptSelf(nums2);
        System.out.print("测试用例2 [2,3,4,5]: ");
        for (int num : result2) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 60 40 30 24)");
        
        // 测试用例3：包含0
        int[] nums3 = {1, 0, 3, 4};
        int[] result3 = productExceptSelf(nums3);
        System.out.print("测试用例3 [1,0,3,4]: ");
        for (int num : result3) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 0 12 0 0)");
        
        // 测试用例4：包含负数和0
        int[] nums4 = {-1, 2, 0, -3};
        int[] result4 = productExceptSelf(nums4);
        System.out.print("测试用例4 [-1,2,0,-3]: ");
        for (int num : result4) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 0 0 6 0)");
        
        // 测试用例5：空数组
        int[] nums5 = {};
        int[] result5 = productExceptSelf(nums5);
        System.out.print("测试用例5 []: ");
        for (int num : result5) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 空数组)");
        
        // 测试用例6：单元素数组
        int[] nums6 = {5};
        int[] result6 = productExceptSelf(nums6);
        System.out.print("测试用例6 [5]: ");
        for (int num : result6) {
            System.out.print(num + " ");
        }
        System.out.println(" (预期: 5)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        int size = 1000000; // 100万元素
        int[] largeArray = new int[size];
        
        // 初始化大数组（避免溢出）
        for (int i = 0; i < size; i++) {
            largeArray[i] = (i % 10) + 1; // 数值范围1-10
        }
        
        long startTime = System.currentTimeMillis();
        productExceptSelf(largeArray);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 " + size + " 个元素耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testProductExceptSelf();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("\n=== 测试完成 ===");
    }
}