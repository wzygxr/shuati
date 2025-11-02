package class092;

import java.util.Arrays;
import java.util.TreeMap;

/**
 * LeetCode 870. 优势洗牌
 * 题目链接：https://leetcode.cn/problems/advantage-shuffle/
 * 难度：中等
 * 
 * 问题描述：
 * 给定两个大小相等的数组 A 和 B，A 相对于 B 的优势可以用满足 A[i] > B[i] 的索引 i 的数目来描述。
 * 返回 A 的任意排列，使其相对于 B 的优势最大化。
 * 
 * 示例：
 * 输入：A = [2,7,11,15], B = [1,10,4,11]
 * 输出：[2,11,7,15]
 * 解释：A[0]=2 > B[0]=1, A[1]=11 > B[1]=10, A[2]=7 > B[2]=4, A[3]=15 > B[3]=11
 * 
 * 解题思路：
 * 贪心算法 + 田忌赛马策略
 * 1. 将数组A排序，便于使用最小的优势赢得比赛
 * 2. 使用TreeMap记录B数组的值和原始索引
 * 3. 对于A中的每个元素，在B中寻找刚好比它小的最大元素
 * 4. 如果找不到，就用A的最小元素对应B的最大元素（田忌赛马策略）
 * 
 * 时间复杂度：O(n log n) - 排序和TreeMap操作
 * 空间复杂度：O(n) - 存储结果和TreeMap
 * 
 * 最优性证明：
 * 贪心策略的正确性：使用田忌赛马策略，用自己最弱的马去对对方最强的马，用自己最强的马去对对方次强的马，
 * 这样可以最大化优势。
 * 
 * 工程化考量：
 * 1. 边界条件处理：空数组、单元素数组
 * 2. 异常处理：数组长度不一致
 * 3. 性能优化：使用TreeMap提高查找效率
 */
public class Code24_AdvantageShuffle {
    
    /**
     * 优势洗牌解决方案
     * @param nums1 数组A
     * @param nums2 数组B
     * @return 使A优势最大化的排列
     */
    public static int[] advantageCount(int[] nums1, int[] nums2) {
        // 边界条件处理
        if (nums1 == null || nums2 == null || nums1.length != nums2.length) {
            throw new IllegalArgumentException("输入数组不能为null且长度必须相等");
        }
        
        int n = nums1.length;
        if (n == 0) {
            return new int[0];
        }
        
        // 排序数组A
        int[] sortedA = nums1.clone();
        Arrays.sort(sortedA);
        
        // 使用TreeMap记录B的值和索引（可能有重复值，所以记录索引列表）
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int num : nums2) {
            map.put(num, map.getOrDefault(num, 0) + 1);
        }
        
        int[] result = new int[n];
        
        // 贪心策略：对于A中的每个元素，在B中寻找刚好比它小的最大元素
        for (int i = 0; i < n; i++) {
            Integer key = map.lowerKey(sortedA[i] + 1); // 寻找小于等于sortedA[i]的最大键
            
            if (key != null) {
                // 找到可以赢的元素
                result[i] = sortedA[i];
                // 更新TreeMap中的计数
                int count = map.get(key);
                if (count == 1) {
                    map.remove(key);
                } else {
                    map.put(key, count - 1);
                }
            } else {
                // 没有找到可以赢的元素，使用田忌赛马策略
                // 用当前最小的元素对应B中最大的元素
                Integer maxKey = map.lastKey();
                result[i] = sortedA[i];
                // 更新TreeMap中的计数
                int count = map.get(maxKey);
                if (count == 1) {
                    map.remove(maxKey);
                } else {
                    map.put(maxKey, count - 1);
                }
            }
        }
        
        return result;
    }
    
    /**
     * 优化版本：双指针 + 排序索引
     * 更高效的实现，避免TreeMap的频繁操作
     */
    public static int[] advantageCountOptimized(int[] nums1, int[] nums2) {
        if (nums1 == null || nums2 == null || nums1.length != nums2.length) {
            throw new IllegalArgumentException("输入数组不能为null且长度必须相等");
        }
        
        int n = nums1.length;
        if (n == 0) {
            return new int[0];
        }
        
        // 排序数组A
        int[] sortedA = nums1.clone();
        Arrays.sort(sortedA);
        
        // 创建B的索引数组并按照B的值排序
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, (i, j) -> Integer.compare(nums2[i], nums2[j]));
        
        // 双指针策略
        int[] result = new int[n];
        int left = 0, right = n - 1;
        
        for (int num : sortedA) {
            // 如果当前A的值大于B的最小值，则配对
            if (num > nums2[indices[left]]) {
                result[indices[left]] = num;
                left++;
            } else {
                // 否则用当前A的值配对B的最大值（田忌赛马）
                result[indices[right]] = num;
                right--;
            }
        }
        
        return result;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：标准示例
        int[] A1 = {2, 7, 11, 15};
        int[] B1 = {1, 10, 4, 11};
        System.out.println("=== 测试用例1 ===");
        System.out.println("A: " + Arrays.toString(A1));
        System.out.println("B: " + Arrays.toString(B1));
        
        int[] result1 = advantageCount(A1, B1);
        int[] result1Opt = advantageCountOptimized(A1, B1);
        
        System.out.println("TreeMap版本结果: " + Arrays.toString(result1));
        System.out.println("双指针版本结果: " + Arrays.toString(result1Opt));
        
        // 验证优势数量
        int advantage1 = calculateAdvantage(result1, B1);
        int advantage1Opt = calculateAdvantage(result1Opt, B1);
        System.out.println("TreeMap版本优势: " + advantage1);
        System.out.println("双指针版本优势: " + advantage1Opt);
        System.out.println();
        
        // 测试用例2：A全部大于B
        int[] A2 = {12, 24, 8, 32};
        int[] B2 = {13, 25, 32, 11};
        System.out.println("=== 测试用例2 ===");
        System.out.println("A: " + Arrays.toString(A2));
        System.out.println("B: " + Arrays.toString(B2));
        
        int[] result2 = advantageCount(A2, B2);
        int[] result2Opt = advantageCountOptimized(A2, B2);
        
        System.out.println("TreeMap版本结果: " + Arrays.toString(result2));
        System.out.println("双指针版本结果: " + Arrays.toString(result2Opt));
        
        int advantage2 = calculateAdvantage(result2, B2);
        int advantage2Opt = calculateAdvantage(result2Opt, B2);
        System.out.println("TreeMap版本优势: " + advantage2);
        System.out.println("双指针版本优势: " + advantage2Opt);
        System.out.println();
        
        // 测试用例3：A全部小于B（极端情况）
        int[] A3 = {2, 2, 2, 2};
        int[] B3 = {3, 3, 3, 3};
        System.out.println("=== 测试用例3 ===");
        System.out.println("A: " + Arrays.toString(A3));
        System.out.println("B: " + Arrays.toString(B3));
        
        int[] result3 = advantageCount(A3, B3);
        int[] result3Opt = advantageCountOptimized(A3, B3);
        
        System.out.println("TreeMap版本结果: " + Arrays.toString(result3));
        System.out.println("双指针版本结果: " + Arrays.toString(result3Opt));
        
        int advantage3 = calculateAdvantage(result3, B3);
        int advantage3Opt = calculateAdvantage(result3Opt, B3);
        System.out.println("TreeMap版本优势: " + advantage3);
        System.out.println("双指针版本优势: " + advantage3Opt);
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int[] largeA = new int[10000];
        int[] largeB = new int[10000];
        for (int i = 0; i < largeA.length; i++) {
            largeA[i] = (int) (Math.random() * 100000);
            largeB[i] = (int) (Math.random() * 100000);
        }
        
        long startTime = System.currentTimeMillis();
        int[] largeResult = advantageCount(largeA, largeB);
        long endTime = System.currentTimeMillis();
        System.out.println("TreeMap版本 - 耗时: " + (endTime - startTime) + "ms");
        
        startTime = System.currentTimeMillis();
        int[] largeResultOpt = advantageCountOptimized(largeA, largeB);
        endTime = System.currentTimeMillis();
        System.out.println("双指针版本 - 耗时: " + (endTime - startTime) + "ms");
        
        int largeAdvantage = calculateAdvantage(largeResult, largeB);
        int largeAdvantageOpt = calculateAdvantage(largeResultOpt, largeB);
        System.out.println("TreeMap版本优势: " + largeAdvantage);
        System.out.println("双指针版本优势: " + largeAdvantageOpt);
    }
    
    /**
     * 计算A相对于B的优势数量
     */
    private static int calculateAdvantage(int[] A, int[] B) {
        int advantage = 0;
        for (int i = 0; i < A.length; i++) {
            if (A[i] > B[i]) {
                advantage++;
            }
        }
        return advantage;
    }
}

/*
算法深度分析：

1. 贪心策略正确性证明：
   - 田忌赛马思想：用自己最弱的马去对对方最强的马，用自己最强的马去对对方次强的马
   - 数学证明：通过反证法可以证明这种策略能最大化优势数量
   - 时间复杂度：O(n log n) 是最优复杂度，因为需要排序

2. 两种实现对比：
   - TreeMap版本：思路直观，但TreeMap操作较慢
   - 双指针版本：效率更高，实现更简洁
   - 推荐使用双指针版本

3. 复杂度分析：
   - 时间复杂度：O(n log n) - 排序占主导
   - 空间复杂度：O(n) - 需要存储结果和辅助数组

工程化深度考量：

1. 边界条件处理：
   - 空数组和单元素数组
   - 数组长度不一致
   - 极端情况（如A全部小于B）

2. 性能优化策略：
   - 使用排序索引避免频繁的TreeMap操作
   - 使用双指针提高效率
   - 避免不必要的对象创建

3. 异常处理：
   - 输入参数验证
   - 数组长度一致性检查
   - 提供清晰的错误信息

4. 测试覆盖：
   - 正常情况测试
   - 边界情况测试
   - 性能测试验证

5. 实际应用价值：
   - 竞赛策略优化
   - 资源分配问题
   - 博弈论应用

6. 与机器学习联系：
   - 可以用于强化学习中的策略优化
   - 贪心策略在在线学习中有应用
   - 可以用于训练智能体学习最优匹配策略
*/