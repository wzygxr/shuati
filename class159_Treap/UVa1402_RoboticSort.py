# UVa 1402 Robotic Sort
# 给定一个序列，每次找到当前序列中最小的元素，通过一系列相邻交换将其移到序列开头，求总的交换次数。
# 测试链接 : https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1402

import sys

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(1000000)

MAXN = 100001

# 数组元素，存储输入的序列
arr = [0] * MAXN

# 笛卡尔树需要的数组
stack = [0] * MAXN  # 单调栈，用于构建笛卡尔树
left = [0] * MAXN   # left[i]表示节点i的左子节点
right = [0] * MAXN  # right[i]表示节点i的右子节点

# 位置数组，记录每个值在原数组中的位置
pos = [0] * MAXN

# 使用笛卡尔树解法求机器人排序的总交换次数
# 核心思想：
# 1. 构建小根笛卡尔树，节点值为数组元素，key为数组下标
# 2. 通过分析笛卡尔树的结构来计算交换次数
# 3. 每个节点需要移动的距离等于其当前位置与目标位置的差的绝对值
def build_cartesian_tree(n):
    """
    使用笛卡尔树解法求机器人排序的总交换次数
    :param n: 序列长度
    :return: 总的交换次数
    """
    # 初始化，将所有节点的左右子节点设为0（空节点）
    for i in range(1, n + 1):
        left[i] = 0
        right[i] = 0
        # 记录每个值的位置（这里记录的是数组下标）
        pos[arr[i]] = i

    # 使用单调栈构建笛卡尔树（小根堆）
    top = 0  # 栈顶指针
    for i in range(1, n + 1):
        stack_pos = top
        # 维护单调栈，弹出比当前元素大的节点
        # 保证栈中节点的值按从小到大排列（小根堆性质）
        while stack_pos > 0 and arr[stack[stack_pos]] > arr[i]:
            stack_pos -= 1
        # 建立父子关系
        if stack_pos > 0:
            # 栈顶元素作为当前元素的父节点，当前元素作为其右子节点
            right[stack[stack_pos]] = i
        if stack_pos < top:
            # 当前节点的左子节点是最后被弹出的节点
            left[i] = stack[stack_pos + 1]
        # 将当前节点压入栈中
        stack[stack_pos + 1] = i
        # 更新栈顶指针
        top = stack_pos + 1

    # 通过DFS计算交换次数
    # 根节点是栈底元素stack[1]
    return dfs(stack[1])

# 深度优先搜索计算交换次数
def dfs(u):
    """
    深度优先搜索计算交换次数
    :param u: 当前节点索引
    :return: 以当前节点为根的子树中的总交换次数
    """
    # 如果当前节点为空，返回0
    if u == 0:
        return 0
    # 递归计算左右子树的交换次数
    left_swaps = dfs(left[u])
    right_swaps = dfs(right[u])
    
    # 计算当前节点需要的交换次数
    # 当前节点需要移到其在排序后的位置
    target_pos = u  # 在排序后的序列中，第u小的元素应该在位置u
    current_pos = pos[arr[u]]  # 当前位置
    
    # 交换次数等于当前位置与目标位置的距离
    current_swaps = abs(current_pos - target_pos)
    
    # 返回总交换次数：左子树交换次数 + 右子树交换次数 + 当前节点交换次数
    return left_swaps + right_swaps + current_swaps

def main():
    """
    主函数
    """
    while True:
        # 读取输入，处理多组测试数据
        line = input().strip()
        if not line:
            continue
        n = int(line)
        # 输入0表示结束
        if n == 0:
            break
        
        # 读取序列元素
        nums = list(map(int, input().split()))
        for i in range(1, n + 1):
            arr[i] = nums[i - 1]
        
        # 计算并输出总交换次数
        print(build_cartesian_tree(n))

if __name__ == "__main__":
    main()