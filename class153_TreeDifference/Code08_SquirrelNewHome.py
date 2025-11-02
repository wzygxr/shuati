# 松鼠的新家 (洛谷 P3258)
# 题目来源：洛谷
# 题目链接：https://www.luogu.com.cn/problem/P3258

"""
松鼠的新家问题（树上点差分算法实现）

题目描述：
松鼠家族的成员需要在树上移动，从一个节点到另一个节点。
给定一棵包含N个节点的树，以及N-1次移动操作。
每次移动操作表示从节点a移动到节点b，经过的路径上的所有节点（包括起点和终点）都会被访问一次。
求每个节点被访问的次数。

算法原理：树上点差分
树上差分是一种将路径操作转化为点标记操作的高效算法。
对于树上的路径u->v，我们需要让路径上的所有节点计数加1。
通过点差分，我们可以：
1. diff[u]++ - 在起点增加标记
2. diff[v]++ - 在终点增加标记
3. diff[lca(u,v)]-- - 在LCA处抵消一次（因为u和v都会到达LCA）
4. diff[parent(lca(u,v))]-- - 在LCA的父节点处抵消一次
最后通过一次DFS回溯累加子节点的差分标记，得到每个节点的最终计数。

时间复杂度分析：
- 预处理LCA：O(N log N)
- 差分标记：O(M)，其中M是操作次数
- DFS回溯统计：O(N)
总时间复杂度：O(N log N + M)，对于本题M=N-1，所以总时间复杂度为O(N log N)

空间复杂度分析：
- 树的存储：O(N)
- LCA倍增数组：O(N log N)
- 差分数组：O(N)
总空间复杂度：O(N log N)

工程化考量：
1. 使用邻接表存储树结构，Python中使用defaultdict(list)实现
2. 使用sys.stdin.read进行一次性读取，避免多次IO操作
3. 预处理log2值，优化倍增数组的大小
4. 递归DFS在Python中存在递归深度限制，对于大规模数据（>1000层）可能需要使用非递归DFS
5. 注意Python中的索引从0开始还是从1开始的问题

最优解分析：
树上差分是解决此类路径覆盖问题的最优解，相比暴力遍历每条路径的O(N*M)复杂度，
树上差分可以将时间复杂度优化到O(N log N)，在大规模数据下效率提升显著。
"""

import sys
import math
from collections import defaultdict

class SquirrelTreeSolver:
    """
    松鼠的新家问题求解器
    
    该类实现了树上点差分算法，通过两次DFS和LCA（最近公共祖先）计算，
    可以在O(N log N)的时间复杂度内高效解决树上路径覆盖问题。
    """
    
    # 最大倍增层数，2^19 = 524,288 > 3e5，足够处理题目中最大数据范围
    MAX_LEVEL = 19
    
    def __init__(self, n):
        """
        初始化树差分数据结构
        
        参数:
            n: 节点数量
        """
        self.n = n
        # 计算最大的幂次，确保2^power > n
        self.max_level = math.floor(math.log2(n)) + 1
        # 确保不超过预定义的最大层数
        self.max_level = min(self.max_level, self.MAX_LEVEL)
        # 差分数组，索引从1开始（与题目节点编号保持一致）
        self.diff = [0] * (n + 1)
        # 邻接表存储树结构
        self.tree = defaultdict(list)
        # 深度数组，记录每个节点的深度
        self.depth = [0] * (n + 1)
        # 父节点数组，记录每个节点的直接父节点
        self.parent = [0] * (n + 1)
        # 倍增数组，stjump[u][p]表示u的2^p级祖先
        self.stjump = [[0] * (self.MAX_LEVEL + 1) for _ in range(n + 1)]
    
    def add_edge(self, u, v):
        """
        向树中添加一条无向边
        
        参数:
            u: 边的起始节点
            v: 边的结束节点
            
        说明：由于树是无向的，需要在两个方向都添加边，
        这样在遍历树时可以双向访问相邻节点。
        """
        self.tree[u].append(v)
        self.tree[v].append(u)
    
    def _preprocess_lca(self, u, parent_node):
        """
        第一次DFS，预处理每个节点的深度、父节点和倍增跳跃数组
        
        参数:
            u: 当前处理的节点
            parent_node: 当前节点的父节点
            
        说明：该方法通过深度优先搜索构建LCA算法所需的数据结构，
        包括每个节点的深度信息和倍增跳跃表。
        """
        # 设置当前节点的深度（父节点深度+1）
        self.depth[u] = self.depth[parent_node] + 1
        # 设置当前节点的父节点
        self.parent[u] = parent_node
        # 设置当前节点的直接父节点（2^0级祖先）
        self.stjump[u][0] = parent_node
        
        # 预处理倍增数组
        # 利用动态规划思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
        # 从p=1开始，直到最大层数
        for p in range(1, self.max_level + 1):
            # 确保不会越界，根节点的祖先仍为0
            if self.stjump[u][p-1] != 0:
                self.stjump[u][p] = self.stjump[self.stjump[u][p-1]][p-1]
            else:
                # 当祖先不存在时，保持为0
                self.stjump[u][p] = 0
        
        # 递归处理所有子节点
        for v in self.tree[u]:
            # 避免回到父节点，造成无限递归
            if v != parent_node:
                self._preprocess_lca(v, u)
    
    def _find_lca(self, a, b):
        """
        使用倍增法计算两个节点的最近公共祖先
        
        参数:
            a: 第一个节点
            b: 第二个节点
            
        返回:
            a和b的最近公共祖先节点编号
            
        算法步骤：
        1. 确保a的深度不小于b
        2. 将a向上跳到与b同一深度
        3. 如果此时a==b，则直接返回a作为LCA
        4. 否则，a和b同时向上跳跃，直到它们的父节点相同
        5. 返回最终的父节点作为LCA
        """
        # 步骤1：确保a的深度不小于b
        if self.depth[a] < self.depth[b]:
            a, b = b, a
        
        # 步骤2：将a向上跳到与b同一深度
        # 从最高幂次开始尝试跳跃，确保最大步长
        for p in range(self.max_level, -1, -1):
            # 只有当跳跃后的深度不小于b的深度时才跳跃
            if self.depth[self.stjump[a][p]] >= self.depth[b]:
                a = self.stjump[a][p]
        
        # 步骤3：如果a和b相遇，说明找到了LCA
        if a == b:
            return a
        
        # 步骤4：同时向上跳跃，直到找到LCA的直接子节点
        for p in range(self.max_level, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
        
        # 步骤5：返回它们的父节点作为LCA
        return self.stjump[a][0]
    
    def _compute_diff_accumulation(self, u, parent_node):
        """
        第二次DFS，通过回溯累加子节点的差分标记，计算每个节点被访问的最终次数
        
        参数:
            u: 当前处理的节点
            parent_node: 当前节点的父节点
            
        说明：该方法通过后序遍历，将子节点的差分标记累加到父节点，
        是树上差分算法的关键步骤，将局部标记转化为全局计数。
        注意：该DFS必须在所有差分标记完成后执行。
        """
        # 步骤1：先递归处理所有子节点
        # 采用后序遍历的方式，确保子节点的计数先计算完成
        for v in self.tree[u]:
            if v != parent_node:
                self._compute_diff_accumulation(v, u)
        
        # 步骤2：将子节点的访问次数累加到当前节点
        # 这一步实现了差分标记的传播和累加，使得路径上的所有节点都能被正确计数
        for v in self.tree[u]:
            if v != parent_node:
                self.diff[u] += self.diff[v]
    
    def process_movements(self, movement_order):
        """
        处理所有移动操作，执行树上点差分
        
        参数:
            movement_order: 节点访问顺序列表（索引从1开始）
            
        说明：对于每一条移动路径u->v，执行树上点差分操作，
        通过修改四个关键点的差分值，实现路径覆盖标记。
        """
        for i in range(1, self.n):
            u = movement_order[i]      # 当前移动的起点
            v = movement_order[i + 1]  # 当前移动的终点
            lca_node = self._find_lca(u, v)     # 计算u和v的最近公共祖先
            lca_father = self.stjump[lca_node][0]  # LCA的父节点
            
            """
            树上点差分核心操作：
            对于路径u->v，我们希望路径上的所有节点计数加1
            通过差分技巧，我们只需要修改四个点：
            1. diff[u]++ - 在起点增加标记
            2. diff[v]++ - 在终点增加标记
            3. diff[lca_node]-- - 在LCA处抵消一次（因为u和v都会到达LCA）
            4. diff[lca_father]-- - 在LCA的父节点处抵消一次
            
            这种标记方式的原理是：
            - 在u和v增加标记，相当于在u到根和v到根的路径上都增加了标记
            - 在LCA和其父亲减少标记，是为了抵消重复计算的部分
            - 最终通过DFS累加，每个节点的差分值就是其在所有路径中的覆盖次数
            """
            self.diff[u] += 1
            self.diff[v] += 1
            self.diff[lca_node] -= 1
            # 根节点的父节点是0，不需要处理
            if lca_father != 0:
                self.diff[lca_father] -= 1

    def solve(self, movement_order):
        """
        解决松鼠的新家问题，获取每个节点的访问次数
        
        参数:
            movement_order: 节点访问顺序列表（索引从1开始）
            
        返回:
            每个节点的最终访问次数列表
            
        处理流程：
        1. 执行第二次DFS，累加差分标记
        2. 处理特殊情况：除最后一个节点外，其他节点需要减1
        """
        # 执行第二次DFS，通过回溯累加子节点的差分标记
        self._compute_diff_accumulation(1, 0)
        
        # 处理特殊情况，除了最后一个节点外，其他节点的访问次数需要减1
        # 原因：松鼠的移动是连续的，除了最后一个终点外，每个节点如果是某次移动的终点，
        # 它也会是下一次移动的起点。但在题目中，这种情况下节点只应被计数一次，而不是两次。
        result = []
        last_node = movement_order[self.n]  # 最后一个访问的节点
        for i in range(1, self.n + 1):
            if i == last_node:
                # 最后一个节点不需要减1
                result.append(self.diff[i])
            else:
                # 其他节点需要减1
                result.append(self.diff[i] - 1)
        
        return result

def main():
    """
    主函数，处理输入、算法执行和输出
    
    输入格式：
    第一行：节点数n
    第二行：n个整数，表示松鼠访问节点的顺序
    接下来n-1行：每行两个整数，表示树的边
    
    输出格式：
    输出n行，每行一个整数，表示每个节点被访问的次数
    """
    # 设置递归深度限制，避免大规模数据下栈溢出
    sys.setrecursionlimit(1 << 25)
    
    # 使用sys.stdin.read一次性读取所有输入，提高性能
    input = sys.stdin.read
    tokens = input().split()
    ptr = 0  # 指针，用于遍历tokens
    
    # 读取节点数量
    n = int(tokens[ptr])
    ptr += 1
    
    # 读取访问顺序（索引从1开始，与题目节点编号保持一致）
    movement_order = [0] * (n + 1)
    for i in range(1, n + 1):
        movement_order[i] = int(tokens[ptr])
        ptr += 1
    
    # 创建求解器实例
    solver = SquirrelTreeSolver(n)
    
    # 读取树的边，构建邻接表
    for _ in range(n - 1):
        u = int(tokens[ptr])
        v = int(tokens[ptr + 1])
        solver.add_edge(u, v)
        ptr += 2
    
    # 预处理LCA所需的数据（以节点1为根）
    solver._preprocess_lca(1, 0)
    
    # 处理所有移动操作，执行树上点差分
    solver.process_movements(movement_order)
    
    # 获取每个节点的最终访问次数
    result = solver.solve(movement_order)
    
    # 输出结果，每个节点占一行
    print('\n'.join(map(str, result)))

if __name__ == '__main__':
    main()