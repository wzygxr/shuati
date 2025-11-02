/**
 * SPOJ GSS2 - Can you answer these queries II
 * 
 * 题目描述:
 * 给定一个整数数组，多次查询某个区间内的最大子段和，但重复元素只计算一次。
 * 
 * 解题思路:
 * 这是一个非常经典的线段树问题，需要使用扫描线算法和历史信息维护。
 * 
 * 关键点:
 * 1. 使用离线处理，将所有查询按右端点排序
 * 2. 使用扫描线从左到右处理数组元素
 * 3. 维护一个线段树，支持区间加法和历史最大值查询
 * 4. 使用last数组记录每个值最后出现的位置
 * 
 * 时间复杂度: O((n + q) * log n)
 * 空间复杂度: O(n)
 */

struct SegmentTreeNode {
    long long maxSum;      // 区间最大子段和
    long long prefixSum;   // 包含左端点的最大子段和
    long long suffixSum;   // 包含右端点的最大子段和
    long long totalSum;    // 区间总和
    
    SegmentTreeNode() : maxSum(0), prefixSum(0), suffixSum(0), totalSum(0) {}
    
    SegmentTreeNode(long long maxSum, long long prefixSum, long long suffixSum, long long totalSum) 
        : maxSum(maxSum), prefixSum(prefixSum), suffixSum(suffixSum), totalSum(totalSum) {}
};

class SegmentTree {
private:
    int n;
    long long* arr;
    SegmentTreeNode* tree;
    long long* lazy;
    
public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     * @param array 数组元素
     */
    SegmentTree(int size, long long* array);
    
    /**
     * 析构函数
     */
    ~SegmentTree();
    
    /**
     * 区间加法更新
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 任务值
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点索引
     */
    void updateRange(int jobl, int jobr, long long jobv, int l, int r, int i);
    
    /**
     * 区间最大子段和查询
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点索引
     * @return 区间最大子段和
     */
    long long queryRange(int jobl, int jobr, int l, int r, int i);
    
private:
    /**
     * 构建线段树
     * 
     * @param i 当前节点索引
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     */
    void build(int i, int l, int r);
    
    /**
     * 下推操作
     * 
     * @param i 当前节点索引
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     */
    void pushDown(int i, int l, int r);
    
    /**
     * 上推操作
     * 
     * @param i 当前节点索引
     */
    void pushUp(int i);
};

struct Query {
    int l, r, id;
    
    Query() : l(0), r(0), id(0) {}
    
    Query(int l, int r, int id) : l(l), r(r), id(id) {}
    
    bool operator<(const Query& other) const {
        return r < other.r;
    }
};