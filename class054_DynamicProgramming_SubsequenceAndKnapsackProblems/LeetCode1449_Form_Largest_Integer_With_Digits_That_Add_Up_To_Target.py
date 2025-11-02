# LeetCode 1449. 数位成本和为目标值的最大数字
# 给你一个整数数组 cost 和一个整数 target 。
# 请你返回满足如下规则可以得到的 最大 整数：
# 给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
# 总成本必须恰好等于 target 。
# 添加的数位中没有数字 0 。
# 由于答案可能会很大，请你以字符串形式返回。
# 如果按照上述要求无法得到任何整数，请你返回 "0" 。
# 测试链接 : https://leetcode.cn/problems/form-largest-integer-with-digits-that-add-up-to-target/

"""
算法详解：数位成本和为目标值的最大数字（LeetCode 1449）

问题描述：
给你一个整数数组 cost 和一个整数 target 。
请你返回满足如下规则可以得到的 最大 整数：
给当前结果添加一个数位（i + 1）的成本为 cost[i] （cost 数组下标从 0 开始）。
总成本必须恰好等于 target 。
添加的数位中没有数字 0 。
由于答案可能会很大，请你以字符串形式返回。
如果按照上述要求无法得到任何整数，请你返回 "0" 。

算法思路：
这是一个完全背包问题的变种。
1. 每个数字（1-9）都有一个成本
2. 背包容量为target，要求恰好装满
3. 目标是构造最大的数字（位数最多，相同位数时字典序最大）

时间复杂度分析：
1. 动态规划：O(9 * target)
2. 构造结果：O(target)
3. 总体时间复杂度：O(target)

空间复杂度分析：
1. dp数组：O(target)
2. 总体空间复杂度：O(target)

工程化考量：
1. 异常处理：检查输入参数是否有效
2. 边界处理：处理target为0和无法构造的情况
3. 字符串处理：正确构造最大数字

极端场景验证：
1. target为0的情况
2. 所有成本都大于target的情况
3. 所有成本都等于target的情况
4. 成本数组包含重复值的情况
"""

def largestNumber(cost, target):
    """
    构造成本和为目标值的最大数字
    
    Args:
        cost (List[int]): 数字1-9的成本数组
        target (int): 目标成本
    
    Returns:
        str: 最大数字字符串，无法构造时返回"0"
    """
    # 异常处理：检查输入参数是否有效
    if not cost or len(cost) != 9 or target < 0:
        return "0"
    
    # dp[i] 表示成本恰好为i时能构造的最大数字长度
    # -1 表示无法构造
    dp = [-1] * (target + 1)
    dp[0] = 0  # 成本为0时，构造空字符串，长度为0
    
    # 完全背包：遍历每个数字（1-9）
    for i in range(9):
        digit = i + 1  # 数字1-9
        c = cost[i]    # 对应的成本
        
        # 从小到大遍历成本，因为是完全背包
        for j in range(c, target + 1):
            # 如果成本j-c可以构造
            if dp[j - c] != -1:
                # 更新dp[j]：选择能构造更长数字的方案
                dp[j] = max(dp[j], dp[j - c] + 1)
    
    # 如果无法构造成本恰好为target的数字
    if dp[target] == -1:
        return "0"
    
    # 构造最大数字
    result = []
    remaining = target
    
    # 从数字9开始往下构造，保证字典序最大
    for digit in range(9, 0, -1):
        c = cost[digit - 1]
        
        # 贪心地尽可能多地选择当前数字
        while remaining >= c and dp[remaining] == dp[remaining - c] + 1:
            result.append(str(digit))
            remaining -= c
    
    return ''.join(result)

# 另一种实现方式：使用字符串DP
def largestNumberAlternative(cost, target):
    """
    构造成本和为目标值的最大数字（替代实现）
    
    Args:
        cost (List[int]): 数字1-9的成本数组
        target (int): 目标成本
    
    Returns:
        str: 最大数字字符串，无法构造时返回"0"
    """
    # 异常处理：检查输入参数是否有效
    if not cost or len(cost) != 9 or target < 0:
        return "0"
    
    # dp[i] 表示成本恰好为i时能构造的最大数字字符串
    # None 表示无法构造
    dp = [None] * (target + 1)
    dp[0] = ""  # 成本为0时，构造空字符串
    
    # 完全背包：遍历每个数字（1-9）
    for i in range(9):
        digit = i + 1  # 数字1-9
        c = cost[i]    # 对应的成本
        
        # 从小到大遍历成本，因为是完全背包
        for j in range(c, target + 1):
            # 如果成本j-c可以构造
            if dp[j - c] is not None:
                # 构造新字符串：将当前数字放在最前面
                new_str = str(digit) + dp[j - c]
                
                # 更新dp[j]：选择字典序更大的字符串
                if dp[j] is None or compare_strings(new_str, dp[j]) > 0:
                    dp[j] = new_str
    
    # 如果无法构造成本恰好为target的数字
    return dp[target] if dp[target] is not None else "0"

# 比较两个数字字符串的大小
def compare_strings(s1, s2):
    """
    比较两个数字字符串的大小
    
    Args:
        s1 (str): 第一个字符串
        s2 (str): 第二个字符串
    
    Returns:
        int: 比较结果，>0表示s1>s2，=0表示相等，<0表示s1<s2
    """
    # 首先比较长度
    if len(s1) != len(s2):
        return len(s1) - len(s2)
    # 长度相同时比较字典序
    return 1 if s1 > s2 else (-1 if s1 < s2 else 0)

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    cost1 = [4,3,2,5,6,7,2,5,5]
    target1 = 9
    print(f"Test 1: {largestNumber(cost1, target1)}")
    print(f"Test 1 (Alternative): {largestNumberAlternative(cost1, target1)}")
    # 期望输出: "7772"
    
    # 测试用例2
    cost2 = [7,6,5,5,5,6,8,7,8]
    target2 = 12
    print(f"Test 2: {largestNumber(cost2, target2)}")
    print(f"Test 2 (Alternative): {largestNumberAlternative(cost2, target2)}")
    # 期望输出: "85"
    
    # 测试用例3
    cost3 = [2,4,6,2,4,6,4,4,4]
    target3 = 5
    print(f"Test 3: {largestNumber(cost3, target3)}")
    print(f"Test 3 (Alternative): {largestNumberAlternative(cost3, target3)}")
    # 期望输出: "0"
    
    # 测试用例4
    cost4 = [6,10,15,40,40,40,40,40,40]
    target4 = 47
    print(f"Test 4: {largestNumber(cost4, target4)}")
    print(f"Test 4 (Alternative): {largestNumberAlternative(cost4, target4)}")
    # 期望输出: "32211"
    
    # 测试用例5
    cost5 = [1,1,1,1,1,1,1,1,1]
    target5 = 0
    print(f"Test 5: {largestNumber(cost5, target5)}")
    print(f"Test 5 (Alternative): {largestNumberAlternative(cost5, target5)}")
    # 期望输出: "0"