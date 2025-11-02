# 阶梯博弈 (Staircase Nim)
# 阶梯博弈是Nim游戏的一个重要变种，有着不同的游戏规则和胜负判定
# 游戏规则：
# 1. 游戏在一个由n个阶梯组成的楼梯上进行
# 2. 每个阶梯上有一定数量的石子
# 3. 玩家轮流进行操作，可以选择一个阶梯i上的若干个石子（至少1个）
# 4. 将选中的石子移动到阶梯i-1上（如果i=1，则石子被移出游戏）
# 5. 无法进行操作的玩家输
# 
# 算法思路：
# 1. 阶梯博弈可以转换为Nim游戏：只需要考虑奇数位置的石子数量
# 2. 胜负判定规则：将所有奇数位置的石子数进行异或操作，如果结果不为0，则先手必胜；否则先手必败
# 3. 这个结论的正确性基于游戏的对称性和必胜策略的构造
# 
# 时间复杂度：O(n) - 只需要遍历一次阶梯，计算奇数位置石子数的异或和
# 空间复杂度：O(1) - 只需要常数额外空间
#
# 适用场景和解题技巧：
# 1. 适用场景：
#    - 资源迁移类游戏
#    - 具有层次结构的博弈问题
#    - 需要将复杂博弈转换为Nim游戏的情况
# 2. 解题技巧：
#    - 识别问题是否符合阶梯博弈模型
#    - 确定哪些位置是关键位置（通常是奇数位置）
#    - 应用Nim游戏的胜负判定规则
# 3. 数学意义：
#    - 展示了博弈论中的简化思想
#    - 利用对称性和不变量解决复杂问题

def can_win_staircase_nim(stairs):
    """
    判断阶梯博弈的先手是否有必胜策略
    
    时间复杂度: O(n) - 只需要遍历一次阶梯，计算奇数位置石子数的异或和
    空间复杂度: O(1) - 只需要常数额外空间
    
    参数:
        stairs: 表示每个阶梯上的石子数量的列表，stairs[i]表示第i+1个阶梯上的石子数
    返回:
        bool: 如果先手有必胜策略，返回True；否则返回False
    """
    # 参数校验
    if stairs is None or len(stairs) == 0:
        # 没有阶梯，先手无法操作，必输
        return False
    
    # 计算所有奇数位置的石子数的异或和
    xor_sum = 0
    for i in range(len(stairs)):
        # 注意：这里的索引i对应阶梯i+1（因为列表从0开始）
        # 所以当i+1为奇数时（即i为偶数时），需要计算异或和
        if i % 2 == 0:
            xor_sum ^= stairs[i]
    
    # 如果异或和不为0，先手必胜；否则先手必败
    return xor_sum != 0

def find_winning_move(stairs):
    """
    寻找阶梯博弈中的必胜策略
    
    时间复杂度: O(n) - 需要遍历所有奇数位置的阶梯
    空间复杂度: O(1) - 只需要常数额外空间
    
    参数:
        stairs: 当前每个阶梯上的石子数量列表
    返回:
        tuple: 如果存在必胜策略，返回一个元组(from_stair, stones_to_move)，
               其中from_stair是源阶梯索引(0-based)，stones_to_move是移动的石子数；
               如果不存在必胜策略，返回None
    """
    # 参数校验
    if stairs is None or len(stairs) == 0:
        return None
    
    xor_sum = 0
    for i in range(len(stairs)):
        if i % 2 == 0:
            xor_sum ^= stairs[i]
    
    # 如果异或和为0，没有必胜策略
    if xor_sum == 0:
        return None
    
    # 寻找可以进行的必胜操作
    for i in range(len(stairs)):
        # 只考虑奇数位置
        if i % 2 == 0:
            # 计算需要将当前阶梯的石子数变为多少才能使异或和为0
            target = stairs[i] ^ xor_sum
            
            # 如果target小于当前石子数，说明可以通过移动石子来达到目标
            if target < stairs[i]:
                stones_to_move = stairs[i] - target
                return (i, stones_to_move)
    
    # 理论上不应该到达这里，因为如果xor_sum不为0，必定存在必胜策略
    return None

def make_move(stairs, from_stair, stones_to_move):
    """
    模拟执行移动操作
    
    时间复杂度: O(n) - 需要复制整个阶梯状态数组
    空间复杂度: O(n) - 需要创建新的数组存储状态
    
    参数:
        stairs: 当前阶梯状态列表
        from_stair: 源阶梯索引（0-based）
        stones_to_move: 移动的石子数量
    返回:
        list: 执行移动后的新阶梯状态
    异常:
        ValueError: 当移动操作无效时抛出
    """
    if stairs is None or from_stair < 0 or from_stair >= len(stairs) or \
       stones_to_move <= 0 or stones_to_move > stairs[from_stair]:
        raise ValueError("无效的移动操作")
    
    # 创建新的状态数组
    new_stairs = stairs.copy()
    
    # 从源阶梯移除石子
    new_stairs[from_stair] -= stones_to_move
    
    # 如果不是最底部的阶梯，将石子移动到下一个阶梯
    if from_stair > 0:
        new_stairs[from_stair - 1] += stones_to_move
    
    return new_stairs

def print_stairs(stairs):
    """
    打印阶梯状态
    
    参数:
        stairs: 阶梯状态列表
    """
    print("当前阶梯状态：")
    # 从顶部到底部打印阶梯
    for i in range(len(stairs) - 1, -1, -1):
        print(f"阶梯 {i + 1}: {stairs[i]} 个石子")
    print()

def test_staircase_nim():
    """
    测试阶梯博弈的各种情况
    """
    # 测试用例1：先手必胜的情况
    # 阶梯1有3个石子，阶梯2有1个石子，阶梯3有4个石子
    # 奇数位置（阶梯1和阶梯3）的异或和：3 ^ 4 = 7 != 0，所以先手必胜
    stairs1 = [3, 1, 4]
    print("测试用例1：")
    print_stairs(stairs1)
    print(f"先手{'有' if can_win_staircase_nim(stairs1) else '无'}必胜策略")
    
    winning_move1 = find_winning_move(stairs1)
    if winning_move1 is not None:
        print(f"必胜策略：从阶梯 {winning_move1[0] + 1} 移动 {winning_move1[1]} 个石子到阶梯 {winning_move1[0]}")
        new_stairs1 = make_move(stairs1, winning_move1[0], winning_move1[1])
        print("移动后的状态：")
        print_stairs(new_stairs1)
        print(f"此时后手{'有' if can_win_staircase_nim(new_stairs1) else '无'}必胜策略")
    
    # 测试用例2：先手必败的情况
    # 阶梯1有1个石子，阶梯2有2个石子，阶梯3有1个石子
    # 奇数位置（阶梯1和阶梯3）的异或和：1 ^ 1 = 0，所以先手必败
    stairs2 = [1, 2, 1]
    print("\n测试用例2：")
    print_stairs(stairs2)
    print(f"先手{'有' if can_win_staircase_nim(stairs2) else '无'}必胜策略")
    
    # 测试用例3：空阶梯
    stairs3 = []
    print("\n测试用例3：")
    print_stairs(stairs3)
    print(f"先手{'有' if can_win_staircase_nim(stairs3) else '无'}必胜策略")
    
    # 测试用例4：只有一个阶梯
    stairs4 = [5]
    print("\n测试用例4：")
    print_stairs(stairs4)
    print(f"先手{'有' if can_win_staircase_nim(stairs4) else '无'}必胜策略")
    
    # 测试用例5：包含零的阶梯
    stairs5 = [0, 3, 0, 4]
    print("\n测试用例5：")
    print_stairs(stairs5)
    print(f"先手{'有' if can_win_staircase_nim(stairs5) else '无'}必胜策略")
    
    # 测试异常处理
    try:
        invalid_move = make_move(stairs1, 0, 4)  # 移动的石子数超过了阶梯上的石子数
    except ValueError as e:
        print(f"\n异常处理测试：成功捕获异常 - {e}")

# 运行测试
if __name__ == "__main__":
    test_staircase_nim()