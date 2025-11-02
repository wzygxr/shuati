package class050;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * LeetCode 15. 三数之和 (3Sum)
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，
 * 同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你找出所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * 
 * 示例1:
 * 输入: nums = [-1,0,1,2,-1,-4]
 * 输出: [[-1,-1,2],[-1,0,1]]
 * 
 * 示例2:
 * 输入: nums = [0,1,1]
 * 输出: []
 * 
 * 示例3:
 * 输入: nums = [0,0,0]
 * 输出: [[0,0,0]]
 * 
 * 提示:
 * 3 <= nums.length <= 3000
 * -10^5 <= nums[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/3sum/
 * 
 * 解题思路:
 * 这道题可以使用排序 + 双指针的方法来解决：
 * 
 * 方法一（暴力解法）：
 * 使用三层嵌套循环遍历所有可能的三元组，找出和为0的三元组。使用HashSet去重。
 * 时间复杂度：O(n^3)，空间复杂度：O(n)
 * 
 * 方法二（排序 + 双指针）：
 * 1. 先对数组进行排序
 * 2. 遍历数组，对于每个元素nums[i]，使用双指针left和right分别指向i+1和数组末尾
 * 3. 计算当前三个数的和：sum = nums[i] + nums[left] + nums[right]
 * 4. 如果sum == 0，将这三个数加入结果集，并移动left和right指针
 *    - 为了避免重复，需要跳过相同的元素
 * 5. 如果sum < 0，说明需要增大和，移动left指针
 * 6. 如果sum > 0，说明需要减小和，移动right指针
 * 7. 重复步骤3-6，直到left >= right
 * 时间复杂度：O(n^2)，空间复杂度：O(1)或O(log n)（排序的空间复杂度）
 * 
 * 最优解是方法二，时间复杂度 O(n^2)，空间复杂度 O(1)或O(log n)。
 */

public class Code28_3Sum {
    
    /**
     * 解法一: 暴力解法（不推荐，会超时）
     * 
     * @param nums 输入数组
     * @return 所有和为0且不重复的三元组
     * @throws IllegalArgumentException 如果输入数组为null或长度小于3
     */
    public static List<List<Integer>> threeSumBruteForce(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (nums.length < 3) {
            throw new IllegalArgumentException("输入数组长度必须至少为3");
        }
        
        Set<List<Integer>> resultSet = new HashSet<>();
        int n = nums.length;
        
        // 三层循环遍历所有可能的三元组
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (nums[i] + nums[j] + nums[k] == 0) {
                        // 将三元组排序后加入结果集，利用Set去重
                        List<Integer> triplet = Arrays.asList(
                            Math.min(Math.min(nums[i], nums[j]), nums[k]),
                            Math.max(Math.min(nums[i], nums[j]), Math.min(Math.max(nums[i], nums[j]), nums[k])),
                            Math.max(Math.max(nums[i], nums[j]), nums[k])
                        );
                        resultSet.add(triplet);
                    }
                }
            }
        }
        
        return new ArrayList<>(resultSet);
    }
    
    /**
     * 解法二: 排序 + 双指针（最优解）
     * 
     * @param nums 输入数组
     * @return 所有和为0且不重复的三元组
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (nums.length < 3) {
            throw new IllegalArgumentException("输入数组长度必须至少为3");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        
        // 对数组进行排序
        Arrays.sort(nums);
        
        // 遍历数组，固定第一个元素
        for (int i = 0; i < n; i++) {
            // 跳过重复的第一个元素，避免产生重复的三元组
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 如果当前元素已经大于0，由于数组已排序，后面的元素都大于0，三数之和不可能为0
            if (nums[i] > 0) {
                break;
            }
            
            // 初始化双指针
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    // 找到一个三元组
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // 跳过重复的左指针元素
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    // 跳过重复的右指针元素
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    
                    // 移动两个指针
                    left++;
                    right--;
                } else if (sum < 0) {
                    // 和小于0，需要增大和，移动左指针
                    left++;
                } else {
                    // 和大于0，需要减小和，移动右指针
                    right--;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 解法三: 优化的双指针实现
     * 
     * @param nums 输入数组
     * @return 所有和为0且不重复的三元组
     */
    public static List<List<Integer>> threeSumOptimized(int[] nums) {
        // 参数校验
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (nums.length < 3) {
            throw new IllegalArgumentException("输入数组长度必须至少为3");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;
        
        // 对数组进行排序
        Arrays.sort(nums);
        
        // 遍历数组，固定第一个元素
        for (int i = 0; i < n - 2; i++) {
            // 跳过重复的第一个元素，避免产生重复的三元组
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 剪枝：如果当前元素已经大于0，三数之和不可能为0
            if (nums[i] > 0) {
                break;
            }
            
            // 剪枝：如果当前元素和最大的两个元素之和仍小于0，跳过
            if (nums[i] + nums[n - 1] + nums[n - 2] < 0) {
                continue;
            }
            
            // 初始化双指针
            int left = i + 1;
            int right = n - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    // 找到一个三元组
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // 跳过重复的左指针元素
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    // 跳过重复的右指针元素
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    
                    // 移动两个指针
                    left++;
                    right--;
                } else if (sum < 0) {
                    // 和小于0，需要增大和，移动左指针
                    left++;
                } else {
                    // 和大于0，需要减小和，移动右指针
                    right--;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 打印三元组列表
     */
    public static void printTriplets(List<List<Integer>> triplets) {
        System.out.print("[");
        for (int i = 0; i < triplets.size(); i++) {
            List<Integer> triplet = triplets.get(i);
            System.out.print("[");
            for (int j = 0; j < triplet.size(); j++) {
                System.out.print(triplet.get(j));
                if (j < triplet.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]");
            if (i < triplets.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
    
    /**
     * 测试函数
     */
    public static void test() {
        // 测试用例1
        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> expected1 = Arrays.asList(
            Arrays.asList(-1, -1, 2),
            Arrays.asList(-1, 0, 1)
        );
        System.out.println("测试用例1:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(nums1));
        List<List<Integer>> result1 = threeSum(nums1);
        System.out.print("结果: ");
        printTriplets(result1);
        System.out.print("期望: ");
        printTriplets(expected1);
        // 由于三元组的顺序可能不同，这里不做严格的相等性验证
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {0, 1, 1};
        List<List<Integer>> expected2 = new ArrayList<>();
        System.out.println("测试用例2:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(nums2));
        List<List<Integer>> result2 = threeSum(nums2);
        System.out.print("结果: ");
        printTriplets(result2);
        System.out.print("期望: ");
        printTriplets(expected2);
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {0, 0, 0};
        List<List<Integer>> expected3 = Arrays.asList(
            Arrays.asList(0, 0, 0)
        );
        System.out.println("测试用例3:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(nums3));
        List<List<Integer>> result3 = threeSum(nums3);
        System.out.print("结果: ");
        printTriplets(result3);
        System.out.print("期望: ");
        printTriplets(expected3);
        System.out.println();
        
        // 测试用例4 - 边界情况：多个重复元素
        int[] nums4 = {-2, 0, 0, 2, 2};
        List<List<Integer>> expected4 = Arrays.asList(
            Arrays.asList(-2, 0, 2)
        );
        System.out.println("测试用例4（多个重复元素）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(nums4));
        List<List<Integer>> result4 = threeSum(nums4);
        System.out.print("结果: ");
        printTriplets(result4);
        System.out.print("期望: ");
        printTriplets(expected4);
        System.out.println();
        
        // 测试用例5 - 边界情况：所有元素都为负数
        int[] nums5 = {-1, -2, -3, -4, -5};
        List<List<Integer>> expected5 = new ArrayList<>();
        System.out.println("测试用例5（全负数）:");
        System.out.print("输入数组: ");
        System.out.println(Arrays.toString(nums5));
        List<List<Integer>> result5 = threeSum(nums5);
        System.out.print("结果: ");
        printTriplets(result5);
        System.out.print("期望: ");
        printTriplets(expected5);
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        // 创建一个中等大小的数组进行性能测试
        int size = 1000;
        int[] mediumArray = new int[size];
        
        // 生成测试数据：包含正负数和零
        for (int i = 0; i < size; i++) {
            mediumArray[i] = (i % 100) - 50;  // -50 到 49
        }
        
        // 测试解法二的性能
        long startTime = System.nanoTime();
        List<List<Integer>> result2 = threeSum(mediumArray);
        long endTime = System.nanoTime();
        long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法二（双指针）耗时: " + duration + "ms, 找到的三元组数量: " + result2.size());
        
        // 测试解法三的性能
        startTime = System.nanoTime();
        List<List<Integer>> result3 = threeSumOptimized(mediumArray);
        endTime = System.nanoTime();
        duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
        System.out.println("解法三（优化双指针）耗时: " + duration + "ms, 找到的三元组数量: " + result3.size());
        
        // 验证两种解法结果一致（这里只比较数量，不比较具体内容）
        boolean resultsConsistent = (result2.size() == result3.size());
        System.out.println("所有解法结果数量一致: " + resultsConsistent);
    }
    
    /**
     * 边界条件测试
     */
    public static void boundaryTest() {
        try {
            // 测试null输入
            threeSum(null);
            System.out.println("边界测试失败：null输入没有抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过：null输入正确抛出异常");
        }
        
        try {
            // 测试长度为2的输入
            threeSum(new int[]{1, 2});
            System.out.println("边界测试失败：长度为2的输入没有抛出异常");
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试通过：长度为2的输入正确抛出异常");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试用例 ===");
        test();
        
        System.out.println("=== 性能测试 ===");
        performanceTest();
        
        System.out.println("=== 边界条件测试 ===");
        boundaryTest();
    }
}