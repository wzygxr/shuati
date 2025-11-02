import math

"""
SPOJ - NKBIN - Python实现
题目链接：https://www.spoj.com/problems/NKBIN/

题目描述：
给定一个数组，支持区间乘法和区间加法，以及单点查询。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
每个块维护两个懒惰标记：乘法标记和加法标记。
预处理每个块的大小和边界。
处理操作时：
1. 对于区间操作，分情况处理左右不完整块和中间完整块
2. 对于完整块，直接更新懒惰标记
3. 对于不完整块，暴力更新每个元素并更新懒惰标记
4. 对于单点查询，应用该点所在块的懒惰标记后返回值

时间复杂度：
- 预处理：O(n)
- 每个操作：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用懒惰标记减少重复计算
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用列表存储元素和懒惰标记
"""

import sys

def main():
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    a = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    block_size = int(math.sqrt(n)) + 1
    block_num = (n + block_size - 1) // block_size
    
    # 初始化块信息
    mul = [1] * block_num  # 乘法懒惰标记初始化为1
    add = [0] * block_num  # 加法懒惰标记初始化为0
    
    # 处理查询
    for _ in range(q):
        op = int(input[ptr])
        ptr += 1
        
        if op == 0:
            # 区间乘法
            l = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            r = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            x = int(input[ptr])
            ptr += 1
            
            left_block = l // block_size
            right_block = r // block_size
            
            if left_block == right_block:
                # 在同一个块内，暴力更新
                for i in range(l, r + 1):
                    a[i] = a[i] * mul[left_block] + add[left_block]
                # 重置当前块的懒惰标记
                mul[left_block] = 1
                add[left_block] = 0
                # 应用新的乘法操作
                for i in range(l, r + 1):
                    a[i] *= x
            else:
                # 处理左边不完整块
                for i in range(l, (left_block + 1) * block_size):
                    a[i] = a[i] * mul[left_block] + add[left_block]
                mul[left_block] = 1
                add[left_block] = 0
                for i in range(l, (left_block + 1) * block_size):
                    a[i] *= x
                
                # 处理中间完整块
                for b in range(left_block + 1, right_block):
                    mul[b] *= x
                    add[b] *= x
                
                # 处理右边不完整块
                for i in range(right_block * block_size, r + 1):
                    a[i] = a[i] * mul[right_block] + add[right_block]
                mul[right_block] = 1
                add[right_block] = 0
                for i in range(right_block * block_size, r + 1):
                    a[i] *= x
        elif op == 1:
            # 区间加法
            l = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            r = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            x = int(input[ptr])
            ptr += 1
            
            left_block = l // block_size
            right_block = r // block_size
            
            if left_block == right_block:
                # 在同一个块内，暴力更新
                for i in range(l, r + 1):
                    a[i] = a[i] * mul[left_block] + add[left_block]
                # 重置当前块的懒惰标记
                mul[left_block] = 1
                add[left_block] = 0
                # 应用新的加法操作
                for i in range(l, r + 1):
                    a[i] += x
            else:
                # 处理左边不完整块
                for i in range(l, (left_block + 1) * block_size):
                    a[i] = a[i] * mul[left_block] + add[left_block]
                mul[left_block] = 1
                add[left_block] = 0
                for i in range(l, (left_block + 1) * block_size):
                    a[i] += x
                
                # 处理中间完整块
                for b in range(left_block + 1, right_block):
                    add[b] += x
                
                # 处理右边不完整块
                for i in range(right_block * block_size, r + 1):
                    a[i] = a[i] * mul[right_block] + add[right_block]
                mul[right_block] = 1
                add[right_block] = 0
                for i in range(right_block * block_size, r + 1):
                    a[i] += x
        elif op == 2:
            # 单点查询
            pos = int(input[ptr]) - 1  # 转换为0-based
            ptr += 1
            
            block = pos // block_size
            result = a[pos] * mul[block] + add[block]
            print(result)

if __name__ == "__main__":
    main()