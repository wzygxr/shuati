"""
文本编辑器，FHQ-Treap实现区间移动，Python版本（优化版）
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

Python实现优化考量：
- 使用迭代方式避免递归深度限制
- 优化内存使用，避免不必要的对象创建
- 添加边界检查和异常处理
- 使用更高效的字符串处理方式
"""

import random
import sys
from typing import List, Tuple, Optional

class FHQTreapNode:
    """FHQ-Treap节点类（优化版）"""
    __slots__ = ('char', 'left', 'right', 'size', 'priority')
    
    def __init__(self, char: str):
        self.char = char
        self.left: Optional['FHQTreapNode'] = None
        self.right: Optional['FHQTreapNode'] = None
        self.size = 1
        self.priority = random.random()

def update_size(node: Optional[FHQTreapNode]) -> int:
    """更新节点大小（优化版）"""
    if node is None:
        return 0
    left_size = node.left.size if node.left else 0
    right_size = node.right.size if node.right else 0
    node.size = left_size + right_size + 1
    return node.size

def merge(x: Optional[FHQTreapNode], y: Optional[FHQTreapNode]) -> Optional[FHQTreapNode]:
    """合并两个Treap（优化版）"""
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

def split_iterative(node: Optional[FHQTreapNode], k: int) -> Tuple[Optional[FHQTreapNode], Optional[FHQTreapNode]]:
    """按大小分割Treap（迭代版，避免递归深度）"""
    if node is None:
        return None, None
    
    # 使用迭代方式实现分割
    left_nodes = []
    right_nodes = []
    current = node
    
    while current is not None:
        left_size = current.left.size if current.left else 0
        
        if k <= left_size:
            # 当前节点属于右子树
            right_nodes.append(current)
            current = current.left
        else:
            # 当前节点属于左子树
            left_nodes.append(current)
            k -= left_size + 1
            current = current.right
    
    # 重建树结构
    left_tree = None
    for node in reversed(left_nodes):
        node.right = left_tree
        update_size(node)
        left_tree = node
    
    right_tree = None
    for node in reversed(right_nodes):
        node.left = right_tree
        update_size(node)
        right_tree = node
    
    return left_tree, right_tree

def get_chars_iterative(node: Optional[FHQTreapNode]) -> List[str]:
    """中序遍历获取字符（迭代版）"""
    result = []
    stack = []
    current = node
    
    while current is not None or stack:
        while current is not None:
            stack.append(current)
            current = current.left
        
        current = stack.pop()
        result.append(current.char)
        current = current.right
    
    return result

def build_tree_iterative(chars: List[str]) -> Optional[FHQTreapNode]:
    """构建字符的Treap树（迭代版）"""
    if not chars:
        return None
    
    # 使用迭代方式构建平衡的Treap
    nodes = [FHQTreapNode(c) for c in chars]
    
    # 构建平衡树
    stack = []
    for node in nodes:
        last = None
        while stack and stack[-1].priority > node.priority:
            last = stack.pop()
        
        if stack:
            stack[-1].right = node
        
        node.left = last
        stack.append(node)
    
    # 更新所有节点的大小
    def update_all_sizes(node: Optional[FHQTreapNode]) -> int:
        if node is None:
            return 0
        left_size = update_all_sizes(node.left)
        right_size = update_all_sizes(node.right)
        node.size = left_size + right_size + 1
        return node.size
    
    root = stack[0] if stack else None
    if root:
        update_all_sizes(root)
    
    return root

def filter_valid_chars(s: str, n: int) -> List[str]:
    """过滤有效字符（优化版）"""
    valid_chars = []
    count = 0
    
    for char in s:
        if count >= n:
            break
        if 32 <= ord(char) <= 126:
            valid_chars.append(char)
            count += 1
    
    return valid_chars

def main():
    """主函数（优化版）"""
    root: Optional[FHQTreapNode] = None
    cursor = 0  # 光标位置
    total_size = 0  # 文本总长度
    
    try:
        for line in sys.stdin:
            line = line.strip()
            if not line:
                continue
                
            parts = line.split()
            command = parts[0]
            
            if command == "Move":
                if len(parts) < 2:
                    continue
                try:
                    k = int(parts[1])
                    # 边界检查
                    if k < 0:
                        k = 0
                    if k > total_size:
                        k = total_size
                    cursor = k
                except ValueError:
                    continue
                    
            elif command == "Insert":
                if len(parts) < 3:
                    continue
                try:
                    n = int(parts[1])
                    # 边界检查
                    if n <= 0:
                        continue
                    
                    # 获取字符串
                    s = ' '.join(parts[2:])
                    
                    # 过滤有效字符
                    valid_chars = filter_valid_chars(s, n)
                    
                    if valid_chars:
                        # 分割文本
                        left_part, right_part = split_iterative(root, cursor)
                        
                        # 创建新节点
                        new_tree = build_tree_iterative(valid_chars)
                        
                        # 合并
                        if left_part is None:
                            root = merge(new_tree, right_part)
                        elif right_part is None:
                            root = merge(left_part, new_tree)
                        else:
                            root = merge(merge(left_part, new_tree), right_part)
                        
                        total_size += len(valid_chars)
                        
                except (ValueError, IndexError):
                    continue
                    
            elif command == "Delete":
                if len(parts) < 2:
                    continue
                try:
                    n = int(parts[1])
                    # 边界检查
                    if n <= 0 or cursor + n > total_size:
                        continue
                    
                    if root:
                        left_part, temp = split_iterative(root, cursor)
                        mid, right_part = split_iterative(temp, n) if temp else (None, None)
                        
                        root = merge(left_part, right_part)
                        total_size -= n
                        
                except ValueError:
                    continue
                    
            elif command == "Get":
                if len(parts) < 2:
                    continue
                try:
                    n = int(parts[1])
                    # 边界检查
                    if n <= 0 or cursor + n > total_size:
                        continue
                    
                    if root:
                        left_part, temp = split_iterative(root, cursor)
                        mid, right_part = split_iterative(temp, n) if temp else (None, None)
                        
                        if mid:
                            result = get_chars_iterative(mid)
                            print(''.join(result))
                        
                        # 恢复树结构
                        if left_part is None:
                            root = merge(mid, right_part)
                        elif right_part is None:
                            root = merge(left_part, mid)
                        else:
                            root = merge(merge(left_part, mid), right_part)
                            
                except ValueError:
                    continue
                    
            elif command == "Prev":
                if cursor > 0:
                    cursor -= 1
                    
            elif command == "Next":
                if cursor < total_size:
                    cursor += 1
                    
    except KeyboardInterrupt:
        # 优雅退出
        pass
    except Exception as e:
        # 异常处理
        print(f"Error: {e}", file=sys.stderr)

if __name__ == "__main__":
    main()