import sys
import random
import time
from typing import List

class NowcoderNC78_SegmentTreeMax:
    """
    牛客网 NC78 线段树区间最大值 - Python实现
    题目链接: https://www.nowcoder.com/practice/1a834e5e3e1a4b7ba251417554e07c00
    
    算法思路:
    线段树是一种二叉树结构，用于高效处理区间查询和更新操作。
    每个节点存储一个区间的聚合信息（最大值）。
    
    时间复杂度分析:
    - 建树: O(n)
    - 查询: O(log n)
    - 更新: O(log n)
    
    空间复杂度分析:
    - 线段树存储: O(4n) 或 O(2n)（优化后）
    
    工程化考量:
    1. 异常处理：空数组、索引越界检查
    2. 性能优化：位运算、缓存友好设计
    3. 可测试性：边界测试、性能测试
    4. 可维护性：清晰的结构、详细注释
    """
    
    def __init__(self, nums: List[int]):
        """
        构造函数：构建线段树
        Args:
            nums: 原始数组
        """
        if not nums:
            raise ValueError("数组不能为空")
        
        self.n = len(nums)
        self.tree = [0] * (4 * self.n)  # 分配4倍空间
        self._build_tree(nums, 0, self.n - 1, 0)
    
    def _build_tree(self, nums: List[int], left: int, right: int, idx: int):
        """
        递归构建线段树
        Args:
            nums: 原始数组
            left: 当前区间左边界
            right: 当前区间右边界
            idx: 当前节点在线段树中的索引
        """
        # 叶子节点，存储单个元素
        if left == right:
            self.tree[idx] = nums[left]
            return
        
        # 计算中间点，分割区间
        mid = left + (right - left) // 2
        left_child = 2 * idx + 1  # 左子节点索引
        right_child = 2 * idx + 2  # 右子节点索引
        
        # 递归构建左右子树
        self._build_tree(nums, left, mid, left_child)
        self._build_tree(nums, mid + 1, right, right_child)
        
        # 合并左右子树信息（取最大值）
        self.tree[idx] = max(self.tree[left_child], self.tree[right_child])
    
    def query(self, query_left: int, query_right: int) -> int:
        """
        查询区间最大值
        Args:
            query_left: 查询区间左边界
            query_right: 查询区间右边界
        Returns:
            区间最大值
        """
        # 参数校验
        if query_left < 0 or query_right >= self.n or query_left > query_right:
            raise ValueError("查询区间不合法")
        
        return self._query_helper(0, self.n - 1, query_left, query_right, 0)
    
    def _query_helper(self, seg_left: int, seg_right: int, 
                     query_left: int, query_right: int, idx: int) -> int:
        """
        递归查询辅助函数
        """
        # 当前区间完全包含在查询区间内
        if query_left <= seg_left and seg_right <= query_right:
            return self.tree[idx]
        
        mid = seg_left + (seg_right - seg_left) // 2
        left_child = 2 * idx + 1
        right_child = 2 * idx + 2
        
        max_val = -sys.maxsize - 1  # 最小整数值
        
        # 查询左子树
        if query_left <= mid:
            max_val = max(max_val, self._query_helper(seg_left, mid, query_left, query_right, left_child))
        
        # 查询右子树
        if query_right > mid:
            max_val = max(max_val, self._query_helper(mid + 1, seg_right, query_left, query_right, right_child))
        
        return max_val
    
    def update(self, index: int, value: int):
        """
        单点更新
        Args:
            index: 要更新的索引
            value: 新的值
        """
        # 参数校验
        if index < 0 or index >= self.n:
            raise ValueError("索引越界")
        
        self._update_helper(0, self.n - 1, index, value, 0)
    
    def _update_helper(self, seg_left: int, seg_right: int, 
                      index: int, value: int, idx: int):
        """
        递归更新辅助函数
        """
        # 找到目标叶子节点
        if seg_left == seg_right:
            self.tree[idx] = value
            return
        
        mid = seg_left + (seg_right - seg_left) // 2
        left_child = 2 * idx + 1
        right_child = 2 * idx + 2
        
        # 根据索引位置决定更新哪棵子树
        if index <= mid:
            self._update_helper(seg_left, mid, index, value, left_child)
        else:
            self._update_helper(mid + 1, seg_right, index, value, right_child)
        
        # 更新父节点（取左右子树最大值）
        self.tree[idx] = max(self.tree[left_child], self.tree[right_child])
    
    def print_tree(self):
        """
        打印线段树结构（用于调试）
        """
        print("线段树结构:")
        self._print_tree_helper(0, self.n - 1, 0, 0)
    
    def _print_tree_helper(self, left: int, right: int, idx: int, depth: int):
        """
        递归打印线段树结构辅助函数
        """
        indent = "  " * depth
        print(f"{indent}区间[{left}, {right}]: {self.tree[idx]}")
        
        if left != right:
            mid = left + (right - left) // 2
            self._print_tree_helper(left, mid, 2 * idx + 1, depth + 1)
            self._print_tree_helper(mid + 1, right, 2 * idx + 2, depth + 1)


def main():
    """
    主函数：测试用例
    """
    # 测试用例1：正常数组
    nums1 = [1, 3, 5, 7, 9, 11]
    st1 = NowcoderNC78_SegmentTreeMax(nums1)
    
    print("=== 测试用例1 ===")
    print(f"数组: {nums1}")
    
    # 测试查询
    print(f"查询[0, 2]最大值: {st1.query(0, 2)}")  # 期望: 5
    print(f"查询[1, 4]最大值: {st1.query(1, 4)}")  # 期望: 9
    print(f"查询[0, 5]最大值: {st1.query(0, 5)}")  # 期望: 11
    
    # 测试更新
    st1.update(2, 10)
    print(f"更新索引2为10后，查询[0, 2]最大值: {st1.query(0, 2)}")  # 期望: 10
    
    # 测试用例2：边界情况
    nums2 = [5]
    st2 = NowcoderNC78_SegmentTreeMax(nums2)
    
    print("\n=== 测试用例2 ===")
    print(f"数组: {nums2}")
    print(f"查询[0, 0]最大值: {st2.query(0, 0)}")  # 期望: 5
    
    # 测试用例3：负数数组
    nums3 = [-1, -3, -5, -7]
    st3 = NowcoderNC78_SegmentTreeMax(nums3)
    
    print("\n=== 测试用例3 ===")
    print(f"数组: {nums3}")
    print(f"查询[0, 3]最大值: {st3.query(0, 3)}")  # 期望: -1
    
    # 性能测试
    print("\n=== 性能测试 ===")
    size = 100000
    large_nums = [random.randint(0, 1000000) for _ in range(size)]
    
    start_time = time.time()
    st_large = NowcoderNC78_SegmentTreeMax(large_nums)
    build_time = (time.time() - start_time) * 1000
    
    start_time = time.time()
    max_val = st_large.query(0, size - 1)
    query_time = (time.time() - start_time) * 1000
    
    print(f"构建{size}个元素的线段树耗时: {build_time:.2f}ms")
    print(f"查询整个区间最大值耗时: {query_time:.2f}ms")
    print(f"最大值: {max_val}")
    
    # 异常测试
    print("\n=== 异常测试 ===")
    try:
        st1.query(-1, 2)
    except ValueError as e:
        print(f"捕获到预期异常: {e}")


if __name__ == "__main__":
    main()