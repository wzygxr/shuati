# P3810 【模板】三维偏序（陌上花开）
# 平台: 洛谷
# 难度: 提高+/省选-
# 标签: CDQ分治, 三维偏序
# 链接: https://www.luogu.com.cn/problem/P3810
# 
# 题目描述:
# 一共有n个对象，属性值范围[1, k]，每个对象有a属性、b属性、c属性
# f(i)表示，aj <= ai 且 bj <= bi 且 cj <= ci 且 j != i 的j的数量
# ans(d)表示，f(i) == d 的i的数量
# 打印所有的ans[d]，d的范围[0, n)
# 
# 示例:
# 输入:
# 10 3
# 3 3 3
# 2 3 3
# 2 3 1
# 3 1 1
# 3 1 2
# 1 3 1
# 1 1 2
# 1 3 3
# 1 1 3
# 1 3 2
# 
# 输出:
# 3
# 1
# 3
# 0
# 0
# 0
# 0
# 0
# 0
# 0
# 
# 解题思路:
# 使用CDQ分治解决三维偏序问题。这是CDQ分治的经典应用。
# 
# 1. 第一维：a属性，通过排序处理
# 2. 第二维：b属性，通过CDQ分治处理
# 3. 第三维：c属性，通过树状数组处理
# 
# 具体步骤：
# 1. 按照a属性排序，相同a的按b排序，相同b的按c排序
# 2. CDQ分治处理b属性
# 3. 在分治的合并过程中，使用双指针处理b属性的大小关系，用树状数组维护c属性的前缀和
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class P3810Solution:
    def __init__(self):
        self.tree = []  # 树状数组
        self.f = []     # 每个对象的答案
        self.ans = []   # 题目要求的ans[d]
        self.a = []     # 对象数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, i, v, k):
        while i <= k:
            self.tree[i] += v
            i += self.lowbit(i)
    
    def query(self, i):
        ret = 0
        while i > 0:
            ret += self.tree[i]
            i -= self.lowbit(i)
        return ret
    
    def merge(self, l, m, r, k):
        # 利用左、右各自b属性有序
        # 不回退的找，当前右组对象包括了几个左组的对象
        p1, p2 = l - 1, m + 1
        while p2 <= r:
            while p1 + 1 <= m and self.a[p1 + 1][2] <= self.a[p2][2]:
                p1 += 1
                self.add(self.a[p1][3], 1, k)
            self.f[self.a[p2][0]] += self.query(self.a[p2][3])
            p2 += 1
        
        # 清空树状数组
        for i in range(l, p1 + 1):
            self.add(self.a[i][3], -1, k)
        
        # 直接根据b属性排序
        self.a[l:r+1] = sorted(self.a[l:r+1], key=lambda x: x[2])
    
    # 大顺序已经按a属性排序，cdq分治里按b属性重新排序
    def cdq(self, l, r, k):
        if l == r:
            return
        mid = (l + r) // 2
        self.cdq(l, mid, k)
        self.cdq(mid + 1, r, k)
        self.merge(l, mid, r, k)
    
    def prepare(self, n):
        # 根据a排序，a一样根据b排序，b一样根据c排序
        # 排序后a、b、c一样的同组内，组前的下标得不到同组后面的统计量
        # 所以把这部分的贡献，提前补偿给组前的下标，然后再跑CDQ分治
        self.a[1:n+1] = sorted(self.a[1:n+1], key=lambda x: (x[1], x[2], x[3]))
        
        l = 1
        while l <= n:
            r = l
            while r + 1 <= n and self.a[l][1] == self.a[r + 1][1] and \
                  self.a[l][2] == self.a[r + 1][2] and \
                  self.a[l][3] == self.a[r + 1][3]:
                r += 1
            
            for i in range(l, r + 1):
                self.f[self.a[i][0]] = r - i
            
            l = r + 1
    
    def solve(self, n, k, objects):
        # 初始化
        self.a = [[0, 0, 0, 0] for _ in range(n + 1)]
        self.tree = [0] * (k + 1)
        self.f = [0] * (n + 1)
        self.ans = [0] * n
        
        # 读取数据
        for i in range(1, n + 1):
            self.a[i] = [i] + objects[i-1]
        
        self.prepare(n)
        self.cdq(1, n, k)
        
        for i in range(1, n + 1):
            self.ans[self.f[i]] += 1
        
        return self.ans

def main():
    # 读取输入
    n, k = map(int, input().split())
    objects = []
    for _ in range(n):
        a, b, c = map(int, input().split())
        objects.append([a, b, c])
    
    # 求解
    solution = P3810Solution()
    result = solution.solve(n, k, objects)
    
    # 输出结果
    for i in range(n):
        print(result[i])

if __name__ == "__main__":
    main()