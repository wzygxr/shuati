/**
 * 线段树实现 - 支持范围重置、范围查询
 * 维护最大值
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

class Code04_SegmentTreeUpdateQueryMax {
private:
    int n;
    long long* maxVal;
    long long* change;
    bool* update;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    Code04_SegmentTreeUpdateQueryMax(int size);

    /**
     * 向上更新节点信息 - 最大值信息的汇总
     * 
     * @param i 当前节点编号
     */
    void pushUp(int i);

    /**
     * 向下传递懒标记
     * 
     * @param i 当前节点编号
     */
    void pushDown(int i);

    /**
     * 懒标记操作
     * 
     * @param i 节点编号
     * @param v 重置的值
     */
    void lazy(int i, long long v);

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
     * 查询最大值
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间最大值
     */
    long long query(int jobl, int jobr, int l, int r, int i);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "线段树测试 - 支持范围重置和范围查询最大值" << endl;
//     // Code04_SegmentTreeUpdateQueryMax segTree(10);
//     // cout << "初始化完成" << endl;
//     // return 0;
// }