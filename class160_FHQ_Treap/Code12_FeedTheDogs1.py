# FHQ-Treap实现Feed the dogs
# POJ 2761 Feed the dogs
# 实现查询区间第k小元素
# 测试链接 : http://poj.org/problem?id=2761

import sys
import random
from io import StringIO

class FeedTheDogsFHQTreap:
    def __init__(self, max_n=100001):
        """
        初始化FHQ Treap结构
        
        Args:
            max_n: 最大节点数
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号
        self.cnt = 0   # 空间使用计数
        
        # 节点信息数组
        self.key = [0] * self.MAXN      # 节点的key值（元素值）
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
            排名为x的数值
        """
        return self.index(self.head, x)


def main():
    """
    主函数，处理输入输出
    """
    # 重定向输入输出用于测试
    input_text = """5
1 2 3 4 5
2
1 3 2
2 4 3"""
    
    sys.stdin = StringIO(input_text)
    
    treap = FeedTheDogsFHQTreap()
    
    n = int(input())  # 序列长度
    
    # 读取序列
    arr = [0] + list(map(int, input().split()))  # 0索引不使用，从1开始
    
    m = int(input())  # 查询次数
    
    # 处理查询
    for _ in range(m):
        l, r, k = map(int, input().split())  # 区间左端点、右端点、查询第k小
        
        # 重新初始化FHQ Treap
        treap.init()
        
        # 将区间[l, r]的元素插入到FHQ Treap中
        for j in range(l, r + 1):
            treap.insert(arr[j])
        
        # 查询第k小
        print(treap.index_by_rank(k))


if __name__ == "__main__":
    main()