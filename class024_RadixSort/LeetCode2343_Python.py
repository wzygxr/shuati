"""
LeetCode 2343. 裁剪数字后查询第K小的数字

题目链接: https://leetcode.cn/problems/query-kth-smallest-trimmed-number/

题目描述:
给你一个下标从0开始的字符串数组nums，其中每个字符串长度相等且只包含数字。
再给你一个下标从0开始的二维整数数组queries，其中queries[i] = [ki, trimi]。
对于每个查询，你需要将nums中的每个数字裁剪到剩下最右边trimi个数位。
在裁剪过后的数字中，找到nums中第ki小数字对应的下标。如果两个裁剪后数字一样大，那么下标较小的数字更小。
返回一个数组answer，其中answer[i]是第i个查询的答案。

解题思路:
1. 对于每个查询，我们需要：
   a. 裁剪所有数字到指定长度
   b. 找到第k小的数字及其索引
2. 可以使用基数排序来优化，因为所有数字都是相同长度的字符串
3. 对于每个trim值，我们可以预先计算排序结果，避免重复计算

时间复杂度分析:
设nums长度为n，每个字符串长度为m，查询次数为q
1. 暴力解法：每次查询都需要O(n*log(n))时间排序，总时间复杂度为O(q*n*log(n))
2. 基数排序优化：对每个trim值进行一次基数排序，时间复杂度为O(m*(n+10))，总时间复杂度为O(m*(n+10)*max_trim + q*n)

空间复杂度分析:
O(n*m) 用于存储裁剪后的数字和索引

工程化考虑:
1. 输入验证：检查输入参数的有效性
2. 边界情况：空数组、单元素数组等
3. 性能优化：使用基数排序处理大量查询
4. 内存优化：复用数组避免重复分配

相关题目:
1. LeetCode 164. 最大间距 - 可以使用基数排序在O(n)时间内完成排序
2. LeetCode 912. 排序数组 - 基数排序是此题的高效解法之一
3. 洛谷 P1177 【模板】排序 - 基数排序是此题的高效解法之一
"""

class LeetCode2343_Python:
    @staticmethod
    def smallestTrimmedNumbers(nums, queries):
        """
        主函数，处理查询数组

        算法步骤:
        1. 遍历所有查询
        2. 对每个查询执行裁剪和查找第k小元素的操作
        3. 将结果收集到答案数组中

        :param nums: 字符串数组，包含数字字符串
        :param queries: 查询数组，每个查询包含[k, trim]
        :return: 答案数组，每个元素对应一个查询的结果
        """
        # 输入验证
        if not nums or not queries:
            return []
        
        n = len(nums)
        answer = [0] * len(queries)
        
        # 预处理：按trim值分组查询，避免重复计算
        trim_to_queries = {}
        for i, query in enumerate(queries):
            trim = query[1]
            if trim not in trim_to_queries:
                trim_to_queries[trim] = []
            trim_to_queries[trim].append(i)
        
        # 对每个不同的trim值进行处理
        for trim, query_indices in trim_to_queries.items():
            # 使用基数排序对裁剪后的数字进行排序
            sorted_indices = LeetCode2343_Python._radix_sort_by_trim(nums, trim)
            
            # 处理所有使用相同trim值的查询
            for query_index in query_indices:
                k = queries[query_index][0]
                # 第k小的元素在排序后的数组中的索引是k-1
                answer[query_index] = sorted_indices[k - 1]
        
        return answer
    
    @staticmethod
    def _radix_sort_by_trim(nums, trim):
        """
        使用基数排序对裁剪后的数字进行排序

        算法原理:
        1. 从最低位开始，对每一位进行计数排序
        2. 使用计数排序保证稳定性
        3. 重复此过程直到最高位

        :param nums: 原始数字字符串数组
        :param trim: 裁剪位数
        :return: 排序后的索引数组
        """
        n = len(nums)
        length = len(nums[0])
        start = length - trim  # 裁剪起始位置
        
        # 初始化索引数组
        indices = list(range(n))
        
        # 从最低位到最高位依次进行计数排序
        for pos in range(length - 1, start - 1, -1):
            # 计数数组，用于统计0-9各数字的出现次数
            count = [0] * 10
            
            # 统计当前位上各数字的出现次数
            for i in range(n):
                digit = int(nums[indices[i]][pos])
                count[digit] += 1
            
            # 计算前缀和，得到各数字在排序后数组中的位置
            for i in range(1, 10):
                count[i] += count[i - 1]
            
            # 辅助数组
            temp_indices = [0] * n
            
            # 从后向前遍历，保证排序的稳定性
            for i in range(n - 1, -1, -1):
                digit = int(nums[indices[i]][pos])
                count[digit] -= 1
                temp_indices[count[digit]] = indices[i]
            
            # 将排序结果复制回原数组
            indices = temp_indices
        
        return indices
    
    @staticmethod
    def smallestTrimmedNumbersBruteForce(nums, queries):
        """
        暴力解法：对每个查询单独排序

        时间复杂度: O(q * n * log(n))
        空间复杂度: O(n)

        适用于查询次数较少的情况

        :param nums: 字符串数组，包含数字字符串
        :param queries: 查询数组，每个查询包含[k, trim]
        :return: 答案数组，每个元素对应一个查询的结果
        """
        answer = [0] * len(queries)
        
        for i, query in enumerate(queries):
            k, trim = query
            
            n = len(nums)
            length = len(nums[0])
            start = length - trim
            
            # 创建裁剪后的数字和索引对
            pairs = []
            for j in range(n):
                trimmed = nums[j][start:]
                pairs.append((trimmed, j))
            
            # 排序
            pairs.sort(key=lambda x: (x[0], x[1]))
            
            # 获取第k小元素的索引
            answer[i] = pairs[k - 1][1]
        
        return answer


# 测试函数
if __name__ == "__main__":
    # 测试用例1
    nums1 = ["102", "473", "251", "814"]
    queries1 = [[1, 1], [2, 3], [4, 2], [1, 2]]
    result1 = LeetCode2343_Python.smallestTrimmedNumbers(nums1, queries1)
    print("测试用例1结果:", result1)  # 预期: [2, 2, 1, 0]
    
    # 测试用例2
    nums2 = ["24", "37", "96", "04"]
    queries2 = [[2, 1], [2, 2]]
    result2 = LeetCode2343_Python.smallestTrimmedNumbers(nums2, queries2)
    print("测试用例2结果:", result2)  # 预期: [3, 0]
    
    # 验证暴力解法结果一致性
    result1_brute = LeetCode2343_Python.smallestTrimmedNumbersBruteForce(nums1, queries1)
    result2_brute = LeetCode2343_Python.smallestTrimmedNumbersBruteForce(nums2, queries2)
    
    print("暴力解法测试用例1结果:", result1_brute)
    print("暴力解法测试用例2结果:", result2_brute)
    
    print("结果一致性验证1:", result1 == result1_brute)
    print("结果一致性验证2:", result2 == result2_brute)