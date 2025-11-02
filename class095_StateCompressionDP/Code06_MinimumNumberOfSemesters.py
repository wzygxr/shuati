# 并行课程 II (Parallel Courses II)
# 给定n门课程，编号从1到n，以及一个数组relations，其中relations[i] = [prevCourse, nextCourse]，
# 表示课程prevCourse必须在课程nextCourse之前修读。
# 在一个学期中，你可以学习最多k门课程，前提是这些课程的先修课程已经在之前的学期中修读。
# 返回上完所有课程所需的最少学期数。
# 测试链接 : https://leetcode.cn/problems/parallel-courses-ii/

class Code06_MinimumNumberOfSemesters:
    
    # 使用状态压缩动态规划解决并行课程问题
    # 核心思想：用二进制位表示已修课程的集合，通过状态转移找到最少学期数
    # 时间复杂度: O(3^n + n * 2^n)
    # 空间复杂度: O(2^n)
    @staticmethod
    def minNumberOfSemesters(n, relations, k):
        # pre[i] 表示课程i+1的先修课程集合（用二进制位表示）
        pre = [0] * n
        for relation in relations:
            # 将先修课程添加到课程的先修集合中
            pre[relation[1] - 1] |= 1 << (relation[0] - 1)
        
        # dp[mask] 表示完成mask代表的课程集合所需的最少学期数
        dp = [n] * (1 << n)  # 最多需要n个学期（每学期学一门课）
        # 初始状态：不需要学习任何课程，需要0个学期
        dp[0] = 0
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << n):
            # 如果当前状态不可达，跳过
            if dp[mask] == n:
                continue
            
            # available表示在当前状态下可以学习的课程集合
            available = 0
            for i in range(n):
                # 如果课程i+1还未学习，且其所有先修课程都已学习，则可以学习
                if (mask & (1 << i)) == 0 and (mask & pre[i]) == pre[i]:
                    available |= 1 << i
            
            # 枚举available的所有非空子集（表示这学期要学习的课程）
            subset = available
            while subset > 0:
                # 如果子集中的课程数量不超过k，则可以这学期学习
                if bin(subset).count('1') <= k:
                    # 更新状态：完成mask+subset集合所需的学期数
                    new_mask = mask | subset
                    dp[new_mask] = min(dp[new_mask], dp[mask] + 1)
                # 枚举下一个子集
                subset = (subset - 1) & available
        
        # 返回完成所有课程所需的最少学期数
        return dp[(1 << n) - 1]