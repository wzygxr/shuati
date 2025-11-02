# 炮兵阵地 (Artillery Position)
# 题目来源: POJ 1185 炮兵阵地
# 题目链接: http://poj.org/problem?id=1185
# 题目描述:
# 司令部的将军们打算在N*M的网格地图上部署他们的炮兵部队。一个N*M的地图由N行M列组成，
# 地图的每一格可能是山地（用"H"表示），也可能是平原（用"P"表示）。
# 在每一格平原上可以布置一支炮兵部队，山地上则不可以。
# 一支炮兵部队在地图上的攻击范围是它所在位置的四个方向（上下左右）各两格内的区域，
# 但不包括该炮兵部队自身所在的格子。
# 任何一支炮兵部队的攻击范围内的格子（包括攻击范围的边界）不能再布置其他炮兵部队。
# 一支炮兵部队的攻击范围与其部署位置有关，不同位置的炮兵部队的攻击范围各不相同。
# 问题要求计算在给定的地图上最多能部署多少支炮兵部队。
#
# 解题思路:
# 这是一道经典的状态压缩DP问题。由于炮兵的攻击范围是上下左右各两格，
# 所以我们需要考虑当前行、前一行和前两行的状态。
# 我们可以按行进行状态压缩，用二进制位表示每一行的炮兵部署状态。
# 对于每一行，我们需要考虑：
# 1. 当前行的地形是否允许在某个位置部署炮兵（平原为P，山地为H）
# 2. 当前行的炮兵部署状态是否合法（同一行内炮兵不能互相攻击）
# 3. 当前行与前一行、前两行的炮兵部署状态是否冲突
#
# 状态定义:
# dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
#
# 状态转移:
# 对于每一行，我们枚举所有可能的合法状态，然后检查与前两行是否冲突
#
# 时间复杂度: O(n * 2^(3*m)) 其中n是行数，m是列数
# 空间复杂度: O(2^(2*m))
#
# 补充题目1: 最大兼容数对 (Compatible Numbers)
# 题目来源: CodeForces 165E
# 题目链接: https://codeforces.com/problemset/problem/165/E
# 题目描述:
# 给定一个数组，对于每个数字，找到另一个数字，使得它们的按位与结果为0。
# 如果不存在这样的数字，输出-1。
# 解题思路:
# 1. 使用状态压缩DP或SOS DP
# 2. 对于每个数字，我们需要找到另一个数字，使得它们的按位与为0
# 3. 这等价于找到一个数字，使得它的二进制表示中为1的位在原数字中都为0
# 4. 可以使用子集枚举或预处理来解决
# 时间复杂度: O(n * 2^k) 其中k是位数
# 空间复杂度: O(2^k)

# 常量定义
MAXN = 105      # 最大行数
MAXM = 15       # 最大列数
MAX_STATES = 1 << 10  # 最大状态数，2^10 = 1024
INF = float('inf')    # 无穷大常量

# POJ 1185 炮兵阵地 解法
def artillery_position(n, m, grid):
    """
    计算炮兵阵地问题的解法
    
    Args:
        n (int): 行数
        m (int): 列数
        grid (List[List[str]]): 二维列表，表示网格，'P'表示平原，'H'表示山地
    
    Returns:
        int: 最多能部署的炮兵数
    """
    # 预处理每一行的合法地形状态
    # valid_states[i] 表示第i行的地形状态，用二进制位表示哪些位置是平原（可以部署炮兵）
    valid_states = [0] * n
    for i in range(n):
        for j in range(m):
            if grid[i][j] == 'P':
                valid_states[i] |= (1 << j)  # 将第j位设为1，表示位置j是平原
    
    # 预处理所有可能的行状态（同一行内炮兵不互相攻击）
    # all_states列表存储所有合法的行状态
    # state_count列表存储每个状态对应的炮兵数量
    all_states = []
    state_count = []
    for i in range(1 << m):  # 枚举所有可能的行状态(0到2^m-1)
        # 检查同一行内炮兵是否互相攻击（距离小于等于2）
        # (i << 1) 检查相邻位置是否有炮兵
        # (i << 2) 检查相隔一个位置是否有炮兵
        # (i >> 1) 检查相邻位置是否有炮兵
        # (i >> 2) 检查相隔一个位置是否有炮兵
        if (i & (i << 1)) == 0 and (i & (i << 2)) == 0 and \
           (i & (i >> 1)) == 0 and (i & (i >> 2)) == 0:
            all_states.append(i)
            state_count.append(bin(i).count('1'))  # 计算该状态下的炮兵数量，即二进制中1的个数
    
    total_states = len(all_states)
    
    # dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
    # 使用-1表示状态不可达
    dp = [[[-1 for _ in range(MAX_STATES)] for _ in range(MAX_STATES)] for _ in range(n + 1)]
    dp[0][0][0] = 0  # 初始状态：处理第0行，前两行都无炮兵的状态下炮兵数为0
    
    # 状态转移过程
    for i in range(1, n + 1):
        # 枚举所有合法的行状态
        for j in range(total_states):
            state = all_states[j]
            count = state_count[j]  # 当前行部署的炮兵数量
            
            # 检查当前状态是否在当前行的合法地形内
            # 如果(state & valid_states[i - 1]) != state，说明state中有某些位置在地形上是山地
            if (state & valid_states[i - 1]) != state:
                continue
            
            # 枚举前两行的状态
            for mask1 in range(1 << m):
                # 如果前一行的状态不可达，跳过
                if dp[i - 1][mask1][0] == -1:
                    continue
                
                for mask2 in range(1 << m):
                    # 如果前两行的状态组合不可达，跳过
                    if dp[i - 1][mask1][mask2] == -1:
                        continue
                    
                    # 检查当前行与前一行、前两行是否冲突
                    # (state & mask1) == 0 表示当前行与前一行无上下相邻
                    # (state & mask2) == 0 表示当前行与前两行无上下相隔一个位置的冲突
                    if (state & mask1) == 0 and (state & mask2) == 0:
                        new_value = dp[i - 1][mask1][mask2] + count
                        # 更新最大炮兵数
                        if dp[i][state][mask1] < new_value:
                            dp[i][state][mask1] = new_value
    
    # 计算最终结果：遍历所有可能的状态组合，找到最大炮兵数
    result = 0
    for mask1 in range(1 << m):
        for mask2 in range(1 << m):
            if dp[n][mask1][mask2] > result:
                result = dp[n][mask1][mask2]
    return result

# 空间优化版本
def artillery_position_optimized(n, m, grid):
    """
    空间优化版本的炮兵阵地问题解法
    通过滚动数组优化空间复杂度，只使用三个二维数组
    
    Args:
        n (int): 行数
        m (int): 列数
        grid (List[List[str]]): 二维列表，表示网格，'P'表示平原，'H'表示山地
    
    Returns:
        int: 最多能部署的炮兵数
    """
    # 预处理每一行的合法地形状态
    valid_states = [0] * n
    for i in range(n):
        for j in range(m):
            if grid[i][j] == 'P':
                valid_states[i] |= (1 << j)
    
    # 预处理所有可能的行状态（同一行内炮兵不互相攻击）
    all_states = []
    state_count = []
    for i in range(1 << m):
        # 检查同一行内炮兵是否互相攻击（距离小于等于2）
        if (i & (i << 1)) == 0 and (i & (i << 2)) == 0 and \
           (i & (i >> 1)) == 0 and (i & (i >> 2)) == 0:
            all_states.append(i)
            state_count.append(bin(i).count('1'))  # 计算该状态下的炮兵数量
    
    total_states = len(all_states)
    
    # 空间优化的DP数组
    # 只需要保存当前行、前一行和前两行的状态，使用滚动数组优化空间
    prev2 = [[-1 for _ in range(MAX_STATES)] for _ in range(MAX_STATES)]  # 前两行状态
    prev1 = [[-1 for _ in range(MAX_STATES)] for _ in range(MAX_STATES)]  # 前一行状态
    current = [[-1 for _ in range(MAX_STATES)] for _ in range(MAX_STATES)] # 当前行状态
    prev2[0][0] = 0  # 初始状态
    
    # 状态转移过程
    for i in range(1, n + 1):
        # 初始化当前状态数组
        for x in range(MAX_STATES):
            for y in range(MAX_STATES):
                current[x][y] = -1
        
        # 枚举所有合法的行状态
        for j in range(total_states):
            state = all_states[j]
            count = state_count[j]
            
            # 检查当前状态是否在当前行的合法地形内
            if (state & valid_states[i - 1]) != state:
                continue
            
            # 枚举前两行的状态
            for mask1 in range(1 << m):
                if prev1[mask1][0] == -1:
                    continue
                
                for mask2 in range(1 << m):
                    if prev1[mask1][mask2] == -1:
                        continue
                    
                    # 检查当前行与前一行、前两行是否冲突
                    if (state & mask1) == 0 and (state & mask2) == 0:
                        new_value = prev1[mask1][mask2] + count
                        if current[state][mask1] < new_value:
                            current[state][mask1] = new_value
        
        # 交换数组，将current的值复制到prev1，prev1的值复制到prev2，为下一次迭代做准备
        prev2, prev1, current = prev1, current, prev2
    
    # 计算最终结果
    result = 0
    for mask1 in range(1 << m):
        for mask2 in range(1 << m):
            if prev1[mask1][mask2] > result:
                result = prev1[mask1][mask2]
    return result

# CodeForces 165E 最大兼容数对解法
def compatible_numbers(nums):
    """
    计算最大兼容数对
    对于每个数字，找到另一个数字，使得它们的按位与结果为0
    
    Args:
        nums (List[int]): 整数列表
    
    Returns:
        List[int]: 结果列表，每个元素表示对应位置数字的兼容数索引，-1表示不存在
    """
    n = len(nums)
    if n == 0:
        return []
    
    # 找到数组中的最大值
    max_val = max(nums)
    
    # 找到最大值的位数
    bits = 0
    while (1 << bits) <= max_val:
        bits += 1
    
    # 预处理每个数字的补集
    # complement字典存储数字到索引的映射
    complement = {}
    
    # 将数组中的数字存入complement字典
    for i in range(n):
        complement[nums[i]] = i
    
    # 结果列表，初始化为-1
    result = [-1] * n
    
    # 对每个数字寻找兼容数
    for i in range(n):
        num = nums[i]
        # 计算num的补集：((1 << bits) - 1) ^ num
        # ((1 << bits) - 1) 生成一个bits位全为1的数
        # ^ num 表示与num进行异或运算，得到补集
        mask = ((1 << bits) - 1) ^ num
        
        # 枚举num的补集的所有子集
        # sub_mask = (sub_mask - 1) & mask 是枚举子集的标准写法
        sub_mask = mask
        while sub_mask > 0:
            if sub_mask in complement:
                result[i] = complement[sub_mask]
                break
            sub_mask = (sub_mask - 1) & mask
    
    return result

# 测试方法
if __name__ == "__main__":
    # 测试 POJ 1185 炮兵阵地
    grid1 = [
        ['P', 'H', 'P', 'P', 'P'],
        ['P', 'P', 'P', 'H', 'P'],
        ['P', 'H', 'P', 'P', 'P'],
        ['P', 'P', 'P', 'P', 'P'],
        ['P', 'H', 'P', 'P', 'P']
    ]
    print("POJ 1185 炮兵阵地 测试:")
    print("结果:", artillery_position(5, 5, grid1))
    print("优化版结果:", artillery_position_optimized(5, 5, grid1))
    
    # 测试 CodeForces 165E 最大兼容数对
    nums1 = [3, 1, 4, 2]
    print("\nCodeForces 165E 最大兼容数对 测试:")
    print("数组:", nums1)
    result1 = compatible_numbers(nums1)
    print("结果:", result1)