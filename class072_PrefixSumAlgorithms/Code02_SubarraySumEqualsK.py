"""
和为K的子数组 (Subarray Sum Equals K)

题目描述:
给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的子数组的个数。
子数组是数组中元素的连续非空序列。

示例:
输入：nums = [1,1,1], k = 2
输出：2

输入：nums = [1,2,3], k = 3
输出：2

提示:
1 <= nums.length <= 2 * 10^4
-1000 <= nums[i] <= 1000
-10^7 <= k <= 10^7

题目链接: https://leetcode.com/problems/subarray-sum-equals-k/

解题思路:
使用前缀和 + 哈希表的方法。
1. 遍历数组，计算前缀和
2. 对于当前位置的前缀和sum，查找是否存在前缀和为(sum - k)的历史记录
3. 如果存在，则说明存在子数组和为k
4. 使用哈希表记录每个前缀和出现的次数

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(n) - 哈希表最多存储n个不同的前缀和

工程化考量:
1. 边界条件处理：空数组、k值极端情况
2. 哈希表选择：字典提供O(1)的平均查找时间
3. 整数溢出：Python自动处理大整数，无需担心溢出
4. 负数处理：k可能为负数，但算法本身支持负数

最优解分析:
这是最优解，因为必须遍历所有元素才能统计所有子数组。
哈希表方法将时间复杂度从O(n^2)优化到O(n)。

算法核心:
设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1] = k
即prefix[j] - k = prefix[i-1]，因此统计prefix[j] - k出现的次数即可。

算法调试技巧:
1. 打印中间过程：可以在循环中打印每个位置的前缀和和哈希表状态
2. 边界测试：测试空数组、k=0、负数等情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python是动态类型语言，无需显式声明变量类型。
Python字典自动处理哈希冲突，语法简洁。
与Java/C++相比，Python有更简洁的语法和内置的数据结构操作。
"""

class Solution:
    def subarraySum(self, nums, k):
        """
        计算和为k的子数组个数
        
        Args:
            nums (List[int]): 输入数组
            k (int): 目标和
            
        Returns:
            int: 和为k的子数组个数
            
        异常场景处理:
        - 空数组：返回0
        - k值极端：可能为极大值或极小值
        - 数组元素包含负数：算法本身支持
        
        边界条件:
        - 数组长度为0
        - k=0的情况（需要特殊处理空子数组）
        - 数组元素全为0且k=0
        """
        # 边界情况处理
        if not nums:
            return 0
        
        # 使用字典记录前缀和及其出现次数
        # 初始化：前缀和为0出现1次（表示空数组）
        prefix_sum_count = {0: 1}
        
        count = 0       # 结果计数
        prefix_sum = 0  # 当前前缀和
        
        # 遍历数组
        for i, num in enumerate(nums):
            # 更新前缀和
            prefix_sum += num
            
            # 调试打印：显示中间过程
            # print(f"位置 {i}: 前缀和 = {prefix_sum}, 目标 = {prefix_sum - k}")
            
            # 查找是否存在前缀和为(prefix_sum - k)的历史记录
            # 如果存在，说明存在子数组和为k
            if prefix_sum - k in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - k]
                # 调试打印：找到子数组
                # print(f"找到子数组，当前计数: {count}")
            
            # 更新当前前缀和的出现次数
            prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
            
            # 调试打印：字典状态
            # print(f"字典更新: {prefix_sum} -> {prefix_sum_count[prefix_sum]}")
        
        return count

def test_subarray_sum():
    """单元测试函数"""
    print("=== 和为K的子数组单元测试 ===")
    solution = Solution()
    
    # 测试用例1：经典情况
    nums1 = [1, 1, 1]
    k1 = 2
    result1 = solution.subarraySum(nums1, k1)
    print(f"测试用例1 [1,1,1] k=2: {result1} (预期: 2)")
    
    # 测试用例2：多个子数组
    nums2 = [1, 2, 3]
    k2 = 3
    result2 = solution.subarraySum(nums2, k2)
    print(f"测试用例2 [1,2,3] k=3: {result2} (预期: 2)")
    
    # 测试用例3：包含0和负数
    nums3 = [1, -1, 0]
    k3 = 0
    result3 = solution.subarraySum(nums3, k3)
    print(f"测试用例3 [1,-1,0] k=0: {result3} (预期: 3)")
    
    # 测试用例4：单个元素
    nums4 = [5]
    k4 = 5
    result4 = solution.subarraySum(nums4, k4)
    print(f"测试用例4 [5] k=5: {result4} (预期: 1)")
    
    # 测试用例5：空数组
    nums5 = []
    k5 = 1
    result5 = solution.subarraySum(nums5, k5)
    print(f"测试用例5 [] k=1: {result5} (预期: 0)")
    
    # 测试用例6：大k值
    nums6 = [1, 2, 3]
    k6 = 100
    result6 = solution.subarraySum(nums6, k6)
    print(f"测试用例6 [1,2,3] k=100: {result6} (预期: 0)")
    
    # 测试用例7：全0数组且k=0
    nums7 = [0, 0, 0]
    k7 = 0
    result7 = solution.subarraySum(nums7, k7)
    print(f"测试用例7 [0,0,0] k=0: {result7} (预期: 6)")

def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    solution = Solution()
    size = 20000  # 2万元素（题目上限）
    large_array = [i % 100 - 50 for i in range(size)]  # 包含正负数
    
    import time
    start_time = time.time()
    result = solution.subarraySum(large_array, 0)  # 测试k=0的情况
    end_time = time.time()
    
    print(f"处理 {size} 个元素，结果: {result}, 耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_subarray_sum()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("\n=== 测试完成 ===")