/**
 * LeetCode 803 - 打砖块
 * https://leetcode-cn.com/problems/bricks-falling-when-hit/
 * 
 * 题目描述：
 * 有一个 m x n 的二元网格，其中 1 表示砖块，0 表示空白。砖块 稳定（不会掉落）的前提是：
 * - 一块砖直接连接到网格的顶部，或者
 * - 至少有一块相邻（4 个方向之一）的砖块 稳定 不会掉落时
 * 
 * 给你一个数组 hits ，这是需要依次消除砖块的位置。每当消除 hits[i] = (rowi, coli) 位置上的砖块时，对应位置的砖块（如果存在）会消失，然后其他砖块可能因为这一消除操作而掉落。
 * 一旦砖块掉落，它会立即从网格中消失（即，它不会参与后续的消除操作）。
 * 
 * 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作后掉落的砖块数目。
 * 
 * 注意，消除可能指向是没有砖块的空白位置，如果发生这种情况，则没有砖块掉落。
 * 
 * 解题思路（逆向思维 + 并查集）：
 * 1. 首先将所有要敲打的砖块标记为被敲打过（如果有砖块的话）
 * 2. 将剩余的稳定砖块用并查集连接起来，特别是与顶部相连的砖块
 * 3. 然后逆向处理每次敲打操作：
 *    a. 将被敲打的砖块恢复
 *    b. 检查它的四个方向，如果有砖块，就将它们合并到并查集中
 *    c. 计算恢复后新增的与顶部相连的砖块数量，减去1（被敲打的砖块本身）
 * 4. 最后反转结果数组
 * 
 * 时间复杂度分析：
 * - 初始化：O(m * n)
 * - 构建初始并查集：O(m * n * α(m * n))
 * - 逆向处理每次敲打：O(h * α(m * n))，其中h是敲打次数
 * - 总体时间复杂度：O((m * n + h) * α(m * n)) ≈ O(m * n + h)
 * 
 * 空间复杂度分析：
 * - 并查集：O(m * n)
 * - 辅助数组：O(m * n)
 * - 总体空间复杂度：O(m * n)
 */

#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class HitBricks {
private:
    int rows;
    int cols;
    vector<int> parent;
    vector<int> size;
    const vector<vector<int>> DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // 上、下、左、右

    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     */
    void unite(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return;
        }

        // 将较小的集合合并到较大的集合中
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    /**
     * 将二维坐标转换为一维索引
     * @param row 行号
     * @param col 列号
     * @return 一维索引
     */
    int getIndex(int row, int col) const {
        return row * cols + col;
    }

    /**
     * 检查坐标是否有效
     * @param row 行号
     * @param col 列号
     * @return 是否有效
     */
    bool isValid(int row, int col) const {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    /**
     * 连接砖块与相邻的砖块
     * @param grid 网格
     * @param row 当前砖块的行号
     * @param col 当前砖块的列号
     */
    void connectAdjacentBricks(vector<vector<int>>& grid, int row, int col) {
        int currentIndex = getIndex(row, col);

        // 检查四个方向的相邻砖块
        for (const auto& dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValid(newRow, newCol) && grid[newRow][newCol] == 1) {
                int adjacentIndex = getIndex(newRow, newCol);
                unite(currentIndex, adjacentIndex);
            }
        }
    }

public:
    /**
     * 打砖块
     * @param grid 网格
     * @param hits 要敲打的砖块位置
     * @return 每次敲打后掉落的砖块数量
     */
    vector<int> hitBricks(vector<vector<int>>& grid, vector<vector<int>>& hits) {
        rows = grid.size();
        cols = grid[0].size();
        int totalBricks = rows * cols;

        // 初始化并查集
        parent.resize(totalBricks);
        size.resize(totalBricks, 1);
        for (int i = 0; i < totalBricks; i++) {
            parent[i] = i;
        }

        // 创建网格的副本，并标记被敲打的砖块
        vector<vector<int>> gridCopy(rows, vector<int>(cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                gridCopy[i][j] = grid[i][j];
            }
        }

        // 首先标记所有被敲打的砖块（如果有砖块的话）
        for (const auto& hit : hits) {
            int row = hit[0];
            int col = hit[1];
            gridCopy[row][col] = 0;
        }

        // 构建初始并查集：将所有剩余的砖块连接起来
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gridCopy[i][j] == 1) {
                    connectAdjacentBricks(gridCopy, i, j);
                }
            }
        }

        // 逆向处理每次敲打
        vector<int> result(hits.size(), 0);
        for (int i = hits.size() - 1; i >= 0; i--) {
            int row = hits[i][0];
            int col = hits[i][1];

            // 如果原始网格中该位置没有砖块，跳过
            if (grid[row][col] == 0) {
                result[i] = 0;
                continue;
            }

            // 记录恢复前与顶部相连的砖块数量
            int beforeCount = 0;
            for (int j = 0; j < cols; j++) {
                if (gridCopy[0][j] == 1) {
                    beforeCount = size[find(getIndex(0, j))];
                    break;
                }
            }

            // 恢复砖块
            gridCopy[row][col] = 1;

            // 连接恢复砖块的相邻砖块
            connectAdjacentBricks(gridCopy, row, col);

            // 记录恢复后与顶部相连的砖块数量
            int afterCount = 0;
            for (int j = 0; j < cols; j++) {
                if (gridCopy[0][j] == 1) {
                    afterCount = size[find(getIndex(0, j))];
                    break;
                }
            }

            // 计算掉落的砖块数量（恢复后新增的稳定砖块数，减去恢复的砖块本身）
            result[i] = max(0, afterCount - beforeCount - 1);
        }

        return result;
    }
};

/**
 * 打印结果数组
 * @param result 结果数组
 */
void printResult(const vector<int>& result) {
    cout << "[";
    for (size_t i = 0; i < result.size(); i++) {
        cout << result[i];
        if (i < result.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

/**
 * 主函数，用于测试
 */
int main() {
    HitBricks solution;

    // 测试用例1
    vector<vector<int>> grid1 = {
        {1, 0, 0, 0},
        {1, 1, 1, 0}
    };
    vector<vector<int>> hits1 = {{1, 0}};
    vector<int> result1 = solution.hitBricks(grid1, hits1);
    cout << "测试用例1结果：";
    printResult(result1);
    // 预期输出：[2]

    // 测试用例2
    vector<vector<int>> grid2 = {
        {1, 0, 0, 0},
        {1, 1, 0, 0}
    };
    vector<vector<int>> hits2 = {{1, 1}, {1, 0}};
    vector<int> result2 = solution.hitBricks(grid2, hits2);
    cout << "测试用例2结果：";
    printResult(result2);
    // 预期输出：[0, 0]

    // 测试用例3
    vector<vector<int>> grid3 = {
        {1, 1, 1},
        {0, 1, 0},
        {0, 0, 0}
    };
    vector<vector<int>> hits3 = {{0, 2}, {2, 0}, {0, 1}, {1, 2}};
    vector<int> result3 = solution.hitBricks(grid3, hits3);
    cout << "测试用例3结果：";
    printResult(result3);
    // 预期输出：[0, 0, 1, 0]

    return 0;
}

/**
 * C++特定优化：
 * 1. 使用vector容器存储并查集和网格数据
 * 2. 使用const引用和移动语义提高效率
 * 3. 定义printResult辅助函数，方便输出结果
 * 4. 使用unite命名替代union，避免与C++关键字冲突
 * 
 * 注意事项：
 * 1. 在C++中，union是关键字，所以使用unite作为合并函数名
 * 2. 使用const修饰不会修改的成员函数和参数
 * 3. 对于大规模数据，可以考虑使用更高效的内存布局和访问模式
 * 4. 可以添加更多的边界检查和错误处理
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(m * n)
 * - 构建初始连通分量：O(m * n * α(m * n))
 * - 逆向处理每次敲打：O(h * α(m * n))
 * - 总体时间复杂度：O((m * n + h) * α(m * n)) ≈ O(m * n + h)
 * 
 * 空间复杂度分析：
 * - 并查集：O(m * n)
 * - 网格副本：O(m * n)
 * - 结果数组：O(h)
 * - 总体空间复杂度：O(m * n + h)
 */