# AtCoder AGC005B Minimum Sum
# 给定一个长度为 n 的排列，求所有连续子数组最小值之和
# 测试链接 : https://atcoder.jp/contests/agc005/tasks/agc005_b

import sys

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(100000)

MAXN = 200001

# 数组元素，存储输入的排列
arr = [0] * MAXN

# 笛卡尔树需要的数组
stack = [0] * MAXN  # 单调栈，用于构建笛卡尔树
left_child = [0] * MAXN  # left_child[i]表示节点i的左子节点
right_child = [0] * MAXN  # right_child[i]表示节点i的右子节点

# 使用笛卡尔树解法求所有连续子数组最小值之和
# 核心思想：
# 1. 构建小根笛卡尔树，每个节点代表数组中的一个元素
# 2. 每个节点对结果的贡献等于其值乘以经过该节点的子数组数量
# 3. 经过该节点的子数组数量 = (左子树大小+1) * (右子树大小+1)
def buildCartesianTree(n):
    """
    使用笛卡尔树解法求所有连续子数组最小值之和
    :param n: 数组长度
    :return: 所有连续子数组最小值之和
    """
    # 初始化，将所有节点的左右子节点设为0（空节点）
    for i in range(1, n+1):
        left_child[i] = 0
        right_child[i] = 0

    # 使用单调栈构建笛卡尔树（小根堆）
    top = 0  # 栈顶指针
    for i in range(1, n+1):
        pos = top
        # 维护单调栈，弹出比当前元素大的节点
        # 保证栈中节点的值按从小到大排列（小根堆性质）
        while pos > 0 and arr[stack[pos]] > arr[i]:
            pos -= 1
        # 建立父子关系
        if pos > 0:
            # 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
            right_child[stack[pos]] = i
        if pos < top:
            # 当前节点的左子节点是最后被弹出的节点
            left_child[i] = stack[pos + 1]
        # 将当前节点压入栈中
        stack[pos + 1] = i
        # 更新栈顶指针
        top = pos + 1

    # 通过DFS计算所有子数组最小值之和
    # 根节点是栈底元素stack[1]
    return dfs(stack[1])

# 计算以指定节点为根的子树大小
def get_size(u):
    """
    计算以指定节点为根的子树大小
    :param u: 节点索引
    :return: 子树大小
    """
    # 如果当前节点为空，返回0
    if u == 0:
        return 0
    # 递归计算子树大小：左子树大小 + 右子树大小 + 1（当前节点）
    return 1 + get_size(left_child[u]) + get_size(right_child[u])

# 深度优先搜索计算结果
def dfs(u):
    """
    深度优先搜索计算结果
    :param u: 当前节点索引
    :return: 以当前节点为根的子树中所有子数组最小值之和
    """
    # 如果当前节点为空，返回0
    if u == 0:
        return 0
    # 递归计算左右子树的贡献
    left_contribution = dfs(left_child[u])
    right_contribution = dfs(right_child[u])
    
    # 计算当前节点的贡献
    # 当前节点作为最小值的子数组数量 = (左子树大小+1) * (右子树大小+1)
    left_size = get_size(left_child[u])
    right_size = get_size(right_child[u])
    # 当前节点的贡献 = 节点值 * 经过该节点的子数组数量
    current_contribution = arr[u] * (left_size + 1) * (right_size + 1)
    
    # 返回总贡献：左子树贡献 + 右子树贡献 + 当前节点贡献
    return left_contribution + right_contribution + current_contribution

def main():
    """
    主函数
    """
    n = int(input())
    arr_list = list(map(int, input().split()))
    for i in range(1, n+1):
        arr[i] = arr_list[i-1]
    print(buildCartesianTree(n))

if __name__ == "__main__":
    main()