// 线性串取石子游戏 (SG函数在线性串上的应用)
// 一串石子，每次可以取走若干个连续的石子
// 取走最后一颗的胜利，给出选取石子数的约束集合
// 求先手胜负
// 
// 题目来源：
// 1. HDU 2999 Stone Game, Why are you always there? - http://acm.hdu.edu.cn/showproblem.php?pid=2999
// 2. POJ 2311 Cutting Game - http://poj.org/problem?id=2311
// 3. 洛谷 P3185 [HNOI2007]分裂游戏 - https://www.luogu.com.cn/problem/P3185
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个区间状态的SG值
// 2. 区间分割：取石子操作将区间分割为两个子区间
// 3. SG定理：整个游戏的SG值等于各子区间SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(n^3) - 计算每个区间的SG值
// 2. 查询：O(1) - 直接返回整个区间的SG值
// 
// 空间复杂度分析：
// 1. SG数组：O(n^2) - 存储每个区间的SG值
// 2. S集合：O(|S|) - 存储可取石子数的集合
// 
// 工程化考量：
// 1. 异常处理：处理负数输入和边界情况
// 2. 性能优化：记忆化搜索避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的S集合和查询

// 最大石子数
const int MAXN = 101;

// SG数组，sg[l][r]表示区间[l,r]的SG值
int sg[MAXN][MAXN];

// S集合，表示每次可以取的连续石子数
int s[21];
int sCount;

// visited数组用于记忆化搜索
int visited[MAXN][MAXN];

// appear数组用于计算mex值
int appear[MAXN];

// 
// 算法原理：
// 1. 对于每个区间[l,r]，计算其后继状态的SG值集合
// 2. 区间[l,r]的后继状态为取走连续k个石子后分割成的两个子区间
// 3. 区间[l,r]的SG值等于不属于后继状态SG值集合的最小非负整数(mex)
// 
// SG函数定义：
// SG([l,r]) = mex{SG([l,i-1]) XOR SG([i+k,r]) | i∈[l,r-k+1], k∈S}
// 其中mex(S)表示不属于集合S的最小非负整数
// XOR表示异或运算
// 
// 对于线性串取石子游戏，区间[l,r]的后继状态为取走连续k个石子后
// 分割成的两个子区间[l,i-1]和[i+k,r]
int computeSG(int l, int r) {
    // 边界条件：区间为空
    if (l > r) {
        return 0;
    }
    
    // 记忆化搜索
    if (visited[l][r]) {
        return sg[l][r];
    }
    
    // 标记已访问
    visited[l][r] = 1;
    
    // 初始化appear数组
    int i, j;
    for (i = 0; i < MAXN; i++) {
        appear[i] = 0;
    }
    
    // 计算区间[l,r]的所有后继状态的SG值
    for (i = 0; i < sCount; i++) {
        int k = s[i];
        // 枚举取走k个连续石子的起始位置
        for (j = l; j <= r - k + 1; j++) {
            // 取走区间[j,j+k-1]的石子后，分割成两个子区间[l,j-1]和[j+k,r]
            // 根据SG定理，后继状态的SG值为两个子区间SG值的异或和
            int nextStateSG = computeSG(l, j - 1) ^ computeSG(j + k, r);
            // 标记后继状态的SG值已出现
            if (nextStateSG < MAXN) {
                appear[nextStateSG] = 1;
            }
        }
    }
    
    // 计算mex值，即不属于appear集合的最小非负整数
    for (i = 0; i < MAXN; i++) {
        if (appear[i] == 0) {
            sg[l][r] = i;
            return sg[l][r];
        }
    }
    
    return 0; // 理论上不会执行到这里
}

// 
// 算法原理：
// 根据SG函数计算整个游戏的SG值
// 1. 整个游戏的SG值为区间[1,n]的SG值
// 2. SG值不为0表示必胜态，为0表示必败态
int solve(int n) {
    // 异常处理：处理非法输入
    if (n <= 0) {
        return 0; // 空游戏，先手败
    }
    
    // 计算区间[1,n]的SG值
    int result = computeSG(1, n);
    
    // SG值不为0表示必胜态，为0表示必败态
    return result != 0 ? 1 : 0; // 1表示WIN，0表示LOSE
}

// 构建SG函数
void buildSG() {
    int i, j;
    // 初始化visited数组
    for (i = 0; i < MAXN; i++) {
        for (j = 0; j < MAXN; j++) {
            visited[i][j] = 0;
        }
    }
    
    // 初始化SG数组
    for (i = 0; i < MAXN; i++) {
        for (j = 0; j < MAXN; j++) {
            sg[i][j] = 0;
        }
    }
}

// 测试示例
int main() {
    // 示例1: S = {1, 2}, n = 4
    s[0] = 1;
    s[1] = 2;
    sCount = 2;
    buildSG();
    int result1 = solve(4);
    // 预期结果: 1 (WIN)
    
    // 示例2: S = {1, 3}, n = 5
    s[0] = 1;
    s[1] = 3;
    sCount = 2;
    buildSG();
    int result2 = solve(5);
    // 预期结果: 0 (LOSE)
    
    return 0;
}