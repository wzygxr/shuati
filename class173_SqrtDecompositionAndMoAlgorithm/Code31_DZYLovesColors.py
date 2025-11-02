# DZY Loves Colors - 分块算法实现 (Python版本)
# 题目来源: http://codeforces.com/contest/444/problem/C
# 题目大意: 给定一个长度为n的数组，初始时每个位置i的颜色为i，颜色变化量为0
# 支持两种操作：
# 1. 将区间[l, r]的所有位置染成颜色x，并累加每个位置的颜色变化量为|原来的颜色 - x|
# 2. 查询区间[l, r]的颜色变化量总和
# 约束条件: 1 <= n, m <= 1e5

import sys
import math

def main():
    sys.setrecursionlimit(1 << 25)
    n, m = map(int, sys.stdin.readline().split())
    blen = int(math.sqrt(n))
    if blen == 0:
        blen = 1  # 避免除零错误
    
    block_count = (n + blen - 1) // blen
    
    # 初始化数组和标记
    arr = [0] * (n + 2)  # 1-based索引
    sum_ = [0] * (n + 2)  # 存储颜色变化量，避免与Python内置函数sum冲突
    tag = [0] * (block_count + 2)  # 块的整体颜色变化量标记
    color_tag = [-1] * (block_count + 2)  # -1表示块内颜色不一致
    
    for i in range(1, n + 1):
        arr[i] = i  # 初始颜色为位置i
        sum_[i] = 0  # 初始颜色变化量为0
    
    # 初始时检查每个块是否颜色一致
    for i in range(1, block_count + 1):
        same = True
        first = arr[(i - 1) * blen + 1]
        for j in range((i - 1) * blen + 2, min(i * blen, n) + 1):
            if arr[j] != first:
                same = False
                break
        if same:
            color_tag[i] = first
    
    # 查询函数
    def query(l, r):
        ans = 0
        L = (l - 1) // blen + 1
        R = (r - 1) // blen + 1
        
        if L == R:
            for i in range(l, r + 1):
                ans += sum_[i] + tag[L]
            return ans
        
        # 左边不完整块
        for i in range(l, L * blen + 1):
            ans += sum_[i] + tag[L]
        
        # 中间完整块
        for i in range(L + 1, R):
            ans += tag[i] * blen
        
        # 右边不完整块
        for i in range((R - 1) * blen + 1, r + 1):
            ans += sum_[i] + tag[R]
        
        return ans
    
    # 更新函数
    def update(l, r, x):
        L = (l - 1) // blen + 1
        R = (r - 1) // blen + 1
        
        if L == R:
            # 如果当前块有颜色标记
            if color_tag[L] != -1:
                old_color = color_tag[L]
                # 展开颜色
                for i in range((L - 1) * blen + 1, min(L * blen, n) + 1):
                    arr[i] = old_color
                color_tag[L] = -1
            
            # 暴力更新
            for i in range(l, r + 1):
                sum_[i] += abs(arr[i] - x)
                arr[i] = x
            
            # 检查是否颜色一致
            same = True
            first = arr[(L - 1) * blen + 1]
            for i in range((L - 1) * blen + 2, min(L * blen, n) + 1):
                if arr[i] != first:
                    same = False
                    break
            if same:
                color_tag[L] = first
            return
        
        # 处理左边不完整块
        if color_tag[L] != -1:
            old_color = color_tag[L]
            for i in range((L - 1) * blen + 1, L * blen + 1):
                arr[i] = old_color
            color_tag[L] = -1
        
        for i in range(l, L * blen + 1):
            sum_[i] += abs(arr[i] - x)
            arr[i] = x
        
        # 检查左边块是否一致
        same_L = True
        first_L = arr[(L - 1) * blen + 1]
        for i in range((L - 1) * blen + 2, L * blen + 1):
            if arr[i] != first_L:
                same_L = False
                break
        if same_L:
            color_tag[L] = first_L
        
        # 处理中间完整块
        for i in range(L + 1, R):
            if color_tag[i] != -1:
                # 块内颜色相同
                tag[i] += abs(color_tag[i] - x)
                color_tag[i] = x
            else:
                # 块内颜色不同，暴力处理
                for j in range((i - 1) * blen + 1, min(i * blen, n) + 1):
                    sum_[j] += tag[i]
                    sum_[j] += abs(arr[j] - x)
                    arr[j] = x
                tag[i] = 0
                color_tag[i] = x
        
        # 处理右边不完整块
        if color_tag[R] != -1:
            old_color = color_tag[R]
            for i in range((R - 1) * blen + 1, min(R * blen, n) + 1):
                arr[i] = old_color
            color_tag[R] = -1
        
        for i in range((R - 1) * blen + 1, r + 1):
            sum_[i] += abs(arr[i] - x)
            arr[i] = x
        
        # 检查右边块是否一致
        same_R = True
        first_R = arr[(R - 1) * blen + 1]
        for i in range((R - 1) * blen + 2, min(R * blen, n) + 1):
            if arr[i] != first_R:
                same_R = False
                break
        if same_R:
            color_tag[R] = first_R
    
    # 处理操作
    for _ in range(m):
        parts = sys.stdin.readline().split()
        op = int(parts[0])
        if op == 1:
            l = int(parts[1])
            r = int(parts[2])
            x = int(parts[3])
            update(l, r, x)
        else:
            l = int(parts[1])
            r = int(parts[2])
            print(query(l, r))

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 预处理：O(n)
- 更新操作：O(√n) 每个完整块O(1)，不完整块O(√n)
- 查询操作：O(√n) 每个完整块O(1)，不完整块O(√n)
- 总体时间复杂度：O(m√n)，其中m为操作次数

空间复杂度分析：
- 数组arr：O(n)
- 数组sum_：O(n)
- 数组tag：O(√n)
- 数组color_tag：O(√n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用sys.stdin.readline()来提高输入效率
2. 函数名sum_避免与Python内置函数sum冲突
3. 注意Python中列表的索引处理，这里使用1-based索引
4. 对于大规模数据，Python版本可能会遇到性能问题，需要注意优化
'''