package class030;

/**
 * 异或运算经典题目扩展
 * 
 * 本文件包含多个使用异或运算解决的经典算法题目，每个题目都有详细的注释说明
 * 和复杂度分析，帮助深入理解异或运算在算法中的应用。
 */

public class Code07_XorInRange {

    /**
     * 题目1: 数组中唯一出现一次的元素 (其他元素都出现两次)
     * 
     * 题目来源: LeetCode 136. Single Number
     * 链接: https://leetcode.cn/problems/single-number/
     * 
     * 题目描述:
     * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。
     * 找出那个只出现了一次的元素。
     * 
     * 解题思路:
     * 利用异或运算的性质:
     * 1. 任何数与自身异或结果为0 (a ^ a = 0)
     * 2. 任何数与0异或结果为其本身 (a ^ 0 = a)
     * 3. 异或运算满足交换律和结合律
     * 
     * 因此，将数组中所有元素进行异或运算，出现两次的元素会相互抵消为0，
     * 最终只剩下出现一次的元素。
     * 
     * 时间复杂度: O(n) - 需要遍历数组一次
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param nums 输入数组
     * @return 只出现一次的元素
     */
    public static int singleNumber(int[] nums) {
        // 利用异或运算的性质，所有出现两次的数会相互抵消
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }

    /**
     * 题目2: 数组中两个只出现一次的元素 (其他元素都出现两次)
     * 
     * 题目来源: LeetCode 260. Single Number III
     * 链接: https://leetcode.cn/problems/single-number-iii/
     * 
     * 题目描述:
     * 给定一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。
     * 找出只出现一次的那两个元素。你可以按任意顺序返回答案。
     * 
     * 解题思路:
     * 1. 首先对所有元素进行异或运算，得到两个只出现一次的数的异或结果eor
     * 2. 利用Brian Kernighan算法找到eor中最右边的1位，记为rightOne
     *    这一位为1说明两个目标数在该位上不同（一个为0，一个为1）
     * 3. 根据该位是否为1将数组分为两组，两个目标数分别在两组中
     * 4. 对每组分别进行异或运算，得到两个目标数
     * 
     * 时间复杂度: O(n) - 需要遍历数组两次
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param nums 输入数组
     * @return 包含两个只出现一次元素的数组
     */
    public static int[] singleNumberIII(int[] nums) {
        // 1. 对所有数字进行异或运算，得到两个目标数的异或结果
        int eor = 0;
        for (int num : nums) {
            eor ^= num;
        }

        // 2. 找到最右边的1位 (Brian Kernighan算法)
        // 这一位为1说明两个目标数在该位上不同
        int rightOne = eor & (-eor);

        // 3. 根据该位是否为1将数组分为两组，分别异或得到两个目标数
        int eor1 = 0;
        for (int num : nums) {
            // 将该位为0的数分为一组进行异或
            if ((num & rightOne) == 0) {
                eor1 ^= num;
            }
        }

        // 4. 利用异或的性质计算另一个数: eor = eor1 ^ eor2 => eor2 = eor ^ eor1
        int eor2 = eor ^ eor1;

        return new int[] { eor1, eor2 };
    }

    /**
     * 题目3: 缺失的数字
     * 
     * 题目来源: LeetCode 268. Missing Number
     * 链接: https://leetcode.cn/problems/missing-number/
     * 
     * 题目描述:
     * 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
     * 
     * 解题思路:
     * 1. 利用异或运算，将0到n的所有数字与数组中的所有数字进行异或
     * 2. 出现两次的数字会相互抵消为0，最终只剩下缺失的数字
     * 
     * 时间复杂度: O(n) - 需要遍历数组一次
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param nums 输入数组
     * @return 缺失的数字
     */
    public static int missingNumber(int[] nums) {
        int n = nums.length;
        int result = n; // 初始值设为n，因为循环中不会处理到索引n

        // 将索引和对应的数组元素进行异或
        for (int i = 0; i < n; i++) {
            result ^= i ^ nums[i];
        }

        return result;
    }

    /**
     * 题目4: 汉明距离
     * 
     * 题目来源: LeetCode 461. Hamming Distance
     * 链接: https://leetcode.cn/problems/hamming-distance/
     * 
     * 题目描述:
     * 两个整数之间的汉明距离指的是这两个数字对应二进制位不同的位置的数目。
     * 给出两个整数 x 和 y，计算并返回它们之间的汉明距离。
     * 
     * 解题思路:
     * 1. 对两个数字进行异或运算，相同位为0，不同位为1
     * 2. 统计异或结果中1的个数，即为汉明距离
     * 
     * 时间复杂度: O(1) - 固定32位
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param x 第一个整数
     * @param y 第二个整数
     * @return 汉明距离
     */
    public static int hammingDistance(int x, int y) {
        // 1. 异或运算找出不同的位
        int xor = x ^ y;
        int distance = 0;

        // 2. 统计1的个数 (Brian Kernighan算法)
        while (xor != 0) {
            distance++;
            xor &= (xor - 1); // 清除最右边的1
        }

        return distance;
    }

    /**
     * 题目5: 数组异或操作
     * 
     * 题目来源: LeetCode 1486. XOR Operation in an Array
     * 链接: https://leetcode.cn/problems/xor-operation-in-an-array/
     * 
     * 题目描述:
     * 给你两个整数，n 和 start 。数组 nums 定义为：nums[i] = start + 2*i（下标从 0 开始）且 n == nums.length 。
     * 请返回 nums 中所有元素按位异或（XOR）后得到的结果。
     * 
     * 解题思路:
     * 直接按照题目要求构造数组并进行异或运算
     * 
     * 时间复杂度: O(n) - 需要计算n次
     * 空间复杂度: O(1) - 只使用常数额外空间
     * 
     * @param n 数组长度
     * @param start 起始值
     * @return 所有元素异或后的结果
     */
    public static int xorOperation(int n, int start) {
        int result = 0;
        // 按照公式 nums[i] = start + 2*i 计算每个元素并异或
        for (int i = 0; i < n; i++) {
            result ^= (start + 2 * i);
        }
        return result;
    }

    /**
     * 题目6: 交换两个数（不使用额外变量）
     * 
     * 题目描述:
     * 在不使用临时变量的情况下交换两个数的值
     * 
     * 解题思路:
     * 利用异或运算的性质: a ^ b ^ b = a
     * 
     * 步骤:
     * 1. a = a ^ b
     * 2. b = a ^ b (实际上是原a ^ b ^ 原b = 原a)
     * 3. a = a ^ b (实际上是原a ^ b ^ 原a = 原b)
     * 
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * 注意: 当两个数相等或指向同一内存地址时，会出现问题（变为0）
     * 
     * @param arr 包含两个元素的数组
     * @param i 第一个元素的索引
     * @param j 第二个元素的索引
     */
    public static void swap(int[] arr, int i, int j) {
        // 注意：当i==j时，会导致该位置变为0，所以需要特殊处理
        if (i != j) {
            arr[i] = arr[i] ^ arr[j];
            arr[j] = arr[i] ^ arr[j];
            arr[i] = arr[i] ^ arr[j];
        }
    }

    // 测试方法
    public static void main(String[] args) {
        // 测试 singleNumber 方法
        int[] nums1 = {2, 2, 1};
        System.out.println("singleNumber([2,2,1]): " + singleNumber(nums1)); // 应该输出 1

        // 测试 singleNumberIII 方法
        int[] nums2 = {1, 2, 1, 3, 2, 5};
        int[] result = singleNumberIII(nums2);
        System.out.println("singleNumberIII([1,2,1,3,2,5]): [" + result[0] + ", " + result[1] + "]"); // 应该输出 [3, 5] 或 [5, 3]

        // 测试 missingNumber 方法
        int[] nums3 = {3, 0, 1};
        System.out.println("missingNumber([3,0,1]): " + missingNumber(nums3)); // 应该输出 2

        // 测试 hammingDistance 方法
        System.out.println("hammingDistance(1, 4): " + hammingDistance(1, 4)); // 应该输出 2
        // 1 的二进制: 0001
        // 4 的二进制: 0100
        // 不同的位:   2位

        // 测试 xorOperation 方法
        System.out.println("xorOperation(5, 0): " + xorOperation(5, 0)); // 应该输出 8
        // 数组为 [0, 2, 4, 6, 8]
        // 0 ^ 2 ^ 4 ^ 6 ^ 8 = 8

        // 测试 swap 方法
        int[] arr = {1, 2};
        System.out.println("交换前: [" + arr[0] + ", " + arr[1] + "]");
        swap(arr, 0, 1);
        System.out.println("交换后: [" + arr[0] + ", " + arr[1] + "]");
    }
}