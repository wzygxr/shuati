# 灌溉花园的最少水龙头数目
# 在x轴上有一个一维的花园，花园长度为n，从点0开始，到点n结束
# 花园里总共有 n + 1 个水龙头，分别位于[0, 1, ... n]
# 给你一个整数n和一个长度为n+1的整数数组ranges
# 其中ranges[i]表示
# 如果打开点i处的水龙头，可以灌溉的区域为[i-ranges[i], i+ranges[i]]
# 请你返回可以灌溉整个花园的最少水龙头数目
# 如果花园始终存在无法灌溉到的地方请你返回-1
# 测试链接 : https://leetcode.cn/problems/minimum-number-of-taps-to-open-to-water-a-garden/

class Solution:
    """
    灌溉花园的最少水龙头数目 - 使用贪心算法解决

    算法思路：
    这是一个经典的区间覆盖问题，可以转化为跳跃游戏的变种。
    1. 首先预处理ranges数组，构造right数组，其中right[i]表示以位置i为起点，
       能够覆盖到的最远右边界。
    2. 使用贪心策略，维护两个变量：
       - cur: 当前水龙头能覆盖到的最远位置
       - next: 下一个水龙头能覆盖到的最远位置
       - ans: 打开水龙头的数量
    3. 遍历位置0到n-1，当当前位置超过当前水龙头能覆盖的范围时，
       就需要打开下一个水龙头，并更新相关变量。

    时间复杂度：O(n) - 只需遍历数组一次
    空间复杂度：O(n) - 需要额外的right数组

    是否最优解：是。这是该问题的最优解法之一。

    适用场景：
    1. 区间覆盖问题
    2. 最少资源选择问题

    相关题目：
    1. LeetCode 45. 跳跃游戏 II - 经典跳跃游戏
    2. LeetCode 55. 跳跃游戏 - 判断是否能到达终点
    3. LeetCode 1024. 视频拼接 - 区间拼接问题
    4. LeetCode 1326. 灌溉花园的最少水龙头数目 - 与本题相同
    5. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
    6. LintCode 391. 数飞机 - 区间调度相关
    7. HackerRank - Jim and the Orders - 贪心调度问题
    8. CodeChef - TACHSTCK - 区间配对问题
    9. AtCoder ABC104C - All Green - 动态规划相关
    10. Codeforces 1363C - Game On Leaves - 博弈论相关
    11. SPOJ ANARC08E - Relax! I am a legend - 数学相关
    12. POJ 3169 - Layout - 差分约束系统
    13. HDU 2586 - How far away? - LCA最近公共祖先
    14. USACO 2014 January Silver - Cross Country Skiing - BFS搜索
    15. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
    16. Project Euler 357 - Prime generating integers - 数论相关
    17. 洛谷 P1208 - 混合牛奶 - 经典贪心问题
    18. 牛客网 NC140 - 排序 - 各种排序算法实现
    """

    def minTaps(self, n, ranges):
        """
        计算灌溉整个花园所需的最少水龙头数目

        Args:
            n: int - 花园长度
            ranges: List[int] - 每个水龙头的灌溉范围

        Returns:
            int - 最少水龙头数目，如果无法完全灌溉则返回-1
        """
        # right[i] = j
        # 所有左边界在i的水龙头里，影响到的最右右边界是j
        right = [0] * (n + 1)
        for i in range(n + 1):
            start = max(0, i - ranges[i])
            right[start] = max(right[start], i + ranges[i])

        # 当前ans数量的水龙头打开，影响到的最右右边界
        cur = 0
        # 如果再多打开一个水龙头，影响到的最右边界
        next_pos = 0
        # 打开水龙头的数量
        ans = 0

        for i in range(n):
            # 来到i位置
            # 先更新下一步的next
            next_pos = max(next_pos, right[i])
            if i == cur:
                if next_pos > i:
                    cur = next_pos
                    ans += 1
                else:
                    return -1

        return ans


# 测试用例
def main():
    solution = Solution()

    # 测试用例1: 基本情况
    n1 = 5
    ranges1 = [3, 4, 1, 1, 0, 0]
    print("输入: n = " + str(n1) + ", ranges = [3,4,1,1,0,0]")
    print("输出: " + str(solution.minTaps(n1, ranges1)))
    print("期望: 1\n")

    # 测试用例2: 需要多个水龙头
    n2 = 3
    ranges2 = [0, 0, 0, 0]
    print("输入: n = " + str(n2) + ", ranges = [0,0,0,0]")
    print("输出: " + str(solution.minTaps(n2, ranges2)))
    print("期望: -1\n")

    # 测试用例3: 精确覆盖
    n3 = 7
    ranges3 = [1, 2, 1, 0, 2, 1, 0, 1]
    print("输入: n = " + str(n3) + ", ranges = [1,2,1,0,2,1,0,1]")
    print("输出: " + str(solution.minTaps(n3, ranges3)))
    print("期望: 3\n")

    # 测试用例4: 单个水龙头覆盖全部
    n4 = 8
    ranges4 = [4, 0, 0, 0, 0, 0, 0, 0, 4]
    print("输入: n = " + str(n4) + ", ranges = [4,0,0,0,0,0,0,0,4]")
    print("输出: " + str(solution.minTaps(n4, ranges4)))
    print("期望: 2\n")


if __name__ == "__main__":
    main()