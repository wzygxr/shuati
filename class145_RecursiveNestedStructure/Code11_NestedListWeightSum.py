# LeetCode 339. Nested List Weight Sum (嵌套列表权重和)
# 来源: LeetCode
# 网址: https://leetcode.cn/problems/nested-list-weight-sum/
# 
# 题目描述:
# 给定一个嵌套的整数列表 nestedList，每个元素要么是整数，要么是列表。同时，列表中元素同样也可以是整数或者是另一个列表。
# 整数的权重是其深度，返回该列表的加权和。
# 
# 示例:
# 输入: [[1,1],2,[1,1]]
# 输出: 10
# 解释: 因为这个列表中有四个深度为 2 的 1 ，和一个深度为 1 的 2。
# 4*1*2 + 1*2*1 = 8 + 2 = 10
# 
# 解题思路:
# 使用递归处理嵌套结构，对每个元素进行深度遍历，累加每个整数与其深度的乘积。
# 
# 时间复杂度: O(n)，其中n是所有整数元素的总数
# 空间复杂度: O(d)，其中d是嵌套列表的最大深度，递归调用栈的深度

# 在Python中，我们可以用整数或列表的混合结构来模拟NestedInteger
# 例如: [[1,1],2,[1,1]] 直接表示嵌套列表

class Solution:
    def depthSum(self, nestedList) -> int:
        """
        计算嵌套列表的加权和
        
        Args:
            nestedList: 嵌套列表，可以包含整数或其他嵌套列表
            
        Returns:
            int: 加权和
        """
        # 从深度1开始递归计算
        return self.dfs(nestedList, 1)
    
    def dfs(self, nestedList, depth) -> int:
        """
        递归深度优先搜索函数
        
        Args:
            nestedList: 当前嵌套列表
            depth: 当前深度
            
        Returns:
            int: 当前嵌套列表的加权和
        """
        sum_total = 0
        
        # 遍历当前列表中的每个元素
        for item in nestedList:
            if isinstance(item, int):
                # 如果是整数，累加其值乘以深度
                sum_total += item * depth
            else:
                # 如果是列表，递归计算其加权和，深度加1
                sum_total += self.dfs(item, depth + 1)
        
        return sum_total

# 测试函数
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [[1,1],2,[1,1]]
    test_case_1 = [[1, 1], 2, [1, 1]]
    print("测试用例1:")
    print(f"输入: {test_case_1}")
    print(f"输出: {solution.depthSum(test_case_1)}")
    print("期望: 10")
    print()
    
    # 测试用例2: [1,[4,[6]]]
    test_case_2 = [1, [4, [6]]]
    print("测试用例2:")
    print(f"输入: {test_case_2}")
    print(f"输出: {solution.depthSum(test_case_2)}")
    print("期望: 27")
    print()
    
    # 测试用例3: []
    test_case_3 = []
    print("测试用例3:")
    print(f"输入: {test_case_3}")
    print(f"输出: {solution.depthSum(test_case_3)}")
    print("期望: 0")
    print()
    
    # 测试用例4: [10, [5, -3]]
    test_case_4 = [10, [5, -3]]
    print("测试用例4:")
    print(f"输入: {test_case_4}")
    result = solution.depthSum(test_case_4)
    print(f"输出: {result}")
    # 计算期望结果: 10*1 + 5*2 + (-3)*2 = 10 + 10 - 6 = 14
    print("期望: 14")