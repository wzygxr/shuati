/**
 * LeetCode 128 - 最长连续序列
 * https://leetcode-cn.com/problems/longest-consecutive-sequence/
 * 
 * 题目描述：
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例 1：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 示例 2：
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 * 
 * 解题思路（使用并查集）：
 * 1. 使用并查集将连续的数字合并到同一个集合中
 * 2. 对于每个数字，如果它的前驱（num-1）存在，就将它们合并
 * 3. 统计每个集合的大小，找出最大的集合大小
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每个数字：O(n * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计最大集合大小：O(n)
 * - 总体时间复杂度：O(n * α(n)) ≈ O(n)
 * 
 * 空间复杂度分析：
 * - 并查集映射和大小映射：O(n)
 * - 总体空间复杂度：O(n)
 */

import java.util.*;

public class Code31_LongestConsecutiveSequence {
    // 并查集的父节点映射
    private Map<Integer, Integer> parent;
    // 每个集合的大小映射
    private Map<Integer, Integer> size;
    
    /**
     * 初始化并查集
     */
    public void initUnionFind() {
        parent = new HashMap<>();
        size = new HashMap<>();
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    public int find(int x) {
        if (parent.get(x) != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent.put(x, find(parent.get(x)));
        }
        return parent.get(x);
    }
    
    /**
     * 添加元素到并查集
     * @param x 要添加的元素
     */
    public void add(int x) {
        if (!parent.containsKey(x)) {
            parent.put(x, x); // 初始时，元素的父节点是自己
            size.put(x, 1); // 初始时，集合的大小为1
        }
    }
    
    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    public void union(int x, int y) {
        // 确保x和y都在并查集中
        add(x);
        add(y);
        
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return; // 已经在同一个集合中
        }
        
        // 将较小的集合合并到较大的集合中，以保持树的平衡
        if (size.get(rootX) < size.get(rootY)) {
            parent.put(rootX, rootY);
            size.put(rootY, size.get(rootY) + size.get(rootX));
        } else {
            parent.put(rootY, rootX);
            size.put(rootX, size.get(rootX) + size.get(rootY));
        }
    }
    
    /**
     * 找出最长连续序列的长度
     * @param nums 整数数组
     * @return 最长连续序列的长度
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 初始化并查集
        initUnionFind();
        
        // 将每个数字及其前驱（如果存在）合并到同一个集合中
        for (int num : nums) {
            add(num);
            // 如果num-1存在，将num和num-1合并
            if (parent.containsKey(num - 1)) {
                union(num, num - 1);
            }
        }
        
        // 找出最大的集合大小
        int maxLength = 0;
        for (int length : size.values()) {
            maxLength = Math.max(maxLength, length);
        }
        
        return maxLength;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code31_LongestConsecutiveSequence solution = new Code31_LongestConsecutiveSequence();
        
        // 测试用例1
        int[] nums1 = {100, 4, 200, 1, 3, 2};
        System.out.println("测试用例1结果：" + solution.longestConsecutive(nums1));
        // 预期输出：4
        
        // 测试用例2
        int[] nums2 = {0, 3, 7, 2, 5, 8, 4, 6, 0, 1};
        System.out.println("测试用例2结果：" + solution.longestConsecutive(nums2));
        // 预期输出：9
        
        // 测试用例3：空数组
        int[] nums3 = {};
        System.out.println("测试用例3结果：" + solution.longestConsecutive(nums3));
        // 预期输出：0
        
        // 测试用例4：单元素数组
        int[] nums4 = {1};
        System.out.println("测试用例4结果：" + solution.longestConsecutive(nums4));
        // 预期输出：1
        
        // 测试用例5：有重复元素的数组
        int[] nums5 = {1, 2, 0, 1};
        System.out.println("测试用例5结果：" + solution.longestConsecutive(nums5));
        // 预期输出：3
    }
    
    /**
     * 优化说明：
     * 1. 使用HashMap实现并查集，以处理任意范围的整数
     * 2. 在合并时根据集合大小进行优化，保持树的平衡
     * 3. 实现了路径压缩，提高查询效率
     * 4. 避免重复处理相同的数字
     * 
     * 时间复杂度分析：
     * - 对于n个不同的数字，每个数字的find和union操作的平均时间复杂度为O(α(n))
     * - 总体时间复杂度为O(n * α(n)) ≈ O(n)
     * 
     * 空间复杂度分析：
     * - HashMap的空间复杂度为O(n)
     * - 总体空间复杂度为O(n)
     */
}