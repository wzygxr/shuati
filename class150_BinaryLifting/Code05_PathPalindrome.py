# 检查树上两节点间的路径是否是回文问题
# 问题描述：
# 一颗树上有n个节点，编号1~n
# 给定长度为n的数组parent, parent[i]表示节点i的父节点编号
# 给定长度为n的数组s, s[i]表示节点i上是什么字符
# 从节点a到节点b经过节点最少的路，叫做a和b的路径
# 一共有m条查询，每条查询(a,b)，a和b的路径字符串是否是回文
# 是回文打印"YES"，不是回文打印"NO"
# 1 <= n <= 10^5
# 1 <= m <= 10^5
# parent[1] = 0，即整棵树的头节点一定是1号节点
# 每个节点上的字符一定是小写字母a~z
# 测试链接 : https://ac.nowcoder.com/acm/contest/78807/G
# 
# 解题思路：
# 使用树上倍增算法预处理每个节点的祖先信息和字符串哈希值
# 对于每次查询，找到两点的LCA，然后分别计算从a到LCA和从b到LCA的路径字符串哈希值
# 比较两个哈希值是否相等来判断路径字符串是否为回文

import sys
import math
from collections import defaultdict

class PathPalindrome:
    def __init__(self, n):
        """
        初始化树上路径回文检查求解器
        :param n: 节点数量
        """
        self.n = n
        self.LIMIT = 17  # 最大跳跃级别
        
        # 初始化数据结构
        self.s = [0] * (n + 1)  # 存储每个节点的字符（转换为数字）
        self.adj = defaultdict(list)  # 邻接表
        self.deep = [0] * (n + 1)  # deep[i] : i节点在第几层
        self.jump = [[0] * self.LIMIT for _ in range(n + 1)]  # jump[i][j] : i节点往上跳2的j次方步，到达什么节点
        self.K = 499  # 哈希参数K
        self.kpow = [0] * (n + 1)  # kpow[i] = k的i次方，用于字符串哈希计算
        self.stup = [[0] * self.LIMIT for _ in range(n + 1)]  # stup[i][j] : i节点往上跳2的j次方步的路径字符串哈希值（向上方向）
        self.stdown = [[0] * self.LIMIT for _ in range(n + 1)]  # stdown[i][j] : i节点往上跳2的j次方步的路径字符串哈希值（向下方向）
        
    def build(self):
        """
        初始化数据结构
        """
        self.power = self.log2(self.n)
        # 预计算K的幂次
        self.kpow[0] = 1
        for i in range(1, self.n + 1):
            self.kpow[i] = self.kpow[i - 1] * self.K
            
    def log2(self, n):
        """
        计算log2(n)的值
        :param n: 输入值
        :return: log2(n)的整数部分
        """
        ans = 0
        while (1 << ans) <= (n >> 1):
            ans += 1
        return ans
        
    def add_edge(self, u, v):
        """
        添加一条无向边到邻接表中
        :param u: 起点
        :param v: 终点
        """
        self.adj[u].append(v)
        self.adj[v].append(u)
        
    def dfs(self, u, f):
        """
        DFS遍历构建倍增表和字符串哈希信息
        算法思路：
        1. 遍历树的每个节点
        2. 构建深度、跳跃表
        3. 构建向上和向下路径的字符串哈希值
        
        时间复杂度：O(n log n)
        空间复杂度：O(n log n)
        
        :param u: 当前节点
        :param f: 父节点
        """
        # 记录深度和直接父节点
        self.deep[u] = self.deep[f] + 1
        self.jump[u][0] = f
        # 记录到父节点的字符（用于哈希计算）
        self.stup[u][0] = self.stdown[u][0] = self.s[f]
        # 构建倍增表和哈希值表
        for p in range(1, self.power + 1):
            v = self.jump[u][p - 1]
            # 跳2^p步 = 跳2^(p-1)步后再跳2^(p-1)步
            self.jump[u][p] = self.jump[v][p - 1]
            # 计算向上路径的哈希值
            # 向上路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
            self.stup[u][p] = self.stup[u][p - 1] * self.kpow[1 << (p - 1)] + self.stup[v][p - 1]
            # 计算向下路径的哈希值
            # 向下路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
            self.stdown[u][p] = self.stdown[v][p - 1] * self.kpow[1 << (p - 1)] + self.stdown[u][p - 1]
        # 递归处理子节点
        for v in self.adj[u]:
            if v != f:
                self.dfs(v, u)
                
    def is_palindrome(self, a, b):
        """
        判断路径是否为回文
        算法思路：
        1. 找到a和b的LCA
        2. 分别计算从a到LCA和从b到LCA的路径字符串哈希值
        3. 比较两个哈希值是否相等
        
        时间复杂度：O(log n)
        空间复杂度：O(1)
        
        :param a: 起点
        :param b: 终点
        :return: 如果路径字符串是回文返回True，否则返回False
        """
        # 计算a和b的LCA
        lca_node = self.lca(a, b)
        # 计算从a到LCA的路径字符串哈希值
        hash1 = self.hash(a, lca_node, b)
        # 计算从b到LCA的路径字符串哈希值
        hash2 = self.hash(b, lca_node, a)
        # 比较两个哈希值是否相等
        return hash1 == hash2
        
    def lca(self, a, b):
        """
        计算树上两点的最近公共祖先
        :param a: 节点a
        :param b: 节点b
        :return: 最近公共祖先
        """
        # 确保a是深度更深的节点
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        # 将a提升到与b相同的深度
        for p in range(self.power, -1, -1):
            if self.deep[self.jump[a][p]] >= self.deep[b]:
                a = self.jump[a][p]
        # 如果a和b已经在同一节点，直接返回
        if a == b:
            return a
        # 同时向上跳跃找到LCA
        for p in range(self.power, -1, -1):
            if self.jump[a][p] != self.jump[b][p]:
                a = self.jump[a][p]
                b = self.jump[b][p]
        # 返回最近公共祖先
        return self.jump[a][0]
        
    def hash(self, from_node, lca_node, to_node):
        """
        计算从from节点到lca节点再到to节点的路径字符串哈希值
        :param from_node: 起始节点
        :param lca_node: 最近公共祖先
        :param to_node: 目标节点
        :return: 路径字符串的哈希值
        """
        # up是上坡hash值（从from到lca）
        up = self.s[from_node]
        # 计算上坡部分的哈希值
        for p in range(self.power, -1, -1):
            if self.deep[self.jump[from_node][p]] >= self.deep[lca_node]:
                # 向上路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
                up = up * self.kpow[1 << p] + self.stup[from_node][p]
                from_node = self.jump[from_node][p]
        # 如果终点就是LCA，只需要返回上坡部分的哈希值
        if to_node == lca_node:
            return up
        # down是下坡hash值（从lca到to）
        down = self.s[to_node]
        # height是目前下坡的总高度
        height = 1
        # 计算下坡部分的哈希值
        for p in range(self.power, -1, -1):
            # 注意这里是 > 而不是 >=，因为不需要包含LCA节点
            if self.deep[self.jump[to_node][p]] > self.deep[lca_node]:
                # 向下路径哈希 = 前半段哈希 * K^(后半段长度) + 后半段哈希
                down = self.stdown[to_node][p] * self.kpow[height] + down
                height += 1 << p
                to_node = self.jump[to_node][p]
        # 完整路径哈希 = 上坡哈希 * K^(下坡长度) + 下坡哈希
        return up * self.kpow[height] + down

def main():
    """
    主函数，处理输入和输出
    """
    # 由于这是算法题，我们模拟输入输出
    # 实际使用时可以根据具体需求调整
    
    # 示例测试
    n = 5
    solver = PathPalindrome(n)
    solver.build()
    
    # 设置节点字符 (示例: "abcba")
    chars = "abcba"
    for i, c in enumerate(chars, 1):
        solver.s[i] = ord(c) - ord('a') + 1
        
    # 添加边信息 (构建树结构)
    edges = [(1, 2), (2, 3), (3, 4), (4, 5)]
    for u, v in edges:
        solver.add_edge(u, v)
        
    # DFS预处理
    solver.dfs(1, 0)
    
    # 查询示例
    queries = [(1, 5), (2, 4)]
    for a, b in queries:
        result = "YES" if solver.is_palindrome(a, b) else "NO"
        print(f"查询({a},{b}): {result}")

if __name__ == "__main__":
    main()