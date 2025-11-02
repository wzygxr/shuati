# LeetCode 364. Nested List Weight Sum II (嵌套列表权重和 II)
# 来源: LeetCode
# 网址: https://leetcode.cn/problems/nested-list-weight-sum-ii/
# 
# 题目描述:
# 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
# 整数的权重与其深度成反比，深度最大的整数权重为 1，深度第二大的整数权重为 2，依此类推。
# 返回该列表的加权和。
# 
# 示例:
# 输入: [[1,1],2,[1,1]]
# 输出: 8
# 解释: 四个 1 位于深度为 1 的位置，一个 2 位于深度为 2 的位置。
# 4*1*2 + 1*2*1 = 8 + 2 = 10？不，这里权重与深度成反比。
# 正确计算: 4*1*1 + 1*2*2 = 4 + 4 = 8
# 
# 解题思路:
# 方法1：先计算最大深度，然后使用深度的倒数作为权重
# 方法2：使用迭代方法，每遍历一层，累加当前层的和，并将其加入下一层的权重计算
# 这里使用方法2，更高效且简洁
# 
# 时间复杂度: O(n)，其中n是所有整数元素的总数
# 空间复杂度: O(d)，其中d是嵌套列表的最大深度

class Solution:
    def depthSumInverse(self, nestedList):
        """
        计算嵌套列表的反向加权和（迭代方法）
        
        Args:
            nestedList: 嵌套列表，可以包含整数或其他嵌套列表
            
        Returns:
            int: 反向加权和
        """
        sum_total = 0  # 当前所有层的和
        weighted_sum = 0  # 最终的加权和
        
        # 当嵌套列表不为空时，继续处理
        while nestedList:
            next_level = []
            level_sum = 0
            
            # 处理当前层的所有元素
            for item in nestedList:
                if isinstance(item, int):
                    # 如果是整数，加到当前层的和中
                    level_sum += item
                else:
                    # 如果是列表，将其元素加入下一层
                    next_level.extend(item)
            
            # 将当前层的和累加到总和中，这样每增加一层，前面层的和就会被多计算一次
            # 这等价于将权重设置为(最大深度 - 当前深度 + 1)
            sum_total += level_sum
            weighted_sum += sum_total
            
            # 处理下一层
            nestedList = next_level
        
        return weighted_sum
    
    def depthSumInverseRecursive(self, nestedList):
        """
        计算嵌套列表的反向加权和（递归方法）
        
        Args:
            nestedList: 嵌套列表，可以包含整数或其他嵌套列表
            
        Returns:
            int: 反向加权和
        """
        # 步骤1: 计算最大深度
        max_depth = self._get_max_depth(nestedList)
        
        # 步骤2: 使用递归计算加权和，权重 = max_depth - depth + 1
        return self._dfs(nestedList, 1, max_depth)
    
    def _get_max_depth(self, nestedList):
        """计算嵌套列表的最大深度"""
        if not nestedList:
            return 0
        
        max_depth = 0
        for item in nestedList:
            if isinstance(item, int):
                max_depth = max(max_depth, 1)
            else:
                max_depth = max(max_depth, 1 + self._get_max_depth(item))
        
        return max_depth
    
    def _dfs(self, nestedList, current_depth, max_depth):
        """递归深度优先搜索函数"""
        sum_total = 0
        weight = max_depth - current_depth + 1
        
        for item in nestedList:
            if isinstance(item, int):
                sum_total += item * weight
            else:
                sum_total += self._dfs(item, current_depth + 1, max_depth)
        
        return sum_total

# 测试函数
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [[1,1],2,[1,1]]
    test_case_1 = [[1, 1], 2, [1, 1]]
    print("测试用例1 (迭代方法):")
    print(f"输入: {test_case_1}")
    print(f"输出: {solution.depthSumInverse(test_case_1)}")
    print("期望: 8")
    print()
    
    print("测试用例1 (递归方法):")
    print(f"输入: {test_case_1}")
    print(f"输出: {solution.depthSumInverseRecursive(test_case_1)}")
    print("期望: 8")
    print()
    
    # 测试用例2: [1,[4,[6]]]
    test_case_2 = [1, [4, [6]]]
    print("测试用例2 (迭代方法):")
    print(f"输入: {test_case_2}")
    print(f"输出: {solution.depthSumInverse(test_case_2)}")
    # 计算期望结果: 1*3 + 4*2 + 6*1 = 3 + 8 + 6 = 17
    print("期望: 17")
    print()
    
    print("测试用例2 (递归方法):")
    print(f"输入: {test_case_2}")
    print(f"输出: {solution.depthSumInverseRecursive(test_case_2)}")
    print("期望: 17")
    print()
    
    # 测试用例3: [10, [5, -3]]
    test_case_3 = [10, [5, -3]]
    print("测试用例3 (迭代方法):")
    print(f"输入: {test_case_3}")
    result = solution.depthSumInverse(test_case_3)
    print(f"输出: {result}")
    # 计算期望结果: 10*2 + 5*1 + (-3)*1 = 20 + 5 - 3 = 22
    print("期望: 22")