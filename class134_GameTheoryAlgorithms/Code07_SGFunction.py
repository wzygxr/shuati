# SG函数 (Sprague-Grundy定理) 实现
# 公平组合游戏(Impartial Game)的通用解法
# 任何公平组合游戏都可以转化为尼姆堆，通过计算每个子游戏的SG值
# 然后将这些SG值异或起来，若结果非零则先手必胜，否则必败
# 
# 算法思路：
# 1. SG函数是对游戏状态的一种抽象表示
# 2. 对于每个状态x，SG(x) = mex{ SG(y) | y是x的后继状态 }
# 3. mex(最小非负整数)函数返回不属于集合中的最小非负整数
# 4. Sprague-Grundy定理：多个独立的子游戏的组合的SG值等于各子游戏SG值的异或和
# 5. 当且仅当组合游戏的SG值不为0时，当前玩家处于必胜态
# 
# 时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
# 空间复杂度：O(n) - 存储SG值的数组
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 公平组合游戏（双方可执行相同操作，游戏状态无差别）
#    - 有确定的终止状态
#    - 每个状态可以转移到有限个其他状态
# 2. 解题技巧：
#    - 确定游戏的状态表示方法
#    - 找出每个状态的所有可能转移
#    - 自底向上计算SG函数值
#    - 利用异或和判断胜负
# 3. 经典应用：
#    - 取石子游戏的变种
#    - 棋盘游戏
#    - 图游戏
#
# 相关题目链接：
# 1. 洛谷 P2197: https://www.luogu.com.cn/problem/P2197
# 2. HDU 1850: http://acm.hdu.edu.cn/showproblem.php?pid=1850
# 3. POJ 2234: http://poj.org/problem?id=2234

# 预处理SG函数值
# 参数说明：
# - n: 最大状态数
# - moves: 可以进行的移动（比如每次可以取1,2,3个石子）
def precompute_sg(n, moves):
    """
    预处理SG函数值
    
    参数说明：
    - n: 最大状态数
    - moves: 可以进行的移动（比如每次可以取1,2,3个石子）
    
    返回:
    - sg: SG函数值数组
    
    算法思路：
    1. 对于每个状态i，计算其所有后继状态的SG值
    2. 使用mex函数找出最小的不属于后继状态SG值集合的非负整数
    3. 该值即为状态i的SG值
    
    时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
    空间复杂度：O(n) - 存储SG值的数组
    """
    # 存储SG函数值
    sg = [0] * (n + 1)
    
    # 自底向上计算每个状态的SG值
    for i in range(1, n + 1):
        # 标记所有后继状态的SG值
        visited = set()
        
        # 遍历所有可能的移动
        for move in moves:
            if i >= move:
                visited.add(sg[i - move])
        
        # 计算mex值
        mex = 0
        while mex in visited:
            mex += 1
        sg[i] = mex
    
    return sg

# 判断当前玩家是否必胜
# 参数说明：
# - piles: 各堆石子的数量（或各个子游戏的状态）
# - moves: 可以进行的移动
def is_winning_position(piles, moves):
    """
    判断当前玩家是否必胜
    
    参数说明：
    - piles: 各堆石子的数量（或各个子游戏的状态）
    - moves: 可以进行的移动
    
    返回:
    - bool: True表示当前玩家必胜，False表示必败
    
    算法思路：
    1. 预处理SG函数值到最大堆的大小
    2. 计算所有堆的SG值异或和
    3. 异或和不为0则先手必胜，否则必败
    
    时间复杂度：O(n * m) - n是状态数，m是每个状态的后继状态数
    空间复杂度：O(n) - 存储SG值的数组
    """
    # 预处理SG函数值到最大堆的大小
    max_pile = max(piles)
    sg = precompute_sg(max_pile, moves)
    
    # 计算所有堆的SG值异或和
    xor_sum = 0
    for pile in piles:
        xor_sum ^= sg[pile]
    
    # 异或和不为0则先手必胜
    return xor_sum != 0

# 示例：取石子游戏变种 - 每次可以取1、2、4个石子
# 测试方法
def main():
    # 测试用例1: 巴什博弈变种 - 每次可以取1、2、4个石子
    moves1 = [1, 2, 4]
    piles1 = [5, 7, 9]
    print("测试用例1 - 取石子游戏变种（每次取1、2、4个）:")
    print(f"各堆石子数: {piles1}")
    print(f"先手是否必胜: {'是' if is_winning_position(piles1, moves1) else '否'}")
    
    # 测试用例2: 标准巴什博弈 - 每次可以取1-3个石子
    moves2 = [1, 2, 3]
    piles2 = [4, 4, 4]
    print("\n测试用例2 - 标准巴什博弈（每次取1-3个）:")
    print(f"各堆石子数: {piles2}")
    print(f"先手是否必胜: {'是' if is_winning_position(piles2, moves2) else '否'}")
    
    # 测试用例3: 斐波那契游戏的SG函数分析
    moves3 = [1, 2]  # 简化版本，实际斐波那契游戏规则更复杂
    piles3 = [5]  # 5是斐波那契数，应该是必败态
    print("\n测试用例3 - 斐波那契游戏简化版:")
    print(f"石子数: {piles3}")
    print(f"先手是否必胜: {'是' if is_winning_position(piles3, moves3) else '否'}")

if __name__ == "__main__":
    main()