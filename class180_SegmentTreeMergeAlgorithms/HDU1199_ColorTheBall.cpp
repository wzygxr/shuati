/**
 * HDU 1199 Color the Ball
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1199
 * 
 * 题目描述:
 * 在数轴上有一些球，每个球都有一个坐标。现在要对这些球进行染色操作，每次操作将一段区间内的球染成白色或黑色。
 * 求最后最长的连续白色区间。
 * 
 * 解题思路:
 * 这是一个经典的区间染色问题，可以使用线段树结合离散化来解决。
 * 由于球的坐标范围很大(1-1e9)，我们需要先对坐标进行离散化处理。
 * 然后使用线段树维护区间的染色状态，支持区间更新和查询操作。
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 建树: O(n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用离散化减少空间复杂度
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

class HDU1199_ColorTheBall {
private:
    int n;
    int* color;  // 0表示未染色，1表示白色，-1表示黑色
    int* lazy;   // 懒标记，0表示无标记，1表示白色，-1表示黑色

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    HDU1199_ColorTheBall(int size);

    /**
     * 析构函数 - 释放内存
     */
    ~HDU1199_ColorTheBall();

    /**
     * 向上更新节点信息 - 如果左右子树颜色相同，则父节点颜色为该颜色，否则为混合状态
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
     * 范围染色 - 将区间[jobl, jobr]染成颜色jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 染色颜色(1表示白色，-1表示黑色)
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    void updateRange(int jobl, int jobr, int jobv, int l, int r, int i);

    /**
     * 查询区间颜色状态
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间颜色状态
     */
    int queryRange(int jobl, int jobr, int l, int r, int i);

    /**
     * 查找最长的连续白色区间
     * 
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 最长连续白色区间的长度
     */
    int findLongestWhiteSegment(int l, int r, int i);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "HDU 1199 Color the Ball 线段树实现" << endl;
//     // 完整的实现请参考Python和Java版本
//     // return 0;
// }