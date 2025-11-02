# 有向图博弈 (SG函数在有向图上的应用)
# 一个有向无环图，在若干点上有若干棋子，两人轮流移动棋子
# 每次只能将一个棋子沿有向边移动一步
# 当无棋子可移动时输，即移动最后一枚棋子者胜
# 
# 题目来源：
# 1. POJ 2425 A Chess Game - http://poj.org/problem?id=2425
# 2. HDU 1524 A Chess Game - http://acm.hdu.edu.cn/showproblem.php?pid=1524
# 3. POJ 2599 A New Stone Game - http://poj.org/problem?id=2599
# 
# 算法核心思想：
# 1. SG函数方法：通过递推计算每个节点的SG值，SG值不为0表示必胜态，为0表示必败态
# 2. SG定理：整个游戏的SG值等于各棋子所在节点SG值的异或和
# 
# 时间复杂度分析：
# 1. 预处理：O(n * max_degree) - 计算每个节点的SG值
# 2. 查询：O(m) - m为棋子数，计算所有棋子SG值的异或和
# 
# 空间复杂度分析：
# 1. 图存储：O(n + e) - n为节点数，e为边数
# 2. SG数组：O(n) - 存储每个节点的SG值
# 
# 工程化考量：
# 1. 异常处理：处理非法输入和边界情况
# 2. 性能优化：预处理SG值避免重复计算
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的图结构和查询

# 最大节点数
MAXN = 1001

# 图的邻接表表示
graph = [[] for _ in range(MAXN)]

# SG数组，sg[i]表示节点i的SG值
sg = [0] * MAXN

# visited数组用于记忆化搜索
visited = [False] * MAXN

def computeSG(node):
    """
    算法原理：
    1. 对于每个节点，计算其后继节点的SG值集合
    2. 节点的SG值等于不属于该集合的最小非负整数(mex)
    3. 根据SG定理，整个游戏的SG值等于各棋子所在节点SG值的异或和
    
    SG函数定义：
    SG(x) = mex{SG(y) | 存在从x到y的有向边}
    其中mex(S)表示不属于集合S的最小非负整数
    
    对于有向图博弈，节点x的后继状态为所有可以直接到达的节点
    """
    # 记忆化搜索
    if visited[node]:
        return sg[node]
    
    # 标记已访问
    visited[node] = True
    
    # 计算节点node的所有后继节点的SG值
    appear = set()
    for next_node in graph[node]:
        # 添加后继节点的SG值
        appear.add(computeSG(next_node))
    
    # 计算mex值，即不属于appear集合的最小非负整数
    mex = 0
    while mex in appear:
        mex += 1
    
    sg[node] = mex
    return sg[node]

def solve(chess_positions, n):
    """
    算法原理：
    根据SG定理计算整个游戏的SG值
    1. 对于每个棋子，计算其所在节点的SG值
    2. 整个游戏的SG值等于各棋子所在节点SG值的异或和
    3. SG值不为0表示必胜态，为0表示必败态
    """
    # 异常处理：处理空数组
    if not chess_positions:
        return "LOSE"  # 空游戏，先手败
    
    # 计算所有棋子所在节点SG值的异或和
    xor_sum = 0
    for pos in chess_positions:
        # 异常处理：处理非法节点
        if pos < 0 or pos >= n:
            return "输入非法"
        xor_sum ^= sg[pos]
    
    # SG值不为0表示必胜态，为0表示必败态
    return "WIN" if xor_sum != 0 else "LOSE"

def buildSG(n):
    """构建SG函数"""
    # 初始化visited数组
    global visited
    visited = [False] * MAXN
    
    # 计算每个节点的SG值
    for i in range(n):
        if not visited[i]:
            computeSG(i)

def clearGraph():
    """清空图"""
    global graph
    graph = [[] for _ in range(MAXN)]

# 测试示例
if __name__ == "__main__":
    # 构建一个简单的有向图
    # 节点0 -> 节点1, 节点2
    # 节点1 -> 节点3
    # 节点2 -> 节点3
    # 节点3 -> (无后继)
    clearGraph()
    graph[0].append(1)
    graph[0].append(2)
    graph[1].append(3)
    graph[2].append(3)
    
    # 构建SG函数
    buildSG(4)
    
    # 示例1: 棋子在节点0
    result1 = solve([0], 4)
    # 预期结果: WIN
    
    # 示例2: 棋子在节点1和节点2
    result2 = solve([1, 2], 4)
    # 预期结果: LOSE
    
    print(f"示例1结果: {result1}")
    print(f"示例2结果: {result2}")