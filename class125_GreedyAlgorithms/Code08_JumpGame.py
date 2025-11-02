# 跳跃游戏 (Jump Game)
# 给定一个非负整数数组 nums ，你最初位于数组的第一个下标。
# 数组中的每个元素代表你在该位置可以跳跃的最大长度。
# 判断你是否能够到达最后一个下标。
# 
# 算法标签: 贪心算法(Greedy Algorithm)、数组遍历(Array Traversal)
# 时间复杂度: O(n)，其中n是数组长度
# 空间复杂度: O(1)，仅使用常数额外空间
# 测试链接 : https://leetcode.cn/problems/jump-game/
# 相关题目: LeetCode 45. 跳跃游戏 II、LeetCode 1306. 跳跃游戏 III
# 贪心算法专题 - 补充题目收集与详解

"""
算法思路详解：
1. 贪心策略：维护能到达的最远位置
   - 这个策略的核心思想是动态维护一个可达范围
   - 通过不断扩展可达范围来判断是否能到达终点

2. 遍历数组，更新能到达的最远位置
   - 对于每个位置i，能到达的最远位置是max(max_reach, i + nums[i])
   - 这表示从位置i出发，最远能到达i + nums[i]位置

3. 如果当前位置超过了能到达的最远位置，则无法到达终点
   - 这意味着存在一个"断点"，无法继续前进
   - 此时可以直接返回False

4. 如果能到达的最远位置大于等于最后一个下标，则能到达终点
   - 表示终点在可达范围内
   - 可以提前返回True

时间复杂度分析：
- 遍历时间复杂度：O(n)，其中n是数组长度
- 总体时间复杂度：O(n)

空间复杂度分析：
- 只使用了常数额外空间存储变量
- 空间复杂度：O(1)

是否最优解：
- 是，这是处理此类问题的最优解法
- 贪心策略保证了局部最优解能导致全局最优解

工程化最佳实践：
1. 异常处理：检查输入是否为空或格式不正确
2. 边界条件：处理空数组、单个元素等特殊情况
3. 性能优化：一次遍历完成计算，提前终止条件避免不必要的计算
4. 可读性：清晰的变量命名和详细注释，便于维护

极端场景与边界情况处理：
1. 空输入：nums为空数组
2. 极端值：只有一个元素、所有元素都是0
3. 重复数据：多个元素相同
4. 有序/逆序数据：元素值递增或递减

跨语言实现差异与优化：
1. Java：使用增强for循环遍历数组，代码更简洁
2. C++：使用传统for循环，性能更优
3. Python：使用for循环或enumerate，语法更灵活

调试与测试策略：
1. 打印中间过程：在循环中打印当前位置和最远可达位置
2. 用断言验证中间结果：确保最远位置不减小
3. 性能退化排查：检查是否只遍历了一次数组
4. 边界测试：测试空数组、单元素等边界情况

实际应用场景与拓展：
1. 路径规划问题：在游戏开发中判断角色是否能到达目标位置
2. 图论算法：作为初始解提供给更复杂的算法
3. 网络路由：用于快速判断网络节点间的连通性

算法深入解析：
贪心算法在跳跃游戏问题中的应用体现了其核心思想：
1. 局部最优选择：每次选择能到达的最远位置
2. 无后效性：当前的选择不会影响之前的状态
3. 最优子结构：问题的最优解包含子问题的最优解
这个问题的关键洞察是，我们不需要关心具体如何跳跃，只需要关心能到达的最远位置。
"""


def canJump(nums):
    """
    跳跃游戏主函数 - 使用贪心算法判断是否能到达最后一个下标
    
    算法思路：
    1. 维护能到达的最远位置
    2. 遍历数组，更新能到达的最远位置
    3. 如果当前位置超过了能到达的最远位置，则无法到达终点
    
    Args:
        nums (List[int]): 非负整数数组，表示每个位置可以跳跃的最大长度
        nums[i]表示在位置i可以跳跃的最大长度
    
    Returns:
        bool: 是否能够到达最后一个下标
    
    时间复杂度: O(n)，其中n是数组长度
    空间复杂度: O(1)，仅使用常数额外空间
    
    Examples:
        >>> canJump([2, 3, 1, 1, 4])
        True
        >>> canJump([3, 2, 1, 0, 4])
        False
    """
    # 异常处理：检查输入是否为空
    if not nums:
        return False
    
    # 边界条件：只有一个元素，肯定能到达
    if len(nums) == 1:
        return True
    
    max_reach = 0  # 能到达的最远位置
    
    # 遍历数组
    # 时间复杂度：O(n)
    for i in range(len(nums)):
        # 如果当前位置超过了能到达的最远位置，则无法到达终点
        if i > max_reach:
            return False
        
        # 更新能到达的最远位置
        max_reach = max(max_reach, i + nums[i])
        
        # 如果能到达的最远位置大于等于最后一个下标，则能到达终点
        if max_reach >= len(nums) - 1:
            return True
    
    return False


# 测试函数
if __name__ == "__main__":
    # 测试用例1：可以到达终点
    nums1 = [2, 3, 1, 1, 4]
    print("测试用例1结果:", canJump(nums1))  # 期望输出: True
    
    # 测试用例2：无法到达终点
    nums2 = [3, 2, 1, 0, 4]
    print("测试用例2结果:", canJump(nums2))  # 期望输出: False
    
    # 测试用例3：边界情况 - 单个元素
    nums3 = [0]
    print("测试用例3结果:", canJump(nums3))  # 期望输出: True
    
    # 测试用例4：极端情况 - 无法前进
    nums4 = [1, 0, 1, 0]
    print("测试用例4结果:", canJump(nums4))  # 期望输出: False
    
    # 测试用例5：全为1 - 可以到达
    nums5 = [1, 1, 1, 1, 1]
    print("测试用例5结果:", canJump(nums5))  # 期望输出: True


# 补充题目1: LeetCode 134. 加油站
# 题目描述: 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
# 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
# 你从其中的一个加油站出发，开始时油箱为空。
# 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
# 注意：如果题目有解，该答案即为唯一答案。
# 链接: https://leetcode.cn/problems/gas-station/

def canCompleteCircuit(gas, cost):
    """
    判断是否可以绕环路行驶一周，并返回起始加油站的编号 - 使用贪心算法
    
    算法思路：
    1. 如果总油量小于总消耗，肯定无法绕行一周
    2. 贪心策略：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点
    
    Args:
        gas (List[int]): 加油站汽油储量的数组，gas[i]表示第i个加油站的汽油量
        cost (List[int]): 每段路程消耗的汽油量数组，cost[i]表示从第i个加油站到第i+1个加油站的消耗
    
    Returns:
        int: 起始加油站编号，如果无法绕行则返回-1
    
    时间复杂度: O(n)，只需遍历一次数组
    空间复杂度: O(1)，只使用常数额外空间
    
    工程化考量：
    1. 异常处理：检查输入是否为空或长度不匹配
    2. 边界条件：处理空数组等特殊情况
    3. 性能优化：一次遍历完成计算
    """
    # 异常处理：检查输入是否为空或长度不匹配
    if not gas or not cost or len(gas) != len(cost):
        return -1  # 输入不合法
    
    n = len(gas)
    totalGas = 0    # 总油量
    totalCost = 0   # 总消耗
    currentGas = 0  # 当前剩余油量
    startStation = 0  # 起始加油站
    
    # 贪心策略：如果从A到B的路上没油了，那么A到B之间的任何一个站点都不能作为起点
    # 时间复杂度：O(n)
    for i in range(n):
        totalGas += gas[i]
        totalCost += cost[i]
        currentGas += gas[i] - cost[i]
        
        # 如果当前剩余油量为负，说明从startStation到i的路径不可行
        if currentGas < 0:
            startStation = i + 1  # 从下一个站点重新开始计算
            currentGas = 0        # 重置当前剩余油量
    
    # 如果总油量小于总消耗，那么无论如何都不可能绕行一周
    if totalGas < totalCost:
        return -1
    
    # 否则，startStation就是答案
    return startStation


# 补充题目2: LeetCode 561. 数组拆分 I
# 题目描述: 给定长度为 2n 的整数数组 nums ，你的任务是将这些数分成 n 对，
# 例如 (a1, b1), (a2, b2), ..., (an, bn) ，使得从 1 到 n 的 min(ai, bi) 总和最大。
# 返回该 最大总和 。
# 链接: https://leetcode.cn/problems/array-partition-i/

def arrayPairSum(nums):
    """
    将数组分成n对，使得min(ai, bi)的总和最大 - 使用贪心算法
    
    算法思路：
    1. 将数组排序
    2. 贪心策略：每两个相邻的数分为一组，取较小的那个（即每对中的第一个数）
    
    Args:
        nums (List[int]): 整数数组，长度为2n
    
    Returns:
        int: 最大总和
    
    时间复杂度: O(n log n)，主要是排序的复杂度
    空间复杂度: O(1)，只使用常数额外空间（假设排序算法使用原地排序）
    
    工程化考量：
    1. 异常处理：检查输入是否为空或长度不是偶数
    2. 边界条件：处理空数组等特殊情况
    3. 性能优化：排序后使用贪心策略
    """
    # 异常处理：检查输入是否为空或长度不是偶数
    if not nums or len(nums) % 2 != 0:
        return 0  # 输入不合法
    
    # 贪心策略：将数组排序后，每两个相邻的数分为一组，取较小的那个（即每对中的第一个数）
    # 时间复杂度：O(n log n)
    nums.sort()
    maxSum = 0
    
    # 每隔一个元素取一个（即每对中的第一个元素）
    # 时间复杂度：O(n)
    for i in range(0, len(nums), 2):
        maxSum += nums[i]
    
    return maxSum


# 补充题目3: LeetCode 402. 移掉K位数字
# 题目描述: 给你一个以字符串表示的非负整数 num 和一个整数 k ，
# 移除这个数中的 k 位数字，使得剩下的数字最小。
# 请你以字符串形式返回这个最小的数字。
# 链接: https://leetcode.cn/problems/remove-k-digits/

def removeKdigits(num, k):
    """
    移掉k位数字，使得剩下的数字最小 - 使用贪心算法+单调栈
    
    算法思路：
    1. 使用栈来存储需要保留的数字
    2. 贪心策略：从左到右遍历，如果当前数字小于栈顶数字，且还有删除次数，则弹出栈顶数字
    
    Args:
        num (str): 表示非负整数的字符串
        k (int): 需要移除的数字个数
    
    Returns:
        str: 最小数字的字符串表示
    
    时间复杂度: O(n)，每个字符最多入栈和出栈一次
    空间复杂度: O(n)，使用栈存储保留的数字
    
    工程化考量：
    1. 异常处理：检查输入是否为空或k过大
    2. 边界条件：处理空字符串、k等于字符串长度等情况
    3. 性能优化：使用单调栈避免重复操作
    """
    # 异常处理：检查输入是否为空或k过大
    if not num or len(num) <= k:
        return "0"  # 移除所有数字，返回0
    
    # 使用栈来存储需要保留的数字
    stack = []
    
    # 贪心策略：从左到右遍历，如果当前数字小于栈顶数字，且还有删除次数，则弹出栈顶数字
    # 时间复杂度：O(n)
    for digit in num:
        # 当栈不为空，当前数字小于栈顶数字，且还有删除次数时，弹出栈顶数字
        while stack and digit < stack[-1] and k > 0:
            stack.pop()
            k -= 1
        # 将当前数字入栈
        stack.append(digit)
    
    # 如果还有删除次数，从栈顶删除
    while k > 0 and stack:
        stack.pop()
        k -= 1
    
    # 构建结果字符串，去除前导零
    result = ''.join(stack).lstrip('0')
    
    # 如果全是零，返回"0"
    return result if result else "0"


# 补充题目4: LeetCode 122. 买卖股票的最佳时机 II（另一种贪心实现）
# 题目描述: 给定一个数组 prices ，其中 prices[i] 表示股票第 i 天的价格。
# 在每一天，你可能会决定购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。
# 你也可以购买它，然后在 同一天 出售。
# 返回 你能获得的 最大 利润 。
# 链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

def maxProfit(prices):
    """
    计算买卖股票的最大利润，允许多次交易 - 使用贪心算法
    
    算法思路：
    1. 贪心策略：只要后一天的价格比前一天高，就进行一次买卖
    
    Args:
        prices (List[int]): 股票每天价格的数组，prices[i]表示第i天的股票价格
    
    Returns:
        int: 最大利润
    
    时间复杂度: O(n)，只需遍历一次数组
    空间复杂度: O(1)，只使用常数额外空间
    
    工程化考量：
    1. 异常处理：检查输入是否为空或长度不足
    2. 边界条件：处理空数组、单元素等情况
    3. 性能优化：一次遍历完成计算
    """
    # 异常处理：检查输入是否为空或长度不足
    if not prices or len(prices) <= 1:
        return 0  # 无法交易
    
    max_profit = 0
    # 贪心策略：只要后一天的价格比前一天高，就进行一次买卖
    # 时间复杂度：O(n)
    for i in range(1, len(prices)):
        # 如果当天价格比前一天高，就进行交易
        if prices[i] > prices[i - 1]:
            max_profit += prices[i] - prices[i - 1]
    
    return max_profit


# 补充题目5: LeetCode 665. 非递减数列
# 题目描述: 给你一个长度为 n 的整数数组 nums ，请你判断在 最多 改变 1 个元素的情况下，
# 该数组能否变成一个非递减数列。
# 非递减数列的定义是：对于数组中任意的 i (0 <= i <= n-2)，总满足 nums[i] <= nums[i+1]。
# 链接: https://leetcode.cn/problems/non-decreasing-array/

def checkPossibility(nums):
    """
    判断是否可以通过最多修改一个元素，使数组成为非递减数列 - 使用贪心算法
    
    算法思路：
    1. 遍历数组，统计需要修改的次数
    2. 贪心策略：尽可能修改nums[i]而不是nums[i+1]，这样对后续影响更小
    
    Args:
        nums (List[int]): 整数数组
    
    Returns:
        bool: 是否可以满足条件
    
    时间复杂度: O(n)，只需遍历一次数组
    空间复杂度: O(1)，只使用常数额外空间
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、单元素等情况
    3. 性能优化：提前终止条件避免不必要的计算
    """
    # 异常处理：检查输入是否为空
    if not nums or len(nums) <= 1:
        return True  # 空数组或只有一个元素是非递减的
    
    count = 0  # 记录需要修改的次数
    
    # 时间复杂度：O(n)
    for i in range(len(nums) - 1):
        if nums[i] > nums[i + 1]:
            count += 1
            if count > 1:
                return False  # 需要修改超过1次
            
            # 贪心策略：尽可能修改nums[i]而不是nums[i+1]，这样对后续影响更小
            # 但是如果nums[i-1] > nums[i+1]，则必须修改nums[i+1]
            if i > 0 and nums[i - 1] > nums[i + 1]:
                nums[i + 1] = nums[i]  # 修改nums[i+1]
            else:
                nums[i] = nums[i + 1]  # 修改nums[i]
    
    return True