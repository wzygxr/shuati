#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Trapping Rain Water（接雨水）

题目描述:
给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

解题思路:
使用单调栈来解决。维护一个单调递减栈，栈中存储柱子的索引。
当遇到一个比栈顶元素高度大的柱子时，说明可能形成了凹槽可以接雨水。
计算凹槽的面积即为接雨水量。

时间复杂度: O(n)，每个元素最多入栈和出栈各一次
空间复杂度: O(n)，栈的空间

测试链接: https://leetcode.cn/problems/trapping-rain-water/
"""


def trap(height):
    """
    计算接雨水量
    
    Args:
        height: List[int] - 柱子高度数组
        
    Returns:
        int - 接雨水量
    """
    n = len(height)
    stack = []  # 单调递减栈，存储索引
    water = 0
    
    # 遍历每个柱子
    for i in range(n):
        # 当栈不为空且当前高度大于栈顶索引对应的高度时
        while stack and height[stack[-1]] < height[i]:
            bottom = height[stack.pop()]  # 弹出栈顶元素作为凹槽底部
            if not stack:  # 如果栈为空，无法形成凹槽
                break
            
            # 计算凹槽的宽度和高度
            width = i - stack[-1] - 1
            min_height = min(height[stack[-1]], height[i])
            water += width * (min_height - bottom)
        stack.append(i)  # 将当前索引压入栈
    
    return water


# 测试函数
def main():
    # 测试用例1
    height1 = [0,1,0,2,1,0,1,3,2,1,2,1]
    result1 = trap(height1)
    # 预期输出: 6
    print("测试用例1输出:", result1)
    
    # 测试用例2
    height2 = [4,2,0,3,2,5]
    result2 = trap(height2)
    # 预期输出: 9
    print("测试用例2输出:", result2)


if __name__ == "__main__":
    main()