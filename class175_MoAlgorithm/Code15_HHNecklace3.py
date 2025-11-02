# HH的项链
# 给定一个长度为n的正整数序列，m次询问，每次询问一个区间内不同数字的种类数
# 1 <= n,m,ai <= 10^6
# 测试链接 : https://www.luogu.com.cn/problem/P1972

# 解题思路：
# 这是莫队算法的经典应用题
# 我们需要维护区间内不同数字的种类数
# 关键在于如何处理数字的添加和删除操作
# 对于每个数字，我们只关心它是否在当前窗口中出现过
# 可以使用计数数组记录每个数字的出现次数，用一个变量记录不同数字的种类数

# 时间复杂度分析：
# 1. 预处理排序：O(m * log m)
# 2. 莫队算法处理：O((n + m) * sqrt(n))
# 3. 总时间复杂度：O(m * log m + (n + m) * sqrt(n))
# 空间复杂度分析：
# 1. 存储原数组：O(n)
# 2. 存储查询：O(m)
# 3. 计数数组：O(max(ai)) = O(10^6)
# 4. 总空间复杂度：O(n + m + 10^6)

# 是否最优解：
# 这是该问题的最优解之一，莫队算法在处理这类离线区间查询问题时具有很好的时间复杂度
# 对于在线查询问题，可以使用主席树等数据结构，但对于离线问题，莫队算法是首选

import sys
import math

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    # 读取所有输入
    data = list(map(int, input().split()))
    idx = 0
    
    # 读取n
    n = data[idx]
    idx += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        arr[i] = data[idx]
        idx += 1
    
    # 读取m
    m = data[idx]
    idx += 1
    
    # 读取查询
    queries = []
    for i in range(m):
        l = data[idx]
        idx += 1
        r = data[idx]
        idx += 1
        queries.append((l, r, i))
    
    # 计算分块大小
    block_size = int(math.sqrt(n))
    
    # 为查询添加块信息并排序
    for i in range(m):
        queries[i] = (queries[i][0], queries[i][1], queries[i][2], (queries[i][0] - 1) // block_size)
    
    # 按照莫队算法的排序规则排序
    queries.sort(key=lambda x: (x[3], x[1]))
    
    # 初始化计数数组和答案
    count = [0] * (1000010)  # 足够大的数组
    distinct_count = 0
    answers = [0] * m
    
    # 莫队算法核心处理
    l = 1
    r = 0
    
    # 添加元素到窗口的函数
    def add(pos):
        nonlocal distinct_count
        val = arr[pos]
        # 如果该数字之前没有出现过，现在出现了，种类数增加
        if count[val] == 0:
            distinct_count += 1
        count[val] += 1
    
    # 从窗口删除元素的函数
    def remove(pos):
        nonlocal distinct_count
        val = arr[pos]
        # 如果该数字之前只出现了一次，现在删除后就没有了，种类数减少
        if count[val] == 1:
            distinct_count -= 1
        count[val] -= 1
    
    # 处理每个查询
    for ql, qr, qid, _ in queries:
        # 调整窗口边界
        while r < qr:
            r += 1
            add(r)
        while r > qr:
            remove(r)
            r -= 1
        while l < ql:
            remove(l)
            l += 1
        while l > ql:
            l -= 1
            add(l)
        
        answers[qid] = distinct_count
    
    # 输出结果
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()