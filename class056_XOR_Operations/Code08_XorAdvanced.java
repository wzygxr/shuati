package class030;

/**
 * 异或运算高级应用
 * 
 * 本文件包含一些更复杂的异或运算题目，展示异或在算法中的高级应用
 */
public class Code08_XorAdvanced {

    /**
     * 题目1: 只出现一次的数字 II
     * 
     * 题目来源: LeetCode 137. Single Number II
     * 链接: https://leetcode.cn/problems/single-number-ii/
     * 
     * 题目描述:
     * 给你一个整数数组 nums ，除某个元素仅出现一次外，其余每个元素都恰出现三次。
     * 请你找出并返回那个只出现了一次的元素。
     * 
     * 解题思路:
     * 使用位运算统计每一位上1出现的次数，如果某一位上1出现的次数不是3的倍数，
     * 说明单独的数在该位上是1。
     * 
     * 时间复杂度: O(n) - 需要遍历数组一次，每次处理32位
     * 空间复杂度: O(1) - 只使用固定大小的数组
     * 
     * @param nums 输入数组
     * @return 只出现一次的元素
     */
    public static int singleNumberII(int[] nums) {
        // 统计每一位上1出现的次数
        int[] count = new int[32];
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                // 统计第i位上1的个数
                count[i] += (num >> i) & 1;
            }
        }

        int result = 0;
        for (int i = 0; i < 32; i++) {
            // 如果第i位上1的个数不是3的倍数，说明目标数在该位上是1
            if (count[i] % 3 != 0) {
                result |= (1 << i);
            }
        }

        return result;
    }

    /**
     * 题目2: 最大异或值
     * 
     * 题目来源: LeetCode 421. Maximum XOR of Two Numbers in an Array
     * 链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
     * 
     * 题目描述:
     * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，
     * 其中 0 ≤ i ≤ j < n 。
     * 
     * 解题思路:
     * 使用贪心策略配合前缀树(Trie)：
     * 1. 从最高位开始构建前缀树
     * 2. 对于每个数，尝试找到与其异或结果最大的数
     * 3. 贪心策略：在前缀树中尽量走相反的位（0走1，1走0）
     * 
     * 时间复杂度: O(n) - 需要遍历数组两次，每次处理32位
     * 空间复杂度: O(n) - 前缀树的空间
     * 
     * @param nums 输入数组
     * @return 最大异或值
     */
    public static int findMaximumXOR(int[] nums) {
        // 构建前缀树的根节点
        TrieNode root = new TrieNode();

        // 1. 将所有数字插入前缀树
        for (int num : nums) {
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

        int maxXOR = 0;
        // 2. 对每个数字，在前缀树中寻找能产生最大异或值的路径
        for (int num : nums) {
            TrieNode node = root;
            int currentXOR = 0;
            // 从最高位开始处理
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                // 贪心策略：尽量走相反的位
                int desiredBit = 1 - bit;
                if (node.children[desiredBit] != null) {
                    // 能走相反的位，该位异或结果为1
                    currentXOR |= (1 << i);
                    node = node.children[desiredBit];
                } else {
                    // 只能走相同的位，该位异或结果为0
                    node = node.children[bit];
                }
            }
            maxXOR = Math.max(maxXOR, currentXOR);
        }

        return maxXOR;
    }

    // 前缀树节点类
    static class TrieNode {
        TrieNode[] children = new TrieNode[2]; // 0和1两个子节点
    }

    /**
     * 题目3: 子数组异或查询
     * 
     * 题目来源: LeetCode 1310. XOR Queries of a Subarray
     * 链接: https://leetcode.cn/problems/xor-queries-of-a-subarray/
     * 
     * 题目描述:
     * 给你一个数组 arr 和一个整数 queries，其中 queries[i] = [Li, Ri]。
     * 对于每个查询 i，计算从 Li 到 Ri 的 XOR 值（即 arr[Li] xor arr[Li+1] xor ... xor arr[Ri]）
     * 作为本次查询的结果。
     * 
     * 解题思路:
     * 利用前缀异或的思想：
     * 1. 构建前缀异或数组 prefixXOR，其中 prefixXOR[i] 表示 arr[0] 到 arr[i-1] 的异或结果
     * 2. 对于查询 [L, R]，结果为 prefixXOR[R+1] ^ prefixXOR[L]
     *    原理：(a[0]^...^a[L-1]) ^ (a[0]^...^a[L-1]^a[L]^...^a[R]) = a[L]^...^a[R]
     * 
     * 时间复杂度: O(n + q) - n为数组长度，q为查询次数
     * 空间复杂度: O(n) - 前缀异或数组的空间
     * 
     * @param arr 输入数组
     * @param queries 查询数组
     * @return 查询结果数组
     */
    public static int[] xorQueries(int[] arr, int[][] queries) {
        int n = arr.length;
        // 构建前缀异或数组，prefixXOR[0] = 0
        int[] prefixXOR = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixXOR[i + 1] = prefixXOR[i] ^ arr[i];
        }

        int[] result = new int[queries.length];
        // 处理每个查询
        for (int i = 0; i < queries.length; i++) {
            int left = queries[i][0];
            int right = queries[i][1];
            // 利用前缀异或的性质计算区间异或结果
            result[i] = prefixXOR[right + 1] ^ prefixXOR[left];
        }

        return result;
    }

    /**
     * 题目4: 数字的补数
     * 
     * 题目来源: LeetCode 476. Number Complement
     * 链接: https://leetcode.cn/problems/number-complement/
     * 
     * 题目描述:
     * 对整数的二进制表示取反（0 变 1，1 变 0）后，再转换为十进制表示，可以得到这个整数的补数。
     * 例如，整数 5 的二进制表示是 "101"，取反后得到 "010"，再转回十进制表示得到补数 2。
     * 给你一个整数 num，输出它的补数。
     * 
     * 解题思路:
     * 1. 构造一个掩码，该掩码的位数与 num 相同，但所有位都是1
     * 2. 使用异或操作将 num 与掩码进行运算，实现取反效果
     * 
     * 时间复杂度: O(log n) - n为num的值
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param num 输入数字
     * @return 补数
     */
    public static int findComplement(int num) {
        // 构造掩码
        int mask = 1;
        while (mask < num) {
            mask = (mask << 1) | 1;
        }
        // 使用异或操作取反
        return num ^ mask;
    }

    /**
     * 题目5: 交替位二进制数
     * 
     * 题目来源: LeetCode 693. Binary Number with Alternating Bits
     * 链接: https://leetcode.cn/problems/binary-number-with-alternating-bits/
     * 
     * 题目描述:
     * 给定一个正整数，检查它的二进制表示是否总是 0、1 交替出现：
     * 换句话说，就是二进制表示中相邻两位的数字永不相同。
     * 
     * 解题思路:
     * 1. 利用异或运算的性质：如果一个数是交替位二进制数，
     *    那么它与自己右移一位后的数进行异或，结果的二进制应该全为1
     * 2. 检查一个二进制全为1的数加1后是否为2的幂（即只有最高位为1，其余为0）
     * 
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param n 输入数字
     * @return 是否为交替位二进制数
     */
    public static boolean hasAlternatingBits(int n) {
        // 如果是交替位，那么 n ^ (n >> 1) 的二进制应该全为1
        int xor = n ^ (n >> 1);
        // 检查 xor & (xor + 1) 是否为0（判断是否全为1）
        return (xor & (xor + 1)) == 0;
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试 singleNumberII 方法
        int[] nums1 = {2, 2, 3, 2};
        System.out.println("singleNumberII([2,2,3,2]): " + singleNumberII(nums1)); // 应该输出 3

        // 测试 findMaximumXOR 方法
        int[] nums2 = {3, 10, 5, 25, 2, 8};
        System.out.println("findMaximumXOR([3,10,5,25,2,8]): " + findMaximumXOR(nums2)); // 应该输出 28 (5^25)

        // 测试 xorQueries 方法
        int[] arr = {1, 3, 4, 8};
        int[][] queries = {{0, 1}, {1, 2}, {0, 3}, {3, 3}};
        int[] result = xorQueries(arr, queries);
        System.out.print("xorQueries([1,3,4,8], [[0,1],[1,2],[0,3],[3,3]]): ");
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println(); // 应该输出 2 7 14 8

        // 测试 findComplement 方法
        System.out.println("findComplement(5): " + findComplement(5)); // 应该输出 2
        // 5 的二进制: 101
        // 补数的二进制: 010
        // 补数的十进制: 2

        // 测试 hasAlternatingBits 方法
        System.out.println("hasAlternatingBits(5): " + hasAlternatingBits(5)); // 应该输出 true
        // 5 的二进制: 101 (交替位)
        System.out.println("hasAlternatingBits(7): " + hasAlternatingBits(7)); // 应该输出 false
        // 7 的二进制: 111 (非交替位)
    }
}