package class031;

// 数组中两个数的最大异或值 - Maximum XOR of Two Numbers in an Array
// 测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// 相关题目:
// 1. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
// 2. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
// 3. 只出现一次的数字 - Single Number: https://leetcode.cn/problems/single-number/
// 4. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
// 5. 位运算技巧大全 - Bit Manipulation Tricks

/*
题目描述：
给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

示例：
输入：nums = [3,10,5,25,2,8]
输出：28
解释：最大运算结果是 5 XOR 25 = 28.

输入：nums = [0]
输出：0

输入：nums = [2,4]
输出：6

输入：nums = [8,10,2]
输出：10

输入：nums = [14,70,53,83,49,91,36,80,92,51,66,70]
输出：127

提示：
1 <= nums.length <= 2 * 10^5
0 <= nums[i] <= 2^31 - 1

进阶：你可以在 O(n) 的时间解决这个问题吗？

解题思路：
这是一道经典的位运算题目，可以使用贪心算法和字典树(Trie)来解决。

方法1：暴力解法
遍历所有可能的数对，计算它们的异或值，返回最大值。
时间复杂度：O(n^2)
空间复杂度：O(1)

方法2：贪心算法 + 字典树（推荐）
核心思想：要使异或结果最大，应该从最高位开始，尽可能使对应位上的数字不同。
1. 构建字典树：将所有数字的二进制表示（从高位到低位）插入到字典树中
2. 贪心查找：对于每个数字，在字典树中寻找与其异或结果最大的数字
   - 从高位开始遍历，如果当前位可以与目标数字的对应位不同，则选择不同的路径
   - 这样可以保证异或结果在当前位为1，从而最大化结果

例如：对于数组 [3,10,5,25,2,8]
3  的二进制：00011
10 的二进制：01010
5  的二进制：00101
25 的二进制：11001
2  的二进制：00010
8  的二进制：01000

要使异或结果最大，应该选择在高位上数字不同的两个数。
25(11001) 和 5(00101) 在第4位不同，异或结果在该位为1，这样可以获得较大的结果。

时间复杂度：O(n * 32) = O(n)，其中n是数组长度，32是整数的位数
空间复杂度：O(n * 32) = O(n)，字典树的空间消耗

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
*/
public class Code13_MaximumXORofTwoNumbersInArray {
    
    // 字典树节点
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
    }
    
    // 字典树根节点
    private TrieNode root = new TrieNode();

    /**
     * 找出数组中两个数的最大异或值
     * 使用贪心算法 + 字典树：
     * 1. 将所有数字插入字典树
     * 2. 对于每个数字，在字典树中寻找与其异或结果最大的数字
     * 
     * @param nums 输入数组
     * @return 最大异或值
     */
    public int findMaximumXOR(int[] nums) {
        // 特殊情况处理
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        // 将所有数字插入字典树
        for (int num : nums) {
            insert(num);
        }
        
        int maxXOR = 0;
        // 对于每个数字，查找与其异或结果最大的数字
        for (int num : nums) {
            maxXOR = Math.max(maxXOR, findMaxXOR(num));
        }
        
        return maxXOR;
    }
    
    /**
     * 将数字插入字典树
     * 从最高位开始处理（第31位到第0位）
     * 
     * @param num 要插入的数字
     */
    private void insert(int num) {
        TrieNode node = root;
        // 从最高位开始处理（第31位到第0位）
        for (int i = 31; i >= 0; i--) {
            // 提取第i位的值（0或1）
            int bit = (num >> i) & 1;
            // 如果对应子节点不存在，则创建新节点
            if (node.children[bit] == null) {
                node.children[bit] = new TrieNode();
            }
            // 移动到子节点
            node = node.children[bit];
        }
    }
    
    /**
     * 查找与给定数字异或结果最大的数字
     * 贪心策略：从高位开始，选择与当前位不同的路径（使异或结果在该位为1）
     * 
     * @param num 给定数字
     * @return 最大异或值
     */
    private int findMaxXOR(int num) {
        TrieNode node = root;
        int maxXOR = 0;
        // 从最高位开始处理（第31位到第0位）
        for (int i = 31; i >= 0; i--) {
            // 提取第i位的值（0或1）
            int bit = (num >> i) & 1;
            // 贪心策略：选择与当前位不同的路径（使异或结果在该位为1）
            int oppositeBit = bit ^ 1;
            
            // 如果与当前位不同的路径存在，则选择该路径
            if (node.children[oppositeBit] != null) {
                // 设置异或结果在该位为1
                maxXOR |= (1 << i);
                node = node.children[oppositeBit];
            } else {
                // 否则选择相同的路径
                node = node.children[bit];
            }
        }
        return maxXOR;
    }
    
    // 暴力解法（时间复杂度较高，仅用于小规模数据）
    // public int findMaximumXOR(int[] nums) {
    //     int maxXOR = 0;
    //     for (int i = 0; i < nums.length; i++) {
    //         for (int j = i + 1; j < nums.length; j++) {
    //             maxXOR = Math.max(maxXOR, nums[i] ^ nums[j]);
    //         }
    //     }
    //     return maxXOR;
    // }
    
    // 测试方法
    public static void main(String[] args) {
        Code13_MaximumXORofTwoNumbersInArray solution = new Code13_MaximumXORofTwoNumbersInArray();
        
        int[] test1 = {3, 10, 5, 25, 2, 8};
        int[] test2 = {0};
        int[] test3 = {2, 4};
        int[] test4 = {8, 10, 2};
        int[] test5 = {14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70};
        
        System.out.println("Test 1: " + solution.findMaximumXOR(test1)); // 输出: 28
        System.out.println("Test 2: " + solution.findMaximumXOR(test2)); // 输出: 0
        System.out.println("Test 3: " + solution.findMaximumXOR(test3)); // 输出: 6
        System.out.println("Test 4: " + solution.findMaximumXOR(test4)); // 输出: 10
        System.out.println("Test 5: " + solution.findMaximumXOR(test5)); // 输出: 127
    }

}