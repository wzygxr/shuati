# Count of Smaller Numbers After Self (计算右侧小于当前元素的个数)
# 题目来源: LeetCode 315. Count of Smaller Numbers After Self
# 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self
# 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self
# 
# 题目描述:
# 给你一个整数数组 nums ，按要求返回一个新数组 counts 。
# 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
# 示例 1：
# 输入：nums = [5,2,6,1]
# 输出：[2,1,1,0]
# 解释：
# 5 的右侧有 2 个更小的元素 (2 和 1)
# 2 的右侧有 1 个更小的元素 (1)
# 6 的右侧有 1 个更小的元素 (1)
# 1 的右侧有 0 个更小的元素
# 示例 2：
# 输入：nums = [-1]
# 输出：[0]
# 示例 3：
# 输入：nums = [-1,-1]
# 输出：[0,0]
# 提示：
# 1 <= nums.length <= 10^5
# -10^4 <= nums[i] <= 10^4
#
# 解题思路:
# 1. 使用离散化+线段树的方法解决
# 2. 从右向左遍历数组，维护一个值域线段树
# 3. 对于每个元素，查询值域中小于它的元素个数
# 4. 将当前元素插入到线段树中
# 5. 利用离散化处理大范围的值域
#
# 时间复杂度: O(n log n)，其中n为数组长度
# 空间复杂度: O(n)

class SegmentTree:
    def __init__(self, size):
        self.n = size
        # 线段树需要4*n的空间
        self.tree = [0] * (4 * size)

    # 更新节点值（单点更新）
    def update(self, index, val):
        self._update_helper(0, 0, self.n - 1, index, val)

    # 更新辅助函数
    def _update_helper(self, node, start, end, index, val):
        # 找到叶子节点，更新值
        if start == end:
            self.tree[node] += val
            return

        # 在左右子树中查找需要更新的索引
        mid = (start + end) // 2
        if index <= mid:
            # 在左子树中
            self._update_helper(2 * node + 1, start, mid, index, val)
        else:
            # 在右子树中
            self._update_helper(2 * node + 2, mid + 1, end, index, val)

        # 更新当前节点的值为左右子节点值的和
        self.tree[node] = self.tree[2 * node + 1] + self.tree[2 * node + 2]

    # 查询区间和
    def query(self, left, right):
        # 处理边界情况
        if left > right:
            return 0
        return self._query_helper(0, 0, self.n - 1, left, right)

    # 查询区间和辅助函数
    def _query_helper(self, node, start, end, left, right):
        # 当前区间与查询区间无交集
        if right < start or left > end:
            return 0

        # 当前区间完全包含在查询区间内
        if left <= start and end <= right:
            return self.tree[node]

        # 当前区间与查询区间有部分交集，递归查询左右子树
        mid = (start + end) // 2
        left_sum = self._query_helper(2 * node + 1, start, mid, left, right)
        right_sum = self._query_helper(2 * node + 2, mid + 1, end, left, right)
        return left_sum + right_sum


def count_smaller(nums):
    n = len(nums)
    result = []

    # 离散化处理
    # 1. 收集所有不同的值
    sorted_nums = sorted(nums)

    # 2. 去重
    unique_size = 1
    for i in range(1, n):
        if sorted_nums[i] != sorted_nums[i - 1]:
            sorted_nums[unique_size] = sorted_nums[i]
            unique_size += 1

    # 3. 创建离散化映射
    # 线段树的大小为去重后的元素个数
    st = SegmentTree(unique_size)

    # 从右向左遍历数组
    for i in range(n - 1, -1, -1):
        # 找到当前元素在离散化数组中的位置
        pos = binary_search(sorted_nums, 0, unique_size - 1, nums[i])

        # 查询比当前元素小的元素个数（在值域上查询[0, pos-1]区间和）
        count = st.query(0, pos - 1)
        result.insert(0, count)  # 插入到结果列表的开头

        # 更新当前元素的计数（在值域上对位置pos进行+1操作）
        st.update(pos, 1)

    return result


# 二分查找元素在排序数组中的位置
def binary_search(arr, left, right, target):
    while left <= right:
        mid = left + (right - left) // 2
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return left


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    print(count_smaller(nums1))  # 输出: [2, 1, 1, 0]

    # 测试用例2
    nums2 = [-1]
    print(count_smaller(nums2))  # 输出: [0]

    # 测试用例3
    nums3 = [-1, -1]
    print(count_smaller(nums3))  # 输出: [0, 0]

# ==========================================================================================
# LeetCode 1649. Create Sorted Array through Instructions
# 题目链接：https://leetcode.com/problems/create-sorted-array-through-instructions/
# 题目描述：
# 给你一个整数数组 instructions，你需要根据 instructions 中的元素创建一个有序数组。
# 一开始数组为空。你需要依次读取 instructions 中的元素，并将它插入到有序数组中的正确位置。
# 每次插入操作的代价是以下两者的较小值：
# 1. 有多少个元素严格小于 instructions[i]（左边）
# 2. 有多少个元素严格大于 instructions[i]（右边）
# 返回插入所有元素的总最小代价。由于答案可能很大，请返回它对 10^9 + 7 取模的结果。
# ==========================================================================================

MOD = 10**9 + 7


def create_sorted_array(instructions):
    """
    计算创建有序数组的最小代价
    
    Args:
        instructions: 指令数组
        
    Returns:
        总最小代价
    """
    n = len(instructions)
    
    # 离散化处理
    sorted_inst = sorted(instructions)
    
    # 去重
    unique_size = 1
    for i in range(1, n):
        if sorted_inst[i] != sorted_inst[i - 1]:
            sorted_inst[unique_size] = sorted_inst[i]
            unique_size += 1
    
    # 创建线段树
    st = SegmentTree(unique_size)
    total_cost = 0
    
    # 处理每个指令
    for i in range(n):
        value = instructions[i]
        
        # 找到离散化后的位置
        pos = binary_search(sorted_inst, 0, unique_size - 1, value)
        
        # 计算左边比当前元素小的个数
        smaller_count = st.query(0, pos - 1)
        
        # 计算右边比当前元素大的个数（总元素数减去到当前索引的前缀和）
        larger_count = i - st.query(0, pos)
        
        # 取较小值作为当前操作的代价
        total_cost = (total_cost + min(smaller_count, larger_count)) % MOD
        
        # 更新线段树，将当前元素的计数加1
        st.update(pos, 1)
    
    return total_cost


# 测试LeetCode 1649题
def test_leetcode_1649():
    # 测试用例1
    instructions1 = [1, 5, 6, 2]
    print("LeetCode 1649 测试用例1结果:", create_sorted_array(instructions1))  # 预期输出: 1
    
    # 测试用例2
    instructions2 = [1, 2, 3, 6, 5, 4]
    print("LeetCode 1649 测试用例2结果:", create_sorted_array(instructions2))  # 预期输出: 3
    
    # 测试用例3
    instructions3 = [1, 3, 3, 3, 2, 4, 2, 1, 2]
    print("LeetCode 1649 测试用例3结果:", create_sorted_array(instructions3))  # 预期输出: 4


# 如果直接运行此文件，也测试LeetCode 1649题
if __name__ == "__main__":
    # 测试原始问题
    print("\n测试原始问题 Count of Smaller Numbers After Self:")
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    print(count_smaller(nums1))  # 输出: [2, 1, 1, 0]

    # 测试用例2
    nums2 = [-1]
    print(count_smaller(nums2))  # 输出: [0]

    # 测试用例3
    nums3 = [-1, -1]
    print(count_smaller(nums3))  # 输出: [0, 0]
    
    # 测试LeetCode 1649题
    print("\n测试LeetCode 1649题:")
    test_leetcode_1649()