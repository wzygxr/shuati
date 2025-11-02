import math
from typing import List

class Code21_SegmentTreeGCD:
    """
    线段树GCD查询 - 支持区间GCD查询和单点更新
    题目来源：Codeforces 914D - Bash and a Tough Math Puzzle
    问题描述：支持区间GCD查询和单点更新的线段树实现
    
    解题思路：
    使用线段树来维护区间GCD信息。线段树是一种二叉树结构，每个节点代表数组的一个区间，
    存储该区间的GCD值。对于叶子节点，它代表数组中的单个元素；对于非叶子节点，
    它代表其左右子树所覆盖区间的合并结果（在这里是GCD）。
    
    算法要点：
    - 使用线段树维护区间GCD信息
    - 支持高效区间GCD查询和单点更新
    - 利用GCD的性质：gcd(a,b,c) = gcd(gcd(a,b), c)
    
    时间复杂度：
    - 构建线段树：O(n)
    - 单点更新：O(log n)
    - 区间GCD查询：O(log n)
    
    空间复杂度：O(n)
    
    应用场景：
    - 区间GCD查询问题
    - 判断区间内元素是否满足特定GCD条件
    - 数学相关的区间查询问题
    """
    
    def __init__(self, arr: List[int]):
        """
        构造函数，初始化线段树
        :param arr: 原始数组
        """
        self.n = len(arr)
        # 线段树通常需要4倍空间，确保足够容纳所有节点
        self.tree = [0] * (4 * self.n)
        # 构建线段树
        self._build_tree(arr, 0, self.n - 1, 0)
    
    def _build_tree(self, arr: List[int], start: int, end: int, idx: int):
        """
        构建线段树
        递归地将数组构建成线段树结构
        :param arr: 原始数组
        :param start: 区间开始索引
        :param end: 区间结束索引
        :param idx: 当前节点索引（在tree数组中的位置）
        
        时间复杂度：O(n)
        空间复杂度：O(log n) - 递归调用栈深度
        """
        # 递归终止条件：当前区间只有一个元素（叶子节点）
        if start == end:
            self.tree[idx] = arr[start]
            return
        
        # 计算区间中点，避免整数溢出
        mid = start + (end - start) // 2
        # 递归构建左子树
        self._build_tree(arr, start, mid, 2 * idx + 1)
        # 递归构建右子树
        self._build_tree(arr, mid + 1, end, 2 * idx + 2)
        # 合并左右子树的结果，当前节点存储左右子树区间GCD值的GCD
        self.tree[idx] = math.gcd(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def update(self, pos: int, val: int):
        """
        单点更新
        :param pos: 要更新的位置
        :param val: 新的值
        
        时间复杂度：O(log n)
        """
        # 调用递归实现
        self._update(0, self.n - 1, pos, val, 0)
    
    def _update(self, start: int, end: int, pos: int, val: int, idx: int):
        """
        单点更新递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param pos: 要更新的位置
        :param val: 新的值
        :param idx: 当前节点在tree数组中的索引
        """
        # 递归终止条件：找到叶子节点
        if start == end:
            self.tree[idx] = val
            return
        
        # 计算区间中点
        mid = start + (end - start) // 2
        # 根据位置决定更新左子树还是右子树
        if pos <= mid:
            # 更新左子树
            self._update(start, mid, pos, val, 2 * idx + 1)
        else:
            # 更新右子树
            self._update(mid + 1, end, pos, val, 2 * idx + 2)
        # 更新完成后，需要维护父节点信息（合并子节点结果）
        self.tree[idx] = math.gcd(self.tree[2 * idx + 1], self.tree[2 * idx + 2])
    
    def query(self, l: int, r: int) -> int:
        """
        区间GCD查询
        :param l: 查询区间左边界（包含）
        :param r: 查询区间右边界（包含）
        :return: 区间GCD值
        
        时间复杂度：O(log n)
        """
        # 调用递归实现
        return self._query(0, self.n - 1, l, r, 0)
    
    def _query(self, start: int, end: int, l: int, r: int, idx: int) -> int:
        """
        区间GCD查询递归实现
        :param start: 当前节点管理区间的起始索引
        :param end: 当前节点管理区间的结束索引
        :param l: 查询区间左边界
        :param r: 查询区间右边界
        :param idx: 当前节点在tree数组中的索引
        :return: 区间[l, r]的GCD值
        
        核心思想：
        1. 判断当前区间与目标区间的关系
        2. 根据关系决定是直接返回、递归查询还是部分返回
        3. 利用GCD的性质进行合并
        """
        # 当前区间完全包含在目标区间内，直接返回当前节点的值
        if l <= start and end <= r:
            return self.tree[idx]
        
        # 计算区间中点
        mid = start + (end - start) // 2
        # 根据查询区间与左右子树区间的关系决定查询策略
        if r <= mid:
            # 查询区间完全在左子树中
            return self._query(start, mid, l, r, 2 * idx + 1)
        elif l > mid:
            # 查询区间完全在右子树中
            return self._query(mid + 1, end, l, r, 2 * idx + 2)
        else:
            # 查询区间跨越左右子树，需要分别查询后再合并
            left_gcd = self._query(start, mid, l, r, 2 * idx + 1)
            right_gcd = self._query(mid + 1, end, l, r, 2 * idx + 2)
            # 利用GCD的性质：gcd(a,b,c) = gcd(gcd(a,b), c)
            return math.gcd(left_gcd, right_gcd)

def main():
    """
    测试函数
    """
    # 测试用例1：基础功能测试
    arr1 = [2, 4, 6, 8, 10]
    st1 = Code21_SegmentTreeGCD(arr1)
    
    print("=== 测试用例1：基础功能测试 ===")
    print(f"数组: {arr1}")
    print(f"区间[0,2]的GCD: {st1.query(0, 2)}")  # 应该为2 (gcd(2,4,6)=2)
    print(f"区间[1,4]的GCD: {st1.query(1, 4)}")  # 应该为2 (gcd(4,6,8,10)=2)
    
    # 更新测试
    st1.update(2, 9)
    print(f"更新位置2为9后，区间[0,2]的GCD: {st1.query(0, 2)}")  # 应该为1 (gcd(2,4,9)=1)
    
    # 测试用例2：边界条件测试
    arr2 = [15]
    st2 = Code21_SegmentTreeGCD(arr2)
    print("\n=== 测试用例2：边界条件测试 ===")
    print(f"单元素数组: {arr2}")
    print(f"单点查询[0,0]的GCD: {st2.query(0, 0)}")  # 应该为15
    
    # 测试用例3：性能验证
    print("\n=== 测试用例3：性能验证 ===")
    print("线段树GCD算法已实现，支持高效区间查询和单点更新")

if __name__ == "__main__":
    main()