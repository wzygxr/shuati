package class172;

// 作诗，C++版
// 题目来源：洛谷P4135 作诗
// 题目链接：https://www.luogu.com.cn/problem/P4135
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
// 操作 l r : 打印arr[l..r]范围上，有多少个数出现正偶数次
// 1 <= 所有数值 <= 10^5
// 题目要求强制在线，具体规则可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P4135
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用分块算法解决此问题，采用预处理优化查询
// 1. 将数组分成sqrt(n)大小的块
// 2. 预处理freq[i][j]表示前i块中数字j出现的次数
// 3. 预处理even[i][j]表示从第i块到第j块中出现正偶数次的数字个数
// 4. 对于查询操作：
//    - 如果在同一个块内，直接暴力统计
//    - 如果跨多个块，结合预处理信息和两端不完整块统计

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，构建freq和even数组
// 2. 查询操作：O(sqrt(n))，处理两端不完整块
// 空间复杂度：O(n*sqrt(n))，存储freq和even数组

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组大小
//const int MAXN = 100001;
//// 最大块数
//const int MAXB = 401;
//// 数组长度、数值范围、操作数
//int n, c, m;
//// 原数组
//int arr[MAXN];
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
//// even[i][j]表示从第i块到第j块，有多少个数出现正偶数次
//int even[MAXB][MAXB];
//// 数字词频统计
//int numCnt[MAXN];
//
///**
// * 获取第l块到第r块中数字v出现的次数
// * 时间复杂度：O(1)
// * @param l 起始块
// * @param r 结束块
// * @param v 数字
// * @return 出现次数
// */
//int getCnt(int l, int r, int v) {
//    return freq[r][v] - freq[l - 1][v];
//}
//
///**
// * 计算某个数的词频变化对出现正偶数次的数字个数的影响
// * 当词频从pre变为pre+1时，返回出现正偶数次的数字个数的变化量
// * @param pre 原来的词频
// * @return 变化量
// */
//int delta(int pre) {
//    // 如果原来词频为0，增加到1后不会影响出现正偶数次的数字个数
//    if (pre == 0) {
//        return 0;
//    }
//    // 如果原来词频为正偶数，增加到奇数后会减少1个出现正偶数次的数字
//    if ((pre & 1) == 0) {
//        return -1;
//    }
//    // 如果原来词频为正奇数，增加到偶数后会增加1个出现正偶数次的数字
//    return 1;
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
//    
//    // 计算每个元素属于哪个块
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    
//    // 计算每个块的左右边界
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
//        br[i] = min(i * blen, n);
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
//        for (int j = 1; j <= c; j++) {
//            freq[i][j] += freq[i - 1][j];
//        }
//    }
//    
//    // 填好even数组
//    // 预处理从第i块到第j块中出现正偶数次的数字个数
//    for (int i = 1; i <= bnum; i++) {
//        for (int j = i; j <= bnum; j++) {
//            // 初始值为从第i块到第j-1块中出现正偶数次的数字个数
//            even[i][j] = even[i][j - 1];
//            // 遍历第j块中的所有元素，更新出现正偶数次的数字个数
//            for (int k = bl[j]; k <= br[j]; k++) {
//                even[i][j] += delta(numCnt[arr[k]]);
//                numCnt[arr[k]]++;
//            }
//        }
//        // 清空统计数组
//        for (int j = 1; j <= c; j++) {
//            numCnt[j] = 0;
//        }
//    }
//}
//
///**
// * 查询区间[l,r]中出现正偶数次的数字个数
// * 时间复杂度：O(sqrt(n))
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 出现正偶数次的数字个数
// */
//int query(int l, int r) {
//    int ans = 0;
//    // 如果在同一个块内，直接暴力统计
//    if (bi[l] == bi[r]) {
//        // 统计各数字出现次数，同时更新出现正偶数次的数字个数
//        for (int i = l; i <= r; i++) {
//            ans += delta(numCnt[arr[i]]);
//            numCnt[arr[i]]++;
//        }
//        // 清空统计数组
//        for (int i = l; i <= r; i++) {
//            numCnt[arr[i]] = 0;
//        }
//    } else {
//        // 获取中间完整块中出现正偶数次的数字个数
//        ans = even[bi[l] + 1][bi[r] - 1];
//        
//        // 处理左端不完整块
//        for (int i = l; i <= br[bi[l]]; i++) {
//            // 计算该数字在完整块和不完整块中的总出现次数
//            int totalCount = getCnt(bi[l] + 1, bi[r] - 1, arr[i]) + numCnt[arr[i]];
//            ans += delta(totalCount);
//            numCnt[arr[i]]++;
//        }
//        
//        // 处理右端不完整块
//        for (int i = bl[bi[r]]; i <= r; i++) {
//            // 计算该数字在完整块和不完整块中的总出现次数
//            int totalCount = getCnt(bi[l] + 1, bi[r] - 1, arr[i]) + numCnt[arr[i]];
//            ans += delta(totalCount);
//            numCnt[arr[i]]++;
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
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> c >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    prepare();
//    // 强制在线处理
//    for (int i = 1, a, b, l, r, lastAns = 0; i <= m; i++) {
//        cin >> a >> b;
//        a = (a + lastAns) % n + 1;
//        b = (b + lastAns) % n + 1;
//        l = min(a, b);
//        r = max(a, b);
//        lastAns = query(l, r);
//        cout << lastAns << '\n';
//    }
//    return 0;
//}