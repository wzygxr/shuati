# 文本编辑器，能通过的Python版本
# 一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
# Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
# Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
# Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
# Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
# Prev       : 光标前移一个字符，操作保证光标不会到非法位置
# Next       : 光标后移一个字符，操作保证光标不会到非法位置
# Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
# 测试链接 : https://www.luogu.com.cn/problem/P4008
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例
# 一个能通过的版本，连数组都自己写扩容逻辑，IO彻底重写，看看就好
# 讲解172，讲解块状链表时，本题又讲了一遍，分块的方法，可以通过所有测试用例，更有学习意义

import random
import sys

# FHQ Treap节点类
class Node:
    def __init__(self, key):
        self.key = key
        self.size = 1
        self.priority = random.random()
        self.left = None
        self.right = None

# 更新节点大小
def update_size(node):
    if node is None:
        return
    node.size = 1
    if node.left is not None:
        node.size += node.left.size
    if node.right is not None:
        node.size += node.right.size

# 分裂操作
def split(root, k):
    if root is None:
        return None, None
    
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
    
    if left.priority >= right.priority:
        left.right = merge(left.right, right)
        update_size(left)
        return left
    else:
        right.left = merge(left, right.left)
        update_size(right)
        return right

# 中序遍历输出
def inorder(root, result):
    if root is None:
        return
    inorder(root.left, result)
    result.append(root.key)
    inorder(root.right, result)

def main():
    data = sys.stdin.read().splitlines()
    if not data:
        return
    
    op_count = int(data[0])
    index = 1
    
    root = None
    cursor = 0  # 光标位置
    
    for _ in range(op_count):
        if index >= len(data):
            break
            
        parts = data[index].split()
        index += 1
        
        if not parts:
            continue
            
        command = parts[0]
        
        if command == "Move":
            k = int(parts[1])
            cursor = k
        elif command == "Insert":
            n = int(parts[1])
            s = parts[2] if len(parts) > 2 else ""
            
            # 过滤有效字符
            valid_chars = []
            for c in s:
                if 32 <= ord(c) <= 126:
                    valid_chars.append(c)
            
            # 分裂树
            left, right = split(root, cursor)
            
            # 插入新节点
            for c in valid_chars:
                new_node = Node(c)
                left = merge(left, new_node)
            
            root = merge(left, right)
        elif command == "Delete":
            n = int(parts[1])
            
            left, right = split(root, cursor)
            mid, right = split(right, n)
            
            root = merge(left, right)
        elif command == "Get":
            n = int(parts[1])
            
            left, right = split(root, cursor)
            mid, right = split(right, n)
            
            result = []
            inorder(mid, result)
            print(''.join(result))
            
            root = merge(merge(left, mid), right)
        elif command == "Prev":
            cursor -= 1
        elif command == "Next":
            cursor += 1

if __name__ == "__main__":
    main()