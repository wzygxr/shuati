# 参加考试的最大学生数 (Maximum Students Taking Exam)
# 给你一个 m * n 的矩阵 seats 表示教室中的座位分布。如果座位是坏的（不可用），
# 射中 'x'；如果是好的座位，则用 'x' 表示。
# 学生可以看到左侧、右侧、左上、右上这四个方向上紧邻他的学生的答卷，
# 但看不到直接坐在他前面或者后面的学生的答卷。
# 返回你最多能安排多少个学生参加考试且无法作弊。
# 测试链接 : https://leetcode.cn/problems/maximum-students-taking-exam/

class Code08_MaximumStudentsTakingExam:
    
    # 使用状态压缩动态规划解决最大学生数问题
    # 核心思想：逐行处理，用二进制位表示每行的座位安排，通过状态转移找到最大人数
    # 时间复杂度: O(m * 2^n * 2^n)，其中m是行数，n是列数
    # 空间复杂度: O(2^n)
    @staticmethod
    def maxStudents(seats):
        m = len(seats)
        n = len(seats[0])
        
        # seat[i] 表示第i行的可用座位（用二进制位表示，1表示可用）
        seat = [0] * m
        for i in range(m):
            for j in range(n):
                # 如果座位可用，将其添加到可用座位集合中
                if seats[i][j] == '.':
                    seat[i] |= 1 << j
        
        # dp[mask] 表示当前行座位安排为mask时的最大学生数
        dp = [-1] * (1 << n)
        # 初始状态：第0行不安排任何学生
        dp[0] = 0
        
        # 逐行处理
        for i in range(m):
            # ndp[next] 表示下一行座位安排为next时的最大学生数
            ndp = [-1] * (1 << n)
            
            # 枚举当前行的所有可能安排
            for mask in range(1 << n):
                # 如果当前状态不可达，跳过
                if dp[mask] == -1:
                    continue
                
                # 枚举下一行的所有可能安排
                for next_mask in range(1 << n):
                    # 检查next安排是否合法
                    if Code08_MaximumStudentsTakingExam._check(seat[i + 1] if i + 1 < m else 0, next_mask, mask):
                        # 更新状态：下一行安排为next时的最大学生数
                        ndp[next_mask] = max(ndp[next_mask], dp[mask] + bin(next_mask).count('1'))
            
            # 更新dp数组
            dp = ndp
        
        # 找到最大值
        result = 0
        for i in range(1 << n):
            result = max(result, dp[i])
        return result
    
    # 检查安排是否合法
    # seat: 当前行的可用座位
    # mask: 当前行的座位安排
    # pre: 上一行的座位安排
    @staticmethod
    def _check(seat, mask, pre):
        # 检查安排的座位是否都在可用座位中
        if (mask & seat) != mask:
            return False
        
        # 检查左右相邻座位是否都被安排了学生（会导致作弊）
        if (mask & (mask << 1)) != 0 or (mask & (mask >> 1)) != 0:
            return False
        
        # 检查左上和右上相邻座位是否都被安排了学生（会导致作弊）
        if (mask & (pre << 1)) != 0 or (mask & (pre >> 1)) != 0:
            return False
        
        return True