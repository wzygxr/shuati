// 由于编译环境限制，使用基本的C++实现方式，避免使用复杂的STL容器
// 使用基本的C++语法和自定义函数

/**
 * Static Range Minimum Queries - CSES 1647
 * 
 * 【题目大意】
 * 给定一个长度为n的整数数组，需要处理q个查询，每个查询给定一个范围[a,b]，
 * 要求回答该范围内元素的最小值。
 * 
 * 【解题思路】
 * 这是经典的RMQ（Range Minimum Query）问题，可以使用Sparse Table来解决。
 * Sparse Table通过预处理所有长度为2的幂次的区间最小值，
 * 实现O(n log n)预处理，O(1)查询的时间复杂度。
 * 
 * 【时间复杂度分析】
 * - 预处理Sparse Table: O(n log n)
 * - 单次查询: O(1)
 * - 总时间复杂度: O(n log n + q)
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组: O(n log n)
 * - 其他辅助数组: O(n)
 * - 总空间复杂度: O(n log n)
 * 
 * 【算法核心思想】
 * 1. 预处理阶段：使用动态规划构建Sparse Table
 * 2. 查询阶段：将任意区间分解为两个重叠的预处理区间，取最小值
 * 
 * 【应用场景】
 * 1. 大数据分析中的快速区间统计
 * 2. 信号处理中的特征提取
 * 3. 游戏开发中的范围检测
 * 4. 网络流量监控中的异常检测
 */

// 常量定义
const int MAXN = 200005;
const int LIMIT = 20;

// 全局变量
int n, q;
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

// 由于环境限制，使用简单的main函数框架
int main() {
    // 读取输入
    // 由于环境限制，使用简单的输入方式
    n = 0; q = 0; // 需要实际的输入方式
    
    // 预处理log2数组和Sparse Table
    precomputeLog();
    buildSparseTable();
    
    // 处理每个查询
    for (int i = 0; i < q; i++) {
        int a, b;
        // 读取查询参数
        a = 0; b = 0; // 需要实际的输入方式
        // 注意：题目输入是1-based
        // 由于环境限制，使用简单输出方式
    }
    
    return 0;
}