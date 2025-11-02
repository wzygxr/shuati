/**
 * 缺失的数字 - Missing Number
 * 测试链接 : https://leetcode.cn/problems/missing-number/
 * 相关题目:
 * 1. 只出现一次的数字 - Single Number: https://leetcode.cn/problems/single-number/
 * 2. 只出现一次的数字 II - Single Number II: https://leetcode.cn/problems/single-number-ii/
 * 3. 只出现一次的数字 III - Single Number III: https://leetcode.cn/problems/single-number-iii/
 * 4. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
 * 5. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
 * 
 * 题目描述：
 * 给定一个包含 [0, n] 中 n 个数的数组 nums ，找出 [0, n] 这个范围内没有出现在数组中的那个数。
 * 
 * 示例：
 * 输入：nums = [3,0,1]
 * 输出：2
 * 解释：n = 3，因为有 3 个数字，所以所有的数字都在范围 [0,3] 内。2 是丢失的数字。
 * 
 * 输入：nums = [0,1]
 * 输出：2
 * 解释：n = 2，因为有 2 个数字，所以所有的数字都在范围 [0,2] 内。2 是丢失的数字。
 * 
 * 输入：nums = [9,6,4,2,3,5,7,0,1]
 * 输出：8
 * 解释：n = 9，因为有 9 个数字，所以所有的数字都在范围 [0,9] 内。8 是丢失的数字。
 * 
 * 输入：nums = [0]
 * 输出：1
 * 解释：n = 1，因为有 1 个数字，所以所有的数字都在范围 [0,1] 内。1 是丢失的数字。
 * 
 * 提示：
 * n == nums.length
 * 1 <= n <= 10^4
 * 0 <= nums[i] <= n
 * nums 中的所有数字都独一无二
 * 
 * 进阶：你能否实现线性时间复杂度、仅使用额外常数空间的算法解决此问题?
 * 
 * 解题思路：
 * 这道题有多种解法：
 * 
 * 方法1：数学求和
 * 计算0到n的和，然后减去数组中所有元素的和，差值就是缺失的数字。
 * 时间复杂度：O(n)
 * 空间复杂度：O(1)
 * 
 * 方法2：异或运算（推荐）
 * 利用异或运算的性质：
 * 1. a ^ a = 0
 * 2. a ^ 0 = a
 * 3. 异或运算满足交换律和结合律
 * 
 * 我们将0到n的所有数字与数组中的所有元素进行异或运算，出现两次的数字会相互抵消为0，最后只剩下缺失的数字。
 * 
 * 方法3：排序后查找
 * 先对数组排序，然后遍历查找缺失的数字。
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(1)
 * 
 * 方法4：哈希表
 * 用哈希表记录出现过的数字，然后遍历0到n查找缺失的数字。
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 时间复杂度：
 * 方法1和方法2：O(n)
 * 方法3：O(n log n)
 * 方法4：O(n)
 * 
 * 空间复杂度：
 * 方法1、2、3：O(1)
 * 方法4：O(n)
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

class Code11_MissingNumber {
public:
    /**
     * 找出数组中缺失的数字
     * 使用异或运算方法：
     * 将0到n的所有数字与数组中的所有元素进行异或
     * 出现两次的数字会相互抵消为0，最后只剩下缺失的数字
     * 
     * @param nums 输入数组
     * @param numsSize 数组长度
     * @return 缺失的数字
     */
    int missingNumber(int* nums, int numsSize) {
        // 使用异或运算
        // 将0到n的所有数字与数组中的所有元素进行异或
        int xorResult = 0;
        
        // 与0到n-1的所有数字异或
        for (int i = 0; i < numsSize; i++) {
            xorResult ^= i ^ nums[i];
        }
        
        // 最后与n异或（因为数组中只有n个元素，范围是0到n）
        return xorResult ^ numsSize;
    }
};

// 测试方法
int main() {
    Code11_MissingNumber solution;
    
    // 测试用例1：正常情况
    int nums1[] = {3, 0, 1};
    int result1 = solution.missingNumber(nums1, 3);
    // 预期结果: 2
    
    // 测试用例2：正常情况
    int nums2[] = {0, 1};
    int result2 = solution.missingNumber(nums2, 2);
    // 预期结果: 2
    
    // 测试用例3：正常情况
    int nums3[] = {9, 6, 4, 2, 3, 5, 7, 0, 1};
    int result3 = solution.missingNumber(nums3, 9);
    // 预期结果: 8
    
    // 测试用例4：边界情况
    int nums4[] = {0};
    int result4 = solution.missingNumber(nums4, 1);
    // 预期结果: 1
    
    return 0;
}