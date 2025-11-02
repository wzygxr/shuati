import heapq
import time
import random
from collections import Counter
from typing import Tuple, List

class Code29_ReorganizeString:
    """
    重构字符串
    
    题目描述：
    给定一个字符串 s ，检查是否能重新排布其中的字母，使得两相邻的字符不同。
    若可行，输出任意可行的结果。若不可行，返回空字符串。
    
    来源：LeetCode 767
    链接：https://leetcode.cn/problems/reorganize-string/
    
    算法思路：
    使用贪心算法 + 优先队列：
    1. 统计每个字符的出现频率
    2. 如果某个字符的频率超过字符串长度的一半，则无法重构，返回空字符串
    3. 使用最大堆（按频率排序）存储字符
    4. 每次从堆中取出频率最高的两个字符，交替排列
    5. 如果堆中还有剩余字符，继续处理
    
    时间复杂度：O(n * logk) - n是字符串长度，k是字符种类数
    空间复杂度：O(k) - 优先队列和频率统计的空间
    
    关键点分析：
    - 贪心策略：每次选择频率最高的两个字符交替排列
    - 边界处理：检查是否有字符频率超过一半
    - 异常场景：单字符字符串的处理
    
    工程化考量：
    - 输入验证：检查字符串是否为空
    - 性能优化：使用Counter统计频率
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def reorganize_string(s: str) -> str:
        """
        重构字符串，使得相邻字符不同
        
        Args:
            s: 输入字符串
            
        Returns:
            str: 重构后的字符串，如果无法重构返回空字符串
        """
        # 输入验证
        if not s:
            return ""
        
        n = len(s)
        
        # 统计字符频率
        freq = Counter(s)
        
        # 检查是否有字符频率超过一半（向上取整）
        max_freq = max(freq.values()) if freq else 0
        
        # 如果最大频率超过 (n+1)/2，则无法重构
        if max_freq > (n + 1) // 2:
            return ""
        
        # 使用最大堆存储字符和频率（Python的heapq是最小堆，所以使用负数）
        max_heap = [(-count, char) for char, count in freq.items()]
        heapq.heapify(max_heap)
        
        result = []
        
        while max_heap:
            # 取出频率最高的字符
            count1, char1 = heapq.heappop(max_heap)
            
            if not result or result[-1] != char1:
                # 如果结果为空或最后一个字符不同，直接添加
                result.append(char1)
                count1 += 1  # 因为存储的是负数，所以加1相当于减1
                
                if count1 < 0:
                    heapq.heappush(max_heap, (count1, char1))
            else:
                # 如果需要交替，但堆为空，无法重构
                if not max_heap:
                    return ""
                
                # 取出第二个字符
                count2, char2 = heapq.heappop(max_heap)
                result.append(char2)
                count2 += 1
                
                # 将两个字符重新加入堆中
                if count2 < 0:
                    heapq.heappush(max_heap, (count2, char2))
                heapq.heappush(max_heap, (count1, char1))
        
        return "".join(result)
    
    @staticmethod
    def reorganize_string_alternate(s: str) -> str:
        """
        另一种实现：更简洁的交替排列方法
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not s:
            return ""
        
        n = len(s)
        
        # 统计频率并找到最大频率字符
        freq = Counter(s)
        max_freq = max(freq.values()) if freq else 0
        max_char = max(freq.items(), key=lambda x: x[1])[0] if freq else ''
        
        # 检查是否可重构
        if max_freq > (n + 1) // 2:
            return ""
        
        # 先放置最大频率字符
        result = [''] * n
        idx = 0
        
        # 先填充偶数位置
        while freq[max_char] > 0:
            result[idx] = max_char
            idx += 2
            freq[max_char] -= 1
            
            # 如果偶数位置填满，转到奇数位置
            if idx >= n:
                idx = 1
        
        # 填充其他字符
        for char in freq:
            while freq[char] > 0:
                if idx >= n:
                    idx = 1
                result[idx] = char
                idx += 2
                freq[char] -= 1
        
        return "".join(result)
    
    @staticmethod
    def is_valid_reorganization(s: str) -> bool:
        """
        验证字符串是否满足相邻字符不同的条件
        
        Args:
            s: 要验证的字符串
            
        Returns:
            bool: 是否满足条件
        """
        if not s or len(s) <= 1:
            return True
        
        for i in range(1, len(s)):
            if s[i] == s[i - 1]:
                return False
        return True
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 重构字符串测试 ===")
        
        # 测试用例1: "aab" -> "aba"
        s1 = "aab"
        print(f"测试用例1: '{s1}'")
        result1 = Code29_ReorganizeString.reorganize_string(s1)
        result1_alt = Code29_ReorganizeString.reorganize_string_alternate(s1)
        print(f"结果1: '{result1}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result1)}")
        print(f"结果2: '{result1_alt}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result1_alt)}")
        
        # 测试用例2: "aaab" -> "" (无法重构)
        s2 = "aaab"
        print(f"\n测试用例2: '{s2}'")
        result2 = Code29_ReorganizeString.reorganize_string(s2)
        result2_alt = Code29_ReorganizeString.reorganize_string_alternate(s2)
        print(f"结果1: '{result2}'")
        print(f"结果2: '{result2_alt}'")
        
        # 测试用例3: "abc" -> 任意有效排列
        s3 = "abc"
        print(f"\n测试用例3: '{s3}'")
        result3 = Code29_ReorganizeString.reorganize_string(s3)
        result3_alt = Code29_ReorganizeString.reorganize_string_alternate(s3)
        print(f"结果1: '{result3}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result3)}")
        print(f"结果2: '{result3_alt}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result3_alt)}")
        
        # 测试用例4: "a" -> "a"
        s4 = "a"
        print(f"\n测试用例4: '{s4}'")
        result4 = Code29_ReorganizeString.reorganize_string(s4)
        result4_alt = Code29_ReorganizeString.reorganize_string_alternate(s4)
        print(f"结果1: '{result4}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result4)}")
        print(f"结果2: '{result4_alt}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result4_alt)}")
        
        # 测试用例5: "aa" -> "" (无法重构)
        s5 = "aa"
        print(f"\n测试用例5: '{s5}'")
        result5 = Code29_ReorganizeString.reorganize_string(s5)
        result5_alt = Code29_ReorganizeString.reorganize_string_alternate(s5)
        print(f"结果1: '{result5}'")
        print(f"结果2: '{result5_alt}'")
        
        # 测试用例6: "aabbcc" -> 有效排列
        s6 = "aabbcc"
        print(f"\n测试用例6: '{s6}'")
        result6 = Code29_ReorganizeString.reorganize_string(s6)
        result6_alt = Code29_ReorganizeString.reorganize_string_alternate(s6)
        print(f"结果1: '{result6}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result6)}")
        print(f"结果2: '{result6_alt}', 有效: {Code29_ReorganizeString.is_valid_reorganization(result6_alt)}")
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        large_string = ''.join(random.choice('abcdefghijklmnopqrstuvwxyz') for _ in range(10000))
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code29_ReorganizeString.reorganize_string(large_string)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"方法1结果有效: {Code29_ReorganizeString.is_valid_reorganization(result1)}")
        
        start_time2 = time.time()
        result2 = Code29_ReorganizeString.reorganize_string_alternate(large_string)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"方法2结果有效: {Code29_ReorganizeString.is_valid_reorganization(result2)}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（优先队列）:")
        print("- 时间复杂度: O(n * logk)")
        print("  - 统计频率: O(n)")
        print("  - 堆操作: O(n * logk)，k为字符种类数")
        print("- 空间复杂度: O(k)")
        print("  - 频率统计: O(k)")
        print("  - 优先队列: O(k)")
        
        print("\n方法2（交替填充）:")
        print("- 时间复杂度: O(n)")
        print("  - 统计频率: O(n)")
        print("  - 填充数组: O(n)")
        print("- 空间复杂度: O(n)")
        print("  - 结果数组: O(n)")
        
        print("\n贪心策略证明:")
        print("1. 优先处理频率最高的字符可以避免冲突")
        print("2. 交替排列确保相邻字符不同")
        print("3. 数学证明：当最大频率 ≤ (n+1)/2 时可重构")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理空字符串和非法输入")
        print("2. 边界处理：单字符和双字符的特殊情况")
        print("3. 性能优化：选择合适的数据结构")
        print("4. 可读性：清晰的算法逻辑和注释")

def main():
    """主函数"""
    Code29_ReorganizeString.run_tests()
    Code29_ReorganizeString.performance_test()
    Code29_ReorganizeString.analyze_complexity()

if __name__ == "__main__":
    main()