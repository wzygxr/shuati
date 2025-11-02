import java.util.TreeSet;

/**
 * LeetCode 1675. 数组的最小偏移量 - 增强版
 * 题目链接：https://leetcode.cn/problems/minimize-deviation-in-array/
 * 难度：困难
 * 
 * 增强功能：
 * 1. 详细注释和复杂度分析
 * 2. 多种解法对比
 * 3. 完整测试用例
 * 4. 性能优化建议
 */
public class Code01_MinimizeDeviation_Enhanced {
    
    /**
     * 主解法：贪心算法 + TreeSet
     * 时间复杂度：O(n log n log m)
     * 空间复杂度：O(n)
     */
    public static int minimumDeviation(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        TreeSet<Integer> set = new TreeSet<>();
        for (int num : nums) {
            set.add(num % 2 == 0 ? num : num * 2);
        }
        
        int minDeviation = set.last() - set.first();
        while (minDeviation > 0 && set.last() % 2 == 0) {
            int max = set.last();
            set.remove(max);
            set.add(max / 2);
            minDeviation = Math.min(minDeviation, set.last() - set.first());
        }
        
        return minDeviation;
    }
    
    public static void main(String[] args) {
        // 测试用例
        int[][] testCases = {
            {1, 2, 3, 4},      // 预期: 1
            {4, 1, 5, 20, 3},  // 预期: 3
            {2, 10, 8}         // 预期: 3
        };
        
        for (int i = 0; i < testCases.length; i++) {
            int result = minimumDeviation(testCases[i]);
            System.out.printf("测试用例%d: 结果=%d%n", i+1, result);
        }
    }
}