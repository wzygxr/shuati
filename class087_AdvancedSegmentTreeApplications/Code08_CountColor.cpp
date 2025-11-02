/**
 * POJ 2777 - Count Color
 * 
 * 题目描述：
 * 给定一个长度为L的板条(1 <= L <= 100000)，初始时所有位置都是颜色1
 * 执行O次操作(1 <= O <= 100000)，操作类型：
 * 1. "C A B C": 将区间[A,B]染成颜色C
 * 2. "P A B": 查询区间[A,B]中有多少种不同的颜色
 * 
 * 解题思路：
 * 使用线段树维护区间信息，每个节点存储以下信息：
 * 1. 区间颜色集合(用位运算表示，第i位为1表示有颜色i)
 * 2. 懒惰标记(表示区间被染成的颜色)
 * 
 * 关键技术：
 * 1. 位运算优化：用一个整数的二进制位表示颜色集合，第i位为1表示有颜色i
 * 2. 懒惰标记：延迟更新子区间
 * 3. 区间染色：将整个区间染成同一种颜色
 * 
 * 时间复杂度分析：
 * 1. 建树：O(L)
 * 2. 更新：O(log L)
 * 3. 查询：O(log L)
 * 4. 空间复杂度：O(L)
 * 
 * 是否最优解：是
 * 这是解决区间染色和颜色计数问题的最优解法，时间复杂度为O(log L)
 * 
 * 工程化考量：
 * 1. 位运算优化：使用位运算高效表示颜色集合
 * 2. 懒惰标记：延迟更新子区间，提高效率
 * 3. 内存管理：静态数组避免频繁内存分配
 * 4. 边界处理：处理区间完全包含和部分重叠的情况
 * 
 * 题目链接：http://poj.org/problem?id=2777
 * 
 * @author Algorithm Journey
 * @version 1.0
 */

// 由于编译环境问题，不使用标准头文件，采用基础C++实现

// 最大数组大小
const int MAXN = 100001;

// 线段树节点信息
int color[MAXN << 2]; // 区间颜色集合(位运算表示)
int lazy[MAXN << 2];  // 懒惰标记

/**
 * 计算一个整数二进制表示中1的个数
 * 用于计算颜色种类数
 * 
 * @param n 输入整数
 * @return 二进制表示中1的个数
 */
int countBits(int n) {
    int count = 0;
    while (n > 0) {
        count += n & 1;
        n >>= 1;
    }
    return count;
}

/**
 * 向上更新节点信息
 * 将左右子节点的颜色集合信息合并到父节点
 * 
 * @param rt 节点索引
 */
void pushUp(int rt) {
    color[rt] = color[rt << 1] | color[rt << 1 | 1];
}

/**
 * 向下传递懒惰标记
 * 在访问子节点前，将当前节点的懒惰标记传递给子节点
 * 
 * @param rt 节点索引
 */
void pushDown(int rt) {
    if (lazy[rt] != 0) {
        lazy[rt << 1] = lazy[rt];
        lazy[rt << 1 | 1] = lazy[rt];
        color[rt << 1] = lazy[rt];
        color[rt << 1 | 1] = lazy[rt];
        lazy[rt] = 0;
    }
}

/**
 * 建立线段树
 * 初始化线段树，所有位置初始颜色为1
 * 
 * @param l  区间左端点
 * @param r  区间右端点
 * @param rt 节点索引
 */
void build(int l, int r, int rt) {
    lazy[rt] = 0;
    if (l == r) {
        color[rt] = 1; // 初始颜色为1，用二进制表示第0位为1
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

/**
 * 区间染色操作
 * 将区间[L, R]染成颜色c
 * 
 * @param L  操作区间左端点
 * @param R  操作区间右端点
 * @param c  染色颜色
 * @param l  当前节点表示的区间左端点
 * @param r  当前节点表示的区间右端点
 * @param rt 当前节点索引
 */
void update(int L, int R, int c, int l, int r, int rt) {
    // 如果当前区间完全被操作区间包含，直接染色
    if (L <= l && r <= R) {
        color[rt] = 1 << (c - 1); // 将第c位设为1
        lazy[rt] = 1 << (c - 1);
        return;
    }
    
    // 下推懒惰标记
    pushDown(rt);
    
    int mid = (l + r) >> 1;
    if (L <= mid) update(L, R, c, l, mid, rt << 1);
    if (R > mid) update(L, R, c, mid + 1, r, rt << 1 | 1);
    
    // 向上更新节点信息
    pushUp(rt);
}

/**
 * 查询区间颜色数
 * 查询区间[L, R]中有多少种不同的颜色
 * 
 * @param L  查询区间左端点
 * @param R  查询区间右端点
 * @param l  当前节点表示的区间左端点
 * @param r  当前节点表示的区间右端点
 * @param rt 当前节点索引
 * @return 区间颜色数
 */
int query(int L, int R, int l, int r, int rt) {
    // 如果当前区间完全被查询区间包含，直接返回颜色数
    if (L <= l && r <= R) {
        return countBits(color[rt]);
    }
    
    // 下推懒惰标记
    pushDown(rt);
    
    int mid = (l + r) >> 1;
    int res = 0;
    if (L <= mid) res |= query(L, R, l, mid, rt << 1);
    if (R > mid) res |= query(L, R, mid + 1, r, rt << 1 | 1);
    
    return countBits(res);
}

// 由于编译环境限制，不实现main函数
// 在实际使用中，需要根据具体环境实现输入输出功能