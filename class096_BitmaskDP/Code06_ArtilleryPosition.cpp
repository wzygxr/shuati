// 炮兵阵地 (Artillery Position)
// 题目来源: POJ 1185 炮兵阵地
// 题目链接: http://poj.org/problem?id=1185
// 题目描述:
// 司令部的将军们打算在N*M的网格地图上部署他们的炮兵部队。一个N*M的地图由N行M列组成，
// 地图的每一格可能是山地（用"H"表示），也可能是平原（用"P"表示）。
// 在每一格平原上可以布置一支炮兵部队，山地上则不可以。
// 一支炮兵部队在地图上的攻击范围是它所在位置的四个方向（上下左右）各两格内的区域，
// 但不包括该炮兵部队自身所在的格子。
// 任何一支炮兵部队的攻击范围内的格子（包括攻击范围的边界）不能再布置其他炮兵部队。
// 一支炮兵部队的攻击范围与其部署位置有关，不同位置的炮兵部队的攻击范围各不相同。
// 问题要求计算在给定的地图上最多能部署多少支炮兵部队。
//
// 解题思路:
// 这是一道经典的状态压缩DP问题。由于炮兵的攻击范围是上下左右各两格，
// 所以我们需要考虑当前行、前一行和前两行的状态。
// 我们可以按行进行状态压缩，用二进制位表示每一行的炮兵部署状态。
// 对于每一行，我们需要考虑：
// 1. 当前行的地形是否允许在某个位置部署炮兵（平原为P，山地为H）
// 2. 当前行的炮兵部署状态是否合法（同一行内炮兵不能互相攻击）
// 3. 当前行与前一行、前两行的炮兵部署状态是否冲突
//
// 状态定义:
// dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
//
// 状态转移:
// 对于每一行，我们枚举所有可能的合法状态，然后检查与前两行是否冲突
//
// 时间复杂度: O(n * 2^(3*m)) 其中n是行数，m是列数
// 空间复杂度: O(2^(2*m))
//
// 补充题目1: 最大兼容数对 (Compatible Numbers)
// 题目来源: CodeForces 165E
// 题目链接: https://codeforces.com/problemset/problem/165/E
// 题目描述:
// 给定一个数组，对于每个数字，找到另一个数字，使得它们的按位与结果为0。
// 如果不存在这样的数字，输出-1。
// 解题思路:
// 1. 使用状态压缩DP或SOS DP
// 2. 对于每个数字，我们需要找到另一个数字，使得它们的按位与为0
// 3. 这等价于找到一个数字，使得它的二进制表示中为1的位在原数字中都为0
// 4. 可以使用子集枚举或预处理来解决
// 时间复杂度: O(n * 2^k) 其中k是位数
// 空间复杂度: O(2^k)

// 常量定义
const int MAXN = 105;      // 最大行数
const int MAXM = 15;       // 最大列数
const int MAX_STATES = 1 << 10; // 最大状态数，2^10 = 1024
const int INF = 0x3f3f3f3f; // 无穷大常量

// 计算整数中1的个数（汉明重量）
// 参数说明:
// x: 输入的整数
// 返回值: x的二进制表示中1的个数
int bitCount(int x) {
    int count = 0;
    while (x) {
        count += x & 1;  // 检查最低位是否为1
        x >>= 1;         // 右移一位
    }
    return count;
}

// POJ 1185 炮兵阵地 解法
// 参数说明:
// n: 网格行数
// m: 网格列数
// grid: 二维字符数组，表示网格地形，'P'表示平原，'H'表示山地
// 返回值: 最多能部署的炮兵部队数量
int artilleryPosition(int n, int m, char grid[][MAXM]) {
    // 预处理每一行的合法地形状态
    // validStates[i] 表示第i行的地形状态，用二进制位表示哪些位置是平原（可以部署炮兵）
    int validStates[MAXN];
    for (int i = 0; i < n; i++) {
        validStates[i] = 0;
        for (int j = 0; j < m; j++) {
            if (grid[i][j] == 'P') {
                validStates[i] |= (1 << j);  // 将第j位设为1，表示位置j是平原
            }
        }
    }

    // 预处理所有可能的行状态（同一行内炮兵不互相攻击）
    // allStates数组存储所有合法的行状态
    // stateCount数组存储每个状态对应的炮兵数量
    // totalStates记录合法状态数量
    int allStates[MAX_STATES];
    int stateCount[MAX_STATES];
    int totalStates = 0;
    for (int i = 0; i < (1 << m); i++) {
        // 检查同一行内炮兵是否互相攻击（距离小于等于2）
        // (i << 1) 检查相邻位置是否有炮兵
        // (i << 2) 检查相隔一个位置是否有炮兵
        // (i >> 1) 检查相邻位置是否有炮兵
        // (i >> 2) 检查相隔一个位置是否有炮兵
        if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 && 
            (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
            allStates[totalStates] = i;
            stateCount[totalStates] = bitCount(i); // 计算该状态下的炮兵数量
            totalStates++;
        }
    }

    // dp[i][mask1][mask2] 表示处理到第i行，第i-1行的部署状态为mask1，第i-2行的部署状态为mask2时的最大炮兵数
    // 使用-1表示状态不可达
    int dp[MAXN][MAX_STATES][MAX_STATES];
    
    // 初始化DP数组
    for (int i = 0; i <= n; i++) {
        for (int j = 0; j < MAX_STATES; j++) {
            for (int k = 0; k < MAX_STATES; k++) {
                dp[i][j][k] = -1;
            }
        }
    }
    dp[0][0][0] = 0;  // 初始状态：处理第0行，前两行都无炮兵的状态下炮兵数为0

    // 状态转移过程
    for (int i = 1; i <= n; i++) {
        // 枚举所有合法的行状态
        for (int j = 0; j < totalStates; j++) {
            int state = allStates[j];
            int count = stateCount[j];  // 当前行部署的炮兵数量
            
            // 检查当前状态是否在当前行的合法地形内
            // 如果(state & validStates[i - 1]) != state，说明state中有某些位置在地形上是山地
            if ((state & validStates[i - 1]) != state) {
                continue;
            }

            // 枚举前两行的状态
            for (int mask1 = 0; mask1 < (1 << m); mask1++) {
                // 如果前一行的状态不可达，跳过
                if (dp[i - 1][mask1][0] == -1) continue;
                
                for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                    // 如果前两行的状态组合不可达，跳过
                    if (dp[i - 1][mask1][mask2] == -1) continue;
                    
                    // 检查当前行与前一行、前两行是否冲突
                    // (state & mask1) == 0 表示当前行与前一行无上下相邻
                    // (state & mask2) == 0 表示当前行与前两行无上下相隔一个位置的冲突
                    if ((state & mask1) == 0 && (state & mask2) == 0) {
                        int newValue = dp[i - 1][mask1][mask2] + count;
                        // 更新最大炮兵数
                        if (dp[i][state][mask1] < newValue) {
                            dp[i][state][mask1] = newValue;
                        }
                    }
                }
            }
        }
    }

    // 计算最终结果：遍历所有可能的状态组合，找到最大炮兵数
    int result = 0;
    for (int mask1 = 0; mask1 < (1 << m); mask1++) {
        for (int mask2 = 0; mask2 < (1 << m); mask2++) {
            if (dp[n][mask1][mask2] > result) {
                result = dp[n][mask1][mask2];
            }
        }
    }
    return result;
}

// 空间优化版本
// 通过滚动数组优化空间复杂度，只使用三个二维数组
int artilleryPositionOptimized(int n, int m, char grid[][MAXM]) {
    // 预处理每一行的合法地形状态
    int validStates[MAXN];
    for (int i = 0; i < n; i++) {
        validStates[i] = 0;
        for (int j = 0; j < m; j++) {
            if (grid[i][j] == 'P') {
                validStates[i] |= (1 << j);
            }
        }
    }

    // 预处理所有可能的行状态（同一行内炮兵不互相攻击）
    int allStates[MAX_STATES];
    int stateCount[MAX_STATES];
    int totalStates = 0;
    for (int i = 0; i < (1 << m); i++) {
        // 检查同一行内炮兵是否互相攻击（距离小于等于2）
        if ((i & (i << 1)) == 0 && (i & (i << 2)) == 0 && 
            (i & (i >> 1)) == 0 && (i & (i >> 2)) == 0) {
            allStates[totalStates] = i;
            stateCount[totalStates] = bitCount(i); // 计算该状态下的炮兵数量
            totalStates++;
        }
    }

    // 空间优化的DP数组
    // 只需要保存当前行、前一行和前两行的状态，使用滚动数组优化空间
    int prev2[MAX_STATES][MAX_STATES];  // 前两行状态
    int prev1[MAX_STATES][MAX_STATES];  // 前一行状态
    int current[MAX_STATES][MAX_STATES]; // 当前行状态
    
    // 初始化DP数组
    for (int i = 0; i < MAX_STATES; i++) {
        for (int j = 0; j < MAX_STATES; j++) {
            prev2[i][j] = -1;
            prev1[i][j] = -1;
            current[i][j] = -1;
        }
    }
    prev2[0][0] = 0;  // 初始状态

    // 状态转移过程
    for (int i = 1; i <= n; i++) {
        // 初始化当前状态数组
        for (int x = 0; x < MAX_STATES; x++) {
            for (int y = 0; y < MAX_STATES; y++) {
                current[x][y] = -1;
            }
        }
        
        // 枚举所有合法的行状态
        for (int j = 0; j < totalStates; j++) {
            int state = allStates[j];
            int count = stateCount[j];
            
            // 检查当前状态是否在当前行的合法地形内
            if ((state & validStates[i - 1]) != state) {
                continue;
            }

            // 枚举前两行的状态
            for (int mask1 = 0; mask1 < (1 << m); mask1++) {
                if (prev1[mask1][0] == -1) continue;
                
                for (int mask2 = 0; mask2 < (1 << m); mask2++) {
                    if (prev1[mask1][mask2] == -1) continue;
                    
                    // 检查当前行与前一行、前两行是否冲突
                    if ((state & mask1) == 0 && (state & mask2) == 0) {
                        int newValue = prev1[mask1][mask2] + count;
                        if (current[state][mask1] < newValue) {
                            current[state][mask1] = newValue;
                        }
                    }
                }
            }
        }
        
        // 交换数组，将current的值复制到prev1，prev1的值复制到prev2，为下一次迭代做准备
        int temp[MAX_STATES][MAX_STATES];
        for (int x = 0; x < MAX_STATES; x++) {
            for (int y = 0; y < MAX_STATES; y++) {
                temp[x][y] = prev2[x][y];
                prev2[x][y] = prev1[x][y];
                prev1[x][y] = current[x][y];
                current[x][y] = temp[x][y];
            }
        }
    }

    // 计算最终结果
    int result = 0;
    for (int mask1 = 0; mask1 < (1 << m); mask1++) {
        for (int mask2 = 0; mask2 < (1 << m); mask2++) {
            if (prev1[mask1][mask2] > result) {
                result = prev1[mask1][mask2];
            }
        }
    }
    return result;
}

// CodeForces 165E 最大兼容数对解法
// 参数说明:
// n: 数组长度
// nums: 输入数组
// result: 输出数组，result[i]表示与nums[i]兼容的数的索引，如果不存在则为-1
void compatibleNumbers(int n, int* nums, int* result) {
    // 找到数组中的最大值
    int maxVal = 0;
    for (int i = 0; i < n; i++) {
        if (nums[i] > maxVal) {
            maxVal = nums[i];
        }
    }
    
    // 找到最大值的位数
    int bits = 0;
    while ((1 << bits) <= maxVal) {
        bits++;
    }
    
    // 预处理每个数字的补集
    // complement[i] 表示数字i在数组中的索引，如果不存在则为-1
    int complement[1 << 10]; // 假设最多10位
    for (int i = 0; i < (1 << bits); i++) {
        complement[i] = -1;
    }
    
    // 将数组中的数字存入complement数组
    for (int i = 0; i < n; i++) {
        complement[nums[i]] = i;
    }
    
    // 初始化结果数组
    for (int i = 0; i < n; i++) {
        result[i] = -1;
    }
    
    // 对每个数字寻找兼容数
    for (int i = 0; i < n; i++) {
        int num = nums[i];
        // 计算num的补集：((1 << bits) - 1) ^ num
        // ((1 << bits) - 1) 生成一个bits位全为1的数
        // ^ num 表示与num进行异或运算，得到补集
        int mask = ((1 << bits) - 1) ^ num;
        
        // 枚举num的补集的所有子集
        // (subMask - 1) & mask 是枚举子集的标准写法
        for (int subMask = mask; subMask > 0; subMask = (subMask - 1) & mask) {
            if (complement[subMask] != -1) {
                result[i] = complement[subMask];
                break;
            }
        }
    }
}

// 主函数 - 用于测试
int main() {
    // 由于编译环境限制，这里不包含测试代码
    // 实际使用时可以添加适当的测试代码
    return 0;
}