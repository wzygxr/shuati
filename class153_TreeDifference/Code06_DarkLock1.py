"""
暗的连锁 (LOJ 10131)

题目来源：LibreOJ
题目链接：https://loj.ac/problem/10131

题目描述：
给定一棵包含N个节点的树（N-1条边），以及M条额外的边（非树边）
每条非树边连接两个节点，与树边一起构成一个连通图
求有多少种方案，通过切断一条树边和一条非树边，使得图变得不连通

算法原理：树上边差分
树上边差分与点差分类似，但针对边进行操作。
对于每条非树边(u,v)，它在原树上会形成一个环，环上的所有树边都被这条非树边覆盖。
通过边差分，我们可以：
1. num[u]++
2. num[v]++
3. num[lca(u,v)] -= 2
最后通过一次DFS回溯累加子节点的差分标记，得到每条边被覆盖的次数。

时间复杂度分析：
- 预处理LCA：O(N log N)
- 差分标记：O(M log N)，其中M是非树边数量，每次需要计算LCA
- DFS回溯统计：O(N)
总时间复杂度：O((N + M) log N)

空间复杂度分析：
- 树的存储：O(N)
- LCA倍增数组：O(N log N)
- 差分数组：O(N)
总空间复杂度：O(N log N)

工程化考量：
1. 使用链式前向星存储树结构，提高空间效率和遍历速度
2. 预处理log2值，优化倍增数组的大小
3. 在Python中，递归DFS对于大规模数据可能存在栈溢出问题
4. 对于节点数量较大的情况，使用全局变量可以减少函数参数传递的开销

最优解分析：
树上边差分是解决此类问题的最优解，通过O(1)的操作标记每条非树边的影响范围，
避免了暴力遍历每条环上的树边，时间复杂度比暴力方法的O(M*N)有极大提升。
"""

import sys
import math

class DarkChainSolver:
    """
    暗的连锁问题求解类
    
    该类使用树上边差分算法来解决暗的连锁问题，计算切断一条树边和一条非树边使图不连通的方案数。
    """
    
    def __init__(self, max_nodes=100001):
        """
        初始化求解器
        
        Args:
            max_nodes: 最大节点数量，默认为100001
        """
        self.MAXN = max_nodes
        self.LIMIT = 17  # 足够处理3e5节点的倍增层数
        
        # 链式前向星相关变量
        self.head = [0] * self.MAXN
        self.next_edge = [0] * (self.MAXN << 1)  # 乘以2处理无向图
        self.to = [0] * (self.MAXN << 1)
        self.cnt = 1  # 边的计数器，从1开始避免0作为有效索引
        
        # 差分数组，num[i]表示节点i到其父节点的边被多少条非树边覆盖
        self.num = [0] * self.MAXN
        
        # LCA相关变量
        self.deep = [0] * self.MAXN  # 深度数组
        self.stjump = [[0] * self.LIMIT for _ in range(self.MAXN)]  # 倍增跳跃数组
        self.power = 0  # 最大幂次
        
        # 结果变量
        self.ans = 0
        self.n = 0  # 节点数
        self.m = 0  # 非树边数
    
    def add_edge(self, u, v):
        """
        向链式前向星结构中添加一条无向边
        
        Args:
            u: 边的一个端点
            v: 边的另一个端点
        """
        # 添加u到v的边
        self.next_edge[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.head[u] = self.cnt
        self.cnt += 1
        
        # 添加v到u的边（无向图）
        self.next_edge[self.cnt] = self.head[v]
        self.to[self.cnt] = u
        self.head[v] = self.cnt
        self.cnt += 1
    
    def dfs1(self, u, f):
        """
        第一次DFS，预处理每个节点的深度和倍增跳跃数组
        
        Args:
            u: 当前处理的节点
            f: 当前节点的父节点
        """
        # 设置当前节点的深度
        self.deep[u] = self.deep[f] + 1
        # 设置当前节点的直接父节点（2^0级祖先）
        self.stjump[u][0] = f
        
        # 预处理倍增数组
        # 利用动态规划思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
        p = 1
        while p <= self.power:
            self.stjump[u][p] = self.stjump[self.stjump[u][p-1]][p-1]
            p += 1
        
        # 递归处理所有子节点
        e = self.head[u]
        while e != 0:
            if self.to[e] != f:
                self.dfs1(self.to[e], u)
            e = self.next_edge[e]
    
    def lca(self, a, b):
        """
        使用倍增法计算两个节点的最近公共祖先
        
        Args:
            a: 第一个节点
            b: 第二个节点
            
        Returns:
            a和b的最近公共祖先
        """
        # 确保a的深度不小于b
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        
        # 将a向上跳到与b同一深度
        p = self.power
        while p >= 0:
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                a = self.stjump[a][p]
            p -= 1
        
        # 如果此时a==b，则找到了LCA
        if a == b:
            return a
        
        # 继续向上跳，直到找到LCA
        p = self.power
        while p >= 0:
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
            p -= 1
        
        # 返回它们的父节点
        return self.stjump[a][0]
    
    def dfs2(self, u, f):
        """
        第二次DFS，计算每条边被覆盖的次数，并统计满足条件的方案数
        
        Args:
            u: 当前处理的节点
            f: 当前节点的父节点
        
        算法逻辑：
        1. 先递归处理所有子节点
        2. 统计每个子节点到父节点这条边的覆盖次数
        3. 根据覆盖次数计算切断这条树边的可行方案数
        4. 累加子节点的覆盖次数到当前节点
        """
        # 递归处理所有子节点
        e = self.head[u]
        while e != 0:
            v = self.to[e]
            if v != f:
                self.dfs2(v, u)
            e = self.next_edge[e]
        
        # 统计每条边的覆盖次数和方案数
        e = self.head[u]
        while e != 0:
            v = self.to[e]
            if v != f:
                # 获取边(u,v)的覆盖次数，存储在v的num数组中
                coverage = self.num[v]
                
                """
                方案数统计逻辑：
                - 覆盖次数为0：这条树边不在任何非树边形成的环中
                  切断它后，无论切断哪条非树边，图都会不连通
                  共有m种方案（m是非树边的数量）
                
                - 覆盖次数为1：这条树边恰好只在一个非树边形成的环中
                  只有切断对应的那条非树边，图才会不连通
                  共有1种方案
                
                - 覆盖次数>=2：这条树边在多个非树边形成的环中
                  切断它后，需要切断所有对应的非树边才能使图不连通
                  但题目要求只切断一条非树边，因此没有可行方案
                  共有0种方案
                """
                if coverage == 0:
                    self.ans += self.m  # 可以与任意一条非树边配对
                elif coverage == 1:
                    self.ans += 1       # 只能与对应的那条非树边配对
                # coverage >= 2 时不需要增加ans
                
                # 将子节点的覆盖次数累加到父节点，完成差分标记的传播
                self.num[u] += self.num[v]
            e = self.next_edge[e]
    
    def process_non_tree_edges(self):
        """
        处理所有非树边，执行树上边差分操作
        
        对于每条非树边(u,v)，它在原树上会形成一个环，环上的所有树边都被这条非树边覆盖。
        使用边差分技巧，我们只需要修改三个点：
        1. num[u]++ - 在u点增加覆盖标记
        2. num[v]++ - 在v点增加覆盖标记
        3. num[lca(u,v)] -= 2 - 在LCA处抵消多余的标记
        """
        for _ in range(self.m):
            u, v = map(int, sys.stdin.readline().split())
            lca_node = self.lca(u, v)
            
            # 树上边差分核心操作
            self.num[u] += 1
            self.num[v] += 1
            self.num[lca_node] -= 2
    
    def solve(self):
        """
        解决暗的连锁问题
        
        Returns:
            满足条件的方案数
        """
        # 读取输入
        input_line = sys.stdin.readline().strip()
        while not input_line:
            input_line = sys.stdin.readline().strip()
        self.n, self.m = map(int, input_line.split())
        
        # 计算最大幂次
        self.power = math.floor(math.log2(self.n)) + 1
        
        # 构建树结构
        for _ in range(1, self.n):
            input_line = sys.stdin.readline().strip()
            while not input_line:
                input_line = sys.stdin.readline().strip()
            u, v = map(int, input_line.split())
            self.add_edge(u, v)
        
        # 预处理LCA所需的数据
        self.dfs1(1, 0)
        
        # 处理所有非树边
        self.process_non_tree_edges()
        
        # 计算最终答案
        self.dfs2(1, 0)
        
        return self.ans

def main():
    """
    主函数
    
    输入格式：
    第一行：两个整数n和m，分别表示节点数和非树边数
    接下来n-1行：每行两个整数，表示树边
    接下来m行：每行两个整数，表示非树边
    
    输出格式：
    一个整数，表示满足条件的方案数
    """
    # 创建求解器实例
    solver = DarkChainSolver()
    
    # 求解并输出结果
    result = solver.solve()
    print(result)

if __name__ == "__main__":
    main()