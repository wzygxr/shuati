/**
 * 数组中两个数的最大异或值
 * 测试链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < nums.length 。
 * 
 * 解题思路：
 * 1. 暴力法：双重循环计算所有异或值
 * 2. 字典树法：使用Trie树存储二进制表示
 * 3. 哈希集合法：利用异或性质进行优化
 * 4. 位运算技巧：逐位确定最大值
 * 
 * 时间复杂度：O(n) - 使用字典树或哈希集合
 * 空间复杂度：O(n) - 需要存储字典树或哈希集合
 */
import java.util.*;

public class Code31_MaximumXOROfTwoNumbersInAnArray {
    
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    public int findMaximumXOR1(int[] nums) {
        int max = 0;
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                max = Math.max(max, nums[i] ^ nums[j]);
            }
        }
        return max;
    }
    
    /**
     * 方法2：字典树法（最优解）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int findMaximumXOR2(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 创建字典树
        TrieNode root = new TrieNode();
        
        // 构建字典树
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
        
        int max = 0;
        // 对每个数字，在字典树中寻找最大异或值
        for (int num : nums) {
            TrieNode node = root;
            int currentMax = 0;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int desiredBit = 1 - bit;  // 希望找到相反的位
                
                if (node.children[desiredBit] != null) {
                    currentMax |= (1 << i);
                    node = node.children[desiredBit];
                } else {
                    node = node.children[bit];
                }
            }
            max = Math.max(max, currentMax);
        }
        
        return max;
    }
    
    /**
     * 字典树节点
     */
    class TrieNode {
        TrieNode[] children;
        
        public TrieNode() {
            children = new TrieNode[2];  // 0和1两个分支
        }
    }
    
    /**
     * 方法3：哈希集合法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int findMaximumXOR3(int[] nums) {
        int max = 0, mask = 0;
        
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            Set<Integer> set = new HashSet<>();
            
            // 提取前缀
            for (int num : nums) {
                set.add(num & mask);
            }
            
            // 尝试设置当前位为1
            int temp = max | (1 << i);
            for (int prefix : set) {
                if (set.contains(temp ^ prefix)) {
                    max = temp;
                    break;
                }
            }
        }
        
        return max;
    }
    
    /**
     * 方法4：位运算优化版
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    public int findMaximumXOR4(int[] nums) {
        int maxResult = 0;
        int mask = 0;
        
        // 从最高位开始尝试
        for (int i = 31; i >= 0; i--) {
            // 设置掩码，只保留前i位
            mask = mask | (1 << i);
            
            Set<Integer> leftBits = new HashSet<>();
            for (int num : nums) {
                leftBits.add(num & mask);
            }
            
            // 尝试设置当前位为1
            int greedyTry = maxResult | (1 << i);
            for (int leftBit : leftBits) {
                // 如果存在两个数使得它们的异或等于greedyTry
                if (leftBits.contains(greedyTry ^ leftBit)) {
                    maxResult = greedyTry;
                    break;
                }
            }
        }
        
        return maxResult;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code31_MaximumXOROfTwoNumbersInAnArray solution = new Code31_MaximumXOROfTwoNumbersInAnArray();
        
        // 测试用例1：正常情况
        int[] nums1 = {3, 10, 5, 25, 2, 8};
        int result1 = solution.findMaximumXOR1(nums1);
        int result2 = solution.findMaximumXOR2(nums1);
        int result3 = solution.findMaximumXOR3(nums1);
        int result4 = solution.findMaximumXOR4(nums1);
        System.out.println("测试用例1 - 输入: " + Arrays.toString(nums1));
        System.out.println("方法1结果: " + result1 + " (预期: 28)");
        System.out.println("方法2结果: " + result2 + " (预期: 28)");
        System.out.println("方法3结果: " + result3 + " (预期: 28)");
        System.out.println("方法4结果: " + result4 + " (预期: 28)");
        
        // 测试用例2：边界情况（两个元素）
        int[] nums2 = {1, 2};
        int result5 = solution.findMaximumXOR2(nums2);
        System.out.println("测试用例2 - 输入: " + Arrays.toString(nums2));
        System.out.println("方法2结果: " + result5 + " (预期: 3)");
        
        // 测试用例3：边界情况（一个元素）
        int[] nums3 = {5};
        int result6 = solution.findMaximumXOR2(nums3);
        System.out.println("测试用例3 - 输入: " + Arrays.toString(nums3));
        System.out.println("方法2结果: " + result6 + " (预期: 0)");
        
        // 复杂度分析
        System.out.println("\n=== 复杂度分析 ===");
        System.out.println("方法1 - 暴力法:");
        System.out.println("  时间复杂度: O(n²)");
        System.out.println("  空间复杂度: O(1)");
        
        System.out.println("方法2 - 字典树法:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法3 - 哈希集合法:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        System.out.println("方法4 - 位运算优化版:");
        System.out.println("  时间复杂度: O(n)");
        System.out.println("  空间复杂度: O(n)");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 方法选择：");
        System.out.println("   - 实际工程：方法2（字典树法）最优");
        System.out.println("   - 面试场景：方法2（字典树法）最优");
        System.out.println("2. 性能优化：避免暴力法，使用高效数据结构");
        System.out.println("3. 边界处理：处理空数组和单元素数组");
        System.out.println("4. 可读性：添加详细注释说明算法原理");
        
        // 算法技巧总结
        System.out.println("\n=== 算法技巧总结 ===");
        System.out.println("1. 字典树应用：高效处理二进制前缀");
        System.out.println("2. 贪心策略：从高位到低位确定最大值");
        System.out.println("3. 哈希集合：利用集合特性快速查找");
        System.out.println("4. 位掩码：逐位构建最大异或值");
    }
}