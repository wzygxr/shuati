# 重构字符串
# 给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
# 如果可以，输出任意可行的结果。如果不可行，返回空字符串。
# 测试链接: https://leetcode.cn/problems/reorganize-string/

import heapq
from collections import Counter

class Solution:
    def reorganizeString(self, s: str) -> str:
        """
        重构字符串问题的贪心解法
        
        解题思路：
        1. 统计每个字符的出现频率
        2. 使用最大堆存储字符及其频率，按频率降序排列
        3. 每次从堆中取出频率最高的两个字符，交替放置
        4. 如果某个字符频率过高，无法重构，返回空字符串
        
        贪心策略的正确性：
        局部最优：每次选择频率最高的两个字符交替放置
        全局最优：得到相邻字符不同的字符串
        
        时间复杂度：O(n log k)，其中k是字符种类数
        空间复杂度：O(k)，用于存储字符频率的堆
        
        Args:
            s: 输入字符串
            
        Returns:
            重构后的字符串或空字符串
        """
        # 边界条件处理
        if not s:
            return ""
        
        # 统计字符频率
        freq = Counter(s)
        
        # 使用最大堆存储字符频率（Python的heapq是最小堆，所以用负数）
        max_heap = [(-count, char) for char, count in freq.items()]
        heapq.heapify(max_heap)
        
        # 如果最高频率超过一半加一，无法重构
        max_count = -max_heap[0][0]
        if max_count > (len(s) + 1) // 2:
            return ""
        
        # 构建结果字符串
        result = []
        
        while len(max_heap) >= 2:
            # 取出频率最高的两个字符
            count1, char1 = heapq.heappop(max_heap)
            count2, char2 = heapq.heappop(max_heap)
            
            # 交替放置这两个字符
            result.append(char1)
            result.append(char2)
            
            # 减少频率并重新加入堆中
            if count1 + 1 < 0:  # 因为count是负数，所以+1表示减少
                heapq.heappush(max_heap, (count1 + 1, char1))
            if count2 + 1 < 0:
                heapq.heappush(max_heap, (count2 + 1, char2))
        
        # 处理最后一个字符（如果有）
        if max_heap:
            count, char = heapq.heappop(max_heap)
            result.append(char)
        
        return ''.join(result)
    
    def reorganizeString2(self, s: str) -> str:
        """
        重构字符串问题的另一种解法（基于奇偶位置）
        
        解题思路：
        1. 统计字符频率，找到最高频率字符
        2. 如果最高频率超过一半加一，无法重构
        3. 将最高频率字符放在偶数位置，其他字符放在奇数位置
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not s:
            return ""
        
        # 统计字符频率
        freq = Counter(s)
        
        # 找到最高频率字符
        max_char = max(freq, key=lambda x: freq[x])
        max_count = freq[max_char]
        
        # 检查是否可重构
        if max_count > (len(s) + 1) // 2:
            return ""
        
        # 创建结果数组
        result = [''] * len(s)
        index = 0
        
        # 先放置最高频率字符在偶数位置
        while freq[max_char] > 0:
            result[index] = max_char
            index += 2
            freq[max_char] -= 1
            
            # 如果偶数位置用完，转到奇数位置
            if index >= len(s):
                index = 1
        
        # 放置其他字符
        for char, count in freq.items():
            while count > 0:
                if index >= len(s):
                    index = 1
                result[index] = char
                index += 2
                count -= 1
        
        return ''.join(result)

# 测试代码
def test_reorganize_string():
    solution = Solution()
    
    # 测试用例1
    # 输入: s = "aab"
    # 输出: "aba"
    s1 = "aab"
    print(f"测试用例1结果: {solution.reorganizeString(s1)}")  # 期望输出: "aba"
    
    # 测试用例2
    # 输入: s = "aaab"
    # 输出: ""
    # 解释: 无法重构，因为'a'出现次数过多
    s2 = "aaab"
    print(f"测试用例2结果: {solution.reorganizeString(s2)}")  # 期望输出: ""
    
    # 测试用例3
    # 输入: s = "vvvlo"
    # 输出: "vlvov" 或 "vovlv"
    s3 = "vvvlo"
    result3 = solution.reorganizeString(s3)
    print(f"测试用例3结果: {result3}")  # 期望输出非空
    print(f"测试用例3长度验证: {len(result3) == len(s3)}")  # 长度应该相同
    
    # 测试用例4：边界情况
    # 输入: s = "a"
    # 输出: "a"
    s4 = "a"
    print(f"测试用例4结果: {solution.reorganizeString(s4)}")  # 期望输出: "a"
    
    # 测试用例5：复杂情况
    # 输入: s = "abbcccdddd"
    # 输出: 非空字符串
    s5 = "abbcccdddd"
    result5 = solution.reorganizeString(s5)
    print(f"测试用例5结果: {result5}")  # 期望输出非空
    print(f"测试用例5长度验证: {len(result5) == len(s5)}")  # 长度应该相同
    
    # 测试用例6：极限情况
    # 输入: s = "aaaaabbbcc"
    # 输出: 非空字符串
    s6 = "aaaaabbbcc"
    result6 = solution.reorganizeString(s6)
    print(f"测试用例6结果: {result6}")  # 期望输出非空
    print(f"测试用例6长度验证: {len(result6) == len(s6)}")  # 长度应该相同

if __name__ == "__main__":
    test_reorganize_string()