package class159;

// 最大异或和，C++版
// 非负序列arr的初始长度为n，一共有m条操作，每条操作是如下两种类型中的一种
// A x     : arr的末尾增加数字x，arr的长度n也增加1
// Q l r x : l~r这些位置中，选一个位置p，现在希望
//           arr[p] ^ arr[p+1] ^ .. ^ arr[n] ^ x 这个值最大
//           打印这个最大值
// 1 <= n、m <= 3 * 10^5
// 0 <= arr[i]、x <= 10^7
// 因为练的就是可持久化前缀树，所以就用在线算法，不要使用离线算法
// 测试链接 : https://www.luogu.com.cn/problem/P4735
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 补充题目1: 最大异或对
// 给定一个非负整数数组 nums，返回 nums[i] XOR nums[j] 的最大结果，其中 0 <= i <= j < n
// 测试链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// 测试链接: https://www.luogu.com.cn/problem/P4551
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// - https://www.luogu.com.cn/problem/P4551
// - https://www.hdu.edu.cn/problem/4825
// - https://codeforces.com/problemset/problem/282/E
// - https://atcoder.jp/contests/abc161/tasks/abc161_f

// 补充题目2: 树上异或路径最大值
// 给定一棵n个点的带权树，结点下标从1开始到n。求树中所有异或路径的最大值
// 测试链接: https://www.luogu.com.cn/problem/P4551
// 相关题目:
// - https://www.luogu.com.cn/problem/P4551
// - https://www.hdu.edu.cn/problem/4757
// - https://codeforces.com/problemset/problem/1175/G
// - https://www.spoj.com/problems/TTM/

// 补充题目3: 与数组中元素的最大异或值
// 给你一个由非负整数组成的数组 nums 。另有一个查询数组 queries ，其中 queries[i] = [xi, mi] 。
// 第 i 个查询的答案是 xi 和任何 nums 数组中不超过 mi 的元素按位异或（XOR）得到的最大值
// 测试链接: https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
// - https://www.codechef.com/problems/XRQRS
// - https://www.spoj.com/problems/ADACOINS/

// 补充题目4: 线性基模板题 - 子集异或和最大值
// 给定n个整数（数字可能重复），求在这些数中选取任意个，使得他们的异或和最大
// 测试链接: https://www.luogu.com.cn/problem/P3812
// 相关题目:
// - https://www.luogu.com.cn/problem/P3812
// - https://www.hdu.edu.cn/problem/3949
// - https://codeforces.com/problemset/problem/959/F
// - https://atcoder.jp/contests/abc141/tasks/abc141_f

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大节点数量，根据题目数据范围设置
//const int MAXN = 600001;
//
//// Trie树最大节点数，每个数字最多需要26位（BIT+1）
//const int MAXT = MAXN * 22;
//
//// 位数，由于数字范围是0 <= arr[i], x <= 10^7，所以最多需要24位（2^24 > 10^7）
//const int BIT = 25;
//
//// 当前数组长度和操作数
//int n, m, eor;
//
//// root[i]表示前i个数构成的可持久化Trie树的根节点编号
//int root[MAXN];
//
//// tree[i][0/1]表示节点i的左右子节点编号
//int tree[MAXT][2];
//
//// pass[i]表示经过节点i的数字个数
//int pass[MAXT];
//
//// 当前使用的节点编号
//int cnt = 0;
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
// * 在可持久化Trie树中查询区间[l,r]与num异或的最大值
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
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(0);
//    cin >> n >> m;
//    eor = 0;
//    // 插入前缀异或和0，表示空数组的情况
//    root[0] = insert(eor, 0);
//    // 读入初始数组并构建可持久化Trie树
//    for (int i = 1, num; i <= n; i++) {
//        cin >> num;
//        // 计算前缀异或和
//        eor ^= num;
//        // 插入前缀异或和并更新根节点
//        root[i] = insert(eor, root[i - 1]);
//    }
//    string op;
//    int x, y, z;
//    // 处理m条操作
//    for (int i = 1; i <= m; i++) {
//        cin >> op;
//        // 添加操作
//        if (op == "A") {
//            cin >> x;
//            // 更新前缀异或和
//            eor ^= x;
//            n++;
//            // 插入新的前缀异或和并更新根节点
//            root[n] = insert(eor, root[n - 1]);
//        } else {
//            // 查询操作
//            cin >> x >> y >> z;
//            // 根据查询区间的不同情况调用查询函数
//            if (x == 1) {
//                // 查询整个区间[1,r]
//                cout << query(eor ^ z, 0, root[y - 1]) << "\n";
//            } else {
//                // 查询区间[l,r]
//                cout << query(eor ^ z, root[x - 2], root[y - 1]) << "\n";
//            }
//        }
//    }
//    return 0;
//}