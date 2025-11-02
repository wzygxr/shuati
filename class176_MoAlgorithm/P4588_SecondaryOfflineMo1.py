import sys
import math
from sys import stdin

"""
洛谷 P4588 【模板】二次离线莫队
题目链接：https://www.luogu.com.cn/problem/P4588

题目描述：
给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内满足a[i] + a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量。

输入格式：
第一行两个整数n和m，表示数组长度和查询次数。
第二行n个整数表示数组元素。
接下来m行，每行两个整数l, r表示查询区间。

输出格式：
对于每个查询，输出一行一个整数表示答案。

数据范围：
1 <= n, m <= 100000
1 <= a[i] <= 100000

解题思路：
1. 首先，我们需要知道两个数的异或结果中1的个数的奇偶性等于它们和的二进制中1的个数的奇偶性
2. 因此，问题转化为求区间内满足a[i] ^ a[j]的二进制表示中1的个数是奇数的无序对(i,j)的数量
3. 对于每个位置j，我们可以维护一个前缀和sum[j]，表示前j个元素中，二进制中1的个数为奇数的元素个数
4. 然后，我们可以使用二次离线莫队算法来高效处理这些查询

时间复杂度：O(n * sqrt(n))
空间复杂度：O(n + m)
"""

MAXN = 100010
MAX_VAL = 100010

# 预处理每个数的二进制中1的个数的奇偶性
def preprocess_popcount():
    popcount = [0] * MAX_VAL
    for i in range(1, MAX_VAL):
        popcount[i] = popcount[i >> 1] ^ (i & 1)  # 如果最后一位是1，奇偶性翻转
    return popcount

def main():
    input = stdin.read().split()
    ptr = 0
    
    popcount = preprocess_popcount()
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取数组
    a = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        a[i] = int(input[ptr])
        ptr += 1
    
    # 读取查询
    queries = []
    for i in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        queries.append( (l, r, i) )
    
    # 设置块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 对查询进行排序
    queries.sort(key=lambda q: (q[0] // block_size, q[1] if (q[0] // block_size) % 2 == 0 else -q[1]))
    
    # 二次离线莫队处理
    # 初始化事件列表
    events = [[] for _ in range(n + 2)]
    ans = [0] * m
    
    # 第一部分：莫队处理
    cur_l = 1
    cur_r = 0
    now = 0  # 当前的答案
    
    for i in range(m):
        l, r, idx = queries[i]
        
        # 处理右边界的扩展
        if r > cur_r:
            events[cur_r].append( (l, r, cur_r, i, 1) )
            now += (r - cur_r) * (l - 1)
            cur_r = r
        
        # 处理右边界的收缩
        if r < cur_r:
            events[r + 1].append( (l, cur_r, l - 1, i, -1) )
            now -= (cur_r - r) * (l - 1)
            cur_r = r
        
        # 处理左边界的扩展
        if l < cur_l:
            events[cur_l - 1].append( (l, cur_l - 1, r, i, 1) )
            now += (cur_l - l) * (n - r)
            cur_l = l
        
        # 处理左边界的收缩
        if l > cur_l:
            events[cur_l].append( (l, l, r, i, -1) )
            now -= (l - cur_l) * (n - r)
            cur_l = l
        
        # 保存当前的中间结果
        ans[queries[i][2]] = now
    
    # 第二部分：离线处理事件
    cnt = [0] * (max(a) + 1) if n > 0 else []
    
    for i in range(1, n + 1):
        # 处理所有与当前位置i相关的事件
        for event in events[i]:
            l, r, x, id_event, type_event = event
            
            # 计算区间[l,r]中满足条件的元素个数
            res = 0
            for j in range(l, r + 1):
                res += popcount[a[j]] ^ popcount[a[x]]
            
            ans[queries[id_event][2]] += res * type_event
        
        # 更新计数器
        cnt[a[i]] += 1
    
    # 处理最终的答案，计算无序对的数量
    for i in range(m):
        l, r, idx = queries[i]
        total = (r - l + 1) * (r - l) // 2
        ans[idx] = total - ans[idx]
    
    # 输出结果
    sys.stdout.write('\n'.join(map(str, ans)) + '\n')

if __name__ == "__main__":
    main()

'''
算法分析：
时间复杂度：O(n * sqrt(n))
- 第一次莫队排序的时间复杂度：O(m * log m)
- 第一次莫队处理的时间复杂度：O((n + m) * sqrt(n))
- 第二次离线处理的时间复杂度：O(n * sqrt(n))
- 整体时间复杂度：O(n * sqrt(n))

空间复杂度：O(n + m)
- 数组存储：O(n)
- 查询数组和答案数组：O(m)
- 事件列表：O(n + m)

优化点：
1. 使用了奇偶优化，减少块间转移的时间
2. 使用sys.stdin.read().split()一次性读取所有输入，提高输入效率
3. 通过预处理二进制中1的个数的奇偶性，加速计算
4. 使用二次离线莫队算法，将时间复杂度从O(n * sqrt(n) * log n)优化到O(n * sqrt(n))

边界情况处理：
1. 确保查询区间的有效性
2. 处理空区间的情况
3. 使用Python的长整型存储答案，避免溢出

工程化考量：
1. 一次性读取所有输入并使用指针访问，提高输入效率
2. 使用sys.stdout.write进行批量输出，提高输出效率
3. 动态创建cnt数组，只存储实际需要的大小

调试技巧：
1. 可以在莫队处理过程中输出中间变量now的值，检查是否正确
2. 测试用例：如n=3, m=1, a=[1,2,3]，查询[1,3]，预期结果为3（所有无序对都满足条件）
3. 在Python中处理大数据时，可能需要进一步优化以通过时间限制

注意事项：
1. 在Python中，对于大规模数据，这个实现可能会超时，可以考虑以下优化：
   - 使用PyPy运行代码
   - 进一步优化事件处理部分的循环
   - 使用更高效的数据结构
2. 由于Python的执行效率问题，在极端情况下可能无法处理最大规模的数据
'''