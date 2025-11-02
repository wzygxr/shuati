// 洛谷 P4145 上帝造题的七分钟2 / 花神游历各国 - C++实现
// 题目：区间开方，区间求和
// 链接：https://www.luogu.com.cn/problem/P4145
// 题目描述：
// 给定一个长度为n的序列，支持区间开方（下取整）操作和区间求和查询。
// 操作：
// 0 l r : 给[l,r]中每个数开平方（下取整）
// 1 l r : 询问[l,r]中各个数的和
// 数据范围：1 <= n <= 1000000

const int MAXN = 1000010;

// 输入数组
long long arr[MAXN];

// 块的大小和数量
int blockSize, blockNum;

// 每个元素所属的块编号
int belong[MAXN];

// 每个块的左右边界
int blockLeft[MAXN], blockRight[MAXN];

// 每个块是否全为0或1的标记
bool isZeroOne[MAXN];

// 每个块的元素和
long long sum[MAXN];

// 手动实现sqrt函数
int my_sqrt(long long n) {
    if (n <= 0) return 0;
    long long x = n;
    while (x * x > n) {
        x = (x + n / x) / 2;
    }
    return (int)x;
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
    
    // 初始化每个块的元素和和标记
    for (int i = 1; i <= blockNum; i++) {
        sum[i] = 0;
        isZeroOne[i] = true;
        for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
            sum[i] += arr[j];
            if (arr[j] != 0 && arr[j] != 1) {
                isZeroOne[i] = false;
            }
        }
    }
}

// 区间开方操作
// 将区间[l,r]中的每个元素都开方（下取整）
void update(int l, int r) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    
    // 如果区间在同一个块内，直接暴力处理
    if (belongL == belongR) {
        // 直接对区间内每个元素开方
        for (int i = l; i <= r; i++) {
            sum[belongL] -= arr[i];
            arr[i] = (long long)my_sqrt(arr[i]);
            sum[belongL] += arr[i];
        }
        // 检查块是否全为0或1
        isZeroOne[belongL] = true;
        for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
            if (arr[i] != 0 && arr[i] != 1) {
                isZeroOne[belongL] = false;
                break;
            }
        }
        return;
    }
    
    // 处理左端点所在的不完整块
    for (int i = l; i <= blockRight[belongL]; i++) {
        sum[belongL] -= arr[i];
        arr[i] = (long long)my_sqrt(arr[i]);
        sum[belongL] += arr[i];
    }
    // 检查块是否全为0或1
    isZeroOne[belongL] = true;
    for (int i = blockLeft[belongL]; i <= blockRight[belongL]; i++) {
        if (arr[i] != 0 && arr[i] != 1) {
            isZeroOne[belongL] = false;
            break;
        }
    }
    
    // 处理右端点所在的不完整块
    for (int i = blockLeft[belongR]; i <= r; i++) {
        sum[belongR] -= arr[i];
        arr[i] = (long long)my_sqrt(arr[i]);
        sum[belongR] += arr[i];
    }
    // 检查块是否全为0或1
    isZeroOne[belongR] = true;
    for (int i = blockLeft[belongR]; i <= blockRight[belongR]; i++) {
        if (arr[i] != 0 && arr[i] != 1) {
            isZeroOne[belongR] = false;
            break;
        }
    }
    
    // 处理中间的完整块
    for (int i = belongL + 1; i < belongR; i++) {
        // 如果块已经全为0或1，则无需处理
        if (isZeroOne[i]) {
            continue;
        }
        
        // 对块内每个元素开方
        for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
            sum[i] -= arr[j];
            arr[j] = (long long)my_sqrt(arr[j]);
            sum[i] += arr[j];
        }
        
        // 检查块是否全为0或1
        isZeroOne[i] = true;
        for (int j = blockLeft[i]; j <= blockRight[i]; j++) {
            if (arr[j] != 0 && arr[j] != 1) {
                isZeroOne[i] = false;
                break;
            }
        }
    }
}

// 查询区间[l,r]的和
long long query(int l, int r) {
    int belongL = belong[l];  // 左端点所属块
    int belongR = belong[r];  // 右端点所属块
    long long result = 0;
    
    // 如果区间在同一个块内，直接暴力统计
    if (belongL == belongR) {
        for (int i = l; i <= r; i++) {
            result += arr[i];
        }
        return result;
    }
    
    // 处理左端点所在的不完整块
    for (int i = l; i <= blockRight[belongL]; i++) {
        result += arr[i];
    }
    
    // 处理右端点所在的不完整块
    for (int i = blockLeft[belongR]; i <= r; i++) {
        result += arr[i];
    }
    
    // 处理中间的完整块
    for (int i = belongL + 1; i < belongR; i++) {
        result += sum[i];
    }
    
    return result;
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
    arr[4] = 4;
    arr[5] = 5;
    
    // 初始化分块结构
    build(n);
    
    // 示例操作
    // 操作 0 1 3 : 将[1,3]区间开方
    update(1, 3);
    
    // 操作 1 1 5 : 查询[1,5]区间的和
    long long result = query(1, 5);  // 应该返回结果
    
    // 由于编译环境限制，这里不进行实际的输入输出操作
    // 在实际环境中，需要根据具体环境实现输入输出函数
    
    return 0;
}

/*
 * 算法解析：
 * 
 * 时间复杂度分析：
 * 1. 建立分块结构：O(n)
 * 2. 区间更新操作：O(√n) - 最多处理两个不完整块(2*√n)和一些完整块(√n)
 * 3. 区间查询操作：O(√n) - 处理两个不完整块和一些完整块
 * 
 * 空间复杂度：O(n) - 存储原数组和分块相关信息
 * 
 * 算法思想：
 * 分块是一种"优雅的暴力"算法，通过将数组分成大小约为√n的块来平衡时间复杂度。
 * 
 * 核心思想：
 * 1. 对于不完整的块（区间端点所在的块），直接暴力处理
 * 2. 对于完整的块，使用标记优化，如果块内元素全为0或1则无需处理
 * 3. 维护每个块的元素和，快速计算完整块的和
 * 
 * 优化技巧：
 * 利用开方次数有限的特性，当块内元素全为0或1时，无需再进行开方操作。
 * 
 * 优势：
 * 1. 实现相对简单，比线段树等数据结构容易理解和编码
 * 2. 利用开方特性进行优化，提高实际运行效率
 * 3. 可以处理大多数区间操作问题
 * 
 * 适用场景：
 * 1. 需要区间开方和区间求和的问题
 * 2. 操作具有有限次数特性的场景
 * 
 * 编译环境说明：
 * 由于当前编译环境存在标准库函数不可用的问题，实际使用时需要根据具体环境实现输入输出函数。
 * 可以使用类似getchar/putchar的函数或者scanf/printf函数来实现输入输出。
 */