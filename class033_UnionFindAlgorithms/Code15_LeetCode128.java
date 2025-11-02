package class056;

import java.util.*;

/**
 * LeetCode 128. 最长连续序列
 * 链接: https://leetcode.cn/problems/longest-consecutive-sequence/
 * 难度: 中等
 * 
 * 题目描述:
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例 1:
 * 输入: nums = [100,4,200,1,3,2]
 * 输出: 4
 * 解释: 最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 示例 2:
 * 输入: nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出: 9
 * 
 * 约束条件:
 * 0 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 */
public class Code15_LeetCode128 {
    
    /**
     * 方法1: 使用并查集解决最长连续序列问题
     * 时间复杂度: O(n * α(n)) ≈ O(n)，其中α是阿克曼函数的反函数
     * 空间复杂度: O(n)
     * 
     * 解题思路:
     * 1. 使用哈希表记录每个数字对应的并查集节点
     * 2. 对于每个数字，检查其相邻数字是否存在，如果存在则合并集合
     * 3. 记录每个集合的大小，返回最大集合的大小
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        UnionFind uf = new UnionFind(nums);
        
        // 遍历数组，合并相邻数字
        for (int num : nums) {
            // 如果存在num-1，则合并num和num-1
            if (uf.contains(num - 1)) {
                uf.union(num, num - 1);
            }
            // 如果存在num+1，则合并num和num+1
            if (uf.contains(num + 1)) {
                uf.union(num, num + 1);
            }
        }
        
        return uf.getMaxSize();
    }
    
    /**
     * 并查集实现，专门用于处理整数序列
     */
    static class UnionFind {
        private Map<Integer, Integer> parent;  // 存储父节点
        private Map<Integer, Integer> size;    // 存储集合大小
        private int maxSize;                   // 最大集合大小
        
        public UnionFind(int[] nums) {
            parent = new HashMap<>();
            size = new HashMap<>();
            maxSize = 0;
            
            // 初始化并查集
            for (int num : nums) {
                parent.put(num, num);
                size.put(num, 1);
            }
            if (!nums.isEmpty()) {
                maxSize = 1;
            }
        }
        
        /**
         * 检查数字是否存在于并查集中
         */
        public boolean contains(int num) {
            return parent.containsKey(num);
        }
        
        /**
         * 查找操作，使用路径压缩优化
         */
        public int find(int x) {
            if (parent.get(x) != x) {
                parent.put(x, find(parent.get(x)));  // 路径压缩
            }
            return parent.get(x);
        }
        
        /**
         * 合并操作
         */
        public void union(int x, int y) {
            if (!contains(x) || !contains(y)) {
                return;
            }
            
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按大小合并，小树合并到大树下
                if (size.get(rootX) < size.get(rootY)) {
                    parent.put(rootX, rootY);
                    size.put(rootY, size.get(rootY) + size.get(rootX));
                    maxSize = Math.max(maxSize, size.get(rootY));
                } else {
                    parent.put(rootY, rootX);
                    size.put(rootX, size.get(rootX) + size.get(rootY));
                    maxSize = Math.max(maxSize, size.get(rootX));
                }
            }
        }
        
        /**
         * 获取最大集合大小
         */
        public int getMaxSize() {
            return maxSize;
        }
    }
    
    /**
     * 方法2: 使用哈希表 + 遍历的优化解法
     * 时间复杂度: O(n)
     * 空间复杂度: O(n)
     * 
     * 解题思路:
     * 1. 将所有数字存入哈希表
     * 2. 遍历数组，对于每个数字，如果它是序列的起点（即num-1不存在），则向后查找连续序列
     * 3. 记录最长序列长度
     */
    public int longestConsecutiveHashSet(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        
        int longestStreak = 0;
        
        for (int num : numSet) {
            // 只有当num是序列的起点时才进行查找
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;
                
                // 向后查找连续序列
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }
                
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }
        
        return longestStreak;
    }
    
    /**
     * 方法3: 排序解法（不满足O(n)时间复杂度要求，但思路简单）
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(1) 或 O(n)（取决于排序算法）
     */
    public int longestConsecutiveSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        Arrays.sort(nums);
        
        int longestStreak = 1;
        int currentStreak = 1;
        
        for (int i = 1; i < nums.length; i++) {
            // 处理重复数字
            if (nums[i] != nums[i - 1]) {
                // 检查是否连续
                if (nums[i] == nums[i - 1] + 1) {
                    currentStreak++;
                } else {
                    longestStreak = Math.max(longestStreak, currentStreak);
                    currentStreak = 1;
                }
            }
            // 如果数字重复，保持currentStreak不变
        }
        
        return Math.max(longestStreak, currentStreak);
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code15_LeetCode128 solution = new Code15_LeetCode128();
        
        // 测试用例1
        int[] nums1 = {100, 4, 200, 1, 3, 2};
        System.out.println("测试用例1 - 并查集解法: " + solution.longestConsecutive(nums1)); // 预期: 4
        System.out.println("测试用例1 - 哈希表解法: " + solution.longestConsecutiveHashSet(nums1)); // 预期: 4
        System.out.println("测试用例1 - 排序解法: " + solution.longestConsecutiveSort(nums1)); // 预期: 4
        
        // 测试用例2
        int[] nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        System.out.println("测试用例2 - 并查集解法: " + solution.longestConsecutive(nums2)); // 预期: 9
        System.out.println("测试用例2 - 哈希表解法: " + solution.longestConsecutiveHashSet(nums2)); // 预期: 9
        System.out.println("测试用例2 - 排序解法: " + solution.longestConsecutiveSort(nums2)); // 预期: 9
        
        // 测试用例3: 空数组
        int[] nums3 = {};
        System.out.println("测试用例3 - 并查集解法: " + solution.longestConsecutive(nums3)); // 预期: 0
        System.out.println("测试用例3 - 哈希表解法: " + solution.longestConsecutiveHashSet(nums3)); // 预期: 0
        System.out.println("测试用例3 - 排序解法: " + solution.longestConsecutiveSort(nums3)); // 预期: 0
        
        // 测试用例4: 单个元素
        int[] nums4 = {5};
        System.out.println("测试用例4 - 并查集解法: " + solution.longestConsecutive(nums4)); // 预期: 1
        System.out.println("测试用例4 - 哈希表解法: " + solution.longestConsecutiveHashSet(nums4)); // 预期: 1
        System.out.println("测试用例4 - 排序解法: " + solution.longestConsecutiveSort(nums4)); // 预期: 1
        
        // 测试用例5: 重复元素
        int[] nums5 = {1, 2, 0, 1};
        System.out.println("测试用例5 - 并查集解法: " + solution.longestConsecutive(nums5)); // 预期: 3
        System.out.println("测试用例5 - 哈希表解法: " + solution.longestConsecutiveHashSet(nums5)); // 预期: 3
        System.out.println("测试用例5 - 排序解法: " + solution.longestConsecutiveSort(nums5)); // 预期: 3
    }
}