package class030.xor_problems_solutions;

/**
 * 题目: LeetCode 421. Maximum XOR of Two Numbers in an Array
 * 链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 题目描述:
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。
 * 
 * 解题思路:
 * 使用前缀树(Trie)结构：
 * 1. 将数组中每个数字的二进制表示插入前缀树中
 * 2. 对于每个数字，在前缀树中查找能与之产生最大异或值的路径
 * 3. 贪心策略：对于每一位，尽量寻找相反的位以最大化异或结果
 * 
 * 时间复杂度: O(n * 32) = O(n) - 每个数字处理32位
 * 空间复杂度: O(n * 32) = O(n) - 前缀树存储
 */
public class LeetCode421_MaximumXOR {
    
    // 前缀树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
    }
    
    /**
     * 找到数组中两个数的最大异或值
     * 
     * @param nums 输入数组
     * @return 最大异或值
     */
    public int findMaximumXOR(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 构建前缀树
        TrieNode root = new TrieNode();
        
        // 将所有数字插入前缀树
        for (int num : nums) {
            insert(root, num);
        }
        
        int maxResult = 0;
        // 对于每个数字，在前缀树中寻找能产生最大异或值的数字
        for (int num : nums) {
            int currentMax = findMaxXor(root, num);
            maxResult = Math.max(maxResult, currentMax);
        }
        
        return maxResult;
    }
    
    // 向前缀树中插入数字
    private void insert(TrieNode root, int num) {
        TrieNode node = root;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            node = node.children[bit];
        }
    }
    
    // 在前缀树中查找与num异或能得到最大值的数字
    private int findMaxXor(TrieNode root, int num) {
        TrieNode node = root;
        int maxXor = 0;
        // 从最高位开始处理
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            // 贪心策略：尽量走相反的位
            int desiredBit = 1 - bit;
            if (node.children[desiredBit] != null) {
                // 能走相反的位，该位异或结果为1
                maxXor |= (1 << i);
                node = node.children[desiredBit];
            } else {
                // 只能走相同的位，该位异或结果为0
                node = node.children[bit];
            }
        }
        return maxXor;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode421_MaximumXOR solution = new LeetCode421_MaximumXOR();
        
        // 测试用例1
        int[] nums1 = {3, 10, 5, 25, 2, 8};
        System.out.println("输入: [3,10,5,25,2,8]");
        System.out.println("输出: " + solution.findMaximumXOR(nums1)); // 应该输出 28 (5^25)
        
        // 测试用例2
        int[] nums2 = {0};
        System.out.println("输入: [0]");
        System.out.println("输出: " + solution.findMaximumXOR(nums2)); // 应该输出 0
        
        // 测试用例3
        int[] nums3 = {2, 4};
        System.out.println("输入: [2,4]");
        System.out.println("输出: " + solution.findMaximumXOR(nums3)); // 应该输出 6 (2^4)
    }
}