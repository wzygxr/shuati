package class086;

import java.util.ArrayList;
import java.util.List;

// LeetCode 78. 子集
// 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
// 解集不能包含重复的子集。你可以按任意顺序返回解集。
// 测试链接 : https://leetcode.cn/problems/subsets/

/**
 * 算法详解：子集问题（LeetCode 78）
 * 
 * 问题描述：
 * 给定一个不含重复元素的整数数组nums，返回其所有可能的子集（幂集）。
 * 解集不能包含重复的子集。
 * 
 * 算法思路：
 * 1. 位掩码法：每个子集对应一个位掩码，遍历所有可能的位掩码
 * 2. 回溯法：使用深度优先搜索生成所有子集
 * 3. 迭代法：基于已有子集逐步添加新元素
 * 
 * 时间复杂度分析：
 * 1. 位掩码法：O(n * 2^n)，需要生成2^n个子集，每个子集需要O(n)时间构建
 * 2. 回溯法：O(n * 2^n)，同样需要生成所有子集
 * 3. 迭代法：O(n * 2^n)，时间复杂度相同但实现更简洁
 * 
 * 空间复杂度分析：
 * 1. 输出空间：O(n * 2^n)，需要存储所有子集
 * 2. 额外空间：O(n)用于递归栈或临时存储
 * 3. 总体空间复杂度：O(n * 2^n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组是否为空
 * 2. 边界处理：处理空数组的情况
 * 3. 性能优化：对于大规模数据考虑内存使用
 * 4. 代码可读性：清晰的变量命名和注释
 * 
 * 极端场景验证：
 * 1. 输入数组为空的情况
 * 2. 数组只有一个元素的情况
 * 3. 数组包含多个元素的情况
 * 4. 大规模数组的性能测试
 */
public class LeetCode78_Subsets {
    
    /**
     * 位掩码法（最直观的解法）
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n * 2^n)
     * 
     * 算法思想：
     * 每个子集对应一个位掩码，掩码的每一位表示是否包含对应元素。
     * 遍历所有可能的位掩码（0到2^n-1），根据掩码构建子集。
     * 
     * 优点：思路清晰，易于理解
     * 缺点：需要处理位运算，对于大规模数据可能内存不足
     */
    public static List<List<Integer>> subsetsBitMask(int[] nums) {
        // 异常处理
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        int n = nums.length;
        List<List<Integer>> result = new ArrayList<>();
        
        // 特殊情况：空数组
        if (n == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        
        // 总子集数：2^n
        int total = 1 << n; // 2^n
        
        // 遍历所有可能的位掩码
        for (int mask = 0; mask < total; mask++) {
            List<Integer> subset = new ArrayList<>();
            
            // 检查掩码的每一位
            for (int i = 0; i < n; i++) {
                // 如果第i位为1，则包含nums[i]
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                }
            }
            
            result.add(subset);
        }
        
        return result;
    }
    
    /**
     * 回溯法（深度优先搜索）
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n) 递归栈空间
     * 
     * 算法思想：
     * 使用递归生成所有子集，每个元素有选或不选两种选择。
     * 通过回溯避免重复计算，保证子集不重复。
     * 
     * 优点：代码结构清晰，易于扩展
     * 缺点：递归深度较大时可能栈溢出
     */
    public static List<List<Integer>> subsetsBacktrack(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        
        // 从索引0开始回溯
        backtrack(nums, 0, current, result);
        return result;
    }
    
    private static void backtrack(int[] nums, int index, List<Integer> current, List<List<Integer>> result) {
        // 基本情况：处理完所有元素，将当前子集加入结果
        if (index == nums.length) {
            result.add(new ArrayList<>(current));
            return;
        }
        
        // 选择1：不包含当前元素
        backtrack(nums, index + 1, current, result);
        
        // 选择2：包含当前元素
        current.add(nums[index]);
        backtrack(nums, index + 1, current, result);
        
        // 回溯：移除最后添加的元素
        current.remove(current.size() - 1);
    }
    
    /**
     * 迭代法（逐步构建）
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n * 2^n)
     * 
     * 算法思想：
     * 从空集开始，逐步添加每个元素。
     * 对于每个已有子集，创建新子集并添加当前元素。
     * 
     * 优点：代码简洁，无需递归
     * 缺点：需要存储中间结果，内存使用较大
     */
    public static List<List<Integer>> subsetsIterative(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // 添加空集
        
        // 遍历每个元素
        for (int num : nums) {
            // 为每个已有子集创建新子集并添加当前元素
            int size = result.size();
            for (int i = 0; i < size; i++) {
                List<Integer> newSubset = new ArrayList<>(result.get(i));
                newSubset.add(num);
                result.add(newSubset);
            }
        }
        
        return result;
    }
    
    /**
     * 优化的回溯法（避免不必要的拷贝）
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n) 递归栈空间
     * 
     * 优化点：
     * 1. 避免频繁创建新列表
     * 2. 使用更高效的数据结构操作
     */
    public static List<List<Integer>> subsetsOptimized(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        dfs(nums, 0, new ArrayList<>(), result);
        return result;
    }
    
    private static void dfs(int[] nums, int index, List<Integer> path, List<List<Integer>> result) {
        // 每次递归都将当前路径加入结果
        result.add(new ArrayList<>(path));
        
        // 从当前索引开始，避免重复
        for (int i = index; i < nums.length; i++) {
            // 选择当前元素
            path.add(nums[i]);
            // 递归处理后续元素
            dfs(nums, i + 1, path, result);
            // 回溯
            path.remove(path.size() - 1);
        }
    }
    
    /**
     * 单元测试方法
     * 验证算法的正确性和各种边界情况
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 78 子集问题测试 ===\n");
        
        // 测试用例1：空数组
        testCase("测试用例1 - 空数组", new int[]{});
        
        // 测试用例2：单元素数组
        testCase("测试用例2 - 单元素", new int[]{1});
        
        // 测试用例3：双元素数组
        testCase("测试用例3 - 双元素", new int[]{1, 2});
        
        // 测试用例4：三元素数组
        testCase("测试用例4 - 三元素", new int[]{1, 2, 3});
        
        // 测试用例5：LeetCode官方示例
        testCase("测试用例5 - 官方示例", new int[]{1, 2, 3});
        
        // 性能测试
        performanceTest();
    }
    
    /**
     * 测试用例辅助方法
     */
    private static void testCase(String description, int[] nums) {
        System.out.println(description);
        System.out.println("输入数组: " + java.util.Arrays.toString(nums));
        
        // 测试所有方法
        List<List<Integer>> result1 = subsetsBitMask(nums);
        List<List<Integer>> result2 = subsetsBacktrack(nums);
        List<List<Integer>> result3 = subsetsIterative(nums);
        List<List<Integer>> result4 = subsetsOptimized(nums);
        
        System.out.println("位掩码法: " + result1.size() + "个子集");
        System.out.println("回溯法: " + result2.size() + "个子集");
        System.out.println("迭代法: " + result3.size() + "个子集");
        System.out.println("优化回溯: " + result4.size() + "个子集");
        
        // 验证所有方法结果一致
        boolean sizeMatch = result1.size() == result2.size() && 
                           result2.size() == result3.size() &&
                           result3.size() == result4.size();
        
        boolean contentMatch = areSubsetsEqual(result1, result2) &&
                              areSubsetsEqual(result2, result3) &&
                              areSubsetsEqual(result3, result4);
        
        if (sizeMatch && contentMatch) {
            System.out.println("测试通过 ✓");
            
            // 打印前几个子集作为示例
            if (result1.size() <= 16) {
                System.out.println("所有子集: " + result1);
            } else {
                System.out.println("前8个子集: " + result1.subList(0, Math.min(8, result1.size())));
            }
        } else {
            System.out.println("测试失败 ✗");
        }
        System.out.println();
    }
    
    /**
     * 比较两个子集列表是否相等（忽略顺序）
     */
    private static boolean areSubsetsEqual(List<List<Integer>> list1, List<List<Integer>> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        
        // 将子集转换为集合的集合进行比较
        java.util.Set<java.util.Set<Integer>> set1 = new java.util.HashSet<>();
        java.util.Set<java.util.Set<Integer>> set2 = new java.util.HashSet<>();
        
        for (List<Integer> subset : list1) {
            set1.add(new java.util.HashSet<>(subset));
        }
        
        for (List<Integer> subset : list2) {
            set2.add(new java.util.HashSet<>(subset));
        }
        
        return set1.equals(set2);
    }
    
    /**
     * 性能测试方法
     * 测试算法在大规模数据下的表现
     */
    private static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成测试数据：中等规模数组（避免内存溢出）
        int n = 15; // 2^15 = 32768个子集
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = i + 1;
        }
        
        System.out.println("测试数据规模: " + n + "个元素");
        System.out.println("预期子集数量: " + (1 << n));
        
        // 测试位掩码法
        long startTime = System.currentTimeMillis();
        List<List<Integer>> result1 = subsetsBitMask(nums);
        long endTime = System.currentTimeMillis();
        System.out.println("位掩码法:");
        System.out.println("  子集数量: " + result1.size());
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 测试优化回溯法（性能较好）
        startTime = System.currentTimeMillis();
        List<List<Integer>> result4 = subsetsOptimized(nums);
        endTime = System.currentTimeMillis();
        System.out.println("优化回溯法:");
        System.out.println("  子集数量: " + result4.size());
        System.out.println("  耗时: " + (endTime - startTime) + "ms");
        
        // 验证结果一致性
        if (result1.size() == result4.size()) {
            System.out.println("结果一致性验证: 通过 ✓");
        } else {
            System.out.println("结果一致性验证: 失败 ✗");
        }
        
        System.out.println("注意：回溯法和迭代法在大规模数据下可能内存不足");
    }
    
    /**
     * 复杂度分析详细计算：
     * 
     * 位掩码法：
     * - 时间：外层循环2^n次，内层循环n次 → O(n * 2^n)
     * - 空间：需要存储所有子集 → O(n * 2^n)
     * - 最优解：是理论最优，但实际中可能内存不足
     * 
     * 回溯法：
     * - 时间：生成2^n个子集，每个子集平均长度n/2 → O(n * 2^n)
     * - 空间：递归深度n → O(n)
     * - 最优解：空间效率较好，但递归可能栈溢出
     * 
     * 迭代法：
     * - 时间：外层循环n次，内层循环2^i次 → O(n * 2^n)
     * - 空间：需要存储所有子集 → O(n * 2^n)
     * - 最优解：代码简洁但内存使用大
     * 
     * 优化回溯法：
     * - 时间：O(n * 2^n)
     * - 空间：O(n) 递归栈空间
     * - 最优解：综合性能最好
     * 
     * 工程选择依据：
     * 1. 小规模数据（n ≤ 20）：任意方法都可
     * 2. 中等规模数据（20 < n ≤ 25）：优化回溯法
     * 3. 大规模数据（n > 25）：考虑使用迭代器或流式处理
     * 
     * 算法调试技巧：
     * 1. 打印中间子集，观察生成过程
     * 2. 使用小规模测试用例验证正确性
     * 3. 添加断言验证关键假设
     */
}