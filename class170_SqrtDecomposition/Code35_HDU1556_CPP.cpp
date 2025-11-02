#include <iostream>
#include <vector>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <string>
#include <sstream>
using namespace std;

/**
 * HDU 1556 Color the ball
 * 题目要求：区间更新，单点查询
 * 核心技巧：分块标记（懒惰标记）
 * 时间复杂度：O(√n) / 操作
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1556
 * 
 * 算法思想详解：
 * 1. 将数组分成√n大小的块
 * 2. 对于完全覆盖的块，使用懒惰标记记录增量
 * 3. 对于部分覆盖的块，暴力更新每个元素
 * 4. 查询时，累加块标记和元素自身的值
 */

class BlockColor {
private:
    vector<int> arr;       // 原始数组
    vector<int> blockAdd;  // 块的懒惰标记
    int blockSize;         // 块的大小
    int n;                 // 数组长度
    
public:
    /**
     * 构造函数，初始化分块数据结构
     * 
     * @param size 数组大小
     */
    BlockColor(int size) : n(size) {
        // 计算块的大小，通常取√n
        blockSize = static_cast<int>(sqrt(n)) + 1;
        arr.resize(n + 1, 0);  // 题目中的球是1-based编号
        blockAdd.resize((n + blockSize - 1) / blockSize, 0);
    }
    
    /**
     * 区间更新操作：将区间[l, r]的每个元素加1
     * 
     * @param l 左边界（1-based）
     * @param r 右边界（1-based）
     */
    void updateRange(int l, int r) {
        // 处理越界情况
        if (l < 1) l = 1;
        if (r > n) r = n;
        if (l > r) return;
        
        int blockL = (l - 1) / blockSize;
        int blockR = (r - 1) / blockSize;
        
        // 同一块内，直接暴力更新
        if (blockL == blockR) {
            for (int i = l; i <= r; ++i) {
                ++arr[i];
            }
            return;
        }
        
        // 处理左边不完整的块
        for (int i = l; i <= (blockL + 1) * blockSize; ++i) {
            ++arr[i];
        }
        
        // 处理中间完整的块，使用懒惰标记
        for (int i = blockL + 1; i < blockR; ++i) {
            ++blockAdd[i];
        }
        
        // 处理右边不完整的块
        for (int i = blockR * blockSize + 1; i <= r; ++i) {
            ++arr[i];
        }
    }
    
    /**
     * 单点查询操作：查询位置x的值
     * 
     * @param x 查询位置（1-based）
     * @return 位置x的值
     */
    int queryPoint(int x) {
        // 处理越界情况
        if (x < 1 || x > n) {
            throw invalid_argument("查询位置越界: " + to_string(x));
        }
        
        int blockIndex = (x - 1) / blockSize;
        // 元素值 = 原始值 + 所属块的标记值
        return arr[x] + blockAdd[blockIndex];
    }
    
    /**
     * 重置所有数据为初始状态
     */
    void clear() {
        fill(arr.begin(), arr.end(), 0);
        fill(blockAdd.begin(), blockAdd.end(), 0);
    }
    
    /**
     * 获取数组长度
     */
    int getSize() const {
        return n;
    }
};

/**
 * 运行标准测试，按题目输入格式处理
 */
void runTest() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    while (cin >> n && n != 0) {
        BlockColor solution(n);
        
        // 处理n个操作
        for (int i = 0; i < n; ++i) {
            int l, r;
            cin >> l >> r;
            solution.updateRange(l, r);
        }
        
        // 输出每个点的最终颜色数
        for (int i = 1; i <= n; ++i) {
            cout << solution.queryPoint(i);
            if (i < n) {
                cout << " ";
            }
        }
        cout << endl;
    }
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 测试不同规模的数据
    const vector<int> testSizes = {100, 1000, 10000, 100000};
    
    for (int size : testSizes) {
        BlockColor solution(size);
        
        clock_t startTime = clock();
        
        // 执行size次随机操作
        for (int i = 0; i < size; ++i) {
            int l = rand() % size + 1;
            int r = rand() % size + 1;
            if (l > r) {
                swap(l, r);
            }
            solution.updateRange(l, r);
        }
        
        // 执行查询
        for (int i = 1; i <= size; i += 100) {
            solution.queryPoint(i);
        }
        
        clock_t endTime = clock();
        double elapsed = double(endTime - startTime) / CLOCKS_PER_SEC * 1000;
        cout << "数据规模 " << size << ", 耗时: " << elapsed << " ms" << endl;
    }
}

/**
 * 测试正确性的函数
 */
void correctnessTest() {
    cout << "=== 正确性测试 ===" << endl;
    
    // 简单测试案例
    BlockColor solution(5);
    
    // 执行更新操作
    solution.updateRange(1, 3);
    solution.updateRange(2, 5);
    solution.updateRange(1, 1);
    
    // 检查结果
    const vector<int> expected = {0, 2, 2, 2, 1, 1}; // expected[0]无效，从1开始
    bool allCorrect = true;
    
    cout << "查询结果：" << endl;
    for (int i = 1; i <= 5; ++i) {
        int actual = solution.queryPoint(i);
        cout << "位置 " << i << ": 预期=" << expected[i] << ", 实际=" << actual << endl;
        if (actual != expected[i]) {
            allCorrect = false;
        }
    }
    
    cout << "测试" << (allCorrect ? "通过" : "失败") << endl;
}

/**
 * 主函数
 */
int main() {
    srand(time(nullptr)); // 初始化随机数种子
    
    cout << "HDU 1556 Color the ball 解决方案" << endl;
    cout << "1. 运行标准测试（按题目输入格式）" << endl;
    cout << "2. 运行正确性测试" << endl;
    cout << "3. 运行性能测试" << endl;
    
    cout << "请选择测试类型：";
    int choice;
    cin >> choice;
    
    switch (choice) {
        case 1:
            runTest();
            break;
        case 2:
            correctnessTest();
            break;
        case 3:
            performanceTest();
            break;
        default:
            cout << "无效选择，运行正确性测试" << endl;
            correctnessTest();
            break;
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用vector容器进行动态内存管理
 * 2. 使用ios::sync_with_stdio(false)和cin.tie(nullptr)加速输入输出
 * 3. 使用const引用传递参数以减少拷贝开销
 * 4. 使用fill算法快速重置数组
 * 5. 内联成员函数可以进一步优化性能
 * 
 * 时间复杂度分析：
 * - 区间更新：
 *   - 对于完整块：O(1)，只更新懒惰标记
 *   - 对于不完整块：O(√n)，最多处理两个不完整块，每个最多√n个元素
 *   - 总时间复杂度：O(√n)
 * 
 * - 单点查询：O(1)，直接返回arr[x] + blockAdd[blockIndex]
 * 
 * 空间复杂度分析：
 * - 数组arr：O(n)
 * - 懒惰标记数组blockAdd：O(√n)
 * - 总空间复杂度：O(n + √n) = O(n)
 * 
 * 代码优化建议：
 * 1. 对于大规模数据，可以考虑使用更高效的数据结构
 * 2. 可以尝试不同的块大小，找到最优的性能平衡点
 * 3. 对于多次查询同一个点的场景，可以添加缓存机制
 * 4. 考虑使用位操作或其他优化技巧进一步减少运行时间
 */