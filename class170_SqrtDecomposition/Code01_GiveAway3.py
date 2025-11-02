# Give Away，Python版
# 题目来源：SPOJ GIVEAWAY
# 题目链接：https://www.spoj.com/problems/GIVEAWAY/
# 题目大意：
# 给定一个长度为n的数组arr，接下来有m条操作，每条操作是如下两种类型中的一种
# 操作 0 a b c : 打印arr[a..b]范围上>=c的数字个数
# 操作 1 a b   : 把arr[a]的值改成b
# 1 <= n <= 5 * 10^5
# 1 <= m <= 10^5
# 1 <= 数组中的值 <= 10^9
# 测试链接 : https://www.luogu.com.cn/problem/SP18185
# 测试链接 : https://www.spoj.com/problems/GIVEAWAY

# 解题思路：
# 使用分块算法解决此问题
# 1. 将数组分成sqrt(n)大小的块
# 2. 每个块内维护一个排序后的数组，用于二分查找
# 3. 对于查询操作，完整块使用二分查找，不完整块直接遍历
# 4. 对于更新操作，更新原数组和对应块的排序数组，并重新排序

# 时间复杂度分析：
# 1. 预处理：O(n*sqrt(n))，对每个块进行排序
# 2. 查询操作：O(sqrt(n)*log(sqrt(n)))，遍历不完整块 + 二分查找完整块
# 3. 更新操作：O(sqrt(n)*log(sqrt(n)))，更新元素并重新排序块

# 空间复杂度：O(n)，存储原数组和排序数组

import math
import bisect
import sys

# 最大数组大小
MAXN = 500001

# 原数组
arr = [0] * MAXN
# 排序后的数组，用于二分查找
sortv = [0] * MAXN

# 块大小和块数量
blen = 0
bnum = 0
# 每个元素所属的块
bi = [0] * MAXN
# 每个块的左右边界
bl = [0] * MAXN
br = [0] * MAXN

def build(n):
    """
    构建分块结构
    时间复杂度：O(n*sqrt(n))
    """
    global blen, bnum
    
    # 块大小取sqrt(n)
    blen = int(math.sqrt(n))
    # 块数量
    bnum = (n + blen - 1) // blen
    
    # 计算每个元素属于哪个块
    for i in range(1, n + 1):
        bi[i] = (i - 1) // blen + 1
    
    # 计算每个块的左右边界
    for i in range(1, bnum + 1):
        bl[i] = (i - 1) * blen + 1
        br[i] = min(i * blen, n)
    
    # 复制原数组用于排序
    for i in range(1, n + 1):
        sortv[i] = arr[i]
    
    # 对每个块内的元素进行排序
    for i in range(1, bnum + 1):
        left = bl[i]
        right = br[i]
        # 提取块内元素并排序
        block_elements = [sortv[j] for j in range(left, right + 1)]
        block_elements.sort()
        # 将排序后的元素放回
        for j in range(len(block_elements)):
            sortv[left + j] = block_elements[j]

def getCnt(i, v):
    """
    在指定块内查找>=v的元素个数
    使用二分查找优化
    时间复杂度：O(log(sqrt(n)))
    :param i: 块编号
    :param v: 查找的值
    :return: >=v的元素个数
    """
    left = bl[i]
    right = br[i]
    
    # 提取块内元素
    block_elements = [sortv[j] for j in range(left, right + 1)]
    
    # 使用二分查找找到第一个>=v的位置
    pos = bisect.bisect_left(block_elements, v)
    
    # 返回>=v的元素个数
    return len(block_elements) - pos

def query(l, r, v):
    """
    查询区间[l,r]内>=v的元素个数
    时间复杂度：O(sqrt(n)*log(sqrt(n)))
    :param l: 区间左端点
    :param r: 区间右端点
    :param v: 查找的值
    :return: >=v的元素个数
    """
    ans = 0
    # 如果在同一个块内，直接暴力处理
    if bi[l] == bi[r]:
        for i in range(l, r + 1):
            if arr[i] >= v:
                ans += 1
    else:
        # 处理左端不完整块
        for i in range(l, br[bi[l]] + 1):
            if arr[i] >= v:
                ans += 1
        
        # 处理右端不完整块
        for i in range(bl[bi[r]], r + 1):
            if arr[i] >= v:
                ans += 1
        
        # 处理中间的完整块，使用二分查找优化
        for i in range(bi[l] + 1, bi[r]):
            ans += getCnt(i, v)
    
    return ans

def update(i, v):
    """
    更新位置i的值为v
    时间复杂度：O(sqrt(n)*log(sqrt(n)))
    :param i: 位置
    :param v: 新值
    """
    global arr
    
    block_id = bi[i]
    left = bl[block_id]
    right = br[block_id]
    arr[i] = v
    
    # 提取块内元素并排序
    block_elements = [arr[j] for j in range(left, right + 1)]
    block_elements.sort()
    
    # 将排序后的元素放回
    for j in range(len(block_elements)):
        sortv[left + j] = block_elements[j]

def main():
    global n, m, arr
    
    # 读取数组长度
    n = int(input())
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = elements[i - 1]
    
    # 构建分块结构
    build(n)
    
    # 读取操作数
    m = int(input())
    
    # 处理操作
    for _ in range(m):
        operation = list(map(int, input().split()))
        op = operation[0]
        a = operation[1]
        b = operation[2]
        
        if op == 0:
            # 查询操作
            c = operation[3]
            print(query(a, b, c))
        else:
            # 更新操作
            update(a, b)

if __name__ == "__main__":
    main()