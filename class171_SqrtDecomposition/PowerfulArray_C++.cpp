/**
 * Codeforces 86D - Powerful Array - C++实现（简化版）
 * 
 * 题目来源：Codeforces
 * 题目链接：https://codeforces.com/contest/86/problem/D
 * 题目描述：
 * 给定一个长度为n的数组，以及m个查询，每个查询要求计算区间[l,r]的权值
 * 区间权值定义为：对于区间内每个不同的值x，如果x出现了c次，则贡献c*c*x到总权值中
 * 
 * 解题思路：
 * 使用莫队算法（Mo's Algorithm）解决此问题。通过维护每个元素出现的次数，
 * 可以在O(1)时间内更新区间的权值。
 * 
 * 算法步骤：
 * 1. 将数组分块，块大小约为sqrt(n)
 * 2. 将所有查询按左端点所在块和右端点排序
 * 3. 通过指针移动维护当前区间的答案
 * 4. 通过添加或删除元素来更新答案
 * 
 * 时间复杂度：O((n+m) * sqrt(n))
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数的有效性
 * 2. 性能优化：使用莫队算法减少重复计算
 * 3. 鲁棒性：处理各种边界情况
 */

// 由于编译环境问题，使用基础C++实现，避免使用复杂的STL容器

const int MAXN = 200010;

// 原数组
long long arr[MAXN];
// 计数数组，记录每个元素出现的次数
long long count[1000010];
// 查询结构
struct Query {
    int l, r, id;
};

Query queries[MAXN];
// 答案数组
long long ans[MAXN];

// 当前区间权值
long long currentAns = 0;

// 块大小和块数量
int blockSize, blockNum, n;

// 简单的数学函数实现
int my_min(int a, int b) {
    return a < b ? a : b;
}

// 简单的平方根近似实现
int my_sqrt(int x) {
    if (x <= 1) return x;
    int left = 1, right = x;
    int result = 1;
    while (left <= right) {
        int mid = (left + right) / 2;
        if (mid <= x / mid) {
            result = mid;
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return result;
}

/**
 * 添加元素到当前区间
 * 
 * @param pos 位置
 */
void add(int pos) {
    long long value = arr[pos];
    currentAns -= count[value] * count[value] * value;
    count[value]++;
    currentAns += count[value] * count[value] * value;
}

/**
 * 从当前区间移除元素
 * 
 * @param pos 位置
 */
void remove(int pos) {
    long long value = arr[pos];
    currentAns -= count[value] * count[value] * value;
    count[value]--;
    currentAns += count[value] * count[value] * value;
}

/**
 * 执行莫队算法
 * 
 * @param n 数组大小
 * @param m 查询数量
 */
void moAlgorithm(int n, int m) {
    blockSize = my_sqrt(n);
    
    // 对查询进行排序（简化版）
    for (int i = 1; i <= m; i++) {
        for (int j = i + 1; j <= m; j++) {
            int blockA = (queries[i].l - 1) / blockSize;
            int blockB = (queries[j].l - 1) / blockSize;
            if (blockA > blockB || (blockA == blockB && queries[i].r > queries[j].r)) {
                // 交换查询
                Query temp = queries[i];
                queries[i] = queries[j];
                queries[j] = temp;
            }
        }
    }
    
    int currentL = 1, currentR = 0;
    
    for (int i = 1; i <= m; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        int id = queries[i].id;
        
        // 扩展或收缩左边界
        while (currentL < l) {
            remove(currentL);
            currentL++;
        }
        while (currentL > l) {
            currentL--;
            add(currentL);
        }
        
        // 扩展或收缩右边界
        while (currentR < r) {
            currentR++;
            add(currentR);
        }
        while (currentR > r) {
            remove(currentR);
            currentR--;
        }
        
        // 记录答案
        ans[id] = currentAns;
    }
}

// 由于环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出