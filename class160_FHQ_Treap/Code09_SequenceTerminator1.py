# FHQ-Treap实现序列终结者
# 洛谷 P4146 序列终结者
# 实现序列操作，支持区间翻转、区间加、区间最大值查询等操作
# 测试链接 : https://www.luogu.com.cn/problem/P4146

import sys
import random
from io import StringIO

class SequenceTerminatorFHQTreap:
    def __init__(self, max_n=500001):
        """
        初始化FHQ Treap序列终结者结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值（序列中的值）
        self.add = [0] * self.MAXN      # 节点的加法标记
        self.max_val = [0] * self.MAXN  # 节点的最大值
        self.reverse = [False] * self.MAXN  # 是否需要翻转标记
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
        self.add = [0] * self.MAXN
        self.max_val = [0] * self.MAXN
        self.reverse = [False] * self.MAXN
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
        self.max_val[i] = self.key[i]
        if self.left[i] != 0:
            self.max_val[i] = max(self.max_val[i], self.max_val[self.left[i]])
        if self.right[i] != 0:
            self.max_val[i] = max(self.max_val[i], self.max_val[self.right[i]])
    
    def down(self, i):
        """
        下传标记
        
        Args:
            i: 节点编号
        """
        if self.add[i] != 0:
            if self.left[i] != 0:
                self.key[self.left[i]] += self.add[i]
                self.add[self.left[i]] += self.add[i]
                self.max_val[self.left[i]] += self.add[i]
            if self.right[i] != 0:
                self.key[self.right[i]] += self.add[i]
                self.add[self.right[i]] += self.add[i]
                self.max_val[self.right[i]] += self.add[i]
            self.add[i] = 0
        if self.reverse[i]:
            if self.left[i] != 0:
                self.reverse[self.left[i]] = not self.reverse[self.left[i]]
            if self.right[i] != 0:
                self.reverse[self.right[i]] = not self.reverse[self.right[i]]
            # 交换左右子树
            self.left[i], self.right[i] = self.right[i], self.left[i]
            self.reverse[i] = False
    
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
            self.down(i)
            if self.size[self.left[i]] + 1 <= pos:
                self.right[l] = i
                self.split_by_position(i, r, self.right[i], pos - self.size[self.left[i]] - 1)
            else:
                self.left[r] = i
                self.split_by_position(l, i, self.left[i], pos)
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
            self.down(l)
            self.right[l] = self.merge(self.right[l], r)
            self.up(l)
            return l
        else:
            self.down(r)
            self.left[r] = self.merge(l, self.left[r])
            self.up(r)
            return r
    
    def add_range(self, l, r, value):
        """
        区间加法
        
        Args:
            l: 区间左端点
            r: 区间右端点
            value: 加法值
        """
        self.split_by_position(0, 0, self.head, l - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, r - l + 1)
        middle_tree = self.right[0]
        
        # 对中间的树进行操作
        self.key[middle_tree] += value
        self.add[middle_tree] += value
        self.max_val[middle_tree] += value
        
        # 重新合并
        self.head = self.merge(self.merge(self.left[0], middle_tree), self.right[0])
    
    def reverse_range(self, l, r):
        """
        区间翻转
        
        Args:
            l: 区间左端点
            r: 区间右端点
        """
        self.split_by_position(0, 0, self.head, l - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, r - l + 1)
        middle_tree = self.right[0]
        
        # 对中间的树进行翻转操作
        self.reverse[middle_tree] = not self.reverse[middle_tree]
        
        # 重新合并
        self.head = self.merge(self.merge(self.left[0], middle_tree), self.right[0])
    
    def query_max(self, l, r):
        """
        查询区间最大值
        
        Args:
            l: 区间左端点
            r: 区间右端点
            
        Returns:
            区间最大值
        """
        self.split_by_position(0, 0, self.head, l - 1)
        left_tree = self.right[0]
        self.split_by_position(0, 0, left_tree, r - l + 1)
        middle_tree = self.right[0]
        
        result = self.max_val[middle_tree]
        
        # 重新合并
        self.head = self.merge(self.merge(self.left[0], middle_tree), self.right[0])
        
        return result
    
    def insert(self, pos, value):
        """
        插入节点
        
        Args:
            pos: 插入位置
            value: 插入值
        """
        self.split_by_position(0, 0, self.head, pos)
        self.cnt += 1
        self.key[self.cnt] = value
        self.max_val[self.cnt] = value
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        self.head = self.merge(self.merge(self.left[0], self.cnt), self.right[0])


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """5 5
1 2 4 5 10
1 2 4 5
2 2 4
3 2 4
1 1 5 1
3 1 5"""
    
    sys.stdin = StringIO(input_text)
    
    treap = SequenceTerminatorFHQTreap()
    treap.init()
    
    n, m = map(int, input().split())  # 序列长度和操作次数
    
    # 读取初始序列
    sequence = list(map(int, input().split()))
    
    # 初始化序列
    for i in range(n):
        treap.insert(i + 1, sequence[i])
    
    # 处理操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        
        if operation[0] == 1:
            # 区间加法
            l, r, value = operation[1], operation[2], operation[3]
            treap.add_range(l, r, value)
        elif operation[0] == 2:
            # 区间翻转
            l, r = operation[1], operation[2]
            treap.reverse_range(l, r)
        else:
            # 查询区间最大值
            l, r = operation[1], operation[2]
            print(treap.query_max(l, r))


if __name__ == "__main__":
    main()