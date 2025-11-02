// LeetCode 78. Subsets
// 子集
// 题目来源：https://leetcode.cn/problems/subsets/

import java.util.*;

/**
 * 问题描述：
 * 给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 解题思路：
 * 使用回溯算法，通过递归生成所有可能的子集
 * 1. 选择：对于每个元素，可以选择包含或不包含在子集中
 * 2. 约束：元素互不相同，不能重复选择同一个元素
 * 3. 目标：生成所有可能的子集（包括空集和原数组本身）
 * 
 * 时间复杂度：O(N * 2^N)，其中N是数组的长度，2^N是子集的总数，每个子集需要O(N)的时间复制
 * 空间复杂度：O(N)，递归栈的深度最多为N
 */

public class Code20_Subsets {
    /**
     * 回溯算法求解子集问题
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            // 至少返回空集
            result.add(new ArrayList<>());
            return result;
        }
        
        // 存储当前子集
        List<Integer> currentSubset = new ArrayList<>();
        
        // 开始回溯
        backtrack(nums, 0, currentSubset, result);
        
        return result;
    }
    
    /**
     * 回溯函数
     * @param nums 输入数组
     * @param start 当前考虑元素的起始索引
     * @param currentSubset 当前正在构建的子集
     * @param result 存储所有子集的结果集
     */
    private void backtrack(int[] nums, int start, List<Integer> currentSubset, List<List<Integer>> result) {
        // 每次进入函数，都将当前子集的副本加入结果集
        result.add(new ArrayList<>(currentSubset));
        
        // 遍历从start开始的所有元素，决定是否将其加入子集
        for (int i = start; i < nums.length; i++) {
            // 选择当前元素，将其加入子集
            currentSubset.add(nums[i]);
            
            // 递归到下一层，考虑下一个位置的元素
            backtrack(nums, i + 1, currentSubset, result);
            
            // 回溯：撤销选择，移除刚刚添加的元素
            currentSubset.remove(currentSubset.size() - 1);
        }
    }
    
    /**
     * 迭代解法：使用位运算生成所有子集
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    public List<List<Integer>> subsetsByBitmask(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        
        int n = nums.length;
        // 总共有2^n个子集
        int totalSubsets = 1 << n; // 相当于2^n
        
        // 遍历从0到2^n-1的所有数字，每个数字代表一个子集的位掩码
        for (int mask = 0; mask < totalSubsets; mask++) {
            List<Integer> subset = new ArrayList<>();
            
            // 检查每一位是否为1，如果为1则将对应的元素加入子集
            for (int i = 0; i < n; i++) {
                // 检查mask的第i位是否为1
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            
            result.add(subset);
        }
        
        return result;
    }
    
    /**
     * 迭代解法：使用增量法构建子集
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    public List<List<Integer>> subsetsIterative(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 初始时加入空集
        result.add(new ArrayList<>());
        
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        // 对于每个元素，将其添加到现有所有子集中，生成新的子集
        for (int num : nums) {
            int size = result.size();
            // 遍历当前所有子集
            for (int i = 0; i < size; i++) {
                // 创建新子集，基于现有子集
                List<Integer> newSubset = new ArrayList<>(result.get(i));
                // 添加当前元素
                newSubset.add(num);
                // 将新子集加入结果集
                result.add(newSubset);
            }
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        Code20_Subsets solution = new Code20_Subsets();
        
        // 测试用例1
        int[] nums1 = {1, 2, 3};
        System.out.println("测试用例1 - 回溯算法:");
        List<List<Integer>> result1 = solution.subsets(nums1);
        printSubsets(result1);
        
        System.out.println("\n测试用例1 - 位运算:");
        List<List<Integer>> result1ByBitmask = solution.subsetsByBitmask(nums1);
        printSubsets(result1ByBitmask);
        
        System.out.println("\n测试用例1 - 迭代增量法:");
        List<List<Integer>> result1Iterative = solution.subsetsIterative(nums1);
        printSubsets(result1Iterative);
        
        // 测试用例2
        int[] nums2 = {0};
        System.out.println("\n测试用例2 - 回溯算法:");
        List<List<Integer>> result2 = solution.subsets(nums2);
        printSubsets(result2);
    }
    
    /**
     * 打印所有子集
     */
    private static void printSubsets(List<List<Integer>> subsets) {
        System.out.print("[");
        for (int i = 0; i < subsets.size(); i++) {
            System.out.print("[");
            for (int j = 0; j < subsets.get(i).size(); j++) {
                System.out.print(subsets.get(i).get(j));
                if (j < subsets.get(i).size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("]");
            if (i < subsets.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }
}

/**
 * 性能分析：
 * 
 * 1. 回溯算法：
 *    - 时间复杂度：O(N * 2^N)，其中N是数组长度，2^N是子集的总数，每个子集需要O(N)的时间复制
 *    - 空间复杂度：O(N)，递归栈的深度最多为N，以及存储当前子集的空间
 *    - 优点：逻辑清晰，容易理解
 *    - 缺点：递归可能导致栈溢出（对于非常大的数组）
 * 
 * 2. 位运算解法：
 *    - 时间复杂度：O(N * 2^N)，需要遍历2^N个掩码，每个掩码需要O(N)的时间生成子集
 *    - 空间复杂度：O(N)，主要是存储结果的空间（不考虑输出）
 *    - 优点：代码简洁，对于小规模问题效率较高
 *    - 缺点：当N较大时（如超过30），2^N会超出整型范围
 * 
 * 3. 迭代增量法：
 *    - 时间复杂度：O(N * 2^N)，需要处理2^N个子集，每个子集可能需要O(N)的复制操作
 *    - 空间复杂度：O(N * 2^N)，存储所有子集
 *    - 优点：避免了递归可能导致的栈溢出问题
 *    - 缺点：空间复杂度较高
 * 
 * 算法优化思路：
 * 1. 剪枝优化：在特定应用场景中，可以根据条件进行剪枝，提前停止某些分支的搜索
 * 2. 内存优化：对于非常大的数组，可以考虑使用迭代解法避免递归栈溢出
 * 3. 并行计算：对于大规模数据，可以考虑并行计算不同的分支
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中应该添加对输入参数的有效性检查
 * 2. 线程安全：如果在多线程环境中使用，需要确保线程安全
 * 3. 扩展性：可以扩展此算法以处理包含重复元素的数组（如LeetCode 90题）
 * 4. 性能监控：对于大规模数据，可以添加性能监控代码
 * 5. 内存管理：对于非常大的结果集，需要考虑内存限制
 * 
 * 子集问题的数学背景：
 * 一个包含n个不同元素的集合，其子集数量为2^n，包括空集和原集合本身。这个问题本质上是求集合的幂集。
 * 
 * 回溯算法与子集生成的关系：
 * 回溯算法是生成子集的一种自然方式，它通过决定每个元素是否包含在子集中来构建所有可能的组合。
 * 与排列问题不同，子集问题不考虑元素的顺序，因此一旦决定不选择某个元素，就不会再考虑它。
 */