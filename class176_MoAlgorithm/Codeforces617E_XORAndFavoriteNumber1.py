import sys
import math

"""
Codeforces 617E XOR and Favorite Number
题目链接：https://codeforces.com/contest/617/problem/E

题目描述：
给定一个长度为n的数组arr和一个目标值k，有m次查询。
每次查询给出区间[l,r]，求区间内有多少个连续子数组的异或和等于k。

输入格式：
第一行三个整数n, m, k
第二行n个整数表示数组arr
接下来m行，每行两个整数l, r表示查询区间

输出格式：
对于每个查询，输出一行一个整数表示答案

数据范围：
1 <= n, m <= 100000
0 <= arr[i], k < 1000000

解题思路：
1. 前缀异或数组：使用前缀异或数组xor_sum，其中xor_sum[i]表示前i个元素的异或和
2. 对于子数组[l,r]的异或和为xor_sum[r] ^ xor_sum[l-1]
3. 我们需要统计在区间[l-1,r]中有多少对(i,j)满足xor_sum[j] ^ xor_sum[i] == k，其中i < j
4. 使用莫队算法维护当前区间内各个xor值的出现次数
5. 当扩展区间时，更新计数并累加答案

时间复杂度：O((n + m) * sqrt(n))
空间复杂度：O(n + m)
"""

def main():
    # 读取输入，使用sys.stdin.readline提高输入效率
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    
    # 计算前缀异或数组
    xor_sum = [0] * (n + 1)
    for i in range(1, n + 1):
        arr_val = int(input[ptr])
        ptr += 1
        xor_sum[i] = xor_sum[i - 1] ^ arr_val
    
    # 读取查询
    queries = []
    for i in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        # 转换为前缀异或数组的索引
        queries.append((l - 1, r, i))
    
    # 设置块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 对查询进行排序
    # 按左端点所在块排序，同一块按右端点排序（奇偶优化）
    queries.sort(key=lambda q: (q[0] // block_size, q[1] if (q[0] // block_size) % 2 == 0 else -q[1]))
    
    # 初始化指针、计数器和结果
    cur_l = 0
    cur_r = -1
    res = 0
    cnt = {}  # 使用字典代替数组，更灵活地处理大的异或值
    
    # 存储每个查询的答案
    answers = [0] * m
    
    def update(pos, add):
        nonlocal res
        current_xor = xor_sum[pos]
        if add:
            # 添加一个元素时，增加对应的配对数量
            target = current_xor ^ k
            if target in cnt:
                res += cnt[target]
            # 更新当前异或值的计数
            cnt[current_xor] = cnt.get(current_xor, 0) + 1
        else:
            # 删除一个元素时，先减少计数
            cnt[current_xor] -= 1
            if cnt[current_xor] == 0:
                del cnt[current_xor]
            # 然后减少对应的配对数量
            target = current_xor ^ k
            if target in cnt:
                res -= cnt[target]
    
    # 莫队算法处理
    for q in queries:
        l, r, idx = q
        
        # 扩展或收缩区间
        while cur_l > l:
            cur_l -= 1
            update(cur_l, True)
        while cur_r < r:
            cur_r += 1
            update(cur_r, True)
        while cur_l < l:
            update(cur_l, False)
            cur_l += 1
        while cur_r > r:
            update(cur_r, False)
            cur_r -= 1
        
        # 保存当前查询的答案
        answers[idx] = res
    
    # 输出结果
    sys.stdout.write('\n'.join(map(str, answers)) + '\n')

if __name__ == "__main__":
    main()

'''
算法分析：
时间复杂度：O((n + m) * sqrt(n))
- 排序查询的时间复杂度：O(m * log m)
- 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，总时间为O(n * sqrt(n))
- 整体时间复杂度：O(m * log m + n * sqrt(n))，通常m和n同阶，所以为O((n + m) * sqrt(n))

空间复杂度：O(n + m)
- 前缀异或数组：O(n)
- 查询数组和答案数组：O(m)
- 计数字典：最坏情况下O(n)

优化点：
1. 使用了奇偶优化，减少块间转移的时间
2. 使用sys.stdin.read().split()一次性读取所有输入，提高输入效率
3. 使用字典代替固定大小的数组，避免处理大数据范围的问题

边界情况处理：
1. 查询区间的转换：原问题中的[l,r]对应前缀异或数组的[l-1,r]
2. 使用字典的get方法处理可能不存在的键
3. 当计数减为0时，删除该键以节省空间

工程化考量：
1. 使用nonlocal关键字在内部函数中修改外部函数的变量
2. 一次性读取所有输入并使用指针访问，提高输入效率
3. 使用sys.stdout.write进行批量输出，提高输出效率

调试技巧：
1. 可以在update函数中添加打印语句，检查计数是否正确
2. 测试用例：如n=3, m=1, k=3, arr=[1, 2, 3]，预期结果为2（子数组[1,2]和[3]）
3. 在处理大数据时，Python版本可能需要进一步优化以通过时间限制
'''