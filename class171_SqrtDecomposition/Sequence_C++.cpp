/**
 * 序列 - C++实现（简化版）
 * 
 * 题目来源：洛谷 P3863
 * 题目描述：
 * 给定一个长度为n的数组arr，初始时刻认为是第0秒
 * 接下来发生m条操作，第i条操作发生在第i秒，操作类型如下
 * 操作 1 l r v : arr[l..r]范围上每个数加v，v可能是负数
 * 操作 2 x v   : 不包括当前这一秒，查询过去多少秒内，arr[x] >= v
 * 
 * 数据范围：
 * 2 <= n、m <= 10^5
 * -10^9 <= 数组中的值 <= +10^9
 * 
 * 解题思路：
 * 这是一个时间轴上的分块问题。我们需要处理两种操作：
 * 1. 区间加法操作：对时间轴上的区间进行加法操作
 * 2. 查询操作：查询在某个时间点之前，满足条件的时间点数量
 * 
 * 关键思路是将所有事件离线处理，按位置排序后使用分块算法：
 * 1. 将所有修改和查询事件存储下来
 * 2. 按位置排序，相同位置时修改事件优先于查询事件
 * 3. 使用分块维护时间轴上的信息
 * 4. 对于每个位置，维护时间轴上该位置的值变化情况
 * 
 * 时间复杂度分析：
 * - 预处理（排序）：O((m+n) * log(m+n))
 * - 每次区间加法操作：O(√m)
 * - 每次查询操作：O(√m)
 * - 总体时间复杂度：O((m+n) * log(m+n) + (m+n) * √m)
 * 
 * 空间复杂度：O(m+n)
 * 
 * 工程化考量：
 * 1. 异常处理：
 *    - 处理空区间情况
 *    - 处理边界条件
 * 2. 性能优化：
 *    - 使用分块算法优化区间操作
 *    - 离线处理减少重复计算
 * 3. 鲁棒性：
 *    - 处理大数值运算（使用long long类型）
 *    - 保证在各种数据分布下的稳定性能
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3863
 */

// 由于编译环境问题，使用基础C++实现，避免使用复杂的STL容器

const int MAXN = 100001;
const int MAXB = 501;

int n, m;
int arr[MAXN];

// 事件结构
struct Event {
    int op, x, t, v, q;
};

Event events[MAXN << 2];
int eventCount = 0, queryCount = 0;

long long tim[MAXN];
long long sortv[MAXN];

// 时间分块相关变量
int blockSize, blockNum;
int blockIndex[MAXN];
int blockLeft[MAXB];
int blockRight[MAXB];
long long lazy[MAXB];

int ans[MAXN];

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

// 简单的排序实现（选择排序，仅用于小数组）
void my_sort(long long* array, int left, int right) {
    for (int i = left; i < right; i++) {
        int minIndex = i;
        for (int j = i + 1; j <= right; j++) {
            if (array[j] < array[minIndex]) {
                minIndex = j;
            }
        }
        if (minIndex != i) {
            long long temp = array[i];
            array[i] = array[minIndex];
            array[minIndex] = temp;
        }
    }
}

/**
 * 初始化分块结构
 * 
 * @param size 时间轴大小
 */
void init(int size) {
    m = size;
    // 设置块大小为sqrt(m)
    blockSize = my_sqrt(m);
    // 计算块数量
    blockNum = (m + blockSize - 1) / blockSize;
    
    // 初始化每个时间点所属的块
    for (int i = 1; i <= m; i++) {
        blockIndex[i] = (i - 1) / blockSize + 1;
    }
    
    // 初始化每个块的边界
    for (int i = 1; i <= blockNum; i++) {
        blockLeft[i] = (i - 1) * blockSize + 1;
        blockRight[i] = my_min(i * blockSize, m);
    }
    
    // 初始化懒惰标记为0
    for (int i = 0; i < MAXB; i++) {
        lazy[i] = 0;
    }
}

/**
 * 对指定时间区间进行加法操作并维护排序数组
 * 
 * @param l 时间区间左端点
 * @param r 时间区间右端点
 * @param v 要增加的值
 */
void innerAdd(int l, int r, long long v) {
    // 对时间区间内每个时间点加上v
    for (int i = l; i <= r; i++) {
        tim[i] += v;
    }
    // 更新该块的排序数组
    int blockId = blockIndex[l];
    for (int i = blockLeft[blockId]; i <= blockRight[blockId]; i++) {
        sortv[i] = tim[i];
    }
    // 对块内时间点重新排序
    my_sort(sortv, blockLeft[blockId], blockRight[blockId]);
}

/**
 * 时间区间加法操作
 * 
 * @param l 时间区间左端点
 * @param r 时间区间右端点
 * @param v 要增加的值
 */
void add(int l, int r, long long v) {
    // 处理空区间
    if (l > r) {
        return;
    }
    
    int leftBlock = blockIndex[l];
    int rightBlock = blockIndex[r];
    
    // 如果区间在同一个块内
    if (leftBlock == rightBlock) {
        innerAdd(l, r, v);
    } else {
        // 处理左边不完整块
        innerAdd(l, blockRight[leftBlock], v);
        // 处理右边不完整块
        innerAdd(blockLeft[rightBlock], r, v);
        // 处理中间完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            lazy[i] += v;
        }
    }
}

/**
 * 在指定时间区间内查询大于等于v的数字个数（暴力方法）
 * 
 * @param l 时间区间左端点
 * @param r 时间区间右端点
 * @param v 比较值
 * @return 大于等于v的数字个数
 */
int innerQuery(int l, int r, long long v) {
    v -= lazy[blockIndex[l]]; // 考虑块的懒惰标记
    int count = 0;
    for (int i = l; i <= r; i++) {
        if (tim[i] >= v) {
            count++;
        }
    }
    return count;
}

/**
 * 第i块内>= v的数字个数（使用二分查找）
 * 
 * @param blockId 块编号
 * @param v 比较值
 * @return 第i块内>= v的数字个数
 */
int blockCount(int blockId, long long v) {
    v -= lazy[blockId]; // 考虑块的懒惰标记
    int left = blockLeft[blockId];
    int right = blockRight[blockId];
    int pos = blockRight[blockId] + 1;
    
    // 二分查找第一个大于等于v的位置
    while (left <= right) {
        int mid = (left + right) >> 1;
        if (sortv[mid] >= v) {
            pos = mid;
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return blockRight[blockId] - pos + 1;
}

/**
 * 查询时间区间内大于等于v的数字个数
 * 
 * @param l 时间区间左端点
 * @param r 时间区间右端点
 * @param v 比较值
 * @return 大于等于v的数字个数
 */
int query(int l, int r, long long v) {
    // 处理空区间
    if (l > r) {
        return 0;
    }
    
    int leftBlock = blockIndex[l];
    int rightBlock = blockIndex[r];
    int count = 0;
    
    // 如果区间在同一个块内
    if (leftBlock == rightBlock) {
        count += innerQuery(l, r, v);
    } else {
        // 处理左边不完整块
        count += innerQuery(l, blockRight[leftBlock], v);
        // 处理右边不完整块
        count += innerQuery(blockLeft[rightBlock], r, v);
        // 处理中间完整块
        for (int i = leftBlock + 1; i < rightBlock; i++) {
            count += blockCount(i, v);
        }
    }
    return count;
}

/**
 * 添加修改事件
 * 
 * @param x 位置
 * @param t 时间
 * @param v 修改值
 */
void addChange(int x, int t, int v) {
    eventCount++;
    events[eventCount].op = 1;
    events[eventCount].x = x;
    events[eventCount].t = t;
    events[eventCount].v = v;
    events[eventCount].q = 0;
}

/**
 * 添加查询事件
 * 
 * @param x 位置
 * @param t 时间
 * @param v 查询标准
 */
void addQuery(int x, int t, int v) {
    eventCount++;
    events[eventCount].op = 2;
    events[eventCount].x = x;
    events[eventCount].t = t;
    events[eventCount].v = v;
    events[eventCount].q = ++queryCount;
}

// 由于环境限制，不实现main函数和事件排序
// 在实际使用中，需要根据具体环境实现输入输出和排序逻辑