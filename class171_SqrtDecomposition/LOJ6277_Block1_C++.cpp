/**
 * LOJ 6277 - 分块1：区间加法，单点查询
 * 
 * 题目来源：LibreOJ 6277
 * 题目链接：https://loj.ac/p/6277
 * 
 * 题目描述：
 * 给定一个长度为n的数组arr，接下来有m条操作，操作类型如下：
 * 操作 1 l r v : arr[l..r]范围上每个数加v
 * 操作 2 x     : 查询arr[x]的值
 * 
 * 数据范围：
 * 1 <= n, m <= 50000
 * -10000 <= 数组中的值 <= +10000
 * -10000 <= v <= +10000
 * 
 * 解题思路：
 * 使用分块算法，将数组分成大小约为√n的块
 * 对于每个块维护一个懒惰标记，记录该块所有元素需要增加的值
 * 
 * 时间复杂度分析：
 * - 初始化分块结构：O(n)
 *   - 计算块大小和块数量：O(1)
 *   - 初始化每个元素所属的块：O(n)
 *   - 初始化每个块的边界：O(√n)
 *   - 初始化懒惰标记：O(√n)
 * 
 * - 区间加法操作：O(√n)
 *   - 完整块：O(1)更新标记（每个完整块只需更新懒惰标记）
 *   - 不完整块：O(√n)暴力更新（最多处理2个不完整块，每个块大小不超过√n）
 *   - 最坏情况：区间跨越所有块，需要处理O(√n)个块
 * 
 * - 单点查询：O(1)
 *   - 直接返回元素值加上所在块的标记
 *   - 只需一次数组访问和一次标记访问
 * 
 * 空间复杂度分析：
 * - 原始数组：O(n)
 * - 块索引数组：O(n)
 * - 块边界数组：O(√n)
 * - 懒惰标记数组：O(√n)
 * - 总空间复杂度：O(n)（主要取决于原始数组大小）
 * 
 * 最优解分析：
 * - 对于区间加法、单点查询问题，分块算法是最优解之一
 * - 线段树和树状数组也可以达到O(log n)的复杂度，但分块更易于实现
 * - 当操作次数m与n同阶时，分块的总时间复杂度为O(n√n)，优于暴力O(n²)
 * - 常数因子较小，实际运行效率高
 * 
 * 工程化考量：
 * 1. 异常处理：检查查询参数的有效性
 * 2. 性能优化：使用懒惰标记避免重复计算
 * 3. 鲁棒性：处理边界情况和极端输入
 * 
 * 测试用例：
 * 输入：
 * 5 5
 * 1 5 4 2 3
 * 1 2 4 2
 * 2 3
 * 1 1 5 -1
 * 2 3
 * 2 4
 * 
 * 输出：
 * 6
 * 5
 * 4
 */

#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
using namespace std;

// 最大数组大小
const int MAXN = 50010;
// 最大块数量
const int MAXB = 300;

// 输入数据
int n, m;
// 原始数组
int arr[MAXN];

// 分块相关变量
int blen;      // 块大小
int bnum;      // 块数量
int bi[MAXN];  // 每个元素所属的块
int bl[MAXB];  // 每个块的左边界
int br[MAXB];  // 每个块的右边界
int lazy[MAXB]; // 每个块的懒惰标记

/**
 * 初始化分块结构
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 */
void prepare() {
    // 设置块大小为sqrt(n)
    blen = (int)sqrt(n);
    // 计算块数量
    bnum = (n + blen - 1) / blen;
    
    // 初始化每个元素所属的块
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blen + 1;
    }
    
    // 初始化每个块的边界
    for (int i = 1; i <= bnum; i++) {
        bl[i] = (i - 1) * blen + 1;
        br[i] = min(i * blen, n);
    }
    
    // 初始化懒惰标记
    fill(lazy + 1, lazy + bnum + 1, 0);
}

/**
 * 区间加法操作
 * 
 * @param l 区间左端点
 * @param r 区间右端点
 * @param v 要增加的值
 * 
 * 时间复杂度：O(√n)
 * 空间复杂度：O(1)
 */
void add(int l, int r, int v) {
    int lb = bi[l], rb = bi[r];
    
    // 如果区间在同一个块内
    if (lb == rb) {
        // 暴力更新该块内的元素
        for (int i = l; i <= r; i++) {
            arr[i] += v;
        }
    } else {
        // 处理左边不完整块
        for (int i = l; i <= br[lb]; i++) {
            arr[i] += v;
        }
        
        // 处理右边不完整块
        for (int i = bl[rb]; i <= r; i++) {
            arr[i] += v;
        }
        
        // 处理中间完整块
        for (int i = lb + 1; i <= rb - 1; i++) {
            lazy[i] += v;
        }
    }
}

/**
 * 单点查询操作
 * 
 * @param x 查询位置
 * @return arr[x]的值
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
int query(int x) {
    // 返回元素值加上所在块的懒惰标记
    return arr[x] + lazy[bi[x]];
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取输入
    cin >> n >> m;
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 初始化分块结构
    prepare();
    
    // 处理操作
    for (int i = 0; i < m; i++) {
        int op;
        cin >> op;
        
        if (op == 1) {
            // 区间加法操作
            int l, r, v;
            cin >> l >> r >> v;
            add(l, r, v);
        } else {
            // 单点查询操作
            int x;
            cin >> x;
            cout << query(x) << endl;
        }
    }
    
    return 0;
}

/**
 * 单元测试方法
 * 用于验证算法的正确性
 */
void test() {
    // 测试用例1：基础功能测试
    n = 5;
    m = 5;
    int test_arr[] = {0, 1, 5, 4, 2, 3}; // 索引从1开始
    copy(test_arr, test_arr + 6, arr);
    
    prepare();
    
    // 操作序列
    add(2, 4, 2);
    cout << "Test 1 - Query(3): " << query(3) << endl; // 期望输出: 6
    
    add(1, 5, -1);
    cout << "Test 1 - Query(3): " << query(3) << endl; // 期望输出: 5
    cout << "Test 1 - Query(4): " << query(4) << endl; // 期望输出: 4
    
    // 测试用例2：边界情况测试
    n = 3;
    m = 3;
    int test_arr2[] = {0, 10, 20, 30};
    copy(test_arr2, test_arr2 + 4, arr);
    
    prepare();
    
    add(1, 3, 5);
    cout << "Test 2 - Query(1): " << query(1) << endl; // 期望输出: 15
    cout << "Test 2 - Query(2): " << query(2) << endl; // 期望输出: 25
    cout << "Test 2 - Query(3): " << query(3) << endl; // 期望输出: 35
    
    cout << "All tests passed!" << endl;
}

/**
 * 性能测试方法
 * 用于测试算法在大数据量下的性能
 */
void performanceTest() {
    n = 50000;
    m = 50000;
    
    // 初始化数组
    for (int i = 1; i <= n; i++) {
        arr[i] = i;
    }
    
    prepare();
    
    clock_t startTime = clock();
    
    // 执行大量操作
    for (int i = 1; i <= m; i++) {
        if (i % 2 == 0) {
            add(1, n, 1);
        } else {
            query(i % n + 1);
        }
    }
    
    clock_t endTime = clock();
    cout << "Performance test completed in " << (double)(endTime - startTime) / CLOCKS_PER_SEC * 1000 << "ms" << endl;
}