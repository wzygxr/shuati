# 斐波那契博弈 (Fibonacci Nim)
# 一堆石子，两人轮流取，每次可取1到上次取的两倍，但第一次只能取1到n-1个石子
# 取到最后一个石子的人获胜
# 
# 题目来源：
# 1. HDU 2516 取石子游戏 - http://acm.hdu.edu.cn/showproblem.php?pid=2516
# 2. POJ 2484 A Funny Game - http://poj.org/problem?id=2484
# 3. CodeForces 1296D Fight with Monsters - https://codeforces.com/problemset/problem/1296/D
# 4. AtCoder ABC193 D - Poker - https://atcoder.jp/contests/abc193/tasks/abc193_d
# 5. 洛谷 P1290 欧几里得的游戏 - https://www.luogu.com.cn/problem/P1290
# 6. LeetCode 877. Stone Game - https://leetcode.com/problems/stone-game/
# 
# 算法核心思想：
# 斐波那契博弈的关键结论是：当石子数n为斐波那契数时，先手必败；否则先手必胜
# 这个结论基于Zeckendorf定理（任何正整数可以唯一表示为若干个不连续的斐波那契数之和）
# 
# 时间复杂度分析：
# O(log n) - 生成斐波那契数列直到超过n
# 
# 空间复杂度分析：
# O(log n) - 存储斐波那契数列
# 
# 工程化考量：
# 1. 异常处理：处理负数输入、边界情况
# 2. 性能优化：使用动态规划预处理斐波那契数列
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的输入格式和查询方式

# 预生成的斐波那契数列
fibonacci_sequence = []

# 最大支持的石子数（防止溢出）
MAX_N = 1000000

def generate_fibonacci_sequence():
    """
    生成斐波那契数列直到超过MAX_N
    """
    global fibonacci_sequence
    fibonacci_sequence = []
    fibonacci_sequence.append(1)  # F(1) = 1
    fibonacci_sequence.append(2)  # F(2) = 2
    
    while True:
        next_fib = fibonacci_sequence[-1] + fibonacci_sequence[-2]
        if next_fib > MAX_N:
            break
        fibonacci_sequence.append(next_fib)

# 初始化时生成斐波那契数列
generate_fibonacci_sequence()

def is_fibonacci(n):
    """
    判断一个数是否是斐波那契数
    
    参数:
        n: 要判断的数
    
    返回:
        如果是斐波那契数返回True，否则返回False
    """
    # 使用二分查找判断n是否在斐波那契数列中
    left, right = 0, len(fibonacci_sequence) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        if fibonacci_sequence[mid] == n:
            return True
        elif fibonacci_sequence[mid] < n:
            left = mid + 1
        else:
            right = mid - 1
    
    return False

def is_first_player_win(n):
    """
    判断斐波那契博弈中先手是否必胜
    
    参数:
        n: 石子总数
    
    返回:
        如果先手必胜返回True，否则返回False
    
    异常:
        ValueError: 当输入非法时抛出异常
    """
    # 异常处理：处理非法输入
    if n <= 0:
        raise ValueError("石子数必须为正整数")
    
    # 特殊情况处理
    if n == 1:
        return False  # 只有1个石子时，先手无法取（必须取n-1=0个），所以必败
    
    # 斐波那契博弈结论：当n为斐波那契数时，先手必败
    return not is_fibonacci(n)

def solve(n):
    """
    斐波那契博弈的解题函数
    
    参数:
        n: 石子总数
    
    返回:
        返回"先手必胜"或"先手必败"
    """
    try:
        return "先手必胜" if is_first_player_win(n) else "先手必败"
    except ValueError as e:
        return f"输入错误: {str(e)}"

def find_winning_move(n):
    """
    找到获胜策略：如果存在必胜策略，返回第一次应该取多少石子
    
    参数:
        n: 石子总数
    
    返回:
        返回第一次取石子的数量，如果是必败态返回"无法必胜"
    """
    try:
        # 检查是否是必胜态
        if not is_first_player_win(n):
            return "无法必胜"
        
        # 根据Zeckendorf定理，将n分解为不连续斐波那契数之和
        # 找到最大的小于n的斐波那契数
        idx = len(fibonacci_sequence) - 1
        while fibonacci_sequence[idx] >= n:
            idx -= 1
        
        # 第一次取n - F(k)个石子
        # 这样剩下的石子数为F(k)，迫使对手处于必败态
        first_move = n - fibonacci_sequence[idx]
        
        return f"第一次取{first_move}个石子"
        
    except ValueError as e:
        return f"输入错误: {str(e)}"

def get_zeckendorf_representation(n):
    """
    获取斐波那契分解（Zeckendorf表示）
    
    参数:
        n: 要分解的数
    
    返回:
        返回分解后的斐波那契数列表
    """
    representation = []
    remaining = n
    
    while remaining > 0:
        # 找到最大的小于等于remaining的斐波那契数
        idx = len(fibonacci_sequence) - 1
        while fibonacci_sequence[idx] > remaining:
            idx -= 1
        
        representation.append(fibonacci_sequence[idx])
        remaining -= fibonacci_sequence[idx]
    
    return representation

def get_detailed_analysis(n):
    """
    打印斐波那契博弈的详细分析
    
    参数:
        n: 石子总数
    
    返回:
        返回详细的分析结果字符串
    """
    analysis = []
    analysis.append("斐波那契博弈分析：")
    analysis.append(f"当前石子数: {n}")
    
    try:
        is_fib = is_fibonacci(n)
        analysis.append(f"是否为斐波那契数: {'是' if is_fib else '否'}")
        
        if not is_fib:
            zeckendorf = get_zeckendorf_representation(n)
            analysis.append(f"Zeckendorf表示: {zeckendorf}")
        
        analysis.append("\n应用斐波那契博弈定理：")
        if n == 1:
            analysis.append("特殊情况：只有1个石子时，先手无法取（必须取n-1=0个）")
            analysis.append("结论: 先手必败")
        elif is_fib:
            analysis.append("当石子数为斐波那契数时，先手必败")
            analysis.append("结论: 先手必败")
        else:
            analysis.append("当石子数不为斐波那契数时，先手必胜")
            analysis.append("结论: 先手必胜")
            analysis.append(f"获胜策略: {find_winning_move(n)}")
        
        analysis.append(f"\n最终结果: {solve(n)}")
        
    except ValueError as e:
        analysis.append(f"分析失败: {str(e)}")
    
    return '\n'.join(analysis)

# 主函数用于测试
if __name__ == "__main__":
    print("斐波那契博弈求解器")
    print("请输入石子总数n (输入-1退出):")
    
    while True:
        try:
            n = int(input("请输入石子总数n: "))
            if n == -1:
                break
            
            print("\n" + get_detailed_analysis(n))
            print("\n输入下一个数 (输入-1退出):")
            
        except ValueError:
            print("输入错误，请输入整数")
            print("\n重新输入:")
        except Exception as e:
            print(f"发生错误: {str(e)}")
            print("\n重新输入:")
    
    print("程序已退出")