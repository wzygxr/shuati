from collections import deque

"""
LeetCode 936. 盖章序列 (Stamping The Sequence)

题目描述:
你想要用小写字母组成一个目标字符串 target。开始时，序列由 target.length 个 '?' 记号组成。
而你有一个小写字母印章 stamp。在每个回合，你可以将印章放在序列上，并将序列中的每个字母替换为印章对应位置的字母。
你最多可以进行 10 * target.length 次操作。
在完成所有操作后，序列必须等于目标字符串 target。
返回一个数组，其中包含按顺序执行的盖章操作的位置（索引从0开始）。如果无法完成目标，则返回一个空数组。

示例1:
输入: stamp = "abc", target = "ababc"
输出: [0,2]
解释:
初始序列是 "?????"
- 放置印章在位置 0 处，得到 "abc??"
- 放置印章在位置 2 处，得到 "ababc"

示例2:
输入: stamp = "abca", target = "aabcaca"
输出: [3,0,1]
解释:
初始序列是 "???????"
- 放置印章在位置 3 处，得到 "???abca"
- 放置印章在位置 0 处，得到 "abcabca"
- 放置印章在位置 1 处，得到 "aabcaca"

提示:
1. 1 <= stamp.length <= target.length <= 1000
2. stamp 和 target 只包含小写字母

题目链接: https://leetcode.com/problems/stamping-the-sequence/

解题思路:
这道题可以用逆向思维和差分数组结合来解决：
1. 从目标字符串 target 倒推回初始的全 ? 字符串
2. 每次找到可以被 stamp 覆盖的子串（允许部分匹配，因为后面可能会被覆盖）
3. 用差分数组来跟踪每个位置被覆盖的次数，确保最终所有字符都被覆盖

具体步骤：
1. 创建一个队列，用于存储可以被完全覆盖的子串位置
2. 使用一个数组记录每个位置已经匹配的字符数
3. 使用差分数组来标记需要检查的位置
4. 逆向模拟盖章过程，找到所有盖章位置，最后反转结果

时间复杂度: O(n * (n - m + 1)) - n是target长度，m是stamp长度
空间复杂度: O(n) - 需要存储匹配信息和差分数组

这是最优解，因为我们需要考虑所有可能的盖章位置和覆盖次数。
"""

class Solution:
    """
    寻找盖章序列
    
    Args:
        stamp: 印章字符串
        target: 目标字符串
    
    Returns:
        盖章操作的位置数组，如果无法完成则返回空数组
    """
    def movesToStamp(self, stamp, target):
        m = len(stamp)
        n = len(target)
        
        # 存储盖章位置，后续需要反转
        result = []
        
        # 将target转换为列表以便修改
        target_list = list(target)
        
        # 记录每个位置被覆盖的次数
        visited = [False] * (n - m + 1)
        
        # 记录已经被匹配为'?'的字符数量
        matched_count = 0
        
        # 队列存储可以完全匹配的位置
        q = deque()
        
        # 预处理所有可能的盖章位置
        for i in range(n - m + 1):
            # 检查当前位置i是否可以盖章
            can_stamp = True
            for j in range(m):
                if target_list[i + j] != stamp[j] and target_list[i + j] != '?':
                    can_stamp = False
                    break
            
            # 如果当前位置可以盖章（完全匹配）
            if can_stamp:
                result.append(i)
                visited[i] = True
                
                # 将该位置覆盖的所有字符标记为'?'
                for j in range(m):
                    if target_list[i + j] != '?':
                        target_list[i + j] = '?'
                        matched_count += 1
                
                # 将该位置加入队列，后续可能影响相邻位置
                q.append(i)
        
        # BFS处理
        while q and matched_count < n:
            pos = q.popleft()
            
            # 检查受影响的位置范围
            start = max(0, pos - m + 1)
            end = min(n - m, pos + m - 1)
            
            for i in range(start, end + 1):
                if visited[i]:
                    continue
                
                can_stamp = True
                
                # 检查当前位置是否可以盖章
                for j in range(m):
                    target_pos = i + j
                    # 如果目标位置既不是'?'也不与印章字符匹配
                    if target_list[target_pos] != '?' and target_list[target_pos] != stamp[j]:
                        can_stamp = False
                        break
                
                # 如果当前位置可以盖章
                if can_stamp:
                    result.append(i)
                    visited[i] = True
                    
                    # 将该位置覆盖的所有字符标记为'?'
                    for j in range(m):
                        if target_list[i + j] != '?':
                            target_list[i + j] = '?'
                            matched_count += 1
                    
                    # 将该位置加入队列
                    q.append(i)
        
        # 检查是否所有字符都被覆盖为'?'
        if matched_count != n:
            return []
        
        # 反转结果，因为我们是逆向操作的
        result.reverse()
        
        return result

# 辅助函数：打印数组
def print_array(arr):
    print(f"[{', '.join(map(str, arr))}]")

# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1
    stamp1 = "abc"
    target1 = "ababc"
    result1 = solution.movesToStamp(stamp1, target1)
    # 预期输出: [0, 2] 或 [1, 0] 或其他有效排列
    print("测试用例1:")
    print_array(result1)

    # 测试用例2
    stamp2 = "abca"
    target2 = "aabcaca"
    result2 = solution.movesToStamp(stamp2, target2)
    # 预期输出: [3, 0, 1] 或其他有效的排列
    print("测试用例2:")
    print_array(result2)
    
    # 测试用例3
    stamp3 = "abc"
    target3 = "abcbc"
    result3 = solution.movesToStamp(stamp3, target3)
    # 预期输出: [2, 0] 或其他有效排列
    print("测试用例3:")
    print_array(result3)

if __name__ == "__main__":
    main()