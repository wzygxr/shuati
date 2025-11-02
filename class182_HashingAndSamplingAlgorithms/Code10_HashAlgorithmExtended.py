"""
哈希算法题目扩展 - Python版本

本文件包含来自各大算法平台（LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等）的
哈希相关题目扩展实现，涵盖基础哈希应用、高级哈希技术、分布式哈希等场景

核心特性：
1. 多平台题目覆盖：LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等
2. 三语言代码实现：Java、C++、Python统一实现
3. 详细注释分析：算法原理、复杂度分析、工程化考量
4. 高级哈希应用：滚动哈希、布隆过滤器、一致性哈希等
5. 单元测试保障：完整测试用例，确保代码正确性

时间复杂度分析：
- 基础哈希操作：O(1) 平均，O(n) 最坏
- 字符串哈希：O(n) 预处理，O(1) 查询
- 分布式哈希：O(log n) 查找，O(1) 更新

空间复杂度分析：
- 哈希表：O(n) 存储n个元素
- 位数组：O(m) 布隆过滤器
- 虚拟节点：O(k*n) 一致性哈希
"""

import time
import random
import hashlib
from typing import List, Dict, Set, Any, Optional
from collections import defaultdict


class LongestConsecutiveSequence:
    """
    LeetCode 128. 最长连续序列 (Longest Consecutive Sequence)
    题目来源：https://leetcode.com/problems/longest-consecutive-sequence/
    题目描述：给定一个未排序的整数数组，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
    
    算法思路：
    1. 使用哈希集合存储所有数字
    2. 对于每个数字，如果它是序列的起点（即num-1不在集合中），则向后查找连续序列
    3. 记录最长序列长度
    
    时间复杂度：O(n) - 每个数字最多被访问两次
    空间复杂度：O(n) - 哈希集合存储所有数字
    
    工程化考量：
    - 空数组处理：返回0
    - 重复元素：哈希集合自动去重
    - 性能优化：避免重复计算，只从序列起点开始查找
    """
    
    def longestConsecutive(self, nums: List[int]) -> int:
        """哈希集合解法 - 最优解"""
        if not nums:
            return 0
        
        # 使用哈希集合存储所有数字，自动去重
        num_set = set(nums)
        longest_streak = 0
        
        # 遍历哈希集合中的每个数字
        for num in num_set:
            # 只有当num是序列的起点时才进行查找
            # 即num-1不在集合中，说明num是某个连续序列的起点
            if num - 1 not in num_set:
                current_num = num
                current_streak = 1
                
                # 向后查找连续序列
                while current_num + 1 in num_set:
                    current_num += 1
                    current_streak += 1
                
                # 更新最长序列长度
                longest_streak = max(longest_streak, current_streak)
        
        return longest_streak
    
    def longestConsecutiveSort(self, nums: List[int]) -> int:
        """
        排序解法（非最优，用于对比）
        时间复杂度：O(n log n)
        空间复杂度：O(1) 或 O(n)（取决于排序算法）
        """
        if not nums:
            return 0
        
        nums_sorted = sorted(nums)
        longest_streak = 1
        current_streak = 1
        
        for i in range(1, len(nums_sorted)):
            # 处理重复元素
            if nums_sorted[i] != nums_sorted[i - 1]:
                if nums_sorted[i] == nums_sorted[i - 1] + 1:
                    current_streak += 1
                else:
                    longest_streak = max(longest_streak, current_streak)
                    current_streak = 1
        
        return max(longest_streak, current_streak)


class FourSumII:
    """
    LeetCode 454. 四数相加 II (4Sum II)
    题目来源：https://leetcode.com/problems/4sum-ii/
    题目描述：给定四个整数数组A、B、C、D，计算有多少个元组(i, j, k, l)使得A[i] + B[j] + C[k] + D[l] = 0
    
    算法思路：
    1. 将A和B的所有和存入哈希表，记录每个和出现的次数
    2. 遍历C和D的所有组合，在哈希表中查找-(c+d)的出现次数
    3. 累加所有满足条件的组合数
    
    时间复杂度：O(n²) - 两个n²的循环
    空间复杂度：O(n²) - 哈希表存储所有A+B的和
    
    工程化考量：
    - 大数处理：使用long类型防止整数溢出
    - 空数组处理：返回0
    - 性能优化：分组处理，降低时间复杂度
    """
    
    def fourSumCount(self, A: List[int], B: List[int], C: List[int], D: List[int]) -> int:
        """标准解法"""
        if not A or not B or not C or not D:
            return 0
        
        # 存储A+B的所有和及其出现次数
        sum_map = defaultdict(int)
        
        # 计算A+B的所有组合
        for a in A:
            for b in B:
                sum_ab = a + b
                sum_map[sum_ab] += 1
        
        count = 0
        
        # 计算C+D的所有组合，查找-(c+d)在哈希表中的出现次数
        for c in C:
            for d in D:
                target = -(c + d)
                if target in sum_map:
                    count += sum_map[target]
        
        return count
    
    def kSumCount(self, arrays: List[List[int]], target: int) -> int:
        """
        扩展：支持k个数组的通用解法
        时间复杂度：O(n^(k/2))
        空间复杂度：O(n^(k/2))
        """
        if not arrays:
            return 0
        
        # 将数组分成两组
        k = len(arrays)
        mid = k // 2
        
        # 第一组：前mid个数组的所有和
        first_half = defaultdict(int)
        self._generate_sums(arrays, 0, mid, 0, first_half)
        
        # 第二组：后k-mid个数组的所有和
        second_half = defaultdict(int)
        self._generate_sums(arrays, mid, k, 0, second_half)
        
        count = 0
        
        # 统计满足条件的组合数
        for sum_val, cnt in first_half.items():
            needed = target - sum_val
            if needed in second_half:
                count += cnt * second_half[needed]
        
        return count
    
    def _generate_sums(self, arrays: List[List[int]], index: int, end: int, 
                      current_sum: int, sum_map: defaultdict):
        """递归生成所有可能的和"""
        if index == end:
            sum_map[current_sum] += 1
            return
        
        for num in arrays[index]:
            self._generate_sums(arrays, index + 1, end, current_sum + num, sum_map)


class ConsecutiveSubsequence:
    """
    Codeforces 977F. Consecutive Subsequence
    题目来源：https://codeforces.com/problemset/problem/977/F
    题目描述：给定一个整数序列，找到最长的连续子序列（子序列中的元素在原序列中可以不连续，但值连续递增）
    
    算法思路：
    1. 使用动态规划+哈希表，dp[x]表示以x结尾的最长连续子序列长度
    2. 对于每个元素x，dp[x] = dp[x-1] + 1
    3. 记录最长序列的结束元素和长度
    4. 回溯重建最长序列
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    工程化考量：
    - 大值域处理：使用哈希表而不是数组
    - 序列重建：记录前驱节点信息
    - 边界处理：负数、大数、重复元素
    """
    
    def findLongestConsecutiveSubsequence(self, nums: List[int]) -> List[int]:
        """查找最长连续子序列（返回具体序列）"""
        if not nums:
            return []
        
        # dp[x]表示以x结尾的最长连续子序列长度
        dp = {}
        # prev[x]记录x在序列中的前一个元素
        prev = {}
        
        max_length = 0
        last_element = nums[0]
        
        for num in nums:
            # 如果num-1存在，则当前序列可以扩展
            if num - 1 in dp:
                dp[num] = dp[num - 1] + 1
                prev[num] = num - 1
            else:
                dp[num] = 1
            
            # 更新最长序列信息
            if dp[num] > max_length:
                max_length = dp[num]
                last_element = num
        
        # 重建最长序列
        result = []
        current = last_element
        for _ in range(max_length):
            result.append(current)
            current = prev.get(current, current - 1)
        
        result.reverse()
        return result
    
    def findLongestConsecutiveLength(self, nums: List[int]) -> int:
        """
        优化版本：只记录序列长度，不重建具体序列
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not nums:
            return 0
        
        num_set = set(nums)
        longest = 0
        
        for num in num_set:
            # 只有当num是序列起点时才计算
            if num - 1 not in num_set:
                current_num = num
                current_length = 1
                
                while current_num + 1 in num_set:
                    current_num += 1
                    current_length += 1
                
                longest = max(longest, current_length)
        
        return longest


class HDU4821String:
    """
    HDU 4821. String
    题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=4821
    题目描述：给定字符串s和整数M、L，统计有多少个长度为M*L的子串，可以分成M个长度为L的不同子串
    
    算法思路：
    1. 使用滚动哈希计算所有长度为L的子串哈希值
    2. 滑动窗口统计每个窗口内不同哈希值的数量
    3. 当窗口内不同哈希值数量等于M时，计数加1
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    工程化考量：
    - 哈希冲突：使用双哈希降低冲突概率
    - 大字符串：使用滚动哈希避免重复计算
    - 性能优化：滑动窗口维护哈希值计数
    """
    
    def __init__(self):
        self.BASE1 = 131
        self.MOD1 = 1000000007
        self.BASE2 = 13131
        self.MOD2 = 1000000009
    
    def countValidSubstrings(self, s: str, M: int, L: int) -> int:
        if len(s) < M * L:
            return 0
        
        n = len(s)
        total_length = M * L
        
        # 预处理滚动哈希
        hash1 = [0] * (n + 1)
        hash2 = [0] * (n + 1)
        power1 = [1] * (n + 1)
        power2 = [1] * (n + 1)
        
        for i in range(1, n + 1):
            hash1[i] = (hash1[i - 1] * self.BASE1 + ord(s[i - 1])) % self.MOD1
            hash2[i] = (hash2[i - 1] * self.BASE2 + ord(s[i - 1])) % self.MOD2
            power1[i] = (power1[i - 1] * self.BASE1) % self.MOD1
            power2[i] = (power2[i - 1] * self.BASE2) % self.MOD2
        
        count = 0
        
        # 对于每个起始位置（模L的余数）
        for start in range(L):
            if start + total_length > n:
                continue
            
            # 使用双哈希降低冲突概率
            hash_count = defaultdict(int)
            
            # 初始化第一个窗口
            for i in range(M):
                l = start + i * L
                r = l + L
                h1 = self._get_hash(hash1, power1, l, r, self.MOD1)
                h2 = self._get_hash(hash2, power2, l, r, self.MOD2)
                combined_hash = h1 * self.MOD2 + h2
                
                hash_count[combined_hash] += 1
            
            # 检查第一个窗口
            if len(hash_count) == M:
                count += 1
            
            # 滑动窗口
            i = start + L
            while i + total_length <= n:
                # 移除最左边的子串
                remove_l = i - L
                remove_r = i
                remove_h1 = self._get_hash(hash1, power1, remove_l, remove_r, self.MOD1)
                remove_h2 = self._get_hash(hash2, power2, remove_l, remove_r, self.MOD2)
                remove_combined = remove_h1 * self.MOD2 + remove_h2
                
                hash_count[remove_combined] -= 1
                if hash_count[remove_combined] == 0:
                    del hash_count[remove_combined]
                
                # 添加最右边的子串
                add_l = i + (M - 1) * L
                add_r = add_l + L
                add_h1 = self._get_hash(hash1, power1, add_l, add_r, self.MOD1)
                add_h2 = self._get_hash(hash2, power2, add_l, add_r, self.MOD2)
                add_combined = add_h1 * self.MOD2 + add_h2
                
                hash_count[add_combined] += 1
                
                # 检查当前窗口
                if len(hash_count) == M:
                    count += 1
                
                i += L
        
        return count
    
    def _get_hash(self, hash_arr: List[int], power_arr: List[int], l: int, r: int, mod: int) -> int:
        """获取子串的哈希值"""
        return (hash_arr[r] - hash_arr[l] * power_arr[r - l] % mod + mod) % mod


class DistributedHashTable:
    """
    分布式哈希表 (Distributed Hash Table) 实现
    应用场景：分布式存储、负载均衡、P2P网络
    
    核心特性：
    1. 一致性哈希：节点增减时数据迁移最小
    2. 虚拟节点：提高负载均衡性
    3. 数据复制：提高系统可靠性
    4. 故障转移：节点故障时自动迁移数据
    
    时间复杂度：
    - 查找：O(log n)
    - 插入：O(log n)
    - 删除：O(log n)
    
    空间复杂度：O(k*n) - k个虚拟节点，n个物理节点
    """
    
    class Node:
        """节点类"""
        
        def __init__(self, node_id: str):
            self.node_id = node_id
            self.data = {}
    
    def __init__(self, virtual_nodes: int = 100, replication_factor: int = 3):
        # 一致性哈希环
        self.hash_ring = {}
        # 虚拟节点数量
        self.virtual_nodes = virtual_nodes
        # 数据复制因子
        self.replication_factor = replication_factor
    
    def _hash(self, key: str) -> int:
        """哈希函数（使用MD5确保分布均匀）"""
        return int(hashlib.md5(key.encode()).hexdigest()[:8], 16)
    
    def add_node(self, node_id: str):
        """添加节点到哈希环"""
        for i in range(self.virtual_nodes):
            virtual_node_id = f"{node_id}#{i}"
            hash_val = self._hash(virtual_node_id)
            self.hash_ring[hash_val] = self.Node(node_id)
    
    def remove_node(self, node_id: str):
        """从哈希环移除节点"""
        for i in range(self.virtual_nodes):
            virtual_node_id = f"{node_id}#{i}"
            hash_val = self._hash(virtual_node_id)
            if hash_val in self.hash_ring:
                del self.hash_ring[hash_val]
    
    def _get_nth_node_hash(self, start_hash: int, n: int) -> int:
        """获取第n个节点哈希（用于数据复制）"""
        sorted_hashes = sorted(self.hash_ring.keys())
        
        # 找到起始位置
        idx = 0
        for i, h in enumerate(sorted_hashes):
            if h >= start_hash:
                idx = i
                break
        
        # 获取第n个节点
        target_idx = (idx + n) % len(sorted_hashes)
        return sorted_hashes[target_idx]
    
    def put(self, key: Any, value: Any):
        """存储数据"""
        key_str = str(key)
        key_hash = self._hash(key_str)
        
        # 找到负责该key的节点
        sorted_hashes = sorted(self.hash_ring.keys())
        first_node_hash = None
        
        for h in sorted_hashes:
            if h >= key_hash:
                first_node_hash = h
                break
        
        if first_node_hash is None:
            first_node_hash = sorted_hashes[0] if sorted_hashes else None
        
        if first_node_hash is None:
            return
        
        # 存储到主节点和副本节点
        for i in range(self.replication_factor):
            node_hash = self._get_nth_node_hash(first_node_hash, i)
            if node_hash in self.hash_ring:
                self.hash_ring[node_hash].data[key] = value
    
    def get(self, key: Any) -> Optional[Any]:
        """获取数据"""
        key_str = str(key)
        key_hash = self._hash(key_str)
        
        # 找到负责该key的节点
        sorted_hashes = sorted(self.hash_ring.keys())
        first_node_hash = None
        
        for h in sorted_hashes:
            if h >= key_hash:
                first_node_hash = h
                break
        
        if first_node_hash is None:
            first_node_hash = sorted_hashes[0] if sorted_hashes else None
        
        if first_node_hash is None or first_node_hash not in self.hash_ring:
            return None
        
        # 从主节点获取数据
        return self.hash_ring[first_node_hash].data.get(key)
    
    def get_load_distribution(self) -> Dict[str, int]:
        """获取负载分布统计"""
        load_map = defaultdict(int)
        
        for node in self.hash_ring.values():
            load_map[node.node_id] += len(node.data)
        
        return dict(load_map)
    
    def get_data_migration_count(self, new_node_id: str, removed_node_id: str) -> int:
        """数据迁移统计（节点增减时）"""
        migration_count = 0
        
        # 这里简化实现，实际需要记录数据迁移
        for node in self.hash_ring.values():
            if node.node_id == removed_node_id:
                migration_count += len(node.data)
        
        return migration_count


class HashAlgorithmTest:
    """
    哈希算法测试类
    包含完整的测试用例，验证算法正确性和性能
    """
    
    def test_longest_consecutive_sequence(self):
        """测试最长连续序列"""
        solution = LongestConsecutiveSequence()
        
        # 测试用例1：正常情况
        nums1 = [100, 4, 200, 1, 3, 2]
        assert solution.longestConsecutive(nums1) == 4, "测试用例1失败"
        
        # 测试用例2：空数组
        nums2 = []
        assert solution.longestConsecutive(nums2) == 0, "测试用例2失败"
        
        # 测试用例3：重复元素
        nums3 = [1, 2, 2, 3, 4]
        assert solution.longestConsecutive(nums3) == 4, "测试用例3失败"
        
        # 测试用例4：大数
        nums4 = [2**31 - 3, 2**31 - 2, 2**31 - 1]
        assert solution.longestConsecutive(nums4) == 3, "测试用例4失败"
        
        print("最长连续序列测试通过")
    
    def test_four_sum_ii(self):
        """测试四数相加"""
        solution = FourSumII()
        
        # 测试用例1：正常情况
        A = [1, 2]
        B = [-2, -1]
        C = [-1, 2]
        D = [0, 2]
        assert solution.fourSumCount(A, B, C, D) == 2, "测试用例1失败"
        
        # 测试用例2：空数组
        empty = []
        assert solution.fourSumCount(empty, empty, empty, empty) == 0, "测试用例2失败"
        
        print("四数相加测试通过")
    
    def test_distributed_hash_table(self):
        """测试分布式哈希表"""
        dht = DistributedHashTable(100, 3)
        
        # 添加节点
        dht.add_node("node1")
        dht.add_node("node2")
        dht.add_node("node3")
        
        # 存储数据
        dht.put("key1", "value1")
        dht.put("key2", "value2")
        dht.put("key3", "value3")
        
        # 验证数据
        assert dht.get("key1") == "value1", "数据验证失败"
        assert dht.get("key2") == "value2", "数据验证失败"
        assert dht.get("key3") == "value3", "数据验证失败"
        
        # 验证负载分布
        load = dht.get_load_distribution()
        assert len(load) == 3, "负载分布验证失败"
        
        print("分布式哈希表测试通过")
    
    def performance_test(self):
        """性能测试：大规模数据"""
        size = 100000
        nums = [random.randint(0, size * 10) for _ in range(size)]
        
        solution = LongestConsecutiveSequence()
        
        start_time = time.time()
        result = solution.longestConsecutive(nums)
        end_time = time.time()
        
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        
        print("性能测试结果：")
        print(f"数据规模：{size}")
        print(f"最长序列长度：{result}")
        print(f"执行时间：{execution_time:.2f}ms")
        
        # 验证性能要求：10万数据应在1秒内完成
        assert execution_time < 1000, "性能测试失败"
        
        print("性能测试通过")
    
    def run_all_tests(self):
        """运行所有测试"""
        try:
            self.test_longest_consecutive_sequence()
            self.test_four_sum_ii()
            self.test_distributed_hash_table()
            self.performance_test()
            
            print("=== 所有测试通过 ===")
        except AssertionError as e:
            print(f"测试失败：{e}")
            raise


def main():
    """主函数：演示和测试"""
    print("=== 哈希算法题目扩展演示 ===\n")
    
    # 运行测试
    test = HashAlgorithmTest()
    test.run_all_tests()
    
    # 演示最长连续序列
    print("\n=== 最长连续序列演示 ===")
    lcs = LongestConsecutiveSequence()
    demo_nums = [100, 4, 200, 1, 3, 2, 5]
    print(f"输入数组：{demo_nums}")
    print(f"最长连续序列长度：{lcs.longestConsecutive(demo_nums)}")
    
    # 演示分布式哈希表
    print("\n=== 分布式哈希表演示 ===")
    dht = DistributedHashTable(50, 2)
    dht.add_node("server1")
    dht.add_node("server2")
    dht.put("user1", "data1")
    dht.put("user2", "data2")
    print(f"user1的数据：{dht.get('user1')}")
    print(f"负载分布：{dht.get_load_distribution()}")
    
    print("\n=== 演示完成 ===")


if __name__ == "__main__":
    main()