# 超级洗衣机
# 假设有n台超级洗衣机放在同一排上
# 开始的时候，每台洗衣机内可能有一定量的衣服，也可能是空的
# 在每一步操作中，你可以选择任意 m (1 <= m <= n) 台洗衣机
# 与此同时将每台洗衣机的一件衣服送到相邻的一台洗衣机
# 给定一个整数数组machines代表从左至右每台洗衣机中的衣物数量
# 请给出能让所有洗衣机中剩下的衣物的数量相等的最少的操作步数
# 如果不能使每台洗衣机中衣物的数量相等则返回-1
# 测试链接 : https://leetcode.cn/problems/super-washing-machines/


class Solution:
    """
    超级洗衣机 - 使用贪心算法解决

    算法思路：
    这是一个很有趣的贪心问题。关键在于理解每台洗衣机在达到平衡状态前，
    需要向左或向右输送多少件衣服。

    解题策略：
    1. 首先检查是否能够平均分配衣服，即总衣服数能否被洗衣机台数整除
    2. 计算每台洗衣机最终应该拥有的衣服数量（平均值）
    3. 对于每台洗衣机，计算它需要向左和向右输送的衣服数量
    4. 在每一步中，瓶颈是需要输送衣服数量的最大值

    关键观察：
    - 每台洗衣机可以同时向左右两个方向输送衣服
    - 对于位置i，左侧需要的衣服数量为 leftNeed = i * avg - leftSum
    - 对于位置i，右侧需要的衣服数量为 rightNeed = (n - i - 1) * avg - rightSum
    - 如果左右两侧都需要衣服，则当前洗衣机是瓶颈，需要的步数是 leftNeed + rightNeed
    - 否则，瓶颈是 max(|leftNeed|, |rightNeed|)

    时间复杂度：O(n) - 只需遍历数组一次
    空间复杂度：O(1) - 只使用了常数额外空间

    是否最优解：是。这是该问题的最优解法。

    适用场景：
    1. 资源平衡分配问题
    2. 流量控制问题

    相关题目：
    1. LeetCode 453. 最小操作次数使数组元素相等 - 数组平衡问题
    2. LeetCode 979. 在二叉树中分配硬币 - 树上资源分配
    3. LeetCode 1024. 视频拼接 - 区间拼接问题
    4. LeetCode 1326. 灌溉花园的最少水龙头数目 - 区间覆盖问题
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

    def findMinMoves(self, machines):
        """
        计算使所有洗衣机衣物数量相等的最少操作步数

        Args:
            machines: List[int] - 每台洗衣机中的衣物数量

        Returns:
            int - 最少操作步数，如果无法平均分配则返回-1
        """
        n = len(machines)
        total = sum(machines)
        
        # 检查是否能够平均分配
        if total % n != 0:
            return -1
            
        avg = total // n  # 每台洗衣机最终要求的衣服数量
        left_sum = 0  # 左侧累加和
        ans = 0
        
        for i in range(n):
            left_need = i * avg - left_sum  # 左边还需要多少件衣服
            right_need = (n - i - 1) * avg - (total - left_sum - machines[i])  # 右边还需要多少件衣服
            
            # 计算当前步骤的瓶颈
            if left_need > 0 and right_need > 0:
                # 如果左右都需要衣服，则当前是瓶颈
                bottle_neck = left_need + right_need
            else:
                # 否则瓶颈是左右需求的最大值
                bottle_neck = max(abs(left_need), abs(right_need))
                
            ans = max(ans, bottle_neck)
            left_sum += machines[i]
            
        return ans


# 测试用例
def main():
    solution = Solution()

    # 测试用例1: 基本情况
    machines1 = [1, 0, 5]
    print("输入: [1, 0, 5]")
    print("输出: ", solution.findMinMoves(machines1))
    print("期望: 3\n")

    # 测试用例2: 无法平均分配
    machines2 = [0, 3, 0]
    print("输入: [0, 3, 0]")
    print("输出: ", solution.findMinMoves(machines2))
    print("期望: 2\n")

    # 测试用例3: 已经平均分配
    machines3 = [2, 2, 2]
    print("输入: [2, 2, 2]")
    print("输出: ", solution.findMinMoves(machines3))
    print("期望: 0\n")

    # 测试用例4: 复杂情况
    machines4 = [4, 0, 0, 4]
    print("输入: [4, 0, 0, 4]")
    print("输出: ", solution.findMinMoves(machines4))
    print("期望: 2\n")


if __name__ == "__main__":
    main()