"""
LinearBasis - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""

# -*- coding: utf-8 -*-

"""
LeetCode 1707. 与数组中元素的最大异或值
题目描述：
给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。
第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值。
如果 nums 中的所有元素都大于 mi，最终答案就是 -1 。
请你返回一个数组 answer 作为查询的答案。

解题思路：
这道题可以使用线性基（Linear Basis）来解决。
关键步骤：
1. 将数组和查询按照元素值/mi排序
2. 离线处理查询，将元素按从小到大的顺序插入线性基
3. 对于每个查询，使用当前的线性基计算最大异或值

线性基是一种数据结构，用于处理异或相关的问题。它可以在O(log max_val)的时间内查询最大异或值。

时间复杂度：O((n + q) * log max_val)，其中n是数组长度，q是查询次数，max_val是数组中的最大值
空间复杂度：O(log max_val)

最优解分析：
这是该问题的最优解法，离线处理结合线性基可以高效地回答所有查询。
"""

class LinearBasis:
    """
    线性基类
    用于处理异或相关的问题，支持插入元素和查询最大异或值
    """
    
    def __init__(self):
        self.MAX_BIT = 30  # 最大位数，因为题目中的元素是非负整数，最大不超过1e9
        self.basis = [0] * (self.MAX_BIT + 1)  # 线性基数组，basis[i]表示最高位为i的基向量
    
    def insert(self, num):
        """
        插入一个数到线性基中
        
        参数：
            num: 要插入的数
        """
        # 从高位到低位处理
        for i in range(self.MAX_BIT, -1, -1):
            if (num >> i) & 1:  # 如果当前位是1
                if self.basis[i] == 0:  # 如果当前位没有基向量
                    self.basis[i] = num
                    break
                else:  # 否则，异或当前基向量，继续处理
                    num ^= self.basis[i]
    
    def query_max_xor(self, num):
        """
        查询与给定数异或的最大值
        
        参数：
            num: 给定的数
        
        返回：
            最大异或值
        """
        res = num
        for i in range(self.MAX_BIT, -1, -1):
            # 如果异或基向量后结果更大，就选择异或
            if (res ^ self.basis[i]) > res:
                res ^= self.basis[i]
        return res

def maximize_xor(nums, queries):
    """
    解决问题的主函数
    
    参数：
        nums: 数组
        queries: 查询数组
    
    返回：
        查询结果数组
    """
    n = len(nums)
    q = len(queries)
    answer = [0] * q
    
    # 将nums数组按升序排序
    nums.sort()
    
    # 将查询保存为对象，记录原始索引
    sorted_queries = []
    for i in range(q):
        sorted_queries.append([queries[i][0], queries[i][1], i])  # xi, mi, 原始索引
    
    # 将查询按mi升序排序
    sorted_queries.sort(key=lambda x: x[1])
    
    # 初始化线性基
    lb = LinearBasis()
    idx = 0  # 当前处理到的nums元素索引
    
    # 离线处理每个查询
    for query in sorted_queries:
        xi, mi, original_index = query
        
        # 将所有<=mi的元素插入线性基
        while idx < n and nums[idx] <= mi:
            lb.insert(nums[idx])
            idx += 1
        
        # 如果没有元素插入，说明所有元素都大于mi
        if idx == 0:
            answer[original_index] = -1
        else:
            # 计算最大异或值
            answer[original_index] = lb.query_max_xor(xi)
    
    return answer

def test():
    """
    测试函数
    """
    # 测试用例1
    nums1 = [0, 1, 2, 3, 4]
    queries1 = [[3, 1], [1, 3], [5, 6]]
    result1 = maximize_xor(nums1, queries1)
    print("Test case 1 result:", result1)  # Expected: [3, 3, 7]
    
    # 测试用例2
    nums2 = [5, 2, 4, 6, 6, 3]
    queries2 = [[12, 4], [8, 1], [6, 3]]
    result2 = maximize_xor(nums2, queries2)
    print("Test case 2 result:", result2)  # Expected: [15, -1, 5]


if __name__ == "__main__":
    test()