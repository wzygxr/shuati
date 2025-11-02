# 树上异或路径最大值
# 给定一棵n个节点的树，每个节点有点权
# 有m次查询，每次查询格式为：
# 1 x y : 在以x为根的子树中找一个点，使其点权与y的异或值最大
# 2 x y z : 在x到y的路径上找一个点，使其点权与z的异或值最大
# 测试链接 : https://www.luogu.com.cn/problem/P4592

# 补充题目1: 树上子树异或最大值查询
# 在树结构中，每个节点有权值，查询以某节点为根的子树中与给定值异或的最大值
# 测试链接: https://www.luogu.com.cn/problem/P4592

# 补充题目2: 树上路径异或最大值查询
# 在树结构中，每个节点有权值，查询两点间路径上与给定值异或的最大值
# 测试链接: https://www.luogu.com.cn/problem/P4592

# 补充题目3: DFS序应用
# 利用DFS序将树上子树问题转化为区间问题
# 测试链接: https://www.luogu.com.cn/problem/P4592

# 补充题目4: LCA应用
# 利用最近公共祖先算法解决树上路径查询问题
# 测试链接: https://www.luogu.com.cn/problem/P4592

import sys
from collections import defaultdict


class XorPath:
    def __init__(self, max_n=100001):
        """
        初始化树上异或路径最大值求解器
        
        :param max_n: 最大节点数
        """
        # 最大节点数
        self.MAXN = max_n
        
        # Trie树最大节点数
        self.MAXT = max_n * 62
        
        # 倍增数组最大高度
        self.MAXH = 16
        
        # 位数，由于数字范围是1 <= 点权、z < 2^30，所以最多需要30位
        self.BIT = 29
        
        # 节点数和查询数
        self.n = 0
        self.m = 0
        
        # 每个节点的点权
        self.arr = [0] * self.MAXN
        
        # 链式前向星需要的数组（用于存储树的边）
        
        # head[i]表示节点i的第一条边的编号
        self.head = [0] * self.MAXN
        
        # next_edge[i]表示第i条边的下一条边的编号
        self.next_edge = [0] * (self.MAXN << 1)
        
        # to[i]表示第i条边指向的节点
        self.to = [0] * (self.MAXN << 1)
        
        # 链式前向星的边的计数器
        self.cntg = 0
        
        # 树上dfs需要的数据结构
        
        # deep[i]表示节点i的深度
        self.deep = [0] * self.MAXN
        
        # size[i]表示以节点i为根的子树大小
        self.size = [0] * self.MAXN
        
        # stjump[i][j]表示节点i向上跳2^j步到达的节点（用于LCA计算）
        self.stjump = [[0] * self.MAXH for _ in range(self.MAXN)]
        
        # dfn[i]表示节点i的DFS序号（用于将子树问题转化为区间问题）
        self.dfn = [0] * self.MAXN
        
        # dfn序号计数器
        self.cntd = 0
        
        # 可持久化Trie相关数据结构
        
        # root1[i]表示基于dfn序的可持久化Trie根节点编号（用于子树查询）
        self.root1 = [0] * self.MAXN
        
        # root2[i]表示基于父节点的可持久化Trie根节点编号（用于路径查询）
        self.root2 = [0] * self.MAXN
        
        # tree[i][0/1]表示Trie树节点i的左右子节点编号
        self.tree = [[0, 0] for _ in range(self.MAXT)]
        
        # pass_count[i]表示经过Trie树节点i的数字个数（用于区间查询）
        self.pass_count = [0] * self.MAXT
        
        # Trie树节点计数器
        self.cntt = 0
    
    # 添加边
    def addEdge(self, u, v):
        """
        添加一条无向边到链式前向星
        
        :param u: 起点
        :param v: 终点
        """
        # 创建新边
        self.cntg += 1
        self.next_edge[self.cntg] = self.head[u]
        self.to[self.cntg] = v
        self.head[u] = self.cntg
    
    # 插入数字到可持久化Trie中
    def insert(self, num, i):
        """
        在可持久化Trie树中插入一个数字
        实现可持久化的核心：只创建被修改的节点，其余节点继承历史版本
        
        :param num: 要插入的数字（节点点权）
        :param i: 前一个版本的根节点编号
        :return: 新版本的根节点编号
        """
        # 创建新根节点
        self.cntt += 1
        rt = self.cntt
        
        # 复用前一个版本的左右子树（可持久化的核心）
        self.tree[rt][0] = self.tree[i][0]
        self.tree[rt][1] = self.tree[i][1]
        
        # 经过该节点的数字个数加1
        self.pass_count[rt] = self.pass_count[i] + 1
        
        # 从高位到低位处理数字的每一位（Trie树的构建过程）
        pre = rt
        for b in range(self.BIT, -1, -1):
            # 提取第b位的值（0或1）
            path = (num >> b) & 1
            
            # 获取前一个版本中对应子节点
            i = self.tree[i][path]
            
            # 创建新节点（只创建需要改变的节点）
            self.cntt += 1
            cur = self.cntt
            
            # 复用前一个版本的子节点信息
            self.tree[cur][0] = self.tree[i][0]
            self.tree[cur][1] = self.tree[i][1]
            
            # 更新经过该节点的数字个数
            self.pass_count[cur] = self.pass_count[i] + 1
            
            # 连接父子节点
            self.tree[pre][path] = cur
            pre = cur
            
        return rt
    
    # 查询区间[u,v]中与num异或的最大值
    def query(self, num, u, v):
        """
        在可持久化Trie树中查询区间[u,v]与num异或的最大值
        利用pass_count数组实现区间查询：通过比较两个版本中节点pass_count值的差来判断区间内是否存在该路径
        
        :param num: 查询的目标数字
        :param u: 区间左边界对应版本的根节点编号
        :param v: 区间右边界对应版本的根节点编号
        :return: 区间内与num异或的最大值
        """
        ans = 0
        
        # 从高位到低位贪心选择使异或结果最大的路径
        for b in range(self.BIT, -1, -1):
            # 提取第b位的值
            path = (num >> b) & 1
            
            # 贪心策略：尽量选择与当前位相反的路径（使异或结果最大）
            best = path ^ 1
            
            # 区间查询的关键：通过pass_count值差判断区间内是否存在best路径
            # 如果在区间[u,v]中存在best路径，则选择该路径
            if self.pass_count[self.tree[v][best]] > self.pass_count[self.tree[u][best]]:
                # 将第b位置为1（异或结果为1）
                ans += 1 << b
                
                # 移动到best子节点
                u = self.tree[u][best]
                v = self.tree[v][best]
            else:
                # 否则只能选择相同路径
                u = self.tree[u][path]
                v = self.tree[v][path]
                
        return ans
    
    # 第一次dfs：计算节点深度、子树大小、dfn序等
    def dfs1(self, u, fa):
        """
        第一次DFS遍历树，计算节点深度、子树大小、dfn序等信息
        
        :param u: 当前节点
        :param fa: 父节点
        """
        # 计算节点深度
        self.deep[u] = self.deep[fa] + 1
        
        # 初始化子树大小
        self.size[u] = 1
        
        # 设置直接父节点
        self.stjump[u][0] = fa
        
        # 记录DFS序号（将树上问题转化为序列问题的关键）
        self.cntd += 1
        self.dfn[u] = self.cntd
        
        # 预处理倍增数组（用于LCA计算）
        for p in range(1, self.MAXH):
            self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
        
        # 遍历子节点
        ei = self.head[u]
        while ei > 0:
            v = self.to[ei]
            if v != fa:
                # 递归处理子节点
                self.dfs1(v, u)
                
                # 累加子树大小
                self.size[u] += self.size[v]
            ei = self.next_edge[ei]
    
    # 第二次dfs：构建可持久化Trie
    def dfs2(self, u, fa):
        """
        第二次DFS遍历树，构建两种版本的可持久化Trie
        
        :param u: 当前节点
        :param fa: 父节点
        """
        # 基于dfn序构建Trie（用于子树查询）
        # 由于DFS序的性质，子树在序列中是连续的区间
        self.root1[self.dfn[u]] = self.insert(self.arr[u], self.root1[self.dfn[u] - 1])
        
        # 基于父节点构建Trie（用于路径查询）
        # 通过维护父子关系来支持路径查询
        self.root2[u] = self.insert(self.arr[u], self.root2[fa])
        
        # 遍历子节点
        ei = self.head[u]
        while ei > 0:
            if self.to[ei] != fa:
                # 递归处理子节点
                self.dfs2(self.to[ei], u)
            ei = self.next_edge[ei]
    
    # 计算两个节点的最近公共祖先
    def lca(self, a, b):
        """
        计算两个节点的最近公共祖先(LCA)
        使用倍增算法实现
        
        :param a: 节点a
        :param b: 节点b
        :return: 最近公共祖先节点编号
        """
        # 确保a节点深度不小于b节点
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        
        # 先将a调整到与b同一深度
        for p in range(self.MAXH - 1, -1, -1):
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                a = self.stjump[a][p]
        
        # 如果a和b在同一节点，直接返回
        if a == b:
            return a
        
        # 同时向上跳，直到相遇
        for p in range(self.MAXH - 1, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
        
        # 返回最近公共祖先的父节点
        return self.stjump[a][0]
    
    def solve(self, n, m, node_values, edges, queries):
        """
        解决树上异或路径最大值问题的主函数
        
        :param n: 节点数
        :param m: 查询数
        :param node_values: 节点点权列表
        :param edges: 边列表
        :param queries: 查询列表
        :return: 查询结果列表
        """
        self.n = n
        self.m = m
        
        # 设置节点值
        for i in range(1, n + 1):
            self.arr[i] = node_values[i - 1]
        
        # 添加边
        for u, v in edges:
            self.addEdge(u, v)
            self.addEdge(v, u)
        
        # 预处理阶段
        # 第一次DFS：计算树的基本信息
        self.dfs1(1, 0)
        
        # 第二次DFS：构建可持久化Trie
        self.dfs2(1, 0)
        
        # 处理查询
        results = []
        for query in queries:
            op = query[0]  # 操作类型
            x = query[1]   # 第一个参数
            y = query[2]   # 第二个参数
            
            if op == 1:
                # 子树查询：在以x为根的子树中找一个点，使其点权与y的异或值最大
                # 利用DFS序的性质，子树在序列中是连续区间[dfn[x], dfn[x]+size[x]-1]
                result = self.query(y, self.root1[self.dfn[x] - 1], self.root1[self.dfn[x] + self.size[x] - 1])
                results.append(result)
            else:
                # 路径查询：在x到y的路径上找一个点，使其点权与z的异或值最大
                z = query[3]  # 第三个参数
                
                # 计算x和y的最近公共祖先
                lca_node = self.lca(x, y)
                
                # 获取LCA的父节点（用于容斥计算）
                lca_fa = self.stjump[lca_node][0]
                
                # 利用容斥原理计算路径上与z异或的最大值：
                # 路径x->y上的点 = (路径root->x上的点) ∪ (路径root->y上的点) - (路径root->lca上的点) - (路径root->lca_fa上的点)
                ans1 = self.query(z, self.root2[lca_fa], self.root2[x])  # x到LCA路径上的点
                ans2 = self.query(z, self.root2[lca_fa], self.root2[y])  # y到LCA路径上的点
                results.append(max(ans1, ans2))
        
        return results


# 测试用例
def main():
    """
    主函数，用于测试树上异或路径最大值求解器
    """
    # 创建求解器实例
    solver = XorPath()
    
    # 示例输入
    n, m = 4, 2
    node_values = [1, 2, 3, 4]
    edges = [(1, 2), (1, 3), (2, 4)]
    queries = [
        [1, 1, 5],    # 子树查询：在以节点1为根的子树中找一个点，使其点权与5的异或值最大
        [2, 3, 4, 6]  # 路径查询：在节点3到节点4的路径上找一个点，使其点权与6的异或值最大
    ]
    
    # 求解并输出结果
    results = solver.solve(n, m, node_values, edges, queries)
    for res in results:
        print(res)


if __name__ == "__main__":
    main()

'''
算法分析:
时间复杂度: O((n + m) * log M)
  - n是节点数，m是查询数
  - log M是数字的位数（这里M=2^30，所以log M=30）
  - 每次插入和查询操作都需要遍历数字的所有位
  - LCA计算的时间复杂度为O(log n)
空间复杂度: O(n * log M)
  - 需要存储两个版本的可持久化Trie
  - 每个版本的Trie最多有log M个节点
  - 总共有n个版本

算法思路:
1. 使用两次dfs预处理树的信息：
   - 第一次计算深度、子树大小、dfn序、倍增数组
   - 第二次构建可持久化Trie
2. 构建两种版本的可持久化Trie：
   - root1: 基于dfn序，用于子树查询
   - root2: 基于父节点，用于路径查询
3. 对于子树查询，在dfn序的区间中查找
4. 对于路径查询，利用LCA将路径分为两段分别查询

关键点:
1. 树上DFS的两次遍历技巧
2. 可持久化Trie的两种构建方式
3. LCA算法的倍增实现
4. 树上路径的拆分技巧

数学原理:
1. DFS序性质：子树在DFS序中是连续的区间
2. 容斥原理：树上路径x->y的点集 = (root->x) ∪ (root->y) - (root->lca) - (root->lca_fa)
3. 倍增LCA：通过预处理跳转表快速计算LCA

工程化考量:
1. 内存管理：合理设置数组大小，避免内存浪费
2. 边界处理：正确处理根节点、叶子节点等特殊情况
3. 性能优化：使用位运算提高计算效率
4. 代码可读性：详细的注释和清晰的变量命名

跨语言实现差异:
1. Python使用列表实现Trie节点，代码简洁但性能可能不如数组实现
2. Python有自动垃圾回收，不需要手动释放内存
3. Python中的位运算与Java/C++基本相同

算法在工程中的应用:
1. 社交网络分析：在社交网络树结构中查找具有特定属性的用户
2. 文件系统：在目录树中查找满足特定条件的文件
3. 网络路由：在网络拓扑树中查找最优路径
4. 数据库索引：树形索引结构中的范围查询优化

调试技巧:
1. 打印中间变量：在关键步骤打印DFS序、深度、Trie节点状态
2. 小例子测试：用简单的树结构验证算法逻辑的正确性
3. 边界测试：测试单节点树、链式树等特殊情况
4. 性能分析：对于大数据量输入，使用性能分析工具监控时间和内存占用

算法优化建议:
1. 对于稀疏数据，可以使用压缩Trie减少空间占用
2. 对于频繁查询的场景，可以增加缓存机制
3. 可以使用位运算的优化技巧，如预计算位掩码
4. 对于大数据量，可以考虑使用NumPy等库优化数组操作
'''