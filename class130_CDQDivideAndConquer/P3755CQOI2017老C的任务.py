# P3755 [CQOI2017]老C的任务
# 平台: 洛谷
# 难度: 提高+/省选-
# 标签: CDQ分治, 二维数点
# 链接: https://www.luogu.com.cn/problem/P3755

# 题目描述:
# 老 C 是个程序员。
# 最近老 C 从老板那里接到了一个任务——给城市中的手机基站写个管理系统。
# 由于一个基站的面积相对于整个城市面积来说非常的小，因此每个的基站都可以看作坐标系中的一个点，
# 其位置可以用坐标 (x,y) 来表示。此外，每个基站还有很多属性，例如高度、功率等。
# 运营商经常会划定一个区域，并查询区域中所有基站的信息。
# 现在你需要实现的功能就是，对于一个给定的矩形区域，回答该区域中（包括区域边界上的）所有基站的功率总和。

# 解题思路:
# 这是一个二维数点问题，可以使用CDQ分治来解决。
# 将基站的插入操作和查询操作都看作事件，然后使用CDQ分治处理。
# 1. 将每个基站看作一个插入事件
# 2. 将每次查询拆分成四个前缀和查询
# 3. 按照x坐标排序
# 4. 使用CDQ分治处理，在合并过程中使用树状数组维护y坐标维度上的前缀和

class Solution:
    def __init__(self):
        self.MAXN = 200005
        self.tree = [0] * self.MAXN  # 树状数组
        self.ans = [0] * self.MAXN   # 答案数组
        self.events = []
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, pos, val):
        while pos < self.MAXN:
            self.tree[pos] += val
            pos += self.lowbit(pos)
    
    def query(self, pos):
        res = 0
        while pos > 0:
            res += self.tree[pos]
            pos -= self.lowbit(pos)
        return res
    
    # 添加基站插入事件
    def addBaseStation(self, x, y, power):
        self.events.append({'type': 1, 'x': x, 'y': y, 'power': power, 'id': 0})
    
    # 添加查询事件，使用容斥原理将矩形查询转换为前缀和查询
    def addQuery(self, x, y, coeff, id):
        self.events.append({'type': 2, 'x': x, 'y': y, 'power': coeff, 'id': id})
    
    # CDQ分治
    def cdq(self, l, r):
        if l >= r:
            return
        mid = (l + r) >> 1
        self.cdq(l, mid)
        self.cdq(mid + 1, r)
        
        # 处理左半部分对右半部分的贡献
        # 按y坐标排序
        left = [i for i in range(l, mid + 1)]
        right = [i for i in range(mid + 1, r + 1)]
        left.sort(key=lambda i: self.events[i]['y'])
        right.sort(key=lambda i: self.events[i]['y'])
        
        j = 0
        for i in range(len(right)):
            # 处理插入事件
            while j < len(left) and self.events[left[j]]['y'] <= self.events[right[i]]['y']:
                if self.events[left[j]]['type'] == 1:
                    self.add(self.events[left[j]]['x'], self.events[left[j]]['power'])
                j += 1
            # 处理查询事件
            if self.events[right[i]]['type'] == 2:
                self.ans[self.events[right[i]]['id']] += self.events[right[i]]['power'] * self.query(self.events[right[i]]['x'])
        
        # 清空树状数组
        for i in range(j):
            if self.events[left[i]]['type'] == 1:
                self.add(self.events[left[i]]['x'], -self.events[left[i]]['power'])
    
    def solve(self):
        # 读取输入
        n, m = map(int, input().split())
        
        # 读取基站信息
        for i in range(n):
            x, y, power = map(int, input().split())
            self.addBaseStation(x, y, power)
        
        # 读取查询信息
        for i in range(1, m + 1):
            x1, y1, x2, y2 = map(int, input().split())
            
            # 使用容斥原理将矩形区域查询转换为四个前缀和查询
            self.addQuery(x2, y2, 1, i)      # 右上角区域加
            self.addQuery(x1-1, y1-1, 1, i)  # 左下角区域加
            self.addQuery(x1-1, y2, -1, i)   # 左上角区域减
            self.addQuery(x2, y1-1, -1, i)   # 右下角区域减
        
        # 按照x坐标排序
        self.events.sort(key=lambda e: (e['x'], e['type']))
        
        # CDQ分治求解
        self.cdq(0, len(self.events) - 1)
        
        # 输出答案
        for i in range(1, m + 1):
            print(self.ans[i])

# 主函数
if __name__ == "__main__":
    solution = Solution()
    solution.solve()