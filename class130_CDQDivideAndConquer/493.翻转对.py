# 493. 翻转对
# 平台: LeetCode
# 难度: 困难
# 标签: CDQ分治, 分治
# 链接: https://leetcode.cn/problems/reverse-pairs/
# 
# 题目描述:
# 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
# 你需要返回给定数组中的重要翻转对的数量。
# 
# 示例:
# 输入: [1,3,2,3,1]
# 输出: 2
# 
# 输入: [2,4,3,5,1]
# 输出: 3
# 
# 解题思路:
# 使用CDQ分治解决这个问题，将问题转化为三维偏序问题：
# 1. 第一维：索引，表示元素在原数组中的位置
# 2. 第二维：数值，表示元素的值
# 3. 第三维：时间/操作类型，用于区分不同类型的查询操作
# 
# 我们将每个元素看作两种操作：
# 1. 插入操作：在位置i插入数值nums[i]
# 2. 查询操作：查询在位置i左侧已经插入的大于2*nums[i]的元素个数
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class Operation493:
    def __init__(self, op, val, idx, id):
        self.op = op    # 操作类型，1表示插入，-1表示查询
        self.val = val  # 元素值
        self.idx = idx  # 原始索引
        self.id = id    # 操作编号
    
    def __lt__(self, other):
        if self.val != other.val:
            return self.val < other.val
        # 查询操作优先于插入操作
        return other.op < self.op

class Solution493:
    def __init__(self):
        self.bit = []  # 树状数组
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, x, v, n):
        i = x
        while i <= n:
            self.bit[i] += v
            i += self.lowbit(i)
    
    def query(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def reversePairs(self, nums):
        n = len(nums)
        if n == 0:
            return 0
        
        # 离散化处理
        sorted_nums = sorted([num for num in nums])
        unique_size = self.remove_duplicates(sorted_nums)
        
        ops = []
        result = [0] * n
        self.bit = [0] * (n + 1)  # 树状数组
        
        # 从左向右处理，构造操作序列
        for i in range(n):
            # 注意：这里要查找2*nums[i]，可能超出int范围
            target = 2 * nums[i]
            val_id = self.binary_search(sorted_nums, 0, unique_size, nums[i]) + 1
            
            # 查询操作：查找大于2*nums[i]的元素个数
            query_id = self.upper_bound(sorted_nums, 0, unique_size, target)
            ops.append(Operation493(-1, target, i, query_id))
            
            # 插入操作
            ops.append(Operation493(1, nums[i], i, val_id))
        
        # 按值排序
        ops.sort()
        
        # 执行CDQ分治
        self.cdq(ops, result, 0, len(ops) - 1, n)
        
        # 统计结果
        total = 0
        for i in range(n):
            total += result[i]
        return total
    
    # CDQ分治主函数
    def cdq(self, ops, result, l, r, n):
        if l >= r:
            return
        
        mid = (l + r) >> 1
        self.cdq(ops, result, l, mid, n)
        self.cdq(ops, result, mid + 1, r, n)
        
        # 合并过程，计算左半部分对右半部分的贡献
        tmp = [None] * (r - l + 1)
        i, j, k = l, mid + 1, 0
        
        while i <= mid and j <= r:
            if ops[i].idx <= ops[j].idx:
                # 左半部分的元素位置小于等于右半部分，处理插入操作
                if ops[i].op == 1:
                    self.add(ops[i].id, ops[i].op, n)  # 插入元素
                tmp[k] = ops[i]
                i += 1
                k += 1
            else:
                # 右半部分的元素位置更大，处理查询操作
                if ops[j].op == -1:
                    # 查询大于当前值的元素个数
                    result[ops[j].idx] += self.query(n) - self.query(ops[j].id)
                tmp[k] = ops[j]
                j += 1
                k += 1
        
        # 处理剩余元素
        while i <= mid:
            tmp[k] = ops[i]
            i += 1
            k += 1
            
        while j <= r:
            if ops[j].op == -1:
                result[ops[j].idx] += self.query(n) - self.query(ops[j].id)
            tmp[k] = ops[j]
            j += 1
            k += 1
        
        # 清理树状数组
        for t in range(l, mid + 1):
            if ops[t].op == 1:
                self.add(ops[t].id, -ops[t].op, n)
        
        # 将临时数组内容复制回原数组
        for t in range(k):
            ops[l + t] = tmp[t]
    
    # 去重函数
    def remove_duplicates(self, nums):
        if len(nums) == 0:
            return 0
        unique_size = 1
        for i in range(1, len(nums)):
            if nums[i] != nums[unique_size - 1]:
                nums[unique_size] = nums[i]
                unique_size += 1
        return unique_size
    
    # 二分查找函数
    def binary_search(self, arr, l, r, target):
        left, right = l, r - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1
        return left
    
    # 上界函数：返回第一个大于target的元素位置
    def upper_bound(self, arr, l, r, target):
        left, right = l, r
        while left < right:
            mid = (left + right) // 2
            if arr[mid] <= target:
                left = mid + 1
            else:
                right = mid
        return left

def main():
    solution = Solution493()
    
    # 测试用例1
    nums1 = [1, 3, 2, 3, 1]
    result1 = solution.reversePairs(nums1)
    print("输入: [1,3,2,3,1]")
    print("输出:", result1)
    print("期望: 2")
    
    # 测试用例2
    nums2 = [2, 4, 3, 5, 1]
    result2 = solution.reversePairs(nums2)
    print("输入: [2,4,3,5,1]")
    print("输出:", result2)
    print("期望: 3")

if __name__ == "__main__":
    main()