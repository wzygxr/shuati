import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 46. 全排列
 * 
 * 题目描述：
 * 给定一个不含重复数字的数组 nums ，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 示例：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 输入：nums = [0,1]
 * 输出：[[0,1],[1,0]]
 * 
 * 输入：nums = [1]
 * 输出：[[1]]
 * 
 * 提示：
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * nums 中的所有整数互不相同
 * 
 * 链接：https://leetcode.cn/problems/permutations/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的排列
 * 2. 对于每个位置，尝试放置每个未使用的数字
 * 3. 通过递归和回溯生成所有满足条件的排列
 * 4. 使用布尔数组标记数字是否已被使用
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：使用布尔数组避免重复使用数字
 * 2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
 * 3. 约束传播：一旦某个数字被使用，立即标记为已使用
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
public class LeetCode46_Permutations {
    
    /**
     * 生成数组的所有全排列
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        List<Integer> path = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, path, used, result);
        return result;
    }
    
    /**
     * 回溯函数生成排列
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
     * 解法二：交换元素法生成排列
     * 通过交换数组元素生成所有排列，避免使用额外的标记数组
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    public List<List<Integer>> permute2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 将数组转换为列表以便操作
        List<Integer> numList = new ArrayList<>();
        for (int num : nums) {
            numList.add(num);
        }
        
        backtrack2(numList, 0, result);
        return result;
    }
    
    /**
     * 通过交换元素生成排列的回溯函数
     * 
     * @param nums 数字列表
     * @param start 当前处理的位置
     * @param result 结果列表
     */
    private void backtrack2(List<Integer> nums, int start, List<List<Integer>> result) {
        // 终止条件：已处理完所有位置
        if (start == nums.size()) {
            result.add(new ArrayList<>(nums));
            return;
        }
        
        // 尝试将每个后续元素交换到当前位置
        for (int i = start; i < nums.size(); i++) {
            // 交换元素
            swap(nums, start, i);
            
            // 递归处理下一个位置
            backtrack2(nums, start + 1, result);
            
            // 回溯：恢复交换前的状态
            swap(nums, start, i);
        }
    }
    
    /**
     * 交换列表中两个位置的元素
     * 
     * @param nums 数字列表
     * @param i 位置i
     * @param j 位置j
     */
    private void swap(List<Integer> nums, int i, int j) {
        int temp = nums.get(i);
        nums.set(i, nums.get(j));
        nums.set(j, temp);
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode46_Permutations solution = new LeetCode46_Permutations();
        
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        List<List<Integer>> result1 = solution.permute(nums1);
        System.out.println("输入: nums = [1,2,3]");
        System.out.println("输出: " + result1);
        
        // 测试用例2
        int[] nums2 = {0, 1};
        List<List<Integer>> result2 = solution.permute(nums2);
        System.out.println("\n输入: nums = [0,1]");
        System.out.println("输出: " + result2);
        
        // 测试用例3
        int[] nums3 = {1};
        List<List<Integer>> result3 = solution.permute(nums3);
        System.out.println("\n输入: nums = [1]");
        System.out.println("输出: " + result3);
        
        // 测试解法二
        System.out.println("\n=== 解法二测试 ===");
        List<List<Integer>> result4 = solution.permute2(nums1);
        System.out.println("输入: nums = [1,2,3]");
        System.out.println("输出: " + result4);
    }
}