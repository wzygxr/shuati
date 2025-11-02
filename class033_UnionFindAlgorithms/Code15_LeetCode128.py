from typing import List

class UnionFind:
    """
    并查集实现，专门用于处理整数序列
    """
    def __init__(self, nums: List[int]):
        self.parent = {}
        self.size = {}
        self.max_size = 0
        
        # 初始化并查集
        for num in nums:
            self.parent[num] = num
            self.size[num] = 1
        if nums:
            self.max_size = 1
    
    def contains(self, num: int) -> bool:
        """检查数字是否存在于并查集中"""
        return num in self.parent
    
    def find(self, x: int) -> int:
        """查找操作，使用路径压缩优化"""
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x: int, y: int) -> None:
        """合并操作"""
        if not self.contains(x) or not self.contains(y):
            return
        
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x != root_y:
            # 按大小合并，小树合并到大树下
            if self.size[root_x] < self.size[root_y]:
                self.parent[root_x] = root_y
                self.size[root_y] += self.size[root_x]
                self.max_size = max(self.max_size, self.size[root_y])
            else:
                self.parent[root_y] = root_x
                self.size[root_x] += self.size[root_y]
                self.max_size = max(self.max_size, self.size[root_x])
    
    def get_max_size(self) -> int:
        """获取最大集合大小"""
        return self.max_size

class Solution:
    """
    LeetCode 128. 最长连续序列
    链接: https://leetcode.cn/problems/longest-consecutive-sequence/
    难度: 中等
    
    题目描述:
    给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
    请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
    
    示例 1:
    输入: nums = [100,4,200,1,3,2]
    输出: 4
    解释: 最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
    
    示例 2:
    输入: nums = [0,3,7,2,5,8,4,6,0,1]
    输出: 9
    
    约束条件:
    0 <= nums.length <= 10^5
    -10^9 <= nums[i] <= 10^9
    """
    
    def longestConsecutive(self, nums: List[int]) -> int:
        """
        方法1: 使用并查集解决最长连续序列问题
        时间复杂度: O(n * α(n)) ≈ O(n)，其中α是阿克曼函数的反函数
        空间复杂度: O(n)
        
        解题思路:
        1. 使用哈希表记录每个数字对应的并查集节点
        2. 对于每个数字，检查其相邻数字是否存在，如果存在则合并集合
        3. 记录每个集合的大小，返回最大集合的大小
        """
        if not nums:
            return 0
        
        uf = UnionFind(nums)
        
        # 遍历数组，合并相邻数字
        for num in nums:
            # 如果存在num-1，则合并num和num-1
            if uf.contains(num - 1):
                uf.union(num, num - 1)
            # 如果存在num+1，则合并num和num+1
            if uf.contains(num + 1):
                uf.union(num, num + 1)
        
        return uf.get_max_size()
    
    def longestConsecutiveHashSet(self, nums: List[int]) -> int:
        """
        方法2: 使用哈希表 + 遍历的优化解法
        时间复杂度: O(n)
        空间复杂度: O(n)
        
        解题思路:
        1. 将所有数字存入哈希表
        2. 遍历数组，对于每个数字，如果它是序列的起点（即num-1不存在），则向后查找连续序列
        3. 记录最长序列长度
        """
        if not nums:
            return 0
        
        num_set = set(nums)
        longest_streak = 0
        
        for num in num_set:
            # 只有当num是序列的起点时才进行查找
            if num - 1 not in num_set:
                current_num = num
                current_streak = 1
                
                # 向后查找连续序列
                while current_num + 1 in num_set:
                    current_num += 1
                    current_streak += 1
                
                longest_streak = max(longest_streak, current_streak)
        
        return longest_streak
    
    def longestConsecutiveSort(self, nums: List[int]) -> int:
        """
        方法3: 排序解法（不满足O(n)时间复杂度要求，但思路简单）
        时间复杂度: O(n log n)
        空间复杂度: O(1) 或 O(n)（取决于排序算法）
        """
        if not nums:
            return 0
        
        nums_sorted = sorted(nums)
        
        longest_streak = 1
        current_streak = 1
        
        for i in range(1, len(nums_sorted)):
            # 处理重复数字
            if nums_sorted[i] != nums_sorted[i - 1]:
                # 检查是否连续
                if nums_sorted[i] == nums_sorted[i - 1] + 1:
                    current_streak += 1
                else:
                    longest_streak = max(longest_streak, current_streak)
                    current_streak = 1
            # 如果数字重复，保持current_streak不变
        
        return max(longest_streak, current_streak)

def test_solution():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1
    nums1 = [100, 4, 200, 1, 3, 2]
    print(f"测试用例1 - 并查集解法: {solution.longestConsecutive(nums1)}")  # 预期: 4
    print(f"测试用例1 - 哈希表解法: {solution.longestConsecutiveHashSet(nums1)}")  # 预期: 4
    print(f"测试用例1 - 排序解法: {solution.longestConsecutiveSort(nums1)}")  # 预期: 4
    
    # 测试用例2
    nums2 = [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]
    print(f"测试用例2 - 并查集解法: {solution.longestConsecutive(nums2)}")  # 预期: 9
    print(f"测试用例2 - 哈希表解法: {solution.longestConsecutiveHashSet(nums2)}")  # 预期: 9
    print(f"测试用例2 - 排序解法: {solution.longestConsecutiveSort(nums2)}")  # 预期: 9
    
    # 测试用例3: 空数组
    nums3 = []
    print(f"测试用例3 - 并查集解法: {solution.longestConsecutive(nums3)}")  # 预期: 0
    print(f"测试用例3 - 哈希表解法: {solution.longestConsecutiveHashSet(nums3)}")  # 预期: 0
    print(f"测试用例3 - 排序解法: {solution.longestConsecutiveSort(nums3)}")  # 预期: 0
    
    # 测试用例4: 单个元素
    nums4 = [5]
    print(f"测试用例4 - 并查集解法: {solution.longestConsecutive(nums4)}")  # 预期: 1
    print(f"测试用例4 - 哈希表解法: {solution.longestConsecutiveHashSet(nums4)}")  # 预期: 1
    print(f"测试用例4 - 排序解法: {solution.longestConsecutiveSort(nums4)}")  # 预期: 1
    
    # 测试用例5: 重复元素
    nums5 = [1, 2, 0, 1]
    print(f"测试用例5 - 并查集解法: {solution.longestConsecutive(nums5)}")  # 预期: 3
    print(f"测试用例5 - 哈希表解法: {solution.longestConsecutiveHashSet(nums5)}")  # 预期: 3
    print(f"测试用例5 - 排序解法: {solution.longestConsecutiveSort(nums5)}")  # 预期: 3

if __name__ == "__main__":
    test_solution()