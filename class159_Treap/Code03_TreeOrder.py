#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Code03_TreeOrder.py - 树的序问题(Python版)

题目来源：洛谷 P1377
题目链接：https://www.luogu.com.cn/problem/P1377

题目描述：
给定一个长度为n的数组arr，表示依次插入数字，会形成一棵搜索二叉树
也许同样的一个序列，依次插入数字后，也能形成同样形态的搜索二叉树
请返回字典序尽量小的结果

算法思路：
1. 使用笛卡尔树（小根堆性质）构建搜索二叉树
2. 通过单调栈算法在O(n)时间内构建笛卡尔树
3. 使用迭代方式实现先序遍历，避免递归爆栈
4. 输出字典序最小的结果

时间复杂度：O(n)
空间复杂度：O(n)

工程化考量：
- 使用列表模拟栈，避免递归深度限制
- 迭代遍历防止递归爆栈
- 输入输出优化提高效率
- 边界条件处理确保鲁棒性
"""

import sys

def main():
    # 读取输入数据
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    
    # 初始化数组
    arr = [0] * (n + 1)  # 下标从1开始
    left = [0] * (n + 1)  # 左子树数组
    right = [0] * (n + 1)  # 右子树数组
    stack = [0] * (n + 1)  # 栈数组
    
    # 读取输入并构建arr数组
    for i in range(1, n + 1):
        val = int(data[i])
        arr[val] = i  # 关键：arr[值] = 位置
    
    # 构建笛卡尔树
    top = 0  # 栈顶指针
    
    for i in range(1, n + 1):
        pos = top
        
        # 维护单调栈：找到第一个小于等于当前值的节点
        while pos > 0 and arr[stack[pos]] > arr[i]:
            pos -= 1
        
        # 连接右子树
        if pos > 0:
            right[stack[pos]] = i
        
        # 连接左子树
        if pos < top:
            left[i] = stack[pos + 1]
        
        # 更新栈
        pos += 1
        stack[pos] = i
        top = pos
    
    # 迭代方式实现先序遍历
    size = 1  # 栈大小
    output = [0] * (n + 1)  # 输出数组
    output_idx = 0  # 输出索引
    
    # 初始化栈，放入根节点
    stack[size] = stack[1]  # 根节点在栈底
    
    while size > 0:
        cur = stack[size]  # 获取栈顶元素
        size -= 1  # 弹出栈顶
        output_idx += 1
        output[output_idx] = cur  # 记录遍历顺序
        
        # 先右后左入栈（因为栈是后进先出，所以先序遍历需要先右后左）
        if right[cur] != 0:
            size += 1
            stack[size] = right[cur]
        if left[cur] != 0:
            size += 1
            stack[size] = left[cur]
    
    # 输出结果
    result = []
    for i in range(1, n + 1):
        result.append(str(output[i]))
    
    print(' '.join(result))

if __name__ == "__main__":
    main()

"""
算法复杂度分析：
时间复杂度：O(n)
  - 构建笛卡尔树：每个元素入栈出栈一次，O(n)
  - 先序遍历：每个节点访问一次，O(n)
  - 总体：O(n)

空间复杂度：O(n)
  - 数组存储：arr, left, right, stack, output 各需要O(n)空间
  - 总体：O(n)

边界条件测试：
1. n=1：单节点树
2. n=2：两个节点的树  
3. 递增序列：形成右斜树
4. 递减序列：形成左斜树
5. 随机序列：验证正确性

工程化改进建议：
1. 添加输入验证，确保n在有效范围内
2. 添加内存分配检查，防止数组越界
3. 使用更安全的数组访问方式
4. 添加详细的错误处理机制
5. 添加单元测试用例

Python语言特性考量：
1. 使用列表代替数组，Python没有原生数组类型
2. 下标从0开始，但为了与算法描述一致，使用1-based索引
3. 注意Python的递归深度限制，使用迭代方式避免
4. 输入输出使用sys.stdin.read()提高效率

调试技巧：
1. 添加中间变量打印，如打印构建过程中的栈状态
2. 使用断言验证关键步骤的正确性
3. 对于大规模数据，使用性能分析工具
"""