import collections

'''
USACO 挤奶牛 (Crowded Cows)
题目来源：USACO

题目描述：
在一条直线上有N头奶牛，每头奶牛的位置为x[i]，权值为v[i]。
一头奶牛被认为是"拥挤的"，当且仅当它左边有至少一头奶牛，右边也有至少一头奶牛，
且左边那头发的权值不小于它，右边那头发的权值也不小于它。
求有多少头奶牛是拥挤的。

解题思路：
这是一个典型的滑动窗口最大值问题，使用单调队列优化。
- 我们需要找出每个位置i左边窗口内的最大值和右边窗口内的最大值
- 对于每个位置i，如果左边最大值 >= v[i] 且右边最大值 >= v[i]，则这头奶牛是拥挤的
- 使用单调队列维护滑动窗口中的最大值

时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
空间复杂度：O(n)，需要存储最大值数组和单调队列
'''

'''
解决挤奶牛问题
参数：
    n: 奶牛数量
    d: 窗口宽度
    positions: 奶牛的位置数组
    values: 奶牛的权值数组
返回值：
    拥挤的奶牛数量
'''
def solve(n, d, positions, values):
    # 记录每个位置右边窗口内的最大值
    right_max = [-1] * n
    
    # 从右到左遍历，使用单调队列维护窗口内的最大值
    deque = collections.deque()
    for i in range(n-1, -1, -1):
        # 移除队列中位置超出窗口的元素（x[j] > x[i] + d）
        while deque and positions[deque[0]] > positions[i] + d:
            deque.popleft()
        
        # 如果队列不为空，当前位置的右边最大值就是队列头部的元素
        if deque:
            right_max[i] = values[deque[0]]
        
        # 维护队列的单调性，移除队列尾部小于等于当前元素的值
        while deque and values[i] >= values[deque[-1]]:
            deque.pop()
        deque.append(i)
    
    # 统计拥挤的奶牛数量
    count = 0
    deque.clear()
    
    # 从左到右遍历，使用单调队列维护窗口内的最大值
    for i in range(n):
        # 移除队列中位置超出窗口的元素（x[j] < x[i] - d）
        while deque and positions[deque[0]] < positions[i] - d:
            deque.popleft()
        
        # 如果左边有最大值且右边有最大值，并且都大于等于当前值，则是拥挤的奶牛
        if deque and right_max[i] != -1 and values[deque[0]] >= values[i] and right_max[i] >= values[i]:
            count += 1
        
        # 维护队列的单调性，移除队列尾部小于等于当前元素的值
        while deque and values[i] >= values[deque[-1]]:
            deque.pop()
        deque.append(i)
    
    return count

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    d = int(input[ptr])
    ptr += 1
    cows = []
    for _ in range(n):
        x = int(input[ptr])
        ptr += 1
        v = int(input[ptr])
        ptr += 1
        cows.append((x, v))
    
    # 按照位置排序
    cows.sort()
    
    positions = [cow[0] for cow in cows]
    values = [cow[1] for cow in cows]
    
    print(solve(n, d, positions, values))

if __name__ == "__main__":
    main()