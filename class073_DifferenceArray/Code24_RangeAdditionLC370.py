"""
LeetCode 370. Range Addition

题目描述:
假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
你将会被给出 k 个更新的操作。
其中，每个操作会被表示为一个三元组：[startIndex, endIndex, inc]，
你需要将子数组 A[startIndex ... endIndex]（包括 startIndex 和 endIndex）增加 inc。

示例:
输入: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
输出: [-2,0,3,5,3]
解释:
初始状态: [0,0,0,0,0]
进行了操作 [1,3,2] 后的状态: [0,2,2,2,0]
进行了操作 [2,4,3] 后的状态: [0,2,5,5,3]
进行了操作 [0,2,-2] 后的状态: [-2,0,3,5,3]

提示:
1 <= length <= 10^5
0 <= updates.length <= 10^4
0 <= startIndex <= endIndex < length
-1000 <= inc <= 1000

题目链接: https://leetcode.com/problems/range-addition/

解题思路:
使用差分数组技巧来优化区间更新操作。
1. 创建一个差分数组diff，大小为n+1
2. 对于每个操作[startIndex, endIndex, inc]，执行diff[startIndex] += inc和diff[endIndex+1] -= inc
3. 对差分数组计算前缀和，得到最终数组

时间复杂度: O(n + k) - 需要遍历所有操作和数组一次
空间复杂度: O(n) - 需要额外的差分数组空间

这是最优解，因为需要处理所有操作，而且数组大小可能很大，不能使用暴力方法。
"""


def get_modified_array(length, updates):
    """
    计算区间加法后的数组
    
    Args:
        length: 数组长度
        updates: 更新操作数组，每个操作包含[起始索引, 结束索引, 增加值]
        
    Returns:
        更新后的数组
        
    时间复杂度: O(n + k) - 需要遍历所有操作和数组一次
    空间复杂度: O(n) - 需要额外的差分数组空间
    
    工程化考量:
    1. 边界处理: 处理空数组和空操作情况
    2. 异常处理: 验证输入参数的合法性
    3. 性能优化: 使用差分数组将区间更新操作从O(n)优化到O(1)
    4. 可读性: 变量命名清晰，注释详细
    """
    # 边界情况处理
    if length <= 0:
        return []
    
    if not updates:
        return [0] * length
    
    # 创建差分数组，大小为length+1以便处理边界情况
    diff = [0] * (length + 1)
    
    # 处理每个更新操作
    for update in updates:
        start_index = update[0]    # 起始索引
        end_index = update[1]      # 结束索引
        inc = update[2]           # 增加值
        
        # 在差分数组中标记区间更新
        diff[start_index] += inc       # 在起始位置增加inc
        if end_index + 1 < length:
            diff[end_index + 1] -= inc # 在结束位置之后减少inc
    
    # 通过计算差分数组的前缀和得到最终数组
    result = [0] * length
    result[0] = diff[0]
    for i in range(1, length):
        result[i] = result[i - 1] + diff[i]
    
    return result


def main():
    """测试用例"""
    # 测试用例1
    length1 = 5
    updates1 = [[1, 3, 2], [2, 4, 3], [0, 2, -2]]
    result1 = get_modified_array(length1, updates1)
    # 预期输出: [-2, 0, 3, 5, 3]
    print("测试用例1:", result1)

    # 测试用例2
    length2 = 10
    updates2 = [[2, 4, 6], [5, 6, 8], [1, 9, -4]]
    result2 = get_modified_array(length2, updates2)
    # 预期输出: [0, -4, 2, 2, 2, 4, 4, -4, -4, -4]
    print("测试用例2:", result2)
    
    # 测试用例3: 边界情况
    length3 = 1
    updates3 = [[0, 0, 5]]
    result3 = get_modified_array(length3, updates3)
    # 预期输出: [5]
    print("测试用例3:", result3)


if __name__ == "__main__":
    main()