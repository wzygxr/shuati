# 笛卡尔树模板代码
# 笛卡尔树是一种特殊的二叉搜索树，同时满足堆的性质
# 构建时间复杂度：O(n)，使用单调栈优化

import sys

# 增加递归深度限制，防止处理大数据时出现栈溢出
sys.setrecursionlimit(1000000)

# 全局变量定义
MAXN = 100001  # 最大节点数
n = 0  # 输入数组的长度
arr = [0] * (MAXN + 1)  # 数组从1开始索引，存储输入的数值
stack = [0] * (MAXN + 1)  # 单调栈，用于构建笛卡尔树
left = [0] * (MAXN + 1)  # left[i]表示节点i的左子节点索引，0表示没有左子节点
right = [0] * (MAXN + 1)  # right[i]表示节点i的右子节点索引，0表示没有右子节点

# 构建笛卡尔树（小根堆）
def build():
    global n, arr, stack, left, right
    # 初始化，将所有节点的左右子节点设为0（空节点）
    for i in range(1, n + 1):
        left[i] = 0
        right[i] = 0
    
    # 使用单调栈构建笛卡尔树
    top = 0  # 栈顶指针
    for i in range(1, n + 1):
        pos = top
        # 维护单调栈，弹出比当前元素大的节点
        # 保证栈中节点的值按从小到大排列（小根堆性质）
        while pos > 0 and arr[stack[pos]] > arr[i]:
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

# 深度优先遍历笛卡尔树，用于验证和展示树结构
def dfs(u):
    if u == 0:
        return
    print(f"Node {u}, value: {arr[u]}")
    print(f"Left child of {u}: {left[u]}")
    print(f"Right child of {u}: {right[u]}")
    dfs(left[u])
    dfs(right[u])

# 中序遍历验证二叉搜索树性质（按索引顺序）
def inorder(u):
    if u == 0:
        return
    inorder(left[u])
    print(arr[u], end=' ')
    inorder(right[u])

# 验证堆性质，检查是否满足小根堆
def checkHeap(u):
    if u == 0:
        return True
    # 检查左子节点是否满足小根堆性质
    if left[u] != 0 and arr[left[u]] < arr[u]:
        return False
    # 检查右子节点是否满足小根堆性质
    if right[u] != 0 and arr[right[u]] < arr[u]:
        return False
    # 递归检查左右子树
    return checkHeap(left[u]) and checkHeap(right[u])

# 主函数
def main():
    global n, arr
    n = int(input())
    # 输入数组，索引从1开始
    nums = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = nums[i - 1]
    
    # 构建笛卡尔树
    build()
    
    # 获取根节点（栈底元素）
    root = stack[1]
    
    print(f"笛卡尔树构建完成，根节点是: {root}")
    print("\n笛卡尔树结构:")
    dfs(root)
    
    print("\n中序遍历结果:")
    inorder(root)
    print()
    
    print(f"\n验证堆性质: {'通过' if checkHeap(root) else '未通过'}")

if __name__ == "__main__":
    main()