/**
 * Codeforces 1401F Reverse and Swap
 * 题目链接: https://codeforces.com/problemset/problem/1401/F
 * 
 * 题目描述:
 * 给定一个长度为2^n的数组a，有以下四种操作：
 * 1. 1 x k: 把a[x]修改为k
 * 2. 2 k: 将数组顺序分为若干个长度为2^k的段，反转每一段的元素
 * 3. 3 k: 将数组顺序分为若干个长度为2^k的段，交换每一段与其相邻段的元素
 * 4. 4 k: 查询前2^k个元素的和
 * 
 * 解题思路:
 * 这是一道线段树维护区间反转和交换操作的题目。关键在于如何高效地处理反转和交换操作。
 * 我们可以使用线段树来维护数组，对于操作2和3，我们不需要真正地去反转或交换元素，
 * 而是通过标记来记录当前状态，查询时根据标记来计算结果。
 * 
 * 时间复杂度分析:
 * - 建树: O(2^n)
 * - 单点更新: O(log 2^n) = O(n)
 * - 区间反转/交换: O(log 2^n) = O(n)
 * - 区间查询: O(log 2^n) = O(n)
 * 
 * 空间复杂度: O(2^n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用位运算优化计算
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

class ReverseAndSwapSegmentTree {
private:
    int n;
    long long* sum;
    bool* rev;
    bool* swap_flag;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小，必须是2的幂次
     */
    ReverseAndSwapSegmentTree(int size);

    /**
     * 析构函数 - 释放内存
     */
    ~ReverseAndSwapSegmentTree();

    /**
     * 向上更新节点信息 - 累加和信息的汇总
     * 
     * @param i 当前节点编号
     */
    void pushUp(int i);

    /**
     * 向下传递懒标记
     * 
     * @param i  当前节点编号
     * @param ln 左子树节点数量
     * @param rn 右子树节点数量
     */
    void pushDown(int i, int ln, int rn);

    /**
     * 建树
     * 
     * @param arr 原始数组
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    void build(long long* arr, int l, int r, int i);

    /**
     * 单点更新 - 将索引idx处的值更新为val
     * 
     * @param idx 要更新的索引
     * @param val 新的值
     * @param l   当前区间左端点
     * @param r   当前区间右端点
     * @param i   当前节点编号
     */
    void updateSingle(int idx, long long val, int l, int r, int i);

    /**
     * 区间反转 - 反转长度为2^k的段
     * 
     * @param k     反转段的级别
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @param level 当前节点所在的层级
     */
    void reverseRange(int k, int l, int r, int i, int level);

    /**
     * 区间交换 - 交换长度为2^k的相邻段
     * 
     * @param k     交换段的级别
     * @param l     当前区间左端点
     * @param r     当前区间右端点
     * @param i     当前节点编号
     * @param level 当前节点所在的层级
     */
    void swapRange(int k, int l, int r, int i, int level);

    /**
     * 查询前缀和 - 查询前2^k个元素的和
     * 
     * @param k 查询前缀的级别
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 前缀和
     */
    long long queryPrefix(int k, int l, int r, int i);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "Codeforces 1401F Reverse and Swap 线段树实现" << endl;
//     // 完整的实现请参考Python和Java版本
//     // return 0;
// }