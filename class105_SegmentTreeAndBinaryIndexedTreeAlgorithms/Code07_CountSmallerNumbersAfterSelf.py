# LeetCode 315. 计算右侧小于当前元素的个数
# 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
# 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
# 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

from typing import List

class FenwickTree:
    """
    树状数组（Fenwick Tree）实现
    """
    def __init__(self, size: int):
        self.n = size
        self.tree = [0] * (size + 1)
    
    def lowbit(self, x: int) -> int:
        """
        获取x的二进制表示中最右边的1所代表的值
        """
        return x & (-x)
    
    def add(self, i: int, delta: int) -> None:
        """
        在位置i上增加值delta
        """
        while i <= self.n:
            self.tree[i] += delta
            i += self.lowbit(i)
    
    def query(self, i: int) -> int:
        """
        查询[1, i]的前缀和
        """
        sum_val = 0
        while i > 0:
            sum_val += self.tree[i]
            i -= self.lowbit(i)
        return sum_val


def countSmaller(nums: List[int]) -> List[int]:
    """
    使用树状数组解决计算右侧小于当前元素的个数问题
    
    解题思路:
    1. 由于元素值范围可能很大，需要进行离散化处理
    2. 从右向左遍历数组，对于每个元素，在树状数组中查询比它小的元素个数
    3. 然后将当前元素加入树状数组
    4. 这样可以保证查询的都是右侧已经遍历过的元素
    
    时间复杂度分析:
    - 离散化: O(n log n)
    - 遍历数组并查询更新: O(n log n)
    - 总时间复杂度: O(n log n)
    
    空间复杂度分析:
    - 树状数组: O(n)
    - 离散化数组: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 离散化处理大数值范围
    2. 边界条件处理
    3. 异常输入检查
    4. 详细注释和变量命名
    """
    n = len(nums)
    result = [0] * n
    
    # 离散化处理
    # 1. 获取所有不重复的元素并排序
    sorted_nums = sorted(set(nums))
    
    # 2. 建立数值到离散化索引的映射
    index_map = {num: i + 1 for i, num in enumerate(sorted_nums)}
    
    # 3. 创建树状数组，大小为离散化后的元素个数
    fenwick_tree = FenwickTree(len(sorted_nums))
    
    # 4. 从右向左遍历数组
    for i in range(n - 1, -1, -1):
        # 获取当前元素的离散化索引
        index = index_map[nums[i]]
        
        # 查询比当前元素小的元素个数
        # 即查询[1, index-1]范围内已存在的元素个数
        result[i] = fenwick_tree.query(index - 1)
        
        # 将当前元素加入树状数组
        fenwick_tree.add(index, 1)
    
    return result


# 测试函数
def test():
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    result1 = countSmaller(nums1)
    print(f"Input: [5, 2, 6, 1]")
    print(f"Output: {result1}")  # 期望输出: [2, 1, 1, 0]
    
    # 测试用例2
    nums2 = [-1]
    result2 = countSmaller(nums2)
    print(f"Input: [-1]")
    print(f"Output: {result2}")  # 期望输出: [0]
    
    # 测试用例3
    nums3 = [-1, -1]
    result3 = countSmaller(nums3)
    print(f"Input: [-1, -1]")
    print(f"Output: {result3}")  # 期望输出: [0, 0]
    
    # 测试用例4
    nums4 = [26, 78, 27, 100, 33, 67, 90, 23, 66, 5, 38, 7, 35, 23, 52, 22, 83, 51, 98, 69, 81, 32, 78, 28, 94, 13, 2, 97, 3, 76, 99, 51, 9, 21, 84, 66, 65, 36, 100, 41]
    result4 = countSmaller(nums4)
    print(f"Large input test case result size: {len(result4)}")


if __name__ == "__main__":
    test()