"""
数组中两个数的最大异或值 - Maximum XOR of Two Numbers in an Array
测试链接 : https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
相关题目:
1. 比特位计数 - Counting Bits: https://leetcode.cn/problems/counting-bits/
2. 汉明距离 - Hamming Distance: https://leetcode.cn/problems/hamming-distance/
3. 只出现一次的数字 - Single Number: https://leetcode.cn/problems/single-number/
4. 2的幂 - Power of Two: https://leetcode.cn/problems/power-of-two/
5. 位运算技巧大全 - Bit Manipulation Tricks

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
"""


class TrieNode:
    def __init__(self):
        self.children = {}  # 使用字典存储子节点


class Solution:
    def __init__(self):
        self.root = TrieNode()

    def findMaximumXOR(self, nums: list[int]) -> int:
        """
        找出数组中两个数的最大异或值
        使用贪心算法 + 字典树：
        1. 将所有数字插入字典树
        2. 对于每个数字，在字典树中寻找与其异或结果最大的数字
        
        :param nums: 输入数组
        :return: 最大异或值
        """
        # 特殊情况处理
        if not nums:
            return 0
        
        # 将所有数字插入字典树
        for num in nums:
            self.insert(num)
        
        max_xor = 0
        # 对于每个数字，查找与其异或结果最大的数字
        for num in nums:
            max_xor = max(max_xor, self.find_max_xor(num))
        
        return max_xor
    
    def insert(self, num: int) -> None:
        """
        将数字插入字典树
        从最高位开始处理（第31位到第0位）
        
        :param num: 要插入的数字
        """
        node = self.root
        # 从最高位开始处理（第31位到第0位）
        for i in range(31, -1, -1):
            # 提取第i位的值（0或1）
            bit = (num >> i) & 1
            # 如果对应子节点不存在，则创建新节点
            if bit not in node.children:
                node.children[bit] = TrieNode()
            # 移动到子节点
            node = node.children[bit]
    
    def find_max_xor(self, num: int) -> int:
        """
        查找与给定数字异或结果最大的数字
        贪心策略：从高位开始，选择与当前位不同的路径（使异或结果在该位为1）
        
        :param num: 给定数字
        :return: 最大异或值
        """
        node = self.root
        max_xor = 0
        # 从最高位开始处理（第31位到第0位）
        for i in range(31, -1, -1):
            # 提取第i位的值（0或1）
            bit = (num >> i) & 1
            # 贪心策略：选择与当前位不同的路径（使异或结果在该位为1）
            opposite_bit = bit ^ 1
            
            # 如果与当前位不同的路径存在，则选择该路径
            if opposite_bit in node.children:
                # 设置异或结果在该位为1
                max_xor |= (1 << i)
                node = node.children[opposite_bit]
            else:
                # 否则选择相同的路径
                node = node.children[bit]
        return max_xor


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1：正常情况
    nums1 = [3, 10, 5, 25, 2, 8]
    result1 = solution.findMaximumXOR(nums1)
    # 预期结果: 28
    print(f"测试用例1 - 输入: {nums1}")
    print(f"结果: {result1} (预期: 28)")
    
    # 测试用例2：边界情况
    nums2 = [0]
    result2 = solution.findMaximumXOR(nums2)
    # 预期结果: 0
    print(f"测试用例2 - 输入: {nums2}")
    print(f"结果: {result2} (预期: 0)")
    
    # 测试用例3：正常情况
    nums3 = [2, 4]
    result3 = solution.findMaximumXOR(nums3)
    # 预期结果: 6
    print(f"测试用例3 - 输入: {nums3}")
    print(f"结果: {result3} (预期: 6)")
    
    # 测试用例4：正常情况
    nums4 = [8, 10, 2]
    result4 = solution.findMaximumXOR(nums4)
    # 预期结果: 10
    print(f"测试用例4 - 输入: {nums4}")
    print(f"结果: {result4} (预期: 10)")
    
    # 测试用例5：正常情况
    nums5 = [14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70]
    result5 = solution.findMaximumXOR(nums5)
    # 预期结果: 127
    print(f"测试用例5 - 输入: {nums5}")
    print(f"结果: {result5} (预期: 127)")