package class159;

// 生成能量密度最大的宝石，C++版
// 给定一个长度为n的数组arr，数组中没有重复数字
// 你可以随意选择一个子数组，长度要求大于等于2，因为这样一来，子数组必存在次大值
// 子数组的次大值 ^ 子数组中除了次大值之外随意选一个数字
// 所能得到的最大结果，叫做子数组的能量密度
// 那么必有某个子数组，拥有最大的能量密度，打印这个最大的能量密度
// 2 <= n <= 5 * 10^4
// 0 <= arr[i] <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P4098
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 补充题目1: 子数组次大值异或最大值
// 给定一个数组，选择一个子数组，用子数组的次大值与子数组中其他任意元素异或，求最大值
// 相关题目:
// - https://www.luogu.com.cn/problem/P4098
// - https://codeforces.com/problemset/problem/1715/E
// - https://www.hdu.edu.cn/problem/5325

// 补充题目2: 可持久化Trie树应用
// 利用可持久化Trie树解决区间异或最大值问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P4735
// - https://www.luogu.com.cn/problem/P4592
// - https://codeforces.com/problemset/problem/1175/G

// 补充题目3: 贪心策略优化
// 通过排序和链表优化减少不必要的计算
// 相关题目:
// - https://www.luogu.com.cn/problem/P4098
// - https://codeforces.com/problemset/problem/1354/D
// - https://www.spoj.com/problems/MKTHNUM/

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组长度
//const int MAXN = 50002;
//
//// Trie树最大节点数
//const int MAXT = MAXN * 32;
//
//// 位数，由于数字范围是0 <= arr[i] <= 10^9，所以最多需要30位（2^30 > 10^9）
//const int BIT = 30;
//
//// 数组长度
//int n;
//
//// arr[i].first表示第i个元素的原始索引
//// arr[i].second表示第i个元素的值
//vector<pair<int, int>> arr;
//
//// 可持久化Trie树的根节点数组
//// root[i]表示前i个数构成的可持久化Trie树的根节点编号
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
//int cnt;
//
//// 链表相关数组
//// last[i]表示位置i的前一个位置
//int last[MAXN];
//
//// nxt[i]表示位置i的后一个位置
//int nxt[MAXN];
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
// * 在可持久化Trie树中查询区间[u,v]与num异或的最大值
// * @param num 查询的数字
// * @param u 区间左边界对应版本的根节点编号
// * @param v 区间右边界对应版本的根节点编号
// * @return 最大异或值
// */
//int query(int num, int u, int v) {
//    int ans = 0;
//    // 从高位到低位贪心选择使异或结果最大的路径
//    for (int b = BIT, path, best; b >= 0; b--) {
//        // 提取第b位的值
//        path = (num >> b) & 1;
//        // 贪心策略：尽量选择与当前位相反的路径
//        best = path ^ 1;
//        // 如果在区间[u,v]中存在best路径，则选择该路径
//        if (pass[tree[v][best]] > pass[tree[u][best]]) {
//            // 将第b位置为1
//            ans += 1 << b;
//            // 移动到best子节点
//            u = tree[u][best];
//            v = tree[v][best];
//        } else {
//            // 否则只能选择相同路径
//            u = tree[u][path];
//            v = tree[v][path];
//        }
//    }
//    return ans;
//}
//
///**
// * 预处理函数，构建可持久化Trie树和链表
// */
//void prepare() {
//    // 初始化链表边界
//    last[0] = 0;
//    nxt[0] = 1;
//    last[n + 1] = n;
//    nxt[n + 1] = n + 1;
//    
//    // 构建可持久化Trie树
//    for (int i = 1; i <= n; i++) {
//        root[i] = insert(arr[i].second, root[i - 1]);
//        // 初始化链表
//        last[i] = i - 1;
//        nxt[i] = i + 1;
//    }
//    
//    // 按值排序数组
//    sort(arr.begin() + 1, arr.end(), [](const pair<int, int>& a, const pair<int, int>& b) {
//        return a.second < b.second;
//    });
//}
//
///**
// * 计算最大能量密度
// * @return 最大能量密度
// */
//int compute() {
//    int ans = 0;
//    // 按值从小到大处理每个元素
//    for (int i = 1, index, value, l1, l2, r1, r2; i <= n; i++) {
//        // 获取元素的原始索引和值
//        index = arr[i].first;
//        value = arr[i].second;
//        
//        // 获取链表中的相邻位置
//        l1 = last[index];  // 左边第一个位置
//        l2 = last[l1];     // 左边第二个位置
//        r1 = nxt[index];   // 右边第一个位置
//        r2 = nxt[r1];      // 右边第二个位置
//        
//        // 如果左边有元素，计算以value为次大值的子数组能量密度
//        if (l1 != 0) {
//            // 在区间[l2, r1-1]中查找与value异或的最大值
//            ans = max(ans, query(value, root[l2], root[r1 - 1]));
//        }
//        
//        // 如果右边有元素，计算以value为次大值的子数组能量密度
//        if (r1 != n + 1) {
//            // 在区间[l1, r2-1]中查找与value异或的最大值
//            ans = max(ans, query(value, root[l1], root[r2 - 1]));
//        }
//        
//        // 更新链表，将当前位置从链表中移除
//        nxt[l1] = r1;
//        last[r1] = l1;
//    }
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    arr.resize(n + 1);
//    // 读入数组元素，同时记录原始索引
//    for (int i = 1; i <= n; i++) {
//        arr[i].first = i;  // 记录原始索引
//        cin >> arr[i].second;  // 记录值
//    }
//    // 预处理
//    prepare();
//    // 输出最大能量密度
//    cout << compute() << "\n";
//    return 0;
//}