# -*- coding: utf-8 -*-
'''
高级动态规划算法融合实现
包含多种优化技术和应用示例
'''
import sys
import math
import heapq
from collections import defaultdict, deque

class DPFusion:
    """
    动态规划融合类：包含多种高级DP优化技术和算法
    """
    
    # ==================== 高级优化体系：Knuth优化 ====================
    """
    Knuth优化的DP算法
    
    问题描述：
    解决区间DP问题，其中状态转移方程满足四边形不等式
    
    解题思路：
    1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
    2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
    3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
    
    应用题目：
    - POJ 1160 Post Office
    - HDU 4008 Parent and son
    - Codeforces 245H Queries for Number of Palindromes
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    class KnuthOptimization:
        """
        Knuth优化实现类
        """
        
        @staticmethod
        def solve(n, cost_func):
            """
            Knuth优化的DP实现
            
            参数:
                n: 区间长度
                cost_func: 计算区间(i,j)代价的函数
            
            返回:
                最小代价矩阵
            """
            # 初始化dp和opt数组
            INF = float('inf')
            dp = [[INF] * (n + 1) for _ in range(n + 1)]
            opt = [[0] * (n + 1) for _ in range(n + 1)]
            
            # 初始化长度为1的区间
            for i in range(1, n + 1):
                dp[i][i] = 0
                opt[i][i] = i
            
            # 枚举区间长度
            for length in range(2, n + 1):
                # 枚举起始点
                for i in range(1, n - length + 2):
                    j = i + length - 1
                    # 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
                    lower = opt[i][j-1]
                    upper = opt[i+1][j] if (i + 1 <= j) else j - 1
                    
                    dp[i][j] = INF
                    for k in range(lower, upper + 1):
                        if dp[i][k] != INF and dp[k+1][j] != INF:
                            cost = cost_func(i, j)
                            if cost != INF:
                                current = dp[i][k] + dp[k+1][j] + cost
                                if current < dp[i][j]:
                                    dp[i][j] = current
                                    opt[i][j] = k
            
            return dp
        
        @staticmethod
        def optimal_binary_search_tree(keys, freq):
            """
            应用示例：最优二叉搜索树问题（POJ 3280）
            
            参数:
                keys: 键数组
                freq: 频率数组
            
            返回:
                最小代价
            
            时间复杂度：O(n^2)
            空间复杂度：O(n^2)
            """
            n = len(keys)
            
            def cost_func(i, j):
                """计算从i到j的频率和"""
                return sum(freq[k] for k in range(i-1, j))
            
            dp = DPFusion.KnuthOptimization.solve(n, cost_func)
            return dp[1][n]
    
    # ==================== 高级优化体系：Divide & Conquer Optimization ====================
    """
    分治优化（Divide & Conquer Optimization）
    
    问题描述：
    解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
    当转移满足决策单调性时使用
    
    解题思路：
    1. 利用决策单调性，使用分治法优化DP
    2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
    3. 使用分治的方式计算每个区间的最优决策
    
    应用题目：
    - Codeforces 321E Ciel and Gondolas
    - HDU 3480 Division
    - SPOJ LARMY
    
    时间复杂度：O(n*m log m)
    空间复杂度：O(n*m)
    """
    class DivideConquerOptimization:
        """
        分治优化实现类
        """
        
        def __init__(self):
            self.dp = None
            self.cost_func = None
            self.n = 0
            self.m = 0
        
        def _solve_subproblem(self, i, l, r, opt_l, opt_r):
            """
            分治求解dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
            """
            if l > r:
                return
            
            mid = (l + r) // 2
            best_k = opt_l
            self.dp[i][mid] = float('inf')
            
            # 在opt_l到min(mid, opt_r)之间寻找最优k
            for k in range(opt_l, min(mid, opt_r) + 1):
                if self.dp[i-1][k] != float('inf'):
                    cost = self.cost_func(k, mid)
                    if cost != float('inf'):
                        current = self.dp[i-1][k] + cost
                        if current < self.dp[i][mid]:
                            self.dp[i][mid] = current
                            best_k = k
            
            # 递归处理左右子区间
            self._solve_subproblem(i, l, mid - 1, opt_l, best_k)
            self._solve_subproblem(i, mid + 1, r, best_k, opt_r)
        
        def solve(self, n, m, cost_func):
            """
            主入口：分治优化DP
            
            参数:
                n: 维度1
                m: 维度2
                cost_func: 计算cost(k,j)的函数
            
            返回:
                dp数组
            """
            # 初始化dp数组
            INF = float('inf')
            self.dp = [[INF] * (m + 1) for _ in range(n + 1)]
            self.cost_func = cost_func
            self.n = n
            self.m = m
            
            self.dp[0][0] = 0  # 初始状态
            
            # 对每个i应用分治优化
            for i in range(1, n + 1):
                self._solve_subproblem(i, 1, m, 0, m)
            
            return self.dp
        
        @staticmethod
        def split_array(nums, k):
            """
            应用示例：将数组分成k个子数组的最小代价和（LeetCode 410）
            
            参数:
                nums: 输入数组
                k: 子数组数量
            
            返回:
                最小代价和
            
            时间复杂度：O(n*k log n)
            空间复杂度：O(n*k)
            """
            n = len(nums)
            # 预处理前缀和
            prefix_sum = [0] * (n + 1)
            for i in range(n):
                prefix_sum[i+1] = prefix_sum[i] + nums[i]
            
            # 代价函数：计算从k+1到j的和的平方
            def cost_func(k, j):
                sum_val = prefix_sum[j] - prefix_sum[k]
                return sum_val * sum_val  # 这里可以根据实际问题定义不同的代价
            
            optimizer = DPFusion.DivideConquerOptimization()
            dp = optimizer.solve(k, n, cost_func)
            return dp[k][n]

sys.setrecursionlimit(1 << 25)
    
    # ==================== 高级优化体系：SMAWK算法（行最小查询） ====================
    """
    SMAWK算法用于在Monge矩阵中快速查找每行的最小值
    
    问题描述：
    给定一个Monge矩阵，快速找到每行的最小值位置
    
    解题思路：
    1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
    2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
    3. 主要步骤包括行压缩和递归求解
    
    应用题目：
    - POJ 3156 Interconnect
    - Codeforces 472D Design Tutorial: Inverse the Problem
    - SPOJ MCQUERY
    
    时间复杂度：O(m+n)，其中m是行数，n是列数
    空间复杂度：O(m+n)
    """
    class SMAWK:
        """
        SMAWK算法实现类
        """
        
        @staticmethod
        def reduce_rows(rows, matrix):
            """
            行压缩：只保留可能成为最小值的行
            """
            stack = []
            for i in rows:
                while len(stack) >= 2:
                    j1 = stack.pop()
                    j2 = stack.pop()
                    stack.append(j1)  # 恢复栈状态
                    
                    # 比较两个行在列stack.size()-2处的值
                    col = len(stack) - 2
                    if col < len(matrix[0]):
                        if matrix[j2][col] <= matrix[i][col]:
                            break
                        else:
                            stack.pop()  # 移除j1
                    else:
                        break
                stack.append(i)
            return stack
        
        @staticmethod
        def smawk_rec(rows, cols, matrix):
            """
            递归实现SMAWK算法
            """
            m = len(rows)
            result = [-1] * m
            
            if m == 0:
                return result
            
            # 行压缩
            reduced_rows = DPFusion.SMAWK.reduce_rows(rows, matrix)
            
            # 递归求解列数为奇数的子问题
            half_cols = [cols[i] for i in range(1, len(cols), 2)]
            
            min_cols = [-1] * len(reduced_rows)
            
            if half_cols:
                # 递归求解
                sub_result = DPFusion.SMAWK.smawk_rec(reduced_rows, half_cols, matrix)
                # 复制结果
                for i in range(len(sub_result)):
                    min_cols[i] = sub_result[i]
            
            # 扩展结果到所有列
            k = 0  # min_cols的索引
            
            for i in range(m):
                row = rows[i]
                # 确定当前行的最小值可能在哪个区间
                start = 0
                if i > 0 and k > 0 and min_cols[k-1] != -1:
                    start = min_cols[k-1]
                end = min_cols[k] if (k < len(min_cols) and min_cols[k] != -1) else cols[-1]
                
                # 在这个区间内查找最小值
                min_val = float('inf')
                min_col = start
                
                # 找到start和end在cols中的索引
                try:
                    start_idx = cols.index(start)
                    end_idx = cols.index(end)
                except ValueError:
                    start_idx = 0
                    end_idx = len(cols) - 1
                
                for idx in range(start_idx, end_idx + 1):
                    col = cols[idx]
                    if col < len(matrix[0]) and matrix[row][col] < min_val:
                        min_val = matrix[row][col]
                        min_col = col
                
                result[i] = min_col
                
                # 如果当前行在reduced_rows中，且不是最后一行，k前进
                if k < len(reduced_rows) and row == reduced_rows[k]:
                    k += 1
            
            return result
        
        @staticmethod
        def solve(matrix):
            """
            SMAWK算法主入口
            
            参数:
                matrix: 一个Monge矩阵
            
            返回:
                每行最小值的列索引数组
            """
            if not matrix or not matrix[0]:
                return []
            
            m = len(matrix)
            n = len(matrix[0])
            
            # 构造行索引和列索引数组
            rows = list(range(m))
            cols = list(range(n))
            
            # 调用递归实现
            return DPFusion.SMAWK.smawk_rec(rows, cols, matrix)
        
        @staticmethod
        def find_row_mins(matrix):
            """
            应用示例：寻找每一行的最小元素
            
            参数:
                matrix: 输入矩阵
            
            返回:
                每行最小元素组成的数组
                
            时间复杂度：O(m+n)
            空间复杂度：O(m+n)
            """
            min_cols = DPFusion.SMAWK.solve(matrix)
            result = [matrix[i][min_cols[i]] for i in range(len(matrix))]
            return result
    
    # ==================== 高级优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
    """
    Aliens Trick（二分约束参数+可行性DP）
    
    问题描述：
    解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
    
    解题思路：
    1. 将约束条件转化为参数λ，构造拉格朗日函数
    2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
    3. 根据可行性DP的结果调整二分区间
    
    应用题目：
    - Codeforces 739E Gosha is Hunting
    - POJ 3686 The Windy's
    - SPOJ QTREE5
    
    时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
    空间复杂度：O(DP空间复杂度)
    """
    class AliensTrick:
        """
        Aliens Trick实现类
        """
        
        class Result:
            """
            结果类
            """
            def __init__(self, lambda_val, value):
                self.lambda_val = lambda_val
                self.value = value
        
        class CostFunctionResult:
            """
            成本函数结果类
            """
            def __init__(self, value, constraint):
                self.value = value
                self.constraint = constraint
        
        @staticmethod
        def solve(cost_func, check_func, left, right, eps=1e-7):
            """
            Aliens Trick主入口
            
            参数:
                cost_func: 成本函数，输入lambda，返回CostFunctionResult
                check_func: 约束检查函数，输入约束值，返回是否满足约束
                left: 二分左边界
                right: 二分右边界
                eps: 精度要求
            
            返回:
                Result对象，包含最优参数lambda和对应最优解
            """
            best_lambda = left
            best_value = 0.0
            
            # 二分查找参数lambda
            while right - left > eps:
                mid = (left + right) / 2
                # 计算当前参数下的解和约束值
                result = cost_func(mid)
                
                if check_func(result.constraint):
                    # 满足约束，尝试更小的参数
                    right = mid
                    best_lambda = mid
                    best_value = result.value
                else:
                    # 不满足约束，需要增大参数
                    left = mid
            
            return DPFusion.AliensTrick.Result(best_lambda, best_value)
        
        @staticmethod
        def split_array_k(nums, k):
            """
            应用示例：将数组分成恰好k个部分，使得最大子数组和最小（LeetCode 410的变种）
            
            参数:
                nums: 输入数组
                k: 子数组数量
            
            返回:
                最小代价和
                
            时间复杂度：O(log(S) * n^2)，其中S是数组元素和
            空间复杂度：O(n)
            """
            # 计算数组元素和作为二分上限
            sum_val = sum(nums)
            
            # 成本函数：使用DP计算在给定lambda下的最小成本
            def cost_func(lambda_val):
                n = len(nums)
                INF = float('inf')
                dp = [INF] * (n + 1)
                cnt = [0] * (n + 1)
                
                dp[0] = 0
                cnt[0] = 0
                
                for i in range(1, n + 1):
                    sum_seg = 0
                    for j in range(i-1, -1, -1):
                        sum_seg += nums[j]
                        if dp[j] != INF:
                            current = dp[j] + sum_seg * sum_seg + lambda_val  # lambda作为惩罚项
                            if current < dp[i]:
                                dp[i] = current
                                cnt[i] = cnt[j] + 1
                
                return DPFusion.AliensTrick.CostFunctionResult(dp[n], cnt[n])
            
            # 约束检查函数：确保分割次数不超过k
            check_func = lambda constraint: constraint <= k
            
            # 执行Aliens Trick
            result = DPFusion.AliensTrick.solve(cost_func, check_func, 0, sum_val * sum_val, 1e-7)
            return result.value
    
    # ==================== 图上DP→最短路：分层图建模 ====================
    """
    分层图Dijkstra算法
    
    问题描述：
    给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
    
    解题思路：
    1. 构建分层图，每层代表使用不同次数的特殊操作
    2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
    3. 使用Dijkstra算法在分层图上寻找最短路径
    
    应用题目：
    - LeetCode 787. K 站中转内最便宜的航班
    - POJ 3159 Candies
    - HDU 2957 Safety Assessment
    
    时间复杂度：O((n*k + m*k) log(n*k))
    空间复杂度：O(n*k + m*k)
    """
    class LayeredGraphShortestPath:
        """
        分层图最短路径算法实现类
        """
        
        class Edge:
            """
            边类
            """
            def __init__(self, to, weight):
                self.to = to
                self.weight = weight
        
        @staticmethod
        def solve(n, edges, k, start, end):
            """
            分层图最短路径算法
            
            参数:
                n: 节点数量
                edges: 边的列表，edges[i]是节点i的边列表
                k: 允许使用的特殊操作次数
                start: 起始节点
                end: 目标节点
            
            返回:
                最短路径长度，-1表示不可达
            """
            # 构建分层图的邻接表
            layered_graph = [[] for _ in range(n * (k + 1))]
            total_nodes = n * (k + 1)
            
            # 添加普通边（不使用特殊操作）
            for i in range(n):
                for j in range(k + 1):
                    from_node = i + j * n
                    for edge in edges[i]:
                        layered_graph[from_node].append(DPFusion.LayeredGraphShortestPath.Edge(
                            edge.to + j * n, edge.weight))
            
            # 添加使用特殊操作的边（如果允许的话）
            for i in range(n):
                for j in range(k):
                    from_node = i + j * n
                    for edge in edges[i]:
                        # 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
                        layered_graph[from_node].append(DPFusion.LayeredGraphShortestPath.Edge(
                            edge.to + (j + 1) * n, 0))
            
            # Dijkstra算法
            INF = float('inf')
            dist = [INF] * total_nodes
            dist[start] = 0  # 起始点在第0层
            
            # 使用优先队列，按距离排序
            # (距离, 节点)的元组
            pq = [(0, start)]
            
            while pq:
                current_dist, u = heapq.heappop(pq)
                
                if current_dist > dist[u]:
                    continue
                
                for edge in layered_graph[u]:
                    v = edge.to
                    w = edge.weight
                    if dist[v] > current_dist + w:
                        dist[v] = current_dist + w
                        heapq.heappush(pq, (dist[v], v))
            
            # 取所有层中到达终点的最小值
            result = min(dist[end + i * n] for i in range(k + 1))
            
            return int(result) if result != INF else -1
        
        @staticmethod
        def find_cheapest_price(n, flights, src, dst, k):
            """
            应用示例：LeetCode 787. K 站中转内最便宜的航班
            
            参数:
                n: 城市数量
                flights: 航班列表，每个元素为[from, to, price]
                src: 出发城市
                dst: 到达城市
                k: 允许的中转次数
            
            返回:
                最便宜的价格，-1表示不可达
                
            时间复杂度：O((n*k + m*k) log(n*k))
            空间复杂度：O(n*k + m*k)
            """
            # 构建图的邻接表
            edges = [[] for _ in range(n)]
            for flight in flights:
                edges[flight[0]].append(DPFusion.LayeredGraphShortestPath.Edge(
                    flight[1], flight[2]))
            
            # 调用分层图算法，注意这里k站中转意味着可以乘坐k+1次航班
            return DPFusion.LayeredGraphShortestPath.solve(n, edges, k + 1, src, dst)
    
    # ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
    """
    期望DP处理有环情况（使用高斯消元）
    
    问题描述：
    在有环的状态转移图中计算期望
    
    解题思路：
    1. 对于每个状态，建立期望方程
    2. 使用高斯消元求解方程组
    
    应用题目：
    - LeetCode 837. 新21点
    - POJ 3744 Scout YYF I
    - HDU 4405 Aeroplane chess
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    class ExpectationDPWithGaussian:
        """
        期望DP与高斯消元实现类
        """
        
        class Transition:
            """
            转移类
            """
            def __init__(self, to, probability):
                self.to = to
                self.probability = probability
        
        @staticmethod
        def gaussian_elimination(matrix):
            """
            高斯消元法求解线性方程组
            
            参数:
                matrix: 增广矩阵，每行最后一个元素是b的值
            
            返回:
                方程组的解数组
            """
            n = len(matrix)
            eps = 1e-9
            
            # 高斯消元过程
            for i in range(n):
                # 找到主元行（当前列中绝对值最大的行）
                max_row = i
                for j in range(i, n):
                    if abs(matrix[j][i]) > abs(matrix[max_row][i]):
                        max_row = j
                
                # 交换主元行和当前行
                if max_row != i:
                    matrix[i], matrix[max_row] = matrix[max_row], matrix[i]
                
                # 如果主元为0，方程组可能有无穷多解或无解
                if abs(matrix[i][i]) < eps:
                    # 这里简化处理，假设方程组总是有解
                    continue
                
                # 消元过程
                for j in range(n):
                    if j != i and abs(matrix[j][i]) > eps:
                        factor = matrix[j][i] / matrix[i][i]
                        for k in range(i, n + 1):
                            matrix[j][k] -= factor * matrix[i][k]
            
            # 回代求解
            x = [0.0] * n
            for i in range(n):
                x[i] = matrix[i][n] / matrix[i][i]
            
            return x
        
        @staticmethod
        def solve(n, transitions, cost):
            """
            期望DP主入口
            
            参数:
                n: 状态数量
                transitions: 转移概率列表，transitions[i]是状态i的转移列表
                cost: 每个状态的代价
            
            返回:
                每个状态的期望值数组
            """
            # 构建线性方程组的增广矩阵
            matrix = [[0.0] * (n + 1) for _ in range(n)]
            
            for i in range(n):
                matrix[i][i] = 1.0  # 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
                matrix[i][n] = cost[i]
                
                for t in transitions[i]:
                    if i != t.to:  # 避免自环的特殊处理
                        matrix[i][t.to] -= t.probability
            
            # 使用高斯消元求解
            return DPFusion.ExpectationDPWithGaussian.gaussian_elimination(matrix)
        
        @staticmethod
        def new21game(N, K, W):
            """
            应用示例：LeetCode 837. 新21点（简化版本）
            
            参数:
                N: 目标值
                K: 停止抽牌的阈值
                W: 每张牌的最大值
            
            返回:
                获胜的概率
                
            时间复杂度：O(n^3)
            空间复杂度：O(n^2)
            """
            if K == 0 or N >= K + W:
                return 1.0
            
            n = K + W
            transitions = [[] for _ in range(n + 1)]
            cost = [0.0] * (n + 1)
            
            # 构建转移概率
            for i in range(K):
                for w in range(1, W + 1):
                    next_state = min(i + w, n)
                    transitions[i].append(DPFusion.ExpectationDPWithGaussian.Transition(
                        next_state, 1.0 / W))
            
            # 终止状态的期望为是否<=N
            for i in range(K, n + 1):
                cost[i] = 1.0 if i <= N else 0.0
                transitions[i].append(DPFusion.ExpectationDPWithGaussian.Transition(i, 1.0))  # 自环
            
            result = DPFusion.ExpectationDPWithGaussian.solve(n + 1, transitions, cost)
            return result[0]
    
    # ==================== 冷门模型：插头DP（轮廓线DP）====================
    """
    插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
    
    问题描述：
    给定一个网格，求其中哈密顿回路的数量
    
    解题思路：
    1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
    2. 插头表示连接的状态，通常用二进制表示
    3. 使用字典优化空间复杂度
    4. 实现合法性判定与对称剪枝
    
    应用题目：
    - HDU 1693 Eat the Trees
    - SPOJ MATCH2 Match the Brackets II
    - Codeforces 1435F Cyclic Shifts Sorting
    
    时间复杂度：O(n*m*4^min(n,m))
    空间复杂度：O(4^min(n,m))
    """
    class PlugDP:
        """
        插头DP实现类
        """
        
        @staticmethod
        def solve(grid):
            """
            插头DP求解哈密顿回路数量
            
            参数:
                grid: 网格，1表示可通行，0表示障碍物
            
            返回:
                哈密顿回路的数量
            """
            if not grid or not grid[0]:
                return 0
            
            n = len(grid)
            m = len(grid[0])
            
            # 使用字典优化空间
            dp = defaultdict(int)
            dp[0] = 1
            
            for i in range(n):
                # 新的一行开始，需要将状态左移两位
                new_dp = defaultdict(int)
                for state, cnt in dp.items():
                    # 左移两位，移除最左边的插头
                    new_state = state << 2
                    # 移除可能的高位，只保留m*2位
                    new_state &= (1 << (2 * m)) - 1
                    new_dp[new_state] += cnt
                dp = new_dp
                
                for j in range(m):
                    new_dp2 = defaultdict(int)
                    
                    for state, cnt in dp.items():
                        # 当前位置左边和上边的插头状态
                        left = (state >> (2 * j)) & 3
                        up = (state >> (2 * (j + 1))) & 3
                        
                        # 如果当前位置是障碍物，跳过
                        if grid[i][j] == 0:
                            # 只有当左右插头都不存在时才合法
                            if left == 0 and up == 0:
                                new_dp2[state] += cnt
                            continue
                        
                        # 处理各种插头组合情况
                        # 1. 没有左插头和上插头
                        if left == 0 and up == 0:
                            # 只能创建新的插头对（用于回路的开始）
                            if (i < n - 1 and j < m - 1 and 
                                grid[i+1][j] == 1 and grid[i][j+1] == 1):
                                new_state = state | (1 << (2 * j)) | (2 << (2 * (j + 1)))
                                new_dp2[new_state] += cnt
                        # 2. 只有左插头
                        elif left != 0 and up == 0:
                            # 向下延伸
                            if i < n - 1 and grid[i+1][j] == 1:
                                new_dp2[state] += cnt
                            # 向右延伸
                            if j < m - 1 and grid[i][j+1] == 1:
                                new_state = (state & ~(3 << (2 * j))) | (left << (2 * (j + 1)))
                                new_dp2[new_state] += cnt
                        # 3. 只有上插头
                        elif left == 0 and up != 0:
                            # 向右延伸
                            if j < m - 1 and grid[i][j+1] == 1:
                                new_dp2[state] += cnt
                            # 向下延伸
                            if i < n - 1 and grid[i+1][j] == 1:
                                new_state = (state & ~(3 << (2 * (j + 1)))) | (up << (2 * j))
                                new_dp2[new_state] += cnt
                        # 4. 同时有左插头和上插头
                        else:
                            # 合并插头
                            new_state = (state & ~(3 << (2 * j))) & ~(3 << (2 * (j + 1)))
                            
                            # 如果是形成回路的最后一步
                            if left == up:
                                # 检查是否所有插头都已连接
                                if new_state == 0 and i == n - 1 and j == m - 1:
                                    new_dp2[new_state] += cnt
                            else:
                                # 合并两个不同的插头
                                # 这里可以加入更多的合法性检查和剪枝
                                new_dp2[new_state] += cnt
                    
                    dp = new_dp2
            
            # 最终状态应该是没有任何插头（形成回路）
            return dp.get(0, 0)
        
        @staticmethod
        def count_grid_cycles(grid):
            """
            应用示例：网格中的回路计数
            
            参数:
                grid: 输入网格
            
            返回:
                回路数量
                
            时间复杂度：O(n*m*4^min(n,m))
            空间复杂度：O(4^min(n,m))
            """
            return DPFusion.PlugDP.solve(grid)
    
    # ==================== 冷门模型：树上背包的优化 ====================
    """
    树上背包的优化实现（小到大合并）
    
    问题描述：
    在树上选择一些节点，使得总重量不超过容量，且总价值最大
    
    解题思路：
    1. 使用后序遍历处理子树
    2. 使用小到大合并的策略优化复杂度
    3. 对于每个节点，维护一个容量为capacity的背包
    
    应用题目：
    - HDU 1561 The more, The Better
    - POJ 2063 Investment
    - Codeforces 1152F2 Neko Rules the Catniverse
    
    时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
    空间复杂度：O(n*capacity)
    """
    class TreeKnapsackOptimized:
        """
        优化的树上背包实现类
        """
        
        def __init__(self):
            self.dp = None
            self.size = None
            self.tree = None
            self.weights = None
            self.values = None
            self.capacity = 0
            self.n = 0
        
        def _dfs(self, u, parent):
            """
            树上背包的DFS处理函数
            """
            # 初始化当前节点
            self.size[u] = 1
            if self.weights[u] <= self.capacity:
                self.dp[u][self.weights[u]] = max(self.dp[u][self.weights[u]], self.values[u])
            
            # 对每个子节点，按照子树大小排序，小的先合并
            children = []
            for v in self.tree[u]:
                if v != parent:
                    self._dfs(v, u)
                    children.append((self.size[v], v))
            
            # 按子树大小排序（小到大）
            children.sort(key=lambda x: x[0])
            
            for sz, v in children:
                # 逆序遍历容量，避免重复计算
                for i in range(min(self.size[u], self.capacity), -1, -1):
                    if self.dp[u][i] == 0 and i != 0:
                        continue
                    for j in range(1, min(sz, self.capacity - i) + 1):
                        if self.dp[v][j] > 0 and i + j <= self.capacity:
                            self.dp[u][i + j] = max(self.dp[u][i + j], self.dp[u][i] + self.dp[v][j])
                
                # 更新子树大小
                self.size[u] += sz
        
        def solve(self, n, root, capacity, tree, weights, values):
            """
            树上背包主入口
            
            参数:
                n: 节点数量
                root: 根节点
                capacity: 背包容量
                tree: 树的邻接表
                weights: 每个节点的重量
                values: 每个节点的价值
            
            返回:
                最大价值
            """
            self.n = n
            self.capacity = capacity
            self.tree = tree
            self.weights = weights
            self.values = values
            
            # 初始化dp数组
            self.dp = [[0] * (capacity + 1) for _ in range(n + 1)]
            self.size = [0] * (n + 1)
            
            # 深度优先搜索处理子树
            self._dfs(root, -1)
            
            # 返回根节点的最大价值
            max_value = max(self.dp[root])
            return max_value
        
        @staticmethod
        def max_tree_value(n, root, capacity, tree, weights, values):
            """
            应用示例：树上最大价值选择
            
            参数:
                n: 节点数量
                root: 根节点
                capacity: 背包容量
                tree: 树的邻接表
                weights: 每个节点的重量
                values: 每个节点的价值
            
            返回:
                最大价值
                
            时间复杂度：O(n*capacity^2)
            空间复杂度：O(n*capacity)
            """
            optimizer = DPFusion.TreeKnapsackOptimized()
            return optimizer.solve(n, root, capacity, tree, weights, values)
    
    # ==================== 补充题目与应用 ====================
    """
    LeetCode 72. 编辑距离
    题目链接：https://leetcode-cn.com/problems/edit-distance/
    
    问题描述：
    给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
    你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
    
    时间复杂度：O(m*n)
    空间复杂度：O(m*n)
    """
    @staticmethod
    def min_distance(word1, word2):
        """
        计算编辑距离
        """
        m = len(word1)
        n = len(word2)
        # 初始化dp数组
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 初始化边界条件
        for i in range(m + 1):
            dp[i][0] = i
        for j in range(n + 1):
            dp[0][j] = j
        
        # 填充dp表
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if word1[i-1] == word2[j-1]:
                    dp[i][j] = dp[i-1][j-1]
                else:
                    dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
        
        return dp[m][n]
    
    """
    LeetCode 300. 最长递增子序列
    题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
    
    问题描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    @staticmethod
    def length_of_lis(nums):
        """
        计算最长递增子序列长度
        """
        if not nums:
            return 0
        
        tails = []
        
        for num in nums:
            # 二分查找tails中第一个大于等于num的位置
            left, right = 0, len(tails)
            while left < right:
                mid = (left + right) // 2
                if tails[mid] < num:
                    left = mid + 1
                else:
                    right = mid
            
            if left == len(tails):
                tails.append(num)
            else:
                tails[left] = num
        
        return len(tails)
    
    """
    LeetCode 322. 零钱兑换
    题目链接：https://leetcode-cn.com/problems/coin-change/
    
    问题描述：
    给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
    计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
    
    时间复杂度：O(amount * n)
    空间复杂度：O(amount)
    """
    @staticmethod
    def coin_change(coins, amount):
        """
        计算凑成总金额所需的最少硬币个数
        """
        # 初始化dp数组，dp[i]表示凑成金额i所需的最少硬币数
        dp = [amount + 1] * (amount + 1)
        dp[0] = 0  # 基础情况
        
        for i in range(1, amount + 1):
            for coin in coins:
                if coin <= i:
                    dp[i] = min(dp[i], dp[i - coin] + 1)
        
        return dp[amount] if dp[amount] <= amount else -1
    
    """
    矩阵链乘法问题
    题目来源：算法导论、POJ 1038
    
    问题描述：
    给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    @staticmethod
    def matrix_chain_order(p):
        """
        计算矩阵链乘法的最优顺序
        
        参数:
            p: 维度数组，p[i-1]和p[i]分别是矩阵A_i的行数和列数
        
        返回:
            最少标量乘法次数
        """
        n = len(p) - 1  # 矩阵的个数
        # 初始化dp数组
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        
        # 枚举子链长度
        for length in range(2, n + 1):
            for i in range(1, n - length + 2):
                j = i + length - 1
                dp[i][j] = float('inf')
                # 枚举分割点
                for k in range(i, j):
                    # 计算在位置k分割的代价
                    cost = dp[i][k] + dp[k+1][j] + p[i-1] * p[k] * p[j]
                    if cost < dp[i][j]:
                        dp[i][j] = cost
        
        return dp[1][n]
    
    """
    旅行商问题（TSP）
    题目来源：算法竞赛、POJ 2480
    
    问题描述：
    给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
    
    时间复杂度：O(n^2 * 2^n)
    空间复杂度：O(n * 2^n)
    """
    @staticmethod
    def tsp(graph):
        """
        解决旅行商问题
        
        参数:
            graph: 邻接矩阵，表示城市间的距离
        
        返回:
            最短路径长度
        """
        n = len(graph)
        # dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径
        dp = [[float('inf')] * n for _ in range(1 << n)]
        
        # 初始化：从城市0出发
        dp[1][0] = 0
        
        # 枚举所有可能的状态
        for mask in range(1, 1 << n):
            for u in range(n):
                if not (mask & (1 << u)):  # 如果u不在mask中，跳过
                    continue
                if dp[mask][u] == float('inf'):  # 如果无法到达u，跳过
                    continue
                
                # 尝试从u出发访问未访问的城市v
                for v in range(n):
                    if mask & (1 << v):  # 如果v已经访问过，跳过
                        continue
                    if graph[u][v] == float('inf'):  # 如果u和v之间没有边，跳过
                        continue
                    
                    new_mask = mask | (1 << v)
                    if dp[new_mask][v] > dp[mask][u] + graph[u][v]:
                        dp[new_mask][v] = dp[mask][u] + graph[u][v]
        
        # 找到最短的回路
        result = min(dp[(1 << n) - 1][u] + graph[u][0] for u in range(n) 
                    if graph[u][0] != float('inf'))
        
        return result if result != float('inf') else -1

# 测试代码
if __name__ == "__main__":
    # 测试编辑距离
    print("编辑距离测试:")
    print(DPFusion.min_distance("horse", "ros"))  # 预期输出: 3
    print(DPFusion.min_distance("intention", "execution"))  # 预期输出: 5
    
    # 测试最长递增子序列
    print("\n最长递增子序列测试:")
    print(DPFusion.length_of_lis([10, 9, 2, 5, 3, 7, 101, 18]))  # 预期输出: 4
    
    # 测试零钱兑换
    print("\n零钱兑换测试:")
    print(DPFusion.coin_change([1, 2, 5], 11))  # 预期输出: 3
    
    # 测试矩阵链乘法
    print("\n矩阵链乘法测试:")
    print(DPFusion.matrix_chain_order([30, 35, 15, 5, 10, 20, 25]))  # 预期输出: 15125
    
    # 测试TSP
    print("\nTSP测试:")
    INF = float('inf')
    graph = [
        [0, 10, 15, 20],
        [10, 0, 35, 25],
        [15, 35, 0, 30],
        [20, 25, 30, 0]
    ]
    print(DPFusion.tsp(graph))  # 预期输出: 80

# ==================== 优化体系：Knuth优化 ====================
def knuth_optimization(n, cost_func):
    '''
    Knuth优化的DP算法
    
    问题描述：
    解决区间DP问题，其中状态转移方程满足四边形不等式
    
    解题思路：
    1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
    2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
    3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
    
    参数：
        n: 区间长度
        cost_func: 计算区间(i,j)代价的函数
    
    返回：
        dp: 结果DP数组
        opt: 最优转移点数组
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    '''
    # 初始化dp和opt数组
    INF = float('inf')
    dp = [[INF] * (n + 1) for _ in range(n + 1)]
    opt = [[0] * (n + 1) for _ in range(n + 1)]
    
    # 初始化长度为1的区间
    for i in range(1, n + 1):
        dp[i][i] = 0
        opt[i][i] = i
    
    # 枚举区间长度
    for length in range(2, n + 1):
        # 枚举起始点
        for i in range(1, n - length + 2):
            j = i + length - 1
            # 初始化为无穷大
            dp[i][j] = INF
            # 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
            if i + 1 <= j:
                upper_k = opt[i + 1][j]
            else:
                upper_k = j - 1
            
            for k in range(opt[i][j-1], min(upper_k, j-1) + 1):
                if dp[i][k] != INF and dp[k+1][j] != INF:
                    cost = cost_func(i, j)
                    if cost != INF:
                        current = dp[i][k] + dp[k+1][j] + cost
                        if current < dp[i][j]:
                            dp[i][j] = current
                            opt[i][j] = k
    
    return dp, opt

# ==================== 优化体系：Divide & Conquer Optimization ====================
def solve_divide_conquer(i, l, r, opt_l, opt_r, dp, cost_func):
    '''
    计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
    '''
    if l > r:
        return
    
    mid = (l + r) // 2
    best_k = opt_l
    INF = float('inf')
    
    # 在opt_l到min(mid, opt_r)之间寻找最优k
    for k in range(opt_l, min(mid, opt_r) + 1):
        if dp[i-1][k] != INF:
            cost = cost_func(k, mid)
            if cost != INF:
                current = dp[i-1][k] + cost
                if current < dp[i][mid]:
                    dp[i][mid] = current
                    best_k = k
    
    # 递归处理左右子区间
    solve_divide_conquer(i, l, mid - 1, opt_l, best_k, dp, cost_func)
    solve_divide_conquer(i, mid + 1, r, best_k, opt_r, dp, cost_func)

def divide_conquer_optimization(n, m, cost_func):
    '''
    Divide & Conquer Optimization（分治优化）
    
    问题描述：
    解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
    当转移满足决策单调性时使用
    
    解题思路：
    1. 利用决策单调性，使用分治法优化DP
    2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
    3. 使用分治的方式计算每个区间的最优决策
    
    参数：
        n: 维度1
        m: 维度2
        cost_func: 计算cost(k,j)的函数
    
    返回：
        dp: DP数组
    
    时间复杂度：O(n*m log m)
    空间复杂度：O(n*m)
    '''
    # 初始化dp数组
    INF = float('inf')
    dp = [[INF] * (m + 1) for _ in range(n + 1)]
    dp[0][0] = 0
    
    # 对每个i应用分治优化
    for i in range(1, n + 1):
        solve_divide_conquer(i, 1, m, 0, m, dp, cost_func)
    
    return dp

# ==================== 优化体系：SMAWK算法（行最小查询） ====================
def reduce_rows(rows, matrix):
    '''行压缩：只保留可能成为最小值的行'''
    stack = []
    for i in rows:
        while len(stack) >= 2:
            j1 = stack.pop()
            j2 = stack[-1]
            stack.append(j1)  # 恢复栈状态
            
            # 比较两个行在列len(stack)-2处的值（因为索引从0开始）
            if matrix[j2][len(stack)-2] <= matrix[i][len(stack)-2]:
                break
            else:
                stack.pop()
        stack.append(i)
    return stack

def smawk_rec(rows, cols, matrix):
    '''递归实现SMAWK算法'''
    if not rows:
        return []
    
    # 行压缩
    reduced_rows = reduce_rows(rows, matrix)
    
    # 递归求解列数为奇数的子问题
    half_cols = cols[1::2]
    min_cols = [-1] * len(reduced_rows)
    
    if half_cols:
        # 递归求解
        result = smawk_rec(reduced_rows, half_cols, matrix)
        # 复制结果
        for i in range(len(result)):
            min_cols[i] = result[i]
    
    # 扩展结果到所有列
    result = [0] * len(rows)
    k = 0  # min_cols的索引
    
    for i in range(len(rows)):
        row = rows[i]
        # 确定当前行的最小值可能在哪个区间
        start = 0 if i == 0 else (min_cols[k-1] if k > 0 else 0)
        end = min_cols[k] if k < len(min_cols) else cols[-1]
        
        # 在这个区间内查找最小值
        min_val = float('inf')
        min_col = start
        
        # 找到start和end在cols中的索引
        start_idx = cols.index(start) if start in cols else -1
        end_idx = cols.index(end) if end in cols else -1
        
        if start_idx != -1 and end_idx != -1:
            for idx in range(start_idx, end_idx + 1):
                col = cols[idx]
                if col < len(matrix[0]) and matrix[row][col] < min_val:
                    min_val = matrix[row][col]
                    min_col = col
        
        result[i] = min_col
        
        # 如果当前行在reduced_rows中，且不是最后一行，k前进
        if k < len(reduced_rows) and row == reduced_rows[k]:
            k += 1
    
    return result

def smawk(matrix):
    '''
    SMAWK算法用于在Monge矩阵中快速查找每行的最小值
    
    问题描述：
    给定一个Monge矩阵，快速找到每行的最小值位置
    
    解题思路：
    1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
    2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
    3. 主要步骤包括行压缩和递归求解
    
    参数：
        matrix: 一个Monge矩阵
    
    返回：
        每行最小值的列索引列表
    
    时间复杂度：O(m+n)，其中m是行数，n是列数
    空间复杂度：O(m+n)
    '''
    m = len(matrix)
    if m == 0:
        return []
    n = len(matrix[0])
    
    # 构造行索引和列索引数组
    rows = list(range(m))
    cols = list(range(n))
    
    # 调用递归实现
    return smawk_rec(rows, cols, matrix)

# ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================
def aliens_trick(cost_func, check_func, left, right, eps=1e-7):
    '''
    Aliens Trick（二分约束参数+可行性DP）
    
    问题描述：
    解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
    
    解题思路：
    1. 将约束条件转化为参数λ，构造拉格朗日函数
    2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
    3. 根据可行性DP的结果调整二分区间
    
    参数：
        cost_func: 计算带参数λ的成本函数，返回[value, constraint]数组
        check_func: 检查当前解是否满足约束的函数
        left: 二分左边界
        right: 二分右边界
        eps: 精度要求
    
    返回：
        包含最优参数lambda和对应最优解的元组(lambda, value)
    
    时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
    '''
    best_lambda = left
    best_value = 0.0
    
    while right - left > eps:
        mid = (left + right) / 2
        # 计算当前参数下的解和约束值
        current_value, constraint_value = cost_func(mid)
        
        if check_func(constraint_value):
            # 满足约束，尝试更小的参数
            right = mid
            best_lambda = mid
            best_value = current_value
        else:
            # 不满足约束，需要增大参数
            left = mid
    
    return (best_lambda, best_value)

# ==================== 图上DP→最短路：分层图建模 ====================
def layered_graph_dijkstra(n, m, edges, k):
    '''
    分层图Dijkstra算法
    
    问题描述：
    给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
    
    解题思路：
    1. 构建分层图，每层代表使用不同次数的特殊操作
    2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
    3. 使用Dijkstra算法在分层图上寻找最短路径
    
    参数：
        n: 节点数量
        m: 边的数量
        edges: 边的列表，每个元素为[u, v, w]表示u到v的权为w的边
        k: 允许使用的特殊操作次数
    
    返回：
        从节点0到节点n-1的最短路径长度
    
    时间复杂度：O((n*k + m*k) log(n*k))
    空间复杂度：O(n*k + m*k)
    '''
    # 构建分层图的邻接表
    graph = [[] for _ in range(n * (k + 1))]
    
    # 添加普通边（不使用特殊操作）
    for u, v, w in edges:
        for i in range(k + 1):
            from_node = u + i * n
            graph[from_node].append((v + i * n, w))
    
    # 添加使用特殊操作的边（如果允许的话）
    for u, v, w in edges:
        for i in range(k):
            # 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
            from_node = u + i * n
            graph[from_node].append((v + (i + 1) * n, 0))
    
    # Dijkstra算法
    INF = float('inf')
    dist = [INF] * (n * (k + 1))
    dist[0] = 0  # 假设起点是节点0
    
    # 使用优先队列，按距离排序
    heap = []
    heapq.heappush(heap, (0, 0))
    
    while heap:
        d, u = heapq.heappop(heap)
        
        if d > dist[u]:
            continue
        
        for v, w in graph[u]:
            if dist[v] > d + w:
                dist[v] = d + w
                heapq.heappush(heap, (dist[v], v))
    
    # 取所有层中到达终点的最小值
    result = INF
    for i in range(k + 1):
        result = min(result, dist[n - 1 + i * n])
    
    return result if result != INF else -1

# ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================
def gaussian_elimination(matrix):
    '''
    高斯消元法求解线性方程组
    
    问题描述：
    求解形如Ax = b的线性方程组
    
    解题思路：
    1. 构建增广矩阵
    2. 进行高斯消元，将矩阵转化为行阶梯形
    3. 回代求解
    
    参数：
        matrix: 增广矩阵，每行最后一个元素是b的值
    
    返回：
        方程组的解数组
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    '''
    n = len(matrix)
    eps = 1e-9
    
    # 高斯消元过程
    for i in range(n):
        # 找到主元行（当前列中绝对值最大的行）
        max_row = i
        for j in range(i, n):
            if abs(matrix[j][i]) > abs(matrix[max_row][i]):
                max_row = j
        
        # 交换主元行和当前行
        matrix[i], matrix[max_row] = matrix[max_row], matrix[i]
        
        # 如果主元为0，方程组可能有无穷多解或无解
        if abs(matrix[i][i]) < eps:
            continue
        
        # 消元过程
        for j in range(i + 1, n):
            factor = matrix[j][i] / matrix[i][i]
            for k in range(i, n + 1):
                matrix[j][k] -= factor * matrix[i][k]
    
    # 回代求解
    x = [0.0] * n
    for i in range(n - 1, -1, -1):
        x[i] = matrix[i][n]
        for j in range(i + 1, n):
            x[i] -= matrix[i][j] * x[j]
        x[i] /= matrix[i][i]
    
    return x

def expectation_dp_with_cycles(n, transitions):
    '''
    期望DP处理有环情况（使用高斯消元）
    
    问题描述：
    在有环的状态转移图中计算期望
    
    解题思路：
    1. 对于每个状态，建立期望方程
    2. 使用高斯消元求解方程组
    
    参数：
        n: 状态数量
        transitions: 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
    
    返回：
        每个状态的期望值数组
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    '''
    # 构建线性方程组的增广矩阵
    matrix = [[0.0] * (n + 1) for _ in range(n)]
    
    for i in range(n):
        matrix[i][i] = 1.0  # 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
        
        # 假设每个状态的代价为1，具体根据问题调整
        cost = 1.0
        matrix[i][n] = cost
        
        for j, p in transitions[i]:
            if i != j:  # 避免自环的特殊处理
                matrix[i][j] -= p
    
    # 使用高斯消元求解
    return gaussian_elimination(matrix)

# ==================== 冷门模型：插头DP（轮廓线DP） ====================
def plug_dp(grid):
    '''
    插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
    
    问题描述：
    给定一个网格，求其中哈密顿回路的数量
    
    解题思路：
    1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
    2. 插头表示连接的状态，通常用二进制表示
    3. 使用字典优化空间复杂度
    
    参数：
        grid: 网格，1表示可通行，0表示障碍物
    
    返回：
        哈密顿回路的数量
    
    时间复杂度：O(n*m*4^min(n,m))
    空间复杂度：O(4^min(n,m))
    '''
    n = len(grid)
    if n == 0:
        return 0
    m = len(grid[0])
    
    # 使用字典优化
    dp = defaultdict(int)
    
    # 初始状态：左上角没有插头
    dp[0] = 1
    
    for i in range(n):
        # 新的一行开始，需要将状态左移一位
        new_dp = defaultdict(int)
        for state, cnt in dp.items():
            # 左移一位，移除最左边的插头
            new_state = state << 1
            new_dp[new_state] += cnt
        dp = new_dp
        
        for j in range(m):
            new_dp2 = defaultdict(int)
            
            for state, cnt in dp.items():
                # 当前位置左边和上边的插头状态
                left = (state >> (2 * j)) & 3
                up = (state >> (2 * (j + 1))) & 3
                
                # 如果当前位置是障碍物，跳过
                if grid[i][j] == 0:
                    # 只有当左右插头都不存在时才合法
                    if left == 0 and up == 0:
                        new_dp2[state] += cnt
                    continue
                
                # 处理各种插头组合情况
                # 1. 没有左插头和上插头
                if left == 0 and up == 0:
                    # 只能创建新的插头对（用于回路的开始）
                    if i < n - 1 and j < m - 1 and grid[i+1][j] == 1 and grid[i][j+1] == 1:
                        new_state = state | (1 << (2 * j)) | (2 << (2 * (j + 1)))
                        new_dp2[new_state] += cnt
                
                # 2. 只有左插头
                elif left != 0 and up == 0:
                    # 向下延伸
                    if i < n - 1 and grid[i+1][j] == 1:
                        new_dp2[state] += cnt
                    # 向右延伸
                    if j < m - 1 and grid[i][j+1] == 1:
                        new_state = (state & ~(3 << (2 * j))) | (left << (2 * (j + 1)))
                        new_dp2[new_state] += cnt
                
                # 3. 只有上插头
                elif left == 0 and up != 0:
                    # 向右延伸
                    if j < m - 1 and grid[i][j+1] == 1:
                        new_dp2[state] += cnt
                    # 向下延伸
                    if i < n - 1 and grid[i+1][j] == 1:
                        new_state = (state & ~(3 << (2 * (j + 1)))) | (up << (2 * j))
                        new_dp2[new_state] += cnt
                
                # 4. 同时有左插头和上插头
                else:
                    # 合并插头
                    new_state = (state & ~(3 << (2 * j))) & ~(3 << (2 * (j + 1)))
                    
                    # 如果是形成回路的最后一步
                    if left == up:
                        # 检查是否所有插头都已连接
                        if new_state == 0 and i == n - 1 and j == m - 1:
                            new_dp2[new_state] += cnt
                    else:
                        # 合并两个不同的插头
                        new_dp2[new_state] += cnt
            
            dp = new_dp2
    
    # 最终状态应该是没有任何插头（形成回路）
    return dp.get(0, 0)

# ==================== 冷门模型：树上背包的优化 ====================
def dfs_tree_knapsack(u, parent, capacity, tree, weights, values, dp, size):
    # 初始化当前节点
    size[u] = 1
    if weights[u] <= capacity:
        dp[u][weights[u]] = values[u]
    
    # 对每个子节点，按照子树大小排序，小的先合并
    children = []
    for v in tree[u]:
        if v != parent:
            dfs_tree_knapsack(v, u, capacity, tree, weights, values, dp, size)
            children.append((size[v], v))
    
    # 按子树大小排序
    children.sort()
    
    for sz, v in children:
        # 逆序遍历容量，避免重复计算
        for i in range(min(size[u], capacity), -1, -1):
            if dp[u][i] == 0 and i != 0:
                continue
            for j in range(1, min(sz, capacity - i) + 1):
                if dp[v][j] > 0 and i + j <= capacity:
                    dp[u][i + j] = max(dp[u][i + j], dp[u][i] + dp[v][j])
        
        # 更新子树大小
        size[u] += sz

def tree_knapsack_optimized(root, capacity, tree, weights, values):
    '''
    树上背包的优化实现（小到大合并）
    
    问题描述：
    在树上选择一些节点，使得总重量不超过容量，且总价值最大
    
    解题思路：
    1. 使用后序遍历处理子树
    2. 使用小到大合并的策略优化复杂度
    3. 对于每个节点，维护一个容量为capacity的背包
    
    参数：
        root: 根节点
        capacity: 背包容量
        tree: 树的邻接表
        weights: 每个节点的重量
        values: 每个节点的价值
    
    返回：
        最大价值
    
    时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
    空间复杂度：O(n*capacity)
    '''
    n = len(tree)
    # 初始化dp数组
    dp = [[0] * (capacity + 1) for _ in range(n)]
    size = [0] * n
    
    # 深度优先搜索处理子树
    dfs_tree_knapsack(root, -1, capacity, tree, weights, values, dp, size)
    
    # 返回根节点的最大价值
    return max(dp[root])

# ==================== 补充题目与应用 ====================
# 以下是一些使用上述高级DP技术的经典题目及其代码实现

# 1. 编辑距离问题（LeetCode 72）
def edit_distance(word1, word2):
    '''
    LeetCode 72. 编辑距离
    题目链接：https://leetcode-cn.com/problems/edit-distance/
    
    问题描述：
    给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
    你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
    
    解题思路：
    使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
    
    时间复杂度：O(m*n)
    空间复杂度：O(m*n)
    '''
    m = len(word1)
    n = len(word2)
    # dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化边界
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j
    
    # 动态规划填表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i - 1] == word2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1]
            else:
                dp[i][j] = min(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1]) + 1
    
    return dp[m][n]

# 2. 最长递增子序列（LeetCode 300）
def length_of_lis(nums):
    '''
    LeetCode 300. 最长递增子序列
    题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
    
    问题描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    
    解题思路：
    使用贪心 + 二分查找优化的DP方法。
    tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    '''
    if not nums:
        return 0
    
    tails = []
    for num in nums:
        # 二分查找num应该插入的位置
        left, right = 0, len(tails)
        while left < right:
            mid = left + (right - left) // 2
            if tails[mid] >= num:
                right = mid
            else:
                left = mid + 1
        if left == len(tails):
            tails.append(num)
        else:
            tails[left] = num
    
    return len(tails)

# 3. 背包问题变种 - 完全背包（LeetCode 322）
def coin_change(coins, amount):
    '''
    LeetCode 322. 零钱兑换
    题目链接：https://leetcode-cn.com/problems/coin-change/
    
    问题描述：
    给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
    计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
    
    解题思路：
    使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
    
    时间复杂度：O(amount * n)
    空间复杂度：O(amount)
    '''
    # 初始化dp数组为无穷大
    INF = float('inf')
    dp = [INF] * (amount + 1)
    dp[0] = 0  # 凑成金额0需要0个硬币
    
    for coin in coins:
        for i in range(coin, amount + 1):
            if dp[i - coin] != INF:
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] != INF else -1

# 4. 矩阵链乘法（区间DP的经典应用）
def matrix_chain_order(p):
    '''
    矩阵链乘法问题
    题目来源：算法导论
    
    问题描述：
    给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
    
    解题思路：
    使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
    可以使用Knuth优化进一步降低时间复杂度。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    '''
    n = len(p) - 1  # 矩阵的个数
    # dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
    INF = float('inf')
    dp = [[INF] * (n + 1) for _ in range(n + 1)]
    # s[i][j]记录最优分割点
    s = [[0] * (n + 1) for _ in range(n + 1)]
    
    # 单个矩阵的代价为0
    for i in range(1, n + 1):
        dp[i][i] = 0
    
    # 枚举区间长度
    for length in range(2, n + 1):
        for i in range(1, n - length + 2):
            j = i + length - 1
            dp[i][j] = INF
            # 枚举分割点
            for k in range(i, j):
                # 计算当前分割点的代价
                cost = dp[i][k] + dp[k + 1][j] + p[i - 1] * p[k] * p[j]
                if cost < dp[i][j]:
                    dp[i][j] = cost
                    s[i][j] = k
    
    return dp, s

# 5. 旅行商问题（TSP）的DP实现
def traveling_salesman_problem(graph):
    '''
    旅行商问题
    题目来源：算法竞赛经典问题
    
    问题描述：
    给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
    
    解题思路：
    使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
    
    时间复杂度：O(n^2 * 2^n)
    空间复杂度：O(n * 2^n)
    '''
    n = len(graph)
    # dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
    INF = float('inf')
    dp = [[INF] * n for _ in range(1 << n)]
    
    # 初始状态：只访问了起点，路径长度为0
    for i in range(n):
        dp[1 << i][i] = 0
    
    # 枚举所有可能的状态
    for mask in range(1, 1 << n):
        # 枚举当前所在的城市
        for u in range(n):
            if not (mask & (1 << u)):
                continue
            # 枚举下一个要访问的城市
            for v in range(n):
                if mask & (1 << v):
                    continue
                new_mask = mask | (1 << v)
                if dp[mask][u] != INF and graph[u][v] != INF:
                    if dp[new_mask][v] > dp[mask][u] + graph[u][v]:
                        dp[new_mask][v] = dp[mask][u] + graph[u][v]
    
    # 找到最短的回路
    result = INF
    for u in range(n):
        if dp[(1 << n) - 1][u] != INF and graph[u][0] != INF:
            result = min(result, dp[(1 << n) - 1][u] + graph[u][0])
    
    return result if result != INF else -1

# 6. 区间DP：最优三角剖分
def minimum_score_triangulation(values):
    '''
    LeetCode 1039. 多边形三角剖分的最低得分
    题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
    
    问题描述：
    给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
    
    解题思路：
    使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    '''
    n = len(values)
    # dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
    INF = float('inf')
    dp = [[0] * n for _ in range(n)]
    
    # 枚举区间长度
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = INF
            # 枚举中间点
            for k in range(i + 1, j):
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + values[i] * values[k] * values[j])
    
    return dp[0][n - 1]

# 7. 博弈DP：石子游戏
def stone_game(piles):
    '''
    LeetCode 877. 石子游戏
    题目链接：https://leetcode-cn.com/problems/stone-game/
    
    问题描述：
    给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
    判断先手是否必胜。
    
    解题思路：
    使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    '''
    n = len(piles)
    # dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
    dp = [[0] * n for _ in range(n)]
    
    # 初始化单个石子堆
    for i in range(n):
        dp[i][i] = piles[i]
    
    # 枚举区间长度
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            # 先手可以选择取左边或右边
            dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1])
    
    # 先手净胜分大于0则必胜
    return dp[0][n - 1] > 0

# 8. 数位DP：统计1出现的次数
def count_digit_one(n):
    '''
    LeetCode 233. 数字1的个数
    题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
    
    问题描述：
    给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
    
    解题思路：
    使用数位DP，逐位处理每一位上1出现的次数。
    
    时间复杂度：O(log n)
    空间复杂度：O(log n)
    '''
    if n <= 0:
        return 0
    
    s = str(n)
    length = len(s)
    count = 0
    
    # 逐位处理
    for i in range(length):
        high = int(s[:i]) if i > 0 else 0
        current = int(s[i])
        low = int(s[i+1:]) if i < length - 1 else 0
        digit = 10 ** (length - i - 1)
        
        if current == 0:
            # 当前位为0，高位决定
            count += high * digit
        elif current == 1:
            # 当前位为1，高位+低位+1
            count += high * digit + low + 1
        else:
            # 当前位大于1，高位+1
            count += (high + 1) * digit
    
    return count

# 9. 树形DP：打家劫舍III
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

def rob_dfs(node):
    if not node:
        return [0, 0]
    
    left = rob_dfs(node.left)
    right = rob_dfs(node.right)
    
    # rob_current表示偷当前节点，not_rob_current表示不偷当前节点
    rob_current = node.val + left[1] + right[1]
    not_rob_current = max(left[0], left[1]) + max(right[0], right[1])
    
    return [rob_current, not_rob_current]

def rob(root):
    '''
    LeetCode 337. 打家劫舍 III
    题目链接：https://leetcode-cn.com/problems/house-robber-iii/
    
    问题描述：
    在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
    一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
    如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    
    解题思路：
    使用树形DP，对于每个节点，维护两个状态：偷或不偷。
    
    时间复杂度：O(n)
    空间复杂度：O(h)，h为树的高度
    '''
    result = rob_dfs(root)
    return max(result[0], result[1])

# 10. 状态压缩DP：蒙斯特曼问题
def monster_game(grid):
    '''
    蒙斯特曼问题
    题目来源：算法竞赛问题
    
    问题描述：
    在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
    
    解题思路：
    使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
    
    时间复杂度：O(n * 2^n)
    空间复杂度：O(2^n)
    '''
    n = len(grid)
    # dp[mask]表示处理到当前行，已放置的列的状态为mask时的方案数
    dp = [0] * (1 << n)
    dp[0] = 1
    
    for i in range(n):
        new_dp = [0] * (1 << n)
        for mask in range(1 << n):
            if dp[mask] == 0:
                continue
            # 枚举所有可能的放置位置
            for j in range(n):
                # 检查是否可以在(i,j)放置怪物
                if not (mask & (1 << j)) and grid[i][j] == 1:
                    # 检查对角线
                    valid = True
                    for k in range(i):
                        if (mask & (1 << k)) and abs(k - j) == i - k:
                            valid = False
                            break
                    if valid:
                        new_dp[mask | (1 << j)] += dp[mask]
        dp = new_dp
    
    return dp[(1 << n) - 1]

# 11. 高维DP：三维背包
def three_dimension_knapsack(n, capacity, items):
    '''
    三维背包问题
    题目来源：算法竞赛问题
    
    问题描述：
    有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
    
    解题思路：
    使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
    
    时间复杂度：O(n * V * W)
    空间复杂度：O(n * V * W)
    '''
    V, W = capacity
    # 初始化dp数组
    dp = [[[0] * (W + 1) for _ in range(V + 1)] for __ in range(n + 1)]
    
    for i in range(1, n + 1):
        v, w, val = items[i-1]
        for j in range(V + 1):
            for k in range(W + 1):
                # 不选当前物品
                dp[i][j][k] = dp[i-1][j][k]
                # 选当前物品（如果有足够的空间）
                if j >= v and k >= w:
                    dp[i][j][k] = max(dp[i][j][k], dp[i-1][j-v][k-w] + val)
    
    return dp[n][V][W]

# 12. 斜率优化DP示例
class ConvexHullTrick:
    '''
    凸包优化技巧示例
    题目来源：算法竞赛问题
    
    问题描述：
    当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
    
    解题思路：
    将转移方程转换为直线的形式，维护凸包以快速查询最小值。
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    '''
    
    class Line:
        def __init__(self, k, b):
            self.k = k
            self.b = b
    
    def __init__(self):
        self.dq = deque()
    
    def add_line(self, k, b):
        # 当队列中至少有两条直线时，检查是否需要删除末尾的直线
        while len(self.dq) >= 2:
            l1 = self._get_nth_last(2)
            l2 = self.dq[-1]
            # 判断直线l1和l2的交点是否在l2和新直线的交点右侧
            if (l2.b - l1.b) * (k - l2.k) >= (b - l2.b) * (l2.k - l1.k):
                self.dq.pop()
            else:
                break
        self.dq.append(self.Line(k, b))
    
    def _get_nth_last(self, n):
        if n <= 0 or n > len(self.dq):
            raise IndexError("索引越界")
        # 转换为列表获取倒数第n个元素
        temp = list(self.dq)
        return temp[-n]
    
    def query_correct(self, x):
        # 正确的查询实现
        while len(self.dq) >= 2:
            l1 = self.dq.popleft()
            l2 = self.dq[0]
            if l1.k * x + l1.b >= l2.k * x + l2.b:
                # 继续弹出
                continue
            else:
                self.dq.appendleft(l1)  # 恢复l1
                break
        
        if not self.dq:
            return float('inf')
        l = self.dq[0]
        return l.k * x + l.b

# ==================== 测试和调试 ====================
if __name__ == "__main__":
    # 测试编辑距离
    print("编辑距离测试:", edit_distance("horse", "ros"))  # 应输出 3
    
    # 测试最长递增子序列
    print("最长递增子序列测试:", length_of_lis([10, 9, 2, 5, 3, 7, 101, 18]))  # 应输出 4
    
    # 测试零钱兑换
    print("零钱兑换测试:", coin_change([1, 2, 5], 11))  # 应输出 3
    
    # 测试矩阵链乘法
    p = [30, 35, 15, 5, 10, 20, 25]
    dp, s = matrix_chain_order(p)
    print("矩阵链乘法最优代价:", dp[1][6])  # 应输出 15125
    
    # 测试石子游戏
    print("石子游戏测试:", stone_game([5, 3, 4, 5]))  # 应输出 True
    
    # 测试数字1的个数
    print("数字1的个数测试:", count_digit_one(13))  # 应输出 6