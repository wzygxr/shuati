package class172;

// 蒲公英，C++版
// 题目来源：洛谷P4168 [Violet]蒲公英
// 题目链接：https://www.luogu.com.cn/problem/P4168
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
// 操作 l r : 打印arr[l..r]范围上的众数，如果有多个众数，打印值最小的
// 1 <= n <= 4 * 10^4
// 1 <= m <= 5 * 10^4
// 1 <= 数组中的值 <= 10^9
// 题目要求强制在线，具体规则可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P4168
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用分块算法解决此问题，采用预处理优化查询
// 1. 将数组分成sqrt(n)大小的块
// 2. 对数组进行离散化处理，将大数值映射到小范围
// 3. 预处理freq[i][j]表示前i块中数字j出现的次数
// 4. 预处理mode[i][j]表示从第i块到第j块的众数（值最小的）
// 5. 对于查询操作：
//    - 如果在同一个块内，直接暴力统计
//    - 如果跨多个块，结合预处理信息和两端不完整块统计

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，构建freq和mode数组
// 2. 查询操作：O(sqrt(n))，处理两端不完整块
// 空间复杂度：O(n*sqrt(n))，存储freq和mode数组

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组大小
//const int MAXN = 40001;
//// 最大块数
//const int MAXB = 201;
//// 数组长度、操作数、离散化后不同数字的个数
//int n, m, s;
//// 原数组
//int arr[MAXN];
//// 数字做离散化
//int sortv[MAXN];
//
//// 块大小和块数量
//int blen, bnum;
//// 每个元素所属的块
//int bi[MAXN];
//// 每个块的左右边界
//int bl[MAXB];
//int br[MAXB];
//
//// freq[i][j]表示前i块中j出现的次数
//int freq[MAXB][MAXN];
//// mode[i][j]表示从i块到j块中的众数(最小)
//int mode[MAXB][MAXB];
//// 数字的词频统计（临时使用）
//int numCnt[MAXN];
//
///**
// * 二分查找离散化后的值
// * 时间复杂度：O(log(n))
// * @param num 原始数值
// * @return 离散化后的值
// */
//int lower(int num) {
//    int l = 1, r = s, m, ans = 0;
//    // 二分查找第一个>=num的位置
//    while (l <= r) {
//        m = (l + r) >> 1;
//        if (sortv[m] >= num) {
//            ans = m;
//            r = m - 1;
//        } else {
//            l = m + 1;
//        }
//    }
//    return ans;
//}
//
///**
// * 获取第l块到第r块中数字v出现的次数
// * 时间复杂度：O(1)
// * @param l 起始块
// * @param r 结束块
// * @param v 数字（离散化后的值）
// * @return 出现次数
// */
//int getCnt(int l, int r, int v) {
//    return freq[r][v] - freq[l - 1][v];
//}
//
///**
// * 预处理函数，构建分块结构和预处理数组
// * 时间复杂度：O(n*sqrt(n))
// */
//void prepare() {
//    // 建块
//    blen = (int)sqrt(n);
//    bnum = (n + blen - 1) / blen;
//    // 计算每个元素属于哪个块
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    // 计算每个块的左右边界
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
//        br[i] = min(i * blen, n);
//    }
//    
//    // 离散化
//    // 复制原数组用于排序
//    for (int i = 1; i <= n; i++) {
//        sortv[i] = arr[i];
//    }
//    // 排序
//    sort(sortv + 1, sortv + n + 1);
//    // 去重，得到不同数字的个数
//    s = 1;
//    for (int i = 2; i <= n; i++) {
//        if (sortv[s] != sortv[i]) {
//            sortv[++s] = sortv[i];
//        }
//    }
//    // 将原数组中的数字映射为离散化后的值
//    for (int i = 1; i <= n; i++) {
//        arr[i] = lower(arr[i]);
//    }
//    
//    // 填好freq数组
//    // 统计每块中各数字出现次数，并计算前缀和
//    for (int i = 1; i <= bnum; i++) {
//        // 统计当前块中各数字出现次数
//        for (int j = bl[i]; j <= br[i]; j++) {
//            freq[i][arr[j]]++;
//        }
//        // 计算前缀和
//        for (int j = 1; j <= s; j++) {
//            freq[i][j] += freq[i - 1][j];
//        }
//    }
//    
//    // 填好mode数组
//    // 预处理从第i块到第j块的众数
//    for (int i = 1; i <= bnum; i++) {
//        for (int j = i; j <= bnum; j++) {
//            // 初始众数为从第i块到第j-1块的众数
//            int most = mode[i][j - 1];
//            int mostCnt = getCnt(i, j, most);
//            // 遍历第j块中的所有元素，更新众数
//            for (int k = bl[j]; k <= br[j]; k++) {
//                int cur = arr[k];
//                int curCnt = getCnt(i, j, cur);
//                // 如果当前数字出现次数更多，或者出现次数相同但值更小，则更新众数
//                if (curCnt > mostCnt || (curCnt == mostCnt && cur < most)) {
//                    most = cur;
//                    mostCnt = curCnt;
//                }
//            }
//            mode[i][j] = most;
//        }
//    }
//}
//
///**
// * 查询区间[l,r]的众数（值最小的）
// * 时间复杂度：O(sqrt(n))
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 众数（原始值）
// */
//int query(int l, int r) {
//    int most = 0;
//    // 如果在同一个块内，直接暴力统计
//    if (bi[l] == bi[r]) {
//        // 统计各数字出现次数
//        for (int i = l; i <= r; i++) {
//            numCnt[arr[i]]++;
//        }
//        // 找出众数
//        for (int i = l; i <= r; i++) {
//            if (numCnt[arr[i]] > numCnt[most] || (numCnt[arr[i]] == numCnt[most] && arr[i] < most)) {
//                most = arr[i];
//            }
//        }
//        // 清空统计数组
//        for (int i = l; i <= r; i++) {
//            numCnt[arr[i]] = 0;
//        }
//    } else {
//        // 处理左端不完整块
//        for (int i = l; i <= br[bi[l]]; i++) {
//            numCnt[arr[i]]++;
//        }
//        // 处理右端不完整块
//        for (int i = bl[bi[r]]; i <= r; i++) {
//            numCnt[arr[i]]++;
//        }
//        
//        // 获取中间完整块的众数
//        most = mode[bi[l] + 1][bi[r] - 1];
//        // 计算该众数在完整块和不完整块中的总出现次数
//        int mostCnt = getCnt(bi[l] + 1, bi[r] - 1, most) + numCnt[most];
//        
//        // 检查左端不完整块中的数字是否能成为新的众数
//        for (int i = l; i <= br[bi[l]]; i++) {
//            int cur = arr[i];
//            int curCnt = getCnt(bi[l] + 1, bi[r] - 1, cur) + numCnt[cur];
//            if (curCnt > mostCnt || (curCnt == mostCnt && cur < most)) {
//                most = cur;
//                mostCnt = curCnt;
//            }
//        }
//        
//        // 检查右端不完整块中的数字是否能成为新的众数
//        for (int i = bl[bi[r]]; i <= r; i++) {
//            int cur = arr[i];
//            int curCnt = getCnt(bi[l] + 1, bi[r] - 1, cur) + numCnt[cur];
//            if (curCnt > mostCnt || (curCnt == mostCnt && cur < most)) {
//                most = cur;
//                mostCnt = curCnt;
//            }
//        }
//        
//        // 清空统计数组
//        for (int i = l; i <= br[bi[l]]; i++) {
//            numCnt[arr[i]] = 0;
//        }
//        for (int i = bl[bi[r]]; i <= r; i++) {
//            numCnt[arr[i]] = 0;
//        }
//    }
//    // 返回原始值
//    return sortv[most];
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    prepare();
//    // 强制在线处理
//    int lastAns = 0, a, b, l, r;
//    for (int i = 1; i <= m; i++) {
//        cin >> a >> b;
//        a = (a + lastAns - 1) % n + 1;
//        b = (b + lastAns - 1) % n + 1;
//        l = min(a, b);
//        r = max(a, b);
//        lastAns = query(l, r);
//        cout << lastAns << '\n';
//    }
//    return 0;
//}