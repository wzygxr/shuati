/**
 * Luogu P1198 [JSOI2008] 最大数
 * 题目链接: https://www.luogu.com.cn/problem/P1198
 * 
 * 题目描述:
 * 维护一个数列，支持两种操作：
 * 1. 查询操作 Q L: 查询当前数列中末尾L个数中的最大数
 * 2. 插入操作 A n: 将n加上最近一次查询操作的答案t（初始为0），对D取模后插入数列末尾
 * 
 * 解题思路:
 * 使用线段树来维护数列，支持区间最大值查询和单点更新操作。
 * 由于数列是动态增长的，我们可以预先开一个足够大的线段树数组，
 * 用一个指针记录当前数列的实际长度。
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(4n)
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

const long long INF = -9223372036854775807LL - 1;

// 线段树类，用于维护区间最大值
class SegmentTree {
private:
    int n;           // 数组大小
    long long* max_val; // 线段树数组，存储区间最大值
    long long* arr;     // 原始数组
    int size;        // 当前数列的实际长度

public:
    /**
     * 构造函数
     * @param size 线段树大小
     */
    SegmentTree(int size);
    
    /**
     * 析构函数
     */
    ~SegmentTree();
    
    /**
     * 向上更新节点信息 - 最大值信息的汇总
     * @param i 当前节点编号
     */
    void pushUp(int i);
    
    /**
     * 单点更新 - 在位置idx处插入值val
     * @param idx 要更新的位置
     * @param val 新的值
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     */
    void update(int idx, long long val, int l, int r, int i);
    
    /**
     * 区间最大值查询
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 区间最大值
     */
    long long queryMax(int jobl, int jobr, int l, int r, int i);
    
    /**
     * 在数列末尾添加一个数
     * @param val 要添加的值
     */
    void add(long long val);
    
    /**
     * 查询末尾L个数中的最大值
     * @param L 查询的个数
     * @return 最大值
     */
    long long queryLastL(int L);
    
    /**
     * 获取当前数列长度
     * @return 数列长度
     */
    int getSize();
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "线段树测试 - Luogu P1198 最大数" << endl;
//     // SegmentTree segTree(10);
//     // cout << "初始化完成" << endl;
//     // return 0;
// }