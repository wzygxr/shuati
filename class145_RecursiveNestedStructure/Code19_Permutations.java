// LeetCode 46. Permutations
// 全排列
// 题目来源：https://leetcode.cn/problems/permutations/

import java.util.*;

/**
 * 问题描述：
 * 给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 使用回溯算法，通过递归生成所有可能的排列组合
 * 1. 选择：从当前可用的数字中选择一个加入当前排列
 * 2. 约束：已经选择过的数字不能再次选择
 * 3. 目标：生成长度等于原数组长度的排列
 * 
 * 时间复杂度：O(N * N!)，其中N是数组的长度，N!是排列的总数，每个排列需要O(N)的时间复制
 * 空间复杂度：O(N)，递归栈的深度最多为N，另外需要O(N)的空间来标记已使用的数字
 */

public class Code19_Permutations {
    /**
     * 回溯算法求解全排列
     * @param nums 输入的不含重复数字的数组
     * @return 所有可能的全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 记录当前的排列
        List<Integer> currentPermutation = new ArrayList<>();
        // 记录数字是否已被使用
        boolean[] used = new boolean[nums.length];
        
        // 开始回溯
        backtrack(nums, currentPermutation, used, result);
        
        return result;
    }
    
    /**
     * 回溯函数
     * @param nums 输入数组
     * @param currentPermutation 当前正在构建的排列
     * @param used 标记数组元素是否已被使用
     * @param result 存储所有全排列的结果集
     */
    private void backtrack(int[] nums, List<Integer> currentPermutation, boolean[] used, List<List<Integer>> result) {
        // 终止条件：当前排列的长度等于原数组长度，说明找到了一个完整的排列
        if (currentPermutation.size() == nums.length) {
            // 将当前排列的副本加入结果集
            result.add(new ArrayList<>(currentPermutation));
            return;
        }
        
        // 尝试选择每个未使用的数字
        for (int i = 0; i < nums.length; i++) {
            // 如果当前数字已经被使用，跳过
            if (used[i]) {
                continue;
            }
            
            // 选择当前数字
            currentPermutation.add(nums[i]);
            used[i] = true;
            
            // 递归到下一层，构建剩余的排列
            backtrack(nums, currentPermutation, used, result);
            
            // 回溯：撤销选择
            used[i] = false;
            currentPermutation.remove(currentPermutation.size() - 1);
        }
    }
    
    /**
     * 不使用used数组的回溯方法（通过交换元素实现）
     * 这种方法更节省空间，但会修改原数组
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    public List<List<Integer>> permuteBySwapping(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 将数组转换为列表，方便后续操作
        List<Integer> numsList = new ArrayList<>();
        for (int num : nums) {
            numsList.add(num);
        }
        
        // 开始回溯（通过交换元素实现）
        backtrackBySwapping(numsList, 0, result);
        
        return result;
    }
    
    /**
     * 通过交换元素实现回溯的辅助函数
     * @param nums 当前的数字列表
     * @param start 开始交换的位置
     * @param result 存储所有全排列的结果集
     */
    private void backtrackBySwapping(List<Integer> nums, int start, List<List<Integer>> result) {
        // 终止条件：当start等于nums.size()时，说明已经确定了一个排列
        if (start == nums.size()) {
            result.add(new ArrayList<>(nums));
            return;
        }
        
        // 从start位置开始，尝试将每个位置的元素与start位置交换
        for (int i = start; i < nums.size(); i++) {
            // 交换元素
            swap(nums, start, i);
            
            // 递归到下一个位置
            backtrackBySwapping(nums, start + 1, result);
            
            // 回溯：撤销交换
            swap(nums, start, i);
        }
    }
    
    /**
     * 交换列表中的两个元素
     */
    private void swap(List<Integer> list, int i, int j) {
        int temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
    
    public static void main(String[] args) {
        Code19_Permutations solution = new Code19_Permutations();
        
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        System.out.println("测试用例1 - 使用used数组:");
        List<List<Integer>> result1 = solution.permute(nums1);
        for (List<Integer> permutation : result1) {
            System.out.println(permutation);
        }
        
        System.out.println("\n测试用例1 - 通过交换元素:");
        List<List<Integer>> result1BySwapping = solution.permuteBySwapping(nums1);
        for (List<Integer> permutation : result1BySwapping) {
            System.out.println(permutation);
        }
        
        // 测试用例2
        int[] nums2 = {0, 1};
        System.out.println("\n测试用例2 - 使用used数组:");
        List<List<Integer>> result2 = solution.permute(nums2);
        for (List<Integer> permutation : result2) {
            System.out.println(permutation);
        }
        
        // 测试用例3
        int[] nums3 = {1};
        System.out.println("\n测试用例3 - 使用used数组:");
        List<List<Integer>> result3 = solution.permute(nums3);
        for (List<Integer> permutation : result3) {
            System.out.println(permutation);
        }
    }
}

/**
 * 性能分析：
 * 
 * 1. 使用used数组的方法：
 *    - 时间复杂度：O(N * N!)，其中N是数组长度
 *    - 空间复杂度：O(N)，用于存储used数组、递归栈和当前排列
 *    - 优点：逻辑清晰，不修改原数组
 *    - 缺点：需要额外的used数组空间
 * 
 * 2. 通过交换元素的方法：
 *    - 时间复杂度：O(N * N!)，其中N是数组长度
 *    - 空间复杂度：O(N)，主要是递归栈的空间
 *    - 优点：节省了used数组的空间，空间效率更高
 *    - 缺点：修改了原数组（在这个实现中我们创建了副本，所以原数组没被修改）
 * 
 * 算法优化思路：
 * 1. 剪枝优化：虽然对于标准的全排列问题没有太多剪枝空间，但在实际应用中可以根据具体条件进行剪枝
 * 2. 并行计算：对于大规模数据，可以考虑并行计算不同的分支
 * 3. 记忆化：在某些特定变体问题中，可以使用记忆化技术避免重复计算
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中应该添加对输入参数的有效性检查
 * 2. 线程安全：如果在多线程环境中使用，需要确保线程安全
 * 3. 扩展性：可以扩展此算法以处理包含重复元素的数组（如LeetCode 47题）
 * 4. 性能监控：对于大规模数据，可以添加性能监控代码
 * 5. 内存优化：对于非常大的排列，可以考虑使用生成器模式进行惰性计算
 * 
 * 回溯算法框架总结：
 * 1. 选择：在每一步选择一个元素加入当前解
 * 2. 约束：确保选择的元素满足问题的约束条件
 * 3. 目标：达到问题的目标状态
 * 4. 回溯：在尝试完一个选择的所有可能性后，撤销该选择，尝试其他选择
 * 
 * 回溯算法通常可以用以下伪代码表示：
 * function backtrack(路径, 选择列表):
 *     if 满足结束条件:
 *         将路径加入结果集
 *         return
 *     for 选择 in 选择列表:
 *         做选择
 *         backtrack(路径, 选择列表)
 *         撤销选择
 */