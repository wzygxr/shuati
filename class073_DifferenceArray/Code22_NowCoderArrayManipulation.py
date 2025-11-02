from typing import List
import sys

class Solution:
    """
    牛客网 - 数组操作问题
    
    题目描述:
    给定一个长度为 n 的数组，初始值都为 0。
    有 m 次操作，每次操作给出三个数 l, r, k，表示将数组下标从 l 到 r 的所有元素都加上 k。
    求执行完所有操作后数组中的最大值。
    
    示例:
    输入: n = 5, operations = [[1,2,100],[2,5,100],[3,4,100]]
    输出: 200
    
    解题思路:
    使用差分数组技巧来处理区间更新操作。
    1. 创建一个差分数组diff，大小为n+1
    2. 对于每个操作[l, r, k]，执行diff[l-1] += k和diff[r] -= k
    3. 对差分数组计算前缀和，得到最终数组
    4. 在计算前缀和的过程中记录最大值
    
    时间复杂度: O(n + m) - 需要遍历所有操作和数组一次
    空间复杂度: O(n) - 需要额外的差分数组空间
    
    这是最优解，因为需要处理所有操作，而且数组大小可能很大。
    """
    
    def maxValueAfterOperations(self, n: int, operations: List[List[int]]) -> int:
        """
        计算数组操作后的最大值
        
        Args:
            n: 数组长度
            operations: 操作数组，每个操作包含[起始索引, 结束索引, 增加值]
            
        Returns:
            操作后数组的最大值
        """
        # 边界情况处理
        if n <= 0 or not operations:
            return 0
        
        # 创建差分数组，大小为n+1以便处理边界情况
        diff = [0] * (n + 1)
        
        # 处理每个操作
        for op in operations:
            l, r, k = op[0], op[1], op[2]
            
            # 在差分数组中标记区间更新
            diff[l - 1] += k      # 在起始位置增加k
            if r < n:
                diff[r] -= k      # 在结束位置之后减少k
        
        # 通过计算差分数组的前缀和得到最终数组，并记录最大值
        max_val = -10**18  # 使用一个很小的数作为初始值
        current_sum = 0
        
        for i in range(n):
            current_sum += diff[i]
            if current_sum > max_val:
                max_val = current_sum
        
        return int(max_val)  # 确保返回int类型

def test_max_value_after_operations():
    """
    测试用例
    """
    solution = Solution()
    
    # 测试用例1
    n1 = 5
    operations1 = [[1, 2, 100], [2, 5, 100], [3, 4, 100]]
    result1 = solution.maxValueAfterOperations(n1, operations1)
    # 预期输出: 200
    print(f"测试用例1: {result1}")

    # 测试用例2
    n2 = 10
    operations2 = [[2, 6, 8], [3, 5, 7], [1, 8, 1], [5, 9, 15]]
    result2 = solution.maxValueAfterOperations(n2, operations2)
    # 预期输出: 31
    print(f"测试用例2: {result2}")
    
    # 测试用例3
    n3 = 4
    operations3 = [[1, 2, 5], [2, 4, 10], [1, 3, 3]]
    result3 = solution.maxValueAfterOperations(n3, operations3)
    # 预期输出: 18
    print(f"测试用例3: {result3}")
    
    # 测试用例4 - 边界情况
    n4 = 1
    operations4 = [[1, 1, 100]]
    result4 = solution.maxValueAfterOperations(n4, operations4)
    # 预期输出: 100
    print(f"测试用例4: {result4}")

if __name__ == "__main__":
    test_max_value_after_operations()