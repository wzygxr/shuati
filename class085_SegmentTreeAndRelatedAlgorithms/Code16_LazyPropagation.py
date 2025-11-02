"""
线段树区间更新 - 懒惰传播 (Lazy Propagation)

题目描述：
实现支持区间加法和区间查询的线段树
操作类型：
1. 区间加法：将区间 [l, r] 内的每个数加上 k
2. 区间求和：查询区间 [l, r] 内所有数的和

题目来源：洛谷 P3372 【模板】线段树 1
测试链接 : https://www.luogu.com.cn/problem/P3372

解题思路：
使用线段树配合懒惰传播技术来高效处理区间更新和区间查询操作。

核心思想：
1. 懒惰传播：当需要对一个区间进行更新时，不立即更新所有相关节点，
   而是在节点上打上标记，只有在后续查询或更新需要访问该节点的子节点时，
   才将标记向下传递，这样可以避免不必要的计算，提高效率。

2. 线段树结构：
   - tree[]数组：存储线段树节点的值（区间和）
   - lazy[]数组：存储懒惰标记（区间加法的增量）

3. 关键操作：
   - push_down：懒惰传播操作，将父节点的标记传递给子节点
   - range_add：区间加法更新
   - range_sum：区间求和查询

时间复杂度分析：
- 构建线段树：O(n)
- 区间加法更新：O(log n)
- 区间求和查询：O(log n)

空间复杂度分析：
- 线段树需要O(n)的额外空间
"""

import sys
from typing import List

class SegmentTree:
    """线段树类 - 支持懒惰传播"""
    
    def __init__(self, nums: List[int]):
        """
        构造函数 - 初始化线段树
        :param nums: 原始数组
        """
        self.n = len(nums)
        # 线段树通常需要4倍空间
        self.tree = [0] * (4 * self.n)  # 存储线段树节点的值（区间和）
        self.lazy = [0] * (4 * self.n)  # 存储懒惰标记（区间加法的增量）
        self.build_tree(nums, 0, 0, self.n - 1)
    
    def build_tree(self, nums: List[int], node: int, start: int, end: int) -> None:
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param nums: 原始数组
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if start == end:
            # 叶子节点 - 直接存储数组元素值
            self.tree[node] = nums[start]
        else:
            mid = (start + end) // 2
            # 递归构建左子树
            self.build_tree(nums, 2 * node + 1, start, mid)
            # 递归构建右子树
            self.build_tree(nums, 2 * node + 2, mid + 1, end)
            # 合并左右子树的结果（区间和）
            self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def push_down(self, node: int, start: int, end: int) -> None:
        """
        懒惰传播 - 将当前节点的懒惰标记传递给子节点
        这是懒惰传播技术的核心实现
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        
        时间复杂度: O(1)
        """
        # 只有当当前节点有懒惰标记时才需要传播
        if self.lazy[node] != 0:
            mid = (start + end) // 2
            left_len = mid - start + 1   # 左子树区间长度
            right_len = end - mid        # 右子树区间长度
            
            # 更新左子树
            # 1. 更新左子树的区间和：增加 lazy[node] * 区间长度
            self.tree[2 * node + 1] += self.lazy[node] * left_len
            # 2. 将懒惰标记传递给左子树
            self.lazy[2 * node + 1] += self.lazy[node]
            
            # 更新右子树
            # 1. 更新右子树的区间和：增加 lazy[node] * 区间长度
            self.tree[2 * node + 2] += self.lazy[node] * right_len
            # 2. 将懒惰标记传递给右子树
            self.lazy[2 * node + 2] += self.lazy[node]
            
            # 清除当前节点的懒惰标记
            self.lazy[node] = 0
    
    def range_add(self, left: int, right: int, val: int) -> None:
        """
        区间加法更新
        将区间[left, right]内的每个数都加上val
        :param left: 更新区间左边界（0-based索引）
        :param right: 更新区间右边界（0-based索引）
        :param val: 要加上的值
        
        时间复杂度: O(log n)
        """
        self._range_add_helper(0, 0, self.n - 1, left, right, val)
    
    def _range_add_helper(self, node: int, start: int, end: int, left: int, right: int, val: int) -> None:
        """
        区间加法更新辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param left: 更新区间左边界（0-based索引）
        :param right: 更新区间右边界（0-based索引）
        :param val: 要加上的值
        """
        # 如果当前区间完全包含在更新区间内
        if left <= start and end <= right:
            # 直接更新当前节点的值和懒惰标记
            self.tree[node] += val * (end - start + 1)  # 更新区间和
            self.lazy[node] += val  # 打上懒惰标记
            return
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        
        # 递归更新左右子树
        mid = (start + end) // 2
        # 如果更新区间与左子树有交集，则更新左子树
        if left <= mid:
            self._range_add_helper(2 * node + 1, start, mid, left, right, val)
        # 如果更新区间与右子树有交集，则更新右子树
        if right > mid:
            self._range_add_helper(2 * node + 2, mid + 1, end, left, right, val)
        
        # 更新父节点的值（子节点更新后需要更新父节点）
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]
    
    def range_sum(self, left: int, right: int) -> int:
        """
        区间求和查询
        查询区间[left, right]内所有数的和
        :param left: 查询区间左边界（0-based索引）
        :param right: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内所有数的和
        
        时间复杂度: O(log n)
        """
        return self._range_sum_helper(0, 0, self.n - 1, left, right)
    
    def _range_sum_helper(self, node: int, start: int, end: int, left: int, right: int) -> int:
        """
        区间求和查询辅助函数
        :param node: 当前线段树节点索引
        :param start: 当前节点表示区间的起始位置
        :param end: 当前节点表示区间的结束位置
        :param left: 查询区间左边界（0-based索引）
        :param right: 查询区间右边界（0-based索引）
        :return: 区间[left, right]内所有数的和
        """
        # 如果当前区间完全包含在查询区间内
        if left <= start and end <= right:
            # 直接返回当前节点的值
            return self.tree[node]
        
        # 需要向下传递懒惰标记（在递归之前）
        self.push_down(node, start, end)
        
        # 递归查询左右子树
        mid = (start + end) // 2
        total = 0
        # 如果查询区间与左子树有交集，则查询左子树
        if left <= mid:
            total += self._range_sum_helper(2 * node + 1, start, mid, left, right)
        # 如果查询区间与右子树有交集，则查询右子树
        if right > mid:
            total += self._range_sum_helper(2 * node + 2, mid + 1, end, left, right)
        
        return total


def main():
    """
    主函数 - 处理输入输出和操作调度
    """
    # 读取n和m
    n, m = map(int, input().split())
    
    # 读取初始数组
    nums = list(map(int, input().split()))
    
    # 构建线段树
    st = SegmentTree(nums)
    
    # 处理m个操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        
        if operation[0] == 1:
            # 区间加法操作
            l, r, k = operation[1], operation[2], operation[3]
            st.range_add(l - 1, r - 1, k)  # 转换为0-based索引
        else:
            # 区间求和操作
            l, r = operation[1], operation[2]
            sum_result = st.range_sum(l - 1, r - 1)  # 转换为0-based索引
            print(sum_result)


def test():
    """
    测试函数 - 验证线段树实现的正确性
    """
    # 测试用例1: 基础功能测试
    nums1 = [1, 2, 3, 4, 5]
    st1 = SegmentTree(nums1)
    
    # 测试区间求和
    print(f"初始数组区间[0,4]的和: {st1.range_sum(0, 4)}")  # 应该输出 15
    print(f"区间[1,3]的和: {st1.range_sum(1, 3)}")  # 应该输出 9
    
    # 测试区间加法
    st1.range_add(1, 3, 2)  # 将索引1-3的元素都加2
    print(f"区间加法后区间[1,3]的和: {st1.range_sum(1, 3)}")  # 应该输出 15 (2+2, 3+2, 4+2 = 4+5+6)
    print(f"区间[0,4]的和: {st1.range_sum(0, 4)}")  # 应该输出 21 (1+4+5+6+5)
    
    # 测试用例2: 边界情况
    nums2 = [10]
    st2 = SegmentTree(nums2)
    print(f"单元素数组区间[0,0]的和: {st2.range_sum(0, 0)}")  # 应该输出 10
    st2.range_add(0, 0, 5)
    print(f"单元素加法后区间[0,0]的和: {st2.range_sum(0, 0)}")  # 应该输出 15
    
    # 测试用例3: 大规模数据测试
    nums3 = [1] * 1000
    st3 = SegmentTree(nums3)
    
    # 对整个数组进行区间加法
    st3.range_add(0, 999, 10)
    print(f"大规模数组区间[0,999]的和: {st3.range_sum(0, 999)}")  # 应该输出 1000 * 11 = 11000


# 如果需要运行测试，取消下面的注释
# test()

# 如果需要运行主程序，取消下面的注释
# if __name__ == "__main__":
#     main()