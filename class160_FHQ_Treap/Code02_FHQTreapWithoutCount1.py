# FHQ-Treap，不用词频压缩，FHQ-Treap最常规的实现，python版
# 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
# 1，增加x，重复加入算多个词频
# 2，删除x，如果有多个，只删掉一个
# 3，查询x的排名，x的排名为，比x小的数的个数+1
# 4，查询数据中排名为x的数
# 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
# 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
# 所有操作的次数 <= 10^5
# -10^7 <= x <= +10^7
# 测试链接 : https://www.luogu.com.cn/problem/P3369
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import sys
import random
from io import StringIO

class FHQTreapWithoutCount:
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
        self.key = [0] * self.MAXN      # 节点的key值
        self.left = [0] * self.MAXN     # 左孩子
        self.right = [0] * self.MAXN    # 右孩子
        self.size = [0] * self.MAXN     # 节点总数
        self.priority = [0.0] * self.MAXN  # 节点优先级
        
    def up(self, i):
        """
        更新节点信息
        
        Args:
            i: 节点编号
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
    
    def split(self, l, r, i, num):
        """
        分裂操作，将树i按照数值num分裂为两棵树
        
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
    
    def add(self, num):
        """
        添加数值
        
        Args:
            num: 添加的数值
        """
        self.split(0, 0, self.head, num)
        self.cnt += 1
        self.key[self.cnt] = num
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def remove(self, num):
        """
        删除数值
        
        Args:
            num: 删除的数值
        """
        self.split(0, 0, self.head, num)
        lm = self.right[0]
        r = self.left[0]
        self.split(0, 0, lm, num - 1)
        l = self.right[0]
        m = self.left[0]
        self.head = self.merge(self.merge(l, self.merge(self.left[m], self.right[m])), r)
    
    def rank(self, num):
        """
        查询数值num的排名
        
        Args:
            num: 查询的数值
            
        Returns:
            数值num的排名
        """
        self.split(0, 0, self.head, num - 1)
        ans = self.size[self.right[0]] + 1
        self.head = self.merge(self.right[0], self.left[0])
        return ans
    
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
        elif self.size[self.left[i]] + 1 < x:
            return self.index(self.right[i], x - self.size[self.left[i]] - 1)
        else:
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
    input_text = """10
1 7
1 2
1 2
1 5
1 1
1 9
3 2
4 3
5 5
6 5"""
    
    sys.stdin = StringIO(input_text)
    
    treap = FHQTreapWithoutCount()
    
    n = int(input())
    for _ in range(n):
        op, x = map(int, input().split())
        if op == 1:
            treap.add(x)
        elif op == 2:
            treap.remove(x)
        elif op == 3:
            print(treap.rank(x))
        elif op == 4:
            print(treap.index_by_rank(x))
        elif op == 5:
            print(treap.pre_by_value(x))
        else:
            print(treap.post_by_value(x))


if __name__ == "__main__":
    main()