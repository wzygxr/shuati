# -*- coding: utf-8 -*-
"""
子集覆盖问题（Set Cover Problem）

问题描述：
给定一个全集U和一组子集S_1, S_2, ..., S_m，其中每个子集S_i是U的子集，并且有一个权值w_i。
我们需要选择一些子集，使得它们的并等于全集U，并且所选子集的权值和最小。

贪心策略：
每次选择能够覆盖最多未被覆盖的元素的子集（按权重计算性价比最高的）。

注意：子集覆盖问题是NP难的，贪心算法不能保证得到最优解，但可以得到一个近似比为ln(n)+1的解，
其中n是全集的大小。

时间复杂度：O(mn)，其中m是子集的数量，n是全集的大小
空间复杂度：O(n+m)，需要存储未覆盖的元素集合和子集信息

相关题目：
1. LeetCode 1541. 平衡括号字符串的最少插入次数
2. LeetCode 1689. 十-二进制数的最少数目
3. LeetCode 45. 跳跃游戏 II
"""

def set_cover(universe, subsets, weights=None):
    """
    子集覆盖问题的贪心算法实现
    
    Args:
        universe: 全集U，一个包含所有元素的集合
        subsets: 子集列表，每个子集是一个集合
        weights: 每个子集的权值列表，如果为None则默认为1
    
    Returns:
        tuple: (选中的子集索引列表, 总权值)
    
    Raises:
        ValueError: 当输入无效时抛出异常
    """
    # 参数验证
    if not isinstance(universe, set):
        raise ValueError("universe必须是一个集合")
    
    if not subsets or not all(isinstance(s, set) for s in subsets):
        raise ValueError("subsets必须是一个非空的集合列表")
    
    if weights is None:
        weights = [1] * len(subsets)
    elif len(weights) != len(subsets):
        raise ValueError("weights的长度必须与subsets相同")
    
    # 检查是否可以覆盖全集
    all_elements = set()
    for s in subsets:
        all_elements.update(s)
    
    if not universe.issubset(all_elements):
        raise ValueError("给定的子集无法覆盖全集")
    
    uncovered = universe.copy()  # 未被覆盖的元素集合
    selected_subsets = []        # 选中的子集索引列表
    total_weight = 0             # 总权值
    
    while uncovered:
        best_subset_index = -1
        best_value = -1  # 性价比 = 覆盖的新元素数量 / 权值
        
        # 找到性价比最高的子集
        for i, subset in enumerate(subsets):
            # 计算该子集能覆盖的未被覆盖的元素数量
            covered_new = len(subset & uncovered)
            if covered_new == 0:
                continue  # 该子集不能覆盖新元素，跳过
            
            # 计算性价比
            value = covered_new / weights[i]
            
            if value > best_value:
                best_value = value
                best_subset_index = i
        
        # 如果没有找到合适的子集，说明无法覆盖全集（理论上不应该发生，因为前面已经检查过）
        if best_subset_index == -1:
            raise ValueError("无法覆盖全集")
        
        # 选择该子集
        selected_subsets.append(best_subset_index)
        total_weight += weights[i]
        
        # 更新未被覆盖的元素集合
        uncovered -= subsets[best_subset_index]
    
    return selected_subsets, total_weight


def set_cover_leetcode_1541(s):
    """
    LeetCode 1541. 平衡括号字符串的最少插入次数
    题目链接：https://leetcode-cn.com/problems/minimum-insertions-to-balance-a-parentheses-string/
    
    问题描述：
    给你一个括号字符串 s ，请你返回满足以下条件的 最少 插入次数：
    - 任何左括号 '(' 必须有相应的两个右括号 '))'
    - 左括号 '(' 必须在对应的连续两个右括号 '))' 之前
    
    解题思路：
    这是一个变种的括号匹配问题，可以使用贪心算法来解决。
    我们维护两个变量：需要的右括号数量和需要添加的左括号数量。
    遍历字符串，根据当前字符和状态更新这两个变量。
    
    Args:
        s: 括号字符串
    
    Returns:
        int: 最少需要插入的字符数
    """
    insert_count = 0  # 需要插入的字符数
    need_right = 0    # 需要的右括号数量
    
    for c in s:
        if c == '(':
            need_right += 2  # 每个左括号需要两个右括号
            
            # 如果需要的右括号数量是奇数，说明前一个字符需要补充一个右括号
            if need_right % 2 == 1:
                insert_count += 1  # 插入一个右括号
                need_right -= 1    # 需要的右括号数量减1
        else:  # c == ')'
            need_right -= 1
            
            # 如果右括号过多，需要添加一个左括号
            if need_right == -1:
                insert_count += 1  # 插入一个左括号
                need_right = 1     # 现在需要一个右括号
    
    return insert_count + need_right


def set_cover_leetcode_1689(s):
    """
    LeetCode 1689. 十-二进制数的最少数目
    题目链接：https://leetcode-cn.com/problems/partitioning-into-minimum-number-of-deci-binary-numbers/
    
    问题描述：
    如果一个十进制数字不含任何前导零，且每一位上的数字不是 0 就是 1，那么该数字就是一个 十-二进制数 。
    例如，101 和 1100 都是 十-二进制数，而 112 和 3001 不是。
    给你一个表示十进制整数的字符串 n ，返回和为 n 的 十-二进制数 的最少数目。
    
    解题思路：
    这个问题可以转化为：找到字符串中最大的数字。因为对于每一位的数字d，我们需要至少d个十-二进制数，
    每个数在该位上提供1。
    
    Args:
        s: 表示十进制整数的字符串
    
    Returns:
        int: 和为s的十-二进制数的最少数目
    """
    return max(int(c) for c in s)


def set_cover_leetcode_45(nums):
    """
    LeetCode 45. 跳跃游戏 II
    题目链接：https://leetcode-cn.com/problems/jump-game-ii/
    
    问题描述：
    给定一个非负整数数组，你最初位于数组的第一个位置。
    数组中的每个元素代表你在该位置可以跳跃的最大长度。
    你的目标是使用最少的跳跃次数到达数组的最后一个位置。
    
    解题思路：
    使用贪心算法，每次都跳转到能够到达的最远位置。
    具体来说，我们维护当前可以到达的最远位置end和下一跳可以到达的最远位置farthest。
    当我们到达end时，更新end为farthest并增加跳跃次数。
    
    Args:
        nums: 非负整数数组
    
    Returns:
        int: 最少跳跃次数
    """
    n = len(nums)
    if n <= 1:
        return 0
    
    jumps = 0      # 跳跃次数
    current_end = 0  # 当前可以到达的最远位置
    current_farthest = 0  # 下一跳可以到达的最远位置
    
    for i in range(n - 1):
        # 更新下一跳可以到达的最远位置
        current_farthest = max(current_farthest, i + nums[i])
        
        # 如果到达了当前可以到达的边界，必须跳一次
        if i == current_end:
            jumps += 1
            current_end = current_farthest
    
    return jumps


# 测试代码
if __name__ == "__main__":
    # 测试子集覆盖算法
    universe = {1, 2, 3, 4, 5}
    subsets = [{1, 2, 3}, {2, 4}, {3, 4}, {4, 5}]
    weights = [5, 10, 3, 8]
    
    try:
        selected, total_weight = set_cover(universe, subsets, weights)
        print(f"选中的子集索引: {selected}")
        print(f"总权值: {total_weight}")
    except ValueError as e:
        print(f"错误: {e}")
    
    # 测试LeetCode 1541
    s1 = "(()))"
    print(f"最少插入次数: {set_cover_leetcode_1541(s1)}")  # 应该输出 1
    
    # 测试LeetCode 1689
    s2 = "32"
    print(f"十-二进制数的最少数目: {set_cover_leetcode_1689(s2)}")  # 应该输出 3
    
    # 测试LeetCode 45
    nums = [2, 3, 1, 1, 4]
    print(f"最少跳跃次数: {set_cover_leetcode_45(nums)}")  # 应该输出 2