#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Daily Temperatures (每日温度)

题目描述:
给定一个整数数组 temperatures ，表示每天的温度，返回一个数组 answer，
其中 answer[i] 是指对于第 i 天，下一个更高温度出现在几天后。
如果气温在这之后都不会升高，请在该位置用 0 来代替。

解题思路:
使用单调栈来解决。维护一个单调递减栈，栈中存储索引。
当遇到一个比栈顶元素温度高的温度时，说明找到了栈顶元素的下一个更高温度。

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，栈的空间

测试链接: https://leetcode.cn/problems/daily-temperatures/
"""


def daily_temperatures(temperatures):
    """
    计算每日温度问题
    
    Args:
        temperatures: List[int] - 温度数组
        
    Returns:
        List[int] - 每日到下一个更高温度的天数差
    """
    n = len(temperatures)
    answer = [0] * n
    stack = []  # 单调递减栈，存储索引
    
    for i in range(n):
        # 当栈不为空且当前温度大于栈顶索引对应的温度时
        while stack and temperatures[stack[-1]] < temperatures[i]:
            index = stack.pop()
            answer[index] = i - index  # 计算天数差
        stack.append(i)  # 将当前索引压入栈
    
    return answer


# 测试函数
def main():
    # 测试用例1
    temperatures1 = [73, 74, 75, 71, 69, 72, 76, 73]
    result1 = daily_temperatures(temperatures1)
    # 预期输出: [1, 1, 4, 2, 1, 1, 0, 0]
    print("测试用例1输出:", result1)
    
    # 测试用例2
    temperatures2 = [30, 40, 50, 60]
    result2 = daily_temperatures(temperatures2)
    # 预期输出: [1, 1, 1, 0]
    print("测试用例2输出:", result2)
    
    # 测试用例3
    temperatures3 = [30, 60, 90]
    result3 = daily_temperatures(temperatures3)
    # 预期输出: [1, 1, 0]
    print("测试用例3输出:", result3)


if __name__ == "__main__":
    main()