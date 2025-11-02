# FHQ-Treap实现书架问题
# 洛谷 P2596 [ZJOI2006]书架
# 实现书架操作，支持将书置于顶部、底部、指定位置，查询书的位置等操作
# 测试链接 : https://www.luogu.com.cn/problem/P2596

import sys
import random
from io import StringIO

class BookshelfFHQTreap:
    def __init__(self, max_n=80001):
        """
        初始化FHQ Treap书架结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值（书的编号）
        self.position = [0] * self.MAXN # 节点在序列中的位置
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 子树大小
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
    def init(self):
        """
        初始化结构
        """
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.position = [0] * self.MAXN
        self.left = [0] * self.MAXN
        self.right = [0] * self.MAXN
        self.size = [0] * self.MAXN
        self.priority = [0.0] * self.MAXN
    
    def up(self, i):
        """
        更新节点信息
        
        Args:
            i: 节点编号
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
    
    def split_by_position(self, l, r, i, pos):
        """
        按位置分裂，将树i按照位置pos分裂为两棵树
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            pos: 分裂位置
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.position[i] <= pos:
                self.right[l] = i
                self.split_by_position(i, r, self.right[i], pos)
            else:
                self.left[r] = i
                self.split_by_position(l, i, self.left[i], pos)
            self.up(i)
    
    def split_by_book_id(self, l, r, i, book_id):
        """
        按书编号分裂，将树i按照书编号book_id分裂为两棵树
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            book_id: 分裂书编号
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= book_id:
                self.right[l] = i
                self.split_by_book_id(i, r, self.right[i], book_id)
            else:
                self.left[r] = i
                self.split_by_book_id(l, i, self.left[i], book_id)
            self.up(i)
    
    def merge(self, l, r):
        """
        合并操作，将两棵树l和r合并为一棵树
        
        Args:
            l: 左树根节点编号
            r: 右树根节点编号
            
        Returns:
            合并后树的根节点编号
        """
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.right[l] = self.merge(self.right[l], r)
            self.up(l)
            return l
        else:
            self.left[r] = self.merge(l, self.left[r])
            self.up(r)
            return r
    
    def find_position(self, i, book_id):
        """
        查找书的位置
        
        Args:
            i: 树根节点编号
            book_id: 书编号
            
        Returns:
            书的位置，如果不存在返回-1
        """
        if i == 0:
            return -1
        if self.key[i] == book_id:
            return self.position[i]
        elif self.key[i] > book_id:
            return self.find_position(self.left[i], book_id)
        else:
            return self.find_position(self.right[i], book_id)
    
    def find_book_by_position(self, i, pos):
        """
        根据位置查找书
        
        Args:
            i: 树根节点编号
            pos: 位置
            
        Returns:
            书编号，如果不存在返回-1
        """
        if i == 0:
            return -1
        if self.position[i] == pos:
            return self.key[i]
        elif self.position[i] > pos:
            return self.find_book_by_position(self.left[i], pos)
        else:
            return self.find_book_by_position(self.right[i], pos)
    
    def insert_top(self, book_id):
        """
        在顶部插入书
        
        Args:
            book_id: 书编号
        """
        # 分裂出前0本书（空）
        self.split_by_position(0, 0, self.head, 0)
        # 创建新节点
        self.cnt += 1
        self.key[self.cnt] = book_id
        self.position[self.cnt] = 1  # 新书放在位置1
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        # 更新所有书的位置（向后移动一位）
        self.update_position(self.right[0], 1)
        # 合并树
        self.head = self.merge(self.cnt, self.right[0])
    
    def update_position(self, i, delta):
        """
        更新子树中所有书的位置
        
        Args:
            i: 树根节点编号
            delta: 位置变化量
        """
        if i == 0:
            return
        self.position[i] += delta
        self.update_position(self.left[i], delta)
        self.update_position(self.right[i], delta)
    
    def insert_bottom(self, book_id):
        """
        在底部插入书
        
        Args:
            book_id: 书编号
        """
        # 分裂出前size[head]本书
        self.split_by_position(0, 0, self.head, self.size[self.head])
        # 创建新节点
        self.cnt += 1
        self.key[self.cnt] = book_id
        self.position[self.cnt] = self.size[self.head] + 1  # 新书放在最后
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        # 合并树
        self.head = self.merge(self.left[0], self.cnt)
    
    def insert_before(self, target_book_id, book_id):
        """
        在指定书前面插入
        
        Args:
            target_book_id: 目标书编号
            book_id: 插入的书编号
        """
        # 查找目标书的位置
        pos = self.find_position(self.head, target_book_id)
        if pos == -1:
            return
        # 分裂出前pos-1本书
        self.split_by_position(0, 0, self.head, pos - 1)
        # 创建新节点
        self.cnt += 1
        self.key[self.cnt] = book_id
        self.position[self.cnt] = pos  # 新书放在目标位置
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        # 更新后面所有书的位置（向后移动一位）
        self.update_position(self.left[0], 1)
        # 合并树
        self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def insert_after(self, target_book_id, book_id):
        """
        在指定书后面插入
        
        Args:
            target_book_id: 目标书编号
            book_id: 插入的书编号
        """
        # 查找目标书的位置
        pos = self.find_position(self.head, target_book_id)
        if pos == -1:
            return
        # 分裂出前pos本书
        self.split_by_position(0, 0, self.head, pos)
        # 创建新节点
        self.cnt += 1
        self.key[self.cnt] = book_id
        self.position[self.cnt] = pos + 1  # 新书放在目标位置后面
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        # 更新后面所有书的位置（向后移动一位）
        self.update_position(self.left[0], 1)
        # 合并树
        self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def move_to_top(self, book_id):
        """
        将书置于顶部
        
        Args:
            book_id: 书编号
        """
        # 查找书的位置
        pos = self.find_position(self.head, book_id)
        if pos == -1 or pos == 1:
            return  # 书不存在或已在顶部
        # 分裂出前pos-1本书
        self.split_by_position(0, 0, self.head, pos - 1)
        # 分裂出前pos本书
        middle = self.right[0]
        self.split_by_position(0, 0, middle, pos)
        # 取出要移动的书
        book = self.right[0]
        # 更新位置信息
        self.position[book] = 1
        self.update_position(self.left[0], -1)  # 前面的书位置前移
        self.update_position(self.left[book], 1)  # 后面的书位置后移（除了刚移动的书）
        # 重新合并树
        self.head = self.merge(self.merge(book, self.left[0]), self.left[book])
    
    def move_to_bottom(self, book_id):
        """
        将书置于底部
        
        Args:
            book_id: 书编号
        """
        # 查找书的位置
        pos = self.find_position(self.head, book_id)
        if pos == -1 or pos == self.size[self.head]:
            return  # 书不存在或已在底部
        # 分裂出前pos本书
        self.split_by_position(0, 0, self.head, pos)
        # 分裂出前pos+1本书
        middle = self.right[0]
        self.split_by_position(0, 0, middle, pos)
        # 取出要移动的书
        book = self.right[0]
        # 更新位置信息
        self.position[book] = self.size[self.head]
        self.update_position(self.left[0], -1)  # 前面的书位置前移（除了刚移动的书）
        self.update_position(self.left[book], -1)  # 后面的书位置前移
        # 重新合并树
        self.head = self.merge(self.merge(self.left[0], self.left[book]), book)
    
    def query_position(self, book_id):
        """
        查询书的位置
        
        Args:
            book_id: 书编号
            
        Returns:
            书的位置
        """
        return self.find_position(self.head, book_id)
    
    def query_book(self, pos):
        """
        查询指定位置的书
        
        Args:
            pos: 位置
            
        Returns:
            书编号
        """
        return self.find_book_by_position(self.head, pos)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """3 5
Top 2
Ask 2
Query 1
Bottom 3
Ask 3"""
    
    sys.stdin = StringIO(input_text)
    
    treap = BookshelfFHQTreap()
    treap.init()
    
    n, m = map(int, input().split())  # 书的总数和操作次数
    
    # 初始化书架，按顺序放入1到n本书
    for i in range(1, n + 1):
        treap.insert_bottom(i)
    
    # 处理操作
    for _ in range(m):
        operation = input().strip().split()
        
        if operation[0] == "Top":
            treap.move_to_top(int(operation[1]))
        elif operation[0] == "Bottom":
            treap.move_to_bottom(int(operation[1]))
        elif operation[0] == "Insert":
            target_book = int(operation[1])
            direction = int(operation[2])
            if direction == -1:
                # 在目标书前面插入
                treap.insert_before(target_book, target_book - 1)
            elif direction == 1:
                # 在目标书后面插入
                treap.insert_after(target_book, target_book + 1)
        elif operation[0] == "Ask":
            print(treap.query_position(int(operation[1])) - 1)  # 转换为0基索引
        elif operation[0] == "Query":
            print(treap.query_book(int(operation[1]) + 1))  # 转换为1基索引


if __name__ == "__main__":
    main()