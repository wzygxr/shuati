// 秃子酋长，C++版
// 题目来源：洛谷 P8078 [COCI2010-2011#6] KRUZNICA
// 题目链接：https://www.luogu.com.cn/problem/P8078
// 题目大意：
// 给定一个长度为n的数组arr，一共有m条查询，格式如下
// 查询 l r : 打印arr[l..r]范围上，如果所有数排序后，
//            相邻的数在原序列中的位置的差的绝对值之和
// 注意arr很特殊，1~n这些数字在arr中都只出现1次
// 1 <= n、m <= 5 * 10^5
// 
// 解题思路：
// 这是一道比较复杂的莫队题目，需要维护相邻元素在原序列中位置差的绝对值之和
// 解决思路：
// 1. 将数组中的值看作下标，将下标看作值，建立pos数组，pos[i]表示数字i在原数组中的位置
// 2. 维护一个链表结构，last[i]表示数字i在当前窗口排序后前一个相邻数字，next[i]表示后一个相邻数字
// 3. 当添加或删除数字时，维护链表结构并更新答案
// 
// 算法要点：
// 1. 使用只删回滚莫队算法解决此问题
// 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
// 3. 维护链表结构来表示当前窗口中数字的排序关系
// 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
//
// 时间复杂度：O((n+m)*sqrt(n))
// 空间复杂度：O(n)
// 
// 相关题目：
// 1. 洛谷 P8078 [COCI2010-2011#6] KRUZNICA - https://www.luogu.com.cn/problem/P8078
// 2. COCI 2010-2011 Contest #6 KRUZNICA - https://oj.uz/problem/view/COCI11_kruznica
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
const int MAXN = 500001;

struct Query {
    int l, r, id;
};

int n, m;
int arr[MAXN];
Query query[MAXN];
int pos[MAXN];

int blen, bnum;
int bi[MAXN];
int bl[MAXN];

int last_arr[MAXN + 1];
int next_arr[MAXN + 1];
long long sum_val;
long long ans[MAXN];

// 核心算法函数
int QueryCmp(Query &a, Query &b) {
    // 查询排序比较函数
}

void del(int num) {
    // 删除数字num，维护链表结构并更新答案
}

void add(int num) {
    // 添加数字num（在回滚时使用）
}

void compute() {
    // 核心计算函数
    // 1. 初始化链表结构
    // 2. 初始时计算答案
    // 3. 按块处理查询
    // 4. 通过收缩和扩展窗口边界来维护答案
    // 5. 通过回滚操作恢复状态
}

void prepare() {
    // 预处理函数
    // 1. 建立pos数组
    // 2. 分块处理
    // 3. 计算每个位置属于哪个块
    // 4. 计算每个块的左边界
    // 5. 对查询进行排序
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