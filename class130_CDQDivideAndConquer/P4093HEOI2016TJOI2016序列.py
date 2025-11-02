# P4093 [HEOI2016/TJOI2016]序列
# 平台: 洛谷
# 难度: 省选/NOI-
# 标签: CDQ分治, 三维偏序
# 链接: https://www.luogu.com.cn/problem/P4093

# 题目描述:
# 佳媛姐姐过生日的时候，她的小伙伴从某宝上买了一个有趣的玩具送给他。
# 玩具上有一个数列，数列中某些项的值可能会变化，但同一个时刻最多只有一个值发生变化。
# 现在佳媛姐姐已经研究出了所有变化的可能性，她想请教你，能否选出一个子序列，
# 使得在**任意一种变化和原序列**中，这个子序列都是不降的？请你告诉她这个子序列的最长长度即可。

# 解题思路:
# 这是一个三维偏序问题，可以使用CDQ分治来解决。
# 对于每个元素i，我们定义三个属性：
# 1. 位置i（第一维）
# 2. 最小可能值minVal[i]（第二维）
# 3. 原始值origVal[i]（第三维）
# 
# 对于两个元素i和j，如果i<j且minVal[j] >= origVal[i]，那么j可以从i转移而来。
# 这就转化为了一个三维偏序问题，可以使用CDQ分治+树状数组来解决。

class Solution:
    def __init__(self):
        self.MAXN = 100005
        self.tree = [0] * self.MAXN  # 树状数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, pos, val):
        while pos < self.MAXN:
            self.tree[pos] = max(self.tree[pos], val)
            pos += self.lowbit(pos)
    
    def query(self, pos):
        res = 0
        while pos > 0:
            res = max(res, self.tree[pos])
            pos -= self.lowbit(pos)
        return res
    
    def clear(self, pos):
        while pos < self.MAXN:
            self.tree[pos] = 0
            pos += self.lowbit(pos)
    
    def cdq(self, nodes, l, r):
        if l >= r:
            return
        mid = (l + r) >> 1
        self.cdq(nodes, l, mid)
        
        # 处理左半部分对右半部分的贡献
        # 按minVal排序
        left = [i for i in range(l, mid + 1)]
        right = [i for i in range(mid + 1, r + 1)]
        left.sort(key=lambda x: nodes[x].minVal)
        right.sort(key=lambda x: nodes[x].minVal)
        
        j = 0
        for i in range(len(right)):
            while j < len(left) and nodes[left[j]].minVal <= nodes[right[i]].minVal:
                self.add(nodes[left[j]].origVal, nodes[left[j]].dp)
                j += 1
            nodes[right[i]].dp = max(nodes[right[i]].dp, self.query(nodes[right[i]].maxVal) + 1)
        
        # 清空树状数组
        for i in range(j):
            self.clear(nodes[left[i]].origVal)
        
        self.cdq(nodes, mid + 1, r)
    
    def solve(self):
        # 读取输入
        n, m = map(int, input().split())
        original = list(map(int, input().split()))
        
        # 初始化
        minVal = original[:]
        maxVal = original[:]
        
        # 处理变化
        for _ in range(m):
            x, y = map(int, input().split())
            x -= 1  # 转换为0索引
            minVal[x] = min(minVal[x], y)
            maxVal[x] = max(maxVal[x], y)
        
        # 节点类
        class Node:
            def __init__(self, pos, origVal, minVal, maxVal):
                self.pos = pos
                self.origVal = origVal
                self.minVal = minVal
                self.maxVal = maxVal
                self.dp = 1
        
        # 构造节点
        nodes = []
        for i in range(n):
            nodes.append(Node(i, original[i], minVal[i], maxVal[i]))
        
        # CDQ分治求解
        self.cdq(nodes, 0, n - 1)
        
        # 计算答案
        ans = 0
        for node in nodes:
            ans = max(ans, node.dp)
        
        return ans

# 主函数
if __name__ == "__main__":
    solution = Solution()
    result = solution.solve()
    print(result)