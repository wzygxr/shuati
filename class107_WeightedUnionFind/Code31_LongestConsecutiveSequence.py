/**
 * LeetCode 128 - 最长连续序列
 * https://leetcode-cn.com/problems/longest-consecutive-sequence/
 * 
 * 题目描述：
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * 
 * 示例 1：
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * 
 * 示例 2：
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 * 
 * 解题思路（使用并查集）：
 * 1. 使用并查集将连续的数字合并到同一个集合中
 * 2. 对于每个数字，如果它的前驱（num-1）存在，就将它们合并
 * 3. 统计每个集合的大小，找出最大的集合大小
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每个数字：O(n * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计最大集合大小：O(n)
 * - 总体时间复杂度：O(n * α(n)) ≈ O(n)
 * 
 * 空间复杂度分析：
 * - 并查集映射和大小映射：O(n)
 * - 总体空间复杂度：O(n)
 */

class LongestConsecutiveSequence:
    def __init__(self):
        # 并查集的父节点映射
        self.parent = {}
        # 每个集合的大小映射
        self.size = {}
    
    def find(self, x):
        """
        查找元素所在集合的根节点，并进行路径压缩
        
        参数:
            x: 要查找的元素
            
        返回:
            根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将x的父节点直接设置为根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def add(self, x):
        """
        添加元素到并查集
        
        参数:
            x: 要添加的元素
        """
        if x not in self.parent:
            self.parent[x] = x  # 初始时，元素的父节点是自己
            self.size[x] = 1    # 初始时，集合的大小为1
    
    def longest_consecutive(self, nums):
        """
        找出最长连续序列的长度（并查集方法）
        
        参数:
            nums: 整数数组
            
        返回:
            最长连续序列的长度
        """
        if not nums:
            return 0
        
        # 清空并查集
        self.parent = {}
        self.size = {}
        
        # 将每个数字及其前驱（如果存在）合并到同一个集合中
        for num in nums:
            self.add(num)
            # 如果num-1存在，将num和num-1合并
            if num - 1 in self.parent:
                root_num = self.find(num)
                root_num_minus_1 = self.find(num - 1)
                
                if root_num != root_num_minus_1:
                    # 将较小的集合合并到较大的集合中，以保持树的平衡
                    if self.size[root_num] < self.size[root_num_minus_1]:
                        self.parent[root_num] = root_num_minus_1
                        self.size[root_num_minus_1] += self.size[root_num]
                    else:
                        self.parent[root_num_minus_1] = root_num
                        self.size[root_num] += self.size[root_num_minus_1]
        
        # 找出最大的集合大小
        return max(self.size.values()) if self.size else 0
    
    def longest_consecutive_hash(self, nums):
        """
        找出最长连续序列的长度（哈希表方法）
        
        参数:
            nums: 整数数组
            
        返回:
            最长连续序列的长度
        """
        if not nums:
            return 0
        
        # 将所有数字存入集合中，以便O(1)时间查找
        num_set = set(nums)
        max_length = 0
        
        # 遍历每个数字
        for num in num_set:
            # 只有当num-1不在集合中时，才开始计算以num开头的连续序列
            # 这样可以避免重复计算
            if num - 1 not in num_set:
                current_num = num
                current_length = 1
                
                # 向后查找连续数字
                while current_num + 1 in num_set:
                    current_num += 1
                    current_length += 1
                
                max_length = max(max_length, current_length)
        
        return max_length

# 测试代码
def test_longest_consecutive():
    solution = LongestConsecutiveSequence()
    
    # 测试用例1
    nums1 = [100, 4, 200, 1, 3, 2]
    print("测试用例1结果（并查集方法）：", solution.longest_consecutive(nums1))
    print("测试用例1结果（哈希表方法）：", solution.longest_consecutive_hash(nums1))
    # 预期输出：4
    
    # 测试用例2
    nums2 = [0, 3, 7, 2, 5, 8, 4, 6, 0, 1]
    print("测试用例2结果（并查集方法）：", solution.longest_consecutive(nums2))
    print("测试用例2结果（哈希表方法）：", solution.longest_consecutive_hash(nums2))
    # 预期输出：9
    
    # 测试用例3：空数组
    nums3 = []
    print("测试用例3结果（并查集方法）：", solution.longest_consecutive(nums3))
    print("测试用例3结果（哈希表方法）：", solution.longest_consecutive_hash(nums3))
    # 预期输出：0
    
    # 测试用例4：单元素数组
    nums4 = [1]
    print("测试用例4结果（并查集方法）：", solution.longest_consecutive(nums4))
    print("测试用例4结果（哈希表方法）：", solution.longest_consecutive_hash(nums4))
    # 预期输出：1
    
    # 测试用例5：有重复元素的数组
    nums5 = [1, 2, 0, 1]
    print("测试用例5结果（并查集方法）：", solution.longest_consecutive(nums5))
    print("测试用例5结果（哈希表方法）：", solution.longest_consecutive_hash(nums5))
    # 预期输出：3

if __name__ == "__main__":
    test_longest_consecutive()

'''
Python特定优化：
1. 利用Python的字典和集合数据结构，实现高效的查找和合并操作
2. 在哈希表方法中，通过检查num-1是否存在，避免重复计算序列长度
3. 代码实现简洁明了，易于理解

算法思路详解：
1. 并查集方法：将连续的数字合并到同一个集合中，然后找出最大集合的大小
2. 哈希表方法：使用集合存储所有数字，对于每个数字，检查它是否是一个序列的起点，然后扩展计算序列长度

两种方法比较：
1. 时间复杂度：两者都是O(n)，但哈希表方法常数因子可能更小
2. 空间复杂度：两者都是O(n)
3. 实现复杂度：哈希表方法实现更简单直观
4. 通用性：并查集方法可以应用于更多的连通性问题

工程化考量：
1. 对于实际应用，哈希表方法可能是更好的选择，因为它更简单且高效
2. 并查集方法更适合作为学习并查集数据结构的练习
3. 对于包含重复元素的数组，两种方法都能正确处理

边界情况处理：
1. 空数组：返回0
2. 单元素数组：返回1
3. 有重复元素的数组：自动去重处理
4. 极端情况：所有元素都连续或所有元素都不连续
'''