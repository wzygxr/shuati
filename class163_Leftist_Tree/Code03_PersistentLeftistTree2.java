package class155;

/**
 * 可持久化左偏树的实现
 * 
 * 问题描述：
 * 实现可持久化左偏树数据结构，支持以下操作：
 * 1. 在某个版本的堆中插入一个元素，生成新版本
 * 2. 合并两个版本的堆，生成新版本
 * 3. 弹出某个版本堆的堆顶元素，生成新版本
 * 
 * 解题思路：
 * 可持久化数据结构是一种可以保存历史版本的数据结构，对它进行修改时，
 * 不会破坏之前的版本，而是生成一个新的版本。
 * 
 * 核心思想：
 * 1. 使用函数式编程思想，每次修改只创建需要修改的节点，共享未修改的部分
 * 2. 通过clone操作复制节点，保持历史版本不变
 * 3. 使用merge操作合并两个左偏树
 * 4. 使用pop操作删除堆顶元素
 * 
 * 关键技术：
 * 1. 节点复制：只复制需要修改的节点，其他节点共享
 * 2. 版本管理：通过版本号管理不同的历史版本
 * 3. 左偏树合并：高效的堆合并操作
 * 
 * 时间复杂度分析：
 * - 插入操作: O(log n)
 * - 合并操作: O(log n)
 * - 弹出操作: O(log n)
 * 
 * 空间复杂度分析:
 * - 每次操作最多增加O(log n)个新节点
 * 
 * 相关题目：
 * - Java实现：Code03_PersistentLeftistTree1.java
 * - C++实现：Code03_PersistentLeftistTree2.java
 */

// #include <iostream>
// #include <vector>
// #include <queue>
// #include <algorithm>
// #include <cstdlib>
// #include <ctime>
// 
// using namespace std;
// 
// const int MAXN = 10000;   // 最大版本数
// const int MAXV = 100000;  // 最大值范围
// const int MAXT = 2000001; // 最大节点数
// 
// // 可持久化左偏树相关数组
// int rt[MAXN];    // 每个版本的根节点
// int num[MAXT];   // 节点权值
// int ls[MAXT];    // 左子节点
// int rs[MAXT];    // 右子节点
// int dist[MAXT];  // 距离（空路径长度）
// int siz[MAXT];   // 子树大小
// int cnt = 0;     // 节点计数器
// 
// /**
//  * 初始化一个新节点
//  * @param v 节点权值
//  * @return 新节点编号
//  */
// int init(int v) {
//     num[++cnt] = v;
//     ls[cnt] = rs[cnt] = dist[cnt] = 0;
//     return cnt;
// }
// 
// /**
//  * 克隆一个节点（可持久化关键操作）
//  * @param i 要克隆的节点编号
//  * @return 新节点编号
//  */
// int clone(int i) {
//     num[++cnt] = num[i];
//     ls[cnt] = ls[i];
//     rs[cnt] = rs[i];
//     dist[cnt] = dist[i];
//     return cnt;
// }
// 
// /**
//  * 合并两个左偏树
//  * @param i 第一棵左偏树的根节点
//  * @param j 第二棵左偏树的根节点
//  * @return 合并后的左偏树根节点
//  */
// int merge(int i, int j) {
//     if (i == 0 || j == 0) {
//         return i + j;
//     }
//     // 维护小根堆性质
//     if (num[i] > num[j]) {
//         swap(i, j);
//     }
//     // 克隆根节点以保持历史版本不变
//     int h = clone(i);
//     // 递归合并右子树
//     rs[h] = merge(rs[h], j);
//     // 维护左偏性质
//     if (dist[ls[h]] < dist[rs[h]]) {
//         swap(ls[h], rs[h]);
//     }
//     // 更新距离
//     dist[h] = dist[rs[h]] + 1;
//     return h;
// }
// 
// /**
//  * 弹出堆顶元素
//  * @param i 左偏树根节点
//  * @return 弹出堆顶后的新根节点
//  */
// int pop(int i) {
//     // 处理边界情况
//     if (ls[i] == 0 && rs[i] == 0) {
//         return 0;
//     }
//     if (ls[i] == 0 || rs[i] == 0) {
//         // 克隆非空子树
//         return clone(ls[i] + rs[i]);
//     }
//     // 合并非空的左右子树
//     return merge(ls[i], rs[i]);
// }
// 
// /**
//  * 可持久化左偏树，x版本加入数字y，生成最新的i版本
//  * @param x 原版本号
//  * @param y 要插入的数字
//  * @param i 新版本号
//  */
// void treeAdd(int x, int y, int i) {
//     // 合并原版本的堆与新节点
//     rt[i] = merge(rt[x], init(y));
//     // 更新新版本堆的大小
//     siz[rt[i]] = siz[rt[x]] + 1;
// }
// 
// /**
//  * 可持久化左偏树，x版本与y版本合并，生成最新的i版本
//  * @param x 第一个版本号
//  * @param y 第二个版本号
//  * @param i 新版本号
//  */
// void treeMerge(int x, int y, int i) {
//     // 处理边界情况
//     if (rt[x] == 0 && rt[y] == 0) {
//         rt[i] = 0;
//     } else if (rt[x] == 0 || rt[y] == 0) {
//         // 克隆非空堆的根节点
//         rt[i] = clone(rt[x] + rt[y]);
//     } else {
//         // 合并两个堆
//         rt[i] = merge(rt[x], rt[y]);
//     }
//     // 更新新版本堆的大小
//     siz[rt[i]] = siz[rt[x]] + siz[rt[y]];
// }
// 
// /**
//  * 可持久化左偏树，x版本弹出顶部，生成最新的i版本
//  * @param x 原版本号
//  * @param i 新版本号
//  */
// void treePop(int x, int i) {
//     // 处理空堆情况
//     if (siz[rt[x]] == 0) {
//         rt[i] = 0;
//     } else {
//         // 弹出堆顶元素
//         rt[i] = pop(rt[x]);
//         // 更新新版本堆的大小
//         siz[rt[i]] = siz[rt[x]] - 1;
//     }
// }
// 
// // 验证结构
// vector<priority_queue<int, vector<int>, greater<int>>> verify;
// 
// /**
//  * 验证结构，x版本加入数字y，生成最新版本
//  * @param x 原版本号
//  * @param y 要插入的数字
//  */
// void verifyAdd(int x, int y) {
//     priority_queue<int, vector<int>, greater<int>> pre = verify[x];
//     vector<int> tmp;
//     while (!pre.empty()) {
//         tmp.push_back(pre.top());
//         pre.pop();
//     }
//     priority_queue<int, vector<int>, greater<int>> cur;
//     for (int number : tmp) {
//         cur.push(number);
//     }
//     cur.push(y);
//     verify.push_back(cur);
// }
// 
// /**
//  * 验证结构，x版本与y版本合并，生成最新版本
//  * @param x 第一个版本号
//  * @param y 第二个版本号
//  */
// void verifyMerge(int x, int y) {
//     priority_queue<int, vector<int>, greater<int>> h1 = verify[x];
//     priority_queue<int, vector<int>, greater<int>> h2 = verify[y];
//     vector<int> tmp;
//     priority_queue<int, vector<int>, greater<int>> cur;
//     while (!h1.empty()) {
//         int number = h1.top();
//         h1.pop();
//         tmp.push_back(number);
//         cur.push(number);
//     }
//     for (int number : tmp) {
//         h1.push(number);
//     }
//     tmp.clear();
//     while (!h2.empty()) {
//         int number = h2.top();
//         h2.pop();
//         tmp.push_back(number);
//         cur.push(number);
//     }
//     for (int number : tmp) {
//         h2.push(number);
//     }
//     verify.push_back(cur);
// }
// 
// /**
//  * 验证结构，x版本弹出顶部，生成最新版本
//  * @param x 原版本号
//  */
// void verifyPop(int x) {
//     priority_queue<int, vector<int>, greater<int>> pre = verify[x];
//     priority_queue<int, vector<int>, greater<int>> cur;
//     if (pre.empty()) {
//         verify.push_back(cur);
//     } else {
//         int top = pre.top();
//         pre.pop();
//         vector<int> tmp;
//         while (!pre.empty()) {
//             tmp.push_back(pre.top());
//             pre.pop();
//         }
//         for (int number : tmp) {
//             pre.push(number);
//             cur.push(number);
//         }
//         pre.push(top);
//         verify.push_back(cur);
//     }
// }
// 
// /**
//  * 验证可持久化左偏树i版本的堆是否等于验证结构i版本的堆
//  * @param i 版本号
//  * @return 是否相等
//  */
// bool check(int i) {
//     int h1 = rt[i];
//     priority_queue<int, vector<int>, greater<int>> h2 = verify[i];
//     if (siz[h1] != h2.size()) {
//         return false;
//     }
//     bool ans = true;
//     vector<int> tmp;
//     while (!h2.empty()) {
//         int o1 = num[h1];
//         h1 = pop(h1);
//         int o2 = h2.top();
//         h2.pop();
//         tmp.push_back(o2);
//         if (o1 != o2) {
//             ans = false;
//             break;
//         }
//     }
//     for (int v : tmp) {
//         h2.push(v);
//     }
//     return ans;
// }
// 
// /**
//  * 主函数，使用对数器验证可持久化左偏树的正确性
//  * 测试操作：
//  * 1. 在某个版本的堆中插入一个元素，生成新版本
//  * 2. 合并两个版本的堆，生成新版本
//  * 3. 弹出某个版本堆的堆顶元素，生成新版本
//  */
// int main() {
//     cout << "test begin" << endl;
//     dist[0] = -1;
//     rt[0] = siz[0] = 0;
//     verify.emplace_back(priority_queue<int, vector<int>, greater<int>>());
//     srand(time(nullptr));
//     for (int i = 1, op, x, y; i < MAXN; i++) {
//         op = i == 1 ? 1 : (rand() % 3 + 1);
//         x = rand() % i;
//         if (op == 1) {
//             y = rand() % MAXV;
//             treeAdd(x, y, i);
//             verifyAdd(x, y);
//         } else if (op == 2) {
//             do {
//                 y = rand() % i;
//             } while (y == x);
//             treeMerge(x, y, i);
//             verifyMerge(x, y);
//         } else {
//             treePop(x, i);
//             verifyPop(x);
//         }
//         if (!check(i)) {
//             cout << "err!" << endl;
//         }
//     }
//     for (int i = 1; i < MAXN; i++) {
//         if (!check(i)) {
//             cout << "err!" << endl;
//         }
//     }
//     cout << "test finish" << endl;
//     return 0;
// }