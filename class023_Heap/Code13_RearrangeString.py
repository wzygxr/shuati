import heapq
from collections import Counter

class Solution:
    """
    相关题目13: LeetCode 767. 重构字符串
    题目链接: https://leetcode.cn/problems/reorganize-string/
    题目描述: 给定一个字符串S，检查是否能重新排布其中的字母，使得两相邻的字符不同。
    若可行，输出任意可行的结果。若不可行，返回空字符串。
    解题思路: 使用最大堆按字符频率排序，然后贪心选择频率最高的字符进行放置
    时间复杂度: O(n log k)，其中n是字符串长度，k是不同字符的数量（最大为26）
    空间复杂度: O(k)，用于存储字符频率和堆
    是否最优解: 此方法是最优解，没有更优的算法
    
    本题属于堆的应用场景：基于频率的优先级处理问题
    """
    
    def reorganizeString(self, s: str) -> str:
        """
        重构字符串，使得相邻字符不同
        
        Args:
            s: 输入字符串
            
        Returns:
            str: 重构后的字符串，如果无法重构则返回空字符串
        """
        # 异常处理：检查输入字符串是否为空
        if not s:
            return ""
        
        # 统计每个字符的出现频率
        char_count = Counter(s)
        
        # 检查是否可以重构：最多的字符出现次数不能超过(len(s)+1)//2
        max_count = max(char_count.values())
        if max_count > (len(s) + 1) // 2:
            return ""
        
        # 创建最大堆（Python的heapq是最小堆，所以用负数表示频率）
        max_heap = [(-count, char) for char, count in char_count.items()]
        heapq.heapify(max_heap)
        
        # 用于存储重构后的字符串
        result = []
        
        # 当堆中有元素时
        while max_heap:
            # 获取当前频率最高的字符
            count1, char1 = heapq.heappop(max_heap)
            
            # 如果结果为空或当前字符与结果最后一个字符不同，直接添加
            if not result or char1 != result[-1]:
                result.append(char1)
                # 如果字符还有剩余，将其放回堆中
                if count1 < -1:
                    heapq.heappush(max_heap, (count1 + 1, char1))
            else:
                # 如果当前字符与结果最后一个字符相同，需要选择下一个最高频率的字符
                # 如果堆为空，说明无法重构
                if not max_heap:
                    return ""
                
                # 获取次高频率的字符
                count2, char2 = heapq.heappop(max_heap)
                result.append(char2)
                # 如果次高频率字符还有剩余，将其放回堆中
                if count2 < -1:
                    heapq.heappush(max_heap, (count2 + 1, char2))
                
                # 将最高频率字符放回堆中
                heapq.heappush(max_heap, (count1, char1))
        
        return ''.join(result)

class AlternativeSolution:
    """
    另一种实现方式，使用贪心算法直接构建结果字符串
    这种方法可能在某些情况下更直观
    """
    
    def reorganizeString(self, s: str) -> str:
        """
        使用贪心算法重构字符串
        
        Args:
            s: 输入字符串
            
        Returns:
            str: 重构后的字符串，如果无法重构则返回空字符串
        """
        if not s:
            return ""
        
        # 统计字符频率
        char_count = Counter(s)
        n = len(s)
        
        # 找出频率最高的字符
        max_char = max(char_count.keys(), key=lambda x: char_count[x])
        max_count = char_count[max_char]
        
        # 检查是否可以重构
        if max_count > (n + 1) // 2:
            return ""
        
        # 创建结果数组
        result = [''] * n
        index = 0
        
        # 首先放置频率最高的字符，间隔放置
        while char_count[max_char] > 0:
            result[index] = max_char
            index += 2
            char_count[max_char] -= 1
        
        # 放置剩余的字符
        for char, count in char_count.items():
            while count > 0:
                # 如果到达数组末尾，从索引1开始
                if index >= n:
                    index = 1
                result[index] = char
                index += 2
                count -= 1
        
        return ''.join(result)

class OptimizedHeapSolution:
    """
    优化的堆实现，使用更简洁的逻辑
    """
    
    def reorganizeString(self, s: str) -> str:
        """
        使用优化的堆方法重构字符串
        
        Args:
            s: 输入字符串
            
        Returns:
            str: 重构后的字符串，如果无法重构则返回空字符串
        """
        if not s:
            return ""
        
        # 统计字符频率
        char_count = Counter(s)
        max_count = max(char_count.values())
        n = len(s)
        
        # 快速检查是否可能重构
        if max_count > (n + 1) // 2:
            return ""
        
        # 创建最大堆（使用负数频率）
        max_heap = [(-count, char) for char, count in char_count.items()]
        heapq.heapify(max_heap)
        
        result = []
        
        # 不断从堆中取出两个字符添加到结果中
        # 这样可以确保不会有相同字符相邻
        while len(max_heap) >= 2:
            count1, char1 = heapq.heappop(max_heap)
            count2, char2 = heapq.heappop(max_heap)
            
            # 添加两个不同的字符
            result.extend([char1, char2])
            
            # 如果字符还有剩余，放回堆中
            if count1 < -1:
                heapq.heappush(max_heap, (count1 + 1, char1))
            if count2 < -1:
                heapq.heappush(max_heap, (count2 + 1, char2))
        
        # 如果堆中还有一个字符，说明字符串长度为奇数，添加最后一个字符
        if max_heap:
            result.append(max_heap[0][1])
        
        return ''.join(result)

# 测试函数，验证算法在不同输入情况下的正确性
def test_reorganize_string():
    print("=== 测试重构字符串算法 ===")
    solution = Solution()
    alternative_solution = AlternativeSolution()
    optimized_solution = OptimizedHeapSolution()
    
    # 测试用例1：基本用例 - 可以重构
    print("\n测试用例1：基本用例 - 可以重构")
    s1 = "aab"
    result1 = solution.reorganizeString(s1)
    alt_result1 = alternative_solution.reorganizeString(s1)
    opt_result1 = optimized_solution.reorganizeString(s1)
    
    print(f"原字符串: {s1}")
    print(f"堆方法结果: {result1}, 有效: {_is_valid_reorganization(s1, result1)}")
    print(f"贪心方法结果: {alt_result1}, 有效: {_is_valid_reorganization(s1, alt_result1)}")
    print(f"优化堆方法结果: {opt_result1}, 有效: {_is_valid_reorganization(s1, opt_result1)}")
    
    # 测试用例2：基本用例 - 可以重构
    print("\n测试用例2：基本用例 - 可以重构")
    s2 = "aaab"
    result2 = solution.reorganizeString(s2)
    alt_result2 = alternative_solution.reorganizeString(s2)
    opt_result2 = optimized_solution.reorganizeString(s2)
    
    print(f"原字符串: {s2}")
    print(f"堆方法结果: {result2}, 有效: {_is_valid_reorganization(s2, result2)}")
    print(f"贪心方法结果: {alt_result2}, 有效: {_is_valid_reorganization(s2, alt_result2)}")
    print(f"优化堆方法结果: {opt_result2}, 有效: {_is_valid_reorganization(s2, opt_result2)}")
    
    # 测试用例3：无法重构的情况
    print("\n测试用例3：无法重构的情况")
    s3 = "aaabbc"
    result3 = solution.reorganizeString(s3)
    alt_result3 = alternative_solution.reorganizeString(s3)
    opt_result3 = optimized_solution.reorganizeString(s3)
    
    print(f"原字符串: {s3}")
    print(f"堆方法结果: {result3}, 有效: {_is_valid_reorganization(s3, result3)}")
    print(f"贪心方法结果: {alt_result3}, 有效: {_is_valid_reorganization(s3, alt_result3)}")
    print(f"优化堆方法结果: {opt_result3}, 有效: {_is_valid_reorganization(s3, opt_result3)}")
    
    # 测试用例4：单字符
    print("\n测试用例4：单字符")
    s4 = "a"
    result4 = solution.reorganizeString(s4)
    alt_result4 = alternative_solution.reorganizeString(s4)
    opt_result4 = optimized_solution.reorganizeString(s4)
    
    print(f"原字符串: {s4}")
    print(f"堆方法结果: {result4}, 有效: {_is_valid_reorganization(s4, result4)}")
    print(f"贪心方法结果: {alt_result4}, 有效: {_is_valid_reorganization(s4, alt_result4)}")
    print(f"优化堆方法结果: {opt_result4}, 有效: {_is_valid_reorganization(s4, opt_result4)}")
    
    # 测试用例5：所有字符相同
    print("\n测试用例5：所有字符相同")
    s5 = "aaaaa"
    result5 = solution.reorganizeString(s5)
    alt_result5 = alternative_solution.reorganizeString(s5)
    opt_result5 = optimized_solution.reorganizeString(s5)
    
    print(f"原字符串: {s5}")
    print(f"堆方法结果: {result5}, 有效: {_is_valid_reorganization(s5, result5)}")
    print(f"贪心方法结果: {alt_result5}, 有效: {_is_valid_reorganization(s5, alt_result5)}")
    print(f"优化堆方法结果: {opt_result5}, 有效: {_is_valid_reorganization(s5, opt_result5)}")
    
    # 测试用例6：所有字符都不同
    print("\n测试用例6：所有字符都不同")
    s6 = "abcdef"
    result6 = solution.reorganizeString(s6)
    alt_result6 = alternative_solution.reorganizeString(s6)
    opt_result6 = optimized_solution.reorganizeString(s6)
    
    print(f"原字符串: {s6}")
    print(f"堆方法结果: {result6}, 有效: {_is_valid_reorganization(s6, result6)}")
    print(f"贪心方法结果: {alt_result6}, 有效: {_is_valid_reorganization(s6, alt_result6)}")
    print(f"优化堆方法结果: {opt_result6}, 有效: {_is_valid_reorganization(s6, opt_result6)}")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    
    # 创建一个较大的可重构的字符串
    large_s = "aabbccddeeffgghh" * 1000  # 总长度 24000
    
    # 测试堆方法性能
    start_time = time.time()
    large_result = solution.reorganizeString(large_s)
    heap_time = time.time() - start_time
    print(f"堆方法处理大字符串用时: {heap_time:.6f}秒, 结果有效: {_is_valid_reorganization(large_s, large_result)}")
    
    # 测试贪心方法性能
    start_time = time.time()
    large_alt_result = alternative_solution.reorganizeString(large_s)
    greedy_time = time.time() - start_time
    print(f"贪心方法处理大字符串用时: {greedy_time:.6f}秒, 结果有效: {_is_valid_reorganization(large_s, large_alt_result)}")
    
    # 测试优化堆方法性能
    start_time = time.time()
    large_opt_result = optimized_solution.reorganizeString(large_s)
    opt_heap_time = time.time() - start_time
    print(f"优化堆方法处理大字符串用时: {opt_heap_time:.6f}秒, 结果有效: {_is_valid_reorganization(large_s, large_opt_result)}")
    
    # 性能比较
    print("\n性能比较:")
    print(f"堆方法 vs 贪心方法: {'贪心方法更快' if greedy_time < heap_time else '堆方法更快'} 约 {(max(heap_time, greedy_time) / min(heap_time, greedy_time)):.2f}倍")
    print(f"堆方法 vs 优化堆方法: {'优化堆方法更快' if opt_heap_time < heap_time else '堆方法更快'} 约 {(max(heap_time, opt_heap_time) / min(heap_time, opt_heap_time)):.2f}倍")
    print(f"贪心方法 vs 优化堆方法: {'优化堆方法更快' if opt_heap_time < greedy_time else '贪心方法更快'} 约 {(max(greedy_time, opt_heap_time) / min(greedy_time, opt_heap_time)):.2f}倍")

# 辅助函数，验证重构后的字符串是否有效
def _is_valid_reorganization(original: str, reorganized: str) -> bool:
    """
    验证重构后的字符串是否有效：
    1. 长度与原字符串相同
    2. 相邻字符不同
    3. 包含原字符串的所有字符
    """
    # 检查是否为空字符串且原字符串不为空
    if not reorganized and original:
        # 检查是否真的无法重构
        char_count = Counter(original)
        max_count = max(char_count.values())
        return max_count > (len(original) + 1) // 2
    
    # 检查长度
    if len(original) != len(reorganized):
        return False
    
    # 检查相邻字符是否不同
    for i in range(1, len(reorganized)):
        if reorganized[i] == reorganized[i-1]:
            return False
    
    # 检查字符频率是否匹配
    return Counter(original) == Counter(reorganized)

# 运行测试
if __name__ == "__main__":
    test_reorganize_string()

# 解题思路总结：
# 1. 问题分析：
#    - 要重新排列字符串，使得相邻字符不同
#    - 关键条件：最高频率字符的出现次数不能超过(len(s)+1)//2
#    - 如果最高频率字符次数超过这个阈值，无法重构
# 
# 2. 堆方法（优先队列）：
#    - 统计每个字符的频率
#    - 将字符及其频率放入最大堆（Python中用最小堆模拟，所以频率取负数）
#    - 每次从堆中取出频率最高的字符添加到结果中
#    - 如果当前字符与结果最后一个字符相同，则取出下一个最高频率的字符
#    - 将使用过的字符（如果还有剩余）重新放回堆中
#    - 时间复杂度：O(n log k)，其中n是字符串长度，k是不同字符的数量
#    - 空间复杂度：O(k)
# 
# 3. 贪心方法：
#    - 先放置频率最高的字符，间隔放置
#    - 然后放置剩余的字符
#    - 时间复杂度：O(n)
#    - 空间复杂度：O(k)
# 
# 4. 优化堆方法：
#    - 每次从堆中取出两个不同的字符添加到结果中
#    - 这样可以确保相邻字符不同
#    - 最后如果还有一个字符（字符串长度为奇数），直接添加
#    - 时间复杂度：O(n log k)
#    - 空间复杂度：O(k)
# 
# 5. 边界情况处理：
#    - 空字符串
#    - 单字符字符串
#    - 所有字符相同的字符串
#    - 所有字符都不同的字符串
#    - 最高频率字符刚好达到阈值的情况
# 
# 6. 堆方法的优势：
#    - 自动维护元素的优先级
#    - 适合需要频繁获取最高优先级元素的场景
#    - 在这里用于贪心选择频率最高的字符进行放置
# 
# 7. 应用场景：
#    - 字符重排问题
#    - 任务调度问题（优先处理高优先级任务）
#    - 资源分配问题（基于某种优先级分配资源）