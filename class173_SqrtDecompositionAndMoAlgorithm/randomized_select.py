import random

"""
随机化选择算法（Randomized Quick Select）
算法思想：基于快速排序的思想，随机选择pivot，将数组分区，直到找到第k小的元素
时间复杂度：期望 O(n)，最坏 O(n²)
空间复杂度：O(log n) - 递归调用栈

相关题目：
1. LeetCode 215. 数组中的第K个最大元素 - https://leetcode-cn.com/problems/kth-largest-element-in-an-array/
2. LintCode 5. 第k大元素 - https://www.lintcode.com/problem/5/
3. CodeChef - KTHMAX - https://www.codechef.com/problems/KTHMAX
4. HackerRank - Kth Largest Element - https://www.hackerrank.com/challenges/find-the-running-median/problem
"""

def find_kth_smallest(arr, k):
    """
    查找数组中第k小的元素（k从1开始计数）
    
    Args:
        arr: 输入数组
        k: 第k小
    
    Returns:
        第k小的元素值
    
    Raises:
        ValueError: 当数组为空或k超出范围时
    """
    if not arr:
        raise ValueError("数组不能为空")
    if k < 1 or k > len(arr):
        raise ValueError(f"k的取值范围应为[1, {len(arr)}]")
    
    return randomized_select(arr, 0, len(arr) - 1, k - 1)

def randomized_select(arr, left, right, index):
    """
    递归实现随机化选择
    
    Args:
        arr: 数组
        left: 左边界
        right: 右边界
        index: 目标索引（第index小，从0开始）
    
    Returns:
        目标元素
    """
    if left == right:
        return arr[left]
    
    # 随机选择pivot并分区
    pivot_index = randomized_partition(arr, left, right)
    
    if index == pivot_index:
        # 找到目标位置
        return arr[index]
    elif index < pivot_index:
        # 在左半部分查找
        return randomized_select(arr, left, pivot_index - 1, index)
    else:
        # 在右半部分查找
        return randomized_select(arr, pivot_index + 1, right, index)

def randomized_partition(arr, left, right):
    """
    随机化分区函数
    
    Args:
        arr: 数组
        left: 左边界
        right: 右边界
    
    Returns:
        pivot的最终位置
    """
    # 随机选择pivot位置
    random_index = random.randint(left, right)
    # 将pivot交换到末尾
    arr[random_index], arr[right] = arr[right], arr[random_index]
    
    return partition(arr, left, right)

def partition(arr, left, right):
    """
    分区函数
    
    Args:
        arr: 数组
        left: 左边界
        right: 右边界
    
    Returns:
        pivot的最终位置
    """
    pivot = arr[right]
    i = left - 1
    
    for j in range(left, right):
        if arr[j] <= pivot:
            i += 1
            arr[i], arr[j] = arr[j], arr[i]
    
    arr[i + 1], arr[right] = arr[right], arr[i + 1]
    return i + 1

# 测试函数
def test_find_kth_smallest():
    arr = [3, 2, 1, 5, 6, 4]
    k = 2
    
    result = find_kth_smallest(arr, k)
    print(f"数组中第{k}小的元素是：{result}")
    
    # 验证结果
    print(f"原数组：{arr}")

if __name__ == "__main__":
    test_find_kth_smallest()