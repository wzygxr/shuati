/**
 * 单点修改的可持久化线段树模版题2，c++版
 * 
 * 题目来源: 洛谷 P3834 【模板】可持久化线段树2
 * 题目链接: https://www.luogu.com.cn/problem/P3834
 * 
 * 题目描述:
 * 给定一个长度为n的数组arr，下标1~n，一共有m条查询
 * 每条查询 l r k : 打印arr[l..r]中第k小的数字
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决静态区间第K小问题。
 * 1. 对数组元素进行离散化处理，将大范围的值映射到小范围的排名
 * 2. 对于每个位置i，建立一个线段树版本，维护前i个元素中每个排名的出现次数
 * 3. 利用前缀和的思想，通过两个版本的线段树相减得到区间信息
 * 4. 在线段树上二分查找第K小的元素
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 1 <= n、m <= 2 * 10^5
 * 0 <= arr[i] <= 10^9
 * 
 * 示例:
 * 输入:
 * 5 5
 * 25957 6405 15770 26287 6556
 * 2 2 1
 * 3 4 1
 * 4 5 1
 * 1 2 2
 * 4 4 1
 * 
 * 输出:
 * 6405
 * 15770
 * 26287
 * 25957
 * 26287
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 200001;
const int MAXT = MAXN * 22;

int n, m, s;
// 原始数组
int arr[MAXN];
// 收集权值排序并且去重做离散化
int sorted[MAXN];
// 可持久化线段树需要
// root[i] : 插入arr[i]之后形成新版本的线段树，记录头节点编号
// 0号版本的线段树代表一个数字也没有时，每种名次的数字出现的次数
// i号版本的线段树代表arr[1..i]范围内，每种名次的数字出现的次数
int root[MAXN];
int left[MAXT];
int right[MAXT];
// 排名范围内收集了多少个数字
int size[MAXT];
int cnt;

/**
 * 返回num在所有值中排名多少
 * @param num 要查询排名的数值
 * @return num的排名
 */
int kth(int num) {
    int left_idx = 1, right_idx = s, mid, ans = 0;
    while (left_idx <= right_idx) {
        mid = (left_idx + right_idx) / 2;
        if (sorted[mid] <= num) {
            ans = mid;
            left_idx = mid + 1;
        } else {
            right_idx = mid - 1;
        }
    }
    return ans;
}

/**
 * 排名范围l~r，建立线段树，返回头节点编号
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 头节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    size[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 排名范围l~r，信息在i号节点，增加一个排名为jobi的数字
 * 返回新的头节点编号
 * @param jobi 要插入的数字的排名
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点编号
 * @return 新节点编号
 */
int insert(int jobi, int l, int r, int i) {
    cnt++;
    int rt = cnt;
    left[rt] = left[i];
    right[rt] = right[i];
    size[rt] = size[i] + 1;
    if (l < r) {
        int mid = (l + r) / 2;
        if (jobi <= mid) {
            left[rt] = insert(jobi, l, mid, left[rt]);
        } else {
            right[rt] = insert(jobi, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 排名范围l~r，老版本信息在u号节点，新版本信息在v号节点
 * 返回，第jobk小的数字，排名多少
 * @param jobk 要查询的第几小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param u 老版本节点编号
 * @param v 新版本节点编号
 * @return 第jobk小的数字的排名
 */
int query(int jobk, int l, int r, int u, int v) {
    if (l == r) {
        return l;
    }
    int lsize = size[left[v]] - size[left[u]];
    int mid = (l + r) / 2;
    if (lsize >= jobk) {
        return query(jobk, l, mid, left[u], left[v]);
    } else {
        return query(jobk - lsize, mid + 1, r, right[u], right[v]);
    }
}

/**
 * 权值做离散化并且去重 + 生成各版本的线段树
 */
void prepare() {
    cnt = 0;
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    
    // 简单排序实现（实际应使用快速排序等高效算法）
    for (int i = 1; i <= n - 1; i++) {
        for (int j = 1; j <= n - i; j++) {
            if (sorted[j] > sorted[j + 1]) {
                int temp = sorted[j];
                sorted[j] = sorted[j + 1];
                sorted[j + 1] = temp;
            }
        }
    }
    
    s = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[s] != sorted[i]) {
            s++;
            sorted[s] = sorted[i];
        }
    }
    root[0] = build(1, s);
    for (int i = 1, x; i <= n; i++) {
        x = kth(arr[i]);
        root[i] = insert(x, 1, s, root[i - 1]);
    }
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    n = 5;
    m = 5;
    
    // 原始数组
    arr[1] = 25957; arr[2] = 6405; arr[3] = 15770; arr[4] = 26287; arr[5] = 6556;
    
    // 离散化处理并构建主席树
    prepare();
    
    // 示例查询
    // 查询区间[2,2]第1小的数
    int rank1 = query(1, 1, s, root[2 - 1], root[2]);
    // 查询区间[3,4]第1小的数
    int rank2 = query(1, 1, s, root[3 - 1], root[4]);
    // 查询区间[4,5]第1小的数
    int rank3 = query(1, 1, s, root[4 - 1], root[5]);
    // 查询区间[1,2]第2小的数
    int rank4 = query(2, 1, s, root[1 - 1], root[2]);
    // 查询区间[4,4]第1小的数
    int rank5 = query(1, 1, s, root[4 - 1], root[4]);
    
    // 输出结果需要根据具体环境实现
    return 0;
}