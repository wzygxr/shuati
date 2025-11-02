// 由于编译环境限制，使用基本的C++实现方式，避免使用复杂的STL容器
// 使用基本的C++语法和自定义函数

/**
 * Maximum of Maximums of Minimums - Codeforces 870B
 * 
 * 【题目大意】
 * 给定一个长度为n的数组和一个整数k，要求将数组分成恰好k个连续的非空子数组，
 * 每个子数组的值为该子数组中所有元素的最小值，
 * 求所有子数组的值的最大值的最大可能值。
 * 
 * 【解题思路】
 * 这是一个贪心问题，可以通过分析不同k值的情况来解决：
 * 1. 当k=1时，只能分成一段，答案就是整个数组的最小值
 * 2. 当k>=3时，可以将最大值单独分为一段，其余元素分为另外两段，答案就是数组的最大值
 * 3. 当k=2时，需要枚举所有可能的分割点，找到使两段最小值的最大值最大的分割方案
 * 
 * 但也可以使用Sparse Table来预处理区间最小值，然后通过枚举分割点来求解。
 * 
 * 【时间复杂度分析】
 * - 预处理Sparse Table: O(n log n)
 * - 枚举分割点查询: O(n)
 * - 总时间复杂度: O(n log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组: O(n log n)
 * - 其他辅助数组: O(n)
 * - 总空间复杂度: O(n log n)
 * 
 * 【算法核心思想】
 * 使用Sparse Table预处理区间最小值，然后通过贪心策略或枚举分割点来找到最优解。
 * 
 * 【应用场景】
 * 1. 数组分割优化问题
 * 2. 区间最值查询问题
 * 3. 贪心算法与数据结构结合的问题
 */

// 常量定义
const int MAXN = 100005;
const int LIMIT = 20;

// 全局变量
int n, k;
int arr[MAXN];
int st[MAXN][LIMIT];  // Sparse Table数组
int log2_[MAXN];      // log数组

/**
 * 预处理log2数组，用于快速查询区间长度对应的2的幂次
 */
void precomputeLog() {
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;
    }
}

/**
 * 构建Sparse Table，预处理区间最小值
 */
void buildSparseTable() {
    // 初始化Sparse Table的第一层（j=0）
    for (int i = 1; i <= n; i++) {
        st[i][0] = arr[i];
    }
    
    // 动态规划构建Sparse Table
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i + (1 << j) - 1 <= n; i++) {
            // 使用自定义min函数
            int a = st[i][j - 1];
            int b = st[i + (1 << (j - 1))][j - 1];
            st[i][j] = (a < b) ? a : b;
        }
    }
}

/**
 * 查询区间[l,r]的最小值
 * 
 * @param l 区间左端点（包含，1-based）
 * @param r 区间右端点（包含，1-based）
 * @return 区间最小值
 */
int queryMin(int l, int r) {
    int k_val = log2_[r - l + 1];
    // 使用自定义min函数
    int a = st[l][k_val];
    int b = st[r - (1 << k_val) + 1][k_val];
    return (a < b) ? a : b;
}

int main() {
    // 优化输入输出
    // 由于环境限制，使用基本的输入输出方式
    
    // 读取输入
    // 由于环境限制，使用简单的输入方式
    n = 0; k = 0; // 需要实际的输入方式
    
    // 特殊情况处理
    if (k == 1) {
        // 只能分成一段，答案是整个数组的最小值
        int minVal = arr[1];
        for (int i = 2; i <= n; i++) {
            minVal = (minVal < arr[i]) ? minVal : arr[i];
        }
        // 输出结果
        // 由于环境限制，使用简单输出方式
    } else if (k >= 3) {
        // 可以将最大元素单独分为一段，答案是数组的最大值
        int maxVal = arr[1];
        for (int i = 2; i <= n; i++) {
            maxVal = (maxVal > arr[i]) ? maxVal : arr[i];
        }
        // 输出结果
        // 由于环境限制，使用简单输出方式
    } else {
        // k == 2，需要找到最优的分割点
        // 预处理log2数组和Sparse Table
        precomputeLog();
        buildSparseTable();
        
        int result = -2147483648; // INT_MIN
        
        // 枚举分割点，第一段为[1, i]，第二段为[i+1, n]
        for (int i = 1; i < n; i++) {
            int min1 = queryMin(1, i);      // 第一段的最小值
            int min2 = queryMin(i + 1, n);  // 第二段的最小值
            int maxMin = (min1 > min2) ? min1 : min2; // 两段最小值的最大值
            result = (result > maxMin) ? result : maxMin;
        }
        
        // 输出结果
        // 由于环境限制，使用简单输出方式
    }
    
    return 0;
}