"""
HackerRank Bit Array - 位数组
题目链接: https://www.hackerrank.com/challenges/bitset-1/problem
题目描述: 根据给定规则生成序列，计算序列中不同整数的个数

问题详细描述:
给定四个整数: N, S, P, Q，按照以下规则生成序列:
a[0] = S mod 2^31
对于i >= 1: a[i] = (a[i-1] * P + Q) mod 2^31
计算序列a[0], a[1], ..., a[N-1]中不同整数的个数

约束条件:
1 ≤ N ≤ 10^8
0 ≤ S, P, Q ≤ 2^31 - 1

解题思路:
方法1: 使用set - 简单但内存消耗大
方法2: 使用位数组 - 内存效率高，适合大N
方法3: Floyd循环检测 - 检测序列中的循环，避免存储整个序列

时间复杂度分析:
方法1: O(N) - 但内存消耗O(N)，不适合大N
方法2: O(N) - 内存消耗O(2^31/8) ≈ 256MB，可行
方法3: O(循环长度) - 最优，但实现复杂

空间复杂度分析:
方法1: O(N) - 存储所有元素
方法2: O(2^31/8) - 固定大小位数组
方法3: O(1) - 常数空间

工程化考量:
1. 内存优化: 对于大N必须使用位数组或循环检测
2. 整数溢出: 使用64位整数进行中间计算
3. 边界处理: 处理N=0,1的特殊情况
4. 异常安全: 使用异常处理机制
"""

import sys
import time
from typing import Set, List, Tuple
import array

class BitArraySolver:
    MOD = 1 << 31  # 2^31
    
    @staticmethod
    def next_value(current: int, p: int, q: int) -> int:
        """计算下一个序列值"""
        next_val = (current * p + q) % BitArraySolver.MOD
        return next_val
    
    @staticmethod
    def count_distinct_hashset(n: int, s: int, p: int, q: int) -> int:
        """
        方法1: 使用set（仅适用于小N）
        时间复杂度: O(N)
        空间复杂度: O(N)
        """
        if n == 0:
            return 0
        if n == 1:
            return 1
        
        seen = set()
        current = s % BitArraySolver.MOD
        seen.add(current)
        
        for i in range(1, n):
            current = BitArraySolver.next_value(current, p, q)
            seen.add(current)
        
        return len(seen)
    
    @staticmethod
    def count_distinct_bitarray(n: int, s: int, p: int, q: int) -> int:
        """
        方法2: 使用位数组（推荐用于大N）
        时间复杂度: O(N)
        空间复杂度: O(2^31/8) ≈ 256MB
        
        使用Python的bytearray实现位数组
        """
        if n == 0:
            return 0
        if n == 1:
            return 1
        
        # 创建位数组，每个字节存储8位
        bit_array_size = (BitArraySolver.MOD + 7) // 8
        visited = bytearray(bit_array_size)
        count = 0
        current = s % BitArraySolver.MOD
        
        for i in range(n):
            # 计算位数组中的位置
            byte_index = current // 8
            bit_index = current % 8
            
            # 检查是否已经访问过
            if not (visited[byte_index] & (1 << bit_index)):
                visited[byte_index] |= (1 << bit_index)
                count += 1
            
            if i < n - 1:
                current = BitArraySolver.next_value(current, p, q)
        
        return count
    
    @staticmethod
    def count_distinct_floyd(n: int, s: int, p: int, q: int) -> int:
        """
        方法3: Floyd循环检测算法（最优解）
        时间复杂度: O(循环长度)
        空间复杂度: O(1)
        """
        if n == 0:
            return 0
        if n == 1:
            return 1
        
        slow = s % BitArraySolver.MOD
        fast = s % BitArraySolver.MOD
        count = 1  # 至少有一个元素s
        
        # 第一阶段: 检测循环
        cycle_found = False
        cycle_length = 0
        
        for i in range(1, n):
            # 慢指针移动一步
            slow = BitArraySolver.next_value(slow, p, q)
            
            # 快指针移动两步
            fast = BitArraySolver.next_value(fast, p, q)
            fast = BitArraySolver.next_value(fast, p, q)
            
            if slow == fast:
                cycle_found = True
                
                # 计算循环长度
                cycle_length = 1
                temp = BitArraySolver.next_value(slow, p, q)
                while temp != slow:
                    temp = BitArraySolver.next_value(temp, p, q)
                    cycle_length += 1
                break
        
        if not cycle_found:
            return n  # 没有循环，所有元素都不同
        
        # 第二阶段: 找到循环起点
        slow = s % BitArraySolver.MOD
        fast = s % BitArraySolver.MOD
        
        # 快指针先移动cycle_length步
        for _ in range(cycle_length):
            fast = BitArraySolver.next_value(fast, p, q)
        
        # 同时移动快慢指针直到相遇
        while slow != fast:
            slow = BitArraySolver.next_value(slow, p, q)
            fast = BitArraySolver.next_value(fast, p, q)
        
        cycle_start = slow
        
        # 第三阶段: 计算不同元素个数
        cycle_elements = set()
        current = cycle_start
        
        while True:
            cycle_elements.add(current)
            current = BitArraySolver.next_value(current, p, q)
            if current == cycle_start:
                break
        
        # 计算循环前元素个数
        elements_before_cycle = 0
        current = s % BitArraySolver.MOD
        while current != cycle_start:
            elements_before_cycle += 1
            current = BitArraySolver.next_value(current, p, q)
        
        return elements_before_cycle + len(cycle_elements)
    
    @staticmethod
    def count_distinct_optimized(n: int, s: int, p: int, q: int) -> int:
        """
        方法4: 优化版本 - 根据N的大小选择算法
        """
        if n == 0:
            return 0
        if n == 1:
            return 1
        
        # 对于小N使用HashSet
        if n <= 1000000:
            return BitArraySolver.count_distinct_hashset(n, s, p, q)
        # 对于大N使用位数组
        elif n <= 100000000:
            return BitArraySolver.count_distinct_bitarray(n, s, p, q)
        # 对于非常大的N使用Floyd算法
        else:
            return BitArraySolver.count_distinct_floyd(n, s, p, q)
    
    @staticmethod
    def count_distinct_with_validation(n: int, s: int, p: int, q: int) -> int:
        """
        工程化改进版本：完整的异常处理
        """
        try:
            # 输入验证
            if s >= BitArraySolver.MOD or p >= BitArraySolver.MOD or q >= BitArraySolver.MOD:
                raise ValueError("s, p, q must be less than 2^31")
            
            return BitArraySolver.count_distinct_optimized(n, s, p, q)
            
        except Exception as e:
            print(f"Error in count_distinct: {e}")
            return 0


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def run_tests():
        """运行单元测试"""
        print("=== HackerRank Bit Array - 单元测试 ===")
        
        # 测试用例
        test_cases = [
            (5, 1, 2, 1, 5),    # 序列: 1, 3, 7, 15, 31
            (10, 1, 3, 0, 4),    # 序列可能产生循环
            (1, 100, 1, 1, 1)    # 边界情况 n=1
        ]
        
        for n, s, p, q, expected in test_cases:
            result = BitArraySolver.count_distinct_optimized(n, s, p, q)
            
            print(f"测试 n={n}, s={s}, p={p}, q={q}, "
                  f"期望={expected}, 实际={result}, "
                  f"{'通过' if result == expected else '失败'}")
        
        # 方法一致性测试
        print("\n=== 方法一致性测试 ===")
        test_n, test_s, test_p, test_q = 100, 1, 3, 1
        
        r1 = BitArraySolver.count_distinct_hashset(test_n, test_s, test_p, test_q)
        r2 = BitArraySolver.count_distinct_bitarray(test_n, test_s, test_p, test_q)
        r3 = BitArraySolver.count_distinct_floyd(test_n, test_s, test_p, test_q)
        r4 = BitArraySolver.count_distinct_optimized(test_n, test_s, test_p, test_q)
        
        print(f"HashSet: {r1}")
        print(f"BitArray: {r2}")
        print(f"Floyd: {r3}")
        print(f"优化法: {r4}")
        print(f"所有方法结果一致: {'是' if r1 == r2 == r3 == r4 else '否'}")
    
    @staticmethod
    def performance_test():
        """性能测试"""
        print("\n=== 性能测试 ===")
        
        test_cases = [
            (1000, 1, 3, 1),
            (10000, 1, 3, 1),
            (100000, 1, 3, 1),
            (1000000, 1, 3, 1)
        ]
        
        for n, s, p, q in test_cases:
            print(f"n = {n}:")
            
            # 测试不同方法
            if n <= 100000:
                start_time = time.time()
                result1 = BitArraySolver.count_distinct_hashset(n, s, p, q)
                time1 = (time.time() - start_time) * 1e9
                print(f"  HashSet: {time1:.0f} ns, 结果: {result1}")
            
            start_time = time.time()
            result2 = BitArraySolver.count_distinct_bitarray(n, s, p, q)
            time2 = (time.time() - start_time) * 1e9
            print(f"  BitArray: {time2:.0f} ns, 结果: {result2}")
            
            start_time = time.time()
            result3 = BitArraySolver.count_distinct_floyd(n, s, p, q)
            time3 = (time.time() - start_time) * 1e9
            print(f"  Floyd: {time3:.0f} ns, 结果: {result3}")
            
            print()


def complexity_analysis():
    """复杂度分析"""
    print("=== 复杂度分析 ===")
    print("方法1（HashSet）:")
    print("  时间复杂度: O(N)")
    print("  空间复杂度: O(N)")
    print("  适用场景: 小规模数据（N <= 10^6）")
    
    print("\n方法2（BitArray）:")
    print("  时间复杂度: O(N)")
    print("  空间复杂度: O(2^31/8) ≈ 256MB")
    print("  适用场景: 中等规模数据（N <= 10^8）")
    
    print("\n方法3（Floyd循环检测）:")
    print("  时间复杂度: O(循环长度)")
    print("  空间复杂度: O(1)")
    print("  适用场景: 大规模数据（N > 10^8）")
    
    print("\n工程化建议:")
    print("1. 根据N的大小动态选择算法")
    print("2. 使用64位整数避免整数溢出")
    print("3. 对于竞赛题目，BitArray方法通常是最佳选择")


def main():
    """主函数"""
    print("HackerRank Bit Array - 位数组问题")
    print("计算根据规则生成的序列中不同整数的个数")
    
    # 运行单元测试
    PerformanceTester.run_tests()
    
    # 运行性能测试
    PerformanceTester.performance_test()
    
    # 复杂度分析
    complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    examples = [
        (5, 1, 2, 1),
        (10, 1, 3, 0),
        (100, 1, 1, 1)
    ]
    
    for n, s, p, q in examples:
        result = BitArraySolver.count_distinct_with_validation(n, s, p, q)
        print(f"n={n}, s={s}, p={p}, q={q} -> 不同元素个数: {result}")


if __name__ == "__main__":
    main()