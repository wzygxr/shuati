// package class052.problems;

import java.util.Arrays;

/**
 * 503. 下一个更大元素 II (Next Greater Element II)
 * 
 * 题目描述:
 * 给定一个循环数组 nums（最后一个元素的下一个元素是数组的第一个元素），
 * 返回每个元素的下一个更大元素。如果不存在，则输出 -1。
 * 
 * 解题思路:
 * 使用单调栈来解决。由于是循环数组，可以遍历数组两次来模拟循环效果。
 * 维护一个单调递减栈，栈中存储元素索引。
 * 当遇到比栈顶元素大的元素时，说明找到了栈顶元素的下一个更大元素。
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈和结果数组
 * 
 * 测试链接: https://leetcode.cn/problems/next-greater-element-ii/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用数组模拟栈，避免对象创建
 * 3. 循环数组处理：遍历两次模拟循环效果
 * 4. 代码可读性：详细注释和模块化设计
 */
public class NextGreaterElementII {
    
    /**
     * 查找循环数组中每个元素的下一个更大元素
     * 
     * @param nums 输入循环数组
     * @return 每个元素的下一个更大元素数组
     */
    public static int[] nextGreaterElements(int[] nums) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1); // 初始化为-1
        
        // 使用数组模拟栈，提高性能
        int[] stack = new int[2 * n]; // 最多需要2n空间
        int top = -1;
        
        // 遍历两次数组模拟循环效果
        for (int i = 0; i < 2 * n; i++) {
            int actualIndex = i % n; // 实际数组索引
            
            // 当栈不为空且当前元素大于栈顶索引对应的元素时
            while (top >= 0 && nums[actualIndex] > nums[stack[top]]) {
                int index = stack[top--]; // 弹出栈顶元素
                result[index] = nums[actualIndex]; // 设置下一个更大元素
            }
            
            // 只在第一次遍历时将索引入栈
            if (i < n) {
                stack[++top] = actualIndex;
            }
        }
        
        return result;
    }
    
    /**
     * 优化版本：使用更简洁的栈实现
     */
    public static int[] nextGreaterElementsOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        
        int[] stack = new int[2 * n];
        int top = -1;
        
        for (int i = 0; i < 2 * n; i++) {
            int idx = i % n;
            
            while (top >= 0 && nums[idx] > nums[stack[top]]) {
                result[stack[top--]] = nums[idx];
            }
            
            // 只在第一次遍历时入栈
            if (i < n) {
                stack[++top] = idx;
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [1,2,1] - 预期: [2,-1,2]
        int[] nums1 = {1, 2, 1};
        int[] result1 = nextGreaterElements(nums1);
        int[] result1Opt = nextGreaterElementsOptimized(nums1);
        System.out.println("测试用例1 [1,2,1]: " + Arrays.toString(result1) + 
                         " (优化版: " + Arrays.toString(result1Opt) + ", 预期: [2, -1, 2])");
        
        // 测试用例2: [1,2,3,4,3] - 预期: [2,3,4,-1,4]
        int[] nums2 = {1, 2, 3, 4, 3};
        int[] result2 = nextGreaterElements(nums2);
        int[] result2Opt = nextGreaterElementsOptimized(nums2);
        System.out.println("测试用例2 [1,2,3,4,3]: " + Arrays.toString(result2) + 
                         " (优化版: " + Arrays.toString(result2Opt) + ", 预期: [2, 3, 4, -1, 4])");
        
        // 测试用例3: 边界情况 - 空数组
        int[] nums3 = {};
        int[] result3 = nextGreaterElements(nums3);
        int[] result3Opt = nextGreaterElementsOptimized(nums3);
        System.out.println("测试用例3 []: " + Arrays.toString(result3) + 
                         " (优化版: " + Arrays.toString(result3Opt) + ", 预期: [])");
        
        // 测试用例4: 单元素数组 [5] - 预期: [-1]
        int[] nums4 = {5};
        int[] result4 = nextGreaterElements(nums4);
        int[] result4Opt = nextGreaterElementsOptimized(nums4);
        System.out.println("测试用例4 [5]: " + Arrays.toString(result4) + 
                         " (优化版: " + Arrays.toString(result4Opt) + ", 预期: [-1])");
        
        // 测试用例5: 所有元素相同 [2,2,2] - 预期: [-1,-1,-1]
        int[] nums5 = {2, 2, 2};
        int[] result5 = nextGreaterElements(nums5);
        int[] result5Opt = nextGreaterElementsOptimized(nums5);
        System.out.println("测试用例5 [2,2,2]: " + Arrays.toString(result5) + 
                         " (优化版: " + Arrays.toString(result5Opt) + ", 预期: [-1, -1, -1])");
        
        // 性能测试：大规模数据
        int[] nums6 = new int[10000];
        Arrays.fill(nums6, 1); // 所有元素为1
        nums6[5000] = 2; // 中间插入一个较大值
        long startTime = System.currentTimeMillis();
        int[] result6 = nextGreaterElementsOptimized(nums6);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [10000个元素]: 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 调试辅助方法：打印中间过程
     */
    private static void debugPrint(int[] nums, int i, int actualIndex, int[] stack, int top, int[] result) {
        System.out.println("i=" + i + ", actualIndex=" + actualIndex + ", nums[actualIndex]=" + nums[actualIndex]);
        System.out.print("栈内容: [");
        for (int j = 0; j <= top; j++) {
            System.out.print(nums[stack[j]]);
            if (j < top) System.out.print(", ");
        }
        System.out.println("]");
        System.out.println("当前结果: " + Arrays.toString(result));
        System.out.println("---");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: O(n)
     * - 虽然遍历了2n次，但每个元素最多入栈和出栈各一次
     * - 总操作次数为O(n)
     * 
     * 空间复杂度: O(n)
     * - 使用了大小为2n的栈数组（实际最多使用n个位置）
     * - 结果数组大小为n
     * 
     * 最优解分析:
     * - 这是循环数组下一个更大元素问题的最优解
     * - 无法在O(n)时间内获得更好的时间复杂度
     * - 空间复杂度也是最优的
     * 
     * 循环数组处理技巧:
     * - 通过取模运算实现循环访问: i % n
     * - 遍历两次数组确保覆盖所有可能的下一更大元素
     * - 只在第一次遍历时入栈，避免重复处理
     */
}