package class159;

// 异或运算，C++版
// 给定一个长度n的数组x，还有一个长度为m的数组y
// 想象一个二维矩阵mat，数组x作为行，数组y作为列，mat[i][j] = x[i] ^ y[j]
// 一共有p条查询，每条查询格式如下
// xl xr yl yr k : 划定mat的范围是，行从xl~xr，列从yl~yr，打印其中第k大的值
// 1 <= n <= 1000
// 1 <= m <= 3 * 10^5
// 1 <= p <= 500
// 0 <= x[i]、y[i] < 2^31
// 测试链接 : https://www.luogu.com.cn/problem/P5795
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 补充题目1: 二维矩阵异或第k大值
// 给定两个数组x和y，构建二维矩阵mat[i][j] = x[i] ^ y[j]，查询子矩阵中第k大的值
// 相关题目:
// - https://www.luogu.com.cn/problem/P5795
// - https://codeforces.com/problemset/problem/1715/E
// - https://www.hdu.edu.cn/problem/5325

// 补充题目2: 可持久化Trie树应用
// 利用可持久化Trie树解决区间异或第k大值问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P5795
// - https://www.luogu.com.cn/problem/P4735
// - https://codeforces.com/problemset/problem/1175/G

// 补充题目3: 位运算优化
// 利用位运算和贪心策略优化第k大值查询
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// - https://www.luogu.com.cn/problem/P4551
// - https://codeforces.com/problemset/problem/282/E

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组长度
//const int MAXN = 300001;
//
//// Trie树最大节点数
//const int MAXT = MAXN * 32;
//
//// 位数，由于数字范围是0 <= x[i]、y[i] < 2^31，所以最多需要31位
//const int BIT = 30;
//
//// 数组长度和查询数
//int n, m, p;
//
//// 数组x
//int x[MAXN];
//
//// 可持久化Trie树的根节点数组
//// root[i]表示前i个y数组元素构成的可持久化Trie树的根节点编号
//int root[MAXN];
//
//// Trie树节点的子节点数组
//// tree[i][0/1]表示Trie树节点i的左右子节点编号
//int tree[MAXT][2];
//
//// 经过Trie树节点的数字个数
//// pass[i]表示经过Trie树节点i的数字个数
//int pass[MAXT];
//
//// Trie树节点计数器
//int cnt = 0;
//
//// xroad[i][0]和xroad[i][1]表示处理第i个x元素时在Trie树中的左右边界节点
//int xroad[MAXN][2];
//
///**
// * 在可持久化Trie树中插入一个数字
// * @param num 要插入的数字
// * @param i 前一个版本的根节点编号
// * @return 新版本的根节点编号
// */
//int insert(int num, int i) {
//    // 创建新根节点
//    int rt = ++cnt;
//    // 复用前一个版本的左右子树
//    tree[rt][0] = tree[i][0];
//    tree[rt][1] = tree[i][1];
//    // 经过该节点的数字个数加1
//    pass[rt] = pass[i] + 1;
//    
//    // 从高位到低位处理数字的每一位
//    for (int b = BIT, path, pre = rt, cur; b >= 0; b--, pre = cur) {
//        // 提取第b位的值（0或1）
//        path = (num >> b) & 1;
//        // 获取前一个版本中对应子节点
//        i = tree[i][path];
//        // 创建新节点
//        cur = ++cnt;
//        // 复用前一个版本的子节点信息
//        tree[cur][0] = tree[i][0];
//        tree[cur][1] = tree[i][1];
//        // 更新经过该节点的数字个数
//        pass[cur] = pass[i] + 1;
//        // 连接父子节点
//        tree[pre][path] = cur;
//    }
//    return rt;
//}
//
///**
// * 查询x[xl..xr]和y[yl..yr]构成的二维矩阵中第k大的异或值
// * @param xl x数组查询范围左端点
// * @param xr x数组查询范围右端点
// * @param yl y数组查询范围左端点
// * @param yr y数组查询范围右端点
// * @param k 第k大
// * @return 第k大的异或值
// */
//int maxKth(int xl, int xr, int yl, int yr, int k) {
//    // 基于哪两个节点的pass值查询，一开始x[xl...xr]每个数字，都是一样的
//    for (int i = xl; i <= xr; i++) {
//        xroad[i][0] = root[yl - 1];  // 左边界
//        xroad[i][1] = root[yr];      // 右边界
//    }
//    
//    int ans = 0;
//    // 从高位到低位贪心选择使第k大结果的每一位
//    for (int b = BIT, path, best, sum; b >= 0; b--) {
//        sum = 0;
//        // 统计x[xl...xr]范围上
//        // 每个数字 ^ y[yl...yr]任意一个数字，在第b位上能取得1的结果，有多少个
//        // 结果数量累加起来
//        for (int i = xl; i <= xr; i++) {
//            // 提取x[i]的第b位
//            path = (x[i] >> b) & 1;
//            // 贪心策略：尽量选择与当前位相反的路径
//            best = path ^ 1;
//            // 计算在第b位上能取得1的结果数量
//            sum += pass[tree[xroad[i][1]][best]] - pass[tree[xroad[i][0]][best]];
//        }
//        
//        // 如果sum >= k
//        // 说明x[xl...xr]对应y[yl...yr]，第k大的异或结果，在第b位上能是1
//        // 如果sum < k
//        // 说明x[xl...xr]对应y[yl...yr]，第k大的异或结果，在第b位上只能是0
//        // x[xl...xr]每个数字，都有自己专属的跳转，要记录好！
//        for (int i = xl; i <= xr; i++) {
//            // 提取x[i]的第b位
//            path = (x[i] >> b) & 1;
//            // 贪心策略：尽量选择与当前位相反的路径
//            best = path ^ 1;
//            if (sum >= k) {
//                // 第k大的结果在第b位上能是1，选择best路径
//                xroad[i][0] = tree[xroad[i][0]][best];
//                xroad[i][1] = tree[xroad[i][1]][best];
//            } else {
//                // 第k大的结果在第b位上只能是0，选择path路径
//                xroad[i][0] = tree[xroad[i][0]][path];
//                xroad[i][1] = tree[xroad[i][1]][path];
//            }
//        }
//        
//        if (sum >= k) {
//            // 第k大的结果在第b位上能是1，将第b位置为1
//            ans += 1 << b;
//        } else {
//            // 第k大的结果在第b位上只能是0，调整k值
//            k -= sum;
//        }
//    }
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    // 读入数组x
//    for (int i = 1; i <= n; i++) {
//        cin >> x[i];
//    }
//    // 构建y数组的可持久化Trie树
//    for (int i = 1, yi; i <= m; i++) {
//        cin >> yi;
//        root[i] = insert(yi, root[i - 1]);
//    }
//    cin >> p;
//    // 处理查询
//    for (int i = 1, xl, xr, yl, yr, k; i <= p; i++) {
//        cin >> xl >> xr >> yl >> yr >> k;
//        // 输出查询结果
//        cout << maxKth(xl, xr, yl, yr, k) << "\n";
//    }
//    return 0;
//}