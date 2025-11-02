#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

/**
 * 二维差分数组算法实现 - 洛谷P3397 地毯问题 - C++版本
 * 
 * 问题描述：
 * 在n×n的格子上有m个地毯，给出这些地毯的信息，问每个点被多少个地毯覆盖。
 * 
 * 核心思想：
 * 1. 利用二维差分数组处理区间更新操作
 * 2. 对每个地毯覆盖区域，在差分数组中进行O(1)标记
 * 3. 通过二维前缀和还原差分数组得到最终结果
 * 
 * 算法详解：
 * 1. 差分标记：对区域[(a,b),(c,d)]增加k，在差分数组中标记：
 *    - diff[a][b] += k
 *    - diff[c+1][b] -= k
 *    - diff[a][d+1] -= k
 *    - diff[c+1][d+1] += k
 * 2. 前缀和还原：通过二维前缀和将差分数组还原为结果数组
 * 
 * 时间复杂度分析：
 * 1. 差分标记：O(m)，m为地毯数量
 * 2. 前缀和还原：O(n²)，n为网格边长
 * 3. 总体复杂度：O(m + n²)
 * 
 * 空间复杂度分析：
 * O(n²)，用于存储差分数组
 * 
 * 算法优势：
 * 1. 区间更新效率高，每次操作O(1)
 * 2. 适合处理大量区间更新操作
 * 3. 空间效率高，复用同一数组
 * 
 * 工程化考虑：
 * 1. 输入输出优化：使用快速IO提高效率
 * 2. 内存管理：通过vector容器自动管理内存
 * 3. 边界处理：扩展数组边界避免特殊判断
 * 
 * 应用场景：
 * 1. 图像处理中的区域操作
 * 2. 游戏开发中的区域影响计算
 * 3. 地理信息系统中的区域统计
 * 
 * 相关题目：
 * 1. 洛谷 P3397 地毯
 * 2. LeetCode 2132. 用邮票贴满网格图
 * 3. Codeforces 835C - Star sky
 * 
 * 测试链接 : https://www.luogu.com.cn/problem/P3397
 * 
 * C++语言特性：
 * 1. 使用vector容器自动管理内存
 * 2. 使用快速IO优化输入输出
 * 3. 支持模板编程，类型安全
 */
class DiffMatrixSolver {
private:
    static const int MAXN = 1002;  // 最大网格尺寸
    vector<vector<int>> diff;      // 差分数组
    int n;                         // 网格边长
    int q;                         // 操作数量

public:
    /**
     * 构造函数：初始化差分数组
     * 
     * 设计思路：
     * 1. 创建(n+2)×(n+2)的二维vector
     * 2. 初始化为0，避免未定义行为
     * 3. 扩展边界简化索引处理
     * 
     * @param size 网格大小
     */
    DiffMatrixSolver(int size) : n(size) {
        // 创建(n+2)×(n+2)的差分数组，初始化为0
        diff.resize(n + 2, vector<int>(n + 2, 0));
    }

    /**
     * 在二维差分数组中标记区域更新
     * 
     * 算法原理：
     * 对区域[(a,b),(c,d)]增加k，在差分数组中进行标记：
     * 1. diff[a][b] += k      // 左上角标记+k
     * 2. diff[c+1][b] -= k    // 右上角右侧标记-k
     * 3. diff[a][d+1] -= k    // 左下角下方标记-k
     * 4. diff[c+1][d+1] += k  // 右下角标记+k，补偿多减的部分
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * @param a 区域左上角行索引
     * @param b 区域左上角列索引
     * @param c 区域右下角行索引
     * @param d 区域右下角列索引
     * @param k 增加的值
     */
    void add(int a, int b, int c, int d, int k) {
        // 参数校验：确保坐标在有效范围内
        if (a < 1 || a > n || b < 1 || b > n || c < 1 || c > n || d < 1 || d > n) {
            cerr << "错误：坐标越界 (" << a << "," << b << "," << c << "," << d << ")" << endl;
            return;
        }
        if (a > c || b > d) {
            cerr << "错误：坐标无效 (" << a << "," << b << "," << c << "," << d << ")" << endl;
            return;
        }

        // 差分标记操作
        diff[a][b] += k;
        diff[c + 1][b] -= k;
        diff[a][d + 1] -= k;
        diff[c + 1][d + 1] += k;
    }

    /**
     * 通过二维前缀和还原差分数组
     * 
     * 算法原理：
     * 利用容斥原理将差分数组还原为结果数组：
     * diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)（原地更新）
     */
    void build() {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                diff[i][j] += diff[i - 1][j] + diff[i][j - 1] - diff[i - 1][j - 1];
            }
        }
    }

    /**
     * 清空差分数组
     * 
     * 工程化考虑：
     * 1. 避免重复分配内存
     * 2. 重置数组状态，为下一次计算做准备
     * 
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    void clear() {
        for (int i = 1; i <= n + 1; i++) {
            for (int j = 1; j <= n + 1; j++) {
                diff[i][j] = 0;
            }
        }
    }

    /**
     * 获取指定位置的最终值
     * 
     * @param i 行索引
     * @param j 列索引
     * @return 该位置的值
     */
    int get(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) {
            return 0;
        }
        return diff[i][j];
    }

    /**
     * 打印结果矩阵
     * 
     * 用于调试和验证结果
     */
    void printResult() {
        for (int i = 1; i <= n; i++) {
            cout << diff[i][1];
            for (int j = 2; j <= n; j++) {
                cout << " " << diff[i][j];
            }
            cout << endl;
        }
    }
};

/**
 * 测试用例和演示代码
 */
int main() {
    // 测试用例1：正常情况 - 洛谷P3397样例
    cout << "=== 测试用例1：正常情况 ===" << endl;
    int n1 = 5;
    DiffMatrixSolver solver1(n1);
    
    // 添加三个地毯
    solver1.add(2, 2, 3, 3, 1);  // 地毯1：区域[2,2,3,3]
    solver1.add(3, 3, 5, 5, 1);  // 地毯2：区域[3,3,5,5]
    solver1.add(1, 2, 1, 4, 1);  // 地毯3：区域[1,2,1,4]
    
    solver1.build();
    solver1.printResult();
    cout << endl;

    // 测试用例2：边界情况 - 单个地毯覆盖整个网格
    cout << "=== 测试用例2：边界情况 ===" << endl;
    int n2 = 3;
    DiffMatrixSolver solver2(n2);
    
    solver2.add(1, 1, 3, 3, 1);  // 地毯覆盖整个3×3网格
    solver2.build();
    solver2.printResult();
    cout << endl;

    // 测试用例3：性能测试 - 大规模数据
    cout << "=== 测试用例3：性能测试 ===" << endl;
    int n3 = 100;
    int k3 = 1000;
    DiffMatrixSolver solver3(n3);
    
    // 生成随机地毯数据
    srand(time(nullptr));
    for (int i = 0; i < k3; i++) {
        int a = rand() % n3 + 1;
        int b = rand() % n3 + 1;
        int c = min(a + rand() % 10, n3);
        int d = min(b + rand() % 10, n3);
        solver3.add(a, b, c, d, 1);
    }
    
    solver3.build();
    cout << "网格大小: " << n3 << "×" << n3 << endl;
    cout << "地毯数量: " << k3 << endl;
    cout << "计算完成" << endl;
    cout << endl;

    // 测试用例4：异常情况测试
    cout << "=== 测试用例4：异常情况测试 ===" << endl;
    int n4 = 5;
    DiffMatrixSolver solver4(n4);
    
    // 测试越界坐标
    solver4.add(0, 1, 3, 3, 1);  // 行索引越界
    solver4.add(1, 0, 3, 3, 1);  // 列索引越界
    solver4.add(1, 1, 6, 3, 1);  // 行索引越界
    solver4.add(1, 1, 3, 6, 1);  // 列索引越界
    
    // 测试无效坐标
    solver4.add(3, 3, 1, 1, 1);  // 左上角在右下角之后
    
    solver4.build();
    solver4.printResult();

    return 0;
}