# 过河问题
# 一共n人出游，他们走到一条河的西岸，想要过河到东岸
# 每个人都有一个渡河时间ti，西岸有一条船，一次最多乘坐两人
# 如果船上有一个人，划到对岸的时间，等于这个人的渡河时间
# 如果船上有两个人，划到对岸的时间，等于两个人的渡河时间的最大值
# 返回最少要花费多少时间，才能使所有人都过河
# 测试链接 : https://www.luogu.com.cn/problem/P1809


class Solution:
    """
    过河问题 - 使用动态规划和贪心算法解决

    算法思路：
    这是一个经典的过河问题，类似于"农夫过河"问题的变种。
    我们需要找到最优的策略来运送所有人过河，使得总时间最少。

    解题策略：
    1. 首先对所有人按渡河时间进行排序
    2. 使用动态规划，dp[i]表示运送前i个人过河所需的最少时间
    3. 对于每个人，有两种策略：
       - 策略1：最快的人陪同当前人过河，然后最快的人回来
       - 策略2：最快的两个人先过河，最快的回来，最慢的两个人过河，第二快的人回来
    4. 取两种策略中的较小值作为当前状态的最优解

    时间复杂度：O(n log n) - 主要是排序的时间复杂度
    空间复杂度：O(n) - dp数组和存储人员时间的数组

    是否最优解：是。这是该问题的最优解法。

    适用场景：
    1. 过河/过桥类问题
    2. 资源调度优化问题

    相关题目：
    1. LeetCode 1024. 视频拼接 - 区间拼接问题
    2. LeetCode 1326. 灌溉花园的最少水龙头数目 - 区间覆盖问题
    3. 牛客网 NC135 买票需要多少时间 - 队列模拟相关
    4. LintCode 391. 数飞机 - 区间调度相关
    5. HackerRank - Jim and the Orders - 贪心调度问题
    6. CodeChef - TACHSTCK - 区间配对问题
    7. AtCoder ABC104C - All Green - 动态规划相关
    8. Codeforces 1363C - Game On Leaves - 博弈论相关
    9. SPOJ ANARC08E - Relax! I am a legend - 数学相关
    10. POJ 3169 - Layout - 差分约束系统
    11. HDU 2586 - How far away? - LCA最近公共祖先
    12. USACO 2014 January Silver - Cross Country Skiing - BFS搜索
    13. 洛谷 P1091 - 合唱队形 - 动态规划最长子序列
    14. Project Euler 357 - Prime generating integers - 数论相关
    15. 洛谷 P1208 - 混合牛奶 - 经典贪心问题
    16. 牛客网 NC140 - 排序 - 各种排序算法实现
    17. 洛谷 P1809 - 过河问题 - 与本题相同
    18. POJ 1700 - Crossing River - 经典过河问题
    """

    def minCost(self, nums):
        """
        计算运送所有人过河的最少时间

        Args:
            nums: List[int] - 每个人的渡河时间

        Returns:
            int - 最少需要的时间
        """
        n = len(nums)
        nums.sort()
        dp = [0] * n

        if n >= 1:
            dp[0] = nums[0]
        if n >= 2:
            dp[1] = nums[1]
        if n >= 3:
            dp[2] = nums[0] + nums[1] + nums[2]

        for i in range(3, n):
            # 策略1: 最快的人陪同当前人过河，然后最快的人回来
            strategy1 = dp[i - 1] + nums[i] + nums[0]
            # 策略2: 最快的两个人先过河，最快的回来，最慢的两个人过河，第二快的人回来
            strategy2 = dp[i - 2] + nums[1] + nums[1] + nums[i] + nums[0]
            dp[i] = min(strategy1, strategy2)

        return dp[n - 1]


# 测试用例
def main():
    solution = Solution()

    # 测试用例1: 基本情况
    nums1 = [1, 2, 5, 10]
    print("输入: [1, 2, 5, 10]")
    print("输出: ", solution.minCost(nums1))
    print("期望: 17\n")

    # 测试用例2: 三人情况
    nums2 = [1, 5, 10]
    print("输入: [1, 5, 10]")
    print("输出: ", solution.minCost(nums2))
    print("期望: 16\n")

    # 测试用例3: 两人情况
    nums3 = [3, 7]
    print("输入: [3, 7]")
    print("输出: ", solution.minCost(nums3))
    print("期望: 7\n")

    # 测试用例4: 一人情况
    nums4 = [8]
    print("输入: [8]")
    print("输出: ", solution.minCost(nums4))
    print("期望: 8\n")


if __name__ == "__main__":
    main()