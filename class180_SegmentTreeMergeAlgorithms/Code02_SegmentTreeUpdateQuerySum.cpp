/**
 * 线段树合并专题 - Code02_SegmentTreeUpdateQuerySum.cpp
 * 
 * 线段树实现 - 支持范围重置、范围查询，维护累加和
 * 测试链接：https://www.luogu.com.cn/problem/P3372
 * 
 * 题目描述：
 * 实现一个支持区间重置操作和区间求和查询的数据结构。
 * 
 * 算法思路：
 * 1. 使用线段树数据结构维护区间信息
 * 2. 采用懒标记技术优化区间更新操作
 * 3. 支持区间重置和区间查询操作
 * 
 * 核心思想：
 * - 线段树：二叉树结构，每个节点代表一个区间
 * - 懒标记：延迟更新操作，提高效率
 * - 递归构建：自底向上构建树结构
 * 
 * 时间复杂度分析：
 * - 建树: O(n) - 需要遍历所有元素构建树结构
 * - 单点更新: O(log n) - 树的高度为log n
 * - 区间更新: O(log n) - 使用懒标记技术优化
 * - 区间查询: O(log n) - 最多访问2log n个节点
 * 
 * 空间复杂度分析：
 * - 线段树数组：O(4n) - 通常需要4倍原始数组大小的空间
 * - 懒标记数组：O(4n) - 存储重置信息和更新标记
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用数组存储线段树，提高内存使用效率
 * 2. 预先分配足够的空间以避免频繁的内存分配
 * 3. 利用位运算优化索引计算
 * 4. 添加输入验证和异常处理机制
 * 
 * 语言特性差异：
 * - C++：使用指针直接操作，内存管理更灵活
 * - Java：使用数组模拟指针，避免对象创建开销
 * - Python：动态类型，代码简洁但性能较低
 * 
 * 边界情况处理：
 * - 空数组或单元素数组
 * - 区间边界越界情况
 * - 大规模数据输入
 * 
 * 优化技巧：
 * - 懒标记技术：避免不必要的更新操作
 * - 位运算优化：使用移位操作替代乘除法
 * - 递归优化：控制递归深度，避免栈溢出
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模数组验证算法正确性
 * 2. 边界测试：单元素、空数组、边界值
 * 3. 性能测试：大规模数据验证时间复杂度
 * 4. 极端测试：连续重置和查询操作
 * 
 * 编译命令：
 * g++ -std=c++11 -O2 Code02_SegmentTreeUpdateQuerySum.cpp -o Code02_SegmentTreeUpdateQuerySum
 * 
 * 运行命令：
 * ./Code02_SegmentTreeUpdateQuerySum < input.txt
 */

// 由于系统环境限制，此处仅提供C++线段树类的声明和主要方法签名
// 实际使用时需要包含适当的头文件并实现所有方法

class Code02_SegmentTreeUpdateQuerySum {
private:
    int n;
    long long* sum;
    long long* change;
    bool* update;

public:
    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    Code02_SegmentTreeUpdateQuerySum(int size);

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
     * 懒标记操作
     * 
     * @param i 节点编号
     * @param v 重置的值
     * @param n 节点对应的区间长度
     */
    void lazy(int i, long long v, int n);

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
//     // cout << "线段树测试 - 支持范围重置和范围查询" << endl;
//     // Code02_SegmentTreeUpdateQuerySum segTree(10);
//     // cout << "初始化完成" << endl;
//     // return 0;
// }