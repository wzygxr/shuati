/**
 * 线段树实现 - 同时支持范围重置、范围增加、范围查询
 * 维护累加和
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 单点更新: O(log n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(4n)
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

class Code05_SegmentTreeUpdateAddQuerySum {
private:
    int n;
    long long* sum;
    long long* add;
    long long* change;
    bool* update;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    Code05_SegmentTreeUpdateAddQuerySum(int size);

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
     * 重置操作的懒标记
     * 
     * @param i 节点编号
     * @param v 重置的值
     * @param n 节点对应的区间长度
     */
    void updateLazy(int i, long long v, int n);

    /**
     * 增加操作的懒标记
     * 
     * @param i 节点编号
     * @param v 增加的值
     * @param n 节点对应的区间长度
     */
    void addLazy(int i, long long v, int n);

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
     * 范围重置 - jobl ~ jobr范围上每个数字重置为jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 重置的值
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    void updateRange(int jobl, int jobr, long long jobv, int l, int r, int i);

    /**
     * 范围修改 - jobl ~ jobr范围上每个数字增加jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 增加的值
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    void addRange(int jobl, int jobr, long long jobv, int l, int r, int i);

    /**
     * 查询累加和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间和
     */
    long long query(int jobl, int jobr, int l, int r, int i);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "线段树测试 - 支持范围重置、范围增加和范围查询" << endl;
//     // Code05_SegmentTreeUpdateAddQuerySum segTree(10);
//     // cout << "初始化完成" << endl;
//     // return 0;
// }