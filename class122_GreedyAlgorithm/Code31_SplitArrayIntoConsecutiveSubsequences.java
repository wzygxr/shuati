package class091;

import java.util.*;

/**
 * 划分数组为连续子序列
 * 
 * 题目描述：
 * 给你一个按升序排序的整数数组 num（可能包含重复数字），请你将它们分割成一个或多个长度至少为 3 的子序列，
 * 其中每个子序列都由连续整数组成。如果可以完成上述分割，则返回 true ；否则，返回 false 。
 * 
 * 来源：LeetCode 659
 * 链接：https://leetcode.cn/problems/split-array-into-consecutive-subsequences/
 * 
 * 算法思路：
 * 使用贪心算法 + 哈希表：
 * 1. 使用两个哈希表：
 *    - freq: 记录每个数字的剩余频率
 *    - need: 记录需要某个数字来延续已有子序列的数量
 * 2. 遍历数组中的每个数字：
 *    - 如果当前数字可以延续某个已有子序列（need中存在），则延续该子序列
 *    - 否则，尝试以当前数字为起点创建新的子序列（需要检查后续两个数字是否存在）
 *    - 如果既不能延续也不能创建新序列，返回false
 * 
 * 时间复杂度：O(n) - 只需要遍历一次数组
 * 空间复杂度：O(n) - 哈希表存储频率和需求信息
 * 
 * 关键点分析：
 * - 贪心策略：优先延续已有子序列，避免创建过多短序列
 * - 哈希表优化：快速查询频率和需求信息
 * - 边界处理：处理重复数字和边界情况
 * 
 * 工程化考量：
 * - 输入验证：检查数组是否为空或null
 * - 性能优化：使用HashMap而非TreeMap
 * - 可读性：清晰的变量命名和注释
 */
public class Code31_SplitArrayIntoConsecutiveSubsequences {
    
    /**
     * 判断是否能将数组划分为连续子序列
     * 
     * @param nums 输入数组
     * @return 是否能划分
     */
    public static boolean isPossible(int[] nums) {
        // 输入验证
        if (nums == null || nums.length == 0) {
            return false;
        }
        if (nums.length < 3) {
            return false;
        }
        
        // 统计每个数字的频率
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        // 记录需要某个数字来延续子序列的数量
        Map<Integer, Integer> need = new HashMap<>();
        
        for (int num : nums) {
            // 如果当前数字已经被用完，跳过
            if (freq.get(num) == 0) {
                continue;
            }
            
            // 优先尝试延续已有子序列
            if (need.getOrDefault(num, 0) > 0) {
                // 延续子序列
                freq.put(num, freq.get(num) - 1);
                need.put(num, need.get(num) - 1);
                // 需要下一个数字
                need.put(num + 1, need.getOrDefault(num + 1, 0) + 1);
            } 
            // 尝试创建新的子序列（需要至少3个连续数字）
            else if (freq.getOrDefault(num + 1, 0) > 0 && freq.getOrDefault(num + 2, 0) > 0) {
                // 创建新子序列
                freq.put(num, freq.get(num) - 1);
                freq.put(num + 1, freq.get(num + 1) - 1);
                freq.put(num + 2, freq.get(num + 2) - 1);
                // 需要下一个数字来延续
                need.put(num + 3, need.getOrDefault(num + 3, 0) + 1);
            } 
            // 既不能延续也不能创建新序列
            else {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 另一种实现：使用优先队列的解法
     * 时间复杂度：O(n * logn)
     * 空间复杂度：O(n)
     */
    public static boolean isPossibleWithPQ(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false;
        }
        
        // 使用最小堆存储每个子序列的结束时间
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        
        for (int num : nums) {
            // 移除所有结束时间小于当前数字-1的子序列
            while (!heap.isEmpty() && heap.peek() < num - 1) {
                if (heap.peek() < num - 1) {
                    int end = heap.poll();
                    // 如果子序列长度小于3，返回false
                    if (num - end - 1 < 3) {
                        return false;
                    }
                }
            }
            
            // 如果堆为空或当前数字可以延续最短的子序列
            if (heap.isEmpty() || heap.peek() >= num) {
                heap.offer(num);
            } 
            // 延续已有的子序列
            else {
                int end = heap.poll();
                heap.offer(num);
                // 检查子序列长度
                if (num - end + 1 < 3) {
                    return false;
                }
            }
        }
        
        // 检查所有剩余子序列的长度
        while (!heap.isEmpty()) {
            int end = heap.poll();
            if (nums[nums.length - 1] - end + 1 < 3) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 简化版的贪心算法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public static boolean isPossibleSimple(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false;
        }
        
        Map<Integer, Integer> freq = new HashMap<>();
        Map<Integer, Integer> appendFreq = new HashMap<>();
        
        for (int num : nums) {
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        
        for (int num : nums) {
            if (freq.get(num) == 0) {
                continue;
            }
            
            if (appendFreq.getOrDefault(num, 0) > 0) {
                // 延续子序列
                appendFreq.put(num, appendFreq.get(num) - 1);
                appendFreq.put(num + 1, appendFreq.getOrDefault(num + 1, 0) + 1);
                freq.put(num, freq.get(num) - 1);
            } else if (freq.getOrDefault(num + 1, 0) > 0 && freq.getOrDefault(num + 2, 0) > 0) {
                // 创建新子序列
                freq.put(num, freq.get(num) - 1);
                freq.put(num + 1, freq.get(num + 1) - 1);
                freq.put(num + 2, freq.get(num + 2) - 1);
                appendFreq.put(num + 3, appendFreq.getOrDefault(num + 3, 0) + 1);
            } else {
                return false;
            }
        }
        
        return true;
    }
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1: [1,2,3,3,4,5] -> true
        // 解释: [1,2,3] 和 [3,4,5]
        int[] nums1 = {1,2,3,3,4,5};
        System.out.println("测试用例1: " + Arrays.toString(nums1));
        System.out.println("方法1结果: " + isPossible(nums1)); // true
        System.out.println("方法2结果: " + isPossibleWithPQ(nums1)); // true
        System.out.println("方法3结果: " + isPossibleSimple(nums1)); // true
        
        // 测试用例2: [1,2,3,3,4,4,5,5] -> true
        // 解释: [1,2,3,4,5] 和 [3,4,5]
        int[] nums2 = {1,2,3,3,4,4,5,5};
        System.out.println("\n测试用例2: " + Arrays.toString(nums2));
        System.out.println("方法1结果: " + isPossible(nums2)); // true
        System.out.println("方法2结果: " + isPossibleWithPQ(nums2)); // true
        System.out.println("方法3结果: " + isPossibleSimple(nums2)); // true
        
        // 测试用例3: [1,2,3,4,4,5] -> false
        // 解释: 无法分割成两个长度至少为3的子序列
        int[] nums3 = {1,2,3,4,4,5};
        System.out.println("\n测试用例3: " + Arrays.toString(nums3));
        System.out.println("方法1结果: " + isPossible(nums3)); // false
        System.out.println("方法2结果: " + isPossibleWithPQ(nums3)); // false
        System.out.println("方法3结果: " + isPossibleSimple(nums3)); // false
        
        // 测试用例4: [1,2,3] -> true
        int[] nums4 = {1,2,3};
        System.out.println("\n测试用例4: " + Arrays.toString(nums4));
        System.out.println("方法1结果: " + isPossible(nums4)); // true
        System.out.println("方法2结果: " + isPossibleWithPQ(nums4)); // true
        System.out.println("方法3结果: " + isPossibleSimple(nums4)); // true
        
        // 测试用例5: [1,2,2,3,3,4,4,5,5,6] -> true
        int[] nums5 = {1,2,2,3,3,4,4,5,5,6};
        System.out.println("\n测试用例5: " + Arrays.toString(nums5));
        System.out.println("方法1结果: " + isPossible(nums5)); // true
        System.out.println("方法2结果: " + isPossibleWithPQ(nums5)); // true
        System.out.println("方法3结果: " + isPossibleSimple(nums5)); // true
        
        // 边界测试：空数组
        int[] nums6 = {};
        System.out.println("\n测试用例6: " + Arrays.toString(nums6));
        System.out.println("方法1结果: " + isPossible(nums6)); // false
        System.out.println("方法2结果: " + isPossibleWithPQ(nums6)); // false
        System.out.println("方法3结果: " + isPossibleSimple(nums6)); // false
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        // 生成大规模测试数据
        int[] largeNums = new int[10000];
        Random random = new Random();
        for (int i = 0; i < largeNums.length; i++) {
            largeNums[i] = random.nextInt(1000);
        }
        Arrays.sort(largeNums);
        
        System.out.println("\n=== 性能测试 ===");
        
        long startTime1 = System.currentTimeMillis();
        boolean result1 = isPossible(largeNums);
        long endTime1 = System.currentTimeMillis();
        System.out.println("方法1执行时间: " + (endTime1 - startTime1) + "ms");
        System.out.println("结果: " + result1);
        
        long startTime2 = System.currentTimeMillis();
        boolean result2 = isPossibleWithPQ(largeNums);
        long endTime2 = System.currentTimeMillis();
        System.out.println("方法2执行时间: " + (endTime2 - startTime2) + "ms");
        System.out.println("结果: " + result2);
        
        long startTime3 = System.currentTimeMillis();
        boolean result3 = isPossibleSimple(largeNums);
        long endTime3 = System.currentTimeMillis();
        System.out.println("方法3执行时间: " + (endTime3 - startTime3) + "ms");
        System.out.println("结果: " + result3);
    }
    
    /**
     * 算法复杂度分析
     */
    public static void analyzeComplexity() {
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("方法1（贪心+哈希表）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 统计频率: O(n)");
        System.out.println("  - 遍历处理: O(n)");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 频率哈希表: O(n)");
        System.out.println("  - 需求哈希表: O(n)");
        
        System.out.println("\n方法2（优先队列）:");
        System.out.println("- 时间复杂度: O(n * logn)");
        System.out.println("  - 堆操作: O(n * logn)");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 优先队列: O(n)");
        
        System.out.println("\n方法3（简化版）:");
        System.out.println("- 时间复杂度: O(n)");
        System.out.println("  - 统计频率: O(n)");
        System.out.println("  - 遍历处理: O(n)");
        System.out.println("- 空间复杂度: O(n)");
        System.out.println("  - 哈希表: O(n)");
        
        System.out.println("\n贪心策略证明:");
        System.out.println("1. 优先延续已有子序列可以避免创建过多短序列");
        System.out.println("2. 创建新序列时要求后续两个数字存在确保序列长度");
        System.out.println("3. 数学归纳法证明贪心选择性质");
        
        System.out.println("\n工程化考量:");
        System.out.println("1. 输入验证：处理空数组和边界情况");
        System.out.println("2. 性能优化：选择合适的哈希表实现");
        System.out.println("3. 可读性：清晰的算法逻辑和注释");
        System.out.println("4. 测试覆盖：全面的测试用例设计");
    }
}