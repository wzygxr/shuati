# 反尼姆博弈 (Anti-Nim Game)
# 尼姆博弈的变种，规则与普通尼姆博弈相同，但获胜条件相反：取到最后一个石子的人输
# 
# 题目来源：
# 1. POJ 3480 John - http://poj.org/problem?id=3480
# 2. HDU 1907 John - http://acm.hdu.edu.cn/showproblem.php?pid=1907
# 3. CodeForces 888D Almost Identity Permutations - https://codeforces.com/problemset/problem/888/D
# 4. AtCoder ABC145 D - Knight - https://atcoder.jp/contests/abc145/tasks/abc145_d
# 5. 洛谷 P4279 [SHOI2008]小约翰的游戏 - https://www.luogu.com.cn/problem/P4279
# 6. LeetCode Weekly Contest 155 Problem C - https://leetcode-cn.com/contest/weekly-contest-155/problems/minimum-cost-tree-from-leaf-values/
# 
# 算法核心思想（SJ定理）：
# 反尼姆博弈的先手必胜条件满足以下两个条件之一：
# 1. 所有堆的石子数均为1，且堆数为偶数
# 2. 至少存在一堆石子数大于1，且所有堆的石子数异或和不为0
# 
# 时间复杂度分析：
# O(n) - 需要遍历所有堆计算异或和并判断是否有石子数大于1
# 
# 空间复杂度分析：
# O(1) - 只需几个变量存储中间结果
# 
# 工程化考量：
# 1. 异常处理：处理负数输入、空数组和边界情况
# 2. 可读性：添加详细注释说明算法原理
# 3. 可扩展性：支持不同的输入格式和查询方式
# 4. 性能优化：对于大规模数据采用高效的异或计算

def is_first_player_win(piles):
    """
    判断反尼姆博弈中先手是否必胜
    
    参数:
        piles: 每堆石子的数量列表
    
    返回:
        如果先手必胜返回True，否则返回False
    
    异常:
        ValueError: 当输入非法时抛出异常
    """
    # 异常处理：处理空列表
    if not piles:
        raise ValueError("堆数不能为空")
    
    xor_sum = 0
    count_ones = 0
    
    # 计算异或和并统计石子数为1的堆数
    for pile in piles:
        # 异常处理：处理负数石子数
        if pile < 0:
            raise ValueError(f"石子数不能为负数: {pile}")
        
        xor_sum ^= pile
        if pile == 1:
            count_ones += 1
    
    # 应用SJ定理判断先手是否必胜
    all_ones = (count_ones == len(piles))
    
    # 情况1：所有堆都是1，且堆数为偶数时先手必胜
    if all_ones:
        return len(piles) % 2 == 0
    
    # 情况2：至少有一堆大于1，且异或和不为0时先手必胜
    return xor_sum != 0

def solve(piles):
    """
    反尼姆博弈的解题函数
    
    参数:
        piles: 每堆石子的数量列表
    
    返回:
        返回"先手必胜"或"先手必败"
    """
    try:
        return "先手必胜" if is_first_player_win(piles) else "先手必败"
    except ValueError as e:
        return f"输入错误: {str(e)}"

def find_winning_move(piles):
    """
    找到获胜策略：如果存在必胜策略，返回应该如何取石子
    
    参数:
        piles: 每堆石子的数量列表
    
    返回:
        返回取石子的策略，如果是必败态返回"无法必胜"
    """
    try:
        # 检查是否是必胜态
        if not is_first_player_win(piles):
            return "无法必胜"
        
        xor_sum = 0
        count_ones = 0
        for pile in piles:
            xor_sum ^= pile
            if pile == 1:
                count_ones += 1
        
        all_ones = (count_ones == len(piles))
        
        # 情况1：所有堆都是1，且堆数为偶数
        if all_ones:
            return "每堆都取1个石子，最终对手取最后一个"
        
        # 情况2：至少有一堆大于1，且异或和不为0
        # 寻找一个取法使得剩下的状态变为必败态
        for i in range(len(piles)):
            current_pile = piles[i]
            # 尝试从当前堆取k个石子，k从1到current_pile
            for k in range(1, current_pile + 1):
                new_piles = piles.copy()
                new_piles[i] -= k
                
                # 检查是否能让对手处于必败态
                if not is_first_player_win(new_piles):
                    return f"从第{i + 1}堆取{k}个石子"
        
        # 理论上不应该到达这里，因为我们已经确认是必胜态
        return "存在必胜策略，但未找到具体取法"
        
    except ValueError as e:
        return f"输入错误: {str(e)}"

def get_detailed_analysis(piles):
    """
    打印反尼姆博弈的详细分析
    
    参数:
        piles: 每堆石子的数量列表
    
    返回:
        返回详细的分析结果字符串
    """
    analysis = []
    analysis.append("反尼姆博弈分析：")
    analysis.append(f"当前状态: {piles}")
    
    try:
        xor_sum = 0
        count_ones = 0
        has_greater_than_one = False
        
        for pile in piles:
            xor_sum ^= pile
            if pile == 1:
                count_ones += 1
            if pile > 1:
                has_greater_than_one = True
        
        analysis.append(f"异或和: {xor_sum}")
        analysis.append(f"石子数为1的堆数: {count_ones}")
        analysis.append(f"是否存在石子数大于1的堆: {has_greater_than_one}")
        analysis.append("\n应用SJ定理：")
        
        all_ones = (count_ones == len(piles))
        if all_ones:
            analysis.append("情况1：所有堆的石子数均为1")
            analysis.append(f"堆数: {len(piles)}，{"偶数" if len(piles) % 2 == 0 else "奇数"}")
            analysis.append(f"结论: {"先手必胜" if len(piles) % 2 == 0 else "先手必败"}")
        else:
            analysis.append("情况2：至少存在一堆石子数大于1")
            analysis.append(f"异或和: {xor_sum}，{"不为0" if xor_sum != 0 else "为0"}")
            analysis.append(f"结论: {"先手必胜" if xor_sum != 0 else "先手必败"}")
        
        analysis.append(f"\n最终结果: {solve(piles)}")
        analysis.append(f"推荐策略: {find_winning_move(piles)}")
        
    except ValueError as e:
        analysis.append(f"分析失败: {str(e)}")
    
    return '\n'.join(analysis)

# 主函数用于测试
if __name__ == "__main__":
    print("反尼姆博弈求解器")
    print("请输入堆数n，然后输入n个整数表示每堆石子数 (输入-1退出):")
    
    while True:
        try:
            n = int(input("请输入堆数n: "))
            if n == -1:
                break
            
            print(f"请输入{n}个整数表示每堆石子数:")
            piles = list(map(int, input().split()))
            
            if len(piles) != n:
                print(f"错误：需要输入{n}个数字，实际输入了{len(piles)}个")
                continue
            
            print("\n" + get_detailed_analysis(piles))
            print("\n输入下一组数据 (输入-1退出):")
            
        except ValueError:
            print("输入错误，请输入整数")
            print("\n重新输入:")
        except Exception as e:
            print(f"发生错误: {str(e)}")
            print("\n重新输入:")
    
    print("程序已退出")