import java.util.*;

// LeetCode 493. 翻转对
// 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
// 你需要返回给定数组中的重要翻转对的数量。
// 测试链接: https://leetcode.cn/problems/reverse-pairs/

public class Code10_ReversePairs {

    /**
     * 使用树状数组解决翻转对问题
     * 
     * 解题思路:
     * 1. 翻转对的定义是 i < j 且 nums[i] > 2*nums[j]
     * 2. 我们可以将所有可能涉及到的数值进行离散化处理
     * 3. 从左到右遍历数组，对于每个元素nums[i]：
     *    - 查询已经处理过的元素中，有多少个元素满足 nums[j] < nums[i]/2.0（j < i）
     *    - 将当前元素加入树状数组
     * 4. 由于涉及到2*nums[j]，我们需要同时将nums[j]和2*nums[j]都加入离散化数组
     * 
     * 时间复杂度分析:
     * - 离散化: O(n log n)
     * - 遍历数组并查询更新: O(n log n)
     * - 总时间复杂度: O(n log n)
     * 
     * 空间复杂度分析:
     * - 树状数组: O(n)
     * - 离散化数组: O(n)
     * - 总空间复杂度: O(n)
     * 
     * 工程化考量:
     * 1. 离散化处理大数值范围
     * 2. 边界条件处理（整数溢出问题）
     * 3. 异常输入检查
     * 4. 详细注释和变量命名
     */

    // 树状数组类
    static class FenwickTree {
        private int[] tree;
        private int n;

        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[n + 1];
        }

        private int lowbit(int x) {
            return x & (-x);
        }

        // 在位置i上增加值delta
        public void add(int i, int delta) {
            while (i <= n) {
                tree[i] += delta;
                i += lowbit(i);
            }
        }

        // 查询[1, i]的前缀和
        public int query(int i) {
            int sum = 0;
            while (i > 0) {
                sum += tree[i];
                i -= lowbit(i);
            }
            return sum;
        }
    }

    public static int reversePairs(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        
        // 离散化处理
        // 1. 收集所有需要离散化的值：nums[i] 和 2*nums[i]
        Set<Long> allNumbers = new HashSet<>();
        for (int num : nums) {
            allNumbers.add((long) num);
            allNumbers.add((long) 2 * num);
        }
        
        // 2. 排序去重
        List<Long> sortedNumbers = new ArrayList<>(allNumbers);
        Collections.sort(sortedNumbers);
        
        // 3. 建立数值到离散化索引的映射
        Map<Long, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < sortedNumbers.size(); i++) {
            indexMap.put(sortedNumbers.get(i), i + 1);
        }
        
        // 4. 创建树状数组
        FenwickTree fenwickTree = new FenwickTree(sortedNumbers.size());
        
        int result = 0;
        
        // 5. 从左到右遍历数组
        for (int i = 0; i < n; i++) {
            // 查询有多少个已经处理过的元素满足 nums[j] > 2*nums[i]
            // 也就是查询有多少个已经处理过的元素满足 nums[j] >= 2*nums[i]+1
            long target = 2L * nums[i] + 1;
            
            // 找到target在离散化数组中的位置
            int pos = Collections.binarySearch(sortedNumbers, target);
            if (pos < 0) {
                // 如果没找到，binarySearch返回的是插入位置的负值减一
                pos = -pos - 1;
            }
            
            // 查询大于等于target的元素个数
            result += fenwickTree.query(sortedNumbers.size()) - fenwickTree.query(pos);
            
            // 将当前元素加入树状数组
            int index = indexMap.get((long) nums[i]);
            fenwickTree.add(index, 1);
        }
        
        return result;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 3, 2, 3, 1};
        int result1 = reversePairs(nums1);
        System.out.println("Input: [1, 3, 2, 3, 1]");
        System.out.println("Output: " + result1); // 期望输出: 2
        
        // 测试用例2
        int[] nums2 = {2, 4, 3, 5, 1};
        int result2 = reversePairs(nums2);
        System.out.println("Input: [2, 4, 3, 5, 1]");
        System.out.println("Output: " + result2); // 期望输出: 3
        
        // 测试用例3
        int[] nums3 = {-5, -5};
        int result3 = reversePairs(nums3);
        System.out.println("Input: [-5, -5]");
        System.out.println("Output: " + result3); // 期望输出: 1
        
        // 测试用例4
        int[] nums4 = {2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647};
        int result4 = reversePairs(nums4);
        System.out.println("Large numbers test case result: " + result4); // 期望输出: 0
    }
}