# 315. 计算右侧小于当前元素的个数
# 平台: LeetCode
# 难度: 困难
# 标签: CDQ分治, 分治
# 链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
# 
# 题目描述:
# 给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
# counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
# 
# 示例:
# 输入: nums = [5,2,6,1]
# 输出: [2,1,1,0]
# 解释:
# 5 的右侧有 2 个更小的元素 (2 和 1)
# 2 的右侧有 1 个更小的元素 (1)
# 6 的右侧有 1 个更小的元素 (1)
# 1 的右侧有 0 个更小的元素
# 
# 解题思路:
# 使用CDQ分治解决这个问题，将问题转化为三维偏序问题：
# 1. 第一维：索引，表示元素在原数组中的位置
# 2. 第二维：数值，表示元素的值
# 3. 第三维：时间/操作类型，用于区分查询和更新操作
# 
# 我们将每个元素看作两种操作：
# 1. 更新操作：在位置i插入数值nums[i]
# 2. 查询操作：查询在位置i右侧小于nums[i]的元素个数
# 
# 为了方便处理，我们从右向左遍历数组，这样问题就转化为：
# 对于每个元素，查询在它左侧（即原数组中它右侧）已经插入的小于它的元素个数
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class Operation:
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

class Solution:
    def __init__(self):
        self.bit = []  # 树状数组，用于高效维护前缀和
    
    def lowbit(self, x):
        """
        计算x的最低位1所代表的值
        树状数组操作的核心函数，用于快速找到父节点和子节点
        """
        return x & (-x)
    
    def add(self, x, v, n):
        """
        更新树状数组中的值
        
        参数:
            x: 要更新的索引位置
            v: 更新的增量值
            n: 树状数组的大小上限
        """
        i = x
        while i <= n:
            self.bit[i] += v
            i += self.lowbit(i)
    
    def query(self, x):
        """
        查询树状数组中[1, x]区间的前缀和
        
        参数:
            x: 查询的上界
        返回:
            前缀和结果
        """
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def countSmaller(self, nums):
        """
        LeetCode 315. 计算右侧小于当前元素的个数
        
        解题思路：使用CDQ分治法结合树状数组解决三维偏序问题
        
        参数:
            nums: List[int] - 输入数组
        返回:
            List[int] - 结果数组，counts[i]表示nums[i]右侧小于nums[i]的元素个数
            
        时间复杂度：O(n log²n)
        空间复杂度：O(n)
        """
        n = len(nums)
        if n == 0:
            return []
        
        # 离散化处理：将原始数值映射到较小的连续整数范围，优化树状数组空间使用
        sorted_nums = sorted(nums)
        unique_size = self.remove_duplicates(sorted_nums)
        
        ops = []  # 存储操作序列
        result = [0] * n  # 初始化结果数组
        self.bit = [0] * (n + 1)  # 初始化树状数组
        
        # 从右向左处理，构造操作序列
        # 这样在处理每个元素时，它右侧的元素已经被处理
        for i in range(n - 1, -1, -1):
            # 使用二分查找找到离散化后的值，+1是因为树状数组从1开始索引
            val_id = self.binary_search(sorted_nums, 0, unique_size, nums[i]) + 1
            
            # 添加插入操作：将当前元素的值插入到树状数组
            ops.append(Operation(1, nums[i], i, val_id))
            # 添加查询操作：查询小于nums[i]的已插入元素个数
            ops.append(Operation(-1, nums[i] - 1, i, val_id))
        
        # 按值排序操作序列，值相同时查询操作优先于插入操作
        ops.sort()
        
        # 执行CDQ分治，处理三维偏序关系
        self.cdq(ops, result, 0, len(ops) - 1, n)
        
        return result
    
    # CDQ分治主函数
    def cdq(self, ops, result, l, r, n):
        """
        CDQ分治算法的主函数，用于处理操作序列
        
        参数:
            ops: 操作序列
            result: 结果数组
            l: 当前处理区间的左边界
            r: 当前处理区间的右边界
            n: 树状数组大小上限
        """
        # 递归终止条件：区间长度为1或0
        if l >= r:
            return
        
        # 划分子区间
        mid = (l + r) >> 1  # 等价于(l + r) // 2，位运算更高效
        
        # 递归处理左右子区间
        self.cdq(ops, result, l, mid, n)
        self.cdq(ops, result, mid + 1, r, n)
        
        # 合并过程：计算左半部分对右半部分的贡献
        tmp = [None] * (r - l + 1)  # 临时数组用于归并排序
        i, j, k = l, mid + 1, 0
        
        # 双指针合并左右两个有序子数组
        while i <= mid and j <= r:
            # 根据元素索引（即原数组中的位置）比较
            if ops[i].idx <= ops[j].idx:
                # 左半部分的元素在原数组中的位置先于右半部分
                # 对于插入操作，更新树状数组
                if ops[i].op == 1:
                    self.add(ops[i].id, ops[i].op, n)  # 插入元素到树状数组
                tmp[k] = ops[i]
                i += 1
                k += 1
            else:
                # 右半部分的元素在原数组中的位置先于左半部分
                # 对于查询操作，计算树状数组中的前缀和
                if ops[j].op == -1:
                    # 查询小于当前值的元素个数
                    result[ops[j].idx] += self.query(ops[j].id - 1)
                tmp[k] = ops[j]
                j += 1
                k += 1
        
        # 处理左半部分剩余的操作
        while i <= mid:
            tmp[k] = ops[i]
            i += 1
            k += 1
            
        # 处理右半部分剩余的操作
        while j <= r:
            if ops[j].op == -1:
                # 对剩余的查询操作计算贡献
                result[ops[j].idx] += self.query(ops[j].id - 1)
            tmp[k] = ops[j]
            j += 1
            k += 1
        
        # 清理树状数组：撤销左半部分插入操作的影响
        # 这一步确保不会影响其他区间的处理
        for t in range(l, mid + 1):
            if ops[t].op == 1:
                self.add(ops[t].id, -ops[t].op, n)
        
        # 将临时数组内容复制回原数组，保证operations[l...r]区间有序
        for t in range(k):
            ops[l + t] = tmp[t]
    
    # 去重函数，保持数组有序性
    def remove_duplicates(self, nums):
        """
        原地移除有序数组中的重复元素
        
        参数:
            nums: 已排序的数组
        返回:
            去重后的数组长度
        """
        if len(nums) == 0:
            return 0
        unique_size = 1  # 指向当前可插入不重复元素的位置
        for i in range(1, len(nums)):
            # 当前元素与已去重部分的最后一个元素不同时，才添加
            if nums[i] != nums[unique_size - 1]:
                nums[unique_size] = nums[i]
                unique_size += 1
        return unique_size
    
    # 二分查找函数
    def binary_search(self, arr, l, r, target):
        """
        在有序数组中二分查找目标值的位置
        
        参数:
            arr: 已排序的数组
            l: 查找区间的左边界
            r: 查找区间的右边界（不包含）
            target: 目标值
        返回:
            目标值在离散化数组中的索引
        """
        left, right = l, r - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1
        return left

def main():
    solution = Solution()
    
    # 测试用例
    nums1 = [5, 2, 6, 1]
    result1 = solution.countSmaller(nums1)
    
    print("输入: [5,2,6,1]")
    print("输出:", result1)
    print("期望: [2,1,1,0]")

if __name__ == "__main__":
    main()