import sys
import math

"""
Codeforces 86D Powerful array
题目链接：https://codeforces.com/problemset/problem/86/D

题目描述：
给定一个数组a[1...n]，有m次查询。每次查询[l,r]区间内，
每种数字出现次数的平方和乘以该数字的值的总和。

输入格式：
第一行两个整数n和m，表示数组长度和查询次数。
第二行n个整数表示数组元素。
接下来m行，每行两个整数l, r表示查询区间。

输出格式：
对于每个查询，输出一行一个整数表示答案。

数据范围：
1 <= n, m <= 200000
1 <= a[i] <= 10^6

解题思路：
1. 使用莫队算法处理区间查询
2. 维护当前区间内每个数字的出现次数cnt[x]
3. 维护当前答案ans，当添加或删除一个元素x时，更新ans：
   - 添加x：ans += x * (2 * cnt[x] + 1)，然后cnt[x]++
   - 删除x：cnt[x]--，然后ans -= x * (2 * cnt[x] + 1)
4. 使用分块排序优化莫队算法的效率

时间复杂度：O((n + m) * sqrt(n))
空间复杂度：O(n + m + max_value)
"""

def main():
    # 读取输入，使用sys.stdin.readline提高输入效率
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        arr[i] = int(input[ptr])
        ptr += 1
    
    # 读取查询
    queries = []
    for i in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        queries.append((l, r, i))
    
    # 设置块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 对查询进行排序
    # 按左端点所在块排序，同一块按右端点排序（奇偶优化）
    queries.sort(key=lambda q: (q[0] // block_size, q[1] if (q[0] // block_size) % 2 == 0 else -q[1]))
    
    # 初始化指针、计数器和结果
    cur_l = 1
    cur_r = 0
    res = 0
    # 使用字典代替数组，因为数组元素值可能很大
    cnt = {}
    
    # 存储每个查询的答案
    answers = [0] * m
    
    def update(pos, add):
        nonlocal res
        x = arr[pos]
        if add:
            # 添加一个元素x，先更新答案，再增加计数
            current_count = cnt.get(x, 0)
            res += x * (2 * current_count + 1)
            cnt[x] = current_count + 1
        else:
            # 删除一个元素x，先减少计数，再更新答案
            current_count = cnt[x]
            cnt[x] = current_count - 1
            if cnt[x] == 0:
                del cnt[x]
            res -= x * (2 * (current_count - 1) + 1)
    
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
- 数组存储：O(n)
- 查询数组和答案数组：O(m)
- 计数字典：最坏情况下O(n)

优化点：
1. 使用了奇偶优化，减少块间转移的时间
2. 使用sys.stdin.read().split()一次性读取所有输入，提高输入效率
3. 使用字典代替固定大小的数组，避免处理大数据范围的问题

边界情况处理：
1. 使用字典的get方法处理可能不存在的键
2. 当计数减为0时，删除该键以节省空间
3. 确保使用长整型来存储结果，避免溢出

工程化考量：
1. 使用nonlocal关键字在内部函数中修改外部函数的变量
2. 一次性读取所有输入并使用指针访问，提高输入效率
3. 使用sys.stdout.write进行批量输出，提高输出效率

调试技巧：
1. 可以在update函数中添加打印语句，检查计数和答案是否正确
2. 测试用例：如n=3, m=1, arr=[1, 2, 1]，查询[1,3]，预期结果为1*2^2 + 2*1^2 = 4 + 2 = 6
3. 在Python中处理大数据时，可能需要进一步优化以通过时间限制，可以考虑使用更快的输入方法
'''