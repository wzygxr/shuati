# 数颜色 / 维护队列 /【模板】带修莫队
# 给定一个大小为N的数组arr，有两种操作：
# 1. Q L R 代表询问从第L支画笔到第R支画笔中共有几种不同颜色的画笔
# 2. R P C 把第P支画笔替换为颜色C
# 1 <= N,M <= 133333
# 1 <= arr[i],C <= 10^6
# 测试链接 : https://www.luogu.com.cn/problem/P1903

# 解题思路：
# 这是带修莫队的经典模板题
# 带修莫队是普通莫队的扩展，支持修改操作
# 在普通莫队的基础上，引入时间维度，排序规则增加时间关键字
# 排序规则：
# 1. 按照左端点所在块编号排序
# 2. 如果左端点在同一块内，按照右端点所在块编号排序
# 3. 如果右端点也在同一块内，按照时间排序

# 时间复杂度分析：
# 1. 预处理排序：O((Q + M) * log(Q + M))
# 2. 带修莫队算法处理：O((N + Q + M) * N^(2/3))
# 3. 总时间复杂度：O((Q + M) * log(Q + M) + (N + Q + M) * N^(2/3))
# 空间复杂度分析：
# 1. 存储原数组：O(N)
# 2. 存储查询和修改操作：O(Q + M)
# 3. 计数数组：O(max(arr[i], C))
# 4. 总空间复杂度：O(N + Q + M + max(arr[i], C))

# 是否最优解：
# 这是该问题的最优解之一，带修莫队算法在处理这类支持修改的离线区间查询问题时具有很好的时间复杂度
# 对于在线查询问题，可以使用树状数组套主席树等数据结构，但对于离线问题，带修莫队算法是首选

import sys
import math

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    # 读取所有输入
    lines = []
    for line in sys.stdin:
        lines.append(line)
    
    # 解析输入
    data = lines[0].split()
    idx = 0
    
    # 读取n, m
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    # 读取数组
    arr = [0] * (n + 1)  # 1-indexed
    data2 = lines[1].split()
    for i in range(1, n + 1):
        arr[i] = int(data2[i-1])
    
    # 读取操作
    queries = []  # 查询操作：类型1，l, r, 时间戳, 查询编号
    updates = []  # 修改操作：类型2，位置, 新值, 旧值, 时间戳
    query_count = 0
    update_count = 0
    
    # 处理操作
    for i in range(2, 2 + m):
        parts = lines[i].split()
        if parts[0] == 'Q':
            # 查询操作
            query_count += 1
            l = int(parts[1])
            r = int(parts[2])
            queries.append([1, l, r, update_count, query_count])
        else:
            # 修改操作
            update_count += 1
            pos = int(parts[1])
            new_val = int(parts[2])
            old_val = arr[pos]
            updates.append([2, pos, new_val, old_val])
    
    # 计算分块大小，带修莫队通常选择 N^(2/3)
    block_size = int(math.pow(n, 2.0/3.0))
    
    # 计算每个位置所属的块
    belong = [0] * (n + 1)
    for i in range(1, n + 1):
        belong[i] = (i - 1) // block_size + 1
    
    # 为查询添加块信息
    for i in range(len(queries)):
        queries[i].append(belong[queries[i][1]])  # 左端点所在块
        queries[i].append(belong[queries[i][2]])  # 右端点所在块
    
    # 按照带修莫队的排序规则排序
    # 1. 按照左端点所在块排序
    # 2. 如果左端点在同一块内，按照右端点所在块排序
    # 3. 如果右端点也在同一块内，按照时间排序
    queries.sort(key=lambda x: (x[5], x[6], x[3]))
    
    # 初始化计数数组和答案
    count = [0] * (1000010)  # 足够大的数组
    distinct_count = 0
    answers = [0] * (query_count + 1)
    
    # 带修莫队算法核心处理
    l = 1
    r = 0
    now = 0
    
    # 添加元素到窗口的函数
    def add(pos):
        nonlocal distinct_count
        val = arr[pos]
        # 如果该颜色之前没有出现过，现在出现了，种类数增加
        if count[val] == 0:
            distinct_count += 1
        count[val] += 1
    
    # 从窗口删除元素的函数
    def remove(pos):
        nonlocal distinct_count
        val = arr[pos]
        # 如果该颜色之前只出现了一次，现在删除后就没有了，种类数减少
        if count[val] == 1:
            distinct_count -= 1
        count[val] -= 1
    
    # 应用修改操作
    def apply_update(time):
        nonlocal distinct_count
        pos = updates[time-1][1]
        new_val = updates[time-1][2]
        old_val = updates[time-1][3]
        
        # 如果该位置在当前窗口内，需要更新答案
        if pos >= l and pos <= r:
            # 删除旧值的影响
            if count[old_val] == 1:
                distinct_count -= 1
            count[old_val] -= 1
            
            # 添加新值的影响
            if count[new_val] == 0:
                distinct_count += 1
            count[new_val] += 1
        
        # 更新数组
        arr[pos] = new_val
    
    # 撤销修改操作
    def undo_update(time):
        nonlocal distinct_count
        pos = updates[time-1][1]
        new_val = updates[time-1][2]
        old_val = updates[time-1][3]
        
        # 如果该位置在当前窗口内，需要更新答案
        if pos >= l and pos <= r:
            # 删除新值的影响
            if count[new_val] == 1:
                distinct_count -= 1
            count[new_val] -= 1
            
            # 添加旧值的影响
            if count[old_val] == 0:
                distinct_count += 1
            count[old_val] += 1
        
        # 更新数组
        arr[pos] = old_val
    
    # 处理每个查询
    for query in queries:
        ql = query[1]
        qr = query[2]
        qt = query[3]
        qid = query[4]
        
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
        
        # 调整时间戳
        while now < qt:
            now += 1
            apply_update(now)
        while now > qt:
            undo_update(now)
            now -= 1
        
        answers[qid] = distinct_count
    
    # 输出结果
    for i in range(1, query_count + 1):
        print(answers[i])

if __name__ == "__main__":
    main()