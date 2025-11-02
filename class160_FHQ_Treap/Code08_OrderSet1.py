# FHQ-Treap实现Order Statistic Set
# SPOJ ORDERSET - Order statistic set
# 实现有序集合，支持插入、删除、查询第k小数、查询某数的排名等操作
# 题目链接: https://www.spoj.com/problems/ORDERSET/
# 题目描述: 维护一个动态集合，支持插入、删除、查询第k小数、查询某数的排名等操作
# 操作类型:
# I x : 插入元素x
# D x : 删除元素x
# K x : 查询第x小的元素
# C x : 查询元素x的排名（比x小的数的个数）

import sys
import random
from io import StringIO

class OrderSetFHQTreap:
    def __init__(self, max_n=200001):
        """
        初始化FHQ Treap有序集合结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值
        self.count = [0] * self.MAXN    # 节点key的计数
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 数字总数
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
    def init(self):
        """
        初始化结构
        """
        self.head = 0
        self.cnt = 0
        self.key = [0] * self.MAXN
        self.count = [0] * self.MAXN
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
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + self.count[i]
    
    def split(self, l, r, i, num):
        """
        按值分裂，将树i按照数值num分裂为两棵树
        
        Args:
            l: 左树根节点编号（结果）
            r: 右树根节点编号（结果）
            i: 待分裂的树根节点编号
            num: 分裂数值
        """
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            if self.key[i] <= num:
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                self.left[r] = i
                self.split(l, i, self.left[i], num)
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
    
    def find(self, i, num):
        """
        查找值为num的节点
        
        Args:
            i: 树根节点编号
            num: 查找的数值
            
        Returns:
            节点编号，如果不存在返回0
        """
        if i == 0:
            return 0
        if self.key[i] == num:
            return i
        elif self.key[i] > num:
            return self.find(self.left[i], num)
        else:
            return self.find(self.right[i], num)
    
    def change_count(self, i, num, change):
        """
        改变节点计数
        
        Args:
            i: 树根节点编号
            num: 目标数值
            change: 变化量
        """
        if self.key[i] == num:
            self.count[i] += change
        elif self.key[i] > num:
            self.change_count(self.left[i], num, change)
        else:
            self.change_count(self.right[i], num, change)
        self.up(i)
    
    def insert(self, num):
        """
        插入数值
        
        Args:
            num: 插入的数值
        """
        if self.find(self.head, num) != 0:
            self.change_count(self.head, num, 1)
        else:
            self.split(0, 0, self.head, num)
            self.cnt += 1
            self.key[self.cnt] = num
            self.count[self.cnt] = self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def remove(self, num):
        """
        删除数值
        
        Args:
            num: 删除的数值
        """
        i = self.find(self.head, num)
        if i != 0:
            if self.count[i] > 1:
                self.change_count(self.head, num, -1)
            else:
                self.split(0, 0, self.head, num)
                lm = self.right[0]
                r = self.left[0]
                self.split(0, 0, lm, num - 1)
                l = self.right[0]
                self.head = self.merge(l, r)
    
    def small(self, i, num):
        """
        计算小于num的数的个数
        
        Args:
            i: 树根节点编号
            num: 比较数值
            
        Returns:
            小于num的数的个数
        """
        if i == 0:
            return 0
        if self.key[i] >= num:
            return self.small(self.left[i], num)
        else:
            return self.size[self.left[i]] + self.count[i] + self.small(self.right[i], num)
    
    def rank(self, num):
        """
        查询数值num的排名
        
        Args:
            num: 查询的数值
            
        Returns:
            数值num的排名
        """
        return self.small(self.head, num) + 1
    
    def index(self, i, x):
        """
        查询排名为x的数值
        
        Args:
            i: 树根节点编号
            x: 排名
            
        Returns:
            排名为x的数值
        """
        if self.size[self.left[i]] >= x:
            return self.index(self.left[i], x)
        elif self.size[self.left[i]] + self.count[i] < x:
            return self.index(self.right[i], x - self.size[self.left[i]] - self.count[i])
        return self.key[i]
    
    def index_by_rank(self, x):
        """
        查询排名为x的数值
        
        Args:
            x: 排名
            
        Returns:
            排名为x的数值，如果不存在返回None
        """
        if x < 1 or x > self.size[self.head]:
            return None  # 表示不存在
        return self.index(self.head, x)
    
    def pre(self, i, num):
        """
        查询数值num的前驱
        
        Args:
            i: 树根节点编号
            num: 查询数值
            
        Returns:
            数值num的前驱
        """
        if i == 0:
            return -2147483648  # Integer.MIN_VALUE
        if self.key[i] >= num:
            return self.pre(self.left[i], num)
        else:
            return max(self.key[i], self.pre(self.right[i], num))
    
    def pre_by_value(self, num):
        """
        查询数值num的前驱
        
        Args:
            num: 查询数值
            
        Returns:
            数值num的前驱
        """
        return self.pre(self.head, num)
    
    def post(self, i, num):
        """
        查询数值num的后继
        
        Args:
            i: 树根节点编号
            num: 查询数值
            
        Returns:
            数值num的后继
        """
        if i == 0:
            return 2147483647  # Integer.MAX_VALUE
        if self.key[i] <= num:
            return self.post(self.right[i], num)
        else:
            return min(self.key[i], self.post(self.left[i], num))
    
    def post_by_value(self, num):
        """
        查询数值num的后继
        
        Args:
            num: 查询数值
            
        Returns:
            数值num的后继
        """
        return self.post(self.head, num)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """11
I -1
I -1
I 2
C 0
K 2
D -1
K 1
K 2
K 4
I 5
C 5"""
    
    sys.stdin = StringIO(input_text)
    
    treap = OrderSetFHQTreap()
    treap.init()
    
    q = int(input())  # 操作次数
    
    for _ in range(q):
        operation = input().strip().split()
        
        if operation[0] == "I":  # 插入
            treap.insert(int(operation[1]))
        elif operation[0] == "D":  # 删除
            treap.remove(int(operation[1]))
        elif operation[0] == "K":  # 查询第k小
            k = int(operation[1])
            result = treap.index_by_rank(k)
            if result is None:
                print("invalid")
            else:
                print(result)
        elif operation[0] == "C":  # 查询排名
            print(treap.small(treap.head, int(operation[1])))


if __name__ == "__main__":
    main()