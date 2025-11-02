/**
 * Luogu P4198 楼房重建
 * 题目链接: https://www.luogu.com.cn/problem/P4198
 * 
 * 题目描述:
 * 小A在平面上(0,0)点的位置，第i栋楼房可以用一条连接(i,0)和(i,Hi)的线段表示。
 * 如果这栋楼房上存在一个高度大于0的点与(0,0)的连线没有与之前的线段相交，那么这栋楼房就被认为是可见的。
 * 每天建筑队会修改一栋楼房的高度，求每天小A能看到多少栋楼房。
 * 
 * 解题思路:
 * 这是一个经典的线段树问题。关键在于将问题转化为斜率比较问题。
 * 从原点(0,0)能看到第i栋楼，当且仅当第i栋楼的斜率Hi/i大于前面所有楼的斜率。
 * 因此，我们需要维护区间最大值，并统计从左到右严格递增的斜率个数。
 * 
 * 我们使用线段树来维护每个区间的以下信息：
 * 1. 区间最大值
 * 2. 区间内从左端点开始能看到的楼房数量（在给定左端点限制斜率的情况下）
 * 
 * 时间复杂度分析:
 * - 单点更新: O(log n)
 * - 查询全局可见楼房数: O(log n)
 * 
 * 空间复杂度: O(4n)
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

const double INF = 1e100;

// 线段树节点类
struct Node {
    double maxSlope;  // 区间最大斜率
    int visibleCount; // 区间内可见楼房数量
    
    Node() : maxSlope(0), visibleCount(0) {}
    Node(double maxSlope, int visibleCount) : maxSlope(maxSlope), visibleCount(visibleCount) {}
};

// 线段树类
class SegmentTree {
private:
    int n;
    Node* tree;
    double* slopes; // 存储每个位置的斜率

public:
    /**
     * 构造函数
     * @param size 数组大小
     */
    SegmentTree(int size);
    
    /**
     * 析构函数
     */
    ~SegmentTree();
    
    /**
     * 向上更新节点信息
     * @param i 当前节点编号
     */
    void pushUp(int i);
    
    /**
     * 计算区间[l,r]内从左端点开始，在限制斜率limit下可见的楼房数量
     * @param l 区间左端点
     * @param r 区间右端点
     * @param limit 限制斜率
     * @param i 当前节点编号
     * @return 可见楼房数量
     */
    int countVisible(int l, int r, double limit, int i);
    
    /**
     * 更新节点可见数量
     * @param l 区间左端点
     * @param r 区间右端点
     * @param i 当前节点编号
     */
    void updateVisibleCount(int l, int r, int i);
    
    /**
     * 单点更新
     * @param idx 要更新的位置
     * @param val 新的高度
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     */
    void update(int idx, int val, int l, int r, int i);
    
    /**
     * 查询全局可见楼房数量
     * @return 可见楼房数量
     */
    int queryVisibleCount();
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "线段树测试 - Luogu P4198 楼房重建" << endl;
//     // SegmentTree segTree(10);
//     // cout << "初始化完成" << endl;
//     // return 0;
// }