/**
 * 只出现一次的数字 - Single Number
 * 测试链接 : https://leetcode.cn/problems/single-number/
 * 相关题目:
 * 1. 只出现一次的数字 II - Single Number II: https://leetcode.cn/problems/single-number-ii/
 * 2. 只出现一次的数字 III - Single Number III: https://leetcode.cn/problems/single-number-iii/
 * 3. 缺失的数字 - Missing Number: https://leetcode.cn/problems/missing-number/
 * 4. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
 * 5. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
 * 
 * 题目描述：
 * 给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。
 * 
 * 示例：
 * 输入：nums = [2,2,1]
 * 输出：1
 * 
 * 输入：nums = [4,1,2,1,2]
 * 输出：4
 * 
 * 输入：nums = [1]
 * 输出：1
 * 
 * 提示：
 * 1 <= nums.length <= 3 * 10^4
 * -3 * 10^4 <= nums[i] <= 3 * 10^4
 * 除了某个元素只出现一次以外，其余每个元素均出现两次。
 * 
 * 解题思路：
 * 这是一个经典的位运算问题，利用异或运算的性质：
 * 1. a ^ a = 0（相同数字异或为0）
 * 2. a ^ 0 = a（任何数与0异或等于自身）
 * 3. 异或运算满足交换律和结合律
 * 
 * 因此，将数组中所有元素进行异或运算，出现两次的元素会相互抵消为0，最后只剩下只出现一次的元素。
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * 补充题目：
 * 1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
 * 2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
 * 3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
 * 4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
 * 5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
 */
// 为适应编译环境，避免使用复杂的STL容器和标准库函数
// 使用基本的C++实现方式和自定义IO函数

class Code07_SingleNumber {
public:
    /**
     * 找出数组中只出现一次的元素
     * 利用异或运算的性质：
     * 1. 相同数字异或为0：a ^ a = 0
     * 2. 任何数与0异或等于自身：a ^ 0 = a
     * 3. 异或运算满足交换律和结合律
     * 
     * @param nums 输入数组
     * @param numsSize 数组长度
     * @return 只出现一次的元素
     */
    int singleNumber(int* nums, int numsSize) {
        // 利用异或运算的性质：
        // 1. 相同数字异或为0：a ^ a = 0
        // 2. 任何数与0异或等于自身：a ^ 0 = a
        // 3. 异或运算满足交换律和结合律
        int result = 0;
        for (int i = 0; i < numsSize; i++) {
            result ^= nums[i];
        }
        return result;
    }
};

// 测试方法
int main() {
    Code07_SingleNumber solution;
    
    // 测试用例1：正常情况
    int nums1[] = {2, 2, 1};
    int result1 = solution.singleNumber(nums1, 3);
    // 预期结果: 1
    
    // 测试用例2：正常情况
    int nums2[] = {4, 1, 2, 1, 2};
    int result2 = solution.singleNumber(nums2, 5);
    // 预期结果: 4
    
    // 测试用例3：边界情况
    int nums3[] = {1};
    int result3 = solution.singleNumber(nums3, 1);
    // 预期结果: 1
    
    return 0;
}