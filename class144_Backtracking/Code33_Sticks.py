"""
POJ 1011 Sticks

给定n根火柴棍，每根火柴棍都有一定的长度。要求将这些火柴棍拼成若干根长度相等的火柴棍，
且每根新火柴棍的长度要尽可能大。求这个最大长度。

算法思路：
使用回溯算法，从最大可能的长度开始尝试，逐步减小，直到找到一个可行解。
对于每个尝试的长度，使用回溯算法检查是否能将所有火柴棍拼成该长度的若干根新火柴棍。

时间复杂度：O(2^n * n)
空间复杂度：O(n)
"""

class Solution:
    def maxLenOfSticks(self, sticks):
        """
        求最大可能的火柴棍长度
        :param sticks: List[int] 火柴棍长度数组
        :return: int 最大可能的火柴棍长度
        """
        # 从大到小排序，便于剪枝
        sticks.sort(reverse=True)
        
        total_sum = sum(sticks)
        
        # 从最大可能长度开始尝试
        for length in range(total_sum // len(sticks), 0, -1):
            if total_sum % length == 0:  # 只有当总长度能被length整除时才可能
                buckets = [0] * (total_sum // length)
                if self.backtrack(sticks, 0, buckets, length):
                    return length
        
        return 1  # 最坏情况，每根火柴棍单独作为一根
    
    def backtrack(self, sticks, index, buckets, target):
        """
        回溯函数，尝试将火柴棍分配到各个桶中
        :param sticks: List[int] 火柴棍长度数组
        :param index: int 当前处理的火柴棍索引
        :param buckets: List[int] 桶数组，记录每个桶当前的长度
        :param target: int 目标长度
        :return: bool 是否能成功分配
        """
        # 终止条件：所有火柴棍都已处理完
        if index == len(sticks):
            return True
        
        stick = sticks[index]
        
        # 尝试将当前火柴棍放入每个桶中
        for i in range(len(buckets)):
            # 剪枝：如果放入当前桶后超过目标长度，则跳过
            if buckets[i] + stick > target:
                continue
            
            buckets[i] += stick
            if self.backtrack(sticks, index + 1, buckets, target):
                return True
            buckets[i] -= stick
            
            # 剪枝：如果当前桶为空，说明当前火柴棍无法放入任何桶中
            if buckets[i] == 0:
                break
        
        return False

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    sticks1 = [5, 2, 1, 5, 2, 1, 5, 2, 1]
    print("Input: [5, 2, 1, 5, 2, 1, 5, 2, 1]")
    print("Output:", solution.maxLenOfSticks(sticks1))
    
    # 测试用例2
    sticks2 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print("\nInput: [1, 2, 3, 4, 5, 6, 7, 8, 9]")
    print("Output:", solution.maxLenOfSticks(sticks2))