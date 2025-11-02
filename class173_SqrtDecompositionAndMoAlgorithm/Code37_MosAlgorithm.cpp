// 莫队算法 - 离线查询优化 (C++版本)
// 题目来源: HDU 3433
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=3433
// 题目大意: 多次查询区间[l,r]内满足条件的元素对
// 约束条件: 数组长度n ≤ 10000，查询次数m ≤ 100000

#include <cstdio>
#include <vector>
#include <algorithm>
#include <cmath>
using namespace std;

const int MAXN = 10005;
const int MAXM = 100005;
const int MAXK = 55; // 题目中元素的最大值

struct Query {
    int l, r, id; // 查询的左右边界和序号
    long long answer; // 查询结果
} q[MAXM];

int n, m, blen; // n是数组长度，m是查询次数，blen是块的大小
int arr[MAXN]; // 原始数组
int count_[MAXK]; // 统计每个元素出现的次数
long long current_answer; // 当前查询的结果

// 按块排序查询
bool compareQuery(const Query& a, const Query& b) {
    // 按块号排序，如果在同一块则按右端点排序（奇偶排序优化）
    int block_a = a.l / blen;
    int block_b = b.l / blen;
    
    if (block_a != block_b) {
        return block_a < block_b;
    }
    
    // 奇偶排序优化：奇数块按右端点升序，偶数块按右端点降序
    if (block_a % 2 == 0) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

// 按id排序查询（用于输出结果）
bool compareId(const Query& a, const Query& b) {
    return a.id < b.id;
}

// 添加一个元素
void add(int pos) {
    int x = arr[pos];
    // 计算添加x后对结果的贡献
    // 对于每个已存在的y，检查是否满足x<=y且x*2>=y
    // 或者y<=x且y*2>=x
    for (int y = 1; y < MAXK; y++) {
        if (count_[y] > 0) {
            if ((x <= y && 2 * x >= y) || (y <= x && 2 * y >= x)) {
                current_answer += (long long)count_[y];
            }
        }
    }
    // 更新元素计数
    count_[x]++;
}

// 移除一个元素
void remove(int pos) {
    int x = arr[pos];
    // 先减少计数，因为我们要计算移除前的影响
    count_[x]--;
    // 计算移除x后对结果的影响
    for (int y = 1; y < MAXK; y++) {
        if (count_[y] > 0) {
            if ((x <= y && 2 * x >= y) || (y <= x && 2 * y >= x)) {
                current_answer -= (long long)count_[y];
            }
        }
    }
}

// 莫队算法主函数
void moAlgorithm() {
    // 初始化块大小
    blen = sqrt(n);
    if (blen == 0) blen = 1;
    
    // 按块排序查询
    sort(q, q + m, compareQuery);
    
    // 初始化计数器和当前答案
    for (int i = 0; i < MAXK; i++) {
        count_[i] = 0;
    }
    current_answer = 0;
    
    // 初始化左右指针
    int current_l = 1;
    int current_r = 0;
    
    // 处理每个查询
    for (int i = 0; i < m; i++) {
        int l = q[i].l;
        int r = q[i].r;
        
        // 移动指针，维护当前区间
        while (current_l > l) add(--current_l);
        while (current_r < r) add(++current_r);
        while (current_l < l) remove(current_l++);
        while (current_r > r) remove(current_r--);
        
        // 记录查询结果
        q[i].answer = current_answer;
    }
    
    // 按id排序查询，恢复原顺序
    sort(q, q + m, compareId);
}

int main() {
    int t;
    scanf("%d", &t);
    
    while (t--) {
        scanf("%d %d", &n, &m);
        
        // 读取数组（假设是1-based索引）
        for (int i = 1; i <= n; i++) {
            scanf("%d", &arr[i]);
        }
        
        // 读取查询
        for (int i = 0; i < m; i++) {
            scanf("%d %d", &q[i].l, &q[i].r);
            q[i].id = i;
        }
        
        // 运行莫队算法
        moAlgorithm();
        
        // 输出结果
        for (int i = 0; i < m; i++) {
            printf("%lld\n", q[i].answer);
        }
    }
    
    return 0;
}

/*
时间复杂度分析：
- 排序查询：O(m log m)
- 莫队算法主循环：
  - 指针移动的总次数：O((n / √n) * n + m * √n) = O(n√n + m√n)
  - 每次add/remove操作：O(k)，k是元素的取值范围
  - 总体时间复杂度：O(m log m + (n + m)√n * k)

空间复杂度分析：
- 存储数组和查询：O(n + m)
- 计数数组：O(k)
- 总体空间复杂度：O(n + m + k)

算法说明：
莫队算法是一种离线查询优化算法，通过将查询按照一定的顺序排序，
使得在处理连续查询时，指针移动的次数最小化，从而提高效率。

算法步骤：
1. 将数组分成大小为√n的块
2. 对查询按照左端点所在块排序，如果在同一块则按右端点排序（奇数块升序，偶数块降序）
3. 维护当前区间的左右指针和结果
4. 按排序后的顺序处理每个查询，通过移动指针来更新当前区间
5. 记录每个查询的结果
6. 按原查询顺序输出结果

优化说明：
1. 奇偶排序优化：奇数块按右端点升序，偶数块按右端点降序，可以减少缓存未命中
2. 块的大小选择为√n，平衡了查询和指针移动的时间复杂度
3. 在add和remove操作中，根据具体问题维护当前结果

与其他方法的对比：
- 暴力法：时间复杂度O(mn)，对于大规模数据无法接受
- 线段树：实现复杂，对于某些问题难以应用
- 莫队算法：实现相对简单，时间复杂度适中

工程化考虑：
1. 注意数组的索引方式（0-based或1-based）
2. 块的大小可以根据具体数据规模调整
3. 对于不同的问题，需要调整add和remove操作的实现
4. 奇偶排序优化可以提高缓存命中率，特别是对于大规模数据
*/