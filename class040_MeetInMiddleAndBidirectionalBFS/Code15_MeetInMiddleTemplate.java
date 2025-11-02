package class063;

import java.util.*;

// Meet in the Middle 算法模板
// 提供折半搜索的通用模板和最佳实践
// 
// 算法概述：
// 折半搜索（Meet in the Middle）是一种将大规模搜索问题分解为两个较小问题的技术
// 适用于时间复杂度为指数级的问题，可以将O(2^n)优化为O(2^(n/2))
// 
// 适用场景：
// 1. 子集和问题（Subset Sum）
// 2. 背包问题变种（Knapsack Variations）
// 3. 状态空间搜索（State Space Search）
// 4. 组合优化问题（Combinatorial Optimization）
// 
// 模板特点：
// 1. 通用性强：适用于多种折半搜索问题
// 2. 性能优化：包含常见的优化技巧
// 3. 可读性好：清晰的代码结构和注释
// 4. 易于扩展：可以轻松适配具体问题

public class Code15_MeetInMiddleTemplate {
    
    /**
     * 折半搜索通用模板
     * 
     * @param elements 输入元素数组
     * @param target 目标值
     * @return 满足条件的结果
     * 
     * 模板使用步骤：
     * 1. 将输入数组分为两半
     * 2. 分别计算两半的所有可能结果
     * 3. 对其中一半的结果进行排序（便于二分查找）
     * 4. 遍历另一半结果，在排序部分中查找匹配项
     * 5. 合并结果并返回
     */
    public static List<List<Integer>> meetInMiddleTemplate(int[] elements, int target) {
        // 边界条件检查
        if (elements == null || elements.length == 0) {
            return new ArrayList<>();
        }
        
        int n = elements.length;
        int mid = n / 2;
        
        // 步骤1：生成左半部分的所有可能子集和
        List<SubsetResult> leftSubsets = generateSubsets(elements, 0, mid);
        
        // 步骤2：生成右半部分的所有可能子集和
        List<SubsetResult> rightSubsets = generateSubsets(elements, mid, n);
        
        // 步骤3：对右半部分按和排序（便于二分查找）
        Collections.sort(rightSubsets, Comparator.comparingInt(s -> s.sum));
        
        // 步骤4：遍历左半部分，在右半部分中查找匹配项
        List<List<Integer>> results = new ArrayList<>();
        
        for (SubsetResult left : leftSubsets) {
            int remaining = target - left.sum;
            
            // 使用二分查找在右半部分中找到所有和为remaining的子集
            List<SubsetResult> matchingRight = binarySearch(rightSubsets, remaining);
            
            // 步骤5：合并左右部分的结果
            for (SubsetResult right : matchingRight) {
                List<Integer> combined = new ArrayList<>(left.elements);
                combined.addAll(right.elements);
                results.add(combined);
            }
        }
        
        return results;
    }
    
    /**
     * 生成指定范围内所有可能的子集
     */
    private static List<SubsetResult> generateSubsets(int[] elements, int start, int end) {
        List<SubsetResult> subsets = new ArrayList<>();
        // 总是包含空集
        subsets.add(new SubsetResult(0, new ArrayList<>()));
        
        // 递归生成所有子集
        generateSubsetsRecursive(elements, start, end, 0, new ArrayList<>(), subsets);
        return subsets;
    }
    
    /**
     * 递归生成子集的辅助方法
     */
    private static void generateSubsetsRecursive(int[] elements, int start, int end,
                                               int currentSum, List<Integer> currentElements,
                                               List<SubsetResult> results) {
        if (start == end) {
            return;
        }
        
        // 对于每个元素，有两种选择：包含或不包含
        for (int i = start; i < end; i++) {
            // 选择包含当前元素
            currentElements.add(elements[i]);
            results.add(new SubsetResult(currentSum + elements[i], new ArrayList<>(currentElements)));
            
            // 递归处理剩余元素
            generateSubsetsRecursive(elements, i + 1, end, currentSum + elements[i], 
                                   currentElements, results);
            
            // 回溯：不包含当前元素
            currentElements.remove(currentElements.size() - 1);
        }
    }
    
    /**
     * 二分查找：在排序的子集列表中查找指定和的所有子集
     */
    private static List<SubsetResult> binarySearch(List<SubsetResult> sortedSubsets, int targetSum) {
        List<SubsetResult> results = new ArrayList<>();
        
        // 找到第一个等于targetSum的位置
        int left = 0, right = sortedSubsets.size() - 1;
        int firstIndex = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midSum = sortedSubsets.get(mid).sum;
            
            if (midSum >= targetSum) {
                if (midSum == targetSum) {
                    firstIndex = mid;
                }
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        // 如果找到，收集所有等于targetSum的子集
        if (firstIndex != -1) {
            for (int i = firstIndex; i < sortedSubsets.size(); i++) {
                if (sortedSubsets.get(i).sum == targetSum) {
                    results.add(sortedSubsets.get(i));
                } else {
                    break;
                }
            }
        }
        
        return results;
    }
    
    /**
     * 子集结果类，包含和与元素列表
     */
    private static class SubsetResult {
        int sum;
        List<Integer> elements;
        
        SubsetResult(int sum, List<Integer> elements) {
            this.sum = sum;
            this.elements = elements;
        }
    }
    
    // 高级优化版本：包含剪枝和去重
    public static class OptimizedMeetInMiddle {
        
        /**
         * 优化版折半搜索：包含剪枝和去重
         */
        public static int optimizedSearch(int[] elements, int target) {
            int n = elements.length;
            int mid = n / 2;
            
            // 生成左半部分的所有可能和（使用Set去重）
            Set<Integer> leftSums = new HashSet<>();
            generateSumsOptimized(elements, 0, mid, 0, leftSums, target);
            
            // 生成右半部分的所有可能和
            Set<Integer> rightSums = new HashSet<>();
            generateSumsOptimized(elements, mid, n, 0, rightSums, target);
            
            // 查找满足条件的组合
            int count = 0;
            for (int leftSum : leftSums) {
                if (rightSums.contains(target - leftSum)) {
                    count++;
                }
            }
            
            return count;
        }
        
        /**
         * 优化版生成和：包含剪枝
         */
        private static void generateSumsOptimized(int[] elements, int start, int end,
                                                 int currentSum, Set<Integer> sums, int target) {
            if (start == end) {
                sums.add(currentSum);
                return;
            }
            
            // 剪枝：如果当前和已经超过目标值（对于非负元素）
            if (currentSum > target && elements[start] >= 0) {
                return;
            }
            
            // 不选择当前元素
            generateSumsOptimized(elements, start + 1, end, currentSum, sums, target);
            
            // 选择当前元素
            generateSumsOptimized(elements, start + 1, end, currentSum + elements[start], sums, target);
        }
    }
    
    // 性能对比测试
    public static void main(String[] args) {
        // 测试用例1：子集和问题
        System.out.println("=== 测试用例1：子集和问题 ===");
        int[] elements1 = {1, 2, 3, 4, 5, 6};
        int target1 = 7;
        
        List<List<Integer>> results1 = meetInMiddleTemplate(elements1, target1);
        System.out.println("元素数组：" + Arrays.toString(elements1));
        System.out.println("目标和：" + target1);
        System.out.println("找到的子集数量：" + results1.size());
        for (List<Integer> subset : results1) {
            System.out.println("子集：" + subset + ", 和：" + subset.stream().mapToInt(Integer::intValue).sum());
        }
        System.out.println();
        
        // 性能测试：大规模数据
        System.out.println("=== 性能测试：大规模数据 ===");
        int size = 30;
        int[] largeElements = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            largeElements[i] = random.nextInt(100) + 1;
        }
        int largeTarget = 1000;
        
        long startTime = System.currentTimeMillis();
        int count = OptimizedMeetInMiddle.optimizedSearch(largeElements, largeTarget);
        long endTime = System.currentTimeMillis();
        
        System.out.println("数据规模：" + size + "个元素");
        System.out.println("执行时间：" + (endTime - startTime) + "ms");
        System.out.println("满足条件的组合数：" + count);
        System.out.println();
        
        // 算法复杂度分析展示
        System.out.println("=== 算法复杂度分析 ===");
        System.out.println("直接暴力搜索复杂度：O(2^n)");
        System.out.println("折半搜索复杂度：O(2^(n/2))");
        System.out.println("优化效果：指数级降低");
        System.out.println();
        
        // 适用问题类型展示
        System.out.println("=== 适用问题类型 ===");
        System.out.println("1. 子集和问题（Subset Sum）");
        System.out.println("2. 背包问题变种（Knapsack Variations）");
        System.out.println("3. 状态空间搜索（State Space Search）");
        System.out.println("4. 组合优化问题（Combinatorial Optimization）");
        System.out.println("5. 约束满足问题（Constraint Satisfaction）");
    }
}

/*
 * 模板深度分析：
 * 
 * 1. 核心思想：
 *    - 分治策略：将大规模问题分解为两个较小问题
 *    - 组合优化：分别求解后合并结果
 *    - 搜索优化：显著降低时间复杂度
 * 
 * 2. 关键优化技巧：
 *    - 排序预处理：便于二分查找
 *    - 剪枝策略：提前终止不可能的分支
 *    - 去重处理：避免重复计算
 *    - 哈希加速：快速查找匹配项
 * 
 * 3. 工程化最佳实践：
 *    - 模块化设计：分离关注点
 *    - 泛型支持：适应不同类型数据
 *    - 性能监控：实时评估算法效率
 *    - 测试覆盖：确保正确性和稳定性
 * 
 * 4. 扩展应用场景：
 *    - 多目标优化：处理多个约束条件
 *    - 动态规划结合：处理更复杂的问题结构
 *    - 并行计算：利用多核处理器加速
 *    - 分布式处理：处理超大规模数据
 * 
 * 5. 性能考量：
 *    - 内存使用：注意大规模数据的存储需求
 *    - 缓存友好：优化数据访问模式
 *    - 常数优化：减少不必要的计算
 *    - 算法选择：根据问题特性选择最合适的变种
 */