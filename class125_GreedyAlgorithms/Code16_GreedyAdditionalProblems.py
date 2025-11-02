"""
贪心算法补充题目集合 - Python版本
收集来自各大算法平台的经典贪心算法题目
每个题目都提供详细的注释、复杂度分析和工程化考量

算法专题概览：
本文件包含10个经典的贪心算法问题，涵盖了不同领域的应用场景：
1. 资源分配问题（加油站、采药）
2. 字符串处理问题（移除数字）
3. 数组优化问题（数组拆分、取反操作）
4. 序列调整问题（非递减数列）
5. 预算优化问题（购买玩具）
6. 策略选择问题（运气平衡）
7. 区间调度问题（线段覆盖）
8. 差值优化问题（教练分配）

贪心算法核心思想：
贪心算法在每个决策点都做出局部最优选择，希望通过一系列局部最优选择达到全局最优解。
适用条件：
1. 贪心选择性质：局部最优选择能导致全局最优解
2. 最优子结构：问题的最优解包含子问题的最优解

工程化最佳实践：
1. 异常处理：检查输入参数的有效性
2. 边界条件：处理空输入、单元素等特殊情况
3. 性能优化：选择合适的数据结构和算法策略
4. 可读性：清晰的变量命名和详细注释
"""

def can_complete_circuit(gas, cost):
    """
    题目1: LeetCode 134. 加油站
    问题描述：在一条环路上有n个加油站，第i个加油站有汽油gas[i]升。你有一辆油箱容量无限的汽车，
    从第i个加油站开往第i+1个加油站需要消耗汽油cost[i]升。你从其中一个加油站出发，开始时油箱为空。
    如果你可以绕环路行驶一周，返回出发时加油站的编号，否则返回-1。

    算法思路：贪心策略，维护当前剩余油量和总剩余油量
    1. 如果总油量小于总消耗，肯定无法绕行一周
    2. 贪心策略：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点
    
    Args:
        gas (List[int]): 每个加油站的汽油量
        cost (List[int]): 从当前加油站到下一站的消耗
    
    Returns:
        int: 能够完成环路的起始加油站编号，如果无法完成返回-1
    
    时间复杂度: O(n)，其中n是加油站数量
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：检查输入长度是否匹配
    if len(gas) != len(cost):
        return -1
    
    total_gas = 0      # 总油量
    current_gas = 0    # 当前剩余油量
    start_station = 0  # 起始加油站
    
    # 遍历所有加油站
    # 时间复杂度：O(n)
    for i in range(len(gas)):
        total_gas += gas[i] - cost[i]
        current_gas += gas[i] - cost[i]
        
        # 如果当前剩余油量为负，说明从start_station到i的路径不可行
        if current_gas < 0:
            start_station = i + 1  # 从下一个站点重新开始计算
            current_gas = 0        # 重置当前剩余油量
    
    # 如果总油量小于总消耗，那么无论如何都不可能绕行一周
    return start_station if total_gas >= 0 else -1

def remove_k_digits(num, k):
    """
    题目2: LeetCode 402. 移掉K位数字
    问题描述：给定一个以字符串表示的非负整数num，移除这个数中的k位数字，使得剩下的数字最小。

    算法思路：使用单调栈保持数字序列递增
    1. 贪心策略：从左到右遍历，如果当前数字小于栈顶数字且还有删除次数，则弹出栈顶数字
    2. 使用栈来存储需要保留的数字
    
    Args:
        num (str): 表示非负整数的字符串
        k (int): 需要移除的数字位数
    
    Returns:
        str: 移除k位数字后的最小数字字符串
    
    时间复杂度: O(n)，其中n是字符串长度
    空间复杂度: O(n)，栈的空间
    """
    # 异常处理：如果移除位数大于等于字符串长度，返回"0"
    if len(num) <= k:
        return "0"
    
    stack = []  # 使用栈存储保留的数字
    
    # 贪心策略：从左到右遍历，如果当前数字小于栈顶数字，且还有删除次数，则弹出栈顶数字
    # 时间复杂度：O(n)
    for digit in num:
        # 当栈不为空，当前数字小于栈顶数字，且还有删除次数时，弹出栈顶
        while stack and k > 0 and digit < stack[-1]:
            stack.pop()
            k -= 1
        stack.append(digit)
    
    # 处理剩余的删除次数
    while k > 0:
        stack.pop()
        k -= 1
    
    # 构建结果字符串，去除前导零
    result = ''.join(stack).lstrip('0')
    return result if result else "0"

def array_pair_sum(nums):
    """
    题目3: LeetCode 561. 数组拆分 I
    问题描述：给定长度为2n的整数数组nums，将这些数分成n对，使得从1到n的min(ai, bi)总和最大。

    算法思路：排序后取每对的第一个元素
    1. 贪心策略：将数组排序后，每两个相邻的数分为一组，取较小的那个（即每对中的第一个数）
    2. 这样可以最大化较小值的总和
    
    Args:
        nums (List[int]): 长度为2n的整数数组
    
    Returns:
        int: 最大min(ai, bi)总和
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：检查数组长度是否为偶数
    if len(nums) % 2 != 0:
        return 0
    
    # 贪心策略：将数组排序后，每两个相邻的数分为一组，取较小的那个
    # 时间复杂度：O(n log n)
    nums.sort()
    total_sum = 0
    
    # 每隔一个元素取一个（即每对中的第一个元素）
    # 时间复杂度：O(n)
    for i in range(0, len(nums), 2):
        total_sum += nums[i]
    
    return total_sum

def check_possibility(nums):
    """
    题目4: LeetCode 665. 非递减数列
    问题描述：给定一个长度为n的整数数组nums，判断在最多改变1个元素的情况下，该数组能否变成非递减数列。

    算法思路：遇到逆序对时优先修改前面的数字
    1. 遍历数组，统计需要修改的次数
    2. 贪心策略：尽可能修改nums[i]而不是nums[i+1]，这样对后续影响更小
    
    Args:
        nums (List[int]): 整数数组
    
    Returns:
        bool: 是否可以通过最多修改一个元素使数组变为非递减数列
    
    时间复杂度: O(n)，其中n是数组长度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空数组或单元素数组已经是非递减数列
    if len(nums) <= 1:
        return True
    
    count = 0  # 记录需要修改的次数
    
    # 时间复杂度：O(n)
    for i in range(len(nums) - 1):
        if nums[i] > nums[i + 1]:
            count += 1
            if count > 1:
                return False  # 需要修改超过1次
            
            # 贪心选择：优先修改nums[i]而不是nums[i+1]
            if i > 0 and nums[i - 1] > nums[i + 1]:
                nums[i + 1] = nums[i]  # 必须修改nums[i+1]
            else:
                nums[i] = nums[i + 1]  # 修改nums[i]
    
    return True

def maximum_toys(prices, budget):
    """
    题目5: HackerRank - Mark and Toys
    问题描述：马克想用给定的预算购买最多的玩具，每个玩具都有价格，求能购买的最大玩具数量。

    算法思路：排序价格后贪心购买
    1. 贪心策略：优先购买价格最低的玩具
    2. 将玩具价格排序后依次购买，直到预算不足
    
    Args:
        prices (List[int]): 玩具价格列表
        budget (int): 预算金额
    
    Returns:
        int: 能购买的最大玩具数量
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：检查输入是否有效
    if not prices or budget <= 0:
        return 0
    
    # 贪心策略：优先购买价格最低的玩具
    # 时间复杂度：O(n log n)
    prices.sort()
    count = 0          # 购买的玩具数量
    current_cost = 0   # 当前花费
    
    # 时间复杂度：O(n)
    for price in prices:
        if current_cost + price <= budget:
            current_cost += price
            count += 1
        else:
            break  # 预算不足，无法购买更多玩具
    
    return count

def luck_balance(k, contests):
    """
    题目6: HackerRank - Luck Balance
    问题描述：参赛者可以输掉一些比赛来获得运气值，但重要比赛最多只能输掉k场，求能获得的最大运气值。

    算法思路：输掉不重要比赛，输掉k场重要比赛
    1. 贪心策略：输掉所有不重要比赛，输掉k场运气值最高的重要比赛
    2. 将重要比赛按运气值降序排序，前k场输掉，其余赢取
    
    Args:
        k (int): 最多能输掉的重要比赛场数
        contests (List[List[int]]): 比赛信息列表，每个元素为[luck, importance]
    
    Returns:
        int: 能获得的最大运气值
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(n)，存储重要比赛运气值的空间
    """
    # 异常处理：检查输入是否有效
    if not contests:
        return 0
    
    important = []      # 重要比赛的运气值
    total_luck = 0      # 总运气值
    
    # 分离重要比赛和不重要比赛
    # 时间复杂度：O(n)
    for contest in contests:
        luck, importance = contest
        if importance == 1:
            important.append(luck)
        else:
            total_luck += luck  # 不重要比赛直接输掉
    
    # 重要比赛按运气值降序排序
    # 时间复杂度：O(m log m)，其中m是重要比赛数量
    important.sort(reverse=True)
    
    # 输掉前k场重要比赛
    # 时间复杂度：O(m)
    for i in range(len(important)):
        if i < k:
            total_luck += important[i]  # 输掉比赛，增加运气
        else:
            total_luck -= important[i]  # 赢得比赛，减少运气
    
    return total_luck

def crazy_herbs(time, herbs):
    """
    题目7: 牛客网 - 疯狂的采药（分数背包贪心解法）
    问题描述：在给定时间内采药，每种草药有价值和采摘时间，求能获得的最大价值（可部分采摘）。

    算法思路：按性价比排序后贪心选择
    1. 贪心策略：按性价比（价值/时间）降序排序
    2. 优先采摘性价比高的草药，可部分采摘
    
    Args:
        time (int): 总采摘时间
        herbs (List[List[int]]): 草药信息列表，每个元素为[value, cost]
    
    Returns:
        int: 能获得的最大价值
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(n)，存储草药信息的空间
    """
    # 异常处理：检查输入是否有效
    if not herbs or time <= 0:
        return 0
    
    # 计算每种草药的性价比
    # 时间复杂度：O(n)
    herb_list = []
    for herb in herbs:
        value, cost = herb
        ratio = value / cost
        herb_list.append((ratio, value, cost))
    
    # 按性价比降序排序
    # 时间复杂度：O(n log n)
    herb_list.sort(key=lambda x: x[0], reverse=True)
    
    total_value = 0        # 总价值
    remaining_time = time  # 剩余时间
    
    # 贪心选择草药
    # 时间复杂度：O(n)
    for herb in herb_list:
        ratio, value, cost = herb
        
        if remaining_time >= cost:
            # 可以采摘完整的草药
            total_value += value
            remaining_time -= cost
        else:
            # 采摘部分草药（分数背包）
            total_value += int(value * (remaining_time / cost))
            break
    
    return total_value

def honest_coach(skills):
    """
    题目8: Codeforces 1360B - Honest Coach
    问题描述：教练要将学生分成两组，使得两组学生能力值的最大差值最小。

    算法思路：排序后找最小相邻差值
    1. 贪心策略：排序后相邻元素的差值最小
    2. 最小相邻差值即为两组学生能力值的最小差值
    
    Args:
        skills (List[int]): 学生能力值列表
    
    Returns:
        int: 两组学生能力值的最小差值
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空数组或单元素数组
    if len(skills) <= 1:
        return 0
    
    # 贪心策略：排序后相邻元素的差值最小
    # 时间复杂度：O(n log n)
    skills.sort()
    min_diff = float('inf')
    
    # 找最小相邻差值
    # 时间复杂度：O(n)
    for i in range(1, len(skills)):
        min_diff = min(min_diff, skills[i] - skills[i - 1])
    
    return min_diff

def max_non_overlapping_intervals(intervals):
    """
    题目9: 洛谷 P1803 - 线段覆盖
    问题描述：给定一些区间，选择最多的不重叠区间。

    算法思路：经典活动选择问题
    1. 贪心策略：按结束时间排序，优先选择结束时间早的区间
    2. 这样能为后续区间留出更多空间
    
    Args:
        intervals (List[List[int]]): 区间列表，每个元素为[start, end]
    
    Returns:
        int: 最多不重叠区间数量
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空区间列表
    if not intervals:
        return 0
    
    # 贪心策略：按结束时间排序，优先选择结束时间早的区间
    # 时间复杂度：O(n log n)
    intervals.sort(key=lambda x: x[1])
    
    count = 1              # 不重叠区间数量
    end = intervals[0][1]  # 上一个选择区间的结束时间
    
    # 时间复杂度：O(n)
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return count

def largest_sum_after_k_negations(nums, k):
    """
    题目10: LeetCode 1005. K 次取反后最大化的数组和
    问题描述：给定一个整数数组nums和整数k，按以下方法修改数组：选择某个下标i并将nums[i]替换为-nums[i]，
    重复这个过程恰好k次，返回数组可能的最大和。

    算法思路：优先处理负数，然后处理最小正数
    1. 贪心策略：优先将负数变为正数
    2. 如果k还有剩余且为奇数，对最小的数取反
    
    Args:
        nums (List[int]): 整数数组
        k (int): 取反操作次数
    
    Returns:
        int: 取反后可能的最大数组和
    
    时间复杂度: O(n log n)，主要是排序的时间复杂度
    空间复杂度: O(1)，只使用常数额外空间
    """
    # 异常处理：空数组
    if not nums:
        return 0
    
    # 贪心策略：优先处理负数
    # 时间复杂度：O(n log n)
    nums.sort()
    
    # 优先将负数变为正数
    # 时间复杂度：O(n)
    for i in range(len(nums)):
        if k > 0 and nums[i] < 0:
            nums[i] = -nums[i]
            k -= 1
        else:
            break
    
    # 如果k还有剩余且为奇数，对最小的数取反
    if k > 0 and k % 2 == 1:
        nums.sort()  # 重新排序找到最小的数
        nums[0] = -nums[0]
    
    # 计算数组和
    # 时间复杂度：O(n)
    return sum(nums)

# 测试函数
if __name__ == "__main__":
    # 测试加油站问题
    gas = [1, 2, 3, 4, 5]
    cost = [3, 4, 5, 1, 2]
    print("加油站测试:", can_complete_circuit(gas, cost))  # 期望: 3
    
    # 测试移掉K位数字
    print("移掉K位数字测试:", remove_k_digits("1432219", 3))  # 期望: "1219"
    
    # 测试数组拆分
    nums = [1, 4, 3, 2]
    print("数组拆分测试:", array_pair_sum(nums))  # 期望: 4
    
    # 测试非递减数列
    nums2 = [4, 2, 3]
    print("非递减数列测试:", check_possibility(nums2))  # 期望: True
    
    # 测试疯狂的采药
    herbs = [[60, 10], [100, 20], [120, 30]]
    print("疯狂的采药测试:", crazy_herbs(50, herbs))  # 期望: 240