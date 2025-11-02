/**
 * 标记永久化，范围增加 + 查询累加和，c++版
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，一共有m条操作，操作类型如下
 * 1 x y k : 将区间[x, y]每个数加上k
 * 2 x y   : 打印区间[x, y]的累加和
 * 这就是普通线段树，请用标记永久化的方式实现
 * 
 * 解题思路:
 * 使用标记永久化的线段树解决区间修改和区间查询问题。
 * 1. 使用标记永久化技术减少空间占用
 * 2. 对于区间修改操作，在路径上的每个节点都记录标记信息
 * 3. 对于区间查询操作，累积路径上的标记信息
 * 
 * 时间复杂度: O(m log n)
 * 空间复杂度: O(n)
 * 
 * 测试链接: https://www.luogu.com.cn/problem/P3372
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 100001;
long long arr[MAXN];
long long sum[MAXN << 2];
long long addTag[MAXN << 2];

/**
 * 构建线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点编号
 */
void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = arr[l];
    } else {
        int mid = (l + r) / 2;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }
    addTag[i] = 0;
}

/**
 * 区间增加操作
 * @param jobl 操作区间左端点
 * @param jobr 操作区间右端点
 * @param jobv 增加的值
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 */
void add(int jobl, int jobr, long long jobv, int l, int r, int i) {
    int a = (jobl > l) ? jobl : l;
    int b = (jobr < r) ? jobr : r;
    sum[i] += jobv * (b - a + 1);
    if (jobl <= l && r <= jobr) {
        addTag[i] += jobv;
    } else {
        int mid = (l + r) / 2;
        if (jobl <= mid) {
            add(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
    }
}

/**
 * 区间查询操作
 * @param jobl 查询区间左端点
 * @param jobr 查询区间右端点
 * @param addHistory 累积的标记信息
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param i 当前节点编号
 * @return 区间和
 */
long long query(int jobl, int jobr, long long addHistory, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i] + addHistory * (r - l + 1);
    }
    int mid = (l + r) >> 1;
    long long ans = 0;
    if (jobl <= mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], l, mid, i << 1);
    }
    if (jobr > mid) {
        ans += query(jobl, jobr, addHistory + addTag[i], mid + 1, r, i << 1 | 1);
    }
    return ans;
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 5;
    int m = 5;
    
    // 原始数组
    arr[1] = 1; arr[2] = 2; arr[3] = 3; arr[4] = 4; arr[5] = 5;
    
    // 构建线段树
    build(1, n, 1);
    
    // 示例操作
    // 1 2 4 2 : 将区间[2, 4]每个数加上2
    add(2, 4, 2, 1, n, 1);
    // 2 1 5 : 查询区间[1, 5]的累加和
    long long result1 = query(1, 5, 0, 1, n, 1);
    // 1 1 3 1 : 将区间[1, 3]每个数加上1
    add(1, 3, 1, 1, n, 1);
    // 2 2 4 : 查询区间[2, 4]的累加和
    long long result2 = query(2, 4, 0, 1, n, 1);
    // 2 1 5 : 查询区间[1, 5]的累加和
    long long result3 = query(1, 5, 0, 1, n, 1);
    
    // 输出结果需要根据具体环境实现
    return 0;
}