import collections

'''
LeetCode 1499 满足不等式的最大值
题目来源：LeetCode
网址：https://leetcode.cn/problems/max-value-of-equation/

题目描述：
给你一个数组 points 和一个整数 k。数组中每个元素 points[i] 表示第 i 个点的坐标，
其中 points[i][0] 是 x 坐标，points[i][1] 是 y 坐标。

要求找出满足 j > i 且 |x_j - x_i| <= k 的所有点对 (i, j)，
并返回其中 equation y_i + y_j + |x_i - x_j| 的最大值。

解题思路：
观察等式：y_i + y_j + |x_i - x_j|
由于输入是按x坐标递增排序的，所以对于j > i，有x_j >= x_i，因此|x_i - x_j| = x_j - x_i
等式可以简化为：(y_i - x_i) + (y_j + x_j)

对于每个j，我们需要找到在i < j且x_j - x_i <= k的条件下，最大的(y_i - x_i)
这可以通过单调队列来维护滑动窗口内的最大值

时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
空间复杂度：O(n)，最坏情况下队列中存储所有元素
'''

'''
找出满足条件的点对的最大等式值
参数：
    points: 点坐标数组
    k: 距离限制
返回值：
    最大等式值
'''
def findMaxValueOfEquation(points, k):
    max_value = float('-inf')
    # 单调队列，存储的是索引，按照(y_i - x_i)单调递减排序
    deque = collections.deque()
    
    for j in range(len(points)):
        xj = points[j][0]
        yj = points[j][1]
        
        # 移除不满足xj - xi <= k的元素
        while deque and xj - points[deque[0]][0] > k:
            deque.popleft()
        
        # 如果队列不为空，计算当前的最大值
        if deque:
            i = deque[0]
            max_value = max(max_value, (yj + xj) + (points[i][1] - points[i][0]))
        
        # 维护队列的单调性，确保队列中的元素按照(y_i - x_i)单调递减
        while deque and (points[j][1] - xj) >= (points[deque[-1]][1] - points[deque[-1]][0]):
            deque.pop()
        
        deque.append(j)
    
    return max_value

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    k = int(input[ptr])
    ptr += 1
    points = []
    for _ in range(n):
        x = int(input[ptr])
        ptr += 1
        y = int(input[ptr])
        ptr += 1
        points.append([x, y])
    
    print(findMaxValueOfEquation(points, k))

if __name__ == "__main__":
    main()