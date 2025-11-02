# LeetCode 84. Largest Rectangle in Histogram
# 给定 n 个非负整数，表示直方图中各个柱子的高度，每个柱子宽度为 1
# 求能勾勒出的最大矩形面积
# 测试链接 : https://leetcode.com/problems/largest-rectangle-in-histogram/

import sys

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(100000)

MAXN = 100001

# heights数组存储柱子高度，下标从1开始
heights = [0] * MAXN

# 笛卡尔树需要的数组
stack = [0] * MAXN  # 单调栈，用于构建笛卡尔树
left = [0] * MAXN   # left[i]表示节点i的左子节点
right = [0] * MAXN  # right[i]表示节点i的右子节点

# 深度优先搜索计算以每个节点为最小高度的最大矩形面积
def dfs(u):
    """
    深度优先搜索计算以每个节点为最小高度的最大矩形面积
    :param u: 当前节点索引
    :return: 以当前节点为最小高度的子树中的最大矩形面积
    """
    if u == 0:
        return 0
    # 递归计算左右子树中的最大面积
    leftSize = dfs(left[u])
    rightSize = dfs(right[u])
    # 计算当前节点为根的子树大小（即以当前高度为最小高度能覆盖的宽度）
    size = leftSize + rightSize + 1
    # 计算以当前节点高度为最小高度的矩形面积
    area = size * heights[u]
    # 返回当前子树中的最大面积（当前节点面积与左右子树最大面积的较大值）
    return max(area, max(leftSize, rightSize))

# 使用笛卡尔树解法
# 以柱子下标为k，高度为w，构建小根笛卡尔树
# 每个节点的子树大小即为该高度所能覆盖的最大宽度
# 节点值乘以子树大小即为以该节点为最小高度的最大矩形面积
def buildCartesianTree():
    """
    使用笛卡尔树解法求直方图中最大矩形面积
    核心思想：
    1. 以柱子下标为k，高度为w，构建小根笛卡尔树
    2. 每个节点的子树大小即为该高度所能覆盖的最大宽度
    3. 节点值乘以子树大小即为以该节点为最小高度的最大矩形面积
    :return: 最大矩形面积
    """
    global n
    # 初始化，将所有节点的左右子节点设为0（空节点）
    for i in range(1, n+1):
        left[i] = 0
        right[i] = 0

    # 使用单调栈构建笛卡尔树（小根堆）
    top = 0  # 栈顶指针
    for i in range(1, n+1):
        pos = top
        # 维护单调栈，弹出比当前元素大的节点
        # 保证栈中节点的高度按从小到大排列（小根堆性质）
        while pos > 0 and heights[stack[pos]] > heights[i]:
            pos -= 1
        # 建立父子关系
        if pos > 0:
            # 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
            right[stack[pos]] = i
        if pos < top:
            # 当前节点的左子节点是最后被弹出的节点
            left[i] = stack[pos + 1]
        # 将当前节点压入栈中
        stack[pos + 1] = i
        # 更新栈顶指针
        top = pos + 1

    # 通过DFS计算最大面积
    # 根节点是栈底元素stack[1]
    return dfs(stack[1])

# 主函数
if __name__ == "__main__":
    n = int(input())
    heights_list = list(map(int, input().split()))
    for i in range(1, n+1):
        heights[i] = heights_list[i-1]
    print(buildCartesianTree())