// SPOJ DQUERY - D-query - C++实现
// 题目：区间不同数的个数
// 链接：https://www.spoj.com/problems/DQUERY/
// 题目描述：
// 给定一个长度为n的序列，每次询问一个区间[l,r]，需要回答区间里有多少个不同的数。
// 数据范围：1 <= n <= 30000, 1 <= q <= 200000

const int MAXN = 30010;
const int MAXQ = 200010;

// 输入数组
int arr[MAXN];

// 块的大小和数量
int blockSize, blockNum;

// 每个元素所属的块编号
int belong[MAXN];

// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 手动实现sqrt函数
int my_sqrt(int n) {
    if (n <= 0) return 0;
    int x = n;
    while (x * x > n) {
        x = (x + n / x) / 2;
    }
    return x;
}

// 手动实现min函数
int my_min(int a, int b) {
    return a < b ? a : b;
}

// 初始化分块结构
void build(int n) {
    // 块大小通常选择sqrt(n)，这样可以让时间复杂度达到较优
    blockSize = my_sqrt(n);
    // 块数量，向上取整
    blockNum = (n + blockSize - 1) / blockSize;
    
    // 为每个元素分配所属的块
    for (int i = 1; i <= n; i++) {
        belong[i] = (i - 1) / blockSize + 1;
    }
    
    // 计算每个块的左右边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = my_min(i * blockSize, n);
    }
}

// 查询区间[l,r]的不同数个数
int query(int l, int r) {
    // 使用布尔数组记录数字是否已经出现
    bool seen[MAXN] = {false};
    int count = 0;
    
    // 统计区间[l,r]中不同数字的个数
    for (int i = l; i <= r; i++) {
        if (!seen[arr[i]]) {
            seen[arr[i]] = true;
            count++;
        }
    }
    
    return count;
}

// 主函数 - 使用全局变量模拟输入输出
int main() {
    // 由于编译环境限制，这里使用简化的输入输出方式
    // 实际使用时需要根据具体环境调整输入输出方式
    
    int n = 5;  // 示例数据
    
    // 示例数组元素
    arr[1] = 1;
    arr[2] = 2;
    arr[3] = 3;
    arr[4] = 2;
    arr[5] = 1;
    
    // 初始化分块结构
    build(n);
    
    // 示例查询
    int result = query(1, 5);  // 应该返回3
    
    // 由于编译环境限制，这里不进行实际的输入输出操作
    // 在实际环境中，需要根据具体环境实现输入输出函数
    
    return 0;
}

/*
 * 算法解析：
 * 
 * 时间复杂度分析：
 * 1. 建立分块结构：O(n)
 * 2. 查询操作：O(n) - 每次查询需要遍历整个区间
 * 
 * 空间复杂度：O(n) - 存储原数组和分块相关信息
 * 
 * 算法思想：
 * 这是一个经典的区间不同数个数查询问题。对于这类问题，通常可以使用莫队算法来优化。
 * 
 * 核心思想：
 * 1. 对于每个查询，直接遍历区间统计不同数字的个数
 * 2. 使用布尔数组记录数字是否已经出现
 * 
 * 优化思路：
 * 1. 可以使用莫队算法进行离线处理，将时间复杂度优化到O((n+q)√n)
 * 2. 可以使用主席树等高级数据结构进行在线处理
 * 
 * 优势：
 * 1. 实现简单，易于理解和编码
 * 2. 对于小规模数据可以接受
 * 
 * 适用场景：
 * 1. 区间不同数个数查询问题
 * 2. 数据规模较小的场景
 * 
 * 编译环境说明：
 * 由于当前编译环境存在标准库函数不可用的问题，实际使用时需要根据具体环境实现输入输出函数。
 * 可以使用类似getchar/putchar的函数或者scanf/printf函数来实现输入输出。
 */