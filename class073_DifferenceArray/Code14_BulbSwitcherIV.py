import time

"""
LeetCode 1529. 灯泡开关 IV (Bulb Switcher IV)

题目描述:
房间中有 n 个灯泡，编号从 0 到 n-1，初始时都处于关闭状态。
你的任务是按照灯泡的编号顺序，对每个灯泡进行一次操作：切换该灯泡以及之后所有灯泡的状态（关闭变打开，打开变关闭）。
例如，第 0 号灯泡是第一个被操作的，它会切换所有灯泡的状态。
第 1 号灯泡是第二个被操作的，它会切换 1 号及之后的灯泡的状态。以此类推。
但是，现在我们有一个目标状态 target，表示每个灯泡最终是打开还是关闭。
请你计算至少需要多少次操作才能使灯泡达到目标状态。

示例1:
输入: target = "10111"
输出: 3
解释：
初始状态：00000
按0号灯泡：11111
按2号灯泡：11000
按3号灯泡：11011
此时已经达到目标状态"10111"？可能需要更准确的操作序列。

示例2:
输入: target = "101"
输出: 3

示例3:
输入: target = "00000"
输出: 0

提示:
1. 1 <= target.length <= 10^5
2. target[i] 是 '0' 或 '1'

题目链接: https://leetcode.com/problems/bulb-switcher-iv/

解题思路:
这个问题可以通过观察灯泡状态的变化规律来解决：
1. 初始时所有灯泡都是关闭的（状态为0）
2. 每一次操作都会切换当前灯泡以及之后所有灯泡的状态
3. 注意到，灯泡状态的切换是相互影响的，并且后面的操作会覆盖前面的部分操作效果

可以采用贪心的策略来解决：
1. 从左到右遍历目标字符串
2. 每次遇到状态变化（与前一个灯泡状态不同），就增加操作次数
3. 特别地，如果第一个灯泡的目标状态是1，需要进行一次初始操作

这种方法的直觉是：
- 灯泡状态的变化只能由操作导致
- 每个灯泡的最终状态取决于它被切换的次数是奇数还是偶数
- 最优策略是在状态变化的位置执行操作，这样可以一次性改变后面所有灯泡的状态

时间复杂度: O(n)，其中 n 是目标字符串的长度
空间复杂度: O(1)，只使用了常数级别的额外空间

这是最优解，因为我们需要至少遍历一次整个字符串，时间复杂度无法更低。
"""

def min_flips(target):
    """
    计算将灯泡从初始状态调整到目标状态所需的最少操作次数
    
    Args:
        target: 目标状态字符串，每个字符是 '0' 或 '1'
    
    Returns:
        最少操作次数
    """
    # 参数校验
    if not target:
        return 0
    
    flips = 0
    # 当前灯泡的期望状态，初始为0（所有灯泡都是关闭的）
    current = '0'
    
    # 遍历目标字符串的每个字符
    for c in target:
        # 如果当前字符与期望状态不同，需要进行一次操作
        if c != current:
            flips += 1
            # 切换期望状态（因为一次操作会改变当前位置及之后所有灯泡的状态）
            current = '1' if current == '0' else '0'
        # 如果相同，不需要操作，继续检查下一个灯泡
    
    return flips

def min_flips_alternative(target):
    """
    另一种实现方式，直接根据目标字符串中相邻字符的变化次数来计算
    并且考虑第一个字符是否为1
    
    Args:
        target: 目标状态字符串
    
    Returns:
        最少操作次数
    """
    # 参数校验
    if not target:
        return 0
    
    flips = 0
    
    # 如果第一个字符是1，需要一次初始操作
    if target[0] == '1':
        flips = 1
    
    # 遍历字符串，统计相邻字符变化的次数
    for i in range(1, len(target)):
        # 如果当前字符与前一个字符不同，说明需要一次操作
        if target[i] != target[i-1]:
            flips += 1
    
    return flips

# 测试代码
def main():
    # 测试用例1
    target1 = "10111"
    print("测试用例1 - min_flips('10111'):", min_flips(target1))  # 预期输出: 3
    print("测试用例1 - min_flips_alternative('10111'):", min_flips_alternative(target1))  # 预期输出: 3
    
    # 测试用例2
    target2 = "101"
    print("测试用例2 - min_flips('101'):", min_flips(target2))  # 预期输出: 3
    print("测试用例2 - min_flips_alternative('101'):", min_flips_alternative(target2))  # 预期输出: 3
    
    # 测试用例3
    target3 = "00000"
    print("测试用例3 - min_flips('00000'):", min_flips(target3))  # 预期输出: 0
    print("测试用例3 - min_flips_alternative('00000'):", min_flips_alternative(target3))  # 预期输出: 0
    
    # 测试用例4 - 全为1的情况
    target4 = "11111"
    print("测试用例4 - min_flips('11111'):", min_flips(target4))  # 预期输出: 1
    print("测试用例4 - min_flips_alternative('11111'):", min_flips_alternative(target4))  # 预期输出: 1
    
    # 测试用例5 - 交替的情况
    target5 = "1010101010"
    print("测试用例5 - min_flips('1010101010'):", min_flips(target5))  # 预期输出: 10
    print("测试用例5 - min_flips_alternative('1010101010'):", min_flips_alternative(target5))  # 预期输出: 10
    
    # 测试用例6 - 边界情况：单个字符
    target6 = "1"
    print("测试用例6 - min_flips('1'):", min_flips(target6))  # 预期输出: 1
    print("测试用例6 - min_flips_alternative('1'):", min_flips_alternative(target6))  # 预期输出: 1
    
    target7 = "0"
    print("测试用例7 - min_flips('0'):", min_flips(target7))  # 预期输出: 0
    print("测试用例7 - min_flips_alternative('0'):", min_flips_alternative(target7))  # 预期输出: 0
    
    # 性能测试
    print("\n性能测试:")
    # 生成一个大的目标字符串
    large_target = ''.join(str(i % 2) for i in range(100000))
    
    start_time = time.time()
    result1 = min_flips(large_target)
    end_time = time.time()
    print("大目标字符串 - min_flips 结果:", result1)
    print("大目标字符串 - min_flips 耗时:", (end_time - start_time) * 1000, "ms")
    
    start_time = time.time()
    result2 = min_flips_alternative(large_target)
    end_time = time.time()
    print("大目标字符串 - min_flips_alternative 结果:", result2)
    print("大目标字符串 - min_flips_alternative 耗时:", (end_time - start_time) * 1000, "ms")

if __name__ == "__main__":
    main()