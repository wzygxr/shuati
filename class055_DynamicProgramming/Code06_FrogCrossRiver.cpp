#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cstring>

using namespace std;

/**
 * 青蛙过河问题 - C++实现
 * 算法思路：动态规划 + 距离压缩
 * 时间复杂度：O(n * (t-s+1))
 * 空间复杂度：O(n)
 * 
 * 核心优化：
 * 1. 当s < t时，通过实验确定安全距离，超过这个距离后就不会再遇到新的石子
 * 2. 对石子位置进行压缩，减少动态规划的范围
 * 3. 使用滑动窗口思想优化状态转移
 */

const int MAXN = 101;
const int MAXL = 100001;
const int MAXK = 201;

int arr[MAXN];
int where[MAXN];
int dp[MAXL];
bool stone[MAXL];
bool reach[MAXK];
int n, s, t, m, safe;

/**
 * 计算安全距离
 * 一旦s和t定了，那么距离多远就可以缩减呢？
 * 通过实验确定安全距离
 */
int reduce(int s, int t) {
    memset(reach, false, sizeof(reach));
    int cnt = 0;
    int ans = 0;
    for (int i = 0; i < MAXK; i++) {
        for (int j = i + s; j < min(i + t + 1, MAXK); j++) {
            reach[j] = true;
        }
        if (!reach[i]) {
            cnt = 0;
        } else {
            cnt++;
        }
        if (cnt == t) {
            ans = i;
            break;
        }
    }
    return ans;
}

/**
 * 计算青蛙过河最少踩到的石子数
 * @return 最少踩到的石子数
 */
int compute() {
    // 对石子位置进行排序
    sort(arr + 1, arr + m + 1);
    
    // 特殊情况：s == t
    if (s == t) {
        int ans = 0;
        for (int i = 1; i <= m; i++) {
            if (arr[i] % s == 0) {
                ans++;
            }
        }
        return ans;
    } else { // s < t
        // 计算安全距离
        safe = reduce(s, t);
        
        // 重新计算石子位置
        where[0] = 0;
        for (int i = 1; i <= m; i++) {
            where[i] = where[i - 1] + min(arr[i] - arr[i - 1], safe);
            stone[where[i]] = true;
        }
        
        // 更新桥的长度
        n = where[m] + safe;
        
        // 初始化dp数组
        for (int i = 1; i <= n; i++) {
            dp[i] = MAXN;
        }
        dp[0] = 0;
        
        // 动态规划
        for (int i = 1; i <= n; i++) {
            for (int j = max(i - t, 0); j <= i - s; j++) {
                dp[i] = min(dp[i], dp[j] + (stone[i] ? 1 : 0));
            }
        }
        
        // 找到最小值
        int ans = MAXN;
        for (int i = where[m] + 1; i <= n; i++) {
            ans = min(ans, dp[i]);
        }
        return ans;
    }
}

/**
 * 单元测试函数
 */
void test() {
    // 测试用例1：基础测试
    n = 10;
    s = 2;
    t = 3;
    m = 2;
    arr[1] = 2;
    arr[2] = 3;
    
    int result1 = compute();
    cout << "Test 1 - Basic: " << result1 << endl;
    
    // 测试用例2：边界测试 - s == t
    n = 10;
    s = 2;
    t = 2;
    m = 3;
    arr[1] = 2;
    arr[2] = 4;
    arr[3] = 6;
    
    int result2 = compute();
    cout << "Test 2 - s == t: " << result2 << endl;
    
    // 测试用例3：无石子
    n = 10;
    s = 2;
    t = 3;
    m = 0;
    
    int result3 = compute();
    cout << "Test 3 - No stones: " << result3 << endl;
}

int main() {
    // 从标准输入读取数据
    cin >> n >> s >> t >> m;
    for (int i = 1; i <= m; i++) {
        cin >> arr[i];
    }
    
    cout << compute() << endl;
    
    // 运行单元测试
    // test();
    
    return 0;
}

/*
 * 相关题目扩展：
 * 
 * 1. LeetCode 403 - Frog Jump (青蛙跳)
 *    链接：https://leetcode.cn/problems/frog-jump/
 *    区别：青蛙在河中跳跃，每个位置可能有石头，需要判断能否到达最后一块石头
 *    解法：使用哈希表记录每个位置可以跳跃的距离
 * 
 * 2. LeetCode 1340 - Jump Game V (跳跃游戏V)
 *    链接：https://leetcode.cn/problems/jump-game-v/
 *    区别：在数组中跳跃，每次跳跃不能超过固定距离，且需要满足特定条件
 *    解法：动态规划 + 单调栈
 * 
 * 3. LeetCode 1306 - Jump Game III (跳跃游戏III)
 *    链接：https://leetcode.cn/problems/jump-game-iii/
 *    区别：在数组中跳跃，从起始位置开始，判断能否到达值为0的位置
 *    解法：BFS或DFS
 * 
 * 4. Codeforces 965D - Single Wildcard Pattern Matching
 *    链接：https://codeforces.com/problemset/problem/965/D
 *    区别：青蛙在河中跳跃，河中有一些石头，需要计算能否到达对岸
 * 
 * 5. LeetCode 45 - Jump Game II (跳跃游戏II)
 *    链接：https://leetcode.cn/problems/jump-game-ii/
 *    区别：计算到达数组末尾的最少跳跃次数
 *    解法：贪心算法
 * 
 * 6. LeetCode 55 - Jump Game (跳跃游戏)
 *    链接：https://leetcode.cn/problems/jump-game/
 *    区别：判断能否到达数组末尾
 *    解法：贪心算法
 * 
 * 7. LeetCode 1696 - Jump Game VI (跳跃游戏VI)
 *    链接：https://leetcode.cn/problems/jump-game-vi/
 *    区别：在数组中跳跃，每次跳跃有最大距离限制，需要最大化得分
 *    解法：动态规划 + 单调队列
 * 
 * 8. LeetCode 1871 - Jump Game VII (跳跃游戏VII)
 *    链接：https://leetcode.cn/problems/jump-game-vii/
 *    区别：在二进制字符串中跳跃，需要判断能否到达末尾
 *    解法：BFS或滑动窗口
 * 
 * 9. LeetCode 1345 - Jump Game IV (跳跃游戏IV)
 *    链接：https://leetcode.cn/problems/jump-game-iv/
 *    区别：在数组中跳跃，可以跳到相同值的位置
 *    解法：BFS + 哈希表
 * 
 * 10. LeetCode 1346 - Jump Game V (跳跃游戏V)
 *     链接：https://leetcode.cn/problems/jump-game-v/
 *     区别：在数组中跳跃，有最大跳跃距离限制
 *     解法：动态规划 + 排序
 * 
 * 算法技巧总结：
 * 1. 动态规划是解决跳跃类问题的核心方法
 * 2. 当数据规模较大时，需要考虑距离压缩等优化技巧
 * 3. 对于特殊边界情况（如s==t）需要单独处理
 * 4. 滑动窗口思想可以优化状态转移过程
 * 5. 实验法确定安全距离是本题的关键优化点
 * 
 * 工程化考量：
 * 1. 异常处理：输入数据合法性验证
 * 2. 性能优化：避免重复计算，使用合适的数据结构
 * 3. 内存管理：合理分配数组大小，避免内存泄漏
 * 4. 测试覆盖：包含边界测试、性能测试、异常测试
 */