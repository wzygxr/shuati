# 文艺平衡树，FHQ-Treap实现范围翻转，Python版本
# 长度为n的序列，下标从1开始，一开始序列为1, 2, ..., n
# 接下来会有k个操作，每个操作给定l，r，表示从l到r范围上的所有数字翻转
# 做完k次操作后，从左到右打印所有数字
# 1 <= n, k <= 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P3391
# 如下实现是Python的版本，Python版本和java版本逻辑完全一样
# 提交如下代码，可以通过所有测试用例

import random
import sys

class Node:
    def __init__(self, key):
        self.key = key
        self.size = 1
        self.priority = random.random()
        self.left = None
        self.right = None
        self.rev = False

# 更新节点大小
def update_size(node):
    if node is None:
        return
    node.size = 1
    if node.left is not None:
        node.size += node.left.size
    if node.right is not None:
        node.size += node.right.size

# 下传翻转标记
def push_down(node):
    if node is None or not node.rev:
        return
    
    # 交换左右子树
    node.left, node.right = node.right, node.left
    
    # 下传标记
    if node.left is not None:
        node.left.rev = not node.left.rev
    if node.right is not None:
        node.right.rev = not node.right.rev
    
    # 清除当前节点标记
    node.rev = False

# 分裂操作
def split(root, k):
    if root is None:
        return None, None
    
    push_down(root)
    
    left_size = root.left.size if root.left is not None else 0
    if left_size + 1 <= k:
        left = root
        left.right, right = split(root.right, k - left_size - 1)
    else:
        right = root
        left, right.left = split(root.left, k)
    
    update_size(root)
    return left, right

# 合并操作
def merge(left, right):
    if left is None:
        return right
    if right is None:
        return left
    
    push_down(left)
    push_down(right)
    
    if left.priority >= right.priority:
        left.right = merge(left.right, right)
        update_size(left)
        return left
    else:
        right.left = merge(left, right.left)
        update_size(right)
        return right

# 翻转操作
def reverse(root, l, r):
    # 分裂出左部分
    left, rest = split(root, l - 1)
    
    # 分裂出中间部分
    mid, right = split(rest, r - l + 1)
    
    # 翻转中间部分
    if mid is not None:
        mid.rev = not mid.rev
    
    # 合并回去
    return merge(merge(left, mid), right)

# 中序遍历输出
def inorder(root, result):
    if root is None:
        return
    
    push_down(root)
    inorder(root.left, result)
    result.append(root.key)
    inorder(root.right, result)

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    k = int(data[1])
    
    # 初始化序列
    root = None
    for i in range(1, n + 1):
        new_node = Node(i)
        root = merge(root, new_node)
    
    # 执行翻转操作
    index = 2
    for _ in range(k):
        if index + 1 >= len(data):
            break
        
        l = int(data[index])
        r = int(data[index + 1])
        index += 2
        
        root = reverse(root, l, r)
    
    # 输出结果
    result = []
    inorder(root, result)
    print(' '.join(map(str, result)))

if __name__ == "__main__":
    main()