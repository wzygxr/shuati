# Chomp游戏 (Chomp Game)
# Chomp是一个经典的公平组合游戏，通常用巧克力块来描述
# 游戏规则：
# 1. 游戏在一个m×n的矩形巧克力板上进行
# 2. 玩家轮流选择一个巧克力块(x,y)，并吃掉该块及其右下角的所有巧克力块
# 3. 左上角的巧克力块(1,1)是有毒的，吃到它的人输
# 
# 算法思路：
# 1. 数学定理：对于任何大小m×n的巧克力板(m,n>1)，先手都有必胜策略
# 2. 这个定理是非构造性的，它证明了必胜策略的存在，但没有给出具体如何操作
# 3. 实际实现中，我们可以使用动态规划或记忆化搜索来求解具体的必胜态
# 4. 使用位掩码表示棋盘状态，或者使用二维数组表示
# 
# 时间复杂度：O(2^(m*n)) - 最坏情况下需要遍历所有可能的状态
# 空间复杂度：O(2^(m*n)) - 存储所有状态的胜负情况
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 组合博弈理论研究
#    - 棋盘覆盖问题
#    - 非构造性证明的示例
# 2. 解题技巧：
#    - 对于小棋盘，可以使用记忆化搜索枚举所有可能的移动
#    - 对于大棋盘，利用对称性或其他性质寻找规律
#    - 利用Sprague-Grundy定理分析游戏状态
# 3. 数学意义：
#    - 说明了存在性证明和构造性证明的区别
#    - 在策梅洛定理的应用实例

from functools import lru_cache

# 检查当前状态是否为必败态
def is_losing_position(m, n):
    """
    根据Chomp游戏的定理，判断当前位置是否为必败态
    时间复杂度: O(1)
    空间复杂度: O(1)
    
    参数:
        m: 棋盘的行数
        n: 棋盘的列数
    返回:
        bool: True表示当前玩家必输，False表示当前玩家有必胜策略
    """
    # 只有1×1的棋盘，先手必输
    return m == 1 and n == 1

# 对于小棋盘的具体实现，使用记忆化搜索
def can_win(m, n):
    """
    判断在m×n的棋盘上，当前玩家是否有必胜策略
    时间复杂度: O(2^(m*n)) - 最坏情况下需要遍历所有可能的状态
    空间复杂度: O(2^(m*n)) - 存储所有状态的胜负情况
    
    参数:
        m: 棋盘的行数
        n: 棋盘的列数
    返回:
        bool: True表示当前玩家有必胜策略，False表示必输
    """
    # 1×1的棋盘，当前玩家必输
    if m == 1 and n == 1:
        return False
    
    # 创建初始棋盘状态
    # board[i][j] 表示位置(i,j)的巧克力是否还在
    board = [[True for _ in range(n)] for _ in range(m)]
    
    # 使用辅助函数进行记忆化搜索
    return can_win_helper(board, m, n)

# 记忆化搜索的辅助函数，使用元组表示棋盘状态以便缓存
def can_win_helper(board, m, n):
    """
    使用记忆化搜索判断当前棋盘状态下玩家是否有必胜策略
    
    参数:
        board: 当前棋盘状态
        m: 棋盘的行数
        n: 棋盘的列数
    返回:
        bool: True表示当前玩家有必胜策略，False表示必输
    """
    # 将棋盘状态转换为元组以便缓存
    board_tuple = tuple(tuple(row) for row in board)
    
    # 使用lru_cache装饰器会更高效，但需要函数参数可哈希
    # 这里为了清晰展示递归过程，手动实现
    
    # 尝试所有可能的移动
    for i in range(m):
        for j in range(n):
            # 只有巧克力存在的位置才能被选择
            if board[i][j]:
                # 创建新的棋盘状态
                new_board = [row.copy() for row in board]
                
                # 吃掉该位置及其右下角的所有巧克力
                for x in range(i, m):
                    for y in range(j, n):
                        new_board[x][y] = False
                
                # 检查左上角是否被吃掉（此时游戏结束，当前玩家获胜）
                if not new_board[0][0]:
                    return True
                
                # 如果对手处于必败态，则当前玩家必胜
                if not can_win_helper(new_board, m, n):
                    return True
    
    # 所有可能的移动都导致对手获胜，当前玩家必败
    return False

# 使用装饰器优化的版本
def can_win_optimized(m, n):
    """
    使用lru_cache优化的版本
    时间复杂度: O(2^(m*n)) - 最坏情况下
    空间复杂度: O(2^(m*n)) - 存储所有状态
    
    参数:
        m: 棋盘的行数
        n: 棋盘的列数
    返回:
        bool: True表示当前玩家有必胜策略，False表示必输
    """
    # 1×1的棋盘，当前玩家必输
    if m == 1 and n == 1:
        return False
    
    # 创建初始棋盘状态
    board = tuple(tuple(True for _ in range(n)) for _ in range(m))
    
    return can_win_helper_optimized(board, m, n)

@lru_cache(maxsize=None)
def can_win_helper_optimized(board, m, n):
    """
    使用lru_cache装饰器优化的记忆化搜索
    
    参数:
        board: 当前棋盘状态（元组形式）
        m: 棋盘的行数
        n: 棋盘的列数
    返回:
        bool: True表示当前玩家有必胜策略，False表示必输
    """
    # 将元组转换为列表以便修改
    board_list = [list(row) for row in board]
    
    # 尝试所有可能的移动
    for i in range(m):
        for j in range(n):
            if board_list[i][j]:
                # 创建新的棋盘状态
                new_board = [row.copy() for row in board_list]
                
                # 吃掉该位置及其右下角的所有巧克力
                for x in range(i, m):
                    for y in range(j, n):
                        new_board[x][y] = False
                
                # 检查左上角是否被吃掉
                if not new_board[0][0]:
                    return True
                
                # 转换为元组以便缓存
                new_board_tuple = tuple(tuple(row) for row in new_board)
                
                # 如果对手处于必败态，则当前玩家必胜
                if not can_win_helper_optimized(new_board_tuple, m, n):
                    return True
    
    # 所有可能的移动都导致对手获胜，当前玩家必败
    return False

# 2×n棋盘的必胜策略
def can_win_2xn(n):
    """
    2×n棋盘的必胜策略分析
    时间复杂度: O(1)
    空间复杂度: O(1)
    
    参数:
        n: 棋盘的列数
    返回:
        bool: True表示先手有必胜策略，False表示必输
    """
    # 根据定理，任何2×n(n>1)的棋盘，先手都有必胜策略
    return n > 1

# 3×n棋盘的分析
def analyze_3xn(max_n=10):
    """
    分析3×n棋盘的胜负情况（仅适用于小n）
    
    参数:
        max_n: 分析的最大列数
    """
    print(f"3×n棋盘的胜负情况分析（基于小n的计算，最多到3×{max_n}）：")
    for n in range(1, max_n + 1):
        # 对于较大的n，使用优化版本
        if n <= 3:  # 小n使用原始版本
            result = can_win(3, n)
        else:  # 较大的n使用优化版本
            result = can_win_optimized(3, n)
        print(f"3×{n}棋盘，先手{'有' if result else '无'}必胜策略")

# 测试函数
def test_chomp_game():
    """
    测试Chomp游戏的各种情况
    """
    print("Chomp游戏定理测试：")
    print(f"1×1棋盘，先手必输: {is_losing_position(1, 1)}")
    print(f"1×2棋盘，先手必输: {is_losing_position(1, 2)}")  # 这里应该返回False
    print(f"2×2棋盘，先手必输: {is_losing_position(2, 2)}")  # 这里应该返回False
    
    print("\n小棋盘具体计算结果：")
    print(f"2×2棋盘，先手{'有' if can_win(2, 2) else '无'}必胜策略")
    print(f"2×3棋盘，先手{'有' if can_win(2, 3) else '无'}必胜策略")
    print(f"3×3棋盘，先手{'有' if can_win(3, 3) else '无'}必胜策略")
    
    print("\n使用优化版本计算：")
    print(f"2×2棋盘，先手{'有' if can_win_optimized(2, 2) else '无'}必胜策略")
    print(f"2×3棋盘，先手{'有' if can_win_optimized(2, 3) else '无'}必胜策略")
    
    print("\n2×n棋盘分析：")
    for n in range(1, 6):
        print(f"2×{n}棋盘，先手{'有' if can_win_2xn(n) else '无'}必胜策略")
    
    print("\n3×n棋盘分析（可能需要较长时间）：")
    try:
        analyze_3xn(5)  # 限制为5以避免计算时间过长
    except Exception as e:
        print(f"分析过程中发生错误：{e}")

# 运行测试
if __name__ == "__main__":
    test_chomp_game()