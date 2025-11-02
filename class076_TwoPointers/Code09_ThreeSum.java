package class050;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 三数之和
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k，
 * 还要满足 nums[i] + nums[j] + nums[k] == 0 。请你返回所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 * 
 * 示例：
 * 输入：nums = [-1,0,1,2,-1,-4]
 * 输出：[[-1,-1,2],[-1,0,1]]
 * 解释：
 * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
 * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
 * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
 * 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
 * 注意，输出的顺序和三元组的顺序并不重要。
 * 
 * 输入：nums = [0,1,1]
 * 输出：[]
 * 解释：唯一可能的三元组和不为 0 。
 * 
 * 输入：nums = [0,0,0]
 * 输出：[[0,0,0]]
 * 解释：唯一可能的三元组和为 0 。
 * 
 * 解题思路：
 * 1. 首先对数组进行排序，这样可以方便使用双指针法，并且便于去重。
 * 2. 遍历数组，固定第一个数 nums[i]，然后在剩余的数组中使用双指针法寻找两个数，
 *    使得这三个数的和为 0。
 * 3. 使用双指针法：左指针指向 i+1，右指针指向数组末尾。
 *    - 如果三数之和等于 0，则找到一个解，同时移动左右指针。
 *    - 如果三数之和小于 0，则左指针右移。
 *    - 如果三数之和大于 0，则右指针左移。
 * 4. 注意去重处理：
 *    - 固定的第一个数如果相同则跳过
 *    - 找到解后，左右指针需要跳过相同的数
 * 
 * 时间复杂度：O(n²) - 外层循环O(n)，内层双指针O(n)
 * 空间复杂度：O(1) - 不考虑返回结果的空间
 * 是否最优解：是 - 基于比较的算法下界为O(n²)，本算法已达到最优
 * 
 * 相关题目：
 * 1. LeetCode 1 - 两数之和（无序数组，使用哈希表）
 * 2. LeetCode 167 - 两数之和 II - 输入有序数组（排序+双指针）
 * 3. LeetCode 15 - 三数之和（当前题目）
 * 4. LeetCode 18 - 四数之和（排序+双指针）
 * 5. LeetCode 16 - 最接近的三数之和
 * 
 * 工程化考虑：
 * 1. 输入验证：检查数组是否为空或长度小于3
 * 2. 异常处理：处理各种边界情况
 * 3. 边界条件：处理全为0、全为正数、全为负数的情况
 * 
 * 语言特性差异：
 * Java: 使用ArrayList存储结果，需要导入相关包
 * C++: 可使用vector存储结果
 * Python: 可使用列表存储结果
 * 
 * 极端输入场景：
 * 1. 数组全为0
 * 2. 数组全为正数或全为负数
 * 3. 数组长度小于3
 * 4. 数组包含大量重复元素
 * 
 * 与机器学习等领域的联系：
 * 1. 在特征选择中，可能需要找到三个特征的组合满足特定条件
 * 2. 在推荐系统中，可能需要找到三个物品的组合满足用户偏好
 */
public class Code09_ThreeSum {

    /**
     * 查找所有和为0的不重复三元组
     * 
     * @param nums 整数数组
     * @return 所有和为0的不重复三元组列表
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        // 结果列表
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length < 3) {
            return result;
        }
        
        // 对数组进行排序，时间复杂度O(n log n)
        Arrays.sort(nums);
        
        // 遍历数组，固定第一个数
        for (int i = 0; i < nums.length - 2; i++) {
            // 如果当前数字大于0，则三数之和一定大于0，结束循环
            if (nums[i] > 0) {
                break;
            }
            
            // 跳过重复元素，避免出现重复解
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 使用双指针在剩余数组中寻找另外两个数
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    // 找到一个解
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
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
                } else if (sum < 0) {
                    // 和小于0，左指针右移
                    left++;
                } else {
                    // 和大于0，右指针左移
                    right--;
                }
            }
        }
        
        return result;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1: [-1,0,1,2,-1,-4] -> [[-1,-1,2],[-1,0,1]]
        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result1 = threeSum(nums1);
        System.out.println("Test 1: nums=" + Arrays.toString(nums1));
        System.out.println("Result: " + result1);
        
        // 测试用例2: [0,1,1] -> []
        int[] nums2 = {0, 1, 1};
        List<List<Integer>> result2 = threeSum(nums2);
        System.out.println("Test 2: nums=" + Arrays.toString(nums2));
        System.out.println("Result: " + result2);
        
        // 测试用例3: [0,0,0] -> [[0,0,0]]
        int[] nums3 = {0, 0, 0};
        List<List<Integer>> result3 = threeSum(nums3);
        System.out.println("Test 3: nums=" + Arrays.toString(nums3));
        System.out.println("Result: " + result3);
    }
}