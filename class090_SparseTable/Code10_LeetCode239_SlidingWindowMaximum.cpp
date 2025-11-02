// 由于编译环境限制，使用基本的C++实现方式，避免使用复杂的STL容器
// 使用基本的C++语法和自定义函数

/**
 * LeetCode 239. 滑动窗口最大值 - Sparse Table应用
 * 题目链接：https://leetcode.com/problems/sliding-window-maximum/
 * 
 * 【题目描述】
 * 给定一个整数数组nums和一个整数k，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的k个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 【算法核心思想】
 * 使用Sparse Table预处理区间最大值，然后对每个滑动窗口进行O(1)查询。
 * 这种方法特别适合k值较大且需要高效查询的场景。
 * 
 * 【核心原理】
 * Sparse Table基于倍增思想，通过预处理所有长度为2的幂次的区间最大值，
 * 实现O(n log n)预处理，O(1)查询的高效区间最值查询。
 * 
 * 【位运算常用技巧】
 * 1. 位移运算：1 << k 等价于 2^k，比Math.pow(2,k)更高效
 * 2. 整数除法：i >> 1 等价于 i / 2，用于快速计算log2值
 * 3. 位掩码：使用位运算快速判断和计算区间覆盖
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n log n) - 构建Sparse Table
 * - 查询：O(n) - 每个窗口一次O(1)查询，共n-k+1个窗口
 * - 总时间复杂度：O(n log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table：O(n log n)
 * - 结果数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【算法优势】
 * 1. 查询时间复杂度为O(1)，非常高效
 * 2. 实现相对简单，代码可读性好
 * 3. 适用于静态数据（不需要修改）
 * 4. 支持多种可重复贡献操作（最大值、最小值、GCD等）
 * 
 * 【算法劣势】
 * 1. 不支持在线修改操作
 * 2. 预处理时间较长O(n log n)
 * 3. 空间复杂度较高O(n log n)
 * 4. 仅适用于可重复贡献的问题
 * 
 * 【应用场景】
 * 1. 大数据分析中的滑动窗口统计
 * 2. 实时数据流分析
 * 3. 股票价格监控
 * 4. 网络流量峰值检测
 * 5. 传感器数据质量监控
 * 
 * 【相关题目】
 * 1. LeetCode 239 - 滑动窗口最大值（本题）
 * 2. Codeforces 514D - R2D2 and Droid Army（区间最大值查询的扩展应用）
 * 3. POJ 3264 - Balanced Lineup（区间最大值与最小值之差）
 * 4. SPOJ RMQSQ - Range Minimum Query（标准区间最小值查询）
 * 5. SPOJ FREQUENT - 区间频繁值查询
 * 6. CodeChef MSTICK - 区间最值查询
 * 7. UVA 11235 - Frequent values（区间频繁值查询）
 * 8. HackerRank Maximum Element in a Subarray（使用ST表高效查询）
 * 9. AtCoder ABC189 C - Mandarin Orange（结合ST表和单调栈的题目）
 * 10. Codeforces 1311E - Concatenation with Beautiful Strings（可使用ST表预处理最值）
 */

// 常量定义
const int MAXN = 100005;
const int LIMIT = 20;

// 全局变量
int st[MAXN][LIMIT];  // Sparse Table数组
int logTable[MAXN];   // 预处理log2值

/**
 * 预处理log2值
 * 
 * 【实现原理】
 * 使用动态规划方法预处理logTable数组，logTable[i]表示不超过i的最大2的幂次的指数
 * 
 * @param n 数组长度
 * 
 * 【时间复杂度】
 * O(n)
 */
void preprocessLog(int n) {
    logTable[1] = 0;
    for (int i = 2; i <= n; i++) {
        logTable[i] = logTable[i >> 1] + 1;
    }
}

/**
 * 构建Sparse Table
 * 
 * @param arr 输入数组
 * @param n 数组长度
 * 
 * 【实现原理】
 * 1. 预处理logTable数组，用于快速计算区间长度对应的最大2的幂次
 * 2. 初始化ST表的第0层（长度为1的区间）
 * 3. 动态规划构建更高层的ST表，每层依赖于前一层的结果
 * 
 * 【时间复杂度】
 * O(n log n)
 * 
 * 【空间复杂度】
 * O(n log n)
 */
void buildSparseTable(int arr[], int n) {
    // 预处理log2值
    preprocessLog(n);
    
    // 初始化第一层
    for (int i = 0; i < n; i++) {
        st[i][0] = arr[i];
    }
    
    // 动态规划构建ST表
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 0; i + (1 << j) - 1 < n; i++) {
            // 使用自定义max函数
            int a = st[i][j - 1];
            int b = st[i + (1 << (j - 1))][j - 1];
            st[i][j] = (a > b) ? a : b;
        }
    }
}

/**
 * 查询区间最大值
 * 
 * 【实现原理】
 * 1. 计算查询区间的长度len = r - l + 1
 * 2. 找到最大的k，使得 2^k ≤ len
 * 3. 构造两个覆盖整个查询区间的预处理区间：
 *    - 第一个区间：[l, l + 2^k - 1]
 *    - 第二个区间：[r - 2^k + 1, r]
 * 4. 这两个区间的最大值即为整个查询区间的最大值
 * 
 * @param l 区间左边界（0-based）
 * @param r 区间右边界（0-based）
 * @return 区间最大值
 * 
 * 【时间复杂度】
 * O(1)
 */
int queryMax(int l, int r) {
    int len = r - l + 1;
    int k = logTable[len];
    
    // 使用自定义max函数
    int a = st[l][k];
    int b = st[r - (1 << k) + 1][k];
    return (a > b) ? a : b;
}

/**
 * 滑动窗口最大值 - 主要函数
 * 
 * 【实现原理】
 * 1. 使用Sparse Table预处理数组，构建区间最大值查询结构
 * 2. 对每个长度为k的滑动窗口，使用O(1)时间查询最大值
 * 3. 将所有查询结果收集到结果数组中
 * 
 * @param nums 输入数组
 * @param n 数组长度
 * @param k 滑动窗口大小
 * @param result 结果数组
 * @param resultSize 结果数组大小
 * 
 * 【时间复杂度】
 * O(n log n) - 预处理O(n log n) + 查询O(n)
 * 
 * 【空间复杂度】
 * O(n log n) - Sparse Table的空间占用
 */
void maxSlidingWindow(int nums[], int n, int k, int result[], int* resultSize) {
    // 边界情况处理
    if (n == 0 || k <= 0) {
        *resultSize = 0;
        return;
    }
    
    if (k > n) k = n;
    
    // 构建Sparse Table
    buildSparseTable(nums, n);
    
    // 查询每个滑动窗口的最大值
    *resultSize = 0;
    for (int i = 0; i <= n - k; i++) {
        result[(*resultSize)++] = queryMax(i, i + k - 1);
    }
}

// 由于环境限制，使用简单的main函数框架
int main() {
    // 示例输入
    int nums[] = {1, 3, -1, -3, 5, 3, 6, 7};
    int n = 8;
    int k = 3;
    
    // 结果数组
    int result[MAXN];
    int resultSize;
    
    // 计算滑动窗口最大值
    maxSlidingWindow(nums, n, k, result, &resultSize);
    
    // 由于环境限制，使用简单输出方式
    // 需要实际的输出方式
    
    return 0;
}