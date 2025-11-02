"""
按公因数计算最大组件大小
给定一个由不同正整数组成的非空数组 nums，考虑下面的图：
有 nums.length 个节点，按从 nums[0] 到 nums[nums.length - 1] 标记；
只有当 nums[i] 和 nums[j] 共用大于 1 的公因数时，nums[i] 和 nums[j] 之间才有一条边。
返回图中最大连通组件的大小。

示例 1:
输入: nums = [4,6,15,35]
输出: 4

示例 2:
输入: nums = [20,50,9,63]
输出: 2

示例 3:
输入: nums = [2,3,6,7,4,12,21,39]
输出: 8

约束条件：
1 <= nums.length <= 2 * 10^4
1 <= nums[i] <= 10^5
nums 中所有值都不同

测试链接: https://leetcode.cn/problems/largest-component-size-by-common-factor/
相关平台: LeetCode 952
"""


class UnionFind:
    """
    并查集数据结构实现
    包含路径压缩和按秩合并优化
    """

    def __init__(self, n):
        """
        初始化并查集
        :param n: 节点数量
        """
        # parent[i]表示节点i的父节点
        self.parent = list(range(n))
        # rank[i]表示以i为根的树的高度上界
        self.rank = [1] * n

    def find(self, x):
        """
        查找节点的根节点（代表元素）
        使用路径压缩优化
        :param x: 要查找的节点
        :return: 节点x所在集合的根节点
        """
        if self.parent[x] != x:
            # 路径压缩：将路径上的所有节点直接连接到根节点
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        """
        合并两个集合
        使用按秩合并优化
        :param x: 第一个节点
        :param y: 第二个节点
        """
        root_x = self.find(x)
        root_y = self.find(y)

        # 如果已经在同一个集合中，则无需合并
        if root_x != root_y:
            # 按秩合并：将秩小的树合并到秩大的树下
            if self.rank[root_x] > self.rank[root_y]:
                self.parent[root_y] = root_x
            elif self.rank[root_x] < self.rank[root_y]:
                self.parent[root_x] = root_y
            else:
                # 秩相等时，任选一个作为根，并将其秩加1
                self.parent[root_y] = root_x
                self.rank[root_x] += 1


def largest_component_size(nums):
    """
    使用并查集解决按公因数计算最大组件大小问题

    解题思路：
    1. 对于每个数字，将其与其所有质因数连接（使用并查集）
    2. 这样，具有公共因数的数字就会在同一个连通组件中
    3. 统计每个连通组件的大小，返回最大的

    时间复杂度：O(N * √M * α(M))，其中N是数组长度，M是数组中的最大值，α是阿克曼函数的反函数
    空间复杂度：O(M + N)
    是否为最优解：是

    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 可配置性：可以扩展支持其他因数规则
    3. 线程安全：当前实现不是线程安全的

    与机器学习等领域的联系：
    1. 图论：连通组件分析是图神经网络中的基础操作
    2. 社交网络分析：识别具有共同兴趣的用户群体

    语言特性差异:
    Java: 对象引用和垃圾回收，数组初始化
    C++: 指针操作和手动内存管理，vector容器
    Python: 动态类型和自动内存管理，list初始化

    极端输入场景:
    1. 空数组
    2. 单个元素数组
    3. 所有元素互质的数组
    4. 大量元素的数组

    性能优化:
    1. 路径压缩优化find操作
    2. 按秩合并优化union操作
    3. 质因数分解优化（只需分解到√n）

    :param nums: 输入数组
    :return: 最大连通组件的大小
    """
    # 边界条件检查
    if not nums:
        return 0

    # 找到数组中的最大值
    max_num = max(nums)

    # 创建并查集，大小为max_num+1
    union_find = UnionFind(max_num + 1)

    # 对于每个数字，将其与其所有质因数连接
    for num in nums:
        # 分解质因数
        i = 2
        while i * i <= num:
            if num % i == 0:
                union_find.union(num, i)
                union_find.union(num, num // i)
            i += 1

    # 统计每个连通组件的大小
    component_size = {}
    max_size = 0

    for num in nums:
        root = union_find.find(num)
        component_size[root] = component_size.get(root, 0) + 1
        max_size = max(max_size, component_size[root])

    return max_size


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    nums1 = [4, 6, 15, 35]
    print("测试用例1结果:", largest_component_size(nums1))  # 预期输出: 4

    # 测试用例2
    nums2 = [20, 50, 9, 63]
    print("测试用例2结果:", largest_component_size(nums2))  # 预期输出: 2

    # 测试用例3
    nums3 = [2, 3, 6, 7, 4, 12, 21, 39]
    print("测试用例3结果:", largest_component_size(nums3))  # 预期输出: 8

    # 测试用例4：单个元素
    nums4 = [10]
    print("测试用例4结果:", largest_component_size(nums4))  # 预期输出: 1

    # 测试用例5：互质数字
    nums5 = [2, 3, 5, 7, 11]
    print("测试用例5结果:", largest_component_size(nums5))  # 预期输出: 1