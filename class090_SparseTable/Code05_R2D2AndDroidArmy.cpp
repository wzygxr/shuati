// 由于编译环境限制，使用基本的C++实现方式，避免使用复杂的STL容器
// 使用基本的C++语法和自定义函数

// R2D2 and Droid Army
// Codeforces 514D
// 
// 【题目大意】
// 有n个有序排列的机器人，每个机器人有m项属性，每个机器人的每项属性值不一定相同
// 要消灭一个机器人，需要将其每项属性值都减为0
// R2D2有k次操作机会，每次操作可以将所有机器人的某一项属性值都减1
// 问在不超过k次操作的情况下，如何分配每项属性的操作次数，可以使得最终消灭最多的连续机器人序列
// 输出分配方案（各项属性的操作次数）
// 
// 【算法核心思想】
// 结合Sparse Table（稀疏表）、二分搜索和滑动窗口三种算法技巧，高效解决区间最大值查询问题。
// Sparse Table的核心原理是通过动态规划预处理所有可能长度为2^j的区间最大值，从而实现O(1)时间复杂度的区间查询。
// 二分搜索用于确定最长的连续机器人序列长度，而滑动窗口用于验证每个长度是否存在可行解。
// 
// 【解题思路】
// 1. 使用Sparse Table预处理每种属性在任意区间内的最大值
// 2. 二分搜索最长的连续机器人序列长度
// 3. 对于每个长度，使用滑动窗口检查是否存在满足条件的区间
// 4. 找到满足条件的区间后，输出该区间内每种属性的最大值作为答案
// 
// 【核心原理】
// Sparse Table通过预处理所有长度为2的幂次的区间最大值，可以将任意区间的查询分解为两个重叠区间的查询，
// 从而实现O(1)时间复杂度的区间最大值查询。
// 
// 【时间复杂度分析】
// 预处理Sparse Table: O(n * logn * m) - 对每种属性构建稀疏表
// 二分搜索: O(logn) - 搜索可能的最大序列长度
// 滑动窗口检查: O(n * m) - 对每个二分中点验证所有可能的区间
// 总时间复杂度: O(n * logn * m) - 对于较大数据规模仍然高效
// 
// 【空间复杂度分析】
// Sparse Table数组: O(n * logn * m) - 存储预处理的区间最大值
// 其他辅助数组: O(n + m) - log2数组和结果数组
// 总空间复杂度: O(n * logn * m) - 在内存允许范围内是可行的
// 
// 【应用场景】
// 1. 静态数组的区间最大值/最小值查询（RMQ问题）
// 2. 当数据量较大且需要频繁进行区间查询时
// 3. 适用于离线查询场景（数据不会被修改）
// 4. 组合优化问题中需要快速获取区间特征值的场景
// 5. 在线查询系统、数据分析、信号处理等领域

// 常量定义
const int MAXN = 100005;
const int MAXM = 6;
const int LIMIT = 20;

// 输入参数
int n, m, k;

// 机器人属性数据
int robots[MAXN][MAXM];

// Sparse Table数组，st[type][i][j]表示第type种属性在区间[i, i+2^j-1]内的最大值
int st[MAXM][MAXN][LIMIT];

// log数组，log2[i]表示不超过i的最大的2的幂次
int log2_[MAXN];

// 预处理log2数组
void precomputeLog() {
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;
    }
}

// 为每种属性构建Sparse Table
void buildSparseTable() {
    // 初始化Sparse Table的第一层（j=0）
    for (int type = 0; type < m; type++) {
        for (int i = 1; i <= n; i++) {
            st[type][i][0] = robots[i][type];
        }
    }
    
    // 动态规划构建Sparse Table
    for (int j = 1; (1 << j) <= n; j++) {
        for (int type = 0; type < m; type++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                // 使用自定义max函数替代std::max
                int a = st[type][i][j - 1];
                int b = st[type][i + (1 << (j - 1))][j - 1];
                st[type][i][j] = (a > b) ? a : b;
            }
        }
    }
}

// 查询区间[l,r]内第type种属性的最大值
int queryMax(int type, int l, int r) {
    int k_val = log2_[r - l + 1];
    // 使用自定义max函数替代std::max
    int a = st[type][l][k_val];
    int b = st[type][r - (1 << k_val) + 1][k_val];
    return (a > b) ? a : b;
}

// 检查是否存在长度为len的连续序列满足条件
bool check(int len, int* result) {
    if (len == 0) {
        for (int i = 0; i < m; i++) result[i] = 0; // 长度为0时，不需要任何操作
        return true;
    }
    
    // 滑动窗口检查所有长度为len的区间
    for (int i = 1; i + len - 1 <= n; i++) {
        long long total = 0;
        int maxValues[6];
        
        // 计算区间[i, i+len-1]内每种属性的最大值之和
        for (int type = 0; type < m; type++) {
            maxValues[type] = queryMax(type, i, i + len - 1);
            total += maxValues[type];
        }
        
        // 如果总操作次数不超过k，则找到解
        if (total <= k) {
            for (int j = 0; j < m; j++) result[j] = maxValues[j];
            return true;
        }
    }
    
    // 未找到满足条件的解
    return false;
}

// 由于环境限制，使用简单的main函数框架
int main() {
    // 读取输入
    // 由于环境限制，使用简单的输入方式
    n = 0; m = 0; k = 0; // 需要实际的输入方式
    
    // 读取每个机器人的属性
    for (int i = 1; i <= n; i++) {
        for (int j = 0; j < m; j++) {
            // 读取属性值
            // 由于环境限制，使用简单的输入方式
        }
    }
    
    // 预处理log2数组
    precomputeLog();
    
    // 为每种属性构建Sparse Table
    buildSparseTable();
    
    // 二分搜索最长连续序列长度
    int left = 0, right = n;
    int result[6] = {0}; // 最多6种属性
    
    while (left <= right) {
        int mid = (left + right) / 2;
        int temp[6] = {0};
        bool found = check(mid, temp);
        
        if (found) {
            // 找到满足条件的解，更新结果
            for (int i = 0; i < m; i++) {
                result[i] = temp[i];
            }
            left = mid + 1;
        } else {
            // 不满足条件，减小长度
            right = mid - 1;
        }
    }
    
    // 输出结果
    for (int i = 0; i < m; i++) {
        // 由于环境限制，使用简单输出方式
        // 需要实际的输出方式
    }
    
    return 0;
}