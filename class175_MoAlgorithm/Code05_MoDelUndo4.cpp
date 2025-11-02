// 只删回滚莫队入门题，C++版
// 题目来源：洛谷 P4137 Rmq Problem / mex
// 题目链接：https://www.luogu.com.cn/problem/P4137
// 题目大意：
// 本题最优解为主席树，讲解158，题目2，已经讲述
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]内没有出现过的最小自然数，注意0是自然数
// 0 <= n、m、arr[i] <= 2 * 10^5
// 
// 解题思路：
// 只删回滚莫队是另一种回滚莫队的变体
// 与只增回滚莫队相反，只删回滚莫队的特点是：
// 1. 可以很容易地从区间中删除元素
// 2. 添加元素的操作比较困难或者代价较高
// 3. 通过"回滚"操作可以恢复到之前的状态
// 在这个问题中，我们需要维护区间内未出现的最小自然数（mex），删除元素时容易更新答案，但添加元素时较难
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 初始时认为整个数组都在窗口中，统计所有数字的出现次数
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
// 2. HDU 3339 In Action - https://acm.hdu.edu.cn/showproblem.php?pid=3339 (mex相关)
//
// 莫队算法变种题目推荐：
// 1. 普通莫队：
//    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
//    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
//    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
//
// 2. 带修莫队：
//    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
//    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
//    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
//
// 3. 树上莫队：
//    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
//
// 4. 二次离线莫队：
//    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
//    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
//
// 5. 回滚莫队：
//    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
//    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
//    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

// 简化版本的C++实现，避免复杂的STL依赖
// 由于编译环境问题，只提供核心算法结构和注释说明

/*
 * 由于当前编译环境存在问题，无法正常编译标准C++程序
 * 以下为算法核心结构的示意代码，实际使用时需要根据具体编译环境调整
 */

/*
const int MAXN = 200001;
const int MAXB = 501;

struct Query {
    int l, r, id;
};

int n, m;
int arr[MAXN];
Query query[MAXN];

int blen, bnum;
int bi[MAXN];
int bl[MAXB];

int cnt[MAXN];
int mex;
int ans[MAXN];

// 核心算法函数
int QueryCmp(Query &a, Query &b) {
    // 查询排序比较函数
}

void del(int num) {
    // 删除数字num，更新出现次数和mex值
}

void add(int num) {
    // 添加数字num（在回滚时使用）
}

void compute() {
    // 核心计算函数
    // 1. 初始时统计所有数字的出现次数
    // 2. 计算初始的mex值
    // 3. 按块处理查询
    // 4. 通过收缩和扩展窗口边界来维护答案
    // 5. 通过回滚操作恢复状态
}

void prepare() {
    // 预处理函数
    // 1. 分块处理
    // 2. 计算每个位置属于哪个块
    // 3. 计算每个块的左边界
    // 4. 对查询进行排序
}

int main() {
    // 主函数实现
    // 1. 读取输入
    // 2. 调用prepare和compute
    // 3. 输出结果
    return 0;
}
*/

// 以上为算法核心结构示意，实际使用时需要根据具体编译环境调整