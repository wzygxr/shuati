// D-query - 普通莫队算法实现 (C++版本)
// 题目来源: SPOJ DQUERY - D-query
// 题目链接: https://www.spoj.com/problems/DQUERY/
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少种不同的数字
// 解题思路: 使用普通莫队算法，通过维护区间内不同数字的个数来回答查询
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
// 相关题目:
// 1. CF617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
// 2. BZOJ2038 [国家集训队]小Z的袜子 - https://www.lydsy.com/JudgeOnline/problem.php?id=2038
// 3. HDU4638 Group - http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 4. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d
// 5. Luogu P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494

//#include <cstdio>
//#include <algorithm>
//#include <cmath>
//using namespace std;
//
//// D-query - 普通莫队算法实现 (C++版本)
//// 题目来源: SPOJ DQUERY - D-query
//// 题目链接: https://www.spoj.com/problems/DQUERY/
//// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少种不同的数字
//// 时间复杂度: O(n*sqrt(n))
//// 空间复杂度: O(n)
//
//const int MAXN = 30010;
//const int MAXV = 1000010;
//int n, m;
//int arr[MAXN];
//struct Query {
//    int l, r, id;
//} query[MAXN];
//
//int bi[MAXN];
//int cnt[MAXV]; // 记录每种数值的出现次数
//int curAns = 0; // 当前区间的答案（不同数字的个数）
//int ans[MAXN]; // 存储答案
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
//// 添加元素到区间
//void add(int value) {
//    // 如果这是该数值第一次出现，则不同数字的个数增加1
//    if (cnt[value] == 0) {
//        curAns++;
//    }
//    cnt[value]++;
//}
//
//// 从区间中删除元素
//void del(int value) {
//    cnt[value]--;
//    // 如果该数值不再出现，则不同数字的个数减少1
//    if (cnt[value] == 0) {
//        curAns--;
//    }
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
//        ans[id] = curAns;
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
//    scanf("%d", &n);
//    
//    for (int i = 1; i <= n; i++) {
//        scanf("%d", &arr[i]);
//    }
//    
//    scanf("%d", &m);
//    for (int i = 1; i <= m; i++) {
//        scanf("%d%d", &query[i].l, &query[i].r);
//        query[i].id = i;
//    }
//    
//    prepare();
//    compute();
//    
//    for (int i = 1; i <= m; i++) {
//        printf("%d\n", ans[i]);
//    }
//    
//    return 0;
//}