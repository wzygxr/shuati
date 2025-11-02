// Powerful Array - 普通莫队算法实现 (C++版本)
// 题目来源: Codeforces 86D - Powerful array
// 题目链接: https://codeforces.com/problemset/problem/86/D
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内所有数字的贡献和
// 每个数字x的贡献为 (出现次数)^2 * x
// 解题思路: 使用普通莫队算法，通过维护区间内每个数字的出现次数来计算贡献和
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
// 相关题目:
// 1. CF617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
// 3. BZOJ2038 [国家集训队]小Z的袜子 - https://www.lydsy.com/JudgeOnline/problem.php?id=2038
// 4. HDU4638 Group - http://acm.hdu.edu.cn/showproblem.php?pid=4638
// 5. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d

//#include <cstdio>
//#include <algorithm>
//#include <cmath>
//using namespace std;
//
//// Powerful Array - 普通莫队算法实现 (C++版本)
//// 题目来源: Codeforces 86D - Powerful array
//// 题目链接: https://codeforces.com/problemset/problem/86/D
//// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内所有数字的贡献和
//// 每个数字x的贡献为 (出现次数)^2 * x
//// 时间复杂度: O(n*sqrt(n))
//// 空间复杂度: O(n)
//
//const int MAXN = 200010;
//const int MAXV = 1000010;
//int n, m;
//int arr[MAXN];
//struct Query {
//    int l, r, id;
//} query[MAXN];
//
//int bi[MAXN;
//long long cnt[MAXV; // 记录每种数值的出现次数
//long long curAns = 0; // 当前区间的答案
//long long ans[MAXN; // 存储答案
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
//    // 当添加一个数值时，先从当前答案中减去旧的贡献
//    curAns -= 1LL * cnt[value] * cnt[value] * value;
//    // 增加该数值的计数
//    cnt[value]++;
//    // 再加上新的贡献
//    curAns += 1LL * cnt[value] * cnt[value] * value;
//}
//
//// 从区间中删除元素
//void del(int value) {
//    // 当删除一个数值时，先从当前答案中减去旧的贡献
//    curAns -= 1LL * cnt[value] * cnt[value] * value;
//    // 减少该数值的计数
//    cnt[value]--;
//    // 再加上新的贡献
//    curAns += 1LL * cnt[value] * cnt[value] * value;
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
//    scanf("%d%d", &n, &m);
//    
//    for (int i = 1; i <= n; i++) {
//        scanf("%d", &arr[i]);
//    }
//    
//    for (int i = 1; i <= m; i++) {
//        scanf("%d%d", &query[i].l, &query[i].r);
//        query[i].id = i;
//    }
//    
//    prepare();
//    compute();
//    
//    for (int i = 1; i <= m; i++) {
//        printf("%I64d\n", ans[i]);
//    }
//    
//    return 0;
//}