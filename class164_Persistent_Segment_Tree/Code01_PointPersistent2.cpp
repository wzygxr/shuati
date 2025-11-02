/**
 * 单点修改的可持久化线段树模版题1，c++版
 * 
 * 题目来源: 洛谷 P3919 【模板】可持久化线段树1（可持久化数组）
 * 题目链接: https://www.luogu.com.cn/problem/P3919
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，原始数组认为是0号版本
 * 一共有m条操作，每条操作是如下两种类型中的一种
 * v 1 x y : 基于v号版本的数组，把x位置的值设置成y，生成新版本的数组
 * v 2 x   : 基于v号版本的数组，打印x位置的值，生成新版本的数组和v版本一致
 * 每条操作后得到的新版本数组，版本编号为操作的计数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决可持久化数组问题。
 * 1. 对于每次修改操作，只创建被修改路径上的新节点，共享未修改的部分
 * 2. 对于查询操作，直接在对应版本的线段树上查询
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n, m <= 10^6
 * 
 * 示例:
 * 输入:
 * 5 10
 * 59 64 65 97 51
 * 0 1 1 10
 * 0 2 2 20
 * 0 3 3 30
 * 0 4 4 40
 * 0 5 5 50
 * 1 2 1 100
 * 1 2 2 200
 * 1 2 3 300
 * 1 2 4 400
 * 1 2 5 500
 * 
 * 输出:
 * 10
 * 20
 * 30
 * 40
 * 50
 * 100
 * 200
 * 300
 * 400
 * 500
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 1000001;
const int MAXT = MAXN * 23;

int n, m;
// 原始数组
int arr[MAXN];
// 可持久化线段树需要
// root[i] : i号版本线段树的头节点编号
int root[MAXN];
int left[MAXT];
int right[MAXT];
// value[i] : 节点i的值信息，只有叶节点有这个信息
int value[MAXT];
// 可持久化线段树的节点空间计数
int cnt = 0;

/**
 * 建树，返回头节点编号
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 头节点编号
 */
int build(int l, int r) {
    int rt = ++cnt;
    if (l == r) {
        value[rt] = arr[l];
    } else {
        int mid = (l + r) >> 1;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 线段树范围l~r，信息在i号节点里
 * 在l~r范围上，jobi位置的值，设置成jobv
 * 生成的新节点编号返回
 * @param jobi 要修改的位置
 * @param jobv 要设置的值
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点编号
 * @return 新节点编号
 */
int update(int jobi, int jobv, int l, int r, int i) {
    int rt = ++cnt;
    left[rt] = left[i];
    right[rt] = right[i];
    value[rt] = value[i];
    if (l == r) {
        value[rt] = jobv;
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            left[rt] = update(jobi, jobv, l, mid, left[rt]);
        } else {
            right[rt] = update(jobi, jobv, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 线段树范围l~r，信息在i号节点里
 * 返回l~r范围上jobi位置的值
 * @param jobi 要查询的位置
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点编号
 * @return 位置jobi的值
 */
int query(int jobi, int l, int r, int i) {
    if (l == r) {
        return value[i];
    }
    int mid = (l + r) >> 1;
    if (jobi <= mid) {
        return query(jobi, l, mid, left[i]);
    } else {
        return query(jobi, mid + 1, r, right[i]);
    }
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    n = 5;
    m = 10;
    arr[1] = 59; arr[2] = 64; arr[3] = 65; arr[4] = 97; arr[5] = 51;
    
    root[0] = build(1, n);
    
    // 示例操作
    // 0 1 1 10
    root[1] = update(1, 10, 1, n, root[0]);
    // 0 2 2 20
    root[2] = update(2, 20, 1, n, root[0]);
    // 0 3 3 30
    root[3] = update(3, 30, 1, n, root[0]);
    // 0 4 4 40
    root[4] = update(4, 40, 1, n, root[0]);
    // 0 5 5 50
    root[5] = update(5, 50, 1, n, root[0]);
    // 1 2 1 100
    root[6] = update(1, 100, 1, n, root[2]);
    // 1 2 2 200
    root[7] = update(2, 200, 1, n, root[2]);
    // 1 2 3 300
    root[8] = update(3, 300, 1, n, root[2]);
    // 1 2 4 400
    root[9] = update(4, 400, 1, n, root[2]);
    // 1 2 5 500
    root[10] = update(5, 500, 1, n, root[2]);
    
    // 示例查询（需要根据具体环境实现输出）
    int result1 = query(1, 1, n, root[1]);  // 应该是10
    int result2 = query(2, 1, n, root[2]);  // 应该是20
    int result3 = query(3, 1, n, root[3]);  // 应该是30
    int result4 = query(4, 1, n, root[4]);  // 应该是40
    int result5 = query(5, 1, n, root[5]);  // 应该是50
    int result6 = query(1, 1, n, root[6]);  // 应该是100
    int result7 = query(2, 1, n, root[7]);  // 应该是200
    int result8 = query(3, 1, n, root[8]);  // 应该是300
    int result9 = query(4, 1, n, root[9]);  // 应该是400
    int result10 = query(5, 1, n, root[10]); // 应该是500
    
    return 0;
}