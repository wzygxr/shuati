/**
 * SPOJ GSS5 - Can you answer these queries V
 * 
 * 题目描述:
 * 给定一个整数数组，多次查询某个区间内的最大子段和，查询区间可能不连续。
 * 
 * 解题思路:
 * 这是一个线段树的经典应用。与GSS1不同的是，GSS5的查询区间可能不连续，需要特殊处理。
 * 
 * 关键点:
 * 1. 使用线段树维护区间最大子段和信息
 * 2. 对于每个查询(x1, y1, x2, y2)，需要找到在区间[x1, y1]中以y1结尾的最大子段和，
 *    以及在区间[x2, y2]中以x2开头的最大子段和
 * 3. 如果y1 < x2，则结果为区间[y1+1, x2-1]的总和加上前面和后面的部分
 * 4. 如果y1 >= x2，则需要特殊处理重叠区间
 * 
 * 时间复杂度: O(n + q * log n)
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
     * 构建线段树
     * 
     * @param i 当前节点索引
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     */
    void build(int i, int l, int r);
    
    /**
     * 上推操作
     * 
     * @param i 当前节点索引
     */
    void pushUp(int i);
    
    /**
     * 区间最大子段和查询
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点索引
     * @return 区间最大子段和信息
     */
    SegmentTreeNode queryRange(int jobl, int jobr, int l, int r, int i);
    
    /**
     * 查询区间总和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点索引
     * @return 区间总和
     */
    long long querySum(int jobl, int jobr, int l, int r, int i);
};

struct Query {
    int x1, y1, x2, y2;
    
    Query(int x1, int y1, int x2, int y2) : x1(x1), y1(y1), x2(x2), y2(y2) {}
};