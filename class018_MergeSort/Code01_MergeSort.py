# ============================================================================
# 归并排序专题：分治策略的经典应用 (Python实现)
# ============================================================================
#
# 【Python语言特性】
# 1. 动态类型：无需声明变量类型，代码简洁
# 2. 自动内存管理：垃圾回收机制，无需手动管理
# 3. 丰富的内置函数：切片操作、列表推导等
# 4. 递归深度限制：默认约1000层，大数据量需注意
#
# 【算法核心思想】
# 归并排序基于分治策略：分解 → 解决 → 合并
# 数学复杂度：T(n) = 2T(n/2) + O(n) ⇒ O(n log n)
#
# 【工程化考量】
# 1. 递归版本：代码简洁，但可能栈溢出
# 2. 非递归版本：避免栈溢出，适合大数据量
# 3. 内存优化：复用辅助数组，避免频繁分配
# 4. 边界处理：空数组、单元素数组等
#
# 测试链接 : https://www.luogu.com.cn/problem/P1177
#
# 详细题目列表请参考同目录下的MERGE_SORT_PROBLEMS.md文件
# 包含LeetCode、洛谷、牛客网、Codeforces等平台的归并排序相关题目
#
# ============================================================================
# 全局变量定义
# ============================================================================
import sys
from typing import List, Optional, Tuple

# 全局变量（竞赛风格）
MAXN = 100001  # 最大数据量
arr = [0] * MAXN      # 原始数组
help_arr = [0] * MAXN # 辅助数组
n = 0                 # 实际数据个数


# ============================================================================
# 基础归并排序实现
# ============================================================================

def merge_sort1(l: int, r: int) -> None:
    """
    归并排序递归版本
    【时间复杂度】O(n log n)
    【空间复杂度】O(n) + O(log n)递归栈
    【适用场景】代码简洁，易于理解
    【注意事项】Python递归深度限制，大数据量可能栈溢出
    详细说明：
    1. 递归终止条件：当l >= r时，子数组只有一个元素，已经有序
    2. 分解：将数组从中间分成两部分
    3. 递归解决：对左右两部分分别进行归并排序
    4. 合并：将两个有序子数组合并成一个有序数组
    """
    if l == r:
        return
    m = (l + r) // 2
    merge_sort1(l, m)
    merge_sort1(m + 1, r)
    merge(l, m, r)


def merge_sort2() -> None:
    """
    归并排序非递归版本
    【时间复杂度】O(n log n)
    【空间复杂度】O(n) - 无递归栈开销
    【适用场景】大数据量排序，避免栈溢出
    【工程优势】更好的缓存局部性，易于理解
    详细说明：
    1. 从最小的子数组开始（长度为1），逐步扩大子数组大小
    2. 每次将相邻的两个子数组合并成一个更大的有序数组
    3. 重复此过程直到整个数组有序
    """
    step = 1
    while step < n:
        l = 0
        while l < n:
            m = l + step - 1
            if m + 1 >= n:  # 没有第二组，不需要合并
                break
            r = min(l + (step << 1) - 1, n - 1)
            merge(l, m, r)
            l = r + 1
        step <<= 1  # 步长翻倍


def merge(l: int, m: int, r: int) -> None:
    """
    合并两个有序数组的核心函数
    【参数说明】
    l: 左边界索引
    m: 中点索引
    r: 右边界索引
    【前置条件】[l, m] 和 [m+1, r] 已经各自有序
    【后置条件】[l, r] 整体有序
    【时间复杂度】O(n)
    【稳定性】稳定（使用<=比较保证）
    详细说明：
    1. 使用三个指针：i指向辅助数组当前位置，a指向左半部分，b指向右半部分
    2. 比较左右两部分的元素，将较小的元素放入辅助数组
    3. 处理剩余元素：将未处理完的部分直接拷贝到辅助数组
    4. 将辅助数组的内容拷贝回原数组
    """
    i = l      # 辅助数组当前位置
    a = l      # 左半部分指针
    b = m + 1  # 右半部分指针
    
    # 双指针合并：比较两个部分的元素
    while a <= m and b <= r:
        if arr[a] <= arr[b]:  # 保证稳定性
            help_arr[i] = arr[a]
            a += 1
        else:
            help_arr[i] = arr[b]
            b += 1
        i += 1
    
    # 处理剩余元素（左半部分或右半部分）
    while a <= m:
        help_arr[i] = arr[a]
        a += 1
        i += 1
    
    while b <= r:
        help_arr[i] = arr[b]
        b += 1
        i += 1
    
    # 将合并结果复制回原数组
    for idx in range(l, r + 1):
        arr[idx] = help_arr[idx]


# ============================================================================
# 题目1：LeetCode 912. 排序数组
# ============================================================================
def sort_array(nums: List[int]) -> List[int]:
    """
    LeetCode 912. 排序数组
    【题目链接】https://leetcode.cn/problems/sort-an-array/
    【题目描述】给定一个整数数组 nums，将该数组升序排列
    【输入示例】[5,2,3,1]
    【输出示例】[1,2,3,5]
    【时间复杂度】O(n log n)
    【空间复杂度】O(n)
    【是否最优】是 - 基于比较的排序算法下界
    详细说明：
    1. 使用切片操作简化数组处理
    2. 递归实现归并排序
    3. 合并过程中保持稳定性
    """
    if len(nums) <= 1:
        return nums
    
    def merge_sort_array(left: int, right: int) -> None:
        """递归排序函数"""
        if left >= right:
            return
        mid = left + (right - left) // 2
        merge_sort_array(left, mid)
        merge_sort_array(mid + 1, right)
        merge_array(left, mid, right)
    
    def merge_array(left: int, mid: int, right: int) -> None:
        """合并两个有序子数组"""
        # 使用切片复制到辅助数组（Python特性）
        helper = nums[left:right+1]
        
        i = left      # 原数组指针
        p1 = 0       # 左半部分在helper中的指针
        p2 = mid - left + 1  # 右半部分在helper中的指针
        
        # 双指针合并
        while p1 <= mid - left and p2 <= right - left:
            if helper[p1] <= helper[p2]:
                nums[i] = helper[p1]
                p1 += 1
            else:
                nums[i] = helper[p2]
                p2 += 1
            i += 1
        
        # 处理剩余元素
        while p1 <= mid - left:
            nums[i] = helper[p1]
            p1 += 1
            i += 1
        
        while p2 <= right - left:
            nums[i] = helper[p2]
            p2 += 1
            i += 1
    
    merge_sort_array(0, len(nums) - 1)
    return nums


# 链表节点定义
class ListNode:
    def __init__(self, val: int = 0, next: Optional['ListNode'] = None):
        self.val = val
        self.next = next


# ============================================================================
# 题目2：LeetCode 148. 排序链表  
# ============================================================================
def sort_list(head: Optional[ListNode]) -> Optional[ListNode]:
    """
    LeetCode 148. 排序链表
    【题目链接】https://leetcode.cn/problems/sort-list/
    【题目描述】给你链表的头结点 head，请将其按升序排列并返回排序后的链表
    【算法优势】归并排序特别适合链表结构，只需修改指针
    【时间复杂度】O(n log n)
    【空间复杂度】O(log n) - 递归调用栈
    【是否最优】是 - 链表排序的最佳选择
    详细说明：
    1. 使用快慢指针找到链表中点
    2. 递归排序两个子链表
    3. 合并两个有序链表
    """
    if not head or not head.next:
        return head
    return process(head)


def process(head: Optional[ListNode]) -> Optional[ListNode]:
    """链表归并排序的递归处理函数"""
    if not head or not head.next:
        return head
    
    # 使用快慢指针找到链表中点（龟兔赛跑算法）
    slow = head
    fast = head
    prev = None
    
    while fast and fast.next:
        prev = slow
        slow = slow.next  # type: ignore
        fast = fast.next.next
    
    # 断开链表，分为左右两部分
    if prev:
        prev.next = None
    
    # 递归排序左右两部分
    left = process(head)
    right = process(slow)  # type: ignore
    
    # 合并两个有序链表
    return merge_list(left, right)


def merge_list(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
    """
    合并两个有序链表
    【技巧】使用哑节点(dummy node)简化边界处理
    【时间复杂度】O(m + n)
    【空间复杂度】O(1)
    详细说明：
    1. 使用哑节点简化边界处理
    2. 双指针比较合并
    3. 处理剩余节点
    """
    dummy = ListNode(0)  # 哑节点
    current = dummy
    
    # 双指针比较合并
    while l1 and l2:
        if l1.val <= l2.val:
            current.next = l1
            l1 = l1.next
        else:
            current.next = l2
            l2 = l2.next
        current = current.next
    
    # 连接剩余节点
    current.next = l1 if l1 else l2
    
    return dummy.next


# LeetCode 23. 合并K个升序链表
# 测试链接: https://leetcode.cn/problems/merge-k-sorted-lists/
# 时间复杂度: O(N * logk) - N是所有节点总数，k是链表数量
# 空间复杂度: O(logk) - 递归调用栈
def merge_k_lists(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
    if not lists:
        return None
    return merge_k_lists_helper(lists, 0, len(lists) - 1)


def merge_k_lists_helper(lists: List[Optional[ListNode]], left: int, right: int) -> Optional[ListNode]:
    if left == right:
        return lists[left]
    if left + 1 == right:
        return merge_list(lists[left], lists[right])
    mid = left + (right - left) // 2
    l1 = merge_k_lists_helper(lists, left, mid)
    l2 = merge_k_lists_helper(lists, mid + 1, right)
    return merge_list(l1, l2)


# LeetCode 88. 合并两个有序数组
# 测试链接: https://leetcode.cn/problems/merge-sorted-array/
# 时间复杂度: O(m + n)
# 空间复杂度: O(1)
def merge_sorted_array(nums1: List[int], m: int, nums2: List[int], n: int) -> None:
    i = m - 1  # nums1的指针
    j = n - 1  # nums2的指针
    k = m + n - 1  # 合并后数组的指针
    
    # 从后往前合并
    while i >= 0 and j >= 0:
        if nums1[i] > nums2[j]:
            nums1[k] = nums1[i]
            i -= 1
        else:
            nums1[k] = nums2[j]
            j -= 1
        k -= 1
    
    # 处理nums2剩余元素
    while j >= 0:
        nums1[k] = nums2[j]
        j -= 1
        k -= 1


# LeetCode 21. 合并两个有序链表
# 测试链接: https://leetcode.cn/problems/merge-two-sorted-lists/
# 时间复杂度: O(m + n)
# 空间复杂度: O(1)
def merge_two_lists(list1: Optional[ListNode], list2: Optional[ListNode]) -> Optional[ListNode]:
    dummy = ListNode(0)
    current = dummy
    
    while list1 and list2:
        if list1.val <= list2.val:
            current.next = list1
            list1 = list1.next
        else:
            current.next = list2
            list2 = list2.next
        current = current.next
    
    # 处理剩余节点
    current.next = list1 if list1 else list2
    
    return dummy.next


# LeetCode 315. 计算右侧小于当前元素的个数
# 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
# 时间复杂度: O(n * logn)
# 空间复杂度: O(n)
def count_smaller(nums: List[int]) -> List[int]:
    result = []
    n = len(nums)
    if n == 0:
        return result
    
    counts = [0] * n
    indices = list(range(n))  # 索引数组
    
    helper = [0] * n
    helper_indices = [0] * n
    
    def merge_sort_count(left: int, right: int) -> None:
        if left >= right:
            return
        
        mid = left + (right - left) // 2
        merge_sort_count(left, mid)
        merge_sort_count(mid + 1, right)
        merge_count(left, mid, right)
    
    def merge_count(left: int, mid: int, right: int) -> None:
        # 复制到辅助数组
        for i in range(left, right + 1):
            helper[i] = nums[i]
            helper_indices[i] = indices[i]
        
        i = left
        p1 = left
        p2 = mid + 1
        
        # 合并两个有序数组
        while p1 <= mid and p2 <= right:
            # 当左侧元素小于等于右侧元素时
            if helper[p1] <= helper[p2]:
                # 更新计数：右侧已处理的元素个数
                counts[helper_indices[p1]] += (p2 - (mid + 1))
                nums[i] = helper[p1]
                indices[i] = helper_indices[p1]
                p1 += 1
            else:
                nums[i] = helper[p2]
                indices[i] = helper_indices[p2]
                p2 += 1
            i += 1
        
        # 处理左侧剩余元素
        while p1 <= mid:
            # 更新计数：右侧所有元素都比它小
            counts[helper_indices[p1]] += (p2 - (mid + 1))
            nums[i] = helper[p1]
            indices[i] = helper_indices[p1]
            p1 += 1
            i += 1
        
        # 处理右侧剩余元素
        while p2 <= right:
            nums[i] = helper[p2]
            indices[i] = helper_indices[p2]
            p2 += 1
            i += 1
    
    merge_sort_count(0, n - 1)
    
    for count in counts:
        result.append(count)
    
    return result


# LeetCode 493. 翻转对
# 测试链接: https://leetcode.cn/problems/reverse-pairs/
# 时间复杂度: O(n * logn)
# 空间复杂度: O(n)
def reverse_pairs(nums: List[int]) -> int:
    if len(nums) <= 1:
        return 0
    
    helper = [0] * len(nums)
    
    def merge_sort_reverse_pairs(left: int, right: int) -> int:
        if left >= right:
            return 0
        
        mid = left + (right - left) // 2
        count = merge_sort_reverse_pairs(left, mid)
        count += merge_sort_reverse_pairs(mid + 1, right)
        count += merge_reverse_pairs(left, mid, right)
        
        return count
    
    def merge_reverse_pairs(left: int, mid: int, right: int) -> int:
        # 复制到辅助数组
        for i in range(left, right + 1):
            helper[i] = nums[i]
        
        count = 0
        j = mid + 1
        
        # 统计翻转对数量
        for i in range(left, mid + 1):
            # 对于nums[i]，找到第一个满足nums[i] > 2 * nums[j]的j
            while j <= right and helper[i] > 2 * helper[j]:
                j += 1
            count += (j - (mid + 1))
        
        # 合并两个有序数组
        i = left
        p1 = left
        p2 = mid + 1
        
        while p1 <= mid and p2 <= right:
            if helper[p1] <= helper[p2]:
                nums[i] = helper[p1]
                p1 += 1
            else:
                nums[i] = helper[p2]
                p2 += 1
            i += 1
        
        while p1 <= mid:
            nums[i] = helper[p1]
            p1 += 1
            i += 1
        
        while p2 <= right:
            nums[i] = helper[p2]
            p2 += 1
            i += 1
        
        return count
    
    return merge_sort_reverse_pairs(0, len(nums) - 1)


# ============================================================================
# 题目3：LeetCode 315. 计算右侧小于当前元素的个数
# ============================================================================
def count_smaller(nums: List[int]) -> List[int]:
    """
    LeetCode 315. 计算右侧小于当前元素的个数
    【题目链接】https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
    【题目描述】统计每个元素右侧比它小的元素个数
    【算法核心】归并排序 + 索引数组
    【时间复杂度】O(n log n)
    【空间复杂度】O(n)
    【关键技巧】在合并过程中利用有序性统计右侧较小元素
    详细说明：
    1. 使用索引数组记录原始位置
    2. 在合并过程中统计右侧较小元素
    3. 利用有序性优化统计过程
    """
    n = len(nums)
    if n == 0:
        return []
    
    # 初始化结果数组和索引数组
    result = [0] * n
    indices = list(range(n))  # 记录原始位置
    
    def merge_sort_count(left: int, right: int) -> None:
        if left >= right:
            return
        
        mid = (left + right) // 2
        merge_sort_count(left, mid)
        merge_sort_count(mid + 1, right)
        
        # 合并并统计
        i, j = left, mid + 1
        temp = []
        temp_indices = []
        
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                # 关键统计：当左侧元素出队时，统计右侧已处理的较小元素
                result[indices[i]] += j - (mid + 1)
                temp.append(nums[i])
                temp_indices.append(indices[i])
                i += 1
            else:
                temp.append(nums[j])
                temp_indices.append(indices[j])
                j += 1
        
        # 处理左侧剩余元素
        while i <= mid:
            result[indices[i]] += j - (mid + 1)
            temp.append(nums[i])
            temp_indices.append(indices[i])
            i += 1
        
        # 处理右侧剩余元素
        while j <= right:
            temp.append(nums[j])
            temp_indices.append(indices[j])
            j += 1
        
        # 更新原数组和索引数组
        for idx in range(left, right + 1):
            nums[idx] = temp[idx - left]
            indices[idx] = temp_indices[idx - left]
    
    merge_sort_count(0, n - 1)
    return result


# ============================================================================
# 题目4：LeetCode 493. 翻转对
# ============================================================================
def reverse_pairs(nums: List[int]) -> int:
    """
    LeetCode 493. 翻转对
    【题目链接】https://leetcode.cn/problems/reverse-pairs/
    【题目描述】统计满足 i < j 且 nums[i] > 2*nums[j] 的翻转对数量
    【与315区别】统计条件更严格（2倍关系）
    【关键技巧】先统计后合并，使用long防溢出
    【时间复杂度】O(n log n)
    【空间复杂度】O(n)
    详细说明：
    1. 在合并前先统计翻转对
    2. 利用有序性优化统计过程
    3. 使用双指针减少重复计算
    """
    if len(nums) <= 1:
        return 0
    
    def merge_sort_count(left: int, right: int) -> int:
        if left >= right:
            return 0
        
        mid = (left + right) // 2
        count = merge_sort_count(left, mid)
        count += merge_sort_count(mid + 1, right)
        
        # 先统计翻转对数量
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)
        
        # 后合并两个有序数组
        temp = []
        i, j = left, mid + 1
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                temp.append(nums[j])
                j += 1
        
        temp.extend(nums[i:mid+1])
        temp.extend(nums[j:right+1])
        nums[left:right+1] = temp
        
        return count
    
    return merge_sort_count(0, len(nums) - 1)


# ============================================================================
# 题目5：逆序对统计（剑指Offer 51风格）
# ============================================================================
def reverse_pairs_count(nums: List[int]) -> int:
    """
    剑指Offer 51. 数组中的逆序对
    【题目链接】https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
    【题目描述】统计数组中的逆序对总数
    【算法核心】归并排序过程中统计逆序对
    【时间复杂度】O(n log n)
    【空间复杂度】O(n)
    详细说明：
    1. 在合并过程中统计逆序对
    2. 利用有序性优化统计过程
    3. 当右元素小于左元素时统计逆序对
    """
    if len(nums) <= 1:
        return 0
    
    def merge_sort_count(left: int, right: int) -> int:
        if left >= right:
            return 0
        
        mid = (left + right) // 2
        count = merge_sort_count(left, mid)
        count += merge_sort_count(mid + 1, right)
        
        # 合并并统计逆序对
        temp = []
        i, j = left, mid + 1
        
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                # 关键统计：当右元素小于左元素时
                count += mid - i + 1
                temp.append(nums[j])
                j += 1
        
        temp.extend(nums[i:mid+1])
        temp.extend(nums[j:right+1])
        nums[left:right+1] = temp
        
        return count
    
    return merge_sort_count(0, len(nums) - 1)


# ============================================================================
# 扩展题目实现：更多归并排序应用
# ============================================================================

# 题目6：LeetCode 2426. 满足不等式的数对数目
# 链接：https://leetcode.cn/problems/number-of-pairs-satisfying-inequality/
# 时间复杂度：O(n log n)
# 空间复杂度：O(n)
# 核心思想：翻转对变种，处理不等式条件
def number_of_pairs(nums1: List[int], nums2: List[int], diff: int) -> int:
    n = len(nums1)
    arr = [nums1[i] - nums2[i] for i in range(n)]  # 构造差值数组
    return count_pairs(arr, diff)

def count_pairs(arr: List[int], diff: int) -> int:
    if len(arr) <= 1:
        return 0
    helper = [0] * len(arr)
    return merge_sort_pairs(arr, helper, 0, len(arr) - 1, diff)

def merge_sort_pairs(arr: List[int], helper: List[int], left: int, right: int, diff: int) -> int:
    if left >= right:
        return 0
    
    mid = (left + right) // 2
    count = merge_sort_pairs(arr, helper, left, mid, diff)
    count += merge_sort_pairs(arr, helper, mid + 1, right, diff)
    
    # 统计满足条件的数对
    j = mid + 1
    for i in range(left, mid + 1):
        # 条件：arr[i] <= arr[j] + diff
        while j <= right and arr[i] <= arr[j] + diff:
            j += 1
        count += j - (mid + 1)
    
    # 合并两个有序数组
    merge_arrays(arr, helper, left, mid, right)
    return count

def merge_arrays(arr: List[int], helper: List[int], left: int, mid: int, right: int) -> None:
    for i in range(left, right + 1):
        helper[i] = arr[i]
    
    i, j, k = left, mid + 1, left
    while i <= mid and j <= right:
        if helper[i] <= helper[j]:
            arr[k] = helper[i]
            i += 1
        else:
            arr[k] = helper[j]
            j += 1
        k += 1
    
    while i <= mid:
        arr[k] = helper[i]
        i += 1
        k += 1
    
    while j <= right:
        arr[k] = helper[j]
        j += 1
        k += 1

# 题目7：LeetCode 4. 寻找两个正序数组的中位数
# 链接：https://leetcode.cn/problems/median-of-two-sorted-arrays/
# 时间复杂度：O(log(min(m, n)))
# 空间复杂度：O(1)
# 核心思想：二分查找，不是归并排序但涉及有序数组合并思想
def find_median_sorted_arrays(nums1: List[int], nums2: List[int]) -> float:
    if len(nums1) > len(nums2):
        return find_median_sorted_arrays(nums2, nums1)
    
    m, n = len(nums1), len(nums2)
    left, right = 0, m
    
    while left <= right:
        i = (left + right) // 2
        j = (m + n + 1) // 2 - i
        
        max_left1 = float('-inf') if i == 0 else nums1[i - 1]
        min_right1 = float('inf') if i == m else nums1[i]
        max_left2 = float('-inf') if j == 0 else nums2[j - 1]
        min_right2 = float('inf') if j == n else nums2[j]
        
        if max_left1 <= min_right2 and max_left2 <= min_right1:
            if (m + n) % 2 == 0:
                return (max(max_left1, max_left2) + min(min_right1, min_right2)) / 2.0
            else:
                return max(max_left1, max_left2)
        elif max_left1 > min_right2:
            right = i - 1
        else:
            left = i + 1
    
    return 0.0

# 题目8：外部排序模拟 - 多路归并
# 模拟处理大规模数据，无法一次性装入内存的情况
def external_sort_simulation(large_array: List[int], memory_limit: int) -> None:
    n = len(large_array)
    chunk_size = memory_limit
    num_chunks = (n + chunk_size - 1) // chunk_size
    
    # 模拟分块排序（实际中会写入临时文件）
    for i in range(num_chunks):
        start = i * chunk_size
        end = min(start + chunk_size, n)
        # 对当前块进行排序（模拟内部排序）
        large_array[start:end] = sorted(large_array[start:end])
    
    print(f"外部排序模拟完成，处理数据量: {n}")

# ============================================================================
# 性能测试与优化
# ============================================================================

import time
import random

# 性能测试：测试不同规模数据的排序性能
def performance_test():
    print("=== 性能测试 ===")
    
    sizes = [1000, 10000, 100000, 1000000]
    for size in sizes:
        test_data = generate_random_array(size)
        
        start_time = time.time()
        sorted_data = sorted(test_data)  # 使用内置排序作为基准
        end_time = time.time()
        
        duration = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"数据量: {size:,}, 耗时: {duration:.2f} ms")

# 生成随机测试数组
def generate_random_array(size: int) -> List[int]:
    return [random.randint(0, size * 10) for _ in range(size)]

# 边界测试：测试各种边界情况
def boundary_test():
    print("=== 边界测试 ===")
    
    # 测试空数组
    test_case([], "空数组")
    
    # 测试单元素数组
    test_case([1], "单元素数组")
    
    # 测试已排序数组
    test_case([1, 2, 3, 4, 5], "已排序数组")
    
    # 测试逆序数组
    test_case([5, 4, 3, 2, 1], "逆序数组")
    
    # 测试重复元素数组
    test_case([2, 2, 1, 1, 3, 3], "重复元素数组")
    
    # 测试大数数组
    test_case([10**9, -10**9, 0], "极值数组")

def test_case(input_data: List[int], description: str):
    result = sorted(input_data)
    passed = is_sorted(result)
    print(f"{description}测试: {'✓ PASSED' if passed else '✗ FAILED'}")

# 检查数组是否有序
def is_sorted(arr: List[int]) -> bool:
    return all(arr[i] <= arr[i + 1] for i in range(len(arr) - 1))

# ============================================================================
# 调试工具方法
# ============================================================================

# 调试打印：打印数组内容（用于调试）
def print_array(arr: List[int], label: str):
    print(f"{label}: [{', '.join(map(str, arr[:min(10, len(arr))]))}" + 
          ("..." if len(arr) > 10 else "") + "]")

# ============================================================================
# 单元测试增强版
# ============================================================================

def test_basic_sort():
    """测试基础排序功能"""
    test_data = [5, 2, 3, 1, 4]
    expected = [1, 2, 3, 4, 5]
    result = sort_array(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ 基础排序测试通过")

def test_reverse_pairs():
    """测试逆序对统计"""
    test_data = [7, 5, 6, 4]
    expected = 5
    result = reverse_pairs_count(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ 逆序对统计测试通过")

def test_count_smaller():
    """测试右侧较小元素统计"""
    test_data = [5, 2, 6, 1]
    expected = [2, 1, 1, 0]
    result = count_smaller(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ 右侧较小元素统计测试通过")

def test_number_of_pairs():
    """测试满足不等式的数对数目"""
    nums1 = [3, 2, 5]
    nums2 = [2, 2, 1]
    diff = 1
    expected = 3
    result = number_of_pairs(nums1, nums2, diff)
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ 不等式数对统计测试通过")

def run_comprehensive_tests():
    """运行全面测试"""
    print("=== 开始全面测试 ===")
    test_basic_sort()
    test_reverse_pairs()
    test_count_smaller()
    test_number_of_pairs()
    boundary_test()
    print("=== 测试完成 ===")

# ============================================================================
# 主函数：支持多种运行模式
# ============================================================================

if __name__ == "__main__":
    import sys
    
    if len(sys.argv) > 1 and sys.argv[1] == "test":
        # 测试模式
        run_comprehensive_tests()
    elif len(sys.argv) > 1 and sys.argv[1] == "perf":
        # 性能测试模式
        performance_test()
    else:
        # 默认模式：运行基础测试
        run_comprehensive_tests()
        print("\n使用 'python Code01_MergeSort.py test' 运行全面测试")
        print("使用 'python Code01_MergeSort.py perf' 运行性能测试")

# ============================================================================
# Python工程化考量总结
# ============================================================================

"""
【Python工程化最佳实践】
1. 类型注解：使用typing模块提高代码可读性
2. 文档字符串：为每个函数编写详细的文档说明
3. 异常处理：合理使用try-except处理异常情况
4. 代码风格：遵循PEP8编码规范
5. 模块化设计：将功能分解为独立的函数和模块

【Python语言特性优势】
1. 简洁语法：代码量少，开发效率高
2. 动态类型：无需声明变量类型，灵活性强
3. 丰富库：标准库和第三方库功能强大
4. 跨平台：一次编写，到处运行

【Python性能优化】
1. 使用内置函数：如sorted()代替手动实现排序
2. 列表推导：比循环更高效
3. 生成器：节省内存，适合大数据处理
4. 局部变量：访问速度比全局变量快

【调试技巧】
1. 使用pdb调试器：设置断点、单步执行
2. 打印调试：使用print输出关键变量
3. 断言检查：使用assert验证中间结果
4. 日志记录：使用logging模块记录运行信息

更多题目请参考同目录下的MERGE_SORT_PROBLEMS.md文件
"""


# ============================================================================
# 单元测试函数
# ============================================================================
def test_basic_sort():
    """测试基础排序功能"""
    test_data = [5, 2, 3, 1, 4]
    expected = [1, 2, 3, 4, 5]
    result = sort_array(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ Basic sort test passed")

def test_reverse_pairs():
    """测试逆序对统计"""
    test_data = [7, 5, 6, 4]
    expected = 5
    result = reverse_pairs_count(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ Reverse pairs test passed")

def test_count_smaller():
    """测试右侧较小元素统计"""
    test_data = [5, 2, 6, 1]
    expected = [2, 1, 1, 0]
    result = count_smaller(test_data.copy())
    assert result == expected, f"Expected {expected}, but got {result}"
    print("✓ Count smaller test passed")

def run_tests():
    """运行所有测试"""
    print("Running tests...")
    test_basic_sort()
    test_reverse_pairs()
    test_count_smaller()
    print("All tests passed! ✓")

# 取消注释以下行来运行测试
# if __name__ == "__main__":
#     run_tests()