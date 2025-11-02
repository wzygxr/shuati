import collections

'''
洛谷P1725 琪露诺问题
题目来源：洛谷 P1725 琪露诺
网址：https://www.luogu.com.cn/problem/P1725

题目描述：
在幻想乡，琪露诺是以笨蛋闻名的冰之妖精。一天，琪露诺又在玩速冻青蛙，
她的魔法可以在地面上形成一个冰之阶梯，用来跳跃。每次跳跃的时候，
琪露诺会消耗一点魔法，然后她可以从当前的格子跳到前面任意一个格子，
前提是这两个格子之间的高度差不超过一个给定的值d。

问题转化为：在给定数组中，找到从位置0到位置n的一条路径，
每次可以向右跳到位置i（i > 当前位置），且满足abs(v[i] - v[j]) <= d，
其中j是当前位置。要求路径长度（跳跃次数）的最小值。

解题思路：
这是一个典型的单调队列优化动态规划问题。
- 状态定义：dp[i] 表示到达位置i所需要的最少跳跃次数
- 状态转移方程：dp[i] = min(dp[j]) + 1，其中 j 满足 i - r <= j <= i - l
- 使用单调队列维护滑动窗口内的最小值

时间复杂度：O(n)，每个元素最多被加入和弹出队列各一次
空间复杂度：O(n)，需要dp数组和单调队列
'''

'''
解决琪露诺跳跃问题
参数：
    n: 总格子数
    l: 最小跳跃距离
    r: 最大跳跃距离
    v: 每个格子的高度值
返回值：
    到达终点的最少跳跃次数，不可达返回-1
'''
def solve(n, l, r, v):
    # dp[i]表示到达位置i的最少跳跃次数
    dp = [float('inf')] * (n + 1)
    dp[0] = 0
    
    # 单调队列，保存的是索引，按照dp值单调递增
    deque = collections.deque()
    deque.append(0)
    
    # 遍历每个位置i
    for i in range(1, n + 1):
        # 移除队列中不在有效范围的元素（i - r <= j）
        while deque and deque[0] < i - r:
            deque.popleft()
        
        # 如果队列不为空，当前dp[i]可以由队列头部的元素转移而来
        if deque:
            dp[i] = dp[deque[0]] + 1
        
        # 当i >= l时，i可以作为后续位置的转移点
        if i >= l:
            # 维护队列的单调性，移除队列尾部dp值大于等于dp[i]的元素
            while deque and dp[i] <= dp[deque[-1]]:
                deque.pop()
            deque.append(i)
    
    # 如果终点不可达，返回-1
    return dp[n] if dp[n] != float('inf') else -1

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    n = int(input[ptr])
    ptr += 1
    l = int(input[ptr])
    ptr += 1
    r = int(input[ptr])
    ptr += 1
    v = []
    for _ in range(n + 1):
        v.append(int(input[ptr]))
        ptr += 1
    
    print(solve(n, l, r, v))

if __name__ == "__main__":
    main()