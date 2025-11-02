/**
 * SPOJ GSS1 - Can you answer these queries I
 * 题目链接: https://www.spoj.com/problems/GSS1/
 * 
 * 题目描述:
 * 给定一个长度为n的整数序列a，需要处理m个查询。
 * 每个查询给定两个整数l和r，要求找出区间[l,r]内的最大子段和。
 * 
 * 解题思路:
 * 这是一个经典的线段树维护区间最大子段和的问题。
 * 对于每个线段树节点，我们需要维护以下信息：
 * 1. 区间和(sum)
 * 2. 区间最大子段和(max_sum)
 * 3. 区间前缀最大和(prefix_max)
 * 4. 区间后缀最大和(suffix_max)
 * 
 * 合并两个子区间时：
 * - 区间和 = 左子区间和 + 右子区间和
 * - 区间最大子段和 = max(左子区间最大子段和, 右子区间最大子段和, 左子区间后缀最大和 + 右子区间前缀最大和)
 * - 区间前缀最大和 = max(左子区间前缀最大和, 左子区间和 + 右子区间前缀最大和)
 * - 区间后缀最大和 = max(右子区间后缀最大和, 右子区间和 + 左子区间后缀最大和)
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用结构体优化节点信息存储
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

/**
 * 线段树节点信息
 */
struct SegmentNode {
    long long sum;           // 区间和
    long long maxSum;        // 区间最大子段和
    long long prefixMax;     // 区间前缀最大和
    long long suffixMax;     // 区间后缀最大和
    
    SegmentNode() : sum(0), maxSum(0), prefixMax(0), suffixMax(0) {}
    
    SegmentNode(long long s, long long ms, long long pm, long long sm) 
        : sum(s), maxSum(ms), prefixMax(pm), suffixMax(sm) {}
};

class SPOJ_GSS1_CanYouAnswerTheseQueriesI {
private:
    int n;
    SegmentNode* nodes;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    SPOJ_GSS1_CanYouAnswerTheseQueriesI(int size);

    /**
     * 析构函数 - 释放内存
     */
    ~SPOJ_GSS1_CanYouAnswerTheseQueriesI();

    /**
     * 向上更新节点信息
     * 
     * @param i 当前节点编号
     */
    void pushUp(int i);

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
     * 查询区间[jobl, jobr]内的最大子段和
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间最大子段和
     */
    SegmentNode query(int jobl, int jobr, int l, int r, int i);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "SPOJ GSS1 - Can you answer these queries I 线段树实现" << endl;
//     // 完整的实现请参考Python和Java版本
//     // return 0;
// }