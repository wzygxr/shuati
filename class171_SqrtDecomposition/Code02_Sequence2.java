package class173;

/**
 * 序列，C++版
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
 * 说明：如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 测试链接：https://www.luogu.com.cn/problem/P3863
 * 提交如下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
///**
// * 事件结构体
// */
//struct Event {
//    int op, x, t, v, q;
//};
//
///**
// * 事件比较函数
// * 按位置排序，位置相同的按时间排序
// */
//bool EventCmp(Event &a, Event &b) {
//    return a.x != b.x ? a.x < b.x : a.t < b.t;
//}
//
//const int MAXN = 100001;
//const int MAXB = 501;
//int n, m;
//int arr[MAXN];
//
//Event event[MAXN << 2];
//int cnte, cntq;
//
//long long tim[MAXN];
//long long sortv[MAXN];
//
//int blen, bnum;
//int bi[MAXN];
//int bl[MAXB];
//int br[MAXB];
//long long lazy[MAXB];
//
//int ans[MAXN];
//
///** 
// * 对指定时间区间进行加法操作并维护排序数组
// * 
// * @param l 时间区间左端点
// * @param r 时间区间右端点
// * @param v 要增加的值
// * 
// * 时间复杂度：O(√m + √m*log(√m)) = O(√m*log(√m))
// * 空间复杂度：O(1)
// */
//void innerAdd(int l, int r, long long v) {
//    // 对时间区间内每个时间点加上v
//    for (int i = l; i <= r; i++) {
//        tim[i] += v;
//    }
//    // 更新该块的排序数组
//    for (int i = bl[bi[l]]; i <= br[bi[l]]; i++) {
//        sortv[i] = tim[i];
//    }
//    // 对块内时间点重新排序
//    sort(sortv + bl[bi[l]], sortv + br[bi[l]] + 1);
//}
//
///** 
// * 时间区间加法操作
// * 
// * @param l 时间区间左端点
// * @param r 时间区间右端点
// * @param v 要增加的值
// * 
// * 时间复杂度：O(√m)
// * 空间复杂度：O(1)
// */
//void add(int l, int r, long long v) {
//    // 处理空区间
//    if (l > r) {
//        return;
//    }
//    // 如果区间在同一个块内
//    if (bi[l] == bi[r]) {
//        innerAdd(l, r, v);
//    } else {
//        // 处理左边不完整块
//        innerAdd(l, br[bi[l]], v);
//        // 处理右边不完整块
//        innerAdd(bl[bi[r]], r, v);
//        // 处理中间完整块
//        for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
//            lazy[i] += v;
//        }
//    }
//}
//
///** 
// * 在指定时间区间内查询大于等于v的数字个数（暴力方法）
// * 
// * @param l 时间区间左端点
// * @param r 时间区间右端点
// * @param v 比较值
// * @return 大于等于v的数字个数
// * 
// * 时间复杂度：O(√m)
// * 空间复杂度：O(1)
// */
//int innerQuery(int l, int r, long long v) {
//    v -= lazy[bi[l]]; // 考虑块的懒惰标记
//    int ans = 0;
//    for (int i = l; i <= r; i++) {
//        if (tim[i] >= v) {
//            ans++;
//        }
//    }
//    return ans;
//}
//
///** 
// * 第i块内>= v的数字个数（使用二分查找）
// * 
// * @param i 块编号
// * @param v 比较值
// * @return 第i块内>= v的数字个数
// * 
// * 时间复杂度：O(log(√m)) = O(log m)
// * 空间复杂度：O(1)
// */
//int getCnt(int i, long long v) {
//    v -= lazy[i]; // 考虑块的懒惰标记
//    int l = bl[i], r = br[i], m, pos = br[i] + 1;
//    // 二分查找第一个大于等于v的位置
//    while (l <= r) {
//        m = (l + r) >> 1;
//        if (sortv[m] >= v) {
//            pos = m;
//            r = m - 1;
//        } else {
//            l = m + 1;
//        }
//    }
//    return br[i] - pos + 1;
//}
//
///** 
// * 查询时间区间内大于等于v的数字个数
// * 
// * @param l 时间区间左端点
// * @param r 时间区间右端点
// * @param v 比较值
// * @return 大于等于v的数字个数
// * 
// * 时间复杂度：O(√m)
// * 空间复杂度：O(1)
// */
//int query(int l, int r, long long v) {
//    // 处理空区间
//    if (l > r) {
//        return 0;
//    }
//    int ans = 0;
//    // 如果区间在同一个块内
//    if (bi[l] == bi[r]) {
//    	ans = innerQuery(l, r, v);
//    } else {
//    	ans += innerQuery(l, br[bi[l]], v);
//    	ans += innerQuery(bl[bi[r]], r, v);
//        // 处理中间完整块
//        for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
//            ans += getCnt(i, v);
//        }
//    }
//    return ans;
//}
//
///** 
// * 添加修改事件
// * 
// * @param x 位置
// * @param t 时间
// * @param v 修改值
// * 
// * 时间复杂度：O(1)
// * 空间复杂度：O(1)
// */
//void addChange(int x, int t, int v) {
//    event[++cnte].op = 1; // 操作类型：修改
//    event[cnte].x = x;    // 位置
//    event[cnte].t = t;    // 时间
//    event[cnte].v = v;    // 修改值
//}
//
///** 
// * 添加查询事件
// * 
// * @param x 位置
// * @param t 时间
// * @param v 查询标准
// * 
// * 时间复杂度：O(1)
// * 空间复杂度：O(1)
// */
//void addQuery(int x, int t, int v) {
//    event[++cnte].op = 2;      // 操作类型：查询
//    event[cnte].x = x;         // 位置
//    event[cnte].t = t;         // 时间
//    event[cnte].v = v;         // 查询标准
//    event[cnte].q = ++cntq;    // 查询编号
//}
//
///** 
// * 初始化分块结构和事件排序
// * 
// * 时间复杂度：O((m+n) * log(m+n))
// * 空间复杂度：O(m+n)
// */
//void prepare() {
//    // 设置块大小为sqrt(m)
//    blen = (int)sqrt(m);
//    // 计算块数量
//    bnum = (m + blen - 1) / blen;
//    // 初始化每个时间点所属的块
//    for (int i = 1; i <= m; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    // 初始化每个块的边界
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
//        br[i] = min(i * blen, m);
//    }
//    // 按位置和时间排序所有事件
//    sort(event + 1, event + cnte + 1, EventCmp);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    m++;
//    // 读取所有操作
//    for (int t = 2, op, l, r, v, x; t <= m; t++) {
//        cin >> op;
//        if (op == 1) {
//            cin >> l >> r >> v;
//            // 使用差分数组技巧处理区间加法
//            addChange(l, t, v);
//            addChange(r + 1, t, -v);
//        } else {
//            cin >> x >> v;
//            addQuery(x, t, v);
//        }
//    }
//    prepare();
//    // 处理所有事件
//    for (int i = 1; i <= cnte; i++) {
//        if (event[i].op == 1) {
//            // 处理修改事件
//            add(event[i].t, m, event[i].v);
//        } else {
//            // 处理查询事件
//            ans[event[i].q] = query(1, event[i].t - 1, 1LL * event[i].v - arr[event[i].x]);
//        }
//    }
//    // 输出所有查询结果
//    for (int i = 1; i <= cntq; i++) {
//        cout << ans[i] << '\n';
//    }
//    return 0;
//}