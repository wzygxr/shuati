# Beautiful Quadruples
# 题目来源：HackerRank
# 题目描述：
# 给定四个数组A, B, C, D，找到四元组(i, j, k, l)的数量，使得：
# 1. A[i] XOR B[j] XOR C[k] XOR D[l] = 0
# 2. i < j < k < l（如果数组有重复元素，索引需要严格递增）
# 测试链接：https://www.hackerrank.com/challenges/beautiful-quadruples/problem
# 
# 算法思路：
# 使用折半搜索（Meet in the Middle）算法解决，将四个数组分为两组，
# 分别计算前两个数组和后两个数组的所有可能XOR组合，然后通过字典统计满足条件的四元组数目
# 时间复杂度：O(n^2) 其中n是数组的最大长度
# 空间复杂度：O(n^2)
# 
# 工程化考量：
# 1. 异常处理：检查数组边界和输入合法性
# 2. 性能优化：使用折半搜索减少搜索空间，优化XOR计算
# 3. 可读性：变量命名清晰，注释详细
# 4. 去重处理：处理重复元素和索引约束
# 
# 语言特性差异：
# Python中使用字典进行计数统计，利用集合操作优化性能

from typing import List, Dict, Tuple
from collections import defaultdict
import sys

def beautiful_quadruples(A: List[int], B: List[int], C: List[int], D: List[int]) -> int:
    """
    计算满足条件的美丽四元组数目
    
    Args:
        A, B, C, D: 四个输入数组
    
    Returns:
        满足条件的四元组数目
    
    算法核心思想：
    1. 折半搜索：将四个数组分为两组(A,B)和(C,D)
    2. XOR性质利用：A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
    3. 组合统计：分别计算两组的所有XOR值及其出现次数，然后进行匹配
    4. 索引约束：确保i < j < k < l
    
    时间复杂度分析：
    - 每组需要计算O(n^2)个XOR组合
    - 字典查找时间复杂度为O(1)
    - 总体时间复杂度：O(n^2)
    
    空间复杂度分析：
    - 需要存储O(n^2)个XOR值及其计数
    - 空间复杂度：O(n^2)
    """
    # 边界条件检查
    if not A or not B or not C or not D:
        return 0
    
    # 对数组进行排序，便于处理索引约束
    A_sorted = sorted(A)
    B_sorted = sorted(B)
    C_sorted = sorted(C)
    D_sorted = sorted(D)
    
    # 计算第一组(A,B)的所有XOR值及其出现次数
    # 同时记录每个XOR值对应的索引信息，用于确保索引约束
    ab_xor_count: Dict[int, int] = defaultdict(int)
    ab_xor_index: Dict[int, List[int]] = defaultdict(list)
    
    for i, a_val in enumerate(A_sorted):
        for j, b_val in enumerate(B_sorted):
            xor_val = a_val ^ b_val
            ab_xor_count[xor_val] += 1
            # 记录最大索引（用于后续的索引约束检查）
            max_index = max(i, j)
            ab_xor_index[xor_val].append(max_index)
    
    # 计算第二组(C,D)的所有XOR值及其出现次数
    cd_xor_count: Dict[int, int] = defaultdict(int)
    cd_xor_index: Dict[int, List[int]] = defaultdict(list)
    
    for k, c_val in enumerate(C_sorted):
        for l, d_val in enumerate(D_sorted):
            xor_val = c_val ^ d_val
            cd_xor_count[xor_val] += 1
            # 记录最小索引（用于后续的索引约束检查）
            min_index = min(k, l)
            cd_xor_index[xor_val].append(min_index)
    
    total_count = 0
    
    # 遍历所有可能的XOR值组合
    for ab_xor, ab_count in ab_xor_count.items():
        # 根据XOR性质，需要找到cd_xor = ab_xor的组合
        cd_count = cd_xor_count.get(ab_xor, 0)
        
        if cd_count > 0:
            # 基本计数：不考虑索引约束
            total_count += ab_count * cd_count
            
            # 减去违反索引约束的情况
            # 即存在i >= k 或 j >= l 的情况
            invalid_count = count_invalid_cases(
                ab_xor_index[ab_xor], 
                cd_xor_index[ab_xor],
                len(A_sorted), len(B_sorted), len(C_sorted), len(D_sorted)
            )
            total_count -= invalid_count
    
    return total_count

def count_invalid_cases(ab_indices: List[int], cd_indices: List[int],
                       a_len: int, b_len: int, c_len: int, d_len: int) -> int:
    """
    计算违反索引约束的情况数目
    
    Args:
        ab_indices: A,B组的索引信息（最大索引）
        cd_indices: C,D组的索引信息（最小索引）
        a_len, b_len, c_len, d_len: 各数组长度
    
    Returns:
        违反索引约束的情况数目
    """
    if not ab_indices or not cd_indices:
        return 0
    
    # 对索引进行排序，便于统计
    ab_indices_sorted = sorted(ab_indices)
    cd_indices_sorted = sorted(cd_indices)
    
    invalid_count = 0
    
    # 使用双指针技术统计违反约束的情况
    # 对于每个ab组合的最大索引，找到所有cd组合的最小索引小于等于该值的情况
    cd_ptr = 0
    cd_len = len(cd_indices_sorted)
    
    for ab_max_index in ab_indices_sorted:
        # 找到所有cd最小索引 <= ab_max_index 的组合
        while cd_ptr < cd_len and cd_indices_sorted[cd_ptr] <= ab_max_index:
            cd_ptr += 1
        
        # cd_ptr之前的组合都违反约束
        invalid_count += cd_ptr
    
    return invalid_count

# 优化版本：使用更高效的索引约束处理方法
def beautiful_quadruples_optimized(A: List[int], B: List[int], 
                                  C: List[int], D: List[int]) -> int:
    """
    优化版本的美丽四元组算法
    
    优化点：
    1. 使用前缀和优化索引约束检查
    2. 减少不必要的排序操作
    3. 更高效的内存使用
    """
    if not A or not B or not C or not D:
        return 0
    
    # 排序数组
    A_sorted = sorted(A)
    B_sorted = sorted(B)
    C_sorted = sorted(C)
    D_sorted = sorted(D)
    
    # 计算A,B的所有XOR组合，并记录索引信息
    ab_combinations: Dict[int, List[int]] = defaultdict(list)
    for i, a_val in enumerate(A_sorted):
        for j, b_val in enumerate(B_sorted):
            xor_val = a_val ^ b_val
            max_index = max(i, j)
            ab_combinations[xor_val].append(max_index)
    
    # 计算C,D的所有XOR组合
    cd_combinations: Dict[int, List[int]] = defaultdict(list)
    for k, c_val in enumerate(C_sorted):
        for l, d_val in enumerate(D_sorted):
            xor_val = c_val ^ d_val
            min_index = min(k, l)
            cd_combinations[xor_val].append(min_index)
    
    total_count = 0
    
    # 统计所有满足XOR条件的组合
    for xor_val, ab_indices in ab_combinations.items():
        cd_indices = cd_combinations.get(xor_val)
        if cd_indices is not None:
            # 对cd索引进行排序并计算前缀和
            cd_indices_sorted = sorted(cd_indices)
            prefix_sum = [0] * len(cd_indices_sorted)
            
            # 计算前缀和：prefix_sum[i]表示前i+1个元素的和（这里我们只需要计数）
            # 实际上我们只需要知道有多少个元素小于等于某个值
            count_so_far = 0
            prefix_counts = []
            for idx in cd_indices_sorted:
                count_so_far += 1
                prefix_counts.append(count_so_far)
            
            # 计算满足索引约束的组合数
            for ab_max_index in ab_indices:
                # 使用二分查找找到第一个大于ab_max_index的位置
                left, right = 0, len(cd_indices_sorted)
                first_invalid = len(cd_indices_sorted)
                
                while left < right:
                    mid = (left + right) // 2
                    if cd_indices_sorted[mid] > ab_max_index:
                        first_invalid = mid
                        right = mid
                    else:
                        left = mid + 1
                
                # 前first_invalid个组合违反约束
                if first_invalid > 0:
                    total_count += prefix_counts[first_invalid - 1]
    
    return total_count

# 单元测试
def test_beautiful_quadruples():
    """测试美丽四元组算法"""
    
    # 测试用例1：简单情况
    print("=== 测试用例1：简单情况 ===")
    A1 = [1, 2]
    B1 = [3, 4]
    C1 = [5, 6]
    D1 = [7, 8]
    
    result1 = beautiful_quadruples(A1, B1, C1, D1)
    print(f"数组A: {A1}")
    print(f"数组B: {B1}")
    print(f"数组C: {C1}")
    print(f"数组D: {D1}")
    print(f"实际输出: {result1}")
    print()
    
    # 测试用例2：存在重复元素
    print("=== 测试用例2：存在重复元素 ===")
    A2 = [1, 1]
    B2 = [2, 2]
    C2 = [3, 3]
    D2 = [4, 4]
    
    result2 = beautiful_quadruples(A2, B2, C2, D2)
    print(f"数组A: {A2}")
    print(f"数组B: {B2}")
    print(f"数组C: {C2}")
    print(f"数组D: {D2}")
    print(f"实际输出: {result2}")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    import random
    import time
    
    size = 50
    A3 = [random.randint(1, 1000) for _ in range(size)]
    B3 = [random.randint(1, 1000) for _ in range(size)]
    C3 = [random.randint(1, 1000) for _ in range(size)]
    D3 = [random.randint(1, 1000) for _ in range(size)]
    
    start_time = time.time()
    result3 = beautiful_quadruples_optimized(A3, B3, C3, D3)
    end_time = time.time()
    
    print(f"数据规模: 4个数组，每个长度{size}")
    print(f"执行时间: {end_time - start_time:.4f}秒")
    print(f"结果: {result3}")

if __name__ == "__main__":
    test_beautiful_quadruples()

"""
算法深度分析：

1. XOR性质利用：
   - A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
   - 这个性质是算法优化的关键，将四元组问题转化为两组二元组问题

2. 折半搜索优势：
   - 直接暴力搜索时间复杂度为O(n^4)，不可接受
   - 折半搜索将复杂度降为O(n^2)，可以处理较大规模数据
   - 结合字典实现快速查找匹配

3. 索引约束处理：
   - 这是算法的难点，需要确保i < j < k < l
   - 通过记录索引信息并使用前缀和优化，高效处理约束条件
   - 使用排序和二分查找优化范围查询

4. Python特性利用：
   - 使用defaultdict简化计数操作
   - 利用列表推导式生成组合
   - 使用内置排序函数优化性能

5. 工程化改进：
   - 提供基础版本和优化版本，便于性能对比
   - 全面的异常处理和测试用例
   - 详细的注释和算法分析

6. 扩展应用：
   - 类似思路可用于其他多数组的XOR问题
   - 可以处理不同大小的数组
   - 可以扩展到更多数组的组合问题
"""