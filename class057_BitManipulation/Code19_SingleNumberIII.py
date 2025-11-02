# 只出现一次的数字 III
# 测试链接 : https://leetcode.cn/problems/single-number-iii/
'''
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
'''

class Solution:
    """
    只出现一次的数字III解决方案类
    使用位运算找到数组中只出现一次的两个元素
    """
    
    def singleNumber(self, nums: list[int]) -> list[int]:
        """
        找出数组中只出现一次的两个元素
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            只出现一次的两个元素组成的数组
        """
        # 步骤1: 对所有数字进行异或运算，得到两个只出现一次的数字的异或结果
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 步骤2: 找到xor_result中任意一个为1的位
        # 这里我们找到最右边的1
        # 例如，对于xor_result = 01010，rightmost_set_bit = 00010
        # 在Python中，我们需要处理负数的情况
        # 对于负数，Python使用无限精度的二进制补码表示，所以我们需要与mask结合
        rightmost_set_bit = 1
        while (xor_result & rightmost_set_bit) == 0:
            rightmost_set_bit <<= 1
        
        # 步骤3: 根据这个位将数组分成两组，并分别对两组进行异或运算
        a = 0  # 该位为0的组异或结果
        b = 0  # 该位为1的组异或结果
        
        for num in nums:
            if (num & rightmost_set_bit) == 0:
                # 该位为0的组
                a ^= num
            else:
                # 该位为1的组
                b ^= num
        
        # 返回两个只出现一次的数字
        return [a, b]
    
    def singleNumber2(self, nums: list[int]) -> list[int]:
        """
        找出数组中只出现一次的两个元素（优化版本）
        在Python中，使用位掩码处理负数情况
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            只出现一次的两个元素组成的数组
        """
        # 步骤1: 对所有数字进行异或运算
        xor_result = 0
        for num in nums:
            xor_result ^= num
        
        # 步骤2: 找到最右边的1
        # 在Python中，为了处理负数，我们可以使用mask
        mask = 1
        while True:
            if xor_result & mask:
                break
            mask <<= 1
        
        # 步骤3: 分组异或
        a, b = 0, 0
        for num in nums:
            if num & mask:
                a ^= num
            else:
                b ^= num
        
        return [a, b]
    
    def singleNumber3(self, nums: list[int]) -> list[int]:
        """
        找出数组中只出现一次的两个元素（使用集合方法，空间复杂度O(n)）
        虽然空间复杂度不如位运算方法好，但实现简单易懂
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            只出现一次的两个元素组成的数组
        """
        seen = set()
        for num in nums:
            if num in seen:
                seen.remove(num)
            else:
                seen.add(num)
        return list(seen)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 2, 1, 3, 2, 5]
    result1 = solution.singleNumber(nums1)
    print("Test 1: ", result1)  # 输出: [3, 5] 或 [5, 3]
    
    # 测试用例2
    nums2 = [-1, 0]
    result2 = solution.singleNumber(nums2)
    print("Test 2: ", result2)  # 输出: [-1, 0] 或 [0, -1]
    
    # 测试用例3
    nums3 = [0, 1]
    result3 = solution.singleNumber(nums3)
    print("Test 3: ", result3)  # 输出: [0, 1] 或 [1, 0]
    
    # 使用第二种方法测试
    print("\nUsing alternative method:")
    result1_alt = solution.singleNumber2(nums1)
    print("Test 1 (alt): ", result1_alt)
    
    # 使用第三种方法测试（集合方法）
    print("\nUsing set method:")
    result1_set = solution.singleNumber3(nums1)
    print("Test 1 (set): ", result1_set)