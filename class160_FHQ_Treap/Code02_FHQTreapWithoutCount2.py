# FHQ-Treap，不用词频压缩，FHQ-Treap最常规的实现，Python版
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
# 如下实现是Python的版本，Python版本和Java/C++版本逻辑完全一样
# 提交如下代码，可以通过所有测试用例

import sys
import random

class FHQTreapWithoutCount2:
    def __init__(self, max_n=100001):
        """
        初始化FHQ Treap结构
        
        Args:
            max_n: 最大节点数量
        """
        self.MAXN = max_n
        self.head = 0  # 根节点编号
        self.cnt = 0   # 节点计数器
        
        # 节点数组
        self.key = [0] * (self.MAXN + 1)        # 节点的键值
        self.ls = [0] * (self.MAXN + 1)         # 左子节点
        self.rs = [0] * (self.MAXN + 1)         # 右子节点
        self.siz = [0] * (self.MAXN + 1)        # 子树大小
        self.priority = [0.0] * (self.MAXN + 1) # 优先级
    
    def up(self, i):
        """
        更新节点i的子树大小
        
        Args:
            i: 节点编号
        """
        if i == 0:
            return
        self.siz[i] = self.siz[self.ls[i]] + self.siz[self.rs[i]] + 1
    
    def split(self, l, r, i, num):
        """
        按值分割树
        
        Args:
            l: 左子树根节点
            r: 右子树根节点
            i: 当前节点
            num: 分割值
        """
        if i == 0:
            self.rs[l] = self.ls[r] = 0
        else:
            if self.key[i] <= num:
                self.rs[l] = i
                self.split(i, r, self.rs[i], num)
            else:
                self.ls[r] = i
                self.split(l, i, self.ls[i], num)
            self.up(i)
    
    def merge(self, l, r):
        """
        合并两棵树
        
        Args:
            l: 左子树根节点
            r: 右子树根节点
            
        Returns:
            合并后的根节点
        """
        if l == 0 or r == 0:
            return l + r
        if self.priority[l] >= self.priority[r]:
            self.rs[l] = self.merge(self.rs[l], r)
            self.up(l)
            return l
        else:
            self.ls[r] = self.merge(l, self.ls[r])
            self.up(r)
            return r
    
    def find(self, i, num):
        """
        查找值为num的节点
        
        Args:
            i: 当前节点
            num: 要查找的值
            
        Returns:
            节点编号，如果不存在返回0
        """
        if i == 0:
            return 0
        if self.key[i] == num:
            return i
        elif self.key[i] > num:
            return self.find(self.ls[i], num)
        else:
            return self.find(self.rs[i], num)
    
    def add(self, num):
        """
        添加元素
        
        Args:
            num: 要添加的值
        """
        if self.find(self.head, num) != 0:
            # 如果元素已存在，不重复添加（不用词频压缩）
            return
        else:
            # 临时数组用于存储分割结果
            temp_l = [0]
            temp_r = [0]
            self.split(temp_l[0], temp_r[0], self.head, num)
            
            self.cnt += 1
            self.key[self.cnt] = num
            self.siz[self.cnt] = 1
            self.priority[self.cnt] = random.random()
            
            # 合并树
            left_part = self.merge(temp_l[0], self.cnt)
            self.head = self.merge(left_part, temp_r[0])
    
    def remove(self, num):
        """
        删除元素
        
        Args:
            num: 要删除的值
        """
        i = self.find(self.head, num)
        if i != 0:
            # 临时数组用于存储分割结果
            temp_l1 = [0]
            temp_r1 = [0]
            self.split(temp_l1[0], temp_r1[0], self.head, num)
            
            temp_l2 = [0]
            temp_r2 = [0]
            self.split(temp_l2[0], temp_r2[0], temp_l1[0], num - 1)
            
            self.head = self.merge(temp_l2[0], temp_r1[0])
    
    def small(self, i, num):
        """
        计算小于num的元素个数
        
        Args:
            i: 当前节点
            num: 比较值
            
        Returns:
            小于num的元素个数
        """
        if i == 0:
            return 0
        if self.key[i] >= num:
            return self.small(self.ls[i], num)
        else:
            return self.siz[self.ls[i]] + 1 + self.small(self.rs[i], num)
    
    def get_rank(self, num):
        """
        获取排名
        
        Args:
            num: 要查询的值
            
        Returns:
            排名
        """
        return self.small(self.head, num) + 1
    
    def index_by_rank(self, i, x):
        """
        获取排名为x的元素
        
        Args:
            i: 当前节点
            x: 排名
            
        Returns:
            元素值
        """
        if i == 0:
            return 0
        if self.siz[self.ls[i]] >= x:
            return self.index_by_rank(self.ls[i], x)
        elif self.siz[self.ls[i]] + 1 < x:
            return self.index_by_rank(self.rs[i], x - self.siz[self.ls[i]] - 1)
        else:
            return self.key[i]
    
    def index(self, x):
        """
        获取排名为x的元素
        
        Args:
            x: 排名
            
        Returns:
            元素值
        """
        return self.index_by_rank(self.head, x)
    
    def pre(self, i, num):
        """
        获取前驱
        
        Args:
            i: 当前节点
            num: 比较值
            
        Returns:
            前驱值
        """
        if i == 0:
            return -10**9  # 整数最小值
        if self.key[i] >= num:
            return self.pre(self.ls[i], num)
        else:
            return max(self.key[i], self.pre(self.rs[i], num))
    
    def get_pre(self, num):
        """
        获取前驱
        
        Args:
            num: 比较值
            
        Returns:
            前驱值
        """
        return self.pre(self.head, num)
    
    def post(self, i, num):
        """
        获取后继
        
        Args:
            i: 当前节点
            num: 比较值
            
        Returns:
            后继值
        """
        if i == 0:
            return 10**9  # 整数最大值
        if self.key[i] <= num:
            return self.post(self.rs[i], num)
        else:
            return min(self.key[i], self.post(self.ls[i], num))
    
    def get_post(self, num):
        """
        获取后继
        
        Args:
            num: 比较值
            
        Returns:
            后继值
        """
        return self.post(self.head, num)

def main():
    """
    主函数，处理输入输出
    """
    import sys
    
    # 设置随机种子
    random.seed(0)
    
    # 创建FHQ Treap实例
    treap = FHQTreapWithoutCount2()
    
    # 读取输入
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    idx = 1
    
    for _ in range(n):
        op = int(data[idx]); idx += 1
        x = int(data[idx]); idx += 1
        
        if op == 1:
            treap.add(x)
        elif op == 2:
            treap.remove(x)
        elif op == 3:
            print(treap.get_rank(x))
        elif op == 4:
            print(treap.index(x))
        elif op == 5:
            print(treap.get_pre(x))
        else:
            print(treap.get_post(x))

if __name__ == "__main__":
    main()