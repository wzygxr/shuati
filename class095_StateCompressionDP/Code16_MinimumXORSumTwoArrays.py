"""
最小XOR值路径 (Minimum XOR Sum of Two Arrays) - Python实现
给你两个整数数组 nums1 和 nums2，两个数组长度相等。
一次操作中，你可以选择两个数组的任意下标 i 和 j，然后将 nums1[i] 变为 (nums1[i] XOR nums2[j])。
请你返回使 nums1 和 nums2 相等所需的最小操作次数。

题目链接: https://leetcode.cn/problems/minimum-xor-sum-of-two-arrays/
难度: 困难

解题思路:
1. 问题可以转化为二分图最小权匹配问题
2. 使用状态压缩动态规划求解
3. dp[mask]表示已经匹配了mask代表的nums2元素时的最小XOR和

时间复杂度: O(n^2 * 2^n) - 其中n是数组长度
空间复杂度: O(2^n) - DP数组大小
"""

from typing import List
import sys
from functools import lru_cache

class Solution:
    def minimumXORSum(self, nums1: List[int], nums2: List[int]) -> int:
        """
        状态压缩动态规划解法
        
        Args:
            nums1: 第一个整数数组
            nums2: 第二个整数数组
            
        Returns:
            最小XOR和
        """
        n = len(nums1)
        
        # 预处理计算XOR代价矩阵
        cost = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                cost[i][j] = nums1[i] ^ nums2[j]
        
        # dp[mask]表示已经匹配了mask代表的nums2元素时的最小XOR和
        dp = [sys.maxsize] * (1 << n)
        dp[0] = 0  # 初始状态：没有匹配任何元素
        
        # 遍历所有可能的状态
        for mask in range(1 << n):
            if dp[mask] == sys.maxsize:
                continue
            
            # 计算当前已经匹配的元素数量
            count = bin(mask).count('1')
            
            # 尝试匹配下一个nums2元素
            for j in range(n):
                # 如果nums2的第j个元素还没有被匹配
                if not (mask & (1 << j)):
                    new_mask = mask | (1 << j)
                    new_cost = dp[mask] + cost[count][j]
                    if new_cost < dp[new_mask]:
                        dp[new_mask] = new_cost
        
        return dp[(1 << n) - 1]
    
    def minimumXORSumKM(self, nums1: List[int], nums2: List[int]) -> int:
        """
        优化版：使用匈牙利算法（KM算法）求解
        时间复杂度: O(n^3)
        空间复杂度: O(n^2)
        """
        n = len(nums1)
        
        # 构建代价矩阵
        cost = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                cost[i][j] = nums1[i] ^ nums2[j]
        
        return self.hungarian(cost)
    
    def hungarian(self, cost: List[List[int]]) -> int:
        """
        匈牙利算法实现
        """
        n = len(cost)
        u = [0] * (n + 1)
        v = [0] * (n + 1)
        p = [0] * (n + 1)
        way = [0] * (n + 1)
        
        for i in range(1, n + 1):
            p[0] = i
            j0 = 0
            minv = [sys.maxsize] * (n + 1)
            used = [False] * (n + 1)
            
            while True:
                used[j0] = True
                i0 = p[j0]
                delta = sys.maxsize
                j1 = 0
                
                for j in range(1, n + 1):
                    if not used[j]:
                        cur = cost[i0 - 1][j - 1] - u[i0] - v[j]
                        if cur < minv[j]:
                            minv[j] = cur
                            way[j] = j0
                        if minv[j] < delta:
                            delta = minv[j]
                            j1 = j
                
                for j in range(n + 1):
                    if used[j]:
                        u[p[j]] += delta
                        v[j] -= delta
                    else:
                        minv[j] -= delta
                
                j0 = j1
                if p[j0] == 0:
                    break
            
            while j0 != 0:
                j1 = way[j0]
                p[j0] = p[j1]
                j0 = j1
        
        return -v[0]
    
    def minimumXORSumBacktrack(self, nums1: List[int], nums2: List[int]) -> int:
        """
        回溯法解法（用于理解问题本质）
        时间复杂度: O(n!) - 全排列
        空间复杂度: O(n) - 递归栈深度
        """
        n = len(nums1)
        used = [False] * n
        
        def backtrack(index: int, current_sum: int) -> int:
            if index == n:
                return current_sum
            
            min_sum = sys.maxsize
            for j in range(n):
                if not used[j]:
                    used[j] = True
                    new_sum = current_sum + (nums1[index] ^ nums2[j])
                    result = backtrack(index + 1, new_sum)
                    min_sum = min(min_sum, result)
                    used[j] = False
            
            return min_sum
        
        return backtrack(0, 0)
    
    def minimumXORSumMemo(self, nums1: List[int], nums2: List[int]) -> int:
        """
        记忆化回溯解法
        使用位掩码记录使用状态
        """
        n = len(nums1)
        memo = {}
        
        def backtrack(index: int, mask: int, current_sum: int) -> int:
            if index == n:
                return current_sum
            
            key = (index, mask)
            if key in memo:
                return memo[key]
            
            min_sum = sys.maxsize
            for j in range(n):
                if not (mask & (1 << j)):
                    new_mask = mask | (1 << j)
                    new_sum = current_sum + (nums1[index] ^ nums2[j])
                    result = backtrack(index + 1, new_mask, new_sum)
                    min_sum = min(min_sum, result)
            
            memo[key] = min_sum
            return min_sum
        
        return backtrack(0, 0, 0)
    
    @lru_cache(maxsize=None)
    def _minimumXORSumLru(self, nums1_tuple: tuple[int, ...], nums2_tuple: tuple[int, ...], 
                         index: int, mask: int, current_sum: int) -> int:
        """
        使用lru_cache的记忆化解法
        """
        n = len(nums1_tuple)
        if index == n:
            return current_sum
        
        min_sum = sys.maxsize
        for j in range(n):
            if not (mask & (1 << j)):
                new_mask = mask | (1 << j)
                new_sum = current_sum + (nums1_tuple[index] ^ nums2_tuple[j])
                result = self._minimumXORSumLru(nums1_tuple, nums2_tuple, index + 1, new_mask, new_sum)
                min_sum = min(min_sum, result)
        
        return min_sum
    
    def minimumXORSumLru(self, nums1: List[int], nums2: List[int]) -> int:
        """
        使用lru_cache的入口函数
        """
        # 转换为元组以便使用lru_cache
        nums1_tuple = tuple(nums1)
        nums2_tuple = tuple(nums2)
        return self._minimumXORSumLru(nums1_tuple, nums2_tuple, 0, 0, 0)

def test_minimum_xor_sum():
    """单元测试函数"""
    solution = Solution()
    
    print("=== 最小XOR值路径Python单元测试 ===")
    
    # 测试用例1: 基础用例
    nums1_1 = [1, 2]
    nums2_1 = [2, 3]
    result1 = solution.minimumXORSum(nums1_1, nums2_1)
    print(f"测试用例1: nums1=[1,2], nums2=[2,3], 结果={result1}")
    assert result1 == 2, "期望结果应为2"
    
    # 测试用例2: 较大数组
    nums1_2 = [1, 0, 3]
    nums2_2 = [5, 3, 4]
    result2 = solution.minimumXORSum(nums1_2, nums2_2)
    print(f"测试用例2: nums1=[1,0,3], nums2=[5,3,4], 结果={result2}")
    
    # 测试用例3: 相同数组
    nums1_3 = [1, 2, 3]
    nums2_3 = [1, 2, 3]
    result3 = solution.minimumXORSum(nums1_3, nums2_3)
    print(f"测试用例3: nums1=[1,2,3], nums2=[1,2,3], 结果={result3}")
    assert result3 == 0, "期望结果应为0"
    
    # 测试用例4: 边界情况
    nums1_4 = [0]
    nums2_4 = [0]
    result4 = solution.minimumXORSum(nums1_4, nums2_4)
    print(f"测试用例4: nums1=[0], nums2=[0], 结果={result4}")
    assert result4 == 0, "期望结果应为0"
    
    # 性能测试：中等规模数据
    import random
    nums1_5 = [random.randint(0, 99) for _ in range(10)]
    nums2_5 = [random.randint(0, 99) for _ in range(10)]
    
    import time
    start_time = time.time()
    result5 = solution.minimumXORSum(nums1_5, nums2_5)
    end_time = time.time()
    print(f"性能测试: 处理10元素数组耗时 {(end_time - start_time) * 1000:.2f} 毫秒")
    print(f"结果: {result5}")
    
    # 测试不同解法的正确性
    print("\n=== 不同解法对比测试 ===")
    result1a = solution.minimumXORSumBacktrack(nums1_1, nums2_1)
    result1b = solution.minimumXORSumMemo(nums1_1, nums2_1)
    result1c = solution.minimumXORSumKM(nums1_1, nums2_1)
    result1d = solution.minimumXORSumLru(nums1_1, nums2_1)
    
    print(f"状态压缩DP结果: {result1}")
    print(f"回溯法结果: {result1a}")
    print(f"记忆化回溯结果: {result1b}")
    print(f"匈牙利算法结果: {result1c}")
    print(f"LRU缓存结果: {result1d}")
    
    # 验证所有方法结果一致
    assert result1 == result1a, "不同解法结果应一致"
    assert result1 == result1b, "不同解法结果应一致"
    assert result1 == result1c, "不同解法结果应一致"
    assert result1 == result1d, "不同解法结果应一致"
    
    # 性能比较测试
    print("\n=== 性能比较测试 ===")
    test_nums1 = [1, 2, 3, 4]
    test_nums2 = [2, 3, 4, 5]
    
    start_time = time.time()
    dp_result = solution.minimumXORSum(test_nums1, test_nums2)
    dp_time = time.time() - start_time
    
    start_time = time.time()
    km_result = solution.minimumXORSumKM(test_nums1, test_nums2)
    km_time = time.time() - start_time
    
    start_time = time.time()
    memo_result = solution.minimumXORSumMemo(test_nums1, test_nums2)
    memo_time = time.time() - start_time
    
    print(f"状态压缩DP耗时: {dp_time * 1e6:.2f} 微秒")
    print(f"匈牙利算法耗时: {km_time * 1e6:.2f} 微秒")
    print(f"记忆化回溯耗时: {memo_time * 1e6:.2f} 微秒")
    
    # 大规模数据性能测试
    print("\n=== 大规模数据性能测试 ===")
    large_nums1 = [i % 20 + 1 for i in range(15)]
    large_nums2 = [i % 20 + 5 for i in range(15)]
    
    start_time = time.time()
    large_result = solution.minimumXORSum(large_nums1, large_nums2)
    large_time = time.time() - start_time
    print(f"大规模数据DP解法耗时: {large_time:.4f} 秒")
    print(f"结果: {large_result}")
    
    print("所有测试用例通过!")

if __name__ == "__main__":
    try:
        test_minimum_xor_sum()
    except AssertionError as e:
        print(f"测试失败: {e}")
    except Exception as e:
        print(f"发生错误: {e}")

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用列表推导式和内置函数简化代码
   - 利用装饰器@lru_cache实现自动记忆化
   - 动态类型使得代码更简洁

2. 性能特点：
   - 列表操作高效，但对象开销较大
   - 递归深度有限制，需要处理栈溢出
   - 字典查找效率高，适合记忆化

3. 与Java/C++的差异：
   - 语法更简洁，可读性更强
   - 动态类型 vs 静态类型
   - 垃圾回收机制不同
   - 开发效率高，运行效率相对较低

4. Python特有优化：
   - 使用lru_cache简化记忆化实现
   - 利用生成器表达式减少内存使用
   - 使用f-string进行字符串格式化

5. 算法实现技巧：
   - 动态规划：从后往前遍历避免重复计数
   - 回溯法：标准的DFS模板
   - 记忆化：使用字典或lru_cache

6. 工程化考量：
   - 类型提示提高代码可读性
   - 文档字符串提供API文档
   - 单元测试框架完善
   - 异常处理机制健全

7. 调试和开发：
   - REPL环境便于快速测试
   - 丰富的第三方库支持
   - 调试工具完善

注意事项：
1. 递归深度：Python默认递归深度有限，大规模数据可能栈溢出
2. 内存使用：大规模数据时注意内存限制
3. 性能优化：对于计算密集型任务，考虑使用PyPy或C扩展
4. 数值范围：Python整数无溢出问题，但需要注意性能

算法选择建议：
- 小规模数据（n ≤ 10）：回溯法或记忆化回溯
- 中等规模（10 < n ≤ 16）：状态压缩DP
- 大规模数据（n > 16）：匈牙利算法
- 当n非常大时：可能需要近似算法

扩展思考：
1. 如果数组长度不相等？
2. 如果要求输出具体的匹配方案？
3. 如果XOR操作有其他约束条件？
4. 如何优化以处理更大规模的数据？
"""

# 最小XOR值路径 (Minimum XOR Sum of Two Arrays)
# 给你两个整数数组 nums1 和 nums2 ，它们的长度都为 n 。
# 你需要将 nums1 和 nums2 中的元素重新排列，使得 nums1[i] XOR nums2[j] 的结果之和最小。
# 返回重新排列后异或和的最小值。
# 测试链接 : https://leetcode.cn/problems/minimum-xor-sum-of-two-arrays/

class Code16_MinimumXORSumTwoArrays:
    
    # 使用状态压缩动态规划解决最小XOR值路径问题
    # 核心思想：用二进制位表示nums2中已使用的元素，通过状态转移找到最小异或和
    # 时间复杂度: O(n^2 * 2^n)
    # 空间复杂度: O(2^n)
    @staticmethod
    def minimumXORSum(nums1, nums2):
        n = len(nums1)
        
        # dp[mask] 表示使用mask代表的nums2元素与nums1的前bin(mask).count('1')个元素匹配的最小异或和
        dp = [float('inf')] * (1 << n)
        # 初始状态：不使用任何nums2元素，异或和为0
        dp[0] = 0
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << n):
            # 如果当前状态不可达，跳过
            if dp[mask] == float('inf'):
                continue
            
            # 计算已使用的nums2元素个数（即当前要匹配的nums1元素索引）
            pos = bin(mask).count('1')
            
            # 枚举下一个要使用的nums2元素
            for i in range(n):
                # 如果第i个nums2元素还未使用
                if (mask & (1 << i)) == 0:
                    # 计算新的状态和异或和
                    new_mask = mask | (1 << i)
                    xor_val = nums1[pos] ^ nums2[i]
                    # 更新状态：使用new_mask代表的元素能获得的最小异或和
                    dp[new_mask] = min(dp[new_mask], dp[mask] + xor_val)
        
        # 返回使用所有nums2元素能获得的最小异或和
        return dp[(1 << n) - 1]

# 测试代码
if __name__ == "__main__":
    solution = Code16_MinimumXORSumTwoArrays()
    
    # 测试用例1: 基础用例
    nums1_1 = [1, 2]
    nums2_1 = [2, 3]
    result1 = solution.minimumXORSum(nums1_1, nums2_1)
    print(f"测试用例1: nums1={nums1_1}, nums2={nums2_1}, 结果={result1}")  # 期望输出: 2
    
    # 测试用例2: 较大数组
    nums1_2 = [1, 0, 3]
    nums2_2 = [5, 3, 4]
    result2 = solution.minimumXORSum(nums1_2, nums2_2)
    print(f"测试用例2: nums1={nums1_2}, nums2={nums2_2}, 结果={result2}")
    
    # 测试用例3: 相同数组
    nums1_3 = [1, 2, 3]
    nums2_3 = [1, 2, 3]
    result3 = solution.minimumXORSum(nums1_3, nums2_3)
    print(f"测试用例3: nums1={nums1_3}, nums2={nums2_3}, 结果={result3}")  # 期望输出: 0
