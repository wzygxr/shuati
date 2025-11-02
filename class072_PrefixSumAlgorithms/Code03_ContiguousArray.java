package class046;

import java.util.HashMap;

/**
 * 连续数组 (Contiguous Array)
 * 
 * 题目描述:
 * 给定一个二进制数组 nums，找到含有相同数量的 0 和 1 的最长连续子数组，并返回该子数组的长度。
 * 
 * 示例:
 * 输入: nums = [0,1]
 * 输出: 2
 * 说明: [0, 1] 是具有相同数量0和1的最长连续子数组。
 * 
 * 输入: nums = [0,1,0]
 * 输出: 2
 * 说明: [0, 1] (或 [1, 0]) 是具有相同数量0和1的最长连续子数组。
 * 
 * 提示:
 * 1 <= nums.length <= 10^5
 * nums[i] 不是 0 就是 1
 * 
 * 题目链接: https://leetcode.com/problems/contiguous-array/
 * 
 * 解题思路:
 * 1. 将0看作-1，问题转化为求和为0的最长子数组
 * 2. 使用前缀和 + 哈希表的方法
 * 3. 遍历数组，计算前缀和
 * 4. 如果某个前缀和之前出现过，说明这两个位置之间的子数组和为0
 * 5. 使用哈希表记录每个前缀和第一次出现的位置
 * 
 * 时间复杂度: O(n) - 需要遍历数组一次
 * 空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和
 * 
 * 工程化考量:
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 哈希表初始化：前缀和为0在位置-1出现，便于计算长度
 * 3. 映射技巧：0→-1, 1→1的转换是关键
 * 4. 性能优化：使用HashMap的O(1)查找时间
 * 
 * 最优解分析:
 * 这是最优解，因为必须遍历所有元素才能找到最长子数组。
 * 哈希表方法将时间复杂度从O(n^2)优化到O(n)。
 * 
 * 算法核心:
 * 设count为前缀和（0→-1, 1→1），当count[i] = count[j]时，子数组[i+1,j]的和为0。
 * 即0和1的数量相等。
 * 
 * 算法调试技巧:
 * 1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
 * 2. 边界测试：测试全0、全1、交替等情况
 * 3. 性能测试：测试大规模数据下的性能表现
 * 
 * 语言特性差异:
 * Java的HashMap自动处理哈希冲突，但需要注意哈希函数的选择。
 * 与C++相比，Java有自动内存管理，无需手动释放哈希表内存。
 * 与Python相比，Java是静态类型语言，需要显式声明类型。
 */
public class Code03_ContiguousArray {

    /**
     * 找到含有相同数量0和1的最长连续子数组的长度
     * 
     * @param nums 输入的二进制数组
     * @return 最长连续子数组的长度
     * 
     * 异常场景处理:
     * - 空数组：返回0
     * - 单元素数组：返回0（不可能有相同数量的0和1）
     * - 全0或全1数组：返回0
     * 
     * 边界条件:
     * - 数组长度为0或1
     * - 数组元素全为0或全为1
     * - 数组元素交替出现
     */
    public static int findMaxLength(int[] nums) {
        // 边界情况处理
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        // 哈希表记录前缀和及其第一次出现的位置
        HashMap<Integer, Integer> map = new HashMap<>();
        // 初始化：前缀和为0在位置-1出现（便于计算长度）
        // 这样当count=0时，长度计算为i - (-1) = i+1
        map.put(0, -1);
        
        int maxLength = 0;  // 最长子数组长度
        int count = 0;      // 当前前缀和（0看作-1，1看作1）
        
        // 遍历数组
        for (int i = 0; i < nums.length; i++) {
            // 更新前缀和：0看作-1，1看作1
            count += (nums[i] == 0) ? -1 : 1;
            
            // 调试打印：显示中间过程
            // System.out.println("位置 " + i + ": 值 = " + nums[i] + ", 前缀和 = " + count);
            
            // 如果当前前缀和之前出现过，更新最大长度
            if (map.containsKey(count)) {
                int length = i - map.get(count);
                maxLength = Math.max(maxLength, length);
                // 调试打印：找到符合条件的子数组
                // System.out.println("找到子数组: 位置 " + (map.get(count) + 1) + " 到 " + i + ", 长度 = " + length);
            } else {
                // 记录当前前缀和第一次出现的位置
                map.put(count, i);
                // 调试打印：记录新前缀和
                // System.out.println("记录新前缀和: " + count + " -> " + i);
            }
        }
        
        return maxLength;
    }

    /**
     * 单元测试方法
     */
    public static void testFindMaxLength() {
        System.out.println("=== 连续数组单元测试 ===");
        
        // 测试用例1：基础情况
        int[] nums1 = {0, 1};
        int result1 = findMaxLength(nums1);
        System.out.println("测试用例1 [0,1]: " + result1 + " (预期: 2)");
        
        // 测试用例2：三个元素
        int[] nums2 = {0, 1, 0};
        int result2 = findMaxLength(nums2);
        System.out.println("测试用例2 [0,1,0]: " + result2 + " (预期: 2)");
        
        // 测试用例3：复杂情况
        int[] nums3 = {0, 0, 1, 0, 0, 1, 1, 0};
        int result3 = findMaxLength(nums3);
        System.out.println("测试用例3 [0,0,1,0,0,1,1,0]: " + result3 + " (预期: 6)");
        
        // 测试用例4：全0数组
        int[] nums4 = {0, 0, 0};
        int result4 = findMaxLength(nums4);
        System.out.println("测试用例4 [0,0,0]: " + result4 + " (预期: 0)");
        
        // 测试用例5：全1数组
        int[] nums5 = {1, 1, 1};
        int result5 = findMaxLength(nums5);
        System.out.println("测试用例5 [1,1,1]: " + result5 + " (预期: 0)");
        
        // 测试用例6：空数组
        int[] nums6 = {};
        int result6 = findMaxLength(nums6);
        System.out.println("测试用例6 []: " + result6 + " (预期: 0)");
        
        // 测试用例7：单元素数组
        int[] nums7 = {0};
        int result7 = findMaxLength(nums7);
        System.out.println("测试用例7 [0]: " + result7 + " (预期: 0)");
        
        // 测试用例8：交替数组
        int[] nums8 = {0, 1, 0, 1, 0, 1};
        int result8 = findMaxLength(nums8);
        System.out.println("测试用例8 [0,1,0,1,0,1]: " + result8 + " (预期: 6)");
    }

    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        int size = 100000; // 10万元素
        int[] largeArray = new int[size];
        
        // 初始化大数组（交替0和1）
        for (int i = 0; i < size; i++) {
            largeArray[i] = i % 2;
        }
        
        long startTime = System.currentTimeMillis();
        int result = findMaxLength(largeArray);
        long endTime = System.currentTimeMillis();
        
        System.out.println("处理 " + size + " 个元素，最长子数组长度: " + result + ", 耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 主函数 - 测试入口
     */
    public static void main(String[] args) {
        // 运行单元测试
        testFindMaxLength();
        
        // 运行性能测试（可选）
        // performanceTest();
        
        System.out.println("\n=== 测试完成 ===");
    }
}