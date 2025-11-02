#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Largest Rectangle in Histogram (柱状图中最大的矩形)

题目描述:
给定 n 个非负整数，用来表示柱状图中各个柱子的高度，每个柱子彼此相邻，且宽度为 1 。
求在该柱状图中，能够勾勒出来的矩形的最大面积。

解题思路:
使用单调栈来解决。维护一个单调递增栈，栈中存储柱子的索引。
当遇到一个比栈顶元素高度小的柱子时，说明以栈顶元素为高度的矩形右边界确定了。
此时可以计算以栈顶元素为高度的矩形面积。

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，栈的空间

测试链接: https://leetcode.cn/problems/largest-rectangle-in-histogram/
"""


def largest_rectangle_area(heights):
    """
    计算柱状图中最大矩形面积
    
    Args:
        heights: List[int] - 柱子高度数组
        
    Returns:
        int - 最大矩形面积
    """
    n = len(heights)
    stack = []  # 单调递增栈，存储索引
    max_area = 0
    
    # 遍历每个柱子
    for i in range(n):
        # 当栈不为空且当前高度小于栈顶索引对应的高度时
        while stack and heights[stack[-1]] > heights[i]:
            height = heights[stack.pop()]  # 弹出栈顶元素作为高度
            # 计算宽度：如果栈为空，宽度为i；否则宽度为i - stack[-1] - 1
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)  # 将当前索引压入栈
    
    # 处理栈中剩余元素
    while stack:
        height = heights[stack.pop()]  # 弹出栈顶元素作为高度
        # 计算宽度：如果栈为空，宽度为n；否则宽度为n - stack[-1] - 1
        width = n if not stack else n - stack[-1] - 1
        max_area = max(max_area, height * width)
    
    return max_area


# 测试函数
def main():
    # 测试用例1
    heights1 = [2, 1, 5, 6, 2, 3]
    result1 = largest_rectangle_area(heights1)
    # 预期输出: 10
    print("测试用例1输出:", result1)
    
    # 测试用例2
    heights2 = [2, 4]
    result2 = largest_rectangle_area(heights2)
    # 预期输出: 4
    print("测试用例2输出:", result2)
    
    # 测试用例3
    heights3 = [1, 2, 3, 4, 5]
    result3 = largest_rectangle_area(heights3)
    # 预期输出: 9
    print("测试用例3输出:", result3)


if __name__ == "__main__":
    main()