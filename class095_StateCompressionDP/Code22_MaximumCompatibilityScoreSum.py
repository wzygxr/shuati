# 最大兼容性评分和 (Maximum Compatibility Score Sum)
# 有一份有 n 个问题的调查问卷，每个问题的答案要么是 0 要么是 1。
# 当两个学生对所有问题的答案都一致时，他们的兼容性评分最高。
# 你需要将所有学生两两配对，使得这 n/2 个兼容性评分的总和最大。
# 测试链接 : https://leetcode.cn/problems/maximum-compatibility-score-sum/

class Code22_MaximumCompatibilityScoreSum:
    
    # 使用状态压缩动态规划解决最大兼容性评分和问题
    # 核心思想：用二进制位表示已配对的学生，通过状态转移找到最大评分和
    # 时间复杂度: O(2^n * n^2)
    # 空间复杂度: O(2^n)
    @staticmethod
    def maxCompatibilitySum(students, mentors):
        n = len(students)
        
        # 预处理计算学生和导师之间的兼容性评分
        # compatibility[i][j] 表示第i个学生和第j个导师的兼容性评分
        compatibility = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                # 计算学生i和导师j的兼容性评分
                score = 0
                for k in range(len(students[i])):
                    if students[i][k] == mentors[j][k]:
                        score += 1
                compatibility[i][j] = score
        
        # dp[mask] 表示配对了mask代表的学生时的最大兼容性评分和
        dp = [0] * (1 << n)
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << n):
            # 计算已配对的学生数量
            count = bin(mask).count('1')
            
            # 如果已配对的学生数量是奇数，跳过（因为需要两两配对）
            if count % 2 == 1:
                continue
            
            # 枚举两个未配对的学生进行配对
            for i in range(n):
                # 如果学生i已配对，跳过
                if mask & (1 << i):
                    continue
                
                for j in range(i + 1, n):
                    # 如果学生j已配对，跳过
                    if mask & (1 << j):
                        continue
                    
                    # 计算新的状态和评分和
                    new_mask = mask | (1 << i) | (1 << j)
                    score = dp[mask] + compatibility[i][j]  # 简化的匹配方式
                    
                    # 更新状态
                    dp[new_mask] = max(dp[new_mask], score)
        
        # 返回所有学生都配对时的最大兼容性评分和
        return dp[(1 << n) - 1]

# 测试代码
if __name__ == "__main__":
    solution = Code22_MaximumCompatibilityScoreSum()
    
    # 测试用例1
    students1 = [[1, 1, 0], [1, 0, 1], [0, 0, 1]]
    mentors1 = [[1, 0, 0], [0, 0, 1], [1, 1, 0]]
    result1 = solution.maxCompatibilitySum(students1, mentors1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 8
    
    # 测试用例2
    students2 = [[0, 0], [0, 0], [0, 0]]
    mentors2 = [[1, 1], [1, 1], [1, 1]]
    result2 = solution.maxCompatibilitySum(students2, mentors2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 0