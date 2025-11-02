#include <iostream>
#include <vector>
#include <string>
using namespace std;

/*
 * 吃草问题 - C++实现
 * 
 * 题目描述：
 * 草一共有n的重量，两只牛轮流吃草，A牛先吃，B牛后吃
 * 每只牛在自己的回合，吃草的重量必须是4的幂，1、4、16、64....
 * 谁在自己的回合正好把草吃完谁赢，根据输入的n，返回谁赢
 * 
 * 解题思路：
 * 这是一个典型的博弈论问题，可以使用动态规划、数学规律或SG函数来解决
 * 1. 动态规划解法：自底向上计算每个状态的胜负
 * 2. 数学规律解法：通过观察周期规律优化计算
 * 3. SG函数解法：计算每个状态的SG值
 * 
 * 相关题目：
 * 1. LeetCode 292. Nim Game：https://leetcode.com/problems/nim-game/
 * 2. LeetCode 877. Stone Game：https://leetcode.com/problems/stone-game/
 * 3. LeetCode 486. Predict the Winner：https://leetcode.com/problems/predict-the-winner/
 * 4. POJ 2484. A Funny Game：http://poj.org/problem?id=2484
 * 5. LeetCode 1510. Stone Game IV：https://leetcode.com/problems/stone-game-iv/
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数输入
 * 2. 边界条件：处理小规模数据
 * 3. 性能优化：使用数学规律O(1)解法
 * 4. 可读性：清晰的变量命名和注释
 */

class EatGrass {
public:
    /*
     * 方法1：动态规划解法
     * 
     * 解题思路：
     * 自底向上计算每个状态的胜负情况：
     * 1. 如果能直接吃完草，则当前玩家获胜
     * 2. 如果存在一种吃草策略能让对手必败，则当前玩家必胜
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 优缺点分析：
     * 优点：思路清晰，适用于展示DP在博弈问题中的应用
     * 缺点：时间空间复杂度较高
     * 
     * 适用场景：展示DP在博弈问题中的应用，教学演示
     */
    static string canWinDP(int n) {
        // 边界条件：没有草时，后手赢
        if (n == 0) return "B"; // 没有草时，后手赢
        
        // 创建dp数组，dp[i]表示先手是否能赢
        vector<bool> dp(n + 1, false); // dp[i]表示先手是否能赢
        
        // 基础情况
        dp[1] = true;  // 只有1棵草，先手赢
        dp[4] = true;  // 只有4棵草，先手赢
        dp[16] = true; // 只有16棵草，先手赢
        
        // 自底向上计算每个状态的胜负
        for (int i = 2; i <= n; i++) {
            // 如果当前状态可以转移到必败状态，则当前状态是必胜状态
            // 尝试吃1棵草
            if (i >= 1 && !dp[i - 1]) {
                dp[i] = true;
            }
            // 尝试吃4棵草
            if (i >= 4 && !dp[i - 4]) {
                dp[i] = true;
            }
            // 尝试吃16棵草
            if (i >= 16 && !dp[i - 16]) {
                dp[i] = true;
            }
        }
        
        // 返回结果：如果先手能赢返回"A"，否则返回"B"
        return dp[n] ? "A" : "B";
    }
    
    // 方法2：数学规律解法（最优解）
    static string canWinMath(int n) {
        // 观察规律：每5个数字一个周期
        // 必败点：0, 2, 7, 9, 14, 16, 21, 23, ...
        if (n == 0) return "B";
        
        // 计算模5的余数
        int mod = n % 5;
        if (mod == 2 || mod == 0) {
            return "B";
        } else {
            return "A";
        }
    }
    
    // 方法3：SG函数解法
    static string canWinSG(int n) {
        if (n == 0) return "B";
        
        // SG函数计算
        vector<int> sg(n + 1, 0);
        vector<int> moves = {1, 4, 16};
        
        for (int i = 1; i <= n; i++) {
            vector<bool> mex(n + 1, false);
            
            for (int move : moves) {
                if (i >= move) {
                    mex[sg[i - move]] = true;
                }
            }
            
            // 计算mex值
            int g = 0;
            while (mex[g]) {
                g++;
            }
            sg[i] = g;
        }
        
        return sg[n] > 0 ? "A" : "B";
    }
    
    // ==================== 扩展题目1: Nim游戏 ====================
    /*
     * LeetCode 292. Nim Game
     * 题目：经典的Nim游戏，每次可以取1-3个石子
     * 网址：https://leetcode.com/problems/nim-game/
     * 
     * 数学规律：如果石子数能被4整除，先手必败，否则先手必胜
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    static bool canWinNim(int n) {
        return n % 4 != 0;
    }
    
    // ==================== 扩展题目2: 石子游戏 ====================
    /*
     * LeetCode 877. Stone Game
     * 题目：石子游戏，每次可以从两端取石子
     * 网址：https://leetcode.com/problems/stone-game/
     * 
     * 动态规划解法：
     * dp[i][j]表示从i到j的石子堆中，先手能获得的最大分数差
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    static bool stoneGame(vector<int>& piles) {
        int n = piles.size();
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化对角线
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        
        // 填充DP表
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        return dp[0][n - 1] > 0;
    }
    
    // ==================== 扩展题目3: 预测赢家 ====================
    /*
     * LeetCode 486. Predict the Winner
     * 题目：预测赢家，每次可以从两端取数字
     * 网址：https://leetcode.com/problems/predict-the-winner/
     * 
     * 动态规划解法：
     * dp[i][j]表示从i到j的数字中，先手能获得的最大分数差
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    static bool predictTheWinner(vector<int>& nums) {
        int n = nums.size();
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化对角线
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }
        
        // 填充DP表
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i][j] = max(nums[i] - dp[i + 1][j], nums[j] - dp[i][j - 1]);
            }
        }
        
        return dp[0][n - 1] >= 0;
    }
};

// 测试函数
int main() {
    cout << "=== 吃草问题测试 ===" << endl;
    for (int i = 0; i <= 50; i++) {
        string result1 = EatGrass::canWinDP(i);
        string result2 = EatGrass::canWinMath(i);
        string result3 = EatGrass::canWinSG(i);
        cout << i << " : " << result2 << endl;
    }
    
    cout << "\n=== 扩展题目测试 ===" << endl;
    
    // 测试Nim游戏
    cout << "Nim Game (4): " << (EatGrass::canWinNim(4) ? "true" : "false") << endl;
    
    // 测试石子游戏
    vector<int> piles = {5, 3, 4, 5};
    cout << "Stone Game: " << (EatGrass::stoneGame(piles) ? "true" : "false") << endl;
    
    // 测试预测赢家
    vector<int> nums = {1, 5, 2};
    cout << "Predict the Winner: " << (EatGrass::predictTheWinner(nums) ? "true" : "false") << endl;
    
    return 0;
}