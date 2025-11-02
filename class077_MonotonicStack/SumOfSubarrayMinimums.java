// package class052.problems;

import java.util.Arrays;

/**
 * 907. 子数组的最小值之和 (Sum of Subarray Minimums)
 * 
 * 题目描述:
 * 给定一个整数数组 arr，找到 min(b) 的总和，其中 b 的范围为 arr 的每个（连续）子数组。
 * 由于答案可能很大，因此返回答案模 10^9 + 7。
 * 
 * 解题思路:
 * 使用单调栈来解决。对于每个元素，找到它作为最小值能覆盖的左右边界。
 * 然后计算该元素对总和的贡献：arr[i] * (左边界的长度) * (右边界的长度)
 * 
 * 时间复杂度: O(n)，每个元素最多入栈和出栈各一次
 * 空间复杂度: O(n)，用于存储单调栈和左右边界数组
 * 
 * 测试链接: https://leetcode.cn/problems/sum-of-subarray-minimums/
 * 
 * 工程化考量:
 * 1. 异常处理：空数组、边界情况处理
 * 2. 性能优化：使用数组模拟栈，避免对象创建
 * 3. 大数处理：使用long类型避免溢出，最后取模
 * 4. 代码可读性：详细注释和模块化设计
 */
public class SumOfSubarrayMinimums {
    
    private static final int MOD = 1000000007;
    
    /**
     * 计算所有子数组的最小值之和
     * 
     * @param arr 输入整数数组
     * @return 子数组最小值之和模 10^9 + 7
     */
    public static int sumSubarrayMins(int[] arr) {
        // 边界条件检查
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        // 使用数组模拟栈，提高性能
        int[] stack = new int[n + 1];
        int top = -1;
        
        // 存储每个元素左边第一个比它小的元素位置
        int[] left = new int[n];
        // 存储每个元素右边第一个比它小的元素位置  
        int[] right = new int[n];
        
        // 初始化左右边界数组
        Arrays.fill(left, -1);
        Arrays.fill(right, n);
        
        // 第一次遍历：找到每个元素右边第一个比它小的位置
        for (int i = 0; i < n; i++) {
            while (top >= 0 && arr[stack[top]] > arr[i]) {
                right[stack[top]] = i;
                top--;
            }
            stack[++top] = i;
        }
        
        // 重置栈
        top = -1;
        
        // 第二次遍历：找到每个元素左边第一个比它小的位置
        for (int i = n - 1; i >= 0; i--) {
            while (top >= 0 && arr[stack[top]] >= arr[i]) {
                left[stack[top]] = i;
                top--;
            }
            stack[++top] = i;
        }
        
        // 计算总和
        long sum = 0;
        for (int i = 0; i < n; i++) {
            // 计算当前元素作为最小值的子数组数量
            long leftCount = i - left[i];
            long rightCount = right[i] - i;
            long contribution = (leftCount * rightCount) % MOD;
            contribution = (contribution * arr[i]) % MOD;
            sum = (sum + contribution) % MOD;
        }
        
        return (int) sum;
    }
    
    /**
     * 优化版本：一次遍历完成左右边界计算
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     */
    public static int sumSubarrayMinsOptimized(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }
        
        int n = arr.length;
        int[] stack = new int[n + 1];
        int top = -1;
        long sum = 0;
        
        // 添加哨兵，简化边界处理
        int[] newArr = new int[n + 1];
        System.arraycopy(arr, 0, newArr, 0, n);
        newArr[n] = 0; // 哨兵值
        
        for (int i = 0; i <= n; i++) {
            while (top >= 0 && newArr[stack[top]] > newArr[i]) {
                int index = stack[top--];
                int left = top >= 0 ? stack[top] : -1;
                long leftCount = index - left;
                long rightCount = i - index;
                long contribution = (leftCount * rightCount) % MOD;
                contribution = (contribution * newArr[index]) % MOD;
                sum = (sum + contribution) % MOD;
            }
            stack[++top] = i;
        }
        
        return (int) sum;
    }
    
    /**
     * 测试方法 - 验证算法正确性
     */
    public static void main(String[] args) {
        // 测试用例1: [3,1,2,4] - 预期: 17
        int[] arr1 = {3, 1, 2, 4};
        int result1 = sumSubarrayMins(arr1);
        int result1Opt = sumSubarrayMinsOptimized(arr1);
        System.out.println("测试用例1 [3,1,2,4]: " + result1 + " (优化版: " + result1Opt + ", 预期: 17)");
        
        // 测试用例2: [11,81,94,43,3] - 预期: 444
        int[] arr2 = {11, 81, 94, 43, 3};
        int result2 = sumSubarrayMins(arr2);
        int result2Opt = sumSubarrayMinsOptimized(arr2);
        System.out.println("测试用例2 [11,81,94,43,3]: " + result2 + " (优化版: " + result2Opt + ", 预期: 444)");
        
        // 测试用例3: 边界情况 - 空数组
        int[] arr3 = {};
        int result3 = sumSubarrayMins(arr3);
        int result3Opt = sumSubarrayMinsOptimized(arr3);
        System.out.println("测试用例3 []: " + result3 + " (优化版: " + result3Opt + ", 预期: 0)");
        
        // 测试用例4: 单元素数组 [5] - 预期: 5
        int[] arr4 = {5};
        int result4 = sumSubarrayMins(arr4);
        int result4Opt = sumSubarrayMinsOptimized(arr4);
        System.out.println("测试用例4 [5]: " + result4 + " (优化版: " + result4Opt + ", 预期: 5)");
        
        // 测试用例5: 重复元素 [2,2,2] - 预期: 12
        int[] arr5 = {2, 2, 2};
        int result5 = sumSubarrayMins(arr5);
        int result5Opt = sumSubarrayMinsOptimized(arr5);
        System.out.println("测试用例5 [2,2,2]: " + result5 + " (优化版: " + result5Opt + ", 预期: 12)");
        
        // 性能测试：大规模数据
        int[] arr6 = new int[10000];
        Arrays.fill(arr6, 1); // 所有元素为1
        long startTime = System.currentTimeMillis();
        int result6 = sumSubarrayMinsOptimized(arr6);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 [10000个1]: 结果=" + result6 + ", 耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
    
    /**
     * 调试辅助方法：打印中间过程
     */
    private static void debugPrint(int[] arr, int[] left, int[] right, long sum) {
        System.out.println("数组: " + Arrays.toString(arr));
        System.out.println("左边界: " + Arrays.toString(left));
        System.out.println("右边界: " + Arrays.toString(right));
        System.out.println("当前总和: " + sum);
        System.out.println("---");
    }
    
    /**
     * 算法复杂度分析:
     * 
     * 时间复杂度: O(n)
     * - 每个元素最多入栈一次和出栈一次
     * - 两次遍历数组，但总操作次数为O(n)
     * 
     * 空间复杂度: O(n)
     * - 使用了三个大小为n的数组：left、right、stack
     * - 优化版本使用了O(n)的额外空间
     * 
     * 最优解分析:
     * - 这是子数组最小值之和问题的最优解
     * - 无法在O(n)时间内获得更好的时间复杂度
     * - 空间复杂度也是最优的，因为需要存储边界信息
     * 
     * 数学原理:
     * - 对于每个元素arr[i]，它作为最小值的子数组数量为：(i - left[i]) * (right[i] - i)
     * - 总贡献为：arr[i] * 子数组数量
     * - 所有元素的贡献之和即为答案
     */
}