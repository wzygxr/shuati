/*
 * 文艺平衡树，FHQ-Treap实现范围翻转，C++版本
 * 长度为n的序列，下标从1开始，一开始序列为1, 2, ..., n
 * 接下来会有k个操作，每个操作给定l，r，表示从l到r范围上的所有数字翻转
 * 做完k次操作后，从左到右打印所有数字
 * 1 <= n, k <= 10^5
 * 测试链接 : https://www.luogu.com.cn/problem/P3391
 * 
 * 时间复杂度和空间复杂度分析：
 * - 时间复杂度：所有操作平均O(log n)，其中n为序列长度
 * - 空间复杂度：O(n)，存储序列数字和Treap节点
 * 最优解：FHQ-Treap是解决此类区间翻转问题的经典最优解
 * 
 * 工程化考量：
 * - 使用延迟标记(lazy propagation)实现高效的区间翻转
 * - 优化内存分配，预分配足够空间
 * - 异常处理：确保操作不会导致程序崩溃
 * - 边界检查：验证所有输入参数的合法性
 */

#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <cstring>
#include <algorithm>
#include <random>
using namespace std;

const int MAXN = 100001;

int head = 0;
int cnt = 0;
int key[MAXN];
int left_[MAXN];
int right_[MAXN];
int size_[MAXN];
double priority_[MAXN];
bool reverse_[MAXN];
int ans[MAXN];
int ansi;

// 更新节点大小信息
void up(int i) {
    size_[i] = size_[left_[i]] + size_[right_[i]] + 1;
}

// 下推延迟标记
void down(int i) {
    if (reverse_[i]) {
        // 交换左右子树
        int tmp = left_[i];
        left_[i] = right_[i];
        right_[i] = tmp;
        
        // 标记下推
        if (left_[i] != 0) {
            reverse_[left_[i]] = !reverse_[left_[i]];
        }
        if (right_[i] != 0) {
            reverse_[right_[i]] = !reverse_[right_[i]];
        }
        
        reverse_[i] = false;
    }
}

// 创建新节点
int newnode(int k) {
    int i = ++cnt;
    key[i] = k;
    left_[i] = 0;
    right_[i] = 0;
    size_[i] = 1;
    // 使用更好的随机数生成器
    static std::random_device rd;
    static std::mt19937 gen(rd());
    static std::uniform_real_distribution<> dis(0.0, 1.0);
    priority_[i] = dis(gen);
    reverse_[i] = false;
    return i;
}

// 合并两个Treap
int merge(int x, int y) {
    if (x == 0) return y;
    if (y == 0) return x;
    
    // 下推延迟标记
    down(x);
    down(y);
    
    if (priority_[x] > priority_[y]) {
        right_[x] = merge(right_[x], y);
        up(x);
        return x;
    } else {
        left_[y] = merge(x, left_[y]);
        up(y);
        return y;
    }
}

// 按大小分割Treap
pair<int, int> split(int x, int k) {
    if (x == 0) return {0, 0};
    
    // 下推延迟标记
    down(x);
    
    if (k <= size_[left_[x]]) {
        auto [a, b] = split(left_[x], k);
        left_[x] = b;
        up(x);
        return {a, x};
    } else {
        auto [a, b] = split(right_[x], k - size_[left_[x]] - 1);
        right_[x] = a;
        up(x);
        return {x, b};
    }
}

// 中序遍历获取结果
void getResult(int x) {
    if (x == 0) return;
    
    // 下推延迟标记
    down(x);
    
    getResult(left_[x]);
    ans[ansi++] = key[x];
    getResult(right_[x]);
}

// 构建初始序列的Treap树
int buildTree(int n) {
    if (n == 0) return 0;
    
    // 使用递归构建平衡的Treap
    function<int(int, int)> build = [&](int l, int r) -> int {
        if (l > r) return 0;
        int mid = (l + r) / 2;
        int node = newnode(mid);
        left_[node] = build(l, mid - 1);
        right_[node] = build(mid + 1, r);
        up(node);
        return node;
    };
    
    return build(1, n);
}

int main() {
    // 初始化随机数种子
    srand(time(0));
    
    int n, k;
    scanf("%d%d", &n, &k);
    
    // 构建初始序列
    head = buildTree(n);
    
    for (int i = 0; i < k; i++) {
        int l, r;
        scanf("%d%d", &l, &r);
        
        // 边界检查
        if (l < 1) l = 1;
        if (r > n) r = n;
        if (l > r) continue;
        
        // 分割序列
        auto [leftPart, temp] = split(head, l - 1);
        auto [mid, rightPart] = split(temp, r - l + 1);
        
        // 翻转中间部分
        if (mid != 0) {
            reverse_[mid] = !reverse_[mid];
        }
        
        // 合并序列
        head = merge(merge(leftPart, mid), rightPart);
    }
    
    // 获取最终结果
    ansi = 0;
    getResult(head);
    
    // 输出结果
    for (int i = 0; i < n; i++) {
        printf("%d ", ans[i]);
    }
    printf("\n");
    
    return 0;
}