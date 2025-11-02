# P5621 [DBOI2019]德丽莎世界第一可爱
# 平台: 洛谷
# 难度: 省选/NOI-
# 标签: CDQ分治, 四维偏序
# 链接: https://www.luogu.com.cn/problem/P5621
# 
# 题目描述:
# 给定n个四元组(a_i, b_i, c_i, d_i)，对于每个i，计算满足以下条件的j的个数：
# a_j ≤ a_i 且 b_j ≤ b_i 且 c_j ≤ c_i 且 d_j ≤ d_i 且 j ≠ i
# 
# 解题思路:
# 使用CDQ分治套CDQ分治解决四维偏序问题。
# 1. 第一维：按a排序
# 2. 第二维：使用外层CDQ分治处理
# 3. 第三维和第四维：使用内层CDQ分治处理
# 
# 具体实现：
# 1. 首先按第一维a排序
# 2. 外层CDQ分治处理第二维b
# 3. 在外层CDQ分治的合并过程中，对第三维c进行排序
# 4. 内层CDQ分治处理第四维d
# 
# 时间复杂度：O(n log^3 n)
# 空间复杂度：O(n)

class Point:
    def __init__(self, a, b, c, d, id):
        self.a = a
        self.b = b
        self.c = c
        self.d = d
        self.id = id
        self.ans = 0

class Solution:
    def __init__(self):
        self.bit = []  # 树状数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, x, v, n):
        i = x
        while i <= n:
            self.bit[i] += v
            i += self.lowbit(i)
    
    def query(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def solveDelisha(self, a, b, c, d):
        n = len(a)
        if n == 0:
            return []
        
        # 创建点数组
        points = []
        for i in range(n):
            points.append(Point(a[i], b[i], c[i], d[i], i))
        
        # 按第一维a排序
        points.sort(key=lambda p: (p.a, p.b, p.c, p.d))
        
        self.bit = [0] * (n + 1)  # 树状数组
        
        # 执行CDQ分治套CDQ分治
        self.cdq2d(points, 0, n - 1)
        
        # 构造结果
        result = [0] * n
        for i in range(n):
            result[points[i].id] = points[i].ans
        return result
    
    # 外层CDQ分治处理第二维b
    def cdq2d(self, points, l, r):
        if l >= r:
            return
        
        mid = (l + r) >> 1
        self.cdq2d(points, l, mid)
        self.cdq2d(points, mid + 1, r)
        
        # 合并过程，计算左半部分对右半部分的贡献
        # 按第三维c排序
        tmp = [None] * (r - l + 1)
        i, j, k = l, mid + 1, 0
        
        while i <= mid and j <= r:
            if points[i].c <= points[j].c:
                # 左半部分的元素c值小于等于右半部分，处理插入操作
                self.add(points[i].d, 1, len(points))  # 插入元素
                tmp[k] = points[i]
                i += 1
                k += 1
            else:
                # 右半部分的元素c值更大，处理查询操作
                # 查询d <= points[j].d的元素个数
                points[j].ans += self.query(points[j].d)
                tmp[k] = points[j]
                j += 1
                k += 1
        
        # 处理剩余元素
        while i <= mid:
            self.add(points[i].d, 1, len(points))
            tmp[k] = points[i]
            i += 1
            k += 1
            
        while j <= r:
            points[j].ans += self.query(points[j].d)
            tmp[k] = points[j]
            j += 1
            k += 1
        
        # 清理树状数组
        for t in range(l, mid + 1):
            self.add(points[t].d, -1, len(points))
        
        # 将临时数组内容复制回原数组
        for t in range(k):
            points[l + t] = tmp[t]

def main():
    solution = Solution()
    
    # 测试用例
    a1 = [1, 2, 3]
    b1 = [1, 2, 3]
    c1 = [1, 2, 3]
    d1 = [1, 2, 3]
    result1 = solution.solveDelisha(a1, b1, c1, d1)
    
    print("输入: a = [1,2,3], b = [1,2,3], c = [1,2,3], d = [1,2,3]")
    print("输出:", result1)

if __name__ == "__main__":
    main()