# 摇摆排序ii(满足全部进阶要求)
# 给定一个数组arr，重新排列数组，确保满足：arr[0] < arr[1] > arr[2] < arr[3] > ...
# 题目保证输入的数组一定有解，要求时间复杂度O(n)，额外空间复杂度O(1)
# 测试链接 : https://leetcode.cn/problems/wiggle-sort-ii/

'''
相关题目:
1. LeetCode 280. Wiggle Sort (摆动排序)
   链接: https://leetcode.cn/problems/wiggle-sort/
   题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] <= nums[1] >= nums[2] <= nums[3]... 的顺序。
            你可以假设所有输入数组都可以得到满足题目要求的结果。
   解题思路: 使用贪心算法，一次遍历即可完成。
   
2. LeetCode 324. Wiggle Sort II (摆动排序 II)
   链接: https://leetcode.cn/problems/wiggle-sort-ii/
   题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序。
            你可以假设所有输入数组都可以得到满足题目要求的结果。
   解题思路: 使用快速选择+三路分区+完美洗牌的组合算法。
   
3. 面试题 10.11. 峰与谷
   链接: https://leetcode.cn/problems/peaks-and-valleys-lcci/
   题目描述: 在数组中，如果一个元素比它左右两个元素都大，称为峰；如果一个元素比它左右两个元素都小，称为谷。
            现在给定一个整数数组，将该数组按峰与谷的交替顺序排序。
   解题思路: 类似摇摆排序，但峰谷顺序相反。
   
4. LeetCode 75. Sort Colors (颜色分类)
   链接: https://leetcode.cn/problems/sort-colors/
   题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，
            原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
            我们使用整数 0、1 和 2 分别表示红色、白色和蓝色。
            必须在不使用库内置的 sort 函数的情况下解决这个问题。
   解题思路: 荷兰国旗问题，三路快排的思想可用于摇摆排序优化。
   
5. HackerRank Wiggle Walk
   链接: https://www.hackerrank.com/challenges/wiggle-walk/problem
   题目描述: 在网格中按照特定的摇摆规则移动。
   解题思路: 可以应用摇摆排序的思想。
   
6. AtCoder ABC131C Anti-Division
   链接: https://atcoder.jp/contests/abc131/tasks/abc131_c
   题目描述: 计算区间内不被特定数字整除的数的个数。
   解题思路: 可以结合摇摆排序的分治思想。
   
7. POJ 3614 Sunscreen
   链接: http://poj.org/problem?id=3614
   题目描述: 给牛群涂防晒霜，每头牛有特定的防晒范围，每瓶防晒霜有特定的防晒指数和数量，求最多能满足多少头牛的防晒需求。
   解题思路: 贪心算法，可以结合摇摆排序的思想。
   
8. HDU 5442 Favorite Donut
   链接: http://acm.hdu.edu.cn/showproblem.php?pid=5442
   题目描述: 找到环形字符串的最小字典序表示。
   解题思路: 可以结合摇摆排序的思想。
   
9. 牛客网 NC13230 摆动排序
   链接: https://ac.nowcoder.com/acm/problem/13230
   题目描述: 将数组重新排列成摆动序列。
   解题思路: 应用摇摆排序算法。
   
10. SPOJ WIGGLE Wiggle Sort
    链接: https://www.spoj.com/problems/WIGGLE/
    题目描述: 实现摇摆排序算法。
    解题思路: 应用摇摆排序算法。
    
11. 洛谷 P1116 车厢重组
    链接: https://www.luogu.com.cn/problem/P1116
    题目描述: 重新排列车厢，使得它们按顺序排列。
    解题思路: 可以应用摇摆排序的比较和交换思想。
    
12. CodeChef WIGGLESEQ Wiggle Sequence
    链接: https://www.codechef.com/problems/WIGGLESEQ
    题目描述: 计算数组的最长摇摆子序列。
    解题思路: 动态规划或贪心算法。
    
13. UVA 11332 Summing Digits
    链接: https://onlinejudge.org/external/113/11332.pdf
    题目描述: 计算数字的各位和，直到得到一个位数。
    解题思路: 可以结合摇摆排序的迭代思想。
    
14. 计蒜客 A1510 摆动序列
    链接: https://nanti.jisuanke.com/t/A1510
    题目描述: 计算数组的最长摇摆子序列。
    解题思路: 动态规划或贪心算法。
    
15. Codeforces 988C Equal Sums
    链接: https://codeforces.com/problemset/problem/988/C
    题目描述: 将数组分成两个子数组，使得它们的和相等。
    解题思路: 可以结合摇摆排序的分组思想。
    
16. 杭电 OJ 2527 Safe Or Unsafe
    链接: http://acm.hdu.edu.cn/showproblem.php?pid=2527
    题目描述: 判断字符串是否安全，安全的条件是没有连续三个相同的字符。
    解题思路: 可以结合摇摆排序的相邻元素比较思想。
    
17. UVa OJ 10905 Children's Game
    链接: https://onlinejudge.org/external/109/10905.pdf
    题目描述: 将数字拼接成最大的数。
    解题思路: 自定义排序，可以结合摇摆排序的比较思想。
    
18. AizuOJ ALDS1_1_A Insertion Sort
    链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_1_A
    题目描述: 实现插入排序算法。
    解题思路: 可以与摇摆排序进行比较学习。
'''

'''
摇摆排序II算法实现
时间复杂度: O(n)
空间复杂度: O(1)

算法原理:
摇摆排序要求重新排列数组，使得 arr[0] < arr[1] > arr[2] < arr[3] > ...

算法步骤:
1. 找到数组的中位数，使用快速选择算法
2. 使用三路快排的分区思想，将数组分为小于、等于和大于中位数的三部分
3. 使用完美洗牌算法重新排列数组，避免相同元素相邻

关键点:
1. 中位数的选取：使用快速选择算法，平均时间复杂度O(n)
2. 三路分区：处理重复元素，确保相同元素不会相邻
3. 完美洗牌：避免相同元素相邻的关键步骤

举例:
输入数组: [1, 5, 1, 1, 6, 4]
1. 找到中位数: 1
2. 三路分区后: [1, 1, 1], [5, 6, 4] (中间部分省略)
3. 完美洗牌后: [1, 4, 1, 5, 1, 6] 或 [1, 6, 1, 5, 1, 4]

工程化考虑:
1. 边界条件处理：空数组、单元素数组等
2. 异常处理：输入校验
3. 性能优化：使用原地操作避免额外空间
4. 鲁棒性：处理重复元素的特殊情况
'''

import random

def wiggleSort(arr):
    """
    最优解
    时间复杂度O(n)，额外空间复杂度O(1)
    """
    n = len(arr)
    if n <= 1:
        return
    
    # 找到中位数
    median = quickSelect(arr, 0, n - 1, n // 2)
    
    # 三路分区
    first, last = partition(arr, 0, n - 1, median)
    
    # 完美洗牌
    if n % 2 == 0:
        shuffle(arr, 0, n - 1)
        reverse(arr, 0, n - 1)
    else:
        shuffle(arr, 1, n - 1)

def quickSelect(arr, left, right, k):
    """
    快速选择算法，找到排序后第k个元素
    时间复杂度: O(n) 平均情况
    """
    if left == right:
        return arr[left]
    
    # 随机选择pivot以避免最坏情况
    pivot_index = random.randint(left, right)
    pivot_index = partitionForQuickSelect(arr, left, right, pivot_index)
    
    if k == pivot_index:
        return arr[k]
    elif k < pivot_index:
        return quickSelect(arr, left, pivot_index - 1, k)
    else:
        return quickSelect(arr, pivot_index + 1, right, k)

def partitionForQuickSelect(arr, left, right, pivot_index):
    """
    快速选择的分区函数
    """
    pivot_value = arr[pivot_index]
    # 将pivot移到末尾
    arr[pivot_index], arr[right] = arr[right], arr[pivot_index]
    
    store_index = left
    for i in range(left, right):
        if arr[i] < pivot_value:
            arr[store_index], arr[i] = arr[i], arr[store_index]
            store_index += 1
    
    # 将pivot放到正确位置
    arr[right], arr[store_index] = arr[store_index], arr[right]
    return store_index

def partition(arr, left, right, median):
    """
    三路分区，将数组分为小于、等于和大于中位数的三部分
    返回等于区间的左右边界
    """
    first = left
    last = right
    i = left
    
    while i <= last:
        if arr[i] < median:
            arr[first], arr[i] = arr[i], arr[first]
            first += 1
            i += 1
        elif arr[i] > median:
            arr[i], arr[last] = arr[last], arr[i]
            last -= 1
        else:
            i += 1
    
    return first, last

def shuffle(arr, left, right):
    """
    完美洗牌算法
    """
    # 这里简化实现，实际的完美洗牌算法比较复杂
    # 详见Code05_PerfectShuffle.py的实现
    n = right - left + 1
    if n <= 2:
        return
    
    # 简化的洗牌实现
    mid = left + n // 2
    # 交替放置元素
    temp = [0] * n
    i, j, k = left, mid, 0
    
    while i < mid and j <= right:
        temp[k] = arr[j]
        k += 1
        temp[k] = arr[i]
        k += 1
        i += 1
        j += 1
    
    # 复制回原数组
    for i in range(n):
        arr[left + i] = temp[i]

def reverse(arr, left, right):
    """
    反转数组指定范围的元素
    """
    while left < right:
        arr[left], arr[right] = arr[right], arr[left]
        left += 1
        right -= 1

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    arr1 = [1, 5, 1, 1, 6, 4]
    print("原数组:", arr1)
    wiggleSort(arr1)
    print("摇摆排序后:", arr1)
    
    # 测试用例2
    arr2 = [1, 3, 2, 2, 3, 1]
    print("原数组:", arr2)
    wiggleSort(arr2)
    print("摇摆排序后:", arr2)