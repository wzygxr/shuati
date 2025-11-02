package class172;

// 空间少求众数的次数，C++版
// 题目来源：洛谷P5048 [Ynoi2019 模拟赛] Yuno loves sqrt technology III
// 题目链接：https://www.luogu.com.cn/problem/P5048
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
// 操作 l r : 打印arr[l..r]范围上，众数到底出现了几次
// 1 <= 所有数值 <= 5 * 10^5
// 内存空间只有64MB，题目要求强制在线，具体规则可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P5048
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用分块算法解决此问题，采用预处理优化查询
// 1. 将数组分成sqrt(n)大小的块
// 2. 对数组元素按值和下标进行排序，构建sortList数组
// 3. 预处理modeCnt[i][j]表示从第i块到第j块中众数的出现次数
// 4. 对于查询操作：
//    - 如果在同一个块内，直接暴力统计
//    - 如果跨多个块，结合预处理信息和两端不完整块统计

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，构建modeCnt数组
// 2. 查询操作：O(sqrt(n))，处理两端不完整块
// 空间复杂度：O(n*sqrt(n))，存储sortList、listIdx和modeCnt数组

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 定义节点结构体，存储值和下标
//struct Node {
//    int v, i;
//};
//
//// 节点比较函数
//bool NodeCmp(Node a, Node b) {
//    if (a.v != b.v) {
//        return a.v < b.v;
//    }
//    return a.i < b.i;
//}
//
//// 最大数组大小
//const int MAXN = 500001;
//// 最大块数
//const int MAXB = 801;
//// 数组长度和操作数
//int n, m;
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
//// sortList数组，存储(值, 下标)对
//Node sortList[MAXN];
//// listIdx[i] = j，表示arr[i]这个元素在sortList里的j位置
//int listIdx[MAXN];
//
//// modeCnt[i][j]表示从i块到j块中众数的出现次数
//int modeCnt[MAXB][MAXB];
//// 数字词频统计
//int numCnt[MAXN];
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
//    // 构建sortList数组，存储(值, 下标)对
//    for (int i = 1; i <= n; i++) {
//        sortList[i].v = arr[i];  // 值
//        sortList[i].i = i;       // 下标
//    }
//    
//    // 按值和下标排序
//    sort(sortList + 1, sortList + n + 1, NodeCmp);
//    
//    // 构建listIdx数组，记录每个元素在sortList中的位置
//    for (int i = 1; i <= n; i++) {
//        listIdx[sortList[i].i] = i;
//    }
//    
//    // 填好modeCnt数组
//    // 预处理从第i块到第j块中众数的出现次数
//    for (int i = 1; i <= bnum; i++) {
//        for (int j = i; j <= bnum; j++) {
//            // 初始众数出现次数为从第i块到第j-1块的众数出现次数
//            int cnt = modeCnt[i][j - 1];
//            // 遍历第j块中的所有元素，更新众数出现次数
//            for (int k = bl[j]; k <= br[j]; k++) {
//                cnt = max(cnt, ++numCnt[arr[k]]);
//            }
//            modeCnt[i][j] = cnt;
//        }
//        // 清空统计数组
//        for (int j = 1; j <= n; j++) {
//            numCnt[j] = 0;
//        }
//    }
//}
//
///**
// * 查询区间[l,r]中众数的出现次数
// * 时间复杂度：O(sqrt(n))
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 众数的出现次数
// */
//int query(int l, int r) {
//    int ans = 0;
//    // 如果在同一个块内，直接暴力统计
//    if (bi[l] == bi[r]) {
//        // 统计各数字出现次数，同时更新最大出现次数
//        for (int i = l; i <= r; i++) {
//            ans = max(ans, ++numCnt[arr[i]]);
//        }
//        // 清空统计数组
//        for (int i = l; i <= r; i++) {
//            numCnt[arr[i]] = 0;
//        }
//    } else {
//        // 获取中间完整块的众数出现次数
//        ans = modeCnt[bi[l] + 1][bi[r] - 1];
//        
//        // 处理左端不完整块
//        // 通过listIdx找到该元素在sortList中的位置，然后向后查找连续相同值的元素
//        for (int i = l, idx; i <= br[bi[l]]; i++) {
//            idx = listIdx[i];
//            // 向后查找连续相同值的元素，直到超出范围或下标大于r
//            while (idx + ans <= n && sortList[idx + ans].v == arr[i] && sortList[idx + ans].i <= r) {
//                ans++;
//            }
//        }
//        
//        // 处理右端不完整块
//        // 通过listIdx找到该元素在sortList中的位置，然后向前查找连续相同值的元素
//        for (int i = bl[bi[r]], idx; i <= r; i++) {
//            idx = listIdx[i];
//            // 向前查找连续相同值的元素，直到超出范围或下标小于l
//            while (idx - ans >= 1 && sortList[idx - ans].v == arr[i] && sortList[idx - ans].i >= l) {
//                ans++;
//            }
//        }
//    }
//    return ans;
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
//    for (int i = 1, l, r, lastAns = 0; i <= m; i++) {
//        cin >> l >> r;
//        l ^= lastAns;
//        r ^= lastAns;
//        lastAns = query(l, r);
//        cout << lastAns << '\n';
//    }
//    return 0;
//}