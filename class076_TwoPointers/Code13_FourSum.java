package class050;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 四数之和
 * 
 * 题目描述：
 * 给你一个由 n 个整数组成的数组 nums ，和一个目标值 target 。
 * 请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b], nums[c], nums[d]] ：
 * 1. 0 <= a, b, c, d < n
 * 2. a、b、c 和 d 互不相同
 * 3. nums[a] + nums[b] + nums[c] + nums[d] == target
 * 
 * 示例：
 * 输入：nums = [1,0,-1,0,-2,2], target = 0
 * 输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 * 
 * 输入：nums = [2,2,2,2,2], target = 8
 * 输出：[[2,2,2,2]]
 * 
 * 解题思路：
 * 1. 首先对数组进行排序，这样可以方便使用双指针法，并且便于去重。
 * 2. 使用两层循环固定前两个数，然后在剩余的数组中使用双指针法寻找另外两个数，
 *    使得这四个数的和等于目标值。
 * 3. 使用双指针法：左指针指向第二个固定数的下一个位置，右指针指向数组末尾。
 *    - 如果四数之和等于目标值，则找到一个解，同时移动左右指针。
 *    - 如果四数之和小于目标值，则左指针右移。
 *    - 如果四数之和大于目标值，则右指针左移。
 * 4. 注意去重处理：
 *    - 第一层循环固定的第一个数如果相同则跳过
 *    - 第二层循环固定的第二个数如果相同则跳过
 *    - 找到解后，左右指针需要跳过相同的数
 * 
 * 时间复杂度：O(n³) - 两层循环O(n²)，内层双指针O(n)
 * 空间复杂度：O(1) - 不考虑返回结果的空间
 * 是否最优解：是 - 基于比较的算法下界为O(n³)，本算法已达到最优
 * 
 * 相关题目：
 * 1. LeetCode 1 - 两数之和（无序数组，使用哈希表）
 * 2. LeetCode 167 - 两数之和 II - 输入有序数组（排序+双指针）
 * 3. LeetCode 15 - 三数之和（排序+双指针）
 * 4. LeetCode 18 - 四数之和（当前题目）
 * 
 * 工程化考虑：
 * 1. 输入验证：检查数组是否为空或长度小于4
 * 2. 异常处理：处理各种边界情况
 * 3. 边界条件：处理全为相同元素的情况
 * 
 * 语言特性差异：
 * Java: 使用ArrayList存储结果，需要导入相关包
 * C++: 可使用vector存储结果
 * Python: 可使用列表存储结果
 * 
 * 极端输入场景：
 * 1. 数组全为相同元素
 * 2. 数组长度小于4
 * 3. 数组包含大量重复元素
 * 
 * 与机器学习等领域的联系：
 * 1. 在特征选择中，可能需要找到四个特征的组合满足特定条件
 * 2. 在推荐系统中，可能需要找到四个物品的组合满足用户偏好
 */
public class Code13_FourSum {

    /**
     * 查找所有和为目标值的不重复四元组
     * 
     * @param nums   整数数组
     * @param target 目标值
     * @return 所有和为目标值的不重复四元组列表
     */
    public static List<List<Integer>> fourSum(int[] nums, int target) {
        // 结果列表
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length < 4) {
            return result;
        }
        
        // 对数组进行排序，时间复杂度O(n log n)
        Arrays.sort(nums);
        
        int n = nums.length;
        
        // 遍历数组，固定第一个数
        for (int i = 0; i < n - 3; i++) {
            // 跳过重复元素，避免出现重复解
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 遍历数组，固定第二个数
            for (int j = i + 1; j < n - 2; j++) {
                // 跳过重复元素，避免出现重复解
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                
                // 使用双指针在剩余数组中寻找另外两个数
                int left = j + 1;
                int right = n - 1;
                
                while (left < right) {
                    long sum = (long) nums[i] + nums[j] + nums[left] + nums[right];
                    
                    if (sum == target) {
                        // 找到一个解
                        result.add(Arrays.asList(nums[i], nums[j], nums[left], nums[right]));
                        
                        // 跳过重复元素
                        while (left < right && nums[left] == nums[left + 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right - 1]) {
                            right--;
                        }
                        
                        // 移动指针继续寻找
                        left++;
                        right--;
                    } else if (sum < target) {
                        // 和小于目标值，左指针右移
                        left++;
                    } else {
                        // 和大于目标值，右指针左移
                        right--;
                    }
                }
            }
        }
        
        return result;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1: [1,0,-1,0,-2,2], target = 0 -> [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
        int[] nums1 = {1, 0, -1, 0, -2, 2};
        int target1 = 0;
        List<List<Integer>> result1 = fourSum(nums1, target1);
        System.out.println("Test 1: nums=" + Arrays.toString(nums1) + ", target=" + target1);
        System.out.println("Result: " + result1);
        
        // 测试用例2: [2,2,2,2,2], target = 8 -> [[2,2,2,2]]
        int[] nums2 = {2, 2, 2, 2, 2};
        int target2 = 8;
        List<List<Integer>> result2 = fourSum(nums2, target2);
        System.out.println("Test 2: nums=" + Arrays.toString(nums2) + ", target=" + target2);
        System.out.println("Result: " + result2);
    }
}