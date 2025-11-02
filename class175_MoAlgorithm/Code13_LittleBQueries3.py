# 小B的询问 /【模板】莫队
# 给定一个长为n的整数序列a，值域为[1,k]
# m个询问，每个询问给定一个区间[l,r]，求∑(i=1 to k) ci^2
# 其中ci表示数字i在[l,r]中的出现次数
# 1 <= n,m,k <= 5*10^4
# 测试链接 : https://www.luogu.com.cn/problem/P2709

# 解题思路：
# 这是普通莫队的经典模板题
# 关键在于如何维护区间内每种数字出现次数的平方和
# 当添加一个数字时：如果该数字原来出现了x次，现在出现了x+1次
# 那么答案的变化为：(x+1)^2 - x^2 = 2*x + 1
# 当删除一个数字时：如果该数字原来出现了x次，现在出现了x-1次
# 那么答案的变化为：(x-1)^2 - x^2 = -2*x + 1

# 时间复杂度分析：
# 1. 预处理排序：O(m * log m)
# 2. 莫队算法处理：O((n + m) * sqrt(n))
# 3. 总时间复杂度：O(m * log m + (n + m) * sqrt(n))
# 空间复杂度分析：
# 1. 存储原数组：O(n)
# 2. 存储查询：O(m)
# 3. 计数数组：O(k)
# 4. 总空间复杂度：O(n + m + k)

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
    
    # 读取n, m, k
    n = data[idx]
    idx += 1
    m = data[idx]
    idx += 1
    k = data[idx]
    idx += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    for i in range(1, n + 1):
        arr[i] = data[idx]
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
    count = [0] * (k + 1)
    current_answer = 0
    answers = [0] * m
    
    # 莫队算法核心处理
    l = 1
    r = 0
    
    # 添加元素到窗口的函数
    def add(pos):
        nonlocal current_answer
        val = arr[pos]
        # 原来出现了count[val]次，现在出现count[val]+1次
        # 答案变化：(count[val]+1)^2 - count[val]^2 = 2*count[val] + 1
        current_answer += 2 * count[val] + 1
        count[val] += 1
    
    # 从窗口删除元素的函数
    def remove(pos):
        nonlocal current_answer
        val = arr[pos]
        # 原来出现了count[val]次，现在出现count[val]-1次
        # 答案变化：(count[val]-1)^2 - count[val]^2 = -2*count[val] + 1
        current_answer -= 2 * count[val] - 1
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
        
        answers[qid] = current_answer
    
    # 输出结果
    for ans in answers:
        print(ans)

if __name__ == "__main__":
    main()