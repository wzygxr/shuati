package class030.xor_problems_solutions;

/**
 * 题目: LeetCode 136. Single Number
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
 */
public class LeetCode136_SingleNumber {
    
    /**
     * 找到数组中唯一出现一次的元素
     * 
     * @param nums 输入数组
     * @return 只出现一次的元素
     */
    public int singleNumber(int[] nums) {
        // 利用异或运算的性质，所有出现两次的数会相互抵消
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
    
    // 测试方法
    public static void main(String[] args) {
        LeetCode136_SingleNumber solution = new LeetCode136_SingleNumber();
        
        // 测试用例1
        int[] nums1 = {2, 2, 1};
        System.out.println("输入: [2,2,1]");
        System.out.println("输出: " + solution.singleNumber(nums1)); // 应该输出 1
        
        // 测试用例2
        int[] nums2 = {4, 1, 2, 1, 2};
        System.out.println("输入: [4,1,2,1,2]");
        System.out.println("输出: " + solution.singleNumber(nums2)); // 应该输出 4
        
        // 测试用例3
        int[] nums3 = {1};
        System.out.println("输入: [1]");
        System.out.println("输出: " + solution.singleNumber(nums3)); // 应该输出 1
    }
}