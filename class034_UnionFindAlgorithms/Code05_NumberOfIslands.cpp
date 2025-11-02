/**
 * 岛屿数量 (Number of Islands) - C++深度优化实现
 * 
 * 题目描述：
 * 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，
 * 请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 
 * 测试链接: https://leetcode.cn/problems/number-of-islands/
 * 
 * 算法核心思想深度解析：
 * ================================================================
 * 1. 问题建模与连通性分析
 *    - 将二维网格建模为图结构，其中陆地单元格是节点，相邻关系是边
 *    - 关键洞察：岛屿就是图中的连通分量，计算岛屿数量等价于计算连通分量数量
 *    - 并查集是处理此类动态连通性问题的理想数据结构
 *    
 * 2. 坐标映射策略的数学原理
 *    - 将二维坐标(i,j)映射到一维索引：index = i * cols + j
 *    - 这种映射保持了网格的拓扑结构，便于相邻关系处理
 *    - 映射公式的数学性质：相邻单元格的索引差为±1或±cols
 * 
 * 3. 水域处理的创新方法
 *    - 统计水域单元格数量，从总连通分量中减去
 *    - 这种方法避免了专门处理水域节点的复杂性
 *    - 数学正确性：每个水域单元格初始都是一个独立的连通分量
 * 
 * 时间复杂度严格分析：
 * ================================================================
 * - 网格遍历：O(m*n) - 必须访问每个单元格
 * - 并查集操作：每个单元格最多进行4次合并操作
 * - 每次合并操作：O(α(m*n))，其中α是阿克曼函数的反函数
 * - 总体时间复杂度：O(m*n*α(m*n)) ≈ O(m*n) - 对于实际应用规模
 * - 理论下界：Ω(m*n)，因为必须检查每个单元格
 * 
 * 空间复杂度分析：
 * ================================================================
 * - 并查集数据结构：O(m*n) - 父节点数组和秩数组
 * - 方向数组：O(1) - 固定大小的4个方向
 * - 总体空间复杂度：O(m*n) - 线性于网格大小
 * 
 * 最优解判定：✅ 是最优解
 * - 理论下界匹配：时间复杂度O(m*n)匹配问题规模的理论下界
 * - 空间效率：空间复杂度O(m*n)是最优的，无法进一步优化
 * - 算法特性：并查集特别适合处理动态连通性问题
 * - 实践验证：在LeetCode等平台上被广泛接受为最优解之一
 * 
 * 工程化深度考量：
 * ================================================================
 * 1. 异常处理与鲁棒性设计
 *    - 输入验证：检查空网格、不规则网格等异常情况
 *    - 字符验证：确保网格只包含'0'和'1'字符
 *    - 边界处理：专门处理单行、单列等边界情况
 * 
 * 2. 性能优化策略
 *    - 路径压缩：大幅优化查找操作的时间复杂度
 *    - 按秩合并：保持树结构平衡，避免退化
 *    - 内存预分配：避免动态扩容带来的性能开销
 *    - 缓存友好：使用连续内存布局提高缓存命中率
 * 
 * 3. 代码质量与可维护性
 *    - 模块化设计：将并查集封装为独立类，职责分离
 *    - 清晰的接口：提供完整的API文档和类型注解
 *    - 测试覆盖：包含全面的单元测试和边界测试
 * 
 * 调试技巧与问题定位实战指南：
 * ================================================================
 * 1. 基础调试方法：
 *    - 打印网格状态：可视化输入网格
 *    - 跟踪合并过程：记录每次合并操作
 *    - 检查坐标映射：验证二维到一维映射的正确性
 * 
 * 2. 高级调试技术：
 *    - 性能剖析：使用性能分析工具分析性能瓶颈
 *    - 内存分析：检查内存使用情况和泄漏风险
 * 
 * 3. 笔试面试调试策略：
 *    - 小例子测试：使用2x2或3x3网格快速验证
 *    - 边界值测试：专门测试边界情况
 *    - 打印调试：通过打印关键变量定位问题
 * 
 * 作者：algorithm-journey
 * 版本：v2.0 深度优化版
 * 日期：2025年10月23日
 * 许可证：开源项目，欢迎贡献和改进
 */

#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <chrono>
#include <utility>  // 添加pair头文件
#include <cstdlib>  // 添加rand和srand头文件
#include <ctime>    // 添加time头文件

using namespace std;

/**
 * 并查集类 - 支持路径压缩和按秩合并优化
 */
class UnionFind {
private:
    vector<int> parent;  // 父节点数组
    vector<int> rank;    // 秩数组，用于按秩合并优化
    int count;           // 连通分量计数器

public:
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    UnionFind(int n) : parent(n), rank(n, 1), count(n) {
        // 初始时每个节点都是自己的父节点
        for (int i = 0; i < n; ++i) {
            parent[i] = i;
        }
    }

    /**
     * 查找节点x的根节点（带路径压缩优化）
     * @param x 要查找的节点
     * @return 节点x所在集合的根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将路径上的所有节点直接连接到根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 合并包含节点x和y的集合（带按秩合并优化）
     * @param x 第一个节点
     * @param y 第二个节点
     */
    void unionSets(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        // 如果已经在同一个集合中，直接返回
        if (rootX == rootY) {
            return;
        }
        
        // 按秩合并：将秩小的树合并到秩大的树下
        if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        // 合并后连通分量数量减1
        count--;
    }

    /**
     * 获取当前连通分量的数量
     * @return 连通分量数量
     */
    int getCount() const {
        return count;
    }
};

/**
 * 计算二维网格中的岛屿数量
 * @param grid 二维字符网格，包含'0'（水）和'1'（陆地）
 * @return 岛屿的数量
 * @throws invalid_argument 如果输入网格无效
 */
int numIslands(vector<vector<char>>& grid) {
    // 输入验证
    if (grid.empty() || grid[0].empty()) {
        return 0;
    }
    
    int rows = grid.size();
    int cols = grid[0].size();
    
    // 验证网格的规则性
    for (int i = 1; i < rows; ++i) {
        if (grid[i].size() != cols) {
            throw invalid_argument("网格行长度不一致");
        }
    }
    
    // 方向数组：上、右、下、左
    vector<pair<int, int>> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    // 初始化并查集
    UnionFind uf(rows * cols);
    
    // 水域计数器
    int waterCount = 0;
    
    // 遍历网格中的每个单元格
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            // 验证字符有效性
            if (grid[i][j] != '0' && grid[i][j] != '1') {
                throw invalid_argument("网格包含无效字符");
            }
            
            // 如果是水域，增加水域计数并跳过
            if (grid[i][j] == '0') {
                waterCount++;
                continue;
            }
            
            // 当前单元格的一维索引
            int currentIndex = i * cols + j;
            
            // 检查四个方向的相邻单元格
            for (const auto& dir : directions) {
                int ni = i + dir.first;
                int nj = j + dir.second;
                
                // 检查相邻单元格是否在网格范围内且是陆地
                if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && grid[ni][nj] == '1') {
                    int neighborIndex = ni * cols + nj;
                    uf.unionSets(currentIndex, neighborIndex);
                }
            }
        }
    }
    
    // 岛屿数量 = 总连通分量数 - 水域数量
    return uf.getCount() - waterCount;
}

/**
 * 测试函数：验证算法的正确性
 */
void testNumIslands() {
    cout << "开始岛屿数量测试..." << endl;
    
    // 测试用例1：标准情况
    vector<vector<char>> grid1 = {
        {'1', '1', '1', '1', '0'},
        {'1', '1', '0', '1', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '0', '0', '0'}
    };
    int result1 = numIslands(grid1);
    int expected1 = 1;
    cout << "测试用例1: 结果=" << result1 << ", 预期=" << expected1 
         << ", " << (result1 == expected1 ? "通过" : "失败") << endl;
    
    // 测试用例2：多个岛屿
    vector<vector<char>> grid2 = {
        {'1', '1', '0', '0', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '1', '0', '0'},
        {'0', '0', '0', '1', '1'}
    };
    int result2 = numIslands(grid2);
    int expected2 = 3;
    cout << "测试用例2: 结果=" << result2 << ", 预期=" << expected2 
         << ", " << (result2 == expected2 ? "通过" : "失败") << endl;
    
    // 测试用例3：全是水域
    vector<vector<char>> grid3 = {
        {'0', '0', '0'},
        {'0', '0', '0'},
        {'0', '0', '0'}
    };
    int result3 = numIslands(grid3);
    int expected3 = 0;
    cout << "测试用例3: 结果=" << result3 << ", 预期=" << expected3 
         << ", " << (result3 == expected3 ? "通过" : "失败") << endl;
    
    // 测试用例4：全是陆地
    vector<vector<char>> grid4 = {
        {'1', '1'},
        {'1', '1'}
    };
    int result4 = numIslands(grid4);
    int expected4 = 1;
    cout << "测试用例4: 结果=" << result4 << ", 预期=" << expected4 
         << ", " << (result4 == expected4 ? "通过" : "失败") << endl;
    
    // 测试用例5：单行网格
    vector<vector<char>> grid5 = {{'1', '0', '1', '0', '1'}};
    int result5 = numIslands(grid5);
    int expected5 = 3;
    cout << "测试用例5: 结果=" << result5 << ", 预期=" << expected5 
         << ", " << (result5 == expected5 ? "通过" : "失败") << endl;
    
    // 测试用例6：单列网格
    vector<vector<char>> grid6 = {{'1'}, {'0'}, {'1'}, {'0'}, {'1'}};
    int result6 = numIslands(grid6);
    int expected6 = 3;
    cout << "测试用例6: 结果=" << result6 << ", 预期=" << expected6 
         << ", " << (result6 == expected6 ? "通过" : "失败") << endl;
    
    // 测试用例7：空网格
    vector<vector<char>> grid7;
    int result7 = numIslands(grid7);
    int expected7 = 0;
    cout << "测试用例7: 结果=" << result7 << ", 预期=" << expected7 
         << ", " << (result7 == expected7 ? "通过" : "失败") << endl;
    
    cout << "测试完成！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "开始性能测试..." << endl;
    
    // 生成大规模测试数据
    int rows = 100, cols = 100;
    vector<vector<char>> largeGrid(rows, vector<char>(cols));
    
    // 随机生成陆地和水域（70%陆地，30%水域）
    srand(time(nullptr));
    for (int i = 0; i < rows; ++i) {
        for (int j = 0; j < cols; ++j) {
            largeGrid[i][j] = (rand() % 100 < 70) ? '1' : '0';
        }
    }
    
    // 测试执行时间
    auto start = chrono::high_resolution_clock::now();
    int result = numIslands(largeGrid);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "大规模网格(" << rows << "x" << cols << ")测试:" << endl;
    cout << "岛屿数量: " << result << endl;
    cout << "执行时间: " << duration.count() << "微秒" << endl;
    cout << "性能评估: " << (duration.count() < 1000 ? "优秀" : "良好") << endl;
}

/**
 * 演示使用示例
 */
void demo() {
    cout << "使用示例:" << endl;
    
    vector<vector<char>> sampleGrid = {
        {'1', '1', '0', '0', '0'},
        {'1', '1', '0', '0', '0'},
        {'0', '0', '1', '0', '0'},
        {'0', '0', '0', '1', '1'}
    };
    
    cout << "输入网格:" << endl;
    for (const auto& row : sampleGrid) {
        for (char cell : row) {
            cout << cell << " ";
        }
        cout << endl;
    }
    
    int islands = numIslands(sampleGrid);
    cout << "岛屿数量: " << islands << endl;
}

int main() {
    try {
        // 运行单元测试
        testNumIslands();
        cout << endl;
        
        // 运行性能测试
        performanceTest();
        cout << endl;
        
        // 演示使用示例
        demo();
        cout << endl;
        
        cout << "C++实现特点总结:" << endl;
        cout << "1. 高性能：直接内存操作，零开销抽象" << endl;
        cout << "2. 内存控制：精确的内存管理，避免泄漏" << endl;
        cout << "3. 类型安全：强类型系统，编译时检查" << endl;
        cout << "4. 标准库：丰富的STL容器和算法支持" << endl;
        cout << "5. 跨平台：良好的可移植性和兼容性" << endl;
        
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * 编译和运行说明：
 * 
 * 1. 使用g++编译：
 *    g++ -std=c++11 -O2 Code05_NumberOfIslands.cpp -o num_islands
 * 
 * 2. 运行程序：
 *    ./num_islands
 * 
 * 3. 编译选项说明：
 *    -std=c++11: 使用C++11标准
 *    -O2: 优化级别2，提高性能
 *    -o num_islands: 指定输出文件名
 * 
 * 4. 平台兼容性：
 *    - Windows: 使用MinGW或Visual Studio编译
 *    - Linux: 使用g++或clang++编译
 *    - macOS: 使用clang++编译
 * 
 * 5. 调试版本编译：
 *    g++ -std=c++11 -g -DDEBUG Code05_NumberOfIslands.cpp -o num_islands_debug
 */