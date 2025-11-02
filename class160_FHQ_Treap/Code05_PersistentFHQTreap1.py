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

class PersistentFHQTreap:
    def __init__(self, maxn=500001, maxm=500001*50):
        self.MAXN = maxn
        self.MAXM = maxm
        self.cnt = 0
        self.head = [0] * (self.MAXN + 1)
        self.key = [0] * (self.MAXM + 1)
        self.left = [0] * (self.MAXM + 1)
        self.right = [0] * (self.MAXM + 1)
        self.size = [0] * (self.MAXM + 1)
        self.priority = [0.0] * (self.MAXM + 1)
        
    def copy_node(self, i):
        if i == 0:
            return 0
        self.cnt += 1
        self.key[self.cnt] = self.key[i]
        self.left[self.cnt] = self.left[i]
        self.right[self.cnt] = self.right[i]
        self.size[self.cnt] = self.size[i]
        self.priority[self.cnt] = self.priority[i]
        return self.cnt
    
    def update_size(self, i):
        if i == 0:
            return
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + 1
    
    def split(self, i, num):
        if i == 0:
            return 0, 0
        
        new_i = self.copy_node(i)
        
        if self.key[new_i] <= num:
            l = new_i
            r1, r2 = self.split(self.right[new_i], num)
            self.right[new_i] = r1
            self.update_size(new_i)
            return new_i, r2
        else:
            r = new_i
            l1, l2 = self.split(self.left[new_i], num)
            self.left[new_i] = l2
            self.update_size(new_i)
            return l1, new_i
    
    def merge(self, l, r):
        if l == 0 or r == 0:
            return l + r
        
        if self.priority[l] >= self.priority[r]:
            new_l = self.copy_node(l)
            self.right[new_l] = self.merge(self.right[new_l], r)
            self.update_size(new_l)
            return new_l
        else:
            new_r = self.copy_node(r)
            self.left[new_r] = self.merge(l, self.left[new_r])
            self.update_size(new_r)
            return new_r
    
    def insert(self, root, x):
        l, r = self.split(root, x)
        
        self.cnt += 1
        self.key[self.cnt] = x
        self.size[self.cnt] = 1
        self.priority[self.cnt] = random.random()
        
        return self.merge(self.merge(l, self.cnt), r)
    
    def remove(self, root, x):
        l, r = self.split(root, x - 1)
        m, r = self.split(r, x)
        
        if m != 0:
            m = self.merge(self.left[m], self.right[m])
        
        return self.merge(self.merge(l, m), r)
    
    def get_rank(self, root, x):
        l, r = self.split(root, x - 1)
        rank = self.size[l] + 1
        self.merge(l, r)  # 恢复树结构
        return rank
    
    def get_kth(self, root, k):
        i = root
        while i != 0:
            left_size = self.size[self.left[i]]
            if left_size + 1 == k:
                return self.key[i]
            elif left_size >= k:
                i = self.left[i]
            else:
                k -= left_size + 1
                i = self.right[i]
        return 0
    
    def get_predecessor(self, root, x):
        l, r = self.split(root, x - 1)
        
        if l == 0:
            pred = -2147483647  # -2^31 + 1
        else:
            pred = self.get_kth(l, self.size[l])
        
        self.merge(l, r)  # 恢复树结构
        return pred
    
    def get_successor(self, root, x):
        l, r = self.split(root, x)
        
        if r == 0:
            succ = 2147483647  # 2^31 - 1
        else:
            succ = self.get_kth(r, 1)
        
        self.merge(l, r)  # 恢复树结构
        return succ

def main():
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    ptr = 1
    
    treap = PersistentFHQTreap()
    treap.head[0] = 0  # 初始版本为空树
    
    for i in range(1, n + 1):
        v = int(data[ptr]); ptr += 1
        op = int(data[ptr]); ptr += 1
        x = int(data[ptr]); ptr += 1
        
        if op == 1:  # 插入
            treap.head[i] = treap.insert(treap.head[v], x)
        elif op == 2:  # 删除
            treap.head[i] = treap.remove(treap.head[v], x)
        else:  # 查询操作
            treap.head[i] = treap.head[v]
            if op == 3:  # 查询排名
                print(treap.get_rank(treap.head[v], x))
            elif op == 4:  # 查询第k小
                print(treap.get_kth(treap.head[v], x))
            elif op == 5:  # 查询前驱
                print(treap.get_predecessor(treap.head[v], x))
            elif op == 6:  # 查询后继
                print(treap.get_successor(treap.head[v], x))

if __name__ == "__main__":
    main()