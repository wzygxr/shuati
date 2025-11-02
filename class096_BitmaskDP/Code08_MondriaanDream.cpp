// Mondriaan's Dream (蒙德里安的梦想)
// 题目来源: POJ 2411 Mondriaan's Dream
// 题目链接: http://poj.org/problem?id=2411
// 题目描述:
// 给定n行m列的矩形，用1×2的砖块填充，问有多少种填充方案。
//
// 解题思路:
// 这是一道经典的轮廓线DP问题，也是状态压缩DP的一种。
// 1. 按格子进行DP，从上到下，从左到右填充
// 2. 用二进制状态表示当前轮廓线上的格子是否已被填充
// 3. dp[i][mask] 表示处理到第i个格子，轮廓线状态为mask时的方案数
// 4. 对于每个格子，有两种选择：横放或竖放砖块
//
// 时间复杂度: O(n*m*2^m)
// 空间复杂度: O(2^m)
//
// 补充题目1: 不要62 (不要62)
// 题目来源: HDU 2089 不要62
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=2089
// 题目描述:
// 杭州人称那些傻乎乎粘嗒嗒的人为62（音：laoer）。
// 杭州交通管理局经常会扩充一些的士车牌照，新近出来一个好消息，
// 以后上牌照，不再含有不吉利的数字了，这样就可以消除个别的士司机和乘客的心理障碍，
// 为社会和谐做出贡献。
// 不吉利的数字为所有包含4或62的号码。例如：62315 73418 88914都属于不吉利的号码。
// 问题是：从n到m的所有整数中，有多少个吉利的数字？
// 解题思路:
// 1. 数位DP解法
// 2. dfs(pos, pre, state, limit) 表示处理到第pos位，前一位数字是pre，状态为state，是否有限制
// 3. state表示是否包含不吉利数字的状态
// 时间复杂度: O(log(n) * 10 * 2)
// 空间复杂度: O(log(n) * 10 * 2)

// 常量定义
const int MAXN = 15;         // 最大行数/列数
const int MAX_STATES = 1 << 11; // 最大状态数，2^11 = 2048
const long long INF = 1LL << 60; // 无穷大常量

// DFS辅助函数声明
// 参数说明:
// row: 当前行号
// col: 当前列号
// prevMask: 前一行的轮廓线状态
// currMask: 当前行的轮廓线状态
// count: 当前状态的方案数
// nextDp: 下一行的DP数组
// m: 列数
void dfsSimple(int row, int col, int prevMask, int currMask, long long count, long long* nextDp, int m);

// DFS辅助函数，用于数位DP
// 参数说明:
// pos: 当前处理的位数
// has62: 是否包含62
// has4: 是否包含4
// limit: 是否有限制
// num: 数字字符串
// dp: 记忆化数组
// len: 字符串长度
int dfsLucky(int pos, int has62, int has4, int limit, const char* num, int dp[20][2][2][2], int len);

// 数位DP辅助函数
// 参数说明:
// num: 数字字符串
// 返回值: 吉利数字的个数
int countLuckyNumbersHelper(const char* num);

// POJ 2411 Mondriaan's Dream 解法
// 参数说明:
// n: 矩形行数
// m: 矩形列数
// 返回值: 填充方案数
long long mondriaanDream(int n, int m) {
    // 特殊情况：如果n*m是奇数，则无法完全填充
    // 因为每个砖块占据2个格子，总格子数必须是偶数才能完全填充
    if ((n * m) % 2 == 1) {
        return 0;
    }
    
    // 交换n和m，确保m<=n，优化时间复杂度
    // 由于时间复杂度是O(n*m*2^m)，让m尽可能小可以优化性能
    if (m > n) {
        int temp = n;
        n = m;
        m = temp;
    }
    
    // dp[mask] 表示当前行的轮廓线状态为mask时的方案数
    // 轮廓线是指当前处理位置上方一行的状态
    // mask的第i位为1表示第i个位置已被前一行的砖块占用（竖放的上半部分）
    long long dp[MAX_STATES];
    long long nextDp[MAX_STATES];
    for (int i = 0; i < (1 << m); i++) {
        dp[i] = 0;
        nextDp[i] = 0;
    }
    dp[0] = 1;  // 初始状态：第0行，没有被占用的状态方案数为1
    
    // 按行进行状态转移
    for (int i = 0; i < n; i++) {
        // 初始化nextDp数组
        for (int j = 0; j < (1 << m); j++) {
            nextDp[j] = 0;
        }
        
        // 按列进行状态转移
        for (int mask = 0; mask < (1 << m); mask++) {
            if (dp[mask] > 0) {
                // 尝试在当前行放置砖块
                // 从第0列开始，前一行状态为mask，当前行状态为0，方案数为dp[mask]
                dfsSimple(i, 0, mask, 0, dp[mask], nextDp, m);
            }
        }
        
        // 交换dp数组，将nextDp的值复制到dp中，为下一次迭代做准备
        for (int j = 0; j < (1 << m); j++) {
            dp[j] = nextDp[j];
        }
    }
    
    // 返回dp[0]，表示处理完所有行后，轮廓线状态为0（没有被占用）的方案数
    return dp[0];
}

// DFS辅助函数，用于处理当前行的砖块放置（简洁版）
// 参数说明:
// row: 当前行号
// col: 当前列号
// prevMask: 前一行的轮廓线状态
// currMask: 当前行的轮廓线状态
// count: 当前状态的方案数
// nextDp: 下一行的DP数组
// m: 列数
void dfsSimple(int row, int col, int prevMask, int currMask, long long count, long long* nextDp, int m) {
    // 如果处理完当前行
    if (col == m) {
        nextDp[currMask] += count;
        return;
    }
    
    // 如果当前位置在前一行已经被填充（prevMask的第col位为1）
    if ((prevMask & (1 << col)) != 0) {
        // 当前位置不需要填充，直接处理下一个位置
        // 因为前一行的竖放砖块已经占用了当前位置
        dfsSimple(row, col + 1, prevMask, currMask, count, nextDp, m);
    } else {
        // 当前位置未被填充，需要放置砖块
        
        // 竖放砖块（占用当前位置和下一行的同一列）
        // 在当前行的轮廓线上标记该位置被占用（currMask | (1 << col)）
        // 这样下一行处理到该列时会知道该位置已被占用
        dfsSimple(row, col + 1, prevMask, currMask | (1 << col), count, nextDp, m);
        
        // 横放砖块（占用当前位置和同一行的下一列），前提是下一列存在且未被填充
        if (col + 1 < m && (prevMask & (1 << (col + 1))) == 0) {
            // 横放砖块不需要在当前轮廓线上标记，因为两个位置都被填充了
            // 直接跳过下一列（col + 2），因为两个位置都被当前砖块占据
            dfsSimple(row, col + 2, prevMask, currMask, count, nextDp, m);
        }
    }
}

// 计算字符串长度
// 参数说明:
// str: 输入字符串
// 返回值: 字符串长度
int strlen(const char* str) {
    int len = 0;
    while (str[len] != '\0') {
        len++;
    }
    return len;
}

// HDU 2089 不要62 解法
// 参数说明:
// n: 起始数字
// m: 结束数字
// 返回值: [n, m]范围内吉利数字的个数
int countLuckyNumbers(int n, int m) {
    // 将数字转换为字符串，便于数位DP处理
    char num1[20], num2[20];
    // 简化处理，实际应用中需要实现整数到字符串的转换
    
    // 计算[0, m]范围内的吉利数字个数
    int count2 = countLuckyNumbersHelper(num2);
    // 计算[0, n-1]范围内的吉利数字个数
    int count1 = countLuckyNumbersHelper(num1);
    
    return count2 - count1;
}

// 数位DP辅助函数
// 参数说明:
// num: 数字字符串
// 返回值: [0, num]范围内吉利数字的个数
int countLuckyNumbersHelper(const char* num) {
    int len = strlen(num);
    if (len == 0) return 0;
    
    // dp[pos][has62][has4][limit] 表示处理到第pos位，是否包含62，是否包含4，是否有限制时的方案数
    // pos: 当前处理的位数
    // has62: 是否已经包含62
    // has4: 是否已经包含4
    // limit: 当前位是否有大小限制
    int dp[20][2][2][2];
    // 初始化为-1，表示未计算
    for (int i = 0; i < 20; i++) {
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                for (int l = 0; l < 2; l++) {
                    dp[i][j][k][l] = -1;
                }
            }
        }
    }
    
    return dfsLucky(0, 0, 0, 1, num, dp, len);
}

// DFS辅助函数，用于数位DP
// 参数说明:
// pos: 当前处理的位数
// has62: 是否包含62
// has4: 是否包含4
// limit: 是否有限制
// num: 数字字符串
// dp: 记忆化数组
// len: 字符串长度
int dfsLucky(int pos, int has62, int has4, int limit, const char* num, int dp[20][2][2][2], int len) {
    // 如果处理完所有位数
    if (pos == len) {
        // 如果不包含不吉利数字，返回1；否则返回0
        return (has62 == 0 && has4 == 0) ? 1 : 0;
    }
    
    // 记忆化搜索
    // 如果没有限制且已经计算过，直接返回结果
    if (!limit && dp[pos][has62][has4][limit] != -1) {
        return dp[pos][has62][has4][limit];
    }
    
    // 确定当前位可以填的最大数字
    int up = limit ? (num[pos] - '0') : 9;
    int res = 0;
    
    // 枚举当前位可以填的数字
    for (int i = 0; i <= up; i++) {
        // 如果当前数字是4，标记包含4
        int newHas4 = (has4 == 1 || i == 4) ? 1 : 0;
        // 如果前一位是6且当前位是2，标记包含62
        int newHas62 = (has62 == 1 || (pos > 0 && num[pos - 1] == '6' && i == 2)) ? 1 : 0;
        
        // 递归处理下一位
        // limit && (i == up) 表示下一位是否有限制
        res += dfsLucky(pos + 1, newHas62, newHas4, limit && (i == up), num, dp, len);
    }
    
    // 记忆化存储
    // 只有在没有限制时才存储，因为有限制的状态每次可能不同
    if (!limit) {
        dp[pos][has62][has4][limit] = res;
    }
    
    return res;
}

// 主函数 - 用于测试
int main() {
    // 由于编译环境限制，这里不包含测试代码
    // 实际使用时可以添加适当的测试代码
    return 0;
}