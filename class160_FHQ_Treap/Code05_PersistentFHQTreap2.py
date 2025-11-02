# 可持久化平衡树，FHQ-Treap实现，不用词频压缩，Python版
# 认为一开始是0版本的树，为空树，实现如下操作，操作一共发生n次
# v 1 x : 基于v版本的树，增加一个x，生成新版本的树
# v 2 x : 基于v版本的树，删除一个x，生成新版本的树
# v 3 x : 基于v版本的树，查询x的排名，生成新版本的树状况=v版本状况
# v 4 x : 基于v版本的树，查询数据中排名为x的数，生成新版本的树状况=v版本状况
# v 5 x : 基于v版本的树，查询x的前驱，生成新版本的树状况=v版本状况
# v 6 x : 基于v版本的树，查询x的后继，生成新版本的树状况=v版本状况
# 不管什么操作，都基于某个v版本，操作完成后得到新版本的树，但v版本不会变化
# 如果x的前驱不存在，返回-2^31 + 1，如果x的后继不存在，返回+2^31 - 1
# 1 <= n <= 5 * 10^5
# -10^9 <= x <= +10^9
# 测试链接 : https://www.luogu.com.cn/problem/P3835
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import random
import sys

class PersistentFHQTreap2:
    def __init__(self, maxn=500001, maxm=500001*50):
        self.MAXN = maxn
        self.MAXM = maxm
        self.cnt = 0
        self.head = [0] * (self.MAXN + 1)
        self.key = [0] * (self.MAXM + 1)
        self.ls = [0] * (self.MAXM + 1)
        self.rs = [0] * (self.MAXM + 1)
        self.siz = [0] * (self.MAXM + 1)
        self.priority = [0.0] * (self.MAXM + 1)
        
    def copy_node(self, i):
        if i == 0:
            return 0
        self.cnt += 1
        self.key[self.cnt] = self.key[i]
        self.ls[self.cnt] = self.ls[i]
        self.rs[self.cnt] = self.rs[i]
        self.siz[self.cnt] = self.siz[i]
        self.priority[self.cnt] = self.priority[i]
        return self.cnt
    
    def update_size(self, i):
        if i == 0:
            return
        self.siz[i] = self.siz[self.ls[i]] + self.siz[self.rs[i]] + 1
    
    def split(self, i, num):
        if i == 0:
            return 0, 0
        
        new_i = self.copy_node(i)
        
        if self.key[new_i] <= num:
            l = new_i
            r1, r2 = self.split(self.rs[new_i], num)
            self.rs[new_i] = r1
            self.update_size(new_i)
            return new_i, r2
        else:
            r = new_i
            l1, l2 = self.split(self.ls[new_i], num)
            self.ls[new_i] = l2
            self.update_size(new_i)
            return l1, new_i
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        
        if self.priority[l] >= self.priority[r]:
            new_l = self.copy_node(l)
            self.rs[new_l] = self.merge(self.rs[new_l], r)
            self.update_size(new_l)
            return new_l
        else:
            new_r = self.copy_node(r)
            self.ls[new_r] = self.merge(l, self.ls[new_r])
            self.update_size(new_r)
            return new_r
    
    def add(self, v, i, num):
        l, r = self.split(i, num)
        
        self.cnt += 1
        self.key[self.cnt] = num
        self.siz[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        
        self.head[v] = self.merge(self.merge(l, self.cnt), r)
    
    def remove(self, v, i, num):
        l, r = self.split(i, num)
        lm, r = self.split(r, num - 1)
        
        if lm != 0:
            lm = self.merge(self.ls[lm], self.rs[lm])
        
        self.head[v] = self.merge(self.merge(l, lm), r)
    
    def small(self, i, num):
        if i == 0:
            return 0
        if self.key[i] >= num:
            return self.small(self.ls[i], num)
        else:
            return self.siz[self.ls[i]] + 1 + self.small(self.rs[i], num)
    
    def index(self, i, x):
        if self.siz[self.ls[i]] >= x:
            return self.index(self.ls[i], x)
        elif self.siz[self.ls[i]] + 1 < x:
            return self.index(self.rs[i], x - self.siz[self.ls[i]] - 1)
        else:
            return self.key[i]
    
    def pre(self, i, num):
        if i == 0:
            return -2147483647  # -2^31 + 1
        if self.key[i] >= num:
            return self.pre(self.ls[i], num)
        else:
            return max(self.key[i], self.pre(self.rs[i], num))
    
    def post(self, i, num):
        if i == 0:
            return 2147483647  # 2^31 - 1
        if self.key[i] <= num:
            return self.post(self.rs[i], num)
        else:
            return min(self.key[i], self.post(self.ls[i], num))

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    ptr = 1
    
    treap = PersistentFHQTreap2()
    treap.head[0] = 0  # 初始版本为空树
    
    for i in range(1, n + 1):
        v = int(data[ptr]); ptr += 1
        op = int(data[ptr]); ptr += 1
        x = int(data[ptr]); ptr += 1
        
        if op == 1:  # 插入
            treap.add(i, treap.head[v], x)
        elif op == 2:  # 删除
            treap.remove(i, treap.head[v], x)
        else:  # 查询操作
            treap.head[i] = treap.head[v]
            if op == 3:  # 查询排名
                print(treap.small(treap.head[v], x) + 1)
            elif op == 4:  # 查询第k小
                print(treap.index(treap.head[v], x))
            elif op == 5:  # 查询前驱
                print(treap.pre(treap.head[v], x))
            elif op == 6:  # 查询后继
                print(treap.post(treap.head[v], x))

if __name__ == "__main__":
    main()