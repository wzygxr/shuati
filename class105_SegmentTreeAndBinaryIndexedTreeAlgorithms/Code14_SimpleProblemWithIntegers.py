# POJ 3468 A Simple Problem with Integers
# 题目描述：给定一个长度为N的整数序列，执行以下操作：
# 1. C a b c: 将区间 [a,b] 中的每个数都加上c
# 2. Q a b: 查询区间 [a,b] 中所有数的和
# 题目链接：http://poj.org/problem?id=3468
# 解题思路：使用线段树 + 懒惰标记实现区间加法和区间求和查询

class Code14_SimpleProblemWithIntegers:
    """
    线段树实现区间加法和区间求和查询
    
    时间复杂度：
    - 构建线段树：O(n)
    - 区间更新：O(log n)
    - 区间查询：O(log n)
    空间复杂度：O(n) - 线段树数组大小为4n
    """
    
    def __init__(self, nums):
        """
        初始化线段树
        :param nums: 输入数组
        """
        self.n = len(nums)
        self.arr = nums.copy()
        # 线段树数组大小为4n，保证足够空间
        # 使用两个数组分别存储区间和和懒惰标记
        self.tree_sum = [0] * (4 * self.n)
        self.tree_add = [0] * (4 * self.n)
        # 构建线段树
        self._build(0, 0, self.n - 1)
    
    def _build(self, node, start, end):
        """
        构建线段树
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        """
        if start == end:
            # 叶子节点，直接赋值
            self.tree_sum[node] = self.arr[start]
            self.tree_add[node] = 0
            return
        
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        # 递归构建左右子树
        self._build(left_node, start, mid)
        self._build(right_node, mid + 1, end)
        
        # 合并左右子树信息
        self.tree_sum[node] = self.tree_sum[left_node] + self.tree_sum[right_node]
        # 非叶子节点初始懒惰标记为0
        self.tree_add[node] = 0
    
    def _push_down(self, node, start, end):
        """
        下传懒惰标记
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        """
        if self.tree_add[node] != 0:
            # 只有当懒惰标记不为0时需要下传
            left_node = 2 * node + 1
            right_node = 2 * node + 2
            mid = start + (end - start) // 2
            
            # 更新左子节点的区间和和懒惰标记
            self.tree_sum[left_node] += self.tree_add[node] * (mid - start + 1)
            self.tree_add[left_node] += self.tree_add[node]
            
            # 更新右子节点的区间和和懒惰标记
            self.tree_sum[right_node] += self.tree_add[node] * (end - mid)
            self.tree_add[right_node] += self.tree_add[node]
            
            # 清除当前节点的懒惰标记
            self.tree_add[node] = 0
    
    def _update_range(self, node, start, end, l, r, val):
        """
        区间更新（加法）
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        :param l: 需要更新的区间左边界
        :param r: 需要更新的区间右边界
        :param val: 要增加的值
        """
        # 当前区间与目标区间无交集
        if start > r or end < l:
            return
        
        # 当前区间完全包含在目标区间内
        if start >= l and end <= r:
            # 更新区间和
            self.tree_sum[node] += val * (end - start + 1)
            # 更新懒惰标记
            self.tree_add[node] += val
            return
        
        # 下传懒惰标记到子节点
        self._push_down(node, start, end)
        
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        # 递归更新左右子树
        self._update_range(left_node, start, mid, l, r, val)
        self._update_range(right_node, mid + 1, end, l, r, val)
        
        # 更新当前节点的区间和
        self.tree_sum[node] = self.tree_sum[left_node] + self.tree_sum[right_node]
    
    def _query_range(self, node, start, end, l, r):
        """
        区间查询
        :param node: 当前节点索引
        :param start: 当前区间左边界
        :param end: 当前区间右边界
        :param l: 查询的区间左边界
        :param r: 查询的区间右边界
        :return: 查询区间的和
        """
        # 当前区间与查询区间无交集
        if start > r or end < l:
            return 0
        
        # 当前区间完全包含在查询区间内
        if start >= l and end <= r:
            return self.tree_sum[node]
        
        # 下传懒惰标记到子节点
        self._push_down(node, start, end)
        
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        # 递归查询左右子树
        left_sum = self._query_range(left_node, start, mid, l, r)
        right_sum = self._query_range(right_node, mid + 1, end, l, r)
        
        # 返回左右子树查询结果的和
        return left_sum + right_sum
    
    def update_range(self, l, r, val):
        """
        公共接口：区间更新
        :param l: 区间左边界（注意：这里是1-based索引）
        :param r: 区间右边界（注意：这里是1-based索引）
        :param val: 要增加的值
        """
        # 转换为0-based索引
        self._update_range(0, 0, self.n - 1, l - 1, r - 1, val)
    
    def query_range(self, l, r):
        """
        公共接口：区间查询
        :param l: 区间左边界（注意：这里是1-based索引）
        :param r: 区间右边界（注意：这里是1-based索引）
        :return: 查询区间的和
        """
        # 转换为0-based索引
        return self._query_range(0, 0, self.n - 1, l - 1, r - 1)

# 主方法，用于处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    # 读取数组长度和查询次数
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    # 读取数组元素
    arr = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    # 创建线段树
    solution = Code14_SimpleProblemWithIntegers(arr)
    
    # 处理每个查询
    results = []
    while q > 0:
        q -= 1
        op = input[ptr]
        ptr += 1
        a = int(input[ptr])
        ptr += 1
        b = int(input[ptr])
        ptr += 1
        
        if op == 'Q':
            # 查询操作
            sum_val = solution.query_range(a, b)
            results.append(str(sum_val))
        elif op == 'C':
            # 更新操作
            c = int(input[ptr])
            ptr += 1
            solution.update_range(a, b, c)
    
    # 输出结果
    print('\n'.join(results))

# 测试方法
def test():
    # 测试用例1：基本操作测试
    nums1 = [1, 2, 3, 4, 5]
    solution1 = Code14_SimpleProblemWithIntegers(nums1)
    print("测试用例1:")
    print("初始数组: [1, 2, 3, 4, 5]")
    print(f"查询区间[1, 5]的和: {solution1.query_range(1, 5)}")  # 应为15
    solution1.update_range(2, 4, 2)
    print("更新区间[2, 4]每个元素加2后，数组变为: [1, 4, 5, 6, 5]")
    print(f"查询区间[1, 5]的和: {solution1.query_range(1, 5)}")  # 应为21
    print(f"查询区间[2, 4]的和: {solution1.query_range(2, 4)}")  # 应为15
    
    # 测试用例2：边界情况测试
    nums2 = [10]
    solution2 = Code14_SimpleProblemWithIntegers(nums2)
    print("\n测试用例2:")
    print("初始数组: [10]")
    print(f"查询区间[1, 1]的和: {solution2.query_range(1, 1)}")  # 应为10
    solution2.update_range(1, 1, -5)
    print("更新区间[1, 1]每个元素加-5后，数组变为: [5]")
    print(f"查询区间[1, 1]的和: {solution2.query_range(1, 1)}")  # 应为5
    
    # 测试用例3：多次更新和查询测试
    nums3 = [0, 0, 0, 0, 0]
    solution3 = Code14_SimpleProblemWithIntegers(nums3)
    print("\n测试用例3:")
    print("初始数组: [0, 0, 0, 0, 0]")
    solution3.update_range(1, 5, 1)  # 所有元素加1
    solution3.update_range(2, 4, 2)  # 中间三个元素再加2
    solution3.update_range(3, 3, 3)  # 中间元素再加3
    print("多次更新后，数组变为: [1, 3, 6, 3, 1]")
    print(f"查询区间[1, 5]的和: {solution3.query_range(1, 5)}")  # 应为14
    print(f"查询区间[1, 3]的和: {solution3.query_range(1, 3)}")  # 应为10
    print(f"查询区间[3, 5]的和: {solution3.query_range(3, 5)}")  # 应为10

# 如果直接运行此脚本，则执行测试
if __name__ == "__main__":
    # 运行测试
    test()
    
    # 如果需要读取标准输入运行，可以取消下面的注释
    # main()