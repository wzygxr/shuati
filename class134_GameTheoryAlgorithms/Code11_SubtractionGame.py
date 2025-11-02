# 减法游戏 (Subtraction Game)
# 减法游戏是取石子游戏的一个通用变种，也称为Take-away Game
# 游戏规则：
# 1. 有一堆石子，数量为n
# 2. 玩家轮流从堆中取石子，每次可以取的石子数必须属于一个给定的集合S
# 3. 无法取石子的玩家输
# 
# 算法思路：
# 1. 使用动态规划计算每个石子数量对应的必胜态（winning position）和必败态（losing position）
# 2. dp[i] = True 表示当石子数为i时，当前玩家处于必胜态
# 3. 状态转移方程：dp[i] = 存在某个s∈S，使得 i >= s 且 dp[i-s] = False
# 4. 边界条件：dp[0] = False（没有石子时，当前玩家无法操作，必败）
# 
# 时间复杂度：O(n*k)，其中n是石子数量上限，k是集合S的大小
# 空间复杂度：O(n)，用于存储dp数组
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 具有特定移动规则的取石子游戏
#    - 需要预处理所有可能状态的博弈问题
#    - 可以作为其他复杂博弈问题的子问题
# 2. 解题技巧：
#    - 识别问题是否符合减法游戏模型
#    - 确定允许的移动集合S
#    - 通过动态规划预处理所有可能的状态
# 3. 变种和扩展：
#    - 标准巴什博弈是减法游戏的特例，其中S = {1, 2, ..., m}
#    - 可以扩展到多堆石子的情况，结合SG函数进行分析


def calculate_winning_positions(max_n, moves):
    """
    计算减法游戏中每个石子数量对应的胜负状态
    
    参数:
        max_n: 最大石子数量
        moves: 允许的移动集合，表示每次可以取的石子数
    
    返回:
        一个布尔列表，dp[i]表示石子数为i时是否为必胜态
    
    异常:
        ValueError: 当参数无效时抛出
    """
    # 参数校验
    if max_n < 0:
        raise ValueError("最大石子数量不能为负数")
    if not moves:
        raise ValueError("移动集合不能为空")
    
    # 确保移动集合中的元素都是正整数且不重复
    move_set = set()
    for move in moves:
        if move <= 0:
            raise ValueError("移动集合中的元素必须为正整数")
        move_set.add(move)
    
    # 排序以优化性能
    sorted_moves = sorted(move_set)
    
    # 初始化dp数组
    dp = [False] * (max_n + 1)
    dp[0] = False  # 边界条件：0个石子时必败
    
    # 动态规划计算每个状态
    for i in range(1, max_n + 1):
        can_win = False
        # 尝试所有可能的移动
        for move in sorted_moves:
            if move > i:
                # 当前移动需要的石子数超过了现有石子数，无法进行
                break  # 由于已排序，可以提前退出
            # 如果存在某个移动，使得对手处于必败态，则当前状态为必胜态
            if not dp[i - move]:
                can_win = True
                break  # 找到一个必胜策略即可退出
        dp[i] = can_win
    
    return dp


def can_win(n, moves):
    """
    判断在给定石子数和移动集合的情况下，当前玩家是否有必胜策略
    
    参数:
        n: 当前石子数量
        moves: 允许的移动集合
    
    返回:
        如果当前玩家有必胜策略，返回True；否则返回False
    
    异常:
        ValueError: 当参数无效时抛出
    """
    # 参数校验
    if n < 0:
        raise ValueError("石子数量不能为负数")
    
    # 计算胜负状态
    dp = calculate_winning_positions(n, moves)
    return dp[n]


def find_winning_move(n, moves):
    """
    寻找当前状态下的必胜策略
    
    参数:
        n: 当前石子数量
        moves: 允许的移动集合
    
    返回:
        如果存在必胜策略，返回一个可以取的石子数；否则返回-1
    
    异常:
        ValueError: 当参数无效时抛出
    """
    # 参数校验
    if n < 0:
        raise ValueError("石子数量不能为负数")
    if not moves:
        raise ValueError("移动集合不能为空")
    
    # 确保移动集合中的元素都是正整数且不重复
    move_set = {move for move in moves if move > 0}
    
    # 尝试所有可能的移动
    for move in move_set:
        if move <= n:
            # 检查取走move个石子后，对手是否处于必败态
            dp = calculate_winning_positions(n - move, moves)
            if not dp[n - move]:
                return move  # 找到一个必胜策略
    
    return -1  # 不存在必胜策略


def calculate_sg(max_n, moves):
    """
    计算SG函数值
    
    参数:
        max_n: 最大石子数量
        moves: 允许的移动集合
    
    返回:
        一个整数列表，sg[i]表示石子数为i时的SG函数值
    
    异常:
        ValueError: 当参数无效时抛出
    """
    # 参数校验
    if max_n < 0:
        raise ValueError("最大石子数量不能为负数")
    if not moves:
        raise ValueError("移动集合不能为空")
    
    # 确保移动集合中的元素都是正整数且不重复
    move_set = {move for move in moves if move > 0}
    
    # 排序以优化性能
    sorted_moves = sorted(move_set)
    
    # 初始化SG数组
    sg = [0] * (max_n + 1)
    sg[0] = 0  # 边界条件：0个石子时SG值为0
    
    # 计算每个状态的SG值
    for i in range(1, max_n + 1):
        reachable_sg = set()
        # 收集所有可达状态的SG值
        for move in sorted_moves:
            if move <= i:
                reachable_sg.add(sg[i - move])
        # 找到最小的未出现的非负整数
        mex = 0  # mex表示最小非负整数
        while mex in reachable_sg:
            mex += 1
        sg[i] = mex
    
    return sg


def print_winning_table(dp):
    """
    打印胜负状态表
    
    参数:
        dp: 胜负状态列表
    """
    print("石子数\t状态")
    print("----\t----")
    for i in range(len(dp)):
        print(f"{i}\t{'必胜态' if dp[i] else '必败态'}")


def print_sg_table(sg):
    """
    打印SG函数值表
    
    参数:
        sg: SG函数值列表
    """
    print("石子数\tSG值")
    print("----\t----")
    for i in range(len(sg)):
        print(f"{i}\t{sg[i]}")


def test_subtraction_game():
    """
    测试减法游戏的功能
    """
    # 测试用例1：标准巴什博弈，每次可以取1-3个石子
    print("测试用例1：标准巴什博弈（每次取1-3个石子）")
    moves1 = [1, 2, 3]
    max_n1 = 10
    dp1 = calculate_winning_positions(max_n1, moves1)
    print_winning_table(dp1)
    
    # 测试特定石子数的胜负状态
    n1 = 4
    print(f"\n石子数为{n1}时，{'先手必胜' if can_win(n1, moves1) else '先手必败'}")
    winning_move1 = find_winning_move(n1, moves1)
    if winning_move1 != -1:
        print(f"必胜策略：取{winning_move1}个石子")
    else:
        print("无必胜策略")
    
    # 计算SG函数值
    sg1 = calculate_sg(max_n1, moves1)
    print("\nSG函数值表：")
    print_sg_table(sg1)
    
    # 测试用例2：只能取奇数个石子
    print("\n\n测试用例2：只能取1、3、5个石子")
    moves2 = [1, 3, 5]
    max_n2 = 10
    dp2 = calculate_winning_positions(max_n2, moves2)
    print_winning_table(dp2)
    
    # 测试用例3：只能取2的幂次方个石子
    print("\n\n测试用例3：只能取1、2、4、8个石子（2的幂次方）")
    moves3 = [1, 2, 4, 8]
    max_n3 = 10
    dp3 = calculate_winning_positions(max_n3, moves3)
    print_winning_table(dp3)
    
    # 测试用例4：异常处理测试
    print("\n\n测试用例4：异常处理")
    try:
        calculate_winning_positions(-1, [1, 2])
    except ValueError as e:
        print(f"预期的异常：{e}")
    
    try:
        calculate_winning_positions(5, [])
    except ValueError as e:
        print(f"预期的异常：{e}")
    
    try:
        calculate_winning_positions(5, [0, 1])
    except ValueError as e:
        print(f"预期的异常：{e}")


if __name__ == "__main__":
    test_subtraction_game()