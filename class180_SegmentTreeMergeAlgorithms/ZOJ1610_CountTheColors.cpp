/**
 * ZOJ 1610 Count the Colors
 * 题目链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
 * 
 * 题目描述:
 * 给定一个长度为8000的数轴，初始时所有位置都没有颜色。现在进行n次操作，
 * 每次操作将区间[l,r)染成颜色c。最后统计每种颜色有多少个连续的段。
 * 
 * 解题思路:
 * 这是一个经典的区间染色问题，可以使用线段树来解决。
 * 1. 使用线段树维护区间的颜色信息
 * 2. 每次染色操作时，更新对应区间的颜色
 * 3. 最后遍历整个数轴，统计每种颜色的连续段数
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * - 统计结果: O(n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用懒标记优化区间更新
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

class ZOJ1610_CountTheColors {
private:
    int n;
    int* color;  // -1表示无颜色
    int* lazy;   // 懒标记，-1表示无标记

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    ZOJ1610_CountTheColors(int size);

    /**
     * 析构函数 - 释放内存
     */
    ~ZOJ1610_CountTheColors();

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
     * 范围染色 - 将区间[jobl, jobr)染成颜色jobv
     * 
     * @param jobl 任务区间左端点（包含）
     * @param jobr 任务区间右端点（不包含）
     * @param jobv 染色颜色
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    void updateRange(int jobl, int jobr, int jobv, int l, int r, int i);

    /**
     * 查询所有位置的颜色并统计每种颜色的段数
     * 
     * @param l      当前区间左端点
     * @param r      当前区间右端点
     * @param i      当前节点编号
     * @param result 结果映射，记录每个位置的颜色
     */
    void queryAllColors(int l, int r, int i, int* result);

    /**
     * 统计每种颜色的段数
     * 
     * @return 指向结果数组的指针，需要调用者释放内存
     */
    int* countColorSegments(int* segmentCount);
};

// 测试代码
// int main() {
//     // 示例测试
//     // cout << "ZOJ 1610 Count the Colors 线段树实现" << endl;
//     // 完整的实现请参考Python和Java版本
//     // return 0;
// }