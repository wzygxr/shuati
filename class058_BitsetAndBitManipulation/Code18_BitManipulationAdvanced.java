package class032;

import java.util.*;

/**
 * 高级位操作技巧和复杂问题
 * 题目来源: LeetCode Hard, Codeforces, 面试难题
 * 包含位操作的高级应用和复杂场景
 * 
 * 解题思路:
 * 方法1: 位掩码 + 状态压缩
 * 方法2: 位运算 + 数学技巧
 * 方法3: 分治 + 位操作
 * 方法4: 动态规划 + 位运算
 * 
 * 时间复杂度分析:
 * 方法1: O(2^n) - 状态压缩
 * 方法2: O(n) - 线性扫描
 * 方法3: O(log n) - 分治策略
 * 方法4: O(n * 2^k) - 带约束的DP
 * 
 * 空间复杂度分析:
 * 方法1: O(2^n) - 状态存储
 * 方法2: O(1) - 常数空间
 * 方法3: O(log n) - 递归栈
 * 方法4: O(n * 2^k) - DP数组
 * 
 * 工程化考量:
 * 1. 内存优化: 使用位压缩减少空间占用
 * 2. 性能优化: 利用位运算的并行性
 * 3. 可扩展性: 设计通用的位操作工具类
 * 4. 错误处理: 处理边界情况和异常输入
 */

public class Code18_BitManipulationAdvanced {
    
    /**
     * LeetCode 137. Single Number II - 只出现一次的数字 II
     * 题目链接: https://leetcode.com/problems/single-number-ii/
     * 题目描述: 给定一个非空整数数组，除了某个元素只出现一次外，其余每个元素均出现三次。找出那个只出现一次的元素。
     * 
     * 方法1: 位计数法
     * 统计每一位上1出现的次数，对3取模
     * 时间复杂度: O(32 * n) = O(n)
     * 空间复杂度: O(1)
     */
    public static int singleNumberII1(int[] nums) {
        int result = 0;
        
        // 遍历32位
        for (int i = 0; i < 32; i++) {
            int count = 0;
            
            // 统计第i位为1的数字个数
            for (int num : nums) {
                if (((num >> i) & 1) == 1) {
                    count++;
                }
            }
            
            // 如果count % 3 != 0，说明只出现一次的数字在该位为1
            if (count % 3 != 0) {
                result |= (1 << i);
            }
        }
        
        return result;
    }
    
    /**
     * 方法2: 有限状态自动机
     * 使用两个变量表示三种状态(00, 01, 10)
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int singleNumberII2(int[] nums) {
        int ones = 0, twos = 0;
        
        for (int num : nums) {
            // ones & num: 计算出现两次的位
            // twos & ~num: 消除出现三次的位
            ones = (ones ^ num) & ~twos;
            twos = (twos ^ num) & ~ones;
        }
        
        return ones;
    }
    
    /**
     * LeetCode 260. Single Number III - 只出现一次的数字 III
     * 题目链接: https://leetcode.com/problems/single-number-iii/
     * 题目描述: 给定一个整数数组，其中恰好有两个元素只出现一次，其余所有元素均出现两次。找出只出现一次的那两个元素。
     * 
     * 方法: 分组异或
     * 时间复杂度: O(n)
     * 空间复杂度: O(1)
     */
    public static int[] singleNumberIII(int[] nums) {
        // 第一步: 所有数字异或，得到两个不同数字的异或结果
        int xor = 0;
        for (int num : nums) {
            xor ^= num;
        }
        
        // 第二步: 找到xor中最低位的1（这个1表示两个数字在该位不同）
        int mask = xor & (-xor);  // 获取最低位的1
        
        // 第三步: 根据mask将数组分成两组，分别异或
        int[] result = new int[2];
        for (int num : nums) {
            if ((num & mask) == 0) {
                result[0] ^= num;
            } else {
                result[1] ^= num;
            }
        }
        
        return result;
    }
    
    /**
     * LeetCode 421. Maximum XOR of Two Numbers in an Array - 数组中两个数的最大异或值
     * 题目链接: https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
     * 题目描述: 给定一个非空数组，返回数组中两个数的最大异或值
     * 
     * 方法1: 暴力法
     * 时间复杂度: O(n^2)
     * 空间复杂度: O(1)
     */
    public static int findMaximumXOR1(int[] nums) {
        int maxXor = 0;
        int n = nums.length;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                maxXor = Math.max(maxXor, nums[i] ^ nums[j]);
            }
        }
        
        return maxXor;
    }
    
    /**
     * 方法2: 前缀树（Trie）
     * 时间复杂度: O(32 * n) = O(n)
     * 空间复杂度: O(32 * n) = O(n)
     */
    public static int findMaximumXOR2(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        
        // 构建前缀树
        TrieNode root = new TrieNode();
        
        // 插入所有数字
        for (int num : nums) {
            TrieNode node = root;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (node.children[bit] == null) {
                    node.children[bit] = new TrieNode();
                }
                node = node.children[bit];
            }
        }
        
        // 查找最大异或值
        int maxXor = 0;
        for (int num : nums) {
            TrieNode node = root;
            int currentXor = 0;
            
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int oppositeBit = 1 - bit;  // 希望走相反的位
                
                if (node.children[oppositeBit] != null) {
                    currentXor |= (1 << i);
                    node = node.children[oppositeBit];
                } else {
                    node = node.children[bit];
                }
            }
            
            maxXor = Math.max(maxXor, currentXor);
        }
        
        return maxXor;
    }
    
    // 前缀树节点定义
    static class TrieNode {
        TrieNode[] children;
        
        public TrieNode() {
            children = new TrieNode[2];  // 0和1两个分支
        }
    }
    
    // 继续添加其他方法...