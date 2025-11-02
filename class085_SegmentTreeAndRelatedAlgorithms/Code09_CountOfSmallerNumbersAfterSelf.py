# 315. 计算右侧小于当前元素的个数 - 动态开点线段树实现
# 题目来源：LeetCode 315 https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
# 
# 题目描述：
# 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质： counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
# 
# 解题思路：
# 使用动态开点线段树来解决这个问题
# 1. 从右向左遍历数组，这样可以确保每次处理的元素右侧元素都已经处理过
# 2. 使用线段树维护值域信息，记录每个值出现的次数
# 3. 对于当前元素，查询值域中小于它的元素个数，即为右侧小于当前元素的个数
# 4. 将当前元素加入线段树，供后续元素查询使用
# 
# 时间复杂度分析：
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
        查询小于等于某个值的元素个数
        :param val: 查询的值
        :return: 小于等于val的元素个数
        时间复杂度: O(log n)
        """
        return self.queryHelper(self.root, val)
    
    def queryHelper(self, node, val):
        """
        查询小于等于某个值的元素个数的辅助函数
        :param node: 当前节点
        :param val: 查询的值
        :return: 小于等于val的元素个数
        """
        # 节点为空或查询值小于节点维护的最小值，返回0
        if node is None or val < node.start:
            return 0
        
        # 查询值大于等于节点维护的最大值，返回该节点的计数
        if val >= node.end:
            return node.count
        
        mid = node.start + (node.end - node.start) // 2
        # 根据值的大小决定查询左子树还是右子树
        if val <= mid:
            return self.queryHelper(node.left, val)
        else:
            # 查询值在右半部分，需要加上左半部分的计数
            left_count = node.left.count if node.left else 0
            return left_count + self.queryHelper(node.right, val)

def countSmaller(nums):
    """
    主函数：计算右侧小于当前元素的个数
    :param nums: 输入数组
    :return: 结果数组，counts[i]表示nums[i]右侧小于nums[i]的元素数量
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    """
    if not nums:
        return []
    
    # 离散化处理，获取值域范围
    sorted_nums = sorted(nums)
    
    # 构建线段树，值域为数组中的最小值到最大值
    tree = SegmentTree(sorted_nums[0], sorted_nums[-1])
    
    result = []
    # 从右向左遍历数组
    for i in range(len(nums) - 1, -1, -1):
        # 查询小于当前元素的个数（即右侧小于当前元素的个数）
        count = tree.query(nums[i] - 1)
        # 将结果插入到列表开头，保持与原数组相同的顺序
        result.insert(0, count)
        # 将当前元素加入线段树，供后续元素查询使用
        tree.update(nums[i])
    
    return result

# 测试方法
if __name__ == "__main__":
    # 测试用例1: nums = [5,2,6,1]
    # 输出: [2,1,1,0]
    # 解释：5右侧有2个元素(2,1)小于5，2右侧有1个元素(1)小于2，
    #       6右侧有1个元素(1)小于6，1右侧有0个元素小于1
    nums1 = [5, 2, 6, 1]
    print(countSmaller(nums1))  # 应该输出 [2, 1, 1, 0]
    
    # 测试用例2: nums = [-1]
    # 输出: [0]
    # 解释：-1右侧没有元素
    nums2 = [-1]
    print(countSmaller(nums2))  # 应该输出 [0]
    
    # 测试用例3: nums = [-1,-1]
    # 输出: [0,0]
    # 解释：第一个-1右侧有1个-1，但不大于它，所以是0；
    #       第二个-1右侧没有元素，所以是0
    nums3 = [-1, -1]
    print(countSmaller(nums3))  # 应该输出 [0, 0]