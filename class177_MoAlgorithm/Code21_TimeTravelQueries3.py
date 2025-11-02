# Time Travel Queries - 带修莫队算法实现 (Python版本)
# 题目来源: 模板题 - 带修改的区间查询
# 题目链接: https://www.luogu.com.cn/problem/P1903
# 题目大意: 给定一个数组，支持单点修改和区间查询，每次查询区间[l,r]中有多少不同的元素
# 时间复杂度: O(n^(5/3))，空间复杂度: O(n)

import sys
import math

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    original_arr = [0] * (n + 1)  # 保存初始数组
    for i in range(1, n + 1):
        original_arr[i] = int(input[ptr])
        ptr += 1
    
    # 复制到当前数组
    arr = original_arr.copy()
    
    queries = []
    modifies = []
    query_count = 0
    modify_count = 0
    
    # 处理所有操作
    for _ in range(m):
        op = input[ptr]
        ptr += 1
        if op == 'Q':
            # 查询操作
            l = int(input[ptr])
            r = int(input[ptr + 1])
            queries.append( (l, r, modify_count, query_count) )
            ptr += 2
            query_count += 1
        else:
            # 修改操作
            pos = int(input[ptr])
            value = int(input[ptr + 1])
            modifies.append( (pos, arr[pos], value) )
            arr[pos] = value
            ptr += 2
            modify_count += 1
    
    # 分块 - 带修莫队的块大小通常取n^(2/3)
    block_size = int(math.pow(n, 2/3)) + 1
    bi = [0] * (n + 1)
    for i in range(1, n + 1):
        bi[i] = (i - 1) // block_size
    
    # 查询排序 - 带修莫队排序
    def query_cmp(q):
        l, r, t, idx = q
        if bi[l] != bi[r]:
            return (bi[l], r if bi[l] % 2 == 1 else -r)
        return (bi[l], r if bi[l] % 2 == 1 else -r, t)
    
    queries.sort(key=query_cmp)
    
    # 初始化变量
    cnt = dict()  # 记录每种数值的出现次数
    diff = 0      # 当前区间不同元素的数量
    ans = [0] * query_count
    win_l, win_r, now_t = 1, 0, 0
    
    # 重新初始化数组为初始状态
    arr = original_arr.copy()
    
    # 添加元素到区间
    def add(value):
        nonlocal cnt, diff
        if value in cnt:
            if cnt[value] == 0:
                diff += 1
            cnt[value] += 1
        else:
            cnt[value] = 1
            diff += 1
    
    # 从区间中删除元素
    def delete(value):
        nonlocal cnt, diff
        cnt[value] -= 1
        if cnt[value] == 0:
            diff -= 1
    
    # 应用修改
    def apply_modify(modify):
        pos, pre, now = modify
        # 如果修改的位置在当前窗口内，需要更新窗口内的元素
        if win_l <= pos <= win_r:
            delete(pre)  # 删除旧值
            add(now)     # 添加新值
        # 更新数组中的值
        arr[pos] = now
    
    # 撤销修改
    def undo_modify(modify):
        pos, pre, now = modify
        # 如果修改的位置在当前窗口内，需要更新窗口内的元素
        if win_l <= pos <= win_r:
            delete(now)  # 删除新值
            add(pre)     # 添加旧值
        # 更新数组中的值
        arr[pos] = pre
    
    # 处理每个查询
    for l, r, t, idx in queries:
        # 调整时间戳
        while now_t < t:
            apply_modify(modifies[now_t])
            now_t += 1
        while now_t > t:
            now_t -= 1
            undo_modify(modifies[now_t])
        
        # 移动窗口左右边界
        while win_r < r:
            win_r += 1
            add(arr[win_r])
        while win_l > l:
            win_l -= 1
            add(arr[win_l])
        while win_r > r:
            delete(arr[win_r])
            win_r -= 1
        while win_l < l:
            delete(arr[win_l])
            win_l += 1
        
        # 记录答案
        ans[idx] = diff
    
    # 输出答案
    sys.stdout.write('\n'.join(map(str, ans)) + '\n')

if __name__ == "__main__":
    main()