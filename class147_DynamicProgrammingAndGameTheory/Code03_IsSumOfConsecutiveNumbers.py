"""
连续正整数和判断 - Python实现

题目描述：
判断一个正整数是否可以表示为连续正整数的和

解题思路：
这是一个数论问题，可以使用数学公式、滑动窗口或数学规律来解决
1. 数学公式解法：利用等差数列求和公式
2. 滑动窗口解法：双指针遍历可能的连续序列
3. 数学规律解法：通过数学推导发现规律

相关题目：
1. LeetCode 829. Consecutive Numbers Sum：https://leetcode.com/problems/consecutive-numbers-sum/
2. LeetCode 53. Maximum Subarray：https://leetcode.com/problems/maximum-subarray/
3. LeetCode 128. Longest Consecutive Sequence：https://leetcode.com/problems/longest-consecutive-sequence/
4. LeetCode 560. Subarray Sum Equals K：https://leetcode.com/problems/subarray-sum-equals-k/

工程化考量：
1. 异常处理：处理负数和零输入
2. 边界条件：处理小规模数据
3. 性能优化：使用数学规律O(1)解法
4. 可读性：清晰的变量命名和注释
"""

class ConsecutiveNumbers:
    
    @staticmethod
    def is_sum_of_consecutive_math(n: int) -> bool:
        """数学公式解法"""
        if n <= 2:
            return False
            
        # n = m*k + m*(m-1)/2
        # 其中m是连续数的个数，k是起始数字
        m = 2
        while m * (m - 1) // 2 < n:
            numerator = n - m * (m - 1) // 2
            if numerator % m == 0 and numerator // m > 0:
                return True
            m += 1
        return False
    
    @staticmethod
    def is_sum_of_consecutive_sliding(n: int) -> bool:
        """滑动窗口解法"""
        if n <= 2:
            return False
            
        left, right = 1, 1
        current_sum = 0
        
        while left <= n // 2 + 1:
            if current_sum < n:
                current_sum += right
                right += 1
            elif current_sum > n:
                current_sum -= left
                left += 1
            else:
                return True
                
        return False
    
    @staticmethod
    def is_sum_of_consecutive_optimal(n: int) -> bool:
        """数学规律解法（最优解）"""
        # 数学规律：一个数可以表示为连续正整数和当且仅当它不是2的幂
        # 因为2的幂只能表示为自身，不能拆分为多个连续正整数
        if n <= 2:
            return False
            
        # 检查是否是2的幂
        return (n & (n - 1)) != 0
    
    # ==================== 扩展题目1: 连续正整数和的个数 ====================
    """
    LeetCode 829. Consecutive Numbers Sum
    题目：计算一个数可以表示为连续正整数和的方案数
    网址：https://leetcode.com/problems/consecutive-numbers-sum/
    
    数学解法：
    n = k + (k+1) + ... + (k+m-1) = m*k + m*(m-1)/2
    时间复杂度：O(sqrt(n))
    空间复杂度：O(1)
    """
    @staticmethod
    def consecutive_numbers_sum(n: int) -> int:
        count = 0
        m = 1
        
        while m * (m - 1) // 2 < n:
            numerator = n - m * (m - 1) // 2
            if numerator % m == 0 and numerator // m > 0:
                count += 1
            m += 1
            
        return count
    
    # ==================== 扩展题目2: 最大连续子数组和 ====================
    """
    LeetCode 53. Maximum Subarray
    题目：求最大连续子数组和
    网址：https://leetcode.com/problems/maximum-subarray/
    
    Kadane算法：
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    @staticmethod
    def max_subarray(nums: list) -> int:
        if not nums:
            return 0
            
        max_sum = current_sum = nums[0]
        
        for i in range(1, len(nums)):
            current_sum = max(nums[i], current_sum + nums[i])
            max_sum = max(max_sum, current_sum)
            
        return max_sum
    
    # ==================== 扩展题目3: 最长连续序列 ====================
    """
    LeetCode 128. Longest Consecutive Sequence
    题目：求最长连续数字序列的长度
    网址：https://leetcode.com/problems/longest-consecutive-sequence/
    
    哈希集合解法：
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    @staticmethod
    def longest_consecutive(nums: list) -> int:
        if not nums:
            return 0
            
        num_set = set(nums)
        longest = 0
        
        for num in num_set:
            # 只从序列的起点开始计算
            if num - 1 not in num_set:
                current_num = num
                current_length = 1
                
                while current_num + 1 in num_set:
                    current_num += 1
                    current_length += 1
                    
                longest = max(longest, current_length)
                
        return longest
    
    # ==================== 扩展题目4: 和为K的子数组 ====================
    """
    LeetCode 560. Subarray Sum Equals K
    题目：计算和为K的子数组个数
    网址：https://leetcode.com/problems/subarray-sum-equals-k/
    
    前缀和+哈希表解法：
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    @staticmethod
    def subarray_sum(nums: list, k: int) -> int:
        from collections import defaultdict
        
        prefix_sum_count = defaultdict(int)
        prefix_sum_count[0] = 1
        
        count = 0
        prefix_sum = 0
        
        for num in nums:
            prefix_sum += num
            if prefix_sum - k in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - k]
            prefix_sum_count[prefix_sum] += 1
            
        return count

# 测试函数
def main():
    print("=== 连续正整数和判断测试 ===")
    for i in range(1, 21):
        result1 = ConsecutiveNumbers.is_sum_of_consecutive_math(i)
        result2 = ConsecutiveNumbers.is_sum_of_consecutive_sliding(i)
        result3 = ConsecutiveNumbers.is_sum_of_consecutive_optimal(i)
        print(f"{i}: {result1} / {result2} / {result3}")
    
    print("\n=== 扩展题目测试 ===")
    
    # 测试连续正整数和的个数
    print(f"Consecutive Numbers Sum (15): {ConsecutiveNumbers.consecutive_numbers_sum(15)}")
    
    # 测试最大子数组和
    nums1 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
    print(f"Maximum Subarray: {ConsecutiveNumbers.max_subarray(nums1)}")
    
    # 测试最长连续序列
    nums2 = [100, 4, 200, 1, 3, 2]
    print(f"Longest Consecutive: {ConsecutiveNumbers.longest_consecutive(nums2)}")
    
    # 测试和为K的子数组
    nums3 = [1, 1, 1]
    print(f"Subarray Sum Equals K (2): {ConsecutiveNumbers.subarray_sum(nums3, 2)}")

if __name__ == "__main__":
    main()