// 小Z的袜子 - 普通莫队算法实现 (C++版本)
// 题目来源: 洛谷 P1494 [国家集训队]小Z的袜子
// 题目链接: https://www.luogu.com.cn/problem/P1494
// 题目大意: 给定一个长度为n的序列，每个元素代表袜子的颜色
// 有m次询问，每次询问区间[l,r]中随机抽取两只袜子颜色相同的概率
// 输出要求: 每个询问输出一个分数A/B表示概率，要求为最简分数
// 解题思路: 使用普通莫队算法，通过维护区间内每种颜色的出现次数来计算概率
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
// 相关题目:
// 1. CF617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
// 3. BZOJ2038 [国家集训队]小Z的袜子 - https://www.lydsy.com/JudgeOnline/problem.php?id=2038
// 4. HDU4638 Group - http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 5. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d

//#include <iostream>
//#include <algorithm>
//#include <cmath>
//using namespace std;
//
//// 小Z的袜子 - 普通莫队算法实现 (C++版本)
//// 题目来源: BZOJ 2038 [2009国家集训队]小Z的袜子(hose)
//// 题目链接: https://www.luogu.com.cn/problem/P1494
//// 题目大意: 给定一个长度为n的序列，每个元素代表袜子的颜色
//// 有m次询问，每次询问区间[l,r]中随机抽取两只袜子颜色相同的概率
//// 输出要求: 每个询问输出一个分数A/B表示概率，要求为最简分数
//// 时间复杂度: O(n*sqrt(n))
//// 空间复杂度: O(n)
//
//const int MAXN = 50010;
//int n, m;
//int arr[MAXN];
//struct Query {
//    int l, r, id;
//} query[MAXN];
//
//int bi[MAXN];
//long long cnt[MAXN]; // 记录每种颜色的出现次数
//long long curAns = 0; // 当前区间的答案（分子）
//long long ansA[MAXN]; // 答案分子
//long long ansB[MAXN]; // 答案分母
//
//// 分块大小
//int block_size;
//
//// 查询排序比较函数
//bool cmp(const Query& a, const Query& b) {
//    if (bi[a.l] != bi[b.l]) {
//        return bi[a.l] < bi[b.l];
//    }
//    if (bi[a.l] & 1) {
//        return a.r < b.r;
//    } else {
//        return a.r > b.r;
//    }
//}
//
//// 求最大公约数
//long long gcd(long long a, long long b) {
//    return b == 0 ? a : gcd(b, a % b);
//}
//
//// 添加元素到区间
//void add(int color) {
//    // 当添加一个颜色时，该颜色对答案的贡献增加2*cnt[color]
//    // 因为对于每一对已存在的该颜色袜子，新加入的袜子都能和它们组成一对
//    curAns += cnt[color] * 2;
//    cnt[color]++;
//}
//
//// 从区间中删除元素
//void del(int color) {
//    cnt[color]--;
//    // 当删除一个颜色时，该颜色对答案的贡献减少2*cnt[color]
//    // 因为对于每一对剩余的该颜色袜子，被删除的袜子不能再和它们组成一对
//    curAns -= cnt[color] * 2;
//}
//
//// 计算查询结果
//void compute() {
//    int winl = 1, winr = 0; // 当前维护的区间 [winl, winr]
//    for (int i = 1; i <= m; i++) {
//        int jobl = query[i].l; // 目标区间左端点
//        int jobr = query[i].r; // 目标区间右端点
//        int id = query[i].id;  // 查询编号
//        
//        // 扩展左边界
//        while (winl > jobl) {
//            add(arr[--winl]);
//        }
//        
//        // 扩展右边界
//        while (winr < jobr) {
//            add(arr[++winr]);
//        }
//        
//        // 收缩左边界
//        while (winl < jobl) {
//            del(arr[winl++]);
//        }
//        
//        // 收缩右边界
//        while (winr > jobr) {
//            del(arr[winr--]);
//        }
//        
//        // 特殊情况：区间长度为1时概率为0
//        if (jobl == jobr) {
//            ansA[id] = 0;
//            ansB[id] = 1;
//            continue;
//        }
//        
//        // 计算答案
//        ansA[id] = curAns;
//        long long len = jobr - jobl + 1;
//        ansB[id] = len * (len - 1);
//        
//        // 化简分数
//        long long g = gcd(ansA[id], ansB[id]);
//        ansA[id] /= g;
//        ansB[id] /= g;
//    }
//}
//
//// 预处理
//void prepare() {
//    block_size = (int)sqrt((double)n);
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / block_size + 1;
//    }
//    sort(query + 1, query + m + 1, cmp);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    
//    for (int i = 1; i <= m; i++) {
//        cin >> query[i].l >> query[i].r;
//        query[i].id = i;
//    }
//    
//    prepare();
//    compute();
//    
//    for (int i = 1; i <= m; i++) {
//        cout << ansA[i] << "/" << ansB[i] << '\n';
//    }
//    
//    return 0;
//}