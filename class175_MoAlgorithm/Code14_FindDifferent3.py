# 数列找不同
# 现有数列A1,A2,…,AN，Q个询问(Li,Ri)，询问ALi,ALi+1,…,ARi是否互不相同
# 1 <= N,Q <= 10^5
# 1 <= Ai <= N
# 1 <= Li <= Ri <= N
# 测试链接 : https://www.luogu.com.cn/problem/P3901

# 解题思路：
# 这是一个典型的莫队算法应用题
# 我们需要判断区间内是否有重复元素
# 可以维护一个计数器，记录当前窗口内重复元素的个数
# 如果重复元素个数为0，则说明区间内所有元素互不相同，输出"Yes"
# 否则输出"No"

# 时间复杂度分析：
# 1. 预处理排序：O(Q * log Q)
# 2. 莫队算法处理：O((N + Q) * sqrt(N))
# 3. 总时间复杂度：O(Q * log Q + (N + Q) * sqrt(N))
# 空间复杂度分析：
# 1. 存储原数组：O(N)
# 2. 存储查询：O(Q)
# 3. 计数数组：O(N)
# 4. 总空间复杂度：O(N + Q)

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
    
    # 读取n, q
    n = data[idx]
    idx += 1
    q = data[idx]
    idx += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        arr[i] = data[idx]
        idx += 1
    
    # 读取查询
    queries = []
    for i in range(q):
        l = data[idx]
        idx += 1
        r = data[idx]
        idx += 1
        queries.append((l, r, i))
    
    # 计算分块大小
    block_size = int(math.sqrt(n))
    
    # 为查询添加块信息并排序
    for i in range(q):
        queries[i] = (queries[i][0], queries[i][1], queries[i][2], (queries[i][0] - 1) // block_size)
    
    # 按照莫队算法的排序规则排序
    queries.sort(key=lambda x: (x[3], x[1]))
    
    # 初始化计数数组和答案
    count = [0] * (n + 1)
    duplicate_count = 0
    answers = [""] * q
    
    # 莫队算法核心处理
    l = 1
    r = 0
    
    # 添加元素到窗口的函数
    def add(pos):
        nonlocal duplicate_count
        val = arr[pos]
        # 如果该数字之前已经出现过一次，现在变成重复了
        if count[val] == 1:
            duplicate_count += 1
        count[val] += 1
    
    # 从窗口删除元素的函数
    def remove(pos):
        nonlocal duplicate_count
        val = arr[pos]
        # 如果该数字之前出现了多次，现在变成只出现一次了
        if count[val] == 2:
            duplicate_count -= 1
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
        
        # 如果没有重复元素，则区间内所有元素互不相同
        answers[qid] = "Yes" if duplicate_count == 0 else "No"
    
    # 输出结果
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()