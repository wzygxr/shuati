from typing import Optional, List

"""
二项堆（Binomial Heap）实现
算法思想：二项堆是一组二项树的集合，每个二项树满足堆性质
支持高效的合并操作，是可并堆的一种重要实现

摊还分析与势能分析：
- 势能函数：选择为堆中树的数量
- 合并操作的摊还时间复杂度：O(log n)
- 插入操作的摊还时间复杂度：O(1)
- 提取最小操作的摊还时间复杂度：O(log n)

相关题目：
1. LeetCode 23. 合并K个排序链表 - https://leetcode-cn.com/problems/merge-k-sorted-lists/
2. LeetCode 1046. 最后一块石头的重量 - https://leetcode-cn.com/problems/last-stone-weight/
3. CodeChef - CHEFBM - https://www.codechef.com/problems/CHEFBM
4. AtCoder - C - Min Difference - https://atcoder.jp/contests/abc129/tasks/abc129_c
"""


class Node:
    """
    二项树节点类
    """
    def __init__(self, key):
        self.key = key  # 节点值
        self.degree = 0  # 节点的度
        self.parent = None  # 父节点
        self.child = None  # 第一个子节点
        self.sibling = None  # 下一个兄弟节点


class BinomialHeap:
    """
    二项堆类
    """
    def __init__(self):
        self.head = None  # 根节点列表的头指针
        self.min_node = None  # 最小节点引用
        self.size = 0  # 节点数量

    def is_empty(self) -> bool:
        """
        检查堆是否为空
        """
        return self.head is None

    def find_min(self) -> int:
        """
        获取堆中的最小元素
        """
        if self.is_empty():
            raise ValueError("Heap is empty")
        return self.min_node.key

    def insert(self, key: int) -> None:
        """
        插入新元素
        摊还时间复杂度：O(1)
        """
        temp_heap = BinomialHeap()
        new_node = Node(key)
        temp_heap.head = new_node
        temp_heap.min_node = new_node
        temp_heap.size = 1
        
        # 合并当前堆和只有一个节点的临时堆
        self.merge(temp_heap)

    def extract_min(self) -> int:
        """
        提取并返回堆中的最小元素
        摊还时间复杂度：O(log n)
        """
        if self.is_empty():
            raise ValueError("Heap is empty")

        # 找到包含最小节点的树，并从根列表中移除
        prev_min = None
        curr = self.head
        min_node = self.min_node
        
        # 查找最小节点的前一个节点
        if self.head != min_node:
            while curr is not None and curr.sibling != min_node:
                curr = curr.sibling
            prev_min = curr
        
        # 从根列表中移除最小节点
        if prev_min is None:
            self.head = min_node.sibling
        else:
            prev_min.sibling = min_node.sibling
        
        # 将最小节点的子树添加到一个新的堆中
        child_heap = BinomialHeap()
        if min_node.child is not None:
            child = min_node.child
            # 反转子树列表，并设置父节点为None
            prev = None
            while child is not None:
                next_node = child.sibling
                child.sibling = prev
                child.parent = None
                prev = child
                child = next_node
            child_heap.head = prev
            # 重新计算新堆的最小节点
            child_heap._update_min_node()
        
        # 合并原堆（已移除最小节点）和子堆
        if self.head is not None:
            self.merge(child_heap)
        else:
            self.head = child_heap.head
            self.min_node = child_heap.min_node
        
        self.size -= 1
        return min_node.key

    def merge(self, other_heap: 'BinomialHeap') -> None:
        """
        合并两个二项堆
        摊还时间复杂度：O(log n)
        """
        if other_heap is None or other_heap.is_empty():
            return
        if self.is_empty():
            self.head = other_heap.head
            self.min_node = other_heap.min_node
            self.size = other_heap.size
            return

        # 合并两个堆的根列表（按度数排序）
        new_head = self._merge_root_lists(self.head, other_heap.head)
        self.head = None
        self.min_node = None
        
        if new_head is None:
            return

        # 合并相同度数的树
        prev = None
        curr = new_head
        next_node = curr.sibling
        
        while next_node is not None:
            # 如果当前树和下一棵树度数不同，或者下下棵树和当前树度数相同，则移动指针
            if (curr.degree != next_node.degree or 
                (next_node.sibling is not None and next_node.sibling.degree == curr.degree)):
                prev = curr
                curr = next_node
            else:
                # 合并度数相同的树
                if curr.key <= next_node.key:
                    # curr成为父节点
                    curr.sibling = next_node.sibling
                    self._link_trees(next_node, curr)
                else:
                    # next成为父节点
                    if prev is None:
                        new_head = next_node
                    else:
                        prev.sibling = next_node
                    self._link_trees(curr, next_node)
                    curr = next_node
            next_node = curr.sibling
        
        self.head = new_head
        # 更新最小节点和大小
        self._update_min_node()
        self.size += other_heap.size

    def _merge_root_lists(self, h1: Node, h2: Node) -> Optional[Node]:
        """
        合并两个有序的根列表（按度数递增排序）
        """
        if h1 is None:
            return h2
        if h2 is None:
            return h1
        
        # 选择度数较小的作为新的头节点
        if h1.degree <= h2.degree:
            head = h1
            h1 = h1.sibling
        else:
            head = h2
            h2 = h2.sibling
        
        current = head
        # 合并剩余节点
        while h1 is not None and h2 is not None:
            if h1.degree <= h2.degree:
                current.sibling = h1
                h1 = h1.sibling
            else:
                current.sibling = h2
                h2 = h2.sibling
            current = current.sibling
        
        # 连接剩余的节点
        if h1 is not None:
            current.sibling = h1
        else:
            current.sibling = h2
        
        return head

    def _link_trees(self, child: Node, parent: Node) -> None:
        """
        将child树链接到parent树下
        """
        child.parent = parent
        child.sibling = parent.child
        parent.child = child
        parent.degree += 1

    def _update_min_node(self) -> None:
        """
        更新最小节点引用
        """
        min_node = None
        current = self.head
        
        while current is not None:
            if min_node is None or current.key < min_node.key:
                min_node = current
            current = current.sibling
        
        self.min_node = min_node

    def get_size(self) -> int:
        """
        获取堆的大小
        """
        return self.size

    def print_heap(self) -> None:
        """
        打印堆的结构（用于调试）
        """
        print("Binomial Heap Structure:")
        if self.is_empty():
            print("Empty heap")
            return
        self._print_node(self.head, 0)

    def _print_node(self, node: Node, level: int) -> None:
        """
        递归打印节点及其子节点
        """
        if node is None:
            return
        
        # 打印当前节点
        print("  " * level + f"Key: {node.key}, Degree: {node.degree}")
        
        # 打印子节点
        if node.child is not None:
            self._print_node(node.child, level + 1)
        
        # 打印兄弟节点
        self._print_node(node.sibling, level)


# 测试函数
def test_binomial_heap():
    heap = BinomialHeap()
    
    # 测试插入操作
    print("插入元素: 10, 20, 5, 15, 30")
    heap.insert(10)
    heap.insert(20)
    heap.insert(5)
    heap.insert(15)
    heap.insert(30)
    
    print(f"堆的大小: {heap.get_size()}")
    print(f"最小元素: {heap.find_min()}")
    
    # 打印堆结构
    heap.print_heap()
    
    # 测试合并操作
    heap2 = BinomialHeap()
    heap2.insert(8)
    heap2.insert(12)
    heap2.insert(2)
    
    print("\n合并另一个堆（元素: 8, 12, 2）")
    heap.merge(heap2)
    
    print(f"合并后堆的大小: {heap.get_size()}")
    print(f"合并后最小元素: {heap.find_min()}")
    
    # 打印堆结构
    heap.print_heap()
    
    # 测试提取最小操作
    print(f"\n提取最小元素: {heap.extract_min()}")
    print(f"提取后最小元素: {heap.find_min()}")
    
    print(f"\n提取最小元素: {heap.extract_min()}")
    print(f"提取后最小元素: {heap.find_min()}")
    
    # 打印最终堆结构
    heap.print_heap()


if __name__ == "__main__":
    test_binomial_heap()