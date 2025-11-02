# 威佐夫博弈 (Wythoff's Game)
# 有两堆各若干个物品，两个人轮流从某一堆或同时从两堆中取同样多的物品
# 规定每次至少取一个，多者不限，最后取光者得胜
# 
# 题目来源：
# 1. HDU 1527 取石子游戏 - http://acm.hdu.edu.cn/showproblem.php?pid=1527
# 2. 洛谷 P1290 欧几里得的游戏 - https://www.luogu.com.cn/problem/P1290
# 3. CodeForces 1371A Magical Sticks - https://codeforces.com/problemset/problem/1371/A
# 4. LeetCode LCP 30. 魔塔游戏 - https://leetcode-cn.com/problems/p0NxJO/
# 5. 牛客网 NC14520 取石子游戏 - https://ac.nowcoder.com/acm/problem/14520
# 
# 算法核心思想：
# 1. 威佐夫博弈的关键在于找到"奇异局势"（必败态）
# 2. 奇异局势满足：a = floor(k*(sqrt(5)+1)/2), b = a + k，其中k为非负整数
# 3. 当两堆石子数(a,b)满足奇异局势时，先手必败；否则先手必胜
# 
# 时间复杂度分析：
# O(1) - 只需常数时间计算黄金分割比和判断是否为奇异局势
# 
# 空间复杂度分析：
# O(1) - 只需几个变量存储中间结果
# 
# 工程化考量：
# 1. 异常处理：处理负数输入和边界情况
# 2. 精度控制：使用足够精度计算黄金分割比
# 3. 可读性：添加详细注释说明算法原理
# 4. 可扩展性：支持不同的输入格式和查询方式

import math

# 黄金分割比 (sqrt(5)+1)/2
GOLDEN_RATIO = (math.sqrt(5) + 1) / 2

def is_losing_position(a, b):
    """
    判断两堆石子数(a,b)是否为威佐夫博弈的奇异局势
    
    参数:
        a: 第一堆石子数
        b: 第二堆石子数
    
    返回:
        如果是奇异局势返回True（先手必败），否则返回False（先手必胜）
    
    异常:
        ValueError: 当石子数为负数时抛出
    """
    # 异常处理：处理非法输入
    if a < 0 or b < 0:
        raise ValueError("石子数不能为负数")
    
    # 确保a <= b
    if a > b:
        a, b = b, a
    
    # 计算k值
    k = b - a
    
    # 计算理论上的a值
    expected_a = math.floor(k * GOLDEN_RATIO)
    
    # 判断实际a值是否等于理论值
    return a == expected_a

def solve(a, b):
    """
    威佐夫博弈的解题函数
    
    参数:
        a: 第一堆石子数
        b: 第二堆石子数
    
    返回:
        返回"先手必胜"或"先手必败"
    """
    # 异常处理：处理非法输入
    try:
        return "先手必败" if is_losing_position(a, b) else "先手必胜"
    except ValueError as e:
        return f"输入错误: {str(e)}"

def find_winning_move(a, b):
    """
    找到获胜策略：如果存在必胜策略，返回应该如何取石子
    
    参数:
        a: 第一堆石子数
        b: 第二堆石子数
    
    返回:
        返回取石子的策略，如果是必败态返回"无法必胜"
    """
    # 异常处理：处理非法输入
    if a < 0 or b < 0:
        return "输入错误：石子数不能为负数"
    
    # 如果已经是必败态，无法必胜
    if is_losing_position(a, b):
        return "无法必胜"
    
    # 确保a <= b
    swapped = False
    if a > b:
        a, b = b, a
        swapped = True
    
    # 尝试三种可能的取法：
    # 1. 从第一堆取x个
    # 2. 从第二堆取x个
    # 3. 从两堆同时取x个
    
    # 计算k值
    k = b - a
    expected_a = math.floor(k * GOLDEN_RATIO)
    
    # 计算需要取多少石子才能到达奇异局势
    if a > expected_a:
        # 方案1或3：从第一堆取或同时取
        x = a - expected_a
        if swapped:
            # 恢复原始顺序
            return f"从第二堆取{x}个石子"
        else:
            return f"从第一堆取{x}个石子"
    else:
        # 方案2：从第二堆取
        expected_b = expected_a + k
        x = b - expected_b
        if swapped:
            # 恢复原始顺序
            return f"从第一堆取{x}个石子"
        else:
            return f"从第二堆取{x}个石子"

# 主函数用于测试
if __name__ == "__main__":
    print("威佐夫博弈求解器")
    print("输入两堆石子数a b (输入-1退出):")
    
    while True:
        try:
            a = int(input("请输入第一堆石子数: "))
            if a == -1:
                break
            b = int(input("请输入第二堆石子数: "))
            
            print(f"结果: {solve(a, b)}")
            print(f"策略: {find_winning_move(a, b)}")
            print("\n输入下一组数据 (输入-1退出):")
        except ValueError:
            print("输入错误，请输入整数")
            print("\n重新输入:")