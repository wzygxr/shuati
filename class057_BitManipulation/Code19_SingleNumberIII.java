package class031;

// 只出现一次的数字 III
// 测试链接 : https://leetcode.cn/problems/single-number-iii/
/*
题目描述：
给你一个整数数组 nums，其中恰好有两个元素只出现一次，其余所有元素均出现两次。 找出只出现一次的那两个元素。你可以按任意顺序返回答案。

示例：
输入：nums = [1,2,1,3,2,5]
输出：[3,5] 或 [5,3]

输入：nums = [-1,0]
输出：[-1,0]

输入：nums = [0,1]
输出：[1,0]

提示：
2 <= nums.length <= 3 * 10^4
-2^31 <= nums[i] <= 2^31 - 1
除两个只出现一次的整数外，nums 中的其他数字都出现两次

解题思路：
这道题是对只出现一次的数字（Single Number）的延伸，现在有两个数字只出现一次，其余数字都出现两次。

关键点是如何将这两个只出现一次的数字分开处理。

1. 首先，对所有数字进行异或运算，得到的结果是两个只出现一次的数字的异或结果（因为相同数字异或为0，0与任何数异或为该数）。
2. 在这个异或结果中，找到任意一个为1的位。这个位为1表示两个只出现一次的数字在这一位上的值不同。
3. 根据这个位是否为1，将原数组分成两组。这样，两个只出现一次的数字会被分到不同的组中。
4. 对每个组内的数字进行异或运算，最终得到两个只出现一次的数字。

时间复杂度：O(n) - 我们需要遍历数组两次
空间复杂度：O(1) - 只使用了常数级别的额外空间
*/
public class Code19_SingleNumberIII {

    /**
     * 找出数组中只出现一次的两个元素
     * @param nums 输入的整数数组
     * @return 只出现一次的两个元素组成的数组
     */
    public static int[] singleNumber(int[] nums) {
        // 步骤1: 对所有数字进行异或运算，得到两个只出现一次的数字的异或结果
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 步骤2: 找到xorResult中任意一个为1的位
        // 这里我们找到最右边的1
        // 例如，对于xorResult = 01010，rightmostSetBit = 00010
        int rightmostSetBit = xorResult & (-xorResult);
        
        // 步骤3: 根据这个位将数组分成两组，并分别对两组进行异或运算
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & rightmostSetBit) == 0) {
                // 该位为0的组
                a ^= num;
            } else {
                // 该位为1的组
                b ^= num;
            }
        }
        
        // 返回两个只出现一次的数字
        return new int[]{a, b};
    }
    
    /**
     * 找出数组中只出现一次的两个元素（另一种实现方式）
     * @param nums 输入的整数数组
     * @return 只出现一次的两个元素组成的数组
     */
    public static int[] singleNumber2(int[] nums) {
        int xorResult = 0;
        for (int num : nums) {
            xorResult ^= num;
        }
        
        // 找到xorResult中任意一个为1的位
        // 这里使用不同的方法来找最右边的1
        int rightmostSetBit = 1;
        while ((xorResult & rightmostSetBit) == 0) {
            rightmostSetBit <<= 1;
        }
        
        int a = 0, b = 0;
        for (int num : nums) {
            if ((num & rightmostSetBit) == 0) {
                a ^= num;
            } else {
                b ^= num;
            }
        }
        
        return new int[]{a, b};
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {1, 2, 1, 3, 2, 5};
        int[] result1 = singleNumber(nums1);
        System.out.print("Test 1: [");
        System.out.print(result1[0] + ", " + result1[1]);
        System.out.println("]"); // 输出: [3, 5] 或 [5, 3]
        
        // 测试用例2
        int[] nums2 = {-1, 0};
        int[] result2 = singleNumber(nums2);
        System.out.print("Test 2: [");
        System.out.print(result2[0] + ", " + result2[1]);
        System.out.println("]"); // 输出: [-1, 0] 或 [0, -1]
        
        // 测试用例3
        int[] nums3 = {0, 1};
        int[] result3 = singleNumber(nums3);
        System.out.print("Test 3: [");
        System.out.print(result3[0] + ", " + result3[1]);
        System.out.println("]"); // 输出: [0, 1] 或 [1, 0]
        
        // 使用第二种方法测试
        System.out.println("\nUsing alternative method:");
        int[] result1_alt = singleNumber2(nums1);
        System.out.print("Test 1 (alt): [");
        System.out.print(result1_alt[0] + ", " + result1_alt[1]);
        System.out.println("]");
    }
}