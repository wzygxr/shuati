/**
 * LeetCode 659: 分割数组为连续子序列
 *
 * 解题思路：
 * 1. 使用哈希表记录每个数字出现的次数
 * 2. 使用另一个哈希表记录以某个数字结尾的子序列的长度列表
 * 3. 遍历数组，尝试将当前数字添加到合适的子序列末尾
 *
 * 时间复杂度：O(n log n)，其中n是数组的长度
 * 空间复杂度：O(n)
 */

import java.util.*;

public class Code16_SplitArrayIntoConsecutiveSubsequences {

    /**
     * 分割数组为连续子序列的解决方案类
     * 
     * 使用哈希表和最小堆来高效实现。
     */
    public static class Solution {
        /**
         * 判断数组是否可以分割成若干个长度至少为3的连续子序列
         * 
         * @param nums 整数数组
         * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public boolean isPossible(int[] nums) {
            // 输入参数校验
            if (nums == null || nums.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            // 统计每个数字的出现次数
            Map<Integer, Integer> count = new HashMap<>();
            for (int num : nums) {
                count.put(num, count.getOrDefault(num, 0) + 1);
            }
            
            // 记录以每个数字结尾的子序列长度（使用最小堆，优先选择长度最短的子序列）
            // 这样可以优先将当前数字添加到较短的子序列中，尽可能让所有子序列都至少达到长度3
            Map<Integer, PriorityQueue<Integer>> endCount = new HashMap<>();
            
            // 遍历每个数字
            for (int num : nums) {
                // 如果当前数字已经用完，跳过
                if (count.get(num) == 0) {
                    continue;
                }
                
                // 减少当前数字的剩余次数
                count.put(num, count.get(num) - 1);
                
                // 尝试将当前数字添加到以num-1结尾的最短子序列后面
                if (endCount.containsKey(num - 1) && !endCount.get(num - 1).isEmpty()) {
                    // 获取以num-1结尾的最短子序列长度
                    int minLen = endCount.get(num - 1).poll();
                    // 将当前数字添加到该子序列后，现在子序列以num结尾，长度+1
                    endCount.putIfAbsent(num, new PriorityQueue<>());
                    endCount.get(num).offer(minLen + 1);
                } else {
                    // 无法添加到现有子序列，创建一个新的子序列，长度为1
                    endCount.putIfAbsent(num, new PriorityQueue<>());
                    endCount.get(num).offer(1);
                }
            }
            
            // 检查所有子序列的长度是否都至少为3
            for (PriorityQueue<Integer> lengths : endCount.values()) {
                for (int length : lengths) {
                    if (length < 3) {
                        return false;
                    }
                }
            }
            
            return true;
        }
    }

    /**
     * 分割数组为连续子序列的替代解决方案类
     * 
     * 使用贪心算法的另一种实现方式，更高效地处理问题。
     */
    public static class AlternativeSolution {
        /**
         * 判断数组是否可以分割成若干个长度至少为3的连续子序列（优化版本）
         * 
         * @param nums 整数数组
         * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public boolean isPossible(int[] nums) {
            // 输入参数校验
            if (nums == null || nums.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            // 统计每个数字的出现次数
            Map<Integer, Integer> count = new HashMap<>();
            // 记录以每个数字结尾的子序列数量
            // tail[num] 表示以num结尾的子序列数量
            Map<Integer, Integer> tail = new HashMap<>();
            
            // 第一次遍历：统计每个数字的频率
            for (int num : nums) {
                count.put(num, count.getOrDefault(num, 0) + 1);
            }
            
            // 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
            for (int num : nums) {
                // 如果当前数字已经用完，跳过
                if (count.get(num) == 0) {
                    continue;
                }
                
                // 尝试将当前数字添加到以num-1结尾的子序列
                else if (tail.containsKey(num - 1) && tail.get(num - 1) > 0) {
                    count.put(num, count.get(num) - 1);
                    tail.put(num - 1, tail.get(num - 1) - 1);
                    tail.put(num, tail.getOrDefault(num, 0) + 1);
                }
                // 尝试创建一个新的子序列：num, num+1, num+2
                else if (count.containsKey(num + 1) && count.get(num + 1) > 0 && 
                         count.containsKey(num + 2) && count.get(num + 2) > 0) {
                    count.put(num, count.get(num) - 1);
                    count.put(num + 1, count.get(num + 1) - 1);
                    count.put(num + 2, count.get(num + 2) - 1);
                    tail.put(num + 2, tail.getOrDefault(num + 2, 0) + 1);
                }
                // 无法形成有效的子序列
                else {
                    return false;
                }
            }
            
            return true;
        }
    }

    /**
     * 分割数组为连续子序列的优化解决方案类
     * 
     * 结合了最小堆和贪心策略，更高效地处理问题。
     */
    public static class OptimizedSolution {
        /**
         * 判断数组是否可以分割成若干个长度至少为3的连续子序列（高效版本）
         * 
         * @param nums 整数数组
         * @return 如果可以分割成符合要求的子序列，返回true，否则返回false
         * @throws IllegalArgumentException 当输入参数无效时抛出
         */
        public boolean isPossible(int[] nums) {
            // 输入参数校验
            if (nums == null || nums.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            // 快速检查：如果数组长度小于3，不可能分割
            if (nums.length < 3) {
                return false;
            }
            
            // 统计每个数字的出现次数
            Map<Integer, Integer> count = new HashMap<>();
            // 记录以每个数字结尾的子序列的最小长度
            Map<Integer, PriorityQueue<Integer>> end = new HashMap<>();
            
            // 第一次遍历：统计每个数字的频率
            for (int num : nums) {
                count.put(num, count.getOrDefault(num, 0) + 1);
            }
            
            // 获取排序后的唯一数字列表
            List<Integer> sortedNums = new ArrayList<>(count.keySet());
            Collections.sort(sortedNums);
            
            // 第二次遍历：尝试将每个数字加入现有子序列或创建新子序列
            for (int num : sortedNums) {  // 按顺序处理数字，确保连续性
                // 处理每个数字的所有出现次数
                while (count.get(num) > 0) {
                    // 尝试将当前数字添加到以num-1结尾的最短子序列
                    if (end.containsKey(num - 1) && !end.get(num - 1).isEmpty()) {
                        // 获取并移除最短的子序列长度
                        int length = end.get(num - 1).poll();
                        // 将当前数字添加到该子序列，现在子序列以num结尾，长度+1
                        end.putIfAbsent(num, new PriorityQueue<>());
                        end.get(num).offer(length + 1);
                    } else {
                        // 创建新子序列
                        end.putIfAbsent(num, new PriorityQueue<>());
                        end.get(num).offer(1);
                    }
                    
                    count.put(num, count.get(num) - 1);
                }
            }
            
            // 验证所有子序列的长度是否都至少为3
            for (PriorityQueue<Integer> lengths : end.values()) {
                for (int length : lengths) {
                    if (length < 3) {
                        return false;
                    }
                }
            }
            
            return true;
        }
    }

    /**
     * 测试分割数组为连续子序列的函数
     */
    public static void testIsPossible() {
        // 测试用例1：基本用例 - 可以分割
        int[] nums1 = {1, 2, 3, 3, 4, 5};
        // 可以分割成 [1,2,3], [3,4,5]
        System.out.println("测试用例1：");
        System.out.println("数组: [1, 2, 3, 3, 4, 5]");
        Solution solution = new Solution();
        boolean result1 = solution.isPossible(nums1);
        System.out.println("结果: " + result1);
        System.out.println("预期结果: true, 测试" + (result1 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2：基本用例 - 可以分割
        int[] nums2 = {1, 2, 3, 3, 4, 4, 5, 5};
        // 可以分割成 [1,2,3,4,5], [3,4,5]
        AlternativeSolution solution2 = new AlternativeSolution();
        boolean result2 = solution2.isPossible(nums2);
        System.out.println("测试用例2：");
        System.out.println("数组: [1, 2, 3, 3, 4, 4, 5, 5]");
        System.out.println("结果: " + result2);
        System.out.println("预期结果: true, 测试" + (result2 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3：不可以分割
        int[] nums3 = {1, 2, 3, 4, 4, 5};
        // 无法分割，因为4,4,5不能形成长度为3的连续子序列
        OptimizedSolution solution3 = new OptimizedSolution();
        boolean result3 = solution3.isPossible(nums3);
        System.out.println("测试用例3：");
        System.out.println("数组: [1, 2, 3, 4, 4, 5]");
        System.out.println("结果: " + result3);
        System.out.println("预期结果: false, 测试" + (!result3 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例4：边界情况 - 数组长度小于3
        int[] nums4 = {1, 2};
        try {
            boolean result4 = solution.isPossible(nums4);
            System.out.println("测试用例4：");
            System.out.println("数组: [1, 2]");
            System.out.println("结果: " + result4);
            System.out.println("预期结果: false, 测试" + (!result4 ? "通过" : "失败"));
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例4：" + e.getMessage());
        }
        System.out.println();
        
        // 测试用例5：较长的数组
        int[] nums5 = {1, 2, 3, 4, 5, 5, 6, 7};
        boolean result5 = solution.isPossible(nums5);
        System.out.println("测试用例5：");
        System.out.println("数组: [1, 2, 3, 4, 5, 5, 6, 7]");
        System.out.println("结果: " + result5);
        System.out.println("预期结果: true, 测试" + (result5 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例6：复杂情况
        int[] nums6 = {1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7};
        boolean result6 = solution.isPossible(nums6);
        System.out.println("测试用例6：");
        System.out.println("数组: [1, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7]");
        System.out.println("结果: " + result6);
        System.out.println("预期结果: true, 测试" + (result6 ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例7：异常输入
        try {
            solution.isPossible(null);
            System.out.println("测试用例7：空数组异常处理 - 失败");
        } catch (IllegalArgumentException e) {
            System.out.println("测试用例7：空数组异常处理 - 通过");
        }
    }

    public static void main(String[] args) {
        testIsPossible();
    }
}