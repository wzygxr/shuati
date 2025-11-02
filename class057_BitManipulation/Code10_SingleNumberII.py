"""
只出现一次的数字 II - Single Number II
测试链接 : https://leetcode.cn/problems/single-number-ii/
相关题目:
1. 只出现一次的数字 - Single Number: https://leetcode.cn/problems/single-number/
2. 只出现一次的数字 III - Single Number III: https://leetcode.cn/problems/single-number-iii/
3. 缺失的数字 - Missing Number: https://leetcode.cn/problems/missing-number/
4. 找不同 - Find the Difference: https://leetcode.cn/problems/find-the-difference/
5. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/

题目描述：
给你一个整数数组 nums ，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次 。请你找出并返回那个只出现了一次的元素。

示例：
输入：nums = [2,2,3,2]
输出：3

输入：nums = [0,1,0,1,0,1,99]
输出：99

提示：
1 <= nums.length <= 3 * 10^4
-2^31 <= nums[i] <= 2^31 - 1
nums 中，除某个元素仅出现 一次 外，其余每个元素都恰出现 三次

进阶：你的算法应该具有线性时间复杂度。 你可以不使用额外空间来实现吗？

解题思路：
这道题是"只出现一次的数字"的升级版。在那道题中，其他元素出现2次，可以用异或运算解决。
但这道题中，其他元素出现3次，异或运算就不适用了。

方法1：哈希表统计
用哈希表统计每个元素出现的次数，然后找出出现次数为1的元素。
时间复杂度：O(n)
空间复杂度：O(n)

方法2：位运算（重点讲解）
对于每个二进制位，统计所有数字在该位上1的个数。
由于除了一个数字外，其他数字都出现3次，所以每个二进制位上1的个数模3的结果就是单独那个数字在该位上的值。

我们用两个变量ones和twos来表示每个二进制位上1的个数模3的结果：
- ones：表示当前出现1次的位
- twos：表示当前出现2次的位

状态转移：
- 当一个数字出现第1次时，它应该加到ones中
- 当一个数字出现第2次时，它应该从ones中移除，加到twos中
- 当一个数字出现第3次时，它应该从twos中移除

通过位运算可以实现这个状态转移：
ones = (ones ^ num) & ~twos;
twos = (twos ^ num) & ~ones;

时间复杂度：O(n)
空间复杂度：O(1)

补充题目：
1. 洛谷 P10118 『STA - R4』And: https://www.luogu.com.cn/problem/P10118
2. 洛谷 P9451 [ZSHOI-R1] 新概念报数: https://www.luogu.com.cn/problem/P9451
3. 洛谷 P10114 [LMXOI Round 1] Size: https://www.luogu.com.cn/problem/P10114
4. 洛谷 P1469 找筷子: https://www.luogu.com.cn/problem/P1469
5. Codeforces 276D Little Girl and Maximum XOR: https://www.luogu.com.cn/problem/CF276D
"""


class Solution:
    def singleNumber(self, nums: list[int]) -> int:
        """
        找出数组中只出现一次的元素（其他元素都出现3次）
        使用位运算方法：
        用两个变量ones和twos来表示每个二进制位上1的个数模3的结果
        ones：表示当前出现1次的位
        twos：表示当前出现2次的位
        
        :param nums: 输入数组
        :return: 只出现一次的元素
        """
        # ones表示当前出现1次的位
        # twos表示当前出现2次的位
        ones = 0
        twos = 0
        
        for num in nums:
            # 更新ones和twos的状态
            # (ones ^ num) 将num中为1的位在ones中翻转
            # & ~twos 确保这些位不在twos中（即不是出现2次的位）
            ones = (ones ^ num) & ~twos
            
            # (twos ^ num) 将num中为1的位在twos中翻转
            # & ~ones 确保这些位不在ones中（即不是出现1次的位）
            twos = (twos ^ num) & ~ones
        
        # 最终ones中保存的就是只出现一次的数字
        return ones


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [2, 2, 3, 2]
    result1 = solution.singleNumber(nums1)
    # 预期结果: 3
    print(f"测试用例1 - 输入: {nums1}")
    print(f"结果: {result1} (预期: 3)")
    
    # 测试用例2：正常情况
    nums2 = [0, 1, 0, 1, 0, 1, 99]
    result2 = solution.singleNumber(nums2)
    # 预期结果: 99
    print(f"测试用例2 - 输入: {nums2}")
    print(f"结果: {result2} (预期: 99)")