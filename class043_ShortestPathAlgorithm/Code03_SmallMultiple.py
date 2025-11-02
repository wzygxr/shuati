# 正整数倍的最小数位和
# 给定一个整数k，求一个k的正整数倍s，使得在十进制下，s的数位累加和最小
# 2 <= k <= 10^5
# 测试链接 : https://www.luogu.com.cn/problem/AT_arc084_b
# 测试链接 : https://atcoder.jp/contests/abc077/tasks/arc084_b

'''
算法思路：
这道题使用01-BFS算法解决。
我们将问题建模为在模k意义下的图上找最短路径。
每个节点i表示当前数字模k的余数为i，边权表示新增数位的值。
有两种操作：
1. 乘以10（相当于在末尾添加0），边权为0
2. 加1（相当于在末尾数位加1），边权为1
从节点1开始搜索，找到到达节点0的最短路径。

时间复杂度：O(k)
空间复杂度：O(k)

题目来源：
1. AtCoder ARC084_B - Small Multiple (https://atcoder.jp/contests/abc077/tasks/arc084_b)
2. AtCoder ABC077_C - Snuke the Wizard

相关题目：
1. Codeforces 176D Wizard in Maze - 迷宫最短路 (https://codeforces.com/problemset/problem/176/D)
2. UVA 11573 Ocean Currents - 海流方向移动 (https://vjudge.net/problem/UVA-11573)
3. SPOJ KATHTHI - 01-BFS模板题 (https://www.spoj.com/problems/KATHTHI/)
4. AtCoder ABC176_D Wizard in Maze (https://atcoder.jp/contests/abc176/tasks/abc176_d)
5. LeetCode 1368 Minimum Cost to Make at Least One Valid Path in a Grid (https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)
6. Codeforces 590C Three States (https://codeforces.com/contest/590/problem/C)
7. LeetCode 2290 Minimum Obstacle Removal to Reach Corner (https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/)
8. LeetCode 1824 Minimum Sideway Jumps (https://leetcode.cn/problems/minimum-sideway-jumps/)
9. LeetCode 773 Sliding Puzzle (https://leetcode.cn/problems/sliding-puzzle/)
10. POJ 3259 Wormholes (http://poj.org/problem?id=3259)
11. HDU 6214 Smallest Minimum Cut (http://acm.hdu.edu.cn/showproblem.php?pid=6214)
12. 洛谷P1429 平面最近点对 (https://www.luogu.com.cn/problem/P1429)
13. 洛谷P2296 寻找道路 (https://www.luogu.com.cn/problem/P2296)
14. 洛谷P2384 道路和航线 (https://www.luogu.com.cn/problem/P2384)
15. 洛谷P2491 逃离僵尸岛 (https://www.luogu.com.cn/problem/P2491)
'''

from collections import deque
import sys

def bfs(k):
    # 使用双端队列实现01-BFS
    dq = deque()
    visited = [False] * k
    
    # 初始状态：余数为1，数位和为1
    dq.appendleft((1, 1))
    
    while dq:
        mod, cost = dq.popleft()
        
        if not visited[mod]:
            visited[mod] = True
            
            if mod == 0:
                return cost
            
            # 两种转移方式：
            # 1. 乘以10（在末尾加0），数位和不变，边权为0
            dq.appendleft(((mod * 10) % k, cost))
            # 2. 加1（末尾数位加1），数位和加1，边权为1
            dq.append(((mod + 1) % k, cost + 1))
    
    return -1

def main():
    k = int(sys.stdin.readline().strip())
    result = bfs(k)
    print(result)

if __name__ == "__main__":
    main()