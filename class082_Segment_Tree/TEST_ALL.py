"""
测试所有算法实现的正确性
"""

import time

# 导入所有实现的算法
from Code06_ReversePairs import Solution as ReversePairsSolution
from Code07_LIS_BIT import Solution as LISSolution
from Code08_NumberOfLISAdvanced import Solution as NumberOfLISSolution
from Code09_GoodTriplets import Solution as GoodTripletsSolution

def test_reverse_pairs():
    """测试翻转对算法"""
    print("测试翻转对算法...")
    solution = ReversePairsSolution()
    
    # 测试用例1
    nums1 = [1, 3, 2, 3, 1]
    result1 = solution.reversePairs(nums1)
    print(f"输入: {nums1}")
    print(f"输出: {result1}")
    print(f"期望: 2")
    assert result1 == 2, f"测试失败，期望2，实际{result1}"
    
    # 测试用例2
    nums2 = [2, 4, 3, 5, 1]
    result2 = solution.reversePairs(nums2)
    print(f"输入: {nums2}")
    print(f"输出: {result2}")
    print(f"期望: 3")
    assert result2 == 3, f"测试失败，期望3，实际{result2}"
    
    print("翻转对算法测试通过！\n")

def test_lis():
    """测试最长递增子序列算法"""
    print("测试最长递增子序列算法...")
    solution = LISSolution()
    
    # 测试用例1
    nums1 = [10, 9, 2, 5, 3, 7, 101, 18]
    result1 = solution.lengthOfLIS(nums1)
    print(f"输入: {nums1}")
    print(f"输出: {result1}")
    print(f"期望: 4")
    assert result1 == 4, f"测试失败，期望4，实际{result1}"
    
    # 测试用例2
    nums2 = [0, 1, 0, 3, 2, 3]
    result2 = solution.lengthOfLIS(nums2)
    print(f"输入: {nums2}")
    print(f"输出: {result2}")
    print(f"期望: 4")
    assert result2 == 4, f"测试失败，期望4，实际{result2}"
    
    # 测试用例3
    nums3 = [7, 7, 7, 7, 7, 7, 7]
    result3 = solution.lengthOfLIS(nums3)
    print(f"输入: {nums3}")
    print(f"输出: {result3}")
    print(f"期望: 1")
    assert result3 == 1, f"测试失败，期望1，实际{result3}"
    
    print("最长递增子序列算法测试通过！\n")

def test_number_of_lis():
    """测试最长递增子序列的个数算法"""
    print("测试最长递增子序列的个数算法...")
    solution = NumberOfLISSolution()
    
    # 测试用例1
    nums1 = [1, 3, 5, 4, 7]
    result1 = solution.findNumberOfLIS(nums1)
    print(f"输入: {nums1}")
    print(f"输出: {result1}")
    print(f"期望: 2")
    assert result1 == 2, f"测试失败，期望2，实际{result1}"
    
    # 测试用例2
    nums2 = [2, 2, 2, 2, 2]
    result2 = solution.findNumberOfLIS(nums2)
    print(f"输入: {nums2}")
    print(f"输出: {result2}")
    print(f"期望: 5")
    assert result2 == 5, f"测试失败，期望5，实际{result2}"
    
    print("最长递增子序列的个数算法测试通过！\n")

def test_good_triplets():
    """测试好三元组数目算法"""
    print("测试好三元组数目算法...")
    solution = GoodTripletsSolution()
    
    # 测试用例1
    nums1_1 = [2, 0, 1, 3]
    nums2_1 = [0, 1, 2, 3]
    result1 = solution.goodTriplets(nums1_1, nums2_1)
    print(f"输入: nums1 = {nums1_1}, nums2 = {nums2_1}")
    print(f"输出: {result1}")
    print(f"期望: 1")
    assert result1 == 1, f"测试失败，期望1，实际{result1}"
    
    # 测试用例2
    nums1_2 = [4, 0, 1, 3, 2]
    nums2_2 = [4, 1, 0, 2, 3]
    result2 = solution.goodTriplets(nums1_2, nums2_2)
    print(f"输入: nums1 = {nums1_2}, nums2 = {nums2_2}")
    print(f"输出: {result2}")
    print(f"期望: 4")
    assert result2 == 4, f"测试失败，期望4，实际{result2}"
    
    print("好三元组数目算法测试通过！\n")

def performance_test():
    """性能测试"""
    print("性能测试...")
    
    # 生成大规模测试数据
    import random
    n = 10000
    nums = [random.randint(1, 100000) for _ in range(n)]
    
    # 测试翻转对算法性能
    solution = ReversePairsSolution()
    start_time = time.time()
    result = solution.reversePairs(nums)
    end_time = time.time()
    print(f"翻转对算法处理{n}个元素耗时: {end_time - start_time:.4f}秒")
    
    print("性能测试完成！\n")

def main():
    """主测试函数"""
    print("开始测试所有算法实现...\n")
    
    try:
        test_reverse_pairs()
        test_lis()
        test_number_of_lis()
        test_good_triplets()
        performance_test()
        
        print("所有测试通过！所有算法实现正确。")
    except Exception as e:
        print(f"测试失败: {e}")
        raise

if __name__ == "__main__":
    main()