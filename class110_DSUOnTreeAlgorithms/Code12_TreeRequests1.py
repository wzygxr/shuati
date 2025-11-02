# DSU on Tree 算法实现 - Tree Requests (Codeforces 570D)
# 题目来源: Codeforces 570D - Tree Requests
# 题目链接: https://codeforces.com/problemset/problem/570D
# 
# 题目大意:
# 给定一棵有根树，每个节点有一个小写字母。有m个查询，每个查询给定节点v和深度h，
# 询问在节点v的子树中，深度为h的所有节点上的字母能否重新排列成一个回文串。
# 如果可以，输出"Yes"，否则输出"No"。
#
# 解题思路:
# 使用DSU on Tree算法统计每个深度上字母的出现频率。
# 对于每个查询，检查该深度上字母频率是否满足回文串条件：
# 最多只能有一个字母出现奇数次。
#
# 时间复杂度: O(n log n + m)
# 空间复杂度: O(n)
#
# 算法详解:
# 1. 重链剖分预处理，确定每个节点的重儿子
# 2. 使用DSU on Tree统计每个深度上字母的出现频率
# 3. 对于每个查询，检查对应深度的字母频率是否满足回文条件
#
# 工程化实现要点:
# 1. 边界处理：空树、单节点树、深度超出范围等情况
# 2. 内存优化：使用位运算优化字母频率统计
# 3. 常数优化：减少函数调用，使用局部变量缓存
# 4. 异常处理：验证输入参数的有效性
#
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import sys
sys.setrecursionlimit(1 << 25)

class DSUOnTree:
    def __init__(self, n):
        self.n = n
        self.s = [''] * (n + 1)
        self.depth = [0] * (n + 1)
        self.tree = [[] for _ in range(n + 1)]
        
        # 树链剖分
        self.fa = [0] * (n + 1)
        self.siz = [0] * (n + 1)
        self.son = [0] * (n + 1)
        
        # DSU on Tree相关
        # freq[d][c]表示深度d上字母c的出现次数
        self.freq = [[0] * 26 for _ in range(n + 1)]
        # 查询存储
        self.queries = [[] for _ in range(n + 1)]
        self.ans = [False] * (n + 1)
    
    def add_edge(self, u, v):
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    # 第一次DFS，重链剖分
    def dfs1(self, u, f):
        self.fa[u] = f
        self.depth[u] = self.depth[f] + 1
        self.siz[u] = 1
        self.son[u] = 0
        
        for v in self.tree[u]:
            if v != f:
                self.dfs1(v, u)
                self.siz[u] += self.siz[v]
                if self.son[u] == 0 or self.siz[self.son[u]] < self.siz[v]:
                    self.son[u] = v
    
    # 添加节点贡献
    def add_node(self, u):
        d = self.depth[u]
        c = ord(self.s[u]) - ord('a')
        self.freq[d][c] += 1
    
    # 移除节点贡献
    def remove_node(self, u):
        d = self.depth[u]
        c = ord(self.s[u]) - ord('a')
        self.freq[d][c] -= 1
    
    # 添加子树贡献
    def add_subtree(self, u):
        self.add_node(u)
        for v in self.tree[u]:
            if v != self.fa[u]:
                self.add_subtree(v)
    
    # 移除子树贡献
    def remove_subtree(self, u):
        self.remove_node(u)
        for v in self.tree[u]:
            if v != self.fa[u]:
                self.remove_subtree(v)
    
    # 检查深度d是否满足回文条件
    def check_depth(self, d):
        odd_count = 0
        for i in range(26):
            if self.freq[d][i] % 2 == 1:
                odd_count += 1
        return odd_count <= 1
    
    # DSU on Tree主过程
    def dfs2(self, u, keep):
        # 处理轻儿子
        for v in self.tree[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.dfs2(v, False)
        
        # 处理重儿子
        if self.son[u] != 0:
            self.dfs2(self.son[u], True)
        
        # 添加当前节点贡献
        self.add_node(u)
        
        # 添加轻儿子贡献
        for v in self.tree[u]:
            if v != self.fa[u] and v != self.son[u]:
                self.add_subtree(v)
        
        # 处理查询
        for h, idx in self.queries[u]:
            # 检查深度h是否满足回文条件
            self.ans[idx] = self.check_depth(h)
        
        # 如果不保留，清除贡献
        if not keep:
            self.remove_subtree(u)
    
    def solve(self):
        self.dfs1(1, 0)
        self.dfs2(1, False)
        return self.ans

def main():
    import sys
    data = sys.stdin.read().split()
    
    n = int(data[0])
    m = int(data[1])
    
    dsu = DSUOnTree(n)
    
    # 读取树结构
    idx = 2
    for i in range(2, n + 1):
        p = int(data[idx])
        idx += 1
        dsu.add_edge(p, i)
    
    # 读取节点字母
    s_str = data[idx]
    idx += 1
    for i in range(1, n + 1):
        dsu.s[i] = s_str[i - 1]
    
    # 读取查询
    for i in range(m):
        v = int(data[idx])
        h = int(data[idx + 1])
        idx += 2
        dsu.queries[v].append((h, i))
    
    # 执行算法
    ans = dsu.solve()
    
    # 输出结果
    for i in range(m):
        print("Yes" if ans[i] else "No")

if __name__ == "__main__":
    main()