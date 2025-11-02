# CF848C Goodbye Souvenir
# 平台: Codeforces
# 难度: 2600
# 标签: CDQ分治, 二维数点
# 链接: https://codeforces.com/problemset/problem/848/C
# 
# 题目描述:
# 给定一个长度为n的序列a，有两种操作：
# 1. 1 x y：将a_x修改为y
# 2. 2 l r：查询区间[l,r]中所有相同元素的最大跨度之和
# 最大跨度定义为：对于值为v的元素，如果它在区间中出现的位置是i1,i2,...,ik，
# 那么它的跨度是ik-i1，所有值的跨度之和就是答案。
# 
# 解题思路:
# 使用CDQ分治解决时间维度的三维偏序问题。
# 1. 第一维：时间（操作顺序）
# 2. 第二维：位置
# 3. 第三维：值
# 
# 我们将每个修改操作和查询操作都看作事件，然后使用CDQ分治来处理。
# 对于每个值，我们维护它之前出现的位置，这样可以将跨度计算转化为二维数点问题。
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class Event:
    def __init__(self, type, time, pos, val, l, r, id):
        self.type = type  # 0表示修改，1表示查询
        self.time = time
        self.pos = pos
        self.val = val
        self.l = l
        self.r = r
        self.id = id

class Solution:
    def __init__(self):
        self.bit = []  # 树状数组
        self.ans = []  # 答案数组
    
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
    
    def solveGoodbyeSouvenir(self, a, operations):
        n = len(a)
        m = len(operations)
        if m == 0:
            return []
        
        # 创建事件数组
        events = []
        time = 0
        
        # 初始数组元素作为修改事件
        for i in range(n):
            events.append(Event(0, time, i, a[i], 0, 0, 0))
            time += 1
        
        # 处理操作
        self.ans = [0] * m
        for i in range(m):
            if operations[i][0] == 1:
                # 修改操作
                x = operations[i][1] - 1  # 转换为0索引
                y = operations[i][2]
                events.append(Event(0, time, x, y, 0, 0, 0))
                time += 1
            else:
                # 查询操作
                l = operations[i][1] - 1  # 转换为0索引
                r = operations[i][2] - 1  # 转换为0索引
                events.append(Event(1, time, 0, 0, l, r, i))
                time += 1
        
        self.bit = [0] * (n + 1)  # 树状数组
        
        # 执行CDQ分治
        self.cdq(events, 0, len(events) - 1)
        
        return self.ans
    
    # CDQ分治主函数
    def cdq(self, events, l, r):
        if l >= r:
            return
        
        mid = (l + r) >> 1
        self.cdq(events, l, mid)
        self.cdq(events, mid + 1, r)
        
        # 合并过程，计算左半部分对右半部分的贡献
        left = []
        right = []
        
        for i in range(l, mid + 1):
            if events[i].type == 0:  # 修改事件
                left.append(events[i])
        
        for i in range(mid + 1, r + 1):
            if events[i].type == 1:  # 查询事件
                right.append(events[i])
        
        # 按位置排序
        left.sort(key=lambda e: e.pos)
        right.sort(key=lambda e: e.l)
        
        # 处理贡献
        j = 0
        for e in right:
            # 将位置小于等于e.l的修改事件加入树状数组
            while j < len(left) and left[j].pos <= e.l:
                self.add(left[j].pos + 1, left[j].val, len(self.bit) - 1)
                j += 1
            
            # 查询位置在[e.l, e.r]范围内的元素和
            self.ans[e.id] += self.query(e.r + 1) - self.query(e.l)
        
        # 清理树状数组
        for i in range(j):
            self.add(left[i].pos + 1, -left[i].val, len(self.bit) - 1)
    
    def main(self):
        # 测试用例
        a1 = [1, 2, 3]
        operations1 = [[2, 1, 3]]
        result1 = self.solveGoodbyeSouvenir(a1, operations1)
        
        print("输入: a = [1,2,3], operations = [[2,1,3]]")
        print("输出:", result1)

if __name__ == "__main__":
    solution = Solution()
    solution.main()