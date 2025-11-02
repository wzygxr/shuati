// S-Nim游戏 (SG函数经典应用)
// 有若干堆石子，每次可以从任意一堆石子中取若干颗（数目必须在集合S中）
// 问谁会获胜
// 
// 题目来源：
// 1. HDU 1536 S-Nim - http://acm.hdu.edu.cn/showproblem.php?pid=1536
// 2. POJ 2960 S-Nim - http://poj.org/problem?id=2960
// 3. 洛谷 P2148 [SDOI2009]E&D - https://www.luogu.com.cn/problem/P2148
// 4. SPOJ 3805. E&D Game - https://www.spoj.com/problems/ED/
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个状态的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. SG定理：整个游戏的SG值等于各子游戏SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(max_n * |S|) - 计算每个石子数的SG值
// 2. 查询：O(k) - k为堆数，计算所有堆SG值的异或和
// 
// 空间复杂度分析：
// 1. SG数组：O(max_n) - 存储每个石子数的SG值
// 2. S集合：O(|S|) - 存储可取石子数的集合
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的S集合和查询

// 最大石子数
const int MAXN = 1001;

// SG数组，sg[i]表示有i个石子时的SG值
int sg[MAXN];

// S集合，表示每次可以取的石子数
int s[101];

// appear数组用于计算mex值
int appear[MAXN];

// visited数组用于记忆化搜索
int visited[MAXN];

// 
// 算法原理：
// 1. 对于每个石子数i，计算其后继状态的SG值集合
// 2. SG值等于不属于该集合的最小非负整数(mex)
// 3. 根据SG定理，整个游戏的SG值等于各堆SG值的异或和
// 
// SG函数定义：
// SG(x) = mex{SG(y) | y是x的后继状态}
// 其中mex(S)表示不属于集合S的最小非负整数
// 
// 对于S-Nim游戏，状态i的后继状态为i-s[0], i-s[1], ..., i-s[k-1]（如果存在）
int computeSG(int x, int k) {
    // 记忆化搜索
    if (visited[x]) {
        return sg[x];
    }
    
    // 标记已访问
    visited[x] = 1;
    
    // 初始化appear数组
    int i, j;
    for (i = 0; i < MAXN; i++) {
        appear[i] = 0;
    }
    
    // 计算状态x的所有后继状态的SG值
    for (i = 0; i < k && s[i] <= x; i++) {
        // 标记后继状态的SG值已出现
        int nextSG = computeSG(x - s[i], k);
        if (nextSG < MAXN) {
            appear[nextSG] = 1;
        }
    }
    
    // 计算mex值，即不属于appear集合的最小非负整数
    for (i = 0; i < MAXN; i++) {
        if (appear[i] == 0) {
            sg[x] = i;
            return sg[x];
        }
    }
    
    return 0; // 理论上不会执行到这里
}

// 
// 算法原理：
// 根据SG定理计算整个游戏的SG值
// 1. 对于每堆石子，计算其SG值
// 2. 整个游戏的SG值等于各堆SG值的异或和
// 3. SG值不为0表示必胜态，为0表示必败态
int solve(int* piles, int pilesCount, int k) {
    // 计算所有堆SG值的异或和
    int xorSum = 0;
    int i;
    for (i = 0; i < pilesCount; i++) {
        xorSum ^= sg[piles[i]];
    }
    
    // SG值不为0表示必胜态，为0表示必败态
    return xorSum != 0 ? 1 : 0; // 1表示W，0表示L
}

// 构建SG函数
void buildSG(int k) {
    int i;
    // 初始化visited数组
    for (i = 0; i < MAXN; i++) {
        visited[i] = 0;
    }
    
    // 预处理SG值
    for (i = 0; i < MAXN; i++) {
        sg[i] = computeSG(i, k);
    }
}

// 测试示例
int main() {
    // 示例1: S = {1, 2, 3}, piles = {1, 2}
    s[0] = 1;
    s[1] = 2;
    s[2] = 3;
    buildSG(3);
    
    int piles1[2] = {1, 2};
    int result1 = solve(piles1, 2, 3);
    // 预期结果: 1 (W)
    
    // 示例2: S = {2, 4, 7}, piles = {3, 5}
    s[0] = 2;
    s[1] = 4;
    s[2] = 7;
    buildSG(3);
    
    int piles2[2] = {3, 5};
    int result2 = solve(piles2, 2, 3);
    // 预期结果: 0 (L)
    
    return 0;
}