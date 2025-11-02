import sys
import math
from collections import defaultdict

"""
洛谷 P3604 美好的每一天
题目链接：https://www.luogu.com.cn/problem/P3604

题目描述：
给定一个字符串，查询区间内能重排成回文串的子串个数。

输入格式：
第一行一个字符串s
第二行一个整数m
接下来m行，每行两个整数l, r表示查询区间

输出格式：
对于每个查询，输出一行一个整数表示答案

数据范围：
1 <= |s| <= 60000
1 <= m <= 60000

解题思路：
1. 一个字符串能重排成回文串的条件是：最多有一个字符的出现次数是奇数
2. 使用异或前缀和来记录每个字符的奇偶性（出现偶数次为0，奇数次为1）
3. 对于子串[i+1,j]，如果其对应的异或值xor[j] ^ xor[i]有0或1个1，则可以重排成回文串
4. 使用莫队算法维护当前区间内各个异或值的出现次数
5. 对于每个异或值，统计有多少个其他异或值与其相差不超过1个1位

时间复杂度：O((n + m) * sqrt(n) * 26)
空间复杂度：O(n + 不同异或值的数量)
"""

def main():
    # 读取输入
    input = sys.stdin.read().split()
    ptr = 0
    s = input[ptr]
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    n = len(s)
    
    # 计算前缀异或数组
    xor_sum = [0] * (n + 1)
    for i in range(1, n + 1):
        c = ord(s[i-1]) - ord('a')  # 将字符转换为0-25的数字
        xor_sum[i] = xor_sum[i-1] ^ (1 << c)  # 异或操作记录奇偶性
    
    # 读取查询
    queries = []
    for i in range(m):
        l = int(input[ptr])
        ptr += 1
        r = int(input[ptr])
        ptr += 1
        # 转换为前缀异或数组的索引
        queries.append( (l-1, r, i) )
    
    # 设置块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 对查询进行排序
    # 按左端点所在块排序，同一块按右端点排序（奇偶优化）
    queries.sort(key=lambda q: (q[0] // block_size, q[1] if (q[0] // block_size) % 2 == 0 else -q[1]))
    
    # 初始化指针、计数器和结果
    cur_l = 0
    cur_r = -1
    res = 0
    # 使用defaultdict作为计数字典
    cnt = defaultdict(int)
    cnt[0] = 1  # 初始时xor_sum[0]出现一次
    
    # 存储每个查询的答案
    answers = [0] * m
    
    # 计算与mask相差不超过1位的所有可能的异或值的出现次数之和
    def count(mask):
        current_count = cnt.get(mask, 0)
        for i in range(26):
            current_count += cnt.get(mask ^ (1 << i), 0)
        return current_count
    
    # 更新当前区间的统计信息和答案
    def update(pos, add):
        nonlocal res
        mask = xor_sum[pos]
        if add:
            # 添加一个元素时，先统计可以配对的数量，再增加计数
            res += count(mask)
            cnt[mask] += 1
        else:
            # 删除一个元素时，先减少计数，再减少对应的配对数量
            cnt[mask] -= 1
            if cnt[mask] == 0:
                del cnt[mask]
            res -= count(mask)
    
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
时间复杂度：O((n + m) * sqrt(n) * 26)
- 排序查询的时间复杂度：O(m * log m)
- 莫队算法处理的时间复杂度：每个元素最多被访问O(sqrt(n))次，每次访问需要O(26)的时间进行位操作
- 整体时间复杂度：O(m * log m + n * sqrt(n) * 26)，通常m和n同阶，所以为O((n + m) * sqrt(n) * 26)

空间复杂度：O(n + 不同异或值的数量)
- 前缀异或数组：O(n)
- 查询数组和答案数组：O(m)
- 计数字典：最坏情况下O(n)，但实际空间使用会小于2^26

优化点：
1. 使用了奇偶优化，减少块间转移的时间
2. 使用sys.stdin.read().split()一次性读取所有输入，提高输入效率
3. 通过位运算高效表示字符奇偶性状态
4. 使用defaultdict代替普通字典，简化代码

边界情况处理：
1. 初始时cnt[0] = 1，因为xor_sum[0]本身也是一个前缀异或值
2. 查询区间的转换：原问题中的[l,r]对应前缀异或数组的[l-1,r]
3. 当计数减为0时，删除对应的键，节省空间

工程化考量：
1. 使用nonlocal关键字在内部函数中修改外部函数的变量
2. 一次性读取所有输入并使用指针访问，提高输入效率
3. 使用sys.stdout.write进行批量输出，提高输出效率
4. 使用get方法安全地访问字典中的值

调试技巧：
1. 可以在update函数中添加打印语句，检查计数和答案是否正确
2. 测试用例：如s="abba"，查询[1,4]，预期结果为6（所有子串都可以重排成回文串）
3. 在Python中处理大数据时，可能需要进一步优化以通过时间限制，可以考虑使用更快的输入方法

注意事项：
1. 在Python中，整数溢出不是问题，因为Python支持无限精度整数
2. 由于Python的执行效率问题，对于大规模数据可能会超时，可以考虑以下优化：
   - 使用PyPy运行代码
   - 进一步优化count函数，减少循环次数
   - 避免频繁的字典访问操作
'''