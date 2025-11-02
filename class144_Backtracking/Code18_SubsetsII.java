import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 90. 子集 II
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
 * 
 * 示例：
 * 输入：nums = [1,2,2]
 * 输出：[[],[1],[1,2],[1,2,2],[2],[2,2]]
 * 
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * 
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/subsets-ii/
 * 
 * 算法思路：
 * 1. 先对数组进行排序，使相同元素相邻
 * 2. 使用回溯算法生成所有子集
 * 3. 在回溯过程中，对于重复元素，只选择第一个出现的，跳过后续相同的元素
 * 4. 这样可以避免生成重复的子集
 * 
 * 时间复杂度：O(n * 2^n)，其中n是数组长度，共有2^n个子集，每个子集需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 */
public class Code18_SubsetsII {

    /**
     * 生成包含重复元素的数组的所有不重复子集
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的子集
     */
    public static List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 先排序，使相同元素相邻
        Arrays.sort(nums);
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    /**
     * 回溯函数生成子集
     * 
     * @param nums 输入数组
     * @param start 当前起始位置
     * @param path 当前路径
     * @param result 结果列表
     */
    private static void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 每一步都添加到结果中
        result.add(new ArrayList<>(path));
        
        // 从start开始遍历，避免重复
        for (int i = start; i < nums.length; i++) {
            // 跳过重复元素：如果当前元素与前一个相同且不是第一个出现的，则跳过
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            path.add(nums[i]);  // 选择当前元素
            backtrack(nums, i + 1, path, result);  // 递归处理下一个元素
            path.remove(path.size() - 1);  // 撤销选择
        }
    }

    /**
     * 解法二：使用计数法处理重复元素
     * 对于重复元素，我们可以选择0个、1个、2个...直到所有重复元素
     * 
     * @param nums 输入数组
     * @return 所有不重复的子集
     */
    public static List<List<Integer>> subsetsWithDup2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        backtrack2(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private static void backtrack2(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        if (start == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 统计当前元素出现的次数
        int count = 1;
        int i = start + 1;
        while (i < nums.length && nums[i] == nums[start]) {
            count++;
            i++;
        }
        
        // 对于当前元素，可以选择0个、1个、2个...count个
        for (int j = 0; j <= count; j++) {
            // 添加j个当前元素
            for (int k = 0; k < j; k++) {
                path.add(nums[start]);
            }
            
            backtrack2(nums, start + count, path, result);
            
            // 回溯，移除添加的元素
            for (int k = 0; k < j; k++) {
                path.remove(path.size() - 1);
            }
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 2};
        List<List<Integer>> result1 = subsetsWithDup(nums1);
        System.out.println("输入: nums = [1, 2, 2]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {0};
        List<List<Integer>> result2 = subsetsWithDup(nums2);
        System.out.println("\n输入: nums = [0]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {1, 1, 2, 2};
        List<List<Integer>> result3 = subsetsWithDup(nums3);
        System.out.println("\n输入: nums = [1, 1, 2, 2]");
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = subsetsWithDup2(nums1);
        System.out.println("输入: nums = [1, 2, 2]");
        System.out.println("输出: " + result4);
    }
}