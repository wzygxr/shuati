package class080;

import java.util.*;

// 子集 II (Subsets II)
// 给你一个整数数组 nums ，其中可能包含重复元素，
// 请你返回该数组所有可能的子集（幂集）。
// 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
// 测试链接 : https://leetcode.cn/problems/subsets-ii/
public class Code14_SubsetsII {
    
    /**
     * 主方法：生成所有不重复子集
     * @param nums 输入数组，可能包含重复元素
     * @return 所有不重复子集的列表
     */
    public List<List<Integer>> subsetsWithDup(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        // 边界情况处理
        if (nums == null || nums.length == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        
        // 排序是去重的关键步骤
        Arrays.sort(nums);
        
        // 使用回溯法生成子集
        backtrack(nums, 0, new ArrayList<>(), result);
        
        return result;
    }
    
    /**
     * 回溯法生成子集
     * @param nums 输入数组
     * @param start 当前处理的起始位置
     * @param current 当前正在构建的子集
     * @param result 存储所有子集的结果列表
     */
    private void backtrack(int[] nums, int start, List<Integer> current, List<List<Integer>> result) {
        // 将当前子集加入结果（包括空集）
        result.add(new ArrayList<>(current));
        
        // 从start位置开始遍历数组
        for (int i = start; i < nums.length; i++) {
            // 关键去重逻辑：跳过重复元素
            // 只有当i > start且当前元素等于前一个元素时才跳过
            // 这样可以确保相同元素的第一个被包含，后续重复的被跳过
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 选择当前元素
            current.add(nums[i]);
            
            // 递归处理下一个位置
            backtrack(nums, i + 1, current, result);
            
            // 回溯：撤销选择
            current.remove(current.size() - 1);
        }
    }
    
    /**
     * 状态压缩解法：使用位运算生成子集
     * 这种方法虽然直观但效率较低，主要用于理解状态压缩思想
     * @param nums 输入数组
     * @return 所有不重复子集的列表
     */
    public List<List<Integer>> subsetsWithDupBitmask(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            result.add(new ArrayList<>());
            return result;
        }
        
        Arrays.sort(nums);
        int n = nums.length;
        
        // 使用Set来去重
        Set<String> seen = new HashSet<>();
        
        // 枚举所有可能的子集掩码
        for (int mask = 0; mask < (1 << n); mask++) {
            List<Integer> subset = new ArrayList<>();
            StringBuilder key = new StringBuilder();
            
            // 根据掩码生成子集
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    subset.add(nums[i]);
                    key.append(nums[i]).append(",");
                }
            }
            
            // 使用字符串键值去重
            if (seen.add(key.toString())) {
                result.add(subset);
            }
        }
        
        return result;
    }
    
    /**
     * 迭代解法：逐步构建子集
     * 这种方法更高效，适合处理较大规模数据
     * @param nums 输入数组
     * @return 所有不重复子集的列表
     */
    public List<List<Integer>> subsetsWithDupIterative(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // 添加空集
        
        if (nums == null || nums.length == 0) {
            return result;
        }
        
        Arrays.sort(nums);
        
        int startIndex = 0; // 新元素开始的位置
        int size = 0;       // 上一轮结果的大小
        
        for (int i = 0; i < nums.length; i++) {
            // 如果当前元素与前一个元素相同，则只在上轮新生成的子集基础上添加
            // 否则在所有子集基础上添加
            if (i > 0 && nums[i] == nums[i - 1]) {
                startIndex = size;
            } else {
                startIndex = 0;
            }
            
            size = result.size();
            // 在当前选定的子集基础上添加新元素
            for (int j = startIndex; j < size; j++) {
                List<Integer> newSubset = new ArrayList<>(result.get(j));
                newSubset.add(nums[i]);
                result.add(newSubset);
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        Code14_SubsetsII solution = new Code14_SubsetsII();
        
        // 测试用例1: 包含重复元素的数组
        int[] nums1 = {1, 2, 2};
        List<List<Integer>> result1 = solution.subsetsWithDup(nums1);
        System.out.println("测试用例1 [1,2,2] 的子集数量: " + result1.size());
        System.out.println("子集内容: " + result1);
        
        // 验证结果正确性
        assert result1.size() == 6 : "子集数量应为6";
        
        // 测试用例2: 不包含重复元素的数组
        int[] nums2 = {1, 2, 3};
        List<List<Integer>> result2 = solution.subsetsWithDup(nums2);
        System.out.println("测试用例2 [1,2,3] 的子集数量: " + result2.size());
        assert result2.size() == 8 : "子集数量应为8";
        
        // 测试用例3: 空数组
        int[] nums3 = {};
        List<List<Integer>> result3 = solution.subsetsWithDup(nums3);
        System.out.println("测试用例3 [] 的子集数量: " + result3.size());
        assert result3.size() == 1 : "空数组子集数量应为1";
        
        // 测试用例4: 全重复元素
        int[] nums4 = {2, 2, 2};
        List<List<Integer>> result4 = solution.subsetsWithDup(nums4);
        System.out.println("测试用例4 [2,2,2] 的子集数量: " + result4.size());
        assert result4.size() == 4 : "全重复元素子集数量应为4";
        
        // 性能测试：较大规模数据
        int[] nums5 = new int[10];
        Arrays.fill(nums5, 1); // 填充重复元素
        long startTime = System.nanoTime();
        List<List<Integer>> result5 = solution.subsetsWithDup(nums5);
        long endTime = System.nanoTime();
        System.out.println("性能测试: 处理10个重复元素耗时 " + 
                          (endTime - startTime) / 1_000_000 + " 毫秒");
        System.out.println("生成子集数量: " + result5.size());
        
        // 测试不同解法的正确性
        System.out.println("\n不同解法对比测试:");
        List<List<Integer>> result1a = solution.subsetsWithDupBitmask(nums1);
        List<List<Integer>> result1b = solution.subsetsWithDupIterative(nums1);
        
        System.out.println("回溯法结果数量: " + result1.size());
        System.out.println("位运算法结果数量: " + result1a.size());
        System.out.println("迭代法结果数量: " + result1b.size());
        
        // 验证三种方法结果一致
        assert result1.size() == result1a.size() : "不同解法结果数量应一致";
        assert result1.size() == result1b.size() : "不同解法结果数量应一致";
        
        System.out.println("所有测试用例通过!");
    }
}

/*
算法深度分析：

1. 去重机制的核心原理：
   - 排序后相同元素相邻，便于识别重复
   - 在回溯过程中，对于重复元素，我们只选择第一个出现的，跳过后续重复的
   - 这样可以确保相同元素的组合只被生成一次

2. 时间复杂度详细分析：
   - 最坏情况下：O(n * 2^n)
   - 最好情况下（全重复元素）：O(n^2)
   - 平均情况：取决于重复元素的数量和分布

3. 空间复杂度分析：
   - 递归栈深度：O(n)
   - 结果存储：O(2^n * n) 但这是输出空间，不计入复杂度
   - 辅助空间：O(n) 用于存储当前路径

4. 算法选择建议：
   - 回溯法：代码简洁，易于理解，适合面试
   - 迭代法：性能更好，适合大规模数据
   - 位运算法：教学目的，帮助理解状态压缩

5. 边界情况处理：
   - 空数组：返回包含空集的列表
   - 单元素数组：返回两个子集（空集和单元素集）
   - 全重复元素：子集数量为n+1

6. 工程化考量：
   - 内存使用：对于大规模数据，考虑使用迭代法减少递归栈深度
   - 异常处理：对输入进行空值检查
   - 性能监控：添加性能测试代码

7. 面试技巧：
   - 能够解释去重原理
   - 比较不同解法的优缺点
   - 讨论时间空间复杂度
   - 处理边界情况

8. 扩展思考：
   - 如何优化以处理更大规模的数据？
   - 如果要求按特定顺序输出子集？
   - 如果数组元素不是整数而是对象？
*/
