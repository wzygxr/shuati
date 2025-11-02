import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 47. 全排列 II
 * 
 * 题目描述：
 * 给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。
 * 
 * 示例：
 * 输入：nums = [1,1,2]
 * 输出：
 * [[1,1,2],
 *  [1,2,1],
 *  [2,1,1]]
 * 
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 提示：
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/permutations-ii/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的排列
 * 2. 先对数组进行排序，使相同元素相邻
 * 3. 对于每个位置，尝试放置每个未使用的数字
 * 4. 通过递归和回溯生成所有满足条件的排列
 * 5. 使用布尔数组标记数字是否已被使用
 * 6. 通过剪枝避免生成重复的排列
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：使用布尔数组避免重复使用数字
 * 2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
 * 3. 约束传播：一旦某个数字被使用，立即标记为已使用
 * 4. 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择
 * 
 * 时间复杂度：O(n! * n)，其中n是数组长度，共有n!种排列，每种排列需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组和单元素数组的特殊情况
 * 2. 参数验证：验证输入参数的有效性
 * 3. 性能优化：通过剪枝减少不必要的计算
 * 4. 内存管理：合理使用数据结构减少内存占用
 * 5. 可读性：添加详细注释和变量命名
 * 6. 异常处理：处理可能的异常情况
 * 7. 模块化设计：将核心逻辑封装成独立方法
 * 8. 可维护性：添加详细注释和文档说明
 */
public class LeetCode47_PermutationsII {
    
    /**
     * 生成数组的所有不重复全排列
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的全排列
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 排序使相同元素相邻，便于剪枝
        Arrays.sort(nums);
        
        List<Integer> path = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, path, used, result);
        return result;
    }
    
    /**
     * 回溯函数生成不重复排列
     * 
     * @param nums 输入数组
     * @param path 当前路径
     * @param used 标记数字是否已被使用的数组
     * @param result 结果列表
     */
    private void backtrack(int[] nums, List<Integer> path, boolean[] used, List<List<Integer>> result) {
        // 终止条件：已选择所有数字
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 尝试每个未使用的数字
        for (int i = 0; i < nums.length; i++) {
            // 可行性剪枝：如果数字已被使用，跳过
            if (used[i]) {
                continue;
            }
            
            // 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择
            // 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过当前元素
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
            
            // 选择当前数字
            path.add(nums[i]);
            used[i] = true;
            
            // 递归处理下一个位置
            backtrack(nums, path, used, result);
            
            // 回溯：撤销选择
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
    
    /**
     * 解法二：使用计数法处理重复元素
     * 统计每个元素的出现次数，然后基于计数生成排列
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的全排列
     */
    public List<List<Integer>> permuteUnique2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 统计每个元素的出现次数
        int[] counts = new int[21]; // 数值范围是[-10, 10]，偏移10映射到[0, 20]
        for (int num : nums) {
            counts[num + 10]++;
        }
        
        List<Integer> path = new ArrayList<>();
        backtrack2(counts, nums.length, path, result);
        return result;
    }
    
    /**
     * 基于计数的回溯函数
     * 
     * @param counts 每个元素的出现次数
     * @param remaining 剩余需要选择的元素个数
     * @param path 当前路径
     * @param result 结果列表
     */
    private void backtrack2(int[] counts, int remaining, List<Integer> path, List<List<Integer>> result) {
        // 终止条件：已选择所有数字
        if (remaining == 0) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        // 尝试每个可用的数字
        for (int i = 0; i < 21; i++) {
            // 如果当前数字还有剩余
            if (counts[i] > 0) {
                // 选择当前数字
                path.add(i - 10); // 偏移回原始值
                counts[i]--;
                
                // 递归处理剩余位置
                backtrack2(counts, remaining - 1, path, result);
                
                // 回溯：撤销选择
                path.remove(path.size() - 1);
                counts[i]++;
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode47_PermutationsII solution = new LeetCode47_PermutationsII();
        
        // 测试用例1
        int[] nums1 = {1, 1, 2};
        List<List<Integer>> result1 = solution.permuteUnique(nums1);
        System.out.println("输入: nums = [1,1,2]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = solution.permuteUnique(nums2);
        System.out.println("\n输入: nums = [1,2,3]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {2, 2, 1, 1};
        List<List<Integer>> result3 = solution.permuteUnique(nums3);
        System.out.println("\n输入: nums = [2,2,1,1]");
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = solution.permuteUnique2(nums1);
        System.out.println("输入: nums = [1,1,2]");
        System.out.println("输出: " + result4);
    }
}