"""
文本编辑器，FHQ-Treap实现区间移动，Python版本
一开始文本为空，光标在文本开头，也就是1位置，请实现如下6种操作
Move k     : 将光标移动到第k个字符之后，操作保证光标不会到非法位置
Insert n s : 在光标处插入长度为n的字符串s，光标位置不变
Delete n   : 删除光标后的n个字符，光标位置不变，操作保证有足够字符
Get n      : 输出光标后的n个字符，光标位置不变，操作保证有足够字符
Prev       : 光标前移一个字符，操作保证光标不会到非法位置
Next       : 光标后移一个字符，操作保证光标不会到非法位置
Insert操作时，字符串s中ASCII码在[32,126]范围上的字符一定有n个，其他字符请过滤掉
测试链接 : https://www.luogu.com.cn/problem/P4008

时间复杂度和空间复杂度分析：
- 时间复杂度：所有操作平均O(log n)，其中n为文本长度
- 空间复杂度：O(n)，存储文本字符和Treap节点
最优解：FHQ-Treap是解决此类区间操作问题的经典最优解

Python实现注意事项：
- 由于Python的递归深度限制，对于大数据量可能需要优化
- 使用随机优先级保证Treap的平衡性
- 注意字符串操作和内存管理
"""

import random
import sys

class FHQTreapNode:
    """FHQ-Treap节点类"""
    def __init__(self, char):
        self.char = char
        self.left = None
        self.right = None
        self.size = 1
        self.priority = random.random()

def update_size(node):
    """更新节点大小"""
    if node is None:
        return 0
    left_size = node.left.size if node.left else 0
    right_size = node.right.size if node.right else 0
    node.size = left_size + right_size + 1
    return node.size

def merge(x, y):
    """合并两个Treap"""
    if x is None:
        return y
    if y is None:
        return x
    
    if x.priority > y.priority:
        x.right = merge(x.right, y)
        update_size(x)
        return x
    else:
        y.left = merge(x, y.left)
        update_size(y)
        return y

def split(node, k):
    """按大小分割Treap"""
    if node is None:
        return None, None
    
    left_size = node.left.size if node.left else 0
    
    if k <= left_size:
        left, right = split(node.left, k)
        node.left = right
        update_size(node)
        return left, node
    else:
        left, right = split(node.right, k - left_size - 1)
        node.right = left
        update_size(node)
        return node, right

def get_chars(node, result):
    """中序遍历获取字符"""
    if node is None:
        return
    get_chars(node.left, result)
    result.append(node.char)
    get_chars(node.right, result)

def build_tree(chars):
    """构建字符的Treap树"""
    if not chars:
        return None
    
    # 使用递归构建平衡的Treap
    def build(l, r):
        if l > r:
            return None
        mid = (l + r) // 2
        node = FHQTreapNode(chars[mid])
        node.left = build(l, mid - 1)
        node.right = build(mid + 1, r)
        update_size(node)
        return node
    
    return build(0, len(chars) - 1)

def main():
    """主函数"""
    root = None
    cursor = 0  # 光标位置
    total_size = 0  # 文本总长度
    
    for line in sys.stdin:
        parts = line.strip().split()
        if not parts:
            continue
            
        command = parts[0]
        
        if command == "Move":
            k = int(parts[1])
            cursor = k
            
        elif command == "Insert":
            n = int(parts[1])
            # 获取字符串（可能包含空格）
            s = ' '.join(parts[2:])[:n]
            
            # 过滤有效字符
            valid_chars = [c for c in s if 32 <= ord(c) <= 126]
            
            if valid_chars:
                # 分割文本
                left_part, right_part = split(root, cursor) if root else (None, None)
                
                # 创建新节点
                new_tree = build_tree(valid_chars)
                
                # 合并
                if left_part is None:
                    root = merge(new_tree, right_part)
                elif right_part is None:
                    root = merge(left_part, new_tree)
                else:
                    root = merge(merge(left_part, new_tree), right_part)
                
                total_size += len(valid_chars)
                
        elif command == "Delete":
            n = int(parts[1])
            
            if root:
                left_part, temp = split(root, cursor)
                mid, right_part = split(temp, n) if temp else (None, None)
                
                root = merge(left_part, right_part)
                total_size -= n
                
        elif command == "Get":
            n = int(parts[1])
            
            if root:
                left_part, temp = split(root, cursor)
                mid, right_part = split(temp, n) if temp else (None, None)
                
                if mid:
                    result = []
                    get_chars(mid, result)
                    print(''.join(result))
                
                # 恢复树结构
                if left_part is None:
                    root = merge(mid, right_part)
                elif right_part is None:
                    root = merge(left_part, mid)
                else:
                    root = merge(merge(left_part, mid), right_part)
                    
        elif command == "Prev":
            if cursor > 0:
                cursor -= 1
                
        elif command == "Next":
            if cursor < total_size:
                cursor += 1

if __name__ == "__main__":
    main()