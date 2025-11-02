# 划分字母区间
# 字符串 S 由小写字母组成。
# 我们要把这个字符串划分为尽可能多的片段，
# 同一字母最多出现在一个片段中。
# 返回一个表示每个字符串片段的长度的列表。
# 测试链接: https://leetcode.cn/problems/partition-labels/

def partitionLabels(s):
    """
    划分字母区间问题的贪心解法
    
    解题思路：
    1. 首先遍历字符串，记录每个字母最后出现的位置
    2. 贪心策略：尽可能早地划分区间，但要保证同一字母只出现在一个片段中
    3. 遍历字符串，维护当前片段的起始位置和结束位置
    4. 当遍历到当前片段的结束位置时，完成一个片段的划分
    
    贪心策略的正确性：
    1. 为了使片段数量尽可能多，我们应该尽早划分片段
    2. 但在划分时必须保证同一字母只出现在一个片段中
    3. 通过记录每个字母最后出现的位置，我们可以确定当前片段的边界
    
    时间复杂度：O(n)，需要遍历字符串两次
    空间复杂度：O(1)，只使用了固定大小的数组存储字母最后位置
    
    :param s: 输入字符串
    :return: 表示每个字符串片段长度的列表
    """
    # 边界条件处理：如果字符串为空，则返回空列表
    if not s:
        return []

    # 1. 记录每个字母最后出现的位置
    last = [0] * 26  # 26个小写字母
    for i in range(len(s)):
        last[ord(s[i]) - ord('a')] = i

    # 2. 初始化结果列表和当前片段的起始、结束位置
    result = []
    start = 0   # 当前片段的起始位置
    end = 0     # 当前片段的结束位置

    # 3. 遍历字符串
    for i in range(len(s)):
        # 4. 更新当前片段的结束位置为当前字符最后出现位置和当前结束位置的最大值
        end = max(end, last[ord(s[i]) - ord('a')])

        # 5. 如果到达当前片段的结束位置，完成一个片段的划分
        if i == end:
            result.append(end - start + 1)  # 添加当前片段长度
            start = end + 1                 # 更新下一个片段的起始位置

    # 6. 返回结果列表
    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    # 输入: s = "ababcbacadefegdehijhklij"
    # 输出: [9,7,8]
    # 解释: 划分结果为 "ababcbaca", "defegde", "hijhklij"
    s1 = "ababcbacadefegdehijhklij"
    print("测试用例1结果:", partitionLabels(s1))  # 期望输出: [9, 7, 8]

    # 测试用例2
    # 输入: s = "eccbbbbdec"
    # 输出: [10]
    s2 = "eccbbbbdec"
    print("测试用例2结果:", partitionLabels(s2))  # 期望输出: [10]

    # 测试用例3：边界情况
    # 输入: s = "a"
    # 输出: [1]
    s3 = "a"
    print("测试用例3结果:", partitionLabels(s3))  # 期望输出: [1]

    # 测试用例4：复杂情况
    # 输入: s = "abcdef"
    # 输出: [1, 1, 1, 1, 1, 1]
    s4 = "abcdef"
    print("测试用例4结果:", partitionLabels(s4))  # 期望输出: [1, 1, 1, 1, 1, 1]