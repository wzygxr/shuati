# 493. 翻转对 - 动态开点线段树实现
# 题目来源：LeetCode 493 https://leetcode.cn/problems/reverse-pairs/
# 
# 题目描述：
# 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
# 你需要返回给定数组中的重要翻转对的数量。
# 
# 解题思路：
# 使用动态开点线段树来解决翻转对问题
# 1. 从右向左遍历数组，这样可以确保每次处理的元素右侧元素都已经处理过
# 2. 使用线段树维护值域信息，记录每个值出现的次数
# 3. 对于当前元素nums[i]，查询值域中大于2*nums[i]的元素个数，即为以i为第一个元素的翻转对数量
# 4. 将当前元素加入线段树，供后续元素查询使用
# 
# 时间复杂度分析：
# - 收集所有值：O(n)
# - 离散化：O(n log n)
# - 构建线段树：O(1)
# - 处理每个元素：O(log n)
# - 总时间复杂度：O(n log n)
# 空间复杂度：O(n)

class SegmentTreeNode:
    def __init__(self, start, end):
        """
        线段树节点
        :param start: 节点维护的值域范围起始值
        :param end: 节点维护的值域范围结束值
        """
        self.start = start           # 节点维护的值域范围
        self.end = end               # 节点维护的值域范围
        self.count = 0               # 该值域范围内元素的个数
        self.left = None             # 左子节点
        self.right = None            # 右子节点

class SegmentTree:
    def __init__(self, start, end):
        """
        线段树实现
        :param start: 线段树维护的值域范围起始值
        :param end: 线段树维护的值域范围结束值
        """
        self.root = SegmentTreeNode(start, end)  # 线段树根节点
    
    def update(self, val):
        """
        更新线段树，将值val的计数加1
        :param val: 要更新的值
        时间复杂度: O(log n)
        """
        self.updateHelper(self.root, val)
    
    def updateHelper(self, node, val):
        """
        更新线段树的辅助函数
        :param node: 当前节点
        :param val: 要更新的值
        """
        # 到达叶子节点，更新计数
        if node.start == node.end:
            node.count += 1
            return
        
        mid = node.start + (node.end - node.start) // 2
        # 根据值的大小决定更新左子树还是右子树
        if val <= mid:
            # 如果左子节点不存在，则创建
            if node.left is None:
                node.left = SegmentTreeNode(node.start, mid)
            self.updateHelper(node.left, val)
        else:
            # 如果右子节点不存在，则创建
            if node.right is None:
                node.right = SegmentTreeNode(mid + 1, node.end)
            self.updateHelper(node.right, val)
        
        # 更新当前节点的计数为左右子节点计数之和
        node.count = (node.left.count if node.left else 0) + \
                     (node.right.count if node.right else 0)
    
    def query(self, val):
        """
        查询大于等于某个值的元素个数
        :param val: 查询的值
        :return: 大于等于val的元素个数
        时间复杂度: O(log n)
        """
        return self.queryHelper(self.root, val)
    
    def queryHelper(self, node, val):
        """
        查询大于等于某个值的元素个数的辅助函数
        :param node: 当前节点
        :param val: 查询的值
        :return: 大于等于val的元素个数
        """
        # 节点为空或查询值大于节点维护的最大值，返回0
        if node is None or val > node.end:
            return 0
        
        # 查询值小于等于节点维护的最小值，返回该节点的计数
        if val <= node.start:
            return node.count
        
        mid = node.start + (node.end - node.start) // 2
        # 根据值的大小决定查询左子树还是右子树
        if val <= mid:
            # 查询值在左半部分，需要加上右半部分的计数
            right_count = node.right.count if node.right else 0
            return self.queryHelper(node.left, val) + right_count
        else:
            return self.queryHelper(node.right, val)

def reversePairs(nums):
    """
    主函数：计算翻转对的数量
    :param nums: 输入数组
    :return: 翻转对的数量
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    """
    if not nums:
        return 0
    
    # 收集所有可能的值用于离散化（包括nums[i]和2*nums[i]）
    all_numbers = set()
    for num in nums:
        all_numbers.add(num)
        all_numbers.add(2 * num)
    
    # 离散化处理
    sorted_nums = sorted(list(all_numbers))
    
    # 构建线段树，值域为离散化后的索引范围
    tree = SegmentTree(0, len(sorted_nums) - 1)
    
    result = 0
    # 从右向左遍历数组
    for i in range(len(nums) - 1, -1, -1):
        # 查找2*nums[i]在离散化数组中的位置
        pos = sorted_nums.index(2 * nums[i])
        # 查询大于2*nums[i]的元素个数（即以i为第一个元素的翻转对数量）
        result += tree.query(pos + 1)
        # 查找nums[i]在离散化数组中的位置并更新线段树
        pos = sorted_nums.index(nums[i])
        tree.update(pos)
    
    return result

# 测试方法
if __name__ == "__main__":
    # 测试用例1: nums = [1,3,2,3,1]
    # 输出: 2
    # 解释：翻转对是(1,4)和(3,4)，即(1>2*1)和(3>2*1)
    nums1 = [1, 3, 2, 3, 1]
    print(reversePairs(nums1))  # 应该输出 2
    
    # 测试用例2: nums = [2,4,3,5,1]
    # 输出: 3
    # 解释：翻转对是(0,4)、(1,4)和(2,4)，即(2>2*1)、(4>2*1)和(3>2*1)
    nums2 = [2, 4, 3, 5, 1]
    print(reversePairs(nums2))  # 应该输出 3