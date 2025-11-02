"""
位集应用算法实现
包含多个实际应用场景的位集算法

题目来源:
1. LeetCode 78 - 子集
2. LeetCode 90 - 子集 II  
3. LeetCode 401 - 二进制手表
4. LeetCode 477 - 汉明距离总和
5. 组合数学问题
6. 状态压缩动态规划

时间复杂度分析:
- 子集生成: O(2^n * n) - 指数级复杂度
- 二进制手表: O(12 * 60) - 常数时间
- 汉明距离总和: O(n) - 线性复杂度
- 状态压缩: O(2^n * n) - 指数级复杂度

空间复杂度分析:
- 子集生成: O(2^n) - 指数级空间
- 二进制手表: O(1) - 常数空间
- 汉明距离总和: O(1) - 常数空间
- 状态压缩: O(2^n) - 指数级空间

工程化考量:
1. 性能优化: 使用位运算优化计算
2. 内存管理: 处理大规模数据时的内存使用
3. 边界处理: 处理空集和边界情况
4. 可读性: 添加详细注释说明算法原理
"""

import time
from typing import List, Set, Tuple
import itertools

class BitSetApplications:
    """位集应用算法类"""
    
    @staticmethod
    def subsets(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 78 - 子集
        生成数组的所有可能子集
        
        方法: 位运算枚举
        时间复杂度: O(2^n * n)
        空间复杂度: O(2^n)
        """
        n = len(nums)
        total = 1 << n  # 2^n个子集
        result = []
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        
        return result
    
    @staticmethod
    def subsets_with_dup(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 90 - 子集 II
        生成包含重复元素的数组的所有可能子集（去重）
        
        方法: 先排序，然后使用位运算枚举，跳过重复元素
        时间复杂度: O(2^n * n)
        空间复杂度: O(2^n)
        """
        nums.sort()
        n = len(nums)
        total = 1 << n
        result = []
        seen = set()
        
        for mask in range(total):
            subset = []
            key_parts = []
            valid = True
            
            for i in range(n):
                if mask & (1 << i):
                    # 检查是否跳过重复元素
                    if i > 0 and nums[i] == nums[i-1] and not (mask & (1 << (i-1))):
                        valid = False
                        break
                    subset.append(nums[i])
                    key_parts.append(str(nums[i]))
            
            if valid:
                key = ",".join(key_parts)
                if key not in seen:
                    result.append(subset)
                    seen.add(key)
        
        return result
    
    @staticmethod
    def read_binary_watch(turned_on: int) -> List[str]:
        """
        LeetCode 401 - 二进制手表
        显示所有可能的二进制手表时间
        
        方法: 枚举所有可能的小时和分钟组合
        时间复杂度: O(12 * 60) = O(720)
        空间复杂度: O(n) - n为结果数量
        """
        result = []
        
        for hour in range(12):
            for minute in range(60):
                if bin(hour).count('1') + bin(minute).count('1') == turned_on:
                    time_str = f"{hour}:{minute:02d}"
                    result.append(time_str)
        
        return result
    
    @staticmethod
    def total_hamming_distance(nums: List[int]) -> int:
        """
        LeetCode 477 - 汉明距离总和
        计算数组中所有数对之间的汉明距离总和
        
        方法: 按位统计，对于每一位，计算0和1的个数
        时间复杂度: O(32 * n) = O(n)
        空间复杂度: O(1)
        """
        total = 0
        n = len(nums)
        
        # 对每一位进行统计（假设32位整数）
        for i in range(32):
            count_ones = 0
            for num in nums:
                if num & (1 << i):
                    count_ones += 1
            total += count_ones * (n - count_ones)
        
        return total
    
    @staticmethod
    def combinations(n: int, k: int) -> List[List[int]]:
        """
        组合数学: 计算组合数 C(n, k)
        使用位运算枚举所有k个元素的组合
        
        方法: Gosper's Hack算法
        时间复杂度: O(C(n, k))
        空间复杂度: O(1)
        """
        if k == 0 or k > n:
            return []
        
        result = []
        mask = (1 << k) - 1  # 初始组合：最低k位为1
        limit = 1 << n
        
        while mask < limit:
            # 将当前掩码转换为组合
            comb = []
            for i in range(n):
                if mask & (1 << i):
                    comb.append(i)
            result.append(comb)
            
            # 使用Gosper's Hack获取下一个组合
            rightmost = mask & -mask
            next_mask = mask + rightmost
            mask = next_mask | (((mask ^ next_mask) // rightmost) >> 2)
        
        return result
    
    @staticmethod
    def tsp(graph: List[List[int]]) -> int:
        """
        状态压缩动态规划: 旅行商问题（TSP）
        使用位集表示访问过的城市状态
        
        方法: 动态规划 + 状态压缩
        时间复杂度: O(2^n * n^2)
        空间复杂度: O(2^n * n)
        """
        n = len(graph)
        if n == 0:
            return 0
        
        # dp[mask][i]: 访问过mask表示的城市集合，当前在城市i的最小代价
        INF = float('inf')
        dp = [[INF] * n for _ in range(1 << n)]
        
        # 初始化：从城市0出发
        dp[1][0] = 0
        
        # 动态规划
        for mask in range(1 << n):
            for i in range(n):
                if not (mask & (1 << i)):  # 城市i不在当前集合中
                    continue
                
                for j in range(n):
                    if mask & (1 << j):  # 城市j已经在集合中
                        continue
                    if graph[i][j] == INF:  # 不可达
                        continue
                    
                    new_mask = mask | (1 << j)
                    if dp[mask][i] != INF:
                        dp[new_mask][j] = min(dp[new_mask][j], dp[mask][i] + graph[i][j])
        
        # 找到回到起点的最小代价
        result = INF
        final_mask = (1 << n) - 1
        for i in range(1, n):
            if dp[final_mask][i] != INF and graph[i][0] != INF:
                result = min(result, dp[final_mask][i] + graph[i][0])
        
        return -1 if result == INF else int(result)
    
    @staticmethod
    def set_operations_demo():
        """
        位集在集合操作中的应用
        实现集合的并集、交集、差集等操作
        """
        print("=== 位集集合操作演示 ===")
        
        # 使用整数表示集合（8位）
        set_a = 0b10101010  # 集合A: {1, 3, 5, 7}
        set_b = 0b11001100  # 集合B: {2, 3, 6, 7}
        
        print(f"集合A: {bin(set_a)} (十进制: {set_a})")
        print(f"集合B: {bin(set_b)} (十进制: {set_b})")
        
        # 并集
        union_set = set_a | set_b
        print(f"并集 A ∪ B: {bin(union_set)}")
        
        # 交集
        intersect_set = set_a & set_b
        print(f"交集 A ∩ B: {bin(intersect_set)}")
        
        # 差集
        diff_set = set_a & ~set_b
        print(f"差集 A - B: {bin(diff_set)}")
        
        # 对称差
        sym_diff_set = set_a ^ set_b
        print(f"对称差 A Δ B: {bin(sym_diff_set)}")
        
        # 子集判断
        is_subset = (set_a & set_b) == set_a
        print(f"A是B的子集: {'是' if is_subset else '否'}")


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_subsets():
        """测试子集生成性能"""
        print("=== 子集生成性能测试 ===")
        
        nums = [1, 2, 3, 4, 5]
        
        start = time.time()
        result = BitSetApplications.subsets(nums)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"数组大小: {len(nums)}")
        print(f"生成子集数量: {len(result)}")
        print(f"耗时: {elapsed:.2f} μs")
        
        # 显示前几个子集
        print("前5个子集:")
        for i in range(min(5, len(result))):
            print(f"  {result[i]}")
    
    @staticmethod
    def test_combinations():
        """测试组合生成性能"""
        print("\n=== 组合生成性能测试 ===")
        
        n, k = 10, 3
        
        start = time.time()
        result = BitSetApplications.combinations(n, k)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"C({n}, {k}) = {len(result)}")
        print(f"耗时: {elapsed:.2f} μs")
        
        # 显示前几个组合
        print("前5个组合:")
        for i in range(min(5, len(result))):
            print(f"  {result[i]}")
    
    @staticmethod
    def test_hamming_distance():
        """测试汉明距离总和性能"""
        print("\n=== 汉明距离总和测试 ===")
        
        nums = [4, 14, 2, 8]
        
        start = time.time()
        result = BitSetApplications.total_hamming_distance(nums)
        elapsed = (time.time() - start) * 1e9  # 纳秒
        
        print(f"数组: {nums}")
        print(f"汉明距离总和: {result}")
        print(f"耗时: {elapsed:.2f} ns")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 位集应用单元测试 ===")
        
        # 测试子集生成
        nums = [1, 2, 3]
        subsets = BitSetApplications.subsets(nums)
        assert len(subsets) == 8
        print("子集生成测试通过")
        
        # 测试汉明距离总和
        nums2 = [4, 14, 2]
        hamming = BitSetApplications.total_hamming_distance(nums2)
        assert hamming > 0
        print("汉明距离总和测试通过")
        
        # 测试组合生成
        combs = BitSetApplications.combinations(5, 2)
        assert len(combs) == 10
        print("组合生成测试通过")
        
        # 测试二进制手表
        times = BitSetApplications.read_binary_watch(1)
        assert len(times) > 0
        print("二进制手表测试通过")
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "subsets": {
                "time": "O(2^n * n)",
                "space": "O(2^n)",
                "desc": "位运算枚举所有子集"
            },
            "combinations": {
                "time": "O(C(n, k))",
                "space": "O(C(n, k))",
                "desc": "Gosper's Hack算法"
            },
            "total_hamming_distance": {
                "time": "O(n)",
                "space": "O(1)",
                "desc": "按位统计0和1的个数"
            },
            "tsp": {
                "time": "O(2^n * n^2)",
                "space": "O(2^n * n)",
                "desc": "状态压缩动态规划"
            }
        }
        
        for name, info in algorithms.items():
            print(f"{name}:")
            print(f"  时间复杂度: {info['time']}")
            print(f"  空间复杂度: {info['space']}")
            print(f"  描述: {info['desc']}")
            print()


def main():
    """主函数"""
    print("位集应用算法实现")
    print("包含子集生成、组合数学、状态压缩等应用")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_subsets()
    PerformanceTester.test_combinations()
    PerformanceTester.test_hamming_distance()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 演示集合操作
    BitSetApplications.set_operations_demo()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 子集生成示例
    nums = [1, 2, 3]
    print(f"数组 {nums} 的所有子集:")
    all_subsets = BitSetApplications.subsets(nums)
    for subset in all_subsets:
        print(f"  {subset}")
    
    # 组合生成示例
    print(f"\nC(5, 2) 的所有组合:")
    combs = BitSetApplications.combinations(5, 2)
    for comb in combs:
        print(f"  {comb}")
    
    # 二进制手表示例
    print(f"\n二进制手表显示1个LED亮的时间:")
    times = BitSetApplications.read_binary_watch(1)
    for i, t in enumerate(times[:5]):  # 显示前5个
        print(f"  {t}", end=" " if i < 4 else "\n")


if __name__ == "__main__":
    main()