#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 216. 组合总和 III

题目描述：
找出所有相加之和为 n 的 k 个数的组合。
只能使用数字1到9，每个数字最多使用一次。
返回所有可能的有效组合的列表。该列表不能包含相同的组合两次。

示例：
输入: k = 3, n = 7
输出: [[1,2,4]]
解释: 1 + 2 + 4 = 7，没有其他符合的组合了。

输入: k = 3, n = 9
输出: [[1,2,6], [1,3,5], [2,3,4]]

输入: k = 4, n = 1
输出: []
解释: 不存在有效的组合。在[1,9]范围内使用4个不同的数字，我们可以得到的最小和是1+2+3+4 = 10，因为10 > 1，没有有效的组合。

提示：
2 <= k <= 9
1 <= n <= 60

链接：https://leetcode.cn/problems/combination-sum-iii/

算法思路：
1. 使用回溯算法生成所有可能的组合
2. 从数字1开始，依次尝试每个数字
3. 对于每个数字，有两种选择：选择或不选择
4. 通过递归和回溯生成所有满足条件的组合
5. 剪枝优化：提前终止不可能产生有效解的分支

剪枝策略：
1. 可行性剪枝：当当前和超过目标值时剪枝
2. 最优性剪枝：当已选择的数字个数超过k时剪枝
3. 边界剪枝：当剩余可选数字不足时剪枝
4. 范围剪枝：只在1-9范围内选择数字

时间复杂度：O(C(9,k))，其中C(9,k)是组合数
空间复杂度：O(k)，递归栈深度和存储路径的空间

工程化考量：
1. 边界处理：处理k=0或n=0的特殊情况
2. 参数验证：验证输入参数的有效性
3. 性能优化：通过剪枝减少不必要的计算
4. 内存管理：合理使用数据结构减少内存占用
5. 可读性：添加详细注释和变量命名
6. 异常处理：处理可能的异常情况
7. 模块化设计：将核心逻辑封装成独立方法
8. 可维护性：添加详细注释和文档说明
"""

class LeetCode216_CombinationSumIII:
    def combination_sum3(self, k: int, n: int) -> list[list[int]]:
        """
        找出所有相加之和为 n 的 k 个数的组合
        
        Args:
            k: 组合中数字的个数
            n: 目标和
            
        Returns:
            所有满足条件的组合
        """
        result = []
        
        # 边界条件检查
        if k <= 0 or n <= 0 or k > 9 or n > 45:  # 1-9的最大和是45
            return result
        
        path = []
        self._backtrack(k, n, 1, 0, path, result)
        return result
    
    def _backtrack(self, k: int, n: int, start: int, current_sum: int, path: list, result: list) -> None:
        """
        回溯函数生成组合
        
        Args:
            k: 组合中数字的个数
            n: 目标和
            start: 当前起始数字
            current_sum: 当前和
            path: 当前路径
            result: 结果列表
        """
        # 终止条件：已选择k个数字
        if len(path) == k:
            # 检查和是否等于目标值
            if current_sum == n:
                result.append(path[:])  # 添加路径的副本
            return
        
        # 剪枝：如果已选择的数字个数超过k或当前和超过目标值，提前终止
        if len(path) > k or current_sum > n:
            return
        
        # 从start开始尝试数字1-9
        for i in range(start, 10):
            # 剪枝：如果加上当前数字后和超过目标值，由于数字递增，后面的数字更大，直接跳出循环
            if current_sum + i > n:
                break
            
            # 剪枝：如果剩余可选数字不足，提前终止
            if 9 - i + 1 < k - len(path):
                break
            
            # 选择当前数字
            path.append(i)
            
            # 递归处理下一个数字
            self._backtrack(k, n, i + 1, current_sum + i, path, result)
            
            # 回溯：撤销选择
            path.pop()
    
    def combination_sum3_2(self, k: int, n: int) -> list[list[int]]:
        """
        解法二：使用位运算枚举所有可能的组合
        
        Args:
            k: 组合中数字的个数
            n: 目标和
            
        Returns:
            所有满足条件的组合
        """
        result = []
        
        # 边界条件检查
        if k <= 0 or n <= 0 or k > 9 or n > 45:
            return result
        
        # 枚举所有可能的组合（1-9的子集）
        for mask in range(1 << 9):
            # 检查组合中数字的个数是否为k
            if bin(mask).count('1') == k:
                combination = []
                total = 0
                
                # 构造组合并计算和
                for i in range(9):
                    if mask & (1 << i):
                        combination.append(i + 1)
                        total += i + 1
                
                # 检查和是否等于目标值
                if total == n:
                    result.append(combination)
        
        return result


def main():
    """测试示例"""
    solution = LeetCode216_CombinationSumIII()
    
    # 测试用例1
    k1, n1 = 3, 7
    result1 = solution.combination_sum3(k1, n1)
    print(f"输入: k = {k1}, n = {n1}")
    print(f"输出: {result1}")
    
    # 测试用例2
    k2, n2 = 3, 9
    result2 = solution.combination_sum3(k2, n2)
    print(f"\n输入: k = {k2}, n = {n2}")
    print(f"输出: {result2}")
    
    # 测试用例3
    k3, n3 = 4, 1
    result3 = solution.combination_sum3(k3, n3)
    print(f"\n输入: k = {k3}, n = {n3}")
    print(f"输出: {result3}")
    
    # 测试解法二
    print("\n=== 解法二测试 ===")
    result4 = solution.combination_sum3_2(k1, n1)
    print(f"输入: k = {k1}, n = {n1}")
    print(f"输出: {result4}")


if __name__ == "__main__":
    main()