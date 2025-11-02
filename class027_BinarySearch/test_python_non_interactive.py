"""
测试Python代码（非交互式）
"""

import sys
import os

# 添加当前目录到Python路径
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

# 导入二分查找类
from Code01_BinarySearch import BinarySearch

def test_binary_search():
    """测试二分查找算法"""
    print("测试二分查找算法...")
    
    # 测试基础二分查找
    nums1 = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    print("基础二分查找测试：")
    print(f"在数组 [1,2,3,4,5,6,7,8,9] 中查找 5: {BinarySearch.binary_search(nums1, 5)}")
    print(f"在数组 [1,2,3,4,5,6,7,8,9] 中查找 10: {BinarySearch.binary_search(nums1, 10)}")
    
    # 测试查找第一个等于目标值的元素
    nums2 = [1, 2, 2, 2, 3, 4, 5]
    print("\n查找第一个等于目标值的元素测试：")
    print(f"在数组 [1,2,2,2,3,4,5] 中查找第一个 2: {BinarySearch.find_first(nums2, 2)}")
    
    # 测试查找最后一个等于目标值的元素
    print("查找最后一个等于目标值的元素测试：")
    print(f"在数组 [1,2,2,2,3,4,5] 中查找最后一个 2: {BinarySearch.find_last(nums2, 2)}")
    
    # 测试查找第一个大于等于目标值的元素
    print("\n查找第一个大于等于目标值的元素测试：")
    print(f"在数组 [1,2,3,4,5] 中查找第一个 >= 3 的元素: {BinarySearch.find_first_greater_or_equal(nums1, 3)}")
    print(f"在数组 [1,2,3,4,5] 中查找第一个 >= 6 的元素: {BinarySearch.find_first_greater_or_equal(nums1, 6)}")
    
    # 测试查找最后一个小于等于目标值的元素
    print("\n查找最后一个小于等于目标值的元素测试：")
    print(f"在数组 [1,2,3,4,5] 中查找最后一个 <= 3 的元素: {BinarySearch.find_last_less_or_equal(nums1, 3)}")
    print(f"在数组 [1,2,3,4,5] 中查找最后一个 <= 0 的元素: {BinarySearch.find_last_less_or_equal(nums1, 0)}")

if __name__ == "__main__":
    test_binary_search()