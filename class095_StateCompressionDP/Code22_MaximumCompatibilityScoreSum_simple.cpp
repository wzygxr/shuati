// 最大兼容性评分和 (Maximum Compatibility Score Sum)
// 有一份有 n 个问题的调查问卷，每个问题的答案要么是 0 要么是 1。
// 当两个学生对所有问题的答案都一致时，他们的兼容性评分最高。
// 你需要将所有学生两两配对，使得这 n/2 个兼容性评分的总和最大。
// 测试链接 : https://leetcode.cn/problems/maximum-compatibility-score-sum/

class Solution {
public:
    // 使用状态压缩动态规划解决最大兼容性评分和问题
    // 核心思想：用二进制位表示已配对的学生，通过状态转移找到最大评分和
    // 时间复杂度: O(2^n * n^2)
    // 空间复杂度: O(2^n)
    int maxCompatibilitySum(int students[][20], int mentors[][20], int n, int m) {
        // 预处理计算学生和导师之间的兼容性评分
        // compatibility[i][j] 表示第i个学生和第j个导师的兼容性评分
        int compatibility[20][20];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // 计算学生i和导师j的兼容性评分
                int score = 0;
                for (int k = 0; k < m; k++) {
                    if (students[i][k] == mentors[j][k]) {
                        score++;
                    }
                }
                compatibility[i][j] = score;
            }
        }
        
        // dp[mask] 表示配对了mask代表的学生时的最大兼容性评分和
        int dp[1 << 20];
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = 0;
        }
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 计算已配对的学生数量
            int count = 0;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    count++;
                }
            }
            
            // 如果已配对的学生数量是奇数，跳过（因为需要两两配对）
            if (count % 2 == 1) {
                continue;
            }
            
            // 枚举两个未配对的学生进行配对
            for (int i = 0; i < n; i++) {
                // 如果学生i已配对，跳过
                if (mask & (1 << i)) {
                    continue;
                }
                
                for (int j = i + 1; j < n; j++) {
                    // 如果学生j已配对，跳过
                    if (mask & (1 << j)) {
                        continue;
                    }
                    
                    // 计算新的状态和评分和
                    int newMask = mask | (1 << i) | (1 << j);
                    int score = dp[mask] + compatibility[i][j];  // 简化的匹配方式
                    
                    // 更新状态
                    if (dp[newMask] < score) {
                        dp[newMask] = score;
                    }
                }
            }
        }
        
        // 返回所有学生都配对时的最大兼容性评分和
        return dp[(1 << n) - 1];
    }
};